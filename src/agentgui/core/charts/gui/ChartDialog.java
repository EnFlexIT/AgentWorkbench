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
package agentgui.core.charts.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.charts.DataModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.SettingsInfo;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.ontology.Chart;
import agentgui.ontology.DataSeries;
import agentgui.ontology.ValuePair;

/**
 * The abstract Class ChartDialog can be used as super class for any type of
 * chart dialog.
 */
public abstract class ChartDialog extends JDialog implements ActionListener, Observer {

	private static final long serialVersionUID = -6509600926902216133L;
	
	// Swing components
	protected JToolBar toolBar;
	protected JTabbedPane tabbedPane;
	protected JPanel buttonPane;
	protected JButton btnApply;
	protected JButton btnCancel;
	protected JButton btnImport;
	
	// Tab contents
	protected ChartTab chartTab;
	protected TableTab tableTab;
	protected SettingsTab settingsTab;
	
	/**
	 * The data model for this chart
	 */
	protected DataModel model;
	/**
	 * Was the dialog left via cancel button?
	 */
	protected boolean canceled = false;
	/**
	 * A thumbnail of the current chart
	 */
	protected BufferedImage chartThumb = null;
	
	public ChartDialog(Window owner, Chart chart){
		
	}
	
	protected JToolBar getToolBar(){
		if(toolBar == null){
			toolBar = new JToolBar();
			toolBar.add(getBtnImport());
		}
		return toolBar;
	}
	
	protected JTabbedPane getTabbedPane(){
		if(tabbedPane == null){
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Chart", getChartTab());
			tabbedPane.addTab("Table", getTableTab());
			tabbedPane.addTab("Settings", getSettingsTab());
		}
		return tabbedPane;
	}
	
	protected abstract ChartTab getChartTab();
	
	protected abstract TableTab getTableTab();
	
	protected SettingsTab getSettingsTab(){
		if(settingsTab == null){
			settingsTab = new SettingsTab(model);
			settingsTab.addObserver(this);
		}
		return settingsTab;
	}
	
	protected JPanel getButtonPane(){
		if(buttonPane == null){
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPane.add(getBtnApply());
			buttonPane.add(getBtnCancel());
		}
		return buttonPane;
	}
	
	protected JButton getBtnApply(){
		if(btnApply == null){
			btnApply = new JButton("OK");
			btnApply.addActionListener(this);
		}
		return btnApply;
	}
	
