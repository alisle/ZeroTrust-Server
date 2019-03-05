import {AfterViewInit, Component, Inject, Input, OnInit, ViewChild} from '@angular/core';
import {LogWriter} from "../../log-writer";
import {ConnectionLinkService} from "../../_services/connection-links/connection-link.service";
import {PageableDataSource} from "../../_services/pageable-data-source";
import {ConnectionLink} from "../../_model/ConnectionLink";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef, MatPaginator, MatSort} from "@angular/material";
import {merge} from "rxjs";
import {tap} from "rxjs/operators";
import {PageableClient} from "../../_services/pageable-client";
import {LoadableObject} from "../../_model/LoadableObject";

@Component({
  selector: 'app-connectionlinks',
  templateUrl: './connection-links.component.html',
  styleUrls: ['./connection-links.component.css']
})

export class ConnectionLinksComponent implements OnInit{
  private pageableClient : PageableClient<ConnectionLink>  = this.service.allConnectionLinks();

  totalConnections : LoadableObject<number> = new LoadableObject(true);
  totalAliveConnections : LoadableObject<number> = new LoadableObject(true);


  constructor(private service: ConnectionLinkService, public dialog: MatDialog) {}
  dataSource = new PageableDataSource<ConnectionLink>(this.pageableClient);

  ngOnInit() {
    this.totalConnections.bind(this.service.totalConnectionLinks());
    this.totalAliveConnections.bind(this.service.totalAlive());
  }
}


