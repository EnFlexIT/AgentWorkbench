package org.awb.env.networkModel.controller.ui;

import javax.swing.table.TableCellRenderer;

import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;

/**
 * The Interface NetworkComponentTableService can be used to extend the columns of the table 
 * in the {@link NetworkComponentTablePanel} by an OSGI-service. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface NetworkComponentTableService {

	/**
	 * Has to return the column header to be introduced with the service.
	 * @return the column header
	 */
	public String getColumnHeader();
	
	/**
	 * Has to return the cell value a String.
	 *
	 * @param networkModel the network model
	 * @param networkComponent the network component
	 * @return the cell value
	 */
	public String getCellValue(NetworkModel networkModel, NetworkComponent networkComponent);
	
	
	
	/**
	 * May return the current width of the column (can be null).
	 * @return the width
	 */
	public Integer getWidth();
	/**
	 * May return the minimum width of the column (can be null).
	 * @return the min width
	 */
	public Integer getMinWidth();
	/**
	 * May return the maximal width of the column (can be null).
	 * @return the max width
	 */
	public Integer getMaxWidth();
	
	/**
	 * May return the table cell renderer for the column (can be null).
	 * @return the table cell renderer
	 */
	public TableCellRenderer getTableCellRenderer();
	
	
}
