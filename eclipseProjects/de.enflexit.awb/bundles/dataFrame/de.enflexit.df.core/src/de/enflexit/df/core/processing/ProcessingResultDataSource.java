package de.enflexit.df.core.processing;

import de.enflexit.df.core.dataSources.DefaultDataSource;
import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import tech.tablesaw.api.Table;

/**
 * The Class ProcessingResultDataSource.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ProcessingResultDataSource extends DefaultDataSource{

	private static final long serialVersionUID = -7659446693580267938L;
	
	private DataTableNode resultNode;
	private ProcessingDataSource processingDataSource;

	/**
	 * Instantiates a new processing result data source.
	 * @param resultNode the result node
	 */
	public ProcessingResultDataSource(DataTableNode resultNode, ProcessingDataSource processingDataSource) {
		this.resultNode = resultNode;
		this.processingDataSource = processingDataSource;
	}
	
	/**
	 * Gets the data table.
	 * @return the data table
	 */
	public Table getDataTable() {
		if (this.resultNode!=null) {
			return resultNode.getDataTable();
		} else {
			throw new IllegalArgumentException("Result node not set!");
		}
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DefaultDataSource#getShortUUID()
	 */
	@Override
	public String getShortUUID() {
		return resultNode.getDataSourceUUID();
	}
	
	/**
	 * Gets the result node.
	 * @return the result node
	 */
	public DataTableNode getResultNode() {
		return resultNode;
	}

	/**
	 * Gets the processing data source.
	 * @return the processing data source
	 */
	public ProcessingDataSource getProcessingDataSource() {
		return processingDataSource;
	}
}
