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
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
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
import {NetworksComponent} from './_components/networks/networks.component';
import {NetworksService} from "./_services/networks/networks.service";
import { NetworkTreeMapComponent } from './_components/network-tree-map/network-tree-map.component';
import {AuthGuardService} from "./_guards/auth-guard.service";
import {AuthService} from "./_services/auth/auth.service";
import { LoginComponent } from './_components/login/login.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ErrorInterceptor} from "./_helpers/error-interceptor/error.interceptor";
import {AuthInterceptor} from "./_helpers/auth-interceptor/auth.interceptor";
import { LogoutComponent } from './_components/logout/logout.component';
import { AdminComponent } from './_components/admin/admin.component';
import { NetworkAddDialog } from './_components/network-add-dialog/network-add.dialog';
import { AdminUsersTableComponent } from './_components/admin-users-table/admin-users-table.component';
import {AuthUsersService} from "./_services/auth-users/auth-users.service";
import { AdminUsersAddDialog } from './_components/admin-users-add-dialog/admin-users-add.dialog';
import { AdminUsersComponent } from './_components/admin-users/admin-users.component';

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
    NetworkAddDialog,
    LoginComponent,
    LogoutComponent,
    AdminComponent,
    NetworkAddDialog,
    AdminUsersTableComponent,
    AdminUsersComponent,
    AdminUsersAddDialog
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
    FormsModule,
  ],
  providers: [
    AgentsService,
    ConnectionsService,
    ConnectionLinkService,
    FlowGraphService,
    NetworksService,
    AuthGuardService,
    AuthService,
    AuthUsersService,
    // Interceptors
    { provide: HTTP_INTERCEPTORS, multi: true, useClass: ErrorInterceptor  },
    { provide: HTTP_INTERCEPTORS, multi: true, useClass: AuthInterceptor },
  ],
  entryComponents: [
    ConnectionLinksDetailsDialog,
    NetworkAddDialog,
    AdminUsersAddDialog

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