	protected JButton getBtnCancel(){
		if(btnCancel == null){
			btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}
	protected JButton getBtnImport() {
		if (btnImport == null) {
			btnImport = new JButton("");
			btnImport.setIcon(new ImageIcon(getClass().getResource(GraphGlobals.getPathImages() + "import.png")));
			btnImport.setToolTipText(Language.translate("Neue Datenreihe(n) importieren"));
			btnImport.addActionListener(this);
		}
		return btnImport;
	}
	
	public BufferedImage getChartThumb() {
		if(chartThumb == null){
			chartThumb = chartTab.createChartThumb();
		}
		return chartThumb;
	}

	/**
	 * @return the model
	 */
	public DataModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(DataModel model) {
		this.model = model;
	}
	/**
	 * @return Was this dialog left using the cancel button?
	 */
	public boolean isCanceled() {
		return canceled;
	}
	/**
	 * Get a key / x value of the correct type for this chart from a string representation. 
	 * @param key The string representation of the key / x value
	 * @return The key / x value
	 */
	protected abstract Number parseKey(String key);
	
	/**
	 * Get a (y) value of the correct type for this chart from a string representation.
	 * @param value the string representation of the (y) value
	 * @return The (y) value
	 */
	protected abstract Number parseValue(String value);
	
	/**
	 * Imports a data series from a CSV file.
	 * @param csvFile The CSV file
	 */
	protected void importDataSeriesFromCSV(File csvFile) {
		// Read the CSV data
		BufferedReader csvFileReader;
		System.out.println("Importing CSV data from "+csvFile.getName());
		
		DataSeries[] importedSeries = null;
		
		// Read the data from the file
		try {
			csvFileReader = new BufferedReader(new FileReader(csvFile));
			String inBuffer = null;
			while((inBuffer = csvFileReader.readLine()) != null){
				// Regex matches two or more numbers (with or without decimals) separated by ;
				if(inBuffer.matches("[\\d]+\\.?[\\d]*[;[\\d]+\\.?[\\d]*]+")){
					String[] parts = inBuffer.split(";");
					if(importedSeries == null && parts.length > 0){
						importedSeries = new DataSeries[parts.length-1];
						
						for(int k=0; k<parts.length-1; k++){
							importedSeries[k] = model.createNewDataSeries(null);
						}
					}
					
					// First column contains the key / x value
					Number key = parseKey(parts[0]);
					
					// Later columns contain data
					for(int i=1; i<parts.length; i++){
						
						if(parts[i].length() > 0){
							// Empty string -> no value for this key in this series -> no new value pair
							
							Number value = parseValue(parts[i]);
							
							ValuePair valuePair = model.createNewValuePair(key, value);
							
							model.getValuePairsFromSeries(importedSeries[i-1]).add(valuePair);
						}
						
					}
				}
			}
			
			csvFileReader.close();
			
			for(int j=0; j < importedSeries.length; j++){
				model.addSeries(importedSeries[j]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// Handle settings changes
				if(arg instanceof SettingsInfo){
					SettingsInfo info = (SettingsInfo) arg;
					if(info.getType() == SettingsInfo.RENDERER_CHANGED){
						chartTab.setRenderer((String) info.getData());
					}else if(info.getType() == SettingsInfo.CHART_TITLE_CHANGED){
						String title = (String) info.getData();
						model.getOntologyModel().getChartSettings().setChartTitle(title);
						chartTab.getChart().setTitle(title);
					}else if(info.getType() == SettingsInfo.X_AXIS_LABEL_CHANGED){
						String label = (String) info.getData();
						model.getOntologyModel().getChartSettings().setXAxisLabel(label);
						chartTab.setXAxisLabel(label);
						model.getTableModel().setKeyColumnLabel(label);
					}else if(info.getType() == SettingsInfo.Y_AXIS_LABEL_CHANGED){
						String label = (String) info.getData();
						model.getOntologyModel().getChartSettings().setYAxisLabel(label);
						chartTab.setYAxisLabel(label);
					}else if(info.getType() == SettingsInfo.SERIES_LABEL_CHANGED){
						String label = (String) info.getData();
						int seriesIndex = info.getSeriesIndex();
						try {
							if(seriesIndex < model.getSeriesCount()){
								model.getTableModel().setSeriesLabel(seriesIndex, label);
								DataSeries series = model.getOntologyModel().getSeries(seriesIndex);
								series.setLabel(label);
								model.getChartModel().getSeries(seriesIndex).setKey(label);
							}else{
								throw new NoSuchSeriesException();
							}
						} catch (NoSuchSeriesException e) {
							System.err.println("Error setting label for series "+seriesIndex);
							e.printStackTrace();
						}
						
					}else if(info.getType() == SettingsInfo.SERIES_COLOR_CHANGED){
						Color color = (Color) info.getData();
						int seriesIndex = info.getSeriesIndex();
						try {
							if(seriesIndex < model.getSeriesCount()){
								model.getOntologyModel().getChartSettings().getYAxisColors().remove(seriesIndex);
								model.getOntologyModel().getChartSettings().getYAxisColors().add(seriesIndex, ""+color.getRGB());
								chartTab.setSeriesColor(seriesIndex, color);
							}else{
									throw new NoSuchSeriesException();
							}
						} catch (NoSuchSeriesException e) {
							System.err.println("Error setting color for series "+seriesIndex);
							e.printStackTrace();
						}
					}else if(info.getType() == SettingsInfo.SERIES_LINE_WIDTH_CHANGED){
						float lineWidth = (Float) info.getData();
						int seriesIndex = info.getSeriesIndex();
						
						try {
							if(seriesIndex < model.getSeriesCount()){
								model.getOntologyModel().getChartSettings().getYAxisLineWidth().remove(seriesIndex);
								model.getOntologyModel().getChartSettings().getYAxisLineWidth().add(seriesIndex, lineWidth);
								chartTab.setSeriesLineWidth(seriesIndex, lineWidth);
							}else{
									throw new NoSuchSeriesException();
							}
						} catch (NoSuchSeriesException e) {
							System.err.println("Error setting color for series "+seriesIndex);
							e.printStackTrace();
						}
					}else if(info.getType() == SettingsInfo.SERIES_ADDED){
						getChartTab().applyColorSettings();
						getChartTab().applyLineWidthsSettings();
					}
				}

	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == btnApply){
			canceled = false;
			chartThumb = getChartTab().createChartThumb();
			setVisible(false);
		}else if(ae.getSource() == btnCancel){
			canceled = true;
			setVisible(false);
		}else 
		if (ae.getSource() == btnImport){
			
			// Import CSV data

			// Choose file
			JFileChooser jFileChooserImportCSV = new JFileChooser(Application.getGlobalInfo().getLastSelectedFolder());
			jFileChooserImportCSV.setFileFilter(new FileNameExtensionFilter(Language.translate("CSV-Dateien"), "csv"));
			if(jFileChooserImportCSV.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				Application.getGlobalInfo().setLastSelectedFolder(jFileChooserImportCSV.getCurrentDirectory());
				File csvFile = jFileChooserImportCSV.getSelectedFile();
				
				// Import data
				importDataSeriesFromCSV(csvFile);
			}
		}

	}

}
