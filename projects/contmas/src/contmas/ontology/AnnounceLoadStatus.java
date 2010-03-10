package contmas.ontology;

import jade.content.AgentAction;

/**
* Protege name: AnnounceLoadStatus
* @author ontology bean generator
* @version 2010/03/10, 15:08:52
*/
public class AnnounceLoadStatus implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID= -3371270053413099171L;
	/**
	* Protege name: load_status
	*/
	private String load_status;

	/**
	* Protege name: load_offer
	*/
	private TransportOrderChain load_offer;

	public TransportOrderChain getLoad_offer(){
		return this.load_offer;
	}

	public String getLoad_status(){
		return this.load_status;
	}

	public void setLoad_offer(TransportOrderChain value){
		this.load_offer=value;
	}

	public void setLoad_status(String value){
		this.load_status=value;
	}

}
