package agentgui.core.charts;

import jade.util.leap.ArrayList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
import agentgui.envModel.graph.GraphGlobals;

public abstract class ChartDialog extends JDialog implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8969696622674876327L;
	protected JToolBar toolBar;
	protected JTabbedPane tabbedPane;
	private JPanel pnlBottom;
	protected JButton btnImport;
	
	protected JButton btnOk;
	protected JButton btnCancel;
	
	protected ChartDataModel model;
	
	protected ChartTab chartTab;
	protected TableTab tableTab;
	protected SettingsTab settingsTab;
	
	private BufferedImage chartThumb = null;
	
	private boolean canceled = false;

	public ChartDialog(Window owner) {
		super(owner);
		
		setSize(600, 450);
		
		getContentPane().add(getToolBar(), BorderLayout.NORTH);
		getContentPane().add(getTabbedPane(), BorderLayout.CENTER);
		getContentPane().add(getPnlBottom(), BorderLayout.SOUTH);
	}
	private JToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.add(getBtnImport());
		}
		return toolBar;
	}
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		}
		return tabbedPane;
	}
	private JPanel getPnlBottom() {
		if (pnlBottom == null) {
			pnlBottom = new JPanel();
			pnlBottom.add(getBtnOk());
			pnlBottom.add(getBtnCancel());
		}
		return pnlBottom;
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
	 * This method initialises jButtonOK.
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton();
			btnOk.setPreferredSize(new Dimension(120, 26));
			btnOk.setFont(new Font("Dialog", Font.BOLD, 12));
			btnOk.setForeground(new Color(0, 153, 0));
			btnOk.setText("OK");
			btnOk.setText(Language.translate(btnOk.getText()));
			btnOk.addActionListener(this);
		}
		return btnOk;
	}
	
	/**
	 * This method initialises jButtonCancel.
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setPreferredSize(new Dimension(120, 26));
			btnCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			btnCancel.setForeground(new Color(153, 0, 0));
			btnCancel.setText("Abbruch");
			btnCancel.setText(Language.translate(btnCancel.getText()));
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}
	
	/**
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * @return the chartThumb
	 */
	public BufferedImage getChartThumb() {
		return chartThumb;
	}
	
	/**
	 * This method imports one time series from a CSV file
	 * @param csvFile The CSV file
	 */
	private void importTimeSeriesFromCSV(File csvFile){
		if(csvFile.exists()){
			
			// Read the CSV data
			BufferedReader csvFileReader;
			System.out.println("Importing CSV data from "+csvFile.getName());
			
			ArrayList xVals = new ArrayList();
			ArrayList yVals = new ArrayList();
			
			try {
				
				// Read the data from the file
				csvFileReader = new BufferedReader(new FileReader(csvFile));
				String inBuffer = null;
				
				while((inBuffer = csvFileReader.readLine()) != null){
					// Regex matches two numbers (with or without decimals) separated by a ;
					if(inBuffer.matches("[\\d]+\\.?[\\d]*;[\\d]+\\.?[\\d]*")){
						String[] parts = inBuffer.split(";");
						
						xVals.add(Float.parseFloat(parts[0]));
						yVals.add(Float.parseFloat(parts[1]));
					}
				}
			} catch (FileNotFoundException e) {
				// Will not happen, as file existence is checked right before creating the reader  
				System.err.println("Error opening CSV file");
			} catch (IOException e) {
				System.err.println("Error reading CSV data");
			}
			
			// Add the new series to the model
			model.addDataSeries(null, xVals, yVals);
			
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == btnOk){
			chartThumb = chartTab.createChartThumb();
			setVisible(false);
		}else if(ae.getSource() == btnCancel){
			canceled = true;
			setVisible(false);
		}else if (ae.getSource() == btnImport){
			// Import CSF data

			// Choose file
			JFileChooser jFileChooserImportCSV = new JFileChooser(Application.RunInfo.getLastSelectedFolder());
			jFileChooserImportCSV.setFileFilter(new FileNameExtensionFilter(Language.translate("CSV-Dateien"), "csv"));
			if(jFileChooserImportCSV.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				Application.RunInfo.setLastSelectedFolder(jFileChooserImportCSV.getCurrentDirectory());
				File csvFile = jFileChooserImportCSV.getSelectedFile();
				
				// Read data and init models
				importTimeSeriesFromCSV(csvFile);
				repaint();
			}
		}
	}
}
