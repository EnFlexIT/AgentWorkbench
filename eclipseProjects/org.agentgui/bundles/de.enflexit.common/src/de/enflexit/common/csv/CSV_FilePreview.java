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

		// --- Size and centre dialog -------------------------------
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
	
	
}
