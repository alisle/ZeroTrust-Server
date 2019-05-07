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
import { HttpClientModule} from "@angular/common/http";
import {MomentModule} from "ngx-moment";
import { AgentDetailsComponent } from './_components/agent-details/agent-details.component';
import {ConnectionsService} from "./_services/connections/connections.service";
import {ConnectionLinkService} from "./_services/connection-links/connection-link.service";
import {ConnectionLinksComponent } from './_components/connection-links/connection-links.component';
import { ConnectionLinkDetailsComponent } from './_components/connection-links-details/connection-link-details.component';
import {FlowGraphService} from "./_services/flowgraph/FlowGraphService";
import { UserCountTableComponent } from './_components/user-count-table/user-count-table.component';
import { ProcessCountTableComponent } from './_components/process-count-table/process-count-table.component';
import {
  ConnectionLinksDetailsDialog,
  ConnectionLinksTableComponent
} from './_components/connection-links-table/connection-links-table.component';
import {NgxChartsModule} from "@swimlane/ngx-charts";
import {NgxGraphModule} from "@swimlane/ngx-graph";
import { ActiveConnectionsGraphComponent } from './_components/active-connections-graph/active-connections-graph.component';
import { ConnectionLinkGraphComponent } from './_components/connection-link-graph/connection-link-graph.component';
import {AgentOutboundConnectionsGraphComponent} from "./_components/agent-outbound-connections-graph/agent-outbound-connections-graph.component";
import { NetworksComponent } from './_components/networks/networks.component';
import {NetworksService} from "./_services/networks/networks.service";
import { NetworkTreeMapComponent } from './_components/network-tree-map/network-tree-map.component';
import {AuthGuardService} from "./_guards/auth-guard.service";
import {AuthService} from "./_services/auth/auth.service";
import { LoginComponent } from './_components/login/login.component';
import {ReactiveFormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    WelcomeComponent,
    DashboardComponent,
    AgentsComponent,
    AgentDetailsComponent,
    ConnectionLinksComponent,
    ConnectionLinkDetailsComponent,
    ConnectionLinksDetailsDialog,
    UserCountTableComponent,
    ProcessCountTableComponent,
    ConnectionLinksTableComponent,
    ActiveConnectionsGraphComponent,
    ConnectionLinkGraphComponent,
    AgentOutboundConnectionsGraphComponent,
    NetworksComponent,
    NetworkTreeMapComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MaterialModule,
    AppRoutingModule,
    FlexLayoutModule,
    HttpClientModule,
    MomentModule,
    NgxChartsModule,
    NgxGraphModule,
    ReactiveFormsModule,
  ],
  providers: [
    AgentsService,
    ConnectionsService,
    ConnectionLinkService,
    FlowGraphService,
    NetworksService,
    AuthGuardService,
    AuthService,
  ],
  entryComponents: [
    ConnectionLinksDetailsDialog
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
