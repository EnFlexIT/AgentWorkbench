package contmas.ontology;

import jade.core.AID;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
* Protege name: ContainerHolder
* @author ontology bean generator
* @version 2010/03/10, 15:08:52
*/
public class ContainerHolder extends AID{

	/**
	 * 
	 */
	private static final long serialVersionUID=8490113416971808762L;
	/**
	* Protege name: contractors
	*/
	private List contractors=new ArrayList();

	/**
	* Protege name: container_states
	*/
	private List container_states=new ArrayList();

	/**
	* Protege name: service_type
	*/
	private String service_type;

	/**
	* Protege name: contains
	*/
	private BayMap contains;

	/**
	* Protege name: lives_in
	*/
	private Domain lives_in;

	public void addContainer_states(TOCHasState elem){
		this.container_states.add(elem);
	}

	public void addContractors(AID elem){
		this.contractors.add(elem);
	}

	public void clearAllContainer_states(){
		this.container_states.clear();
	}

	public void clearAllContractors(){
		this.contractors.clear();
	}

	public Iterator getAllContainer_states(){
		return this.container_states.iterator();
	}

	public Iterator getAllContractors(){
		return this.contractors.iterator();
	}

	public List getContainer_states(){
		return this.container_states;
	}

	public BayMap getContains(){
		return this.contains;
	}

	public List getContractors(){
		return this.contractors;
	}

	public Domain getLives_in(){
		return this.lives_in;
	}

	public String getService_type(){
		return this.service_type;
	}

	public boolean removeContainer_states(TOCHasState elem){
		boolean result=this.container_states.remove(elem);
		return result;
	}

	public boolean removeContractors(AID elem){
		boolean result=this.contractors.remove(elem);
		return result;
	}

	public void setContainer_states(List l){
		this.container_states=l;
	}

	public void setContains(BayMap value){
		this.contains=value;
	}

	public void setContractors(List l){
		this.contractors=l;
	}

	public void setLives_in(Domain value){
		this.lives_in=value;
	}

	public void setService_type(String value){
		this.service_type=value;
	}

}
