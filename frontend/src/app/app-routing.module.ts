import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {WelcomeComponent} from "./_components/welcome/welcome.component";
import {DashboardComponent} from "./_components/dashboard/dashboard.component";
import {AgentsComponent} from "./_components/agents/agents.component";
import {AgentDetailsComponent} from "./_components/agent-details/agent-details.component";

const routes: Routes = [
  { path: '', component: WelcomeComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'agents', component: AgentsComponent },
  { path: 'agents/:id', component: AgentDetailsComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
