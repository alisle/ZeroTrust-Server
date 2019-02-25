import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import {LogWriter} from "../../log-writer";
import {Observable} from "rxjs";
import {ProcessCount} from "../../_model/ProcessCount";
import {MatSort, MatTable} from "@angular/material";
import {UserCount} from "../../_model/UserCount";

@Component({
  selector: 'app-process-count-table',
  templateUrl: './process-count-table.component.html',
  styleUrls: ['./process-count-table.component.css']
})
export class ProcessCountTableComponent implements OnInit, AfterViewInit {
  private log : LogWriter = new LogWriter("process-count-table.component");
  constructor() { }

  @Input()
  counts: Observable<ProcessCount[]>;
  count: ProcessCount[];

  displayedColumns = [
    "process",
    "count"
  ];

  @ViewChild(MatSort) sort : MatSort;
  @ViewChild(MatTable) table: MatTable<ProcessCount>;

  ngOnInit() {
    this.counts.subscribe((count : ProcessCount[] ) => {
      this.count = count;
    });
  }


  ngAfterViewInit(): void {
    this.sort.sortChange.subscribe(() => {
      this.count = this.count.sort((first : ProcessCount, next : ProcessCount) : number => {
        switch(this.sort.active) {
          case "user": {
            switch(this.sort.direction) {
              case '': { }
              case 'asc': {
                this.log.debug("changing sort direction based on process, asc");
                if(first.process != null && next.process != null) {
                  return first.process.localeCompare(next.process);
                } else if( first.process == null && next.process != null ){
                  return 1;
                } else {
                  return -1;
                }

              } break;
              case 'desc' : {
                this.log.debug("changing sort direction based on process, desc");
                if(first.process != null && next.process != null) {
                  return first.process.localeCompare(next.process) * -1;
                } else if( first.process == null && next.process != null ){
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
      });
      this.table.renderRows();
    });
  }



}
