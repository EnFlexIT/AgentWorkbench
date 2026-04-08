package de.enflexit.df.core.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import de.enflexit.common.swing.AwbThemeImageIcon;
import de.enflexit.common.swing.KeyAdapter4Numbers;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.DataTreeNodeDataSource;
import de.enflexit.df.core.model.TablesawTableModel;
import tech.tablesaw.api.Table;

/**
 * The Class JPanelDataTable.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelDataTable extends JPanel implements PropertyChangeListener, ActionListener {

	private static final long serialVersionUID = -2503356793902058897L;

	private DataController dataController;
	private JScrollPane jScrollPaneData;
	private JTable jTableData;
	
	private JToolBar jToolBarDatasetNavigation;
	
	private JButton jButtonDatasetFirst;
	private JButton jButtonDatasetPrevious;
	private JTextField jTextFieldDatasetNo;
	private JButton jButtonDatasetNext;
	private JButton jButtonDatasetLast;
	
	
	/**
	 * Instantiates a new j panel data table.
	 * @param dataController the data controller
	 */
	public JPanelDataTable(DataController dataController) {
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
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jScrollPaneData = new GridBagConstraints();
		gbc_jScrollPaneData.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneData.gridx = 0;
		gbc_jScrollPaneData.gridy = 0;
		add(getJScrollPaneData(), gbc_jScrollPaneData);
		GridBagConstraints gbc_jToolBarDatasetNavigation = new GridBagConstraints();
		gbc_jToolBarDatasetNavigation.fill = GridBagConstraints.HORIZONTAL;
		gbc_jToolBarDatasetNavigation.insets = new Insets(5, 5, 5, 5);
		gbc_jToolBarDatasetNavigation.gridx = 0;
		gbc_jToolBarDatasetNavigation.gridy = 1;
		add(getJToolBarDatasetNavigation(), gbc_jToolBarDatasetNavigation);
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
	
	private JScrollPane getJScrollPaneData() {
		if (jScrollPaneData == null) {
			jScrollPaneData = new JScrollPane();
			jScrollPaneData.setViewportView(getJTableData());
		}
		return jScrollPaneData;
	}
	private JTable getJTableData() {
		if (jTableData == null) {
			jTableData = new JTable();
			jTableData.setFillsViewportHeight(true);
			jTableData.getTableHeader().setReorderingAllowed(false);
			jTableData.setDefaultRenderer(Object.class, new DateTimeTableCellRenderer("dd.MM.yyyy HH:mm:ss"));
			jTableData.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent lse) {
					if (lse.getValueIsAdjusting()==true) return;
					JPanelDataTable.this.setDatasetSelection(lse.getFirstIndex(), lse.getLastIndex(), false);
				}
			});
		}
		return jTableData;
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
		}
		return jToolBarDatasetNavigation;
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
			jTextFieldDatasetNo.setPreferredSize(new Dimension(60, 24));
			jTextFieldDatasetNo.setMaximumSize(new Dimension(60, 24));
			jTextFieldDatasetNo.setFont(new Font("Dialog", Font.PLAIN, 12));
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

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		String propChanged = evt.getPropertyName();
		
		switch (propChanged) {
		case DataController.DC_DATA_LOADED:
		case DataController.DC_NEW_TREE_PATH_SELECTED:
			
			DataTreeNodeDataSource<?> dtnoDS = this.getDataController().getSelectionModel().getSelectedDataTreeNodeDataSource();
			if (dtnoDS!=null) {
				TablesawTableModel tsTM = null;
				Table dataTable = dtnoDS.getTable();
				if (dataTable!=null) {
					tsTM = new TablesawTableModel(dataTable);
					this.getJTableData().setModel(tsTM);
				} else {
					this.getJTableData().setModel(new DefaultTableModel());
				}
			}
			break;
		}
		
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
			} catch (Exception ex) {
			}
			if (dsNo2Select!=null) {
				this.setDatasetSelection(dsNo2Select, 0, true);
			}
		}
	}
	
	/**
	 * Sets the dataset selection.
	 *
	 * @param dataRowToSelect the data row to select
	 * @param direction the second index
	 * @param isSelectInTable the is select in table
	 */
	private void setDatasetSelection(Integer dataRowToSelect, int direction, boolean isSelectInTable) {
		
		// --- Adjust number of data row text field -----------------
		ListSelectionModel selMode = this.getJTableData().getSelectionModel();
		if (selMode.getSelectedItemsCount()==1) {
			int rowSelected = selMode.getSelectedIndices()[0] + 1;
			this.getJTextFieldDatasetNo().setText(rowSelected + "");
		} else {
			this.getJTextFieldDatasetNo().setText("");
		}
		
		// --- Adjust table selection? ------------------------------
		if (isSelectInTable==true) {
			if (dataRowToSelect!=null) {
				// --- React on text field changes ------------------ 
				if (dataRowToSelect<1) {
					selMode.setSelectionInterval(0, 0);
					this.getJTextFieldDatasetNo().setText("1");
				} else if (dataRowToSelect>this.getJTableData().getRowCount()) {
					// TODO: Further load data or reduce number row row count  
				} else {
					selMode.setSelectionInterval(dataRowToSelect-1, dataRowToSelect-1);
				}
				
			} else {
				// --- React on navigation buttons ------------------
				switch (direction) {
				case 0:
					dataRowToSelect = 1;
					break;
				case -1: 
					dataRowToSelect = this.getSelectedDataRow(selMode.getSelectedIndices(), false) - 1;
					if (dataRowToSelect<1) dataRowToSelect = 1;
					break;
				case 1: 
					dataRowToSelect = this.getSelectedDataRow(selMode.getSelectedIndices(), true) + 1;
					if (dataRowToSelect>this.getJTableData().getRowCount()) dataRowToSelect = this.getJTableData().getRowCount();
					break;
				case Integer.MAX_VALUE:
					dataRowToSelect = this.getJTableData().getRowCount();
					break;
				}
				selMode.setSelectionInterval(dataRowToSelect-1, dataRowToSelect-1);
				this.getJTextFieldDatasetNo().setText(dataRowToSelect + "");
				
			}
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
