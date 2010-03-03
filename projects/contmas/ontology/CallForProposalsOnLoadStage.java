package contmas.ontology;

import jade.content.AgentAction;

/**
 * Protege name: CallForProposalsOnLoadStage
 * 
 * @author ontology bean generator
 * @version 2010/03/3, 11:16:16
 */
public class CallForProposalsOnLoadStage implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID= -1006014859113916450L;
	/**
	 * Protege name: required_turnover_capacity
	 */
	private LoadList required_turnover_capacity;

	public LoadList getRequired_turnover_capacity(){
		return this.required_turnover_capacity;
	}

	public void setRequired_turnover_capacity(LoadList value){
		this.required_turnover_capacity=value;
	}

}
