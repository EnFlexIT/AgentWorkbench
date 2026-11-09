package de.enflexit.df.core.processing;

import java.io.IOException;

import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.dataSources.integration.AbstractPaginationDataLoader;
import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformation.DataTransformationProcessor;
import tech.tablesaw.api.Table;

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
		this.setImageIcon(BundleHelper.getThemedIcon("MBtable.png"));
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO#getShortUUID()
	 */
	@Override
	public String getShortUUID() {
		return this.getDataSource().getShortUUID();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO#getCaption()
	 */
	@Override
	public String getCaption() {
		return this.getDataSource().getResultNode().getLabelText();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO#getPaginationDataLoader()
	 */
	@Override
	public AbstractPaginationDataLoader<ProcessingResultDataSource> getPaginationDataLoader() {
		return new PaginationLoaderForTransformationResult(this.getDataSourceIntegration());
	}
	
	/**
	 * Gets the processing data source integration.
	 * @return the processing data source integration
	 */
	protected ProcessingDataSourceIntegration getProcessingDataSourceIntegration() {
		return (ProcessingDataSourceIntegration) this.getDataSource().getProcessingDataSource().getDataSourceIntegration(this.getDataController(), this.getDataWorkbook());
	}
	
	/**
	 * No actual pagination, just needed for asynchronous loading.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class PaginationLoaderForTransformationResult extends AbstractPaginationDataLoader<ProcessingResultDataSource> {

		public PaginationLoaderForTransformationResult(AbstractDataSourceIntegration<ProcessingResultDataSource> dsIntegration) {
			super(dsIntegration);
		}

		/* (non-Javadoc)
		 * @see java.io.Closeable#close()
		 */
		@Override
		public void close() throws IOException {
			// --- Not needed here ---------------------------------- 
		}

		/* (non-Javadoc)
		 * @see de.enflexit.df.core.dataSources.integration.AbstractPaginationDataLoader#loadNextPage()
		 */
		@Override
		public Table loadNextPage() {
			ProcessingDataSourceIntegration processingIntegration = getProcessingDataSourceIntegration();
			DataTransformationProcessor transformationProcessor = new DataTransformationProcessor(processingIntegration.getGraphController());
			AbstractDataTransformation transformation = processingIntegration.getGraphController().getTransformationForNode(this.getDataSource().getResultNode());
			transformationProcessor.performTransformation(transformation);
			return this.getDataSource().getResultNode().getDataTable();
		}
		
	}

}
