import {Post} from '../../_model/Post';
import {DataSource} from '@angular/cdk/table';
import {Observable} from 'rxjs/Observable';
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
  dataSource = new PostDataSource(this.dataService);
}

export class PostDataSource extends DataSource<any> {
  constructor(private dataService: AgentsService) {
    super();
  }

  connect(): Observable<Post[]> {
    return this.dataService.get(null);
  }

  disconnect() {
  }
}
