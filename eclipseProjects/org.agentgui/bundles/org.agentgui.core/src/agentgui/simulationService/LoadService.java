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
package agentgui.simulationService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import agentgui.core.application.Application;
import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.core.jade.Platform;
import agentgui.simulationService.agents.LoadMeasureAgent;
import agentgui.simulationService.load.LoadAgentMap;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.load.LoadInformation;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;
import agentgui.simulationService.load.LoadInformation.NodeDescription;
import agentgui.simulationService.load.LoadMeasureOSHI;
import agentgui.simulationService.load.LoadMeasureThread;
import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.load.LoadUnits;
import agentgui.simulationService.load.threading.ThreadDetail;
import agentgui.simulationService.load.threading.ThreadProtocol;
import agentgui.simulationService.load.threading.ThreadProtocolReceiver;
import agentgui.simulationService.ontology.AgentGUI_DistributionOntology;
import agentgui.simulationService.ontology.AgentGuiVersion;
import agentgui.simulationService.ontology.BenchmarkResult;
import agentgui.simulationService.ontology.ClientAvailableMachinesReply;
import agentgui.simulationService.ontology.ClientAvailableMachinesRequest;
import agentgui.simulationService.ontology.ClientRemoteContainerReply;
import agentgui.simulationService.ontology.ClientRemoteContainerRequest;
import agentgui.simulationService.ontology.OSInfo;
import agentgui.simulationService.ontology.PlatformAddress;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.PlatformPerformance;
import agentgui.simulationService.ontology.RemoteContainerConfig;
import de.enflexit.common.SystemEnvironmentHelper;
import de.enflexit.common.VersionInfo;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.AgentContainer;
import jade.core.BaseService;
import jade.core.ContainerID;
import jade.core.Filter;
import jade.core.HorizontalCommand;
import jade.core.IMTPException;
import jade.core.Location;
import jade.core.MainContainer;
import jade.core.NameClashException;
import jade.core.Node;
import jade.core.NotFoundException;
import jade.core.Profile;
import jade.core.ProfileException;
import jade.core.Service;
import jade.core.ServiceDescriptor;
import jade.core.ServiceException;
import jade.core.ServiceHelper;
import jade.core.VerticalCommand;
import jade.core.management.AgentManagementSlice;
import jade.core.messaging.MessagingSlice;
import jade.core.mobility.AgentMobilityHelper;
import jade.lang.acl.ACLMessage;
import jade.mtp.MTPDescriptor;
import jade.security.JADESecurityException;
import jade.util.Logger;
import jade.util.ObjectManager;
import jade.util.leap.ArrayList;

