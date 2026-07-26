package de.enflexit.df.impl.csv;

import java.util.List;

import javax.swing.JComponent;

import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.dataSources.PaginationDataLoader;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.treeNode.AbstractDataTreeNodeDataSource;
import de.enflexit.df.core.workbook.DataWorkbook;

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
	 * @param dataWorkbook the data workbook
	 * @param dataSource the data source
	 */
	public DataTreeNodeDataSourceCsv(DataController dataController, DataWorkbook dataWorkbook, CsvDataSource dataSource) {
		super(dataController, dataWorkbook, dataSource);
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
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getConfigurationToolbarComponents()
	 */
	@Override
	public List<JComponent> getConfigurationToolbarComponents() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getConfigurationPanel()
	 */
	@Override
	public JComponent getConfigurationPanel() {
		if (jPanelDataSourceConfigurationCsv==null) {
			jPanelDataSourceConfigurationCsv = new JPanelDataSourceConfigurationCsv(this);
		}
		return jPanelDataSourceConfigurationCsv;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetConfigurationPanel()
	 */
	@Override
	public void resetConfigurationPanel() {
		this.jPanelDataSourceConfigurationCsv = null;
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
