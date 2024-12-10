package de.enflexit.awb.simulation.load.threading.storage;

/**
 * Storage class for storing Thread-Load-Information of (physical)machine
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorageMachine extends ThreadInfoStorageXYSeries{
	
	private double mflops;
	private double actualAverageLoadCPU;
	
	/**
	 * Instantiates a new thread info storage machine.
	 * @param name the name
	 */
	public ThreadInfoStorageMachine(String name) {
		super(name);
	}

	/**
	 * Return the MFLOPS of that (physical) machine.
	 * @return the MFLOPS
	 */
	public double getMflops() {
		return mflops;
	}
	/**
	 * Set the MFLOPS of that (physical) machine.
	 * @param mflops the MFLOPS to set
	 */
	public void setMflops(double mflops) {
		this.mflops = mflops;
	}

	/**
	 * The actual average load CPU
	 * @return the actualAverageLoadCPU
	 */
	public double getActualAverageLoadCPU() {
		return actualAverageLoadCPU;
	}

	/**
	 * The actual average load CPU
	 * @param actualAverageLoadCPU the actualAverageLoadCPU to set
	 */
	public void setActualAverageLoadCPU(double actualAverageLoadCPU) {
		this.actualAverageLoadCPU = actualAverageLoadCPU;
	}

}
