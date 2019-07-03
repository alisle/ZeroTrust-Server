import {AfterViewInit, Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Agent} from "../../_model/Agent";
import {merge } from "rxjs";
import {AgentsService} from "../../_services/agents/agents.service";
import {map, tap} from "rxjs/operators";
import {MatPaginator, MatSort} from "@angular/material";
import {LogWriter} from "../../log-writer";
import {PageableDataSource} from "../../_services/pageable-data-source";
import {Router} from "@angular/router";
import {LoadableObject} from "../../_model/LoadableObject";
import {AgentCount} from "../../_model/AgentCount";
import {ChartNode} from "../../_model/graphs/ChartNode";

import {GraphScheme} from "../../_model/graphs/GraphScheme";

@Component({
  selector: 'app-agents',
  templateUrl: './agents.component.html',
  styleUrls: ['./agents.component.css']
})
export class AgentsComponent implements OnInit, AfterViewInit {
  private log : LogWriter = new LogWriter("agents.component");

  constructor(private service: AgentsService, private router: Router) { }
  dataSource = new PageableDataSource<Agent>(this.service.allAgents());

  displayedColumns = [
    'name',
    'alive',
    'lastSeen',
    'firstSeen',
    'known',
    'aliveConnectionCount',
    'connectionCount',
    'aliveSourceConnectionCount',
    'sourceConnectionCount',
    'aliveDestinationConnectionCount',
    'destinationConnectionCount'
  ];


  selectedRow : Agent = null;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort : MatSort;

  totalAgents : LoadableObject<number> = new LoadableObject(true);
  totalAlive : LoadableObject<number> = new LoadableObject(true);
  incomingMap : LoadableObject<AgentCount[]> = new LoadableObject(true);
  outgoingMap : LoadableObject<AgentCount[]> = new LoadableObject(true);

  private sourceMapData : AgentCount[];
  private destinationMapData : AgentCount[];

  activeOutgoingScheme : GraphScheme =  new GraphScheme();
  activeIncomingScheme : GraphScheme = new GraphScheme();

  activeIncoming : ChartNode[] = [];
  activeOutgoing : ChartNode[] = [];


  ngOnInit() {
    this.dataSource.length$.subscribe(size => {
      this.log.debug(`setting total elements to ${size}`);
      this.paginator.length = size
    });
    this.dataSource.pageSize$.subscribe( size => {
      this.log.debug(`setting size of page to ${size}`);
      this.paginator.pageSize = size
    });

    this.log.debug(`requesting first page of agents`);
    this.dataSource.get(0);

    this.totalAlive.bind(this.service.totalAlive());
    this.totalAgents.bind(this.service.totalAgents());
    this.incomingMap.bind(this.service.countIncomingConnections()).value$.subscribe((value : AgentCount[]) => { this.sourceMapData = value; });
    this.outgoingMap.bind(this.service.countOutgoingConnections()).value$.subscribe((value : AgentCount[]) => { this.destinationMapData = value; });
  }

  ngAfterViewInit(): void {
    merge(this.paginator.page, this.sort.sortChange)
      .pipe(
        tap( () => {
          this.dataSource.sortDirection(this.sort.direction);
          this.dataSource.sortOn(this.sort.active);
          this.dataSource.pageSize(this.paginator.pageSize);
          this.dataSource.get(this.paginator.pageIndex);
        })
      ).subscribe();


    this.incomingMap.value$.pipe(
      map((count: AgentCount[]) : ChartNode[] => { return ChartNode.convertAgentCount(count); })
    ).subscribe((nodes : ChartNode[])  => {
      this.activeOutgoingScheme.rainbow(nodes);
      this.activeOutgoing = nodes;
    });

    this.outgoingMap.value$.pipe(
      map((count: AgentCount[]) : ChartNode[] => { return ChartNode.convertAgentCount(count); })
    ).subscribe((nodes: ChartNode[]) => {
      this.log.debug("setting sourcemap nodes", nodes);
      this.activeIncomingScheme.rainbow(nodes);
      this.activeIncoming = nodes;
    });
  }

  selectedGraph(agent) : void {
    this.log.debug("I got the agent", agent);
    this.activeIncoming.forEach((node : ChartNode) => {
      if(node.name == agent.name) {
        this.router.navigate(['agents', node.uuid ]);
      }
    });

    this.activeOutgoing.forEach((node : ChartNode) => {
      if(node.name == agent.name) {
        this.router.navigate(['agents', node.uuid ]);
      }
    })
  }

  selectRow(row) : void {
    this.log.debug("I got row", row);
    this.selectedRow = row;
    this.router.navigate(['agents', this.selectedRow.uuid ]);
  }

}
