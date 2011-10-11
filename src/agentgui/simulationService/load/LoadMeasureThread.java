package agentgui.simulationService.load;



public class LoadMeasureThread extends Thread {
    
	private boolean debugInterval = false;
	private boolean debugSigar = false;
	private boolean debugJVM = false;
	private boolean debugThreshold = false;
	private int debugUnit = LoadUnits.CONVERT2_MEGA_BYTE;
	
	public static String threadName = "Agent.GUI - Load Monitoring";
	private int localmsInterval = 500;
	private int localuseN4AvgCount = 5;
	
	private static boolean thisThreadExecuted = false;
	
	// --- Load-Information for the current measurement -----------------------
	private LoadMeasureSigar measuredMemCpuData = new LoadMeasureSigar();
	private LoadMeasureJVM measuredJVMData = new LoadMeasureJVM();
	
	// --- Load-Information for reading through getter and setter -------------
	private static LoadMeasureSigar loadCurrent = null;
	private static LoadMeasureAvgSigar loadCurrentAvg = null;
	private static LoadMeasureJVM loadCurrentJVM = null;
	private static LoadMeasureAvgJVM loadCurrentAvgJVM = null;
	
	// --- Threshold-Information ----------------------------------------------
	private static LoadThresholdLevels thresholdLevels = new LoadThresholdLevels(); 
	private static int thresholdLevelesExceeded = 0; 	
	private static int thresholdLeveleExceededCPU = 0;
	private static int thresholdLeveleExceededMemoSystem = 0;
	private static int thresholdLeveleExceededMemoJVM = 0;
	private static int thresholdLeveleExceededNoThreads = 0;
	
	// --- Resulting Benchmark-Value ------------------------------------------
	private static float compositeBenchmarkValue = 0;
	
	// --- Current Values of Interest -----------------------------------------
	private static float loadCPU = 0;
	private static float loadMemoryJVM = 0;
	private static float loadMemorySystem = 0;
	private static Integer loadNoThreads = 0;
	
	/**
	 * Simple constructor of this class
	 */
	public LoadMeasureThread() {
		loadCurrentAvg = new LoadMeasureAvgSigar(localuseN4AvgCount);
		loadCurrentAvgJVM = new LoadMeasureAvgJVM(localuseN4AvgCount);
	}

	/**
	 * Constructor of this class with values for measure-interval
	 * and moving (sliding) average
	 */
	public LoadMeasureThread(Integer msInterval, Integer useN4AvgCount) {
		localmsInterval = msInterval;
		localuseN4AvgCount = useN4AvgCount;
		loadCurrentAvg = new LoadMeasureAvgSigar(localuseN4AvgCount);
		loadCurrentAvgJVM = new LoadMeasureAvgJVM(localuseN4AvgCount);
	}
	
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
				double jvmHeapCommited = LoadUnits.bytes2(measuredJVMData.getJvmHeapCommited(), debugUnit);
				double jvmHeapUsed = LoadUnits.bytes2(measuredJVMData.getJvmHeapUsed(), debugUnit);
			    
				double jvmThreadCount = measuredJVMData.getJvmThreadCount();
				
