import {AfterViewInit, Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {LogWriter} from "../../log-writer";
import {ActivatedRoute} from "@angular/router";
import {ConnectionLinkService} from "../../_services/connection-links/connection-link.service";
import {ConnectionLink} from "../../_model/ConnectionLink";
import {LoadableObject} from "../../_model/LoadableObject";
import {Page} from "../../_model/page/page";
import {Agent} from "../../_model/Agent";
import {Connection} from "../../_model/Connection";
import {PageableClient} from "../../_services/pageable-client";


enum Type {
  SOURCE,
  DESTINATION
};

@Component({
  selector: 'app-connection-link-details',
  templateUrl: './connection-link-details.component.html',
  styleUrls: ['./connection-link-details.component.css']
})
export class ConnectionLinkDetailsComponent implements OnInit, AfterViewInit {
  private log: LogWriter = new LogWriter("connectionlinks-details-component");

  constructor(private route: ActivatedRoute, private service: ConnectionLinkService) { }

  linkLoad : LoadableObject<ConnectionLink> = new LoadableObject();
  connectionCountLoad : LoadableObject<number> = new LoadableObject(true);
  graphLoad : LoadableObject<Page<ConnectionLink>> = new LoadableObject(true);

  @Input()
  connectionID ? : string;

  connectionLink : ConnectionLink = null;
  connectionCount : number = 0;

  links : ConnectionLink[];

  @ViewChildren('diagram', { read: ElementRef })  diagramElementContainer: QueryList<ElementRef>;

  createClient(link : ConnectionLink) : PageableClient<ConnectionLink> {
    let client : PageableClient<ConnectionLink> = null;
    if(link.sourceAgent != null && link.destinationAgent != null) {
      client = this.service.connectionsBetweenAgents(link.sourceAgent.uuid, link.destinationAgent.uuid);
    } else if( link.sourceAgent != null  && link.destinationAgent == null) {
      client = this.service.connectionsBetweenSourceAgentandIP(link.sourceAgent.uuid, link.destinationString);
    } else if( link.sourceAgent == null && link.destinationAgent != null) {
      client = this.service.connectionsBetweenIPandDestinationAgent(link.sourceString, link.destinationAgent.uuid);
    }

    return client;
  }

  ngOnInit() {
    const id = (this.connectionID == null) ? this.route.snapshot.paramMap.get('id') : this.connectionID;

    this.linkLoad.bind(this.service.get(id, "DefaultConnectionLinkProjection"));
    this.linkLoad.value$.subscribe((link : ConnectionLink) => {
      this.connectionLink = link;
    });

    this.linkLoad.value$.subscribe((link : ConnectionLink) => {
      if(link != null) {
        let client : PageableClient<ConnectionLink> = this.createClient(link);
        if(client == null) {
          this.graphLoad.succeed(null);
          this.connectionCountLoad.succeed(0);
        } else {
          this.graphLoad.bind(client.page(0));
          this.connectionCountLoad.bind(client.totalElements());
        }
      }


    });

    this.connectionCountLoad.value$.subscribe((value : number) => {
      this.connectionCount = value;
    });

    this.graphLoad.value$.subscribe((page : Page<ConnectionLink>) => {
      if( page != null) {
        this.links = page.items;
      }
    });

  }

  public getSourceHeader(link : ConnectionLink) : string {
    return this.getHeader(Type.SOURCE, link);
  }

  public getDestinationHeader(link: ConnectionLink) : string {
    return this.getHeader(Type.DESTINATION, link);
  }


  public getHeader(type: Type, link: ConnectionLink) : string {
    let name = this.getName(type, link);
    let process = this.getProcess(type, link);
    return name + ":" + process;
  }

  public getName(type: Type, link: ConnectionLink) : string {
    switch (type) {
      case Type.SOURCE: {
        if(link.sourceAgentName != null) {
          return link.sourceAgentName;
        }

        if(link.sourceString != null) {
          return link.sourceString;
        }


      } break;
      case Type.DESTINATION: {
        if(link.destinationAgentName != null) {
          return link.destinationAgentName;
        }

        if(link.destinationString != null) {
          return link.destinationString;
        }
      } break;
    }

    return "unknown";
  }

  public getProcess(type: Type, link: ConnectionLink) : string {
    switch(type) {
      case Type.SOURCE: {
        if(link.sourceProcessName != null) {
          return link.sourceProcessName;
        }

        if(link.sourcePort != null) {
          return link.sourcePort + "";
        }
      } break;
      case Type.DESTINATION: {
        if(link.destinationProcessName != null) {
          return link.destinationProcessName;
        }

        if(link.destinationPort != null) {
          return link.destinationPort + "";
        }
      } break;
    }

    return "unknown";
  }


  ngAfterViewInit(): void {
  }

}
