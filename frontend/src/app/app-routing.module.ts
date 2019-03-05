import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {WelcomeComponent} from "./_components/welcome/welcome.component";
import {DashboardComponent} from "./_components/dashboard/dashboard.component";
import {AgentsComponent} from "./_components/agents/agents.component";
import {AgentDetailsComponent} from "./_components/agent-details/agent-details.component";
import {ConnectionLinksComponent} from "./_components/connection-links/connection-links.component";
import {ConnectionLinkDetailsComponent} from "./_components/connectionlinks-details/connection-link-details.component";

const routes: Routes = [
  { path: '', component: WelcomeComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'agents', component: AgentsComponent },
  { path: 'agents/:id', component: AgentDetailsComponent },
  { path: 'connection_links', component: ConnectionLinksComponent },
  { path: 'connection_links/:id', component: ConnectionLinkDetailsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
