import { Component, OnInit } from '@angular/core';
import {ErrorStateMatcher, MatSnackBar} from "@angular/material";
import {FormControl, FormGroupDirective, NgForm, Validators} from "@angular/forms";
import {LogWriter} from "../../log-writer";
import {AuthService} from "../../_services/auth/auth.service";
import {ActivatedRouteSnapshot, Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  email = "";
  password = "";

  private log : LogWriter = new LogWriter("login-component");
  emailFormControl = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);

  passwordFormControl = new FormControl( '', [
    Validators.required
  ]);

  matcher = new MyErrorStateMatcher();
  loading$ = this.auth.loading$;

  constructor(private auth : AuthService, private snackBar: MatSnackBar, private router: Router ) { }

  ngOnInit() { }

  submit() {
    this.auth.authenticate(this.email, this.password).subscribe(
      (value : boolean) => {
        if(value == false) {
          if(this.auth.isAuthenticated()) {
            this.snackBar.open("Authentication Successful!", null, {
              duration: 2000,
            });

            this.router.navigate(['/connection_links']);

          } else {
            this.snackBar.open("Authentication Failed!", null, {
              duration: 2000,
            });
          }

        }
      });

  }

  enabled() : boolean {
    return !((this.email != "" && this.password != "") && !this.matcher.isErrorState(this.emailFormControl, null));
  }
}


export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}
