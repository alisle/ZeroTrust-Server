import {AfterViewChecked, AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {LogWriter} from "../../log-writer";
import {AuthUsersService} from "../../_services/auth-users/auth-users.service";
import {Router} from "@angular/router";
import {PageableDataSource} from "../../_services/pageable-data-source";
import {AuthUser} from "../../_model/AuthUser";
import {MatPaginator, MatSort} from "@angular/material";
import {merge} from 'rxjs';
import {tap} from "rxjs/operators";
import {ConnectionLink} from "../../_model/ConnectionLink";

@Component({
  selector: 'app-admin-users-table',
  templateUrl: './admin-users-table.component.html',
  styleUrls: ['./admin-users-table.component.css']
})
export class AdminUsersTableComponent implements OnInit, AfterViewInit {
  private log : LogWriter = new LogWriter("admin-users-table.component");

  constructor(private service : AuthUsersService) { }
  dataSource = new PageableDataSource<AuthUser>(this.service.allUsers());

  displayColumns = [
    "email",
    "expired",
    "locked",
    "credentialsExpired",
    "enabled",
  ];

  selectedRow : AuthUser = null;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort : MatSort;


  ngOnInit() {
    this.dataSource.sortOn("email");
    this.dataSource.length$.subscribe(size => {
      this.paginator.length = size
    });
    this.dataSource.pageSize$.subscribe( size => {
      this.paginator.pageSize = size
    });
    this.dataSource.get(0);
  }


  ngAfterViewInit(): void {
    merge(this.paginator.page, this.sort.sortChange)
      .pipe(
        tap(() => {
          this.dataSource.sortDirection(this.sort.direction);
          this.dataSource.sortOn(this.sort.active);
          this.dataSource.pageSize(this.paginator.pageSize);
          this.dataSource.get(this.paginator.pageIndex);
        })
      ).subscribe();
  }

  selectRow(row : AuthUser) : void {
    this.log.debug("selected user:", row);
    this.selectedRow = row;
  }
}
