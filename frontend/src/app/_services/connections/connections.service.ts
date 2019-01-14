import { Injectable } from '@angular/core';
import {PageableService} from "../pageable.service";
import {Connection} from "../../_model/Connection";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class ConnectionsService  extends PageableService<Connection> {
  constructor(http: HttpClient) {
    super("connections", "/connections", http);
  }

}
