import {PageMetadata} from "./pagemetadata";
import {Links} from "./links";

export class Page<T> {
  public page : PageMetadata;
  public _embedded: any;
  public links: Links;
  public items: T[];

  constructor(private _key : string, private _raw : any) {
    this.page = _raw.page as PageMetadata;
    this._embedded = _raw._embedded;
    this.items = this._embedded[_key];
    this.links = _raw._links as Links;
  }

}
