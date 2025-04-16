package de.enflexit.common.csv;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import de.enflexit.common.images.ImageProvider;
import de.enflexit.common.images.ImageProvider.ImageFile;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;
import de.enflexit.language.Language;

/**
 * The Class CSV_FilePreview represents a dialog that is used for debugging /
 * visualisation purposes of the {@link CsvDataController}.
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
     * @param owner             the owner frame
     * @param csvDataController the HashMap of CsvDataController
     */
    public CSV_FilePreview(Window owner, HashMap<String, CsvDataController> csvDataController) {
		super(owner);
		this.csvDataController = new TreeMap<>();
		this.csvDataController.putAll(csvDataController);
		this.initialize();
    }

    /**
     * Instantiates a new CS v_ file preview.
     *
     * @param owner             the owner frame
     * @param csvDataController the TreeMap of CsvDataController
     */
    public CSV_FilePreview(Window owner, TreeMap<String, CsvDataController> csvDataController) {
		super(owner);
		this.csvDataController = csvDataController;
		this.initialize();
    }

    /**
     * Initialize.
     */
    private void initialize() {

    	this.setTitle("Debug: CSV - Data Import");
    	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    	this.getContentPane().add(this.getJTabbedPaneImport(), BorderLayout.CENTER);
		if (this.csvDataController.size() > 0) {

			for (String fileName : this.csvDataController.keySet()) {
				// --- Get the data controller ----------------------
				CsvDataController dataController = this.csvDataController.get(fileName);
				if (dataController != null) {
					
					// --- Create a JScrollPane and add it ----------
					JTable table = new JTable(dataController.getDataModel());
					table.setAutoCreateRowSorter(true);
					//table.setCellSelectionEnabled(true);
					JScrollPane scrollPane = new JScrollPane(table);

					// --- Create search component ------------------
					JPanelSearchTable searchPanel = new JPanelSearchTable(table);
					
					// --- Create JPanel to show table and search ---
					JPanel jPanelDisplay = new JPanel();
					jPanelDisplay.setLayout(new BorderLayout());
					jPanelDisplay.add(scrollPane, BorderLayout.CENTER);
					jPanelDisplay.add(searchPanel, BorderLayout.SOUTH);
					
					this.getJTabbedPaneImport().add(fileName, jPanelDisplay);
				}
			}
		}

		// --- Size and center dialog -------------------------------
		Rectangle screenSize = this.getGraphicsConfiguration().getBounds();
		this.setSize((int) (screenSize.getWidth() * 0.5), (int) (screenSize.getHeight() * 0.7));
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentBottomRight);

		this.setVisible(true);
	}

	private JTabbedPane getJTabbedPaneImport() {
		if (jTabbedPaneImport == null) {
			jTabbedPaneImport = new JTabbedPane(JTabbedPane.TOP);
			jTabbedPaneImport.setFont(new Font("Dialog", Font.BOLD, 13));
		}
		return jTabbedPaneImport;
	}

	/**
	 * Set the focus to the tab with the specified title / file name
	 * 
	 * @param fileName to use as tab focus search phrase
	 */
	public void setTabFocusToFile(String fileName) {
		for (int i = 0; i < this.getJTabbedPaneImport().getTabCount(); i++) {
			String title = this.getJTabbedPaneImport().getTitleAt(i);
			if (title.equals(fileName) == true) {
				this.getJTabbedPaneImport().setSelectedIndex(i);
				break;
			}
		}
	}

    /**
     * Return the current selection within the file preview
     * 
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
		if (selectedTableRows != null && selectedTableRows.length > 0) {
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
	 * Provides a complete description about the current selection in the file
	 * preview.
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

	/**
	 * The Class JPanelSearchTable.
	 *
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	private class JPanelSearchTable extends JPanel implements ActionListener {
		
		private static final long serialVersionUID = -8330135301200145305L;

		private JTable jTableToSearchIn;
		
		private JTextField jTextFieldSearch;
		private JButton jButtonSearchForward;
		private JButton jButtonSearchBackwards;
		private JButton jButtonSearchDelete;
		
		private Timer timerHighlight;

		/**
		 * Instantiates a new JPanel that allows to search the specified table.
		 * @param jTableToSearchIn the j table to search in
		 */
		public JPanelSearchTable(JTable jTableToSearchIn) {
			this.jTableToSearchIn = jTableToSearchIn;
			this.initialize();
		}
		private void initialize() {
		
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 3;
			gridBagConstraints5.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 4;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.gridy = -1;
			gridBagConstraints2.insets = new Insets(0, 0, 0, 0);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridx = -1;
			gridBagConstraints3.gridy = -1;
			gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridx = -1;
			gridBagConstraints1.gridy = -1;
			gridBagConstraints1.weightx = 0.0;
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			
			JLabel jLabelDummy = new JLabel();
			jLabelDummy.setText("");
			
			this.setLayout(new GridBagLayout());
			this.setBorder(BorderFactory.createEmptyBorder()); 
			this.add(getJTextFieldSearch(), gridBagConstraints1);
			this.add(getJButtonSearchBackwards(), gridBagConstraints3);
			this.add(getJButtonSearchForward(), gridBagConstraints2);
			this.add(jLabelDummy, gridBagConstraints4);
			this.add(getJButtonSearchDelete(), gridBagConstraints5);
		}
		
		/**
		 * This method initializes jTextFieldSearch	
		 * @return javax.swing.JTextField	
		 */
		private JTextField getJTextFieldSearch() {
			if (jTextFieldSearch == null) {
				jTextFieldSearch = new JTextField();
				jTextFieldSearch.setPreferredSize(new Dimension(180, 26));
				jTextFieldSearch.setToolTipText(Language.translate("Suche"));
				jTextFieldSearch.addActionListener(this);
			}
			return jTextFieldSearch;
		}
		/**
		 * This method initializes jButtonSearchForward	
		 * @return javax.swing.JButton	
		 */
		private JButton getJButtonSearchForward() {
			if (jButtonSearchForward == null) {
				jButtonSearchForward = new JButton();
				jButtonSearchForward.setPreferredSize(new Dimension(26, 26));
				jButtonSearchForward.setIcon(ImageProvider.getImageIcon(ImageFile.ARRAOW_Right_PNG));
				jButtonSearchForward.setToolTipText(Language.translate("Suche vorwärts"));
				jButtonSearchForward.addActionListener(this);
			}
			return jButtonSearchForward;
		}
		/**
		 * This method initializes jButtonSearchBackwards	
		 * @return javax.swing.JButton	
		 */
		private JButton getJButtonSearchBackwards() {
			if (jButtonSearchBackwards == null) {
				jButtonSearchBackwards = new JButton();
				jButtonSearchBackwards.setPreferredSize(new Dimension(26, 26));
				jButtonSearchBackwards.setIcon(ImageProvider.getImageIcon(ImageFile.ARRAOW_Left_PNG));
				jButtonSearchBackwards.setToolTipText(Language.translate("Suche rückwärts"));
				jButtonSearchBackwards.addActionListener(this);
			}
			return jButtonSearchBackwards;
		}
		/**
		 * This method initializes jButtonSearchDelete	
		 * @return javax.swing.JButton	
		 */
		private JButton getJButtonSearchDelete() {
			if (jButtonSearchDelete == null) {
				jButtonSearchDelete = new JButton();
				jButtonSearchDelete.setPreferredSize(new Dimension(26, 26));
				jButtonSearchDelete.setIcon(ImageProvider.getImageIcon(ImageFile.MB_Delete_PNG));
				jButtonSearchDelete.setToolTipText(Language.translate("Suche löschen"));
				jButtonSearchDelete.addActionListener(this);
			}
			return jButtonSearchDelete;
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {
			
			if (ae.getSource() == this.getJTextFieldSearch()) {
				this.searchTextInTable(this.getJTextFieldSearch().getText(), false);
			} else if (ae.getSource() == this.getJButtonSearchForward()) {
				this.searchTextInTable(this.getJTextFieldSearch().getText(), false);
			} else if (ae.getSource() == this.getJButtonSearchBackwards()) {
				this.searchTextInTable(this.getJTextFieldSearch().getText(), true);
			} else if (ae.getSource() == this.getJButtonSearchDelete()) {
				this.getJTextFieldSearch().setText(null);
			}
		}
		
		/**
		 * Searches the specified text in the table.
		 *
		 * @param searchText the search text
		 * @param backwardSearch the backward search
		 * @return true, if the search value could be found 
		 */
		private boolean searchTextInTable(String searchText, boolean backwardSearch) {
			return this.searchTextInTable(searchText, backwardSearch, false);
		}
		/**
		 * Searches the specified text in the table.
		 *
		 * @param searchText the search text
		 * @param backwardSearch the backward search
		 * @param isInternalRestartCall the is internal restart call
		 * @return true, if the search value could be found 
		 */
		private boolean searchTextInTable(String searchText, boolean backwardSearch, boolean isInternalRestartCall) {
			
			if (searchText==null || searchText.isBlank()==true) return false;

			// --- From where to start the search? ----------------------------
			int rowStartSearch = this.jTableToSearchIn.getSelectedRow();
			int colStartSearch = this.jTableToSearchIn.getSelectedColumn();
			
			// --- Call to start from top or bottom? --------------------------
			if (isInternalRestartCall==true) {
				rowStartSearch = -1;	
				colStartSearch = -1;
			}
			
			// --- Define result indexes, consider search direction -----------
			int rowMatch = -1;
			int colMatch = -1;
			if (backwardSearch==false) {
				// ------------------------------------------------------------
				// --- Forward search -----------------------------------------
				// ------------------------------------------------------------
				// --- Search index adjustments -----------
				if (rowStartSearch!=-1 && colStartSearch!=-1) {
					colStartSearch++;
					if (colStartSearch>=this.jTableToSearchIn.getColumnCount()) {
						colStartSearch=0;
						rowStartSearch++;
						if (rowStartSearch>=this.jTableToSearchIn.getRowCount()) {
							rowStartSearch=0;
						}
					}
				}
				// --- Search table values ------------------------------------
				tableSearch: for (int rowSearch = rowStartSearch==-1 ? 0 : rowStartSearch; rowSearch < this.jTableToSearchIn.getRowCount(); rowSearch++) {
					if (rowStartSearch>-1) rowStartSearch = -1;
					for (int colSearch = colStartSearch==-1 ? 0 : colStartSearch; colSearch < this.jTableToSearchIn.getColumnCount(); colSearch++) {
						if (colStartSearch>-1) colStartSearch = -1;
						Object cellValueObject = this.jTableToSearchIn.getValueAt(rowSearch, colSearch);
						if (cellValueObject!=null) {
							if (cellValueObject.toString().toLowerCase().contains(searchText.toLowerCase())==true) {
								rowMatch = rowSearch;
								colMatch = colSearch;
								break tableSearch;
							} else {
								// --- Earlier search exit --------------------
								if (isInternalRestartCall==true) {
									if (rowSearch > this.jTableToSearchIn.getSelectedRow() & colSearch > this.jTableToSearchIn.getSelectedColumn()) {
										// --- Exit here ----------------------
										break tableSearch;
									}
								}
							}
						}
					}
				}
				
			} else {
				// ------------------------------------------------------------
				// --- Backwards search ---------------------------------------
				// ------------------------------------------------------------
				// --- Search index adjustments -----------
				if (rowStartSearch!=-1 && colStartSearch!=-1) {
					colStartSearch--;
					if (colStartSearch<=0) {
						colStartSearch=this.jTableToSearchIn.getColumnCount()-1;
						rowStartSearch--;
						if (rowStartSearch<=0) {
							rowStartSearch=this.jTableToSearchIn.getRowCount()-1;
						}
					}
				}				
				// --- Search table values ------------------------------------
				tableSearch: for (int rowSearch = rowStartSearch==-1 ? this.jTableToSearchIn.getRowCount()-1 : rowStartSearch; rowSearch >= 0 ; rowSearch--) {
					if (rowStartSearch>-1) rowStartSearch = -1;
					for (int colSearch = colStartSearch==-1 ? this.jTableToSearchIn.getColumnCount()-1 : colStartSearch; colSearch >= 0; colSearch--) {
						if (colStartSearch>-1) colStartSearch = -1;
						Object cellValueObject = this.jTableToSearchIn.getValueAt(rowSearch, colSearch);
						if (cellValueObject!=null) {
							if (cellValueObject.toString().toLowerCase().contains(searchText.toLowerCase())==true) {
								rowMatch = rowSearch;
								colMatch = colSearch;
								break tableSearch;
							} else {
								// --- Earlier search exit --------------------
								if (isInternalRestartCall==true) {
									if (rowSearch < this.jTableToSearchIn.getSelectedRow() & colSearch < this.jTableToSearchIn.getSelectedColumn()) {
										// --- Exit here ----------------------
										break tableSearch;
									}
								}
							}
						}
					}
				}
				
			}

			// ----------------------------------------------------------------
			// --- Found a value? ---------------------------------------------
			// ----------------------------------------------------------------
			boolean foundValue = false;
			if (rowMatch==-1 || colMatch==-1) {
				if (isInternalRestartCall==false) {
					// --- No value found, start search from top or bottom ----
					foundValue = this.searchTextInTable(searchText, backwardSearch, true);
				} else {
					JOptionPane.showMessageDialog(OwnerDetection.getOwnerWindowForComponent(this), "Search value '" + searchText + "' could not be found!", "Table-Search", JOptionPane.INFORMATION_MESSAGE);
					this.getJTextFieldSearch().requestFocus();
				}
				
			} else {
				// --- Focus the value found ----------------------------------
				this.jTableToSearchIn.setRowSelectionInterval(rowMatch, rowMatch);
				this.jTableToSearchIn.setColumnSelectionInterval(colMatch, colMatch);
				this.jTableToSearchIn.scrollRectToVisible(this.jTableToSearchIn.getCellRect(rowMatch, colMatch, false));
				foundValue = true;
				// --- Shortly highlight cell ---------------------------------
				this.jTableToSearchIn.setCellSelectionEnabled(true);
				this.getTimerHighlight().restart();
			}
			return foundValue;
		}
		
		
		/**
		 * Return the local highlight timer.
		 * @return the timer highlight
		 */
		private Timer getTimerHighlight() {
			if (timerHighlight==null) {
				timerHighlight = new Timer(750, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JPanelSearchTable.this.doHighlightRow();
					}
				});
				timerHighlight.setRepeats(false);
			}
			return timerHighlight;
		}
		private void doHighlightRow() {
			this.jTableToSearchIn.setCellSelectionEnabled(false);
			this.jTableToSearchIn.setRowSelectionAllowed(true);
		}
	}
	
}
