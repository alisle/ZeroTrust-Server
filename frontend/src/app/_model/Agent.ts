import {IPAddress} from "./IPAddress";

export interface Agent {
  id: string;
  name: string;
  firstSeen: Date;
  alive: boolean;
  known: boolean;
  lastSeen: Date;
  connectionCount: number;
  aliveConnectionCount: number;
  addresses: IPAddress[];
}


