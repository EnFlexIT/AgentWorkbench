package de.enflexit.df.impl.excel;

import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.dataSources.integration.AbstractPaginationDataLoader;

/**
 * The Class ExcelDataSourceDTNO.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExcelDataSourceDTNO extends AbstractDataSourceDTNO<ExcelDataSource> {

	private PaginationDataLoader4Excel paginationDataLoader4Excel;
	
	/**
	 * Instantiates a new data tree node data source excel.
	 * @param dsIntegration the ds integration
	 */
	public ExcelDataSourceDTNO(ExcelDataSourceIntegration dsIntegration) {
		super(dsIntegration);
		if (this.getDataSource().getName()==null) {
			this.getDataSource().setName("New Excel data source");
		}
		this.setImageIcon(BundleHelper.getThemedIcon("MS-Excel-Light.png", "MS-Excel-Dark.png"));
		this.setTooltipText("Please, configure the Excel file settings ...");
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.treeNode.AbstractDTNO_DataSource#getPaginationDataLoader()
	 */
	@Override
	public AbstractPaginationDataLoader<ExcelDataSource> getPaginationDataLoader() {
		if (paginationDataLoader4Excel==null) {
			paginationDataLoader4Excel = new PaginationDataLoader4Excel(this.getDataSource());
		}
		return paginationDataLoader4Excel;
	}
	

}
