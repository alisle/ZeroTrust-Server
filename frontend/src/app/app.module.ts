import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MaterialModule } from "./material/material.module";
import { FlexLayoutModule } from "@angular/flex-layout";
import { WelcomeComponent } from './_components/welcome/welcome.component';
import { DashboardComponent } from './_components/dashboard/dashboard.component';
import { AgentsService} from "./_services/agents/agents.service";
import { AgentsComponent } from './_components/agents/agents.component';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {MomentModule} from "ngx-moment";
import { AgentDetailsComponent } from './_components/agent-details/agent-details.component';
import { ConnectionsComponent } from './_components/connections/connections.component';
import {ConnectionsService} from "./_services/connections/connections.service";
import {ConnectionlinkService} from "./_services/connectionlinks/connectionlink.service";
import {
  ConnectionlinksComponent,
  ConnectionLinksDetailsDialog
} from './_components/connectionlinks/connectionlinks.component';
import { ConnectionLinkDetailsComponent } from './_components/connectionlinks-details/connection-link-details.component';
import {FlowGraphService} from "./_services/flowgraph/FlowGraphService";

@NgModule({
  declarations: [
    AppComponent,
    WelcomeComponent,
    DashboardComponent,
    AgentsComponent,
    AgentDetailsComponent,
    ConnectionsComponent,
    ConnectionlinksComponent,
    ConnectionLinkDetailsComponent,
    ConnectionLinksDetailsDialog
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MaterialModule,
    AppRoutingModule,
    FlexLayoutModule,
    HttpClientModule,
    MomentModule,
  ],
  providers: [
    AgentsService,
    ConnectionsService,
    ConnectionlinkService,
    FlowGraphService,
  ],
  entryComponents: [
    ConnectionLinksDetailsDialog
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
