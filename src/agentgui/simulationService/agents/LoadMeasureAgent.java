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

import java.awt.Color;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import agentgui.core.application.Application;
import agentgui.core.project.DistributionSetup;
import agentgui.core.project.Project;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.simulationService.LoadService;
import agentgui.simulationService.LoadServiceHelper;
import agentgui.simulationService.balancing.DynamicLoadBalancing;
import agentgui.simulationService.balancing.DynamicLoadBalancingBase;
import agentgui.simulationService.load.LoadAgentMap;
import agentgui.simulationService.load.LoadInformation.NodeDescription;
import agentgui.simulationService.load.LoadMeasureThread;
import agentgui.simulationService.load.LoadMerger;
import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.load.gui.SystemLoadDialog;
import agentgui.simulationService.load.gui.SystemLoadPanel;
import agentgui.simulationService.load.gui.SystemLoadSingle;
import agentgui.simulationService.load.threading.ThreadMeasureBehaviour;
import agentgui.simulationService.load.threading.ThreadProtocol;
import agentgui.simulationService.load.threading.ThreadProtocolVector;
import agentgui.simulationService.load.threading.gui.ThreadMonitor;
import agentgui.simulationService.load.threading.storage.ThreadInfoStorage;
import agentgui.simulationService.ontology.OSInfo;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.PlatformPerformance;
import agentgui.simulationService.ontology.ShowMonitorGUI;
import agentgui.simulationService.ontology.ShowThreadGUI;
import jade.content.Concept;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.Agent;
import jade.core.Location;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.Search;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.ACLMessage;

