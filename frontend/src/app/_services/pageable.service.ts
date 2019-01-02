import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Page} from "../_model/page/page";

export abstract class PageableService<T> {
  protected base_url: string = "http://localhost:8080";
  public page_size: number = 20;
  public sort_direction: string = 'ASC';
  public sort_on: string = 'id';

  constructor(protected key: string, protected URL: string, protected http: HttpClient) {}

  get(id: string): Observable<T> {
    return this.http.get(
      `http://${this.base_url}${this.URL}/${id}`
    ).pipe(
      map((res: any) => {
        let object = res as T;
        return object;
      })
    );
  }

  page(page: number): Observable<Page<T>> {
    console.log(`page ${page} has been requested for URL:${this.URL}`);

    let params = new HttpParams()
      .append("size", "" + this.page_size)
      .append("page", "" + page)
      .append("sort", `${this.sort_on},${this.sort_direction}`);

    return this.http.get(
      `${this.base_url}${this.URL}`,
      {
        params: params
      })
      .pipe(map((res: any) => {
        console.log(`recieved page object: ${res}`);
        let page = this.objToPage(res);
        return page;
      }));
  }

  private objToPage(obj: any): Page<T> {
    console.log(`converting object to page, ${this.key}`);
    let page : Page<T> = new Page<T>(this.key, obj);

    console.log(page);
    return new Page<T>(this.key, obj);
  }

}
