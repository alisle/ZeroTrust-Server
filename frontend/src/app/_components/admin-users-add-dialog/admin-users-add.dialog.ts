import {Component, Inject, OnInit} from '@angular/core';
import {ErrorStateMatcher, MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {FormControl, FormGroupDirective, NgForm, Validators} from "@angular/forms";
import {AuthUserNewDTO} from "../../_model/AuthUserNewDTO";
import {AuthService} from "../../_services/auth/auth.service";
import {AuthUsersService} from "../../_services/auth-users/auth-users.service";
import {LogWriter} from "../../log-writer";
import {LoadableObject} from "../../_model/LoadableObject";
import {EmailPassword} from "../../_model/EmailPassword";

@Component({
  selector: 'app-admin-users-add-dialog',
  templateUrl: './admin-users-add.dialog.html',
  styleUrls: ['./admin-users-add.dialog.css']
})
export class AdminUsersAddDialog implements OnInit {
  private log : LogWriter = new LogWriter("admin-users-add.dialog");
  private returnedObject : LoadableObject<EmailPassword> = new LoadableObject(false);
  private hasSubmitted : boolean = false;

  emailFormControl = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);

  dto = new AuthUserNewDTO();
  matcher = new MyErrorStateMatcher();
  emailPassword : EmailPassword;

  constructor(
    public dialogRef: MatDialogRef<AdminUsersAddDialog>,
    private service : AuthUsersService
  ) { }

  roles = this.service.allRoles();
  checked : { [role : string]: boolean } = {};

  ngOnInit() {
    this.roles.forEach(role => {
      this.checked[role.name] = false;
    });
  }

  cancel() : void {
    this.dialogRef.close();
  }

  isDisabled() : boolean {
    if(!this.emailFormControl.hasError('email') && !this.emailFormControl.hasError('required')) {
      for(let role in this.checked) {
        if (this.checked[role] == true) {
          return false;
        }
      }
    }
    return true;
  }

  submit() : void {
    let newRole : string[] = [];
    for(let role in this.checked) {
      if(this.checked[role] == true) {
        newRole.push(role);
      }
    }

    this.dto.roles = newRole;
    this.log.debug("I have the following dto", this.dto);
    this.hasSubmitted = true;
    this.returnedObject.bind(this.service.add(this.dto));

    this.returnedObject.value$.subscribe((tuple : EmailPassword) => {
      this.emailPassword = tuple;
    });

  }
}

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}
