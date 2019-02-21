import {AfterViewInit, Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {LogWriter} from "../../log-writer";
import {ActivatedRoute} from "@angular/router";
import {ConnectionlinkService} from "../../_services/connectionlinks/connectionlink.service";
import {ConnectionLink} from "../../_model/ConnectionLink";
import {FlowGraphService} from "../../_services/flowgraph/FlowGraphService";
import {LoadableObject} from "../../_model/LoadableObject";
import {Page} from "../../_model/page/page";

@Component({
  selector: 'app-connection-link-details',
  templateUrl: './connection-link-details.component.html',
  styleUrls: ['./connection-link-details.component.css']
})
export class ConnectionLinkDetailsComponent implements OnInit, AfterViewInit {
  private flowGraph : FlowGraphService = new FlowGraphService();
  private log: LogWriter = new LogWriter("connectionlinks-details-component");

  constructor(private route: ActivatedRoute, private service: ConnectionlinkService) { }

  linkLoad : LoadableObject<ConnectionLink> = new LoadableObject();
  connectionCountLoad : LoadableObject<number> = new LoadableObject(true);
  graphLoad : LoadableObject<Page<ConnectionLink>> = new LoadableObject(true);

  @Input()
  connectionID ? : string;

  connectionLink : ConnectionLink = null;
  connectionCount : number = 0;


  @ViewChildren('diagram', { read: ElementRef })  diagramElementContainer: QueryList<ElementRef>;

  ngOnInit() {
    const id = (this.connectionID == null) ? this.route.snapshot.paramMap.get('id') : this.connectionID;

    this.linkLoad.bind(this.service.get(id, "DefaultConnectionLinkProjection"));
    this.linkLoad.value$.subscribe((link : ConnectionLink) => {
      this.connectionLink = link;
    });

    this.linkLoad.value$.subscribe((link : ConnectionLink) => {
      if( link.sourceAgent != null && link.destinationAgent != null) {
        this.connectionCountLoad.bind(this.service.countConnectionsBetweenAgents(link.sourceAgent.uuid, link.destinationAgent.uuid));
      } else if( link.sourceAgent != null && link.destinationAgent == null) {
        // Do stuff.
      } else if( link.sourceAgent == null && link.destinationAgent != null) {
        // Do other stuff.
      } else {
        this.connectionCountLoad.succeed(0);
      }
    });

    this.linkLoad.value$.subscribe((link : ConnectionLink) => {
      if(link != null) {
        if(link.sourceAgent != null && link.destinationAgent != null) {
          this.graphLoad.bind(this.service.connectionsBetweenAgents(link.sourceAgent.uuid, link.destinationAgent.uuid))
        } else if( link.sourceAgent != null  && link.destinationAgent == null) {
          this.graphLoad.bind(this.service.connectionsBetweenSourceAgentandIP(link.sourceAgent.uuid, link.destinationString))
        } else if( link.sourceAgent == null && link.destinationAgent != null) {
          this.graphLoad.bind(this.service.connectionsBetweenIPandDestinationAgent(link.sourceString, link.destinationAgent.uuid))
        } else {
          this.graphLoad.succeed(null);
        }
      }
    });

    this.connectionCountLoad.value$.subscribe((value : number) => {
      this.connectionCount = value;
    });

    this.graphLoad.value$.subscribe((page : Page<ConnectionLink>) => {
      if( page != null) {
        this.log.debug("creating graph", page);
        this.populateFlowGraph(400, 400, page);
      }
    });

  }

  public getSourceHeader(link : ConnectionLink) : string {
    let name = this.getSourceCanonical(link);
    let process = this.getSourceProcess(link);

    return name + ":" + process;
  }

  public getDestinationHeader(link: ConnectionLink) : string {
    let name = this.getDestinationCanonical(link);
    let process = this.getDestinationProcess(link);

    return name + ":" + process;
  }


  public getSourceProcess(link : ConnectionLink) : string {
    if(link.sourceProcessName != null) {
      return link.sourceProcessName;
    }

    if(link.sourcePort != null) {
      return link.sourcePort + "";
    }

    return "unknown";
  }

  public getSourceCanonical(link : ConnectionLink) : string {
    if(link.sourceAgentName != null) {
      return link.sourceAgentName;
    }

    if(link.sourceString != null) {
      return link.sourceString;
    }

    return "unknown";
  }

  public getDestinationProcess(link : ConnectionLink) : string {
    if(link.destinationProcessName != null) {
      return link.destinationProcessName;
    }

    if(link.destinationPort != null) {
      return link.destinationPort + "";
    }

    return "unknown";
  }


  public getDestinationCanonical(link: ConnectionLink) : string {
    if(link.destinationAgentName != null) {
      return link.destinationAgentName;
    }

    if(link.destinationString != null) {
      return link.destinationString;
    }

    return "unknown";
  }


  ngAfterViewInit(): void {
    this.diagramElementContainer.changes.subscribe((components: QueryList<ElementRef>) => {
      if(components.length >= 1) {
        this.log.debug("found svg with id, starting to create the picture");
        this.flowGraph.setElement(components.first);
      } else {
        this.log.debug("haven't got a component yet, skipping");
      }
    });
  }


  populateFlowGraph(width: number, height: number, page: Page<ConnectionLink>) : void {
    page.items.forEach((link) => {
      let sourceAgent = link.sourceAgent.name;
      let destinationAgent = link.destinationAgent.name;
      let sourceUser = link.sourceConnection.username;
      let destinationUser = link.destinationConnection.username;
      let sourceProcess = link.sourceConnection.processName;
      let destinationProcess = link.destinationConnection.processName;

      let sourceAgentID = "source_agent_" + link.sourceAgent.uuid;
      let destinationAgentID = "destination_agent_" +  link.destinationAgent.uuid;
      let sourceUserID = "source_user_" + link.sourceConnection.username;
      let destinationUserID = "destination_user_" + link.destinationConnection.username;
      let sourceProcessID = "source_process_" + link.sourceConnection.processName;
      let destinationProcessID = "destination_process_" + link.destinationConnection.processName;

      this.flowGraph.addNode(sourceAgentID, sourceAgent);
      this.flowGraph.addNode(destinationAgentID, destinationAgent);
      this.flowGraph.addNode(sourceUserID, sourceUser);
      this.flowGraph.addNode(destinationUserID, destinationUser);
      this.flowGraph.addNode(sourceProcessID, sourceProcess);
      this.flowGraph.addNode(destinationProcessID, destinationProcess);

      this.flowGraph.addEdge(sourceAgentID, sourceUserID);
      this.flowGraph.addEdge(sourceUserID, sourceProcessID);
      this.flowGraph.addEdge(sourceProcessID, destinationProcessID);
      this.flowGraph.addEdge(destinationProcessID, destinationUserID);
      this.flowGraph.addEdge(destinationUserID, destinationAgentID);
    });

    this.flowGraph.draw();
  }
}
