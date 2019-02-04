import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {PageableService} from "../pageable.service";
import {ConnectionLink} from "../../_model/ConnectionLink";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Injectable()
export class ConnectionlinkService extends PageableService<ConnectionLink> {
  constructor(http: HttpClient) {
    super("connection_links", "/connection_links", http);
  }

  countConnectionsBetween(sourceAgent: string, destinationAgent: string) : Observable<number> {
    console.log(`requesting number of connections between ${sourceAgent} and ${destinationAgent}`);

    let params = new HttpParams()
      .append("source_agent_id", sourceAgent)
      .append( "destination_agent_id", destinationAgent);

    return this.http.get(
      `${this.base_url}${this.URL}/search/count-source-destination-agent-id`,
      {
        params: params
      })
      .pipe(
        map((res: number) => {
          this.log.debug(`received number: ${res}`);
          return res;
        }));
  }
}
