package load;

import java.util.ArrayList;

public class LoadMeasureAvgJVM {


	private Integer useAVGCounter = 0;//maximum length of List
	private ArrayList<LoadMeasureJVM> measureList = new ArrayList<LoadMeasureJVM>();
	
	private long totalMemoryUsageJVM;               
    private long freeMemorryUsageJVM;            
    private long currentHeapInitJVM;  
    private long maximumHeapSizeJVM;            
    private long currentHeapSizeJVM;
    private long currentHeapFreeJVM;
		
	public LoadMeasureAvgJVM(Integer avgCounter) {
		useAVGCounter = avgCounter;                     //maximum length of List
	}

	public void put(LoadMeasureJVM currentLoadMeasure) {
		//inserting object in a list
		// the objects contains information about cpu and memory. 
		if (measureList.size() >= useAVGCounter) {
			measureList.remove(0);
		}
		measureList.add(currentLoadMeasure); //add new object in the list
		this.calculateLoadAverage();
	}
	
	/**
	 * 
	 */
	private void calculateLoadAverage() {
      
		int size = measureList.size();
		
		long totalMemoryUsageJVMTemp =0;               
	    long freeMemorryUsageJVMTemp =0;            
	    long currentHeapInitJVMTemp =0;  
	    long maximumHeapSizeJVMTemp =0;            
	    long currentHeapSizeJVMTemp =0;
	    long currentHeapFreeJVMTemp =0;
	    
		//calculating JVM memory average value
		for (int i = 0; i < size; i++) {

			// average used JVM memory
			totalMemoryUsageJVMTemp += measureList.get(i).getTotalMemoryUsageJVM();

			// average free memory
			freeMemorryUsageJVMTemp += measureList.get(i).getCurrentHeapFreeJVM();

			// average init 
			currentHeapInitJVMTemp += measureList.get(i).getcurrentHeapInitJVM();

			// average JVM heap memory
			maximumHeapSizeJVMTemp += measureList.get(i).getMaximumHeapSizeJVM();

			// average current Heap memory
			currentHeapSizeJVMTemp += measureList.get(i).getCurrentHeapSizeJVM();

			// average current heap free
			currentHeapFreeJVMTemp += measureList.get(i).getCurrentHeapFreeJVM();

		}	
		
		//setting the values of memory
		setTotalMemoryUsageJVM(totalMemoryUsageJVMTemp/size);
		setCurrentHeapFreeJVM(currentHeapFreeJVMTemp/size);
		setCurrentHeapInitJVM(currentHeapInitJVMTemp/size);
		setCurrentHeapSizeJVM(currentHeapSizeJVMTemp/size);
		setFreeMemorryUsageJVM(freeMemorryUsageJVMTemp/size);
		setMaximumHeapSizeJVM(maximumHeapSizeJVMTemp/size);
		
	}

	//Loadaverage information
	//////////////////////////////////////////

	/**
	 * @param totalMemoryUsageJVM the totalMemoryUsageJVM to set
	 */
	public void setTotalMemoryUsageJVM(long totalMemoryUsageJVM) {
		this.totalMemoryUsageJVM = totalMemoryUsageJVM;
	}

	/**
	 * @return the totalMemoryUsageJVM
	 */
	public long getTotalMemoryUsageJVM() {
		return totalMemoryUsageJVM;
	}

	/**
	 * @param freeMemorryUsageJVM the freeMemorryUsageJVM to set
	 */
	public void setFreeMemorryUsageJVM(long freeMemorryUsageJVM) {
		this.freeMemorryUsageJVM = freeMemorryUsageJVM;
	}

	/**
	 * @return the freeMemorryUsageJVM
	 */
	public long getFreeMemorryUsageJVM() {
		return freeMemorryUsageJVM;
	}

	/**
	 * @param currentHeapInitJVM the currentHeapInitJVM to set
	 */
	public void setCurrentHeapInitJVM(long currentHeapInitJVM) {
		this.currentHeapInitJVM = currentHeapInitJVM;
	}

	/**
	 * @return the currentHeapInitJVM
	 */
	public long getCurrentHeapInitJVM() {
		return currentHeapInitJVM;
	}

	/**
	 * @param maximumHeapSizeJVM the maximumHeapSizeJVM to set
	 */
	public void setMaximumHeapSizeJVM(long maximumHeapSizeJVM) {
		this.maximumHeapSizeJVM = maximumHeapSizeJVM;
	}

	/**
	 * @return the maximumHeapSizeJVM
	 */
	public long getMaximumHeapSizeJVM() {
		return maximumHeapSizeJVM;
	}

	/**
	 * @param currentHeapSizeJVM the currentHeapSizeJVM to set
	 */
	public void setCurrentHeapSizeJVM(long currentHeapSizeJVM) {
		this.currentHeapSizeJVM = currentHeapSizeJVM;
	}

	/**
	 * @return the currentHeapSizeJVM
	 */
	public long getCurrentHeapSizeJVM() {
		return currentHeapSizeJVM;
	}

	/**
	 * @param currentHeapFreeJVM the currentHeapFreeJVM to set
	 */
	public void setCurrentHeapFreeJVM(long currentHeapFreeJVM) {
		this.currentHeapFreeJVM = currentHeapFreeJVM;
	}

	/**
	 * @return the currentHeapFreeJVM
	 */
	public long getCurrentHeapFreeJVM() {
		return currentHeapFreeJVM;
	}

}
