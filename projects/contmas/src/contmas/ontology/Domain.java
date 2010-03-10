package contmas.ontology;

import jade.content.Concept;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
* Protege name: Domain
* @author ontology bean generator
* @version 2010/03/10, 15:08:52
*/
public class Domain implements Concept{

	/**
	 * 
	 */
	private static final long serialVersionUID= -8745598207570190661L;
	/**
	* Protege name: lies_in
	*/
	private Domain lies_in;

	/**
	* Protege name: id
	*/
	private String id;

	/**
	* Protege name: has_subdomains
	*/
	private List has_subdomains=new ArrayList();

	public void addHas_subdomains(Domain elem){
		this.has_subdomains.add(elem);
	}

	public void clearAllHas_subdomains(){
		this.has_subdomains.clear();
	}

	public Iterator getAllHas_subdomains(){
		return this.has_subdomains.iterator();
	}

	public List getHas_subdomains(){
		return this.has_subdomains;
	}

	public String getId(){
		return this.id;
	}

	public Domain getLies_in(){
		return this.lies_in;
	}

	public boolean removeHas_subdomains(Domain elem){
		boolean result=this.has_subdomains.remove(elem);
		return result;
	}

	public void setHas_subdomains(List l){
		this.has_subdomains=l;
	}

	public void setId(String value){
		this.id=value;
	}

	public void setLies_in(Domain value){
		this.lies_in=value;
	}

}
