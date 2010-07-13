package load;

import config.GlobalInfo;
import application.Application;


public class LoadMeasureThread extends Thread {
    
	private GlobalInfo global = Application.RunInfo;
	private int msInterval;
	private int useN4AvgCount;
	private LoadMeasure memCpuInfo;
	private LoadMeasureAVG memCpuInfoAvg;
	
	public LoadMeasureThread(Integer msInterval, Integer useN4AvgCount) {
		
		this.msInterval = msInterval;
		this.useN4AvgCount = useN4AvgCount;
		if (global.getLoadCurrentAvg() == null) {
			global.setLoadCurrentAvg(new LoadMeasureAVG(useN4AvgCount));
		}
		memCpuInfo = new LoadMeasure();
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
