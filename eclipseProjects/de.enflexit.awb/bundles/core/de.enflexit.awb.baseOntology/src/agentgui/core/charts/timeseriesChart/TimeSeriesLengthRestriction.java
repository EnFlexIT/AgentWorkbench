package agentgui.core.charts.timeseriesChart;

/**
 * This class defines a length restriction for a time series, based on a duration and/or number of states.
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeSeriesLengthRestriction {

	private long maxDuration;
	private int maxNumberOfStates;
	
	/**
	 * Gets the max duration.
	 * @return the max duration
	 */
	public long getMaxDuration() {
		return maxDuration;
	}
	/**
	 * Sets the max duration.
	 * @param maxDuration the new max duration
	 */
	public void setMaxDuration(long maxDuration) {
		this.maxDuration = maxDuration;
	}
	
	/**
	 * Gets the max number of states.
	 * @return the max number of states
	 */
	public int getMaxNumberOfStates() {
		return maxNumberOfStates;
	}
	/**
	 * Sets the max number of states.
	 * @param maxNumberOfStates the new max number of states
	 */
	public void setMaxNumberOfStates(int maxNumberOfStates) {
		this.maxNumberOfStates = maxNumberOfStates;
	}
}
