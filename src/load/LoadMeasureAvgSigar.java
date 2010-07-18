package load;

import java.util.ArrayList;

public class LoadMeasureAvgSigar {

	private Integer useAVGCounter = 0;//maximum length of List
	private ArrayList<LoadMeasureSigar> measureList = new ArrayList<LoadMeasureSigar>();
	
	private String vendor=null;
	private long Mhz;
	private String model;
	private int totalCpu;
	
	private double cpuSystemTime;
	private double cpuUserTime;
	private double cpuIdleTime;
	private double cpuWaitTime;
	private long combineTime;
	private long totalMemory;
	private long freeMemory;
	private long useMemory;
		
	public LoadMeasureAvgSigar(Integer avgCounter) {
		useAVGCounter = avgCounter;                     //maximum length of List
	}

	public void put(LoadMeasureSigar currentLoadMeasure) {
        
		if(vendor==null){
         //setting system information once
         setVendor(currentLoadMeasure.getVendor());
         setModel(currentLoadMeasure.getModel());
         setMhz(currentLoadMeasure.getMhz());
         setTotalCpu(currentLoadMeasure.getTotalCpu());
        }	
        
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
		
		double cpuSystemTimeTemp = 0;
		double cpuUserTimeTemp = 0;
		double cpuIdleTimeTemp = 0;
		double cpuWaitTimeTemp = 0;
		long combineTimeTemp = 0;
		long totalMemoryTemp = 0;
		long freeMemoryTemp = 0;
		long useMemoryTemp = 0;

		//calculating cpu and memory average value
		for (int i = 0; i < size; i++) {

			// average cpu user time
			cpuSystemTimeTemp += measureList.get(i).getCpuSystemTime();

			// average cpu system time
			cpuUserTimeTemp += measureList.get(i).getCpuUserTime();

			// average cpu idel time
			cpuIdleTimeTemp += measureList.get(i).getCpuIdleTime();

			// average cpu wait time
			cpuWaitTimeTemp += measureList.get(i).getCpuWaitTime();

			// average cpu combine time
			combineTimeTemp += measureList.get(i).getCombineTime();

			// average free memory time
			freeMemoryTemp += measureList.get(i).getFreeMemory();

			// average used memory time
			useMemoryTemp += measureList.get(i).getUseMemory();

			// average total memory time
			totalMemoryTemp += measureList.get(i).getTotalMemory();
		}	
		
		//setting the values of memory and cpu
		this.setCombineTime(combineTimeTemp/size);
		this.setCpuIdleTime(cpuIdleTimeTemp/size);
		this.setCpuSystemTime(cpuSystemTimeTemp/size);
		this.setCpuWaitTime(cpuWaitTimeTemp/size);
		this.setFreeMemory(freeMemoryTemp/size);
		this.setUseMemory(useMemoryTemp/size);
		this.setTotalMemory(totalMemoryTemp/size);
		
	}

	//Loadaverage information
	//////////////////////////////////////////
	/**
	 * @param cpuSystemTime the cpuSystemTime to set
	 */
	public void setCpuSystemTime(double cpuSystemTime) {
		this.cpuSystemTime = cpuSystemTime;
	}

	/**
	 * @return the cpuSystemTime
	 */
	public double getCpuSystemTime() {
		return cpuSystemTime*100;
	}

	/**
	 * @param cpuUserTime the cpuUserTime to set
	 */
	public void setCpuUserTime(double cpuUserTime) {
		this.cpuUserTime = cpuUserTime;
	}

	/**
	 * @return the cpuUserTime
	 */
	public double getCpuUserTime() {
		return cpuUserTime*100;
	}

	/**
	 * @param cpuWaitTime the cpuWaitTime to set
	 */
	public void setCpuWaitTime(double cpuWaitTime) {
		this.cpuWaitTime = cpuWaitTime;
	}

	/**
	 * @return the cpuWaitTime
	 */
	public double getCpuWaitTime() {
		return cpuWaitTime*100;
	}

	/**
	 * @param cpuIdleTime the cpuIdleTime to set
	 */
	public void setCpuIdleTime(double cpuIdleTime) {
		this.cpuIdleTime = cpuIdleTime;
	}

	/**
	 * @return the cpuIdleTime
	 */
	public double getCpuIdleTime() {
		return cpuIdleTime*100;
	}

	/**
	 * @param d the combineTime to set
	 */
	public void setCombineTime(double d) {
		this.combineTime = (long) d;
	}

	/**
	 * @return the combineTime
	 */
	public long getCombineTime() {
		return combineTime*100;
	}

	/**
	 * @param totalMemory the totalMemory to set
	 */
	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	/**
	 * @return the totalMemory
	 */
	public long getTotalMemory() {
		return totalMemory/1048576; //memory in MB
	}

	/**
	 * @param freeMemory the freeMemory to set
	 */
	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	/**
	 * @return the freeMemory
	 */
	public long getFreeMemory() {
		return freeMemory/1048576; //memory in MB
	}

	/**
	 * @param useMemory the useMemory to set
	 */
	public void setUseMemory(long useMemory) {
		this.useMemory = useMemory;
	}

	/**
	 * @return the useMemory
	 */
	public long getUseMemory() {
		return useMemory/1048576; //memory in MB
	}
	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the mhz
	 */
	public long getMhz() {
		return Mhz;
	}

	/**
	 * @param mhz the mhz to set
	 */
	public void setMhz(long mhz) {
		Mhz = mhz;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @param totalCpu the totalCpu to set
	 */
	public void setTotalCpu(int totalCpu) {
		this.totalCpu = totalCpu;
	}

	/**
	 * @return the totalCpu
	 */
	public int getTotalCpu() {
		return totalCpu;
	}

}
