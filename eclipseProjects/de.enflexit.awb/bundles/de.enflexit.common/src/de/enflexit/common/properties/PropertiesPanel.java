package de.enflexit.common.properties;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
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
public class PropertiesPanel extends JPanel implements ActionListener, PropertiesListener {
	
	private static final long serialVersionUID = 8199025287240543269L;

	public static final int COLUMN_DisplayInstruction = 0;
	public static final int COLUMN_PropertyName = 1;
	public static final int COLUMN_PropertyType = 2;
	public static final int COLUMN_PropertyValue = 3;
	
	
	private Properties properties;
	private boolean isReadOnly = false;
	
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

	private JSplitPane jSplitPaneProperties;
	
	private JScrollPane jScrollPaneProperties;
	private DefaultTableModel tableModelProperties;
	private JTable jTableProperties;
	private TableRowSorter<DefaultTableModel> jTableRowSorter;
	private RowFilter<DefaultTableModel, Integer> jTableRowFilter;
	private TableColumnModelListener tableColumnModelListener;
	
	private PropertiesEditPanel propertiesEditPanel;
	
	private boolean debugDisplayInstruction = false;
	private boolean debugDisplayPropertiesTree = false;
	
	private PropertiesTree propertiesTree;
	private JScrollPane jScrollPaneTree;
	private JTree jTreeProperties;
	private JSeparator jSeparatorTop;
	
	private ArrayList<String> requiredProperties;
	
