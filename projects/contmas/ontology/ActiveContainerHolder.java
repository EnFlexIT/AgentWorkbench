package contmas.ontology;

import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
* Protege name: ActiveContainerHolder
* @author ontology bean generator
* @version 2010/03/10, 15:08:52
*/
public class ActiveContainerHolder extends ContainerHolder{

	/**
	 * 
	 */
	private static final long serialVersionUID=8496180633867445239L;
	/**
	* Protege name: capable_of
	*/
	private List capable_of=new ArrayList();

	public void addCapable_of(Domain elem){
		this.capable_of.add(elem);
	}

	public void clearAllCapable_of(){
		this.capable_of.clear();
	}

	public Iterator getAllCapable_of(){
		return this.capable_of.iterator();
	}

	public List getCapable_of(){
		return this.capable_of;
	}

	public boolean removeCapable_of(Domain elem){
		boolean result=this.capable_of.remove(elem);
		return result;
	}

	public void setCapable_of(List l){
		this.capable_of=l;
	}

}
