package contmas.ontology;

import jade.content.Concept;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
* Protege name: Domain
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
*/
public class Domain implements Concept{

	/**
	 * 
	 */
	private static final long serialVersionUID= -8745598207570190661L;
	/**
	* Protege name: has_subdomains
	   */
	private List has_subdomains=new ArrayList();

	public void addHas_subdomains(Domain elem){
		this.has_subdomains.add(elem);
	}

	public boolean removeHas_subdomains(Domain elem){
		boolean result=this.has_subdomains.remove(elem);
		return result;
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

	public void setHas_subdomains(List l){
		this.has_subdomains=l;
	}

	/**
	* Protege name: lies_in
	*/
	private Domain lies_in;

	public void setLies_in(Domain value){
		this.lies_in=value;
	}

	public Domain getLies_in(){
		return this.lies_in;
	}

	/**
	* Protege name: id
	*/
	private String id;

	public void setId(String value){
		this.id=value;
	}

	public String getId(){
		return this.id;
	}

}
