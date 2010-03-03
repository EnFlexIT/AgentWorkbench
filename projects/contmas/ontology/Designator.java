package contmas.ontology;

import jade.content.Concept;
import jade.core.AID;

/**
 * Protege name: Designator
 * 
 * @author ontology bean generator
 * @version 2010/03/3, 11:16:16
 */
public class Designator implements Concept{

	/**
	 * 
	 */
	private static final long serialVersionUID= -3374749115045652946L;
	/**
	 * Protege name: type
	 */
	private String type;

	/**
	 * Protege name: abstract_designation
	 */
	private Domain abstract_designation;

	/**
	 * Protege name: concrete_designation
	 */
	private AID concrete_designation;

	public Domain getAbstract_designation(){
		return this.abstract_designation;
	}

	public AID getConcrete_designation(){
		return this.concrete_designation;
	}

	public String getType(){
		return this.type;
	}

	public void setAbstract_designation(Domain value){
		this.abstract_designation=value;
	}

	public void setConcrete_designation(AID value){
		this.concrete_designation=value;
	}

	public void setType(String value){
		this.type=value;
	}

}
