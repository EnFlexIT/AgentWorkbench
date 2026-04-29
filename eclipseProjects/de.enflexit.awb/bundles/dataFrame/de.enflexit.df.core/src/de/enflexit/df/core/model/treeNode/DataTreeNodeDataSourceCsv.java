package de.enflexit.df.core.model.treeNode;

import java.util.List;

import javax.swing.JComponent;

import de.enflexit.common.dataSources.CsvDataSource;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.data.PaginationDataLoader;
import de.enflexit.df.core.data.PaginationDataLoader4CSV;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.dataSource.JPanelDataSourceConfigurationCsv;

/**
 * The Class DataTreeNodeDataSourceCsv.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeNodeDataSourceCsv extends AbstractDataTreeNodeDataSource<CsvDataSource> {

	private JPanelDataSourceConfigurationCsv jPanelDataSourceConfigurationCsv;

	private PaginationDataLoader4CSV paginationDataLoader4CSV;
	
	/**
	 * Instantiates a new data tree node data source csv.
	 *
	 * @param dataController the data controller
	 * @param dataSource the data source
	 */
	public DataTreeNodeDataSourceCsv(DataController dataController, CsvDataSource dataSource) {
		super(dataController, dataSource);
		if (dataSource.getName()==null) {
			dataSource.setName("New CSV data source");
		}
		this.setImageIcon(BundleHelper.getThemedIcon("CsvFileBlack.png", "CsvFileGrey.png"));
		this.setTooltipText("Please, configure the CSV File settings ...");
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.DataTreeNodeObjectBase#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return this.getDataSource().getCsvFilePath();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.ConfigurationPanel#getConfigurationToolbarComponents()
	 */
	@Override
	public List<JComponent> getConfigurationToolbarComponents() {
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.ConfigurationPanel#getConfigurationPanel()
	 */
	@Override
	public JComponent getConfigurationPanel() {
		if (jPanelDataSourceConfigurationCsv==null) {
			jPanelDataSourceConfigurationCsv = new JPanelDataSourceConfigurationCsv(this.getDataController(), this);
		}
		return jPanelDataSourceConfigurationCsv;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.treeNode.AbstractDataTreeNodeDataSource#getPaginationDataLoader()
	 */
	@Override
	public PaginationDataLoader<CsvDataSource> getPaginationDataLoader() {
		if (paginationDataLoader4CSV==null) {
			paginationDataLoader4CSV = new PaginationDataLoader4CSV(this.getDataSource());
		}
		return paginationDataLoader4CSV;
	}

}
