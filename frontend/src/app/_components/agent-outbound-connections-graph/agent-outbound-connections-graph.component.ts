import {Component, Input, OnInit} from '@angular/core';
import {ConnectionLink} from "../../_model/ConnectionLink";
import * as d3 from 'd3';
import * as dagre from 'dagre';
import {ConnectionGraphNode} from "../../_model/graphs/ConnectionGraphNode";
import {ConnectionGraph} from "../../_model/graphs/ConnectionGraph";

@Component({
  selector: 'app-agent-outbound-connections-graph',
  templateUrl: './agent-outbound-connections-graph.component.html',
  styleUrls: ['./agent-outbound-connections-graph.component.css']
})
export class AgentOutboundConnectionsGraphComponent implements OnInit {
  private svg = null;
  private graph : ConnectionGraph = null;

  @Input()
  links: ConnectionLink[] = [];

  constructor() {
  }

  ngOnInit() {
    this.svg = d3.select("#graph");
    this.graph = this.setupGraph(this.svg);

    this.links.forEach((link: ConnectionLink) => {
      this.addConnectionLink(link);
    });

    this.graph.draw();

  }

  private addConnectionLink(link : ConnectionLink) : void {

    let outboundAgent = (link.sourceAgentName != null) ? link.sourceAgentName : null;
    let outboundInterface = link.sourceString;
    let outboundProcess = (link.sourceConnection != null) ? link.sourceConnection.processName : null;

    let inboundPort = link.destinationPort + "";
    let inboundInterface = link.destinationString;
    let inboundProcess = (link.destinationConnection != null) ? link.destinationConnection.processName : null;
    let inboundAgent = (link.destinationAgentName != null) ? link.destinationAgentName : null;


    let outboundAgentNode = new OutboundAgent(outboundAgent, outboundAgent);
    let outboundInterfaceNode = new OutboundInterface(outboundInterface, outboundInterface);
    let outboundProcessNode = new OutboundProcess(outboundProcess, outboundProcess);
    let inboundPortNode = new InboundPort(inboundPort, inboundPort);
    let inboundInterfaceNode = new InboundInterface(inboundInterface, inboundInterface);
    let inboundProcessNode = new InboundProcess(inboundProcess, inboundProcess);
    let inboundAgentNode = new InboundAgent(inboundAgent, inboundAgent);

    this.graph.addNode("outbound_agents", outboundAgentNode);
    this.graph.addNode("outbound_interfaces", outboundInterfaceNode);
    this.graph.addNode("outbound_processes", outboundProcessNode);
    this.graph.addNode("inbound_ports", inboundPortNode);
    this.graph.addNode("inbound_interfaces", inboundInterfaceNode);
    this.graph.addNode("inbound_processes", inboundProcessNode);
    this.graph.addNode("inbound_agents", inboundAgentNode);

    if(outboundAgent != null) {
      this.graph.addEdge(outboundAgentNode, outboundInterfaceNode);
    }

    if(outboundProcess != null) {
      this.graph.addEdge(outboundInterfaceNode, outboundProcessNode);
      this.graph.addEdge(outboundProcessNode, inboundPortNode);
    } else {
      this.graph.addEdge(outboundInterfaceNode, inboundPortNode);
    }

    this.graph.addEdge(inboundPortNode, inboundInterfaceNode);

    if(inboundProcess != null) {
      this.graph.addEdge(inboundInterfaceNode, inboundProcessNode);
      this.graph.addEdge(inboundProcessNode, inboundAgentNode);
    }

  }

  private setupGraph(svg) : ConnectionGraph {
    let graph = new ConnectionGraph(svg);

    graph.addBucket("outbound_agents");
    graph.addBucket("outbound_interfaces");
    graph.addBucket("outbound_processes");
    graph.addBucket("inbound_ports");
    graph.addBucket("inbound_interfaces");
    graph.addBucket("inbound_processes");
    graph.addBucket("inbound_agents");

    return graph;
  }
}



class OutboundAgent extends ConnectionGraphNode {
  constructor(id: string, label: string) {
    super("outbound_agent_" + id, label);
    this.x = 2;
  }
}

class OutboundInterface extends ConnectionGraphNode {
  constructor(id: string, label: string) {
    super("outbound_interface_" + id, label);
    this.x = 17;
  }
}

class OutboundProcess extends ConnectionGraphNode {
  constructor(id: string, label: string) {
    super("outbound_process_" + id, label);
    this.x = 33;
  }
}

class InboundPort extends ConnectionGraphNode {
  constructor(id: string, label: string) {
    super("inbound_port_" + id, label);
    this.x = 46;
    this.width = 7;
  }
}


class InboundInterface extends ConnectionGraphNode {
  constructor(id: string, label: string) {
    super("inbound_interface_" + id, label);
    this.x = 57;
  }
}


class InboundProcess extends ConnectionGraphNode {
  constructor(id: string, label: string) {
    super("inbound_process_" + id, label);
    this.x = 73;
  }
}

class InboundAgent extends ConnectionGraphNode {
  constructor(id: string, label: string) {
    super("inbound_agent_" + id, label)
    this.x = 87;
  }
}

