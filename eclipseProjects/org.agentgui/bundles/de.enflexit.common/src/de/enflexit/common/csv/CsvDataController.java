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
package de.enflexit.common.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Vector;

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
	private DefaultTableModel tableModel;

	
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
		this.setTableModel(dataModel);
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
		return tableModel;
	}
	/**
	 * Sets the data model.
	 * @param dataModel the new data model
	 */
	public void setTableModel(DefaultTableModel dataModel) {
		this.tableModel = dataModel;
		setChanged();
		notifyObservers(EVENT_TABLE_MODEL_REPLACED);
	}

	/**
	 * This method imports data from a CSV file
	 */
	public void doImport(){

		if (this.getFile()==null) {
			System.err.println("No CSV file specified for import!");
		}else{
			CsvFileReader fileReader = new CsvFileReader(this.separator);
			Vector<Vector<Object>> importedData = fileReader.importData(this.file);
			if(importedData != null){
				Vector<Object> headlines;
				if(this.hasHeadlines()){
					headlines = importedData.get(0);
					importedData.remove(0);
				}else{
					headlines = new Vector<Object>();
					int columnCount = importedData.get(0).size();
					for(int i=0; i<columnCount; i++){
						headlines.addElement("Column "+(i+1));
					}
				}
				
				DefaultTableModel tableModel = new DefaultTableModel();
				tableModel.setDataVector(importedData, headlines);
				this.setTableModel(tableModel);
			}
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
				this.setTableModel(newTableModel);
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
		
		@SuppressWarnings("unchecked")
		Vector<Vector<Object>> tableData = new Vector<>(this.getDataModel().getDataVector());
		
		if(this.hasHeadlines() == true){
			Vector<Object> columnTitles = new Vector<Object>();
			for(int i=0; i<this.tableModel.getColumnCount(); i++){
				columnTitles.add(this.tableModel.getColumnName(i));
			}
			tableData.add(0, columnTitles);
		}
		
		CsvFileWriter fileWriter = new CsvFileWriter();
		fileWriter.exportData(file, tableData);
		
	}
	
}
