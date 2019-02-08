import {BehaviorSubject, Observable, of} from "rxjs";
import {catchError, finalize} from "rxjs/operators";
import {LogWriter} from "../log-writer";

export class LoadableObject<T> {
  private log: LogWriter = new LogWriter("LoadableObject");

  private loadingSubject : BehaviorSubject<boolean> = new BehaviorSubject(false);
  private errorSubject : BehaviorSubject<boolean> = new BehaviorSubject(false);
  private valueSubject : BehaviorSubject<T> = new BehaviorSubject(null);

  public loading$  = this.loadingSubject.asObservable();
  public error$ = this.errorSubject.asObservable();
  public value$ = this.valueSubject.asObservable();

  constructor(loading: boolean = false) {
    this.loadingSubject.next(loading);
  }

  public bind(promise : Observable<T>) : void {
    this.loadingSubject.next(true);
    promise.pipe(
      catchError(() => {
        this.log.error("error loading object!");
        this.errorSubject.next(true);
        return of(null);
      }),
      finalize(() => {
        this.log.debug("finished loading object");
        this.loadingSubject.next(false);
      })
    ).subscribe((value : T) => {
      if(value != null) {
        this.log.debug("returning object");
        this.valueSubject.next(value);
      }
    });

  }
}
