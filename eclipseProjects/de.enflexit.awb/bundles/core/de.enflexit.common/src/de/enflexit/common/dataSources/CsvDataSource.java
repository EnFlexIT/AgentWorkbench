package de.enflexit.common.dataSources;

import de.enflexit.common.StringHelper;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The Class CsvDataSource.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@DiscriminatorValue("csv")

@XmlRootElement(name = "CsvDataSource")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "csvFilePath",
    "dateTimeFormat",
    "columnSeparator",
    "headline"
})

public class CsvDataSource extends AbstractDataSource {

	private static final long serialVersionUID = 9196800931542107902L;

	public static final String CHANGED_CSV_FILE = "CHANGED_CSV_FILE";
	public static final String CHANGED_CSV_DATE_TIME_FORMAT = "CHANGED_CSV_DATE_TIME_FORMAT";
	public static final String CHANGED_CSV_COLUMN_SEPARATOR = "CHANGED_CSV_COLUMN_SEPARATOR";
	public static final String CHANGED_CSV_HAS_HEADLINE = "CHANGED_CSV_HAS_HEADLINE";
	
	public static final String[] COLUMN_SEPARATORS = {";",",",":","."};
	
	
	@Column(name="csv_file_path", nullable=false)
	private String csvFilePath;
	
	@Column(name="date_time_format", nullable=false)
	private String dateTimeFormat = "dd.MM.yyyy HH:mm";
	@Column(name="column_separator", nullable=false)
	private String columnSeparator = ";";
	
	private boolean headline;
	
	
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
	 * @see de.enflexit.common.dataSources.AbstractDataSource#equals(java.lang.Object)
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
