import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageableClient} from "../pageable-client";
import {ConnectionLink} from "../../_model/ConnectionLink";
import {LogWriter} from "../../log-writer";
import {DefaultService} from "../default.service";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Injectable()
export class ConnectionLinkService extends DefaultService<ConnectionLink>  {
  private log : LogWriter = new LogWriter("connection-link.service");

  constructor(http: HttpClient) {
    super("connection_links", "/connection_links", http);
  }

  allConnectionLinks() : PageableClient<ConnectionLink> {
    this.log.debug("creating new pageable client for all connection links");
    return this.default();
  }

  agentConnections(agent: string): PageableClient<ConnectionLink> {
    this.log.debug(`requesting all connections for ${agent}`);
    let client = this.search("agent-connections");
    client.addParam("agent-id", agent);

    return client;
  }

  activeSourceConnections(sourceAgent: string) : PageableClient<ConnectionLink> {
    this.log.debug(`requesting active connections with source ${sourceAgent}`);
    let client = this.search("active-source-agent-id");
    client.addParam("source_agent_id", sourceAgent);

    return client;
  }

  activeDestinationConnections(destinationAgent: string) : PageableClient<ConnectionLink> {
    this.log.debug(`requesting active connections with destination ${destinationAgent}`);
    let client = this.search("active-destination-agent-id");
    client.addParam("destination_agent_id", destinationAgent);
    return client;
  }

  connectionsBetweenSourceAgentandIP(sourceAgent: string, destinationAddress : string) : PageableClient<ConnectionLink> {
    this.log.debug(`requesting connections between ${sourceAgent} and ${destinationAddress}`);
    let client = this.search("source-agent-id-destination-ip");
    client.addParam("source_agent_id", sourceAgent);
    client.addParam("destination_address", destinationAddress);

    client.pageSize = 1000;
    client.sortOn =  "timestamp";
    client.sortDirection = "ASC";

    return client;
  }

  connectionsBetweenIPandDestinationAgent(sourceAddress: string, destinationAgent: string) : PageableClient<ConnectionLink> {
    this.log.debug(`requesting connections between ${sourceAddress} and ${destinationAgent}`);
    let client = this.search("source-ip-destination-agent-id");
    client.addParam("source_address", sourceAddress);
    client.addParam("destination_agent_id", destinationAgent);

    client.pageSize = 1000;
    client.sortOn =  "timestamp";
    client.sortDirection = "ASC";

    return client;
  }

  connectionsBetweenAgents(sourceAgent: string, destinationAgent: string) : PageableClient<ConnectionLink> {
    this.log.debug(`requesting connections between ${sourceAgent} and ${destinationAgent}`);
    let client = this.search("source-destination-agent-id");
    client.addParam("source_agent_id", sourceAgent);
    client.addParam("destination_agent_id", destinationAgent);

    client.pageSize = 1000;
    client.sortOn =  "timestamp";
    client.sortDirection = "ASC";

    return client;

  }

  aliveConnections() : PageableClient<ConnectionLink> {
    this.log.debug(`requesting all live connections`);
    let client = this.search("active");
    client.pageSize = 1000;
    client.sortOn = "timestamp";
    client.sortDirection = "ASC";

    return client;
  }

  private count(endpoint: string) : Observable<Object> {
    let url = `${this.base_url}${this.URL}/search/${endpoint}`;

    return this.http.get(url);
  }

  totalAlive() : Observable<number> {
    return this.count("total-alive-connection-links").pipe(
      map((res: Object) : number => {
        return res as number;
      })
    );
  }

  totalConnectionLinks() : Observable<number> {
    return this.count("total-connection-links").pipe(
      map((res: Object) : number => {
        return res as number;
      })
    )
  }

}
