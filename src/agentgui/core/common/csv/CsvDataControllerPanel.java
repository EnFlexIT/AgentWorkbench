package agentgui.core.common.csv;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.GridBagLayout;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import agentgui.core.application.Application;
import agentgui.core.application.Language;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.JTable;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JSeparator;

/**
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
 *
 */
public class CsvDataControllerPanel extends JPanel implements ActionListener{

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -8553767098312965499L;
	/** Swing components */
	private JToolBar toolBar;
	private JTable table;
	private JButton btnImport;
	private JButton btnExport;
	private JLabel lblSeparator;
	private JCheckBox chckbxHasHeadlines;
	private JComboBox<String> cbSeparator;
	
	/**
	 * The CSV data controller instance
	 */
	private CsvDataController importer;
	/**
	 * List of CSV data separators to choose from
	 */
	private String[] seperators = {";",",",":","."};
	
	
	/**
	 * Create the panel.
	 */
	public CsvDataControllerPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.anchor = GridBagConstraints.WEST;
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 0;
		add(getToolBar(), gbc_toolBar);
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 1;
		add(new JScrollPane(getTable()), gbc_table);

	}

	private JToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.add(getBtnImport());
			toolBar.add(getBtnExport());
			toolBar.addSeparator(new Dimension(5, toolBar.getHeight()));
			toolBar.add(getCbSeparator());
			toolBar.add(getLblSeparator());
			toolBar.addSeparator(new Dimension(5, toolBar.getHeight()));
			toolBar.add(getChckbxHasHeadlines());
		}
		return toolBar;
	}
	private JTable getTable() {
		if (table == null) {
			table = new JTable();
		}
		return table;
	}
	private JButton getBtnImport() {
		if (btnImport == null) {
			btnImport = new JButton("Import");
			btnImport.setIcon(new ImageIcon(CsvDataControllerPanel.class.getResource("/agentgui/envModel/graph/img/import.png")));
			btnImport.addActionListener(this);
		}
		return btnImport;
	}
	private JButton getBtnExport() {
		if (btnExport == null) {
			btnExport = new JButton("Export");
			btnExport.setIcon(new ImageIcon(CsvDataControllerPanel.class.getResource("/agentgui/envModel/graph/img/export.png")));
			btnExport.addActionListener(this);
		}
		return btnExport;
	}
	private JComboBox<String> getCbSeparator() {
		if (cbSeparator == null) {
			cbSeparator = new JComboBox<String>();
			cbSeparator.setModel(new DefaultComboBoxModel<String>(this.seperators));
		}
		return cbSeparator;
	}

	private JLabel getLblSeparator() {
		if (lblSeparator == null) {
			lblSeparator = new JLabel(Language.translate("Trennzeichen"));
		}
		return lblSeparator;
	}

	private JCheckBox getChckbxHasHeadlines() {
		if (chckbxHasHeadlines == null) {
			chckbxHasHeadlines = new JCheckBox(Language.translate("Spaltenüberschriften"));
			chckbxHasHeadlines.setSelected(true);
		}
		return chckbxHasHeadlines;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if(ae.getSource() == this.getBtnImport()){
			
			// --- Import data from CSV
			JFileChooser jFileChooserImportCSV = new JFileChooser(Application.getGlobalInfo().getLastSelectedFolder());
			jFileChooserImportCSV.setFileFilter(new FileNameExtensionFilter(Language.translate("CSV-Dateien"), "csv"));
			jFileChooserImportCSV.setDialogTitle(Language.translate("CSV-Datei importieren"));
			
			if(jFileChooserImportCSV.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				Application.getGlobalInfo().setLastSelectedFolder(jFileChooserImportCSV.getCurrentDirectory());
				File csvFile = jFileChooserImportCSV.getSelectedFile();
				
				this.importer = new CsvDataController();
				this.importer.setHeadlines(this.getChckbxHasHeadlines().isSelected());
				this.importer.setSeparator((String) this.getCbSeparator().getSelectedItem());
				
				this.importer.doImport(csvFile);
				DefaultTableModel dtm = this.importer.getDataModel();
				if(dtm != null){
					this.getTable().setModel(dtm);
				}
			}
		}else{
			
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
				
				importer.doExport(csvFile);
			}
		}
	}
	
	/**
	 * Just for development, remove later
	 * @param args
	 */
	public static void main(String[] args){
		JFrame frame = new JFrame("CSV Importer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new CsvDataControllerPanel());
		frame.setSize(600, 450);
		frame.setVisible(true);
	}
}
