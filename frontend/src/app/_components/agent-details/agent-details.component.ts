import {AfterViewInit, Component, ElementRef, OnInit, QueryList, ViewChildren} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {LogWriter} from "../../log-writer";
import {AgentsService} from "../../_services/agents/agents.service";
import {Agent} from "../../_model/Agent";
import {LoadableObject} from "../../_model/LoadableObject";
import {ConnectionLink} from "../../_model/ConnectionLink";
import {Page} from "../../_model/page/page";
import {UserCount} from "../../_model/UserCount";
import {ProcessCount} from "../../_model/ProcessCount";
import {PageableDataSource} from "../../_services/pageable-data-source";
import {ConnectionLinkService} from "../../_services/connection-links/connection-link.service";
import {FlowGraphService} from "../../_services/flowgraph/FlowGraphService";
import {IPAddress} from "../../_model/IPAddress";
import {GraphScheme} from "../../_model/graphs/GraphScheme";
import {ChartNode} from "../../_model/graphs/ChartNode";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-agent-details',
  templateUrl: './agent-details.component.html',
  styleUrls: ['./agent-details.component.css']
})
export class AgentDetailsComponent implements OnInit, AfterViewInit {
  private log : LogWriter = new LogWriter("agent-details-component");
  private sourceFlowGraph : FlowGraphService = new FlowGraphService();
  private destinationFlowGraph : FlowGraphService = new FlowGraphService();

  agentLoad : LoadableObject<Agent> = new LoadableObject();

  destinationGraphLoad : LoadableObject<Page<ConnectionLink>> = new LoadableObject(true);
  sourceGraphLoad : LoadableObject<Page<ConnectionLink>> = new LoadableObject(true);

  destinationUserLoad : LoadableObject<UserCount[]> = new LoadableObject(true);
  sourceUserLoad : LoadableObject<UserCount[]> = new LoadableObject(true);

  destinationProcessLoad : LoadableObject<ProcessCount[]> = new LoadableObject(true);
  sourceProcessLoad : LoadableObject<ProcessCount[]> = new LoadableObject(true);


  activeSourceDataSource : PageableDataSource<ConnectionLink> = null;
  activeDestinationDataSource : PageableDataSource<ConnectionLink> = null;
  connectionsDataSource : PageableDataSource<ConnectionLink> = null;

  agent : Agent = null;

  sourceLinks : ConnectionLink[] = [];
  destinationLinks : ConnectionLink[] = [];

  userOutgoingConnectionsScheme : GraphScheme = new GraphScheme();
  userOutgoingConnections : ChartNode[] = [];

  userIncomingConnectionsScheme : GraphScheme = new GraphScheme();
  userIncomingConnections : ChartNode[] = [];


  processOutgoingConnectionsScheme : GraphScheme = new GraphScheme();
  processOutgoingConnections : ChartNode[] = [];

  processIncomingConnectionsScheme : GraphScheme = new GraphScheme();
  processIncomingConnections : ChartNode[] = [];

  @ViewChildren('destinationDiagram', { read: ElementRef }) destinationDiagramElementContainer: QueryList<ElementRef>;

  constructor(private route: ActivatedRoute, private service: AgentsService, private connectionLinkService: ConnectionLinkService) {
  }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');

    this.activeSourceDataSource = new PageableDataSource<ConnectionLink>(this.connectionLinkService.activeSourceConnections(id));
    this.activeDestinationDataSource = new PageableDataSource<ConnectionLink>(this.connectionLinkService.activeDestinationConnections(id));
    this.connectionsDataSource = new PageableDataSource<ConnectionLink>(this.connectionLinkService.agentConnections(id));

    // Start hitting the rest api
    this.destinationUserLoad.bind(this.service.countUsersDestination(id));
    this.sourceUserLoad.bind(this.service.countUsersSource(id));

    this.sourceProcessLoad.bind(this.service.countProcessSource(id));
    this.destinationProcessLoad.bind(this.service.countProcessDestination(id));

    this.agentLoad.bind(this.service.get(id));


