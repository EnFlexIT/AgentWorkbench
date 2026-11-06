package de.enflexit.df.core.processing;

import java.util.List;

import javax.swing.JComponent;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class ProcessingResultDataSourceIntegration.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ProcessingResultDataSourceIntegration extends AbstractDataSourceIntegration<ProcessingResultDataSource> {
	
	private ProcessingResultDataSourceDTNO dtno;


	public ProcessingResultDataSourceIntegration(DataController dataController, DataWorkbook dataWorkbook, ProcessingResultDataSource dataSource) {
		super(dataController, dataWorkbook, dataSource);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSourceIntegration#getDTNO()
	 */
	@Override
	public ProcessingResultDataSourceDTNO getDTNO() {
		if (dtno==null) {
			dtno = new ProcessingResultDataSourceDTNO(this);
		}
		return dtno;
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
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetConfigurationPanel()
	 */
	@Override
	public void resetConfigurationPanel() {}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getDetailViewPanel()
	 */
	@Override
	public JComponent getDetailViewPanel() {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetDetailViewPanel()
	 */
	@Override
	public void resetDetailViewPanel() {}

}
