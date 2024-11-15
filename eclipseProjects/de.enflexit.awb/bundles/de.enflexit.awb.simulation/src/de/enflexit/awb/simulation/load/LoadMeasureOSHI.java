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

package de.enflexit.awb.simulation.load;

import de.enflexit.oshi.OSHI_Test;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;

/**
 * This class provides load information for each CPU and the memory found on the 
 * current system - regardless, if it is Windows, UNIX or Linux-system. 
 * Therefore, the the OSHI library is used that is bundles within de.enflexit.oshi
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LoadMeasureOSHI {
	
	private boolean debug = false;
	private boolean debugOshiSystemInfoTest = false;
	
	private SystemInfo systemInfo;
	
	/** +++ The information about the available CPU's. +++ */
	/** The processorName of the chip set. */
	private String processorName=null;
	/** The processor speed. */
	private long Mhz;
	/** The number of cores on this machine. */
	private int numberPhysicalCPU;
	private int numberLogicalCPU;
	
	/** +++ The CPU usage. +++*/
	private double cpuUsage;
	private long[] prevTicks;
	
	/** +++ The memory usage. +++*/
	/** A memory information. */
	private long totalMemory;			// Bytes
	/** A memory information. */
	private long freeMemory;			// Bytes
	/** A memory information. */
	private long usedMemory;			// Bytes
	
	/** +++ The swap memory usage . +++ */
	/** A swap memory information. */
	private long totalMemorySwap; 		// Bytes
	/** A swap memory information. */
	private long freeMemorySwap;		// Bytes
	/** A swap memory information. */
	private long usedMemorySwap;		// Bytes
	
	/** The precision value for calculations. */
	private int round2 = 2;
	/** The round factor for calculations. */
	private double round2factor = Math.pow(10, round2);

    
    /**
     * Instantiates this class.
     */
    public LoadMeasureOSHI() {
        
        try {
        	// --- Print OSHI example --------------------- 
        	if (this.debugOshiSystemInfoTest)  {
        		OSHI_Test.test(null);
        	}
        	
     		// --- Get CPU information ---------------------
			this.setProcessorInformation(); 
			// --- get the first measurements --------------
			this.measureLoadOfSystem();
		
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Gets the system info.
     * @return the system info
     */
    private SystemInfo getSystemInfo() {
    	if (systemInfo==null) {
    		systemInfo = new SystemInfo();
    	}
    	return systemInfo;
    }
    
    /**
     * Returns a copy of the local instance and its measurements.
     * @return the copy of the LoadMeasureOSHI instance
     */
    public LoadMeasureOSHI getCopy() {
    	
    	LoadMeasureOSHI copy = new LoadMeasureOSHI();

    	copy.setProcessorName(this.getProcessorName());
    	copy.setMhz(this.getMhz());
    	copy.setNumberOfPhysicalCPU(this.getNumberOfPhysicalCPU());
    	copy.setNumberOfLogicalCPU(this.getNumberOfLogicalCPU());

    	copy.setCPU_Usage(this.getCPU_Usage());

    	copy.setTotalMemory(this.getTotalMemory());
    	copy.setFreeMemory(this.getFreeMemory());
    	copy.setUsedMemory(getUsedMemory());
    	
    	copy.setTotalMemorySwap(this.getTotalMemorySwap());
    	copy.setFreeMemorySwap(this.getFreeMemorySwap());
    	copy.setUsedMemorySwap(this.getUsedMemorySwap());

    	return copy;
    }
    
    
	/**
	 * Measures load of the system, by using the OSHI functionalities.
	 * @throws Exception the exception
	 */
	public void measureLoadOfSystem() throws Exception {
		this.setSystemCpuLoad();		// --- processor load -------------
		this.setMemoryInformation();	// --- memory information ---------
	}

	/**
	 * Will set up the CPU information.
	 */
	public void setProcessorInformation() {
		CentralProcessor proc = this.getSystemInfo().getHardware().getProcessor();
		if (proc!=null ){
	        this.setProcessorName(proc.getProcessorIdentifier().getName());
	        double freqMhz = proc.getProcessorIdentifier().getVendorFreq() / Math.pow(10, 6);
	        this.setMhz((long) freqMhz);
	        this.setNumberOfPhysicalCPU(proc.getPhysicalProcessorCount());
	        this.setNumberOfLogicalCPU(proc.getLogicalProcessorCount());
		}
    }

    /**
     * Will set up the memory and the swap information.
     * @throws SigarException 
     */
    private void setMemoryInformation() {
    	
    	GlobalMemory memory =  this.getSystemInfo().getHardware().getMemory();
    	setTotalMemory(memory.getTotal());
    	setFreeMemory(memory.getAvailable());
    	setUsedMemory(this.getTotalMemory()-this.getFreeMemory());

    	setTotalMemorySwap(memory.getVirtualMemory().getSwapTotal());
    	setUsedMemorySwap(memory.getVirtualMemory().getSwapUsed());
    	setFreeMemorySwap(this.getTotalMemorySwap()-this.getUsedMemorySwap());

    	double memoryPercentageSwap = this.doubleRound(((double)this.getUsedMemorySwap() / (double)this.getTotalMemorySwap()*100.0));
    	
    	if (this.debug) {
    		System.out.println("Total Memory=" + this.getTotalMemory() + " - Available Memory=" + this.getFreeMemory() + " - (" + this.getUsedMemoryPercentage() + " % usage)");
    		System.out.println("Swap Memory=" + this.getTotalMemorySwap() + " - Free Swap Memory=" + this.getFreeMemorySwap() + " - (" + memoryPercentageSwap + " % usage)");
    		System.out.println();
    	}
    }

    /**
     * Sets the system CPU load.
     */
    private void setSystemCpuLoad() {
		
    	CentralProcessor processor = this.getSystemInfo().getHardware().getProcessor();
		if (processor!=null) {
			
			double cpuUsageBetweenTicksOwn = 0;
			long[] ticks = processor.getSystemCpuLoadTicks();
			if (this.prevTicks!=null && this.prevTicks.length>0) {
				// --- Calculate the total CPU delta ---------------- 
				long user = ticks[TickType.USER.getIndex()] - this.prevTicks[TickType.USER.getIndex()];
				long nice = ticks[TickType.NICE.getIndex()] - this.prevTicks[TickType.NICE.getIndex()];
				long sys = ticks[TickType.SYSTEM.getIndex()] - this.prevTicks[TickType.SYSTEM.getIndex()];
				long idle = ticks[TickType.IDLE.getIndex()] - this.prevTicks[TickType.IDLE.getIndex()];
				long iowait = ticks[TickType.IOWAIT.getIndex()] - this.prevTicks[TickType.IOWAIT.getIndex()];
				long irq = ticks[TickType.IRQ.getIndex()] - this.prevTicks[TickType.IRQ.getIndex()];
				long softirq = ticks[TickType.SOFTIRQ.getIndex()] - this.prevTicks[TickType.SOFTIRQ.getIndex()];
				long steal = ticks[TickType.STEAL.getIndex()] - this.prevTicks[TickType.STEAL.getIndex()];
				long totalCpuTimeDelta = user + nice + sys + idle + iowait + irq + softirq + steal;
				
				long totalCpuUsageDelta = totalCpuTimeDelta - (idle + iowait);
				cpuUsageBetweenTicksOwn = this.doubleRound( ((double)totalCpuUsageDelta / (double)totalCpuTimeDelta) * 100.0);
			}
			this.prevTicks = ticks;
			
			if (this.debug) {
				System.out.println("CPU usage: " + cpuUsageBetweenTicksOwn + " % (Between Ticks/Own)");
			}
			// --- Set the value of the CPU load --------------------
			this.setCPU_Usage(cpuUsageBetweenTicksOwn);
			
		}
    }
    
    
	/**
	 * Sets the processorName.
	 * @param processorName the processorName to set
	 */
	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}
	/**
	 * Gets the processorName.
	 * @return the processorName
	 */
	public String getProcessorName() {
		return processorName;
	}

	/**
	 * Sets the mhz.
	 * @param mhz the mhz to set
	 */
	public void setMhz(long mhz) {
		Mhz = mhz;
	}
	/**
	 * Gets the mhz.
	 * @return the mhz
	 */
	public long getMhz() {
		return Mhz;
	}

	/**
	 * Sets the number of physical CPUs.
	 * @param totalCpu the new number of physical CPUs
	 */
	public void setNumberOfPhysicalCPU(int totalCpu) {
		this.numberPhysicalCPU = totalCpu;
	}
	/**
	 * Gets the number of physical CPUs.
	 * @return the number of physical CPUs
	 */
	public int getNumberOfPhysicalCPU() {
		return numberPhysicalCPU;
	}

	/**
	 * Sets the number of logical CPUs.
	 * @param totalCpu the new number of logical CPUs
	 */
	public void setNumberOfLogicalCPU(int totalCpu) {
		this.numberLogicalCPU = totalCpu;
	}
	/**
	 * Gets the number of logical CPUs.
	 * @return the number of logical CPUs
	 */
	public int getNumberOfLogicalCPU() {
		return numberLogicalCPU;
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
	 * Gets the total memory in MB.
	 * @return the totalMemory
	 */
	public long getTotalMemory() {
		return totalMemory; //memory in MB
	}

	/**
	 * Sets the free memory.
	 * @param freeMemory the freeMemory to set
	 */
	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}
	/**
	 * Gets the free memory in MB.
	 * @return the freeMemory
	 */
	public long getFreeMemory() {
		return freeMemory; //memory in MB
	}

	/**
	 * Sets the used memory.
	 * @param usedMemory the usedMemory to set
	 */
	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}
	/**
	 * Gets the used memory.
	 * @return the usedMemory
	 */
	public long getUsedMemory() {
		return usedMemory; //memory in MB
	}

	/**
	 * Returns the used memory in %.
	 * @return the used memory percentage
	 */
	public double getUsedMemoryPercentage() {
		return this.doubleRound(((double)this.getUsedMemory() / (double)this.getTotalMemory()*100.0));	
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
	 * @return the usedMemorySwap
	 */
	public long getUsedMemorySwap() {
		return usedMemorySwap;
	}
	/**
	 * Sets the use memory swap.
	 * @param usedMemorySwap the usedMemorySwap to set
	 */
	public void setUsedMemorySwap(long usedMemorySwap) {
		this.usedMemorySwap = usedMemorySwap;
	}
	
	
	
	/**
	 * Rounds an incoming double value to the specified decimal.
	 * @param input the input
	 * @return the double
	 */
	private double doubleRound(double input) {
		return Math.round(input * round2factor) / round2factor;
	}
	
	
}
