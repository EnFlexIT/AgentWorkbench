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
}