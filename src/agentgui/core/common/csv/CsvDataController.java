package agentgui.core.common.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.table.DefaultTableModel;

/**
 * Controller class for handling CSV data import and export
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
 *
 */
public class CsvDataController {
	/**
	 * The field separator for the CSV file
	 */
	private String separator;
	/**
	 * If true, the first row will be treated as headlines
	 */
	private boolean headlines;
	/**
	 * The data model
	 */
	private DefaultTableModel dataModel;

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public boolean hasHeadlines() {
		return headlines;
	}

	public void setHeadlines(boolean headlines) {
		this.headlines = headlines;
	}

	public DefaultTableModel getDataModel() {
		return dataModel;
	}


	public void setDataModel(DefaultTableModel dataModel) {
		this.dataModel = dataModel;
	}


	/**
	 * This method imports data from a CSV file
	 * @param csvFile The file to import from
	 */
	void doImport(File csvFile){
		
		if(csvFile != null){
			try {
				
				// Initialize input stream
				BufferedReader br = new BufferedReader(new FileReader(csvFile));
				
				// Get the first line
				String inBuffer = br.readLine();
				
				if(inBuffer == null){
					System.err.println("Empty File");
				}else{
					
					String[] parts = inBuffer.split(this.separator);
					
					if(this.headlines){
						
						// Use first line's contents as column headers
						this.dataModel = new DefaultTableModel(parts, 0);
						
					}else{
						
						// Use artificial column headers
						this.dataModel = new DefaultTableModel();
						for(int i=0; i<parts.length; i++){
							this.dataModel.addColumn("Column"+(i+1));
						}
						
						// First line contains data
						this.dataModel.addRow(parts);
					}

					// Read linewise, split and append to the table model
					while((inBuffer = br.readLine()) != null){
						parts = inBuffer.split(this.separator);
						this.dataModel.addRow(parts);
					}
				}
				
				br.close();
				
				
			} catch (FileNotFoundException e) {
				System.err.println("File not found!");
			} catch (IOException e) {
				System.err.println("Error reading file!");
			}
		}
		
	}
	
	
	/**
	 * This method exports data to a CSV file
	 * @param csvFile The file to export to
	 */
	void doExport(File csvFile){
		
		try {
			
			// --- Prepare the file writer --------------
			PrintWriter fw = new PrintWriter(csvFile);
			
			if(headlines){
				
				// Put the column titles in the first line
				
				StringBuffer columnTitles = new StringBuffer();
				for(int i=0; i<dataModel.getColumnCount(); i++){
					// Append the title of column i
					columnTitles.append(dataModel.getColumnName(i));
					
					// If more columns follow, append a semicolon
					if(i<dataModel.getColumnCount() -1){
						columnTitles.append(";");
					}
				}
				
				// Write to the file
				fw.println(columnTitles);
			}

			// --- Append the data rows --------------
			for(int i=0; i<dataModel.getRowCount(); i++){
				
				StringBuffer rowData = new StringBuffer();
				for(int j=0; j<dataModel.getColumnCount(); j++){
					rowData.append(dataModel.getValueAt(i, j));
					if(j < dataModel.getColumnCount() - 1){
						rowData.append(separator);
					}
				}
				
				fw.println(rowData);
			}
			
			// --- Close the file ---------------
			fw.flush();
			fw.close();
			
		} catch (FileNotFoundException e) {
			System.err.println("File not found!");
		}
	}
	
}
