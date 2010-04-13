package contmas.ontology;

import jade.content.Concept;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
* Protege name: LoadList
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
*/
public class LoadList implements Concept{

	/**
	 * 
	 */
	private static final long serialVersionUID=5334624132980391537L;
	/**
	* Protege name: next_step
	   */
	private LoadList next_step;

	public void setNext_step(LoadList value){
		this.next_step=value;
	}

	public LoadList getNext_step(){
		return this.next_step;
	}

	/**
	* Protege name: consists_of
	*/
	private List consists_of=new ArrayList();

	public void addConsists_of(TransportOrderChain elem){
		this.consists_of.add(elem);
	}

	public boolean removeConsists_of(TransportOrderChain elem){
		boolean result=this.consists_of.remove(elem);
		return result;
	}

	public void clearAllConsists_of(){
		this.consists_of.clear();
	}

	public Iterator getAllConsists_of(){
		return this.consists_of.iterator();
	}

	public List getConsists_of(){
		return this.consists_of;
	}

	public void setConsists_of(List l){
		this.consists_of=l;
	}

}
