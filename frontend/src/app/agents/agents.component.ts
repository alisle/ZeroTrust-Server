import { Component, OnInit } from '@angular/core';
import {Agent} from "../_model/Agent";
import {DataSource} from "@angular/cdk/table";
import {DataService} from "../_services/data/data.service";
import {BehaviorSubject, Observable, of} from "rxjs";
import {AgentsService} from "../_services/agents/agents.service";
import {CollectionViewer} from "@angular/cdk/collections";
import {catchError, finalize} from "rxjs/operators";
import {Page} from "../_model/page/page";

@Component({
  selector: 'app-agents',
  templateUrl: './agents.component.html',
  styleUrls: ['./agents.component.css']
})
export class AgentsComponent implements OnInit {


  constructor(private service: AgentsService) { }
  dataSource = new AgentsDataSource(this.service);
  displayedColumns = ['name', 'alive', 'lastSeen', 'firstSeen' ];


  ngOnInit() {
    this.dataSource.get(0);
  }

}

export class AgentsDataSource extends DataSource<Agent> {
  private getSubject = new BehaviorSubject<Agent[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private sizeSubject = new BehaviorSubject<number>(0);

  public loading$ = this.loadingSubject.asObservable();
  public length$ = this.sizeSubject.asObservable();

  constructor(private agentService: AgentsService) {
    super();
  }

  connect(collectionViewer: CollectionViewer): Observable<Agent[]> {
    return this.getSubject.asObservable();
  }


  get(page: number)  {
    this.loadingSubject.next(true);
    this.agentService.page(page)
      .pipe(
        catchError(() => of([])),
        finalize(() => this.loadingSubject.next(false))
      ).subscribe((page: Page<Agent>) => {
        this.sizeSubject.next(page.page.totalElements);
        return this.getSubject.next(page.items);
      });

  }


  disconnect() {
    this.getSubject.complete();
    this.loadingSubject.complete();
    this.sizeSubject.complete();
  }
}

