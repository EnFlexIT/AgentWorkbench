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
package agentgui.simulationService.time;

import java.util.HashMap;

import agentgui.core.project.Project;


/**
 * This is a discrete time model, which can either start from 0 or 
 * from a specified time. Additionally the step width in time has to 
 * be configured in order to allow a discrete temporal progression.  
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelDiscrete extends TimeModel {

	private static final long serialVersionUID = 3931340225354221294L;
	
	public final static String PROP_TimeCurrent = "TimeCurrent";
	public final static String PROP_TimeStart = "TimeStart";
	public final static String PROP_TimeStop = "TimeStop";
	public final static String PROP_StepWidth = "StepWidth";
	public final static String PROP_DisplayUnitIndex = "DisplayUnitIndex";
	
	private long timeStart = System.currentTimeMillis();
	private long timeStop = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
	private long time = new Long(0);
	private long step = new Long(1000 * 60);
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
	 * @param stepInTime the step width in time
	 * @param startTime the start time
	 */
	public TimeModelDiscrete(long startTime, long stopTime, long stepInTime) {
		this.timeStart = startTime;
		this.timeStop = stopTime;
		this.step = stepInTime;
		
		this.time = this.timeStart;
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
	 * Sets the start time .
	 * @param timeStart the new start time
	 */
	public void setTimeStart(long timeStart) {
		this.timeStart = timeStart;
	}
	/**
	 * Gets the start time.
	 * @return the start time 
	 */
	public long getTimeStart() {
		return timeStart;
	}
	
	/**
	 * Sets the stop time.
	 * @param timeStop the new stop time
	 */
	public void setTimeStop(long timeStop) {
		this.timeStop = timeStop;
	}
	/**
	 * Gets the stop time.
	 * @return the stop time 
	 */
	public long getTimeStop() {
		return timeStop;
	}
	
	/**
	 * Sets the current time of the model.
	 * @param counter the counter to set
	 */
	public void setTime(long counter) {
		this.time = counter;
	}
	/**
	 * Returns the time as long.
	 * @return the time
	 */
	public long getTime() {
		return time;
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
	 * @see agentgui.simulationService.time.TimeModel#getJPanel4Configuration()
	 */
	@Override
	public DisplayJPanel4Configuration getJPanel4Configuration(Project project) {
		return new TimeModelDiscreteConfiguration(project);
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModel#setSetupConfiguration(java.util.HashMap)
	 */
	@Override
	public void setTimeModelSettings(HashMap<String, String> timeModelSettings) {
		
		try {
			String stringTimeCurrent = timeModelSettings.get(PROP_TimeCurrent);
			String stringTimeStart = timeModelSettings.get(PROP_TimeStart);
			String stringTimeStop = timeModelSettings.get(PROP_TimeStop);
			String stringStepWidth = timeModelSettings.get(PROP_StepWidth);
			String stringDisplayUnitIndex = timeModelSettings.get(PROP_DisplayUnitIndex);

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
	
		} catch (Exception ex) {
			System.err.println("Error while converting TimeModel settings from setup");
		}
				
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModel#getSetupConfiguration()
	 */
	@Override
	public HashMap<String, String> getTimeModelSetting() {
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put(PROP_TimeCurrent, ((Long) this.time).toString());
		hash.put(PROP_TimeStart, ((Long) this.timeStart).toString());
		hash.put(PROP_TimeStop, ((Long) this.timeStop).toString());
		hash.put(PROP_StepWidth, ((Long) this.step).toString());
		hash.put(PROP_DisplayUnitIndex, ((Integer) this.stepDisplayUnitAsIndexOfTimeUnitVector).toString());
		return hash;
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModel#getJToolBar4Execution()
	 */
	@Override
	public DisplayJToolBar4Execution getJToolBar4Execution() {
		return null;
	}
}