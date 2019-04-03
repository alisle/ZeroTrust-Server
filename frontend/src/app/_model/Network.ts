export class Network {
  public uuid : string;
  public name : string;
  public description: string;

  public addressString : string;
  public maskString : string;

  public sourceConnectionCount : number;
  public destinationConnectionCount: number;

  public activeSourceConnectionCount : number;
  public activeDestinationConnectionCount: number;

  public agentNumber : number;
}
