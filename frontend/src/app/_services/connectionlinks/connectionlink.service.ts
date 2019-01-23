import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageableService} from "../pageable.service";
import {ConnectionLink} from "../../_model/ConnectionLink";

@Injectable()
export class ConnectionlinkService extends PageableService<ConnectionLink> {
  constructor(http: HttpClient) {
    super("connection_links", "/connection_links", http);
  }
}
