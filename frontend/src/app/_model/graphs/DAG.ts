import {DAGEdge} from "./DAGEdge";
import {DAGNode} from "./DAGNode";
import {LogWriter} from "../../log-writer";

export class DAG {
  private log : LogWriter = new LogWriter("DAG");
  private knownNodes : Set<string>;
  private knownEdges : Set<string>;

  constructor(public nodes: DAGNode[] = [], public edges : DAGEdge[] = []) {
    this.knownEdges = new Set<string>();
    this.knownNodes = new Set<string>();

    nodes.forEach((node : DAGNode) => {
      this.knownNodes.add(node.id);
    });

    edges.forEach((edge: DAGEdge) => {
      this.knownEdges.add(edge.id);
    })
  };

  addNode(id: string, label: string) : void {
    if(!this.knownNodes.has(id)) {
      let node = new DAGNode(id, label)
      this.log.debug("adding node", node);

      this.nodes.push(node);
      this.knownNodes.add(id);
    }
  }

  addEdge(id: string, source: string, target: string, label: string) : void {
    if(!this.knownEdges.has(id)) {
      let edge = new DAGEdge(id, source, target, label);
      this.log.debug("adding edge", edge);

      this.edges.push(edge);
      this.knownEdges.add(id);
    }
  }

}
