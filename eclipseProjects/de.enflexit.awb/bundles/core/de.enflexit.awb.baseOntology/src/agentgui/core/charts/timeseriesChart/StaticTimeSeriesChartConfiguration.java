package agentgui.core.charts.timeseriesChart;

/**
 * The Class StaticTimeSeriesChartConfiguration.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class StaticTimeSeriesChartConfiguration {

	
	// ------------------------------------------------------------------------
	// --- TimeSeriesLengthRestriction ----------------------------------------
	// ------------------------------------------------------------------------
	private static TimeSeriesLengthRestriction timeSeriesLengthRestriction;
	
	/**
	 * Sets the time series length restriction.
	 * @param newTimeSeriesLengthRestriction the new time series length restriction
	 */
	public static void setTimeSeriesLengthRestriction(TimeSeriesLengthRestriction newTimeSeriesLengthRestriction) {
		timeSeriesLengthRestriction = newTimeSeriesLengthRestriction;
	}
	/**
	 * Gets the time series length restriction.
	 * @return the time series length restriction
	 */
	public static TimeSeriesLengthRestriction getTimeSeriesLengthRestriction() {
		return timeSeriesLengthRestriction;
	}
	
	
	// ------------------------------------------------------------------------
	// --- StaticTimeSeriesSettingEvaluator -----------------------------------
	// ------------------------------------------------------------------------
	private static StaticTimeSeriesSettingEvaluator staticTimeSeriesSettingEvaluator;
	
	/**
	 * Returns the current static time series setting evaluator.
	 * @return the static time series setting evaluator
	 */
	public static StaticTimeSeriesSettingEvaluator getStaticTimeSeriesSettingEvaluator() {
		return staticTimeSeriesSettingEvaluator;
	}
	/**
	 * Sets the static time series setting evaluator.
	 * @param staticTimeSeriesSettingEvaluator the new static time series setting evaluator
	 */
	public static void setStaticTimeSeriesSettingEvaluator(StaticTimeSeriesSettingEvaluator staticTimeSeriesSettingEvaluator) {
		StaticTimeSeriesChartConfiguration.staticTimeSeriesSettingEvaluator = staticTimeSeriesSettingEvaluator;
	}
	
	/**
	 * Returns a start time for ....
	 * @return the start time
	 */
	public static Long getStartTime() {
		return getStaticTimeSeriesSettingEvaluator()==null ? null : getStaticTimeSeriesSettingEvaluator().getStartTime();
	}
	/**
	 * Returns a start time for ....
	 * @return the start time
	 */
	public static String getTimeFormatPattern() {
		return getStaticTimeSeriesSettingEvaluator()==null ? null : getStaticTimeSeriesSettingEvaluator().getTimeFormatPattern();
	}
	
}
