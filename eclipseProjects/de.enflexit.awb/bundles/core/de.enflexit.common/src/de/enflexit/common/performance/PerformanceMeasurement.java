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

	private String taskDescriptor; 
	private PerformanceGroup performanceGroup;
	
	private int maxNumberForAverages = DEFAULT_MAX_NUMBER_FOR_AVERAGES;
	private int startCounter;

	// --- Variables for regular measurements -------------
	private long measurementStartTime = 0;
	private List<Long> measurementIntervalList;
	private double measurementIntervalAverage = 0;
	
	// --- Variables for loop measurements ----------------
	private Long loopMeasurementStartTime = null;
	private List<Long> loopTimeMeasurementList;
	
	private List<Integer> loopCountList;
	private List<Double> loopAverageList;
	
	private double loopCountsAverage = 0;
	private double loopMeasurementAverage = 0;
	
	
	
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
	
	
	/**
	 * Reduces the length of the specified list to the max number for averages 
	 * by removing the first element of the list as long as required.
	 * @param listToReduce the list to reduce
	 */
	private void reduceListSizeToMaxNumberForAverages(List<?> listToReduce) {
		while (listToReduce.size()>this.maxNumberForAverages) {
			listToReduce.remove(0);
		}
	}
	
	
	// ----------------------------------------------------
	// --- Regular measurements ---------------------------
	/**
	 * Sets the measurement started.
	 */
	public void setMeasurementStarted() {
		this.measurementStartTime = System.currentTimeMillis();
		this.startCounter++;
	}
	/**
	 * Return the list of measurements.
	 * @return the provision interval list
	 */
	private List<Long> getMeasurementIntervalList() {
		if (measurementIntervalList==null) {
			measurementIntervalList = new ArrayList<>();
		}
		return measurementIntervalList;
	}
	/**
	 * Updates the measurement interval average.
	 */
	private void updateMeasurementIntervalAverage() {

		// --- Reduce number of durations -------
		this.reduceListSizeToMaxNumberForAverages(this.getMeasurementIntervalList());

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
		this.measurementIntervalAverage = Math.round((((double)sum) / ((double)this.getMeasurementIntervalList().size())) * 10.0) / 10.0;
	}
	/**
	 * Returns the average measurement interval.
	 * @return the interval average of the measurement in ms
	 */
	public double getMeasurementIntervalAverage() {
		return measurementIntervalAverage;
	}
	
	
	// ----------------------------------------------------
	// --- Loop measurements ------------------------------
	/**
	 * Sets that a loop measurement was started.
	 */
	public void setLoopMeasurementStarted() {
		if (this.getLoopTimeMeasurementList().size()==0) {
			this.setMeasurementStarted();
		} else {
			if (this.loopMeasurementStartTime!=null) {
				long loopDuration = System.currentTimeMillis() - this.loopMeasurementStartTime;
				this.getLoopTimeMeasurementList().add(loopDuration);
			}
		}
		this.loopMeasurementStartTime = System.currentTimeMillis();
	}
	/**
	 * Sets that a measurement within a loop is finalized.
	 */
	public void setLoopMeasurementFinalized() {
		if (this.loopMeasurementStartTime!=null) {
			long loopDuration = System.currentTimeMillis() - this.loopMeasurementStartTime;
			this.getLoopTimeMeasurementList().add(loopDuration);
			this.loopMeasurementStartTime = null;
		}
	}
	
	/**
	 * Returns the loop time measurement list.
	 * @return the loop time measurement list
	 */
	private List<Long> getLoopTimeMeasurementList() {
		if (loopTimeMeasurementList==null) {
			loopTimeMeasurementList = new ArrayList<>();
		}
		return loopTimeMeasurementList;
	}
	/**
	 * Returns the list of loop counts.
	 * @return the loop counts
	 */
	private List<Integer> getLoopCountList() {
		if (loopCountList==null) {
			loopCountList = new ArrayList<>();
		}
		return loopCountList;
	}
	/**
	 * returns the list of average times for a loop measurement.
	 * @return the loop average times
	 */
	private List<Double> getLoopAverageList() {
		if (loopAverageList==null) {
			loopAverageList = new ArrayList<>();
		}
		return loopAverageList;
	}
	/**
	 * Updates the current loop average.
	 */
	private void updateLoopAverage() {
		
		int loopCounts = this.getLoopTimeMeasurementList().size();
		if (loopCounts==0) return;
		
		// --- Do calculations for all loop times ---------
		double loopAverage = 0.0;
		if (loopCounts==1) {
			// --- For single loop measurements -----------
			loopAverage = this.getLoopTimeMeasurementList().get(0);
			
		} else {
			// --- For multiple loop measurements ---------
			long sum = 0;
			for (int i = 0; i < this.getLoopTimeMeasurementList().size(); i++) {
				sum += this.getLoopTimeMeasurementList().get(i);
			}
			loopAverage = Math.round(((double)sum / (double)loopCounts) * 10.0) / 10.0;
		}
		
		// --- Update statistics --------------------------
		this.getLoopCountList().add(loopCounts);
		this.getLoopAverageList().add(loopAverage);
		
		// --- Reduce the list length ---------------------
		this.reduceListSizeToMaxNumberForAverages(this.getLoopCountList());
		this.reduceListSizeToMaxNumberForAverages(this.getLoopAverageList());
		
		// --- Summarize Loop Count List ------------------
		int sumLoopCounts = 0;
		for (int i = 0; i < this.getLoopCountList().size(); i++) {
			sumLoopCounts += this.getLoopCountList().get(i);
		}
		// --- Calculate new average ------------
		this.loopCountsAverage = Math.round(((double)sumLoopCounts / (double)this.getLoopCountList().size()) * 10.0) / 10.0;
		
		// --- Summarize Loop Count List ------------------
		double sumLoopTimeAverages = 0;
		for (int i = 0; i < this.getLoopAverageList().size(); i++) {
			sumLoopTimeAverages += this.getLoopAverageList().get(i);
		}
		// --- Calculate new average ------------
		this.loopMeasurementAverage = Math.round((sumLoopTimeAverages / (double)this.getLoopAverageList().size()) * 10.0) / 10.0;
	}
	/**
	 * Returns the loop counts average.
	 * @return the loop counts average
	 */
	public double getLoopCountsAverage() {
		return loopCountsAverage;
	}
	/**
	 * Returns the loop measurement average.
	 * @return the loop measurement average
	 */
	public double getLoopMeasurementAverage() {
		return loopMeasurementAverage;
	}
	
	// ----------------------------------------------------
	// --- Finalized regular and loop measurements -------- 
	/**
	 * Sets the measurement (regular and loop) finalized and updates the average value.
	 */
	public void setMeasurementFinalized() {
		
		// --- Calculate new average value ------
		long duration = System.currentTimeMillis() - this.measurementStartTime;
		this.getMeasurementIntervalList().add(duration);
		this.updateMeasurementIntervalAverage();
		
		// --- For loop measurements ------------
		this.setLoopMeasurementFinalized();
		if (this.getLoopTimeMeasurementList().size()>0) {
			this.updateLoopAverage();
			this.getLoopTimeMeasurementList().clear();
		}
		
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
	 * Returns the current measurement status information.
	 * @return the status information
	 */
	public String getMeasurementStatusInformation() {
		String info = "[" + this.getClass().getSimpleName() + "][" + this.taskDescriptor + "] AVG time: " + String.format("%5s", this.getMeasurementIntervalAverage())  + " ms";
		info += " (" + this.getMeasurementIntervalList().size() + " values)";
		if (this.getLoopCountList().size()>0) {
			info += ", AvG Number of Loops: " + this.getLoopCountsAverage() + ", AVG loop time: " + String.format("%4s", this.getLoopMeasurementAverage()) + " ms";
		}
		return info;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String myName = this.getClass().getSimpleName() + ": " + this.getTaskDescriptor();
		if (this.getLoopCountList().size()>0) myName += " Loop Measurement";
		if (this.getPerformanceGroup()!=null) myName += " (Group: " + this.getPerformanceGroup().getGroupName() + ")";
		return myName;
	}

	
}
