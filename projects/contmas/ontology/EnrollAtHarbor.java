package contmas.ontology;

import jade.content.AgentAction;

/**
 * Protege name: EnrollAtHarbor
 * 
 * @author ontology bean generator
 * @version 2010/03/3, 11:16:16
 */
public class EnrollAtHarbor implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID=1503209431214053567L;
	/**
	 * Protege name: ship_length
	 */
	private float ship_length;

	public float getShip_length(){
		return this.ship_length;
	}

	public void setShip_length(float value){
		this.ship_length=value;
	}

}
