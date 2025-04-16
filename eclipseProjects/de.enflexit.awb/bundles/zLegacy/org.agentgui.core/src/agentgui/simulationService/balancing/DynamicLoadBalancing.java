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
package agentgui.simulationService.balancing;

import jade.core.Location;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.agentgui.gui.swing.project.Distribution;

import agentgui.simulationService.agents.LoadMeasureAgent;
import agentgui.simulationService.load.LoadMerger;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.load.LoadAgentMap.AID_Container_List;

/**
 * This is the default class for the dynamic load balancing. It will apply, if
 * the 'Enable dynamic load balancing' - checkbox is set selected.
 * 
 * @see DynamicLoadBalancingBase
 * @see BaseLoadBalancingInterface
 * @see Distribution
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DynamicLoadBalancing extends DynamicLoadBalancingBase {

	private static final long serialVersionUID = -4721675611537786965L;

	private Location lastNewLocation = null;
	
	/**
	 * Instantiates a new dynamic load balancing.
	 *
	 * @param loadMeasureAgent the load agent
	 */
	public DynamicLoadBalancing(LoadMeasureAgent loadMeasureAgent) {
		super(loadMeasureAgent);
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.balancing.DynamicLoadBalancingBase#doBalancing()
	 */
	//@Override
	public void doBalancing() {
		
		Vector<AID_Container> transferAgents = new Vector<AID_Container>();
		
		if (currThresholdExceededOverAll!=0) {
			// --------------------------------------------------------------------------
			// --- If the threshold level is exceeded, start a new remote container -----
			// --------------------------------------------------------------------------

			// --------------------------------------------------------------------------
			// --- The 'startRemoteContainer'-method waits for an answer which can be
			// --- 'isStarted', 'isCancelled' or 'isTimedOut'. In case that the new
			// --- container was started, the method will return the new container name.
			// --- Additionally the current state of the new container will be added to
			// --- to the load-informations 
			// --------------------------------------------------------------------------
			String newRemoteContainer = this.startRemoteContainer();
			if (newRemoteContainer!=null) {

				// ----------------------------------------------------------------------
				// --- Define the static load balancing factor --------------------------
				float staticLoadBalancingFactor = noAgents / machinesBenchmarkSummation;
				
				// ----------------------------------------------------------------------
				// --- Define a new static distribution of the agents -------------------
				Hashtable<String, Integer[]> newMachineBalance = new Hashtable<String, Integer[]>();
				int noAgents2SetSum = 0;

				// ----------------------------------------------------------------------
				// --- Find agents, which can be pulled out of it's container  ----------
				// --- and fill the Vector 'transferAgents' with agents to move ---------
 				for (int i=0; i<noMachines; i++) {
					
					String machineID = machineArray[i];
					LoadMerger load = loadMachines4Balancing.get(machineID);
					
					int noAgents2Set = 0;
					if (i==noMachines-1) {
						noAgents2Set = noAgents - noAgents2SetSum;
					} else {
						noAgents2Set = Math.round(staticLoadBalancingFactor * load.getBenchmarkValue());
					}
					noAgents2SetSum += noAgents2Set;
					int noAgents2SetDiff = (-1)*(load.getNumberOfAgents() - noAgents2Set);
					
					// ------------------------------------------------------------------
					// --- If agents can be pulled out from this machine, select agents -
					if (noAgents2SetDiff<0) {
						selectAgents4MigrationFromMachine(transferAgents, load, Math.abs(noAgents2SetDiff));
					}
					// ------------------------------------------------------------------					
					Integer[] no2Reach = new Integer[2];
					no2Reach[0] = noAgents2Set;
					no2Reach[1] = noAgents2SetDiff;
					newMachineBalance.put(machineID, no2Reach);
				}// - end for -
 				
 				// ----------------------------------------------------------------------
				// --- Now find the locations to migrate the agents to ------------------		
 				int transferAgentsPointer = 0;
				for (int i=0; i<noMachines; i++) {
					
					String machineID = machineArray[i];
					LoadMerger load = loadMachines4Balancing.get(machineID);
					
					Integer[] no2Reach = newMachineBalance.get(machineID);
					if (no2Reach[1]>0) {
						// --- Transfer agents to this machine ------
						transferAgentsPointer = setAgents4MigrationToMachine(transferAgents, load, no2Reach[1], transferAgentsPointer);
					}
				}// - end for -

				// ----------------------------------------------------------------------
				// --- If not all tranferAgent have new Locations -----------------------		
				if ( (transferAgentsPointer+1) < transferAgents.size() && lastNewLocation!=null){
					for (int i = transferAgentsPointer; i < transferAgents.size(); i++) {
						transferAgents.get(i).setNewLocation(lastNewLocation);
					}
				}
				
 				// ----------------------------------------------------------------------
				// --- Notify the agents that they have to migrate ----------------------
				if (transferAgents.size()>0) {
					System.out.println( "Start migration of " + transferAgents.size() + " agents ..." );
					this.setAgentMigration(transferAgents);
				}
			} 
			
		} else {
			// --------------------------------------------------------------------------
			// --- TODO: lets see .........
			// --------------------------------------------------------------------------
			
		}
	}	
	
	/**
	 * This method will distribute the given number of agents to the machine
	 * identified by the 'identifier' of 'load'.
	 *
	 * @param transferAgents the transfer agents
	 * @param loadOnMachine the load on machine
	 * @param no2MigrateSum the no to migrate in sum
	 * @param transferAgentsPointer the transfer agents pointer
	 * @return the number of agents to transfer  
	 */
	private int setAgents4MigrationToMachine(Vector<AID_Container>transferAgents, LoadMerger loadOnMachine, int no2MigrateSum, int transferAgentsPointer) {
		
		// --- Distribute the given number of agents to this machine --------------------		
		int noOfJVMs = loadOnMachine.getJvmList().size();
		int noOfAgentsPerJVM = no2MigrateSum / noOfJVMs;
		
		// --- 1. Take all JVMs into account --------------------------------------------
		Iterator<String> itJVM = loadOnMachine.getJvmList().iterator();
		while (itJVM.hasNext()) {
			String jvmPID = itJVM.next();
			int noOfContainer = loadJVM4Balancing.get(jvmPID).getContainerList().size();
			int noOfAgentsPerContainer = noOfAgentsPerJVM / noOfContainer;

			// --- 2. Run through all container in the JVM ------------------------------ 
			Iterator<String> itContainer = loadJVM4Balancing.get(jvmPID).getContainerList().iterator();
			while (itContainer.hasNext()) {
				String containerName = itContainer.next();
				Location newLocation = currContainerLoactions.get(containerName); 
				lastNewLocation = newLocation;
				
				// --- 3. Set the new locations in the transfer list --------------------
				for (int i=0; i<noOfAgentsPerContainer; i++) {
					if(transferAgents.size()-1 >= transferAgentsPointer) {
						transferAgents.get(transferAgentsPointer).setNewLocation(newLocation);
						transferAgentsPointer++;
					}
				}// --- end for ---
				
			} // --- end inner while ---
		}// --- end outer while ---
		return transferAgentsPointer;
	}
	
	/**
	 * This method selects agents which can be migrated from the machine
	 * identified by the 'identifier' of 'load'.
	 *
	 * @param transferAgents the transfer agents
	 * @param loadOnMachine the load on machine
	 * @param no2MigrateSum the no2 migrate sum
	 */
	private void selectAgents4MigrationFromMachine(Vector<AID_Container>transferAgents, LoadMerger loadOnMachine, int no2MigrateSum) {

		int no2MigrateFoundSum = 0;
		
		// --- 1. Look at all JVMs and evt. increase the number of migration ------------
		Iterator<String> itJVM = loadOnMachine.getJvmList().iterator();
		while (itJVM.hasNext()) {
			String jvmPID = itJVM.next();
			int noOfThreads = loadJVM4Balancing.get(jvmPID).getMachineLoad().getLoadNoThreads();
			if (noOfThreads>currThresholdLevels.getThNoThreadsH()) {
				no2MigrateSum += noOfThreads-currThresholdLevels.getThNoThreadsH();
			}
		}
		
		// --- 2. Get number of agents running in all container ------------------------- 
		Iterator<String> itContainer = loadOnMachine.getContainerList().iterator();
		while (itContainer.hasNext()) {
			
			String containerName = itContainer.next();
			// --- get List of agents ---
			AID_Container_List agentList = loadContainerAgentMap.getAgentsAtContainer().get(containerName);
			Iterator<String> itAgents = agentList.keySet().iterator();
			while (itAgents.hasNext()) {
				String agentName = itAgents.next();
				AID_Container agent = agentList.get(agentName);
				if (agent.hasServiceSensor()==true) {
					// --- Take this agent for the migration --------
					transferAgents.add(agent);
					no2MigrateFoundSum++;
					if (no2MigrateFoundSum>=no2MigrateSum) {
						return;	
					}
				}
				
			}// - end while -
		}// - end while -
	}

	
}
