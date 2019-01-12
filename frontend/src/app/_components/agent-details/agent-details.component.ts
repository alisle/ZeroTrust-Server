import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {LogWriter} from "../../log-writer";
import {AgentsService} from "../../_services/agents/agents.service";
import {catchError, finalize} from "rxjs/operators";
import {of} from "rxjs";
import {Agent} from "../../_model/Agent";

@Component({
  selector: 'app-agent-details',
  templateUrl: './agent-details.component.html',
  styleUrls: ['./agent-details.component.css']
})
export class AgentDetailsComponent implements OnInit {
  private log : LogWriter = new LogWriter("agent-details-component");

  constructor(private route: ActivatedRoute, private service: AgentsService) { }

  loading : boolean = true;
  error : boolean = false;
  agent: Agent = null;

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.service.get(id)
      .pipe(
        catchError(() => {
          this.log.error("unable to find agent!");
          this.error = true;
          return of(null);
        }),
        finalize(() => {
          this.loading = false;
        })
      ).subscribe(agent => {
        console.log("got agent:", agent);
        this.agent = agent;
    })
  }

}
