import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import {Observable} from "rxjs";
import {UserCount} from "../../_model/UserCount";
import {LogWriter} from "../../log-writer";
import {Log} from "@angular/core/testing/src/logger";
import {DataSource} from "@angular/cdk/table";
import {CollectionViewer} from "@angular/cdk/collections";
import {MatSort, MatTable, SortDirection} from "@angular/material";

@Component({
  selector: 'app-user-count-table',
  templateUrl: './user-count-table.component.html',
  styleUrls: ['./user-count-table.component.css']
})
export class UserCountTableComponent implements OnInit, AfterViewInit {
  private log : LogWriter = new LogWriter("user-count-table.component");
  constructor() { }

  @Input()
  counts : Observable<UserCount[]>;
  count : UserCount[];

  displayedColumns = [
    "user",
    "count"
  ];

  @ViewChild(MatSort) sort : MatSort;
  @ViewChild(MatTable) table: MatTable<UserCount>;

  ngOnInit() {
    this.counts.subscribe((count : UserCount[] ) => {
      this.count = count;
    });

  }

  ngAfterViewInit(): void {
    this.sort.sortChange.subscribe(() => {
      this.count = this.count.sort((first : UserCount, next : UserCount) : number => {
        switch(this.sort.active) {
          case "user": {
            switch(this.sort.direction) {
              case '': { }
              case 'asc': {
                this.log.debug("changing sort direction based on user, asc");
                if(first.user != null && next.user != null) {
                  return first.user.localeCompare(next.user);
                } else if( first.user == null && next.user != null ){
                  return 1;
                } else {
                  return -1;
                }

              } break;
              case 'desc' : {
                this.log.debug("changing sort direction based on user, desc");
                if(first.user != null && next.user != null) {
                  return first.user.localeCompare(next.user) * -1;
                } else if( first.user == null && next.user != null ){
                  return -1;
                } else {
                  return 1;
                }
              } break;
            }
          } break;
          case "count": {
            switch(this.sort.direction) {
              case '': { }
              case 'asc': {
                this.log.debug("changing sort direction based on count, asc");
                return first.count < next.count ? -1 : first.count > next.count ? 1 : 0;
              } break;
              case 'desc' : {
                this.log.debug("changing sort direction based on count, desc");
                return first.count > next.count ? -1 : first.count  < next.count ? 1 : 0;
              } break;
            }
          } break;
        }
      })
      this.log.debug("usercount has changed to ", this.count);
      this.table.renderRows();
    });
  }



}
