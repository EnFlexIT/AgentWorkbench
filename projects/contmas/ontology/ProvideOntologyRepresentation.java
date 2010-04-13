package contmas.ontology;

import jade.content.AgentAction;

/**
* Protege name: ProvideOntologyRepresentation
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
*/
public class ProvideOntologyRepresentation implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID= -5004411116219592351L;
	/**
	* Protege name: according_ontrep
	   */
	private ContainerHolder according_ontrep;

	public void setAccording_ontrep(ContainerHolder value){
		this.according_ontrep=value;
	}

	public ContainerHolder getAccording_ontrep(){
		return this.according_ontrep;
	}

}
