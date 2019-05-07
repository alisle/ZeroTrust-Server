import { Injectable } from '@angular/core';
import {PageableClient} from "../pageable-client";
import {Connection} from "../../_model/Connection";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../auth/auth.service";

@Injectable()
export class ConnectionsService  extends PageableClient<Connection> {
  constructor(http: HttpClient, auth: AuthService) {
    super("connections", "/connections", http, auth);
  }

}
