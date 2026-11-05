package de.enflexit.df.core.processing;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.dataSources.integration.AbstractPaginationDataLoader;

/**
 * The Class ProcessingResultDataSourceDTNO.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ProcessingResultDataSourceDTNO extends AbstractDataSourceDTNO<ProcessingResultDataSource> {

	/**
	 * Instantiates a new processing result data source DTNO.
	 * @param dsIntegration the ds integration
	 */
	public ProcessingResultDataSourceDTNO(AbstractDataSourceIntegration<ProcessingResultDataSource> dsIntegration) {
		super(dsIntegration);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO#getPaginationDataLoader()
	 */
	@Override
	public AbstractPaginationDataLoader<ProcessingResultDataSource> getPaginationDataLoader() {
		return null;
	}

}
