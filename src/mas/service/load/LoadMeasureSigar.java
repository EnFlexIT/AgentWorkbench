/*
 * Copyright (C) [2004, 2005, 2006], Hyperic, Inc.
 * This file is part of SIGAR.
 * 
 * SIGAR is free software; you can redistribute it and/or modify
 * it under the terms version 2 of the GNU General Public License as
 * published by the Free Software Foundation. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 */

package mas.service.load;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.cmd.SigarCommandBase;

/**
 * Display cpu information for each cpu found on the system.
 */
public class LoadMeasureSigar extends SigarCommandBase {
	
	private String vendor;
	private String model;
	private int totalCpu;
	private long Mhz;	
	
	private long totalMemory; 	// Bytes
	private long freeMemory;	// Bytes
	private long useMemory;		// Bytes
	
	private double cpuSystemTime;
	private double cpuUserTime;
	private double cpuIdleTime;
	private double cpuWaitTime;
	private double cpuCombinedTime;
	
	private int round2 = 4;
	private double round2factor = Math.pow(10, round2);

    private CpuInfo[] cpuInfos;
    private Mem memory;
    private CpuPerc cpuPerc;
    
    
    public LoadMeasureSigar() {
        super();
        try {
     		// --- getting cpu information ----------------
			this.outputCpu(); 
			// --- get the first measurments --------------
			this.measureLoadOfSystem();
			
		} catch (SigarException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	public void measureLoadOfSystem() throws Exception {
		outputTimes(); 	// --- timing informations --------
		outputMem();	// --- memory information ---------
	}

	public void outputCpu() throws SigarException {
        // --- setting cpu-Information --------------------
		cpuInfos = this.sigar.getCpuInfoList();
		CpuInfo info = cpuInfos[0];
        setVendor(info.getVendor());
        setModel(info.getModel());
        setMhz(info.getMhz());
        setTotalCpu(info.getTotalCores());
    }

    private void outputMem() throws SigarException{
    	// --- setting memory values ----------------------
    	memory = this.sigar.getMem();
    	setFreeMemory(memory.getFree());
    	setTotalMemory(memory.getTotal());
    	setUseMemory(memory.getUsed());
    }

    private void outputTimes() throws SigarException {
        // --- set cpu load information -------------------
    	cpuPerc = this.sigar.getCpuPerc();
    	this.setCpuUserTime(cpuPerc.getUser());
    	this.setCpuSystemTime(cpuPerc.getSys());
    	this.setCpuIdleTime(cpuPerc.getIdle());
    	this.setCpuCombinedTime(cpuPerc.getCombined());
    	this.setCpuWaitTime(cpuPerc.getWait());
    }

	

	@Override
	public void output(String[] arg0) throws SigarException {
		
	}
	
	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param mhz the mhz to set
	 */
	public void setMhz(long mhz) {
		Mhz = mhz;
	}
	/**
	 * @return the mhz
	 */
	public long getMhz() {
		return Mhz;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}
	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
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
	public double getCpuSystemTimeRounded() {
		return doubleRound(cpuSystemTime);
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
	public double getCpuUserTimeRounded() {
		return doubleRound(cpuUserTime);
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
	public double getCpuWaitTimeRounded() {
		return doubleRound(cpuWaitTime);
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
	public double getCpuIdleTimeRounded() {
		return doubleRound(cpuIdleTime);
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
	public double getCpuCombineTimeRounded() {
		return doubleRound(cpuCombinedTime);
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
		return totalMemory; //memory in MB
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
		return freeMemory; //memory in MB
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
		return useMemory; //memory in MB
	}

	/**
	 * Rounds an incomming double value
	 * @param input
	 * @return
	 */
	private double doubleRound(double input) {
		return Math.round(input * round2factor) / round2factor;
	}
	
	
}