/**
 * This class represents the agent, which monitors the load information 
 * of all involved JVM's, container and agents of the platform.<br>   
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LoadMeasureAgent extends Agent {

	private static final long serialVersionUID = 3035508112883482740L;
	
	// --------------------------------------------------------------
	// --- To which Project are we running in the moment ------------ 
	/** The current project. */
	private Project currProject;
	/** The current SimulationSetup. */
	private SimulationSetup currSimSetup;
	/** The current DistributionSetup. */
	private DistributionSetup currDisSetup;
	/** The Load Helper for this agent */
	private LoadServiceHelper loadHelper;
	
	// --------------------------------------------------------------
	// --- Display-elements of the Monitoring system ---------------- 
	private SystemLoadDialog loadDialog;
	// --- Remember container Informations/Instances (Display) ------
	private Hashtable<String, SystemLoadSingle> containerLoadDialogs = new Hashtable<String, SystemLoadSingle>(); 
	// --------------------------------------------------------------

	// --------------------------------------------------------------
	// --- The measure/display behaviour of the agent --------------- 
	/** The monitor behaviour, which is a TickerBehaviour. */
	private MonitorBehaviour monitorBehaviour;
	private long monitorBehaviourTickingPeriod = 500L;
	// --------------------------------------------------------------

	// --------------------------------------------------------------
	// --- Display-elements of the Threading ------------------------ 
	private ThreadMonitor threadDialog;
	private long threadMeasurementTickingPeriod = 5000L;
	// --------------------------------------------------------------
	
	// --------------------------------------------------------------
	// --- The balancing algorithm of this agent --------------------
	private DynamicLoadBalancingBase loadBalancing;
	/** Indicator if a activate DynamicLoadBalancing is still active. */
	private boolean dynLoadBalancaingStillActivated = false;
	// --------------------------------------------------------------
	
	// --------------------------------------------------------------
	// --- Over all and current threshold level ---------------------
	/** The indicator if thresholds exceeded over all. */
	public Integer loadThresholdExceededOverAll = 0;
	/** The currently configured threshold levels. */
	public LoadThresholdLevels loadThresholdLevels = null;
	/** The average cycle time of a simulation. */
	public double loadCycleTime = 0;
	/** The used (dead or alive) nodes of the system, ordered ascending. */
	public Vector<String> loadContainer2Display = null;
	/** The current LoadAgentMap. */
	public LoadAgentMap loadContainerAgentMap = null;
	/** The PlatformLoad in the different container. */
	public Hashtable<String, PlatformLoad> loadContainer = null;
	/** The Location information in the different container. */
	public Hashtable<String, Location> loadContainerLoactions = null;
	/** The benchmark value /results in the different container. */
	public Hashtable<String, Float> loadContainerBenchmarkResults = new Hashtable<String, Float>();
	
	/** The currently running JVM's for this platform. */
	public Hashtable<String, LoadMerger> loadJVM4Balancing = new Hashtable<String, LoadMerger>();
	/** The currently running physical machines for this platform. */
	public Hashtable<String, LoadMerger> loadMachines4Balancing = new Hashtable<String, LoadMerger>();
	// --------------------------------------------------------------

	// --------------------------------------------------------------
	// ---- Variables for storing the measurements in a file --------
	private boolean monitorSaveLoad = false;
	private BufferedWriter monitorDatasetWriter;
	// --- Some System-String ---------------------------------------
	private final String monitorDatasetDelimiter = ";";
	private final String monitorDatasetLineSeperator = System.getProperty("line.separator");
	private String monitorDecimalSeparator = Character.toString(new DecimalFormatSymbols().getDecimalSeparator());
	// --- Files which will be created for storing monitoring data --  
	private final String monitorFileMeasurementTmp = "LoadMeasurement.tmp";
	private final String monitorFileMeasurement = "LoadMeasurement.csv";
	private final String monitorFileMachines = "LoadMachines.txt";
	// --- Timestamp of a measurement and its String-format ---------
	private Long monitorTimeStamp;
	private SimpleDateFormat monitorTimeStampFormat = new SimpleDateFormat("dd.MM.yy hh:mm:ss;S");
	
	private Hashtable<String, String> monitorDatasetParts = new Hashtable<String, String>();
	private Hashtable<String, String> monitorDatasetPartsHeader = new Hashtable<String, String>();
	private Hashtable<String, String> monitorDatasetPartsDescription = new Hashtable<String, String>();
	// --------------------------------------------------------------
	
	// --------------------------------------------------------------
	// ---- Variables for threat measurements -----------------------
	private ThreadMeasureBehaviour threadMeasureBehaviour; 
	private ThreadProtocolVector threadProtocolVector;
	private ThreadInfoStorage threadInfoStorage;
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		
		this.getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
		this.getContentManager().registerOntology(JADEManagementOntology.getInstance());
		this.getContentManager().registerOntology(FIPAManagementOntology.getInstance());
		
		this.loadBalancing = new DynamicLoadBalancing(this);
		
		this.addBehaviour(this.getMonitorBehaviour()) ;
		this.addBehaviour(new ReceiveBehaviour());
	}
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() {
		super.takeDown();
		if (monitorDatasetWriter!=null) {
			this.closeMonitorFile();
		}
		if (loadDialog!=null) {
			this.getSystemLoadDialog().setVisible(false);
			this.setSystemLoadDialog(null);	
		}
		if (threadDialog!=null) {
			if(currProject.getDistributionSetup().isAutoSaveRealMetricsOnSimStop()){
				this.getThreadInfoStorage().getThreadMeasureMetrics().getMetrics();
			}
			this.getThreadDialog().setVisible(false);
			this.setThreadDialog(null);	
		}
	}
	
	/**
	 * Returns the {@link MonitorBehaviour}.
	 * @return the monitor behaviour
	 */
	public MonitorBehaviour getMonitorBehaviour() {
		if (monitorBehaviour==null) {
			monitorBehaviour = new MonitorBehaviour(this, getMonitorBehaviourTickingPeriod());
		}
		return monitorBehaviour;
	}
	
	/**
	 * Returns the monitor behaviour ticking period.
	 * @return the monitorBehaviourTickingPeriod
	 */
	public long getMonitorBehaviourTickingPeriod() {
		return monitorBehaviourTickingPeriod;
	}
	/**
	 * Sets the monitor behaviour ticking period.
	 * @param monitorBehaviourTickingPeriod the monitorBehaviourTickingPeriod to set
	 */
	public void setMonitorBehaviourTickingPeriod(long monitorBehaviourTickingPeriod) {
		this.monitorBehaviourTickingPeriod = monitorBehaviourTickingPeriod;
		this.getMonitorBehaviour().reset(monitorBehaviourTickingPeriod);
	}
	
	/**
	 * Sets the instance of the dynamic load balancing algorithm to use.
	 * @param loadBalancing the new load balancing
	 */
	public void setLoadBalancing(DynamicLoadBalancingBase loadBalancing) {
		this.loadBalancing = loadBalancing;
	}
	/**
	 * Gets the currently use dynamic load balancing algorithm.
	 * @return the loadBalancing
	 */
	public DynamicLoadBalancingBase getLoadBalancing() {
		return loadBalancing;
	}

	
	/**
	 * Sets the system load dialog.
	 * @param newLoadDialog the new system load dialog
	 */
	private void setSystemLoadDialog(SystemLoadDialog newLoadDialog) {
		this.loadDialog = newLoadDialog;
	}
	/**
	 * Returns the {@link SystemLoadDialog}.
	 * @return the system load dialog
	 */
	private SystemLoadDialog getSystemLoadDialog() {
		if (loadDialog==null && Application.isOperatingHeadless()==false) {
			loadDialog = new SystemLoadDialog(this);
		}
		return loadDialog;
	}
	/**
	 * Gets the system load panel.
	 * @return the system load panel
	 */
	private SystemLoadPanel getSystemLoadPanel() {
		if (this.getSystemLoadDialog()==null) return null;
		return this.getSystemLoadDialog().getSystemLoadPanel();
	}

	/**
	 * Returns the current {@link Project}.
	 * @return the project
	 */
	public Project getProject() {
		if (currProject==null) {
			currProject = Application.getProjectFocused();
		}
		return currProject;
	}
	/**
	 * Returns the current {@link SimulationSetup} if available.
	 * @return the simulation setup
	 */
	public SimulationSetup getSimulationSetup() {
		if (currSimSetup==null && this.getProject()!=null) {
			currSimSetup=this.getProject().getSimulationSetups().getCurrSimSetup();
		}
		return currSimSetup;
	}
	/**
	 * Returns the {@link DistributionSetup} of the current {@link Project} if available.
	 * @return the distribution setup
	 */
	public DistributionSetup getDistributionSetup() {
		if (currDisSetup==null && this.getProject()!=null) {
			currDisSetup = this.getProject().getDistributionSetup();
		}
		return currDisSetup;
	}
	
	/**
	 * Returns the {@link LoadServiceHelper}.
	 * @return the load service helper
	 */
	public LoadServiceHelper getLoadServiceHelper() {
		if (loadHelper==null) {
			try {
				loadHelper = (LoadServiceHelper) getHelper(LoadService.NAME);
			} catch (ServiceException se) {
				se.printStackTrace();
			}	
		}
		return loadHelper;
	}
	
	/**
	 * This TickerBehaviour measures, displays (if wanted) and stores the measured load values.
	 * 
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	public class MonitorBehaviour extends TickerBehaviour {

		private static final long serialVersionUID = -5802791218164507242L;
		
		private boolean initiatedLoadRecordingOnce = false;
		private int dialogTitleHeight = 38;
		private int dialogHeight = 0;
		
		/**
		 * Instantiates a new monitor behaviour.
		 *
		 * @param agent the agent
		 * @param tickerPeriod the period in which the measurements are done
		 */
		public MonitorBehaviour(Agent agent, long tickerPeriod) {
			super(agent, tickerPeriod);
		}
		
		/* (non-Javadoc)
		 * @see jade.core.behaviours.TickerBehaviour#onTick()
		 */
		@Override
		protected void onTick() {
			
			int layoutNodesVisible = 0;
			try {

				// --- Get the PlatformLoad and the Agents at their locations -----------
				monitorTimeStamp = System.currentTimeMillis();
				loadCycleTime = getLoadServiceHelper().getAvgCycleTime();
				loadContainer = getLoadServiceHelper().getContainerLoads();
				loadContainerAgentMap = getLoadServiceHelper().getAgentMap();
				loadContainerLoactions = getLoadServiceHelper().getContainerLocations();
				
				// --- Display number of agents -----------------------------------------
				if (getSystemLoadPanel()!=null) {
					getSystemLoadPanel().setNumberOfAgents(loadContainerAgentMap.noAgentsAtPlatform);
					getSystemLoadPanel().setNumberOfContainer(loadContainer.size());
					getSystemLoadPanel().setCycleTime(loadCycleTime);
				}
				loadThresholdLevels = LoadMeasureThread.getThresholdLevels();				
				// Initialise variables JVM-balancing -----------------------------------
				loadThresholdExceededOverAll = 0;
				loadMachines4Balancing = new Hashtable<String, LoadMerger>();
				loadJVM4Balancing = new Hashtable<String, LoadMerger>();
				
				// --- Walk through the list of all containers --------------------------
				loadContainer2Display = new Vector<String>(getLoadServiceHelper().getContainerQueue());
				
				for (int i = 0; i < loadContainer2Display.size(); i++) {
					// --- Get container name -------------------------------------------
					String containerName = loadContainer2Display.get(i);
					// --- Get the benchmark-result for this node/container -------------
					NodeDescription containerDesc = getLoadServiceHelper().getContainerDescription(containerName);
					Float benchmarkValue = containerDesc.getBenchmarkValue().getBenchmarkValue();
					String jvmPID = containerDesc.getJvmPID(); 
					String machineURL = containerDesc.getPlAddress().getUrl();
					// --- Get all needed load informations -----------------------------
					PlatformLoad containerLoad = loadContainer.get(containerName);
					Integer containerNoAgents = loadContainerAgentMap.getNoAgentsAtContainerHash().get(containerName);
					loadContainerBenchmarkResults.put(containerName, benchmarkValue);	
					
					// ------------------------------------------------------------------
					// --- Store informations also by the JVM (merge) -------------------
					// ------------------------------------------------------------------
					if (containerLoad!=null && jvmPID!=null) {
						// --- Observe the over all Threshold -----------------
						loadThresholdExceededOverAll += ((Math.abs(containerLoad.getLoadExceeded())));
						
						// --- Merge the load per physical machine  -----------
						LoadMerger loadMachine = loadMachines4Balancing.get(machineURL);
						if (loadMachine==null) {
							loadMachine = new LoadMerger(machineURL);
						}
						loadMachine.merge(containerName, jvmPID, benchmarkValue, containerLoad, containerNoAgents);
						loadMachines4Balancing.put(machineURL, loadMachine);
						
						// --- Merge the load per JVM -------------------------
						LoadMerger loadJvmMachine = loadJVM4Balancing.get(jvmPID);
						if (loadJvmMachine==null) {
							loadJvmMachine = new LoadMerger(jvmPID);
						}
						loadJvmMachine.merge(containerName, jvmPID, benchmarkValue, containerLoad, containerNoAgents);
						loadJVM4Balancing.put(jvmPID, loadJvmMachine);
					}
					// ------------------------------------------------------------------					
					
					// --- Get the Container-Load-Panel ---------------------------------
					SystemLoadSingle dialogSingle = containerLoadDialogs.get(containerName);
					if (dialogSingle==null) {
						dialogSingle = new SystemLoadSingle();
						if (getSystemLoadPanel()!=null) {
							getSystemLoadPanel().getJPanelForLoadDisplays().add(dialogSingle, null);	
						}
						containerLoadDialogs.put(containerName, dialogSingle);
					}
					
					// --- Display the current values -----------------------------------
					if (getSystemLoadPanel()!=null) {
						if (containerLoad==null) {
							dialogSingle.setVisibleAWTsafe(false);								
						} else {
							dialogSingle.setVisibleAWTsafe(true);
							dialogSingle.updateViewAWTsafe(containerName, containerDesc, benchmarkValue, containerLoad, containerNoAgents);
							layoutNodesVisible++;
						}
					}
					// --- If wanted, save the current values to file -------------------
					if (monitorSaveLoad==true) {
						buildDatasetPart(containerName, containerDesc, benchmarkValue, containerLoad, containerNoAgents);
					}
					
				} // --- End while ----
				
			} catch (ServiceException e) {
				e.printStackTrace();
			}

			// --- Refresh View ---------------------------------------------------------
			if (getSystemLoadPanel()!=null) {
				int newloadDialogHeightMax = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
				int newloadDialogHeight = (SystemLoadSingle.loadPanelHeight * layoutNodesVisible) + (getSystemLoadPanel().getJToolBarLoad().getHeight() + this.dialogTitleHeight);
				if (newloadDialogHeight>newloadDialogHeightMax) {
					newloadDialogHeight=newloadDialogHeightMax;
				}
				if (this.dialogHeight!=newloadDialogHeight) {
					this.dialogHeight = newloadDialogHeight;
					getSystemLoadDialog().setSize(getSystemLoadDialog().getWidth(), newloadDialogHeight);						
				}		
			}

			// --- If wanted, save the current values to file ---------------------------
			if (monitorSaveLoad==true) {
				buildAndSaveDataSet();
				if (getSystemLoadPanel()!=null) {
					if (getSystemLoadPanel().jLabelRecord.getForeground().equals(Color.gray) ) {
						getSystemLoadPanel().jLabelRecord.setForeground(Color.red);
					} else {
						getSystemLoadPanel().jLabelRecord.setForeground(Color.gray);
					}	
				}
			}
			
			// --------------------------------------------------------------------------
			// --- Which Project, SimulationSetup and DistributionSetup is used?  -------
			// --------------------------------------------------------------------------

			// --- Check if load recording has to be started directly -------------------
			if (this.initiatedLoadRecordingOnce==false && getDistributionSetup()!=null && getDistributionSetup().isImmediatelyStartLoadRecording()==true) {
				getSystemLoadPanel().setRecordingInterval(getDistributionSetup().getLoadRecordingInterval());
				getSystemLoadPanel().setDoLoadRecording(true);
				// --- Make sure that this is done only once, in order to allow stop ---
				this.initiatedLoadRecordingOnce = true;
			}
			
			// --------------------------------------------------------------------------
			// --- If configured, activate the load balancing in a dedicated thread -----
			// --------------------------------------------------------------------------
			if (getDistributionSetup()!=null && getDistributionSetup().isDoDynamicLoadBalancing()==true) {
				this.doCheckDynamicLoadBalancing();
			}
			// --------------------------------------------------------------------------
		}

		/**
		 * This method check if the dynamic load balancing is activate 
		 * in the currently executed project and which class should be 
		 * used for it.
		 */
		private void doCheckDynamicLoadBalancing() {
			
			// --- If the dynamic load balancing is still running/executed  	 --- 
			// --- from the last measure tick, exit here to prevent side effects ---
			if (isDynLoadBalancaingStillActivated()==true) return;
			// --- Set that load balancing is active now ---------------------------
			setDynLoadBalancaingStillActivated(true);
			
			// --- If the dynamic load balancing is activated: ---------------------
			LoadMeasureAgent thisLoadAgent = (LoadMeasureAgent) myAgent;
			try {
				@SuppressWarnings("unchecked")
				Class<? extends DynamicLoadBalancingBase> dynLoBaClass = (Class<? extends DynamicLoadBalancingBase>) Class.forName(getDistributionSetup().getDynamicLoadBalancingClass());
				loadBalancing = dynLoBaClass.getDeclaredConstructor( new Class[] { thisLoadAgent.getClass() }).newInstance( new Object[] { thisLoadAgent });
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}	
			// --- If loading of the class was not successful ----------------------
			// --- start the default class for balancing	  ----------------------
			if (loadBalancing==null) {
				loadBalancing = new DynamicLoadBalancing(thisLoadAgent);
			}
			// --- get the instance of the ThreadedBehaviour -----------------------
			ThreadedBehaviourFactory loadBalancingThread = new ThreadedBehaviourFactory();
			// --- execute the dynamic load balancing ------------------------------
			myAgent.addBehaviour(loadBalancingThread.wrap(loadBalancing));
			
		}// --- end dynamic LoadBalancing
		
	} // --- End of MonitorBehaviour (class) ----
	
	public boolean isDynLoadBalancaingStillActivated() {
		return dynLoadBalancaingStillActivated;
	}
	public void setDynLoadBalancaingStillActivated(boolean dynLoadBalancaingStillActivated) {
		this.dynLoadBalancaingStillActivated = dynLoadBalancaingStillActivated;
	}
	
	/**
	 * Checks if the current LoadAgent has to the save the load information in a file.
	 * @return the monitorSaveLoad
	 */
	public boolean isMonitorSaveLoad() {
		return monitorSaveLoad;
	}
	/**
	 * Sets the current LoadAgent to save or not save the load information in a file.
	 * @param monitorSaveLoad true, if the information should be saved
	 */
	public void setMonitorSaveLoad(boolean monitorSaveLoad) {
		
		this.monitorSaveLoad = monitorSaveLoad;
		// --- Create DatasetWriter -----------------------
		if (this.monitorSaveLoad==true) {
			monitorDatasetWriter = this.createMonitorFile(this.monitorFileMeasurementTmp);
		}
		// --- Close the current DatasetWriter ------------ 
		if (this.monitorSaveLoad==false && this.monitorDatasetWriter!=null) {
			this.closeMonitorFile();				
		}
	}
	
	/**
	 * This method builds one part for the load dataset where one part corresponds to one container.
	 *
	 * @param containerName the container name
	 * @param nodeDescription the NodeDescription
	 * @param benchmarkValue the benchmark value
	 * @param platformLoad the PlatformLoad
	 * @param numberOfAgents the number of agents
	 */
	private void buildDatasetPart(String containerName, NodeDescription nodeDescription, float benchmarkValue, PlatformLoad platformLoad, Integer numberOfAgents) {
		
		String dataSet = null;
		if (platformLoad == null) {
			
			dataSet = getDatasetPartEmpty();
		} else {
			
			StringBuilder sb = new StringBuilder();
			// --- CPU-Load -----------------------------------
			sb.append(platformLoad.getLoadCPU()).append(monitorDatasetDelimiter);
			// --- Memory-Load of the machine -----------------
			sb.append(platformLoad.getLoadMemorySystem()).append(monitorDatasetDelimiter);
			// --- Java Heap-Load -----------------------------
			sb.append(platformLoad.getLoadMemoryJVM()).append(monitorDatasetDelimiter);
			// --- Number of Threads --------------------------
			sb.append(platformLoad.getLoadNoThreads()).append(monitorDatasetDelimiter);
			// --- Number of Agents ---------------------------
			sb.append(numberOfAgents).append(monitorDatasetDelimiter);
			dataSet = sb.toString();
		}
		monitorDatasetParts.put(containerName, dataSet);
				
		// --- Build the Header of the dataset part ------
		if (monitorDatasetPartsHeader.get(containerName)==null) {
			
			StringBuilder sbHeader = new StringBuilder();
			// --- CPU-Load -----------------------------------
			sbHeader.append(containerName + ": % CPU-load").append(monitorDatasetDelimiter);
			// --- Memory-Load of the machine -----------------
			sbHeader.append(containerName + ": % Memory-load machine").append(monitorDatasetDelimiter);
			// --- Java Heap-Load -----------------------------
			sbHeader.append(containerName + ": % Memory-load JVM").append(monitorDatasetDelimiter);
			// --- Number of Threads --------------------------
			sbHeader.append(containerName + ": No. Threads").append(monitorDatasetDelimiter);
			// --- Number of Agents ---------------------------
			sbHeader.append(containerName + ": No. Agents").append(monitorDatasetDelimiter);
			monitorDatasetPartsHeader.put(containerName, sbHeader.toString());
		}
		
		// --- Build the part for the description of a machine --- 
		if (monitorDatasetPartsDescription.get(containerName)==null) {
			
			String newLine = monitorDatasetLineSeperator;
			
			OSInfo os = nodeDescription.getOsInfo();
			String opSys = os.getOs_name() + " " + os.getOs_version();
			
			PlatformPerformance pP = nodeDescription.getPlPerformace();
			String perform = pP.getCpu_vendor() + ": " + pP.getCpu_model();
			perform = perform.replaceAll("  ", " ");
			perform+= newLine + pP.getCpu_numberOf() + " x "+ pP.getCpu_speedMhz() + "MHz [" + pP.getMemory_totalMB() + " MB RAM]";
			
			String bench = benchmarkValue + " Mflops";
			String description = "=>" + containerName + newLine + opSys + newLine + perform + newLine + bench + newLine;

			monitorDatasetPartsDescription.put(containerName, description);
		}
	}
	
	/**
	 * This method builds one EMPTY part for the load dataset, where one part correspond to one container.
	 *
	 * @return an empty String for a dataset 
	 */
	private String getDatasetPartEmpty() {
		
		StringBuilder sb = new StringBuilder();
		// --- CPU-Load -----------------------------------
		sb.append(monitorDatasetDelimiter);
		// --- Memory-Load of the machine -----------------
		sb.append(monitorDatasetDelimiter);
		// --- Java Heap-Load -----------------------------
		sb.append(monitorDatasetDelimiter);
		// --- Number of Threads --------------------------
		sb.append(monitorDatasetDelimiter);
		// --- Number of Agents ---------------------------
		sb.append(monitorDatasetDelimiter);
		return sb.toString();
	}

	/**
	 * Saves the measured load of the containers to a dataset in the file.
	 */
	private void buildAndSaveDataSet() {
		
		String dataSet = null;
		StringBuilder sb = new StringBuilder();
		// --- Build complete dataset ---------------------
		Iterator<String> it = loadContainer2Display.iterator();
		while (it.hasNext()) {
			
			String containerName = it.next();
			String dataSetpart = monitorDatasetParts.get(containerName);
			if (dataSetpart== null) {
				dataSetpart = getDatasetPartEmpty();
			}
			sb.append(dataSetpart);
		}
		dataSet = sb.toString();
		
		// --- If the LOCALE DecimalSeperator is differnt to '.' (dot ---------
		if ( monitorDecimalSeparator.equals(".") == false ) {
			dataSet = dataSet.replaceAll("\\.", monitorDecimalSeparator);
		}
		
		// --- Add TimeStamp ------------------------------
		String timeStamp = monitorTimeStampFormat.format(new Date(monitorTimeStamp));
		dataSet = timeStamp + monitorDatasetDelimiter + dataSet;

		// --- Save the dataset to file -------------------
		this.saveDataSet(dataSet);

		// --- Reset the dataset Array --------------------
		monitorDatasetParts.clear();
	}
	
	/**
	 * This method writes a single dataset to the current file of the 'monitorDatasetWriter'.
	 * @param dataSet the dataset
	 */
	private void saveDataSet(String dataSet) {
	
		// --- Check if the file is open ------------------
		if (this.monitorDatasetWriter==null) {
			this.monitorDatasetWriter = this.createMonitorFile(this.monitorFileMeasurement);
		}
		
		// --- Write the dataset to the file --------------
		try {
			this.monitorDatasetWriter.write(dataSet + this.monitorDatasetLineSeperator);
			this.monitorDatasetWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

	/**
	 * This Method returns the header line for the main monitoring file.
	 * @return the header line as a String
	 */
	private String getHeaderLine() {
		
		StringBuilder sb = new StringBuilder();
		
		// --- Add Date / Millisecond - Header ------------
		sb.append("Time").append(monitorDatasetDelimiter);
		sb.append("Millis").append(monitorDatasetDelimiter);

		// --- Build complete dataset ---------------------
		Iterator<String> it = loadContainer2Display.iterator();
		while (it.hasNext()) {
			String containerName = it.next();
			sb.append(monitorDatasetPartsHeader.get(containerName));
		}
		return sb.toString();
	}
	
	/**
	 * This method creates a BufferedWriter for the measurements.
	 *
	 * @param fileName the file name
	 * @return the buffered writer
	 */
	private BufferedWriter createMonitorFile(String fileName) {
		
		File monitorFile = new File(fileName); 
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(monitorFile);
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return bw;
	}
	
	/**
	 * This method closes the BufferedWriter for the Measurements.
	 */
	private void closeMonitorFile() {
		
		// ----------------------------------------------------------
		// --- Close the file now -----------------------------------
		// ----------------------------------------------------------
		try {
			this.monitorDatasetWriter.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		this.monitorDatasetWriter = null;

		// ----------------------------------------------------------
		// --- Create a complete file of the Monitoring with header - 
		// ----------------------------------------------------------
		// --- Writer to copy the tmp-file ----------------
		BufferedWriter bwMeasurements = null;
		BufferedReader br = null;
		String currLine = null;
		try {
			bwMeasurements = createMonitorFile(this.monitorFileMeasurement);
			// --- add the header-part --------------------	
			bwMeasurements.write(this.getHeaderLine() + this.monitorDatasetLineSeperator);
			// --- open tmp-file and write it new ---------
			br = new BufferedReader(new FileReader(this.monitorFileMeasurementTmp));
			while ((currLine = br.readLine()) != null) { 
				bwMeasurements.write(currLine + this.monitorDatasetLineSeperator);
				bwMeasurements.flush();
			} 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bwMeasurements!=null) bwMeasurements.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			try {
				if (br!=null) br.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		// ----------------------------------------------------------
		// --- Write down all container descriptions ---------------- 
		// ----------------------------------------------------------
		BufferedWriter bwMachines = null;
		try {
			bwMachines = createMonitorFile(this.monitorFileMachines);
			for (String containerName : this.loadContainer2Display) {
				bwMeasurements.write(this.monitorDatasetPartsDescription.get(containerName));
				bwMeasurements.flush();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bwMachines!=null) bwMachines.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	
	// ------------------------------------------------------------
	// --- Methods for the Thread Measurements --- Start ----------
	// ------------------------------------------------------------
	public ThreadMeasureBehaviour getThreadMeasureBehaviour() {
		if (threadMeasureBehaviour==null) {
			threadMeasureBehaviour = new ThreadMeasureBehaviour(this, this.getThreadMeasurementTickingPeriod());
		}
		return threadMeasureBehaviour;
	}
	/**
	 * (Re-)Starts the thread measurement.
	 * @param oneShotBehaviour set true if you want to do a single measurement, otherwise false
	 */
	public void reStartThreadMeasurement(boolean oneShotBehaviour) {
		ThreadMeasureBehaviour tmb = this.getThreadMeasureBehaviour();
		tmb.setOneShotBehaviour(oneShotBehaviour);
		if (oneShotBehaviour==true) {
			tmb.reset(1);	
		} else {
			tmb.reset(this.getThreadMeasurementTickingPeriod());
		}
		this.addBehaviour(tmb);
	}
	/**
	 * Sets the thread measurement ticking period.
	 * @param threadMeasurementTickingPeriod the new thread measurement ticking period
	 */
	public void setThreadMeasurementTickingPeriod(long threadMeasurementTickingPeriod) {
		this.threadMeasurementTickingPeriod = threadMeasurementTickingPeriod;
		this.getThreadMeasureBehaviour().reset(threadMeasurementTickingPeriod);
	}
	/**
	 * Returns the thread measurement ticking period.
	 * @return the thread measurement ticking period
	 */
	public Long getThreadMeasurementTickingPeriod() {
		return threadMeasurementTickingPeriod;
	}
	
	/**
	 * Adds a thread protocol to the information of the agent.
	 * @param tp the thread protocol
	 */
	public void addThreadProtocol(ThreadProtocol tp) {
		synchronized (this.getThreadProtocolVector()) {
			if (this.getThreadProtocolVector().getTimestamp()!=tp.getTimestamp()) {
				this.getThreadProtocolVector().clear();
			}
			this.getThreadProtocolVector().add(tp);
			this.getThreadInfoStorage().add(tp);
			this.getThreadDialog().getJPanelMeasureMetrics().enableMetricsCalculationButton();
		}
	}
	/**
	 * Returns the current {@link ThreadProtocol}.
	 * @return the thread protocol
	 */
	public ThreadProtocolVector getThreadProtocolVector() {
		if (threadProtocolVector==null) {
			threadProtocolVector = new ThreadProtocolVector();
		}
		return threadProtocolVector;
	}
	
	/**
	 * Gets the thread info storage.
	 * @return the thread info storage
	 */
	public ThreadInfoStorage getThreadInfoStorage() {
		if (threadInfoStorage==null) {
			threadInfoStorage = new ThreadInfoStorage();
		}
		return threadInfoStorage;
	}
	
	/**
	 * Sets the thread dialog.
	 * @param threadDialog the new thread dialog
	 */
	public void setThreadDialog(ThreadMonitor threadDialog) {
		this.threadDialog = threadDialog;
	}
	/**
	 * Gets the thread dialog.
	 * @return the thread dialog
	 */
	public ThreadMonitor getThreadDialog() {
		if (threadDialog==null) {
			threadDialog = new ThreadMonitor(this);
		}
		return threadDialog;
	}
	// ------------------------------------------------------------
	// --- Methods for the Thread Measurements --- End ------------
	// ------------------------------------------------------------	
	
	
	// -----------------------------------------------------
	// --- Message-Receive-Behaviour --- S T A R T ---------
	// -----------------------------------------------------
	/**
	 * This is the message receive behaviour of the agent, which is basically waiting 
	 * for a message to open the {@link SystemLoadDialog}. 
	 * 
	 */
	private class ReceiveBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			
			Action act = null;
			
			ACLMessage msg = myAgent.receive();			
			if (msg!=null) {
				// --- Ontology-specific Message ----------------
				try {
					if (msg.getOntology().equals(FIPAManagementOntology.NAME)==true) {
						Object fipaMessage = getContentManager().extractContent(msg);
						if (fipaMessage instanceof Result) {
							Result result = (Result) fipaMessage;
							if (result.getAction() instanceof Search ) {
								// --- no action required -------
								System.err.println("=> " + this.getAgent().getLocalName() + " - Received " + FIPAManagementOntology.NAME +" result:");
							}
						}
						
					} else {
						act = (Action) getContentManager().extractContent(msg);						
					}
					
				} catch (UngroundedException ue) {
					ue.printStackTrace();
				} catch (CodecException ce) {
					ce.printStackTrace();
				} catch (OntologyException oe) {
					oe.printStackTrace();
				}

				if (act!=null) {
					Concept agentAction = act.getAction();
					if (agentAction instanceof ShowMonitorGUI) {
						// --- Open Load monitor ----------
						if (Application.isOperatingHeadless()==false) {
							getSystemLoadDialog().setVisible(true);	
						}
					} else if (agentAction instanceof ShowThreadGUI) {
						// --- Open Thread monitor --------
						if (Application.isOperatingHeadless()==false) {
							getThreadDialog().setVisible(true);
						}
					}
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

	
}
