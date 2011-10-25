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

/**
 * This class represents the Thread, which is permanently running on a system
 * and observes the current system information and its loads. Therefore the classes
 * {@link LoadMeasureSigar}, {@link LoadMeasureAvgSigar}, {@link LoadMeasureJVM} and 
 * {@link LoadMeasureAvgJVM} are used in detail.<br>
 * Starting this Thread the Thread will give itself the name <i>Agent.GUI - Load Monitoring</i>, 
 * which will prevent to start a second Thread of this kind on one JVM.<br>
 * This measurements will be executed every 500 milliseconds and the measured values
 * will be averaged over 5 measured values.<br>
 * The access methods of this Thread are static, in order to allow an access from
 * every point of the application, including a running agent system. 
 *
 * @see LoadMeasureSigar
 * @see LoadMeasureAvgSigar
 * @see LoadMeasureJVM
 * @see LoadMeasureAvgJVM
 *  
 * @author Christopher Nde - DAWIS - ICB - University of Duisburg - Essen
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LoadMeasureThread extends Thread {
    
	/** Debugging option. */
	private static boolean debugInterval = false;
	/** Debugging option. */
	private static boolean debugSigar = false;
	/** Debugging option. */
	private static boolean debugJVM = false;
	/** Debugging option. */
	private static boolean debugThreshold = false;
	/** Debugging option. */
	private static int debugUnit = LoadUnits.CONVERT2_MEGA_BYTE;
	
	/** The name of the running Thread. */
	public static String threadName = "Agent.GUI - Load Monitoring";
	
	/** The sampling interval of this Thread. */
	private int localmsInterval = 500;
	/** The number of measurements, which will be used for the mean value. */
	private int localuseN4AvgCount = 5;
	
	/** Indicator if the local threshold is exceeded. */
	private static boolean thisThreadExecuted = false;

	// --- The measurements, that are read by other same kind instances -------
	/** The current measurements using Hyperic-SIGAR. */
	private LoadMeasureSigar measuredMemCpuData = new LoadMeasureSigar();
	/** The current measurements using the on board utilities of the JVM. */
	private LoadMeasureJVM measuredJVMData = new LoadMeasureJVM();
	
	// --- Load-Information for reading through getter and setter -------------
	/** Load instance for values of Hyperic-SIGAR. */
	private static LoadMeasureSigar loadCurrent = null;
	/** Load instance for averaged values of Hyperic-SIGAR. */
	private static LoadMeasureAvgSigar loadCurrentAvg = null;
	/** Load instance for values of the JVM. */
	private static LoadMeasureJVM loadCurrentJVM = null;
	/** Load instance for average values of the JVM. */
	private static LoadMeasureAvgJVM loadCurrentAvgJVM = null;
	
	// --- Threshold-Information ----------------------------------------------
	/** Threshold-Information. */
	private static LoadThresholdLevels thresholdLevels = new LoadThresholdLevels();
	/** Threshold-Information. */
	private static int thresholdLevelExceeded = 0;
	/** Threshold-Information. */
	private static int thresholdLevelExceededCPU = 0;
	/** Threshold-Information. */
	private static int thresholdLevelExceededMemoSystem = 0;
	/** Threshold-Information. */
	private static int thresholdLevelExceededMemoJVM = 0;
	/** Threshold-Information. */
	private static int thresholdLevelExceededNoThreads = 0;
	
	// --- Resulting Benchmark-Value ------------------------------------------
	/** The local benchmark value. */
	private static float compositeBenchmarkValue = 0;
	
	// --- Current Values of Interest -----------------------------------------
	/** Percentage value of CPU load. */
	private static float loadCPU = 0;
	/** Percentage value of JVM memory. */
	private static float loadMemoryJVM = 0;
	/** Percentage value of system memory. */
	private static float loadMemorySystem = 0;
	/** Number of running threads. */
	private static Integer loadNoThreads = 0;
	
	/**
	 * Simple constructor of this class.
	 */
	public LoadMeasureThread() {
		loadCurrentAvg = new LoadMeasureAvgSigar(localuseN4AvgCount);
		loadCurrentAvgJVM = new LoadMeasureAvgJVM(localuseN4AvgCount);
	}

	/**
	 * Constructor of this class with values for measure-interval
	 * and moving (sliding) average.
	 *
	 * @param msInterval the sampling interval in milliseconds 
	 * @param useN4AvgCount the number of measurements, which will be used for the average
	 */
	public LoadMeasureThread(Integer msInterval, Integer useN4AvgCount) {
		localmsInterval = msInterval;
		localuseN4AvgCount = useN4AvgCount;
		loadCurrentAvg = new LoadMeasureAvgSigar(localuseN4AvgCount);
		loadCurrentAvgJVM = new LoadMeasureAvgJVM(localuseN4AvgCount);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		// ------------------------------------------------------
		// --- Is this class already executed on this Node ? ----
		// ------------------------------------------------------		
		if ( measuredJVMData.threadExists(LoadMeasureThread.threadName) ) {
			return;
		}		
		System.out.println("Start measuring the system load !");
		this.setName(LoadMeasureThread.threadName);
		// ------------------------------------------------------
		
		long timeStart = 0;
		long timeEnd = 0;
		long timeWork = 0;
		
		while(true){
		try {
			// --- Define the time for the next measurement -----
			timeStart = System.currentTimeMillis();

			// --- Measure here ---------------------------------
			this.measuredMemCpuData.measureLoadOfSystem();
			this.measuredJVMData.measureLoadOfSystem();
			LoadMeasureThread.setLoadCurrent(measuredMemCpuData);
			LoadMeasureThread.setLoadCurrentJVM(measuredJVMData);
			
			if (debugSigar) {
				String vendor = measuredMemCpuData.getVendor().trim();
				String model = measuredMemCpuData.getModel().trim();
				Integer nCPU = measuredMemCpuData.getTotalCpu();
				Long cpuMHZ = measuredMemCpuData.getMhz();
				
				double timeCombined = measuredMemCpuData.getCpuCombineTimeRounded();
				double timeIdle = measuredMemCpuData.getCpuIdleTimeRounded();
				double timeSystem = measuredMemCpuData.getCpuSystemTimeRounded();
				double timeUser = measuredMemCpuData.getCpuUserTimeRounded();
				double timeWait = measuredMemCpuData.getCpuWaitTimeRounded();

				double tMemory = LoadUnits.bytes2(measuredMemCpuData.getTotalMemory(), debugUnit);
				double fMemory = LoadUnits.bytes2(measuredMemCpuData.getFreeMemory(), debugUnit);
				double uMemory = LoadUnits.bytes2(measuredMemCpuData.getUseMemory(), debugUnit);
				double uMemoryPerc = measuredMemCpuData.getUsedMemoryPercent();
				
				double tMemorySwap = LoadUnits.bytes2(measuredMemCpuData.getTotalMemorySwap(), debugUnit);
				double fMemorySwap = LoadUnits.bytes2(measuredMemCpuData.getFreeMemorySwap(), debugUnit);
				double uMemorySwap = LoadUnits.bytes2(measuredMemCpuData.getUseMemorySwap(), debugUnit);
				
				System.out.println("Prozessor-Info:  " + vendor + " [" + model + "] " + nCPU + " " + cpuMHZ + "MHz " );
				System.out.println("Zeiteausnutzung: " + timeCombined + " " + timeIdle + " " + timeSystem + " " + timeUser + " " + timeWait );
				System.out.println("Arbeitsspeicher: " + tMemory + "MB (" + fMemory + "MB+" + uMemory + "MB) = (" + uMemoryPerc + " %)");
				System.out.println("Swap-Speicher:   " + tMemorySwap + "MB (" + fMemorySwap + "MB+" + uMemorySwap + "MB) ");
			}
			
			if (debugJVM) {
				
				String jvmProcessID = measuredJVMData.getJvmPID();
				
				double jvmMemoFree = LoadUnits.bytes2(measuredJVMData.getJvmMemoFree(), debugUnit); 
				double jvmMemoTotal = LoadUnits.bytes2(measuredJVMData.getJvmMemoTotal(), debugUnit);
				double jvmMemoMax = LoadUnits.bytes2(measuredJVMData.getJvmMemoMax(), debugUnit);
			   
				double jvmHeapInit = LoadUnits.bytes2(measuredJVMData.getJvmHeapInit(), debugUnit);
				double jvmHeapMax = LoadUnits.bytes2(measuredJVMData.getJvmHeapMax(), debugUnit);
				double jvmHeapCommited = LoadUnits.bytes2(measuredJVMData.getJvmHeapCommitted(), debugUnit);
				double jvmHeapUsed = LoadUnits.bytes2(measuredJVMData.getJvmHeapUsed(), debugUnit);
			    
				double jvmThreadCount = measuredJVMData.getJvmThreadCount();
				
				System.out.println( "JVM-PID [ProcessID]: " + jvmProcessID);
			    System.out.println( "JVM-Memo: (" + jvmMemoMax + " - " + jvmMemoTotal + " - " + jvmMemoFree + ") ");
			    System.out.println( "JVM-Heap: (" + jvmHeapInit + " / " + jvmHeapMax + ") Commited: " + jvmHeapCommited + " - Used: " + jvmHeapUsed );
			    System.out.println( "JVM-Number of Threads: " + jvmThreadCount );
			}

			// --- check values and Threshold-Levels ------------
			setThresholdLevelExceeded(LoadMeasureThread.isLevelExceeded());			
			
			// --- Wait for the end of the measure-interval -----
			sleep(localmsInterval);	
			if (debugInterval) {
				timeEnd = System.currentTimeMillis();
				timeWork = timeEnd - timeStart;
				System.out.println("=> Start-Time: " + timeStart);
				System.out.println("=> End-Time:   " + timeEnd);
				System.out.println("=> RunTime:    " + timeWork + "ms");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	  } // --- End while --------------
		
	}
	
	/**
	 * This method checks if one of the Threshold-Levels is exceeded.
	 * @return an int, which can be 0 (OK), +1 (system overloaded) or -1 (system underloaded). 
	 */
	public static int isLevelExceeded(){
		
		int levelExceeded = 0;
		thresholdLevelExceededCPU = 0;
		thresholdLevelExceededMemoSystem = 0;
		thresholdLevelExceededMemoJVM = 0;
		thresholdLevelExceededNoThreads = 0;
		
		// --- Current percentage "CPU used" --------------
//		double tempCPU  = loadCurrentAvg.getUsedMemoryPercent();
		double tempCPU  = (double)Math.round((1-loadCurrentAvg.getCpuIdleTime())*10000)/100;
		loadCPU = (float) tempCPU;

		// --- Current percentage "Memory used in System" -
		long tempTotalMemoryCombined = loadCurrentAvg.getTotalMemory() + loadCurrentAvg.getTotalMemorySwap();
		long tempUseMemoryCombined = loadCurrentAvg.getUseMemory() + loadCurrentAvg.getUseMemorySwap();
		double tempMemoSystem = (double)Math.round(((double)tempUseMemoryCombined / tempTotalMemoryCombined) * 10000)/100;
		loadMemorySystem = (float) tempMemoSystem;
		
		// --- Current percentage "Memory used in the JVM" 
		double tempMemoJVM = (double)Math.round(((double)loadCurrentAvgJVM.getJvmHeapUsed() / (double)loadCurrentAvgJVM.getJvmHeapMax()) * 10000)/100;
		loadMemoryJVM = (float) tempMemoJVM;
		
		// --- Current number of running threads ----------
		int tempNoThreads = loadCurrentAvgJVM.getJvmThreadCount();
		loadNoThreads = tempNoThreads;
		
		if (debugThreshold) {
			System.out.println( );
			System.out.println( "CPU used:        " + tempCPU + "% (" + thresholdLevels.getThCpuL() + "/" + thresholdLevels.getThCpuH() + ")" );
			System.out.println( "Sys-Memory used: " + tempMemoSystem + "% (" + thresholdLevels.getThMemoL() + "/" + thresholdLevels.getThMemoH() + ")" );
			System.out.println( "JVM-Memory used: " + tempMemoJVM + "% (" + thresholdLevels.getThMemoL() + "/" + thresholdLevels.getThMemoH() + ")" );
			System.out.println( "N-Threads:       " + tempNoThreads + " (" + thresholdLevels.getThNoThreadsL() + "/" + thresholdLevels.getThNoThreadsH() + ")" );
		}
		
		// --- Check CPU-Usage ----------------------------
		if ( tempCPU > thresholdLevels.getThCpuH()) {
			thresholdLevelExceededCPU = 1;
		} else if ( tempCPU < thresholdLevels.getThCpuL()) {
			thresholdLevelExceededCPU = -1;
		}
		
		// --- Check Memory-Usage SYSTEM ------------------
		if ( tempMemoSystem > thresholdLevels.getThMemoH()) {
			thresholdLevelExceededMemoSystem = 1;
		} else if ( tempMemoSystem < thresholdLevels.getThMemoL()) {
			thresholdLevelExceededMemoSystem = -1;
		}
		
		// --- Check Memory-Usage JVM ---------------------
		if ( tempMemoJVM > thresholdLevels.getThMemoH()) {
			thresholdLevelExceededMemoJVM = 1;
		} else if ( tempMemoJVM < thresholdLevels.getThMemoL()) {
			thresholdLevelExceededMemoJVM = -1;
		}
		
		// --- Check Number of Threads --------------------
		if ( tempNoThreads > thresholdLevels.getThNoThreadsH()) {
			thresholdLevelExceededNoThreads = 1;
		} else if ( tempNoThreads < thresholdLevels.getThNoThreadsL()) {
			thresholdLevelExceededNoThreads = -1;
		}		
		
		
		// --- Set conclusion of Threshold Level Check ----
		if (thresholdLevelExceededCPU > 0 || 
			thresholdLevelExceededMemoSystem > 0 || 
			thresholdLevelExceededMemoJVM > 0 ||
			thresholdLevelExceededNoThreads > 0) {
			levelExceeded = 1;	// --- Hi-Alarm ---
		}
		if (thresholdLevelExceededCPU < 0 && 
			thresholdLevelExceededMemoSystem < 0 && 
			thresholdLevelExceededMemoJVM < 0 &&
			thresholdLevelExceededNoThreads < 0) {
			levelExceeded = -1;	// --- Low-Alarm ---
		}
		return levelExceeded;
		
	}
	
	/**
	 * Checks if is this thread executed.
	 * @return the thisThreadExecuted
	 */
	public static boolean isThisThreadExecuted() {
		return thisThreadExecuted;
	}
	/**
	 * Sets the this thread executed.
	 * @param thisThreadExecuted the thisThreadExecuted to set
	 */
	public static void setThisThreadExecuted(boolean thisThreadExecuted) {
		LoadMeasureThread.thisThreadExecuted = thisThreadExecuted;
	}
	
	/**
	 * Gets the load current.
	 * @return the loadCurrent
	 */
	public static LoadMeasureSigar getLoadCurrent() {
		return loadCurrent;
	}
	/**
	 * Sets the load current.
	 * @param loadCurrent2Set the new load current
	 */
	public static void setLoadCurrent(LoadMeasureSigar loadCurrent2Set) {
		loadCurrent = loadCurrent2Set;
		loadCurrentAvg.put(loadCurrent);
	}

	/**
	 * Gets the load current avg.
	 * @return the loadAvgMemLoad
	 */
	public static LoadMeasureAvgSigar getLoadCurrentAvg() {
		return loadCurrentAvg;
	}
	/**
	 * Sets the load current avg.
	 * @param loadCurrentAvg2Set the new load current avg
	 */
	public static void setLoadCurrentAvg(LoadMeasureAvgSigar loadCurrentAvg2Set) {
		loadCurrentAvg = loadCurrentAvg2Set;
	}
	
	/**
	 * Gets the load current jvm.
	 * @return the loadCurrentJVM
	 */
	public static LoadMeasureJVM getLoadCurrentJVM() {
		return loadCurrentJVM;
	}
	/**
	 * Sets the load current jvm.
	 * @param loadCurrentJVM2Set the new load current jvm
	 */
	public static void setLoadCurrentJVM(LoadMeasureJVM loadCurrentJVM2Set) {
		loadCurrentJVM = loadCurrentJVM2Set;
		loadCurrentAvgJVM.put(loadCurrentJVM);
	}
	
	/**
	 * Gets the load current avg jvm.
	 * @return the loadCurrentAvgJVM
	 */
	public static LoadMeasureAvgJVM getLoadCurrentAvgJVM() {
		return loadCurrentAvgJVM;
	}
	/**
	 * Sets the load current avg jvm.
	 * @param loadCurrentAvgJVM2Set the new load current avg jvm
	 */
	public static void setLoadCurrentAvgJVM(LoadMeasureAvgJVM loadCurrentAvgJVM2Set) {
		loadCurrentAvgJVM = loadCurrentAvgJVM2Set;
	}

	/**
	 * Gets the threshold levels.
	 * @return the thresholdLevel
	 */
	public static LoadThresholdLevels getThresholdLevels() {
		return thresholdLevels;
	}
	/**
	 * Sets the threshold levels.
	 * @param thresholdLevels the new threshold levels
	 */
	public static void setThresholdLevels(LoadThresholdLevels thresholdLevels) {
		LoadMeasureThread.thresholdLevels = thresholdLevels;
	}

	/**
	 * Gets the threshold levels exceeded.
	 * @return the thresholdLevelExceeded
	 */
	public static int getThresholdLevelExceeded() {
		return thresholdLevelExceeded;
	}
	/**
	 * Sets the threshold levels exceeded.
	 * @param thresholdLevelExceeded the thresholdLevelExceeded to set
	 */
	public static void setThresholdLevelExceeded(int thresholdLevelExceeded) {
		LoadMeasureThread.thresholdLevelExceeded = thresholdLevelExceeded;
	}
	
	/**
	 * Gets the threshold levele exceeded cpu.
	 * @return the thresholdLevelExceededCPU
	 */
	public static int getThresholdLevelExceededCPU() {
		return thresholdLevelExceededCPU;
	}
	/**
	 * Sets the threshold levele exceeded cpu.
	 * @param thresholdLevelExceededCPU the thresholdLevelExceededCPU to set
	 */
	public static void setThresholdLevelExceededCPU(int thresholdLevelExceededCPU) {
		LoadMeasureThread.thresholdLevelExceededCPU = thresholdLevelExceededCPU;
	}

	/**
	 * Gets the threshold level exceeded memo system.
	 * @return the thresholdLevelExceededMemoSystem
	 */
	public static int getThresholdLevelExceededMemoSystem() {
		return thresholdLevelExceededMemoSystem;
	}
	/**
	 * Sets the threshold level exceeded memo system.
	 * @param thresholdLevelExceededMemoSystem the thresholdLevelExceededMemoSystem to set
	 */
	public static void setThresholdLevelExceededMemoSystem(int thresholdLevelExceededMemoSystem) {
		LoadMeasureThread.thresholdLevelExceededMemoSystem = thresholdLevelExceededMemoSystem;
	}

	/**
	 * Gets the threshold level exceeded memo jvm.
	 * @return the thresholdLevelExceededMemoJVM
	 */
	public static int getThresholdLevelExceededMemoJVM() {
		return thresholdLevelExceededMemoJVM;
	}
	/**
	 * Sets the threshold level exceeded memo jvm.
	 * @param thresholdLevelExceededMemoJVM the thresholdLevelExceededMemoJVM to set
	 */
	public static void setThresholdLevelExceededMemoJVM(int thresholdLevelExceededMemoJVM) {
		LoadMeasureThread.thresholdLevelExceededMemoJVM = thresholdLevelExceededMemoJVM;
	}
	
	/**
	 * Gets the threshold level exceeded no threads.
	 * @return the thresholdLevelExceededNoThreads
	 */
	public static int getThresholdLevelExceededNoThreads() {
		return thresholdLevelExceededNoThreads;
	}
	/**
	 * Sets the threshold level exceeded no threads.
	 * @param thresholdLevelExceededNoThreads the thresholdLevelExceededNoThreads to set
	 */
	public static void setThresholdLevelExceededNoThreads(int thresholdLevelExceededNoThreads) {
		LoadMeasureThread.thresholdLevelExceededNoThreads = thresholdLevelExceededNoThreads;
	}

	/**
	 * Sets the composite benchmark value.
	 * @param compositeBenchmarkValue the compositeBenchmarkValue to set
	 */
	public static void setCompositeBenchmarkValue(float compositeBenchmarkValue) {
		LoadMeasureThread.compositeBenchmarkValue = compositeBenchmarkValue;
	}
	/**
	 * Gets the composite benchmark value.
	 * @return the compositeBenchmarkValue
	 */
	public static float getCompositeBenchmarkValue() {
		return compositeBenchmarkValue;
	}

	/**
	 * Gets the load cpu.
	 * @return the loadCPU
	 */
	public static float getLoadCPU() {
		return loadCPU;
	}
	/**
	 * Sets the load cpu.
	 * @param loadCPU the loadCPU to set
	 */
	public static void setLoadCPU(float loadCPU) {
		LoadMeasureThread.loadCPU = loadCPU;
	}

	/**
	 * Gets the load memory jvm.
	 * @return the loadMemory
	 */
	public static float getLoadMemoryJVM() {
		return loadMemoryJVM;
	}
	/**
	 * Sets the load memory jvm.
	 * @param loadMemoryJVM the new load memory jvm
	 */
	public static void setLoadMemoryJVM(float loadMemoryJVM) {
		LoadMeasureThread.loadMemoryJVM = loadMemoryJVM;
	}

	/**
	 * Sets the load memory system.
	 * @param loadMemorySystem the loadMemorySystem to set
	 */
	public static void setLoadMemorySystem(float loadMemorySystem) {
		LoadMeasureThread.loadMemorySystem = loadMemorySystem;
	}
	/**
	 * Gets the load memory system.
	 * @return the loadMemorySystem
	 */
	public static float getLoadMemorySystem() {
		return loadMemorySystem;
	}

	/**
	 * Gets the load no threads.
	 * @return the loadNoThreads
	 */
	public static Integer getLoadNoThreads() {
		return loadNoThreads;
	}
	/**
	 * Sets the load no threads.
	 * @param loadNoThreads the loadNoThreads to set
	 */
	public static void setLoadNoThreads(Integer loadNoThreads) {
		LoadMeasureThread.loadNoThreads = loadNoThreads;
	}
	
}
