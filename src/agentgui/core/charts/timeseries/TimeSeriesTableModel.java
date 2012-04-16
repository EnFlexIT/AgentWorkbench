package agentgui.core.charts.timeseries;

import javax.swing.table.DefaultTableModel;
/**
 * DefaultTableModel with overridden isCellEditable()
 * @author Nils
 *
 */
class TimeSeriesTableModel extends DefaultTableModel {
	
	private TimeSeriesDataModel parent;
	
	TimeSeriesTableModel(TimeSeriesDataModel parent){
		super();
		this.parent = parent;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3078957605878256501L;

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		if(column == (parent.getSumSeriesIndex()+1)){	// +1 because of the time column  
			return false;
		}else{
			return true;
		}
	}
	
	
	
}
