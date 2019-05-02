import {Observable} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";
import {map} from "rxjs/operators";
import {PageableClient} from "./pageable-client";

export abstract class DefaultService<T> {
  protected base_url = "http://localhost:4300/api";
  //protected base_url = "http://localhost:8080";

  protected constructor(protected key : string, protected URL : string, protected http: HttpClient) {}

  protected default() : PageableClient<T> {
    return new PageableClient(this.key, `${this.base_url}${this.URL}`, this.http);
  }

  protected search(searchURL: string) : PageableClient<T> {
    return new PageableClient(this.key, `${this.base_url}${this.URL}/search/${searchURL}`, this.http);
  }

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

  protected count(endpoint: string, param_name: string = null, id: string = null) : Observable<Object> {
    let url = `${this.base_url}${this.URL}/search/${endpoint}`;

    let params : HttpParams = new HttpParams();
    if(id != null) {
      params = params.append(param_name, id);
    }

    return this.http.get(
      url,
      {
        params: params
      }
    );
  }
}
