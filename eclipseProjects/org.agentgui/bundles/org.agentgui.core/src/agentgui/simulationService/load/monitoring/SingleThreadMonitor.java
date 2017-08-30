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
package agentgui.simulationService.load.monitoring;

/**
 * The Class SingleThreadMonitor can be used to monitor any type of Thread.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SingleThreadMonitor extends AbstractMonitoringTask {

	private Thread threadToMonitor;
	private MonitoringMeasureType monitoringMeasureType;
	private Runnable measure;
	
	/**
	 * Instantiates a new thread monitor.
	 *
	 * @param threadToMonitor the thread to monitor
	 * @param monitoringMeasureType the actual MonitoringMeasureType
	 */
	public SingleThreadMonitor(Thread threadToMonitor, MonitoringMeasureType monitoringMeasureType) {
		this(threadToMonitor, monitoringMeasureType, null);
	}
	/**
	 * Instantiates a new thread monitor.
	 *
	 * @param threadToMonitor the thread to monitor
	 * @param monitoringMeasureType the actual MonitoringMeasureType
	 * @param runnable the runnable
	 */
	public SingleThreadMonitor(Thread threadToMonitor, MonitoringMeasureType monitoringMeasureType, Runnable runnable) {
		if (threadToMonitor==null) {
			throw new IllegalArgumentException("No Thread was specified for the monitoring task.");
		}
		if (monitoringMeasureType==null) {
			throw new IllegalArgumentException("No measure were specified in case of a thread failure.");
		}
		if (monitoringMeasureType==MonitoringMeasureType.RESTART_AGENT) {
			throw new IllegalArgumentException("An agents restart can not be defines by a " + this.getClass().getSimpleName() + ".");
		}
		if (monitoringMeasureType==MonitoringMeasureType.CUSTOM_MEASURE && runnable==null) {
			throw new IllegalArgumentException("No runnable was defined for the custom measure.");
		}
		this.threadToMonitor = threadToMonitor;
		this.monitoringMeasureType = monitoringMeasureType;
		this.measure = runnable;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.load.monitoring.AbstractMonitoringTask#isFaultlessProcess()
	 */
	@Override
	public boolean isFaultlessProcess() {
		return this.threadToMonitor.isAlive();
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.load.monitoring.AbstractMonitoringTask#getFaultMeasure()
	 */
	@Override
	public Runnable getFaultMeasure() {
		return this.measure;
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.load.monitoring.AbstractMonitoringTask#getMonitoringType()
	 */
	@Override
	public MonitoringType getMonitoringType() {
		return MonitoringType.THREAD_MONITORING;
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.load.monitoring.AbstractMonitoringTask#getMonitoringMeasureType()
	 */
	@Override
	public MonitoringMeasureType getMonitoringMeasureType() {
		return this.monitoringMeasureType;
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.load.monitoring.AbstractMonitoringTask#removeAfterMeasure()
	 */
	@Override
	public boolean removeTaskAfterMeasure() {
		return true;
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.load.monitoring.AbstractMonitoringTask#getTaskDescription()
	 */
	@Override
	public String getTaskDescription() {
		return "Monitoring of thread " + this.threadToMonitor.getName();
	}
	
}
