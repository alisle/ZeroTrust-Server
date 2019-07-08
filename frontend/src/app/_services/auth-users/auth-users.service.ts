import { Injectable } from '@angular/core';
import {DefaultService} from "../default.service";
import {AuthUser} from "../../_model/AuthUser";
import {AuthService} from "../auth/auth.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {PageableClient} from "../pageable-client";
import {Role} from "../../_model/Role";
import {AuthUserNewDTO} from "../../_model/AuthUserNewDTO";
import {Observable} from "rxjs";
import {EmailPassword} from "../../_model/EmailPassword";
import {map} from "rxjs/operators";

@Injectable()
export class AuthUsersService extends DefaultService<AuthUser> {

  private roles : Role[] =  [
    new Role("Admin", "admin"),
    new Role("Agents Read", "agents_read"),
    new Role("Agents Write", "agents_write"),
    new Role("Connections Read", "connections_read"),
    new Role("Connections Write", "connections_write"),
    new Role("Networks Read", "networks_read"),
    new Role("Networks Write", "networks_write")
  ];


  constructor(http: HttpClient, auth: AuthService) {
    super('users', "/auth/users", http, auth);
  }

  allUsers() : PageableClient<AuthUser> {
    return this.default();
  }

  allRoles() : Role[] {
    return this.roles;
  }

  add(dto : AuthUserNewDTO) : Observable<EmailPassword> {
    let url = `${DefaultService.base_url}${this.URL}`;
    return this.http.post(
      url,
      dto,
      {
        headers: new HttpHeaders( { 'Content-Type' : 'application/json'} )
      }
    ).pipe(
      map((obj : any) => {
        return obj as EmailPassword;
    })
    );
  }
}
