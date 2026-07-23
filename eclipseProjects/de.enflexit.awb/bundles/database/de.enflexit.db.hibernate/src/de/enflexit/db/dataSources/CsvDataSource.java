package de.enflexit.db.dataSources;

import de.enflexit.common.GlobalConstants;
import de.enflexit.common.NumberHelper;
import de.enflexit.common.StringHelper;

/**
 * The Class CsvDataSource.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - ICB - University of Duisburg-Essen
 */
public class CsvDataSource extends DefaultDataSource {

	private static final long serialVersionUID = 9196800931542107902L;

	public static final String KEY_FILE_PATH = "FILE_PATH"; 
	public static final String KEY_DATE_TIME_FORMAT = "DATE_TIME_FORMAT";
	public static final String KEY_COLUMN_SEPARATOR = "COLUMN_SEPARATOR";
	public static final String KEY_HAS_HEADLINE = "HAS_HEADLINE";
	
	public static final String CHANGED_CSV_FILE = "CHANGED_CSV_FILE";
	public static final String CHANGED_CSV_DATE_TIME_FORMAT = "CHANGED_CSV_DATE_TIME_FORMAT";
	public static final String CHANGED_CSV_COLUMN_SEPARATOR = "CHANGED_CSV_COLUMN_SEPARATOR";
	public static final String CHANGED_CSV_HAS_HEADLINE = "CHANGED_CSV_HAS_HEADLINE";
	
	public static final String[] COLUMN_SEPARATORS = {";",",",":","."};
	
	
	private String csvFilePath;
	private String dateTimeFormat = GlobalConstants.DEFAULT_TIME_FORMAT;
	private String columnSeparator = ";";
	private boolean headline;
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#newInstance()
	 */
	@Override
	public CsvDataSource newInstance() {
		return new CsvDataSource();
	}
	
	
	/**
	 * Returns the csv file path.
	 * @return the csv file path
	 */
	public String getCsvFilePath() {
		// --- Replace windows path separators ------------
		if (this.csvFilePath!=null && this.csvFilePath.contains("\\")) {
			this.csvFilePath = this.csvFilePath.replace("\\", "/");
		}
		return csvFilePath;
	}
	/**
	 * Sets the csv file path.
	 * @param csvFilePath the new csv file path
	 */
	public void setCsvFilePath(String csvFilePath) {
		// --- Replace windows path separators ------------
		if (csvFilePath!=null && csvFilePath.contains("\\")) {
			this.csvFilePath = csvFilePath.replace("\\", "/");
		} else {
			this.csvFilePath = csvFilePath;
		}
	}

	/**
	 * Gets the date time format.
	 * @return the date time format
	 */
	public String getDateTimeFormat() {
		return dateTimeFormat;
	}
	/**
	 * Sets the date time format.
	 * @param dateTimeFormat the new date time format
	 */
	public void setDateTimeFormat(String dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}
	
	/**
	 * Gets the column separator.
	 * @return the column separator
	 */
	public String getColumnSeparator() {
		return columnSeparator;
	}
	/**
	 * Sets the column separator.
	 * @param columnSeparator the new column separator
	 */
	public void setColumnSeparator(String columnSeparator) {
		this.columnSeparator = columnSeparator;
	}

	/**
	 * Checks if is headline.
	 * @return true, if is headline
	 */
	public boolean isHeadline() {
		return headline;
	}
	/**
	 * Sets the headline.
	 * @param headline the new headline
	 */
	public void setHeadline(boolean headline) {
		this.headline = headline;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#toConfigurationString()
	 */
	@Override
	public String toConfigurationString() {
		
		String config = new String();
		
		config = DatabaseDataSource.addConfigValue(config, KEY_ID, (this.getId() + ""));
		config = DatabaseDataSource.addConfigValue(config, KEY_NAME, this.getName());
		config = DatabaseDataSource.addConfigValue(config, KEY_DESCRIPTION, this.getDescription());
		config = DatabaseDataSource.addConfigValue(config, KEY_ROWS_PER_PAGE, this.getRowsPerPage() + "");
		
		config = DatabaseDataSource.addConfigValue(config, KEY_FILE_PATH, this.getCsvFilePath());
		config = DatabaseDataSource.addConfigValue(config, KEY_COLUMN_SEPARATOR, this.getColumnSeparator());
		config = DatabaseDataSource.addConfigValue(config, KEY_DATE_TIME_FORMAT, this.getDateTimeFormat());
		config = DatabaseDataSource.addConfigValue(config, KEY_HAS_HEADLINE, Boolean.toString(this.isHeadline()));
		
		if (config.isBlank()==true) {
			config = null;
		}
		return config;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DataSource#fromConfigurationString(java.lang.String)
	 */
	@Override
	public CsvDataSource fromConfigurationString(String config) {
		
		if (config==null || config.isBlank()==true) return this;
		
		String[] keyValuePairs = config.split("\\|");
		if (keyValuePairs.length==0) return this;
		
		// --- Create new instance ----------------------------------
		for (String keyValuePair : keyValuePairs) {
			
			int idxTagOpen  = keyValuePair.indexOf("[");
			int idxTagClose = keyValuePair.indexOf("]");
			
			String key   = keyValuePair.substring(0, idxTagOpen);
			String value = keyValuePair.substring(idxTagOpen + 1, idxTagClose);
			if (value.isBlank()==true) continue;
			
			switch (key) {
			case KEY_ID:
				this.setId(NumberHelper.parseInteger(value));
				break;
			case KEY_NAME:
				this.setName(value);
				break;
			case KEY_DESCRIPTION:
				this.setDescription(value);
				break;
			case KEY_ROWS_PER_PAGE:
				Integer rowsPerPage = NumberHelper.parseInteger(value);
				if (rowsPerPage!=null) this.setRowsPerPage(rowsPerPage);
				break;
						
			case KEY_FILE_PATH:
				this.setCsvFilePath(value);
				break;
			case KEY_COLUMN_SEPARATOR:
				this.setColumnSeparator(value);
				break;
			case KEY_DATE_TIME_FORMAT:
				this.setDateTimeFormat(value);
				break;
			case KEY_HAS_HEADLINE:
				this.setHeadline(Boolean.parseBoolean(value));
				break;
			}
		} // end for
		return this;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.dataSources.DefaultDataSource#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		
		if (super.equals(compObj)==false) return false;
		if (compObj instanceof CsvDataSource == false) return false;
		
		CsvDataSource csvDsComp = (CsvDataSource) compObj;
		
		if (StringHelper.isEqualString(this.getCsvFilePath(), csvDsComp.getCsvFilePath())==false) return false;
		if (StringHelper.isEqualString(this.getDateTimeFormat(), csvDsComp.getDateTimeFormat())==false) return false;
		if (StringHelper.isEqualString(this.getColumnSeparator(), csvDsComp.getColumnSeparator())==false) return false;
		if (this.isHeadline()!=csvDsComp.isHeadline()) return false;
		
		return true;
	}
	
}
