package de.enflexit.awb.timeSeriesDataProvider.jdbc;

import javax.swing.ImageIcon;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSourceConfiguration;

/**
 * Data source configuration for JDBC-based data sources.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JDBCDataScourceConfiguration extends AbstractDataSourceConfiguration {
	
	private static final long serialVersionUID = 4864231784777194491L;
	
	private static final String ICON_PATH_WHITE = "/icons/DataBaseWhite.png";
	private static final String ICON_PATH_BLACK = "/icons/DataBaseBlack.png";
	private static final String ICON_PATH_GREY = "/icons/DataBaseGrey.png";
	
	private String jdbcURL;
	private String tableName;
	private String dateTimeColumn; 

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.dataModel.AbstractDataSourceConfiguration#getImageIcon(boolean)
	 */
	@Override
	public ImageIcon getImageIcon(boolean isSelected, boolean isDarkMode) {
		String iconPath;
		if (isSelected==true) {
			iconPath = ICON_PATH_WHITE;
		} else if (isDarkMode==true) {
			iconPath = ICON_PATH_GREY;
		} else {
			iconPath = ICON_PATH_BLACK;
		}
		return new ImageIcon(this.getClass().getResource(iconPath));
	}

	/**
	 * Gets the JDBC URL for this data source.
	 * @return the jdbc URL
	 */
	public String getJdbcURL() {
		return jdbcURL;
	}

	/**
	 * Sets the JDBC URL for this data source.
	 * @param jdbcURL the new jdbc URL
	 */
	public void setJdbcURL(String jdbcURL) {
		this.jdbcURL = jdbcURL;
	}

	/**
	 * Gets the name of the table or view to query the data from.
	 * @return the table name
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * Sets the name of the table or view to query the data from.
	 * @param tableName the new table name
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * Gets the date time column.
	 * @return the date time column
	 */
	public String getDateTimeColumn() {
		return dateTimeColumn;
	}
	/**
	 * Sets the date time column.
	 * @param dateTimeColumn the new date time column
	 */
	public void setDateTimeColumn(String dateTimeColumn) {
		this.dateTimeColumn = dateTimeColumn;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSourceConfiguration#createDataSource()
	 */
	@Override
	public AbstractDataSource createDataSource() {
		return new JDBCDataSource(this);
	}

}
