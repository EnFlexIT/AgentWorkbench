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

import de.enflexit.oshi.SystemInfoTest;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

/**
 * This class provides load information for each CPU and the memory found on the 
 * current system - regardless, if it is Windows, UNIX or Linux-system. 
 * Therefore, the the OSHI library is used that is bundles within de.enflexit.oshi
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LoadMeasureSigar implements Cloneable {
	
	private boolean debug = false;
	private SystemInfo systemInfo;
	
	/** +++ The information about the available CPU's. +++ */
	/** The vendor of the chip set. */
	private String vendor=null;
	/** The processor speed. */
	private long Mhz;
	/** The model description. */
	private String model;
	/** The number of cores on this machine. */
	private int totalCpu;
	
	/** +++ The memory usage. +++*/
	/** A memory information. */
	private long totalMemory;			// Bytes
	/** A memory information. */
	private long freeMemory;			// Bytes
	/** A memory information. */
	private long useMemory;				// Bytes
	/** A memory information. */
	private double usedMemoryPercent;	// %
	
	/** +++ The swap memory usage . +++ */
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
        
        try {
        	// --- Print OSHI example --------------------- 
        	if (debug) SystemInfoTest.main(null);
        	
     		// --- getting CPU information ----------------
			this.setProcessorInformation(); 
			// --- get the first measurements --------------
			this.measureLoadOfSystem();
		
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
	 * Measures load of the system, by using the OSHI functionalities.
	 * @throws Exception the exception
	 */
	public void measureLoadOfSystem() throws Exception {
		this.setMemoryInformation();	// --- memory information ---------
	}

	/**
	 * Will set up the CPU information.
	 * @throws SigarException 
	 */
	public void setProcessorInformation() {
		CentralProcessor proc = this.getSystemInfo().getHardware().getProcessor();
		if (proc!=null ){
	        this.setVendor(proc.getVendor());
	        this.setModel(proc.getModel());
	        double freqMhz = proc.getVendorFreq() / Math.pow(10, 6);
	        this.setMhz((long) freqMhz);
	        this.setTotalCpu(proc.getPhysicalProcessorCount());
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
    	setUseMemory(this.getTotalMemory()-this.getFreeMemory());
    	
    	setUsedMemoryPercent( this.doubleRound(((double)this.getUseMemory() / (double)this.getTotalMemory()*100.0)) );
    	
    	setTotalMemorySwap(memory.getSwapTotal());
    	setUseMemorySwap(memory.getSwapUsed());
    	setFreeMemorySwap(this.getTotalMemorySwap()-this.getUseMemorySwap());
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
