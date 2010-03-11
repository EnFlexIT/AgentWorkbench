package contmas.ontology;

import jade.content.Concept;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
* Protege name: TransportOrderChain
* @author ontology bean generator
* @version 2010/03/10, 15:08:52
*/
public class TransportOrderChain implements Concept{

	/**
	 * 
	 */
	private static final long serialVersionUID=2756754330954242529L;
	/**
	* Protege name: transports
	*/
	private Container transports;

	/**
	* Protege name: terminates_at
	*/
	private String terminates_at;

	/**
	* Protege name: is_linked_by
	*/
	private List is_linked_by=new ArrayList();

	public void addIs_linked_by(TransportOrder elem){
		this.is_linked_by.add(elem);
	}

	public void clearAllIs_linked_by(){
		this.is_linked_by.clear();
	}

	public Iterator getAllIs_linked_by(){
		return this.is_linked_by.iterator();
	}

	public List getIs_linked_by(){
		return this.is_linked_by;
	}

	public String getTerminates_at(){
		return this.terminates_at;
	}

	public Container getTransports(){
		return this.transports;
	}

	public boolean removeIs_linked_by(TransportOrder elem){
		boolean result=this.is_linked_by.remove(elem);
		return result;
	}

	public void setIs_linked_by(List l){
		this.is_linked_by=l;
	}

	public void setTerminates_at(String value){
		this.terminates_at=value;
	}

	public void setTransports(Container value){
		this.transports=value;
	}

}
