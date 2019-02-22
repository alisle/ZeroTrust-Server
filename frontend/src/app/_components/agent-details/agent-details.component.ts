import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {LogWriter} from "../../log-writer";
import {AgentsService} from "../../_services/agents/agents.service";
import {catchError, finalize} from "rxjs/operators";
import {of} from "rxjs";
import {Agent} from "../../_model/Agent";
import {LoadableObject} from "../../_model/LoadableObject";
import {ConnectionLink} from "../../_model/ConnectionLink";
import {Page} from "../../_model/page/page";

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
  destinationUserLoad : LoadableObject<Page<string>> = new LoadableObject(true);
  sourceUserLoad : LoadableObject<Page<string>> = new LoadableObject(true);

  agent : Agent = null;

  constructor(private route: ActivatedRoute, private service: AgentsService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');

    this.agentLoad.bind(this.service.get(id));
    this.agentLoad.value$.subscribe((agent: Agent) => {
      this.log.debug("loaded agent:", agent);
      this.agent = agent;
    });


  }

}
