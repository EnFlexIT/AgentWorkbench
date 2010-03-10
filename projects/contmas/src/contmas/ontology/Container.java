package contmas.ontology;

import jade.content.Concept;

/**
 * Protege name: Container
 * 
 * @author ontology bean generator
 * @version 2010/03/3, 11:16:16
 */
public class Container implements Concept{

	/**
	 * 
	 */
	private static final long serialVersionUID= -6741750554123390398L;
	/**
	 * Protege name: bic_code
	 */
	private String bic_code;

	/**
	 * Protege name: weight
	 */
	private float weight;

	/**
	 * Protege name: length
	 */
	private float length;

	/**
	 * Protege name: height
	 */
	private float height;

	/**
	 * Protege name: width
	 */
	private float width;

	public String getBic_code(){
		return this.bic_code;
	}

	public float getHeight(){
		return this.height;
	}

	public float getLength(){
		return this.length;
	}

	public float getWeight(){
		return this.weight;
	}

	public float getWidth(){
		return this.width;
	}

	public void setBic_code(String value){
		this.bic_code=value;
	}

	public void setHeight(float value){
		this.height=value;
	}

	public void setLength(float value){
		this.length=value;
	}

	public void setWeight(float value){
		this.weight=value;
	}

	public void setWidth(float value){
		this.width=value;
	}

}
