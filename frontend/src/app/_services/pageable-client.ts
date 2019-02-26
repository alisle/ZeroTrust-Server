import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Page} from "../_model/page/page";
import {LogWriter} from "../log-writer";

export class PageableClient<T> {
  private _log : LogWriter = new LogWriter("pageable.client");
  public pageSize: number = 20;
  public sortDirection: string = 'ASC';
  public sortOn: string = 'id';
  private optionalParams : HttpParams = new HttpParams();

  constructor(protected key: string, protected URL: string, protected http: HttpClient) {}

  addParam(key: string, value: string) {
    this.optionalParams = this.optionalParams
      .append(key, value);
  }

  protected _page(page: number, params: HttpParams, url : string) : Observable<Page<T>> {
    this._log.debug(`actually making call to ${page} -:- ${url}`, params);
    return this.http.get(
      `${url}`,
      {
        params: params
      })
      .pipe(map((res: any) => {
        this._log.debug(`received page object:`, res);
        let page = this.objToPage(res);
        return page;
      }));
  }

  protected _create_params(page: number, projection: string) : HttpParams {
    let params = this.optionalParams
      .append("size", "" + this.pageSize)
      .append("page", "" + page)
      .append("sort", `${this.sortOn},${this.sortDirection}`);

    if (projection != null) {
      params = params.append("projection", projection);
    }

    return params;
  }


  page(page: number, projection: string = null): Observable<Page<T>> {
    this._log.debug(`page ${page}:${this.pageSize} has been requested for URL:${this.URL}`);
    let params = this._create_params(page, projection);

    let url = `${this.URL}`;
    return this._page(page, params, url);
  }

  totalElements() : Observable<number> {
    return this.page(0)
      .pipe(
        map((res: Page<T>) => {
          return res.page.totalElements;
        })
      );
  }

  private objToPage(obj: any): Page<T> {
    this._log.debug(`converting object to page, ${this.key}`);
    let page : Page<T> = new Page<T>(this.key, obj);
    this._log.debug(`converted page:`, page);
    return new Page<T>(this.key, obj);
  }

}
