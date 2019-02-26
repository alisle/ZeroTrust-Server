import { Injectable } from '@angular/core';
import {PageableClient} from "../pageable-client";
import {Connection} from "../../_model/Connection";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class ConnectionsService  extends PageableClient<Connection> {
  constructor(http: HttpClient) {
    super("connections", "/connections", http);
  }

}
