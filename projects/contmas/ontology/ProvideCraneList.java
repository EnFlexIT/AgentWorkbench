package contmas.ontology;

import jade.content.AgentAction;
import jade.core.AID;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
 * Protege name: ProvideCraneList
 * 
 * @author ontology bean generator
 * @version 2010/03/3, 11:16:16
 */
public class ProvideCraneList implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID= -1275201910898305533L;
	/**
	 * Protege name: available_cranes
	 */
	private List available_cranes=new ArrayList();

	public void addAvailable_cranes(AID elem){
		this.available_cranes.add(elem);
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

	public boolean removeAvailable_cranes(AID elem){
		boolean result=this.available_cranes.remove(elem);
		return result;
	}

	public void setAvailable_cranes(List l){
		this.available_cranes=l;
	}

}
