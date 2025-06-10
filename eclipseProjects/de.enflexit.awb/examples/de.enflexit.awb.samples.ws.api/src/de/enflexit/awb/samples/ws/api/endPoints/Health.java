package de.enflexit.awb.samples.ws.api.endPoints;

import java.lang.management.ManagementFactory;

public class Health {

    private String hostname;
    private String ip;
    private long startTime;
    private long upTime;

    public Health(String hostname, String ip) {
        this.hostname = hostname;
        this.ip = ip;
        this.startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        this.upTime = ManagementFactory.getRuntimeMXBean().getUptime();
    }

	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getUpTime() {
		return upTime;
	}
	public void setUpTime(long upTime) {
		this.upTime = upTime;
	}
	
}