package contmas.ontology;

import jade.content.AgentAction;
import jade.core.AID;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
* Protege name: AssignHarborQuay
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
*/
public class AssignHarborQuay implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID=1952149312666441386L;
	/**
	* Protege name: assigned_quay
	   */
	private Quay assigned_quay;

	public void setAssigned_quay(Quay value){
		this.assigned_quay=value;
	}

	public Quay getAssigned_quay(){
		return this.assigned_quay;
	}

	/**
	* Protege name: available_cranes
	*/
	private List available_cranes=new ArrayList();

	public void addAvailable_cranes(AID elem){
		this.available_cranes.add(elem);
	}

	public boolean removeAvailable_cranes(AID elem){
		boolean result=this.available_cranes.remove(elem);
		return result;
	}

	public void clearAllAvailable_cranes(){
		this.available_cranes.clear();
	}

	public Iterator getAllAvailable_cranes(){
		return this.available_cranes.iterator();
	}

	public List getAvailable_cranes(){
		return this.available_cranes;
	}

	public void setAvailable_cranes(List l){
		this.available_cranes=l;
	}

}
