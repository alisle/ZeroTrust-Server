
import {Agent} from "./Agent";
import {Connection} from "./Connection";

export interface ConnectionLink {
  timestamp: Date;
  connectionHash: number;
  alive: boolean;
  sourceAgent: Agent;
  destinationAgent: Agent;
  sourceConnection: Connection;
  destinationConnection: Connection;
}