/**
 * This extended BaseService is basically used to transport the load information 
 * from different remote locations to the Main-Container. Beside the load, measured
 * with HypericSigar in the {@link LoadMeasureThread}, its evaluates also some local
 * information and enables to set the migration of agents.<br> 
 * Additionally it can be used in order to start agents directly on remote container, 
 * which requires to have the agents resources (e.g. a jar file) available there. 
 * 
 * @see LoadMeasureThread
 * @see LoadServiceHelper
 * @see LoadServiceProxy
 * @see LoadServiceSlice 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public class LoadService extends BaseService {

	/** The external NAME of the this Service ('agentgui.simulationService.LoadService'). */
	public static final String NAME = LoadServiceHelper.SERVICE_NAME;
	
	private AgentContainer myContainer;
	private MainContainer myMainContainer;

	private LoadMeasureAgent loadMeasureAgent;
	
	private Filter incFilter;
	private Filter outFilter;
	private ServiceComponent localSlice;
	
	private String myMTP_URL;
	private Boolean simulationServiceActive;
	
	/** The local ClientRemoteContainerReply instance. */
	private ClientRemoteContainerReply myCRCReply; 
	private RemoteContainerConfig defaults4RemoteContainerConfig; 

	/** The List of Agents, which are registered to this service  **/ 
	private HashMap<String, AID> serviceUsingAgents = new HashMap<String, AID>();
	/**  The Load-Information Array of all slices **/
	private LoadInformation loadInfo = new LoadInformation(); 
	
	private static ClientAvailableMachinesReply availableMachines;
	
	
	/* (non-Javadoc)
	 * @see jade.core.BaseService#init(jade.core.AgentContainer, jade.core.Profile)
	 */
	public void init(AgentContainer ac, Profile p) throws ProfileException {
		
		super.init(ac,p);
		myContainer = ac;
		myMainContainer = ac.getMain();		
		// --- Create filters -----------------------------
		outFilter = new CommandOutgoingFilter();
		incFilter = new CommandIncomingFilter();
		// --- Create local slice -------------------------
		localSlice = new ServiceComponent();
		
		if (myContainer!=null) {
			if (myLogger.isLoggable(Logger.FINE)) {
				myLogger.log(Logger.FINE, "Starting LoadService for container: " + myContainer.toString());
			}
		}
		if (myMainContainer!=null) {
			// --- Is !=null, if the Service will start at the Main-Container !!! ----
			if (myLogger.isLoggable(Logger.FINE)) {
				myLogger.log(Logger.FINE, "Main-Container: " + myMainContainer.toString());
			}
		}
		// --- Start the Load-Measurements on this Node ---
		new LoadMeasureThread().start();  
		
	}
	/* (non-Javadoc)
	 * @see jade.core.BaseService#boot(jade.core.Profile)
	 */
	public void boot(Profile p) throws ServiceException {
		super.boot(p);
		this.getLocalClientRemoteContainerReply();
	}
	/* (non-Javadoc)
	 * @see jade.core.BaseService#shutdown()
	 */
	@Override
	public void shutdown() {
		super.shutdown();
	}
	/* (non-Javadoc)
	 * @see jade.core.Service#getName()
	 */
	public String getName() {
		return NAME;
	}
	/* (non-Javadoc)
	 * @see jade.core.BaseService#getHelper(jade.core.Agent)
	 */
	public ServiceHelper getHelper (Agent ag) {
		return new LoadServiceImpl();
	}
	/* (non-Javadoc)
	 * @see jade.core.BaseService#getCommandFilter(boolean)
	 */
	public Filter getCommandFilter(boolean direction) {
		if(direction == Filter.INCOMING) {
			return incFilter;
		} else {
			return outFilter;
		}
	}
	/* (non-Javadoc)
	 * @see jade.core.BaseService#getHorizontalInterface()
	 */
	public Class<?> getHorizontalInterface() {
		return LoadServiceSlice.class;
	}
	/* (non-Javadoc)
	 * @see jade.core.BaseService#getLocalSlice()
	 */
	public Service.Slice getLocalSlice() {
		return localSlice;
	}

	
	// --------------------------------------------------------------	
	// ---- Inner-Class 'AgentTimeImpl' ---- Start ------------------
	// --------------------------------------------------------------
	/**
	 * Sub-Class to provide interaction between Agents and this Service by using the {@link LoadServiceHelper}.
	 *
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
	 */
	public class LoadServiceImpl implements LoadServiceHelper {

		/* (non-Javadoc)
		 * @see jade.core.ServiceHelper#init(jade.core.Agent)
		 */
		public void init(Agent ag) {
			// --- Store the Agent in the agentList -----------------
			serviceUsingAgents.put(ag.getLocalName(), ag.getAID());			
		}
				
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#startAgent(java.lang.String, java.lang.String, java.lang.Object[], java.lang.String)
		 */
		public boolean startAgent(String nickName, String agentClassName, Object[] args, String containerName) throws ServiceException {
			return broadcastStartAgent(nickName, agentClassName, args, containerName);
		}
		
		// ----------------------------------------------------------
		// --- Methods to start a new remote-container -------------- 
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#getDefaultRemoteContainerConfig(boolean)
		 */
		public RemoteContainerConfig getAutoRemoteContainerConfig() throws ServiceException {
			return broadcastGetAutoRemoteContainerConfig();
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#setDefaultRemoteContainerConfig(agentgui.simulationService.ontology.RemoteContainerConfig)
		 */
		public void setDefaults4RemoteContainerConfig(RemoteContainerConfig remoteContainerConfig) throws ServiceException {
			broadcastSetDefaults4RemoteContainerConfig(remoteContainerConfig);
		}

		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#startNewRemoteContainer()
		 */
		public String startNewRemoteContainer() throws ServiceException {
			return this.startNewRemoteContainer(null);
		}
		
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#startNewRemoteContainer(agentgui.simulationService.ontology.RemoteContainerConfig, boolean)
		 */
		public String startNewRemoteContainer(RemoteContainerConfig remoteConfig) throws ServiceException {
			return broadcastStartNewRemoteContainer(remoteConfig);
		}
		
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#startNewRemoteContainerStaus(java.lang.String)
		 */
		public Container2Wait4 startNewRemoteContainerStaus(String containerName) throws ServiceException {
			return broadcastGetNewContainer2Wait4Status(containerName);
		}
		
		// ----------------------------------------------------------
		// --- Methods to set the local description of this node ----
		// --- which is stored in the file 'AgentGUINode.bin'    ----
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#getLocalCRCReply()
		 */
		public ClientRemoteContainerReply getLocalCRCReply() throws ServiceException {
			return getLocalClientRemoteContainerReply();
		}
		
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#setAndSaveCRCReplyLocal(agentgui.simulationService.ontology.ClientRemoteContainerReply)
		 */
		public void setAndSaveCRCReplyLocal(ClientRemoteContainerReply crcReply) throws ServiceException {
			setLocalClientRemoteContainerReply(crcReply);
		}

		// ----------------------------------------------------------
		// --- Methods for container info about OS, benchmark etc. -- 
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#putContainerDescription(agentgui.simulationService.ontology.ClientRemoteContainerReply)
		 */
		public void putContainerDescription(ClientRemoteContainerReply crcReply) throws ServiceException {
			if (crcReply.getRemoteAddress()==null && crcReply.getRemoteOS()==null && crcReply.getRemotePerformance()==null && crcReply.getRemoteBenchmarkResult()==null) {
				// --- RemoteContainerRequest WAS NOT successful ----
				loadInfo.setNewContainerCanceled(crcReply.getRemoteContainerName());
			} else {
				broadcastPutContainerDescription(crcReply);	
			}
		}
		
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#getContainerDescriptions()
		 */
		public Hashtable<String, NodeDescription> getContainerDescriptions() throws ServiceException {
			return loadInfo.getContainerDescriptionHash();
		}
		
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#getContainerDescription(java.lang.String)
		 */
		public NodeDescription getContainerDescription(String containerName) throws ServiceException {
			return loadInfo.getContainerDescriptionHash().get(containerName);
		}
		
		// ----------------------------------------------------------
		// --- Method for getting Location-Objects ------------------ 
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#getContainerLocations()
		 */
		public Hashtable<String, Location> getContainerLocations() throws ServiceException {
			broadcastGetContainerLocation(getAllSlices());
			return loadInfo.getContainerLocationHash();
		}
		
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#getContainerLocation(java.lang.String)
		 */
		public Location getContainerLocation(String containerName) throws ServiceException {
			this.getContainerLocations();
			return loadInfo.getContainerLocationHash().get(containerName);
		}
		
		// ----------------------------------------------------------
		// --- Method to get the Load-Informations of all containers 
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#setThresholdLevels(agentgui.simulationService.load.LoadThresholdLevels)
		 */
		public void setThresholdLevels(LoadThresholdLevels thresholdLevels) throws ServiceException {
			broadcastThresholdLevels(getAllSlices(), thresholdLevels);
		}
		
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#getContainerLoads()
		 */
		public Hashtable<String, PlatformLoad> getContainerLoadHash() throws ServiceException {
			broadcastMeasureLoad(getAllSlices());
			return loadInfo.getContainerLoadHash();
		}
		
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#getContainerLoad(java.lang.String)
		 */
		public PlatformLoad getContainerLoad(String containerName) throws ServiceException {
			broadcastMeasureLoad(getAllSlices());
			return loadInfo.getContainerLoadHash().get(containerName);
		}
		
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#getContainerQueue()
		 */
		public Vector<String> getContainerNames() throws ServiceException {
			return loadInfo.getContainerNames();
		}
		
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#setSimulationCycleStartTimeStamp()
		 */
		public void setSimulationCycleStartTimeStamp() throws ServiceException {
			loadInfo.setSimulationCycleStartTimeStamp();
		}
		
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#getAvgCycleTime()
		 */
		public double getAvgCycleTime() throws ServiceException{
			return loadInfo.getAvgCycleTime();
		}
		// ----------------------------------------------------------
		// --- Method to get positions of Agents at this platform --- 
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#getAgentMap()
		 */
		public LoadAgentMap getLoadAgentMap() throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastGetAIDListSensorAgents(slices);
			broadcastGetAIDList(slices);
			return loadInfo.getLoadAgentMap();
		}
		
		// ----------------------------------------------------------
		// --- Method to set the agent migration --------------------
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#setAgentMigration(java.util.Vector)
		 */
		public void setAgentMigration(Vector<AID_Container> transferAgents) throws ServiceException {
			broadcastAgentMigration(transferAgents, getAllSlices());
		}

		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#requestThreadMeasurements(agentgui.simulationService.agents.LoadMeasureAgent)
		 */
		@Override
		public void requestThreadMeasurements(LoadMeasureAgent loadMeasurementAgent) throws ServiceException {
			loadMeasureAgent = loadMeasurementAgent;
			broadcastThreadMeasurementRequest(getAllSlices(), System.currentTimeMillis());
		}

		
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#requestAvailableMachines()
		 */
		@Override
		public void requestAvailableMachines() throws ServiceException {
			mainRequestAvailableMachines();
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#putAvailableMachines()
		 */
		@Override
		public void putAvailableMachines(ClientAvailableMachinesReply availableMachines) throws ServiceException {
			LoadService.availableMachines = availableMachines;
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.LoadServiceHelper#getAvailableMachines()
		 */
		@Override
		public ClientAvailableMachinesReply getAvailableMachines() throws ServiceException {
			return LoadService.availableMachines;
		}

	}
	// --------------------------------------------------------------	
	// ---- Inner-Class 'AgentTimeImpl' ---- End --------------------
	// --------------------------------------------------------------
	
	/**
	 * Broadcast the new locations to the agents.
	 *
	 * @see AID_Container
	 * @param transferAgents the Vector of agents to transfer 
	 * @param slices the slices
	 * @throws ServiceException the service exception
	 */
	private void broadcastAgentMigration(Vector<AID_Container> transferAgents, Service.Slice[] slices) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending migration notification to agents!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				LoadServiceSlice slice = (LoadServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Sending migration notification to agents at " + sliceName);
				}
				slice.setAgentMigration(transferAgents);
			
			} catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while sending migration notification to agents at slice " + sliceName, t);
			}
		}
	}

	/**
	 * This method starts an agent on an designate (remote) container.
	 *
	 * @param agentName the nick local Name of the agent
	 * @param agentClassName the agent class name
	 * @param args the args
	 * @param containerName the container name
	 * @return true, if successful
	 * @throws ServiceException the service exception
	 */
	private boolean broadcastStartAgent(String agentName, String agentClassName, Object[] args, String containerName) throws ServiceException {
			
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Try to start agent '" + agentName + "' on container '" + containerName + "'!");
		}
		try {
			LoadServiceSlice slice = (LoadServiceSlice) this.getSlice(containerName);
			if (slice==null) {
				myLogger.log(Logger.WARNING, "Could not get access to container '" + containerName + "' for the start of agent '" + agentName + "'!");
				return false;
			}
			// --- Send start agent call to the slice of the container -------- 
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Start agent '" + agentName + "' on container '" + containerName + "'");
			}
			return slice.startAgent(agentName, agentClassName, args);
		
		} catch(Throwable t) {
			// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
			myLogger.log(Logger.WARNING, "Error while trying to start agent in container '" + containerName + "':", t);
		}
		return false;
	}
	
	/**
	 * Broadcast to set the defaults for a remote container configuration.
	 *
	 * @param remoteContainerConfig the remote container configuration
	 * @throws ServiceException the service exception
	 */
	private void broadcastSetDefaults4RemoteContainerConfig(RemoteContainerConfig remoteContainerConfig) throws ServiceException {

		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending the default remote container configuration!");
		}
		String sliceName = null;
		try {
			LoadServiceSlice slice = (LoadServiceSlice) getSlice(MAIN_SLICE);
			sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Sending the default remote container configuration to container (" + sliceName + ")");
			}
			slice.setDefaults4RemoteContainerConfig(remoteContainerConfig);
		
		} catch(Throwable t) {
			// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
			myLogger.log(Logger.WARNING, "Error while try to send the default remote container configuration to container " + sliceName, t);
		}
		
	}
	
	/**
	 * This Methods returns the default Remote-Container-Configuration, coming from the Main-Container.
	 *
	 * @param preventUsageOfAlreadyUsedComputers the prevent usage of already used computers
	 * @return the RemoteContainerConfig
	 * @throws ServiceException the service exception
	 */
	private RemoteContainerConfig broadcastGetAutoRemoteContainerConfig() throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Start request for the default remote container configuration!");
		}
		String sliceName = null;
		try {
			LoadServiceSlice slice = (LoadServiceSlice) getSlice(MAIN_SLICE);
			sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Start request for the default remote container configuration at container (" + sliceName + ")");
			}
			return slice.getAutoRemoteContainerConfig();
		
		} catch(Throwable t) {
			// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
			myLogger.log(Logger.WARNING, "Error while trying to get the default remote container configuration from " + sliceName, t);
		}
		return null;
	}
	
	/**
	 * Broadcast to start a new remote-container for this platform to the Main-Container.
	 *
	 * @param remoteConfig the configuration of the remote container
	 * @param preventUsageOfAlreadyUsedComputers the prevent usage of already used computers
	 * @return the name of the new container
	 * @throws ServiceException the service exception
	 */
	private String broadcastStartNewRemoteContainer(RemoteContainerConfig remoteConfig) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Start a new remote container!");
		}
		String sliceName = null;
		try {
			LoadServiceSlice slice = (LoadServiceSlice) getSlice(MAIN_SLICE);
			sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Try to start a new remote container (" + sliceName + ")");
			}
			return slice.startNewRemoteContainer(remoteConfig);
			
		} catch(Throwable t) {
			// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
			myLogger.log(Logger.WARNING, "Error while starting a new remote-container from " + sliceName, t);
		}
		return null;
	}
	
	/**
	 * Broadcast to start a new remote-container for this platform.
	 *
	 * @param containerName2Wait4 the container name2 wait4
	 * @return the container2 wait4
	 * @throws ServiceException the service exception
	 */
	private Container2Wait4 broadcastGetNewContainer2Wait4Status(String containerName2Wait4) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Start a new remote container!");
		}
		String sliceName = null;
		try {
			LoadServiceSlice slice = (LoadServiceSlice) getSlice(MAIN_SLICE);
			sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Try to start a new remote container (" + sliceName + ")");
			}
			return slice.getNewContainer2Wait4Status(containerName2Wait4);
			
		} catch(Throwable t) {
			// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
			myLogger.log(Logger.WARNING, "Error while starting a new remote-container from " + sliceName, t);
		}
		return null;
	}
	
	/**
	 * Collects all {@link Location} information from the connected container. 
	 * The information will be set to the local {@link #loadInfo}.
	 *
	 * @param slices the slices
	 * @throws ServiceException the service exception
	 */
	private void broadcastGetContainerLocation(Service.Slice[] slices) throws ServiceException {
		
		loadInfo.getContainerLocationHash().clear();
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Try to get Location-Informations!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				LoadServiceSlice slice = (LoadServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try to get Location-Object for " + sliceName );
				}
				Location cLoc = slice.getLocation();
				loadInfo.getContainerLocationHash().put(sliceName, cLoc);
			
			} catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while try to get Location-Object from " + sliceName, t);
			}
		}	
	}
	
	/**
	 * Broadcasts the local container description (instance of ClientRemoteContainerReply).
	 */
	private void broadcastLocalContainerDescription() {
		try {
			this.broadcastPutContainerDescription(this.getLocalClientRemoteContainerReply());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Broadcast informtion's of the remote-container (OS etc.) to all remote-container of this platform.
	 *
	 * @param slices the slices
	 * @param crcReply the ClientRemoteContainerReply
	 * @throws ServiceException the service exception
	 */
	private void broadcastPutContainerDescription(ClientRemoteContainerReply crcReply) throws ServiceException {
		
		Service.Slice[] slices = this.getAllSlices();
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending remote container Information!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				LoadServiceSlice slice = (LoadServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try sending remote container Information to " + sliceName );
				}
				slice.putContainerDescription(crcReply);
			
			} catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while try to send container information to " + sliceName, t);
			}
		}	
	}
	
	/**
	 * Broadcast the set of threshold levels to all container.
	 *
	 * @param slices the slices
	 * @param thresholdLevels the threshold levels
	 * @throws ServiceException the service exception
	 */
	private void broadcastThresholdLevels(Service.Slice[] slices, LoadThresholdLevels thresholdLevels) throws ServiceException {
		
		loadInfo.getContainerLoadHash().clear();
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Try to set threshold level to all Containers !");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				LoadServiceSlice slice = (LoadServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try to set threshold level to " + sliceName);
				}
				slice.setThresholdLevels(thresholdLevels);
			
			} catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while try to set threshold level to slice " + sliceName, t);
			}
		}		
	}
	
	/**
	 * 'Broadcast' (or receive) all Informations about the containers load.
	 * The information will be set to the local {@link #loadInfo}.
	 *
	 * @param slices the slices
	 * @throws ServiceException the service exception
	 */
	private void broadcastMeasureLoad(Service.Slice[] slices) throws ServiceException {
		
		loadInfo.getContainerLoadHash().clear();
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Try to get Load-Information from all Containers !");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				LoadServiceSlice slice = (LoadServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try to get Load-Information of " + sliceName);
				}
				PlatformLoad pl = slice.measureLoad();
				loadInfo.getContainerLoadHash().put(sliceName, pl);
			
			} catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while executing 'MeasureLoad' on slice " + sliceName, t);
			}
		}		
	}
	
	/**
	 * Broadcast the thread measurement request.
	 *
	 * @param slices the slices
	 * @param timeStamp the time stamp for the request
	 * @throws ServiceException the service exception
	 */
	private void broadcastThreadMeasurementRequest(Service.Slice[] slices, long timeStamp) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Try to request thread measurements from all Containers !");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				LoadServiceSlice slice = (LoadServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try to request thread measurement of " + sliceName);
				}
				slice.requestThreadMeasurement(timeStamp);
			
			} catch(Throwable t) {
				myLogger.log(Logger.WARNING, "Error while requesting Thread Measurement on slice " + sliceName, t);
			}
		}		
	}
	
	/**
	 * 'Broadcast' (or receive) the list of all agents in a container.
	 * The information will be set to the local {@link #loadInfo}.
	 *
	 * @param slices the slices
	 * @throws ServiceException the service exception
	 */
	private void broadcastGetAIDList(Service.Slice[] slices) throws ServiceException {
		
		if (this.isActiveSimulationService()==false) return;
		this.loadInfo.resetLoadAgentMap();
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Try to get AID's from all Containers !");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				LoadServiceSlice slice = (LoadServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try to get AID's from " + sliceName);
				}
				AID[] aid = slice.getAIDList();
				loadInfo.putAIDsToLoadAgentMap(sliceName, aid);
			
			} catch(Throwable t) {
				myLogger.log(Logger.WARNING, "Error while trying to get AID's from " + sliceName, t);
			}
		}
		this.loadInfo.doCountingsInLoadAgentMap();
	}
	
	/**
	 * 'Broadcast' (or receive) the list of all agents in a container with a registered sensor.
	 * The information will be set to the local {@link #loadInfo}.
	 *
	 * @param slices the slices
	 * @throws ServiceException the service exception
	 */
	private void broadcastGetAIDListSensorAgents(Service.Slice[] slices) throws ServiceException {
		
		if (this.isActiveSimulationService()==false) return;
		this.loadInfo.getSensorAgents().clear();
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Try to get Sensor-AID's from all Containers !");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				LoadServiceSlice slice = (LoadServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try to get Sensor-AID's from " + sliceName);
				}
				AID[] aidList = slice.getAIDListSensorAgents();
				loadInfo.getSensorAgents().addAll(new Vector<AID>(Arrays.asList(aidList)) );
			
			} catch(Throwable t) {
				myLogger.log(Logger.WARNING, "Error while trying to get Sensor-AID's from " + sliceName, t);
			}
		}
	}
	
	/**
	 * Checks if is active {@link SimulationService}.
	 * @return true, if is active simulation service
	 */
	private boolean isActiveSimulationService() {
		if (simulationServiceActive==null) {
			Vector<?> localServices = myContainer.getServiceManager().getLocalServices();
			for (int i = 0; i < localServices.size(); i++) {
				ServiceDescriptor sd = (ServiceDescriptor) localServices.get(i);
				if (sd.getName().equals(SimulationService.NAME)==true) {
					simulationServiceActive = true;
					return true;
				}
			}
			simulationServiceActive = false;
		}
		return simulationServiceActive;
	}
	;
	/**
	 * Sends the specified {@link ThreadProtocol} to the main container.
	 *
	 * @param threadProtocol the thread protocol
	 * @throws ServiceException the service exception
	 */
	private void sendThreadProtocolToMainContainer(ThreadProtocol threadProtocol) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending thread protocol to the Main-Container!");
		}
		String sliceName = null;
		try {
			LoadServiceSlice slice = (LoadServiceSlice) getSlice(MAIN_SLICE);
			sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Send thread protocol to " + sliceName);
			}
			slice.putThreadProtocol(threadProtocol);
		
		} catch(Throwable t) {
			myLogger.log(Logger.WARNING, "Error while sending the thread protocol to slice  " + sliceName, t);
		}
	}	
	
	
	/**
	 * Sends a request to the server.client to ask for all available machines of the server.master.
	 *
	 * @return the name of the new container
	 * @throws ServiceException the service exception
	 */
	private void mainRequestAvailableMachines() throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Start a request for availble machines!");
		}
		String sliceName = null;
		try {
			LoadServiceSlice slice = (LoadServiceSlice) getSlice(MAIN_SLICE);
			sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Try to request for availble machines at (" + sliceName + ")");
			}
			slice.mainRequestAvailableMachines();
			
		} catch(Throwable t) {
			// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
			myLogger.log(Logger.WARNING, "Error while starting a request for availble machines to " + sliceName, t);
		}
	}
	
	
	// --------------------------------------------------------------	
	// ---- Inner-Class 'ServiceComponent' ---- Start ---------------
	// --------------------------------------------------------------
	/**
	 * Inner class ServiceComponent. Will receive Commands, which 
	 * are coming from the LoadServiceProxy and do local method invocations. 
	 */
	private class ServiceComponent implements Service.Slice, ThreadProtocolReceiver {
		
		private static final long serialVersionUID = 1776886375724997808L;

		/* (non-Javadoc)
		 * @see jade.core.Service.Slice#getService()
		 */
		public Service getService() {
			return LoadService.this;
		}
		
		/* (non-Javadoc)
		 * @see jade.core.Service.Slice#getNode()
		 */
		public Node getNode() throws ServiceException {
			try {
				return LoadService.this.getLocalNode();
			} catch(IMTPException imtpe) {
				throw new ServiceException("Error retrieving local node", imtpe);
			}
		}
		
		/* (non-Javadoc)
		 * @see jade.core.Service.Slice#serve(jade.core.HorizontalCommand)
		 */
		public VerticalCommand serve(HorizontalCommand cmd) {
			
			try {
				if (cmd==null) return null;
				//if ( ! cmd.getService().equals(NAME) ) return null;
				
				String cmdName = cmd.getName();
				Object[] params = cmd.getParams();
				
				//System.out.println( "=> LOAD ServiceComponent " + cmd.getService() + " " +  cmdName);
				if (cmdName.equals(LoadServiceSlice.SERVICE_START_AGENT)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Starting a new agent on this platform");
					}
					String nickName = (String) params[0];
					String agentClassName = (String) params[1];
					Object[] args = (Object[]) params[2];
					cmd.setReturnValue(this.startAgent(nickName, agentClassName, args));
					
				} else if (cmdName.equals(LoadServiceSlice.SERVICE_START_NEW_REMOTE_CONTAINER)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Starting a new remote-container for this platform");
					}
					RemoteContainerConfig remoteConfig = (RemoteContainerConfig) params[0];
					cmd.setReturnValue(this.startRemoteContainer(remoteConfig));
					
				} else if (cmdName.equals(LoadServiceSlice.SERVICE_SET_DEFAULTS_4_REMOTE_CONTAINER_CONFIG)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Got the default settings for ");
					}
					RemoteContainerConfig remoteContainerConfig = (RemoteContainerConfig) params[0];
					defaults4RemoteContainerConfig = remoteContainerConfig;
					
				} else if (cmdName.equals(LoadServiceSlice.SERVICE_GET_AUTO_REMOTE_CONTAINER_CONFIG)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering to request for 'get_default_remote_container_config'");
					}
					cmd.setReturnValue(this.getAutoRemoteContainerConfig());
					
				} else if (cmdName.equals(LoadServiceSlice.SERVICE_GET_NEW_CONTAINER_2_WAIT_4_STATUS)) {
					String container2Wait4 = (String) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering request for new container status of container '" + container2Wait4 + "'");
					}
					cmd.setReturnValue(this.getNewContainer2Wait4Status(container2Wait4));
					
				} else if (cmdName.equals(LoadServiceSlice.SERVICE_GET_LOCATION)) {
					cmd.setReturnValue(myContainer.here());
					
				} else if (cmdName.equals(LoadServiceSlice.SERVICE_SET_THRESHOLD_LEVEL)) {
					LoadThresholdLevels thresholdLevels = (LoadThresholdLevels) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Getting new threshold levels for load");
					}
					this.setThresholdLevels(thresholdLevels);
					
				} else if (cmdName.equals(LoadServiceSlice.SERVICE_MEASURE_LOAD)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering request for Container-Load");
					}
					cmd.setReturnValue(this.measureLoad());
					
				} else if (cmdName.equals(LoadServiceSlice.SERVICE_PUT_CONTAINER_DESCRIPTION)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Putting container description");
					}
					this.putContainerDescription((ClientRemoteContainerReply) params[0]);
					
				} else if (cmdName.equals(LoadServiceSlice.SERVICE_GET_CONTAINER_DESCRIPTION)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering request for container description");
					}
					cmd.setReturnValue(this.getContainerDescription());
					
				} else if (cmdName.equals(SimulationServiceSlice.SIM_STEP_SIMULATION)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received 'Step Simulation'");
					}	
					this.setSimulationCycleStartTimeStamp();
					
				} else if (cmdName.equals(LoadServiceSlice.SERVICE_THREAD_MEASUREMENT_REQUEST)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Starting thread measurement for this platform");
					}
					long timestamp = (Long) params[0];
					this.startThreadMeasurement(timestamp);
					
				} else if (cmdName.equals(LoadServiceSlice.SERVICE_THREAD_MEASUREMENT_PUT)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Got ThreadProtocol for display");
					}
					ThreadProtocol threadProtocol = (ThreadProtocol) params[0];
					this.putThreadProtocolToLoadMeasureAgent(threadProtocol);
					
				}  else if (cmdName.equals(LoadServiceSlice.SERVICE_REQUEST_AVAILABLE_MACHINES)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Starting available machine request");
					}
					this.requestAvailableMachines();
				}
				
			} catch (Throwable t) {
				cmd.setReturnValue(t);
			}
			return null;
		}
		

		// -----------------------------------------------------------------
		// --- The real methods for the Service Component --- Start -------- 
		// -----------------------------------------------------------------
		/**
		 * Start agent.
		 *
		 * @param agentName the agent name
		 * @param agentClassName the agent class name
		 * @param args the args
		 * @return true, if successful
		 */
		private boolean startAgent(String agentName, String agentClassName, Object[] args) {
			
			AID agentID = new AID();
			agentID.setLocalName(agentName);
			
			try {
				Agent agent = (Agent) ObjectManager.load(agentClassName, ObjectManager.AGENT_TYPE);
				if (agent == null) {
					agent = (Agent) ClassLoadServiceUtility.newInstance(agentClassName);
				}
				agent.setArguments(args);
				myContainer.initAgent(agentID, agent, null, null);
				myContainer.powerUpLocalAgent(agentID);
				
			} catch (IMTPException e) {
				e.printStackTrace();
			} catch (NameClashException e) {
				e.printStackTrace();
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (JADESecurityException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return true;
		}
		// ----------------------------------------------------------
		// --- Method to set the agent migration --------------------
		/**
		 * Start remote container.
		 *
		 * @param remoteConfig the remote config
		 * @param preventUsageOfAlreadyUsedComputers the prevent usage of already used computers
		 * @return the container name
		 */
		private String startRemoteContainer(RemoteContainerConfig remoteConfig) {
			return sendMsgRemoteContainerRequest(remoteConfig);
		}
		
		/**
		 * Gets the default remote container configuration.
		 *
		 * @param preventUsageOfAlreadyUsedComputers the prevent usage of already used computers
		 * @return the default remote container configuration
		 */
		private RemoteContainerConfig getAutoRemoteContainerConfig() {
			return getRemoteContainerConfigAuto();
		}
		
		/**
		 * Gets the new container to wait status.
		 * @param container2Wait4 the container2 wait4
		 * @return the new container2 wait4 status
		 */
		private Container2Wait4 getNewContainer2Wait4Status(String container2Wait4) {
			return loadInfo.getNewContainer2Wait4Status(container2Wait4);
		}
		
		/**
		 * Sets the threshold levels.
		 * @param thresholdLevels the new threshold levels
		 */
		private void setThresholdLevels(LoadThresholdLevels thresholdLevels) {
			LoadMeasureThread.setThresholdLevels(thresholdLevels);
		}
		
		/**
		 * Measures the local system load.
		 * @return the platform load
		 */
		private PlatformLoad measureLoad() {
			PlatformLoad pl = new PlatformLoad();
			pl.setLoadCPU(LoadMeasureThread.getLoadCPU());
			pl.setLoadMemorySystem(LoadMeasureThread.getLoadRAM());
			pl.setLoadMemoryJVM(LoadMeasureThread.getLoadMemoryJVM());
			pl.setLoadNoThreads(LoadMeasureThread.getLoadNoThreads());
			pl.setLoadExceeded(LoadMeasureThread.getThresholdLevelExceeded());
			return pl;
		}
		
		/**
		 * Put the container description of the {@link LoadService#loadInfo}.
		 * @param crcReply the ClientRemoteContainerReply
		 */
		private void putContainerDescription(ClientRemoteContainerReply crcReply) {
			loadInfo.putContainerDescription(crcReply);
		}
		
		/**
		 * Returns the local container description.
		 * @return the container description
		 */
		private ClientRemoteContainerReply getContainerDescription() {
			return getLocalClientRemoteContainerReply();
		}
		
		/**
		 * Sets the simulation cycle start time stamp.
		 */
		private void setSimulationCycleStartTimeStamp() {
			loadInfo.setSimulationCycleStartTimeStamp();
		}
		
		// ----------------------------------------------------------
		// --- Method for the thread measurement --------------------
		/**
		 * Start thread measurement.
		 * @param timestamp the time stamp
		 */
		private void startThreadMeasurement(long timestamp) {
			LoadMeasureThread.doThreadMeasurement(timestamp, this);
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.load.threading.ThreadProtocolReceiver#receiveThreadProtocol(agentgui.simulationService.load.threading.ThreadProtocol)
		 */
		@Override
		public void receiveThreadProtocol(ThreadProtocol threadProtocol) {

			ClientRemoteContainerReply crcReply = getLocalClientRemoteContainerReply();
			if (crcReply!=null) {
				
				// --- Add information about container, machine ---------------
				threadProtocol.setContainerName(crcReply.getRemoteContainerName());
				threadProtocol.setProcessID(crcReply.getRemotePID());
				
				// --- jvm@machine, e.g. 73461@dell-blade-2 -------------------
				String[] temp = threadProtocol.getProcessID().split("@");
				String jvmName = temp[0];
				String machineName = temp[1];
				
				threadProtocol.setJVMName(jvmName);
				threadProtocol.setMachineName(machineName);
				
				int noOfCPU = crcReply.getRemotePerformance().getCpu_numberOfLogicalCores();
				double mflops = crcReply.getRemoteBenchmarkResult().getBenchmarkValue();
				threadProtocol.setMflops(mflops * noOfCPU);
				
				// --- Check the container names in current JVM ---------------
				for (int i = 0; i <  loadInfo.getContainerNames().size(); i++) {
					String containerName = loadInfo.getContainerNames().get(i);
					if (threadProtocol.getContainerName().contains(containerName)==false) {
						threadProtocol.setContainerName(threadProtocol.getContainerName() + " / " + containerName);
					}
				}
				
				// --- Do the check if a thread is an agent or not ------------
				Hashtable<String, AID_Container> agentsAtPlatform = loadInfo.getLoadAgentMap().getAgentsAtPlatform();
				for (int i = 0; i < threadProtocol.getThreadDetails().size(); i++) {
					
					ThreadDetail threadDetail = threadProtocol.getThreadDetails().get(i);
					
					AID_Container aidContainer = agentsAtPlatform.get(threadDetail.getThreadName());
					if (aidContainer!=null) {
						// --- Found an agent thread --------------------------
						threadDetail.setIsAgent(true);
						// --- Check the class name ---------------------------
						Object classNameObject = aidContainer.getAID().getAllUserDefinedSlot().get(SimulationService.AID_PROPERTY_CLASSNAME);
						if (classNameObject!=null) {
							threadDetail.setClassName((String) classNameObject);
						}
					}
				}
				
				// --- Send protocol to the main container --------------------
				try {
					sendThreadProtocolToMainContainer(threadProtocol);
				} catch (ServiceException se) {
					se.printStackTrace();
				}
			}	
		}
		/**
		 * Put the specified ThreadProtocol to the {@link LoadMeasureAgent}.
		 * @param threadProtocol the thread protocol
		 */
		public void putThreadProtocolToLoadMeasureAgent(ThreadProtocol threadProtocol) {
			loadMeasureAgent.addThreadProtocol(threadProtocol);
		}
		
		/**
		 * Requests for available machines by forwarding this request to the server.client.
		 */
		public void requestAvailableMachines() {
			sendMsgAvailableMachinesRequest();	
		}
		// -----------------------------------------------------------------
		// --- The real functions for the Service Component --- Stop ------- 
		// -----------------------------------------------------------------
	} 
	// --------------------------------------------------------------	
	// ---- Inner-Class 'ServiceComponent' ---- End -----------------
	// --------------------------------------------------------------
	
	
	// --------------------------------------------------------------	
	// ---- Inner-Class 'CommandOutgoingFilter' ---- Start ----------
	// --------------------------------------------------------------
	/**
	 * Inner class CommandOutgoingFilter.
	 */
	private class CommandOutgoingFilter extends Filter {
		
		/**
		 * Instantiates a new command outgoing filter.
		 */
		public CommandOutgoingFilter() {
			super();
			//setPreferredPosition(2);  // Before the Messaging (encoding) filter and the security related ones
		}
		
		/* (non-Javadoc)
		 * @see jade.core.Filter#accept(jade.core.VerticalCommand)
		 */
		public final boolean accept(VerticalCommand cmd) {
			
			if (cmd==null) return true;

			String cmdName = cmd.getName();
//			System.out.println( "=> out " + cmdName + " - " + cmd.getService() + " - " + cmd.getService().getClass() );	
			
			if (cmdName.equals(MessagingSlice.SET_PLATFORM_ADDRESSES) && myMTP_URL==null ) {
				// --- Handle that the MTP-Address was created ------
				Object[] params = cmd.getParams();
				AID aid = (AID)params[0];
				String[] aidArr = aid.getAddressesArray();
				if (aidArr.length!=0) {
					myMTP_URL = aidArr[0];
					broadcastLocalContainerDescription();;
				}
				// Veto the original SEND_MESSAGE command, if needed
				// return false;
			} else if (cmdName.equals(AgentManagementSlice.KILL_CONTAINER)) {
				Object[] params = cmd.getParams();
				ContainerID id = (ContainerID) params[0];
				String containerName = id.getName();
				loadInfo.getContainerLoadHash().remove(containerName);
				loadInfo.getContainerLocationHash().remove(containerName);
			
			} else if (cmdName.equals(AgentMobilityHelper.INFORM_MOVED)) {
				Object[] params = cmd.getParams();
				@SuppressWarnings("unused")
				AID aid = (AID) params[0];
//				ContainerID id = (ContainerID) params[1];
//				localServiceActuator.setAgentMigrated(aid);
			}
			// Never veto other commands
			return true;
		}
	}
	// --------------------------------------------------------------	
	// ---- Inner-Class 'CommandOutgoingFilter' ---- End ------------
	// --------------------------------------------------------------


	// --------------------------------------------------------------	
	// ---- Inner-Class 'CommandIncomingFilter' ---- Start ----------
	// --------------------------------------------------------------
	/**
	 * Inner class CommandIncomingFilter.
	 */
	private class CommandIncomingFilter extends Filter {
		
		/* (non-Javadoc)
		 * @see jade.core.Filter#accept(jade.core.VerticalCommand)
		 */
		public boolean accept(VerticalCommand cmd) {
			
			if (cmd==null) return true;
			String cmdName = cmd.getName();
			//System.out.println( "=> in " + cmdName + " - " + cmd.getService());
			
			if (myMainContainer != null) {
				if (cmdName.equals(Service.NEW_SLICE)) {
					// --- If the new slice is a LoadServiceSlice, notify it about the current state ---
					handleNewSlice(cmd);
				}
				
			} else {
				if (cmdName.equals(Service.REATTACHED)) {
					// The Main lost all information related to this container --> Notify it again
				}
			}
			// Never veto a Command
			return true;
		}
	} 
	// --------------------------------------------------------------	
	// ---- Inner-Class 'CommandIncomingFilter' ---- End ------------
	// --------------------------------------------------------------

	/**
	 * If the new slice is a LoadServiceSlice notify it about the current state.
	 *
	 * @param cmd the VerticalCommand
	 */
	private void handleNewSlice(VerticalCommand cmd) {
		
		if (cmd.getService().equals(NAME)) {
			// --- We ARE in the Main-Container !!! ----------------------------------------
			Object[] params = cmd.getParams();
			String newSliceName = (String) params[0];
			try {
				// --- Is this the slice, we have waited for? ------------------------------
				loadInfo.setNewContainerStarted(newSliceName);
				// --- Be sure to get the new (fresh) slice --> Bypass the service cache ---
				LoadServiceSlice newSlice = (LoadServiceSlice) getFreshSlice(newSliceName);
				// --- Set the local ThresholdLevels to the new container ------------------
				newSlice.setThresholdLevels(LoadMeasureThread.getThresholdLevels());
				
			} catch (Throwable t) {
				myLogger.log(Logger.WARNING, "Error notifying new slice "+newSliceName+" about current LoadService-State", t);
			}
		}
	}
	
	
	/**
	 * Forwards a request for available machines to the server.cliebnt.
	 */
	private void sendMsgAvailableMachinesRequest() {
		
		// --- Get the local Address of JADE ------------------------
		String myPlatformAddress = myContainer.getPlatformID();
		
		// --- Define the AgentAction -------------------------------
		ClientAvailableMachinesRequest request = new ClientAvailableMachinesRequest();
		
		Action act = new Action();
		act.setActor(myContainer.getAMS());
		act.setAction(request);

		// --- Define receiver of the Message ----------------------- 
		AID agentGUIAgent = new AID("server.client" + "@" + myPlatformAddress, AID.ISGUID );
		//mainPlatformAgent.addAddresses(mainPlatform.getHttp4mtp());
		
		// --- Build Message ----------------------------------------
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setSender(myContainer.getAMS());
		msg.addReceiver(agentGUIAgent);		
		msg.setLanguage(new SLCodec().getName());
		msg.setOntology(AgentGUI_DistributionOntology.getInstance().getName());
		try {
			msg.setContentObject(act);
		} catch (IOException errCont) {
			errCont.printStackTrace();
		}

		// --- Send message -----------------------------------------
		myContainer.postMessageToLocalAgent(msg, agentGUIAgent);
	}
	
	
	/**
	 * This method returns a default configuration for a new remote container.
	 *
	 * @param preventUsageOfAlreadyUsedComputers the prevent usage of already used computers
	 * @return the default RemoteContainerConfig
	 */
	private RemoteContainerConfig getRemoteContainerConfigAuto() {
		
		// --- Variable for the new container name ------------------
		String newContainerPrefix = "remote";
		String newContainerName = null;
		Integer newContainerNo = 0;
		
		// --- Get the local IP-Address -----------------------------
		String myIP = myContainer.getNodeDescriptor().getContainer().getAddress();
		// --- Get the local port of JADE ---------------------------
		String myPort = myContainer.getNodeDescriptor().getContainer().getPort();
	
		// --- Get the List of services started here ----------------
		String myServices = "";
		List<?> services = myContainer.getServiceManager().getLocalServices();
		Iterator<?> it = services.iterator();
		while (it.hasNext()) {
			ServiceDescriptor serviceDesc = (ServiceDescriptor) it.next();
			String service = serviceDesc.getService().getClass().getName() + ";";
			myServices += service;				
		}			
		
		// --- Define the new container name ------------------------
		try {
			Service.Slice[] slices = getAllSlices();
			for (int i = 0; i < slices.length; i++) {
				LoadServiceSlice slice = (LoadServiceSlice) slices[i];
				String sliceName = slice.getNode().getName();
				if (sliceName.startsWith(newContainerPrefix)) {
					String endString = sliceName.replace(newContainerPrefix, "");
					try {
						Integer endNumber = Integer.parseInt(endString);
						if (endNumber>newContainerNo) {
							newContainerNo = endNumber;
						}	
						
					} catch (Exception e) {}
				}
			}	
		} catch (ServiceException errSlices) {
			errSlices.printStackTrace();
		}
		newContainerNo++;
		newContainerName = newContainerPrefix + newContainerNo;
		
		// --- Get machines to be excluded for remote start ---------
		jade.util.leap.List hostExcludeIP = new ArrayList();
		java.util.ArrayList<String> containerNames = new java.util.ArrayList<>(this.loadInfo.getContainerDescriptionHash().keySet());
		for (int i = 0; i < containerNames.size(); i++) {
			String containerName = containerNames.get(i);
			NodeDescription nodeDesc = this.loadInfo.getContainerDescriptionHash().get(containerName);
			hostExcludeIP.add(nodeDesc.getPlAddress().getIp());
		}
		
		// --- Get the AID of the file manager agent ----------------
		AID fileManagerAID = null;
		try {
			AID[] agentAIDs = myMainContainer.agentNames();
			for (int i = 0; i < agentAIDs.length; i++) {
				if (agentAIDs[i].getLocalName().equals(Platform.BackgroundSystemAgentFileManger)==true) {
					fileManagerAID = agentAIDs[i]; 
					break;
				}
			} 
			// --- Add the know MTP addresses -----------------------
			if (fileManagerAID!=null) {
				ContainerID containerID = myMainContainer.getContainerID(fileManagerAID);
				jade.util.leap.List mtps = myMainContainer.containerMTPs(containerID);
				for (int i = 0; i < mtps.size(); i++) {
					MTPDescriptor mtpDescriptors = (MTPDescriptor) mtps.get(i);
					for (int j = 0; j < mtpDescriptors.getAddresses().length; j++) {
						fileManagerAID.addAddresses(mtpDescriptors.getAddresses()[j]);
					}
				}
			}
			
		} catch (NotFoundException nfEx) {
			nfEx.printStackTrace();
		}
		
		
		// --- For the Jade-Logger with love ;-) --------------------
		if (myLogger.isLoggable(Logger.FINE)) {
			myLogger.log(Logger.FINE, "-- Infos to start the remote container ------------");
			myLogger.log(Logger.FINE, "=> Services2Start:   " + myServices);
			myLogger.log(Logger.FINE, "=> NewContainerName: " + newContainerName);
			myLogger.log(Logger.FINE, "=> ThisAddresses:    " + myIP +  " - Port: " + myPort);
			myLogger.log(Logger.FINE, "=> FileManagerAgent: " + fileManagerAID.toString());
		}
		
		// ----------------------------------------------------------
		// --- Define the 'RemoteContainerConfig' - Object ----------
		// ----------------------------------------------------------
		RemoteContainerConfig remConf = new RemoteContainerConfig();
		remConf.setJadeShowGUI(true);
		remConf.setFileManagerAgent(fileManagerAID);
		remConf.setJadeIsRemoteContainer(true);
		remConf.setJadeContainerName(newContainerName);
		remConf.setJadeHost(myIP);
		remConf.setJadePort(myPort);
		remConf.setJadeServices(myServices);
		
		// --- Apply defaults, if set -------------------------------
		if (this.defaults4RemoteContainerConfig!=null) {
			if (defaults4RemoteContainerConfig.getPreventUsageOfUsedComputer()==true && hostExcludeIP.size()>0) {
				remConf.setHostExcludeIP(hostExcludeIP);	
			}
			remConf.setJadeShowGUI(this.defaults4RemoteContainerConfig.getJadeShowGUI());
			remConf.setJvmMemAllocInitial(this.defaults4RemoteContainerConfig.getJvmMemAllocInitial());
			remConf.setJvmMemAllocMaximum(this.defaults4RemoteContainerConfig.getJvmMemAllocMaximum());
		}
		return remConf;
	}
	
	/**
	 * This method configures and send a ACLMessage to start a new remote-Container.
	 *
	 * @param remConf the RemoteContainerConfig
	 * @param preventUsageOfAlreadyUsedComputers the boolean prevent usage of already used computers
	 * @return the name of the container
	 */
	private String sendMsgRemoteContainerRequest(RemoteContainerConfig remConf) {
		
		// --- If the remote-configuration is null configure it now -
		if (remConf==null) {
			remConf = this.getRemoteContainerConfigAuto();
		}
		
		// --- Define the AgentAction -------------------------------
		ClientRemoteContainerRequest req = new ClientRemoteContainerRequest();
		req.setRemoteConfig(remConf);
		
		Action act = new Action();
		act.setActor(myContainer.getAMS());
		act.setAction(req);

		// --- Define receiver of the Message ----------------------- 
		AID agentGUIAgent = new AID(Platform.BackgroundSystemAgentApplication +  "@" + this.myContainer.getPlatformID(), AID.ISGUID);
		
		// --- Build Message ----------------------------------------
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setSender(myContainer.getAMS());
		msg.addReceiver(agentGUIAgent);		
		msg.setLanguage(new SLCodec().getName());
		msg.setOntology(AgentGUI_DistributionOntology.getInstance().getName());
		try {
			msg.setContentObject(act);
		} catch (IOException errCont) {
			errCont.printStackTrace();
		}

		// --- Send message -----------------------------------------
		myContainer.postMessageToLocalAgent(msg, agentGUIAgent);
		
		// --- Remind, that we're waiting for this container --------
		loadInfo.setNewContainer2Wait4(remConf.getJadeContainerName());
		
		// --- Return -----------------------------------------------
		return remConf.getJadeContainerName();
	}
	
	/**
	 * This method defines the local field 'myCRCReply' which is an instance
	 * of 'ClientRemoteContainerReply' and holds the information about
	 * Performance, BenchmarkResult, Network-Addresses of this container-node.
	 *
	 * @return the local client remote container reply
	 */
	private ClientRemoteContainerReply getLocalClientRemoteContainerReply() {
		if (myCRCReply==null) {
			// ----------------------------------------------------------------
			// --- In case of the man container, wait for the MTP-URL ---------
			// ----------------------------------------------------------------
			if (this.myMainContainer!=null && this.myMTP_URL==null) return null;
			
			// ----------------------------------------------------------------
			// --- Build the Descriptions from the running system -------------
			// ----------------------------------------------------------------
			
			// --- Get info about the network connection -----
			InetAddress currAddress = null;
			InetAddress addressLocal = null;
			InetAddress addressLocalAlt = null;
			String hostIP, hostName, port;
			
			try {
				currAddress = InetAddress.getByName(this.myContainer.getID().getAddress());
				addressLocal = InetAddress.getLocalHost();
				addressLocalAlt = InetAddress.getByName("127.0.0.1");
				if (currAddress.equals(addressLocalAlt)) {
					currAddress = addressLocal;
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			hostIP = currAddress.getHostAddress();
			hostName = currAddress.getHostName();
			port = this.myContainer.getID().getPort();
			
			// --- Define Platform-Info -----------------------
			PlatformAddress myPlatform = new PlatformAddress();
			myPlatform.setIp(hostIP);
			myPlatform.setUrl(hostName);
			myPlatform.setPort(Integer.parseInt(port));
			myPlatform.setHttp4mtp(this.myMTP_URL);
			
			// --- Set OS-Informations ------------------------
			OSInfo myOS = new OSInfo();
			myOS.setOs_name(SystemEnvironmentHelper.getOperatingSystem());
			myOS.setOs_version(SystemEnvironmentHelper.getOperatingSystemsVersion());
			myOS.setOs_arch(SystemEnvironmentHelper.getOperatingSystemsArchitecture());
			
			// --- Set the Performance of machine -------------
			LoadMeasureOSHI sys = LoadMeasureThread.getCurrentLoadMeasureOSHI();
			PlatformPerformance myPerformance = new PlatformPerformance();
			myPerformance.setCpu_processorName(sys.getProcessorName());
			myPerformance.setCpu_numberOfLogicalCores(sys.getNumberOfLogicalCPU());
			myPerformance.setCpu_numberOfPhysicalCores(sys.getNumberOfPhysicalCPU());
			myPerformance.setCpu_speedMhz((int) sys.getMhz());
			myPerformance.setMemory_totalMB((int) LoadUnits.bytes2(sys.getTotalMemory(), LoadUnits.CONVERT2_MEGA_BYTE));
			
			// --- Wait until benchmarks end ------------------
			while (Application.isBenchmarkRunning()==true) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			// --- Set the performance (Mflops) of the system -
			BenchmarkResult bench = new BenchmarkResult();
			bench.setBenchmarkValue(LoadMeasureThread.getCompositeBenchmarkValue());
			
			// --- Get the PID of this JVM --------------------
			String jvmPID = LoadMeasureThread.getCurrentLoadMeasureJVM().getJvmPID();
			
			// --- Set Agent.GUI version info -----------------
			VersionInfo versionInfo = Application.getGlobalInfo().getVersionInfo();
			AgentGuiVersion version = new AgentGuiVersion();
			version.setMajorRevision(versionInfo.getVersionMajor());
			version.setMinorRevision(versionInfo.getVersionMinor());
			version.setMicroRevision(versionInfo.getVersionMicro());
			
			// --- Finally define this local description ------
			myCRCReply = new ClientRemoteContainerReply();
			myCRCReply.setRemoteContainerName(myContainer.getID().getName());
			myCRCReply.setRemotePID(jvmPID);
			myCRCReply.setRemoteAddress(myPlatform);
			myCRCReply.setRemoteOS(myOS);
			myCRCReply.setRemotePerformance(myPerformance);
			myCRCReply.setRemoteBenchmarkResult(bench);
			myCRCReply.setRemoteAgentGuiVersion(version);
			
			// --- Broadcast the ClientRemoteContainerReply-Object to all other container ---
			this.broadcastLocalContainerDescription();
			
		}
		return myCRCReply;
	}
	/**
	 * Sets the local client remote container reply.
	 * @param newCRCReply the new local client remote container reply
	 */
	private void setLocalClientRemoteContainerReply(ClientRemoteContainerReply newCRCReply) {
		myCRCReply = newCRCReply;
		// --- Broadcast the ClientRemoteContainerReply-Object to all other container ---
		this.broadcastLocalContainerDescription();

	}
	
}
