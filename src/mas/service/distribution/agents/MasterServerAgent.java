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

		// --- Add Main-Behaiviours -----------------------
		parBehaiv = new ParallelBehaviour(this,ParallelBehaviour.WHEN_ALL);
		parBehaiv.addSubBehaviour( new ReceiveBehaviour() );
		parBehaiv.addSubBehaviour( new CleanUpBehaviour(this, 1000*60) );
		// --- Add Parallel Behaiviour --------------------
		this.addBehaviour(parBehaiv);
	}
	
	@Override
	protected void takeDown() {
		super.takeDown();
		
	}

	// -----------------------------------------------------
	// --- ClenUp-Behaiviour --- S T A R T -----------------
	// -----------------------------------------------------
	private class CleanUpBehaviour extends TickerBehaviour {

		private static final long serialVersionUID = -2401912961869254054L;
		public CleanUpBehaviour(Agent a, long period) {
			super(a, period);
		}

		protected void onTick() {
			
			String sqlStmt = null;

			// --- Delete DB-Entries, older than 1 hour ---
			sqlStmt = "DELETE FROM platforms " +
					  "WHERE currently_available=0 " +
					  "  AND DATE_ADD(last_contact_at, INTERVAL 1 HOUR) < now()";
			dbConn.getSqlExecuteUpdate(sqlStmt);
			
		}
	}
	// -----------------------------------------------------
	// --- ClenUp-Behaiviour --- E N D E -------------------
	// -----------------------------------------------------

	
	// -----------------------------------------------------
	// --- Message-Receive-Behaiviour --- S T A R T --------
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

					} else if ( agentAction instanceof ClientRegister ) {
						
						ClientRegister cr = (ClientRegister) agentAction;						
						PlatformAddress plAdd = cr.getClientAddress();
						PlatformTime plTime = cr.getClientTime();
						PlatformPerformance plPerf = cr.getClientPerformance();
						OSInfo os = cr.getClientOS();
						
						Long timestamp = Long.parseLong(plTime.getTimeStampAsString() );
						Date plDate = new Date(timestamp);						
						
						dbRegisterPlatform(senderAID, os, plAdd, plPerf, plDate, false);
						
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
						handleContainerRequest(crcr); // --- !!!!! ---						
						
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
	// --- Message-Receive-Behaiviour --- E N D ------------
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
	
	
	private boolean handleContainerRequest( ClientRemoteContainerRequest crcr ) {
		
		String sqlStmt = "";
		String slaveAgent = null;
		String slaveAgentAddress = null;
		AID slavePlatformAgent = null; 
		
		// --------------------------------------------------------------------
		// --- Select the machine with the highest potential of 		-------
		// --- Mflops (Millions of floating point operations per second -------
		// --- in relation to the current processor/CPU-load 			-------
		// --------------------------------------------------------------------
		sqlStmt = "SELECT (benchmark_value-(benchmark_value*current_load_cpu/100)) AS potential, platforms.* ";
		sqlStmt+= "FROM platforms ";
		sqlStmt+= "WHERE is_server=-1 AND currently_available=-1 ";
		sqlStmt+= "ORDER BY (benchmark_value-(benchmark_value*current_load_cpu/100)) DESC";
		// --------------------------------------------------------------------
		
		ResultSet res = dbConn.getSqlResult4ExecuteQuery(sqlStmt);
		try {
			if (res.wasNull()) {
				System.out.println("server.master: No server.slave was found! - Cancle Action!");
				return false;	
			}
			res.next(); 	
			slaveAgent = res.getString("contact_agent");
			slaveAgentAddress = res.getString("http4mtp");
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		// --- Set the ReceiverAgent ----------------------
		slavePlatformAgent = new AID(slaveAgent, AID.ISGUID );
		slavePlatformAgent.addAddresses(slaveAgentAddress);
		System.out.println("Inform server.slave for Remote-Container: "  + slaveAgent + " | " + slaveAgentAddress);
		
		// --- Send a message to its Server.Slave - Agent -
		Action act = new Action();
		act.setActor(this.getAID());
		act.setAction(crcr);

		// --- Nachricht zusammenbauen und ... ------------
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setSender(getAID());
		msg.addReceiver(slavePlatformAgent);
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());

		// --- ... versenden ------------------------------
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
