package contmas.ontology;

import jade.content.AgentAction;

/**
 * Protege name: ProvideBayMap
 * 
 * @author ontology bean generator
 * @version 2010/03/3, 11:16:16
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

	public BayMap getProvides(){
		return this.provides;
	}

	public void setProvides(BayMap value){
		this.provides=value;
	}

}
