import {Component} from "@angular/core";
import {AgentsService} from "../../_services/agents/agents.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  constructor(private dataService: AgentsService) {
  }

  displayedColumns = ['date_posted', 'title', 'category', 'delete'];
}

