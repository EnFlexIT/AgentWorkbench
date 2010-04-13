package contmas.ontology;

import jade.content.AgentAction;

/**
* Protege name: RequestRandomLoadSequence
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
*/
public class RequestRandomLoadSequence implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID= -6862984917307693932L;
	/**
	* Protege name: provides
	   */
	private BayMap provides;

	public void setProvides(BayMap value){
		this.provides=value;
	}

	public BayMap getProvides(){
		return this.provides;
	}

}
