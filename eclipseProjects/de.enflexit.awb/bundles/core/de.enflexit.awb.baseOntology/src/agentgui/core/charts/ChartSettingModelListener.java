package agentgui.core.charts;

/**
 * The listener interface for receiving updates of the ChartSettingModel.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public interface ChartSettingModelListener {

	/**
	 * Sets the chart setting model.
	 * @param newChartSettingModel the new chart setting model
	 */
	public void replaceModel(ChartSettingModel newChartSettingModel);
	
	
	/**
	 * Can be used to notify underlying elements to stop edit actions.
	 */
	public void stopEditing();
}
