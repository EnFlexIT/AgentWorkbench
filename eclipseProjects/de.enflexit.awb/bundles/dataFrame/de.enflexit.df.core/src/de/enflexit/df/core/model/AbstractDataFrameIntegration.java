package de.enflexit.df.core.model;

import de.enflexit.df.core.model.treeNode.AbstractDataTreeNodeDataSource;
import de.enflexit.df.core.ui.AbstractJPanelDataSourceConfiguration;

/**
 * The Class AbstractDataFrameIntegration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class AbstractDataFrameIntegration {

	
	/**
	 * Has to return the data source configuration panel.
	 * @return the data source configuration panel
	 */
	public abstract AbstractJPanelDataSourceConfiguration<AbstractDataTreeNodeDataSource<?>> getDataSourceConfigurationPanel();
	
}
