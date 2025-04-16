package agentgui.core.charts.timeseriesChart;

/**
 * The Interface StaticTimeSeriesSettingEvaluator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface StaticTimeSeriesSettingEvaluator {

	/**
	 * Has to return a start time.
	 * @return the time start
	 */
	public Long getStartTime();
	
	/**
	 * Has to return a time format pattern.
	 * @return the time format pattern
	 */
	public String getTimeFormatPattern();
	
}
