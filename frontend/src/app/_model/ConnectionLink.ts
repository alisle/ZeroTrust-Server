
import {Agent} from "./Agent";
import {Connection} from "./Connection";
import {LogWriter} from "../log-writer";

export interface ConnectionLink {

  uuid: string;
  timestamp: Date;
  ended: Date;
  duration: number;

  connectionHash: number;
  alive: boolean;

  sourceAgent: Agent;
  sourceAgentName: string;
  sourceConnection: Connection;
  sourceAddress: number;
  sourceString: string;
  sourcePort: number;
  sourceProcessName: string;

  destinationAgent: Agent;
  destinationAgentName: string;
  destinationConnection: Connection;
  destinationAddress: number;
  destinationString: string;
  destinationPort: number;
  destinationProcessName: string;
}
