package de.enflexit.df.core.ui;

import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import de.enflexit.df.core.extension.ColumnDescription;

/**
 * The Class JTableHeaderWithToolTips.
 * @author Tim Fischer - DAWIS - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JTableHeaderWithToolTips extends JTableHeader {
	
	private static final long serialVersionUID = 2671430380112775131L;
	
	private List<ColumnDescription> columnDescriptionList;
	
	
	/**
	 * Instantiates a new table header with tool tips.
	 * @param model the TableColumnModel to apply the tool tips to
	 * @param tooltipVector the tool tips as String vector
	 */
	public JTableHeaderWithToolTips(TableColumnModel model) {
		super(model);
	}
	
	/**
	 * Returns the column description list.
	 * @return the column description list
	 */
	public List<ColumnDescription> getColumnDescriptionList() {
		return columnDescriptionList;
	}
	/**
	 * Sets the column description list.
	 * @param columnDescriptionList the new column description list
	 */
	public void setColumnDescriptionList(List<ColumnDescription> columnDescriptionList) {
		this.columnDescriptionList = columnDescriptionList;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.JTableHeader#getToolTipText(java.awt.event.MouseEvent)
	 */
	@Override
	public String getToolTipText(MouseEvent event) {
		
		if (this.getColumnDescriptionList().size()==0) return "";
		
		int col = columnAtPoint(event.getPoint());
		int modelCol = this.getTable().convertColumnIndexToModel(col);
		if (modelCol==-1) return "";
		
		return this.getColumnDescriptionList().get(modelCol).getToolTip();
	}
	
}