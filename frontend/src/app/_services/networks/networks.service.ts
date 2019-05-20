import { Injectable } from '@angular/core';
import {DefaultService} from "../default.service";
import {Network} from "../../_model/Network";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {PageableClient} from "../pageable-client";
import {Observable} from "rxjs";
import {AgentCount} from "../../_model/AgentCount";
import {catchError, map} from "rxjs/operators";
import {AuthService} from "../auth/auth.service";
import {NetworkNewDTO} from "../../_model/NetworkNewDTO";

@Injectable()
export class NetworksService extends DefaultService<Network> {
  constructor(http: HttpClient, auth: AuthService) {
    super("networks", "/networks", http, auth);
  }

  allNetworks() : PageableClient<Network> {
    return this.default();
  }

  add(network : NetworkNewDTO) : Observable<boolean> {
    let url = `${DefaultService.base_url}${this.URL}`;
    return this.http.post(
      url,
      network,
      {
        headers : new HttpHeaders({'Content-Type' : 'application/json'})
      }
    ).pipe(
      map(() => {
        return true;
      })
    );
  }

  countActiveSourceConnections(network : string) : Observable<AgentCount[]> {
    return this.count("count-active-source-connections", "network_id", network).pipe(
      map((res: Object) : AgentCount[] => {
        return res as AgentCount[];
      })
    );
  }

  countActiveDestinationConnections(network : string) : Observable<AgentCount[]> {
    return this.count("count-active-destination-connections", "network_id", network).pipe(
      map((res: Object) : AgentCount[] => {
        return res as AgentCount[];
      })
    );
  }
}
