import { Injectable } from '@angular/core';
import {DefaultService} from "../default.service";
import {Network} from "../../_model/Network";
import {HttpClient, HttpParams} from "@angular/common/http";
import {PageableClient} from "../pageable-client";
import {Observable} from "rxjs";
import {AgentCount} from "../../_model/AgentCount";
import {map} from "rxjs/operators";

@Injectable()
export class NetworksService extends DefaultService<Network> {
  constructor(http: HttpClient) {
    super("networks", "/networks", http);
  }

  allNetworks() : PageableClient<Network> {
    return this.default();
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
