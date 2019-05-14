import { Injectable } from '@angular/core';
import {LogWriter} from "../../log-writer";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {DefaultService} from "../default.service";
import {BehaviorSubject, Observable, ObservableInput, of} from "rxjs";
import {catchError, finalize, map} from "rxjs/operators";
import {Token} from "../../_model/token";
import {local} from "d3-selection";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private log : LogWriter = new LogWriter("auth-service");
  private loadingSubject : BehaviorSubject<boolean> = new BehaviorSubject(false);
  public loading$  = this.loadingSubject.asObservable();

  constructor(private http : HttpClient) {}

  isAuthenticated() : boolean {
    return !(localStorage.getItem("auth_token") === null);
  }

  private setAuthentication(token : Token) {
    localStorage.setItem("auth_token", token.access_token);
    localStorage.setItem("auth_expires", token.expires_in + "");
  }

  private removeAuthentication() {
    localStorage.removeItem("auth_token");
    localStorage.removeItem("auth_expires");
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
