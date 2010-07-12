package config;

import load.memoryAndCpuInfo;

public class LoadMeasureThread extends Thread {
    
	private int msInterval;
	private int useN4AvgCount;
	private memoryAndCpuInfo memCpuInfo;
	
	public LoadMeasureThread(Integer msInterval, Integer useN4AvgCount) {
		this.msInterval = msInterval;
		this.useN4AvgCount = useN4AvgCount;
		memCpuInfo = new memoryAndCpuInfo();
	}
	
	@Override
	public void run() {

		try {
			sleep(msInterval);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       		
	}
}
