package mas.service.distribution.agents;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;
import java.util.Vector;

import mas.service.SimulationService;
import mas.service.SimulationServiceHelper;
import mas.service.distribution.ontology.PlatformLoad;
import mas.service.load.LoadInformation.AgentMap;
import mas.service.load.LoadInformation.Container2Wait4;
import mas.service.sensoring.ServiceSensor;
import mas.service.time.TimeModelStroke;

/**
 * @version 1.0
 */ 
public class ASimulationServiceExampleAgent extends Agent implements ServiceSensor { 

	private static final long serialVersionUID = 1L;
	
	protected void setup() { 

		SimulationServiceHelper simHelper = null;
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			TimeModelStroke tmd = new TimeModelStroke();
			simHelper.setTimeModel(tmd);

		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		// ---- Add Cyclic Behaviour -----------
		//this.addBehaviour(new HelloBehaviour(this,3000));
		//this.requestNewRemoteContainer();
		this.addBehaviour(new HelloBehaviour(this, 1000));
	} 
	
	class HelloBehaviour extends TickerBehaviour { 

		private static final long serialVersionUID = 1L;

		public HelloBehaviour(Agent a, long period) {
			super(a, period);
		}
		
		public void onTick() { 
			
//			LoadMeasureAvgSigar loadCurrentAvg = LoadMeasureThread.getLoadCurrentAvg();
//			LoadMeasureAvgJVM loadCurrentAvgJVM = LoadMeasureThread.getLoadCurrentAvgJVM();
//			LoadThresholdLeveles thresholdLeveles = LoadMeasureThread.getThresholdLeveles();
//			
//			// --- Current percentage "CPU used" --------------
//			double tempCPU  = (double)Math.round((1-loadCurrentAvg.getCpuIdleTime())*10000)/100;
//			// --- Current percentage "Memory used" -----------
//			double tempMemo = (double)Math.round(((double)loadCurrentAvgJVM.getJvmHeapUsed() / (double)loadCurrentAvgJVM.getJvmHeapMax()) * 10000)/100;
//			// --- Current number of running threads ----------
//			int tempNoThreads = loadCurrentAvgJVM.getJvmThreadCount();
//						
//			System.out.println( "[Agent] -> CPU used: " + tempCPU + "% (" + thresholdLeveles.getThCpuL() + "/" + thresholdLeveles.getThCpuH() + ")" );
//			System.out.println( "[Agent] -> Memory used: " + tempMemo + "% (" + thresholdLeveles.getThMemoL() + "/" + thresholdLeveles.getThMemoH() + ")" );
//			System.out.println( "[Agent] -> N-Threads: " + tempNoThreads + " (" + thresholdLeveles.getThNoThreadsL() + "/" + thresholdLeveles.getThNoThreadsH() + ")" );

			
			SimulationServiceHelper simHelper = null;
			Hashtable<String, PlatformLoad> lma = null;
			String newContainerName = null;
			try {
				simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				
				// --- Start a new remote container -----------------
				newContainerName = simHelper.startNewRemoteContainer();
				boolean newContainerStarted = false;
				while (true) {
					Container2Wait4 waitCont = simHelper.startNewRemoteContainerStaus(newContainerName);	
					if (waitCont.isStarted()) {
						System.out.println("Remote Container '" + newContainerName + "' was started!");
						newContainerStarted = true;
						break;
					}
					if (waitCont.isCancelled()) {
						System.out.println("Remote Container '" + newContainerName + "' was NOT started!");
						newContainerStarted = false;
						break;
					}
					if (waitCont.isTimedOut()) {
						System.out.println("Remote Container '" + newContainerName + "' timed out!");
						newContainerStarted = false;
						break;	
					}
					doWait(1000);
				}
				
				if (newContainerStarted == true) {
					
//					// --- Get Location-Info for the new container ------
//					Location loc = simHelper.getContainerLocation(newContainerName);
//					System.out.println("Location: " + loc.getName() + " - " + loc.getID()+ " - " + loc.getAddress());
//					System.out.println("");
//					
//					
//					// --- Get the Node-Description ---------------------
//					NodeDescription nodeDesc = simHelper.getContainerDescription(newContainerName);
//					// --- Name ---
//					String nodeName = nodeDesc.getContainerName();
//					// --- Operating System ---
//					OSInfo nodeOS = nodeDesc.getOsInfo();
//					// --- Hardware ---
//					PlatformPerformance nodePerf = nodeDesc.getPlPerformace();
//					// --- Benchmark ---
//					BenchmarkResult nodeBench = nodeDesc.getBenchmarkValue();
//					
//					System.out.println("=> Node-Description: " + nodeName);
//					System.out.println("=> " + nodeOS.getOs_name() + " - " + nodeOS.getOs_version() + " (" + nodeOS.getOs_arch() + ")");
//					System.out.println("=> " + nodePerf.getCpu_vendor() + " - " + nodePerf.getCpu_model());
//					System.out.println("=> CPU: " + nodePerf.getCpu_numberOf() + " x " + nodePerf.getCpu_speedMhz() + " Mhz | Memory: " + nodePerf.getMemory_totalMB() + " MB");
//					System.out.println("=> Benchmark-Value: " + nodeBench.getBenchmarkValue() );
//					System.out.println("");
//					
//					
//					// --- Try to get the default values for the remote-container-configuration 
//					RemoteContainerConfig remConf = simHelper.getDefaultRemoteContainerConfig();
//					System.out.println("++ New Remote Config: " + remConf.getJadeContainerName() );
//					System.out.println("++ Jade Main: " + remConf.getJadeHost() + " - " + remConf.getJadePort());
//					System.out.println("++ Jade Services: " + remConf.getJadeServices());
//					System.out.println("++ Java-VM: " + remConf.getJvmMemAllocInitial() + " - " + remConf.getJvmMemAllocMaximum());
//					System.out.println("");

					
					// --- Measure the loads on the containers ----------
					lma = simHelper.getContainerLoads();
					Vector<String> vecLoad = new Vector<String>( lma.keySet() );
					Collections.sort(vecLoad);
				    Iterator<String> it = vecLoad.iterator();
				    while (it.hasNext()) {
				    	String container = it.next();
				    	PlatformLoad pl = lma.get(container);
				    	System.out.println( "Load on '" + container + "': CPU: " + pl.getLoadCPU() + " - MemorySystem: " + pl.getLoadMemorySystem() + " - MemorySystemJVM: " + pl.getLoadMemoryJVM() + " - NoThreads: " + pl.getLoadNoThreads() + " - Exceeded: " + pl.getLoadExceeded() + "" );
				    }
				    
					// --- Refresh the AgentMap -------------------------
				    AgentMap agentMap = simHelper.getAgentMap();
				    System.out.println( "====> Agents at Platform: " +  agentMap.noAgentsAtPlatform + " <====");

				    Vector<String> vecAgentMap = new Vector<String>( lma.keySet() );
				    Collections.sort(vecAgentMap);
				    Iterator<String> no = vecAgentMap.iterator();
					while (no.hasNext()) {
						String contName = no.next();
						System.out.println( "==> Agents at " + contName + ": " +  agentMap.noAgentsAtContainer.get(contName) );
					}					
					
				}
				
				
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
			myAgent.doSuspend();
			
		} 
	}

	@Override
	public void update(Observable o, Object arg) {
		
		
	}
	
} 
