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

package load;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarLoader;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.cmd.CpuInfo;
import org.hyperic.sigar.cmd.Shell;
import org.hyperic.sigar.cmd.SigarCommandBase;

/**
 * Display cpu information for each cpu found on the system.
 */
public class LoadMeasureSigar extends SigarCommandBase {
	

	// Instance variables for CPU
	// ////////////////////////////////
	private String vendor;
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
	

	// Object variables for CPU
	// ////////////////////////////////
    public Mem mem;
    public CpuPerc cpuPerc;
    public org.hyperic.sigar.CpuInfo[] cpuInfo;
	
    public boolean displayTimes = true;
    
    public LoadMeasureSigar(Shell shell) {
        super(shell);
        try {
			mem = this.sigar.getMem();
			cpuPerc = this.sigar.getCpuPerc();
		} catch (SigarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public LoadMeasureSigar() {
        super();
        try {
			mem = this.sigar.getMem();
			cpuPerc = this.sigar.getCpuPerc();
			
		} catch (SigarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
	 // Internal implementation methods
    //////////////////////////////////
	
    public String getUsageShort() {
        return "Display cpu information";
    }
    
    private static Long format(long value) {
        return new Long(value / 1024);
    }
    
    private void outputMem() throws SigarException{
  	  Mem mem   = this.sigar.getMem();

  	  //setting memory values
  	  setFreeMemory(mem.getFree());
  	  setTotalMemory(mem.getTotal());
  	  setUseMemory(mem.getUsed());
  	  
  	
  }

    private void output(CpuPerc cpu) {
    	
        //set cpu load information
    	setCpuUserTime(cpu.getUser());
    	setCpuSystemTime(cpu.getSys());
    	setCpuIdleTime(cpu.getIdle());
    	setCpuWaitTime(cpu.getWait());
    	setCombineTime(cpu.getCombined());

    }

	public void outputCpu() throws SigarException {
        org.hyperic.sigar.CpuInfo[] infos =
            this.sigar.getCpuInfoList();

        CpuPerc[] cpus =
            this.sigar.getCpuPercList();

      //  CpuTimer cpuTimer[] = this.sigar.getCpuPercList();
        org.hyperic.sigar.CpuInfo info = infos[0];
        long cacheSize = info.getCacheSize();
        setVendor(info.getVendor());
        setModel(info.getModel());
        setMhz(info.getMhz());
        setTotalCpu(info.getTotalCores());
     
    }

	@Override
	public void output(String[] arg0) throws SigarException {
		// TODO Auto-generated method stub
	}
	
	  public void measureLoadOfSystem() throws Exception {
  		outputMem(); //memory information
  		outputCpu(); //cpu information
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
		return totalCpu*100;
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

}
