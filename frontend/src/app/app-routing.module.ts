import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {WelcomeComponent} from "./_components/welcome/welcome.component";
import {DashboardComponent} from "./_components/dashboard/dashboard.component";
import {AgentsComponent} from "./_components/agents/agents.component";
import {AgentDetailsComponent} from "./_components/agent-details/agent-details.component";
import {ConnectionLinksComponent} from "./_components/connection-links/connection-links.component";
import {ConnectionLinkDetailsComponent} from "./_components/connection-links-details/connection-link-details.component";
import {NetworksComponent} from "./_components/networks/networks.component";
import {AuthGuardService} from "./_guards/auth-guard.service";
import {LoginComponent} from "./_components/login/login.component";
import {LogoutComponent} from "./_components/logout/logout.component";
import {AdminComponent} from "./_components/admin/admin.component";

const routes: Routes = [
  { path: '', component: DashboardComponent, canActivate: [ AuthGuardService ] },
  { path: 'agents', component: AgentsComponent, canActivate: [ AuthGuardService ]  },
  { path: 'agents/:id', component: AgentDetailsComponent, canActivate: [ AuthGuardService ]  },
  { path: 'connection_links', component: ConnectionLinksComponent, canActivate: [ AuthGuardService ]  },
  { path: 'connection_links/:id', component: ConnectionLinkDetailsComponent, canActivate: [ AuthGuardService ]  },
  { path: 'networks', component: NetworksComponent, canActivate: [ AuthGuardService ]  },
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LogoutComponent, canActivate: [ AuthGuardService ]  },
  { path: 'admin', component: AdminComponent, canActivate: [ AuthGuardService ]  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
