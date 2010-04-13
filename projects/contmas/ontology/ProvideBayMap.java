package contmas.ontology;

import jade.content.AgentAction;

/**
* Protege name: ProvideBayMap
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
*/
public class ProvideBayMap implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID=5180900127414569991L;
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
