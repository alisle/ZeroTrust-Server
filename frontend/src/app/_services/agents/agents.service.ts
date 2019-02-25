import { Injectable } from '@angular/core';
import { Agent } from "../../_model/Agent";
import {PageableService} from "../pageable.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable, pipe} from "rxjs";
import {map} from "rxjs/operators";
import {UserCount} from "../../_model/UserCount";

@Injectable()
export class AgentsService  extends PageableService<Agent> {
  constructor(http: HttpClient) {
    super("agents", "/agents", http);
  }

  countUsersSource(sourceAgent: string) : Observable<UserCount[]> {
    let params : HttpParams = new HttpParams()
      .append("agent-id", sourceAgent);

    return this.count("users-source", params);
  }

  countUsersDestination(destinationAgent: string) : Observable<UserCount[]> {
    let params : HttpParams = new HttpParams()
      .append("agent-id", destinationAgent);

    return this.count("users-destination", params);
  }

  countUsersTotal(agent: string) : Observable<UserCount[]> {
    let params : HttpParams = new HttpParams()
      .append("agent-id", agent);

    return this.count("users", params);
  }


  private count(endpoint : string, params : HttpParams) : Observable<UserCount[]> {
    let url = `${this.base_url}${this.URL}/search/${endpoint}`;

    return this.http.get(
      url,
      {
        params: params
      }
    ).pipe(
      map((res: Object) => {
        return res as UserCount[];
      })
    );


  }




}
