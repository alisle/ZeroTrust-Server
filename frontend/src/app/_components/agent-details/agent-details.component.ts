import { Component, OnInit } from '@angular/core';
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

@Component({
  selector: 'app-agent-details',
  templateUrl: './agent-details.component.html',
  styleUrls: ['./agent-details.component.css']
})
export class AgentDetailsComponent implements OnInit {
  private log : LogWriter = new LogWriter("agent-details-component");

  agentLoad : LoadableObject<Agent> = new LoadableObject();

  destinationGraphLoad : LoadableObject<Page<ConnectionLink>> = new LoadableObject(true);
  sourceGraphLoad : LoadableObject<Page<ConnectionLink>> = new LoadableObject(true);

  destinationUserLoad : LoadableObject<UserCount[]> = new LoadableObject(true);
  sourceUserLoad : LoadableObject<UserCount[]> = new LoadableObject(true);

  destinationProcessLoad : LoadableObject<ProcessCount[]> = new LoadableObject(true);
  sourceProcessLoad : LoadableObject<ProcessCount[]> = new LoadableObject(true);


  activeSourceDataSource : PageableDataSource<ConnectionLink> = null;
  activeDestinationDataSource : PageableDataSource<ConnectionLink> = null;


  agent : Agent = null;

  constructor(private route: ActivatedRoute, private service: AgentsService, private connectionLinkService: ConnectionLinkService) {
  }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');

    this.activeSourceDataSource = new PageableDataSource<ConnectionLink>(this.connectionLinkService.activeSourceConnections(id));
    this.activeDestinationDataSource = new PageableDataSource<ConnectionLink>(this.connectionLinkService.activeDestinationConnections(id));

    // Start hitting the rest api
    this.destinationUserLoad.bind(this.service.countUsersDestination(id));
    this.sourceUserLoad.bind(this.service.countUsersSource(id));

    this.destinationProcessLoad.bind(this.service.countProcessSource(id));
    this.sourceProcessLoad.bind(this.service.countProcessDestination(id));

    this.agentLoad.bind(this.service.get(id));


    this.agentLoad.value$.subscribe((agent: Agent) => {
      this.log.debug("loaded agent:", agent);
      this.agent = agent;
    });

    this.sourceUserLoad.value$.subscribe(( count: UserCount[] ) => {
      this.log.debug("loaded source user count:", count);
    });

    this.destinationUserLoad.value$.subscribe((count: UserCount[] ) => {
      this.log.debug("loading destination user count:", count);
    })



  }

}
