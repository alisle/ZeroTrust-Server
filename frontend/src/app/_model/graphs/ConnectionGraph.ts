import {ConnectionGraphNode} from "./ConnectionGraphNode";
import {ConnectionGraphEdge} from "./ConnectionGraphEdge";
import * as d3 from "d3";
import * as dagre from 'dagre';

export class ConnectionGraph {
  private maxOrder : number = 0;
  private buckets : Map<string, Map<string, ConnectionGraphNode>> = new Map<string, Map<string, ConnectionGraphNode>>();

  private nodes : Map<string, ConnectionGraphNode> = new Map<string, ConnectionGraphNode>();


  private width : number = 0;
  private edges : ConnectionGraphEdge[] = new Array();

  public constructor(private svg) {
    this.width = parseInt(svg.style("width"));
  };

  public addBucket(id: string) {
    this.buckets.set(id, new Map<string, ConnectionGraphNode>());
  }


  public addEdge(source : ConnectionGraphNode, destination : ConnectionGraphNode, pivot : number = -1) {
    this.edges.push(new ConnectionGraphEdge(source.id, destination.id, pivot));
  }


  public addNode(bucketName : string, node : ConnectionGraphNode) : void {
    let bucket = this.buckets.get(bucketName);
    if(node.id != null && node.label != null && bucket != null) {
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

    this.nodes.forEach((node : ConnectionGraphNode) => {
      graph.setNode(node.id, { label: node.label, width: node.label.length * 10,  height: 20 });
    });


    this.edges.forEach((edge : ConnectionGraphEdge) => {
      graph.setEdge(edge.source, edge.destination);
    });

    dagre.layout(graph);


    graph.nodes().forEach((name) => {
      let graphNode = graph.node(name);
      let node = this.nodes.get(name);
      node.index = graphNode.y - (graphNode.height / 2);
    });
  }

  private drawNode(node : ConnectionGraphNode, color: string, stroke: string) {
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
      .attr("x", node.x + (node.width / 2)+ "%")
      .attr("y", node.y + node.textOffset)
      .attr("dominant-baseline", "middle")
      .attr("text-anchor", "middle")
      .attr("fill", "black")
      .text(node.label);
  }

  private drawEdge(edge: ConnectionGraphEdge) {
    let source = this.nodes.get(edge.source);
    let destination = this.nodes.get(edge.destination);

    console.log(`drawing edge between ${source.y}, ${destination.y}`, source, destination);

    let x1 = source.getAbsoluteWidth(this.width);
    let y1 = source.getAbsoluteMidpointY(this.width);

    let x2 = destination.getAbsoluteX(this.width);
    let y2 = destination.getAbsoluteMidpointY(this.width);


    let points : [number, number][] = [];

    if(edge.pivot < 0) {
      points = [
        [ x1, y1 ],
        [ x2, y2 ],
      ];
    } else {
      points = [
        [ x1, y1 ],
        [ (this.width / 100) * edge.pivot, y1],
        [ x2, y2]
      ];
    }

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

    let colorScale = d3.scaleLinear().domain([0, this.nodes.size]).range([0, 1]);

    this.setMaxOrder();
    this.constructDagre();

    this.buckets.forEach((bucket : Map<String, ConnectionGraphNode>) => {
      this.setOrders(bucket);
    });

    let counter = 0;
    this.nodes.forEach((node : ConnectionGraphNode) => {
      let color = d3.interpolateRainbow(colorScale(counter));
      this.drawNode(node, color, "blue");

      counter = counter + 1;
    });

    this.edges.forEach((edge: ConnectionGraphEdge) => {
      this.drawEdge(edge);
    });


    this.setHeight();

    if(this.nodes.size == 0) {
      this.svg.append("rect")
        .attr("x", 0)
        .attr("y", 0)
        .attr("width", "100%")
        .attr("height", "100%")
        .attr("stroke-width", 0)
        .attr("fill", "white");

      this.svg.append("text")
        .attr("x","50%")
        .attr("y",  "50%")
        .attr("dominant-baseline", "middle")
        .attr("text-anchor", "middle")
        .attr("fill", "black")
        .text("No Links Found!");

    }

  }

  private setMaxOrder() {
    this.maxOrder = 0;
    this.buckets.forEach((bucket : Map<String,ConnectionGraphNode>) => {
      if(bucket.size > this.maxOrder) {
        this.maxOrder = bucket.size;
      }
    })
  }

  private setHeight() {
    let height = 0;
    this.nodes.forEach((node : ConnectionGraphNode) => { height = (node.getAbsoluteHeight() > height) ? node.getAbsoluteHeight() : height });


    if( height == 0) {
      // Okay we don't have anything to show, set it to 100px;
      height = 100;
    } else {
      height = height + 55;
    }
    this.svg.attr("height", height);
  }

  private setOrders(bucket: Map<String, ConnectionGraphNode>) {
    let orderStep : number = this.maxOrder / (bucket.size + 1);
    let orderedBucket : ConnectionGraphNode[] = [];

    bucket.forEach((node : ConnectionGraphNode) => orderedBucket.push(node) );
    orderedBucket = orderedBucket.sort((first : ConnectionGraphNode, second : ConnectionGraphNode) => first.index - second.index);
    orderedBucket.forEach((node : ConnectionGraphNode, index : number) =>  node.updatePosition(( index + 1 ) * orderStep ));
  }


}
