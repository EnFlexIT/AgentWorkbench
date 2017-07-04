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
package agentgui.core.charts;

import java.awt.Color;
import java.util.Observable;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import agentgui.core.application.Language;
import agentgui.ontology.DataSeries;

/**
 * The Class ChartSettingModel represents the special data model 
 * for the ChartSetting Tab.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class ChartSettingModel extends Observable{
	
	/**
	 * List of constants indicating changes of chart settings
	 */
	public enum EventType{
		TITLE_CHANGED, X_AXIS_LABEL_CHANGED, Y_AXIS_LABEL_CHANGED, RENDERER_CHANGED, TIME_FORMAT_CHANGED, SERIES_LABEL_CHANGED, SERIES_COLOR_CHANGED, SERIES_LINE_WIDTH_CHANGED
	}

	protected DataModel parentDataModel;
	
	private String chartTitle = null;
	private String chartXAxisLabel = null;
	private String chartYAxisLabel = null;
	private String renderType = null;
	
	private DefaultTableModel tableModelSeriesSettings = null;
	
	private Vector<ChartSettingModelListener> chartSettingModelListener = null;
	
	private boolean notificationsEnabled = true;
	
	/**
	 * Instantiates a new chart setting model.
	 * @param parentDataModel the parent data model
	 */
	public ChartSettingModel(DataModel parentDataModel) {
		this.parentDataModel = parentDataModel;
		this.refresh();
	}

	/**
	 * Sets the current instance according to the settings in the ontology.
	 */
	public void refresh() {
		this.notificationsEnabled = false;
		this.chartTitle = this.parentDataModel.getOntologyModel().getChartSettings().getChartTitle();
		this.chartXAxisLabel = this.parentDataModel.getOntologyModel().getChartSettings().getXAxisLabel();
		this.chartYAxisLabel = this.parentDataModel.getOntologyModel().getChartSettings().getYAxisLabel();
		this.renderType = this.parentDataModel.getOntologyModel().getChartSettings().getRendererType();
		this.updateSeriesList();
		this.informChartSettingModelListener();
		this.notificationsEnabled = true;
	}
	
	/**
	 * Gets the chart title.
	 * @return the chartTitle
	 */
	public String getChartTitle() {
		return chartTitle;
	}
	/**
	 * Sets a new chart title.
	 * @param newTitle the chartTitle to set
	 */
	public void setChartTitle(String newTitle) {
		this.chartTitle = newTitle;
		this.sendNotifications(EventType.TITLE_CHANGED, newTitle);
	}

	/**
	 * Gets the chart x axis label.
	 * @return the chartXAxisLabel
	 */
	public String getChartXAxisLabel() {
		return chartXAxisLabel;
	}
	/**
	 * Sets the chart x axis label.
	 * @param newXAxisLabel the chartXAxisLabel to set
	 */
	public void setChartXAxisLabel(String newXAxisLabel) {
		this.chartXAxisLabel = newXAxisLabel;
		this.sendNotifications(EventType.X_AXIS_LABEL_CHANGED, newXAxisLabel);
	}

	/**
	 * Gets the chart y axis label.
	 * @return the chartYAxisLabel
	 */
	public String getChartYAxisLabel() {
		return chartYAxisLabel;
	}
	/**
	 * Sets the chart y axis label.
	 * @param newYAxisLabel the chartYAxisLabel to set
	 */
	public void setChartYAxisLabel(String newYAxisLabel) {
		this.chartYAxisLabel = newYAxisLabel;
		this.sendNotifications(EventType.Y_AXIS_LABEL_CHANGED, newYAxisLabel);
	}

	/**
	 * Returns the current render type.
	 * @return the renderer
	 */
	public String getRenderType() {
		return renderType;
	}
	/**
	 * Sets a new render type.
	 * @param newRendererType the renderer to set
	 */
	public void setRenderType(String newRendererType) {
		this.renderType = newRendererType;
//		this.parentDataModel.getOntologyModel().getChartSettings().setRendererType(newRendererType);

		this.sendNotifications(EventType.RENDERER_CHANGED, newRendererType);
	}
	
	/**
	 * Sets the series label for a data series, specified by its index 
	 * @param seriesIndex The series index
	 * @param newLabel The new series label
	 * @throws NoSuchSeriesException Invalid series index
	 */
	public void setSeriesLabel(int seriesIndex, String newLabel) throws NoSuchSeriesException{
		// Old version
//		this.parentDataModel.getOntologyModel().getSeries(seriesIndex).setLabel(newLabel);
//		this.parentDataModel.getTableModel().setSeriesLabel(seriesIndex, newLabel);
//		this.parentDataModel.getChartModel().setSeriesLabel(seriesIndex, newLabel);
		
		this.sendNotifications(EventType.SERIES_LABEL_CHANGED, seriesIndex, newLabel);
		
	}
	/**
	 * Sets the plot color for a data series, specified by its index 
	 * @param seriesIndex The series index
	 * @param newColor The new plot color
	 * @throws NoSuchSeriesException Invalid series index
	 */
	public void setSeriesColor(int seriesIndex, Color newColor) throws NoSuchSeriesException{

		// Old approach
//		this.parentDataModel.getOntologyModel().getChartSettings().getYAxisColors().remove(seriesIndex);
//		this.parentDataModel.getOntologyModel().getChartSettings().getYAxisColors().add(seriesIndex, ""+newColor.getRGB());
		
		this.sendNotifications(EventType.SERIES_COLOR_CHANGED, seriesIndex, newColor);
	}
	/**
	 * Sets the plot line width for a data series, specified by its index 
	 * @param seriesIndex The series index
	 * @param newWidth The new plot line width
	 * @throws NoSuchSeriesException Invalid series index
	 */
	public void setSeriesLineWidth(int seriesIndex, Float newWidth) throws NoSuchSeriesException{

		// Old approach
//		this.parentDataModel.getOntologyModel().getChartSettings().getYAxisLineWidth().remove(seriesIndex);
//		this.parentDataModel.getOntologyModel().getChartSettings().getYAxisLineWidth().add(seriesIndex, newWidth);
		
		this.sendNotifications(EventType.SERIES_LINE_WIDTH_CHANGED, seriesIndex, newWidth);
	}
	
	/**
	 * Sets the table model series settings.
	 * @param tableModelSeriesSettings the tableModelSeriesSettings to set
	 */
	public void setTableModelSeriesSettings(DefaultTableModel tableModelSeriesSettings) {
		this.tableModelSeriesSettings = tableModelSeriesSettings;
	}
	/**
	 * Gets the table model series settings.
	 * @return the tableModelSeriesSettings
	 */
	public DefaultTableModel getTableModelSeriesSettings() {
		if (this.tableModelSeriesSettings==null) {
			tableModelSeriesSettings = new DefaultTableModel();
			tableModelSeriesSettings.addColumn(Language.translate("Name"));
			tableModelSeriesSettings.addColumn(Language.translate("Farbe"));
			tableModelSeriesSettings.addColumn(Language.translate("Liniendicke"));
		}
		return tableModelSeriesSettings;
	}

	/**
	 * Updates the list of series-specific settings.
	 */
	public void updateSeriesList() {

		// --- Remove all elements first ----------------------------
		while(this.getTableModelSeriesSettings().getRowCount()>0) {
			this.getTableModelSeriesSettings().removeRow(0);
		}
		
		// --- Add rows containing the series specific settings -----
		for(int i=0; i < parentDataModel.getOntologyModel().getChartData().size(); i++){
				
			// Extract series settings from the ontology model
			DataSeries series = (DataSeries) parentDataModel.getOntologyModel().getChartData().get(i);
			
			String rgb = null;
			if (parentDataModel.getOntologyModel().getChartSettings().getYAxisColors().size() < (i+1)) {
				rgb = ((Integer) DataModel.DEFAULT_COLORS[i % DataModel.DEFAULT_COLORS.length].getRGB()).toString();
				parentDataModel.getOntologyModel().getChartSettings().getYAxisColors().add(i, rgb);
			} else {
				rgb = (String) parentDataModel.getOntologyModel().getChartSettings().getYAxisColors().get(i);
			}
			
			Float width = null; 
			if (parentDataModel.getOntologyModel().getChartSettings().getYAxisLineWidth().size() < (i+1)) {
				width = DataModel.DEFAULT_LINE_WIDTH;
				parentDataModel.getOntologyModel().getChartSettings().getYAxisLineWidth().add(i, width);
			} else {
				width = (Float) parentDataModel.getOntologyModel().getChartSettings().getYAxisLineWidth().get(i);
			}
			
			// Create a table row for the series
			Vector<Object> rowVector = new Vector<Object>();
			rowVector.add(series.getLabel());
			rowVector.add(new Color(Integer.parseInt(rgb)));
			rowVector.add(width);
			
			this.getTableModelSeriesSettings().addRow(rowVector);
		}
	}

	/**
	 * Gets the vector chart setting model listener.
	 * @return the chart setting model listener
	 */
	public Vector<ChartSettingModelListener> getChartSettingModelListener() {
		if (this.chartSettingModelListener==null) {
			chartSettingModelListener = new Vector<ChartSettingModelListener>();
		}
		return chartSettingModelListener;
	}
	/**
	 * Adds the chart setting model listener.
	 * @param listener the listener
	 */
	public void addChartSettingModelListener(ChartSettingModelListener listener) {
		this.getChartSettingModelListener().add(listener);
	}
	/**
	 * Removes the chart setting model listener.
	 * @param listener the listener
	 */
	public void removeChartSettingModelListener(ChartSettingModelListener listener) {
		this.getChartSettingModelListener().remove(listener);
	}
	/**
	 * Inform chart setting model listener.
	 */
	protected void informChartSettingModelListener() {
		for (int i = 0; i < this.getChartSettingModelListener().size(); i++) {
			this.getChartSettingModelListener().get(i).replaceModel(this);
		}
	}
	
	/**
	 * Sends a notification about a chart related change to the observers
	 * @param eventType The event type, indicating which setting was changed
	 * @param newValue The new value of the setting
	 */
	protected void sendNotifications(EventType eventType, Object newValue){
		this.sendNotifications(eventType, -1, newValue);
	}
	
	/**
	 * Sends a notification about a series related change to the observers
	 * @param eventType The event type, indicating which setting was changed
	 * @param seriesIndex The index of the affected series
	 * @param newValue The new value of the setting
	 */
	protected void sendNotifications(EventType eventType, int seriesIndex, Object newValue){
		if(this.notificationsEnabled){
			// --- Prepare ChartSettingsUpdateInfo object ------------
			ChartSettingsUpdateNotification notification = new ChartSettingsUpdateNotification();
			notification.setEventType(eventType);
			notification.setSeriesIndex(seriesIndex);
			notification.setNewValue(newValue);
			
			// --- Notify observers ------------------------
			this.setChanged();
			this.notifyObservers(notification);
		}
	}


	/**
	 * This class provides information about changes of settings to observers
	 * 
	 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
	 *
	 */
	public class ChartSettingsUpdateNotification{
		
		private EventType eventType;
		private int seriesIndex;
		private Object newValue;
		
		/**
		 * Returns the event type that specifies which settings have changed.
		 * @return The event type
		 */
		public EventType getEventType() {
			return eventType;
		}
		
		/**
		 * Sets the event type that specifies which settings have changed.
		 * @param eventType The event type
		 */
		public void setEventType(EventType eventType) {
			this.eventType = eventType;
		}
		
		/**
		 * Returns the index of the affected series, or -1 if the change is not series-specific.
		 * @return the series index
		 */
		public int getSeriesIndex() {
			return seriesIndex;
		}
		
		/**
		 * Sets the index of the affected series.
		 * @param seriesIndex The series index
		 */
		public void setSeriesIndex(int seriesIndex) {
			this.seriesIndex = seriesIndex;
		}
		
		/**
		 * Returns the new value of the changed setting.
		 * @return The new value
		 */
		public Object getNewValue() {
			return newValue;
		}
		
		/**
		 * Sets the new setting.
		 * @param newValue the new new setting
		 */
		public void setNewValue(Object newValue) {
			this.newValue = newValue;
		}
		
		
	}


	public boolean isNotificationsEnabled() {
		return notificationsEnabled;
	}

	public void setNotificationsEnabled(boolean notificationsEnabled) {
		this.notificationsEnabled = notificationsEnabled;
	}


}
