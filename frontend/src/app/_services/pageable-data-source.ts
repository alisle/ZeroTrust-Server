import {DataSource} from "@angular/cdk/table";
import {BehaviorSubject, Observable} from "rxjs";
import {CollectionViewer} from "@angular/cdk/collections";
import {PageableService} from "./pageable.service";
import {catchError, finalize} from "rxjs/operators";
import {Page} from "../_model/page/page";

export class PageableDataSource<T> extends DataSource<T> {
  private getSubject = new BehaviorSubject<T[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private sizeSubject = new BehaviorSubject<number>(0);
  private  pageSizeSubject = new BehaviorSubject<number>(20);

  public loading$ = this.loadingSubject.asObservable();
  public length$ = this.sizeSubject.asObservable();
  public pageSize$ = this.pageSizeSubject.asObservable();

  constructor(private service : PageableService<T>) {
    super();
  }

  connect(collectionViewer: CollectionViewer) : Observable<T[]> {
    return this.getSubject.asObservable();
  }

  pageSize(size: number) {
    this.service.pageSize = size;
  }

  sortOn(element: string) {
    this.service.sortOn = element;
  }

  sortDirection(direction: string) {
    this.service.sortDirection = direction;
  }

  get(page: number) {
    this.loadingSubject.next(true);
    this.service.page(page)
      .pipe(
        catchError(() => []),
        finalize(() => this.loadingSubject.next(false))
      ).subscribe((page: Page<T>) => {
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
