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

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class ThreadProtocolVector is used to handle several {@link ThreadProtocol} instances.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
@XmlRootElement
public class ThreadProtocolVector extends Vector<ThreadProtocol> {

	private static final long serialVersionUID = -6007682527796979437L;

	private String simulationSetup;
	private DefaultTableModel tableModel;
	
	/**
	 * Instantiates a new thread protocol vector.
	 */
	public ThreadProtocolVector() {
	}
	/**
	 * Instantiates a new thread protocol vector.
	 * @param simulationSetup the simulation setup
	 */
	public ThreadProtocolVector(String simulationSetup) {
		this.setSimulationSetup(simulationSetup);
	}
	
	/**
	 * Gets the simulation setup.
	 * @return the simulation setup
	 */
	public String getSimulationSetup() {
		return simulationSetup;
	}
	/**
	 * Sets the simulation setup.
	 * @param simulationSetup the new simulation setup
	 */
	public void setSimulationSetup(String simulationSetup) {
		this.simulationSetup = simulationSetup;
	}
	
	/**
	 * Gets the table model for this {@link ThreadProtocolVector}.
	 * @return the table model
	 */
	public DefaultTableModel getTableModel() {
		
		if (this.tableModel==null) {
			
			Vector<String> header = new Vector<String>();
			header.add("PID");
			header.add("Thread Name");
			header.add("System Time");
			header.add("User Time");
			
			this.tableModel = new DefaultTableModel(null, header){

				private static final long serialVersionUID = 1L;

				/* (non-Javadoc)
				 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
				 */
				public Class<?> getColumnClass(int column){
			          if (column >= 0 && column <= getColumnCount())
			            return getValueAt(0, column).getClass();
			          else
			            return Object.class;
				}
			};
			//neccessary for preventing sorter from throwing error about empty row
			this.addTableModelRow("", new ThreadTime("", 0L, 0L));
		}
		return this.tableModel;
	}
	/**
	 * Adds a table model row.
	 * @param pid the process ID
	 * @param threadTime the thread time
	 */
	private void addTableModelRow(String pid, ThreadTime threadTime) {
		Vector<Object> row = new Vector<Object>();
		row.add(pid);
		row.add(threadTime.getThreadName());
		row.add(threadTime.getCpuTime());
		row.add(threadTime.getUserTime());
		
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
		for (int i = 0; i < threadProtocol.getThreadTimes().size(); i++) {
			this.addTableModelRow(pid, threadProtocol.getThreadTimes().get(i));
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
	
	/**
	 * Gets the Vector of all thread times.
	 * @return the thread times
	 */
	public Vector<ThreadTime> getThreadTimes() {
		Vector<ThreadTime> threadTimes = new Vector<ThreadTime>();
		for (int i = 0; i < this.size(); i++) {
			ThreadProtocol tp = this.get(i);
			threadTimes.addAll(tp.getThreadTimes());
		}
		return threadTimes;
	}
	
}
