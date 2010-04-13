package contmas.ontology;

import jade.content.AgentAction;
import jade.core.AID;

/**
* Protege name: RequestOntologyRepresentation
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
*/
public class RequestOntologyRepresentation implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID= -7750853176110985545L;
	/**
	* Protege name: agent_in_question
	   */
	private AID agent_in_question;

	public void setAgent_in_question(AID value){
		this.agent_in_question=value;
	}

	public AID getAgent_in_question(){
		return this.agent_in_question;
	}

}
