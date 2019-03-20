import {Component, Input, OnInit} from '@angular/core';
import * as d3 from 'd3';

import {ConnectionLink} from "../../_model/ConnectionLink";
import {ConnectionGraphNode} from "../../_model/graphs/ConnectionGraphNode";
import {ConnectionGraph} from "../../_model/graphs/ConnectionGraph";

@Component({
  selector: 'app-connection-link-graph',
  templateUrl: './connection-link-graph.component.html',
  styleUrls: ['./connection-link-graph.component.css']
})
export class ConnectionLinkGraphComponent implements OnInit {
  private svg = null;
  private graph : ConnectionGraph = null;
  @Input()
  links : ConnectionLink[] = null;

  constructor() { }

  ngOnInit() {
    this.svg = d3.select("#graph");
    this.graph = this.setupGraph(this.svg);
    this.links.forEach((link : ConnectionLink) => {
      this.addConnectionLink(link);
    });
    this.graph.draw();
  }

  private setupGraph(svg) : ConnectionGraph {
    let graph = new ConnectionGraph(svg);
    graph.addBucket("outgoing_processes");
    graph.addBucket("outgoing_users");
    graph.addBucket("outgoing_machines");
    graph.addBucket("incoming_ports");
    graph.addBucket("incoming_processes");
    graph.addBucket("incoming_users");
    graph.addBucket("incoming_machines");

    return graph;
  }

  private addConnectionLink(link : ConnectionLink) : void {

    let outgoingProcess = link.sourceProcessName;
    let outgoingUser = (link.sourceConnection != null) ? link.sourceConnection.username : null;
    let outgoingMachine = (link.sourceAgentName != null) ? link.sourceAgentName : link.sourceString;

    let incomingPort = link.destinationPort + "";
    let incomingProcess = link.destinationProcessName;
    let incomingUser =  (link.destinationConnection != null) ? link.destinationConnection.username : null;
    let incomingMachine = (link.destinationAgentName != null) ? link.destinationAgentName : link.destinationString;


    let outgoingMachineNode = new OutgoingMachine(outgoingMachine, outgoingMachine);
    let outgoingUserNode = new OutgoingUser(outgoingUser, outgoingUser);
    let outgoingProcessNode = new OutgoingProcess(outgoingProcess, outgoingProcess);

    let incomingPortNode = new IncomingPort(incomingPort, incomingPort);
    let incomingProcessNode = new IncomingProcess(incomingProcess, incomingProcess);
    let incomingUserNode = new IncomingUser(incomingUser, incomingUser);
    let incomingMachineNode = new IncomingMachine(incomingMachine, incomingMachine);


    this.graph.addNode("outgoing_processes", outgoingProcessNode);
    this.graph.addNode("outgoing_users", outgoingUserNode);
    this.graph.addNode("outgoing_machines", outgoingMachineNode);
    this.graph.addNode("incoming_ports", incomingPortNode);
    this.graph.addNode("incoming_processes", incomingProcessNode);
    this.graph.addNode("incoming_users", incomingUserNode);
    this.graph.addNode("incoming_machines", incomingMachineNode);


    if(outgoingUser != null) {
      this.graph.addEdge(outgoingMachineNode, outgoingUserNode);
      if(outgoingProcess != null) {
        this.graph.addEdge(outgoingUserNode, outgoingProcessNode);
        this.graph.addEdge(outgoingProcessNode, incomingPortNode);
      } else {
        this.graph.addEdge(outgoingUserNode, incomingPortNode);
      }
    } else {
      this.graph.addEdge(outgoingMachineNode, incomingPortNode);
    }

    if(incomingProcess != null) {
      this.graph.addEdge(incomingPortNode, incomingProcessNode);
      if(incomingUser != null) {
        this.graph.addEdge(incomingProcessNode, incomingUserNode);
        this.graph.addEdge(incomingUserNode, incomingMachineNode);
      } else {
        this.graph.addEdge(incomingProcessNode, incomingMachineNode);
      }
    } else {
      this.graph.addEdge(incomingPortNode, incomingMachineNode)
    }

  }

}



class OutgoingProcess extends ConnectionGraphNode {
  constructor(id: string, label: string) {
    super("outgoing_process_" + id, label);
    this.x = 30;
  }
}

class OutgoingUser extends ConnectionGraphNode {
  constructor(id: string, label: string) {
    super("outgoing_user_" + id, label);
    this.x = 16;
  }
}

class OutgoingMachine extends ConnectionGraphNode {
  constructor(id: string, label: string) {
    super("outgoing_machine_" + id, label);
    this.x = 2;
  }
}



class IncomingPort extends ConnectionGraphNode {
  constructor(id : string, label: string) {
    super("incoming_port_" + id, label);
    this.width = 5;
    this.x = 51;
  }
}

class IncomingProcess extends ConnectionGraphNode {
  constructor(id: string, label: string) {
    super("incoming_process_" + id, label);
    this.x = 59;
  }
}

class IncomingUser extends ConnectionGraphNode {
  constructor(id: string, label: string) {
    super("incoming_user_" + id, label);
    this.x = 73;
  }
}

class IncomingMachine extends ConnectionGraphNode {
  constructor(id: string, label: string) {
    super("incoming_machine_" + id, label);
    this.x = 87;
  }
}

