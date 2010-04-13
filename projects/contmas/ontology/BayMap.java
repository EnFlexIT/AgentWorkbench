package contmas.ontology;

import jade.content.Concept;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
* Protege name: BayMap
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
*/
public class BayMap implements Concept{

	/**
	 * 
	 */
	private static final long serialVersionUID=8190273791175658779L;
	/**
	* Protege name: y_dimension
	   */
	private int y_dimension;

	public void setY_dimension(int value){
		this.y_dimension=value;
	}

	public int getY_dimension(){
		return this.y_dimension;
	}

	/**
	* Protege name: is_filled_with
	*/
	private List is_filled_with=new ArrayList();

	public void addIs_filled_with(BlockAddress elem){
		this.is_filled_with.add(elem);
	}

	public boolean removeIs_filled_with(BlockAddress elem){
		boolean result=this.is_filled_with.remove(elem);
		return result;
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

	public void setIs_filled_with(List l){
		this.is_filled_with=l;
	}

	/**
	* Protege name: z_dimension
	*/
	private int z_dimension;

	public void setZ_dimension(int value){
		this.z_dimension=value;
	}

	public int getZ_dimension(){
		return this.z_dimension;
	}

	/**
	* Protege name: x_dimension
	*/
	private int x_dimension;

	public void setX_dimension(int value){
		this.x_dimension=value;
	}

	public int getX_dimension(){
		return this.x_dimension;
	}

}
