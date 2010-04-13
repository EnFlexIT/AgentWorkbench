package contmas.ontology;

import jade.content.AgentAction;

/**
* Protege name: AcceptLoadOffer
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
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

	public void setLoad_offer(TransportOrderChain value){
		this.load_offer=value;
	}

	public TransportOrderChain getLoad_offer(){
		return this.load_offer;
	}

}
