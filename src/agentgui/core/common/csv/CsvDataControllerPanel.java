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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import java.awt.Font;

/**
 * The CSV Controller Panel that can be used with a customised CSV-Importer 
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
 */
public class CsvDataControllerPanel extends JPanel implements ActionListener, Observer{

	private static final long serialVersionUID = -8553767098312965499L;
	
	private JToolBar jToolBarCsvHandling;
	
	private JScrollPane jScrollPaneTable;
	private JTable jTableData;
	private JButton jButtonImport;
	private JButton jButtonExport;
	
	private JLabel jLabelSeparator;
	private JCheckBox jCheckBoxHasHeadlines;
	private JComboBox<String> jComboBoxSeparator;
	
	/**
	 * The CSV data controller instance
	 */
	private CsvDataController csvDataController;
	/**
	 * List of CSV data separators to choose from
	 */
	private String[] seperators = {";",",",":","."};
	
	
	/**
	 * Create the panel.
	 */
	public CsvDataControllerPanel() {
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		this.add(this.getJToolBarCsvHandling(), BorderLayout.NORTH);
		this.add(this.getJScrollPaneTable(), BorderLayout.CENTER);
	}
	
	private JToolBar getJToolBarCsvHandling() {
		if (jToolBarCsvHandling == null) {
			jToolBarCsvHandling = new JToolBar();
			jToolBarCsvHandling.setFloatable(false);
			jToolBarCsvHandling.add(getJButtonImport());
			jToolBarCsvHandling.add(getJButtonExport());
			jToolBarCsvHandling.addSeparator();
			jToolBarCsvHandling.add(getJLabelSeparator());
			jToolBarCsvHandling.add(getJComboBoxSeparator());
			jToolBarCsvHandling.addSeparator();
			jToolBarCsvHandling.add(getJCheckBoxHasHeadlines());
		}
		return jToolBarCsvHandling;
	}
	
	private JScrollPane getJScrollPaneTable() {
		if (jScrollPaneTable==null) {
			jScrollPaneTable = new JScrollPane();
			jScrollPaneTable.setViewportView(this.getJTableData());
		}
		return jScrollPaneTable;
	}
	private JTable getJTableData() {
		if (jTableData == null) {
			jTableData = new JTable();
			jTableData.setFillsViewportHeight(true);
		}
		return jTableData;
	}
	private JButton getJButtonImport() {
		if (jButtonImport == null) {
			jButtonImport = new JButton();
			jButtonImport.setToolTipText("Import");
			jButtonImport.setIcon(new ImageIcon(CsvDataControllerPanel.class.getResource("/agentgui/core/gui/img/import.png")));
			jButtonImport.addActionListener(this);
		}
		return jButtonImport;
	}
	private JButton getJButtonExport() {
		if (jButtonExport == null) {
			jButtonExport = new JButton();
			jButtonExport.setToolTipText("Export");
			jButtonExport.setIcon(new ImageIcon(CsvDataControllerPanel.class.getResource("/agentgui/core/gui/img/export.png")));
			jButtonExport.addActionListener(this);
		}
		return jButtonExport;
	}
	private JComboBox<String> getJComboBoxSeparator() {
		if (jComboBoxSeparator == null) {
			jComboBoxSeparator = new JComboBox<String>();
			jComboBoxSeparator.setFont(new Font("Dialog", Font.BOLD, 12));
			jComboBoxSeparator.setModel(new DefaultComboBoxModel<String>(this.seperators));
			jComboBoxSeparator.setPreferredSize(new Dimension(50, 26));
			jComboBoxSeparator.addActionListener(this);
		}
		return jComboBoxSeparator;
	}

