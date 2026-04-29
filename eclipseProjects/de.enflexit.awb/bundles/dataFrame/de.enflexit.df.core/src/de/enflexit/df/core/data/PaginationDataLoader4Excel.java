package de.enflexit.df.core.data;

import java.io.IOException;

import de.enflexit.common.dataSources.ExcelDataSource;
import tech.tablesaw.api.Table;

/**
 * The Class PaginationDataLoader4Excel.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PaginationDataLoader4Excel extends PaginationDataLoader<ExcelDataSource> {

	/**
	 * Instantiates a new pagination data loader 4 excel.
	 * @param dataSource the data source
	 */
	public PaginationDataLoader4Excel(ExcelDataSource dataSource) {
		super(dataSource);
	}

	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.data.PaginationDataLoader#loadNextPage()
	 */
	@Override
	public Table loadNextPage() {
		// TODO Auto-generated method stub
		return null;
	}


}
