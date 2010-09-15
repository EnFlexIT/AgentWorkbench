package mas.service.load;

import jade.core.Location;

import java.util.Hashtable;

import mas.service.distribution.ontology.BenchmarkResult;
import mas.service.distribution.ontology.ClientRemoteContainerReply;
import mas.service.distribution.ontology.OSInfo;
import mas.service.distribution.ontology.PlatformLoad;
import mas.service.distribution.ontology.PlatformPerformance;

/**
 * Manages all Load-Informations of the Containers, which
 * are connected to this Main-Container
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public class LoadInformation  {

	private static final long serialVersionUID = -8535049489754734307L;
	
	public Hashtable<String, NodeDescription> containerDescription = new Hashtable<String, NodeDescription>();
	public Hashtable<String, PlatformLoad> containerLoads = new Hashtable<String, PlatformLoad>(); 
	public Hashtable<String, Location> containerLocations = new Hashtable<String, Location>();
	
	private String lastNewContainer = null;
	
	/**
	 * Constructor of this class
	 */
	public LoadInformation() {
		
	}
	
	/**
	 * Method to put the answer of the Server.Master directly 
	 * in the local public containerDescription - Variable
	 * @param crcReply
	 */
	public void putContainerDescription(ClientRemoteContainerReply crcReply) {
		
		NodeDescription node = new NodeDescription(crcReply);
		this.containerDescription.put(node.getContainerName(), node);
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

	
	
	// --------------------------------------------------------------
	// --- Sub-Class NodeDescription --- S T A R T ------------------
	// --------------------------------------------------------------
	/**
	 * This class describes the container(computer), which are 
	 * connected to the Main-Container
	 */
	public class NodeDescription {
		
		private String containerName = null;
		private OSInfo osInfo = null;
		private PlatformPerformance plPerformace = null;
		private BenchmarkResult benchmarkValue = null;
		
		/**
		 * Constuructor of this Sub-Class
		 */
		public NodeDescription() {
		
		}
		/**
		 * Constuructor of this Sub-Class, using the 
		 * ClientRemoteContainerReply from the Server.Master
		 * @param crcReply
		 */
		public NodeDescription(ClientRemoteContainerReply crcReply) {
			containerName = crcReply.getRemoteContainerName();
			osInfo = crcReply.getRemoteOS();
			plPerformace = crcReply.getRemotePerformance();
			benchmarkValue = crcReply.getRemoteBenchmarkResult();
		}
		
		/**
		 * @param containerName the containerName to set
		 */
		public void setContainerName(String containerName) {
			this.containerName = containerName;
		}
		/**
		 * @return the containerName
		 */
		public String getContainerName() {
			return containerName;
		}
		
		/**
		 * @return the osInfo
		 */
		public OSInfo getOsInfo() {
			return osInfo;
		}
		/**
		 * @param osInfo the osInfo to set
		 */
		public void setOsInfo(OSInfo osInfo) {
			this.osInfo = osInfo;
		}
		
		/**
		 * @return the plPerformace
		 */
		public PlatformPerformance getPlPerformace() {
			return plPerformace;
		}
		/**
		 * @param plPerformace the plPerformace to set
		 */
		public void setPlPerformace(PlatformPerformance plPerformace) {
			this.plPerformace = plPerformace;
		}
		
		/**
		 * @return the benchmarkValue
		 */
		public BenchmarkResult getBenchmarkValue() {
			return benchmarkValue;
		}
		/**
		 * @param benchmarkValue the benchmarkValue to set
		 */
		public void setBenchmarkValue(BenchmarkResult benchmarkValue) {
			this.benchmarkValue = benchmarkValue;
		}
		
	}
	// --------------------------------------------------------------
	// --- Sub-Class NodeDescription --- E N D ----------------------
	// --------------------------------------------------------------

}
