package agentgui.simulationService.agents;

import jade.content.Concept;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.Location;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.ACLMessage;

import java.awt.Color;
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
import agentgui.core.application.Project;
import agentgui.core.sim.setup.DistributionSetup;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.balancing.DynamicLoadBalancing;
import agentgui.simulationService.balancing.DynamicLoadBalancingBase;
import agentgui.simulationService.load.LoadAgentMap;
import agentgui.simulationService.load.LoadMeasureThread;
import agentgui.simulationService.load.LoadMerger;
import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.load.LoadInformation.NodeDescription;
import agentgui.simulationService.load.gui.SystemLoad;
import agentgui.simulationService.load.gui.SystemLoadDialog;
import agentgui.simulationService.load.gui.SystemLoadSingle;
import agentgui.simulationService.load.gui.SystemLoad.TimeSelection;
import agentgui.simulationService.ontology.OSInfo;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.PlatformPerformance;
import agentgui.simulationService.ontology.ShowMonitorGUI;


public class LoadAgent extends Agent {

	private static final long serialVersionUID = 3035508112883482740L;
	
	// --------------------------------------------------------------
	// --- To which Project are we running in the moment ------------ 
	protected Project currProject = null;
	protected SimulationSetup currSimSetup = null;
	protected DistributionSetup currDisSetup = null;
	
	// --------------------------------------------------------------
	// --- Display-elements of the Monitoring system ---------------- 
	private SystemLoadDialog loadDialog = null; 
	private SystemLoad loadPanel = null;
	// --- Remember container Informations/Instances (Display) ------
	private Hashtable<String, SystemLoadSingle> containerLoadDialogs = new Hashtable<String, SystemLoadSingle>(); 
	// --------------------------------------------------------------

	// --------------------------------------------------------------
	// --- The measure/display behaviour of the agent --------------- 
	public MonitorBehaviour monitorBehaviour = null;
	private long monitorBehaviourTickingPeriod = 0;
	// --------------------------------------------------------------

	// --------------------------------------------------------------
	// --- The balancing algorithm of this agent --------------------
	private DynamicLoadBalancingBase loadBalancing = null;
	public boolean loadBalancingActivated = false;
	// --------------------------------------------------------------
	
	// --------------------------------------------------------------
	// --- Over all and current threshold level ---------------------
	public Integer loadThresholdExceededOverAll = 0;
	public LoadThresholdLevels loadThresholdLevels = null;
	public double loadCycleTime = 0;
	
	// --- Used (dead or alive) Nodes of the system, ordered --------
	public Vector<String> loadContainer2Display = null;
	public LoadAgentMap loadContainerAgentMap = null;
	public Hashtable<String, PlatformLoad> loadContainer = null;
	public Hashtable<String, Location> loadContainerLoactions = null;
	public Hashtable<String, Float> loadContainerBenchmarkResults = new Hashtable<String, Float>();
	
	// --- Currently running JVM's for this platform -----------------
	public Hashtable<String, LoadMerger> loadJVM4Balancing = new Hashtable<String, LoadMerger>();

	// --- Currently running phys. Machine for this platform ---------
	public Hashtable<String, LoadMerger> loadMachines4Balancing = new Hashtable<String, LoadMerger>();
	// --------------------------------------------------------------

	// --------------------------------------------------------------
	// ---- Variables for storing the current measurements ----------
	private boolean monitorSaveLoad = false;
	private BufferedWriter monitorDatasetWriter = null;
	// --- Some System-String ---------------------------------------
	private final String monitorDatasetDelimiter = ";";
	private final String monitorDatasetLineSeperator = System.getProperty("line.separator");
	private String monitorDecimalSeparator;
	// --- Files which will be created for storing monitoring data --  
	private final String monitorFileMeasurementTmp = "LoadMeasurement.tmp";
	private final String monitorFileMeasurement = "LoadMeasurement.csv";
	private final String monitorFileMachines = "LoadMachines.txt";
	// --- Timestamp of a measurement and its String-format ---------
	private Long monitorTimeStamp = null;
	private SimpleDateFormat monitorTimeStampFormat = new SimpleDateFormat("dd.MM.yy hh:mm:ss;S");
	