	/**
	 * Instantiates a new properties panel.
	 */
	public PropertiesPanel() {
		this(null, "Properties", false);
	}
	/**
	 * Instantiates a new properties panel.
	 *
	 * @param properties the properties
	 * @param header the header to be shown
	 */
	public PropertiesPanel(Properties properties, String header) {
		this(properties, header, false);
	}
	/**
	 * Instantiates a new properties panel.
	 *
	 * @param properties the properties
	 * @param header the header
	 * @param isReadOnly the indicator that the properties shown are read only
	 */
	public PropertiesPanel(Properties properties, String header, boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
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
			this.setProperties(new Properties());
		}
		return properties;
	}
	/**
	 * Sets the properties.
	 * @param properties the new properties
	 */
	public void setProperties(Properties properties) {
		
		if (this.properties!=null) this.properties.removePropertiesListener(this);
		
		this.properties = properties;
		if (this.properties!=null) this.properties.addPropertiesListener(this);
		
		this.getJPanelPropertiesEdit().setProperties(this.properties);
		this.getJPanelPropertiesEdit().setIdentifier(null);
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
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.insets = new Insets(10, 13, 0, 10);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		this.add(this.getJLabelHeader(), gbc_jLabelHeader);
		
		GridBagConstraints gbc_jPanelSearch = new GridBagConstraints();
		gbc_jPanelSearch.insets = new Insets(10, 0, 0, 5);
		gbc_jPanelSearch.fill = GridBagConstraints.BOTH;
		gbc_jPanelSearch.gridx = 1;
		gbc_jPanelSearch.gridy = 0;
		this.add(this.getJPanelSearch(), gbc_jPanelSearch);
		
		GridBagConstraints gbc_jSeparatorHeaderLeft = new GridBagConstraints();
		gbc_jSeparatorHeaderLeft.fill = GridBagConstraints.VERTICAL;
		gbc_jSeparatorHeaderLeft.insets = new Insets(10, 5, 0, 5);
		gbc_jSeparatorHeaderLeft.gridx = 2;
		gbc_jSeparatorHeaderLeft.gridy = 0;
		this.add(this.getJSeparatorHeaderLeft(), gbc_jSeparatorHeaderLeft);
		
		GridBagConstraints gbc_jToggleButtonTreeView = new GridBagConstraints();
		gbc_jToggleButtonTreeView.insets = new Insets(10, 5, 0, 0);
		gbc_jToggleButtonTreeView.gridx = 3;
		gbc_jToggleButtonTreeView.gridy = 0;
		this.add(this.getJToggleButtonTreeView(), gbc_jToggleButtonTreeView);
		
		GridBagConstraints gbc_jToggleButtonListView = new GridBagConstraints();
		gbc_jToggleButtonListView.insets = new Insets(10, 5, 0, this.isReadOnly==false ? 5 : 10);
		gbc_jToggleButtonListView.gridx = 4;
		gbc_jToggleButtonListView.gridy = 0;
		this.add(this.getJToggleButtonListView(), gbc_jToggleButtonListView);
		
		GridBagConstraints gbc_jSeparatorHeaderRight = new GridBagConstraints();
		gbc_jSeparatorHeaderRight.insets = new Insets(10, 5, 0, 5);
		gbc_jSeparatorHeaderRight.fill = GridBagConstraints.VERTICAL;
		gbc_jSeparatorHeaderRight.gridx = 5;
		gbc_jSeparatorHeaderRight.gridy = 0;
		this.add(this.getJSeparatorHeaderRight(), gbc_jSeparatorHeaderRight);
		
		GridBagConstraints gbc_jButtonAddProperty = new GridBagConstraints();
		gbc_jButtonAddProperty.insets = new Insets(10, 5, 0, 0);
		gbc_jButtonAddProperty.gridx = 6;
		gbc_jButtonAddProperty.gridy = 0;
		this.add(this.getJButtonAddProperty(), gbc_jButtonAddProperty);
		
		GridBagConstraints gbc_jButtonRemoveProperty = new GridBagConstraints();
		gbc_jButtonRemoveProperty.insets = new Insets(10, 5, 0, 10);
		gbc_jButtonRemoveProperty.gridx = 7;
		gbc_jButtonRemoveProperty.gridy = 0;
		this.add(this.getJButtonRemoveProperty(), gbc_jButtonRemoveProperty);
		if (this.isReadOnly==true) {
			this.remove(this.getJSeparatorHeaderRight());
			this.remove(this.getJButtonAddProperty());
			this.remove(this.getJButtonRemoveProperty());
		}
		
		GridBagConstraints gbc_jSeparatorTop = new GridBagConstraints();
		gbc_jSeparatorTop.insets = new Insets(5, 10, 0, 10);
		gbc_jSeparatorTop.gridwidth = 8;
		gbc_jSeparatorTop.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSeparatorTop.gridx = 0;
		gbc_jSeparatorTop.gridy = 1;
		this.add(getJSeparatorTop(), gbc_jSeparatorTop);
		
		GridBagConstraints gbc_jScrollPaneProperties = new GridBagConstraints();
		gbc_jScrollPaneProperties.gridwidth = 8;
		gbc_jScrollPaneProperties.insets = new Insets(5, 10, 10, 10);
		gbc_jScrollPaneProperties.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneProperties.gridx = 0;
		gbc_jScrollPaneProperties.gridy = 2;
		this.add(this.getJSplitPaneProperties(), gbc_jScrollPaneProperties);
		if (this.isReadOnly==true) {
			this.remove(this.getJSplitPaneProperties());
			this.add(this.getJScrollPaneProperties(),  gbc_jScrollPaneProperties);
		}
		
		if (this.debugDisplayPropertiesTree==true) {
			GridBagConstraints gbc_jScrollPaneTree = new GridBagConstraints();
			gbc_jScrollPaneTree.insets = new Insets(5, 0, 10, 10);
			gbc_jScrollPaneTree.fill = GridBagConstraints.BOTH;
			gbc_jScrollPaneTree.gridx = 8;
			gbc_jScrollPaneTree.gridy = 2;
			this.add(this.getJScrollPaneTree(), gbc_jScrollPaneTree);
			gridBagLayout.columnWeights[gridBagLayout.columnWeights.length-2] = 1.0;
		}
		
		// --- Update the column width information in the PropertiesEditPanel ----
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent ce) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						PropertiesPanel.this.updateColumnWidthInEditor();
					}
				});
			}
			@Override
			public void componentResized(ComponentEvent ce) {
				PropertiesPanel.this.updateColumnWidthInEditor();
			}
		});
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
			jPanelSearch.setMinimumSize(new Dimension(200, 26));
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
	private boolean isTreeView() {
		return (this.getJToggleButtonTreeView().isSelected()==true);
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
			jButtonAddProperty.addActionListener(this);
		}
		return jButtonAddProperty;
	}
	private JButton getJButtonRemoveProperty() {
		if (jButtonRemoveProperty == null) {
			jButtonRemoveProperty = new JButton();
			jButtonRemoveProperty.setIcon(BundleHelper.getImageIcon("ListMinus.png"));
			jButtonRemoveProperty.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonRemoveProperty.setPreferredSize(new Dimension(26, 26));
			jButtonRemoveProperty.addActionListener(this);
		}
		return jButtonRemoveProperty;
	}
	
	private JSeparator getJSeparatorTop() {
		if (jSeparatorTop == null) {
			jSeparatorTop = new JSeparator();
		}
		return jSeparatorTop;
	}
	
	private JSplitPane getJSplitPaneProperties() {
		if (jSplitPaneProperties==null) {
			jSplitPaneProperties = new JSplitPane();
			jSplitPaneProperties.setDividerSize(5);
			jSplitPaneProperties.setDividerLocation(0.3);
			jSplitPaneProperties.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPaneProperties.setTopComponent(this.getJPanelPropertiesEdit());
			jSplitPaneProperties.setBottomComponent(this.getJScrollPaneProperties());
		}
		return jSplitPaneProperties;
	}
	
	private JScrollPane getJScrollPaneProperties() {
		if (jScrollPaneProperties == null) {
			jScrollPaneProperties = new JScrollPane();
			jScrollPaneProperties.setViewportView(this.getJTableProperties());
		}
		return jScrollPaneProperties;
	}
	private DefaultTableModel getTableModelProperties() {
		if (tableModelProperties==null) {
			Vector<String> colName = new Vector<>();
			colName.add("Display Instruction");
			colName.add("Name");
			colName.add("Type");
			colName.add("Value");
			tableModelProperties = new DefaultTableModel(null, colName) {
				private static final long serialVersionUID = 7841309046550089999L;
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
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		}
		return tableModelProperties;
	}
	/**
	 * Returns the JTable for the properties.
	 * @return the properties table 
	 */
	private JTable getJTableProperties() {
		if (jTableProperties == null) {
			jTableProperties = new JTable(this.getTableModelProperties());
			jTableProperties.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTableProperties.setFillsViewportHeight(true);
			jTableProperties.setAutoCreateRowSorter(true);
			jTableProperties.getTableHeader().setReorderingAllowed(false);
			
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
			tc.setPreferredWidth(200);
			tc.setMinWidth(150);
			
			tc = tcm.getColumn(COLUMN_PropertyType);
			tc.setCellRenderer(propRenderer);
			tc.setMinWidth(80);
			tc.setMaxWidth(80);
			
			tc = tcm.getColumn(COLUMN_PropertyValue);
			tc.setCellRenderer(propRenderer);
			tc.setPreferredWidth(600);
			tc.setMinWidth(100);
			
			tcm.addColumnModelListener(this.getTableColumnModelListener());
			
			// --- Add selection listener -------------------------------------
			jTableProperties.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
				private int lastSelectedRow = -1;
        		public void valueChanged(ListSelectionEvent event) {
        			
        			if (event.getValueIsAdjusting()==true) return;
        			
		        	String identifier = null;
		        	JTable tbProperies = PropertiesPanel.this.getJTableProperties();
		        	int selectedRow = tbProperies.getSelectedRow();
		        	if (tbProperies.getSelectedRowCount()==1 && selectedRow!=this.lastSelectedRow) {
		        		// --- Got new single selection -----------------------
		        		identifier = (String) tbProperies.getValueAt(selectedRow, COLUMN_PropertyName);
		        		if (identifier==null) {
		        			identifier = (String) tbProperies.getValueAt(selectedRow, COLUMN_DisplayInstruction);
		        		}
		        		this.lastSelectedRow = selectedRow;
		        	
		        	} else if (tbProperies.getSelectedRowCount()==0) {
		        		// --- Select was cleared -----------------------------
		        		this.lastSelectedRow = selectedRow;
		        		
		        	}
		        	// --- Update view of properties editor -------------------
		        	PropertiesPanel.this.getJPanelPropertiesEdit().setIdentifier(identifier);
		        	
		        	// --- Required properties can't be removed ---------------
		        	if (PropertiesPanel.this.isRequiredProperty(identifier)) {
		        		PropertiesPanel.this.setRemoveButtonEnabled(false);
		        	} else {
		        		PropertiesPanel.this.setRemoveButtonEnabled(true);
		        	}
		        }
		    });
			
			// --- Define the row sorter and filter --------------------------- 
			jTableProperties.setRowSorter(this.getJTableRowSorter());
		}
		return jTableProperties;
	}
	/**
     * Gets the table column model listener.
     * @return the table column model listener
     */
    private TableColumnModelListener getTableColumnModelListener() {
    	if (tableColumnModelListener==null) {
    		tableColumnModelListener = new TableColumnModelListener() {
    			@Override
    			public void columnMarginChanged(ChangeEvent e) {
    				PropertiesPanel.this.updateColumnWidthInEditor();
    			}
				@Override
				public void columnSelectionChanged(ListSelectionEvent e) { }
				@Override
				public void columnRemoved(TableColumnModelEvent e) { }
				@Override
				public void columnMoved(TableColumnModelEvent e) { }
				@Override
				public void columnAdded(TableColumnModelEvent e) { }
    		};
    	}
    	return tableColumnModelListener;
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
     * Return the row filter.
     * @return the row filter
     */
    private RowFilter<DefaultTableModel, Integer> getJTableRowFilter() {
    	if (jTableRowFilter==null) {
    		jTableRowFilter = new RowFilter<DefaultTableModel, Integer>() {
			    @Override
    			public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
    				
			    	// --- Deactivate filter in tree view ---------------------
			    	if (PropertiesPanel.this.isTreeView()==true) return true;

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
   	 * Filters the properties table model according to the search string.
   	 */
   	private void filterPropertiesTableModel() {
   		try {
   			this.refillTable();
   			if (this.isTreeView()==false) {
   				this.getJTableRowSorter().sort();
   			}
   			
   		} catch (Exception ex) {
   			ex.printStackTrace();
   		}
   	}
    
	/**
	 * Refills the properties table (clear & fill).
	 */
	private void refillTable() {
		this.getTableModelProperties().setRowCount(0);
		this.resetPropertiesTree();
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
	
	/**
	 * Returns the selected identifier from the current selection.
	 * @return the selected identifier from selection, if only one row is selected
	 */
	@SuppressWarnings("unused")
	private String getSelectedIdentifier() {
		
		List<String> idList = this.getSelectedIdentifiers();
		if (idList!=null && idList.size()==1) {
			return idList.get(0);
		}
		return null;
	}
	/**
	 * Returns the list of selected identifier from the current selection.
	 * @return the selected identifier from selection
	 */
	private List<String> getSelectedIdentifiers() {
		
		List<String> idList = null;
		int[] selRows = this.getJTableProperties().getSelectedRows();
		if (selRows!=null && selRows.length>0) {
			// --- Get the corresponding identifier of the ----------
			idList = this.getIdentifierFromRows(selRows);
		}
		return idList;
	}
	/**
	 * Returns the list of identifier from row selection.
	 *
	 * @param rowsSelected the rows selected
	 * @return the list of identifier from the row selection, or <code>null</code> if nothing is selected
	 */
	private List<String> getIdentifierFromRows(int[] rowsSelected) {
		
		if (rowsSelected==null || rowsSelected.length==0) return null;
		
		List<String> idList = new ArrayList<>();
		for (int i = 0; i < rowsSelected.length; i++) {
			// --- Get the identifier ---------------------
			String displayInstruction  = (String) this.getJTableProperties().getValueAt(rowsSelected[i], COLUMN_DisplayInstruction);
			String identifier = (String) this.getJTableProperties().getValueAt(rowsSelected[i], COLUMN_PropertyName);
			if (identifier==null && displayInstruction!=null) {
				// --- Get node of this row ---------------
				List<String> subIdList = this.getPropertiesTree().getIdentifierFromDisplayInstruction(displayInstruction);
				if (subIdList!=null) {
					idList.addAll(subIdList);
				}
			} else {
				idList.add(identifier);
			}
			
		}
		return idList;
	}
	
	
	/**
	 * Sets the table selection to the property specified by its identifier.
	 * @param identifier the new table selection
	 */
	private void setSelection(String identifier) {
		List<String> idList = new ArrayList<>();
		idList.add(identifier);
		this.setSelection(idList);
	}
	/**
	 * Sets the table selection to the properties specified by the identifier list.
	 * @param idList the new table selection
	 */
	private void setSelection(List<String> idList) {
		
		if (idList==null || idList.size()==0) return;

		this.getJTableProperties().getSelectionModel().clearSelection();
		
		for (int i = 0; i < this.getJTableProperties().getRowCount(); i++) {
			String identifier = (String) this.getJTableProperties().getValueAt(i, COLUMN_PropertyName);
			if (idList.contains(identifier)==true) {
				this.getJTableProperties().getSelectionModel().addSelectionInterval(i, i);
			}
		}
	}

	
	/**
	 * Returns the PropertiesEditPanel.
	 * @return the j panel properties edit
	 */
	private PropertiesEditPanel getJPanelPropertiesEdit() {
		if (propertiesEditPanel==null) {
			propertiesEditPanel = new PropertiesEditPanel(this.getProperties());
		}
		return propertiesEditPanel;
	}
	/**
	 * Updates the column width information in the edit panel.
	 */
	private void updateColumnWidthInEditor() {
		
		int[] tbColumWidth = new int[3];
		TableColumnModel tcm = this.getJTableProperties().getColumnModel();
		tbColumWidth[0] = tcm.getColumn(COLUMN_DisplayInstruction).getWidth() + tcm.getColumn(COLUMN_PropertyName).getWidth();
		tbColumWidth[1] = tcm.getColumn(COLUMN_PropertyType).getWidth();
		tbColumWidth[2] = tcm.getColumn(COLUMN_PropertyValue).getWidth();
		this.getJPanelPropertiesEdit().updateColumnWidth(tbColumWidth);
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
			this.getJTableProperties().getSelectionModel().clearSelection();
			
		} else if (ae.getSource()==this.getJButtonRemoveProperty()) {
			// --- Remove current property ----------------
			this.removeSelectedProperties();
			
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

	/* (non-Javadoc)
	 * @see de.enflexit.common.properties.PropertiesListener#onPropertiesEvent(de.enflexit.common.properties.PropertiesEvent)
	 */
	@Override
	public void onPropertiesEvent(PropertiesEvent propertiesEvent) {

		switch (propertiesEvent.getAction()) {
		case PropertyAdded:
		case PropertyUpdate:
			this.filterPropertiesTableModel();
			this.setSelection(propertiesEvent.getIdentifier());
			this.getJTableProperties().requestFocus();
			break;
			
		case PropertyRemoved:
			// --- Handled above already ---
			break;
			
		case PropertiesCleared:
			this.filterPropertiesTableModel();
			this.getJPanelPropertiesEdit().setIdentifier(null);
			break;
		}
	}

	
	/**
	 * Removes the selected properties.
	 */
	private void removeSelectedProperties() {
		
		List<String> idListToRemove = this.getSelectedIdentifiers(); 
		if (idListToRemove!=null && idListToRemove.size()>0) {
			// --- Explicitly (re-)select identifier ---------------- 
			this.setSelection(idListToRemove);
			
			// --- Prepare message ----------------------------------
			String title = "Delete selected property?";
			String msg = "Are you sure to delete the currently selected property?";
			if (idListToRemove.size()>1) {
				title = "Delete selected properties?";
				msg = "Are you sure to delete the selected " + idListToRemove.size() + " properties?";
			}
			// --- Ask user -----------------------------------------
			if (JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION) {
				for (String identifier : idListToRemove) {
					this.getProperties().remove(identifier);
				}
				this.filterPropertiesTableModel();
			}
		}
	}
	
	/**
	 * Adds a property to the read only list, editor components will be disabled for that property then.
	 * Read only properties are also added to the required list.
	 * @param propertyKey the property key
	 */
	public void addReadOnlyProperty(String propertyKey) {
		this.getJPanelPropertiesEdit().addReadOnlyProperty(propertyKey);
		this.getRequiredProperties().add(propertyKey);
	}
	
	/**
	 * Removes a property from the read only list.
	 * @param propertyKey the property key
	 */
	public void removeReadOnyProperty(String propertyKey) {
		this.getJPanelPropertiesEdit().removeReadOnyProperty(propertyKey);
		this.getRequiredProperties().remove(propertyKey);
	}
	
	/**
	 * Adds several properties to the read only list, editor components will be disabled for these properties then.
	 * Read only properties are also added to the required list.
	 * @param propertyKeys the property keys
	 */
	public void addReadOnlyProperties(List<String> propertyKeys) {
		this.getJPanelPropertiesEdit().addReadOnlyProperties(propertyKeys);
		this.getRequiredProperties().addAll(propertyKeys);
	}
	
	private ArrayList<String> getRequiredProperties() {
		
		if (requiredProperties==null) {
			requiredProperties = new ArrayList<>();
		}
		return requiredProperties;
	}
	
	/**
	 * Adds a property to the required properties list, it can't be deleted then.
	 * Read only properties are also added to the required list automatically, you don't have to do that separately. 
	 * @param propertyKey the property key
	 */
	public void addRequiredProperty(String propertyKey) {
		this.getRequiredProperties().add(propertyKey);
	}
	
	/**
	 * Removes a property from the required properties list.
	 * @param propertyKey the property key
	 */
	public void removeRequiredProperty(String propertyKey) {
		this.getRequiredProperties().remove(propertyKey);
	}
	
	/**
	 * Adds a list of properties to the required properties list, they can't be deleted then.
	 * Read only properties are also added to the required list automatically, you don't have to do that separately.
	 * @param propertyKeys the property keys
	 */
	public void addRequiredProperties(List<String> propertyKeys) {
		this.getRequiredProperties().addAll(propertyKeys);
	}
	
	/**
	 * Checks if a property is required.
	 * @param propertyKey the property key
	 * @return true, if is required property
	 */
	public boolean isRequiredProperty(String propertyKey) {
		return this.getRequiredProperties().contains(propertyKey);
	}
	
	/**
	 * Enables or disables the remove button.
	 * @param enabled the new removes the button enabled
	 */
	private void setRemoveButtonEnabled(boolean enabled) {
		this.getJButtonRemoveProperty().setEnabled(enabled);
		PropertiesPanel.this.getJButtonRemoveProperty().setToolTipText((enabled==true) ? null : "This property is required, and can't be removed");
	}
	
}
