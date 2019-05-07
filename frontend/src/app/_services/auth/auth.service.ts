import { Injectable } from '@angular/core';
import {LogWriter} from "../../log-writer";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {DefaultService} from "../default.service";
import {BehaviorSubject, Observable, ObservableInput, of} from "rxjs";
import {catchError, finalize, map} from "rxjs/operators";
import {Token} from "../../_model/token";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private log : LogWriter = new LogWriter("auth-service");
  public token : Token = null;
  private authenticated : boolean = false;

  private loadingSubject : BehaviorSubject<boolean> = new BehaviorSubject(false);
  public loading$  = this.loadingSubject.asObservable();

  constructor(private http : HttpClient) {}

  isAuthenticated() : boolean {
    return this.authenticated;
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
        this.authenticated = false;
        return of(null);
      }),
      finalize(() => {
        this.loadingSubject.next(false);
      })
    ).subscribe((token : Token) => {
      if(token != null) {
        this.log.debug("logged in");
        this.token = token;
        this.authenticated = true;
      }
    });

    return this.loadingSubject.asObservable();
  }

  getBearerToken() {
    return `Bearer ${this.token.access_token}`;
  }

  getHeaders() : HttpHeaders {
    return new HttpHeaders().append("Authorization", this.getBearerToken());
  }
}
