import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {LogWriter} from "../../log-writer";
import {AgentsService} from "../../_services/agents/agents.service";

@Component({
  selector: 'app-agent-details',
  templateUrl: './agent-details.component.html',
  styleUrls: ['./agent-details.component.css']
})
export class AgentDetailsComponent implements OnInit {
  private log : LogWriter = new LogWriter("agent-details-component");

  constructor(private route: ActivatedRoute, private service: AgentsService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.log.debug(`got id: ${id}`);
  }

}