	private Hashtable<String, String> monitorDatasetParts = new Hashtable<String, String>();
	private Hashtable<String, String> monitorDatasetPartsHeader = new Hashtable<String, String>();
	private Hashtable<String, String> monitorDatasetPartsDescription = new Hashtable<String, String>();
	// --------------------------------------------------------------
	
	
	@Override
	protected void setup() {
		super.setup();
		
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(); 
		monitorDecimalSeparator = Character.toString(dfs.getDecimalSeparator());
		
		this.getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
		this.getContentManager().registerOntology(JADEManagementOntology.getInstance());
		
		loadPanel = new SystemLoad(this);
		
		loadDialog = new SystemLoadDialog();
		loadDialog.setContentPane(loadPanel);
		//loadDialog.setVisible(true);
		
		loadBalancing = new DynamicLoadBalancing(this);
		
		monitorBehaviourTickingPeriod = ((TimeSelection) loadPanel.jComboBoxInterval.getSelectedItem()).getTimeInMill();
		monitorBehaviour = new MonitorBehaviour(this, monitorBehaviourTickingPeriod);
		this.addBehaviour(monitorBehaviour) ;
		this.addBehaviour(new ReceiveBehaviour());
	}
	
	@Override
	protected void takeDown() {
		super.takeDown();
		if (monitorDatasetWriter!=null) {
			this.closeMonitorFile();
		}
		if (loadDialog!=null) {
			loadDialog.setVisible(false);
			loadDialog = null;
		}
	}
	
	/**
	 * This Method shows the GUI of this LoadAgent
	 */
	public void showGUI() {
		loadDialog.setVisible(true);
	}
	/**
	 * @return the monitorBehaviourTickingPeriod
	 */
	public long getMonitorBehaviourTickingPeriod() {
		return monitorBehaviourTickingPeriod;
	}
	/**
	 * @param monitorBehaviourTickingPeriod the monitorBehaviourTickingPeriod to set
	 */
	public void setMonitorBehaviourTickingPeriod(long monitorBehaviourTickingPeriod) {
		this.monitorBehaviourTickingPeriod = monitorBehaviourTickingPeriod;
		this.monitorBehaviour.reset(monitorBehaviourTickingPeriod);
	}
	/**
	 * @param loadBalancingBase the loadBalancing to set
	 */
	public void setLoadBalancing(DynamicLoadBalancingBase loadBalancing) {
		this.loadBalancing = loadBalancing;
	}
	/**
	 * @return the loadBalancing
	 */
	public DynamicLoadBalancingBase getLoadBalancing() {
		return loadBalancing;
	}

	/**
	 * This behaviour measures, display and (if wanted) stores the measured load values 
	 * @author Christian Derksen
	 */
	public class MonitorBehaviour extends TickerBehaviour {

		private static final long serialVersionUID = -5802791218164507242L;
		private int loadDialogHeight = 0;
		
