import * as dagre from 'dagre';
import * as d3 from 'd3';

import {LogWriter} from "../../log-writer";
import {ElementRef} from "@angular/core";

class FlowGraphMetadata {
  public dimensions : [number, number];
  public margins : [number, number];
  public rankDir : string;

  constructor() {
    this.dimensions = [ 500, 500 ];
    this.margins = [20, 20];
    this.rankDir = "LR"
  }
}

export class FlowGraphService {
  private log: LogWriter = new LogWriter("flow-graph-service");

  private graph;
  private metadata : FlowGraphMetadata;
  private nodes : Set<string>;
  private line;
  private element : ElementRef;

  constructor() {
    this.nodes = new Set<string>();
    this.graph = new dagre.graphlib.Graph();
    this.metadata = new FlowGraphMetadata();
    this.graph.setGraph(this.metadata);
    this.graph.setDefaultEdgeLabel( () => { return {}; });
    this.updateMetaData();

    this.line = d3.line()
      .x((d: [number, number]) => { return d[0]; })
      .y((d: [number, number]) => { return d[1]; })
      .curve(d3.curveBasis);
  }

  private createLabel(name: string) : object {
    return {
      label: name,
      width: name.length * 10,
      height: 30
    };
  }

  private updateMetaData() : void {
    this.graph.setGraph({
      width: this.metadata.dimensions[0],
      height: this.metadata.dimensions[1],
      rankDir: this.metadata.rankDir,
      marginx: this.metadata.margins[0],
      marginy: this.metadata.margins[1]
    });
  }

  setElement(element: ElementRef) {
    this.element = element;
    this.draw();
  }

  setDimensions(width: number, height: number) {
    this.log.debug(`setting dimensions to ${width}:${height}`);
    this.metadata.dimensions = [ width, height ];
    this.updateMetaData();
  }

  setMargins(width: number, height: number) {
    this.log.debug(`setting margins to ${width}:${height}`);
    this.metadata.margins = [ width, height ];
    this.updateMetaData();
  }

  addNode(node: string, label: string = null) {
    if(label == null) {
      label = node;
    }

    if(!this.nodes.has(node.toLowerCase())) {
      this.log.debug(`adding node ${node} to graph`);
      this.nodes.add(node.toLowerCase());
      this.graph.setNode(node.toLowerCase(), this.createLabel(label));
    } else {
      this.log.debug(`skipping adding node ${node} as it already exists`);
    }
  }

  addEdge(first: string, second: string) {
    this.log.debug(`adding edge ${first} -> ${second}`);
    this.graph.setEdge(first.toLowerCase(), second.toLowerCase());
  }


  draw() : void {
    if ( this.element == null) {
      this.log.debug("element hasn't been defined, skipping drawing");
      return;
    }

    this.log.debug("creating graph on element", this.element);
    this.log.debug("creating layout");
    dagre.layout(this.graph);
    let width = 0;
    let height = 150;
    let svg = d3.select(this.element.nativeElement);

    this.graph.edges().forEach((e) => {
      let edge = this.graph.edge(e);
      this.log.debug("drawing edge", edge);

      // Because of some fuckery between d3 / dagre being JS and converting to TS we need to change this mapping
      // otherwise the compiler which bitch.

      let points : [number, number][] = [
        [ edge.points[0].x, edge.points[0].y ],
        [ edge.points[1].x, edge.points[1].y ],
        [ edge.points[2].x, edge.points[2].y ],
        ];

      svg.append("path")
        .attr("d", this.line(points))
        .attr("stroke", "blue")
        .attr("stroke-width", 1.5)
        .attr("fill", "none");
    });

    this.graph.nodes().forEach((n) => {
      let node = this.graph.node(n);
      this.log.debug("drawing node", node);

      if(node.x + node.width > width) {
        width = node.x + node.width;
      }

      if(node.y + node.height > height) {
        height = node.y + node.height;
      }


      svg.append("rect")
        .attr("x", node.x - (node.width / 2))
        .attr("y", node.y - (node.height / 2))
        .attr("width", node.width)
        .attr("height", node.height)
        .attr("rx", 5)
        .attr("ry", 5)
        .attr("fill", "lightblue")
        .attr("stroke", "blue")
        .attr("stroke-width", 1);

      svg.append("text")
        .attr("x", node.x)
        .attr("y", node.y)
        .attr("dominant-baseline", "middle")
        .attr("text-anchor", "middle")
        .text(node.label);
    });

    this.log.debug(`setting SVG width to ${width}`);

    width += 20;
    height += 20;

    svg.attr("width", width + "px");
    svg.attr("height", height + "px");
  }
}
