package de.enflexit.jade.phonebook.behaviours;

import java.util.Arrays;
import java.util.List;

public class IncreasingRetryIntervalsHelper {
	
	private List<Long> retryIntervals;
	private int currentRetryIntervalIndex;
	private int retryCounter;
	private int increaseAfter;
	
	/**
	 * Instantiates a new retry intervals helper with the default intervals, increased every five tries.
	 */
	public IncreasingRetryIntervalsHelper() {
		this(5);
	}
	
	/**
	 * Instantiates a new retry intervals helper with the default intervals, increased after the specified number of tries.
	 * @param increaseAfter the number of tries to increase after
	 */
	public IncreasingRetryIntervalsHelper(int increaseAfter) {
		this(getDefaultIntervals(), increaseAfter);
	}

	/**
	 * Instantiates a new retry intervals helper with the default intervals, increased after the specified number of tries.
	 * @param retryIntervals the retry intervals
	 * @param increaseAfter the increase after
	 */
	public IncreasingRetryIntervalsHelper(List<Long> retryIntervals, int increaseAfter) {
		this.retryIntervals = retryIntervals;
		this.increaseAfter = increaseAfter;
	}
	
	/**
	 * Gets the default intervals - 5s, 10s, 30s, 1m, 10m, 30m, 1h.
	 * @return the default intervals
	 */
	private static final List<Long> getDefaultIntervals() {
		return Arrays.asList(5000L, 10000L, 30000L, 60000L, 600000L, 1800000L, 3600000L);
	}

	/**
	 * Gets the retry intervals.
	 * @return the retry intervals
	 */
	public List<Long> getRetryIntervals() {
		if (retryIntervals==null) {
			// --- Initialize with the default intervals
			this.retryIntervals = getDefaultIntervals();
		}
		return retryIntervals;
	}
	
	/**
	 * Sets custom retry intervals.
	 * @param retryIntervals the custom retry intervals
	 */
	public void setRetryIntervals(List<Long> retryIntervals) {
		this.retryIntervals = retryIntervals;
	}
	
	/**
	 * Gets the increase after.
	 * @return the increase after
	 */
	public int getIncreaseAfter() {
		return increaseAfter;
	}

	/**
	 * Sets the increase after.
	 * @param increaseAfter the new increase after
	 */
	public void setIncreaseAfter(int increaseAfter) {
		this.increaseAfter = increaseAfter;
	}
	
	/**
	 * Gets the current retry interval, which is increased every 5 tries.
	 * @return the current retry interval
	 */
	public long getCurrentRetryInterval() {
		long retryInterval = this.getRetryIntervals().get(currentRetryIntervalIndex);
		this.retryCounter++;
		
		// --- Check if the interval is below the maximum -----------
		if (this.currentRetryIntervalIndex<this.getRetryIntervals().size()-1) {
			// --- Switch to next interval every increaseAfter tries  
			if (this.retryCounter%increaseAfter==0) {
				this.currentRetryIntervalIndex++;
			}
		}
		return retryInterval;
	}
	
	/**
	 * Gets the current retry interval without increasing the counter.
	 * @return the current interval without increase
	 */
	public long getCurrentIntervalWithoutIncrease() {
		return this.getRetryIntervals().get(this.currentRetryIntervalIndex);
	}
	
	/**
	 * Resets the interval index and retry counter.
	 */
	public void reset() {
		this.currentRetryIntervalIndex = 0;
		this.retryCounter = 0;
	}
}
