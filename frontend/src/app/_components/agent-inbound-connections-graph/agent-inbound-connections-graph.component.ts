import {Component, Input, OnInit} from '@angular/core';
import {ConnectionLink} from "../../_model/ConnectionLink";

@Component({
  selector: 'app-agent-inbound-connections-graph',
  templateUrl: './agent-inbound-connections-graph.component.html',
  styleUrls: ['./agent-inbound-connections-graph.component.css']
})
export class AgentInboundConnectionsGraphComponent implements OnInit {

  @Input()
  links : ConnectionLink[];

  constructor() { }

  ngOnInit() {
  }

}
