package de.enflexit.df.core.processing;

import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.dataSources.integration.AbstractPaginationDataLoader;

/**
 * DTNO implementation for {@link ProcessingDataSource}s.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ProcessingDataSourceDTNO extends AbstractDataSourceDTNO<ProcessingDataSource> {

	/**
	 * Instantiates a new processing data source DTNO.
	 * @param dsIntegration the ds integration
	 */
	public ProcessingDataSourceDTNO(AbstractDataSourceIntegration<ProcessingDataSource> dsIntegration) {
		super(dsIntegration);
		if (this.getDataSource().getName()==null) {
			this.getDataSource().setName("New processing data source");
		}
		this.setImageIcon(BundleHelper.getThemedIcon("DataProcessingBlack.png", "DataProcessingGrey.png"));
		this.setTooltipText("Please, configure the CSV File settings ...");
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO#getPaginationDataLoader()
	 */
	@Override
	public AbstractPaginationDataLoader<ProcessingDataSource> getPaginationDataLoader() {
		// --- Probably not needed ------------------------
		return null;
	}

}
