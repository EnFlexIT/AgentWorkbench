package agentgui.core.config;

import agentgui.core.application.Application;
import agentgui.core.charts.timeseriesChart.StaticTimeSeriesChartConfiguration;
import agentgui.core.charts.timeseriesChart.StaticTimeSeriesSettingEvaluator;
import agentgui.core.project.Project;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelDateBased;

/**
 * The Class TimeSeriesSettingEvaluator enables to read settings from a current, date based {@link TimeModel} 
 * and used it within the {@link StaticTimeSeriesChartConfiguration}.
 * 
 * @see StaticTimeSeriesChartConfiguration
 * @see TimeModelDateBased
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TimeSeriesSettingEvaluator implements StaticTimeSeriesSettingEvaluator {

	/**
	 * Return the currently focused project, if available.
	 * @return the currently focused project or <code>null</code>
	 */
	private Project getProject() {
		return Application.getProjectFocused()==null ? null : Application.getProjectFocused();
	}
	/**
	 * Returns the current date based time model.
	 * @return the time model
	 */
	private TimeModelDateBased getDateBasedTimeModel() {
		
		Project project = this.getProject();
		if (project!=null) {
			TimeModel tm = project.getTimeModelController().getTimeModel();
			if (tm!=null && tm instanceof TimeModelDateBased) {
				return (TimeModelDateBased) tm;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.timeseriesChart.StaticTimeSeriesSettingEvaluator#getStartTime()
	 */
	@Override
	public Long getStartTime() {
		TimeModelDateBased tmdb = this.getDateBasedTimeModel();
		if (tmdb!=null) {
			return tmdb.getTimeStart();
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.timeseriesChart.StaticTimeSeriesSettingEvaluator#getTimeFormatPattern()
	 */
	@Override
	public String getTimeFormatPattern() {
		TimeModelDateBased tmdb = this.getDateBasedTimeModel();
		if (tmdb!=null) {
			return tmdb.getTimeFormat();
		}
		return null;
	}

}
