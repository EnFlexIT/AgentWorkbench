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
	private double cpuCombinedTime;
	
	private long totalMemory;			// Bytes
	private long freeMemory;			// Bytes
	private long useMemory;				// Bytes
	private double usedMemoryPercent;	// %
	
	private long totalMemorySwap; 		// Bytes
	private long freeMemorySwap;		// Bytes
	private long useMemorySwap;			// Bytes
	
	
	public LoadMeasureAvgSigar(Integer avgCounter) {
		useAVGCounter = avgCounter;                     //maximum length of List
	}

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
	 * 
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
	 * @param cpuSystemTime the cpuSystemTime to set
	 */
	public void setCpuSystemTime(double cpuSystemTime) {
		this.cpuSystemTime = cpuSystemTime;
	}
	/**
	 * @return the cpuSystemTime
	 */
	public double getCpuSystemTime() {
		return cpuSystemTime;
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
		return cpuUserTime;
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
		return cpuWaitTime;
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
		return cpuIdleTime;
	}

	/**
	 * @param d the combineTime to set
	 */
	public void setCpuCombinedTime(double d) {
		this.cpuCombinedTime = d;
	}
	/**
	 * @return the combineTime
	 */
	public double getCpuCombinedTime() {
		return cpuCombinedTime;
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
		return totalMemory; 
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
		return freeMemory; 
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
		return useMemory;
	}
	
	/**
	 * @return the usedMemoryPercent
	 */
	public double getUsedMemoryPercent() {
		return usedMemoryPercent;
	}
	/**
	 * @param usedMemoryPercent the usedMemoryPercent to set
	 */
	public void setUsedMemoryPercent(double usedMemoryPercent) {
		this.usedMemoryPercent = usedMemoryPercent;
	}

	/**
	 * @return the totalMemorySwap
	 */
	public long getTotalMemorySwap() {
		return totalMemorySwap;
	}
	/**
	 * @param totalMemorySwap the totalMemorySwap to set
	 */
	public void setTotalMemorySwap(long totalMemorySwap) {
		this.totalMemorySwap = totalMemorySwap;
	}

	/**
	 * @return the freeMemorySwap
	 */
	public long getFreeMemorySwap() {
		return freeMemorySwap;
	}
	/**
	 * @param freeMemorySwap the freeMemorySwap to set
	 */
	public void setFreeMemorySwap(long freeMemorySwap) {
		this.freeMemorySwap = freeMemorySwap;
	}

	/**
	 * @return the useMemorySwap
	 */
	public long getUseMemorySwap() {
		return useMemorySwap;
	}
	/**
	 * @param useMemorySwap the useMemorySwap to set
	 */
	public void setUseMemorySwap(long useMemorySwap) {
		this.useMemorySwap = useMemorySwap;
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
