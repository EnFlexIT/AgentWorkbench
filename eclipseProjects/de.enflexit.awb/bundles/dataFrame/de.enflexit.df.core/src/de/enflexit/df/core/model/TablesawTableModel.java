package de.enflexit.df.core.model;

import javax.swing.table.AbstractTableModel;

import tech.tablesaw.api.Table;

/**
 * The Class TablesawTableModel.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TablesawTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -5733728417074673805L;
	
	private final Table table;
	
	/**
	 * Instantiates a new tablesaw table model.
	 * @param table the table
	 */
	public TablesawTableModel(Table table) {
		this.table = table;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return table.column(column).name();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return table.rowCount();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return table.columnCount();
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		//Object value = table.column(columnIndex).get(rowIndex);
		return table.column(columnIndex).get(rowIndex);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		//Class<?> clazz = super.getColumnClass(columnIndex);
		return super.getColumnClass(columnIndex);
	}

}
