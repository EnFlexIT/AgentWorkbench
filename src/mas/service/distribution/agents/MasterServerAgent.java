package mas.service.distribution.agents;

import jade.content.Concept;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import mas.service.distribution.ontology.*;

import com.mysql.jdbc.ResultSet;

import application.Application;

import database.DBConnection;

public class MasterServerAgent extends Agent {

	private static final long serialVersionUID = -3947798460986588734L;
	
	private Ontology ontology = AgentGUI_DistributionOntology.getInstance();
	private Codec codec = new SLCodec();
	
	private ParallelBehaviour parBehaiv = null;
	private DBConnection dbConn = Application.DBconnection;

	
	@Override
	protected void setup() {
		super.setup();
		
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		// --- Add Main-Behaviours ------------------------
		parBehaiv = new ParallelBehaviour(this,ParallelBehaviour.WHEN_ALL);
		parBehaiv.addSubBehaviour( new ReceiveBehaviour() );
		parBehaiv.addSubBehaviour( new CleanUpBehaviour(this, 1000*60) );
		// --- Add Parallel Behaviour ---------------------
		this.addBehaviour(parBehaiv);
	}
	
	@Override
	protected void takeDown() {
		super.takeDown();
		
	}

	private boolean sendReply(ACLMessage msg,Concept agentAction) {
		
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
	// --- ClenUp-Behaviour --- S T A R T ------------------
	// -----------------------------------------------------
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
	// --- ClenUp-Behaviour --- E N D E --------------------
	// -----------------------------------------------------

	
	// -----------------------------------------------------
	// --- Message-Receive-Behaviour --- S T A R T ---------
	// -----------------------------------------------------
	private class ReceiveBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

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
					System.out.println( "ACLMessage.FAILURE from " + msg.getSender().getName() + ": " + msg.getContent() );

				} else {
					// --- Ontology-specific Message ----------------
					try {
						act = (Action) getContentManager().extractContent(msg);
					} catch (UngroundedException e) {
						e.printStackTrace();
					} catch (CodecException e) {
						e.printStackTrace();
					} catch (OntologyException e) {
						e.printStackTrace();
					}
				}
				
