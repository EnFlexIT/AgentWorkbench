package agentgui.core.charts.timeseriesChart;

import java.util.Vector;

import agentgui.core.charts.TableModel;

/**
 * Data model for the table representation of a time series
 * @author Nils
 */
public class TimeSeriesTableModel extends TableModel {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 7951673309652098789L;
	
	private static final String DEFAULT_TIME_COLUMN_TITLE = "Time";
	
	/**
	 * Constructor
	 */
	public TimeSeriesTableModel(TimeSeriesDataModel parent){
		this.parent = parent;
		columnTitles = new Vector<String>();
		columnTitles.add(DEFAULT_TIME_COLUMN_TITLE);
		tableData = new Vector<Vector<Object>>();
	}

	

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if(columnIndex == 0){
			return Long.class;
		}else{
			return Float.class;
		}
	}
	
	
	/**
	 * @return the defaultTimeColumnTitle
	 */
	public static String getDefaultTimeColumnTitle() {
		return DEFAULT_TIME_COLUMN_TITLE;
	}

	
	
	
	
}
