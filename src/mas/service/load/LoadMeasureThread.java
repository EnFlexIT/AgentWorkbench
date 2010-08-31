package mas.service.load;

import application.Application;
import config.GlobalInfo;

public class LoadMeasureThread extends Thread {
    
	private int debugUnit = LoadUnits.CONVERT2_MEGA_BYTE;
	
	private boolean debugSigar = false;
	private boolean debugJVM = false;
	private boolean debugThreshold =false;
	
	
	private int localmsInterval;
	private int localuseN4AvgCount;
	private int threshold;
	
	private GlobalInfo global = Application.RunInfo;
	private LoadMeasureSigar measuredMemCpuData = new LoadMeasureSigar();
	private LoadMeasureJVM measuredJVMData = new LoadMeasureJVM();
	private LoadThreshold loadThreshold = null;
	
	private LoadMeasureAvgJVM loadMeasureAvgJVM= null;
	private LoadMeasureAvgSigar loadMeasureAvgSigar = null;
	
	public LoadMeasureThread(Integer msInterval, Integer useN4AvgCount,int thresholdValue, boolean startMeasurement) {
		
		localmsInterval = msInterval;
		localuseN4AvgCount = useN4AvgCount;
		this.threshold = thresholdValue;
		
		this.setName("Load Monitoring");
		
		loadMeasureAvgJVM = new LoadMeasureAvgJVM(localuseN4AvgCount);
		loadMeasureAvgSigar = new LoadMeasureAvgSigar(localuseN4AvgCount);
		loadThreshold = new LoadThreshold(loadMeasureAvgJVM, loadMeasureAvgSigar, threshold);
		
		global.setLoadCurrentAvg(loadMeasureAvgSigar);
		global.setLoadCurrentAvgJVM(loadMeasureAvgJVM);
		global.setLoadThreshold(loadThreshold);
		
		debugJVM = debugSigar = startMeasurement;

	}
	
	@Override
	public void run() {

		while(true){
		try {
			// --- Measure here ---------------------------
			measuredMemCpuData.measureLoadOfSystem();	//SIGAR
			measuredJVMData.measureLoadOfSystem();		//JVM
			global.setLoadCurrent(measuredMemCpuData);	//SIGAR average load
			global.setLoadCurrentJVM(measuredJVMData);	//JVM average load
			loadThreshold.controlThreshold();
			
			if (debugSigar) {
				String vendor = measuredMemCpuData.getVendor().trim();
				String model = measuredMemCpuData.getModel().trim();
				Integer nCPU = measuredMemCpuData.getTotalCpu();
				Long cpuMHZ = measuredMemCpuData.getMhz();
				
				Double timeCombined = measuredMemCpuData.getCpuCombineTimeRounded();
				Double timeIdle = measuredMemCpuData.getCpuIdleTimeRounded();
				Double timeSystem = measuredMemCpuData.getCpuSystemTimeRounded();
				Double timeUser = measuredMemCpuData.getCpuUserTimeRounded();
				Double timeWait = measuredMemCpuData.getCpuWaitTimeRounded();

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
			    
			    System.out.println( "JVM-Memo: (" + jvmMemoMax + " - " + jvmMemoTotal + " - " + jvmMemoFree + ") ");
			    System.out.println( "JVM-Heap: (" + jvmHeapInit + " / " + jvmHeapMax + ") Commited: " + jvmHeapCommited + " - Used: " + jvmHeapUsed );
				
			}
			
			if (debugThreshold) {
				
				//threshold info
			}
			
			sleep(localmsInterval);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	  } // --- End while --------------
		
	}
	
}
