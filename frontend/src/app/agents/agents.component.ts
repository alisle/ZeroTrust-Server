import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {Agent} from "../_model/Agent";
import {DataSource} from "@angular/cdk/table";
import {BehaviorSubject, merge, Observable, of} from "rxjs";
import {AgentsService} from "../_services/agents/agents.service";
import {CollectionViewer} from "@angular/cdk/collections";
import {catchError, finalize, tap} from "rxjs/operators";
import {Page} from "../_model/page/page";
import {MatPaginator, MatSort} from "@angular/material";
import {LogWriter} from "../log-writer";

@Component({
  selector: 'app-agents',
  templateUrl: './agents.component.html',
  styleUrls: ['./agents.component.css']
})
export class AgentsComponent implements OnInit, AfterViewInit {
  private log : LogWriter = new LogWriter("agents.component");

  constructor(private service: AgentsService) { }
  dataSource = new AgentsDataSource(this.service);
  displayedColumns = ['name', 'alive', 'lastSeen', 'firstSeen', 'known', 'connectionCount' ];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort : MatSort;

  ngOnInit() {
    this.dataSource.length$.subscribe(size => {
      this.log.debug(`setting total elements to ${size}`);
      this.paginator.length = size
    });
    this.dataSource.pageSize$.subscribe( size => {
      this.log.debug(`setting size of page to ${size}`);
      this.paginator.pageSize = size
    });

    this.log.debug(`requesting first page of agents`);
    this.dataSource.get(0);
  }

  ngAfterViewInit(): void {
    merge(this.paginator.page, this.sort.sortChange)
      .pipe(
        tap( () => {
          this.dataSource.sortDirection(this.sort.direction);
          this.dataSource.sortOn(this.sort.active);
          this.dataSource.pageSize(this.paginator.pageSize);
          this.dataSource.get(this.paginator.pageIndex);
        })
      ).subscribe();
  }


}

export class AgentsDataSource extends DataSource<Agent> {
  private getSubject = new BehaviorSubject<Agent[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private sizeSubject = new BehaviorSubject<number>(0);
  private  pageSizeSubject = new BehaviorSubject<number>(20);

  public loading$ = this.loadingSubject.asObservable();
  public length$ = this.sizeSubject.asObservable();
  public pageSize$ = this.pageSizeSubject.asObservable();

  constructor(private agentService: AgentsService) {
    super();
  }

  connect(collectionViewer: CollectionViewer): Observable<Agent[]> {
    return this.getSubject.asObservable();
  }

  pageSize(size: number) {
    this.agentService.pageSize = size;
  }

  sortOn(element: string) {
    this.agentService.sortOn = element;
  }

  sortDirection(direction: string) {
    this.agentService.sortDirection = direction;
  }

  get(page: number)  {
    this.loadingSubject.next(true);
    this.agentService.page(page, "agentList")
      .pipe(
        catchError(() => of([])),
        finalize(() => this.loadingSubject.next(false))
      ).subscribe((page: Page<Agent>) => {
        this.sizeSubject.next(page.page.totalElements);
        this.pageSizeSubject.next(page.page.size);
        return this.getSubject.next(page.items);
      });

  }


  disconnect() {
    this.getSubject.complete();
    this.loadingSubject.complete();
    this.sizeSubject.complete();
  }
}

