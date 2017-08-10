package de.enflexit.common.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * Generic CSV file writer
 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
 *
 */
public class CsvFileWriter {
	
	private static final String DEFAULT_SEPARATOR = ";";
	private static final String DEFAULT_NEWLINE = "\n";
	
	private String separator;
	private String newLine;
	
	/**
	 * Constructor - using the defaults for field and line separators 
	 */
	public CsvFileWriter(){
		this(DEFAULT_SEPARATOR, DEFAULT_NEWLINE);
	}

	/**
	 * Constructor - using user-defined field and line separators
	 * @param separator Field separator
	 * @param newLine Line separator
	 */
	public CsvFileWriter(String separator, String newLine) {
		super();
		this.separator = separator;
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

		// --- Initializations -----------
		boolean success = false;
		FileWriter fileWriter = null;
		
		try {
			// --- Create output file --------------
			fileWriter = new FileWriter(csvFile);
			
			if(headline != null){
				fileWriter.append(headline);
				fileWriter.append(this.newLine);
			}
			
			// --- Iterate over the data ----------
			for(int i=0; i<data.length; i++){
				for (int j=0; j<data[i].length; j++){
					// --- Append next data field
					fileWriter.append(data[i][j].toString());
					// --- Append field or line separator -----
					if (j<(data[i].length-1)){
						fileWriter.append(this.separator);
					} else {
						fileWriter.append(this.newLine);
					}
				}
			}
			success = true;
		
		} catch (IOException ioe) {
			System.err.println("Error writing CSV file!");
			ioe.printStackTrace();
			
		} finally {
			try {
				fileWriter.flush();
			} catch (IOException e) {
				System.out.println("Error while flushing fileWriter !!!");
                e.printStackTrace();
			}
			try {
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while closing fileWriter !!!");
                e.printStackTrace();
			}
		}
		return success;
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

		for(int i=0; i<data.size(); i++){
			dataArray[i] = data.get(i).toArray();
		}
		
		return this.exportData(csvFile, dataArray, headline);
	}
	
}