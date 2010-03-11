package contmas.ontology;

import jade.content.AgentAction;

/**
* Protege name: ProvidePopulatedBayMap
* @author ontology bean generator
* @version 2010/03/10, 15:08:52
*/
public class ProvidePopulatedBayMap implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID= -3730304865923255074L;
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
