package load;

import application.Application;
import config.GlobalInfo;


public class LoadMeasureThreadJVM extends Thread {
	
	private GlobalInfo global = Application.RunInfo;
	private int msInterval;
	private int useN4AvgCount;
	private LoadMeasureJVM measureJVM;
	private LoadMeasureJVM memCpuInfo;
	private LoadMeasureAvgJVM memCpuInfoAvg;
	
	public LoadMeasureThreadJVM(Integer msInterval, Integer useN4AvgCount) {
		this.msInterval = msInterval;
		this.useN4AvgCount = useN4AvgCount;
		
		if (global.getLoadCurrentAvg() == null) {
			global.setLoadCurrentAvgJVM(new LoadMeasureAvgJVM(useN4AvgCount));
		}
		
		measureJVM = new LoadMeasureJVM();
	}
	
	@Override
	public void run() {

		while(true){
		try {
			measureJVM.measureLoadOfSystem();
			global.setLoadCurrentJVM(memCpuInfo);
			sleep(msInterval);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }	
	}
}
