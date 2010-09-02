package mas.service.load;


public class LoadMeasureThread extends Thread {
    
	private boolean debugSigar = false;
	private boolean debugJVM = false;
	private boolean debugThreshold = false;
	private int debugUnit = LoadUnits.CONVERT2_MEGA_BYTE;
	
	public static String threadName = "Agent.GUI - Load Monitoring";
	private int localmsInterval;
	private int localuseN4AvgCount;
	
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
	private static LoadThresholdLeveles thresholdLeveles = new LoadThresholdLeveles(); 
	private static int thresholdLevelesExceeded = 0; 	
	private static int thresholdLeveleExceededCPU = 0;
	private static int thresholdLeveleExceededMemo = 0;
	private static int thresholdLeveleExceededNoThreads = 0;
	
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
		System.out.println("Starting Load-Measurements");
		this.setName(LoadMeasureThread.threadName);
		// ------------------------------------------------------

		while(true){
		try {
			// --- Measure here ---------------------------
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
				double fMemmory = LoadUnits.bytes2(measuredMemCpuData.getFreeMemory(), debugUnit);
				double uMemmory = LoadUnits.bytes2(measuredMemCpuData.getUseMemory(), debugUnit);
				
				System.out.println("Prozessor-Info: " + vendor + " [" + model + "] " + nCPU + " " + cpuMHZ + " => " );
				System.out.println("Zeiteausnutzung: " + timeCombined + " " + timeIdle + " " + timeSystem + " " + timeUser + " " + timeWait );
				System.out.println("Arbeitsspeicher: " + tMemory + " (" + fMemmory + "/" + uMemmory + ") ");
			}
			
			if (debugJVM) {
				double jvmMemoFree = LoadUnits.bytes2(measuredJVMData.getJvmMemoFree(), debugUnit); 
				double jvmMemoTotal = LoadUnits.bytes2(measuredJVMData.getJvmMemoTotal(), debugUnit);
				double jvmMemoMax = LoadUnits.bytes2(measuredJVMData.getJvmMemoMax(), debugUnit);
			   
				double jvmHeapInit = LoadUnits.bytes2(measuredJVMData.getJvmHeapInit(), debugUnit);
				double jvmHeapMax = LoadUnits.bytes2(measuredJVMData.getJvmHeapMax(), debugUnit);
				double jvmHeapCommited = LoadUnits.bytes2(measuredJVMData.getJvmHeapCommited(), debugUnit);
				double jvmHeapUsed = LoadUnits.bytes2(measuredJVMData.getJvmHeapUsed(), debugUnit);
			    
				double jvmThreadCount = measuredJVMData.getJvmThreadCount();
				
			    System.out.println( "JVM-Memo: (" + jvmMemoMax + " - " + jvmMemoTotal + " - " + jvmMemoFree + ") ");
			    System.out.println( "JVM-Heap: (" + jvmHeapInit + " / " + jvmHeapMax + ") Commited: " + jvmHeapCommited + " - Used: " + jvmHeapUsed );
			    System.out.println( "JVM-Number of Threads: " + jvmThreadCount );
			}

			// --- check values and Threshold-Levels ------
			setThresholdLevelesExceeded(this.isLevelExceeded());			
			
			// --- Wait for the measure-interval ----------
			sleep(localmsInterval);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	  } // --- End while --------------
		
	}
	
	/**
	 * This method ckecks if one of the Threshold-Levels is exceeded
	 */
	public int isLevelExceeded(){
		
		int levelExceeded = 0;
		thresholdLeveleExceededCPU = 0;
		thresholdLeveleExceededMemo = 0;
		thresholdLeveleExceededNoThreads = 0;
		
		// --- Current percentage "CPU used" --------------
		double tempCPU  = (double)Math.round((1-loadCurrentAvg.getCpuIdleTime())*10000)/100;
		// --- Current percentage "Memory used" -----------
		double tempMemo = (double)Math.round(((double)loadCurrentAvgJVM.getJvmHeapUsed() / (double)loadCurrentAvgJVM.getJvmHeapMax()) * 10000)/100;
		// --- Current number of running threads ----------
		int tempNoThreads = loadCurrentAvgJVM.getJvmThreadCount();
		
		if (debugThreshold) {
			System.out.println( );
			System.out.println( "CPU used: " + tempCPU + "% (" + thresholdLeveles.getThCpuL() + "/" + thresholdLeveles.getThCpuH() + ")" );
			System.out.println( "Memory used: " + tempMemo + "% (" + thresholdLeveles.getThMemoL() + "/" + thresholdLeveles.getThMemoH() + ")" );
			System.out.println( "N-Threads: " + tempNoThreads + " (" + thresholdLeveles.getThNoThreadsL() + "/" + thresholdLeveles.getThNoThreadsH() + ")" );
		}
		
		// --- Check CPU-Usage ----------------------------
		if ( tempCPU > thresholdLeveles.getThCpuH()) {
			thresholdLeveleExceededCPU = 1;
		} else if ( tempCPU < thresholdLeveles.getThCpuL()) {
			thresholdLeveleExceededCPU = -1;
		}
		
		// --- Check Memory-Usage -------------------------
		if ( tempMemo > thresholdLeveles.getThMemoH()) {
			thresholdLeveleExceededMemo = 1;
		} else if ( tempMemo < thresholdLeveles.getThMemoL()) {
			thresholdLeveleExceededMemo = -1;
		}
		
		// --- Check Number of Threads --------------------
		if ( tempNoThreads > thresholdLeveles.getThNoThreadsH()) {
			thresholdLeveleExceededNoThreads = 1;
		} else if ( tempNoThreads < thresholdLeveles.getThNoThreadsL()) {
			thresholdLeveleExceededNoThreads = -1;
		}		
		
		
		// --- Set conclusion of Threshold Level Check ----
		if (thresholdLeveleExceededCPU>0 || thresholdLeveleExceededMemo>0 || thresholdLeveleExceededNoThreads>0) {
			levelExceeded=1;	// --- Hi-Alarm ---
		}
		if (thresholdLeveleExceededCPU<0 && thresholdLeveleExceededMemo<0 && thresholdLeveleExceededNoThreads ==0) {
			levelExceeded=-1;	// --- Low-Alarm ---
		}
		return levelExceeded;
		
	}
	
	/**
	 * @param thisThreadExecuted the thisThreadExecuted to set
	 */
	public static void setThisThreadExecuted(boolean thisThreadExecuted) {
		LoadMeasureThread.thisThreadExecuted = thisThreadExecuted;
	}
	/**
	 * @return the thisThreadExecuted
	 */
	public static boolean isThisThreadExecuted() {
		return thisThreadExecuted;
	}

	/**
	 * @param loadCurrent the loadCurrent to set
	 */
	public static void setLoadCurrent(LoadMeasureSigar loadCurrent2Set) {
		loadCurrent = loadCurrent2Set;
		loadCurrentAvg.put(loadCurrent);
	}
	/**
	 * @return the loadCurrent
	 */
	public static LoadMeasureSigar getLoadCurrent() {
		return loadCurrent;
	}

	/**
	 * @param loadAvgMemLoad the loadAvgMemLoad to set
	 */
	public static void setLoadCurrentAvg(LoadMeasureAvgSigar loadCurrentAvg2Set) {
		loadCurrentAvg = loadCurrentAvg2Set;
	}
	/**
	 * @return the loadAvgMemLoad
	 */
	public static LoadMeasureAvgSigar getLoadCurrentAvg() {
		return loadCurrentAvg;
	}
	
	/**
	 * @param loadCurrentJVM the loadCurrentJVM to set
	 */
	public static void setLoadCurrentJVM(LoadMeasureJVM loadCurrentJVM2Set) {
		loadCurrentJVM = loadCurrentJVM2Set;
		loadCurrentAvgJVM.put(loadCurrentJVM);
	}
	/**
	 * @return the loadCurrentJVM
	 */
	public static LoadMeasureJVM getLoadCurrentJVM() {
		return loadCurrentJVM;
	}
	
	/**
	 * @param loadCurrentAvgJVM the loadCurrentAvgJVM to set
	 */
	public static void setLoadCurrentAvgJVM(LoadMeasureAvgJVM loadCurrentAvgJVM2Set) {
		loadCurrentAvgJVM = loadCurrentAvgJVM2Set;
	}
	/**
	 * @return the loadCurrentAvgJVM
	 */
	public static LoadMeasureAvgJVM getLoadCurrentAvgJVM() {
		return loadCurrentAvgJVM;
	}

	/**
	 * @param thresholdLeveles the thresholdLeveles to set
	 */
	public static void setThresholdLeveles(LoadThresholdLeveles thresholdLeveles) {
		LoadMeasureThread.thresholdLeveles = thresholdLeveles;
	}
	/**
	 * @return the thresholdLeveles
	 */
	public static LoadThresholdLeveles getThresholdLeveles() {
		return thresholdLeveles;
	}

	/**
	 * @param thresholdLevelesExceeded the thresholdLevelesExceeded to set
	 */
	public static void setThresholdLevelesExceeded(int thresholdLevelesExceeded) {
		LoadMeasureThread.thresholdLevelesExceeded = thresholdLevelesExceeded;
	}
	/**
	 * @return the thresholdLevelesExceeded
	 */
	public static int getThresholdLevelesExceeded() {
		return thresholdLevelesExceeded;
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
	 * @return the thresholdLeveleExceededMemo
	 */
	public static int getThresholdLeveleExceededMemo() {
		return thresholdLeveleExceededMemo;
	}
	/**
	 * @param thresholdLeveleExceededMemo the thresholdLeveleExceededMemo to set
	 */
	public static void setThresholdLeveleExceededMemo(
			int thresholdLeveleExceededMemo) {
		LoadMeasureThread.thresholdLeveleExceededMemo = thresholdLeveleExceededMemo;
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
	public static void setThresholdLeveleExceededNoThreads(
			int thresholdLeveleExceededNoThreads) {
		LoadMeasureThread.thresholdLeveleExceededNoThreads = thresholdLeveleExceededNoThreads;
	}
}
