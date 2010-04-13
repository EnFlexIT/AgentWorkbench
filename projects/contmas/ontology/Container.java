package contmas.ontology;

import jade.content.Concept;

/**
* Protege name: Container
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
*/
public class Container implements Concept{

	/**
	 * 
	 */
	private static final long serialVersionUID= -6741750554123390398L;
	/**
	* Protege name: width
	   */
	private float width;

	public void setWidth(float value){
		this.width=value;
	}

	public float getWidth(){
		return this.width;
	}

	/**
	* Protege name: length
	*/
	private float length;

	public void setLength(float value){
		this.length=value;
	}

	public float getLength(){
		return this.length;
	}

	/**
	* Protege name: height
	*/
	private float height;

	public void setHeight(float value){
		this.height=value;
	}

	public float getHeight(){
		return this.height;
	}

	/**
	* Protege name: bic_code
	*/
	private String bic_code;

	public void setBic_code(String value){
		this.bic_code=value;
	}

	public String getBic_code(){
		return this.bic_code;
	}

	/**
	* Protege name: weight
	*/
	private float weight;

	public void setWeight(float value){
		this.weight=value;
	}

	public float getWeight(){
		return this.weight;
	}

}
