package de.enflexit.df.core.workbook.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import de.enflexit.df.core.extension.DataWorkbookExtension;
import de.enflexit.df.core.extension.ExtensionManager;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class JPanelExtensionSelection.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelExtensionSelection extends JPanel implements PropertyChangeListener {
	
	private static final long serialVersionUID = 1737633640686585339L;
	
	private DataController dataController;
	private DataWorkbook dataWorkbook; 
	
	private JLabel jLabelHeader;
	private JLabel jLabelHeaderDescription;

	private JScrollPane jScrollPaneTable;
	private DefaultTableModel tableModelExtensions;
	private JTable jTableExtensionSelection;
	
	/**
	 * Instantiates a new j panel extension selection.
	 *
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 */
	public JPanelExtensionSelection(DataController dataController, DataWorkbook dataWorkbook) {
		this.initialize();
		
		this.dataController = dataController;
		this.dataWorkbook = dataWorkbook;
		this.fillExtensionTable();
		if (this.dataController!=null) {
			this.dataController.addPropertyChangeListener(this);
		}
		
	}
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		this.add(this.getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jLabelHeaderDescription = new GridBagConstraints();
		gbc_jLabelHeaderDescription.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelHeaderDescription.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeaderDescription.gridx = 0;
		gbc_jLabelHeaderDescription.gridy = 1;
		this.add(this.getJLabelHeaderDescription(), gbc_jLabelHeaderDescription);
		GridBagConstraints gbc_jScrollPaneTable = new GridBagConstraints();
		gbc_jScrollPaneTable.insets = new Insets(5, 5, 5, 5);
		gbc_jScrollPaneTable.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneTable.gridx = 0;
		gbc_jScrollPaneTable.gridy = 2;
		this.add(this.getJScrollPaneTable(), gbc_jScrollPaneTable);
	}
	
	
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Registered Workbook-Extensions");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	private JLabel getJLabelHeaderDescription() {
		if (jLabelHeaderDescription == null) {
			jLabelHeaderDescription = new JLabel("Please, select the extensions that you want to use in your workbook");
			jLabelHeaderDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelHeaderDescription;
	}
	private JScrollPane getJScrollPaneTable() {
		if (jScrollPaneTable == null) {
			jScrollPaneTable = new JScrollPane();
			jScrollPaneTable.setViewportView(getJTableExtensionSelection());
		}
		return jScrollPaneTable;
	}
	
	private DefaultTableModel getTableModelExtensions() {
		if (tableModelExtensions==null) {
			
			Vector<String> header = new Vector<>();
			header.add("Select");
			header.add("Extension Name");
			header.add("Description");
			tableModelExtensions = new DefaultTableModel(null, header) {
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					Class<?> colClass = super.getColumnClass(columnIndex);
					switch (columnIndex) {
					case 0:
						colClass = Boolean.class;
						break;
					case 1:
						colClass = String.class;
						break;
					}
					return colClass;
				}
				@Override
				public boolean isCellEditable(int row, int column) {
					if (column==0) {
						return true;
					}
					return false;
				}
			};
		}
		return tableModelExtensions;
	}
	private JTable getJTableExtensionSelection() {
		if (jTableExtensionSelection == null) {
			jTableExtensionSelection = new JTable(this.getTableModelExtensions());
			jTableExtensionSelection.setFillsViewportHeight(true);
			jTableExtensionSelection.setFont(new Font("Dialog", Font.PLAIN, 11));
			jTableExtensionSelection.getTableHeader().setReorderingAllowed(false);
			
			TableColumnModel tcm = jTableExtensionSelection.getColumnModel();
			
			tcm.getColumn(0).setPreferredWidth(50);
			tcm.getColumn(0).setMinWidth(40);
			tcm.getColumn(0).setMaxWidth(60);
			
			tcm.getColumn(1).setPreferredWidth(200);
			tcm.getColumn(1).setMinWidth(160);
			tcm.getColumn(1).setMaxWidth(240);
			
		}
		return jTableExtensionSelection;
	}
	
	private void fillExtensionTable() {
		ExtensionManager.getDataWorkbookExtensionList(true).forEach(dwes -> this.addDataWorkbookExtensionService(dwes));
	}
	private void addDataWorkbookExtensionService(DataWorkbookExtension dwes) {
		
		Vector<Object> dataRow = new Vector<>();
		dataRow.add(ExtensionManager.isSelectedForDataWorkbook(dwes, this.dataWorkbook));
		dataRow.add(dwes.getExtensionName());
		dataRow.add(dwes.getExtensionDescription());
		this.getTableModelExtensions().addRow(dataRow);
	}
	
	/**
	 * Returns the list of selected extensions.
	 * @return the selected extensions
	 */
	private List<String> getSelectedExtensions() {
		
		List<String> extSelected = new ArrayList<>();
		for (int row = 0; row < this.getJTableExtensionSelection().getRowCount(); row++) {
			boolean isSelected = (Boolean) this.getJTableExtensionSelection().getValueAt(row, 0);
			String extensionName = (String) this.getJTableExtensionSelection().getValueAt(row, 1);
			if (isSelected==true) {
				extSelected.add(extensionName);
			}
		} 
		return extSelected;
	}
	/**
	 * Checks for changed selected extensions.
	 * @return the new list of extensions or <code>null</code> if nothing has changed
	 */
	private List<String> hasChangedSelectedExtensions() {
		
		List<String> extWorkbook = this.dataWorkbook.getWorkbookExtensions();
		List<String> extSelected = this.getSelectedExtensions();
		
		if (extSelected.size()!=extWorkbook.size()) return extSelected;
		
		for (String extension : extSelected) {
			if (extWorkbook.contains(extension)==false) {
				return extSelected;
			}
		}
		return null;
	}
	/**
	 * Updates activated extensions of a workbook.
	 */
	private void updateExtensionsOfWorkbook() {
		List<String> extChanged = this.hasChangedSelectedExtensions();
		if (extChanged!=null) {
			this.dataWorkbook.setWorkbookExtensions(extChanged);
			this.dataWorkbook.getExtensionCache().updateLoadedExtensions();
		}
	}
	
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		switch (evt.getPropertyName()) {
		case DataController.DC_PREPARE_FOR_SAVING_DATA_WORKBOOK:
			this.updateExtensionsOfWorkbook();
			break;
			
		}
	}
	
}
