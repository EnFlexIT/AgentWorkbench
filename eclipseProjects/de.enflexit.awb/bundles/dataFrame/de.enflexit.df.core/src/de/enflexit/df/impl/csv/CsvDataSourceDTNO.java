package de.enflexit.df.impl.csv;

import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.dataSources.integration.AbstractPaginationDataLoader;

/**
 * The Class CsvDataSourceDTNO.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class CsvDataSourceDTNO extends AbstractDataSourceDTNO<CsvDataSource> {

	private PaginationDataLoader4CSV paginationDataLoader4CSV;
	
	/**
	 * Instantiates a new data tree node data source csv.
	 *
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 * @param dataSource the data source
	 */
	public CsvDataSourceDTNO(CsvDataSourceIntegration dsIntegration) {
		super(dsIntegration);
		if (this.getDataSource().getName()==null) {
			this.getDataSource().setName("New CSV data source");
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
	 * @see de.enflexit.df.core.model.treeNode.AbstractDTNO_DataSource#getPaginationDataLoader()
	 */
	@Override
	public AbstractPaginationDataLoader<CsvDataSource> getPaginationDataLoader() {
		if (paginationDataLoader4CSV==null) {
			paginationDataLoader4CSV = new PaginationDataLoader4CSV(this.getDataSource());
		}
		return paginationDataLoader4CSV;
	}

}
