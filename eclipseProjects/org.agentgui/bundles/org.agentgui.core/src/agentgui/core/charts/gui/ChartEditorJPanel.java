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
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.charts.DataModel;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;
import agentgui.core.charts.timeseriesChart.gui.TimeFormatImportConfiguration;
import agentgui.core.charts.timeseriesChart.gui.TimeSeriesChartEditorJPanel;
import agentgui.core.config.GlobalInfo;
import agentgui.core.gui.imaging.ConfigurableFileFilter;
import agentgui.core.gui.imaging.ImageFileView;
import agentgui.core.gui.imaging.ImagePreview;
import agentgui.core.gui.imaging.ImageUtils;
import agentgui.ontology.DataSeries;
import agentgui.ontology.ValuePair;
import de.enflexit.common.csv.CsvFileWriter;
import de.enflexit.common.ontology.gui.DynForm;
import de.enflexit.common.ontology.gui.OntologyClassEditorJPanel;

/**
 * General superclass for OntologyClassEditorJPanel implementations for charts.
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public abstract class ChartEditorJPanel extends OntologyClassEditorJPanel implements ActionListener, FocusListener{
	
	private static final long serialVersionUID = -306986715544317480L;
	
	private static final Integer DEFAULT_ImageWidth = 1600; 
	private static final Integer DEFAULT_ImageHeight = 900;
	
    private final Dimension jButtonSize = new Dimension(26, 26); 
     
	// Swing components
	protected JToolBar toolBar;
	protected JTabbedPane tabbedPane;
	protected JButton jButtonCsvImport;
	protected JButton jButtonCsvExport;

	protected JButton jButtonSaveAsImage = null;
	protected JLabel jLabelExport = null;
	protected JComboBox<String> cbImageAspectRatio = null;
	protected JTextField tfImageWidth = null;
	protected JLabel jLabelX = null;
	protected JTextField tfImageHeight= null;
	protected JScrollPane scrollPane4SettingTab = null;
	// Tab contents
	protected ChartTab chartTab;
	protected TableTab tableTab;
	protected ChartSettingsTab settingsTab;
	
	
	/**
	 * Instantiates a new chart editor j panel.
	 *
	 * @param dynForm the dyn form
	 * @param startArgIndex the start arg index
	 */
	public ChartEditorJPanel(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
		
		this.setOntologyClassInstance(dynForm.getOntoArgsInstance()[startArgIndex]);
		
		this.setLayout(new BorderLayout());
		this.add(getToolBar(), BorderLayout.NORTH);
		this.add(getTabbedPane(), BorderLayout.CENTER);
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
			toolBar.add(getJButtonCsvImport());
			toolBar.add(getJButtonCsvExport());
			toolBar.addSeparator();
			toolBar.add(jLabelExport);
			toolBar.add(getCbImageAspectRatio());
			toolBar.add(getTfImageWidth());
			toolBar.add(jLabelX);
			toolBar.add(getTfImageHeight());
			toolBar.add(getJButtonSaveAsImage());
		}
		return toolBar;
	}
	
	protected JTabbedPane getTabbedPane(){
		if(tabbedPane == null){
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Chart", getChartTab());
			tabbedPane.addTab("Table", getTableTab());
			tabbedPane.addTab("Settings", getJScrollPane4SettingsTab());
			tabbedPane.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent ce) {
					// --- Stop editing cells in tables ---
					getTableTab().stopEditing();
					getChartSettingsTab().stopEditing();
				}
			});
		}
		return tabbedPane;
	}
	protected JScrollPane getJScrollPane4SettingsTab() {
		if (scrollPane4SettingTab==null) {
			scrollPane4SettingTab = new JScrollPane();
			scrollPane4SettingTab.setViewportView(this.getChartSettingsTab());
		}
		return scrollPane4SettingTab;
	}
	
	protected abstract ChartTab getChartTab();
	
	protected abstract TableTab getTableTab();
	
	protected abstract ChartSettingsTab getChartSettingsTab();
	
	protected JButton getJButtonCsvImport() {
		if (jButtonCsvImport == null) {
			jButtonCsvImport = new JButton("");
			jButtonCsvImport.setIcon(GlobalInfo.getInternalImageIcon("MBtransImport.png"));
			jButtonCsvImport.setPreferredSize(jButtonSize);
			jButtonCsvImport.setToolTipText(Language.translate("Neue Datenreihe(n) importieren"));
			jButtonCsvImport.addActionListener(this);
		}
		return jButtonCsvImport;
	}
	
	/**
     * This method initializes jButtonSaveImage
     * @return javax.swing.JButton
     */
    protected JButton getJButtonSaveAsImage() {
		if (jButtonSaveAsImage == null) {
			jButtonSaveAsImage = new JButton();
			jButtonSaveAsImage.setIcon(GlobalInfo.getInternalImageIcon("SaveAsImage.png"));
			jButtonSaveAsImage.setPreferredSize(jButtonSize);
			jButtonSaveAsImage.setToolTipText(Language.translate("Als Bild exportieren"));
			jButtonSaveAsImage.addActionListener(this);
		}
		return jButtonSaveAsImage;
    }
    
    protected JComboBox<String> getCbImageAspectRatio(){
    	if(cbImageAspectRatio == null){
    		DefaultComboBoxModel<String> formatsModel = new DefaultComboBoxModel<String>(new String[]{"16:9", "4:3"});
    		cbImageAspectRatio = new JComboBox<String>(formatsModel);
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
	
	protected JButton getJButtonCsvExport() {
		if (jButtonCsvExport == null) {
			jButtonCsvExport = new JButton("");
			jButtonCsvExport.setIcon(GlobalInfo.getInternalImageIcon("MBtransExport.png"));
			jButtonCsvExport.setToolTipText("Export data to CSV");
			jButtonCsvExport.addActionListener(this);
		}
		return jButtonCsvExport;
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
	public abstract DataModel getDataModel();
	
	
	/**
	 * Get a key / x value of the correct type for this chart from a string representation.
	 *
	 * @param key The string representation of the key / x value
	 * @param keyFormat the key format
	 * @param keyOffset the key offset
	 * @return The key / x value
	 */
	protected abstract Number parseKey(String key, String keyFormat, Number keyOffset);
	
	/**
	 * Get a (y) value of the correct type for this chart from a string representation.
	 * @param value the string representation of the (y) value
	 * @return The (y) value
	 */
	protected abstract Number parseValue(String value);
	
	/**
	 * Imports a data series from a CSV file.
	 *
	 * @param csvFile The CSV file
	 * @param keyFormat the key format
	 */
	protected void importDataSeriesFromCSV(File csvFile, String keyFormat, Number keyOffset) {
		
		// --- Read the CSV data --------------------------
		BufferedReader csvFileReader = null;
		DataSeries[] importedSeries = null;
		boolean validLine = false;

		// --- Read the data from the file ----------------
		System.out.println("Importing CSV data from " + csvFile.getName());
		try {
			csvFileReader = new BufferedReader(new FileReader(csvFile));
			String inBuffer = null;
			while((inBuffer = csvFileReader.readLine()) != null){
					
				// --- Do we have a valid line from the file here? ------------
				if (keyFormat!=null) {
					// -- Case TimeSeries -----------------
					String residualInBuffer = "1234.567;" + inBuffer.substring(inBuffer.indexOf(";")+1);
					validLine = residualInBuffer.matches("[\\d]+\\.?[\\d]*[;[\\d]+\\.?[\\d]*]+");					
				} else {
					// -- Case Else -----------------------
					validLine = inBuffer.matches("[\\d]+\\.?[\\d]*[;[\\d]+\\.?[\\d]*]+");
				}
				
				// --- Valid lines are to proceed -----------------------------
				if(validLine==true){
					String[] parts = inBuffer.split(";");
					if(importedSeries == null && parts.length > 0){
						importedSeries = new DataSeries[parts.length-1];
						
						for(int k=0; k<parts.length-1; k++){
							importedSeries[k] = this.getDataModel().createNewDataSeries(null);
						}
					}
					
					// First column contains the key / x value
					Number key = parseKey(parts[0], keyFormat, keyOffset);
					
					// Later columns contain data
					for(int i=1; i<parts.length; i++){
						if(parts[i].length() > 0){
							// Empty string -> no value for this key in this series -> no new value pair
							Number value = parseValue(parts[i]);
							ValuePair valuePair = this.getDataModel().createNewValuePair(key, value);
							this.getDataModel().getValuePairsFromSeries(importedSeries[i-1]).add(valuePair);
						}
					}
				}
			}
			csvFileReader.close();
			
			if (importedSeries!=null) {
				for(int j=0; j < importedSeries.length; j++){
					this.getDataModel().addSeries(importedSeries[j]);
				}	
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
			int msgAnswer = JOptionPane.showConfirmDialog(this, msgText, msgHead, JOptionPane.YES_NO_OPTION);
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
	/**
	 * Exports the chart data to a CSV file
	 * @return Export successful?
	 */
	private boolean exportDataAsCSV(){
		boolean success = false;
		
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV File", "csv");
		fileChooser.addChoosableFileFilter(csvFilter);
		fileChooser.setFileFilter(csvFilter);
		
		if(fileChooser.showSaveDialog(Application.getMainWindow()) == JFileChooser.APPROVE_OPTION){
			File csvFile = fileChooser.getSelectedFile();
			// --- Append suffix if necessary ---------------------------------
			if(csvFile.getAbsolutePath().toLowerCase().endsWith(".csv") == false){
				csvFile = new File(csvFile.getAbsolutePath()+".csv");
			}

			CsvFileWriter fileWriter = new CsvFileWriter();
			
			// --- Get the data to export ------------------
			Vector<Vector<Object>> dataVector = null;
			boolean parseTimestamp = this instanceof TimeSeriesChartEditorJPanel;	// If called from a TimeSeriesChartEditorJPanel, parse the 
			dataVector = this.getDataModel().getTableModel().getTableDataAsObjectVector(true, parseTimestamp);
			
			
			success = fileWriter.exportData(csvFile, dataVector);
		}
		
		return success;
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource() == jButtonCsvImport){
			// --- Import CSV data / Choose file ----------
			JFileChooser jFileChooserImportCSV = new JFileChooser(Application.getGlobalInfo().getLastSelectedFolder());
			jFileChooserImportCSV.setFileFilter(new FileNameExtensionFilter(Language.translate("CSV-Dateien"), "csv"));
			jFileChooserImportCSV.setDialogTitle(Language.translate("CSV-Datei importieren"));
			if(jFileChooserImportCSV.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				Application.getGlobalInfo().setLastSelectedFolder(jFileChooserImportCSV.getCurrentDirectory());
				File csvFile = jFileChooserImportCSV.getSelectedFile();
				
				// --- Separate DataModel --------------------------- 
				if (this.getDataModel() instanceof TimeSeriesDataModel) {
					// --- Ask for time format ----------------------
					TimeFormatImportConfiguration importFormatDialog = new TimeFormatImportConfiguration(Application.getMainWindow(), csvFile); 
					importFormatDialog.setVisible(true);
					// - - - - - - - - - - - - - - - - - - - - - - -  
					if (importFormatDialog.isCanceled()==true) return;
					// --- Import data ------------------------------
					this.importDataSeriesFromCSV(csvFile, importFormatDialog.getTimeFormat(), importFormatDialog.getTimeOffset());
					
				} else {
					// --- Import data ------------------------------
					this.importDataSeriesFromCSV(csvFile, null, null);	
					
				}
				this.getTableTab().setButtonsEnabledToSituation();
			}
			
		} else if(ae.getSource() == jButtonSaveAsImage) {
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
		}else if(ae.getSource() == jButtonCsvExport){
			this.exportDataAsCSV();
		}
	
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
}
