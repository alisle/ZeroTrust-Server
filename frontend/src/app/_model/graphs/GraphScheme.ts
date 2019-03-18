import {ChartNode} from "./ChartNode";
import * as d3 from "d3";
import {LogWriter} from "../../log-writer";

export class GraphScheme {
  private log : LogWriter = new LogWriter("graphscheme");
  public domain : string[];

  rainbow(nodes : Object[]) : void {
    this.log.debug("got ndoes", nodes);
    let colorScale = d3.scaleLinear().domain([0, nodes.length]).range([0, 1]);
    this.domain =  nodes.map((node : Object, index : number) : string => {
      return d3.interpolateRainbow(colorScale(index));
    });
  }

}
