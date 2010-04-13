package contmas.ontology;

import jade.content.AgentAction;

/**
* Protege name: AnnounceLoadStatus
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
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

	public void setLoad_status(String value){
		this.load_status=value;
	}

	public String getLoad_status(){
		return this.load_status;
	}

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
