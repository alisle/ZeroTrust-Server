import {Component, OnInit, ViewChild} from '@angular/core';
import {LogWriter} from "../../log-writer";
import {AuthService} from "../../_services/auth/auth.service";
import {MatDialog, MatSnackBar} from "@angular/material";
import {AdminUsersAddDialog} from "../admin-users-add-dialog/admin-users-add.dialog";
import {AuthUserNewDTO} from "../../_model/AuthUserNewDTO";
import {AdminUsersTableComponent} from "../admin-users-table/admin-users-table.component";

@Component({
  selector: 'app-admin-users',
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.css']
})
export class AdminUsersComponent implements OnInit {
  private log: LogWriter = new LogWriter("admin-users-component");

  @ViewChild(AdminUsersTableComponent) table : AdminUsersTableComponent;

  constructor(public authService: AuthService, public dialog: MatDialog, private snackBar: MatSnackBar) { }

  ngOnInit() {
  }

  openDialog() {
    this.log.debug("adding user");

    const dialogRef = this.dialog.open(AdminUsersAddDialog, {
      width: '500px'
    });

    dialogRef.afterClosed().subscribe(result => {
      this.table.refresh();
      2
      this.log.debug("the add user dialog was closed", result);
      if( result == null) {
        this.log.debug("dialog was cancelled");
      } else {
        let newAuthUser : AuthUserNewDTO = result;
        this.log.debug("got new user to add", newAuthUser);
        // I need to squirt this over to the service now..

      }
    })
  }

}
