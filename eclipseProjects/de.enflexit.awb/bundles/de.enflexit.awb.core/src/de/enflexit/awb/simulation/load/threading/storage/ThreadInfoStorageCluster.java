package de.enflexit.awb.simulation.load.threading.storage;

/**
 * Storage class for storing Thread-Load-Information about entire machine cluster
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorageCluster extends ThreadInfoStorageXYSeries{
	
	/**
	 * Instantiates a new thread info storage for a JVM.
	 * @param name the name
	 */
	public ThreadInfoStorageCluster(String name) {
		super(name);
	}
}