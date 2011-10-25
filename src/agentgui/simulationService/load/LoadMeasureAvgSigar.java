/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.simulationService.load;

import java.util.ArrayList;

/**
 * This class calculates the average values of the measurements, done by the 
 * instance of the class {@link LoadMeasureSigar}.
 * 
 * @see LoadMeasureSigar
 * 
 * @author Christopher Nde - DAWIS - ICB - University of Duisburg - Essen
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LoadMeasureAvgSigar {

	/** The used average counter - maximum length of List. */
	private Integer useAVGCounter = 0;
	/** The list of measurements. */
	private ArrayList<LoadMeasureSigar> measureList = new ArrayList<LoadMeasureSigar>();
	
	/** The vendor of the chip set. */
	private String vendor=null;
	/** The processor speed. */
	private long Mhz;
	/** The model description. */
	private String model;
	/** The number of cores on this machine. */
	private int totalCpu;
	
	/** A time measurement. */
	private double cpuSystemTime;
	/** A time measurement. */
	private double cpuUserTime;
	/** A time measurement. */
	private double cpuIdleTime;
	/** A time measurement. */
	private double cpuWaitTime;
	/** A time measurement. */
	private double cpuCombinedTime;
	
	/** A memory information. */
	private long totalMemory;			// Bytes
	/** A memory information. */
	private long freeMemory;			// Bytes
	/** A memory information. */
	private long useMemory;				// Bytes
	/** A memory information. */
	private double usedMemoryPercent;	// %
	
	/** A swap memory information. */
	private long totalMemorySwap; 		// Bytes
	/** A swap memory information. */
	private long freeMemorySwap;		// Bytes
	/** A swap memory information. */
	private long useMemorySwap;			// Bytes
	
	
	/**
	 * Instantiates this class.
	 * @param avgCounter the maximum length of List for building the average
	 */
	public LoadMeasureAvgSigar(Integer avgCounter) {
		useAVGCounter = avgCounter;
	}
	
	/**
	 * Used to put a new load measurement into this class.
	 * @param currentLoadMeasure the current load measured
	 */
	public void put(LoadMeasureSigar currentLoadMeasure) {
        
		if (vendor == null) {
			// setting system information once
			setVendor(currentLoadMeasure.getVendor());
			setModel(currentLoadMeasure.getModel());
			setMhz(currentLoadMeasure.getMhz());
			setTotalCpu(currentLoadMeasure.getTotalCpu());
		}

		// inserting object in a list
		// the objects contains information about cpu and memory.
		if (measureList.size() >= useAVGCounter) {
			measureList.remove(0);
		}
		measureList.add(currentLoadMeasure.clone()); // add new object in the list
		this.calculateLoadAverage();
	}
	
	
	/**
	 * Calculates the load average.
	 */
	private void calculateLoadAverage() {
      
		int size = measureList.size();
		
		double cpuSystemTimeTemp = 0;
		double cpuUserTimeTemp = 0;
		double cpuIdleTimeTemp = 0;
		double cpuWaitTimeTemp = 0;
		double cpuCombinedTimeTemp = 0;

		long totalMemoryTemp = 0;
		long freeMemoryTemp = 0;
		long useMemoryTemp = 0;
		double usedMemoryPercentTemp = 0;
		
		long totalMemorySwapTemp = 0;
		long freeMemorySwapTemp = 0;
		long useMemorySwapTemp = 0;
		
		//calculating cpu and memory average value
		for (int i = 0; i < size; i++) {

			cpuSystemTimeTemp += measureList.get(i).getCpuSystemTime();
			cpuUserTimeTemp += measureList.get(i).getCpuUserTime();
			cpuIdleTimeTemp += measureList.get(i).getCpuIdleTime();
			cpuWaitTimeTemp += measureList.get(i).getCpuWaitTime();
			cpuCombinedTimeTemp += measureList.get(i).getCpuCombinedTime();

			totalMemoryTemp += measureList.get(i).getTotalMemory();
			freeMemoryTemp += measureList.get(i).getFreeMemory();
			useMemoryTemp += measureList.get(i).getUseMemory();
			usedMemoryPercentTemp += measureList.get(i).getUsedMemoryPercent();

			totalMemorySwapTemp += measureList.get(i).getTotalMemorySwap(); 
			freeMemorySwapTemp += measureList.get(i).getFreeMemorySwap(); 
			useMemorySwapTemp += measureList.get(i).getUseMemorySwap();
			
		}	
		
		//setting the values of memory and cpu
		this.setCpuIdleTime(cpuIdleTimeTemp/size);
		this.setCpuSystemTime(cpuSystemTimeTemp/size);
		this.setCpuWaitTime(cpuWaitTimeTemp/size);
		this.setCpuUserTime(cpuUserTimeTemp/size);
		this.setCpuCombinedTime(cpuCombinedTimeTemp/size);
		
		this.setTotalMemory(totalMemoryTemp/size);
		this.setFreeMemory(freeMemoryTemp/size);
		this.setUseMemory(useMemoryTemp/size);
		this.setUsedMemoryPercent(usedMemoryPercentTemp/size);
		
		this.setTotalMemorySwap(totalMemorySwapTemp/size);
		this.setFreeMemorySwap(freeMemorySwapTemp/size);
		this.setUseMemorySwap(useMemorySwapTemp/size);
	}

	/**
	 * Sets the cpu system time.
	 * @param cpuSystemTime the cpuSystemTime to set
	 */
	public void setCpuSystemTime(double cpuSystemTime) {
		this.cpuSystemTime = cpuSystemTime;
	}
	/**
	 * Gets the cpu system time.
	 * @return the cpuSystemTime
	 */
	public double getCpuSystemTime() {
		return cpuSystemTime;
	}

	/**
	 * Sets the cpu user time.
	 * @param cpuUserTime the cpuUserTime to set
	 */
	public void setCpuUserTime(double cpuUserTime) {
		this.cpuUserTime = cpuUserTime;
	}
	/**
	 * Gets the cpu user time.
	 * @return the cpuUserTime
	 */
	public double getCpuUserTime() {
		return cpuUserTime;
	}

	/**
	 * Sets the cpu wait time.
	 * @param cpuWaitTime the cpuWaitTime to set
	 */
	public void setCpuWaitTime(double cpuWaitTime) {
		this.cpuWaitTime = cpuWaitTime;
	}
	/**
	 * Gets the cpu wait time.
	 * @return the cpuWaitTime
	 */
	public double getCpuWaitTime() {
		return cpuWaitTime;
	}

	/**
	 * Sets the cpu idle time.
	 * @param cpuIdleTime the cpuIdleTime to set
	 */
	public void setCpuIdleTime(double cpuIdleTime) {
		this.cpuIdleTime = cpuIdleTime;
	}
	/**
	 * Gets the cpu idle time.
	 * @return the cpuIdleTime
	 */
	public double getCpuIdleTime() {
		return cpuIdleTime;
	}

	/**
	 * Sets the cpu combined time.
	 * @param d the combineTime to set
	 */
	public void setCpuCombinedTime(double d) {
		this.cpuCombinedTime = d;
	}
	/**
	 * Gets the cpu combined time.
	 * @return the combineTime
	 */
	public double getCpuCombinedTime() {
		return cpuCombinedTime;
	}

	/**
	 * Sets the total memory.
	 * @param totalMemory the totalMemory to set
	 */
	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}
	/**
	 * Gets the total memory.
	 * @return the totalMemory
	 */
	public long getTotalMemory() {
		return totalMemory; 
	}

	/**
	 * Sets the free memory.
	 * @param freeMemory the freeMemory to set
	 */
	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}
	/**
	 * Gets the free memory.
	 * @return the freeMemory
	 */
	public long getFreeMemory() {
		return freeMemory; 
	}

	/**
	 * Sets the use memory.
	 * @param useMemory the useMemory to set
	 */
	public void setUseMemory(long useMemory) {
		this.useMemory = useMemory;
	}
	/**
	 * Gets the use memory.
	 * @return the useMemory
	 */
	public long getUseMemory() {
		return useMemory;
	}
	
	/**
	 * Gets the used memory percent.
	 * @return the usedMemoryPercent
	 */
	public double getUsedMemoryPercent() {
		return usedMemoryPercent;
	}
	/**
	 * Sets the used memory percent.
	 * @param usedMemoryPercent the usedMemoryPercent to set
	 */
	public void setUsedMemoryPercent(double usedMemoryPercent) {
		this.usedMemoryPercent = usedMemoryPercent;
	}

	/**
	 * Gets the total memory swap.
	 * @return the totalMemorySwap
	 */
	public long getTotalMemorySwap() {
		return totalMemorySwap;
	}
	/**
	 * Sets the total memory swap.
	 * @param totalMemorySwap the totalMemorySwap to set
	 */
	public void setTotalMemorySwap(long totalMemorySwap) {
		this.totalMemorySwap = totalMemorySwap;
	}

	/**
	 * Gets the free memory swap.
	 * @return the freeMemorySwap
	 */
	public long getFreeMemorySwap() {
		return freeMemorySwap;
	}
	/**
	 * Sets the free memory swap.
	 * @param freeMemorySwap the freeMemorySwap to set
	 */
	public void setFreeMemorySwap(long freeMemorySwap) {
		this.freeMemorySwap = freeMemorySwap;
	}

	/**
	 * Gets the use memory swap.
	 * @return the useMemorySwap
	 */
	public long getUseMemorySwap() {
		return useMemorySwap;
	}
	/**
	 * Sets the use memory swap.
	 * @param useMemorySwap the useMemorySwap to set
	 */
	public void setUseMemorySwap(long useMemorySwap) {
		this.useMemorySwap = useMemorySwap;
	}

	/**
	 * Gets the vendor.
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}
	/**
	 * Sets the vendor.
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * Gets the mhz.
	 * @return the mhz
	 */
	public long getMhz() {
		return Mhz;
	}
	/**
	 * Sets the mhz.
	 * @param mhz the mhz to set
	 */
	public void setMhz(long mhz) {
		Mhz = mhz;
	}

	/**
	 * Gets the model.
	 * @return the model
	 */
	public String getModel() {
		return model;
	}
	/**
	 * Sets the model.
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Sets the total cpu.
	 * @param totalCpu the totalCpu to set
	 */
	public void setTotalCpu(int totalCpu) {
		this.totalCpu = totalCpu;
	}
	/**
	 * Gets the total cpu.
	 * @return the totalCpu
	 */
	public int getTotalCpu() {
		return totalCpu;
	}

}
