import {AfterViewInit, Component, OnInit} from '@angular/core';
import * as shape from "d3-shape";
import {LoadableObject} from "../../_model/LoadableObject";
import {ConnectionLink} from "../../_model/ConnectionLink";
import {Page} from "../../_model/page/page";
import {ConnectionLinkService} from "../../_services/connection-links/connection-link.service";
import {PageableClient} from "../../_services/pageable-client";
import {DAG} from "../../_model/graphs/DAG";
import {map} from "rxjs/operators";
import {LogWriter} from "../../log-writer";
import {GraphScheme} from "../../_model/graphs/GraphScheme";

@Component({
  selector: 'app-active-connections-graph',
  templateUrl: './active-connections-graph.component.html',
  styleUrls: ['./active-connections-graph.component.css']
})
export class ActiveConnectionsGraphComponent implements OnInit, AfterViewInit {
  private log : LogWriter = new LogWriter("active-connections-graph-component");
  private pageableClient : PageableClient<ConnectionLink> = this.service.aliveConnections();
  curve = shape.curveBundle.beta(1);
  graph : DAG = new DAG();
  graphScheme : GraphScheme = new GraphScheme();
  activeConnections : LoadableObject<Page<ConnectionLink>> = new LoadableObject(true);
  constructor(private service : ConnectionLinkService) { }

  ngOnInit() {
    this.activeConnections.bind(this.pageableClient.page(0, "DefaultConnectionLinkProjection"));
  }

  ngAfterViewInit(): void {
    this.activeConnections.value$.pipe(
      map((page : Page<ConnectionLink>) : DAG => {
        let dag = new DAG();

        if(page != null) {
          this.graphScheme.rainbow(page.items);
          page.items.forEach((link : ConnectionLink, index : number) => {
            let sourceNodeName = link.sourceAgentName;
            let sourceNodeID : string = null;
            let sourceProcessName : string = link.sourceProcessName;

            let destinationNodeName = link.destinationAgentName;
            let destinationNodeID : string = null;
            let destinationProcessName: string = link.destinationProcessName;

            if(sourceNodeName == null) {
              sourceNodeName = link.sourceString;
              sourceNodeID = link.sourceString;
              sourceProcessName = link.sourcePort + "";
            } else {
              sourceNodeID = link.sourceAgent.uuid;
            }

            if(destinationNodeName == null) {
              destinationNodeName = link.destinationString;
              destinationNodeID = link.destinationString;
              destinationProcessName = link.destinationPort + "";
            } else {
              destinationNodeID = link.destinationAgent.uuid;
            }


            let edgeLabel = sourceProcessName + "-" + destinationProcessName;

            dag.addNode(sourceNodeID, sourceNodeName);
            dag.addNode(destinationNodeID, destinationNodeName);
            let id = "id-" + link.uuid.replace(/-/g, "");
            dag.addEdge(id, sourceNodeID, destinationNodeID, edgeLabel);
          });
        }

        return dag;
      })).subscribe((dag : DAG) => {
      this.graph = dag;
    });
  }


}
