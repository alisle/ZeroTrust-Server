import {UserCount} from "../UserCount";
import {AgentCount} from "../AgentCount";
import {ProcessCount} from "../ProcessCount";

export class ChartNode {
  constructor(public name, public value, public uuid) {}

  static convertUserCount(count : UserCount[]) : ChartNode[] {
    if(count == null) {
      return [];
    }

    return count.map( (user: UserCount) : ChartNode => {
      return new ChartNode(user.user, user.count, null);
    });
  }



  static convertAgentCount(count: AgentCount[]) : ChartNode[] {
    if(count == null) {
      return [];
    }

    return count.map( (agent: AgentCount ) : ChartNode => {
      if(agent.uuid == null) {
        // this is an outside node, we need to clean it up.
        return new ChartNode("unknown", agent.count, null);
      }

      return new ChartNode(agent.agent, agent.count, agent.uuid);

    });
  }

  static convertProcessCount(count : ProcessCount[]) : ChartNode[]  {
    if(count == null) {
      return [];
    }

    return count.map((process : ProcessCount) : ChartNode => {
      return new ChartNode(process.process, process.count, null);
    });
  }

}
