package mas.service.load;

public class LoadThreshold {
	
	//-------threshold values ---------------------------
	private long thresholdCPU;
	private long thresholdMem;
	
	//-------JVM variable -------------------------------	
	private long currentJvmMemoFree;			// Byte
	private boolean jvmMemoryThresholdExceeded;			
	
	//-------cpu variable --------------------------------		
	private double currentCpuIdleTime;				// Byte
	private boolean cpuThresholdExceeded;
	
	//-------- CPU & JVM --------------------------------
	private boolean cpuAndMemThresholdExceeded;
	
	//-------constructors --------------------------------		
	private LoadMeasureAvgJVM loadMeasureAvgJVM = null;				// Byte
	private LoadMeasureAvgSigar loadMeasureAvgSigar = null;
	
	public LoadThreshold(LoadMeasureAvgJVM loadMeasureAvgJVM, LoadMeasureAvgSigar loadMeasureAvgSigar, int threshold) {
		this.loadMeasureAvgJVM = loadMeasureAvgJVM;
		this.loadMeasureAvgSigar = loadMeasureAvgSigar;
		setCpuThresholdExceeded(false);
		setJvmMemoryThresholdExceeded(false);
		setCpuAndMemThresholdExceeded(false);
		
		setThresholdValues(threshold);
		
	}
	private void setThresholdValues(int threshold) {

		this.thresholdCPU = threshold; // e.g max CPU usage 75% if threshold = 25%
		
		long limit = loadMeasureAvgJVM.getJvmHeapMax()*threshold/100;
		this.thresholdMem = loadMeasureAvgJVM.getJvmHeapMax()-limit; // e.g max memory usage 768 if threshold = 25%
	}
	//--- method for controlling threshold in the system -------------------------
	public void controlThreshold(){
			
		double tempThresholdCPU = loadMeasureAvgSigar.getCpuIdleTime();	//	current cpu availble for use
		long tempThresholdMem = loadMeasureAvgJVM.getJvmHeapUsed();		//	current memory used
		setCurrentCpuIdleTime(tempThresholdMem);						//	current idle of free CPU
		setCurrentJvmMemoFree(loadMeasureAvgJVM.getJvmMemoFree());		//	current free memory
			
			  if (thresholdCPU >= tempThresholdCPU) {
				  setCpuThresholdExceeded(true);
			  }
			  else  if (thresholdCPU < tempThresholdCPU) {
				  setCpuThresholdExceeded(false);
			  }
			  if (thresholdMem >= tempThresholdMem) {
				  setJvmMemoryThresholdExceeded(true);
			  } 
			  else   if (thresholdMem < tempThresholdMem) {
				  setJvmMemoryThresholdExceeded(false);
			  } 
			  if (thresholdCPU >= tempThresholdCPU || thresholdMem >= tempThresholdMem) {
				  setCpuAndMemThresholdExceeded(true);
			  }
			  else if (thresholdCPU < tempThresholdCPU || thresholdMem < tempThresholdMem) {
				  setCpuAndMemThresholdExceeded(false);
			  }
		
	}
	/**
	 * @param cpuAndMemThresholdExceeded the cpuAndMemThresholdExceeded to set
	 */
	public void setCpuAndMemThresholdExceeded(boolean cpuAndMemThresholdExceeded) {
		this.cpuAndMemThresholdExceeded = cpuAndMemThresholdExceeded;
	}
	/**
	 * @return the cpuAndMemThresholdExceeded
	 */
	public boolean isCpuAndMemThresholdExceeded() {
		return cpuAndMemThresholdExceeded;
	}
	/**
	 * @param cpuThresholdExceeded the cpuThresholdExceeded to set
	 */
	public void setCpuThresholdExceeded(boolean cpuThresholdExceeded) {
		this.cpuThresholdExceeded = cpuThresholdExceeded;
	}
	/**
	 * @return the cpuThresholdExceeded
	 */
	public boolean isCpuThresholdExceeded() {
		return cpuThresholdExceeded;
	}
	/**
	 * @param jvmMemoryThresholdExceeded the jvmMemoryThresholdExceeded to set
	 */
	public void setJvmMemoryThresholdExceeded(boolean jvmMemoryThresholdExceeded) {
		this.jvmMemoryThresholdExceeded = jvmMemoryThresholdExceeded;
	}
	/**
	 * @return the jvmMemoryThresholdExceeded
	 */
	public boolean isJvmMemoryThresholdExceeded() {
		return jvmMemoryThresholdExceeded;
	}
	/**
	 * @param currentCpuIdleTime the currentCpuIdleTime to set
	 */
	public void setCurrentCpuIdleTime(double currentCpuIdleTime) {
		this.currentCpuIdleTime = currentCpuIdleTime;
	}
	/**
	 * @return the currentCpuIdleTime
	 */
	public double getCurrentCpuIdleTime() {
		return currentCpuIdleTime;
	}
	/**
	 * @param currentJvmMemoFree the currentJvmMemoFree to set
	 */
	public void setCurrentJvmMemoFree(long currentJvmMemoFree) {
		this.currentJvmMemoFree = currentJvmMemoFree;
	}
	/**
	 * @return the currentJvmMemoFree
	 */
	public long getCurrentJvmMemoFree() {
		return currentJvmMemoFree;
	}

}