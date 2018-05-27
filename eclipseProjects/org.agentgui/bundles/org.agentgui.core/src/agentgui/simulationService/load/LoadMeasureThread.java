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

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;

import agentgui.core.application.Application;
import agentgui.simulationService.load.monitoring.AbstractMonitoringTask;
import agentgui.simulationService.load.monitoring.SingleAgentMonitor;
import agentgui.simulationService.load.threading.ThreadDetail;
import agentgui.simulationService.load.threading.ThreadProtocol;
import agentgui.simulationService.load.threading.ThreadProtocolReceiver;

/**
 * This class represents the Thread, which is permanently running on a system
 * and observes the current system information and its loads. Therefore the classes
 * {@link LoadMeasureOSHI}, {@link LoadMeasureAvgOSHI}, {@link LoadMeasureJVM} and 
 * {@link LoadMeasureAvgJVM} are used in detail.<br>
 * Starting this Thread the Thread will give itself the name <i>Agent.GUI - Load Monitoring</i>, 
 * which will prevent to start a second Thread of this kind on one JVM.<br>
 * This measurements will be executed every 500 milliseconds and the measured values
 * will be averaged over 5 measured values.<br>
 * The access methods of this Thread are static, in order to allow an access from
 * every point of the application, including a running agent system. 
 *
 * @see LoadMeasureOSHI
 * @see LoadMeasureAvgOSHI
 * @see LoadMeasureJVM
 * @see LoadMeasureAvgJVM
 *  
 * @author Christopher Nde - DAWIS - ICB - University of Duisburg - Essen
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class LoadMeasureThread extends Thread {
    
	/** Debugging option. */
	private static boolean debugInterval = false;
	/** Debugging option. */
	private static boolean debugOSHI = false;
	/** Debugging option. */
	private static boolean debugJVM = false;
	/** Debugging option. */
	private static boolean debugThreshold = false;
	/** Debugging option. */
	private static int debugUnit = LoadUnits.CONVERT2_MEGA_BYTE;
	
	/** The name of the running Thread. */
	public static String threadName = "Agent.GUI - Load Monitoring";
	
	/** The sampling interval of this Thread. */
	private int localIntervalInMS = 500;
	/** The number of measurements, which will be used for the mean value. */
	private static int localuseN4AvgCount = 5;
	
	/** Indicator if the local threshold is exceeded. */
	private static boolean thisThreadExecuted = false;

	// --- Load-Information for reading through getter and setter -------------
	/** Load instance for values of OSHI. */
	private static LoadMeasureOSHI currentLoadMeasureOSHI;
	/** Load instance for averaged values of OSHI. */
	private static LoadMeasureAvgOSHI avgLoadMeasureOSHI;
	/** Load instance for values of the JVM. */
	private static LoadMeasureJVM currentLoadMeasureJVM;
	/** Load instance for average values of the JVM. */
	private static LoadMeasureAvgJVM avgLoadMeasureJVM;
	
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
	/** The local systems benchmark value. As default the stored value will be taken */
	private static float compositeBenchmarkValue = Application.getGlobalInfo().getBenchValue();
	
	// --- Current Values of Interest -----------------------------------------
	/** Percentage value of CPU load. */
	private static float loadCPU = 0;
	/** Percentage value of system memory. */
	private static float loadRAM = 0;
	/** Percentage value of JVM memory. */
	private static float loadMemoryJVM = 0;
	/** Number of running threads. */
	private static Integer loadNoThreads = 0;
	
	/** Reminder for the last thread measurement */
	private static long lastThreadProtocolTimeStamp;
	
	// --- Monitoring tasks for this thread -----------------------------------
	private static ArrayList<AbstractMonitoringTask> monitoringTasks;
	
	
	/**
	 * Simple constructor of this class.
	 */
	public LoadMeasureThread() {
	}
	/**
	 * Constructor of this class with values for measure-interval
	 * and moving (sliding) average.
	 *
	 * @param msInterval the sampling interval in milliseconds 
	 * @param useN4AvgCount the number of measurements, which will be used for the average
	 */
	public LoadMeasureThread(Integer msInterval, Integer useN4AvgCount) {
		localIntervalInMS = msInterval;
		localuseN4AvgCount = useN4AvgCount;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		// ------------------------------------------------------
		// --- Is this thread is already executed in the JVM? ---
		// ------------------------------------------------------		
		if (getCurrentLoadMeasureJVM().threadExists(LoadMeasureThread.threadName)==true) return;

		System.out.println("Start measuring the system load !");
		this.setName(LoadMeasureThread.threadName);
		// ------------------------------------------------------
		
		long timeStart = 0;
		long timeEnd = 0;
		long timeNext = 0;
		long timeSleep = 0;
		
		while(true){
		try {
			// --- Define the time for the next measurement -----
			timeStart = System.currentTimeMillis();
			
			// --- Measure here ---------------------------------
			getCurrentLoadMeasureOSHI().measureLoadOfSystem();
			getCurrentLoadMeasureJVM().measureLoadOfSystem();
			
			getAvgLoadMeasureOSHI().put(getCurrentLoadMeasureOSHI().clone());
			getAvgLoadMeassureJVM().put(getCurrentLoadMeasureJVM().clone());
			
			if (debugOSHI) {
				String processorName = getCurrentLoadMeasureOSHI().getProcessorName().trim();
				Integer nPhysicalCPU = getCurrentLoadMeasureOSHI().getNumberOfPhysicalCPU();
				Integer nLogicalCPU = getCurrentLoadMeasureOSHI().getNumberOfLogicalCPU();
				Long cpuMHZ = getCurrentLoadMeasureOSHI().getMhz();
				
				double tMemory = LoadUnits.bytes2(getCurrentLoadMeasureOSHI().getTotalMemory(), debugUnit);
				double fMemory = LoadUnits.bytes2(getCurrentLoadMeasureOSHI().getFreeMemory(), debugUnit);
				double uMemory = LoadUnits.bytes2(getCurrentLoadMeasureOSHI().getUsedMemory(), debugUnit);
				double uMemoryPerc = getCurrentLoadMeasureOSHI().getUsedMemoryPercentage();
				
				double tMemorySwap = LoadUnits.bytes2(getCurrentLoadMeasureOSHI().getTotalMemorySwap(), debugUnit);
				double fMemorySwap = LoadUnits.bytes2(getCurrentLoadMeasureOSHI().getFreeMemorySwap(), debugUnit);
				double uMemorySwap = LoadUnits.bytes2(getCurrentLoadMeasureOSHI().getUsedMemorySwap(), debugUnit);
				
				System.out.println("Prozessor-Info:  " + processorName + " - " + nLogicalCPU + "(" + nPhysicalCPU + ") CPU's with " + cpuMHZ + "MHz " );
				System.out.println("Arbeitsspeicher: " + tMemory + "MB (" + fMemory + "MB+" + uMemory + "MB) = (" + uMemoryPerc + " %)");
				System.out.println("Swap-Speicher:   " + tMemorySwap + "MB (" + fMemorySwap + "MB + " + uMemorySwap + "MB) ");
			}
			
			if (debugJVM) {
				
				String jvmProcessID = getCurrentLoadMeasureJVM().getJvmPID();
				
				double jvmMemoFree = LoadUnits.bytes2(getCurrentLoadMeasureJVM().getJvmMemoFree(), debugUnit); 
				double jvmMemoTotal = LoadUnits.bytes2(getCurrentLoadMeasureJVM().getJvmMemoTotal(), debugUnit);
				double jvmMemoMax = LoadUnits.bytes2(getCurrentLoadMeasureJVM().getJvmMemoMax(), debugUnit);
			   
				double jvmHeapInit = LoadUnits.bytes2(getCurrentLoadMeasureJVM().getJvmHeapInit(), debugUnit);
				double jvmHeapMax = LoadUnits.bytes2(getCurrentLoadMeasureJVM().getJvmHeapMax(), debugUnit);
				double jvmHeapCommited = LoadUnits.bytes2(getCurrentLoadMeasureJVM().getJvmHeapCommitted(), debugUnit);
				double jvmHeapUsed = LoadUnits.bytes2(getCurrentLoadMeasureJVM().getJvmHeapUsed(), debugUnit);
			    
				double jvmThreadCount = getCurrentLoadMeasureJVM().getJvmThreadCount();
				
				System.out.println( "JVM-PID [ProcessID]: " + jvmProcessID);
			    System.out.println( "JVM-Memo: (" + jvmMemoMax + " - " + jvmMemoTotal + " - " + jvmMemoFree + ") ");
			    System.out.println( "JVM-Heap: (" + jvmHeapInit + " / " + jvmHeapMax + ") Commited: " + jvmHeapCommited + " - Used: " + jvmHeapUsed );
			    System.out.println( "JVM-Number of Threads: " + jvmThreadCount );
			}

			// --- Check values and Threshold-Levels ------------
			setThresholdLevelExceeded(this.isLevelExceeded());			
			
			// --- Do the registered monitoring tasks -----------
			this.doMonitoringTasks();
			
			// --- Define sleep interval ------------------------
			timeEnd = System.currentTimeMillis();
			timeNext = timeStart + localIntervalInMS;
			timeSleep = timeNext - timeEnd;
			if (timeSleep>0) {
				sleep(timeSleep);	
			}
			if (debugInterval) {
				long timeWork = timeEnd - timeStart;
				System.out.println("[" + this.getClass().getSimpleName() + "] Work Time: " + timeWork + " ms + Sleep Time: " + timeSleep + " ms = Interval: " + (timeWork + timeSleep) + " ms");
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
	private int isLevelExceeded(){
		
		int levelExceeded = 0;
		thresholdLevelExceededCPU = 0;
		thresholdLevelExceededMemoSystem = 0;
		thresholdLevelExceededMemoJVM = 0;
		thresholdLevelExceededNoThreads = 0;
		
		// --- Current percentage "CPU used" --------------
		loadCPU = (float) getAvgLoadMeasureOSHI().getCPU_Usage();

		// --- Current percentage load RAM ----------------
		long tempTotalMemoryCombined = getAvgLoadMeasureOSHI().getTotalMemory();
		long tempUseMemoryCombined = getAvgLoadMeasureOSHI().getUsedMemory();
		double tempMemoSystem = (double)Math.round(((double)tempUseMemoryCombined / tempTotalMemoryCombined) * 10000)/100;
		loadRAM = (float) tempMemoSystem;
		
		// --- Current percentage "Memory used in the JVM" 
		double tempMemoJVM = (double)Math.round(((double)getAvgLoadMeassureJVM().getJvmHeapUsed() / (double)getAvgLoadMeassureJVM().getJvmHeapMax()) * 10000)/100;
		loadMemoryJVM = (float) tempMemoJVM;
		
		// --- Current number of running threads ----------
		int tempNoThreads = getAvgLoadMeassureJVM().getJvmThreadCount();
		loadNoThreads = tempNoThreads;
		
		if (debugThreshold) {
			System.out.println( );
			System.out.println( "CPU used:        " + loadCPU + "% (" + thresholdLevels.getThCpuL() + "/" + thresholdLevels.getThCpuH() + ")" );
			System.out.println( "Sys-Memory used: " + tempMemoSystem + "% (" + thresholdLevels.getThMemoL() + "/" + thresholdLevels.getThMemoH() + ")" );
			System.out.println( "JVM-Memory used: " + tempMemoJVM + "% (" + thresholdLevels.getThMemoL() + "/" + thresholdLevels.getThMemoH() + ")" );
			System.out.println( "N-Threads:       " + tempNoThreads + " (" + thresholdLevels.getThNoThreadsL() + "/" + thresholdLevels.getThNoThreadsH() + ")" );
		}
		
		// --- Check CPU-Usage ----------------------------
		if (loadCPU > thresholdLevels.getThCpuH()) {
			thresholdLevelExceededCPU = 1;
		} else if ( loadCPU < thresholdLevels.getThCpuL()) {
			thresholdLevelExceededCPU = -1;
		}
		
		// --- Check Memory-Usage SYSTEM ------------------
		if (tempMemoSystem > thresholdLevels.getThMemoH()) {
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
	 * @return the currentLoadMeasureOSHI
	 */
	public static LoadMeasureOSHI getCurrentLoadMeasureOSHI() {
		if (currentLoadMeasureOSHI==null) {
			currentLoadMeasureOSHI = new LoadMeasureOSHI();
		}
		return currentLoadMeasureOSHI;
	}
	/**
	 * Gets the average load measure OSHI.
	 * @return the average load measure OSHI
	 */
	public static LoadMeasureAvgOSHI getAvgLoadMeasureOSHI() {
		if (avgLoadMeasureOSHI==null) {
			avgLoadMeasureOSHI = new LoadMeasureAvgOSHI(localuseN4AvgCount);
		}
		return avgLoadMeasureOSHI;
	}
	
	
	/**
	 * Gets the load current jvm.
	 * @return the currentLoadMeasureJVM
	 */
	public static LoadMeasureJVM getCurrentLoadMeasureJVM() {
		if (currentLoadMeasureJVM==null) {
			currentLoadMeasureJVM = new LoadMeasureJVM();
		}
		return currentLoadMeasureJVM;
	}
	/**
	 * Gets the load current avg jvm.
	 * @return the avgLoadMeasureJVM
	 */
	public static LoadMeasureAvgJVM getAvgLoadMeassureJVM() {
		if (avgLoadMeasureJVM==null) {
			avgLoadMeasureJVM = new LoadMeasureAvgJVM(localuseN4AvgCount);
		}
		return avgLoadMeasureJVM;
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
	 * Sets the load RAM.
	 * @param loadMemorySystem the new load RAM
	 */
	public static void setLoadRAM(float loadMemorySystem) {
		LoadMeasureThread.loadRAM = loadMemorySystem;
	}
	/**
	 * Gets the load RAM.
	 * @return the load RAM
	 */
	public static float getLoadRAM() {
		return loadRAM;
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

	/**
	 * Does the thread measurement. For this, a single thread will be started and the results 
	 * will be transfered to the specified {@link ThreadProtocolReceiver}.
	 * 
	 * @see ThreadProtocolReceiver
	 *
	 * @param timestamp the time stamp of the initial request
	 * @param threadProtocolReceiver the {@link ThreadProtocolReceiver}
	 */
	public static void doThreadMeasurement(final long timestamp, final ThreadProtocolReceiver threadProtocolReceiver) {

		if (timestamp!=lastThreadProtocolTimeStamp) {
			// --- Remind this time stamp in order to avoid double work -------
			lastThreadProtocolTimeStamp = timestamp;
			
			// --- Start Thread to do the work -------------------------------
			Runnable threadMeasurement = new Runnable() {
				@Override
				public void run() {
					
					// --- Create a protocol instance -------------------------
					ThreadProtocol tp = new ThreadProtocol(timestamp, getLoadCPU());

					// --- Configure ThreadMXBean if possible and needed ------ 
					ThreadMXBean tmxb = ManagementFactory.getThreadMXBean();
			        if (tmxb.isThreadCpuTimeSupported()==true){
			        	if (tmxb.isThreadCpuTimeEnabled()==false) {
			        		tmxb.setThreadCpuTimeEnabled(true);  
			        	}	
			        } else{
			        	System.err.println("ThreadTimeMeasurement not supported !!");
			        	threadProtocolReceiver.receiveThreadProtocol(null);
			        	return;
			        }
			        
			        // --- Do measurement ------------------------------------- 
			        String threadName;
			        long nanosCpuTime = 0L;
			        long nanosUserTime = 0L;
			        long factorMiliseconds = 1000000;

			        long[] threadIDs = tmxb.getAllThreadIds();
			        for (int i = 0; i < threadIDs.length; i++) {
			        	
			        	long threadID = threadIDs[i];
			            nanosCpuTime = tmxb.getThreadCpuTime(threadID);
			            nanosUserTime = tmxb.getThreadUserTime(threadID);
			            if (nanosCpuTime==-1 || nanosUserTime==-1) {
			            	continue;   // Thread died
			            }
			        	threadName = tmxb.getThreadInfo(threadID).getThreadName();
			        	
			        	// --- add times, converted to milliseconds, to thread-Protocol
			        	ThreadDetail tDetails = new ThreadDetail(threadName, (nanosCpuTime/factorMiliseconds), (nanosUserTime/factorMiliseconds));
			        	tp.getThreadDetails().add(tDetails);
			        }
			        // --- Send protocol to the requester of the measurement --
			        threadProtocolReceiver.receiveThreadProtocol(tp);
				}
			};
			// --- Start measurement thread -----------------------------------
			threadMeasurement.run();
		}
	}
	
	
	
	
	/**
	 * Returns the registered Monitoring tasks.
	 * @return the monitoring tasks
	 */
	private static ArrayList<AbstractMonitoringTask> getMonitoringTasks() {
		if (monitoringTasks==null) {
			monitoringTasks = new ArrayList<>();					
		}
		return monitoringTasks;
	}
	/**
	 * Can be used to register a JVM monitoring task.
	 * @param monitoringTask the monitoring task
	 */
	public static void registerMonitoringTask(AbstractMonitoringTask monitoringTask) {
		if (getMonitoringTasks().contains(monitoringTask)==false) {
			getMonitoringTasks().add(monitoringTask);
		}
	}
	/**
	 * Removes the specified monitoring task.
	 * @param monitoringTask the monitoring task
	 */
	public static void removeMonitoringTask(AbstractMonitoringTask monitoringTask) {
		getMonitoringTasks().remove(monitoringTask);
	}
	/**
	 * Removes the monitoring tasks for agents.
	 */
	public static void removeMonitoringTasksForAgents() {
		ArrayList<AbstractMonitoringTask> tasks = new ArrayList<>(LoadMeasureThread.getMonitoringTasks());
		for (int i = 0; i < tasks.size(); i++) {
			AbstractMonitoringTask task = tasks.get(i);
			if (task instanceof SingleAgentMonitor) {
				getMonitoringTasks().remove(task);
			}
		}
	}
	/**
	 * Does the tasks of the registered monitoring tasks.
	 */
	private void doMonitoringTasks() {

		if (LoadMeasureThread.getMonitoringTasks().size()==0) return; 
		
		// --- Execute the defined tasks ------------------
		ArrayList<AbstractMonitoringTask> tasks = new ArrayList<>(LoadMeasureThread.getMonitoringTasks());
		for (int i = 0; i < tasks.size(); i++) {
			// --- Execute single task --------------------
			AbstractMonitoringTask task = tasks.get(i);
			try {
				task.doMonitoringTask();
			} catch (Exception ex) {
				System.err.println("Error while executing '" + task.getTaskDescription() + "': " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
	
}