	private JLabel getJLabelSeparator() {
		if (jLabelSeparator == null) {
			jLabelSeparator = new JLabel(Language.translate("Separator:", Language.EN));
			jLabelSeparator.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelSeparator;
	}

	private JCheckBox getJCheckBoxHasHeadlines() {
		if (jCheckBoxHasHeadlines == null) {
			jCheckBoxHasHeadlines = new JCheckBox(Language.translate("Column Headers:", Language.EN));
			jCheckBoxHasHeadlines.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxHasHeadlines.setSelected(this.getCsvDataController().hasHeadlines());
			jCheckBoxHasHeadlines.addActionListener(this);
		}
		return jCheckBoxHasHeadlines;
	}
	
	/**
	 * Gets the {@link CsvDataController}, initializes it if necessary
	 * @return The {@link CsvDataController} instance
	 */
	public CsvDataController getCsvDataController() {
		if(this.csvDataController == null){
			this.csvDataController = new CsvDataController();
			this.csvDataController.addObserver(this);
		}
		return this.csvDataController;
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if(ae.getSource() == this.getJButtonImport()) {
			
			// --- Import data from CSV
			JFileChooser jFileChooserImportCSV = new JFileChooser(Application.getGlobalInfo().getLastSelectedFolder());
			jFileChooserImportCSV.setFileFilter(new FileNameExtensionFilter(Language.translate("CSV-Dateien"), "csv"));
			jFileChooserImportCSV.setDialogTitle(Language.translate("CSV-Datei importieren"));
			
			if(jFileChooserImportCSV.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				Application.getGlobalInfo().setLastSelectedFolder(jFileChooserImportCSV.getCurrentDirectory());
				File csvFile = jFileChooserImportCSV.getSelectedFile();
				
				this.getCsvDataController().setHeadline(this.getJCheckBoxHasHeadlines().isSelected());
				this.getCsvDataController().setSeparator((String) this.getJComboBoxSeparator().getSelectedItem());
				this.getCsvDataController().setFile(csvFile);
				this.getCsvDataController().doImport();
			}
			
		} else if(ae.getSource() == this.getJButtonExport()) {
			
			// --- Export data to CSV
			JFileChooser jFileChooserExportCSV = new JFileChooser(Application.getGlobalInfo().getLastSelectedFolder());
			jFileChooserExportCSV.setFileFilter(new FileNameExtensionFilter(Language.translate("CSV-Dateien"), "csv"));
			jFileChooserExportCSV.setDialogTitle(Language.translate("CSV-Datei exportieren"));
			
			if(jFileChooserExportCSV.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
				Application.getGlobalInfo().setLastSelectedFolder(jFileChooserExportCSV.getCurrentDirectory());
				File csvFile = jFileChooserExportCSV.getSelectedFile();
				if(csvFile.getPath().endsWith(".csv") == false){
					csvFile = new File(csvFile.getPath().concat(".csv"));
				}
				this.getCsvDataController().setFile(csvFile);
				this.getCsvDataController().doExport();
			}
		} else if(ae.getSource() == this.getJComboBoxSeparator()) {
			
			// Handle separator changes
			this.getCsvDataController().setSeparator((String) this.getJComboBoxSeparator().getSelectedItem());
			
			// If the model was loaded already, reload it
			if(this.getCsvDataController().getDataModel() != null){
				 this.getCsvDataController().doImport();
			}
		} else if (ae.getSource() == this.getJCheckBoxHasHeadlines()){
			this.getCsvDataController().setHeadline(this.getJCheckBoxHasHeadlines().isSelected());
			
			// If the model was loaded already, reload it
			if(this.getCsvDataController().getDataModel() != null){
				 this.getCsvDataController().doImport();
			}
		}
	}
	
	/**
	 * Just for development, remove later
	 * @param args
	 */
//	public static void main(String[] args){
//		JFrame frame = new JFrame("CSV Importer");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().add(new CsvDataControllerPanel());
//		frame.setSize(600, 450);
//		frame.setVisible(true);
//	}
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof CsvDataController && arg.equals(CsvDataController.EVENT_TABLE_MODEL_REPLACED)){
			DefaultTableModel dtm = this.csvDataController.getDataModel();
			 if(dtm != null){
				this.getJTableData().setModel(dtm);
			 }
		}
	}
}
