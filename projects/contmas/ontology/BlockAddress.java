package contmas.ontology;

import jade.content.Concept;

/**
 * Protege name: BlockAddress
 * 
 * @author ontology bean generator
 * @version 2010/03/3, 11:16:16
 */
public class BlockAddress implements Concept{

	/**
	 * 
	 */
	private static final long serialVersionUID= -5530157105637994040L;
	/**
	 * Protege name: x_dimension
	 */
	private int x_dimension;

	/**
	 * Protege name: y_dimension
	 */
	private int y_dimension;

	/**
	 * Protege name: z_dimension
	 */
	private int z_dimension;

	/**
	 * Protege name: locates
	 */
	private Container locates;

	public Container getLocates(){
		return this.locates;
	}

	public int getX_dimension(){
		return this.x_dimension;
	}

	public int getY_dimension(){
		return this.y_dimension;
	}

	public int getZ_dimension(){
		return this.z_dimension;
	}

	public void setLocates(Container value){
		this.locates=value;
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
