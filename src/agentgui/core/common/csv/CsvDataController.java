/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.common.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;

import javax.swing.table.DefaultTableModel;

/**
 * Controller class for handling CSV data import and export
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
 */
public class CsvDataController extends Observable{
	
	/** Event code to inform observers that the table model was replaced */
	public static final int EVENT_TABLE_MODEL_REPLACED = 0;
	
	/** The file to import or to export*/
	private File file;
	/** The field separator for the CSV file */
	private String separator = ";";
	/** If true, the first row will be treated as headlines	 */
	private boolean headline = true;
	/** The data model */
	private DefaultTableModel dataModel;

	
	/**
	 * Instantiates a new CSV data controller.
	 */
	public CsvDataController() {
	}
	
	/**
	 * Instantiates a new CSV data controller for the import.
	 *
	 * @param file the file to import or to export
	 * @param separator the separator within the CSV file
	 * @param headline Indicate if there is a headline or not
	 * @param dataModel the data model
	 */
	public CsvDataController(File file, String separator, boolean headline) {
		this(file, separator, headline, null);
	}
	/**
	 * Instantiates a new CSV data controller for the import OR the export.
	 *
	 * @param file the file to import or to export
	 * @param separator the separator within the CSV file
	 * @param headline Indicate if there is a headline or not  
	 * @param dataModel the data model
	 */
	public CsvDataController(File file, String separator, boolean headline, DefaultTableModel dataModel) {
		this.setFile(file);
		this.setSeparator(separator);
		this.setHeadline(headline);
		this.setDataModel(dataModel);
	}

	
	/**
	 * Gets the file object of the CSV file.
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	/**
	 * Sets the file.
	 * @param file the new file
	 */
	public void setFile(File file) {
		this.file = file;
	}
	/**
	 * Gets the separator.
	 * @return the separator
	 */
	public String getSeparator() {
		return separator;
	}
	/**
	 * Sets the separator.
	 * @param separator the new separator
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * Checks for a headline.
	 * @return true, if the current model/file has a headline
	 */
	public boolean hasHeadlines() {
		return headline;
	}
	/**
	 * Sets if there is headline or not.
	 * @param hasHeadline the new headline
	 */
	public void setHeadline(boolean hasHeadline) {
		this.headline = hasHeadline;
	}

	/**
	 * Gets the data model.
	 * @return the data model
	 */
	public DefaultTableModel getDataModel() {
		return dataModel;
	}
	/**
	 * Sets the data model.
	 * @param dataModel the new data model
	 */
	public void setDataModel(DefaultTableModel dataModel) {
		this.dataModel = dataModel;
		setChanged();
		notifyObservers(EVENT_TABLE_MODEL_REPLACED);
	}

	/**
	 * This method imports data from a CSV file
	 */
	public void doImport(){

		if (this.getFile()==null) {
			System.err.println("No CSV file specified for import!");
			return;
		}
		
		try {
			// --- Initialise a BufferedReader ----------------------
			BufferedReader br = new BufferedReader(new FileReader(this.getFile()));
			// --- Do the actual import -----------------------------
			this.doImport(br);
			
		} catch (FileNotFoundException fnfe) {
			System.err.println("File not found: " + this.getFile().getAbsolutePath());
//			fnfe.printStackTrace();
		}
	}
	
	/**
	 * This method imports data from a CSV file.
	 * @param br the BufferedReader to use for the read operation
	 */
	public void doImport(BufferedReader br){
		
		try {
			// --- Get the first line -------------------------------
			String inBuffer = br.readLine();
			
			if(inBuffer==null){
				System.err.println("Empty File");
			} else {
				
				String[] parts = inBuffer.split(this.getSeparator());
				DefaultTableModel newTableModel;
				if(this.hasHeadlines()==true){
					// --- Use first row's contents as headers ----- 
					newTableModel = new DefaultTableModel(parts, 0);
				} else {
					// --- Use artificial column headers ------------
					newTableModel = new DefaultTableModel();
					for(int i=0; i<parts.length; i++){
						newTableModel.addColumn("Column"+(i+1));
					}
					// --- First row contains data -----------------
					newTableModel.addRow(parts);
				}
				// --- Read lines, split and append them to  model --
				while((inBuffer = br.readLine()) != null){
					parts = inBuffer.split(this.getSeparator());
					newTableModel.addRow(parts);
				}
				this.setDataModel(newTableModel);
			}
			br.close();
	
		} catch (IOException e) {
			System.err.println("Error reading file!");
		}
	}
	
	/**
	 * This method exports data to a CSV file
	 */
	public void doExport(){
		
		if (this.getFile()==null) {
			System.err.println("No CSV file specified for export!");
			return;
		}
		
		try {
			// --- Prepare the file writer --------------
			PrintWriter fw = new PrintWriter(this.getFile());
			if (this.hasHeadlines()==true) {
				// Put the column titles in the first line
				StringBuffer columnTitles = new StringBuffer();
				for(int i=0; i<this.getDataModel().getColumnCount(); i++){
					// Append the title of column i
					columnTitles.append(this.getDataModel().getColumnName(i));
					// If more columns follow, append a semicolon
					if(i<this.getDataModel().getColumnCount() -1){
						columnTitles.append(this.getSeparator());
					}
				}
				// Write to the file
				fw.println(columnTitles);
			}

			// --- Append the data rows --------------
			for(int i=0; i<this.getDataModel().getRowCount(); i++){
				
				StringBuffer rowData = new StringBuffer();
				for(int j=0; j<this.getDataModel().getColumnCount(); j++){
					rowData.append(this.getDataModel().getValueAt(i, j));
					if(j < this.getDataModel().getColumnCount() - 1){
						rowData.append(this.getSeparator());
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
