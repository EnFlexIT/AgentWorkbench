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

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.cmd.SigarCommandBase;

/**
 * This class provides load information for each CPU and the memory found on the 
 * current system - regardless, if it is Windows, UNIX or Linux-system.<br> 
 * Therefore the Hyperic-SIGAR API is used, which can be found
 * <a href="http://www.hyperic.com/support/docs/sigar/" target="_blank">here</a>.
 * 
 * @author Christopher Nde - DAWIS - ICB - University of Duisburg - Essen
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LoadMeasureSigar extends SigarCommandBase implements Cloneable {
	
	/** The information about the available CPU's. */
	private CpuInfo[] cpuInfos;
	/** The vendor of the chip set. */
	private String vendor=null;
	/** The processor speed. */
	private long Mhz;
	/** The model description. */
	private String model;
	/** The number of cores on this machine. */
	private int totalCpu;
	
	/** The calculated percentage load of the CPU using Hyperic-SIGAR. */
	private CpuPerc cpuPerc;
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
	
	/** The Hyperic-SIGAR memory. */
	private Mem memory;
	/** A memory information. */
	private long totalMemory;			// Bytes
	/** A memory information. */
	private long freeMemory;			// Bytes
	/** A memory information. */
	private long useMemory;				// Bytes
	/** A memory information. */
	private double usedMemoryPercent;	// %
	
	/** The Hyperic-SIGAR swap memory. */
	private Swap swap;
	/** A swap memory information. */
	private long totalMemorySwap; 		// Bytes
	/** A swap memory information. */
	private long freeMemorySwap;		// Bytes
	/** A swap memory information. */
	private long useMemorySwap;			// Bytes
	
	/** The precision value for calculations. */
	private int round2 = 4;
	
	/** The round factor for calculations. */
	private double round2factor = Math.pow(10, round2);

    
    /**
     * Instantiates this class.
     */
    public LoadMeasureSigar() {
        super();
        try {
     		// --- getting CPU information ----------------
			this.outputCpu(); 
			// --- get the first measurements --------------
			this.measureLoadOfSystem();
			
		} catch (SigarException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Returns a copy of the current object.
     * @return the copy of the current instance 
     */
    @Override
    protected LoadMeasureSigar clone() {
    	try {
			return (LoadMeasureSigar) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
    }
    
	/**
	 * Measures load of the system, by using the sigar functionalities .
	 * @throws Exception the exception
	 */
	public void measureLoadOfSystem() throws Exception {
		outputTimes(); 	// --- timing informations --------
		outputMem();	// --- memory information ---------
	}

	/**
	 * Will set up the CPU information.
	 * @throws SigarException 
	 */
	public void outputCpu() throws SigarException {
        // --- setting cpu-Information --------------------
		cpuInfos = this.sigar.getCpuInfoList();
		CpuInfo info = cpuInfos[0];
        setVendor(info.getVendor());
        setModel(info.getModel());
        setMhz(info.getMhz());
        setTotalCpu(info.getTotalCores());
    }

    /**
     * Will set up the memory and the swap information.
     * @throws SigarException 
     */
    private void outputMem() throws SigarException{
    	// --- setting memory values ----------------------
    	memory = this.sigar.getMem();
    	setTotalMemory(memory.getTotal());
    	setFreeMemory(memory.getFree());
    	setUseMemory(memory.getUsed());
    	setUsedMemoryPercent(memory.getUsedPercent());
    	
    	swap = this.sigar.getSwap();
    	setTotalMemorySwap(swap.getTotal());
    	setFreeMemorySwap(swap.getFree());
    	setUseMemorySwap(swap.getUsed());
    }

    /**
     * Will set up the CPU times in percentage format.
     * @throws SigarException the sigar exception
     */
    private void outputTimes() throws SigarException {
        // --- set cpu load information -------------------
    	cpuPerc = this.sigar.getCpuPerc();
    	this.setCpuUserTime(cpuPerc.getUser());
    	this.setCpuSystemTime(cpuPerc.getSys());
    	this.setCpuIdleTime(cpuPerc.getIdle());
    	this.setCpuCombinedTime(cpuPerc.getCombined());
    	this.setCpuWaitTime(cpuPerc.getWait());
    }

	/* (non-Javadoc)
	 * @see org.hyperic.sigar.cmd.SigarCommandBase#output(java.lang.String[])
	 */
	@Override
	public void output(String[] arg0) throws SigarException {
	}
	
	
	/**
	 * Sets the vendor.
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	/**
	 * Gets the vendor.
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
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
	 * Sets the model.
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}
	/**
	 * Gets the model.
	 * @return the model
	 */
	public String getModel() {
		return model;
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
	 * Gets the cpu system time rounded.
	 * @return the cpu system time rounded
	 */
	public double getCpuSystemTimeRounded() {
		return doubleRound(cpuSystemTime);
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
	 * Gets the cpu user time rounded.
	 * @return the cpu user time rounded
	 */
	public double getCpuUserTimeRounded() {
		return doubleRound(cpuUserTime);
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
	 * Gets the cpu wait time rounded.
	 * @return the cpu wait time rounded
	 */
	public double getCpuWaitTimeRounded() {
		return doubleRound(cpuWaitTime);
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
	 * Gets the cpu idle time rounded.
	 * @return the cpu idle time rounded
	 */
	public double getCpuIdleTimeRounded() {
		return doubleRound(cpuIdleTime);
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
	 * Gets the cpu combine time rounded.
	 * @return the cpu combine time rounded
	 */
	public double getCpuCombineTimeRounded() {
		return doubleRound(cpuCombinedTime);
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
		return useMemory; //memory in MB
	}

	/**
	 * Sets the used memory percent.
	 * @param usedMemoryPercent the usedMemoryPercent to set
	 */
	public void setUsedMemoryPercent(double usedMemoryPercent) {
		this.usedMemoryPercent = usedMemoryPercent;
	}
	/**
	 * Gets the used memory percent.
	 * @return the usedMemoryPercent
	 */
	public double getUsedMemoryPercent() {
		return usedMemoryPercent;
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
	 * Rounds an incoming double value to the specified decimal.
	 * @param input the input
	 * @return the double
	 */
	private double doubleRound(double input) {
		return Math.round(input * round2factor) / round2factor;
	}
	
	
}
