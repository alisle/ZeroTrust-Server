import {Component, Input, OnInit} from '@angular/core';
import {AgentCount} from "../../_model/AgentCount";
import {GraphScheme} from "../../_model/graphs/GraphScheme";
import {ChartNode} from "../../_model/graphs/ChartNode";
import {Observable} from "rxjs";

@Component({
  selector: 'app-network-tree-map',
  templateUrl: './network-tree-map.component.html',
  styleUrls: ['./network-tree-map.component.css']
})
export class NetworkTreeMapComponent implements OnInit {
  static currentID = 0;
  public id : string =  "NTMGRAPH" + NetworkTreeMapComponent.currentID;
  public scheme : GraphScheme = new GraphScheme();

  @Input()
  observable : Observable<AgentCount[]> = null;
  nodes : ChartNode[] = [];

  constructor() {
    NetworkTreeMapComponent.currentID += 1;
  }

  ngOnInit() {
    if(this.observable != null) {
      this.observable.subscribe((counts : AgentCount[] ) => {
        if(counts != null) {
          this.scheme.rainbow(counts);
          this.nodes = ChartNode.convertAgentCount(counts);
        }
      });
    }
  }

}
