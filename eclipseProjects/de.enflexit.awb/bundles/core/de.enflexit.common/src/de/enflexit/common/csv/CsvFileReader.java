package de.enflexit.common.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

/**
 * A generic CSV file reader implementation.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class CsvFileReader {
	
	public static final String DEFAULT_SEPARATOR = ";";

	private String columnSeparator;
	
	/**
	 * Instantiates a new CSV file reader, using the default column separator.
	 */
	public CsvFileReader(){
		this(DEFAULT_SEPARATOR);
	}

	/**
	 * Instantiates a new CSV file reader, using the specified column separator.
	 * @param columnSeparator the separator
	 */
	public CsvFileReader(String columnSeparator) {
		this.columnSeparator = columnSeparator;
	}
	
	/**
	 * Imports data from the specified CSV file.
	 * @param csvFile the csv file
	 * @return the imported data vector
	 */
	public Vector<Vector<Object>> importData(File csvFile){

		Vector<Vector<Object>> importedData = null;
		if (csvFile == null || csvFile.exists()==false) {
			System.err.println("No CSV file selected");
			
		} else {

			BufferedReader fileReader = null;
			try {
				
				importedData = new Vector<Vector<Object>>();
				fileReader = new BufferedReader(new FileReader(csvFile));
				
				String line;
				while((line = fileReader.readLine()) != null){
					String[] tokens = line.split(this.columnSeparator);
					Vector<Object> lineData = new Vector<Object>(Arrays.asList(tokens));
					importedData.addElement(lineData);
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					fileReader.close();
				} catch (IOException e) {
					System.err.println("Error closing file reader");
					e.printStackTrace();
				}
			}
		}
		return importedData;
	}
	
}
