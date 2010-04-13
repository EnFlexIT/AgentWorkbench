package contmas.ontology;

import jade.content.Concept;
import jade.core.AID;

/**
* Protege name: Designator
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
*/
public class Designator implements Concept{

	/**
	 * 
	 */
	private static final long serialVersionUID= -3374749115045652946L;
	/**
	* Protege name: concrete_designation
	   */
	private AID concrete_designation;

	public void setConcrete_designation(AID value){
		this.concrete_designation=value;
	}

	public AID getConcrete_designation(){
		return this.concrete_designation;
	}

	/**
	* Protege name: type
	*/
	private String type;

	public void setType(String value){
		this.type=value;
	}

	public String getType(){
		return this.type;
	}

	/**
	* Protege name: abstract_designation
	*/
	private Domain abstract_designation;

	public void setAbstract_designation(Domain value){
		this.abstract_designation=value;
	}

	public Domain getAbstract_designation(){
		return this.abstract_designation;
	}

}
