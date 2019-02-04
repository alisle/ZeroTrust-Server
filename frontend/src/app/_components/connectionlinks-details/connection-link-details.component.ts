import {AfterViewInit, Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {LogWriter} from "../../log-writer";
import {ActivatedRoute} from "@angular/router";
import {ConnectionlinkService} from "../../_services/connectionlinks/connectionlink.service";
import {ConnectionLink} from "../../_model/ConnectionLink";
import {catchError, finalize} from "rxjs/operators";
import {of} from "rxjs";
import * as d3 from 'd3';

@Component({
  selector: 'app-connection-link-details',
  templateUrl: './connection-link-details.component.html',
  styleUrls: ['./connection-link-details.component.css']
})
export class ConnectionLinkDetailsComponent implements OnInit, AfterViewInit {

  private log: LogWriter = new LogWriter("connectionlinks-details-component");
  constructor(private route: ActivatedRoute, private service: ConnectionlinkService) { }

  loading : boolean = true;
  error: boolean = false;
  connectionLink : ConnectionLink = null;

  @ViewChildren('diagram', { read: ElementRef })  diagramElementContainer: QueryList<ElementRef>;

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.service.get(id, "DefaultConnectionLinkProjection")
      .pipe(
        catchError(() => {
          this.log.error("unable to find connection link!");
          this.error = true;
          return of(null);
        }),
        finalize(() => {
          this.loading = false;
        })
      ).subscribe(link => {
        console.log("got connection link:", link);
        this.connectionLink = link;
    })


  }

  ngAfterViewInit(): void {
    this.diagramElementContainer.changes.subscribe((components: QueryList<ElementRef>) => {
      if(components.length >= 1) {
        this.log.debug("creating svg on id");
        this.createPicture(components.first);
      } else {
        this.log.debug("haven't got a component yet, skipping");
      }
    });
  }

  createPicture(diagramElement: ElementRef) {
    this.log.debug("my element", diagramElement);

    let div = d3.select(diagramElement.nativeElement);
    this.log.debug("creating component diagram on element", div);
    let svg = div.append("svg")
      .attr("width", 400)
      .attr("height", 400);

    svg.append("circle")
      .attr("cx", 50)
      .attr("cy", 50)
      .attr("r", 50)
      .style("fill", "red");

  }

  svghandle(event: any) {
    this.log.debug("I am doing stuff", event);
  }

}
