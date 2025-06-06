package de.enflexit.awb.simulation.load;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * This class calculates the average values of the measurements, done by the 
 * instance of the class {@link LoadMeasureJVM}.
 * 
 * @see LoadMeasureJVM
 *
 * @author Christopher Nde - DAWIS - ICB - University of Duisburg - Essen
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LoadMeasureAvgJVM {

	/** The used average counter - maximum length of List. */
	private Integer useAVGCounter = 0;
	/** The list of measurements. */
	private ArrayList<LoadMeasureJVM> measureList = new ArrayList<LoadMeasureJVM>();
	
	/** The JVM PID (process ID). */
	private String jvmPID = null;
	/** The free memory of the JVM. */
	private long jvmMemoFree = 0;			// bytes
	/** The total memory of the JVM. */
	private long jvmMemoTotal = 0;			// bytes
	/** The maximum memory of the JVM. */
	private long jvmMemoMax = 0;			// bytes
    /** The currently used heap . */
    private long jvmHeapUsed = 0;  			// bytes
    /** The heap committed. */
    private long jvmHeapCommitted = 0;  	// bytes           
    /** The maximum heap. */
    private long jvmHeapMax = 0;  			// bytes
    /** The initial heap. */
    private long jvmHeapInit = 0;  			// bytes

    /** The number of Threads running here. */
    private int jvmThreadCount = 0;
    /** The thread times. */
    private Hashtable<String, Long> jvmThreadTimes = null;
    
    
	/**
	 * Instantiates this class
	 * @param avgCounter the maximum length of List for building the average
	 */
	public LoadMeasureAvgJVM(Integer avgCounter) {
		useAVGCounter = avgCounter;                     //maximum length of List
	}

	/**
	 * Used to put a new load measurement into this class 
	 * @param newLoadMeasureJVM the current load measured
	 */
	public void put(LoadMeasureJVM newLoadMeasureJVM) {

		this.jvmPID = newLoadMeasureJVM.getJvmPID();
		
		if (measureList.size() >= useAVGCounter) {
			measureList.remove(0);
		}
		measureList.add(newLoadMeasureJVM);
		this.calculateLoadAverage();
	}
	
	/**
	 * Calculates load average.
	 */
	private void calculateLoadAverage() {
      
		int size = measureList.size();
		
		long tmpJvmMemoFree = 0;	 
		long tmpJvmMemoTotal = 0;	
		long tmpJvmMemoMax = 0;	
	   
	    long tmpJvmHeapUsed = 0;  	
	    long tmpJvmHeapCommited = 0;  		            
	    long tmpJvmHeapMax = 0;  			
	    long tmpJvmHeapInit = 0; 
	    
	    int tmpJvmThreadCount = 0;
	    
		// --- calculating JVM memory average values ----------------
		for (int i = 0; i < size; i++) {

			tmpJvmMemoFree += measureList.get(i).getJvmMemoFree();
			tmpJvmMemoTotal += measureList.get(i).getJvmMemoTotal();
			tmpJvmMemoMax += measureList.get(i).getJvmMemoMax();

			tmpJvmHeapUsed += measureList.get(i).getJvmHeapUsed();
			tmpJvmHeapCommited += measureList.get(i).getJvmHeapCommitted();
			tmpJvmHeapMax += measureList.get(i).getJvmHeapMax();
			tmpJvmHeapInit += measureList.get(i).getJvmHeapInit();
			
			tmpJvmThreadCount += measureList.get(i).getJvmThreadCount();
			
		}	
	    
		jvmMemoFree 	 = tmpJvmMemoFree / size;
		jvmMemoTotal 	 = tmpJvmMemoTotal / size;
		jvmMemoMax 		 = tmpJvmMemoMax / size;

		jvmHeapUsed		 = tmpJvmHeapUsed / size;
		jvmHeapCommitted = tmpJvmHeapCommited / size;
		jvmHeapMax 		 = tmpJvmHeapMax / size;
		jvmHeapInit 	 = tmpJvmHeapInit / size;

		jvmThreadCount 	 = tmpJvmThreadCount / size;
	}

	/**
	 * Sets the JVM PID.
	 * @param jvmPID the jvmPID to set
	 */
	public void setJvmPID(String jvmPID) {
		this.jvmPID = jvmPID;
	}
	/**
	 * Returns the JVM PID.
	 * @return the jvmPID
	 */
	public String getJvmPID() {
		return jvmPID;
	}

	/**
	 * Returns the JVM free memory.
	 * @return the jvmMemoFree
	 */
	public long getJvmMemoFree() {
		return jvmMemoFree;
	}
	/**
	 * Sets the JVM free memory.
	 * @param jvmMemoFree the jvmMemoFree to set
	 */
	public void setJvmMemoFree(long jvmMemoFree) {
		this.jvmMemoFree = jvmMemoFree;
	}

	/**
	 * Returns the JVM total memory.
	 * @return the jvmMemoTotal
	 */
	public long getJvmMemoTotal() {
		return jvmMemoTotal;
	}
	/**
	 * Sets the JVM total memory.
	 * @param jvmMemoTotal the jvmMemoTotal to set
	 */
	public void setJvmMemoTotal(long jvmMemoTotal) {
		this.jvmMemoTotal = jvmMemoTotal;
	}

	/**
	 * Returns the JVM maximum memory.
	 * @return the jvmMemoMax
	 */
	public long getJvmMemoMax() {
		return jvmMemoMax;
	}
	/**
	 * Sets the JVM maximum memory.
	 * @param jvmMemoMax the jvmMemoMax to set
	 */
	public void setJvmMemoMax(long jvmMemoMax) {
		this.jvmMemoMax = jvmMemoMax;
	}

	/**
	 * Returns the JVM used heap memory.
	 * @return the jvmHeapUsed
	 */
	public long getJvmHeapUsed() {
		return jvmHeapUsed;
	}
	/**
	 * Sets the JVM used heap memory.
	 * @param jvmHeapUsed the jvmHeapUsed to set
	 */
	public void setJvmHeapUsed(long jvmHeapUsed) {
		this.jvmHeapUsed = jvmHeapUsed;
	}

	/**
	 * Returns the JVM committed heap memory.
	 * @return the jvmHeapCommited
	 */
	public long getJvmHeapCommitted() {
		return jvmHeapCommitted;
	}
	/**
	 * Sets the JVM committed heap memory.
	 * @param jvmHeapCommited the jvmHeapCommited to set
	 */
	public void setJvmHeapCommitted(long jvmHeapCommited) {
		this.jvmHeapCommitted = jvmHeapCommited;
	}

	/**
	 * Returns the JVM maximum heap memory.
	 * @return the jvmHeapMax
	 */
	public long getJvmHeapMax() {
		return jvmHeapMax;
	}
	/**
	 * Sets the JVM maximum heap memory.
	 * @param jvmHeapMax the jvmHeapMax to set
	 */
	public void setJvmHeapMax(long jvmHeapMax) {
		this.jvmHeapMax = jvmHeapMax;
	}

	/**
	 * Returns the JVM initial heap memory.
	 * @return the jvmHeapInit
	 */
	public long getJvmHeapInit() {
		return jvmHeapInit;
	}
	/**
	 * Returns the JVM initial heap memory.
	 * @param jvmHeapInit the jvmHeapInit to set
	 */
	public void setJvmHeapInit(long jvmHeapInit) {
		this.jvmHeapInit = jvmHeapInit;
	}
	

	/**
	 * Returns the number of threads running in the JVM.
	 * @return the jvmThreadCount
	 */
	public int getJvmThreadCount() {
		return jvmThreadCount;
	}
	/**
	 * Sets the number of threads running in the JVM.
	 * @param jvmThreadCount the jvmThreadCount to set
	 */
	public void setJvmThreadCount(int jvmThreadCount) {
		this.jvmThreadCount = jvmThreadCount;
	}

	/**
	 * Returns the times of the threads running in the JVM.
	 * @return the jvmThreadTimes
	 */
	public Hashtable<String, Long> getJvmThreadTimes() {
		return jvmThreadTimes;
	}
	
	/**
	 * Sets the times of the threads running in the JVM.
	 * @param jvmThreadTimes the jvmThreadTimes to set
	 */
	public void setJvmThreadTimes(Hashtable<String, Long> jvmThreadTimes) {
		this.jvmThreadTimes = jvmThreadTimes;
	}


}
