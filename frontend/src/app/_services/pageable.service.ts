import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Page} from "../_model/page/page";
import {LogWriter} from "../log-writer";

export abstract class PageableService<T> {
  protected log : LogWriter = new LogWriter("pageable.service");
  protected base_url: string = "http://localhost:8080";
  public pageSize: number = 20;
  public sortDirection: string = 'ASC';
  public sortOn: string = 'id';

  protected constructor(protected key: string, protected URL: string, protected http: HttpClient) {}

  get(id: string, projection: string = null): Observable<T> {
    let params = new HttpParams();

    if (projection != null) {
      params = params.append("projection", projection);
    }

    return this.http.get(
      `${this.base_url}${this.URL}/${id}`,
      {
        params: params
      }
    ).pipe(
      map((res: any) => {
        let object = res as T;
        return object;
      })
    );
  }

  protected _page(page: number, params: HttpParams, url : string) : Observable<Page<T>> {
    return this.http.get(
      `${url}`,
      {
        params: params
      })
      .pipe(map((res: any) => {
        this.log.debug(`received page object:`, res);
        let page = this.objToPage(res);
        return page;
      }));
  }
  page(page: number, projection: string = null): Observable<Page<T>> {
    console.log(`page ${page}:${this.pageSize} has been requested for URL:${this.URL}`);

    let params = new HttpParams()
      .append("size", "" + this.pageSize)
      .append("page", "" + page)
      .append("sort", `${this.sortOn},${this.sortDirection}`);

    if (projection != null) {
      params = params.append("projection", projection);
    }

    let url = `${this.base_url}${this.URL}`;
    return this._page(page, params, url);
  }

  private objToPage(obj: any): Page<T> {
    this.log.debug(`converting object to page, ${this.key}`);
    let page : Page<T> = new Page<T>(this.key, obj);
    this.log.debug(`converted page:`, page);
    return new Page<T>(this.key, obj);
  }

}
