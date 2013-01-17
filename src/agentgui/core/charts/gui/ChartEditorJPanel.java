package agentgui.core.charts.gui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.charts.DataModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.SettingsInfo;
import agentgui.core.ontologies.gui.DynForm;
import agentgui.core.ontologies.gui.OntologyClassEditorJPanel;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.ontology.DataSeries;
import agentgui.ontology.ValuePair;

/**
 * General superclass for OntologyClassEditorJPanel implementations for charts.
 * @author Nils
 */
public abstract class ChartEditorJPanel extends OntologyClassEditorJPanel implements ActionListener, Observer {
	
	// TODO Import-Zeugs ins Model?

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -306986715544317480L;
	
	// Swing components
	protected JToolBar toolBar;
	protected JTabbedPane tabbedPane;
	protected JButton btnImport;
	
	// Tab contents
	protected ChartTab chartTab;
	protected TableTab tableTab;
	protected SettingsTab settingsTab;
	
	/**
	 * The data model for this chart
	 */
	protected DataModel model;

	public ChartEditorJPanel(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
		
		this.setLayout(new BorderLayout());
		this.add(getToolBar(), BorderLayout.NORTH);
		this.add(getTabbedPane(), BorderLayout.CENTER);
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
	
	protected JButton getBtnImport() {
		if (btnImport == null) {
			btnImport = new JButton("");
			btnImport.setIcon(new ImageIcon(getClass().getResource(GraphGlobals.getPathImages() + "import.png")));
			btnImport.setToolTipText(Language.translate("Neue Datenreihe(n) importieren"));
			btnImport.addActionListener(this);
		}
		return btnImport;
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
	
	public BufferedImage getChartThumb(){
		return this.getChartTab().getChartThumb();
	}

}