				if (act!=null) {
					
					agentAction = act.getAction();
					senderAID = act.getActor();
					
					// ------------------------------------------------------------------
					// --- Fallunterscheidung AgentAction --- S T A R T -----------------
					// ------------------------------------------------------------------
					if (agentAction instanceof SlaveRegister) {
						
						SlaveRegister sr = (SlaveRegister) agentAction;						
						PlatformAddress plAdd = sr.getSlaveAddress();
						PlatformTime plTime = sr.getSlaveTime();
						PlatformPerformance plPerf = sr.getSlavePerformance();
						OSInfo os = sr.getSlaveOS();
						
						Long timestamp = Long.parseLong(plTime.getTimeStampAsString() );
						Date plDate = new Date(timestamp);						
						
						dbRegisterPlatform(senderAID, os, plAdd, plPerf, plDate, true);

						// --- Answer with 'RegisterReceipt' ------------------
						RegisterReceipt rr = new RegisterReceipt();
						ACLMessage reply = msg.createReply();
						sendReply(reply, rr);
						
					} else if ( agentAction instanceof ClientRegister ) {
						
						ClientRegister cr = (ClientRegister) agentAction;						
						PlatformAddress plAdd = cr.getClientAddress();
						PlatformTime plTime = cr.getClientTime();
						PlatformPerformance plPerf = cr.getClientPerformance();
						OSInfo os = cr.getClientOS();
						
						Long timestamp = Long.parseLong(plTime.getTimeStampAsString() );
						Date plDate = new Date(timestamp);						
						
						dbRegisterPlatform(senderAID, os, plAdd, plPerf, plDate, false);
						
						// --- Answer with 'RegisterReceipt' ------------------
						RegisterReceipt rr = new RegisterReceipt();
						ACLMessage reply = msg.createReply();
						sendReply(reply, rr);

					} else if ( agentAction instanceof SlaveTrigger ) {
						
						SlaveTrigger st = (SlaveTrigger) agentAction;						
						PlatformTime plTime = st.getTriggerTime();
						PlatformLoad plLoad = st.getSlaveLoad();
						BenchmarkResult bmr = st.getSlaveBenchmarkValue();
						
						Long timestamp = Long.parseLong(plTime.getTimeStampAsString() );
						Date plDate = new Date(timestamp);

						dbTriggerPlatform(senderAID, plDate, plLoad, bmr);
						
					} else if ( agentAction instanceof ClientTrigger ) {
						
						ClientTrigger st = (ClientTrigger) agentAction;						
						PlatformTime plTime = st.getTriggerTime();
						PlatformLoad plLoad = st.getClientLoad();
						BenchmarkResult bmr = st.getClientBenchmarkValue();
						
						Long timestamp = Long.parseLong(plTime.getTimeStampAsString() );
						Date plDate = new Date(timestamp);

						dbTriggerPlatform(senderAID, plDate, plLoad, bmr);
						
					} else if ( agentAction instanceof SlaveUnregister ) {

						dbUnregisterPlatform(senderAID);
						
					} else if ( agentAction instanceof ClientUnregister ) {
						
						dbUnregisterPlatform(senderAID);
						
					} else if ( agentAction instanceof ClientRemoteContainerRequest ) {

						ClientRemoteContainerRequest crcr = (ClientRemoteContainerRequest) agentAction;
						handleContainerRequest(msg, crcr); // --- !!!!! ---
						
					} else {
						// --- Unknown AgentAction ------------
						System.out.println( "----------------------------------------------------" );
						System.out.println( myAgent.getLocalName() + ": Unknown Message-Type!" );
						System.out.println( agentAction.toString() );
					}
					// ------------------------------------------------------------------
					// --- Fallunterscheidung AgentAction --- E N D E -------------------
					// ------------------------------------------------------------------
				}
			}
			else {
				block();
			}			
		}
		
	}
	// -----------------------------------------------------
	// --- Message-Receive-Behaviour --- E N D -------------
	// -----------------------------------------------------

	/**
	 * This method is used for Register Slave-Platforms
	 * in the database - table
	 */
	private void dbRegisterPlatform(AID sender, OSInfo os, PlatformAddress platform, PlatformPerformance performance, Date time, boolean isServer) {
		
		String sqlStmt = "";
		Timestamp sqlDate = new Timestamp(time.getTime());
		int isServerBool = dbConn.dbBool2Integer(isServer);
		
		// --- Does the AID already exists in TB ----------		
		sqlStmt = "SELECT * FROM platforms WHERE contact_agent='" + sender.getName() + "'";
		ResultSet res = dbConn.getSqlResult4ExecuteQuery(sqlStmt);
		if (res!=null) {
			try {
				
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
								"os_name = '" + os.getOs_name() + "'," +
								"os_version = '" + os.getOs_version() + "'," +
								"os_arch = '" + os.getOs_arch() + "'," +
								"cpu_vendor = '" + performance.getCpu_vendor() + "'," +
								"cpu_model = '" + performance.getCpu_model() + "'," +
								"cpu_n = " + performance.getCpu_numberOf() + "," +
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
								"os_name = '" + os.getOs_name() + "'," +
								"os_version = '" + os.getOs_version() + "'," +
								"os_arch = '" + os.getOs_arch() + "'," +
								"cpu_vendor = '" + performance.getCpu_vendor() + "'," +
								"cpu_model = '" + performance.getCpu_model() + "'," +
								"cpu_n = " + performance.getCpu_numberOf() + "," +
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
				dbConn.dbError.setErrNumber( e.getErrorCode() );
				dbConn.dbError.setHead( "DB-Error during registration of a Slave-Servers!" );
				dbConn.dbError.setText( e.getLocalizedMessage() );
				dbConn.dbError.setErr(true);
				dbConn.dbError.show();
			}
		}
		
	}
	
	private void dbTriggerPlatform(AID sender, Date time, PlatformLoad plLoad, BenchmarkResult bmr) {
		
		String sqlStmt = "";
		Timestamp sqlDate = new Timestamp(time.getTime());
	
		// --- Update Dataset ---------------------
		sqlStmt = "UPDATE platforms SET " +
					"last_contact_at = now()," +
					"local_last_contact_at = '" + sqlDate + "'," +
					"benchmark_value = " + bmr.getBenchmarkValue() + "," +
					"currently_available = -1, " +
					"current_load_cpu = " + plLoad.getLoadCPU() + "," +
					"current_load_memory_system = " + plLoad.getLoadMemorySystem() + "," +
					"current_load_memory_jvm = " + plLoad.getLoadMemoryJVM() + "," +
					"current_load_no_threads = " + plLoad.getLoadNoThreads() + "," +
					"current_load_threshold_exceeded = " + plLoad.getLoadExceeded() + " " + 
				   "WHERE contact_agent='" + sender.getName() + "'";
		dbConn.getSqlExecuteUpdate(sqlStmt);
	}
	
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
	
	
	private boolean handleContainerRequest( ACLMessage request, ClientRemoteContainerRequest crcr ) {
		
		boolean exitRequest = false;
		
		String sqlStmt = "";
		String slaveAgent = null;
		String slaveAgentAddress = null;
		AID slavePlatformAgent = null; 
		
		Action act = null;
		ClientRemoteContainerReply replyContent = new ClientRemoteContainerReply();
		
		RemoteContainerConfig remConf = crcr.getRemoteConfig();
		String containerName = remConf.getJadeContainerName();
		
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
		sqlStmt+= "ORDER BY (benchmark_value-(benchmark_value*current_load_cpu/100)) DESC";
		// --------------------------------------------------------------------
		ResultSet res = dbConn.getSqlResult4ExecuteQuery(sqlStmt);
		// --------------------------------------------------------------------
		
		try {
			// --- Do we have a SQL-result ? ----------------------------------
			if ( res.wasNull() )   
				exitRequest = true;
			if ( exitRequest == false && dbConn.getRowCount(res)==0 ) 
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
				plPerf.setCpu_vendor(res.getString("cpu_vendor"));
				plPerf.setCpu_model(res.getString("cpu_model"));
				plPerf.setCpu_numberOf(res.getInt("cpu_n"));
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
		} catch (CodecException e) {
			e.printStackTrace();
			return false;
		} catch (OntologyException e) {
			e.printStackTrace();
			return false;
		}
		
		// --- If the SQL-statemant had no results, exit here -----------------
		if (exitRequest==true) {
			return true;
		}
			
			
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

		// --- Nachricht zusammenbauen und ... --------------------------------
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
