package de.enflexit.awb.simulation.load.threading.storage;

/**
 * Storage class for storing Thread-Load-Information of JavaVirtualMachine
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorageContainer extends ThreadInfoStorageXYSeries {
	
	/**
	 * Instantiates a new thread info storage for a container.
	 * @param name the name
	 */
	public ThreadInfoStorageContainer(String name) {
		super(name);
	}
}
