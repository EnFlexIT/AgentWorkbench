package contmas.ontology;

import jade.content.AgentAction;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
* Protege name: ProvideHarbourSetup
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
*/
public class ProvideHarbourSetup implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID= -7503649385328423980L;
	/**
	* Protege name: current_harbour_layout
	   */
	private Domain current_harbour_layout;

	public void setCurrent_harbour_layout(Domain value){
		this.current_harbour_layout=value;
	}

	public Domain getCurrent_harbour_layout(){
		return this.current_harbour_layout;
	}

	/**
	* Protege name: currently_active_container_holders
	*/
	private List currently_active_container_holders=new ArrayList();

	public void addCurrently_active_container_holders(ContainerHolder elem){
		this.currently_active_container_holders.add(elem);
	}

	public boolean removeCurrently_active_container_holders(ContainerHolder elem){
		boolean result=this.currently_active_container_holders.remove(elem);
		return result;
	}

	public void clearAllCurrently_active_container_holders(){
		this.currently_active_container_holders.clear();
	}

	public Iterator getAllCurrently_active_container_holders(){
		return this.currently_active_container_holders.iterator();
	}

	public List getCurrently_active_container_holders(){
		return this.currently_active_container_holders;
	}

	public void setCurrently_active_container_holders(List l){
		this.currently_active_container_holders=l;
	}

}
