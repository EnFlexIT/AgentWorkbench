package mas.service.load;

import jade.core.Location;

import java.util.Hashtable;

import mas.service.distribution.ontology.PlatformLoad;

/**
 * Manages all Load-Informations of the Containers, which
 * are connected to this Main-Container
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public class LoadInformation  {

	private static final long serialVersionUID = -8535049489754734307L;
	
	public Hashtable<String, PlatformLoad> containerLoads = new Hashtable<String, PlatformLoad>(); 
	public Hashtable<String, Location> containerLocations = new Hashtable<String, Location>();
	
	private String lastNewContainer = null;
	
	/**
	 * Constructor of this class
	 */
	public LoadInformation() {
	}
	
	/**
	 * @param lastNewContainer the lastNewContainer to set
	 */
	public void setLastNewContainer(String lastNewContainer) {
		this.lastNewContainer = lastNewContainer;
	}
	/**
	 * @return the lastNewContainer
	 */
	public String getLastNewContainer() {
		return lastNewContainer;
	}


}
