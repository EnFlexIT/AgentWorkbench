package contmas.ontology;

import jade.content.AgentAction;

/**
* Protege name: RequestRandomBayMap
* @author ontology bean generator
* @version 2010/03/10, 15:08:52
*/
public class RequestRandomBayMap implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID= -478011342476951241L;
	/**
	* Protege name: y_dimension
	*/
	private int y_dimension;

	/**
	* Protege name: x_dimension
	*/
	private int x_dimension;

	/**
	* Protege name: z_dimension
	*/
	private int z_dimension;

	public int getX_dimension(){
		return this.x_dimension;
	}

	public int getY_dimension(){
		return this.y_dimension;
	}

	public int getZ_dimension(){
		return this.z_dimension;
	}

	public void setX_dimension(int value){
		this.x_dimension=value;
	}

	public void setY_dimension(int value){
		this.y_dimension=value;
	}

	public void setZ_dimension(int value){
		this.z_dimension=value;
	}

}
