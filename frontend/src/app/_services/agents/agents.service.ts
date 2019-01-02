import { Injectable } from '@angular/core';
import { Agent } from "../../_model/Agent";
import {PageableService} from "../pageable.service";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class AgentsService  extends PageableService<Agent> {
  constructor(http: HttpClient) {
    super("agents", "/agents", http);
  }


}
