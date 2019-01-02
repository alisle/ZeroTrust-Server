import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MaterialModule } from "./material/material.module";
import { FlexLayoutModule } from "@angular/flex-layout";
import { WelcomeComponent } from './welcome/welcome.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import {DataService} from "./_services/data/data.service";
import {AgentsService} from "./_services/agents/agents.service";
import { AgentsComponent } from './agents/agents.component';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {MomentModule} from "ngx-moment";

@NgModule({
  declarations: [
    AppComponent,
    WelcomeComponent,
    DashboardComponent,
    AgentsComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MaterialModule,
    AppRoutingModule,
    FlexLayoutModule,
    HttpClientModule,
    MomentModule
  ],
  providers: [
    DataService,
    AgentsService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
