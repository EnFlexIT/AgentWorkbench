package contmas.ontology;

import jade.content.AgentAction;

/**
* Protege name: AssignHarborQuay
* @author ontology bean generator
* @version 2010/03/10, 15:08:52
*/
public class AssignHarborQuay implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID= -7686566620595609447L;
	/**
	* Protege name: assigned_quay
	*/
	private Quay assigned_quay;

	public Quay getAssigned_quay(){
		return this.assigned_quay;
	}

	public void setAssigned_quay(Quay value){
		this.assigned_quay=value;
	}

}
