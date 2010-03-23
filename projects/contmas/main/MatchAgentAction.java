/**
 * @author Hanno - Felix Wagner, 22.03.2010
 * Copyright 2010 Hanno - Felix Wagner
 * 
 * This file is part of ContMAS.
 *
 * ContMAS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ContMAS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ContMAS.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package contmas.main;

import contmas.agents.ContainerAgent;
import jade.content.AgentAction;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate.MatchExpression;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class MatchAgentAction implements MatchExpression{
	
	AgentAction actionToMatch;
	Agent myAgent;
	/**
	 * 
	 */
	public MatchAgentAction(Agent myAgent,AgentAction action){
		actionToMatch=action;
		this.myAgent=myAgent;
	}

	/* (non-Javadoc)
	 * @see jade.lang.acl.MessageTemplate.MatchExpression#match(jade.lang.acl.ACLMessage)
	 */
	@Override
	public boolean match(ACLMessage msg){
		AgentAction Content = ContainerAgent.extractAction(myAgent,msg);
		if(actionToMatch.getClass().isInstance(Content)){
			return true;
		}
		return false;
	}
}
