package contmas.ontology;

import jade.content.Concept;

/**
* Protege name: TOCHasState
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
*/
public class TOCHasState implements Concept{

	/**
	 * 
	 */
	private static final long serialVersionUID= -339143260998409724L;
	/**
	* Protege name: subjected_toc
	   */
	private TransportOrderChain subjected_toc;

	public void setSubjected_toc(TransportOrderChain value){
		this.subjected_toc=value;
	}

	public TransportOrderChain getSubjected_toc(){
		return this.subjected_toc;
	}

	/**
	* Protege name: state
	*/
	private TransportOrderChainState state;

	public void setState(TransportOrderChainState value){
		this.state=value;
	}

	public TransportOrderChainState getState(){
		return this.state;
	}

}
