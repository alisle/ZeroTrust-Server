import { Injectable } from '@angular/core';
import {DefaultService} from "../default.service";
import {AuthUser} from "../../_model/AuthUser";
import {AuthService} from "../auth/auth.service";
import {HttpClient} from "@angular/common/http";
import {PageableClient} from "../pageable-client";

@Injectable()
export class AuthUsersService extends DefaultService<AuthUser> {

  constructor(http: HttpClient, auth: AuthService) {
    super('users', "/auth/users", http, auth);
  }

  allUsers() : PageableClient<AuthUser> {
    return this.default();
  }
}
