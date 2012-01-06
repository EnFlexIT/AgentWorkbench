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
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import agentgui.core.application.Application;
import agentgui.core.network.JadeUrlChecker;
import agentgui.simulationService.LoadService;
import agentgui.simulationService.LoadServiceHelper;
import agentgui.simulationService.distribution.JadeRemoteStart;
import agentgui.simulationService.load.LoadMeasureThread;
import agentgui.simulationService.ontology.AgentGUI_DistributionOntology;
import agentgui.simulationService.ontology.BenchmarkResult;
import agentgui.simulationService.ontology.ClientRemoteContainerReply;
import agentgui.simulationService.ontology.ClientRemoteContainerRequest;
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

/**
 * This agent is part of the <b>Agent.GUI</b> background-system and waits for a 
 * so called {@link ClientRemoteContainerRequest} in order to start a new 
 * container for a remotely located platform.<br>
 * Additionally this agent informs the {@link ServerMasterAgent} about the current
 * state of the machine, where this agent is located. 
 * 
 * @see ServerMasterAgent
 * @see ServerClientAgent
 * 
 * @see JadeRemoteStart
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ServerSlaveAgent extends Agent {

	private static final long serialVersionUID = -3947798460986588734L;
	
	private Ontology ontology = AgentGUI_DistributionOntology.getInstance();
	private Codec codec = new SLCodec();
	
	private PlatformAddress mainPlatform = new PlatformAddress();
	private AID mainPlatformAgent = null;
	private boolean mainPlatformReachable = true;
	
	private ClientRemoteContainerReply myCRCreply = null;
	private PlatformAddress myPlatform = null;
	private PlatformPerformance myPerformance = null;
	private OSInfo myOS = null;

	private PlatformTime myPlatformTime = new PlatformTime();
	private SlaveRegister myRegistration = new SlaveRegister();
	private PlatformLoad myLoad = new PlatformLoad();	
	
	private ParallelBehaviour parBehaiv = null;
	private TriggerBehaiviour trigger = null;
	private SaveNodeDescriptionBehaviour sndBehaiv = null;
	
	private long triggerTime = new Long(1000*1);
	private long triggerTime4Reconnection = new Long(1000*20);

	private int sendNotReachable = 0;
	private int sendNotReachableMax = 3;
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();
		// --- Register Codec and Ontology ----------------
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		LoadServiceHelper simHelper = null;
		try {
			simHelper = (LoadServiceHelper) getHelper(LoadService.NAME);
			// --- get the local systems-informations ---------
			myCRCreply = simHelper.getLocalCRCReply();

			// --- Define Platform-Info -----------------------
			myPlatform = myCRCreply.getRemoteAddress();
			// --- Set the Performance of machine -------------
			myPerformance = myCRCreply.getRemotePerformance();
			// --- Set OS-Informations ------------------------
			myOS = myCRCreply.getRemoteOS();
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		// --- Define Main-Platform-Info ------------------
		JadeUrlChecker myURL = Application.JadePlatform.urlChecker;
		mainPlatform.setIp(myURL.getHostIP());
		mainPlatform.setUrl(myURL.getHostName());
		mainPlatform.setPort(myURL.getPort());
		mainPlatform.setHttp4mtp(myURL.getJADEurl4MTP());
		
		// --- Define Receiver of local Status-Info -------
		mainPlatformAgent = new AID("server.master" + "@" + myURL.getJADEurl(), AID.ISGUID );
		mainPlatformAgent.addAddresses(mainPlatform.getHttp4mtp());
		
		// --- Set myTime ---------------------------------
		myPlatformTime.setTimeStampAsString( Long.toString(System.currentTimeMillis()) ) ;
		
		// --- Send 'Register'-Information ----------------
		myRegistration.setSlaveAddress(myPlatform);
		myRegistration.setSlaveTime(myPlatformTime);
		myRegistration.setSlavePerformance(myPerformance);
		myRegistration.setSlaveOS(myOS);
		this.sendMessage2MainServer(myRegistration);

		// --- Add Main-Behaiviours -----------------------
		parBehaiv = new ParallelBehaviour(this,ParallelBehaviour.WHEN_ALL);
		parBehaiv.addSubBehaviour( new MessageReceiveBehaviour() );
		trigger = new TriggerBehaiviour(this,triggerTime);
		parBehaiv.addSubBehaviour( trigger );
		sndBehaiv = new SaveNodeDescriptionBehaviour(this,500);
		parBehaiv.addSubBehaviour( sndBehaiv );
		
		// --- Add Parallel Behaiviour --------------------
		this.addBehaviour(parBehaiv);
		
	}
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() {
		super.takeDown();
		
		// --- Stop Parallel-Behaiviour -------------------
		this.removeBehaviour(parBehaiv);
		
		// --- Send 'Unregister'-Information --------------
		SlaveUnregister unReg = new SlaveUnregister();
		sendMessage2MainServer(unReg);

				
	}
	
	/**
	 * This method sends a message to the ServerMasterAgent.
	 *
	 * @param agentAction the agent action
	 * @return true, if successful
	 */
	private boolean sendMessage2MainServer(Concept agentAction) {
		
		try {
			// --- Definition einer neuen 'Action' --------
			Action act = new Action();
			act.setActor(getAID());
			act.setAction(agentAction);

			// --- Nachricht zusammenbauen und ... --------
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setSender(getAID());
			msg.addReceiver(mainPlatformAgent);
			msg.setLanguage(codec.getName());
			msg.setOntology(ontology.getName());

			// --- ... versenden --------------------------
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

	/**
	 * The TriggerBehaiviour of the agents, which informs the ServerMasterAgent
	 * about the current system state and load.
	 */
	private class TriggerBehaiviour extends TickerBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

		/**
		 * Instantiates a new trigger behaiviour.
		 *
		 * @param agent the agent
		 * @param period the period
		 */
		public TriggerBehaiviour(Agent agent, long period) {
			super(agent, period);
		}
		/* (non-Javadoc)
		 * @see jade.core.behaviours.TickerBehaviour#onTick()
		 */
		@Override
		protected void onTick() {
			// --- Current Time ---------------------------------
			myPlatformTime.setTimeStampAsString( Long.toString(System.currentTimeMillis()) ) ;
			
			if (mainPlatformReachable==false)  {
				// --------------------------------------------------
				// --- Try to (re)register --------------------------
				// --------------------------------------------------
				
				// --- Refresh registration time --------------------
				myRegistration.setSlaveTime(myPlatformTime);

				// --- Send Message ---------------------------------
				sendMessage2MainServer(myRegistration);

			} else {
				// --------------------------------------------------
				// --- Just send a trigger --------------------------
				// --------------------------------------------------
				SlaveTrigger trig = new SlaveTrigger();

				// --- Current Time ---------------------------------
				trig.setTriggerTime( myPlatformTime );
				
				// --- get current Load-Level -----------------------
				myLoad.setLoadCPU(LoadMeasureThread.getLoadCPU());
				myLoad.setLoadMemorySystem(LoadMeasureThread.getLoadMemorySystem());
				myLoad.setLoadMemoryJVM(LoadMeasureThread.getLoadMemoryJVM());
				myLoad.setLoadNoThreads(LoadMeasureThread.getLoadNoThreads());
				myLoad.setLoadExceeded(LoadMeasureThread.getThresholdLevelExceeded());
				trig.setSlaveLoad(myLoad);
				
				// --- get Current Benchmark-Result -----------------
				BenchmarkResult bmr = new BenchmarkResult(); 
				bmr.setBenchmarkValue(LoadMeasureThread.getCompositeBenchmarkValue());
				trig.setSlaveBenchmarkValue(bmr);
				
				// --- Send Message ---------------------------------
				sendMessage2MainServer(trig);
			}
			
		}
	}

	
	
	// -----------------------------------------------------
	// --- Message-Receive-Behaiviour --- S T A R T --------
	// -----------------------------------------------------
	/**
	 * The Class MessageReceiveBehaviour of this agent.
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
			@SuppressWarnings("unused")
			AID senderAID = null;

			ACLMessage msg = myAgent.receive();			
			if (msg!=null) {
				
				act = null; // --- default ---
				
				if (msg.getPerformative()==ACLMessage.FAILURE) {
					// --- Server.Master not reachable ? ------------
					String msgContent = msg.getContent();
					if (msgContent.contains("server.master")) {
						if (sendNotReachable<sendNotReachableMax) {
							System.out.println( "Server.Master not reachable! Try to reconnect in " + (triggerTime4Reconnection/1000) + " seconds ..." );
							sendNotReachable++;
							if (sendNotReachable>=sendNotReachableMax) {
								System.out.println( "Server.Master not reachable! Stop now to inform that Server.Master is not reachable!" );
							}
						}
						System.out.println( "Server.Master not reachable! Try to reconnect in " + (triggerTime4Reconnection/1000) + " seconds ..." );
						trigger.reset(triggerTime4Reconnection);
						mainPlatformReachable = false;							
					} else {
						System.out.println( "ACLMessage.FAILURE from " + msg.getSender().getName() + ": " + msg.getContent() );	
					}
					
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
					if (agentAction instanceof ClientRemoteContainerRequest) {
						
						ClientRemoteContainerRequest crcr = (ClientRemoteContainerRequest) agentAction;
						startRemoteContainer(crcr.getRemoteConfig());
					
					} else if (agentAction instanceof RegisterReceipt) {
						System.out.println( "Server.Master (re)connected!" );
						sendNotReachable = 0;
						mainPlatformReachable = true;
						trigger.reset(triggerTime);
						
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
	 * Starts a Remote-Container for given RemoteContainerConfig-Instance.
	 *
	 * @param remoteContainerConfig the remote container configuration
	 */
	private void startRemoteContainer(RemoteContainerConfig remoteContainerConfig) {
		
		System.out.println("Starting remote container ... ");
		new JadeRemoteStart(remoteContainerConfig).start();		
		
	}
	
	// -----------------------------------------------------
	// --- Save Node Description Behaviour --- S T A R T ---
	// -----------------------------------------------------
	/**
	 * The SaveNodeDescriptionBehaviour, which waits for the result of the benchmark 
	 * in order to save this value also in the local node description by using the LoadService.
	 */
	private class SaveNodeDescriptionBehaviour extends TickerBehaviour {

		private static final long serialVersionUID = 5704581376150290621L;
		
		/**
		 * Instantiates a new save node description behaviour.
		 *
		 * @param a the a
		 * @param period the period
		 */
		public SaveNodeDescriptionBehaviour(Agent a, long period) {
			super(a, period);
		}
		
		/* (non-Javadoc)
		 * @see jade.core.behaviours.TickerBehaviour#onTick()
		 */
		protected void onTick() {
			
			if (LoadMeasureThread.getCompositeBenchmarkValue()!=0) {
				// --- Put the local NodeDescription into the -----
				// --- SimulationService 					  -----
				try {
					BenchmarkResult bench = new BenchmarkResult();
					bench.setBenchmarkValue(LoadMeasureThread.getCompositeBenchmarkValue());
					myCRCreply.setRemoteBenchmarkResult(bench);
					
					LoadServiceHelper simHelper = (LoadServiceHelper) getHelper(LoadService.NAME);
					simHelper.putContainerDescription(myCRCreply);
					simHelper.setAndSaveCRCReplyLocal(myCRCreply);

				} catch (ServiceException servEx) {
					servEx.printStackTrace();
				}
				this.stop();
			}
		}
		
	}
	// -----------------------------------------------------
	// --- Save Node Description Behaviour --- E N D -------
	// -----------------------------------------------------
	
}
