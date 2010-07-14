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

package game_of_life.agent_distributor;

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
public class memoryAndCpuLoad extends SigarCommandBase {
	

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
    
    public memoryAndCpuLoad(Shell shell) {
        super(shell);
        try {
			mem = this.sigar.getMem();
			cpuPerc = this.sigar.getCpuPerc();
		} catch (SigarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public memoryAndCpuLoad() {
        super();
        try {
			mem = this.sigar.getMem();
			cpuPerc = this.sigar.getCpuPerc();
		} catch (SigarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@Override
	public void output(String[] arg0) throws SigarException {
		// TODO Auto-generated method stub
		
	}
}
