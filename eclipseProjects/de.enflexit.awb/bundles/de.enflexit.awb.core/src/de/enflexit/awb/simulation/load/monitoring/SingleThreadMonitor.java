package de.enflexit.awb.simulation.load.monitoring;

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
			throw new IllegalArgumentException("An agent restart can not be defined by an extended SingleThreadMonitor (see class: " + this.getClass().getSimpleName() + ").");
		}
		if (monitoringMeasureType==MonitoringMeasureType.CUSTOM_MEASURE && runnable==null) {
			throw new IllegalArgumentException("No runnable was defined for the custom measure.");
		}
		this.threadToMonitor = threadToMonitor;
		this.monitoringMeasureType = monitoringMeasureType;
		this.measure = runnable;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.load.monitoring.AbstractMonitoringTask#isFaultlessProcess()
	 */
	@Override
	public boolean isFaultlessProcess() {
		return this.threadToMonitor.isAlive();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.load.monitoring.AbstractMonitoringTask#getFaultMeasure()
	 */
	@Override
	public Runnable getFaultMeasure() {
		return this.measure;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.load.monitoring.AbstractMonitoringTask#getMonitoringType()
	 */
	@Override
	public MonitoringType getMonitoringType() {
		return MonitoringType.THREAD_MONITORING;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.load.monitoring.AbstractMonitoringTask#getMonitoringMeasureType()
	 */
	@Override
	public MonitoringMeasureType getMonitoringMeasureType() {
		return this.monitoringMeasureType;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.load.monitoring.AbstractMonitoringTask#removeAfterMeasure()
	 */
	@Override
	public boolean removeTaskAfterMeasure() {
		return true;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.load.monitoring.AbstractMonitoringTask#getTaskDescription()
	 */
	@Override
	public String getTaskDescription() {
		return "Monitoring of thread " + this.threadToMonitor.getName();
	}
	
}
