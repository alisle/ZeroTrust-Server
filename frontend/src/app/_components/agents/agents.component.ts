import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {Agent} from "../../_model/Agent";
import {merge } from "rxjs";
import {AgentsService} from "../../_services/agents/agents.service";
import {tap} from "rxjs/operators";
import {MatPaginator, MatSort} from "@angular/material";
import {LogWriter} from "../../log-writer";
import {PageableDataSource} from "../../_services/pageable-data-source";

@Component({
  selector: 'app-agents',
  templateUrl: './agents.component.html',
  styleUrls: ['./agents.component.css']
})
export class AgentsComponent implements OnInit, AfterViewInit {
  private log : LogWriter = new LogWriter("agents.component");

  constructor(private service: AgentsService) { }
  dataSource = new PageableDataSource<Agent>(this.service);
  displayedColumns = ['name', 'alive', 'lastSeen', 'firstSeen', 'known', 'aliveConnectionCount', 'connectionCount' ];
  selectedRow : Agent = null;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort : MatSort;

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
    this.log.debug("selected agent:", row);
    this.selectedRow = row;

  }

}
