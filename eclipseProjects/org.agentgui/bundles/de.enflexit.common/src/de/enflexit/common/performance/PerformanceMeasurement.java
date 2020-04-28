package de.enflexit.common.performance;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class PerformanceMeasurement serves as as a measurement container for a specific 
 * topic / task (e.g. a 'round trip time').
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class PerformanceMeasurement {

	public static final int DEFAULT_MAX_NUMBER_FOR_AVERAGES = 16;

	// --- Variables for the measurement ----------------
	private String taskDescriptor; 
	
	private long measurementStartTime = 0;
	private List<Long> measurementIntervalList;
	private long measurementIntervalAverage = 0;
	
	private int maxNumberForAverages = DEFAULT_MAX_NUMBER_FOR_AVERAGES;
	private int startCounter;
	
	private PerformanceGroup performanceGroup;
	
	
	/**
	 * Instantiates a new simulation measurement.
	 * @param taskDescriptor the task descriptor
	 */
	public PerformanceMeasurement(String taskDescriptor) {
		this(taskDescriptor, null);
	}
	/**
	 * Instantiates a new simulation measurement.
	 *
	 * @param taskDescriptor the task descriptor
	 * @param maxNumberForAverages the max number for averages
	 */
	public PerformanceMeasurement(String taskDescriptor, Integer maxNumberForAverages) {
		this.setTaskDescriptor(taskDescriptor);
		if (maxNumberForAverages!=null && maxNumberForAverages>0) {
			this.maxNumberForAverages = maxNumberForAverages;
		}
	}
	
	/**
	 * Sets the task descriptor.
	 * @param taskDescriptor the new task descriptor
	 */
	public void setTaskDescriptor(String taskDescriptor) {
		this.taskDescriptor = taskDescriptor;
	}
	/**
	 * Return the task descriptor.
	 * @return the task descriptor
	 */
	public String getTaskDescriptor() {
		return taskDescriptor;
	}
	
	
	
	/**
	 * Sets the measurement started.
	 */
	public void setMeasurementStarted() {
		this.measurementStartTime = System.currentTimeMillis();
		this.startCounter++;
	}
	/**
	 * Sets the measurement finalized and updates the average value.
	 */
	public void setMeasurementFinalized() {
		long duration = System.currentTimeMillis() - this.measurementStartTime;
		this.getMeasurementIntervalList().add(duration);
		this.updateMeasurementIntervalAverage();
		// --- Print current average? -----------
		if (this.startCounter>=this.maxNumberForAverages) {
			this.startCounter = 0;
			if (this.getPerformanceGroup()!=null) {
				this.getPerformanceGroup().addNewAverageCalculated(this);
			} else {
				System.out.println(this.getMeasurementStatusInformation());
			}
		}
	}

	/**
	 * Return the list of provision interval list.
	 * @return the provision interval list
	 */
	private List<Long> getMeasurementIntervalList() {
		if (measurementIntervalList==null) {
			measurementIntervalList = new ArrayList<>();
		}
		return measurementIntervalList;
	}
	/**
	 * Updates the provision interval average.
	 */
	private void updateMeasurementIntervalAverage() {

		// --- Reduce number of durations -------
		while (this.getMeasurementIntervalList().size()>this.maxNumberForAverages) {
			this.getMeasurementIntervalList().remove(0);
		}
		// --- For the first measurement --------
		if (this.getMeasurementIntervalList().size()==1) {
			this.measurementIntervalAverage = this.getMeasurementIntervalList().get(0); 
			return;
		}
		// --- Summarize ------------------------
		long sum = 0;
		for (int i = 0; i < this.getMeasurementIntervalList().size(); i++) {
			sum += this.getMeasurementIntervalList().get(i);
		}
		// --- Calculate new average ------------
		this.measurementIntervalAverage = sum / this.getMeasurementIntervalList().size();
	}
	/**
	 * Returns the average measurement interval.
	 * @return the interval average of the measurement in ms
	 */
	public long getMeasurementIntervalAverage() {
		return measurementIntervalAverage;
	}
	
	
	/**
	 * Returns the current measurement status information.
	 * @return the status information
	 */
	public String getMeasurementStatusInformation() {
		return "[" + this.getClass().getSimpleName() + "][" + this.taskDescriptor + "] Average time: " + this.getMeasurementIntervalAverage() + " ms (based on " + this.getMeasurementIntervalList().size() + " measurements";
	}

	
	/**
	 * Sets the performance group to which this measurement belongs.
	 * @param performanceGroup the new performance group
	 */
	public void setPerformanceGroup(PerformanceGroup performanceGroup) {
		this.performanceGroup = performanceGroup;
	}
	/**
	 * Returns the performance group to which this measurement belongs.
	 * @return the performance group
	 */
	public PerformanceGroup getPerformanceGroup() {
		return performanceGroup;
	}

}
