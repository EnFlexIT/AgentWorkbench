package contmas.ontology;

import jade.content.Concept;

/**
 * Protege name: TransportOrder
 * 
 * @author ontology bean generator
 * @version 2010/03/3, 11:16:16
 */
public class TransportOrder implements Concept{

	/**
	 * 
	 */
	private static final long serialVersionUID=4648227166113507592L;
	/**
	 * Protege name: takes
	 */
	private float takes;

	/**
	 * Protege name: starts_at
	 */
	private Designator starts_at;

	/**
	 * Protege name: ends_at
	 */
	private Designator ends_at;

	public Designator getEnds_at(){
		return this.ends_at;
	}

	public Designator getStarts_at(){
		return this.starts_at;
	}

	public float getTakes(){
		return this.takes;
	}

	public void setEnds_at(Designator value){
		this.ends_at=value;
	}

	public void setStarts_at(Designator value){
		this.starts_at=value;
	}

	public void setTakes(float value){
		this.takes=value;
	}

}
