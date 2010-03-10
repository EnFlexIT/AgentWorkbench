package contmas.ontology;

import jade.content.AgentAction;

/**
* Protege name: GetCraneList
* @author ontology bean generator
* @version 2010/03/10, 15:08:52
*/
public class GetCraneList implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID= -3405083597435633066L;
	/**
	* Protege name: assigned_quay
	*/
	private Quay assigned_quay;

	/**
	* Protege name: required_turnover_capacity
	*/
	private LoadList required_turnover_capacity;

	public Quay getAssigned_quay(){
		return this.assigned_quay;
	}

	public LoadList getRequired_turnover_capacity(){
		return this.required_turnover_capacity;
	}

	public void setAssigned_quay(Quay value){
		this.assigned_quay=value;
	}

	public void setRequired_turnover_capacity(LoadList value){
		this.required_turnover_capacity=value;
	}

}
