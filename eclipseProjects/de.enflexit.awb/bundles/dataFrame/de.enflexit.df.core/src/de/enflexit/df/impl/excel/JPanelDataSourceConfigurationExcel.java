package de.enflexit.df.impl.excel;

import de.enflexit.df.core.dataSources.integration.AbstractJPanelDataSourceConfiguration;

/**
 * The Class JPanelDataSourceConfigurationExcel.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @param <CsvDataSourceDTNO> the generic type
 */
public class JPanelDataSourceConfigurationExcel  extends AbstractJPanelDataSourceConfiguration<ExcelDataSource, ExcelDataSourceIntegration> {

	private static final long serialVersionUID = 2214513797513629518L;

	/**
	 * Instantiates a new JPanelDataSourceConfigurationExcel.
	 * @param dsTreeNode the ExcelDataSourceDTNO
	 */
	public JPanelDataSourceConfigurationExcel(ExcelDataSourceIntegration dsIntegration) {
		super(dsIntegration);
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		
	}

	

}
