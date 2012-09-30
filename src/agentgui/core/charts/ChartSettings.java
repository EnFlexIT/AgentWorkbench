package agentgui.core.charts;

import java.awt.Color;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import agentgui.ontology.Chart;

/**
 * This class manages the chart settings.
 * @author Nils
 *
 */
public class ChartSettings extends Observable{
	private String chartTitle;
	private String xAxisLabel;
	private String yAxisLabel;
	private String rendererType;
	private List<SeriesSettings> seriesSettings;
	
	public ChartSettings(Chart chart){
		chartTitle = chart.getVisualizationSettings().getChartTitle();
		xAxisLabel = chart.getVisualizationSettings().getXAxisLabel();
		yAxisLabel = chart.getVisualizationSettings().getYAxisLabel();
		rendererType = chart.getVisualizationSettings().getRendererType();
		
		seriesSettings = new Vector<SeriesSettings>();
	}

	public void addSeriesSettings(SeriesSettings settings){
		seriesSettings.add(settings);
		setChanged();
		SettingsInfo changeInfo = new SettingsInfo(SettingsInfo.SERIES_ADDED, settings);
		notifyObservers(changeInfo);
	}
	
	public void removeSeriesSettings(int seriesIndex){
		if(seriesIndex < seriesSettings.size()){
			seriesSettings.remove(seriesIndex);
			setChanged();
			SettingsInfo changeInfo = new SettingsInfo(SettingsInfo.SERIES_REMOVED, seriesIndex, null);
			notifyObservers(changeInfo);
		}
	}

	/**
	 * @return the chartTitle
	 */
	public String getChartTitle() {
		return chartTitle;
	}

	/**
	 * @param chartTitle the chartTitle to set
	 */
	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
		SettingsInfo changeInfo = new SettingsInfo(SettingsInfo.CHART_TITLE_CHANGED, chartTitle);
		setChanged();
		notifyObservers(changeInfo);
	}

	/**
	 * @return the xAxisLabel
	 */
	public String getxAxisLabel() {
		return xAxisLabel;
	}

	/**
	 * @param xAxisLabel the xAxisLabel to set
	 */
	public void setxAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
		SettingsInfo changeInfo = new SettingsInfo(SettingsInfo.X_AXIS_LABEL_CHANGED, xAxisLabel);
		setChanged();
		notifyObservers(changeInfo);
	}

	/**
	 * @return the yAxisLabel
	 */
	public String getyAxisLabel() {
		return yAxisLabel;
	}

	/**
	 * @param yAxisLabel the yAxisLabel to set
	 */
	public void setyAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
		SettingsInfo changeInfo = new SettingsInfo(SettingsInfo.Y_AXIS_LABEL_CHANGED, yAxisLabel);
		setChanged();
		notifyObservers(changeInfo);
	}

	/**
	 * @return the rendererType
	 */
	public String getRendererType() {
		return rendererType;
	}

	/**
	 * @param rendererType the rendererType to set
	 */
	public void setRendererType(String rendererType) {
		this.rendererType = rendererType;
		SettingsInfo changeInfo = new SettingsInfo(SettingsInfo.RENDERER_CHANGED, rendererType);
		setChanged();
		notifyObservers(changeInfo);
	}

	/**
	 * @return the seriesSettings
	 */
	public List<SeriesSettings> getSeriesSettings() {
		return seriesSettings;
	}
	
	public SeriesSettings getSeriesSettings(int seriesIndex) throws NoSuchSeriesException{
		if(seriesIndex < seriesSettings.size()){
			return seriesSettings.get(seriesIndex);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	public void setSeriesLabel(int seriesIndex, String label) throws NoSuchSeriesException{
		if(seriesIndex < seriesSettings.size()){
			seriesSettings.get(seriesIndex).setLabel(label);
			SettingsInfo changeInfo = new SettingsInfo(SettingsInfo.SERIES_LABEL_CHANGED, seriesIndex, label);
			setChanged();
			notifyObservers(changeInfo);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	public void setSeriesColor(int seriesIndex, Color color) throws NoSuchSeriesException{
		if(seriesIndex < seriesSettings.size()){
			seriesSettings.get(seriesIndex).setColor(color);
			SettingsInfo changeInfo = new SettingsInfo(SettingsInfo.SERIES_COLOR_CHANGED, seriesIndex, color);
			setChanged();
			notifyObservers(changeInfo);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	public void setSeriesLineWidth(int seriesIndex, float lineWidth) throws NoSuchSeriesException{
		if(seriesIndex < seriesSettings.size()){
			seriesSettings.get(seriesIndex).setLineWIdth(lineWidth);
			SettingsInfo changeInfo = new SettingsInfo(SettingsInfo.SERIES_LINE_WIDTH_CHANGED, seriesIndex, lineWidth);
			setChanged();
			notifyObservers(changeInfo);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	
}