		public MonitorBehaviour(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void onTick() {
			
			int layoutNodesVisible = 0;
			try {
				SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				
				// --- Get the PlatformLoad and the Agents at their locations -----------
				monitorTimeStamp = System.currentTimeMillis();
				loadCycleTime = simHelper.getAvgCycleTime();
				loadContainer = simHelper.getContainerLoads();
				loadContainerAgentMap = simHelper.getAgentMap();
				loadContainerLoactions = simHelper.getContainerLocations();
				
				// --- Display number of agents -----------------------------------------
				loadPanel.setNumberOfAgents(loadContainerAgentMap.noAgentsAtPlatform);
				loadPanel.setNumberOfContainer(loadContainer.size());
				loadPanel.setCycleTime(loadCycleTime);
				loadThresholdLevels = LoadMeasureThread.getThresholdLevels();
				
				// Initialise variables JVM-balancing -----------------------------------
				loadThresholdExceededOverAll = 0;
				loadMachines4Balancing = new Hashtable<String, LoadMerger>();
				loadJVM4Balancing = new Hashtable<String, LoadMerger>();
				
				// --- Walk through the list of all containers --------------------------
				loadContainer2Display = new Vector<String>(simHelper.getContainerQueue());
				Iterator<String> it = loadContainer2Display.iterator();
				while (it.hasNext()) {
					
					String containerName = it.next();
					
					// --- Get the benchmark-result for this node/container -------------
					NodeDescription containerDesc = simHelper.getContainerDescription(containerName);
					Float benchmarkValue = containerDesc.getBenchmarkValue().getBenchmarkValue();
					String jvmPID = containerDesc.getJvmPID(); 
					String machineURL = containerDesc.getPlAddress().getUrl();
					
					// --- Get all needed load informations -----------------------------
					PlatformLoad containerLoad = loadContainer.get(containerName);
					Integer containerNoAgents = loadContainerAgentMap.noAgentsAtContainer.get(containerName);
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
						loadPanel.jPanelLoad.add(dialogSingle, null);
						containerLoadDialogs.put(containerName, dialogSingle);
					}
					
					// --- Display the current values -----------------------------------
					if (containerLoad==null) {
						dialogSingle.setVisible(false);
					} else {
						dialogSingle.setVisible(true);
						dialogSingle.updateView(containerName, containerDesc, benchmarkValue, containerLoad, containerNoAgents);
						dialogSingle.repaint();
						layoutNodesVisible++;
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
			int newloadDialogHeight = (85*layoutNodesVisible) + 60; 
			if (newloadDialogHeight!=loadDialogHeight) {
				loadDialog.setSize(loadDialog.getWidth(), newloadDialogHeight);
				loadDialogHeight = newloadDialogHeight;
			}		
			// --- If wanted, save the current values to file ---------------------------
			if (monitorSaveLoad==true) {
				buildAndSaveDataSet();
				if (loadPanel.jLabelRecord.getForeground().equals(Color.gray) ) {
					loadPanel.jLabelRecord.setForeground(Color.red);
				} else {
					loadPanel.jLabelRecord.setForeground(Color.gray);
				}
			}
			// --------------------------------------------------------------------------
			// --- Now, activate the load balancing algorithm in a dedicated thread -----
			// --------------------------------------------------------------------------
			this.doCheckDynamicLoadBalancing();
			// --------------------------------------------------------------------------
		}

		/**
		 * This method check if the dynamic load balancing is activate 
		 * in the currently executed project and which class should be 
		 * used for it.
		 */
		public void doCheckDynamicLoadBalancing() {
			
			// --- if the dynamic load balancing is still running, executed --- 
			// --- the last time, exit here to prevent side effects  		---
			if (loadBalancingActivated == true) {
				return;
			}
			loadBalancingActivated = true;
			
			// --- Which project is currently used?  --------------------------
			currProject = Application.ProjectCurr;		
			if ( currProject == null ) {
				currSimSetup = null;
				currDisSetup = null;
				return;
			} 
			// --- Get the current simulation setup ---------------------------
			currSimSetup = currProject.simSetups.getCurrSimSetup();
			// --- Get the current distribution setup -------------------------
			currDisSetup = currSimSetup.getDistributionSetup();
			
			// --- If the dynamic load balancing is activated: ----------------
			if (currDisSetup!=null && currDisSetup.isDoDynamicLoadBalalncing()==true) {
			
				LoadAgent thisLoadAgent = (LoadAgent) myAgent;
				try {
					@SuppressWarnings("unchecked")
					Class<? extends DynamicLoadBalancingBase> dynLoBaClass = (Class<? extends DynamicLoadBalancingBase>) Class.forName(currDisSetup.getDynamicLoadBalancingClass());
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
				// --- If loading of the class was not successful ----
				// --- start the default class for balancing	  ----
				if (loadBalancing==null) {
					loadBalancing = new DynamicLoadBalancing(thisLoadAgent);
				}
				// --- get the instance of the ThreadedBehaviour --------------
				ThreadedBehaviourFactory loadBalancingThread = new ThreadedBehaviourFactory();
				// --- execute the dynamic load balancing ---------------------
				myAgent.addBehaviour(loadBalancingThread.wrap(loadBalancing));
						
			}
			
		}// --- end dynamic LoadBalancing
		
	} // --- End of MonitorBehaviour (class) ----
	
	
	/**
	 * @return the monitorSaveLoad
	 */
	public boolean isMonitorSaveLoad() {
		return monitorSaveLoad;
	}
	/**
	 * @param monitorSaveLoad the monitorSaveLoad to set
	 */
	public void setMonitorSaveLoad(boolean monitorSaveLoad) {
		this.monitorSaveLoad = monitorSaveLoad;

		// --- Create DatasetWriter -----------------------
		if (monitorSaveLoad==true) {
			monitorDatasetWriter = createMonitorFile(monitorFileMeasurementTmp);
		}
		// --- Close the current DatasetWriter ------------ 
		if (monitorSaveLoad==false && monitorDatasetWriter!=null) {
			this.closeMonitorFile();				
		}
	}
	
	/**
	 * This method builds one part for the load dataset where  
	 * one part corresponds to one container 
	 */
	private void buildDatasetPart(String containerName, NodeDescription nD, float benchmarkValue, PlatformLoad pL, Integer noAg) {
		
		String dataSet = null;
		if (pL == null) {
			
			dataSet = getDatasetPartEmpty();
		} else {
			
			StringBuilder sb = new StringBuilder();
			// --- CPU-Load -----------------------------------
			sb.append(pL.getLoadCPU()).append(monitorDatasetDelimiter);
			// --- Memory-Load of the machine -----------------
			sb.append(pL.getLoadMemorySystem()).append(monitorDatasetDelimiter);
			// --- Java Heap-Load -----------------------------
			sb.append(pL.getLoadMemoryJVM()).append(monitorDatasetDelimiter);
			// --- Number of Threads --------------------------
			sb.append(pL.getLoadNoThreads()).append(monitorDatasetDelimiter);
			// --- Number of Agents ---------------------------
			sb.append(noAg).append(monitorDatasetDelimiter);
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
			
			OSInfo os = nD.getOsInfo();
			String opSys = os.getOs_name() + " " + os.getOs_version();
			
			PlatformPerformance pP = nD.getPlPerformace();
			String perform = pP.getCpu_vendor() + ": " + pP.getCpu_model();
			perform = perform.replaceAll("  ", " ");
			perform+= newLine + pP.getCpu_numberOf() + " x "+ pP.getCpu_speedMhz() + "MHz [" + pP.getMemory_totalMB() + " MB RAM]";
			
			String bench = benchmarkValue + " Mflops";
			String description = "=>" + containerName + newLine + opSys + newLine + perform + newLine + bench + newLine;

			monitorDatasetPartsDescription.put(containerName, description);
		}
	}
	
	/**
	 * This method builds one EMPTY part for the load dataset where  
	 * one part correspond to one container 
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
	 * This method writes a dataset to the current file of the 'monitorDatasetWriter'
	 * @param dataSet
	 */
	private void saveDataSet(String dataSet) {
	
		// --- Check if the file is open ------------------
		if (monitorDatasetWriter==null) {
			monitorDatasetWriter = createMonitorFile(monitorFileMeasurement);
		}
		
		// --- Write teh dataset to the file --------------
		try {
			monitorDatasetWriter.write(dataSet + monitorDatasetLineSeperator);
			monitorDatasetWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

	/**
	 * This Method returns the header line for the main monitoring file 
	 * @return
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
	 * This method creates a BufferedWriter for the measurements
	 * @param fileName
	 * @return
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
	 * This method closes the BufferedWriter for the Measurements
	 */
	private void closeMonitorFile() {
		
		// ----------------------------------------------------------
		// --- Close the file now -----------------------------------
		// ----------------------------------------------------------
		try {
			monitorDatasetWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		monitorDatasetWriter = null;

		// ----------------------------------------------------------
		// --- Create a complete file of the Monitoring with header - 
		// ----------------------------------------------------------
		// --- Writer to copy the tmp-file ----------------
		BufferedWriter bw = createMonitorFile(monitorFileMeasurement);
		BufferedReader br = null;
		String currLine = null;
		try {
			// --- add the header-part --------------------	
			bw.write(this.getHeaderLine() + monitorDatasetLineSeperator);
			// --- open tmp-file and write it new ---------
			br = new BufferedReader(new FileReader(monitorFileMeasurementTmp));
			while ((currLine = br.readLine()) != null) { 
				bw.write(currLine + monitorDatasetLineSeperator);
				bw.flush();
			} 
			bw.close();
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// ----------------------------------------------------------
		// --- Write down all container descriptions ---------------- 
		// ----------------------------------------------------------
		bw = createMonitorFile(monitorFileMachines);
		try {
			Iterator<String> it = loadContainer2Display.iterator();
			while (it.hasNext()) {
				
				String containerName = it.next();
				bw.write(monitorDatasetPartsDescription.get(containerName));
				bw.flush();
			}
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// -----------------------------------------------------
	// --- Message-Receive-Behaiviour --- S T A R T --------
	// -----------------------------------------------------
	/**
	 * This is the message receive behaviour of the agent
	 */
	private class ReceiveBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

		@Override
		public void action() {
			
			Action act = null;
			
			ACLMessage msg = myAgent.receive();			
			if (msg!=null) {
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

				if (act!=null) {
					Concept agentAction = act.getAction();
					if (agentAction instanceof ShowMonitorGUI) {
						loadDialog.setVisible(true);
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
