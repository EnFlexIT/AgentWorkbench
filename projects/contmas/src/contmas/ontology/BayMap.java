package contmas.ontology;

import jade.content.Concept;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
 * Protege name: BayMap
 * 
 * @author ontology bean generator
 * @version 2010/03/3, 11:16:16
 */
public class BayMap implements Concept{

	/**
	 * 
	 */
	private static final long serialVersionUID=8190273791175658779L;
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
	 * Protege name: is_filled_with
	 */
	private List is_filled_with=new ArrayList();

	public void addIs_filled_with(BlockAddress elem){
		this.is_filled_with.add(elem);
	}

	public void clearAllIs_filled_with(){
		this.is_filled_with.clear();
	}

	public Iterator getAllIs_filled_with(){
		return this.is_filled_with.iterator();
	}

	public List getIs_filled_with(){
		return this.is_filled_with;
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

	public boolean removeIs_filled_with(BlockAddress elem){
		boolean result=this.is_filled_with.remove(elem);
		return result;
	}

	public void setIs_filled_with(List l){
		this.is_filled_with=l;
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
