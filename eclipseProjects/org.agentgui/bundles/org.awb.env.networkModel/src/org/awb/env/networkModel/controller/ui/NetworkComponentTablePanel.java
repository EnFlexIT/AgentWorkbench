package org.awb.env.networkModel.controller.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS.ComponentSorting;

import agentgui.core.application.Language;
import de.enflexit.common.ServiceFinder;

/**
 * This class provides a table containing a list of network components in a
 * network model, including a text filter and editing functions.
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkComponentTablePanel extends JPanel implements TableModelListener {

    private static final long serialVersionUID = -3251961286476097320L;

    private static final String newLine = "\n";

    private GraphEnvironmentController graphController;
    private List<NetworkComponent> netCompsToDisplay;
    private boolean netCompsToDisplayWereSetExternally;

    private boolean editingEnabled;
    private boolean enableMultipleSelection;

    private List<NetworkComponentTableService> tableServiceList;

    private JLabel jLabelTable;
    private JTextField jTextFieldSearch;
    private JButton jButtonClearSearch;
    private JScrollPane jScrollPaneTable;

    private DefaultTableModel componentsTableModel;
    private JTable jTableComponents;
    private TableRowSorter<DefaultTableModel> jTableRowSorter;
    private ComponentSorting componentSorting;
    private RowFilter<DefaultTableModel, Integer> jTableRowFilter;

    private boolean tabelModelListenerPaused;
    private Vector<String> excludeList;
    private NetworkComponent currNetworkComponent;

    /**
     * Instantiates a new network component table panel. To directly show the
     * available network components, invoke {@link #reLoadNetworkComponents()} or
     * set an exclude list by using {@link #setExcludeList(Vector)} and call
     * {@link #reLoadNetworkComponents()}.<br>
     * Alternatively, set the list of NetworkCompents by using
     * {@link #setNetworkComponentList(List)}. These will be displayed immediately.
     * wbp.parser.constructor
     * 
     * @param graphController         the graph controller
     * @param enableEditing           if true, the table can be used to edit network
     *                                component
     * @param enableMultipleSelection if true, multiple network components can be
     *                                selected
     * 
     */
    public NetworkComponentTablePanel(GraphEnvironmentController graphController, boolean enableEditing,
	    boolean enableMultipleSelection) {
	this(graphController, enableEditing, enableMultipleSelection, null);
    }

    /**
     * Instantiates a new network component table panel. To directly show the
     * available network components, invoke {@link #reLoadNetworkComponents()} or
     * set an exclude list by using {@link #setExcludeList(Vector)} and call
     * {@link #reLoadNetworkComponents()}.<br>
     * Alternatively, set the list of NetworkCompents by using
     * {@link #setNetworkComponentList(List)}. These will be displayed immediately.
     *
     * @param graphController         the graph controller
     * @param enableEditing           if true, the table can be used to edit network
     *                                component
     * @param enableMultipleSelection if true, multiple network components can be
     *                                selected
     * @param columnServicesToAdd     the table services to considered and to add
     *                                further columns to the table
     */
    public NetworkComponentTablePanel(GraphEnvironmentController graphController, boolean enableEditing,
	    boolean enableMultipleSelection, List<NetworkComponentTableService> columnServicesToAdd) {
	this.graphController = graphController;
	this.editingEnabled = enableEditing;
	this.enableMultipleSelection = enableMultipleSelection;
	if (columnServicesToAdd != null && columnServicesToAdd.size() > 0) {
	    this.getNetworkComponentTableServiceList().addAll(columnServicesToAdd);
	}
	this.initialize();
    }

    /**
     * Returns the graph environment controller.
     * 
     * @return the graph environment controller
     */
    private GraphEnvironmentController getGraphController() {
	return this.graphController;
    }

    /**
     * Return the list of {@link NetworkComponentTableService} that extend the local
     * JTable.
     * 
     * @return the network component table service list
     */
    public List<NetworkComponentTableService> getNetworkComponentTableServiceList() {
	if (tableServiceList == null) {
	    tableServiceList = ServiceFinder.findServices(NetworkComponentTableService.class);
	}
	return tableServiceList;
    }

    /**
     * Initialize.
     */
    private void initialize() {

	GridBagConstraints gbcJLabelSearch = new GridBagConstraints();
	gbcJLabelSearch.insets = new Insets(0, 5, 0, 0);
	gbcJLabelSearch.gridx = 0;
	gbcJLabelSearch.gridy = 0;
	gbcJLabelSearch.anchor = GridBagConstraints.WEST;
	gbcJLabelSearch.gridwidth = 2;
	gbcJLabelSearch.fill = GridBagConstraints.HORIZONTAL;

	GridBagConstraints gbcJTextFieldSearch = new GridBagConstraints();
	gbcJTextFieldSearch.fill = GridBagConstraints.BOTH;
	gbcJTextFieldSearch.gridx = 0;
	gbcJTextFieldSearch.gridy = 1;
	gbcJTextFieldSearch.weightx = 0.5;
	gbcJTextFieldSearch.gridwidth = 1;
	gbcJTextFieldSearch.insets = new Insets(0, 0, 0, 1);

	GridBagConstraints gbcJButtonClearSearch = new GridBagConstraints();
	gbcJButtonClearSearch.gridx = 1;
	gbcJButtonClearSearch.gridy = 1;

	GridBagConstraints gbcJTableNetComps = new GridBagConstraints();
	gbcJTableNetComps.gridx = 0;
	gbcJTableNetComps.gridy = 2;
	gbcJTableNetComps.fill = GridBagConstraints.BOTH;
	gbcJTableNetComps.weightx = 1.0;
	gbcJTableNetComps.weighty = 1.0;
	gbcJTableNetComps.gridheight = 1;
	gbcJTableNetComps.gridwidth = 0;

	jLabelTable = new JLabel();
	jLabelTable.setFont(new Font("Dialog", Font.BOLD, 12));
	jLabelTable.setText("Search Components");
	jLabelTable.setText(Language.translate(jLabelTable.getText(), Language.EN));

	this.setLayout(new GridBagLayout());
	this.add(jLabelTable, gbcJLabelSearch);
	this.add(this.getJTextFieldSearch(), gbcJTextFieldSearch);
	this.add(this.getJButtonClearSearch(), gbcJButtonClearSearch);
	this.add(this.getJScrollPaneTable(), gbcJTableNetComps);
    }

    /**
     * This method initializes jTextFieldSearch Search box - Used for filtering the
     * components
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldSearch() {
	if (jTextFieldSearch == null) {
	    jTextFieldSearch = new JTextField();
	    jTextFieldSearch.setPreferredSize(new Dimension(100, 20));
	    jTextFieldSearch.addKeyListener(new java.awt.event.KeyAdapter() {
		@Override
		public void keyReleased(java.awt.event.KeyEvent e) {
		    NetworkComponentTablePanel.this.applyTableSorter();
		}
	    });
	}
	return jTextFieldSearch;
    }

    /**
     * This method initializes jButtonClearSearch
     * 
     * @return javax.swing.JButton
     */
    private JButton getJButtonClearSearch() {
	if (jButtonClearSearch == null) {
	    jButtonClearSearch = new JButton();
	    jButtonClearSearch.setPreferredSize(new Dimension(16, 20));
	    jButtonClearSearch.setToolTipText("Clear search");
	    jButtonClearSearch.setIcon(
		    new ImageIcon(this.getClass().getResource(GraphGlobals.getPathImages() + "ClearSearch.png")));
	    jButtonClearSearch.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    NetworkComponentTablePanel.this.getJTextFieldSearch().setText(null);
		    NetworkComponentTablePanel.this.applyTableSorter();
		}
	    });
	}
	return jButtonClearSearch;
    }

    /**
     * This method initializes scpComponentTable
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPaneTable() {
	if (jScrollPaneTable == null) {
	    jScrollPaneTable = new JScrollPane();
	    jScrollPaneTable.setViewportView(this.getJTableComponents());
	}
	return jScrollPaneTable;
    }

    /**
     * This method initializes network components table tblComponents and the
     * TabelModel
     * 
     * @return javax.swing.JTable
     */
    private JTable getJTableComponents() {
	if (jTableComponents == null) {
	    jTableComponents = new JTable(this.getDefaultTableModel4Components());
	    jTableComponents.setFillsViewportHeight(true);
	    jTableComponents.setShowGrid(false);
	    if (this.enableMultipleSelection) {
		jTableComponents.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    } else {
		jTableComponents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    }
	    jTableComponents.getTableHeader().setReorderingAllowed(false);

	    // --- Define the column widths -------------------------
	    TableColumnModel colModel = jTableComponents.getColumnModel();
	    colModel.getColumn(0).setPreferredWidth(40);
	    colModel.getColumn(0).setCellRenderer(new NetworkComponentTablePanelEditID());
	    colModel.getColumn(0).setCellEditor(new NetworkComponentTablePanelEditID());

	    colModel.getColumn(1).setPreferredWidth(40);
	    colModel.getColumn(1).setCellRenderer(new NetworkComponentTablePanelEditID());

	    // --- Check for NetworkComponentTableService -----------
	    for (int i = 0; i < this.getNetworkComponentTableServiceList().size(); i++) {

		NetworkComponentTableService tableService = this.getNetworkComponentTableServiceList().get(i);

		Integer tableServiceWidth = tableService.getWidth();
		Integer tableServiceMinWidth = tableService.getMinWidth();
		Integer tableServiceMaxWidth = tableService.getMaxWidth();
		TableCellRenderer tableServiceRenderer = tableService.getTableCellRenderer();
		// TableCellEditor tableServiceEditor = tableService.getTableCellEditor();

		int colIndex = colModel.getColumnCount() - 1;
		if (tableServiceWidth != null)
		    colModel.getColumn(colIndex).setWidth(tableServiceWidth);
		if (tableServiceMinWidth != null)
		    colModel.getColumn(colIndex).setMinWidth(tableServiceMinWidth);
		if (tableServiceMaxWidth != null)
		    colModel.getColumn(colIndex).setMaxWidth(tableServiceMaxWidth);
		if (tableServiceRenderer != null)
		    colModel.getColumn(colIndex).setCellRenderer(tableServiceRenderer);
		// if (tableServiceEditor!=null)
		// colModel.getColumn(colIndex).setCellEditor(tableServiceEditor);
	    }

	    // --- No third column if editingEnabled==false ---------
	    if (this.editingEnabled == true) {
		int editColIndex = colModel.getColumnCount() - 1;
		int editColWidth = 38;
		colModel.getColumn(editColIndex).setWidth(editColWidth);
		colModel.getColumn(editColIndex).setMinWidth(editColWidth);
		colModel.getColumn(editColIndex).setMaxWidth(editColWidth);
		colModel.getColumn(editColIndex)
			.setCellRenderer(new NetworkComponentTablePanelEditButton(this.getGraphController()));
		colModel.getColumn(editColIndex)
			.setCellEditor(new NetworkComponentTablePanelEditButton(this.getGraphController()));
	    }

	    // --- Set the table sorter -----------------------
	    TableRowSorter<DefaultTableModel> tblSorter = this.getJTableRowSorter();
	    jTableComponents.setRowSorter(tblSorter);

	    // --- Define the first sort order ----------------
	    List<SortKey> sortKeys = new ArrayList<SortKey>();
	    for (int i = 0; i < jTableComponents.getColumnCount(); i++) {
		sortKeys.add(new SortKey(i, SortOrder.ASCENDING));
	    }
	    tblSorter.setSortKeys(sortKeys);

	    jTableComponents.getModel().addTableModelListener(this);
	    jTableComponents.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

	}
	return jTableComponents;
    }

    /**
     * Returns the DefaultTableModel for the network components.
     * 
     * @return the DefaultTableModel for the network components
     */
    private DefaultTableModel getDefaultTableModel4Components() {
	if (componentsTableModel == null) {

	    Vector<String> titles = new Vector<String>();
	    titles.add("ID");

	    // --- Check for NetworkComponentTableService -----------
	    for (int i = 0; i < this.getNetworkComponentTableServiceList().size(); i++) {
		NetworkComponentTableService tableService = this.getNetworkComponentTableServiceList().get(i);
		titles.add(tableService.getColumnHeader());
	    }

	    // --- Add type and edit column -------------------------
	    titles.add(Language.translate("Typ"));
	    if (this.editingEnabled == true) {
		titles.add(Language.translate("Edit", Language.EN));
	    }

	    // --- Set DataVector -----------------------------------
	    componentsTableModel = new DefaultTableModel(null, titles) {
		private static final long serialVersionUID = 1636744550817904118L;

		@Override
		public boolean isCellEditable(int row, int col) {
		    if (NetworkComponentTablePanel.this.editingEnabled == true) {
			// --- Decide when cell editing is allowed --
			int colCount = NetworkComponentTablePanel.this.getDefaultTableModel4Components()
				.getColumnCount();
			if (NetworkComponentTablePanel.this.getGraphController().getProject() != null) {
			    // --- During Simulation Setup: ---------
			    // --- => Last and first allowed --------
			    if (col == 0 || col == colCount - 1)
				return true;

			} else {
			    // --- During agent execution: ----------
			    // --- => Last allowed only -------------
			    if (col == colCount - 1)
				return true;

			}
		    }
		    return false;
		}
	    };

	}
	return componentsTableModel;
    }

    /**
     * Returns the table row sorter.
     * 
     * @return the table row sorter
     */
    private TableRowSorter<DefaultTableModel> getJTableRowSorter() {
	if (jTableRowSorter == null) {
	    // --- Set Sorter for the table -------------------
	    jTableRowSorter = new TableRowSorter<DefaultTableModel>(this.getDefaultTableModel4Components()) {
		@Override
		public void sort() {
		    // --- may throw a "java.lang.IllegalArgumentException: Comparison method
		    // violates its general contract!" ---
		    try {
			NetworkComponentTablePanel.this.updateComponentSorting();
			super.sort();

		    } catch (IllegalArgumentException iaEx) {
			iaEx.printStackTrace();
		    }
		}
	    };

	    // --- Define a comparator for the first row ------
	    jTableRowSorter.setComparator(0, new Comparator<String>() {
		@Override
		public int compare(String netCompId1, String netCompId2) {
		    if (NetworkComponentTablePanel.this.componentSorting == ComponentSorting.Alphanumeric) {
			Long ncID1 = this.parseLong(netCompId1);
			Long ncID2 = this.parseLong(netCompId2);
			if (ncID1 != null && ncID2 != null) {
			    return ncID1.compareTo(ncID2);
			} else if (ncID1 == null & ncID2 != null) {
			    return -1;
			} else if (ncID1 != null & ncID2 == null) {
			    return 1;
			}
		    }
		    return netCompId1.compareToIgnoreCase(netCompId2);
		}

		private Long parseLong(String netCompID) {
		    Long longValue = null;
		    String ncIdNumberString = netCompID.replaceAll("\\D+", "");
		    if (ncIdNumberString.length() > 0) {
			try {
			    longValue = Long.parseLong(ncIdNumberString);
			} catch (NumberFormatException nfEx) {
			    // nfEx.printStackTrace();
			}
		    }
		    return longValue;
		}
	    });
	    jTableRowSorter.setRowFilter(this.getJTableRowFilter());
	}
	return jTableRowSorter;
    }

    /**
     * Return the row filter.
     * 
     * @return the row filter
     */
    private RowFilter<DefaultTableModel, Integer> getJTableRowFilter() {
	if (jTableRowFilter == null) {
	    jTableRowFilter = new RowFilter<DefaultTableModel, Integer>() {
		@Override
		public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {

		    if (getExcludeList().contains(entry.getStringValue(0))) {
			// --- Check exclude list -----------------------------
			return false;
		    } else {
			// --- Return true if the search phrase is empty ------
			String searchPhrase = NetworkComponentTablePanel.this.getJTextFieldSearch().getText().trim();
			if (searchPhrase == null || searchPhrase.length() == 0)
			    return true;

			// --- Check for the search phrase --------------------
			int searchTo = entry.getValueCount();
			if (NetworkComponentTablePanel.this.editingEnabled == true)
			    searchTo--;

			for (int i = 0; i < searchTo; i++) {
			    String value = entry.getStringValue(i);
			    boolean matchsSearchPhrase = value.matches("(?i).*(" + searchPhrase + ").*");
			    if (matchsSearchPhrase == true)
				return true;
			}
			return false;
		    }

		}
	    };
	}
	return jTableRowFilter;
    }

    /**
     * Gets the exclude list.
     * 
     * @return the exclude list
     */
    public Vector<String> getExcludeList() {
	if (excludeList == null) {
	    excludeList = new Vector<>();
	}
	return excludeList;
    }

    /**
     * Sets the exclude list.
     * 
     * @param excludeList the new exclude list
     */
    public void setExcludeList(Vector<String> excludeList) {
	this.excludeList = excludeList;
	this.applyTableSorter();
    }

    /**
     * Returns the network component list that will be used for displaying network
     * components.
     * 
     * @return the network component list
     */
    public List<NetworkComponent> getNetworkComponentList() {
	if (this.netCompsToDisplay != null && this.netCompsToDisplayWereSetExternally == true) {
	    return this.netCompsToDisplay;
	}
	return new ArrayList<>(this.getGraphController().getNetworkModel().getNetworkComponents().values());
    }

    /**
     * Sets the network component list to be used for displaying
     * {@link NetworkComponent}s.
     * 
     * @param netCompsToDisplay the new network component list
     */
    public void setNetworkComponentList(List<NetworkComponent> netCompsToDisplay) {
	this.netCompsToDisplay = netCompsToDisplay;
	this.netCompsToDisplayWereSetExternally = true;
	this.reLoadNetworkComponents();
    }

    /**
     * This method builds the tblComponents' contents based on the controllers
     * GridModel
     * 
     * @return The grid components' IDs and class names
     */
    private void fillTableModel() {
	if (this.getGraphController().getNetworkModel() != null
		&& this.getGraphController().getNetworkModelUndoManager() != null) {
	    // --- Clear ------------------------
	    this.clearTableModel();
	    // --- Fill -------------------------
	    this.addNetworkComponents(this.getNetworkComponentList());
	}
    }

    /**
     * Clears the table model.
     */
    private void clearTableModel() {
	setTabelModelListenerPaused(true);
	this.getDefaultTableModel4Components().getDataVector().removeAllElements();
	this.getDefaultTableModel4Components().fireTableDataChanged();
	setTabelModelListenerPaused(false);
    }

    /**
     * Adds a new network component to the list of components in the table.
     * 
     * @param networkComponent the network component
     */
    protected void addNetworkComponent(NetworkComponent networkComponent) {
	this.addNetworkComponent(networkComponent, true);
    }

    /**
     * Adds a new network component to the list of components in the table.
     * 
     * @param networkComponent         the network component
     * @param updateNumberOfComponents if true, the number of components will be
     *                                 updated
     */
    private void addNetworkComponent(NetworkComponent networkComponent, boolean updateNumberOfComponents) {

	Vector<String> compData = new Vector<String>();
	compData.add(networkComponent.getId());

	// --- Check for NetworkComponentTableService -----------
	for (int i = 0; i < this.getNetworkComponentTableServiceList().size(); i++) {
	    compData.add(this.getNetworkComponentTableServiceList().get(i)
		    .getCellValue(this.graphController.getNetworkModel(), networkComponent));
	}

	compData.add(networkComponent.getType());
	if (this.editingEnabled) {
	    compData.add("Edit"); // For the edit properties button
	}
	this.getDefaultTableModel4Components().addRow(compData);
	if (updateNumberOfComponents == true) {
	    this.setNumberOfComponents();
	}
    }

    /**
     * Adds several network components to the list of components in the table.
     * 
     * @param netCompList the network component list
     */
    protected void addNetworkComponents(List<NetworkComponent> netCompList) {
	this.setTabelModelListenerPaused(true);
	for (int i = 0; i < netCompList.size(); i++) {
	    this.addNetworkComponent(netCompList.get(i), false);
	}
	this.setTabelModelListenerPaused(false);
	this.setNumberOfComponents();
    }

    /**
     * Removes a network component from the list of components in the table.
     * 
     * @param networkComponent the network component
     */
    protected void removeNetworkComponent(NetworkComponent networkComponent) {
	this.removeNetworkComponent(networkComponent, true);
    }

    /**
     * Removes a network component from the list of components in the table.
     *
     * @param networkComponent         the network component
     * @param updateNumberOfComponents the update number of components
     */
    private void removeNetworkComponent(NetworkComponent networkComponent, boolean updateNumberOfComponents) {
	int rowCount = this.getDefaultTableModel4Components().getRowCount();
	for (int row = 0; row < rowCount; row++) {
	    String entry = (String) this.getDefaultTableModel4Components().getValueAt(row, 0);
	    if (entry.equals(networkComponent.getId())) {
		this.getDefaultTableModel4Components().removeRow(row);
		if (updateNumberOfComponents == true) {
		    this.setNumberOfComponents();
		}
		return;
	    }
	}
    }

    /**
     * Removes a set of NetworkComponent's from the list of components in the table.
     * 
     * @param netCompList the list of NetworkComponent's
     */
    protected void removeNetworkComponents(List<NetworkComponent> netCompList) {

	// --- Get a list of all NetworkComponent Id's ----
	HashSet<String> networkComponentIDs = new HashSet<String>();
	for (int i = 0; i < netCompList.size(); i++) {
	    networkComponentIDs.add(netCompList.get(i).getId());
	}

	for (int row = 0; row < this.getDefaultTableModel4Components().getRowCount(); row++) {
	    String entry = (String) this.getDefaultTableModel4Components().getValueAt(row, 0);
	    if (networkComponentIDs.contains(entry)) {
		this.getDefaultTableModel4Components().removeRow(row);
		row--;
	    }
	}
	this.setNumberOfComponents();
    }

    /**
     * Applies the table sorter again.
     */
    public void applyTableSorter() {
	try {
	    this.getJTableRowSorter().sort();

	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	this.setNumberOfComponents();

    }

    /**
     * Updates the setting for the component sorting.
     */
    private void updateComponentSorting() {
	if (this.getGraphController() != null && this.getGraphController().getGeneralGraphSettings4MAS() != null
		&& this.getGraphController().getGeneralGraphSettings4MAS().getComponentSorting() != null) {
	    this.componentSorting = this.getGraphController().getGeneralGraphSettings4MAS().getComponentSorting();
	}
    }

    /**
     * Reloads the network model.
     */
    public void reLoadNetworkComponents() {

	if (SwingUtilities.isEventDispatchThread() == true) {
	    this.reloadNetworkComponentTable();
	} else {
	    SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
		    NetworkComponentTablePanel.this.reloadNetworkComponentTable();
		}
	    });
	}
    }

    /**
     * Reload network component table.
     */
    private void reloadNetworkComponentTable() {
	// --- (Re)fill the table model -----------
	NetworkComponentTablePanel.this.fillTableModel();
	// --- Clear search field -----------------
	NetworkComponentTablePanel.this.getJTextFieldSearch().setText(null);
	NetworkComponentTablePanel.this.applyTableSorter();
    }

    /**
     * Set / show number of components.
     */
    private void setNumberOfComponents() {
	// --- Set the number of rows displayed -----------
	String text = this.jLabelTable.getText();
	if (text.indexOf("(") > -1) {
	    text = text.substring(0, text.indexOf("(")).trim();
	}
	text = text + " (" + jTableComponents.getRowCount() + ")";
	this.jLabelTable.setText(text);
    }

    /**
     * Adds a table model listener.
     * 
     * @param tableModelListener the table model listener
     */
    public void addTableModelListener(TableModelListener tableModelListener) {
	this.getJTableComponents().getModel().addTableModelListener(tableModelListener);
    }

    /**
     * Removes a table model listener.
     * 
     * @param tableModelListener the table model listener
     */
    public void removeTableModelListener(TableModelListener tableModelListener) {
	this.getJTableComponents().getModel().removeTableModelListener(tableModelListener);
    }

    /**
     * Adds a list selection listener.
     * 
     * @param listSelectionListener the list selection listener
     */
    public void addListSelectionListener(ListSelectionListener listSelectionListener) {
	this.getJTableComponents().getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    /**
     * Removes a list selection listener.
     * 
     * @param listSelectionListener the list selection listener
     */
    public void removeListSelectionListener(ListSelectionListener listSelectionListener) {
	this.getJTableComponents().getSelectionModel().removeListSelectionListener(listSelectionListener);
    }

    /**
     * Adds a mouse listener.
     * 
     * @param mouseListener the mouse listener
     */
    @Override
    public void addMouseListener(MouseListener mouseListener) {
	this.getJTableComponents().addMouseListener(mouseListener);
    }

    /**
     * Removes a mouse listener.
     * 
     * @param mouseListener the mouse listener
     */
    @Override
    public void removeMouseListener(MouseListener mouseListener) {
	this.getJTableComponents().removeMouseListener(mouseListener);
    }

    /**
     * Gets the currently selected network component.
     * 
     * @return the currently selected network component
     */
    public NetworkComponent getSelectedNetworkComponent() {
	NetworkComponent selectedComponent = null;
	if (this.getJTableComponents().getSelectedRowCount() > 0) {
	    // Converting from view coordinates to model coordinates
	    int selectedIndex = getJTableComponents().convertRowIndexToModel(getJTableComponents().getSelectedRow());
	    String componentID = (String) jTableComponents.getModel().getValueAt(selectedIndex, 0);
	    selectedComponent = this.getGraphController().getNetworkModel().getNetworkComponent(componentID);
	}

	return selectedComponent;
    }

    /**
     * Gets the selected network components.
     * 
     * @return the selected network components
     */
    public Vector<NetworkComponent> getSelectedNetworkComponents() {
	Vector<NetworkComponent> selectedComponents = new Vector<>();
	if (this.getJTableComponents().getSelectedRowCount() > 0) {
	    int[] selectedRows = this.getJTableComponents().getSelectedRows();
	    for (int i = 0; i < selectedRows.length; i++) {
		int selectedIndex = getJTableComponents().convertRowIndexToModel(selectedRows[i]);
		String componentID = (String) jTableComponents.getModel().getValueAt(selectedIndex, 0);
		selectedComponents.add(this.getGraphController().getNetworkModel().getNetworkComponent(componentID));
	    }
	}
	return selectedComponents;
    }

    /**
     * Sets the currently selected network component.
     * 
     * @param networkComponent the network component to be selected
     */
    public void setSelectedNetworkComponent(NetworkComponent networkComponent) {
	if (networkComponent != null && networkComponent.getId() != null) {
	    this.currNetworkComponent = networkComponent;
	    int rowCount = this.getDefaultTableModel4Components().getRowCount();
	    int row = -1;
	    // Searching all the rows in the table
	    for (row = 0; row < rowCount; row++) {
		String compId = (String) getJTableComponents().getModel().getValueAt(row, 0);
		if (compId != null) {
		    // Checking for the matching component Id
		    if (compId.equals(networkComponent.getId())) {
			// Converting from model coordinates to view coordinates
			int viewRowIndex = getJTableComponents().convertRowIndexToView(row);
			int viewColumnIndex = getJTableComponents().convertColumnIndexToView(0);
			boolean toggle = false;
			boolean extend = false;
			// Selecting the cell in the table
			this.getJTableComponents().changeSelection(viewRowIndex, viewColumnIndex, toggle, extend);
			break;
		    }
		}
	    }
	}
    }

    /**
     * Clear the selection.
     */
    public void clearSelection() {
	this.getJTableComponents().clearSelection();
    }

    /**
     * Renames a network component in the table.
     * 
     * @param oldID the old ID
     * @param newID the new ID
     */
    protected void renameNetworkComponent(String oldID, String newID) {
	int rowCount = this.getDefaultTableModel4Components().getRowCount();
	int row = -1;
	// --- Searching all the rows in the table ----------------------------
	for (row = 0; row < rowCount; row++) {
	    String compId = (String) getJTableComponents().getModel().getValueAt(row, 0);
	    if (compId != null) {
		// --- Checking for the matching to the old ID ----------------
		if (compId.equals(oldID)) {
		    // --- Set the new ID in the table model ------------------
		    this.setTabelModelListenerPaused(true);
		    this.getJTableComponents().getModel().setValueAt(newID, row, 0);
		    this.setTabelModelListenerPaused(false);
		    // --- Set focus on component -----------------------------
		    int viewRowIndex = getJTableComponents().convertRowIndexToView(row);
		    int viewColumnIndex = getJTableComponents().convertColumnIndexToView(0);
		    boolean toggle = false;
		    boolean extend = false;
		    // --- Select cell in the table ---------------------------
		    this.getJTableComponents().changeSelection(viewRowIndex, viewColumnIndex, toggle, extend);
		    break;
		}
	    }
	}
    }

    /**
     * Sets the renaming enabled.
     * 
     * @param renamingEnabled the new renaming enabled
     */
    protected void setRenamingEnabled(boolean renamingEnabled) {
	this.getJTableComponents().setEnabled(renamingEnabled);
    }

    /**
     * Checks if the tabel model listener is paused.
     * 
     * @return true, if the tabel model listener is paused
     */
    protected boolean isTabelModelListenerPaused() {
	return tabelModelListenerPaused;
    }

    /**
     * Sets the tabel model listener paused.
     * 
     * @param tabelModelListenerPaused if true, the table model listener will be
     *                                 paused
     */
    protected void setTabelModelListenerPaused(boolean tabelModelListenerPaused) {
	this.tabelModelListenerPaused = tabelModelListenerPaused;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.
     * TableModelEvent)
     */
    @Override
    public void tableChanged(TableModelEvent tme) {

	if (this.isTabelModelListenerPaused() == false) {

	    int row = tme.getFirstRow();
	    int column = tme.getColumn();

	    DefaultTableModel model = (DefaultTableModel) tme.getSource();
	    if (column == 0 && row >= 0 && row < model.getRowCount()) {

		String oldCompID = this.currNetworkComponent.getId();
		String newCompID = (String) model.getValueAt(row, column);
		if (oldCompID.equals(newCompID) == false) {

		    String message = null;
		    String title = "Warning";

		    if (newCompID == null || newCompID.length() == 0) {
			// --- Check if the component id is empty
			message = "Enter a valid name";
			JOptionPane.showMessageDialog(this, Language.translate(message, Language.EN),
				Language.translate(title, Language.EN), JOptionPane.WARNING_MESSAGE);
			getJTableComponents().getModel().setValueAt(oldCompID, row, column);

		    } else if (newCompID.contains(" ")) {
			// --- Check for spaces
			message = "Enter the name without spaces";
			JOptionPane.showMessageDialog(this, Language.translate(message, Language.EN),
				Language.translate(title, Language.EN), JOptionPane.WARNING_MESSAGE);
			getJTableComponents().getModel().setValueAt(oldCompID, row, column);

		    } else if (getGraphController().getNetworkModel().getNetworkComponent(newCompID) != null) {
			// --- Check if a network component name already exists
			message = "The component name already exists!" + newLine + "Choose a different one.";
			JOptionPane.showMessageDialog(this, Language.translate(message, Language.EN),
				Language.translate(title, Language.EN), JOptionPane.WARNING_MESSAGE);
			getJTableComponents().getModel().setValueAt(oldCompID, row, column);

		    } else if (this.getGraphController().getProject().getSimulationSetups().getCurrSimSetup()
			    .isAgentNameExists(newCompID)) {
			// --- Check if the agent name already exists in the simulation setup
			message = "An agent with the name already exists in the simulation setup!" + newLine
				+ " Choose a different one.";
			JOptionPane.showMessageDialog(this, Language.translate(message, Language.EN),
				Language.translate(title, Language.EN), JOptionPane.WARNING_MESSAGE);
			getJTableComponents().getModel().setValueAt(oldCompID, row, column);

		    } else {
			// --- All validations done, rename the component and update the network model
			// --- renaming NetworkComponents and GraphElements
			this.getGraphController().getNetworkModelUndoManager().renameNetworkComponent(oldCompID,
				newCompID);
		    }
		}
	    }
	}
    }

}
