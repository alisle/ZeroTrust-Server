import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {DataSource} from "@angular/cdk/table";
import {Connection} from "../../_model/Connection";
import {BehaviorSubject, merge} from "rxjs";
import {Agent} from "../../_model/Agent";
import {AgentsService} from "../../_services/agents/agents.service";
import {LogWriter} from "../../log-writer";
import {ConnectionsService} from "../../_services/connections/connections.service";
import {PageableDataSource} from 'src/app/_services/pageable-data-source';
import {MatPaginator, MatSort} from "@angular/material";
import {tap} from "rxjs/operators";

@Component({
  selector: 'app-connections',
  templateUrl: './connections.component.html',
  styleUrls: ['./connections.component.css']
})
export class ConnectionsComponent implements OnInit, AfterViewInit {
  private log : LogWriter = new LogWriter("connections.component");
  constructor(private service: ConnectionsService) { }

  dataSource = new PageableDataSource<Connection>(this.service);
  displayedColumns = ['start', 'end', 'duration', 'protocol', 'source', 'sourcePort', 'destination', 'destinationPort' , 'username', 'processName' ];
  selectedRow : Connection = null;

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
    this.log.debug("selected connection:", row);
    this.selectedRow = row;

  }

}
