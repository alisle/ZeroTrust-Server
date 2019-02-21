import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {PageableService} from "../pageable.service";
import {ConnectionLink} from "../../_model/ConnectionLink";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Page} from "../../_model/page/page";
import {__param} from "tslib";
import {Connection} from "../../_model/Connection";
import {LogWriter} from "../../log-writer";
import {Log} from "@angular/core/testing/src/logger";

@Injectable()
export class ConnectionlinkService extends PageableService<ConnectionLink> {
  private log : LogWriter = new LogWriter("connection-link.service");
  constructor(http: HttpClient) {
    super("connection_links", "/connection_links", http);
  }

  connectionsBetweenSourceAgentandIP(sourceAgent: string, destinationAddress: string) : Observable<Page<ConnectionLink>> {
    this.log.debug(`requesting connections between ${sourceAgent} and ${destinationAddress}`);

    let params = new HttpParams()
      .append("source_agent_id", sourceAgent)
      .append("destination_address", destinationAddress);

    return this.connections("source-agent-id-destination-ip", params);
  }

  countConnectionsBetweenSourceAgentandIP(sourceAgent: string, destinationAddress: string) : Observable<number> {
    return this.counts(this.connectionsBetweenSourceAgentandIP(sourceAgent, destinationAddress));
  }

  connectionsBetweenIPandDestinationAgent(sourceAddress: string, destinationAgent: string) : Observable<Page<ConnectionLink>> {
    this.log.debug(`requesting connections between ${sourceAddress} and ${destinationAgent}`);

    let params = new HttpParams()
      .append("source_address", sourceAddress)
      .append("destination_agent_id", destinationAgent);

    return this.connections("source-agent-id-destination-ip", params);
  }

  countConnectionsBetweenIPandDestinationAgent(sourceAddress: string, destinationAgent: string) : Observable<number> {
    return this.counts(this.connectionsBetweenIPandDestinationAgent(sourceAddress, destinationAgent));
  }

  connectionsBetweenAgents(sourceAgent: string, destinationAgent: string) : Observable<Page<ConnectionLink>> {
    this.log.debug(`requesting connections between ${sourceAgent} and ${destinationAgent}`);

    let params = new HttpParams()
      .append("source_agent_id", sourceAgent)
      .append("destination_agent_id", destinationAgent);

    return this.connections("source-destination-agent-id", params);
  }

  countConnectionsBetweenAgents(sourceAgent: string, destinationAgent: string) : Observable<number> {
    return this.counts(this.connectionsBetweenAgents(sourceAgent, destinationAgent));
  }


  private connections(endpoint : string, params : HttpParams) : Observable<Page<ConnectionLink>> {
    params = params
      .append("size", "" + 1000)
      .append("page", "" + 0)
      .append("sort", `timestamp,ASC`);

    let url = `${this.base_url}${this.URL}/search/${endpoint}`;
    return this._page(0, params, url);

  }

  private counts(observable: Observable<Page<ConnectionLink>>) : Observable<number> {
    return observable
      .pipe(
        map((res: Page<ConnectionLink>) => {
          this.log.error("got page to get count", res);
          return res.page.totalElements;
        }
      ));
  }


}
