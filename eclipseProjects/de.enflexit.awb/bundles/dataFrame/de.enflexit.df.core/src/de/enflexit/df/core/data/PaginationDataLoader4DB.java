package de.enflexit.df.core.data;

import java.io.IOException;

import de.enflexit.common.dataSources.DatabaseDataSource;
import tech.tablesaw.api.Table;


/**
 * The Class PaginationDataLoader4DB.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PaginationDataLoader4DB extends PaginationDataLoader<DatabaseDataSource> {

	/**
	 * Instantiates a new pagination data loader 4 DB.
	 * @param dataSource the data source
	 */
	public PaginationDataLoader4DB(DatabaseDataSource dataSource) {
		super(dataSource);
	}

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
