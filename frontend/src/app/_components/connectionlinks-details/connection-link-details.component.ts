import {AfterViewInit, Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {LogWriter} from "../../log-writer";
import {ActivatedRoute} from "@angular/router";
import {ConnectionlinkService} from "../../_services/connectionlinks/connectionlink.service";
import {ConnectionLink} from "../../_model/ConnectionLink";
import {FlowGraphService} from "../../_services/flowgraph/FlowGraphService";
import {LoadableObject} from "../../_model/LoadableObject";
import {Page} from "../../_model/page/page";
import {Agent} from "../../_model/Agent";
import {Connection} from "../../_model/Connection";


enum Type {
  SOURCE,
  DESTINATION
};

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
      if(link == null) {
        return;
      }

      if( link.sourceAgent != null && link.destinationAgent != null) {
        this.connectionCountLoad.bind(this.service.countConnectionsBetweenAgents(link.sourceAgent.uuid, link.destinationAgent.uuid));
      } else if( link.sourceAgent != null && link.destinationAgent == null) {
        this.connectionCountLoad.bind(this.service.countConnectionsBetweenSourceAgentandIP(link.sourceAgent.uuid, link.destinationString));
      } else if( link.sourceAgent == null && link.destinationAgent != null) {
        this.connectionCountLoad.bind(this.service.countConnectionsBetweenIPandDestinationAgent(link.sourceString, link.destinationAgent.uuid))
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
    return this.getHeader(Type.SOURCE, link);
  }

  public getDestinationHeader(link: ConnectionLink) : string {
    return this.getHeader(Type.DESTINATION, link);
  }


  public getHeader(type: Type, link: ConnectionLink) : string {
    let name = this.getName(type, link);
    let process = this.getProcess(type, link);
    return name + ":" + process;
  }

  public getName(type: Type, link: ConnectionLink) : string {
    switch (type) {
      case Type.SOURCE: {
        if(link.sourceAgentName != null) {
          return link.sourceAgentName;
        }

        if(link.sourceString != null) {
          return link.sourceString;
        }


      } break;
      case Type.DESTINATION: {
        if(link.destinationAgentName != null) {
          return link.destinationAgentName;
        }

        if(link.destinationString != null) {
          return link.destinationString;
        }
      } break;
    }

    return "unknown";
  }

  public getProcess(type: Type, link: ConnectionLink) : string {
    switch(type) {
      case Type.SOURCE: {
        if(link.sourceProcessName != null) {
          return link.sourceProcessName;
        }

        if(link.sourcePort != null) {
          return link.sourcePort + "";
        }
      } break;
      case Type.DESTINATION: {
        if(link.destinationProcessName != null) {
          return link.destinationProcessName;
        }

        if(link.destinationPort != null) {
          return link.destinationPort + "";
        }
      } break;
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


  private createFlowGraphAgent(type: Type, link: ConnectionLink) : string {
    const source_prefix = "source";
    const destination_prefix = "destination";

    let agent : Agent = null;
    let connection : Connection = null;
    let prefix = "unknown";

    switch(type) {
      case Type.SOURCE: {
        agent = link.sourceAgent;
        connection = link.sourceConnection;
        prefix = source_prefix;
      } break;
      case Type.DESTINATION: {
        agent = link.destinationAgent;
        connection = link.destinationConnection;
        prefix = destination_prefix;
      }
    }


    let agentID = prefix + "_agent_" + agent.uuid;
    let userID = prefix + +"_user_" + connection.username;
    let processID = prefix + "_process_" + connection.processName;

    this.flowGraph.addNode(agentID, agent.name);
    this.flowGraph.addNode(userID, connection.username);
    this.flowGraph.addNode(processID, connection.processName);

    switch(type) {
      case Type.SOURCE: {
        this.flowGraph.addEdge(agentID, userID);
        this.flowGraph.addEdge(userID, processID);
      } break;
      case Type.DESTINATION: {
        this.flowGraph.addEdge(processID, userID);
        this.flowGraph.addEdge(userID, agentID);
      }
    }

    return processID;
  }

  private createFlowGraphIP(type: Type, link:ConnectionLink) : string {
    const source_prefix = "source_";
    const destination_prefix = "destination_";

    let port = 0;
    let address = "unknown";
    let prefix = "unknown";

    switch(type) {
      case Type.SOURCE: {
        address = link.sourceString;
        port = link.sourcePort;
        prefix = source_prefix;
      } break;
      case Type.DESTINATION: {
        address = link.destinationString;
        port = link.destinationPort;
        prefix = destination_prefix;

      } break;
    }

    let addressId = prefix +"_address_" + address;
    let portId = prefix + "_port_" + port;
    this.flowGraph.addNode(addressId, address);
    this.flowGraph.addNode(portId, port + "");

    this.flowGraph.addEdge(addressId, portId);

    return addressId;
  }


  private populateFlowGraph(width: number, height: number, page: Page<ConnectionLink>) : void {
    page.items.forEach((link) => {
      let sourceLink : string = null;
      let destinationLink : string = null;

      if(link.sourceAgent != null) {
        sourceLink = this.createFlowGraphAgent(Type.SOURCE, link);
      } else {
        sourceLink = this.createFlowGraphIP(Type.SOURCE, link);
      }

      if(link.destinationAgent != null) {
        destinationLink = this.createFlowGraphAgent(Type.DESTINATION, link);
      } else {
        destinationLink = this.createFlowGraphIP(Type.DESTINATION, link);
      }

      this.flowGraph.addEdge(sourceLink, destinationLink);
    });

    this.flowGraph.draw();
  }
}
