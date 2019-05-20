import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {NetworksService} from "../../_services/networks/networks.service";
import {LogWriter} from "../../log-writer";
import {Network} from "../../_model/Network";
import {PageableDataSource} from "../../_services/pageable-data-source";
import {MatDialog, MatPaginator, MatSnackBar} from "@angular/material";
import {LoadableObject} from "../../_model/LoadableObject";
import {AgentCount} from "../../_model/AgentCount";
import {NetworkAddDialog} from "../network-add-dialog/network-add.dialog";
import {NetworkNewDTO} from "../../_model/NetworkNewDTO";
import {AuthService} from "../../_services/auth/auth.service";

@Component({
  selector: 'app-networks',
  templateUrl: './networks.component.html',
  styleUrls: ['./networks.component.css']
})
export class NetworksComponent implements OnInit {
  private log : LogWriter = new LogWriter("networks.component");
  private networks : NetworkMeta[] = [];
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private service: NetworksService, public authService: AuthService, public dialog: MatDialog, private snackBar: MatSnackBar ) { }

  dataSource = new PageableDataSource<Network>(this.service.allNetworks());

  openDialog(): void {
    this.log.debug("opening new network dialog");

    const dialogRef = this.dialog.open(NetworkAddDialog, {
      width: '500px',
      data : new NetworkNewDTO()
    });

    dialogRef.afterClosed().subscribe(result => {
      this.log.debug("the dialog was closed", result);
      if(result == null) {
        this.log.debug("the dialog was canceled");
      } else {
        let newNetwork : NetworkNewDTO = result;
        this.log.debug("this is the new network", newNetwork);

        this.service.add(newNetwork).subscribe(( success : boolean) => {
          this.snackBar.open('Network Added!', null, { duration: 2000 });
          this.dataSource.get(0);
        });
      }
    })
  }

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

