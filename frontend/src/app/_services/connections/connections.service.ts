import { Injectable } from '@angular/core';
import {PageableService} from "../pageable.service";

@Injectable()
export class ConnectionsService  extends PageableService<Connection> {

  constructor() { }
}
