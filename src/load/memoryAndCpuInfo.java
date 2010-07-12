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
import org.hyperic.sigar.cmd.Shell;
import org.hyperic.sigar.cmd.SigarCommandBase;

/**
 * Display cpu information for each cpu found on the system.
 */
public class memoryAndCpuInfo extends SigarCommandBase {
	

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
	
	
    public boolean displayTimes = true;
    
    public memoryAndCpuInfo(Shell shell) {
        super(shell);
        try {
			mem = this.sigar.getMem();
			cpuPerc = this.sigar.getCpuPerc();
		} catch (SigarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public memoryAndCpuInfo() {
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
    	   System.out.println("");
  	  Mem mem   = this.sigar.getMem();

  	  
  	  //printing the Values of the memory
        Object[] header = new Object[] { "total", "used", "free" };

        Object[] memRow = new Object[] {
            format(mem.getTotal()),
            format(mem.getUsed()),
            format(mem.getFree())
        };

        Object[] actualRow = new Object[] {
            format(mem.getActualUsed()),
            format(mem.getActualFree())
        };

        printf("%18s %10s %10s", header);

        printf("Mem:    %10ld %10ld %10ld", memRow);

        //e.g. linux
        if ((mem.getUsed() != mem.getActualUsed()) ||
            (mem.getFree() != mem.getActualFree()))
        {
            printf("-/+ buffers/cache: " + "%10ld %10d", actualRow);
        }

        printf("RAM:    %10ls", new Object[] { mem.getRam() + "MB" });
        
        System.out.println("===========================================================================");
        System.out.println();
  }

    private void output(CpuPerc cpu) {
    	
        
    	println("User Time....." + CpuPerc.format(cpu.getUser()));
        println("Sys Time......" + CpuPerc.format(cpu.getSys()));
        println("Idle Time....." + CpuPerc.format(cpu.getIdle()));                //0,00% => cpu is not being used by any program
                                                                                  //or CPU is used by programs with very low priority
                                                                                  //100% => CPU is complete occupied
        println("Wait Time....." + CpuPerc.format(cpu.getWait()));
        println("Nice Time....." + CpuPerc.format(cpu.getNice()));
        println("Combined......" + CpuPerc.format(cpu.getCombined()));
        println("Irq Time......" + CpuPerc.format(cpu.getIrq()));
        
        
        if (SigarLoader.IS_LINUX) {
            println("SoftIrq Time.." + CpuPerc.format(cpu.getSoftIrq()));
            println("Stolen Time...." + CpuPerc.format(cpu.getStolen()));
        }
        println("");
    }

	public void outputCpu() throws SigarException {
        org.hyperic.sigar.CpuInfo[] infos =
            this.sigar.getCpuInfoList();

        CpuPerc[] cpus =
            this.sigar.getCpuPercList();

      //  CpuTimer cpuTimer[] = this.sigar.getCpuPercList();
        org.hyperic.sigar.CpuInfo info = infos[0];
        long cacheSize = info.getCacheSize();
        println("Vendor........." + info.getVendor());
        println("Model.........." + info.getModel());
        println("Mhz............" + info.getMhz());
        println("Total CPUs....." + info.getTotalCores());
        if ((info.getTotalCores() != info.getTotalSockets()) ||
            (info.getCoresPerSocket() > info.getTotalCores()))
        {
            println("Physical CPUs.." + info.getTotalSockets());
            println("Cores per CPU.." + info.getCoresPerSocket());
        }

        if (cacheSize != Sigar.FIELD_NOTIMPL) {
            println("Cache size...." + cacheSize);
        }
        println("");
        
        if (!this.displayTimes) {
            return;
        }
        System.out.println("===========================================================================");
        System.out.println();

        for (int i=0; i<cpus.length; i++) {
            println("CPU " + i + ".........");
            output(cpus[i]);
            System.out.println("===========================================================================");
            System.out.println();
        }

        println("Totals........");
        output(this.sigar.getCpuPerc());
        System.out.println("===========================================================================");
        System.out.println("===========================================================================");
        System.out.println();
    }

	  public void measureLoadOfSystem() throws Exception {
	    		
	    		outputMem();
	    		outputCpu();
	    }

	@Override
	public void output(String[] arg0) throws SigarException {
		// TODO Auto-generated method stub
		outputCpu();
		outputMem();
	}
}
