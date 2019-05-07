import { Injectable } from '@angular/core';
import { Agent } from "../../_model/Agent";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable, pipe} from "rxjs";
import {map} from "rxjs/operators";
import {UserCount} from "../../_model/UserCount";
import {ProcessCount} from "../../_model/ProcessCount";
import {DefaultService} from "../default.service";
import {PageableClient} from "../pageable-client";
import {AgentCount} from "../../_model/AgentCount";
import {AuthService} from "../auth/auth.service";

@Injectable()
export class AgentsService  extends DefaultService<Agent> {
  constructor(http: HttpClient, auth: AuthService) {
    super("agents", "/agents", http, auth);
  }

  allAgents() : PageableClient<Agent> {
    return this.default();
  }

  countUsersSource(sourceAgent: string) : Observable<UserCount[]> {
    return this.usersCount("users-source", sourceAgent);
  }

  countUsersDestination(destinationAgent: string) : Observable<UserCount[]> {
    return this.usersCount("users-destination", destinationAgent);
  }

  countUsersTotal(agent: string) : Observable<UserCount[]> {
    return this.usersCount("users", agent);
  }


  countProcessSource(sourceAgent: string) : Observable<ProcessCount[]> {
    return this.processCount("processes-source", sourceAgent);
  }

  countProcessDestination(destinationAgent: string) : Observable<ProcessCount[]> {
    return this.processCount("processes-destination", destinationAgent);
  }

  countIncomingConnections() : Observable<AgentCount[]> {
    return this.count("count-incoming-connections").pipe(
      map( (res: Object) : AgentCount[] => {
        return res as AgentCount[];
      })
    );
  }

  countOutgoingConnections() : Observable<AgentCount[]> {
    return this.count("count-outgoing-connections").pipe(
      map( (res: Object) : AgentCount[] => {
        return res as AgentCount[];
      })
    );
  }


  totalAgents() : Observable<number> {
    return this.count("total-agents").pipe(
      map((res: Object) : number => {
        return res as number;
      })
    );
  }

  totalAlive() : Observable<number> {
    return this.count("total-alive-agents").pipe(
      map((res: Object) : number => {
        return res as number;
      })
    );
  }




  private usersCount(endpoint : string, agent: string) : Observable<UserCount[]> {
    return this.count(endpoint, "agent_id", agent).pipe(
      map( (res: Object) : UserCount[]  => {
        return res as UserCount[];
      })
    );
  }

  private processCount(endpoint: string, agent: string) : Observable<ProcessCount[]> {
    return this.count(endpoint, "agent_id", agent).pipe(
      map((res: Object) : ProcessCount[] => {
        return res as ProcessCount[];
      })
    )
  }




}
