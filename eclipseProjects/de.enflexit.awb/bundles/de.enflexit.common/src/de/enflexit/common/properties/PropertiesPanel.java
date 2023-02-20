package de.enflexit.common.properties;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.common.BundleHelper;
import de.enflexit.common.properties.PropertiesTree.PropertyNodeUserObject;
import de.enflexit.common.swing.JTreeUtil;


/**
 * The Class PropertiesPanel provides an panel to edit {@link Properties}.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertiesPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 8199025287240543269L;

	public static final int COLUMN_DisplayInstruction = 0;
	public static final int COLUMN_PropertyName = 1;
	public static final int COLUMN_PropertyType = 2;
	public static final int COLUMN_PropertyValue = 3;
	
	
	private Properties properties;
	
	private JLabel jLabelHeader;

	private JPanel jPanelSearch;
	private JLabel jLabelSearch;
	private JTextField jTextFieldSearch;
	private JButton jButtonClearSearch;

	private JSeparator jSeparatorHeaderRight;
	private JToggleButton jToggleButtonTreeView;
	private JToggleButton jToggleButtonListView;
	
	private JSeparator jSeparatorHeaderLeft;
	private JButton jButtonAddProperty;
	private JButton jButtonRemoveProperty;

	private JScrollPane jScrollPaneProperties;
	private DefaultTableModel tableModelProperties;
	private JTable jTableProperties;
	private TableRowSorter<DefaultTableModel> jTableRowSorter;
	private RowFilter<DefaultTableModel, Integer> jTableRowFilter;
	
	
	private boolean debugDisplayInstruction = false;
	private boolean debugDisplayPropertiesTree = false;
	
	private PropertiesTree propertiesTree;
	private JScrollPane jScrollPaneTree;
	private JTree jTreeProperties;
	
	
	/**
	 * Instantiates a new properties panel.
	 */
	public PropertiesPanel() {
		this(null, "Properties");
	}
	
	/**
	 * Instantiates a new properties panel.
	 *
	 * @param properties the properties
	 * @param header the header to be shown
	 */
	public PropertiesPanel(Properties properties, String header) {
		this.initialize();
		this.setProperties(properties);
		this.getJLabelHeader().setText(header);
	}
	
	/**
	 * Return the current properties.
	 * @return the properties
	 */
	public Properties getProperties() {
		if (properties==null) {
			properties = new Properties();
		}
		return properties;
	}
	/**
	 * Sets the properties.
	 * @param properties the new properties
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
		this.resetPropertiesTree();
		this.refillTable();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(this.getJToggleButtonTreeView());
		bg.add(this.getJToggleButtonListView());
		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{300, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.insets = new Insets(10, 13, 0, 0);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		this.add(getJLabelHeader(), gbc_jLabelHeader);
		
		GridBagConstraints gbc_jPanelSearch = new GridBagConstraints();
		gbc_jPanelSearch.insets = new Insets(10, 10, 0, 5);
		gbc_jPanelSearch.fill = GridBagConstraints.BOTH;
		gbc_jPanelSearch.gridx = 1;
		gbc_jPanelSearch.gridy = 0;
		this.add(getJPanelSearch(), gbc_jPanelSearch);
		
		GridBagConstraints gbc_jSeparatorHeaderLeft = new GridBagConstraints();
		gbc_jSeparatorHeaderLeft.fill = GridBagConstraints.VERTICAL;
		gbc_jSeparatorHeaderLeft.insets = new Insets(10, 5, 0, 5);
		gbc_jSeparatorHeaderLeft.gridx = 2;
		gbc_jSeparatorHeaderLeft.gridy = 0;
		this.add(getJSeparatorHeaderLeft(), gbc_jSeparatorHeaderLeft);
		
		GridBagConstraints gbc_jToggleButtonTreeView = new GridBagConstraints();
		gbc_jToggleButtonTreeView.insets = new Insets(10, 5, 0, 0);
		gbc_jToggleButtonTreeView.gridx = 3;
		gbc_jToggleButtonTreeView.gridy = 0;
		this.add(getJToggleButtonTreeView(), gbc_jToggleButtonTreeView);
		
		GridBagConstraints gbc_jToggleButtonListView = new GridBagConstraints();
		gbc_jToggleButtonListView.insets = new Insets(10, 5, 0, 5);
		gbc_jToggleButtonListView.gridx = 4;
		gbc_jToggleButtonListView.gridy = 0;
		this.add(getJToggleButtonListView(), gbc_jToggleButtonListView);
		
		GridBagConstraints gbc_jSeparatorHeaderRight = new GridBagConstraints();
		gbc_jSeparatorHeaderRight.insets = new Insets(10, 5, 0, 5);
		gbc_jSeparatorHeaderRight.fill = GridBagConstraints.VERTICAL;
		gbc_jSeparatorHeaderRight.gridx = 5;
		gbc_jSeparatorHeaderRight.gridy = 0;
		this.add(getJSeparatorHeaderRight(), gbc_jSeparatorHeaderRight);
		
		GridBagConstraints gbc_jButtonAddProperty = new GridBagConstraints();
		gbc_jButtonAddProperty.insets = new Insets(10, 5, 0, 0);
		gbc_jButtonAddProperty.gridx = 6;
		gbc_jButtonAddProperty.gridy = 0;
		this.add(getJButtonAddProperty(), gbc_jButtonAddProperty);
		
		GridBagConstraints gbc_jButtonRemoveProperty = new GridBagConstraints();
		gbc_jButtonRemoveProperty.insets = new Insets(10, 5, 0, 10);
		gbc_jButtonRemoveProperty.gridx = 7;
		gbc_jButtonRemoveProperty.gridy = 0;
		this.add(getJButtonRemoveProperty(), gbc_jButtonRemoveProperty);
		
		GridBagConstraints gbc_jScrollPaneProperties = new GridBagConstraints();
		gbc_jScrollPaneProperties.gridwidth = 8;
		gbc_jScrollPaneProperties.insets = new Insets(5, 10, 10, 10);
		gbc_jScrollPaneProperties.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneProperties.gridx = 0;
		gbc_jScrollPaneProperties.gridy = 1;
		this.add(getJScrollPaneProperties(), gbc_jScrollPaneProperties);
		
		if (this.debugDisplayPropertiesTree==true) {
			GridBagConstraints gbc_jScrollPaneTree = new GridBagConstraints();
			gbc_jScrollPaneTree.insets = new Insets(5, 0, 10, 10);
			gbc_jScrollPaneTree.fill = GridBagConstraints.BOTH;
			gbc_jScrollPaneTree.gridx = 8;
			gbc_jScrollPaneTree.gridy = 1;
			this.add(getJScrollPaneTree(), gbc_jScrollPaneTree);
			gridBagLayout.columnWeights[gridBagLayout.columnWeights.length-2] = 1.0;
		}
		
	}

	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Properties");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	
	private JPanel getJPanelSearch() {
		if (jPanelSearch == null) {
			jPanelSearch = new JPanel();
			GridBagLayout gbl_jPanelSearch = new GridBagLayout();
			gbl_jPanelSearch.columnWidths = new int[]{0, 0, 0, 0};
			gbl_jPanelSearch.rowHeights = new int[]{0, 0};
			gbl_jPanelSearch.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelSearch.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelSearch.setLayout(gbl_jPanelSearch);
			GridBagConstraints gbc_jLabelSearch = new GridBagConstraints();
			gbc_jLabelSearch.gridx = 0;
			gbc_jLabelSearch.gridy = 0;
			jPanelSearch.add(getJLabelSearch(), gbc_jLabelSearch);
			GridBagConstraints gbc_jTextFieldSearch = new GridBagConstraints();
			gbc_jTextFieldSearch.insets = new Insets(0, 5, 0, 0);
			gbc_jTextFieldSearch.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldSearch.gridx = 1;
			gbc_jTextFieldSearch.gridy = 0;
			jPanelSearch.add(getJTextFieldSearch(), gbc_jTextFieldSearch);
			GridBagConstraints gbc_jButtonClearSearch = new GridBagConstraints();
			gbc_jButtonClearSearch.insets = new Insets(0, 5, 0, 0);
			gbc_jButtonClearSearch.gridx = 2;
			gbc_jButtonClearSearch.gridy = 0;
			jPanelSearch.add(getJButtonClearSearch(), gbc_jButtonClearSearch);
		}
		return jPanelSearch;
	}
	private JLabel getJLabelSearch() {
		if (jLabelSearch == null) {
			jLabelSearch = new JLabel("Search:");
			jLabelSearch.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSearch;
	}
	private JTextField getJTextFieldSearch() {
		if (jTextFieldSearch == null) {
			jTextFieldSearch = new JTextField();
			jTextFieldSearch.setPreferredSize(new Dimension(100, 26));
			jTextFieldSearch.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldSearch.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent ke) {
					PropertiesPanel.this.filterPropertiesTableModel();
				}
			});
		}
		return jTextFieldSearch;
	}
	private JButton getJButtonClearSearch() {
		if (jButtonClearSearch == null) {
			jButtonClearSearch = new JButton();
			jButtonClearSearch.setIcon(BundleHelper.getImageIcon("ClearSearch.png"));
			jButtonClearSearch.setToolTipText("Clear search");
			jButtonClearSearch.setPreferredSize(new Dimension(26, 26));
			jButtonClearSearch.addActionListener(this);
		}
		return jButtonClearSearch;
	}
	
	
	private JSeparator getJSeparatorHeaderLeft() {
		if (jSeparatorHeaderLeft == null) {
			jSeparatorHeaderLeft = new JSeparator();
			jSeparatorHeaderLeft.setOrientation(SwingConstants.VERTICAL);
		}
		return jSeparatorHeaderLeft;
	}
	private JToggleButton getJToggleButtonTreeView() {
		if (jToggleButtonTreeView == null) {
			jToggleButtonTreeView = new JToggleButton();
			jToggleButtonTreeView.setSelected(true);
			jToggleButtonTreeView.setIcon(BundleHelper.getImageIcon("TreeView.png"));
			jToggleButtonTreeView.setToolTipText("Switch to tree view");
			jToggleButtonTreeView.setPreferredSize(new Dimension(26, 26));
			jToggleButtonTreeView.addActionListener(this);
		}
		return jToggleButtonTreeView;
	}
	private JToggleButton getJToggleButtonListView() {
		if (jToggleButtonListView == null) {
			jToggleButtonListView = new JToggleButton();
			jToggleButtonListView.setIcon(BundleHelper.getImageIcon("ListView.png"));
			jToggleButtonListView.setToolTipText("Switch to list view");
			jToggleButtonListView.setPreferredSize(new Dimension(26, 26));
			jToggleButtonListView.addActionListener(this);
		}
		return jToggleButtonListView;
	}

	
	
	private JSeparator getJSeparatorHeaderRight() {
		if (jSeparatorHeaderRight == null) {
			jSeparatorHeaderRight = new JSeparator();
			jSeparatorHeaderRight.setOrientation(SwingConstants.VERTICAL);
		}
		return jSeparatorHeaderRight;
	}
	private JButton getJButtonAddProperty() {
		if (jButtonAddProperty == null) {
			jButtonAddProperty = new JButton();
			jButtonAddProperty.setIcon(BundleHelper.getImageIcon("ListPlus.png"));
			jButtonAddProperty.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonAddProperty.setPreferredSize(new Dimension(26, 26));
		}
		return jButtonAddProperty;
	}
	private JButton getJButtonRemoveProperty() {
		if (jButtonRemoveProperty == null) {
			jButtonRemoveProperty = new JButton();
			jButtonRemoveProperty.setIcon(BundleHelper.getImageIcon("ListMinus.png"));
			jButtonRemoveProperty.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonRemoveProperty.setPreferredSize(new Dimension(26, 26));
		}
		return jButtonRemoveProperty;
	}
	private JScrollPane getJScrollPaneProperties() {
		if (jScrollPaneProperties == null) {
			jScrollPaneProperties = new JScrollPane();
			jScrollPaneProperties.setViewportView(getJTableProperties());
		}
		return jScrollPaneProperties;
	}
	
	public DefaultTableModel getTableModelProperties() {
		if (tableModelProperties==null) {
			Vector<String> colName = new Vector<>();
			colName.add("Display Instruction");
			colName.add("Name");
			colName.add("Type");
			colName.add("Value");
			tableModelProperties = new DefaultTableModel(null, colName) {
				private static final long serialVersionUID = 7841309046550089999L;
				/* (non-Javadoc)
				 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
				 */
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					Class<?> columnClass = null;
					switch (columnIndex) {
					case COLUMN_DisplayInstruction:
					case COLUMN_PropertyName:
						columnClass = String.class;
						break;
					case COLUMN_PropertyType:
					case COLUMN_PropertyValue:
						columnClass = PropertyValue.class;
						break;
					}
					
					if (columnClass==null) {
						columnClass = super.getColumnClass(columnIndex);
					}
					return columnClass;
				}
			};
		}
		return tableModelProperties;
	}
	
	private JTable getJTableProperties() {
		if (jTableProperties == null) {
			jTableProperties = new JTable(this.getTableModelProperties());
			jTableProperties.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTableProperties.setFillsViewportHeight(true);
			jTableProperties.setAutoCreateRowSorter(true);
			jTableProperties.getTableHeader().setReorderingAllowed(false);
			
			// --- Define the row sorter and filter --------------------------- 
			jTableProperties.setRowSorter(this.getJTableRowSorter());
			
			// --- Configure the editor and the renderer of the cells ---------
			TableColumnModel tcm = jTableProperties.getColumnModel();
			TableColumn tc = null;
			
			// --- Define PropertyCellRenderer --------------------------------
			PropertyCellRenderer propRenderer = new PropertyCellRenderer();
			
			tc = tcm.getColumn(COLUMN_DisplayInstruction);
			tc.setMinWidth(this.debugDisplayInstruction==true ? 150 : 0);
			tc.setMaxWidth(this.debugDisplayInstruction==true ? 150 : 0);
			
			tc = tcm.getColumn(COLUMN_PropertyName);
			tc.setCellRenderer(propRenderer);
			tc.setPreferredWidth(100);
			tc.setMinWidth(100);
			
			tc = tcm.getColumn(COLUMN_PropertyType);
			tc.setCellRenderer(propRenderer);
			tc.setMinWidth(100);
			tc.setMaxWidth(100);
			
			tc = tcm.getColumn(COLUMN_PropertyValue);
			tc.setCellRenderer(propRenderer);
			tc.setPreferredWidth(600);
			tc.setMinWidth(100);
			
			jTableProperties.doLayout();
		}
		return jTableProperties;
	}
	
	/**
     * Returns the table row sorter.
     * @return the table row sorter
     */
    private TableRowSorter<DefaultTableModel> getJTableRowSorter() {
    	if (jTableRowSorter==null) {
    		// --- Define the sorter ------------
    		jTableRowSorter = new TableRowSorter<DefaultTableModel>(this.getTableModelProperties());
    		jTableRowSorter.setComparator(COLUMN_PropertyName, new Comparator<String>() {
				@Override
				public int compare(String s1, String s2) {
					return s1.compareTo(s2);
				}
			});
    		jTableRowSorter.setComparator(COLUMN_PropertyType,  PropertyValue.getComparator(0));
    		jTableRowSorter.setComparator(COLUMN_PropertyValue, PropertyValue.getComparator(1));
			// --- Define row filter ------------
			jTableRowSorter.setRowFilter(this.getJTableRowFilter());
    	}
    	return jTableRowSorter;
    }

    /**
   	 * Filters the properties table model according to the search string.
   	 */
   	private void filterPropertiesTableModel() {
   		try {
   			this.resetPropertiesTree();
   			if (this.getJToggleButtonTreeView().isSelected()==true) {
   				this.refillTable();
   			} else {
   				this.getJTableRowSorter().sort();
   			}
   			
   		} catch (Exception ex) {
   			ex.printStackTrace();
   		}
   	}
   	
    /**
     * Return the row filter.
     * @return the row filter
     */
    private RowFilter<DefaultTableModel, Integer> getJTableRowFilter() {
    	if (jTableRowFilter==null) {
    		jTableRowFilter = new RowFilter<DefaultTableModel, Integer>() {
			    @Override
    			public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
    				
			    	// --- Deactivate filter in tree view ---------------------
			    	if (PropertiesPanel.this.getJToggleButtonTreeView().isSelected()==true) return true;

			    	// --- Return true if the search phrase is empty ----------
    				String searchPhrase = PropertiesPanel.this.getJTextFieldSearch().getText().trim();
    				if ((searchPhrase==null || searchPhrase.isEmpty() || searchPhrase.isBlank())) return true;
    				
    				// --------------------------------------------------------
    				// --- Do the column value filtering ----------------------
					boolean rowMatch = false;
					for (int i = 0; i < entry.getValueCount(); i++) {
						
						String stringValue = null;
						Object entryObject = entry.getValue(i);
						if (entryObject==null) continue;
						
						switch (i) {
						case COLUMN_PropertyName:
							stringValue = ((String)entryObject).toString();
							break;
						case COLUMN_PropertyType:
							stringValue = ((PropertyValue)entryObject).getValueClass();
							break;
						case COLUMN_PropertyValue:
							stringValue = ((PropertyValue)entryObject).getValueString();
						}
						if (stringValue==null || stringValue.isEmpty()==true) continue;
						
						if (stringValue.toLowerCase().matches("(?i).*(" + searchPhrase.toLowerCase() + ").*")==true) {
							rowMatch = true;
							break;
						}
					}
					return rowMatch;
    			}
    		};
    	}
    	return jTableRowFilter;
    }
	
	/**
	 * Refills the properties table (clear & fill).
	 */
	private void refillTable() {
		this.getTableModelProperties().setRowCount(0);
		this.fillTable();
	}
	/**
	 * Fill the properties table.
	 */
	private void fillTable() {
		
		if (this.getJToggleButtonListView().isSelected()==true) {
			// --- Fill as simple list -------------------------------------------------- 
			List<String> idList = this.getProperties().getIdentifierList();
			for (String identifyer : idList) {
				// --- Get PropertyValue ------------------------------------------------
				PropertyValue pValue = this.getProperties().getPropertyValue(identifyer);
				this.addPropertyRow(null, identifyer, pValue);
			}
			
		} else {
			// --- Fill from tree model -------------------------------------------------
			List<DefaultMutableTreeNode> treeNodeList = this.getPropertiesTree().asList();
			for (DefaultMutableTreeNode treeNode : treeNodeList) {
				PropertyNodeUserObject pnuo = (PropertyNodeUserObject) treeNode.getUserObject();
				this.addPropertyRow(pnuo.getDisplayInstruction(), pnuo.getIdentifier(), pnuo.getPropertyValue());
			}
			
		}
	}
	/**
	 * Adds the property specified property.
	 *
	 * @param identifier the identifier of the property
	 * @param propertyValue the property value
	 */
	private void addPropertyRow(String displayInstruction, String identifier, PropertyValue propertyValue) {
		Vector<Object> row = new Vector<>();
		row.add(displayInstruction);
		row.add(identifier);
		row.add(propertyValue);
		row.add(propertyValue);
		this.getTableModelProperties().addRow(row);
	}
	
	
	
	private JScrollPane getJScrollPaneTree() {
		if (jScrollPaneTree == null) {
			jScrollPaneTree = new JScrollPane();
			jScrollPaneTree.setViewportView(getJTreeProperties());
		}
		return jScrollPaneTree;
	}
	private PropertiesTree getPropertiesTree() {
		if (propertiesTree==null) {
			propertiesTree = new PropertiesTree(this.getProperties());
		}
		return propertiesTree;
	}
	public void resetPropertiesTree() {
		this.propertiesTree = new PropertiesTree(this.getProperties(), this.getJTextFieldSearch().getText());
		this.getJTreeProperties().setModel(this.propertiesTree);
		JTreeUtil.setTreeExpandedState(this.getJTreeProperties(), true);
	}
	private JTree getJTreeProperties() {
		if (jTreeProperties == null) {
			jTreeProperties = new JTree(this.getPropertiesTree());
			jTreeProperties.setRootVisible(false);
			JTreeUtil.setTreeExpandedState(jTreeProperties, true);
		}
		return jTreeProperties;
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonAddProperty()) {
			// --- Add a new property ---------------------
			
		} else if (ae.getSource()==this.getJButtonRemoveProperty()) {
			// --- Remove current property ----------------
			
			
		} else if (ae.getSource()==this.getJButtonClearSearch()) {
			// --- Clear search ---------------------------
			this.getJTextFieldSearch().setText(null);
			this.filterPropertiesTableModel();

			
		} else if (ae.getSource()==this.getJToggleButtonTreeView()) {
			// --- Switch to list view --------------------
			this.refillTable();
			
		} else if (ae.getSource()==this.getJToggleButtonListView()) {
			// --- Switch to tree view --------------------
			this.refillTable();
		}
		
	}
	
	
}
