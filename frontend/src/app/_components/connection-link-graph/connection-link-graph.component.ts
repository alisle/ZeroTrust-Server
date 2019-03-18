import {Component, Input, OnInit} from '@angular/core';
import * as d3 from 'd3';
import * as dagre from 'dagre';
import {LogWriter} from "../../log-writer";
import {ConnectionLink} from "../../_model/ConnectionLink";

@Component({
  selector: 'app-connection-link-graph',
  templateUrl: './connection-link-graph.component.html',
  styleUrls: ['./connection-link-graph.component.css']
})
export class ConnectionLinkGraphComponent implements OnInit {
  private log : LogWriter = new LogWriter("connection-link-graph-component");
  private svg = null;
  private maxHeight : number = 100;

  @Input()
  links : ConnectionLink[] = null;

  constructor() { }

  ngOnInit() {
    this.svg = d3.select("#graph");
    let graph = new Graph(this.svg);
    this.links.forEach((link : ConnectionLink) => {
      graph.addConnectionLink(link);
    });
    graph.draw();
  }

}

abstract class Node {
  public height : number = 20;
  public width: number = 10;
  public x: number;
  public y: number;
  public textX: number ;
  public textOffset: number = 10;
  public positionOffset : number = 40;
  public padding : number = 55;
  public index : number;

  protected constructor(public id : string, public label: string) {
    this.y = this.positionOffset + this.padding;
  }

  public updatePosition(order : number) {
    this.y = (order * this.positionOffset) + this.padding;
  }

  public getAbsoluteHeight() : number {
    console.log(`my height is ${this.y + this.height}`, this);
    return this.y + this.height;
  }

  public getAbsoluteWidth(frameWidth : number) : number {
    return (frameWidth / 100) * (this.width + this.x);
  }

  public getAbsoluteMidpointX(frameWidth : number) : number {
    return this.getAbsoluteWidth(frameWidth) - ((frameWidth / 100) * this.width);
  }

  public getAbsoluteMidpointY(frameWidth : number) : number {
    return this.getAbsoluteHeight() - (this.height / 2);
  }

  public getAbsoluteX(framewidth: number) : number {
    return (framewidth / 100) * this.x;
  }

}

class Edge {
  constructor(public source : string, public destination : string) {}

}

class OutgoingProcess extends Node {
  constructor(id: string, label: string, svg) {
    super(id, label);
    this.x = 30;
    this.textX = 35;
  }
}

class OutgoingUser extends Node {
  constructor(id: string, label: string, svg) {
    super(id, label);
    this.x = 16;
    this.textX = 21;
  }
}

class OutgoingMachine extends Node {
  constructor(id: string, label: string, svg) {
    super(id, label);
    this.x = 2;
    this.textX = 7;
  }
}



class IncomingPort extends Node {
  constructor(id : string, label: string, svg) {
    super(id, label);
    this.width = 5;
    this.x = 51;
    this.textX = 53.5;
  }
}

class IncomingProcess extends Node {
  constructor(id: string, label: string, svg) {
    super(id, label);
    this.x = 59;
    this.textX = 64;
  }
}

class IncomingUser extends Node {
  constructor(id: string, label: string, svg) {
    super(id, label);
    this.x = 73;
    this.textX = 78;
  }
}

class IncomingMachine extends Node {
  constructor(id: string, label: string, svg) {
    super(id, label);
    this.x = 87;
    this.textX = 92;
  }
}

class Graph {
  private log : LogWriter = new LogWriter("connection-link-graph");
  private maxOrder : number = 0;
  private nodes : Map<string, Node> = new Map<string, Node>();

  private outgoingProcesses : Map<string, Node> = new Map<string, Node>();
  private outgoingUser : Map<string, Node> = new Map<string, Node>();
  private outgoingMachine : Map<string, Node>  = new Map<string, Node>();

  private incomingPorts : Map<string, Node> = new Map<string, Node>();
  private incomingProcesses : Map<string, Node> = new Map<string, Node>();
  private incomingUser : Map<string, Node> = new Map<string, Node>();
  private incomingMachine : Map<string, Node>  = new Map<string, Node>();

  private width : number = 0;
  private edges : Edge[] = new Array();

