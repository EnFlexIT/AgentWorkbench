package de.enflexit.common.csv;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import de.enflexit.common.swing.AwbBasicTabbedPaneUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * The Class CSV_FilePreview represents a dialog that is used for 
 * debugging / visualisation purposes of the {@link CSV_FileImporterLowVoltageGrid}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class CSV_FilePreview extends JDialog {

	private static final long serialVersionUID = -7425514934078795521L;
	
	private TreeMap<String, CsvDataController> csvDataController;

	private JTabbedPane jTabbedPaneImport;
	
	
	/**
	 * Instantiates a new CS v_ file preview.
	 *
	 * @param owner the owner frame
	 * @param csvDataController the HashMap of CsvDataController
	 */
	public CSV_FilePreview(Frame owner, HashMap<String, CsvDataController> csvDataController) {
		super(owner);
		this.csvDataController = new TreeMap<>();
		this.csvDataController.putAll(csvDataController);
		this.initialize();
	}
	
	/**
	 * Instantiates a new CS v_ file preview.
	 *
	 * @param owner the owner frame
	 * @param csvDataController the TreeMap of CsvDataController
	 */
	public CSV_FilePreview(Frame owner, TreeMap<String, CsvDataController> csvDataController) {
		super(owner);
		this.csvDataController = csvDataController;
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {

		this.getContentPane().add(this.getJTabbedPaneImport(), BorderLayout.CENTER);
		if (this.csvDataController.size()>0) {
			
			for (String fileName : this.csvDataController.keySet()) {
				// --- Get the data controller ----------------------
				CsvDataController dataController = this.csvDataController.get(fileName);
				if (dataController!=null) {
					// --- Create a JScrollPane and add it ----------
					JTable table = new JTable(dataController.getDataModel());
					JScrollPane scrollPane = new JScrollPane(table);
					this.getJTabbedPaneImport().add(fileName, scrollPane);
				}
			}
		}

		// --- Size and center dialog -------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		
		this.setSize((int) (screenSize.getWidth()*0.5), (int)(screenSize.getHeight()*0.7));
		
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);	

		this.setTitle("Debug: CSV - Data Import");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	    this.setVisible(true);
	}
	
	private JTabbedPane getJTabbedPaneImport() {
		if (jTabbedPaneImport == null) {
			jTabbedPaneImport = new JTabbedPane(JTabbedPane.TOP);
			jTabbedPaneImport.setUI(new AwbBasicTabbedPaneUI());
			jTabbedPaneImport.setFont(new Font("Dialog", Font.BOLD, 13));
		}
		return jTabbedPaneImport;
	}
	
	/**
	 * Set the focus to the tab with the specified title / file name
	 * @param fileName to use as tab focus search phrase
	 */
	public void setTabFocusToFile(String fileName) {
		for (int i = 0; i < this.getJTabbedPaneImport().getTabCount(); i++) {
			String title = this.getJTabbedPaneImport().getTitleAt(i);
			if (title.equals(fileName)==true) {
				this.getJTabbedPaneImport().setSelectedIndex(i);
				break;
			}
		}
	}
	
	/**
	 * Return the current selection within the file preview 
	 * @return CSV_FilePreviewSelection that describes the selection
	 */
	public CSV_FilePreviewSelection getSelection() {
		
		// --- Get the file ---------------------
		int selectedTabIndex = this.getJTabbedPaneImport().getSelectedIndex();
		String fileName = this.getJTabbedPaneImport().getTitleAt(selectedTabIndex);

		// --- Get the CsvDataController --------
		CsvDataController dataController = this.csvDataController.get(fileName);
		
		// --- Get the data row selected --------
		JScrollPane scrollPane = (JScrollPane) this.getJTabbedPaneImport().getComponentAt(selectedTabIndex);
		JTable table = (JTable) scrollPane.getViewport().getComponent(0);
		int[] selectedTableRows = table.getSelectedRows();
		int[] selectedModelRows = null;
		if (selectedTableRows!=null && selectedTableRows.length>0) {
			selectedModelRows = new int[selectedTableRows.length];
			for (int i = 0; i < selectedTableRows.length; i++) {
				selectedModelRows[i] = table.convertRowIndexToModel(selectedTableRows[i]);
			}
		} else {
			selectedModelRows = selectedTableRows;
		}
		
		// --- Create the return instance -------
		CSV_FilePreviewSelection selection = new CSV_FilePreviewSelection();
		selection.setSelectedIndex(selectedTabIndex);
		selection.setSelectedFile(fileName);
		selection.setSelectedCsvDataController(dataController);
		selection.setSelectedModelRows(selectedModelRows);
		return selection;
	}
	
	/**
	 * Provides a complete description about the current selection in the file preview.  
	 */
	public class CSV_FilePreviewSelection {
		
		private int selectedIndex;
		private String selectedFile;
		
		private CsvDataController selectedCsvDataController;
		private int[] selectedModelRows;
		
		
		public int getSelectedIndex() {
			return selectedIndex;
		}
		public void setSelectedIndex(int selectedIndex) {
			this.selectedIndex = selectedIndex;
		}
		
		public String getSelectedFile() {
			return selectedFile;
		}
		public void setSelectedFile(String selectedFile) {
			this.selectedFile = selectedFile;
		}
		
		public CsvDataController getSelectedCsvDataController() {
			return selectedCsvDataController;
		}
		public void setSelectedCsvDataController(CsvDataController csvDataController) {
			this.selectedCsvDataController = csvDataController;
		}
		
		public int[] getSelectedModelRows() {
			return selectedModelRows;
		}
		public void setSelectedModelRows(int[] selectedModelRows) {
			this.selectedModelRows = selectedModelRows;
		}
	}
	
}
