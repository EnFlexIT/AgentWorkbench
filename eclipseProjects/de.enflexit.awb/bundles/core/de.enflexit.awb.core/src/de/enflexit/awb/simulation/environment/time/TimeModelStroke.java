package de.enflexit.awb.simulation.environment.time;

import java.util.HashMap;

import de.enflexit.awb.core.environment.TimeModelController;
import de.enflexit.awb.core.ui.AwbTimeModelConfigurationWidget;
import de.enflexit.awb.simulation.environment.EnvironmentModel;

/**
 * This is a stroke time model, which inherits just a simple counter and can
 * be used in an {@link EnvironmentModel}
 * 
 * @see TimeModelController
 * @see TimeModelStrokeConfiguration
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelStroke extends TimeModel {

	private static final long serialVersionUID = -63223704339241994L;

	public final static String PROP_CounterStart= "CounterStart";
	public final static String PROP_CounterStop = "CounterStop";
	public final static String PROP_CounterCurrent = "CounterCurrent";
	
	private int counterStart = 1;
	private int counterStop = 9999;
	private int counter = 0;
	
	
	/**
	 * Instantiates a new time model stroke.
	 */
	public TimeModelStroke() {
	}
	/**
	 * Instantiates a new time model stroke.
	 * @param currentCounterValue the position number the counter is currently set
	 */
	public TimeModelStroke(Integer currentCounterValue) {
		this.counter = currentCounterValue;
	}
	/**
	 * Instantiates a new time model stroke.
	 * @param counterValueStart the counter start value
	 * @param counterValueStop the counter stop value
	 */
	public TimeModelStroke(Integer counterValueStart, Integer counterValueStop) {
		this.counterStart = counterValueStart;
		this.counterStop = counterValueStop;
	}
		
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModel#getCopy()
	 */
	@Override
	public TimeModel getCopy() {
		TimeModelStroke tms = new TimeModelStroke();
		tms.setCounterStart(this.counterStart);
		tms.setCounterStop(this.counterStop);
		tms.setCounter(this.counter);
		return tms;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModel#step()
	 */
	@Override
	public void step() {
		counter++;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModel#stepBack()
	 */
	@Override
	public void stepBack() {
		counter--;
	}

	/**
	 * Sets the value at which the TimeModel starts.
	 * @param counterStart the new counter start
	 */
	public void setCounterStart(int counterStart) {
		this.counterStart = counterStart;
	}
	/**
	 * Returns the value at which the TimeModel starts.
	 * @return the counter start
	 */
	public int getCounterStart() {
		return counterStart;
	}
	
	/**
	 * Sets the value to stop the TimeModel.
	 * @param counterStop the new counter stop
	 */
	public void setCounterStop(int counterStop) {
		this.counterStop = counterStop;
	}
	/**
	 * Returns the value to stop the TimeModel.
	 * @return the counter stop
	 */
	public int getCounterStop() {
		return counterStop;
	}
	
	/**
	 * Sets the current counter.
	 * @param counter the counter to set
	 */
	public void setCounter(Integer counter) {
		this.counter = counter;
	}
	/**
	 * Returns the current counter.
	 * @return the counter
	 */
	public Integer getCounter() {
		return counter;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.environment.time.TimeModel#getConfigurationWidget()
	 */
	@Override
	public AwbTimeModelConfigurationWidget getConfigurationWidget() {
		return new TimeModelStrokeConfiguration();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModel#getJToolBar4Execution()
	 */
	@Override
	public TimeModelBaseExecutionElements getDisplayElements4Execution() {
		return new TimeModelStrokeExecutionElements();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModel#setSetupConfiguration(java.util.HashMap)
	 */
	@Override
	public void setTimeModelSettings(HashMap<String, String> timeModelSettings) {
		
		try {
			
			if (timeModelSettings.size()==0) {
				// --- Use Default values -----------------
				this.counterStart = 1;
				this.counterStop = 9999;
				this.counter = 0;
				return;
			}
			
			String stringCounterStart = timeModelSettings.get(PROP_CounterStart);
			String stringCounterStop = timeModelSettings.get(PROP_CounterStop);
			String stringCounter = timeModelSettings.get(PROP_CounterCurrent);

			if (stringCounterStart!=null) {
				this.counterStart = Integer.parseInt(stringCounterStart);	
			}
			if (stringCounterStop!=null) {
				this.counterStop = Integer.parseInt(stringCounterStop);	
			}
			if (stringCounter!=null) {
				this.counter = Integer.parseInt(stringCounter);	
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
		hash.put(PROP_CounterStart, ((Integer) this.counterStart).toString());
		hash.put(PROP_CounterStop, ((Integer) this.counterStop).toString());
		hash.put(PROP_CounterCurrent, ((Integer) this.counter).toString());
		return hash;
	}
	
}