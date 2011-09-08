package agentgui.envModel.graph.networkModel;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wrapper class encapsulating a HashMap of NetworkComponents. Its' only 
 * purpose is saving a list of NetworkComponents via JAXB.
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 *
 */
@XmlRootElement
public class NetworkComponentList {
	private HashMap<String, NetworkComponent> componentList;
	/**
	 * Default constructor, required for JAXB
	 */
	public NetworkComponentList(){
		
	}
	/**
	 * Constructor
	 * @param componentList The componentList
	 */
	public NetworkComponentList(HashMap<String, NetworkComponent> componentList){
		this.componentList = componentList;
	}

	/**
	 * @return the componentList
	 */
	public HashMap<String, NetworkComponent> getComponentList() {
		return componentList;
	}

	/**
	 * @param componentList the componentList to set
	 */
	public void setComponentList(HashMap<String, NetworkComponent> componentList) {
		this.componentList = componentList;
	}
	
	
}
