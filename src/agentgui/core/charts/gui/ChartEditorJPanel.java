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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.charts.DataModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.SettingsInfo;
import agentgui.core.gui.imaging.ConfigurableFileFilter;
import agentgui.core.gui.imaging.ImageFileView;
import agentgui.core.gui.imaging.ImagePreview;
import agentgui.core.gui.imaging.ImageUtils;
import agentgui.core.ontologies.gui.DynForm;
import agentgui.core.ontologies.gui.OntologyClassEditorJPanel;
import agentgui.ontology.DataSeries;
import agentgui.ontology.ValuePair;

/**
 * General superclass for OntologyClassEditorJPanel implementations for charts.
 * @author Nils
 */
public abstract class ChartEditorJPanel extends OntologyClassEditorJPanel implements ActionListener, FocusListener, Observer {
	
	private static final long serialVersionUID = -306986715544317480L;
	
	private static final Integer DEFAULT_ImageWidth = 1600; 
	private static final Integer DEFAULT_ImageHeight = 900;
	
	private final String pathImage = Application.getGlobalInfo().PathImageIntern(); // @jve:decl-index=0:
    private final Dimension jButtonSize = new Dimension(26, 26); // @jve:decl-index=0:
     
	// Swing components
	protected JToolBar toolBar;
	protected JTabbedPane tabbedPane;
	protected JButton btnImport;
	protected JButton btnSaveImage = null;
	protected JLabel jLabelExport = null;
	protected JComboBox cbImageAspectRatio = null;
	protected JTextField tfImageWidth = null;
	protected JLabel jLabelX = null;
	protected JTextField tfImageHeight= null;
	
	// Tab contents
	protected ChartTab chartTab;
	protected TableTab tableTab;
	protected ChartSettingsTab settingsTab;
	
	/**
	 * The data model for this chart
	 */
	protected DataModel model;

	public ChartEditorJPanel(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
		
		this.setLayout(new BorderLayout());
		this.add(getToolBar(), BorderLayout.NORTH);
		this.add(getTabbedPane(), BorderLayout.CENTER);
		
		this.model.addObserver(this);
		this.model.getChartSettings().addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		
		if (this.model==null) return;
		
		// --- Handle changes of the chart data 
		if(o == this.model){
			setNewOntologyClassInstance(this.getOntologyClassInstance());
		}
		
		
		// --- Handle changes of the chart settings
		else if (o == this.model.getChartSettings()){
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
		}else{
			super.update(o, arg);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource() == btnImport){
			// --- Import CSV data / Choose file ----------
			JFileChooser jFileChooserImportCSV = new JFileChooser(Application.getGlobalInfo().getLastSelectedFolder());
			jFileChooserImportCSV.setFileFilter(new FileNameExtensionFilter(Language.translate("CSV-Dateien"), "csv"));
			jFileChooserImportCSV.setDialogTitle(Language.translate("CSV-Datei importieren"));
			if(jFileChooserImportCSV.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				Application.getGlobalInfo().setLastSelectedFolder(jFileChooserImportCSV.getCurrentDirectory());
				File csvFile = jFileChooserImportCSV.getSelectedFile();
				
				// --- Import data ------------------------
				this.importDataSeriesFromCSV(csvFile);
				setNewOntologyClassInstance(this.getOntologyClassInstance());
			}
			
		} else if(ae.getSource() == btnSaveImage) {
			// --- Export the image ---------------------------------
			this.saveAsImage();
		} else if(ae.getSource() == tfImageWidth) {
			// --- Recalculate image size when enter was pressed ----
			this.recalculateImageHeight();
		}else if(ae.getSource() == tfImageHeight){
			// --- Recalculate image size when enter was pressed-----
			this.recalculateImageWidth();
		}else if(ae.getSource() == cbImageAspectRatio){
			// --- Recalculate image size when aspect ratio changed -
			this.recalculateImageHeight();
		}

	}