				System.out.println( "JVM-PID [ProcessID]: " + jvmProcessID);
			    System.out.println( "JVM-Memo: (" + jvmMemoMax + " - " + jvmMemoTotal + " - " + jvmMemoFree + ") ");
			    System.out.println( "JVM-Heap: (" + jvmHeapInit + " / " + jvmHeapMax + ") Commited: " + jvmHeapCommited + " - Used: " + jvmHeapUsed );
			    System.out.println( "JVM-Number of Threads: " + jvmThreadCount );
			}

			// --- check values and Threshold-Levels ------------
			setThresholdLevelesExceeded(this.isLevelExceeded());			
			
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
	 * This method checks if one of the Threshold-Levels is exceeded
	 */
	public int isLevelExceeded(){
		
		int levelExceeded = 0;
		thresholdLeveleExceededCPU = 0;
		thresholdLeveleExceededMemoSystem = 0;
		thresholdLeveleExceededMemoJVM = 0;
		thresholdLeveleExceededNoThreads = 0;
		
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
			thresholdLeveleExceededCPU = 1;
		} else if ( tempCPU < thresholdLevels.getThCpuL()) {
			thresholdLeveleExceededCPU = -1;
		}
		
		// --- Check Memory-Usage SYSTEM ------------------
		if ( tempMemoSystem > thresholdLevels.getThMemoH()) {
			thresholdLeveleExceededMemoSystem = 1;
		} else if ( tempMemoSystem < thresholdLevels.getThMemoL()) {
			thresholdLeveleExceededMemoSystem = -1;
		}
		
		// --- Check Memory-Usage JVM ---------------------
		if ( tempMemoJVM > thresholdLevels.getThMemoH()) {
			thresholdLeveleExceededMemoJVM = 1;
		} else if ( tempMemoJVM < thresholdLevels.getThMemoL()) {
			thresholdLeveleExceededMemoJVM = -1;
		}
		
		// --- Check Number of Threads --------------------
		if ( tempNoThreads > thresholdLevels.getThNoThreadsH()) {
			thresholdLeveleExceededNoThreads = 1;
		} else if ( tempNoThreads < thresholdLevels.getThNoThreadsL()) {
			thresholdLeveleExceededNoThreads = -1;
		}		
		
		
		// --- Set conclusion of Threshold Level Check ----
		if (thresholdLeveleExceededCPU > 0 || 
			thresholdLeveleExceededMemoSystem > 0 || 
			thresholdLeveleExceededMemoJVM > 0 ||
			thresholdLeveleExceededNoThreads > 0) {
			levelExceeded = 1;	// --- Hi-Alarm ---
		}
		if (thresholdLeveleExceededCPU < 0 && 
			thresholdLeveleExceededMemoSystem < 0 && 
			thresholdLeveleExceededMemoJVM < 0 &&
			thresholdLeveleExceededNoThreads < 0) {
			levelExceeded = -1;	// --- Low-Alarm ---
		}
		return levelExceeded;
		
	}
	
	/**
	 * @return the thisThreadExecuted
	 */
	public static boolean isThisThreadExecuted() {
		return thisThreadExecuted;
	}
	/**
	 * @param thisThreadExecuted the thisThreadExecuted to set
	 */
	public static void setThisThreadExecuted(boolean thisThreadExecuted) {
		LoadMeasureThread.thisThreadExecuted = thisThreadExecuted;
	}
	
	/**
	 * @return the loadCurrent
	 */
	public static LoadMeasureSigar getLoadCurrent() {
		return loadCurrent;
	}
	/**
	 * @param loadCurrent the loadCurrent to set
	 */
	public static void setLoadCurrent(LoadMeasureSigar loadCurrent2Set) {
		loadCurrent = loadCurrent2Set;
		loadCurrentAvg.put(loadCurrent);
	}

	/**
	 * @return the loadAvgMemLoad
	 */
	public static LoadMeasureAvgSigar getLoadCurrentAvg() {
		return loadCurrentAvg;
	}
	/**
	 * @param loadAvgMemLoad the loadAvgMemLoad to set
	 */
	public static void setLoadCurrentAvg(LoadMeasureAvgSigar loadCurrentAvg2Set) {
		loadCurrentAvg = loadCurrentAvg2Set;
	}
	
	/**
	 * @return the loadCurrentJVM
	 */
	public static LoadMeasureJVM getLoadCurrentJVM() {
		return loadCurrentJVM;
	}
	/**
	 * @param loadCurrentJVM the loadCurrentJVM to set
	 */
	public static void setLoadCurrentJVM(LoadMeasureJVM loadCurrentJVM2Set) {
		loadCurrentJVM = loadCurrentJVM2Set;
		loadCurrentAvgJVM.put(loadCurrentJVM);
	}
	
	/**
	 * @return the loadCurrentAvgJVM
	 */
	public static LoadMeasureAvgJVM getLoadCurrentAvgJVM() {
		return loadCurrentAvgJVM;
	}
	/**
	 * @param loadCurrentAvgJVM the loadCurrentAvgJVM to set
	 */
	public static void setLoadCurrentAvgJVM(LoadMeasureAvgJVM loadCurrentAvgJVM2Set) {
		loadCurrentAvgJVM = loadCurrentAvgJVM2Set;
	}

	/**
	 * @return the thresholdLeveles
	 */
	public static LoadThresholdLevels getThresholdLevels() {
		return thresholdLevels;
	}
	/**
	 * @param thresholdLeveles the thresholdLeveles to set
	 */
	public static void setThresholdLevels(LoadThresholdLevels thresholdLevels) {
		LoadMeasureThread.thresholdLevels = thresholdLevels;
	}

	/**
	 * @return the thresholdLevelesExceeded
	 */
	public static int getThresholdLevelesExceeded() {
		return thresholdLevelesExceeded;
	}
	/**
	 * @param thresholdLevelesExceeded the thresholdLevelesExceeded to set
	 */
	public static void setThresholdLevelesExceeded(int thresholdLevelesExceeded) {
		LoadMeasureThread.thresholdLevelesExceeded = thresholdLevelesExceeded;
	}
	
	/**
	 * @return the thresholdLeveleExceededCPU
	 */
	public static int getThresholdLeveleExceededCPU() {
		return thresholdLeveleExceededCPU;
	}
	/**
	 * @param thresholdLeveleExceededCPU the thresholdLeveleExceededCPU to set
	 */
	public static void setThresholdLeveleExceededCPU(int thresholdLeveleExceededCPU) {
		LoadMeasureThread.thresholdLeveleExceededCPU = thresholdLeveleExceededCPU;
	}

	/**
	 * @return the thresholdLeveleExceededMemoSystem
	 */
	public static int getThresholdLeveleExceededMemoSystem() {
		return thresholdLeveleExceededMemoSystem;
	}
	/**
	 * @param thresholdLeveleExceededMemoSystem the thresholdLeveleExceededMemoSystem to set
	 */
	public static void setThresholdLeveleExceededMemoSystem(
			int thresholdLeveleExceededMemoSystem) {
		LoadMeasureThread.thresholdLeveleExceededMemoSystem = thresholdLeveleExceededMemoSystem;
	}

	/**
	 * @return the thresholdLeveleExceededMemoJVM
	 */
	public static int getThresholdLeveleExceededMemoJVM() {
		return thresholdLeveleExceededMemoJVM;
	}
	/**
	 * @param thresholdLeveleExceededMemoJVM the thresholdLeveleExceededMemoJVM to set
	 */
	public static void setThresholdLeveleExceededMemoJVM(
			int thresholdLeveleExceededMemoJVM) {
		LoadMeasureThread.thresholdLeveleExceededMemoJVM = thresholdLeveleExceededMemoJVM;
	}
	
	/**
	 * @return the thresholdLeveleExceededNoThreads
	 */
	public static int getThresholdLeveleExceededNoThreads() {
		return thresholdLeveleExceededNoThreads;
	}
	/**
	 * @param thresholdLeveleExceededNoThreads the thresholdLeveleExceededNoThreads to set
	 */
	public static void setThresholdLeveleExceededNoThreads(int thresholdLeveleExceededNoThreads) {
		LoadMeasureThread.thresholdLeveleExceededNoThreads = thresholdLeveleExceededNoThreads;
	}

	/**
	 * @param compositeBenchmarkValue the compositeBenchmarkValue to set
	 */
	public static void setCompositeBenchmarkValue(float compositeBenchmarkValue) {
		LoadMeasureThread.compositeBenchmarkValue = compositeBenchmarkValue;
	}
	/**
	 * @return the compositeBenchmarkValue
	 */
	public static float getCompositeBenchmarkValue() {
		return compositeBenchmarkValue;
	}

	/**
	 * @return the loadCPU
	 */
	public static float getLoadCPU() {
		return loadCPU;
	}
	/**
	 * @param loadCPU the loadCPU to set
	 */
	public static void setLoadCPU(float loadCPU) {
		LoadMeasureThread.loadCPU = loadCPU;
	}

	/**
	 * @return the loadMemory
	 */
	public static float getLoadMemoryJVM() {
		return loadMemoryJVM;
	}
	/**
	 * @param loadMemory the loadMemory to set
	 */
	public static void setLoadMemoryJVM(float loadMemoryJVM) {
		LoadMeasureThread.loadMemoryJVM = loadMemoryJVM;
	}

	/**
	 * @param loadMemorySystem the loadMemorySystem to set
	 */
	public static void setLoadMemorySystem(float loadMemorySystem) {
		LoadMeasureThread.loadMemorySystem = loadMemorySystem;
	}
	/**
	 * @return the loadMemorySystem
	 */
	public static float getLoadMemorySystem() {
		return loadMemorySystem;
	}

	/**
	 * @return the loadNoThreads
	 */
	public static Integer getLoadNoThreads() {
		return loadNoThreads;
	}
	/**
	 * @param loadNoThreads the loadNoThreads to set
	 */
	public static void setLoadNoThreads(Integer loadNoThreads) {
		LoadMeasureThread.loadNoThreads = loadNoThreads;
	}
}
