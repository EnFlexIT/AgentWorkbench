package de.enflexit.common.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

public class CsvFileReader {
	
	private static final String DEFAULT_SEPARATOR = ";";

	private String separator;
	
	public CsvFileReader(){
		this(DEFAULT_SEPARATOR);
	}

	public CsvFileReader(String separator) {
		super();
		this.separator = separator;
	}
	
	public Vector<Vector<Object>> importData(File csvFile){
		Vector<Vector<Object>> importedData = null;
		
		if(csvFile == null){
			System.err.println("No CSV file selected");
		}else{
			BufferedReader fileReader = null;
			try {
				
				importedData = new Vector<Vector<Object>>();
				fileReader = new BufferedReader(new FileReader(csvFile));
				
				String line;
				while((line = fileReader.readLine()) != null){
					String[] tokens = line.split(this.separator);
					Vector<Object> lineData = new Vector<Object>(Arrays.asList(tokens));
					importedData.addElement(lineData);
				}
				
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
