package agentgui.core.charts.xyChart;

import java.util.Vector;

import agentgui.core.charts.TableModel;

public class XyTableModel extends TableModel {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -8873141815861732107L;
	private static final String DEFAULT_KEY_COLUMN_TITLE = "X value";
	
	public XyTableModel(XyDataModel parent){
		this.parent = parent;
		columnTitles = new Vector<String>();
		columnTitles.add(DEFAULT_KEY_COLUMN_TITLE);
		tableData = new Vector<Vector<Object>>();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Float.class;
	}
	
	/**
	 * @return the defaultTimeColumnTitle
	 */
	public static String getDefaultKeyColumnTitle() {
		return DEFAULT_KEY_COLUMN_TITLE;
	}

}
