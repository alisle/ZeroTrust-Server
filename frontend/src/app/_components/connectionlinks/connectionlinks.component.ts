import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {LogWriter} from "../../log-writer";
import {ConnectionlinkService} from "../../_services/connectionlinks/connectionlink.service";
import {PageableDataSource} from "../../_services/pageable-data-source";
import {ConnectionLink} from "../../_model/ConnectionLink";
import {MatPaginator, MatSort} from "@angular/material";
import {merge} from "rxjs";
import {tap} from "rxjs/operators";

@Component({
  selector: 'app-connectionlinks',
  templateUrl: './connectionlinks.component.html',
  styleUrls: ['./connectionlinks.component.css']
})
export class ConnectionlinksComponent implements OnInit, AfterViewInit {
  private log : LogWriter = new LogWriter("connectlinks.component");
  constructor(private service: ConnectionlinkService) { }

  dataSource = new PageableDataSource<ConnectionLink>(this.service);
  displayedColumns = [ 'alive', 'timestamp', 'sourceAgentName', 'sourceProcessName', 'destinationAgentName', 'destinationProcessName'];
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

  }
}
