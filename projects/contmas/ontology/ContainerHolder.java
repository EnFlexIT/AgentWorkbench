package contmas.ontology;

import jade.core.AID;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
* Protege name: ContainerHolder
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
*/
public class ContainerHolder extends AID{

	/**
	 * 
	 */
	private static final long serialVersionUID=1968086081770972203L;
	/**
	* Protege name: contractors
	   */
	private List contractors=new ArrayList();

	public void addContractors(AID elem){
		this.contractors.add(elem);
	}

	public boolean removeContractors(AID elem){
		boolean result=this.contractors.remove(elem);
		return result;
	}

	public void clearAllContractors(){
		this.contractors.clear();
	}

	public Iterator getAllContractors(){
		return this.contractors.iterator();
	}

	public List getContractors(){
		return this.contractors;
	}

	public void setContractors(List l){
		this.contractors=l;
	}

	/**
	* Protege name: lives_in
	*/
	private Domain lives_in;

	public void setLives_in(Domain value){
		this.lives_in=value;
	}

	public Domain getLives_in(){
		return this.lives_in;
	}

	/**
	* Protege name: localName
	*/
	private String localName;

	@Override
	public void setLocalName(String value){
		this.localName=value;
	}

	@Override
	public String getLocalName(){
		return this.localName;
	}

	/**
	* Protege name: contains
	*/
	private BayMap contains;

	public void setContains(BayMap value){
		this.contains=value;
	}

	public BayMap getContains(){
		return this.contains;
	}

	/**
	* Protege name: service_type
	*/
	private String service_type;

	public void setService_type(String value){
		this.service_type=value;
	}

	public String getService_type(){
		return this.service_type;
	}

	/**
	* Protege name: container_states
	*/
	private List container_states=new ArrayList();

	public void addContainer_states(TOCHasState elem){
		this.container_states.add(elem);
	}

	public boolean removeContainer_states(TOCHasState elem){
		boolean result=this.container_states.remove(elem);
		return result;
	}

	public void clearAllContainer_states(){
		this.container_states.clear();
	}

	public Iterator getAllContainer_states(){
		return this.container_states.iterator();
	}

	public List getContainer_states(){
		return this.container_states;
	}

	public void setContainer_states(List l){
		this.container_states=l;
	}

}
