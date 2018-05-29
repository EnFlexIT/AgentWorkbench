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
package agentgui.simulationService.agents;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import agentgui.core.application.Application;
import agentgui.core.database.DBConnection;
import agentgui.core.update.AWBUpdater;
import agentgui.simulationService.ontology.AgentGUI_DistributionOntology;
import agentgui.simulationService.ontology.AgentGuiVersion;
import agentgui.simulationService.ontology.BenchmarkResult;
import agentgui.simulationService.ontology.ClientAvailableMachinesReply;
import agentgui.simulationService.ontology.ClientAvailableMachinesRequest;
import agentgui.simulationService.ontology.ClientRegister;
import agentgui.simulationService.ontology.ClientRemoteContainerReply;
import agentgui.simulationService.ontology.ClientRemoteContainerRequest;
import agentgui.simulationService.ontology.ClientTrigger;
import agentgui.simulationService.ontology.ClientUnregister;
import agentgui.simulationService.ontology.MachineDescription;
import agentgui.simulationService.ontology.MasterUpdateNote;
import agentgui.simulationService.ontology.OSInfo;
import agentgui.simulationService.ontology.PlatformAddress;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.PlatformPerformance;
import agentgui.simulationService.ontology.PlatformTime;
import agentgui.simulationService.ontology.RegisterReceipt;
import agentgui.simulationService.ontology.RemoteContainerConfig;
import agentgui.simulationService.ontology.SlaveRegister;
import agentgui.simulationService.ontology.SlaveTrigger;
import agentgui.simulationService.ontology.SlaveUnregister;
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
import jade.util.leap.ArrayList;

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
	
	private Ontology ontology = AgentGUI_DistributionOntology.getInstance();
	private Codec codec = new SLCodec();
	
	private ParallelBehaviour parBehaiv;
	private DBConnection dbConn = Application.getDatabaseConnection();
	
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
		this.parBehaiv.addSubBehaviour( new CleanUpBehaviour(this, 1000*60) );
		this.parBehaiv.addSubBehaviour( new UpdateBehaviour(this, new Date()) );
		// --- Add Parallel Behaviour ---------------------
		this.addBehaviour(parBehaiv);
	}
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() {
		super.takeDown();
		
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
		private long tickingInterval_ms = 0;
		private Long tickingInterval_s = new Long(0);
		
		public CleanUpBehaviour(Agent a, long period) {
			super(a, period);
			tickingInterval_ms = period;
			tickingInterval_s = tickingInterval_ms / 1000;
			// --- Execute the first 'Tick' right now ------
			this.onTick();
		}

		protected void onTick() {
			
			String sqlStmt = null;
			// --- Delete DB-Entries, older than the interval-length of this behaviour ---
			sqlStmt = "DELETE FROM platforms " +
					  "WHERE  DATE_ADD(last_contact_at, INTERVAL " + tickingInterval_s.toString() + " SECOND) < now()";
			dbConn.getSqlExecuteUpdate(sqlStmt);
			
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
						AgentGuiVersion version = sr.getSlaveVersion();
						
						Long timestamp = Long.parseLong(plTime.getTimeStampAsString() );
						Date plDate = new Date(timestamp);						
						
						// --- Register platform ------------------------------
						dbRegisterPlatform(senderAID, os, plAdd, plPerf, version, plDate, true);
						if (this.isAgentGuiVersionUpToDate(version)==true) {
							// --- Answer with 'RegisterReceipt' --------------							
							RegisterReceipt rr = new RegisterReceipt();
							ACLMessage reply = msg.createReply();
							sendReply(reply, rr);
						} else {
							// --- Prepare for update of client or slave ------
							this.replyUpdateAdvice(msg);
						}
						
					} else if (agentAction instanceof ClientRegister) {
						
						ClientRegister cr = (ClientRegister) agentAction;						
						PlatformAddress plAdd = cr.getClientAddress();
						PlatformTime plTime = cr.getClientTime();
						PlatformPerformance plPerf = cr.getClientPerformance();
						OSInfo os = cr.getClientOS();
						AgentGuiVersion version = cr.getClientVersion();
						
						Long timestamp = Long.parseLong(plTime.getTimeStampAsString() );
						Date plDate = new Date(timestamp);						
						
						// --- Register platform ------------------------------
						dbRegisterPlatform(senderAID, os, plAdd, plPerf, version, plDate, true);
						if (this.isAgentGuiVersionUpToDate(version)==true) {
							// --- Answer with 'RegisterReceipt' --------------							
							RegisterReceipt rr = new RegisterReceipt();
							ACLMessage reply = msg.createReply();
							sendReply(reply, rr);
						} else {
							// --- Prepare for update of client or slave ------
							this.replyUpdateAdvice(msg);
						}

					} else if (agentAction instanceof SlaveTrigger) {
						
						SlaveTrigger st = (SlaveTrigger) agentAction;						
						PlatformTime plTime = st.getTriggerTime();
						PlatformLoad plLoad = st.getSlaveLoad();
						BenchmarkResult bmr = st.getSlaveBenchmarkValue();
						
						Long timestamp = Long.parseLong(plTime.getTimeStampAsString() );
						Date plDate = new Date(timestamp);

						dbTriggerPlatform(senderAID, plDate, plLoad, bmr);
						
					} else if (agentAction instanceof ClientTrigger) {
						
						ClientTrigger st = (ClientTrigger) agentAction;						
						PlatformTime plTime = st.getTriggerTime();
						PlatformLoad plLoad = st.getClientLoad();
						BenchmarkResult bmr = st.getClientBenchmarkValue();
						
						Long timestamp = Long.parseLong(plTime.getTimeStampAsString() );
						Date plDate = new Date(timestamp);

						dbTriggerPlatform(senderAID, plDate, plLoad, bmr);
						
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
		private boolean isAgentGuiVersionUpToDate(AgentGuiVersion foreignVersion) {
			return Application.getGlobalInfo().getVersionInfo().isUpToDate(foreignVersion.getMajorRevision(), foreignVersion.getMinorRevision(), foreignVersion.getMicroRevision());
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
	 * @param platform the PlatformAddress of the platform
	 * @param performance the PlatformPerformance
	 * @param time the time
	 * @param isServer true, if the sender is a server instance of Agent.GUI
	 * 
	 * @see OSInfo
	 * @see PlatformAddress
	 * @see PlatformPerformance
	 */
	private void dbRegisterPlatform(AID sender, OSInfo os, PlatformAddress platform, PlatformPerformance performance, AgentGuiVersion version, Date time, boolean isServer) {
		
		String sqlStmt = "";
		Timestamp sqlDate = new Timestamp(time.getTime());
		int isServerBool = dbConn.dbBool2Integer(isServer);
		
		// --- Does the AID already exists in TB ----------		
		sqlStmt = "SELECT * FROM platforms WHERE contact_agent='" + sender.getName() + "'";
		ResultSet res = null;
		try {
			res = dbConn.getSqlResult4ExecuteQuery(sqlStmt);
			res.last();
			if ( res.getRow()==0 ) {
				// --- Insert new dataset -----------------
				sqlStmt = "INSERT INTO platforms SET " +
							"contact_agent = '" + sender.getName() + "'," +
							"platform_name = '" + platform.getIp() + ":" + platform.getPort() + "/JADE'," + 
							"is_server = " + isServerBool + "," +
							"ip = '" + platform.getIp() + "'," +
							"url = '" + platform.getUrl() + "'," +
							"jade_port = " + platform.getPort() + "," +
							"http4mtp = '" + platform.getHttp4mtp() + "'," +
							"vers_major = " + version.getMajorRevision() + "," +
							"vers_minor = " + version.getMinorRevision() + "," +
							"vers_build = " + version.getMicroRevision() + "," +
							"os_name = '" + os.getOs_name() + "'," +
							"os_version = '" + os.getOs_version() + "'," +
							"os_arch = '" + os.getOs_arch() + "'," +
							"cpu_processorName = '" + performance.getCpu_processorName() + "'," +
							"cpu_nLogical = " + performance.getCpu_numberOfLogicalCores() + "," +
							"cpu_nPhysical = " + performance.getCpu_numberOfPhysicalCores() + "," +
							"cpu_speed_mhz = " + performance.getCpu_speedMhz() + "," +
							"memory_total_mb = " + performance.getMemory_totalMB() + "," +
							"benchmark_value = 0," +
							"online_since = now()," + 
							"last_contact_at = now()," +
							"local_online_since = '" + sqlDate + "'," +
							"local_last_contact_at = '" + sqlDate + "'," +
							"currently_available = "+ dbConn.dbBool2Integer(true) + " ";
				dbConn.getSqlExecuteUpdate(sqlStmt);
				
			} else {
				// --- Update dataset ---------------------
				sqlStmt = "UPDATE platforms SET " +
							"contact_agent = '" + sender.getName() + "'," +
							"platform_name = '" + platform.getIp() + ":" + platform.getPort() + "/JADE'," + 
							"is_server = " + isServerBool + "," +
							"ip = '" + platform.getIp() + "'," +
							"url = '" + platform.getUrl() + "'," +
							"jade_port = " + platform.getPort() + "," +
							"http4mtp = '" + platform.getHttp4mtp() + "'," +
							"vers_major = " + version.getMajorRevision() + "," +
							"vers_minor = " + version.getMinorRevision() + "," +
							"vers_build = " + version.getMicroRevision() + "," +
							"os_name = '" + os.getOs_name() + "'," +
							"os_version = '" + os.getOs_version() + "'," +
							"os_arch = '" + os.getOs_arch() + "'," +
							"cpu_processorName = '" + performance.getCpu_processorName() + "'," +
							"cpu_nLogical = " + performance.getCpu_numberOfPhysicalCores() + "," +
							"cpu_nPhysical = " + performance.getCpu_numberOfLogicalCores() + "," +
							"cpu_speed_mhz = " + performance.getCpu_speedMhz() + "," +
							"memory_total_mb = " + performance.getMemory_totalMB() + "," +
							"benchmark_value = 0," +
							"online_since = now()," + 
							"last_contact_at = now()," +
							"local_online_since = '" + sqlDate + "'," +
							"local_last_contact_at = '" + sqlDate + "'," +
							"currently_available = "+ dbConn.dbBool2Integer(true) + " " +
						   "WHERE contact_agent='" + sender.getName() + "'";
				dbConn.getSqlExecuteUpdate(sqlStmt);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			dbConn.getError().setErrNumber( e.getErrorCode() );
			dbConn.getError().setHead( "DB-Error during registration of a Slave-Server!" );
			dbConn.getError().setText( e.getLocalizedMessage() );
			dbConn.getError().setErr(true);
			dbConn.getError().show();
			
		} finally {
			try {
				if (res!=null) res.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		
	}
	
	/**
	 * This method will process the trigger events of the involved slave- and client-platforms.
	 *
	 * @param sender the AID of the sender
	 * @param time the time
	 * @param platformLoad the PlatformLoad of the platform
	 * @param benchmarkResult the BenchmarkResult of the platform
	 * 
	 * @see PlatformLoad
	 * @see BenchmarkResult
	 */
	private void dbTriggerPlatform(AID sender, Date time, PlatformLoad platformLoad, BenchmarkResult benchmarkResult) {
		
		String sqlStmt = "";
		Timestamp sqlDate = new Timestamp(time.getTime());
	
		// --- Update Dataset ---------------------
		sqlStmt = "UPDATE platforms SET " +
					"last_contact_at = now()," +
					"local_last_contact_at = '" + sqlDate + "'," +
					"benchmark_value = " + benchmarkResult.getBenchmarkValue() + "," +
					"currently_available = -1, " +
					"current_load_cpu = " + platformLoad.getLoadCPU() + "," +
					"current_load_memory_system = " + platformLoad.getLoadMemorySystem() + "," +
					"current_load_memory_jvm = " + platformLoad.getLoadMemoryJVM() + "," +
					"current_load_no_threads = " + platformLoad.getLoadNoThreads() + "," +
					"current_load_threshold_exceeded = " + platformLoad.getLoadExceeded() + " " + 
				   "WHERE contact_agent='" + sender.getName() + "'";
		dbConn.getSqlExecuteUpdate(sqlStmt);
	}
	
	/**
	 * This method will unregister slave- and client-platform.
	 *
	 * @param sender the AID of the sender
	 */
	private void dbUnregisterPlatform(AID sender) {
		
		String sqlStmt = "";
		// --- Update Dataset ---------------------
		sqlStmt = "UPDATE platforms SET " +
					"online_since = null," + 
					"local_online_since = null," +
					"currently_available = "+ dbConn.dbBool2Integer(false) + "," +
					"current_load_cpu = null," +
					"current_load_memory_system = null," +
					"current_load_memory_jvm = null," +
					"current_load_no_threads = null," +
					"current_load_threshold_exceeded = null " + 
				   "WHERE contact_agent='" + sender.getName() + "'";
		dbConn.getSqlExecuteUpdate(sqlStmt);
	}
	
	
	/**
	 * Handles the client remote container request.
	 *
	 * @param request the request ACLMessage
	 * @param crcr the ClientRemoteContainerRequest
	 * @return true, if successful
	 */
	private boolean handleClientRemoteContainerRequest(ACLMessage request, ClientRemoteContainerRequest crcr) {
		
		boolean exitRequest = false;
		
		String sqlStmt = "";
		String slaveAgent = null;
		String slaveAgentAddress = null;
		AID slavePlatformAgent = null; 
		
		Action act = null;
		ClientRemoteContainerReply replyContent = new ClientRemoteContainerReply();
		
		RemoteContainerConfig remConf = crcr.getRemoteConfig();
		String containerName = remConf.getJadeContainerName();
		
		// --- Build exclude part of the sql-statement ------------------------
		String excludeIPsql = "";
		ArrayList excludeIPs = (ArrayList) remConf.getHostExcludeIP();

		@SuppressWarnings("unchecked")
		Iterator<String> excludeIPsIterator = excludeIPs.iterator();
		for (Iterator<String> it = excludeIPsIterator; it.hasNext();) {
			String ip = it.next();
			if (excludeIPsql.equals("")==false) {
				excludeIPsql += ", ";
			}
			excludeIPsql += "'" + ip + "'"; 
		}
		if (excludeIPsql.equals("")==false) {
			excludeIPsql = "AND ip NOT IN (" + excludeIPsql + ") ";
		}
		// --------------------------------------------------------------------
		// --- Select the machine with the highest potential of 		 ------
		// --- Mflops (Millions of floating point operations per second) ------
		// --- in relation to the current processor/CPU-load AND 		 ------
		// --- with the needed memory									 ------
		// --------------------------------------------------------------------
		sqlStmt = "SELECT (benchmark_value-(benchmark_value*current_load_cpu/100)) AS potential, ";
		sqlStmt+= "platforms.* ";
		sqlStmt+= "FROM platforms ";
		sqlStmt+= "WHERE is_server=-1 AND currently_available=-1 AND current_load_threshold_exceeded=0 ";
		sqlStmt+= excludeIPsql;
		sqlStmt+= "ORDER BY (benchmark_value-(benchmark_value*current_load_cpu/100)) DESC";
		
		ResultSet res = null;
		try {
			// ----------------------------------------------------------------
			res = dbConn.getSqlResult4ExecuteQuery(sqlStmt);
			// ----------------------------------------------------------------
		
			// --- Do we have a SQL-result ? ----------------------------------
			if (res.wasNull())   
				exitRequest = true;
			if (exitRequest==false && dbConn.getRowCount(res)==0) 
				exitRequest = true;
			
			if ( exitRequest == true ) {
				// --- No Server.Slave was found => EXIT ----------------------
				System.out.println( this.getLocalName() + ": No Server.Slave was found! - Cancle action of remote-container-request!");
				
				replyContent.setRemoteContainerName(containerName);
				replyContent.setRemoteAddress(null);
				replyContent.setRemoteOS(null);
				replyContent.setRemotePerformance(null);
				replyContent.setRemoteBenchmarkResult(null);
					
			} else {
				// --- Server.Slave was found => request a remote-container ---
				res.next(); 	
				slaveAgent = res.getString("contact_agent");
				slaveAgentAddress = res.getString("http4mtp");
				
				// ------------------------------------------------------------
				// --- Collect all need information for the reply -------------
				// ------------------------------------------------------------
				OSInfo os = new OSInfo();
				os.setOs_name(res.getString("os_name"));
				os.setOs_version(res.getString("os_version"));
				os.setOs_arch(res.getString("os_arch"));
				
				PlatformAddress plAdd = new PlatformAddress();
				plAdd.setIp(res.getString("ip"));
				plAdd.setUrl(res.getString("url"));
				plAdd.setPort(res.getInt("jade_port"));
				plAdd.setHttp4mtp(res.getString("http4mtp"));
				
				PlatformPerformance plPerf = new PlatformPerformance();
				plPerf.setCpu_processorName(res.getString("cpu_processorName"));
				plPerf.setCpu_numberOfLogicalCores(res.getInt("cpu_nLogical"));
				plPerf.setCpu_numberOfPhysicalCores(res.getInt("cpu_nPhysical"));
				plPerf.setCpu_speedMhz(res.getInt("cpu_speed_mhz"));
				plPerf.setMemory_totalMB(res.getInt("memory_total_mb"));
				
				BenchmarkResult bench = new BenchmarkResult();
				bench.setBenchmarkValue(res.getFloat("benchmark_value"));
				
				replyContent.setRemoteContainerName(containerName);
				replyContent.setRemoteAddress(plAdd);
				replyContent.setRemoteOS(os);
				replyContent.setRemotePerformance(plPerf);
				replyContent.setRemoteBenchmarkResult(bench);
				// ------------------------------------------------------------

			}
			res.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (res!=null) res.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		
		// -------------------------------------------------------------------- 
		// --- Answer Request with 'ClientRemoteContainerReply' ---------------
		// --------------------------------------------------------------------		
		act = new Action();
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
		
		// --- If the SQL-statement had no results, exit here -----------------
		if (exitRequest==true) return true;
			
			
		// -------------------------------------------------------------------- 
		// --- Forward request to the chosen Server.Slave ---------------------
		// --------------------------------------------------------------------		

		// --- Set the ReceiverAgent ------------------------------------------
		slavePlatformAgent = new AID(slaveAgent, AID.ISGUID );
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
		
		boolean exitRequest = false;
		
		ClientAvailableMachinesReply replyContent = new ClientAvailableMachinesReply();
		replyContent.setAvailableMachines(new jade.util.leap.ArrayList());
		
		ResultSet res = null;
		try {
			// ----------------------------------------------------------------			
			res = this.dbConn.getSqlResult4ExecuteQuery("SELECT * FROM platforms");
			// --- Do we have a SQL-result ? ----------------------------------
			if (res.wasNull()) exitRequest = true;
			if (exitRequest==false && dbConn.getRowCount(res)==0) exitRequest = true;
			
			if (exitRequest==true ) {
				// --- No Server.Slave was found => EXIT ----------------------
				System.out.println(this.getLocalName() + ": No Server.Slave or Server.Client was found!");
					
			} else {
				// --- Server.Slave was found => request a remote-container ---
				while (res.next()) {
					// --------------------------------------------------------
					// --- Collect single machine info ------------------------
					// --------------------------------------------------------
					BenchmarkResult bench = new BenchmarkResult();
					bench.setBenchmarkValue(res.getFloat("benchmark_value"));
					
					OSInfo os = new OSInfo();
					os.setOs_name(res.getString("os_name"));
					os.setOs_version(res.getString("os_version"));
					os.setOs_arch(res.getString("os_arch"));
					
					AgentGuiVersion aguiVersion = new AgentGuiVersion();
					aguiVersion.setMajorRevision(res.getInt("vers_major"));
					aguiVersion.setMinorRevision(res.getInt("vers_minor"));
					aguiVersion.setMicroRevision(res.getInt("vers_build"));
					
					PlatformPerformance plPerf = new PlatformPerformance();
					plPerf.setCpu_processorName(res.getString("cpu_processorName"));
					plPerf.setCpu_numberOfLogicalCores(res.getInt("cpu_nLogical"));
					plPerf.setCpu_numberOfPhysicalCores(res.getInt("cpu_nPhysical"));
					plPerf.setCpu_speedMhz(res.getInt("cpu_speed_mhz"));
					plPerf.setMemory_totalMB(res.getInt("memory_total_mb"));
					
					PlatformAddress plAdd = new PlatformAddress();
					plAdd.setIp(res.getString("ip"));
					plAdd.setUrl(res.getString("url"));
					plAdd.setPort(res.getInt("jade_port"));
					plAdd.setHttp4mtp(res.getString("http4mtp"));
					
					// --- Create machine description -------------------------
					MachineDescription md = new MachineDescription();
					md.setContactAgent(res.getString("contact_agent"));
					md.setPlatformName(res.getString("platform_name"));
					md.setIsAvailable(res.getBoolean("currently_available"));
					md.setIsThresholdExceeded(res.getBoolean("current_load_threshold_exceeded"));
					md.setBenchmarkResult(bench);
					md.setRemoteOS(os);
					md.setAgentGuiVersion(aguiVersion);
					md.setPerformance(plPerf);
					md.setPlatformAddress(plAdd);
					
					replyContent.getAvailableMachines().add(md);
				}
			}
			res.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (res!=null) res.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
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