	/**
	 * Gets the JToolBar for an editor panel.
	 * @return the JToolBar
	 */
	protected JToolBar getToolBar(){
		if(toolBar == null){
			
			jLabelX = new JLabel("x");
			jLabelX.setFont(new Font("Arial", Font.PLAIN, 12));
			
			jLabelExport = new JLabel(Language.translate("Export:"));
			jLabelExport.setFont(new Font("Arial", Font.BOLD, 12));
			
			toolBar = new JToolBar();
			toolBar.setFloatable(false);
			toolBar.setRollover(true);
			toolBar.add(getBtnImport());
			toolBar.addSeparator();
			toolBar.add(jLabelExport);
			toolBar.add(getCbImageAspectRatio());
			toolBar.add(getTfImageWidth());
			toolBar.add(jLabelX);
			toolBar.add(getTfImageHeight());
			toolBar.add(getBtnSaveImage());
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
	
	protected abstract ChartSettingsTab getSettingsTab();
	
	protected JButton getBtnImport() {
		if (btnImport == null) {
			btnImport = new JButton("");
			btnImport.setIcon(new ImageIcon(getClass().getResource(pathImage + "MBtransImport.png")));
			btnImport.setPreferredSize(jButtonSize);
			btnImport.setToolTipText(Language.translate("Neue Datenreihe(n) importieren"));
			btnImport.addActionListener(this);
		}
		return btnImport;
	}
	
	/**
     * This method initializes jButtonSaveImage
     * @return javax.swing.JButton
     */
    protected JButton getBtnSaveImage() {
		if (btnSaveImage == null) {
			btnSaveImage = new JButton();
			btnSaveImage.setIcon(new ImageIcon(getClass().getResource(pathImage  + "SaveAsImage.png")));
			btnSaveImage.setPreferredSize(jButtonSize);
			btnSaveImage.setToolTipText(Language.translate("Als Bild exportieren"));
			btnSaveImage.addActionListener(this);
		}
		return btnSaveImage;
    }
    
    protected JComboBox getCbImageAspectRatio(){
    	if(cbImageAspectRatio == null){
    		DefaultComboBoxModel formatsModel = new DefaultComboBoxModel(new String[]{"16:9", "4:3"});
    		cbImageAspectRatio = new JComboBox(formatsModel);
    		cbImageAspectRatio.setToolTipText(Language.translate("Das Seitenverhältnis des exportierten Bildes"));
    		cbImageAspectRatio.addActionListener(this);
    	}
    	return cbImageAspectRatio;
    }
    
	protected JTextField getTfImageWidth() {
		if(tfImageWidth == null){
			tfImageWidth = new JTextField(DEFAULT_ImageWidth.toString());
			tfImageWidth.setToolTipText(Language.translate("Die Breite des exportieren Bildes"));
			tfImageWidth.setPreferredSize(new Dimension(50, 26));
			tfImageWidth.setHorizontalAlignment(JTextField.CENTER);
			tfImageWidth.addActionListener(this);
			tfImageWidth.addFocusListener(this);
			tfImageWidth.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent ke) {
					char charackter = ke.getKeyChar();
					String singleChar = Character.toString(charackter);
					if (singleChar.matches("[0-9]")==false) {
						ke.consume();	
						return;
					}
				}
				@Override
				public void keyReleased(KeyEvent e) {
					String width = tfImageWidth.getText();
					if (width!=null && width.equals("")==false) {
						recalculateImageHeight();	
					}
				}
			});
		}
		return tfImageWidth;
	}
    
	protected JTextField getTfImageHeight(){
    	if(tfImageHeight == null){
    		tfImageHeight = new JTextField(DEFAULT_ImageHeight.toString());
    		tfImageHeight.setToolTipText(Language.translate("Die Höhe des exportieren Bildes"));
    		tfImageHeight.setPreferredSize(new Dimension(50, 26));
    		tfImageHeight.setHorizontalAlignment(JTextField.CENTER);
    		tfImageHeight.addActionListener(this);
    		tfImageHeight.addFocusListener(this);
    		tfImageHeight.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent ke) {
					char charackter = ke.getKeyChar();
					String singleChar = Character.toString(charackter);
					if (singleChar.matches("[0-9]")==false) {
						ke.consume();	
						return;
					}
				}
				@Override
				public void keyReleased(KeyEvent e) {
					String height = tfImageHeight.getText();
					if (height!=null && height.equals("")==false) {
						recalculateImageWidth();	
					}
				}
			});
    	}
    	return tfImageHeight;
    }
	
	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent arg0) {
		// --- Not required ---
	}
	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent fe) {
		// --- Recalculate image size when leaving one of the text fields
		if(fe.getSource()==tfImageWidth){
			this.recalculateImageHeight();
		} else if(fe.getSource()==tfImageHeight) {
			this.recalculateImageWidth();
		}
	}
	
	private void setImageDefaultValues() {
		this.getTfImageWidth().setText(DEFAULT_ImageWidth.toString());
		this.getTfImageHeight().setText(DEFAULT_ImageHeight.toString());
	}
	
	private void recalculateImageHeight(){
		String imageWidthString = this.getTfImageWidth().getText();
		if (imageWidthString==null || imageWidthString.equals("")) {
			JOptionPane.showMessageDialog(this, Language.translate("Bitte nur ganze Zahlen größer 0 eingeben!"), Language.translate("Fehlerhafte Eingabe"), JOptionPane.ERROR_MESSAGE);
			this.setImageDefaultValues();
			return;
		} 
		int imageWidth = Integer.parseInt(imageWidthString);
		this.getTfImageHeight().setText(calculateImageHeight(imageWidth).toString());

	}
	private void recalculateImageWidth(){
		String imageHeightString = this.getTfImageHeight().getText();
		if (imageHeightString==null || imageHeightString.equals("")) {
			JOptionPane.showMessageDialog(this, Language.translate("Bitte nur ganze Zahlen größer 0 eingeben!"), Language.translate("Fehlerhafte Eingabe"), JOptionPane.ERROR_MESSAGE);
			this.setImageDefaultValues();
			return;
		} 
		int imageHeight = Integer.parseInt(imageHeightString);
		this.getTfImageWidth().setText(calculateImageWidth(imageHeight).toString());

	}
	
	private Integer calculateImageWidth(int imageHeight){
		if(getCbImageAspectRatio().getSelectedItem().equals("4:3")){
			return imageHeight / 3 * 4;
		}else{
			return imageHeight / 9 * 16;
		}
	}
	private Integer calculateImageHeight(int imageWidth){
		if(getCbImageAspectRatio().getSelectedItem().equals("4:3")){
			return imageWidth / 4 * 3;
		}else{
			return imageWidth / 16 * 9;
		}
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Export chart as image.
	 *
	 * @param width the width
	 * @param height the height
	 * @return the buffered image
	 */
	public BufferedImage exportChartAsImage(int width, int height){
		return this.getChartTab().exportAsImage(width, height, false);
	}
	
	private void saveAsImage(){
		
		// --- Determine image size ----------------------------
		Integer imageWidth = Integer.parseInt(this.getTfImageWidth().getText());
		if (imageWidth==0) {
			JOptionPane.showMessageDialog(this, Language.translate("Bitte nur ganze Zahlen größer 0 eingeben!"), Language.translate("Fehlerhafte Eingabe"), JOptionPane.ERROR_MESSAGE);
			this.setImageDefaultValues();
			return;
		}
		Integer imageHeight = Integer.parseInt(this.getTfImageHeight().getText());
		if (imageHeight==0) {
			JOptionPane.showMessageDialog(this, Language.translate("Bitte nur ganze Zahlen größer 0 eingeben!"), Language.translate("Fehlerhafte Eingabe"), JOptionPane.ERROR_MESSAGE);
			this.setImageDefaultValues();
			return;
		}
		
		String currentFolder = null;
		if (Application.getGlobalInfo()!=null) {
			// --- Get the last selected folder of Agent.GUI ---
			currentFolder = Application.getGlobalInfo().getLastSelectedFolderAsString();
		}

		// --- Create instance of JFileChooser -----------------
		JFileChooser jfc = new JFileChooser();
		jfc.setMultiSelectionEnabled(false);
		jfc.setAcceptAllFileFilterUsed(false);

		// --- Add custom icons for file types. ----------------
		jfc.setFileView(new ImageFileView());
		// --- Add the preview pane. ---------------------------
		jfc.setAccessory(new ImagePreview(jfc));

		// --- Set the file filter -----------------------------
		String[] extensionsJPEG = {ImageUtils.jpg, ImageUtils.jpeg};

		ConfigurableFileFilter filterJPG = new ConfigurableFileFilter(extensionsJPEG, "JPEG - Image");
		ConfigurableFileFilter filterPNG = new ConfigurableFileFilter(ImageUtils.png, "PNG - File");
		ConfigurableFileFilter filterGIF = new ConfigurableFileFilter(ImageUtils.gif, "GIF - Image");

		jfc.addChoosableFileFilter(filterGIF);
		jfc.addChoosableFileFilter(filterJPG);
		jfc.addChoosableFileFilter(filterPNG);

		jfc.setFileFilter(filterPNG);

		// --- Maybe set the current directory -----------------
		if (currentFolder != null) {
			jfc.setCurrentDirectory(new File(currentFolder));
		}
		
		// === Show dialog and wait on user action =============
		int state = jfc.showSaveDialog(this);
		
		if (state == JFileChooser.APPROVE_OPTION) {
			ConfigurableFileFilter cff = (ConfigurableFileFilter) jfc.getFileFilter();
			String selectedExtension = cff.getFileExtension()[0];
			String mustExtension = "." + selectedExtension;

			File selectedFile = jfc.getSelectedFile();
			if (selectedFile != null) {
				String selectedPath = selectedFile.getAbsolutePath();
				if (selectedPath.endsWith(mustExtension) == false) {
					selectedPath = selectedPath + mustExtension;
				}
				
				BufferedImage image = this.exportChartAsImage(imageWidth, imageHeight);
				this.writeImageFile(image, selectedPath, selectedExtension);
				
				if (Application.getGlobalInfo() != null) {
					Application.getGlobalInfo().setLastSelectedFolder(jfc.getCurrentDirectory());
				}
			}
		}
	}
	
	private void writeImageFile(BufferedImage image, String path, String extension){
		// --- Overwrite existing file ? ------------------
		File writeFile = new File(path);
		if (writeFile.exists()) {
			String msgHead = "Overwrite?";
			String msgText = "Overwrite existing file?";
			int msgAnswer = JOptionPane.showInternalConfirmDialog(this, msgText, msgHead, JOptionPane.YES_NO_OPTION);
			if (msgAnswer == JOptionPane.NO_OPTION) {
				return;
			}
		}
		
		try {
			ImageIO.write(image, extension, writeFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
