export interface Connection {
  id: string;
  connectionHash : number;
  start: Date;
  end: Date;
  duration: number;
  protocol: string;
  sourcePort: number;
  source: number;
  sourceString: string;
  destinationPort: number;
  destination: number;
  destinationString: string;
  username: string;
  userID: number;
  inode: number;
  pid: number;
  processName: string;
  commandLine: string;
}
