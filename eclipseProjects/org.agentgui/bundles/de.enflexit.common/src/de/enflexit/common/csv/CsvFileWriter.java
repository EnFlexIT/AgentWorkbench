package de.enflexit.common.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Vector;

/**
 * Generic CSV file writer
 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
 */
public class CsvFileWriter {
	
	private static final String DEFAULT_SEPARATOR = ";";
	private static final String DEFAULT_NEWLINE = "\n";
	
	private String separator;
	private String decimalSeparator;
	private String newLine;
	
	
	/**
	 * Constructor - using the defaults for field and line separators 
	 */
	public CsvFileWriter(){
		this(DEFAULT_SEPARATOR, null, DEFAULT_NEWLINE);
	}
	/**
	 * Constructor - using user-defined field and line separators
	 * @param separator Field separator
	 * @param newLine Line separator
	 */
	public CsvFileWriter(String separator, String newLine) {
		this(separator, null, newLine);
	}
	/**
	 * Constructor - using user-defined field and line separators.
	 *
	 * @param separator Field separator
	 * @param decimalSeparator the decimal separator
	 * @param newLine Line separator
	 */
	public CsvFileWriter(String separator, String decimalSeparator, String newLine) {
		this.setSeparator(separator);
		this.setDecimalSeparator(decimalSeparator);
		this.setNewLine(newLine);
	}
	

	/**
	 * Returns the separator for row values.
	 * @return the separator
	 */
	public String getSeparator() {
		if (separator==null) {
			separator = DEFAULT_SEPARATOR;
		}
		return separator;
	}
	/**
	 * Sets the separator for the row values.
	 * @param separator the new separator
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	
	/**
	 * Returns the decimal separator for float or decimal values.
	 * @return the decimal separator
	 */
	public String getDecimalSeparator() {
		if (decimalSeparator==null) {
			DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
			DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
			decimalSeparator = Character.toString(symbols.getDecimalSeparator());
		}
		return decimalSeparator;
	}
	/**
	 * Sets the decimal separator.
	 * @param decimalSeparator the new decimal separator
	 */
	public void setDecimalSeparator(String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}
	
	/**
	 * Returns the new line string.
	 * @return the new line
	 */
	public String getNewLine() {
		if (newLine==null) {
			newLine = DEFAULT_NEWLINE;
		}
		return newLine;
	}
	/**
	 * Sets the new line string.
	 * @param newLine the new new line
	 */
	public void setNewLine(String newLine) {
		this.newLine = newLine;
	}
	
	
	/**
	 * Writes data from a 2D array to a CSV file
	 * @param csvFile The target file
	 * @param data The data to be exported
	 * @return Export successful?
	 */
	public boolean exportData(File csvFile, Object[][] data){
		return this.exportData(csvFile, data, null);
	}

	/**
	 * Writes data from a 2D array to a CSV file
	 * @param csvFile The target file
	 * @param data The data to be exported
	 * @param headline Optional headline
	 * @return Export successful?
	 */
	public boolean exportData(File csvFile, Object[][] data, String headline){

		// --- Initializations --------------------------------------
		boolean success = false;
		FileWriter fileWriter = null;
		
		try {
			// --- Create output file -------------------------------
			fileWriter = new FileWriter(csvFile);
			
			if (headline != null) {
				fileWriter.append(headline);
				fileWriter.append(this.getNewLine());
			}
			
			// --- Iterate over the data ----------------------------
			for (int i=0; i<data.length; i++) {
				for (int j=0; j<data[i].length; j++) {
					// --- Append next data field
					if (data[i][j]!=null) {
						fileWriter.append(this.getDataString(data[i][j]));
					}
					// --- Append field or line separator -----------
					if (j<(data[i].length-1)){
						fileWriter.append(this.getSeparator());
					} else {
						fileWriter.append(this.getNewLine());
					}
				}
			}
			success = true;
		
		} catch (IOException ioe) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error writing CSV file!");
			ioe.printStackTrace();
			
		} finally {
			try {
				fileWriter.flush();
			} catch (IOException e) {
				System.out.println("[" + this.getClass().getSimpleName() + "] Error while flushing fileWriter!");
                e.printStackTrace();
			}
			try {
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("[" + this.getClass().getSimpleName() + "] Error while closing fileWriter!");
                e.printStackTrace();
			}
		}
		return success;
	}
	/**
	 * Return the data string for the specified object.
	 *
	 * @param dataObject the data object
	 * @return the data string
	 */
	private String getDataString(Object dataObject) {
		
		String dataString = dataObject.toString();
		if (dataObject instanceof Double || dataObject instanceof Float) {
			// --- Place the right decimal separator into the string ----------
			String ds = this.getDecimalSeparator();
			if (ds.equals(".")==false) {
				dataString = dataString.replace(".", ds);
			}
		}
		return dataString;
	}
	
	
	/**
	 * Writes data from a 2D array to a CSV file
	 * @param csvFile The target file
	 * @param data The data to be exported
	 * @return Export successful?
	 */
	public boolean exportData(File csvFile, Vector<Vector<Object>> data){
		return this.exportData(csvFile, data, null);
	}
	
	/**
	 * Writes data from a vector of vectors to a CSV file
	 * @param csvFile The target file
	 * @param data The data to be exported
	 * @param headline Optional headline
	 * @return Export successful?
	 */
	public boolean exportData(File csvFile, Vector<Vector<Object>> data, String headline){
		
		Object[][] dataArray = new Object[data.size()][];
		for (int i=0; i<data.size(); i++){
			dataArray[i] = data.get(i).toArray();
		}
		return this.exportData(csvFile, dataArray, headline);
	}
	
}