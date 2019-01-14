export interface Connection {
  id: string;
  connectionHash : number;
  start: Date;
  end: Date;
  duration: number;
  protocol: string;
  sourcePort: number;
  source: string;
  destinationPort: number;
  destination: string;
  username: string;
  userID: number;
  inode: number;
  pid: number;
  processName: string;
  commandLine: string;
}
