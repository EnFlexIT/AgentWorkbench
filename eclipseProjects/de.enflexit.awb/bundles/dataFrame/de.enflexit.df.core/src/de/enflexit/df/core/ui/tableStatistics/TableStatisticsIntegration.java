package de.enflexit.df.core.ui.tableStatistics;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.extension.DetailViewPanelService;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.treeNode.DTNO_Base;

/**
 * Integrates the table statistics tab in the properties panel.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableStatisticsIntegration implements DetailViewPanelService {
	
	private static final String PROPERTIES_TAB_NAME = "Table Statistics";
	
	private TableStatisticsPanel tableStatisticsPanel;
	
	/**
	 * Gets the table statistics panel.
	 * @return the table statistics panel
	 */
	public TableStatisticsPanel getTableStatisticsPanel() {
		if (tableStatisticsPanel==null) {
			tableStatisticsPanel = new TableStatisticsPanel();
		}
		return tableStatisticsPanel;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DetailViewPanelService#providesDetailView(de.enflexit.df.core.model.DataController, javax.swing.tree.DefaultMutableTreeNode)
	 */
	@Override
	public boolean providesDetailView(DataController dataController, DefaultMutableTreeNode treeNode) {
		
		if (treeNode==null) return false;
		
		DTNO_Base dtno = (DTNO_Base) treeNode.getUserObject();
		if (dtno instanceof AbstractDataSourceDTNO<?> dsDtno) {
			// --- For data source DTNOs, show the statistics tab if the DTNO contains a table 
			return dsDtno.getTable()!=null;
		} else {
			// --- No data source DTNO -> no table -> no statistics -----------   
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DetailViewPanelService#getDetailViewPanels(de.enflexit.df.core.model.DataController, javax.swing.tree.DefaultMutableTreeNode)
	 */
	@Override
	public List<DetailViewTabComponent> getDetailViewPanels(DataController dataController, DefaultMutableTreeNode treeNode) {
		ArrayList<DetailViewTabComponent> tabsList = new ArrayList<DetailViewTabComponent>();
		// --- If there is a table in the current node, provide a statistics panel
		DTNO_Base dtno = (DTNO_Base) treeNode.getUserObject();
		if (dtno instanceof AbstractDataSourceDTNO<?> dsDtno && dsDtno.getTable()!=null) {
			this.getTableStatisticsPanel().setDataTable(dsDtno.getTable());
			DetailViewTabComponent tabComponent = new DetailViewTabComponent(PROPERTIES_TAB_NAME, this.getTableStatisticsPanel());
			tabsList.add(tabComponent);
		}
		return tabsList;
	}

}
