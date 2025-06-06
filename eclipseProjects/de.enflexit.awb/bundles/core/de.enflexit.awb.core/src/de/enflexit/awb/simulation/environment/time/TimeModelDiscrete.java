package de.enflexit.awb.simulation.environment.time;

import java.time.ZoneId;
import java.util.HashMap;

import de.enflexit.awb.core.ui.AwbTimeModelConfigurationWidget;

/**
 * This is a discrete time model, which can either start from 0 or 
 * from a specified time. Additionally the step width in time has to 
 * be configured in order to allow a discrete temporal progression.  
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelDiscrete extends TimeModelDateBased {

	private static final long serialVersionUID = 3931340225354221294L;
	
	public final static String PROP_TimeCurrent = "TimeCurrent";
	public final static String PROP_TimeStart = "TimeStart";
	public final static String PROP_TimeStop = "TimeStop";
	public final static String PROP_StepWidth = "StepWidth";
	public final static String PROP_DisplayUnitIndex = "DisplayUnitIndex";
	public final static String PROP_ZoneId = "ZoneId";
	public final static String PROP_TimeFormat = "TimeFormat";
	
	private long time = timeStart;
	private long step = Long.valueOf(1000 * 60);
	private int stepDisplayUnitAsIndexOfTimeUnitVector = 1;
	
	/**
	 * Instantiates a new time model discrete.
	 */
	public TimeModelDiscrete() {
		
	}
	/**
	 * Instantiates a new time model discrete.
	 * @param stepInTime the step width in time
	 */
	public TimeModelDiscrete(long stepInTime) {
		this.step = stepInTime;
	}
	
	/**
	 * Instantiates a new time model discrete.
	 *
	 * @param startTime the start time
	 * @param stopTime the stop time
	 * @param stepInTime the step width in time
	 */
	public TimeModelDiscrete(long startTime, long stopTime, long stepInTime) {
		this.timeStart = startTime;
		this.timeStop = stopTime;
		this.step = stepInTime;
		
		this.time = this.timeStart;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModel#getCopy()
	 */
	@Override
	public TimeModel getCopy() {
		TimeModelDiscrete tmd = new TimeModelDiscrete();
		tmd.setTimeStart(this.timeStart);
		tmd.setTimeStop(this.timeStop);
		tmd.setTime(this.time);
		tmd.setStep(this.step);
		tmd.setTimeFormat(this.timeFormat);
		tmd.setZoneId(this.zoneId);
		return tmd;
	}
	
	/**
	 * Sets the current time of the model.
	 * @param counter the counter to set
	 */
	public void setTime(long counter) {
		this.time = counter;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModelDateBased#getTime()
	 */
	@Override
	public long getTime() {
		return time;
	}
	
	/**
	 * Steps the time with the given time step.
	 */
	@Override
	public void step() {
		this.time = this.time + this.step;
	}
	/**
	 * Steps back the time with the given time step.
	 */
	@Override
	public void stepBack() {
		this.time = this.time - this.step;		
	}
	
	/**
	 * Returns the step width as long.
	 * @return the step
	 */
	public long getStep() {
		return step;
	}
	/**
	 * Sets the step width.
	 * @param step the step width as long
	 */
	public void setStep(long step) {
		this.step = step;
	}

	/**
	 * Sets the step display unit as index of the TimeUnitVector.
	 * @see TimeUnitVector
	 * @param stepDisplayUnitAsIndexOfTimeUnitVector the new step display unit as index of the TimeUnitVector
	 */
	public void setStepDisplayUnitAsIndexOfTimeUnitVector(int stepDisplayUnitAsIndexOfTimeUnitVector) {
		this.stepDisplayUnitAsIndexOfTimeUnitVector = stepDisplayUnitAsIndexOfTimeUnitVector;
	}
	/**
	 * Returns the step display unit as index of the TimeUnitVector.
	 * @see TimeUnitVector
	 * @return the step display unit as index of the TimeUnitVector
	 */
	public int getStepDisplayUnitAsIndexOfTimeUnitVector() {
		return stepDisplayUnitAsIndexOfTimeUnitVector;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.environment.time.TimeModel#getConfigurationWidget()
	 */
	@Override
	public AwbTimeModelConfigurationWidget getConfigurationWidget() {
		return new TimeModelDiscreteConfiguration();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModel#getJToolBar4Execution()
	 */
	@Override
	public TimeModelBaseExecutionElements getDisplayElements4Execution() {
		return new TimeModelDiscreteExecutionElements();
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
				this.time = Long.valueOf(0);;
				this.step = Long.valueOf(1000 * 60);
				this.stepDisplayUnitAsIndexOfTimeUnitVector = 1;
				this.timeFormat = TimeModelDateBased.DEFAULT_TIME_FORMAT;
				return;
			}
			
			String stringTimeCurrent = timeModelSettings.get(PROP_TimeCurrent);
			String stringTimeStart = timeModelSettings.get(PROP_TimeStart);
			String stringTimeStop = timeModelSettings.get(PROP_TimeStop);
			String stringStepWidth = timeModelSettings.get(PROP_StepWidth);
			String stringDisplayUnitIndex = timeModelSettings.get(PROP_DisplayUnitIndex);
			String stringZoneID = timeModelSettings.get(PROP_ZoneId);
			String stringTimeFormat = timeModelSettings.get(PROP_TimeFormat);

			if (stringTimeCurrent!=null) {
				this.time = Long.parseLong(stringTimeCurrent);	
			}
			if (stringTimeStart!=null) {
				this.timeStart = Long.parseLong(stringTimeStart);	
			}
			if (stringTimeStop!=null) {
				this.timeStop = Long.parseLong(stringTimeStop);	
			}
			if (stringStepWidth!=null) {
				this.step = Long.parseLong(stringStepWidth);	
			}
			if (stringDisplayUnitIndex!=null) {
				this.stepDisplayUnitAsIndexOfTimeUnitVector = Integer.parseInt(stringDisplayUnitIndex);	
			}
			if (stringZoneID!=null) {
				this.zoneId = ZoneId.of(stringZoneID);
			}
			if (stringTimeFormat!=null) {
				this.timeFormat = stringTimeFormat;
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
		hash.put(PROP_TimeCurrent, ((Long) this.time).toString());
		hash.put(PROP_TimeStart, ((Long) this.timeStart).toString());
		hash.put(PROP_TimeStop, ((Long) this.timeStop).toString());
		hash.put(PROP_StepWidth, ((Long) this.step).toString());
		hash.put(PROP_DisplayUnitIndex, ((Integer) this.stepDisplayUnitAsIndexOfTimeUnitVector).toString());
		hash.put(PROP_ZoneId, this.getZoneId().getId());
		hash.put(PROP_TimeFormat, this.timeFormat);
		return hash;
	}

}