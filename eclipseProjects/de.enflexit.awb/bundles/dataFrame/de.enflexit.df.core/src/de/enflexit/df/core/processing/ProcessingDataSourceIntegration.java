package de.enflexit.df.core.processing;

import java.util.List;

import javax.swing.JComponent;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.processing.ui.JPanelDataProcessingDetailsView;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class ProcessingDataSourceIntegration.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ProcessingDataSourceIntegration extends AbstractDataSourceIntegration<ProcessingDataSource>{
	
	private ProcessingDataSourceDTNO dtno;
	private JPanelDataSourceConfigurationProcessing configPanel;
	private JPanelDataProcessingDetailsView detailsViewPanel;

	/**
	 * Instantiates a new processing data source integration.
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 * @param dataSource the data source
	 */
	public ProcessingDataSourceIntegration(DataController dataController, DataWorkbook dataWorkbook, ProcessingDataSource dataSource) {
		super(dataController, dataWorkbook, dataSource);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSourceIntegration#getDTNO()
	 */
	@Override
	public AbstractDataSourceDTNO<ProcessingDataSource> getDTNO() {
		if (dtno==null) {
			dtno = new ProcessingDataSourceDTNO(this);
		}
		return dtno;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getConfigurationToolbarComponents()
	 */
	@Override
	public List<JComponent> getConfigurationToolbarComponents() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getConfigurationPanel()
	 */
	@Override
	public JComponent getConfigurationPanel() {
		if (configPanel==null) {
			configPanel = new JPanelDataSourceConfigurationProcessing(this);
		}
		return configPanel;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetConfigurationPanel()
	 */
	@Override
	public void resetConfigurationPanel() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getDetailViewPanel()
	 */
	@Override
	public JComponent getDetailViewPanel() {
		if (detailsViewPanel==null) {
			detailsViewPanel = new JPanelDataProcessingDetailsView(this.getDataController());
		}
		return detailsViewPanel;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetDetailViewPanel()
	 */
	@Override
	public void resetDetailViewPanel() {
		// TODO Auto-generated method stub
		
	}

}
