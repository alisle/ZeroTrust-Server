import { Component, OnInit } from '@angular/core';
import {LogWriter} from "../../log-writer";
import {ActivatedRoute} from "@angular/router";
import {ConnectionlinkService} from "../../_services/connectionlinks/connectionlink.service";
import {ConnectionLink} from "../../_model/ConnectionLink";
import {catchError, finalize} from "rxjs/operators";
import {of} from "rxjs";

@Component({
  selector: 'app-connection-link-details',
  templateUrl: './connection-link-details.component.html',
  styleUrls: ['./connection-link-details.component.css']
})
export class ConnectionLinkDetailsComponent implements OnInit {
  private log: LogWriter = new LogWriter("connectionlinks-details-component");
  constructor(private route: ActivatedRoute, private service: ConnectionlinkService) { }

  loading : boolean = true;
  error: boolean = false;
  connectionLink : ConnectionLink = null;

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.service.get(id, "DefaultConnectionLinkProjection")
      .pipe(
        catchError(() => {
          this.log.error("unable to find connection link!");
          this.error = true;
          return of(null);
        }),
        finalize(() => {
          this.loading = false;
        })
      ).subscribe(link => {
        console.log("got connection link:", link);
        this.connectionLink = link;
    })
  }

}
