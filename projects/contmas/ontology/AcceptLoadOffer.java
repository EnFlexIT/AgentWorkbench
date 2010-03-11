package contmas.ontology;

import jade.content.AgentAction;

/**
* Protege name: AcceptLoadOffer
* @author ontology bean generator
* @version 2010/03/10, 15:08:52
*/
public class AcceptLoadOffer implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID= -2022690818463977434L;
	/**
	* Protege name: load_offer
	*/
	private TransportOrderChain load_offer;

	public TransportOrderChain getLoad_offer(){
		return this.load_offer;
	}

	public void setLoad_offer(TransportOrderChain value){
		this.load_offer=value;
	}

}
