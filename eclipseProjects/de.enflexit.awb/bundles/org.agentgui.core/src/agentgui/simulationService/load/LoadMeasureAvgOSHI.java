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
 * instance of the class {@link LoadMeasureOSHI}.
 * 
 * @see LoadMeasureOSHI
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LoadMeasureAvgOSHI {

	/** The used average counter - maximum length of List. */
	private Integer useAVGCounter = 0;
	/** The list of measurements. */
	private ArrayList<LoadMeasureOSHI> measureList = new ArrayList<LoadMeasureOSHI>();
	
	/** The processorName of the chip set. */
	private String processorName=null;
	/** The processor speed. */
	private long Mhz;
	/** The number of cores on this machine. */
	private int totalCpu;
	
	/** +++ The CPU usage. +++*/
	private double cpuUsage;
	
	/** A memory information. */
	private long totalMemory;			// Bytes
	/** A memory information. */
	private long freeMemory;			// Bytes
	/** A memory information. */
	private long usedMemory;			// Bytes
	
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
	public LoadMeasureAvgOSHI(Integer avgCounter) {
		useAVGCounter = avgCounter;
	}
	
	/**
	 * Used to put a new load measurement into this class.
	 * @param currentLoadMeasure the current load measured
	 */
	public void put(LoadMeasureOSHI currentLoadMeasure) {
        
		if (processorName==null) {
			// setting system information once
			this.setProcessorName(currentLoadMeasure.getProcessorName());
			this.setMhz(currentLoadMeasure.getMhz());
			this.setTotalCpu(currentLoadMeasure.getNumberOfPhysicalCPU());
		}

		// inserting object in a list
		// the objects contains information about cpu and memory.
		if (this.measureList.size() >= this.useAVGCounter) {
			this.measureList.remove(0);
		}
		this.measureList.add(currentLoadMeasure.clone()); // add new object in the list
		this.calculateLoadAverage();
	}
	
	
	/**
	 * Calculates the load average.
	 */
	private void calculateLoadAverage() {
      
		int size = measureList.size();
		
		double cpuUsageTemp = 0;
		
		long totalMemoryTemp = 0;
		long freeMemoryTemp = 0;
		long useMemoryTemp = 0;
		
		long totalMemorySwapTemp = 0;
		long freeMemorySwapTemp = 0;
		long useMemorySwapTemp = 0;
		
		//calculating cpu and memory average value
		for (int i = 0; i < size; i++) {

			cpuUsageTemp += measureList.get(i).getCPU_Usage();
			
			totalMemoryTemp += measureList.get(i).getTotalMemory();
			freeMemoryTemp += measureList.get(i).getFreeMemory();
			useMemoryTemp += measureList.get(i).getUsedMemory();

			totalMemorySwapTemp += measureList.get(i).getTotalMemorySwap(); 
			freeMemorySwapTemp += measureList.get(i).getFreeMemorySwap(); 
			useMemorySwapTemp += measureList.get(i).getUsedMemorySwap();
			
		}	
		
		this.setCPU_Usage(cpuUsageTemp/size);
		
		this.setTotalMemory(totalMemoryTemp/size);
		this.setFreeMemory(freeMemoryTemp/size);
		this.setUsedMemory(useMemoryTemp/size);
		
		this.setTotalMemorySwap(totalMemorySwapTemp/size);
		this.setFreeMemorySwap(freeMemorySwapTemp/size);
		this.setUseMemorySwap(useMemorySwapTemp/size);
	}

	
	/**
	 * Sets the CPU usage.
	 * @param cpuUsage the new CPU usage
	 */
	public void setCPU_Usage(double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	/**
	 * Gets the CPU usage.
	 * @return the CPU usage
	 */
	public double getCPU_Usage() {
		return cpuUsage;
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
	 * @param usedMemory the usedMemory to set
	 */
	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}
	/**
	 * Gets the use memory.
	 * @return the usedMemory
	 */
	public long getUsedMemory() {
		return usedMemory;
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
	 * Gets the processorName.
	 * @return the processorName
	 */
	public String getProcessorName() {
		return processorName;
	}
	/**
	 * Sets the processorName.
	 * @param processorName the processorName to set
	 */
	public void setProcessorName(String processorName) {
		this.processorName = processorName;
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
