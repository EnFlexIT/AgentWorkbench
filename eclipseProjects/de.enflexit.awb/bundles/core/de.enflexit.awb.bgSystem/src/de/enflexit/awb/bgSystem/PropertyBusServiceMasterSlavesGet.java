package de.enflexit.awb.bgSystem;

import java.util.List;

import de.enflexit.awb.bgSystem.db.dataModel.BgSystemPlatform;
import de.enflexit.awb.bgSystem.db.dataModel.PlatformStore;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceMasterSlavesGet can be used to get Data about
 * the slave systems connected to a server.master.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceMasterSlavesGet implements PropertyBusService {

	public final static String BGPLATFORM_CONTACTAGENT="bgplatform[X].contactagent";
	public final static String BGPLATFORM_PLATFORMNAME="bgplatform[X].platformname";
	
	public final static String BGPLATFORM_ISSERVER="bgplatform[X].server";
	public final static String BGPLATFORM_IPADDRESS="bgplatform[X].ipaddress";
	public final static String BGPLATFORM_URL="bgplatform[X].url";
	public final static String BGPLATFORM_JADEPORT="bgplatform[X].jadeport";
	public final static String BGPLATFORM_HTTPMTP="bgplatform[X].httpmtp";
	
	public final static String BGPLATFORM_VERSIONMAJOR="bgplatform[X].versionmajor";
	public final static String BGPLATFORM_VERSIONMINOR="bgplatform[X].versionminor";
	public final static String BGPLATFORM_VERSIONMICRO="bgplatform[X].versionmicro";
	public final static String BGPLATFORM_VERSIONBUILD="bgplatform[X].versionbuild";
	
	public final static String BGPLATFORM_OS_NAME="bgplatform[X].os.name";
	public final static String BGPLATFORM_OS_VERSION="bgplatform[X].os.version";
	public final static String BGPLATFORM_OS_ARCHITECTURE="bgplatform[X].os.architecture";
	
	public final static String BGPLATFORM_CPU_NAME="bgplatform[X].cpu.name";
	public final static String BGPLATFORM_CPU_NUMLOGICAL="bgplatform[X].cpu.numlogical";
	public final static String BGPLATFORM_CPU_NUMPHYSICAL="bgplatform[X].cpu.numphysical";
	public final static String BGPLATFORM_CPU_SPEEDMHZ="bgplatform[X].cpu.speedmhz";
	
	public final static String BGPLATFORM_MEMORYMB="bgplatform[X].memorymb";
	public final static String BGPLATFORM_BENCHMARKVALUE="bgplatform[X].benchmarkvalue";
	public final static String BGPLATFORM_CURRENTLYAVAILABLE="bgplatform[X].currentlyavailable";
	
	public final static String BGPLATFORM_TIME_ONLINESINCE="bgplatform[X].time.onlinesince";
	public final static String BGPLATFORM_TIME_LASTCONTACT="bgplatform[X].time.lastcontact";
	public final static String BGPLATFORM_LOCALTIME_ONLINESINCE="bgplatform[X].localtime.onlinesince";
	public final static String BGPLATFORM_LOCALTIME_LASTCONTACT="bgplatform[X].localtime.lastcontact";
	
	public final static String BGPLATFORM_CURRENT_CPULOAD="bgplatform[X].current.cpuload";
	public final static String BGPLATFORM_CURRENT_MEMORYLOAD="bgplatform[X].current.memoryload";
	public final static String BGPLATFORM_CURRENT_MEMORYLOADJVM="bgplatform[X].current.memoryloadjvm";
	public final static String BGPLATFORM_CURRENT_NUMTHREADS="bgplatform[X].current.numthreads";
	public final static String BGPLATFORM_CURRENT_THRESHOLDEXCEEDED="bgplatform[X].current.thresholdexceeded";

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {
		return "EXEC.MASTER.SLAVES.GET";
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#setProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public boolean setProperties(Properties properties, String arguments) {
		return false;
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public Properties getProperties(Properties properties, String arguments) {
		
		if (properties == null) properties = new Properties();
		
		PlatformStore platformStore = PlatformStore.getInstance();
		if (platformStore == null) {
			return properties;
		}
		
		int platformCounter = 0;
		List<BgSystemPlatform> platformList = platformStore.getPlatformList();
		
		for (BgSystemPlatform platform : platformList) {
			// --- Get all values for the next platform -------------------------------------------
			String agentName = platform.getContactAgent();
			String platformName = platform.getPlatformName();
			
			Boolean isServer = platform.isServer();
			String ipAddress = platform.getIpAddress();
			String url = platform.getUrl();
			Integer jadePort = platform.getJadePort();
			String http4mtp = platform.getHttp4mtp();
			
			Integer platformMajor = platform.getVersionMajor();
			Integer platformMinor = platform.getVersionMinor();
			Integer platformMicro = platform.getVersionMicro();
			String versionBuild = platform.getVersionBuild();
			
			String osName = platform.getOsName();
			String osVersion = platform.getOsVersion();
			String osArchitecture = platform.getOsArchitecture();
			
			String cpuName = platform.getCpuProcessorName();
			Integer cpuNumLogical = platform.getCpuNoOfLogical();
			Integer cpuNumPhysical = platform.getCpuNoOfPhysical();
			Integer cpuSpeedMhz = platform.getCpuSpeedMHz();
			
			Integer memoryMB = platform.getMemoryMB();
			Double benchmarkValue = platform.getBenchmarkValue();
			
			Long timeOnlineSince = platform.getTimeOnlineSince().getTimeInMillis();
			Long timeLastContact = platform.getTimeLastContact().getTimeInMillis();
			Long localTimeOnlineSince = platform.getLocalTimeOnlineSince().getTimeInMillis();
			Long localTimeLastContact = platform.getLocalTimeLastContact().getTimeInMillis();
			
			Boolean isPlatformAvailable = platform.isCurrentlyAvailable();
			
			Double currCpuLoad = platform.getCurrentLoadCPU();
			Double currMemoryLoad = platform.getCurrentLoadMemory();
			Double currMemoryLoadJvm = platform.getCurrentLoadMemoryJVM();
			Integer currNumThreads = platform.getCurrentLoadNoOfThreads();
			Boolean isThresholdExceeded = platform.isCurrentLoadThresholdExceeded();
			
			// --- Set the results ----------------------------------------------------------------
			properties.setStringValue(BGPLATFORM_PLATFORMNAME.replace("X", String.valueOf(platformCounter)), platformName);
			properties.setStringValue(BGPLATFORM_CONTACTAGENT.replace("X", String.valueOf(platformCounter)), agentName);
			
			properties.setBooleanValue(BGPLATFORM_ISSERVER.replace("X", String.valueOf(platformCounter)), isServer);
			properties.setStringValue(BGPLATFORM_IPADDRESS.replace("X", String.valueOf(platformCounter)), ipAddress);
			properties.setStringValue(BGPLATFORM_URL.replace("X", String.valueOf(platformCounter)), url);
			properties.setIntegerValue(BGPLATFORM_JADEPORT.replace("X", String.valueOf(platformCounter)), jadePort);
			properties.setStringValue(BGPLATFORM_HTTPMTP.replace("X", String.valueOf(platformCounter)), http4mtp);
			
			properties.setIntegerValue(BGPLATFORM_VERSIONMAJOR.replace("X", String.valueOf(platformCounter)), platformMajor);
			properties.setIntegerValue(BGPLATFORM_VERSIONMINOR.replace("X", String.valueOf(platformCounter)), platformMinor);
			properties.setIntegerValue(BGPLATFORM_VERSIONMICRO.replace("X", String.valueOf(platformCounter)), platformMicro);
			properties.setStringValue(BGPLATFORM_VERSIONBUILD.replace("X", String.valueOf(platformCounter)), versionBuild);
			
			properties.setStringValue(BGPLATFORM_OS_NAME.replace("X", String.valueOf(platformCounter)), osName);
			properties.setStringValue(BGPLATFORM_OS_VERSION.replace("X", String.valueOf(platformCounter)), osVersion);
			properties.setStringValue(BGPLATFORM_OS_ARCHITECTURE.replace("X", String.valueOf(platformCounter)), osArchitecture);
			
			properties.setStringValue(BGPLATFORM_CPU_NAME.replace("X", String.valueOf(platformCounter)), cpuName);
			properties.setIntegerValue(BGPLATFORM_CPU_NUMLOGICAL.replace("X", String.valueOf(platformCounter)), cpuNumLogical);
			properties.setIntegerValue(BGPLATFORM_CPU_NUMPHYSICAL.replace("X", String.valueOf(platformCounter)), cpuNumPhysical);
			properties.setIntegerValue(BGPLATFORM_CPU_SPEEDMHZ.replace("X", String.valueOf(platformCounter)), cpuSpeedMhz);
			
			properties.setIntegerValue(BGPLATFORM_MEMORYMB.replace("X", String.valueOf(platformCounter)), memoryMB);
			properties.setDoubleValue(BGPLATFORM_BENCHMARKVALUE.replace("X", String.valueOf(platformCounter)), benchmarkValue);
			
			properties.setLongValue(BGPLATFORM_TIME_ONLINESINCE.replace("X", String.valueOf(platformCounter)), timeOnlineSince);
			properties.setLongValue(BGPLATFORM_TIME_LASTCONTACT.replace("X", String.valueOf(platformCounter)), timeLastContact);
			properties.setLongValue(BGPLATFORM_LOCALTIME_ONLINESINCE.replace("X", String.valueOf(platformCounter)), localTimeOnlineSince);
			properties.setLongValue(BGPLATFORM_LOCALTIME_LASTCONTACT.replace("X", String.valueOf(platformCounter)), localTimeLastContact);
			
			properties.setBooleanValue(BGPLATFORM_CURRENTLYAVAILABLE.replace("X", String.valueOf(platformCounter)), isPlatformAvailable);
			
			properties.setDoubleValue(BGPLATFORM_CURRENT_CPULOAD.replace("X", String.valueOf(platformCounter)), currCpuLoad);
			properties.setDoubleValue(BGPLATFORM_CURRENT_MEMORYLOAD.replace("X", String.valueOf(platformCounter)), currMemoryLoad);
			properties.setDoubleValue(BGPLATFORM_CURRENT_MEMORYLOADJVM.replace("X", String.valueOf(platformCounter)), currMemoryLoadJvm);
			properties.setIntegerValue(BGPLATFORM_CURRENT_NUMTHREADS.replace("X", String.valueOf(platformCounter)), currNumThreads);
			properties.setBooleanValue(BGPLATFORM_CURRENT_THRESHOLDEXCEEDED.replace("X", String.valueOf(platformCounter)), isThresholdExceeded);
		
			platformCounter++;
		}
		return properties;
	}

}