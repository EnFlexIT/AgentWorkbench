package de.enflexit.awb.timeSeriesDataProvider.dataModel;

import java.io.File;
import java.nio.file.Path;

import javax.swing.ImageIcon;

import de.enflexit.awb.core.Application;

/**
 * This class contains the configuration of a CSV-based data source. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class CsvDataSourceConfiguration extends AbstractDataSourceConfiguration {

	private static final long serialVersionUID = 1236384812660513627L;
	
	private static final String ICON_PATH_SELECTED = "/icons/CSVFileWhite.png";
	private static final String ICON_PATH_NOT_SELECTED = "/icons/CSVFileBlack.png";
	
	private String csvFilePath;
	
	private String dateTimeFormat = "dd.MM.yyyy HH:mm";
	private String columnSeparator = ";";
	
	private boolean headline;

	/**
	 * Gets the csv file path.
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
	 * Sets the csv file path.
	 * @param csvFilePath the new csv file path
	 */
	public void setCsvFilePath(Path csvFilePath) {
		if (csvFilePath.isAbsolute()) {
			this.csvFilePath = this.convertToRelativePath(csvFilePath).toString();
		} else {
			this.csvFilePath = csvFilePath.toString();
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

	/**
	 * Convert an absolute path to one relative to the project folder.
	 * @param absolutePath the absolute path
	 * @return the relative path
	 */
	private Path convertToRelativePath(Path absolutePath) {
		Path projectFolderPath = new File(Application.getProjectFocused().getProjectFolderFullPath()).toPath();
		return projectFolderPath.relativize(absolutePath);
	}
	
	/**
	 * Gets the csv file.
	 * @return the csv file
	 */
	public File getCsvFile() {
		Path projectFolderPath = new File(Application.getProjectFocused().getProjectFolderFullPath()).toPath();
		return projectFolderPath.resolve(this.getCsvFilePath()).toFile();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.dataModel.AbstractDataSourceConfiguration#getImageIcon(boolean)
	 */
	@Override
	public ImageIcon getImageIcon(boolean isSelected) {
		String iconPath;
		if (isSelected==true) {
			iconPath = ICON_PATH_SELECTED;
		} else {
			iconPath = ICON_PATH_NOT_SELECTED;
		}
		return new ImageIcon(this.getClass().getResource(iconPath));
	}
	
}