  constructor(private svg) {
    this.width = parseInt(svg.style("width"));
  };

  public addConnectionLink(link: ConnectionLink) : void {
    let outgoingProcess = link.sourceProcessName;
    let outgoingUser = (link.sourceConnection != null) ? link.sourceConnection.username : null;
    let outgoingMachine = (link.sourceAgentName != null) ? link.sourceAgentName : link.sourceString;

    let incomingPort = link.destinationPort + "";
    let incomingProcess = link.destinationProcessName;
    let incomingUser =  (link.destinationConnection != null) ? link.destinationConnection.username : null;
    let incomingMachine = (link.destinationAgentName != null) ? link.destinationAgentName : link.destinationString;

    let outgoingMachineNode = this.addOutgoingMachine("outgoing_machine_" + outgoingMachine, outgoingMachine);
    let outgoingUserNode = this.addOutgoingUser("outgoing_user_" + outgoingUser, outgoingUser);
    let outgoingProcessNode = this.addOutgoingProcess("outgoing_process_" + outgoingProcess, outgoingProcess);

    let incomingPortNode = this.addIncomingPort("incoming_port_" + incomingPort, incomingPort);
    let incomingProcessNode = this.addIncomingProcess("incoming_process_" + incomingProcess, incomingProcess);
    let incomingUserNode = this.addIncomingUser("incoming_user_" + incomingUser, incomingUser);
    let incomingMachineNode = this.addIncomingMachine("incoming_machine" + incomingMachine, incomingMachine);



    if(outgoingUser != null) {
      this.addEdge(outgoingMachineNode, outgoingUserNode);
      if(outgoingProcess != null) {
        this.addEdge(outgoingUserNode, outgoingProcessNode);
        this.addEdge(outgoingProcessNode, incomingPortNode);
      } else {
        this.addEdge(outgoingUserNode, incomingPortNode);
      }
    } else {
      this.addEdge(outgoingMachineNode, incomingPortNode);
    }

    if(incomingProcess != null) {
      this.addEdge(incomingPortNode, incomingProcessNode);
      if(incomingUser != null) {
        this.addEdge(incomingProcessNode, incomingUserNode);
        this.addEdge(incomingUserNode, incomingMachineNode);
      } else {
        this.addEdge(incomingProcessNode, incomingMachineNode);
      }
    } else {
      this.addEdge(incomingPortNode, incomingMachineNode)
    }

  }

  public addEdge(source : Node, destination : Node) {
    this.edges.push(new Edge(source.id, destination.id));
}

  public addOutgoingProcess(id: string, label: string) : OutgoingProcess {
    let node = new OutgoingProcess(id, label, this.svg);
    this.addNode(this.outgoingProcesses, node);
    return node;
  }

  public addOutgoingUser(id: string, label: string) : OutgoingUser {
    let node = new OutgoingUser(id, label, this.svg);
    this.addNode(this.outgoingUser, node);
    return node;
  }

  public addOutgoingMachine(id: string, label: string) : OutgoingMachine {
    let node = new OutgoingMachine(id, label, this.svg);
    this.addNode(this.outgoingMachine, node);
    return node;
  }

  public addIncomingPort(id : string, label: string) : IncomingPort {
    let node = new IncomingPort(id, label, this.svg);
    this.addNode(this.incomingPorts, node);
    return node;
  }

  public addIncomingProcess(id: string, label: string) : OutgoingProcess {
    let node = new IncomingProcess(id, label, this.svg);
    this.addNode(this.incomingProcesses, node);
    return node;
  }

  public addIncomingUser(id: string, label: string) : OutgoingUser {
    let node = new IncomingUser(id, label, this.svg);
    this.addNode(this.incomingUser, node);
    return node;
  }

  public addIncomingMachine(id: string, label: string) : OutgoingMachine {
    let node = new IncomingMachine(id, label, this.svg);
    this.addNode(this.incomingMachine, node);
    return node;
  }


  private addNode(bucket : Map<String, Node>, node : Node) : void {
    if(node.id != null && node.label != null) {
      this.nodes.set(node.id, node);
      bucket.set(node.id, node);
    }
  }

