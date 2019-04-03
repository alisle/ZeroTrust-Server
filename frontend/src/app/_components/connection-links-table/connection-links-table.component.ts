import {AfterViewInit, Component, Inject, Input, OnInit, ViewChild} from '@angular/core';
import {LogWriter} from "../../log-writer";
import {ConnectionLink} from "../../_model/ConnectionLink";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef, MatPaginator, MatSort} from "@angular/material";
import {PageableDataSource} from "../../_services/pageable-data-source";
import {merge} from "rxjs";
import {tap} from "rxjs/operators";

@Component({
  selector: 'app-connection-links-table',
  templateUrl: './connection-links-table.component.html',
  styleUrls: ['./connection-links-table.component.css']
})
export class ConnectionLinksTableComponent implements OnInit, AfterViewInit {
  private log : LogWriter = new LogWriter("connectlinks.component");

  constructor(public dialog: MatDialog) { }

  @Input()
  dataSource: PageableDataSource<ConnectionLink>;

  @Input()
  displayedColumns = [
    'alive',
    'duration',
    'timestamp',
    'ended',
    'sourceAgentName',
    'sourceNetworkName',
    'sourceString',
    'sourcePort',
    'sourceProcessName',
    'destinationAgentName',
    'destinationNetworkName',
    'destinationString',
    'destinationPort',
    'destinationProcessName'
  ];

  selectedRow : ConnectionLink = null;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  ngOnInit() {
    this.dataSource.length$.subscribe(size => {
      this.log.debug(`setting total elements to ${size}`);
      this.paginator.length = size
    });
    this.dataSource.pageSize$.subscribe( size => {
      this.log.debug(`setting size of page to ${size}`);
      this.paginator.pageSize = size
    });

    this.log.debug(`requesting first page of agents`);
    this.dataSource.get(0);
  }

  ngAfterViewInit(): void {
    merge(this.paginator.page, this.sort.sortChange)
      .pipe(
        tap( () => {
          this.dataSource.sortDirection(this.sort.direction);
          this.dataSource.sortOn(this.sort.active);
          this.dataSource.pageSize(this.paginator.pageSize);
          this.dataSource.get(this.paginator.pageIndex);
        })
      ).subscribe();
  }

  selectRow(row) : void {
    this.log.debug("selected connectionlink:", row);
    this.selectedRow = row;
    this.openDialog(this.selectedRow);

  }

  openDialog(link : ConnectionLink): void {
    this.log.debug("opening details dialog");

    const dialogRef = this.dialog.open(ConnectionLinksDetailsDialog, {
      width: '95%',
      data: link.uuid
    });

    dialogRef.afterClosed().subscribe(result => {
      this.log.debug("the dialog was closed");
    })
  }
}

@Component({
  selector: 'connection-link-details-dialog',
  template: '<div mat-dialog-content><app-connection-link-details [connectionID]="id"></app-connection-link-details></div>',
})
export class ConnectionLinksDetailsDialog {
  private log : LogWriter = new LogWriter("connectlinks-details-dialog");
  id : string;
  constructor(
    public dialogRef: MatDialogRef<ConnectionLinksDetailsDialog>,
    @Inject(MAT_DIALOG_DATA) public data: string) {
    this.id = data;
    this.log.debug(`the data package looks like ${data}`);

  }



}
