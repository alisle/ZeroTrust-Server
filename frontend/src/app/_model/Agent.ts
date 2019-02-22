import {IPAddress} from "./IPAddress";

export interface Agent {
  uuid: string;
  name: string;
  firstSeen: Date;
  alive: boolean;
  known: boolean;
  lastSeen: Date;

  connectionCount: number;
  aliveConnectionCount: number;

  sourceConnectionCount: number;
  aliveSourceConnectionCount : number;

  destinationConnectionCount: number;
  aliveDestinationConnectionCount: number;

  addresses: IPAddress[];
}


