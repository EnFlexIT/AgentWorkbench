package de.enflexit.awb.simulation.environment.time;

import java.time.ZoneId;
import java.util.HashMap;

import de.enflexit.awb.core.ui.AwbTimeModelConfigurationWidget;
import de.enflexit.awb.simulation.SimulationService;
import de.enflexit.awb.simulation.SimulationServiceHelper;
import jade.core.Agent;
import jade.core.ServiceException;

/**
 * The Class TimeModelContinuous.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelContinuous extends TimeModelDateBased {

	private static final long serialVersionUID = -6787156387409895035L;

	public final static String PROP_TimeStart = "TimeStart";
	public final static String PROP_TimeStop = "TimeStop";
	public final static String PROP_AccelerationFactor = "AccelerationFactor";
	public final static String PROP_ZoneId = "ZoneId";
	public final static String PROP_TimeFormat = "TimeFormat";
	
	private transient Agent timeAskingAgent = null;
	private transient SimulationServiceHelper simHelper = null;
	
	private boolean executed = false;
	private Long pauseTime = null;
	private long timeDiff = 0;

	private double accelerationFactor = 1.F;
	private long timeMeasuredAcceleratedLast = 0;
	private long timeMeasuredLast = 0;
	
	
	/**
	 * Instantiates a new continuous TimeModel .
	 */
	public TimeModelContinuous() { }
	
	/**
	 * Instantiates a new continuous TimeModel ..
	 *
	 * @param startTime the start time for the time model
	 * @param stopTime the stop time for the time model
	 */
	public TimeModelContinuous(Long startTime, Long stopTime) {
		this.setTimeStart(startTime);
		this.setTimeStop(stopTime);
	}
		
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModel#getCopy()
	 */
	@Override
	public TimeModel getCopy() {
		TimeModelContinuous tmc = new TimeModelContinuous();
		// ------------------------------------------------
		// --- Do this first to avoid side effects --------
		tmc.setExecuted(this.executed);
		tmc.setPauseTime(this.pauseTime);
		tmc.setTimeDiff(this.timeDiff);
		// --- Do this first to avoid side effects --------
		// ------------------------------------------------		
		tmc.setTimeStart(this.timeStart);
		tmc.setTimeStop(this.timeStop);
		tmc.setAccelerationFactor(this.accelerationFactor);
		tmc.setTimeFormat(this.timeFormat);
		tmc.setZoneId(this.zoneId);
		return tmc;
	}
	
	/**
	 * Gets the acceleration factor.
	 * @return the accelerationFactor
	 */
	public double getAccelerationFactor() {
		return accelerationFactor;
	}
	/**
	 * Sets the acceleration factor.
	 * @param accelerationFactor the accelerationFactor to set
	 */
	public void setAccelerationFactor(double accelerationFactor) {
		this.accelerationFactor = accelerationFactor;
	}

	/**
	 * Sets the time, where the time model was paused.
	 * @param pauseTime the new pause time
	 */
	private void setPauseTime(Long pauseTime) {
		this.pauseTime = pauseTime;
	}
	/**
	 * Returns the time, where the time model was paused.
	 * @return the pause time
	 */
	private Long getPauseTime() {
		return pauseTime;
	}
	
	/**
	 * Sets the time model to be executed in order to forward the time in the  model.
	 * In this method the platform time (provided by the SimulationService) will be used 
	 * to set the timeDiff.
	 *
	 * @param executeTimeModel the new executed
	 */
	public void setExecuted(boolean executeTimeModel) {
		
		if (this.getTimeAskingAgent()==null) {
			// --- Work on local time settings ----------------------
			if (executeTimeModel==true) {
				if (this.getPauseTime()==null) {
					this.setTimeDiff(this.getSystemTime() - this.getTimeStart());	
				} else {
					this.setTimeDiff(this.getSystemTime() - this.getPauseTime());
				}
			} else {
				this.setPauseTime(this.getTimeLocal());
			}
			
		} else {
			// --- Work on the synchronised platform time -----------
			if (executeTimeModel==true) {
				if (this.getPauseTime()==null) {
					this.setTimeDiff(this.getSystemTimeSynchronized() - this.getTimeStart());	
				} else {
					this.setTimeDiff(this.getSystemTimeSynchronized() - this.getPauseTime());
				}
			} else {
				this.setPauseTime(this.getTimePlatform());
			}
			
		}
		this.executed=executeTimeModel;		
	}
	/**
	 * Checks if this time model is executed and so, if time is moving forward.
	 * @return true, if is executed
	 */
	public boolean isExecuted() {
		return executed;
	}
	
	/**
	 * Sets the time difference.
	 * @param timeDiff the timeDiff to set
	 */
	private void setTimeDiff(long timeDiff) {
		this.timeDiff = timeDiff;
	}
	/**
	 * Returns the time difference.
	 * @return the timeDiff
	 */
	public long getTimeDiff() {
		return timeDiff;
	}
	
	/**
	 * Gets the accelerated time.
	 * @return the accelerated time
	 */
	private long getAcceleratedTime(long measuredTime) {
		
		long acceleratedTime = 0;
		if (this.timeMeasuredLast==0) this.timeMeasuredLast = measuredTime;
		if (this.timeMeasuredAcceleratedLast==0) this.timeMeasuredAcceleratedLast = measuredTime;
		
		if (this.accelerationFactor==1.000) {
			acceleratedTime = measuredTime;
			
		} else {
			// --- get the real time interval between now and -----------------
			// --- the last time where the time was measured ------------------
			long timeInterval = measuredTime - this.timeMeasuredLast;
			
			// --- get the new value to add to the last return value ----------
			double timeAdditionDouble = this.accelerationFactor * (double)timeInterval;
			long timeAddition = Math.round(timeAdditionDouble);

			// --- add the calculated value to the old value ------------------
			acceleratedTime = this.timeMeasuredAcceleratedLast + timeAddition;
			
		}

		this.timeMeasuredLast = measuredTime;
		this.timeMeasuredAcceleratedLast = acceleratedTime;
		return acceleratedTime;
		
	}
	
	/**
	 * Returns the current time depending on the simulation setup. If an agent was set to the instance 
	 * of the class the synchronized platform time will be used. If no agent is set, the time based 
	 * on the local system time will be returned  
	 * 
	 * @see #setTimeAskingAgent(Agent)
	 * @see SimulationServiceHelper#getSynchTimeMillis()
	 * 
	 * @return the time 
	 */
	@Override
	public long getTime() {
		long timeAnswer=0;
		if (this.getTimeAskingAgent()==null) {
			timeAnswer=this.getTimeLocal();
		} else {
			timeAnswer=this.getTimePlatform();
		}
		// --- Consider the factor of acceleration --------
		timeAnswer = this.getAcceleratedTime(timeAnswer);
		// --- Done ---------------------------------------
		return timeAnswer;
	}
	
	/**
	 * Returns the local time as long including the currently set timeDiff.
	 * @return the time
	 */
	private long getTimeLocal() {
		long currTime;
		if (isExecuted()==true) {
			currTime = this.getSystemTime() - this.getTimeDiff();
		} else {
			if (this.getPauseTime()==null) {
				currTime = this.getTimeStart();
			} else {
				currTime = this.getPauseTime();
			}
		}
		return currTime;
	}
	
	/**
	 * Returns the synchronized platform time, if available, or the local time including the timeDiff. 
	 * Platform time requires that JADE is running and that the SimulationService is available.
	 *
	 * @see SimulationServiceHelper#getSynchTimeMillis()
	 * 
	 * @return the time 
	 */
	private long getTimePlatform() {
		long currTime;
		if (isExecuted()==true) {
			currTime = this.getSystemTimeSynchronized() - this.getTimeDiff();
		} else {
			if (this.getPauseTime()==null) {
				currTime = this.getTimeStart();
			} else {
				currTime = this.getPauseTime();
			}
		}
		return currTime;
	}

	/**
	 * Returns the local system time. 
	 * @return the system time
	 */
	private long getSystemTime() {
		return System.currentTimeMillis();
	}
	
	/**
	 * Returns the system time, which is either the synchronized time of the SimulationService 
	 * or, in case of an inactive JADE platform, the time of the local machine. 
	 *
	 * @return the synchronized system time
	 * @see SimulationServiceHelper#getSynchTimeMillis()
	 */
	protected long getSystemTimeSynchronized() {
		long sysTime;
		if (this.getTimeAskingAgent()==null) {
			// --- Just take the local time -------------------------
			sysTime = System.currentTimeMillis();
			logTookLocalTime();
		} else {
			// --- Try to get the synchronized platform time --------
			try {
				sysTime = this.getSimulationServiceHelper().getSynchTimeMillis();
				
			} catch (ServiceException se) {
				System.err.println("Could not get synchronized platform time. Took local time!");
				sysTime = System.currentTimeMillis();
			}
		}
		return sysTime;
	}
	
	/**
	 * Outputs a message to the STDERR console, indicating that no load balancing agent was set and therefore the local time was taken
	 */
	protected void logTookLocalTime(){
		System.err.println("No agent was set. Took local time!");
	}
	
	/**
	 * Gets the agent.
	 * @return the agent
	 */
	public Agent getTimeAskingAgent() {
		return this.timeAskingAgent;
	}
	/**
	 * Sets the agent.
	 * @param askingAgent the new agent
	 */
	public void setTimeAskingAgent(Agent askingAgent) {
		this.timeAskingAgent = askingAgent;
	}
	
	/**
	 * Returns the SimulationServiceHelper that allows to connect to the SimulationService.
	 * @see SimulationService
	 * @see SimulationServiceHelper
	 * @return the SimulationServiceHelper
	 */
	private SimulationServiceHelper getSimulationServiceHelper() {
		
		if (this.getTimeAskingAgent()==null) {
			return null;
		}
		
		if (this.simHelper==null) {
			try {
				this.simHelper = (SimulationServiceHelper) this.getTimeAskingAgent().getHelper(SimulationService.NAME);
			} catch (ServiceException se) {
				System.err.println("Could not connect to SimulationService!");
			}
		}
		return this.simHelper;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModel#step()
	 */
	@Override
	public void step() {
		// --- nothing to do here -------------------------
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModel#stepBack()
	 */
	@Override
	public void stepBack() {
		// --- nothing to do here -------------------------
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.environment.time.TimeModel#getConfigurationWidget()
	 */
	@Override
	public AwbTimeModelConfigurationWidget getConfigurationWidget() {
		return new TimeModelContinuousConfiguration();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModel#getJToolBar4Execution()
	 */
	@Override
	public TimeModelBaseExecutionElements getDisplayElements4Execution() {
		return new TimeModelContinuousExecutionElements();
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModel#setSetupConfiguration(java.util.HashMap)
	 */
	@Override
	public void setTimeModelSettings(HashMap<String, String> timeModelSettings) {
		
		try {
			
			if (timeModelSettings.size()==0) {
				// --- Use Default values -----------------
				this.timeStart = this.getDateForMidnight(System.currentTimeMillis()).getTime();
				this.timeStop = this.timeStart + 1000 * 60 * 60 * 24;
				this.accelerationFactor = 1;
				this.timeFormat = TimeModelDateBased.DEFAULT_TIME_FORMAT;
				return;
			}
			
			String stringStartTime = timeModelSettings.get(PROP_TimeStart);
			String stringStopTime = timeModelSettings.get(PROP_TimeStop);
			String stringAccelerationFactor = timeModelSettings.get(PROP_AccelerationFactor);
			String stringZoneID = timeModelSettings.get(PROP_ZoneId);
			String stringTimeModelFormat= timeModelSettings.get(PROP_TimeFormat);
			
			if (stringStartTime!=null) {
				this.timeStart = Long.parseLong(stringStartTime);	
			}
			if (stringStopTime!=null) {
				this.timeStop = Long.parseLong(stringStopTime);	
			}
			if (stringAccelerationFactor!=null) {
				this.accelerationFactor = Float.parseFloat(stringAccelerationFactor);	
			}
			if (stringZoneID!=null) {
				this.zoneId = ZoneId.of(stringZoneID);
			}
			if (stringTimeModelFormat!=null) {
				this.timeFormat = stringTimeModelFormat;
			} else {
				this.timeFormat = TimeModelDateBased.DEFAULT_TIME_FORMAT;
			}
	
		} catch (Exception ex) {
			System.err.println("Error while converting TimeModel settings from setup");
		}
		
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModel#getSetupConfiguration()
	 */
	@Override
	public HashMap<String, String> getTimeModelSetting() {
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put(PROP_TimeStart, ((Long) this.timeStart).toString());
		hash.put(PROP_TimeStop, ((Long) this.timeStop).toString());
		hash.put(PROP_AccelerationFactor, ((Double) this.accelerationFactor).toString());
		hash.put(PROP_ZoneId, this.getZoneId().getId());
		hash.put(PROP_TimeFormat, this.timeFormat);
		return hash;
	}
	
}
