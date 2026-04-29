package de.enflexit.df.core.data;

import java.io.Closeable;
import java.io.IOException;

import de.enflexit.common.dataSources.AbstractDataSource;
import de.enflexit.df.core.DataFramePreferences;
import tech.tablesaw.api.Table;

/**
 * The Class PaginationDataLoader serves as base for specific DataSources.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class PaginationDataLoader<DS extends AbstractDataSource> implements Closeable {

	private DS dataSource;
	
	private Boolean paginationActivated;
	private Integer numberOfRecordsPerPage;

	private int pageNumberLoaded;
	
	protected String errorMessage;
	
	
	/**
	 * Instantiates a new pagination data loader.
	 * @param dataSource the data source to be used
	 */
	public PaginationDataLoader(DS dataSource) {
		this.setDataSource(dataSource);
	}
	
	/**
	 * Sets the data source.
	 * @param dataSource the new data source
	 */
	public void setDataSource(DS dataSource) {
		this.dataSource = dataSource;
	}
	/**
	 * Returns the current data source to be used for the data loading.
	 * @return the data source
	 */
	public DS getDataSource() {
		return dataSource;
	}
	
	
	/**
	 * Checks if is pagination activated.
	 * @return true, if is pagination activated
	 */
	public boolean isPaginationActivated() {
		if (paginationActivated==null) {
			if (this.getDataSource()!=null) {
				paginationActivated = (this.getDataSource().getRowsPerPage()>0);
			} else {
				paginationActivated = DataFramePreferences.getInstance().getBoolean(DataFramePreferences.DATA_FRAME_PAGINATION_ACTIVATED, true);
			}
		}
		return paginationActivated;
	}
	/**
	 * Sets the pagination activated.
	 * @param paginationActivated the new pagination activated
	 */
	public void setPaginationActivated(boolean paginationActivated) {
		this.paginationActivated = paginationActivated;
		this.setRowsPerPageToDataSource();
	}
	
	/**
	 * Returns the number of records per page.
	 * @return the number of records per page
	 */
	public int getNumberOfRecordsPerPage() {
		if (numberOfRecordsPerPage==null) {
			if (this.getDataSource()!=null) {
				numberOfRecordsPerPage = Math.abs(this.getDataSource().getRowsPerPage());
				if (numberOfRecordsPerPage==0) {
					numberOfRecordsPerPage = DataFramePreferences.getInstance().getInt(DataFramePreferences.DATA_FRAME_NUMBER_OF_RECORDS_PER_PAGE, 1000);		
				}
			} else {
				numberOfRecordsPerPage = DataFramePreferences.getInstance().getInt(DataFramePreferences.DATA_FRAME_NUMBER_OF_RECORDS_PER_PAGE, 1000);
			}
		}
		return numberOfRecordsPerPage;
	}
	/**
	 * Sets the number of records per page.
	 * @param numberOfRecordsPerPage the new number of records per page
	 */
	public void setNumberOfRecordsPerPage(int numberOfRecordsPerPage) {
		this.numberOfRecordsPerPage = numberOfRecordsPerPage;
		if (this.numberOfRecordsPerPage!=null) {
			this.setRowsPerPageToDataSource();
		}
	}
	
	/**
	 * Sets the rows per page to data source.
	 */
	private void setRowsPerPageToDataSource() {
		
		if (this.getDataSource()==null) return;
		
		int rowsPerPage = this.getNumberOfRecordsPerPage();
		if (this.isPaginationActivated()==true) {
			rowsPerPage = Math.abs(rowsPerPage);
		} else {
			rowsPerPage = -Math.abs(rowsPerPage);
		}
		this.getDataSource().setRowsPerPage(rowsPerPage);
	}
	
	
	/**
	 * Returns the current page number that is the number of pages that were already loaded.
	 * @return 0, if nothing was loaded yet, the positive page number, if pagination is activate or the negative value -1, if everything was loaded at once
	 */
	public int getPageNumberLoaded() {
		return pageNumberLoaded;
	}
	/**
	 * Sets the page number.
	 * @param pageNumberLoaded the new page number
	 */
	public void setPageNumberLoaded(int pageNumber) {
		this.pageNumberLoaded = pageNumber;
	}
	
	
	/**
	 * Gets the error message.
	 * @return the error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * Sets the error message.
	 * @param errorMessage the new error message
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * Checks if there are errors to be shown.
	 * @return true, if successful
	 */
	public boolean hasErrors() {
		return this.getErrorMessage()!=null && this.getErrorMessage().isBlank()==false;
	}
	
	/**
	 * Has to reset the loader, in order to be able to start reading from the beginning.
	 */
	public void reset() {
		try {
			this.close();
			this.setPageNumberLoaded(0);
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}
	
	/**
	 * Has to load the next data page by considering {@link #isPaginationActivated()} 
	 * and {@link #getNumberOfRecordsPerPage()} in the local settings.
	 * @return the tablesaw table instance for the page
	 */
	public abstract Table loadNextPage();
	
	
}
