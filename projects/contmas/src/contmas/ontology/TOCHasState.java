package contmas.ontology;

import jade.content.Concept;

/**
 * Protege name: TOCHasState
 * 
 * @author ontology bean generator
 * @version 2010/03/3, 11:16:16
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

	/**
	 * Protege name: state
	 */
	private TransportOrderChainState state;

	public TransportOrderChainState getState(){
		return this.state;
	}

	public TransportOrderChain getSubjected_toc(){
		return this.subjected_toc;
	}

	public void setState(TransportOrderChainState value){
		this.state=value;
	}

	public void setSubjected_toc(TransportOrderChain value){
		this.subjected_toc=value;
	}

}
