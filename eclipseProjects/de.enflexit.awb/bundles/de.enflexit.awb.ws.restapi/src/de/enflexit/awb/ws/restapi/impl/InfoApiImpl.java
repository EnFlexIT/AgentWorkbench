package de.enflexit.awb.ws.restapi.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import de.enflexit.awb.simulation.load.LoadUnits;
import de.enflexit.awb.ws.restapi.RestApiConfiguration;
import de.enflexit.awb.ws.restapi.gen.InfoApi;
import de.enflexit.awb.ws.restapi.gen.InfoApiService;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;
import de.enflexit.awb.ws.restapi.gen.model.NetworkConnection;
import de.enflexit.awb.ws.restapi.gen.model.SystemInformation;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

/**
 * The individual implementation class for the {@link InfoApi}.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class InfoApiImpl extends InfoApiService {

	private int DEFAULT_ROUND_PRECISION = 2;
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.restapi.gen.InfoApiService#infoGet(javax.ws.rs.core.SecurityContext)
	 */
	@Override
	public Response infoGet(SecurityContext securityContext) throws NotFoundException {
		
		// --- Get OSHI information ---------------------------------
		SystemInfo si = new SystemInfo();
		OperatingSystem os = si.getOperatingSystem();
		HardwareAbstractionLayer hw = si.getHardware();
		
		// --- Create return instance and fill values ---------------
		SystemInformation sysInfo = new SystemInformation();
		this.setOperatingSystemInfo(sysInfo, os);
		this.setProcessorInfo(sysInfo, hw);
		this.setMemoryInfo(sysInfo, hw);
		this.setNetworkConnectionInfo(sysInfo, hw);

		return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(sysInfo).build();
	}

	/**
	 * Sets the operating system information to the specified {@link SystemInformation}.
	 *
	 * @param sysInfo the SystemInformation to fill
	 * @param os the OperatingSystem of OSHI
	 */
	private void setOperatingSystemInfo(SystemInformation sysInfo, OperatingSystem os ) {
		
		sysInfo.setOsDescription(os.toString());
		sysInfo.setOsFamilly(os.getFamily());
		sysInfo.setOsManufacturer(os.getManufacturer());
		sysInfo.setOsVersion(os.getVersionInfo().toString());
	}
	
	/**
	 * Sets the processor information to the specified {@link SystemInformation}.
	 *
	 * @param sysInfo the SystemInformation to fill
	 * @param hw the hardware info of OSHI
	 */
	private void setProcessorInfo(SystemInformation sysInfo, HardwareAbstractionLayer hw) {
		
		CentralProcessor proc = hw.getProcessor();
		
		String processorName = proc.getProcessorIdentifier().getName();
		double freqMhz = proc.getProcessorIdentifier().getVendorFreq() / Math.pow(10, 6);
		int noProcessorPhysical = proc.getPhysicalProcessorCount();
		int noProcessorLogical  = proc.getLogicalProcessorCount();

		sysInfo.setProcessorName(processorName);
		sysInfo.setProcessorFrequenceInMhz(freqMhz);
		sysInfo.setProcessorNoPhysical(noProcessorPhysical);
		sysInfo.setProcessorNoLogical(noProcessorLogical);
	}
	
	 /**
 	 * Will set up the memory and the swap information.
 	 *
	 * @param sysInfo the SystemInformation to fill
 	 * @param hw the  hardware info of OSHI
 	 */
    private void setMemoryInfo(SystemInformation sysInfo, HardwareAbstractionLayer hw) {
    	
    	GlobalMemory memory =  hw.getMemory();
    	long systemMemoryTotal = memory.getTotal();
    	long virtMemoryTotal   = memory.getVirtualMemory().getSwapTotal();
    	long jvmHeapMemoryMax  = Runtime.getRuntime().maxMemory();
    	
    	sysInfo.setMemoryTotalInGB(this.round(LoadUnits.bytes2(systemMemoryTotal, LoadUnits.CONVERT2_GIGA_BYTE)));
    	sysInfo.setSwapMemoryTotalInGB(this.round(LoadUnits.bytes2(virtMemoryTotal, LoadUnits.CONVERT2_GIGA_BYTE)));
    	sysInfo.setHeapMemoryMaxInGB(this.round(LoadUnits.bytes2(jvmHeapMemoryMax, LoadUnits.CONVERT2_GIGA_BYTE)));
    }
    
    /**
     * Will sets the network connections info.
     *
     * @param sysInfo the SystemInformation to fill
     * @param hw the  hardware info of OSHI
     */
    private void setNetworkConnectionInfo(SystemInformation sysInfo, HardwareAbstractionLayer hw) {
    	
    	ArrayList<NetworkConnection> netConnList = new ArrayList<>();
    	List<NetworkIF> networkIFList = hw.getNetworkIFs();
    	for (int i = 0; i < networkIFList.size(); i++) {
    		// --- Get the single OSHI instance ---------------------
    		NetworkIF netInt = networkIFList.get(i);
    		
    		// --- Fill the object ----------------------------------
    		NetworkConnection netConn = new NetworkConnection();
    		netConn.setName(netInt.getName() + " [" + netInt.getIfAlias() + "]");
    		netConn.setDisplayName(netInt.getDisplayName());
    		netConn.setMacAddress(netInt.getMacaddr());
    		netConn.setIp4Addresses(String.join(",", netInt.getIPv4addr()));
    		netConn.setIp6Addresses(String.join(",", netInt.getIPv6addr()));
    		netConn.setTrafficReceivedInMB(this.round(LoadUnits.bytes2(netInt.getBytesRecv(), LoadUnits.CONVERT2_MEGA_BYTE)));
    		netConn.setTrafficSendInMB(this.round(LoadUnits.bytes2(netInt.getBytesSent(), LoadUnits.CONVERT2_MEGA_BYTE)));
    		// --- Add to list --------------------------------------
    		netConnList.add(netConn);
		}
    	
    	// --- Sort result list -------------------------------------
    	Collections.sort(netConnList, new Comparator<NetworkConnection>() {
			@Override
			public int compare(NetworkConnection nc1, NetworkConnection nc2) {
				return nc1.getName().compareTo(nc2.getName());
			}
		});

    	// --- Set to SystemInformation -----------------------------
    	sysInfo.setNetworkConnections(netConnList);
    }
    
    
    
    /**
     * Rounds an incoming double value to the specified decimal.
     *
     * @param input the input
     * @return the double
     */
	private double round(double input) {
		return this.round(input, this.DEFAULT_ROUND_PRECISION);
	}
    /**
     * Rounds an incoming double value to the specified decimal.
     *
     * @param input the input
     * @param DEFAULT_ROUND_PRECISION the round precision
     * @return the double
     */
	private double round(double input, int roundPrecision) {
		return Math.round(input * Math.pow(10, roundPrecision)) / Math.pow(10, roundPrecision);
	}
    
}