    this.agentLoad.value$.subscribe((agent: Agent) => {
      if( agent != null) {
        this.log.debug("loaded agent:", agent);
        this.agent = agent;
        this.createDestinationDiagram(agent);
      }
    });


    this.sourceUserLoad.value$.pipe(
      map((count : UserCount[]) : ChartNode[] => {
        return ChartNode.convertUserCount(count);
      })
    ).subscribe((nodes : ChartNode[]) => {
      this.userOutgoingConnections = nodes;
      this.userOutgoingConnectionsScheme.rainbow(nodes);
    });


    this.destinationUserLoad.value$.pipe(
      map((count : UserCount[]) : ChartNode[] => {
        return ChartNode.convertUserCount(count);
      })
    ).subscribe((nodes : ChartNode[]) => {
      this.userIncomingConnections = nodes;
      this.userIncomingConnectionsScheme.rainbow(nodes);
    });

    this.sourceProcessLoad.value$.pipe(
      map((count : ProcessCount[]) : ChartNode[] => {
        return ChartNode.convertProcessCount(count);
      })
    ).subscribe((nodes : ChartNode[]) => {
      this.processOutgoingConnections = nodes;
      this.processOutgoingConnectionsScheme.rainbow(nodes);
    });


    this.destinationProcessLoad.value$.pipe(
      map((count : ProcessCount[]) : ChartNode[] => {
        return ChartNode.convertProcessCount(count);
      })
    ).subscribe((nodes : ChartNode[]) => {
      this.processIncomingConnections = nodes;
      this.processIncomingConnectionsScheme.rainbow(nodes);
    });

    let source = this.connectionLinkService.activeSourceConnections(id);
    source.pageSize = 100;
    this.sourceGraphLoad.bind(source.page(0));

    let destination = this.connectionLinkService.activeDestinationConnections(id);
    destination.pageSize = 100;
    this.destinationGraphLoad.bind(destination.page(0))

    this.sourceGraphLoad.value$.subscribe((page : Page<ConnectionLink>) => {
      if( page != null) {
        this.sourceLinks = page.items;
      }
    });

    this.destinationGraphLoad.value$.subscribe((page : Page<ConnectionLink>) => {
      if( page != null) {
        this.destinationLinks = page.items;
      }
    });


  }

  ngAfterViewInit(): void {

    this.destinationDiagramElementContainer.changes.subscribe((components: QueryList<ElementRef>) => {
      if(components.length >= 1) {
        this.log.debug("setting destination element ref");
        this.destinationFlowGraph.setElement(components.first);
      }
    });
  }


  createDestinationDiagram(agent: Agent) {
    this.destinationGraphLoad.value$.subscribe((page : Page<ConnectionLink>) => {
      if(page == null) {
        return;
      }

      page.items.forEach((link) => {
        let process_id = "process_" + link.destinationProcessName;
        let link_id = link.uuid + "_source_link";
        let link_node = link.uuid + "_source_node";


        this.destinationFlowGraph.addNode(process_id, link.destinationProcessName);

        if(link.sourceAgent != null) {
          this.destinationFlowGraph.addNode(link_node, link.sourceAgent.name);
        } else {
          this.destinationFlowGraph.addNode(link_node, link.sourceString);
        }

        if(link.sourceProcessName != null) {
          this.destinationFlowGraph.addNode(link_id, link.sourceProcessName);
        } else {
          this.destinationFlowGraph.addNode(link_id, link.sourcePort + "");
        }

        this.destinationFlowGraph.addEdge(link_node, link_id);

        this.destinationFlowGraph.addEdge(link_id, process_id);
        this.destinationFlowGraph.addEdge(process_id, link.destinationString);
      });
    });

    this.destinationFlowGraph.addNode("agent", agent.name);
    agent.addresses.forEach((address: IPAddress) => {
      this.destinationFlowGraph.addNode(address.addressString, address.addressString);
      this.destinationFlowGraph.addEdge(address.addressString, "agent");
    });

    this.destinationFlowGraph.draw();
  }

}
