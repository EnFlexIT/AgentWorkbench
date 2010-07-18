package load;

import config.GlobalInfo;
import application.Application;


public class LoadMeasureThreadSigar extends Thread {
    
	private GlobalInfo global = Application.RunInfo;
	private int msInterval;
	private int useN4AvgCount;
	private LoadMeasureSigar memCpuInfo;
	private LoadMeasureAvgSigar memCpuInfoAvg;
	
	public LoadMeasureThreadSigar(Integer msInterval, Integer useN4AvgCount) {
		
		this.msInterval = msInterval;
		this.useN4AvgCount = useN4AvgCount;
		if (global.getLoadCurrentAvg() == null) {
			global.setLoadCurrentAvg(new LoadMeasureAvgSigar(useN4AvgCount));
		}
		memCpuInfo = new LoadMeasureSigar();
	}
	
	@Override
	public void run() {

		while(true){
		try {
			memCpuInfo.measureLoadOfSystem();
			global.setLoadCurrent(memCpuInfo);
			sleep(msInterval);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }	
	}
}