  public constructDagre() : void {
    let graph = new dagre.graphlib.Graph();

    graph.setGraph({
      rankDir: "LR",
      dimensions: [ 1000, 1000 ],
      margins: [0, 0]
    });
    graph.setDefaultEdgeLabel( () => { return {}; });

    this.nodes.forEach((node : Node) => {
      graph.setNode(node.id, { label: node.label, width: node.label.length * 10,  height: 20 });
    });


    this.edges.forEach((edge : Edge) => {
      graph.setEdge(edge.source, edge.destination);
    });

    dagre.layout(graph);


    graph.nodes().forEach((name) => {
      let graphNode = graph.node(name);
      let node = this.nodes.get(name);
      node.index = graphNode.y - (graphNode.height / 2);
    });
  }

  private drawNode(node : Node, color: string, stroke: string) {
    this.svg.append("rect")
      .attr("height", node.height + "px")
      .attr("width", node.width + "%")
      .attr("x", node.x + "%")
      .attr("y",  node.y)
      .attr("rx", 2)
      .attr("ry", 2)
      .attr("stroke", stroke)
      .attr("stroke-width", 1)
      .attr("fill", color);

    this.svg.append("text")
      .attr("x", node.textX + "%")
      .attr("y", node.y + node.textOffset)
      .attr("dominant-baseline", "middle")
      .attr("text-anchor", "middle")
      .attr("fill", "black")
      .text(node.label);
  }

  private drawEdge(edge: Edge, color: string) {
    let source = this.nodes.get(edge.source);
    let destination = this.nodes.get(edge.destination);

    console.log(`drawing edge between ${source.y}, ${destination.y}`, source, destination);

    let x1 = source.getAbsoluteWidth(this.width);
    let y1 = source.getAbsoluteMidpointY(this.width);

    let x2 = destination.getAbsoluteX(this.width);
    let y2 = destination.getAbsoluteMidpointY(this.width);

    let points : [number, number][] = [
      [ x1, y1 ],
      [ x2, y2 ],
    ];

    let line = d3.line()
      .x((d: [number, number]) => { return d[0]; })
      .y((d: [number, number]) => { return d[1]; })
      .curve(d3.curveStep);


    this.svg.append("path")
      .attr("d", line(points))
      .attr("stroke", "blue")
      .attr("stroke-width", 2)
      .attr("fill", "none");

  }

  public draw() : void {
    let buckets : Map<String, Node>[] = [
      this.outgoingProcesses,
      this.outgoingUser,
      this.outgoingMachine,
      this.incomingPorts,
      this.incomingProcesses,
      this.incomingUser,
      this.incomingMachine
    ];


    let colorScale = d3.scaleLinear().domain([0, this.nodes.size]).range([0, 1]);

    this.setMaxOrder(buckets);
    this.constructDagre();

    buckets.forEach((bucket : Map<String, Node>) => {
      this.setOrders(bucket);
    });

    let counter = 0;
    this.nodes.forEach((node : Node) => {
      let color = d3.interpolateRainbow(colorScale(counter));
      this.drawNode(node, color, "blue");

      counter = counter + 1;
    });

    this.edges.forEach((edge: Edge) => {
      this.drawEdge(edge, "blue");
    });


    this.setHeight();
  }

  private setMaxOrder(buckets : Map<String,Node>[]) {
    this.maxOrder = 0;
    buckets.forEach((bucket : Map<String,Node>) => {
      if(bucket.size > this.maxOrder) {
        this.maxOrder = bucket.size;
      }
    })
  }

  private setHeight() {
    let height = 0;
    this.nodes.forEach((node : Node) => { height = (node.getAbsoluteHeight() > height) ? node.getAbsoluteHeight() : height });
    height = height + 55;
    this.svg.attr("height", height);
  }

  private setOrders(bucket: Map<String, Node>) {
    let orderStep : number = this.maxOrder / (bucket.size + 1);
    let orderedBucket : Node[] = [];

    bucket.forEach((node : Node) => orderedBucket.push(node) );
    orderedBucket = orderedBucket.sort((first : Node, second : Node) => first.index - second.index);
    orderedBucket.forEach((node : Node, index : number) =>  node.updatePosition(( index + 1 ) * orderStep ));
  }


}
