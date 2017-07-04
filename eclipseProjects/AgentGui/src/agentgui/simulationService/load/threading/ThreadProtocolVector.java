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
package agentgui.simulationService.load.threading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.xml.bind.annotation.XmlRootElement;

import agentgui.core.application.Application;
import agentgui.core.project.Project;
import agentgui.core.sim.setup.AgentClassElement4SimStart;
import agentgui.core.sim.setup.SimulationSetup;


/**
 * The Class ThreadProtocolVector is used to handle several {@link ThreadProtocol} instances.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
@XmlRootElement
public class ThreadProtocolVector extends Vector<ThreadProtocol> {

	private static final long serialVersionUID = -6007682527796979437L;

	
	/** The agent start hash map reminder. */
	private HashMap<String, AgentClassElement4SimStart> agentStartHashMapReminder;
	
	/** The table model. */
	private DefaultTableModel tableModel;

	
	/**
	 * Instantiates a new thread protocol vector.
	 */
	public ThreadProtocolVector() {
		super();
	}
	
	/**
	 * Gets the agent start list of the current {@link SimulationSetup}.
	 * @return the agent start list
	 */
	private ArrayList<AgentClassElement4SimStart> getAgentListFromProjectSetup() {
		Project project = Application.getProjectFocused();
		if (project!=null) {
			// --- Get Agent list from simulation setup -------------
			SimulationSetup simSetup = project.getSimulationSetups().getCurrSimSetup();
			if (simSetup!=null) {
				return simSetup.getAgentList();
			}
		}
		return null;
	}
	/**
	 * Gets the agent start hash map reminder.
	 * @return the agent start hash map reminder
	 */
	private HashMap<String, AgentClassElement4SimStart> getAgentStartHashMapReminder() {
		if (agentStartHashMapReminder==null) {
			agentStartHashMapReminder = new HashMap<String, AgentClassElement4SimStart>();
		}
		return agentStartHashMapReminder;
	}
	/**
	 * Sets the agent start hash map reminder.
	 * @param agentStartHashMap the agent start hash map
	 */
	private void setAgentStartHashMapReminder(HashMap<String, AgentClassElement4SimStart> agentStartHashMap) {
		this.agentStartHashMapReminder = agentStartHashMap;
	}
	/**
	 * Returns the agent start list as hash map, in order to accelerate the access.
	 * @return the agent hash map
	 */
	public HashMap<String, AgentClassElement4SimStart> getAgentStartHashMap() {
		
		ArrayList<AgentClassElement4SimStart> agentStartList = this.getAgentListFromProjectSetup();
		if (agentStartList!=null) {
			// ------------------------------------------------------
			// --- Check the size of the reminded HashMap -----------
			// ------------------------------------------------------
			if (agentStartList.size()!=this.getAgentStartHashMapReminder().size()) {
				// --- Number of elements are different: Rebuild ----
				HashMap<String, AgentClassElement4SimStart> agentStartHashMap = new HashMap<String, AgentClassElement4SimStart>();
				for (int i = 0; i < agentStartList.size(); i++) {
					AgentClassElement4SimStart ace4ss = agentStartList.get(i); 
					agentStartHashMap.put(ace4ss.getStartAsName(), ace4ss);
				}
				// --- Remind ---------------------------------------
				this.setAgentStartHashMapReminder(agentStartHashMap);
			}
			return this.getAgentStartHashMapReminder();
			// ------------------------------------------------------
		}
		return null;
	}
	
	/**
	 * Gets the table model for this {@link ThreadProtocolVector}.
	 * @return the table model
	 */
	public DefaultTableModel getTableModel() {
		
		if (tableModel==null) {
			
			Vector<String> header = new Vector<String>();
			header.add("PID");
			header.add("Thread");
			header.add("Class");
			header.add("System Time [ms]");
			header.add("User Time [ms]");
			
			tableModel = new DefaultTableModel(null, header){

				private static final long serialVersionUID = 1L;

				/* (non-Javadoc)
				 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
				 */
				public Class<?> getColumnClass(int column){
					if (column >= 0 && column <= getColumnCount()) {
						return getValueAt(0, column).getClass();
					} else {
						return Object.class;
					}
				}
			};
			// --- Necessary for preventing sorter from throwing error about empty row
			addTableModelRow("", null);
		}
		return tableModel;
	}
	
	
	/**
	 * Adds a table model row.
	 * @param pid the process ID
	 * @param threadDetail the thread time
	 */
	private void addTableModelRow(String pid, ThreadDetail threadDetail) {
		
		if (threadDetail == null) {
			threadDetail = new ThreadDetail();
		}
		
		// --- Check for agent class out of the setup start-list ----
		if (threadDetail.isAgent()==true) {
			HashMap<String, AgentClassElement4SimStart> agentStartHashMap = this.getAgentStartHashMap();
			if (agentStartHashMap!=null) {
				AgentClassElement4SimStart ace4ss = agentStartHashMap.get(threadDetail.getThreadName());
				if (ace4ss!=null) {
					threadDetail.setClassName(ace4ss.getAgentClassReference());
				}
			}
		}
		
		String[] className = threadDetail.getClassName().split("\\.");
		// --- Create row vector ------------------------------------
		Vector<Object> row = new Vector<Object>();
		row.add(pid);
		row.add(threadDetail);
		row.add(className[className.length-1]);
		row.add(threadDetail.getSystemTime());
		row.add(threadDetail.getUserTime());
		
		// --- Add row to table model -------------------------------
		this.getTableModel().addRow(row);
	}
	
	/**
	 * Clears the table model.
	 */
	private void clearTableModel() {
		while (this.getTableModel().getRowCount()>0) {
			this.getTableModel().removeRow(0);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.Vector#add(java.lang.Object)
	 */
	@Override
	public synchronized boolean add(ThreadProtocol threadProtocol) {
		// --- Add to the local vector ------------------------------
		boolean done = super.add(threadProtocol);
		// --- Add the new Thread Times to the table model ----------
		String pid = threadProtocol.getProcessID();
		for (int i = 0; i < threadProtocol.getThreadDetails().size(); i++) {
			this.addTableModelRow(pid, threadProtocol.getThreadDetails().get(i));
		}
		return done;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Vector#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		this.clearTableModel();
	}
	
	/**
	 * Returns the current time stamp of this protocol vector.
	 * @return the time stamp
	 */
	public long getTimestamp() {
		if (this.size()==0) {
			return 0; 
		} else {
			return this.get(0).getTimestamp();
		}
	}
}
