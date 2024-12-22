package de.enflexit.awb.bgSystem.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.enflexit.awb.bgSystem.db.dataModel.BgSystemPlatform;
import de.enflexit.awb.bgSystem.db.dataModel.PlatformStore;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.update.AWBUpdater;
import de.enflexit.awb.simulation.agents.ServerClientAgent;
import de.enflexit.awb.simulation.ontology.AWB_DistributionOntology;
import de.enflexit.awb.simulation.ontology.BenchmarkResult;
import de.enflexit.awb.simulation.ontology.ClientAvailableMachinesReply;
import de.enflexit.awb.simulation.ontology.ClientAvailableMachinesRequest;
import de.enflexit.awb.simulation.ontology.ClientRegister;
import de.enflexit.awb.simulation.ontology.ClientRemoteContainerReply;
import de.enflexit.awb.simulation.ontology.ClientRemoteContainerRequest;
import de.enflexit.awb.simulation.ontology.ClientTrigger;
import de.enflexit.awb.simulation.ontology.ClientUnregister;
import de.enflexit.awb.simulation.ontology.MachineDescription;
import de.enflexit.awb.simulation.ontology.MasterUpdateNote;
import de.enflexit.awb.simulation.ontology.OSInfo;
import de.enflexit.awb.simulation.ontology.PlatformAddress;
import de.enflexit.awb.simulation.ontology.PlatformLoad;
import de.enflexit.awb.simulation.ontology.PlatformPerformance;
import de.enflexit.awb.simulation.ontology.PlatformTime;
import de.enflexit.awb.simulation.ontology.RegisterReceipt;
import de.enflexit.awb.simulation.ontology.RemoteContainerConfig;
import de.enflexit.awb.simulation.ontology.SlaveRegister;
import de.enflexit.awb.simulation.ontology.SlaveTrigger;
import de.enflexit.awb.simulation.ontology.SlaveUnregister;
import de.enflexit.awb.simulation.ontology.Version;
import jade.content.Concept;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * This agent is part of the <b>Agent.GUI</b> background-system and collects the
 * system information of {@link ServerClientAgent}'s and {@link ServerSlaveAgent}'s
 * to the connected MySQL database.<br>
 * <br>
 * Furthermore the agent answers to {@link ClientRemoteContainerRequest} of<br>
 * {@link ServerClientAgent}'s in that way, that it on one hand side forwards <br>
 * this request to a selected {@link ServerSlaveAgent} - on the other hand it<br>
 * answers the {@link ServerClientAgent} about the available and  selected<br> 
 * machine or node with a {@link ClientRemoteContainerReply}, so that the start<br> 
 * of a remote container can be observed, e. g. by using time outs and so on.<br>     
 * 
 * @see ServerClientAgent
 * @see ServerSlaveAgent
 * @see ClientRemoteContainerRequest
 * @see ClientRemoteContainerReply
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ServerMasterAgent extends Agent {

	private static final long serialVersionUID = -3947798460986588734L;
	
	private static long SERVER_MASTER_CLEAN_UP_INTERVAL = 1000 * 60;
	
	
	private Ontology ontology = AWB_DistributionOntology.getInstance();
	private Codec codec = new SLCodec();
	
	private PlatformStore platformStore;
	
	private ParallelBehaviour parBehaiv;
	
	private HashMap<String, Long> lastRequestedSlaveAgents;
	private long lastRequestTimeOut = 30 * 1000; // 30 seconds
	
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();
		
		System.out.println("Starting Background System-Agent '" + this.getName() + "'");
		
		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(ontology);

		// --- Add Main-Behaviours ------------------------
		this.parBehaiv = new ParallelBehaviour(this,ParallelBehaviour.WHEN_ALL);
		this.parBehaiv.addSubBehaviour( new MessageReceiveBehaviour() );
		this.parBehaiv.addSubBehaviour( new CleanUpBehaviour(this, SERVER_MASTER_CLEAN_UP_INTERVAL) );
		this.parBehaiv.addSubBehaviour( new UpdateBehaviour(this, new Date()) );
		// --- Add Parallel Behaviour ---------------------
		this.addBehaviour(parBehaiv);
	}
	
	/**
	 * Returns the platform store that is used to keep information about available server.slaves and server.clients.
	 * @return the platform store
	 */
	private PlatformStore getPlatformStore() {
		if (platformStore==null) {
			platformStore = new PlatformStore();
		}
		return platformStore;
	}
	
	/**
	 * Sends a reply ACL message with a specified agent action.
	 *
	 * @param msg the ACL message
	 * @param agentAction the agent action
	 * @return true, if successful
	 */
	private boolean sendReply(ACLMessage msg, Concept agentAction) {
		
		// --- Define a new action ------------------------
		Action act = new Action();
		act.setActor(getAID());
		act.setAction(agentAction);
		
		try {
			// --- ... send -------------------------------
			getContentManager().fillContent(msg, act);
			send(msg);			
			return true;
		} catch (CodecException e) {
			e.printStackTrace();
			return false;
		} catch (OntologyException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	// -----------------------------------------------------
	// --- Update-Behaviour --- S T A R T ------------------
	// -----------------------------------------------------
	/**
	 * The Class UpdateBehaviour.
	 */
	private class UpdateBehaviour extends WakerBehaviour {

		private static final long serialVersionUID = 4604859330218354875L;

		/**
		 * Instantiates a new update behaviour.
		 *
		 * @param agent the agent
		 * @param wakeupDate the wake up date
		 */
		public UpdateBehaviour(Agent agent, Date wakeupDate) {
			super(agent, wakeupDate);
		}
		
		/* (non-Javadoc)
		 * @see jade.core.behaviours.WakerBehaviour#onWake()
		 */
		@Override
		protected void onWake() {
			
			// --- Start the Updater --------------------------
			new AWBUpdater().start();
			
			// --- Set the time for the next update check -----
			long updateStartTimeLong = 1000 + Application.getGlobalInfo().getUpdateDateLastChecked() + AWBUpdater.UPDATE_CHECK_PERIOD;
			Date updateStartDate = new Date(updateStartTimeLong); 
			this.reset(updateStartDate);
		}
		
	}
	// -----------------------------------------------------
	// --- Update-Behaviour --- E N D E --------------------
	// -----------------------------------------------------

	

	// -----------------------------------------------------
	// --- CleanUp-Behaviour --- S T A R T -----------------
	// -----------------------------------------------------
	/**
	 * The CleanUpBehaviour searches for old database entries and removes them.
	 */
	private class CleanUpBehaviour extends TickerBehaviour {

		private static final long serialVersionUID = -2401912961869254054L;
		
		public CleanUpBehaviour(Agent a, long period) {
			super(a, period);
			// --- Execute the first 'Tick' right now ------
			this.onTick();
		}
		/* (non-Javadoc)
		 * @see jade.core.behaviours.TickerBehaviour#onTick()
		 */
		@Override
		protected void onTick() {
			ServerMasterAgent.this.getPlatformStore().removeOutDatedPlatforms(System.currentTimeMillis() - SERVER_MASTER_CLEAN_UP_INTERVAL);
		}
	}
	// -----------------------------------------------------
	// --- CleanUp-Behaviour --- E N D E -------------------
	// -----------------------------------------------------

	
	// -----------------------------------------------------
	// --- Message-Receive-Behaviour --- S T A R T ---------
	// -----------------------------------------------------
	/**
	 * The MessageReceiveBehaviour.
	 */
	private class MessageReceiveBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			
			Action act = null;
			Concept agentAction = null; 
			AID senderAID = null;
			
			ACLMessage msg = myAgent.receive();
			if (msg!=null) {
				
				if (msg.getPerformative()==ACLMessage.FAILURE) {
					// --- No Ontology-specific Message -------------
					act = null;
					System.out.println( "ACLMessage.FAILURE from " + msg.getSender().getName() + ": " + msg.getContent());

				} else {
					// --- Ontology-specific Message ----------------
					try {
						act = (Action) getContentManager().extractContent(msg);
					} catch (CodecException | OntologyException ex) {
						System.err.println("Error extracting message from " + msg.getSender().getName() + ": " + msg.getContent());
						ex.printStackTrace();
					}
				}
				
				if (act!=null) {
					
					agentAction = act.getAction();
					senderAID = act.getActor();
					
					// ------------------------------------------------------------------
					// --- Cases AgentAction --- S T A R T ------------------------------
					// ------------------------------------------------------------------
					if (agentAction instanceof SlaveRegister) {
						
						SlaveRegister sr = (SlaveRegister) agentAction;						
						PlatformAddress plAdd = sr.getSlaveAddress();
						PlatformTime plTime = sr.getSlaveTime();
						PlatformPerformance plPerf = sr.getSlavePerformance();
						OSInfo os = sr.getSlaveOS();
						Version version = sr.getSlaveVersion();
						
						Long timestamp = Long.parseLong(plTime.getTimeStampAsString() );
						
						// --- Register platform ------------------------------
						dbRegisterPlatform(senderAID, os, plAdd, plPerf, version, new Date(timestamp), true);
						// --- Answer with 'RegisterReceipt' ------------------				
						RegisterReceipt rr = new RegisterReceipt();
						ACLMessage reply = msg.createReply();
						sendReply(reply, rr);
						if (this.isAgentWorkbenchVersionUpToDate(version)==false) {
							// --- Prepare for update of client or slave ------
							this.replyUpdateAdvice(msg);
						}
						
					} else if (agentAction instanceof ClientRegister) {
						
						ClientRegister cr = (ClientRegister) agentAction;						
						PlatformAddress plAdd = cr.getClientAddress();
						PlatformTime plTime = cr.getClientTime();
						PlatformPerformance plPerf = cr.getClientPerformance();
						OSInfo os = cr.getClientOS();
						Version version = cr.getClientVersion();
						
						Long timestamp = Long.parseLong(plTime.getTimeStampAsString() );
						
						// --- Register platform ------------------------------
						dbRegisterPlatform(senderAID, os, plAdd, plPerf, version, new Date(timestamp), false);
						// --- Answer with 'RegisterReceipt' ------------------					
						RegisterReceipt rr = new RegisterReceipt();
						ACLMessage reply = msg.createReply();
						sendReply(reply, rr);
						if (this.isAgentWorkbenchVersionUpToDate(version)==false) {
							// --- Prepare for update of client or slave ------
							this.replyUpdateAdvice(msg);
						}

					} else if (agentAction instanceof SlaveTrigger) {
						
						SlaveTrigger st = (SlaveTrigger) agentAction;						
						PlatformTime plTime = st.getTriggerTime();
						PlatformLoad plLoad = st.getSlaveLoad();
						BenchmarkResult bmr = st.getSlaveBenchmarkValue();
						
						Long timestamp = Long.parseLong(plTime.getTimeStampAsString() );

						dbTriggerPlatform(senderAID, new Date(timestamp), plLoad, bmr);
						
					} else if (agentAction instanceof ClientTrigger) {
						
						ClientTrigger st = (ClientTrigger) agentAction;						
						PlatformTime plTime = st.getTriggerTime();
						PlatformLoad plLoad = st.getClientLoad();
						BenchmarkResult bmr = st.getClientBenchmarkValue();
						
						Long timestamp = Long.parseLong(plTime.getTimeStampAsString() );

						dbTriggerPlatform(senderAID, new Date(timestamp), plLoad, bmr);
						
					} else if (agentAction instanceof SlaveUnregister) {

						dbUnregisterPlatform(senderAID);
						
					} else if (agentAction instanceof ClientUnregister) {
						
						dbUnregisterPlatform(senderAID);
						
					} else if (agentAction instanceof ClientRemoteContainerRequest) {

						ClientRemoteContainerRequest crcr = (ClientRemoteContainerRequest) agentAction;
						handleClientRemoteContainerRequest(msg, crcr); // --- !!!!! ---

					} else if (agentAction instanceof ClientAvailableMachinesRequest) {
						handleAvailableMachineRequest(msg);
						
					} else {
						// --- Unknown AgentAction ------------
						System.out.println( "----------------------------------------------------" );
						System.out.println( myAgent.getLocalName() + ": Unknown Message-Type!" );
						System.out.println( agentAction.toString() );
					}
					// ------------------------------------------------------------------
					// --- Cases AgentAction --- E N D E --------------------------------
					// ------------------------------------------------------------------
				}
			}
			else {
				block();
			}			
		}
		
		/**
		 * Checks if a foreign version is up to date.
		 *
		 * @param foreignVersion the foreign version
		 * @return true, if is up to date
		 */
		private boolean isAgentWorkbenchVersionUpToDate(Version foreignVersion) {
			return Application.getGlobalInfo().getVersionInfo().isUpToDate(foreignVersion.getMajorRevision(), foreignVersion.getMinorRevision(), foreignVersion.getMicroRevision(), foreignVersion.getQualifier());
		}
		
		/**
		 * Sets and prepares the updates for the client or slave.
		 * @param msg 
		 */
		private void replyUpdateAdvice(ACLMessage msg) {
			// --- Notify that an update is needed ----------------------------
			MasterUpdateNote mun = new MasterUpdateNote();
			ACLMessage reply = msg.createReply();
			sendReply(reply, mun);
		}
		
	}
	// -----------------------------------------------------
	// --- Message-Receive-Behaviour --- E N D -------------
	// -----------------------------------------------------

	
	/**
	 * This method is used for the registration of slave- and client-platforms
	 * in the database table 'platforms' of the used database.
	 *
	 * @param sender the AID of the sender
	 * @param os the information about the operating system
	 * @param platformAddress the PlatformAddress of the platform
	 * @param performance the PlatformPerformance
	 * @param slaveOrClientTime the time
	 * @param isServer true, if the sender is a server instance (not an AWB-client)
	 * 
	 * @see OSInfo
	 * @see PlatformAddress
	 * @see PlatformPerformance
	 */
	private void dbRegisterPlatform(AID sender, OSInfo os, PlatformAddress platformAddress, PlatformPerformance performance, Version version, Date slaveOrClientTime, boolean isServer) {
		
		// --- Try getting the platform information -----------------
		BgSystemPlatform platform = ServerMasterAgent.this.getPlatformStore().getPlatform(sender.getName());
		if (platform==null) {
			platform = new BgSystemPlatform();
		}
		
		// --- Set the specific information -------------------------
		platform.setContactAgent(sender.getName());
		
		platform.setPlatformName(platformAddress.getIp() + ":" + platformAddress.getPort() + "/JADE',");
		platform.setServer(isServer);
		platform.setIpAddress(platformAddress.getIp());
		platform.setUrl(platformAddress.getUrl());
		platform.setJadePort(platformAddress.getPort());
		platform.setHttp4mtp(platformAddress.getHttp4mtp());
		
		platform.setVersionMajor(version.getMajorRevision());
		platform.setVersionMinor(version.getMinorRevision());
		platform.setVersionMicro(version.getMicroRevision());
		platform.setVersionBuild(version.getQualifier());
		
		platform.setOsName(os.getOs_name());
		platform.setOsVersion(os.getOs_version());
		platform.setOsArchitecture(os.getOs_arch());
		
		platform.setCpuProcessorName(performance.getCpu_processorName());
		platform.setCpuNoOfLogical(performance.getCpu_numberOfLogicalCores());
		platform.setCpuNoOfPhysical(performance.getCpu_numberOfPhysicalCores());
		platform.setCpuSpeedMHz(performance.getCpu_speedMhz());
		platform.setMemoryMB(performance.getMemory_totalMB());
		
		platform.setBenchmarkValue(0);
		
		platform.getTimeOnlineSince();
		platform.getTimeLastContact();
		platform.getLocalTimeOnlineSince().setTime(slaveOrClientTime);
		platform.getLocalTimeLastContact().setTime(slaveOrClientTime);
		
		platform.setCurrentlyAvailable(true);
		
		// --- Add or update the local PlatformStore ----------------
		ServerMasterAgent.this.getPlatformStore().addOrUpdatePlatform(platform);
		
	}
	
	/**
	 * This method will process the trigger events of the involved slave- and client-platforms.
	 *
	 * @param sender the AID of the sender
	 * @param slaveOrClientTime the time
	 * @param platformLoad the PlatformLoad of the platform
	 * @param benchmarkResult the BenchmarkResult of the platform
	 * 
	 * @see PlatformLoad
	 * @see BenchmarkResult
	 */
	private void dbTriggerPlatform(AID sender, Date slaveOrClientTime, PlatformLoad platformLoad, BenchmarkResult benchmarkResult) {
		
		// --- Try getting the platform information -----------------
		BgSystemPlatform platform = ServerMasterAgent.this.getPlatformStore().getPlatform(sender.getName());
		if (platform!=null) {
			
			platform.getTimeLastContact().setTimeInMillis(System.currentTimeMillis());
			platform.getLocalTimeLastContact().setTime(slaveOrClientTime);
			
			platform.setBenchmarkValue(benchmarkResult.getBenchmarkValue());
			
			platform.setCurrentlyAvailable(true);
			platform.setCurrentLoadCPU(platformLoad.getLoadCPU());
			platform.setCurrentLoadMemory(platformLoad.getLoadMemorySystem());
			platform.setCurrentLoadMemoryJVM(platformLoad.getLoadMemoryJVM());
			platform.setCurrentLoadNoOfThreads(platformLoad.getLoadNoThreads());
			platform.setCurrentLoadThresholdExceeded(platformLoad.getLoadExceeded()==0 ? false : true);
			ServerMasterAgent.this.getPlatformStore().addOrUpdatePlatform(platform);
		}
	}
	
	/**
	 * This method will unregister slave- and client-platform.
	 *
	 * @param sender the AID of the sender
	 */
	private void dbUnregisterPlatform(AID sender) {
		
		BgSystemPlatform platform = ServerMasterAgent.this.getPlatformStore().getPlatform(sender.getName());
		if (platform!=null) {
			ServerMasterAgent.this.getPlatformStore().removePlatform(platform);
		}
	}
	
	
	/**
	 * Returns the last requested slave agents.
	 * @return the last requested slave agents
	 */
	private HashMap<String, Long> getLastRequestedSlaveAgents() {
		if (lastRequestedSlaveAgents==null) {
			lastRequestedSlaveAgents = new HashMap<>();
		}
		return lastRequestedSlaveAgents;
	}
	/**
	 * Checks if is slave agent request is timed out.
	 *
	 * @param contactAgent the contact agent
	 * @return true, if is slave agent request timed out
	 */
	private boolean isSlaveAgentRequestTimedOut(String contactAgent) {
		
		Long timeOut = this.getLastRequestedSlaveAgents().get(contactAgent);
		if (timeOut!=null) {
			if (System.currentTimeMillis() < timeOut) {
				return false;
			}
			this.getLastRequestedSlaveAgents().remove(contactAgent);
		}
		return true;
	}
	
	/**
	 * Handles the client remote container request.
	 *
	 * @param request the request ACLMessage
	 * @param crcr the ClientRemoteContainerRequest
	 * @return true, if successful
	 */
	private boolean handleClientRemoteContainerRequest(ACLMessage request, ClientRemoteContainerRequest crcr) {
		
		RemoteContainerConfig remConf = crcr.getRemoteConfig();
		String newJadeContainerName = remConf.getJadeContainerName();
		
		// --- Filter for usable platform systems -----------------------------
		List<BgSystemPlatform> filteredPlatformList = new ArrayList<>();
		for (BgSystemPlatform platform : this.getPlatformStore().getPlatformList()) {

			boolean isServer    = platform.isServer(); 
			boolean isAvailable = platform.isCurrentlyAvailable();
			boolean isLoadOk	= platform.isCurrentLoadThresholdExceeded()==false;
			boolean isAllowedIP	= remConf.getHostExcludeIP().contains(platform.getIpAddress())==false;
			boolean isTimedOut  = this.isSlaveAgentRequestTimedOut(platform.getContactAgent());
			
			boolean isUsable 	= isServer && isAvailable && isLoadOk && isAllowedIP && isTimedOut;
			if (isUsable==true) {
				filteredPlatformList.add(platform);
			}
		}
		
		// --- Sort by ResidualCalculationCapability --------------------------
		Collections.sort(filteredPlatformList, new Comparator<BgSystemPlatform>() {
			@Override
			public int compare(BgSystemPlatform platform1, BgSystemPlatform platform2) {
				Double resMflops1 = platform1.getResidualCalculationCapability();
				Double resMflops2 = platform2.getResidualCalculationCapability();
				return resMflops1.compareTo(resMflops2);
			}
		});

		
		// --- Prepare corresponding messages ---------------------------------
		String slaveAgent = null;
		String slaveAgentAddress = null;
		ClientRemoteContainerReply replyContent = new ClientRemoteContainerReply();
		if (filteredPlatformList.size()==0) {
			// --- No Server.Slave was found => EXIT --------------------------
			System.out.println( this.getLocalName() + ": No Server.Slave was found! - Cancle action of remote-container-request!");
			
			replyContent.setRemoteContainerName(newJadeContainerName);
			replyContent.setRemoteAddress(null);
			replyContent.setRemoteOS(null);
			replyContent.setRemotePerformance(null);
			replyContent.setRemoteBenchmarkResult(null);
				
		} else {
			// --- Server.Slave was found => request a remote-container -------
			BgSystemPlatform platform = filteredPlatformList.get(filteredPlatformList.size()-1);
			
			slaveAgent = platform.getContactAgent();
			slaveAgentAddress = platform.getHttp4mtp();
			
			// ----------------------------------------------------------------
			// --- Collect all need information for the reply -----------------
			// ----------------------------------------------------------------
			OSInfo os = new OSInfo();
			os.setOs_name(platform.getOsName());
			os.setOs_version(platform.getOsVersion());
			os.setOs_arch(platform.getOsArchitecture());
			
			PlatformAddress plAdd = new PlatformAddress();
			plAdd.setIp(platform.getIpAddress());
			plAdd.setUrl(platform.getUrl());
			plAdd.setPort(platform.getJadePort());
			plAdd.setHttp4mtp(platform.getHttp4mtp());
			
			PlatformPerformance plPerf = new PlatformPerformance();
			plPerf.setCpu_processorName(platform.getCpuProcessorName());
			plPerf.setCpu_numberOfLogicalCores(platform.getCpuNoOfLogical());
			plPerf.setCpu_numberOfPhysicalCores(platform.getCpuNoOfPhysical());
			plPerf.setCpu_speedMhz(platform.getCpuSpeedMHz());
			plPerf.setMemory_totalMB(platform.getMemoryMB());
			
			BenchmarkResult bench = new BenchmarkResult();
			bench.setBenchmarkValue((float) platform.getBenchmarkValue());
			
			replyContent.setRemoteContainerName(newJadeContainerName);
			replyContent.setRemoteAddress(plAdd);
			replyContent.setRemoteOS(os);
			replyContent.setRemotePerformance(plPerf);
			replyContent.setRemoteBenchmarkResult(bench);
			// ------------------------------------------------------------
			
			// --- Remind as last requested -------------------------------
			this.getLastRequestedSlaveAgents().put(slaveAgent, System.currentTimeMillis() + this.lastRequestTimeOut);

		}
		
		
		// -------------------------------------------------------------------- 
		// --- Answer Request with 'ClientRemoteContainerReply' ---------------
		// --------------------------------------------------------------------		
		Action act = new Action();
		act.setActor(this.getAID());
		act.setAction(replyContent);
		
		ACLMessage reply = request.createReply();
		try {
			this.getContentManager().fillContent(reply, act);
			this.send(reply);
			
		} catch (CodecException | OntologyException ex) {
			ex.printStackTrace();
			return false;
		}
		
		// --- If no Server.Slave was found, exit here ------------------------
		if (filteredPlatformList.size()==0) return true;
			
			
		// -------------------------------------------------------------------- 
		// --- Forward request to the chosen Server.Slave ---------------------
		// --------------------------------------------------------------------		
		
		// --- Set the ReceiverAgent ------------------------------------------
		AID slavePlatformAgent = new AID(slaveAgent, AID.ISGUID );
		slavePlatformAgent.addAddresses(slaveAgentAddress);
		
		// --- Define Action --------------------------------------------------
		act = new Action();
		act.setActor(this.getAID());
		act.setAction(crcr);

		// --- Create Message and ... -----------------------------------------
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setSender(getAID());
		msg.addReceiver(slavePlatformAgent);
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());

		// --- send ... -------------------------------------------------------
		System.out.println("Inform server.slave for Remote-Container: "  + slaveAgent + " | " + slaveAgentAddress);
		try {
			this.getContentManager().fillContent(msg, act);
			this.send(msg);
			
		} catch (CodecException | OntologyException ex) {
			ex.printStackTrace();
			return false;
		}		
		return true;		
	}
	
	
	/**
	 * Handles the request for available machines.
	 *
	 * @param request the request ACLMessage
	 * @return true, if successful
	 */
	private boolean handleAvailableMachineRequest(ACLMessage request) {
		
		ClientAvailableMachinesReply replyContent = new ClientAvailableMachinesReply();
		replyContent.setAvailableMachines(new jade.util.leap.ArrayList());
		
		List<BgSystemPlatform> platformList = this.getPlatformStore().getPlatformList();
		if (platformList.size()==0) {
			System.out.println(this.getLocalName() + ": No Server.Slave or Server.Client was found!");
			
		} else {
			// --- Get all registered platform systems ----------------------------
			for (BgSystemPlatform platform : platformList) {
				
				// --------------------------------------------------------
				// --- Collect single machine info ------------------------
				// --------------------------------------------------------
				BenchmarkResult bench = new BenchmarkResult();
				bench.setBenchmarkValue((float) platform.getBenchmarkValue());
				
				OSInfo os = new OSInfo();
				os.setOs_name(platform.getOsName());
				os.setOs_version(platform.getOsVersion());
				os.setOs_arch(platform.getOsArchitecture());
				
				Version aguiVersion = new Version();
				aguiVersion.setMajorRevision(platform.getVersionMajor());
				aguiVersion.setMinorRevision(platform.getVersionMinor());
				aguiVersion.setMicroRevision(platform.getVersionMicro());
				
				PlatformPerformance plPerf = new PlatformPerformance();
				plPerf.setCpu_processorName(platform.getCpuProcessorName());
				plPerf.setCpu_numberOfLogicalCores(platform.getCpuNoOfLogical());
				plPerf.setCpu_numberOfPhysicalCores(platform.getCpuNoOfPhysical());
				plPerf.setCpu_speedMhz(platform.getCpuSpeedMHz());
				plPerf.setMemory_totalMB(platform.getMemoryMB());
				
				PlatformAddress plAdd = new PlatformAddress();
				plAdd.setIp(platform.getIpAddress());
				plAdd.setUrl(platform.getUrl());
				plAdd.setPort(platform.getJadePort());
				plAdd.setHttp4mtp(platform.getHttp4mtp());
				
				// --- Create machine description -------------------------
				MachineDescription md = new MachineDescription();
				md.setContactAgent(platform.getContactAgent());
				md.setPlatformName(platform.getPlatformName());
				md.setIsAvailable(platform.isCurrentlyAvailable());
				md.setIsThresholdExceeded(platform.isCurrentLoadThresholdExceeded());
				md.setBenchmarkResult(bench);
				md.setRemoteOS(os);
				md.setVersion(aguiVersion);
				md.setPerformance(plPerf);
				md.setPlatformAddress(plAdd);
				
				replyContent.getAvailableMachines().add(md);
			}
		}
		
		// -------------------------------------------------------------------- 
		// --- Answer Request with 'ClientAvailableMachinesReply' -------------
		// --------------------------------------------------------------------		
		Action act = new Action();
		act.setActor(this.getAID());
		act.setAction(replyContent);
		
		ACLMessage reply = request.createReply();
		try {
			this.getContentManager().fillContent(reply, act);
			this.send(reply);
		} catch (CodecException e) {
			e.printStackTrace();
			return false;
		} catch (OntologyException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
