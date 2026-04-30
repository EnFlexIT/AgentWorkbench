package de.enflexit.df.core.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import de.enflexit.common.swing.AwbThemeImageIcon;
import de.enflexit.common.swing.KeyAdapter4Numbers;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.data.PaginationDataLoader;
import de.enflexit.df.core.model.AffectedDataObjects;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.TablesawTableModel;
import de.enflexit.df.core.model.treeNode.AbstractDataTreeNodeDataSource;
import tech.tablesaw.api.Table;

/**
 * The Class JPanelDataDetailView.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelDataDetailView extends JPanel implements PropertyChangeListener, ActionListener {

	private static final long serialVersionUID = -2503356793902058897L;

	private DataController dataController;
	
	private Font baseFont = new Font("Dialog", Font.PLAIN, 12);
	private Dimension textFieldDimension = new Dimension(60, 24);
	
	
	private JScrollPane jScrollPaneData;
	private JTable jTableData;

	private JPanel jPanelDataless;
	
	private JToolBar jToolBarDatasetNavigation;
		private JButton jButtonDatasetFirst;
		private JButton jButtonDatasetPrevious;
		private JTextField jTextFieldDatasetNo;
		private JButton jButtonDatasetNext;
		private JButton jButtonDatasetLast;
	
		private JToggleButton jToggleButtonEnabledPagination;
		private JLabel jLabelRowsPerPage;
		private JTextField jTextFieldRowsPerPage;
		private JLabel jLabelPageLoaded;
		private JTextField jTextFieldPageLoaded;
	
	/**
	 * Instantiates a new j panel data table.
	 * @param dataController the data controller
	 */
	public JPanelDataDetailView(DataController dataController) {
		this.setDataController(dataController);
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jScrollPaneData = new GridBagConstraints();
		gbc_jScrollPaneData.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneData.gridx = 0;
		gbc_jScrollPaneData.gridy = 0;
		this.add(this.getJScrollPaneData(), gbc_jScrollPaneData);
		
		GridBagConstraints gbc_jToolBarDatasetNavigation = new GridBagConstraints();
		gbc_jToolBarDatasetNavigation.fill = GridBagConstraints.HORIZONTAL;
		gbc_jToolBarDatasetNavigation.insets = new Insets(5, 5, 5, 5);
		gbc_jToolBarDatasetNavigation.gridx = 0;
		gbc_jToolBarDatasetNavigation.gridy = 1;
		this.add(this.getJToolBarDatasetNavigation(), gbc_jToolBarDatasetNavigation);
		
		this.setJToolDatasetNavigationEnabled();
		
	}

	/**
	 * Returns the data controller.
	 * @return the data controller
	 */
	public DataController getDataController() {
		return dataController;
	}
	/**
	 * Sets the data controller.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
		if (this.dataController!=null) {
			this.dataController.addPropertyChangeListener(this);
		}
	}
	/**
	 * Returns the currently selected data tree node data source.
	 * @return the selected data tree node data source
	 */
	private AbstractDataTreeNodeDataSource<?> getSelectedDataTreeNodeDataSource() {
		return this.getDataController().getSelectionModel().getSelectedDataTreeNodeDataSource();
	}
	/**
	 * Returns the current PaginationDataLoader or <code>null</code>, if no data tree node can be found.
	 * @return the pagination data loader
	 */
	private PaginationDataLoader<?> getPaginationDataLoader() {
		AbstractDataTreeNodeDataSource<?> dtnoDS = getSelectedDataTreeNodeDataSource();
		return (dtnoDS==null ? null : dtnoDS.getPaginationDataLoader());
	}
	
	
	private JScrollPane getJScrollPaneData() {
		if (jScrollPaneData == null) {
			jScrollPaneData = new JScrollPane();
			jScrollPaneData.setViewportView(this.getJPanelDataless());
		}
		return jScrollPaneData;
	}
	private JTable getJTableData() {
		if (jTableData == null) {
			jTableData = new JTable();
			jTableData.setFillsViewportHeight(true);
			jTableData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			jTableData.getTableHeader().setReorderingAllowed(false);
			jTableData.setDefaultRenderer(Object.class, new DateTimeTableCellRenderer("dd.MM.yyyy HH:mm:ss"));
			jTableData.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent lse) {
					if (lse.getValueIsAdjusting()==true) return;
					JPanelDataDetailView.this.setDatasetSelection(lse.getFirstIndex(), lse.getLastIndex(), false);
				}
			});
		}
		return jTableData;
	}

	private JPanel getJPanelDataless() {
		if (jPanelDataless==null) {
			jPanelDataless = new JPanel();
		}
		return jPanelDataless;
	}
	
	private JToolBar getJToolBarDatasetNavigation() {
		if (jToolBarDatasetNavigation == null) {
			jToolBarDatasetNavigation = new JToolBar();
			jToolBarDatasetNavigation.setFloatable(false);
			jToolBarDatasetNavigation.setRollover(true);
			jToolBarDatasetNavigation.setPreferredSize(new Dimension(120, 26));
			
			jToolBarDatasetNavigation.add(this.getJButtonDatasetFirst());
			jToolBarDatasetNavigation.add(this.getJButtonDatasetPrevious());
			jToolBarDatasetNavigation.add(this.getJTextFieldDatasetNo());
			jToolBarDatasetNavigation.add(this.getJButtonDatasetNext());
			jToolBarDatasetNavigation.add(this.getJButtonDatasetLast());
			
			jToolBarDatasetNavigation.addSeparator();
			jToolBarDatasetNavigation.add(this.getJToggleButtonEnabledPagination());
			jToolBarDatasetNavigation.add(this.getJLabelRowsPerPage());
			jToolBarDatasetNavigation.add(this.getJTextFieldRowsPerPage());
			
			jToolBarDatasetNavigation.add(this.getJLabelPagesLoaded());
			jToolBarDatasetNavigation.add(this.getJTextFieldPageLoaded());
			
		}
		return jToolBarDatasetNavigation;
	}
	/**
	 * Depending on the current selection, sets the navigation toolbar enabled.
	 */
	private void setJToolDatasetNavigationEnabled() {
		
		AbstractDataTreeNodeDataSource<?> dtnoDS = this.getSelectedDataTreeNodeDataSource();
		boolean isEnabledToolBar = (dtnoDS!=null);
		
		this.getJToggleButtonEnabledPagination().setSelected(isEnabledToolBar && this.getPaginationDataLoader().isPaginationActivated()==true);
		this.setJToggleButtonEnabledPaginationIcon();
		
		this.getJButtonDatasetFirst().setEnabled(isEnabledToolBar);
		this.getJButtonDatasetPrevious().setEnabled(isEnabledToolBar);
		this.getJTextFieldDatasetNo().setEnabled(isEnabledToolBar);
		this.getJButtonDatasetNext().setEnabled(isEnabledToolBar);
		this.getJButtonDatasetLast().setEnabled(isEnabledToolBar);
		
		this.getJToggleButtonEnabledPagination().setEnabled(isEnabledToolBar);
		this.getJLabelRowsPerPage().setEnabled(isEnabledToolBar);
		this.getJTextFieldRowsPerPage().setEnabled(isEnabledToolBar);
		
		this.getJLabelPagesLoaded().setEnabled(isEnabledToolBar);
		this.getJTextFieldPageLoaded().setEnabled(isEnabledToolBar);
	}

	private JButton getJButtonDatasetFirst() {
		if (jButtonDatasetFirst==null) {
			jButtonDatasetFirst = new JButton(new AwbThemeImageIcon(BundleHelper.getImageIcon("Dataset_First.png")));
			jButtonDatasetFirst.setPreferredSize(new Dimension(26, 26));
			jButtonDatasetFirst.addActionListener(this);
		}
		return jButtonDatasetFirst;
	}
		
	private JButton getJButtonDatasetPrevious() {
		if (jButtonDatasetPrevious==null) {
			jButtonDatasetPrevious = new JButton(new AwbThemeImageIcon(BundleHelper.getImageIcon("Dataset_Previous.png")));
			jButtonDatasetPrevious.setPreferredSize(new Dimension(26, 26));
			jButtonDatasetPrevious.addActionListener(this);
		}
		return jButtonDatasetPrevious;
	}
	
	private JTextField getJTextFieldDatasetNo() {
		if (jTextFieldDatasetNo==null) {
			jTextFieldDatasetNo = new JTextField();
			jTextFieldDatasetNo.setPreferredSize(this.textFieldDimension);
			jTextFieldDatasetNo.setMaximumSize(this.textFieldDimension);
			jTextFieldDatasetNo.setFont(this.baseFont);
			jTextFieldDatasetNo.setHorizontalAlignment(JTextField.CENTER);
			jTextFieldDatasetNo.addKeyListener(new KeyAdapter4Numbers(false));
			jTextFieldDatasetNo.addActionListener(this);
			
		}
		return jTextFieldDatasetNo;
	}
	private JButton getJButtonDatasetNext() {
		if (jButtonDatasetNext==null) {
			jButtonDatasetNext = new JButton(new AwbThemeImageIcon(BundleHelper.getImageIcon("Dataset_Next.png")));
			jButtonDatasetNext.setPreferredSize(new Dimension(26, 26));
			jButtonDatasetNext.addActionListener(this);
		}
		return jButtonDatasetNext;
	}

	private JButton getJButtonDatasetLast() {
		if (jButtonDatasetLast==null) {
			jButtonDatasetLast = new JButton(new AwbThemeImageIcon(BundleHelper.getImageIcon("Dataset_Last.png")));
			jButtonDatasetLast.setPreferredSize(new Dimension(26, 26));
			jButtonDatasetLast.addActionListener(this);
		}
		return jButtonDatasetLast;
	}

	
	private JToggleButton getJToggleButtonEnabledPagination() {
		if (jToggleButtonEnabledPagination==null) {
			jToggleButtonEnabledPagination = new JToggleButton();
			jToggleButtonEnabledPagination.setToolTipText("Pagination On / Off");
			jToggleButtonEnabledPagination.setPreferredSize(new Dimension(26, 26));
			jToggleButtonEnabledPagination.setMinimumSize(new Dimension(26, 26));
			jToggleButtonEnabledPagination.addActionListener(this);
			this.setJToggleButtonEnabledPaginationIcon();
		}
		return jToggleButtonEnabledPagination;
	}
	
	private void setJToggleButtonEnabledPaginationIcon() {
		if (this.getJToggleButtonEnabledPagination().isSelected()==true) {
			this.getJToggleButtonEnabledPagination().setIcon(new AwbThemeImageIcon(BundleHelper.getImageIcon("Pagination-On.png")));
			this.getJToggleButtonEnabledPagination().setToolTipText("Pagination enabled");
		} else {
			this.getJToggleButtonEnabledPagination().setIcon(new AwbThemeImageIcon(BundleHelper.getImageIcon("Pagination-Off.png")));
			this.getJToggleButtonEnabledPagination().setToolTipText("Pagination disbaled");
		}
	}
	
	private JLabel getJLabelRowsPerPage() {
		if (jLabelRowsPerPage==null) {
			jLabelRowsPerPage = new JLabel("Rows / Page: ");
			jLabelRowsPerPage.setFont(baseFont.deriveFont(Font.BOLD));
		}
		return jLabelRowsPerPage;
	}
	private JTextField getJTextFieldRowsPerPage() {
		if (jTextFieldRowsPerPage==null) {
			jTextFieldRowsPerPage = new JTextField();
			jTextFieldRowsPerPage.setPreferredSize(this.textFieldDimension);
			jTextFieldRowsPerPage.setMaximumSize(this.textFieldDimension);
			jTextFieldRowsPerPage.setFont(this.baseFont);
			jTextFieldRowsPerPage.setHorizontalAlignment(JTextField.CENTER);
			jTextFieldRowsPerPage.addKeyListener(new KeyAdapter4Numbers(false));
			jTextFieldRowsPerPage.addActionListener(this);
		}
		return jTextFieldRowsPerPage;
	}
	
	private JLabel getJLabelPagesLoaded() {
		if (jLabelPageLoaded==null) {
			jLabelPageLoaded = new JLabel("  Pages-Loaded: ");
			jLabelPageLoaded.setFont(baseFont.deriveFont(Font.BOLD));
		}
		return jLabelPageLoaded;
	}
	private JTextField getJTextFieldPageLoaded() {
		if (jTextFieldPageLoaded==null) {
			jTextFieldPageLoaded = new JTextField();
			jTextFieldPageLoaded.setPreferredSize(this.textFieldDimension);
			jTextFieldPageLoaded.setMaximumSize(this.textFieldDimension);
			jTextFieldPageLoaded.setFont(baseFont);
			jTextFieldPageLoaded.setHorizontalAlignment(JTextField.CENTER);
			jTextFieldPageLoaded.setEditable(false);
		}
		return jTextFieldPageLoaded;
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		AbstractDataTreeNodeDataSource<?> dtnoDS = null;
		
		String propChanged = evt.getPropertyName();
		
		switch (propChanged) {
		case DataController.DC_DATA_LOADED:
			// --- Data could be loaded in a dedicated thread -------
			AffectedDataObjects ado = (AffectedDataObjects) evt.getNewValue();
			dtnoDS = ado.getDataTreeNodeObjectDataSource();
			
			if (SwingUtilities.isEventDispatchThread()==true) {
				// --- Just execute view adjustment -----------------
				this.setDetailView(dtnoDS);
				
			} else {
				// --- Hand over to Swing Thread --------------------
				final AbstractDataTreeNodeDataSource<?> dtnoDSFinal = dtnoDS; 
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						JPanelDataDetailView.this.setDetailView(dtnoDSFinal);
					}
				});
			}
			break;
			
		case DataController.DC_NEW_TREE_PATH_SELECTED:
			dtnoDS = this.getSelectedDataTreeNodeDataSource();
			this.setDetailView(dtnoDS);
			break;
			
		}
		
	}
	
	/**
	 * Sets the detail view.
	 * @param dtnoDS the data source tree node to show
	 */
	private void setDetailView(AbstractDataTreeNodeDataSource<?> dtnoDS) {

		// --- Direct exit? -----------------------------------------
		AbstractDataTreeNodeDataSource<?> dtnoDsSelected = this.getSelectedDataTreeNodeDataSource();
		boolean useDatalessView = (dtnoDS==null || dtnoDsSelected==null);
		boolean isDifferentData = (dtnoDS!=null && dtnoDsSelected!=null && dtnoDS!=dtnoDsSelected);
		if (isDifferentData==true) return;
		
		JComponent uiDetail = this.getJPanelDataless();
		if (useDatalessView==false) {
			
			Table tablesawDataTable = dtnoDS.getTable();
			if (tablesawDataTable!=null) {
				// --- Show table data ------------------------------
				this.getJTableData().setModel(new TablesawTableModel(tablesawDataTable));
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						JPanelDataDetailView.this.setDatasetSelection(dtnoDS.getRowSelected(), 0, true);
						JPanelDataDetailView.this.getJTextFieldRowsPerPage().setText(dtnoDS.getPaginationDataLoader().getNumberOfRecordsPerPage() + "");
						JPanelDataDetailView.this.getJTextFieldPageLoaded().setText(dtnoDS.getPaginationDataLoader().getPageNumberLoaded() + "");
					}
				});
				
			} else {
				// --- Try loading? ---------------------------------
				dtnoDS.loadDataWithinThread();
				this.getJTableData().setModel(new DefaultTableModel());
			}
			uiDetail = this.getJTableData();
		}
		this.setJToolDatasetNavigationEnabled();
		
		// --- Set UI component -------------------------------------
		this.getJScrollPaneData().setViewportView(uiDetail);
		this.getJScrollPaneData().validate();
		this.getJScrollPaneData().repaint();
		
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonDatasetFirst()) {
			this.setDatasetSelection(null, 0, true);
		} else if (ae.getSource()==this.getJButtonDatasetPrevious()) {
			this.setDatasetSelection(null, -1, true);
		} else if (ae.getSource()==this.getJButtonDatasetNext()) {
			this.setDatasetSelection(null, 1, true);
		} else if (ae.getSource()==this.getJButtonDatasetLast()) {
			this.setDatasetSelection(null, Integer.MAX_VALUE, true);
		} else if (ae.getSource()==this.getJTextFieldDatasetNo()) {
			// --- Jump to the selected dataset number --------------
			Integer dsNo2Select = null;
			try {
				dsNo2Select = Integer.parseInt(this.getJTextFieldDatasetNo().getText());
			} catch (Exception ex) { }
			if (dsNo2Select!=null) {
				this.setDatasetSelection(dsNo2Select, 0, true);
			}
			
		} else if (ae.getSource()==this.getJToggleButtonEnabledPagination()) {
			// --- React on pagination toggle ----------------------- 
			this.setJToggleButtonEnabledPaginationIcon();
			this.getPaginationDataLoader().setPaginationActivated(this.getJToggleButtonEnabledPagination().isSelected());
			this.getSelectedDataTreeNodeDataSource().reloadTable();
			
		} else if (ae.getSource()==this.getJTextFieldRowsPerPage()) {
			// --- Change the number of rows per page ---------------
			int newRowsPerPage = -1;
			try {
				newRowsPerPage = Integer.parseInt(this.getJTextFieldRowsPerPage().getText().trim());
				if (newRowsPerPage != this.getPaginationDataLoader().getNumberOfRecordsPerPage()) {
					this.getPaginationDataLoader().setNumberOfRecordsPerPage(newRowsPerPage);
					this.getSelectedDataTreeNodeDataSource().reloadTable();
					this.getJButtonDatasetLast().requestFocus();
				}
				
			} catch (Exception ex) { }
			
		}
	}
	
	/**
	 * Sets the dataset selection.
	 *
	 * @param dataRowToSelect the data row to select; may be <code>null</code> for direction!=0 and isSelectInTable==true
	 * @param direction the second index
	 * @param isSelectInTable the is select in table
	 */
	private void setDatasetSelection(Integer dataRowToSelect, int direction, boolean isSelectInTable) {
		
		AbstractDataTreeNodeDataSource<?> dtnoDS = this.getSelectedDataTreeNodeDataSource();
		if (dtnoDS==null) return;
		
		// --- Adjust number of data row text field -----------------
		ListSelectionModel selModel = this.getJTableData().getSelectionModel();
		if (selModel.getSelectedItemsCount()==1) {
			int rowSelected = selModel.getSelectedIndices()[0] + 1;
			this.getJTextFieldDatasetNo().setText(rowSelected + "");
		    // --- Remind selected data row ------------------------- 
		    dtnoDS.setRowSelected(rowSelected);
		    
		} else {
			this.getJTextFieldDatasetNo().setText("");
		}
		
		// --- Adjust table selection? ------------------------------
		if (isSelectInTable==true) {
			
			if (dataRowToSelect!=null) {
				// --- React on text field changes ------------------ 
				if (dataRowToSelect<1) {
					dataRowToSelect = 1;
				} else if (dataRowToSelect > this.getJTableData().getRowCount()) {
					this.setDatasetSelection(null, Integer.MAX_VALUE, true);
					return;
				}
				
			} else {
				// --- React on navigation buttons ------------------
				switch (direction) {
				case 0:
					dataRowToSelect = 1;
					break;
				case -1: 
					dataRowToSelect = this.getSelectedDataRow(selModel.getSelectedIndices(), false) - 1;
					if (dataRowToSelect<1) dataRowToSelect = 1;
					break;
				case 1: 
					dataRowToSelect = this.getSelectedDataRow(selModel.getSelectedIndices(), true) + 1;
					if (dataRowToSelect>this.getJTableData().getRowCount()) {
						dataRowToSelect = this.getJTableData().getRowCount();
					}
					if (dataRowToSelect==this.getJTableData().getRowCount() && dtnoDS.getPaginationDataLoader().isPaginationActivated()==true) {
						dtnoDS.loadDataWithinThread();
					}
					break;
				case Integer.MAX_VALUE:
					dataRowToSelect = this.getJTableData().getRowCount();
					if (dtnoDS.getPaginationDataLoader().isPaginationActivated()==true) {
						dtnoDS.loadDataWithinThread();
					}
					break;
				}
			}
			
			selModel.setSelectionInterval(dataRowToSelect-1, dataRowToSelect-1);
			this.getJTextFieldDatasetNo().setText(dataRowToSelect + "");
			
		    Rectangle rect = this.getJTableData().getCellRect(dataRowToSelect-1, 0, true);
		    this.getJTableData().scrollRectToVisible(rect);
			
		    // --- Remind selected data row ------------------------- 
		    dtnoDS.setRowSelected(dataRowToSelect);
		}
	}
	
	
	/**
	 * Returns a selected index depending on the direction, a user wants to navigate.
	 *
	 * @param selectedIndices the selected indices
	 * @param getMaxValue the get max value
	 * @return the selected index
	 */
	private int getSelectedDataRow(int[] selectedIndices, boolean getMaxValue) {
		return this.getSelectedIndex(selectedIndices, getMaxValue) + 1;
	}
	/**
	 * Returns a selected index depending on the direction, a user wants to navigate.
	 *
	 * @param selectedIndices the selected indices
	 * @param getMaxValue the get max value
	 * @return the selected index
	 */
	private int getSelectedIndex(int[] selectedIndices, boolean getMaxValue) {
		
		if (selectedIndices==null || selectedIndices.length==0) return 0;
		if (selectedIndices.length==1) return selectedIndices[0];
		
		List<Integer> selectedIndicesList = new ArrayList<>();
		for (int selectedIndex : selectedIndices) {
			selectedIndicesList.add(selectedIndex);
		}
		
		if (getMaxValue==true) {
			return selectedIndicesList.stream().max(Comparator.comparingInt(Math::abs)).orElseThrow(NoSuchElementException::new);
		}
		return selectedIndicesList.stream().min(Comparator.comparingInt(Math::abs)).orElseThrow(NoSuchElementException::new);
	}
	
}
