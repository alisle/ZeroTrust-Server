import {Component, OnInit, ViewChild} from '@angular/core';
import {NetworksService} from "../../_services/networks/networks.service";
import {LogWriter} from "../../log-writer";
import {Network} from "../../_model/Network";
import {PageableDataSource} from "../../_services/pageable-data-source";
import {MatPaginator} from "@angular/material";
import {LoadableObject} from "../../_model/LoadableObject";
import {AgentCount} from "../../_model/AgentCount";

@Component({
  selector: 'app-networks',
  templateUrl: './networks.component.html',
  styleUrls: ['./networks.component.css']
})
export class NetworksComponent implements OnInit {
  private log : LogWriter = new LogWriter("networks.component");
  private networks : NetworkMeta[] = [];
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private service: NetworksService) { }
  dataSource = new PageableDataSource<Network>(this.service.allNetworks());

  ngOnInit() {
    this.dataSource.length$.subscribe(size => {
      this.log.debug(`setting total elemtns to ${size}`);
      this.paginator.length = size;
    });


    this.dataSource.pageSize$.subscribe(size => {
      this.log.debug(`setting size of page to ${size}`);
      this.paginator.pageSize = size;
    });

    this.dataSource.connect(null).subscribe(items => {
      if(items != null) {
        this.networks = [];
        items.forEach(network => {
          let meta = new NetworkMeta(network);
          meta.sourceObjects.bind(this.service.countActiveSourceConnections(network.uuid));
          meta.destinationObjects.bind(this.service.countActiveDestinationConnections(network.uuid));
          this.networks.push(meta);
        });
      }

    });

    this.dataSource.get(0);
  }

}


class NetworkMeta {
  public sourceObjects : LoadableObject<AgentCount[]> = new LoadableObject<AgentCount[]>(true);
  public destinationObjects : LoadableObject<AgentCount[]> = new LoadableObject<AgentCount[]>(true);

  constructor(public network : Network) {}
}
