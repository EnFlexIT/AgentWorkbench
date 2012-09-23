package agentgui.core.charts.timeseriesChart.gui;

import java.awt.BorderLayout;
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
import javax.swing.JToolBar;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.charts.SettingsInfo;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.ontology.Simple_Float;
import agentgui.ontology.Simple_Long;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesChart;
import agentgui.ontology.TimeSeriesValuePair;

public class TimeSeriesChartDialog extends JDialog implements ActionListener, Observer{

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -2712116890492352158L;
	
	// Swing components
	private JToolBar toolBar;
	private JTabbedPane tabbedPane;
	private JPanel buttonPane;
	private JButton btnApply;
	private JButton btnCancel;
	private JButton btnImport;
	
	// Tab contents
	private TimeSeriesChartTab chartTab;
	private TimeSeriesTableTab tableTab;
	private TimeSeriesSettingsTab settingsTab;
	
	/**
	 * The data model for this chart
	 */
	private TimeSeriesDataModel model;
	/**
	 * Was the dialog left via cancel button?
	 */
	private boolean canceled = false;
	/**
	 * A thumbnail of the current chart
	 */
	private BufferedImage chartThumb = null;

	/**
	 * Create the dialog.
	 */
	public TimeSeriesChartDialog(Window owner, TimeSeriesChart chart) {
		
		super(owner);
		this.setModal(true);
		
		setSize(600, 450);
		
		this.model = new TimeSeriesDataModel(chart);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getToolBar(), BorderLayout.NORTH);
		getContentPane().add(getTabbedPane(), BorderLayout.CENTER);
		getContentPane().add(getButtonPane(), BorderLayout.SOUTH);
	}
	
	private JToolBar getToolBar(){
		if(toolBar == null){
			toolBar = new JToolBar();
			toolBar.add(getBtnImport());
		}
		return toolBar;
	}
	
	private JTabbedPane getTabbedPane(){
		if(tabbedPane == null){
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Chart", getChartTab());
			tabbedPane.addTab("Table", getTableTab());
			tabbedPane.addTab("Settings", getSettingsTab());
		}
		return tabbedPane;
	}
	
	private TimeSeriesChartTab getChartTab(){
		if(chartTab == null){
			chartTab = new TimeSeriesChartTab(this.model);
		}
		return chartTab;
	}
	
	private TimeSeriesTableTab getTableTab(){
		if(tableTab == null){
			tableTab = new TimeSeriesTableTab(this.model);
		}
		return tableTab;
	}
	
	private TimeSeriesSettingsTab getSettingsTab(){
		if(settingsTab == null){
			settingsTab = new TimeSeriesSettingsTab(model);
			settingsTab.addObserver(this);
		}
		return settingsTab;
	}
	
	private JPanel getButtonPane(){
		if(buttonPane == null){
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPane.add(getBtnApply());
			buttonPane.add(getBtnCancel());
		}
		return buttonPane;
	}
	
	private JButton getBtnApply(){
		if(btnApply == null){
			btnApply = new JButton("OK");
			btnApply.addActionListener(this);
		}
		return btnApply;
	}
	
	private JButton getBtnCancel(){
		if(btnCancel == null){
			btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}
	private JButton getBtnImport() {
		if (btnImport == null) {
			btnImport = new JButton("");
			btnImport.setIcon(new ImageIcon(getClass().getResource(GraphGlobals.getPathImages() + "import.png")));
			btnImport.setToolTipText(Language.translate("Neue Datenreihe(n) importieren"));
			btnImport.addActionListener(this);
		}
		return btnImport;
	}
	
	/**
	 * Imports time series from a CSV file.
	 * !!! Time values are assumed to be minutes! Must be made configurable later! !!! 
	 * @param csvFile The CSV file
	 */
	private void importDataSeriesFromCSV(File csvFile) {
		// Read the CSV data
		BufferedReader csvFileReader;
		System.out.println("Importing CSV data from "+csvFile.getName());
		
		TimeSeries[] importedSeries = null;
		
		// Read the data from the file
		try {
			csvFileReader = new BufferedReader(new FileReader(csvFile));
			String inBuffer = null;
			while((inBuffer = csvFileReader.readLine()) != null){
				// Regex matches two or more numbers (with or without decimals) separated by ;
				if(inBuffer.matches("[\\d]+\\.?[\\d]*[;[\\d]+\\.?[\\d]*]+")){
					String[] parts = inBuffer.split(";");
					if(importedSeries == null && parts.length > 0){
						importedSeries = new TimeSeries[parts.length-1];
						
						for(int k=0; k<parts.length-1; k++){
							importedSeries[k] = new TimeSeries();
						}
					}
					
					// First column contains the time
					int minutes = Integer.parseInt(parts[0]);
					Simple_Long timestamp = new Simple_Long();
					timestamp.setLongValue(model.getStartDate().getTime() + minutes * 60000);
					
					// Later columns contain data
					for(int i=1; i<parts.length; i++){
						
						if(parts[i].length() > 0){
							// Empty string -> no value for this series at time -> no new value pair
							Simple_Float value = new Simple_Float();
							value.setFloatValue(Float.parseFloat(parts[i]));
							
							TimeSeriesValuePair valuePair = new TimeSeriesValuePair();
							valuePair.setTimestamp(timestamp);
							valuePair.setValue(value);
							
							importedSeries[i-1].addTimeSeriesValuePairs(valuePair);
						}
						
					}
				}
			}
			
			csvFileReader.close();
			
			for(int j=0; j < importedSeries.length; j++){
				model.addSeries(importedSeries[j]);
				settingsTab.addSeries(importedSeries[j]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	public TimeSeriesDataModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(TimeSeriesDataModel model) {
		this.model = model;
	}

	public boolean isCanceled() {
		return canceled;
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

	@Override
	public void update(Observable o, Object arg) {
		
		// Handle settings changes
		if(arg instanceof SettingsInfo){
			SettingsInfo info = (SettingsInfo) arg;
			if(info.getType() == SettingsInfo.RENDERER_CHANGED){
				chartTab.setRenderer((String) info.getData());
			}else if(info.getType() == SettingsInfo.CHART_TITLE_CHANGED){
				String title = (String) info.getData();
				model.getOntologyModel().getGeneralSettings().setChartTitle(title);
				chartTab.getChart().setTitle(title);
			}else if(info.getType() == SettingsInfo.X_AXIS_LABEL_CHANGED){
				String label = (String) info.getData();
				model.getOntologyModel().getGeneralSettings().setXAxisLabel(label);
				chartTab.setXAxisLabel(label);
				model.getTableModel().setTimeColumnLabel(label);
			}else if(info.getType() == SettingsInfo.Y_AXIS_LABEL_CHANGED){
				String label = (String) info.getData();
				model.getOntologyModel().getGeneralSettings().setYAxisLabel(label);
				chartTab.setYAxisLabel(label);
			}else if(info.getType() == SettingsInfo.SERIES_LABEL_CHANGED){
				String label = (String) info.getData();
				int seriesIndex = info.getSeriesIndex();
				try {
					if(seriesIndex < model.getSeriesCount()){
						model.getTableModel().setSeriesLabel(seriesIndex, label);
						TimeSeries series = (TimeSeries) model.getOntologyModel().getChartData().get(seriesIndex);
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
						model.getOntologyModel().getGeneralSettings().getYAxisColors().remove(seriesIndex);
						model.getOntologyModel().getGeneralSettings().getYAxisColors().add(seriesIndex, ""+color.getRGB());
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
						model.getOntologyModel().getGeneralSettings().getYAxisLineWidth().remove(seriesIndex);
						model.getOntologyModel().getGeneralSettings().getYAxisLineWidth().add(seriesIndex, lineWidth);
						chartTab.setSeriesLineWidth(seriesIndex, lineWidth);
					}else{
							throw new NoSuchSeriesException();
					}
				} catch (NoSuchSeriesException e) {
					System.err.println("Error setting color for series "+seriesIndex);
					e.printStackTrace();
				}
			}
		}
	}
	

}
