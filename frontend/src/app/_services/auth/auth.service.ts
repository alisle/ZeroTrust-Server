import { Injectable } from '@angular/core';
import {LogWriter} from "../../log-writer";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {DefaultService} from "../default.service";
import {BehaviorSubject, Observable, ObservableInput, of} from "rxjs";
import {catchError, finalize, map} from "rxjs/operators";
import {Token} from "../../_model/token";
import {JwtHelperService} from "@auth0/angular-jwt";
import {TokenPayload} from "../../_model/tokenPayload";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private log : LogWriter = new LogWriter("auth-service");
  private loadingSubject : BehaviorSubject<boolean> = new BehaviorSubject(false);
  public loading$  = this.loadingSubject.asObservable();
  private jwtHelper : JwtHelperService = new JwtHelperService();

  constructor(private http : HttpClient) {}

  isAuthenticated() : boolean {
    let token = localStorage.getItem("auth_token");

    if(token === null) {
      return false;
    } else {
      let expired =this.jwtHelper.isTokenExpired(token);
      if(expired) {
        this.removeAuthentication();
        return false;
      } else {
        return true;
      }
    }
  }

  hasAuthority(authority : string) : boolean {
    let token = localStorage.getItem("auth_token");
    if(token == null) {
      return false;
    } else {
      let payload =  this.jwtHelper.decodeToken(token) as TokenPayload;
      return payload.authorities.includes(authority);
    }
  }


  private setAuthentication(token : Token) {
    localStorage.setItem("auth_token", token.access_token);
  }

  private removeAuthentication() {
    localStorage.removeItem("auth_token");
  }


  authenticate(email: string, password: string) : Observable<boolean> {
    let packet = { 'email' : email, 'password' : password };

    let url = `${DefaultService.base_url}/auth`;
    this.loadingSubject.next(true);
    this.http.post(
      url,
      packet,
      {
        headers : new HttpHeaders({'Content-Type': 'application/json' })
      }
    ).pipe(
      catchError(() => {
        this.log.error("unable to login");
        this.removeAuthentication();
        return of(null);
      }),
      finalize(() => {
        this.loadingSubject.next(false);
      })
    ).subscribe((token : Token) => {
      if(token != null) {
        this.log.debug("logged in");
        this.setAuthentication(token);
      }
    });

    return this.loadingSubject.asObservable();
  }

  getBearerToken() {
    let token = localStorage.getItem("auth_token");
    return `Bearer ${token}`;
  }

  logout() {
    this.removeAuthentication();
  }
}
