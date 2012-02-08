/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */

package agentgui.envModel.graph.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import agentgui.core.agents.AgentClassElement4SimStart;
import agentgui.core.application.Language;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.environment.EnvironmentPanel;
import agentgui.core.project.Project;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.components.TableCellEditor4TableButton;
import agentgui.envModel.graph.components.TableCellRenderer4Button;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The GUI for a GraphEnvironmentController. This contains a pane showing the NetworkComponents table and the BasicGraphGUI. The main class which associates with the components table, the environment
 * model and the Basic Graph GUI.
 * 
 * @see GraphEnvironmentController
 * @see BasicGraphGui
 * @see agentgui.envModel.graph.networkModel
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology - Guwahati
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class GraphEnvironmentControllerGUI extends EnvironmentPanel implements ListSelectionListener, TableModelListener {

    private static final long serialVersionUID = 7376906096627051173L;

    private final static String pathImage = GraphGlobals.getPathImages();

    /** The SplitPane containing this GUI's components */
    private JComponent mainDisplayComponent = null;
    private boolean useTabs = false;
    private JSplitPane jSplitPaneRoot = null;
    private JTabbedPane jTabbedPaneAltNetModels = null;
    private HashMap<String, GraphEnvironmentControllerGUI> networkModelTabs = null; // @jve:decl-index=0:

    private JPanel jPanelControls = null;
    private JScrollPane jScrollPaneComponentsTable = null;
    private JLabel jLabelTable = null;
    private JTextField jTextFieldSearch = null;
    private JButton jButtonClearSearch = null;

    private JTable jTableComponents = null;

    /** The graph visualization component */
    private BasicGraphGui graphGUI = null;

    /**
     * This is the default constructor for just displaying the current environment model during a running simulation
     */
    public GraphEnvironmentControllerGUI(EnvironmentController envController) {
		super(envController);
		this.initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setLayout(new BorderLayout());

	if (this.getGraphController().getProject() != null) {
	    this.mainDisplayComponent = getJSplitPaneRoot();
	    this.useTabs = false;
	} else {
	    this.mainDisplayComponent = getJTabbedPaneAltNetModels();
	    this.useTabs = true;
	}
	this.add(this.mainDisplayComponent, null);
	this.showNumberOfComponents();
    }

    /**
     * Returns the graph environment controller.
     * 
     * @return the graph environment controller
     */
    public GraphEnvironmentController getGraphController() {
	return (GraphEnvironmentController) this.environmentController;
    }

    /**
     * Gets the current project.
     * 
     * @return the current project
     */
    public Project getCurrentProject() {
	return this.getGraphController().getProject();
    }

    /**
     * Gets the JTabbedPane for alternative network models.
     * 
     * @return the JTabbedPane for alternative network models
     */
    private JTabbedPane getJTabbedPaneAltNetModels() {
	if (jTabbedPaneAltNetModels == null) {
	    jTabbedPaneAltNetModels = new JTabbedPane();
	    jTabbedPaneAltNetModels.setFont(new Font("Dialog", Font.BOLD, 12));

	    // --- Initialize the Tab-Reminder ------------
	    this.networkModelTabs = new HashMap<String, GraphEnvironmentControllerGUI>();

	    // --- Display the normal topology ------------
	    jTabbedPaneAltNetModels.addTab(Language.translate("Topologie"), getJSplitPaneRoot());
	    this.networkModelTabs.put(Language.translate("Topologie"), this);

	}
	return jTabbedPaneAltNetModels;
    }

    /**
     * Sets to use tabs or not.
     * 
     * @param use the new use
     */
    public void setUseTabs(boolean use) {

	if (this.useTabs != use) {
	    // --- Change the appearance of this tab ----------------
	    this.useTabs = use;
	    this.remove(this.mainDisplayComponent);

	    if (this.useTabs == true) {
		// --- NO tabs were displayed yet => show them now --
		this.mainDisplayComponent = this.getJTabbedPaneAltNetModels();
	    } else {
		// --- Tabs are displayed => remove them now --------
		this.mainDisplayComponent = getJSplitPaneRoot();
	    }

	    // --- Finally ------------------------------------------
	    this.add(this.mainDisplayComponent, null);
	    this.validate();
	    this.repaint();
	}
    }

    /**
     * This method initializes jSplitPaneRoot
     * 
     * @return javax.swing.JSplitPane
     */
    private JSplitPane getJSplitPaneRoot() {
	if (jSplitPaneRoot == null) {
	    jSplitPaneRoot = new JSplitPane();
	    jSplitPaneRoot.setOneTouchExpandable(true);
	    jSplitPaneRoot.setLeftComponent(getPnlControlls());
	    jSplitPaneRoot.setRightComponent(getGraphVisualization());
	    jSplitPaneRoot.setDividerLocation(230);
	}
	return jSplitPaneRoot;
    }

    /**
     * This method initializes pnlControlls
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPnlControlls() {
		if (jPanelControls == null) {
		    GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		    gridBagConstraints1.gridx = 1;
		    gridBagConstraints1.insets = new Insets(0, 0, 0, 5);
		    gridBagConstraints1.gridy = 1;
		    GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		    gridBagConstraints11.fill = GridBagConstraints.BOTH;
		    gridBagConstraints11.gridy = 1;
		    gridBagConstraints11.weightx = 0.5;
		    gridBagConstraints11.gridwidth = 1;
		    gridBagConstraints11.insets = new Insets(0, 10, 0, 1);
		    gridBagConstraints11.gridx = 0;
		    GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		    gridBagConstraints6.gridx = 0;
		    gridBagConstraints6.anchor = GridBagConstraints.WEST;
		    gridBagConstraints6.insets = new Insets(0, 15, 0, 5);
		    gridBagConstraints6.gridwidth = 2;
		    gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
		    gridBagConstraints6.gridy = 0;
		    jLabelTable = new JLabel();
		    jLabelTable.setText("Search Components");
		    jLabelTable.setFont(new Font("Dialog", Font.BOLD, 12));
		    jLabelTable.setText(Language.translate(jLabelTable.getText(), Language.EN));
		    GridBagConstraints gridBagConstraints = new GridBagConstraints();
		    gridBagConstraints.fill = GridBagConstraints.BOTH;
		    gridBagConstraints.gridy = 2;
		    gridBagConstraints.weightx = 1.0;
		    gridBagConstraints.weighty = 1.0;
		    gridBagConstraints.gridheight = 1;
		    gridBagConstraints.gridwidth = 0;
		    gridBagConstraints.insets = new Insets(0, 10, 0, 5);
		    gridBagConstraints.gridx = 0;
		    jPanelControls = new JPanel();
		    jPanelControls.setLayout(new GridBagLayout());
		    jPanelControls.add(jLabelTable, gridBagConstraints6);
		    jPanelControls.add(getJTextFieldSearch(), gridBagConstraints11);
		    jPanelControls.add(getScpComponentTable(), gridBagConstraints);
		    jPanelControls.add(getJButtonClearSearch(), gridBagConstraints1);
		}
		return jPanelControls;
    }

    /**
     * This method initializes scpComponentTable
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getScpComponentTable() {
		if (jScrollPaneComponentsTable == null) {
		    jScrollPaneComponentsTable = new JScrollPane();
		    jScrollPaneComponentsTable.setViewportView(getJTableComponents());
		}
		return jScrollPaneComponentsTable;
    }

    /**
     * This method initializes network components table tblComponents and the TabelModel
     * 
     * @return javax.swing.JTable
     */
    private JTable getJTableComponents() {
		if (jTableComponents == null) {
		    // Column titles
		    Vector<String> titles = new Vector<String>();
		    titles.add(Language.translate("Komponente"));
		    titles.add(Language.translate("Typ"));
		    titles.add(Language.translate("Options", Language.EN));
	
		    final Vector<Vector<String>> data = getComponentTableContents();
		    DefaultTableModel model = new DefaultTableModel(data, titles) {
				private static final long serialVersionUID = 1636744550817904118L;
				@Override
				public boolean isCellEditable(int row, int col) {
				    if (col != 1) {
				    	return true;
				    }
				    return false;
				}
		    };
	
		    jTableComponents = new JTable(model);
		    jTableComponents.setFillsViewportHeight(true);
		    jTableComponents.setCellSelectionEnabled(true);
		    jTableComponents.setShowGrid(false);
		    jTableComponents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		    
		    jTableComponents.getModel().addTableModelListener(this);
		    jTableComponents.getSelectionModel().addListSelectionListener(this);

		    TableRowSorter<DefaultTableModel> tblSorter = new TableRowSorter<DefaultTableModel>(model);
		    tblSorter.setComparator(0, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					Integer o1Int = extractNumericalValue(o1);
					Integer o2Int = extractNumericalValue(o2);
					if (o1Int!=null && o2Int!=null) {
						return o1Int.compareTo(o2Int);
					} else if (o1Int==null && o2Int!=null) {
						return -1;
					} else if (o1Int!=null && o2Int==null) {
						return 1;
					} else {
						return o1.compareTo(o2);	
					}
				}
			});
		    jTableComponents.setRowSorter(tblSorter);		    

		    // --- Define the first sort order ------------
			List<SortKey> sortKeys = new ArrayList<SortKey>();
			for (int i = 0; i < jTableComponents.getColumnCount(); i++) {
			    sortKeys.add(new SortKey(i, SortOrder.ASCENDING));
			}
			jTableComponents.getRowSorter().setSortKeys(sortKeys);
			
			
			TableColumnModel colModel = jTableComponents.getColumnModel();
			colModel.getColumn(0).setPreferredWidth(20);
			colModel.getColumn(2).setPreferredWidth(30);
			colModel.getColumn(2).setCellEditor(new TableCellEditor4TableButton(getGraphController(), jTableComponents));			
			colModel.getColumn(2).setCellRenderer(new TableCellRenderer4Button());

		}
		return jTableComponents;
    }

    /**
     * Extract the numerical value from a String.
     * @param expression the expression
     * @return the integer value
     */
    private Integer extractNumericalValue(String expression) {
    	String  numericString = "";
    	Integer numeric = null;
    	for (int i = 0; i < expression.length(); i++) {
    		String letter = Character.toString(expression.charAt(i));
    		if (letter.matches("[0-9]")) {
    			numericString += letter;	
    		}
		}
    	if (numericString.equals("")==false) {
    		numeric = Integer.parseInt(numericString);
    	}
    	return numeric;
    }
    /**
     * Row filter for updating table view based on the expression in the text box Used for searching components
     */
    @SuppressWarnings("unchecked")
	public void tblFilter() {
		
    	RowFilter<DefaultTableModel, Object> rf = null;
		// If current expression doesn't parse, don't update.
		try {
		    rf = RowFilter.regexFilter("(?i).*(" + getJTextFieldSearch().getText() + ").*", 0, 1);
		} catch (java.util.regex.PatternSyntaxException e) {
		    return;
		}
		((TableRowSorter<DefaultTableModel>)this.getJTableComponents().getRowSorter()).setRowFilter(rf);
		this.showNumberOfComponents();

    }

    /**
     * Show number of components.
     */
    public void showNumberOfComponents() {
	// --- Set the number of rows displayed -----------
	String text = this.jLabelTable.getText();
	if (text.indexOf("(") > -1) {
	    text = text.substring(0, text.indexOf("(")).trim();
	}
	text = text + " (" + jTableComponents.getRowCount() + ")";
	this.jLabelTable.setText(text);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event. TableModelEvent)
     */
    /**
     * Invoked when a table data model changes. Used for renaming the network component.
     */
    @Override
    public void tableChanged(TableModelEvent e) {

	int row = e.getFirstRow();
	int column = e.getColumn(); // In model coordinates

	DefaultTableModel model = (DefaultTableModel) e.getSource();
	// If the component name is changed
	if (column == 0 && row >= 0 && row < model.getRowCount()) {
	    String newCompID = (String) model.getValueAt(row, column);
	    // Getting the corresponding comp from the network model
	    NetworkComponent comp = (NetworkComponent) getGraphController().getNetworkModel().getNetworkComponents().values().toArray()[row];
	    // The Old component ID before the change
	    String oldCompID = comp.getId();

	    // If new ID is not equal to old ID
	    if (!oldCompID.equals(newCompID)) {

		if (newCompID == null || newCompID.length() == 0) {
		    // --- Check if the component id is empty
		    JOptionPane.showMessageDialog(this, Language.translate("Enter a valid name", Language.EN), Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
		    getJTableComponents().getModel().setValueAt(oldCompID, row, column);

		} else if (newCompID.contains(" ")) {
		    // --- Check for spaces
		    JOptionPane.showMessageDialog(this, Language.translate("Enter the name without spaces", Language.EN), Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
		    getJTableComponents().getModel().setValueAt(oldCompID, row, column);

		} else if (getGraphController().getNetworkModel().getNetworkComponent(newCompID) != null) {
		    // --- Check if a network component name already exists
		    JOptionPane.showMessageDialog(this, Language.translate("The component name already exists!\n Choose a different one.", Language.EN), Language.translate("Warning", Language.EN),
			    JOptionPane.WARNING_MESSAGE);
		    getJTableComponents().getModel().setValueAt(oldCompID, row, column);

		} else if (getCurrentProject().simulationSetups.getCurrSimSetup().isAgentNameExists(newCompID)) {
		    // --- Check if the agent name already exists in the
		    // simulation setup
		    JOptionPane.showMessageDialog(this, Language.translate("An agent with the name already exists in the simulation setup!\n Choose a different one.", Language.EN),
			    Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
		    getJTableComponents().getModel().setValueAt(oldCompID, row, column);

		} else {
		    // --- All validations done, rename the component and update
		    // the network model
		    handleRenameComponent(oldCompID, newCompID);
		}
	    }
	}
	// System.out.println(row+","+column);
    }

    /**
     * Changes the network component Id from old to new and updates the graph and the network model accordingly
     * 
     * @param oldCompID
     * @param newCompID
     */
    private void handleRenameComponent(String oldCompID, String newCompID) {
	// renaming NetworkComponents and GraphElements
	getGraphController().getNetworkModel().renameComponent(oldCompID, newCompID);
	getGraphController().refreshNetworkModel();

	// Renaming the agent in the agent start list of the simulation setup
	int i = 0;
	for (i = 0; i < this.getGraphController().getAgents2Start().size(); i++) {
	    AgentClassElement4SimStart ac4s = (AgentClassElement4SimStart) this.getGraphController().getAgents2Start().get(i);
	    if (ac4s.getStartAsName().equals(oldCompID)) {
		ac4s.setStartAsName(newCompID);
		break;
	    }
	}
    }

    /**
     * This method builds the tblComponents' contents based on the controllers GridModel
     * 
     * @return The grid components' IDs and class names
     */
    private Vector<Vector<String>> getComponentTableContents() {

	Vector<Vector<String>> componentVector = new Vector<Vector<String>>();
	if (this.getGraphController().getNetworkModel() != null) {

	    // Get the components from the controllers GridModel
	    Iterator<NetworkComponent> components = this.getGraphController().getNetworkModel().getNetworkComponents().values().iterator();
	    // Add component ID and class name to the data vector
	    while (components.hasNext()) {
		NetworkComponent comp = components.next();

		Vector<String> compData = new Vector<String>();
		compData.add(comp.getId());
		compData.add(comp.getType());
		compData.add("Edit"); // For the edit properties button
		componentVector.add(compData);
	    }
	}
	return componentVector;
    }

    /**
     * Gets the graph visualization either for configuration or for displaying it during the simulation runtime.
     * 
     * @return the graph visualization
     */
    private JComponent getGraphVisualization() {
	return this.getGraphGUI();
    }

    /**
     * Get the visualization component
     * 
     * @return the basic graph GUI which contains the graph visualization component
     */
    public BasicGraphGui getGraphGUI() {
	if (graphGUI == null) {
	    graphGUI = new BasicGraphGui(this.getGraphController());
	    NetworkModel networkModel = this.getGraphController().getNetworkModel();
	    if (networkModel != null && networkModel.getGraph() != null) {
		graphGUI.setGraph(networkModel.getGraph());
	    }
	}
	return graphGUI;
    }

    /**
     * Sets the network model to the display.
     * 
     * @param nm the new NetworkModel
     */
    public void setNetworkModel(NetworkModel nm) {

	this.getGraphGUI().setGraph(nm.getGraph());
	this.showNumberOfComponents();

	if (nm.getAlternativeNetworkModel().size() > 0) {
	    // --------------------------------------------------------------------------
	    // --- Alternative NetworkModel's has to be displayed
	    // -----------------------
	    // --------------------------------------------------------------------------
	    HashMap<String, NetworkModel> altNetModelHash = nm.getAlternativeNetworkModel();
	    // --- Get the model names ordered
	    // ------------------------------------------
	    Vector<String> altNetModels = new Vector<String>(altNetModelHash.keySet());
	    Collections.sort(altNetModels);

	    for (int i = 0; i < altNetModels.size(); i++) {

		String altNetModelName = altNetModels.get(i);
		NetworkModel altNetModel = altNetModelHash.get(altNetModelName);

		GraphEnvironmentControllerGUI graphControllerGUI = null;
		GraphEnvironmentController graphController = null;

		if (networkModelTabs.get(altNetModelName) == null) {
		    // --- Create a new Controller for the alternative
		    // NetworkModel -----
		    graphController = new GraphEnvironmentController();
		    graphController.setEnvironmentModel(altNetModel);
		    graphControllerGUI = (GraphEnvironmentControllerGUI) graphController.getEnvironmentPanel();

		    this.jTabbedPaneAltNetModels.addTab(altNetModelName, graphControllerGUI);
		    this.networkModelTabs.put(altNetModelName, graphControllerGUI);

		} else {
		    // --- Get the Controller for the alternative NetworkModel
		    // ----------
		    graphControllerGUI = networkModelTabs.get(altNetModelName);
		    graphController = graphControllerGUI.getGraphController();
		    graphController.setEnvironmentModel(altNetModel);

		}

		// --- Set the appearance of the GUI to use or not use a
		// JTabbedPane ----
		if (altNetModel.getAlternativeNetworkModel().size() > 0) {
		    graphControllerGUI.setUseTabs(true);
		} else {
		    graphControllerGUI.setUseTabs(false);
		}

	    }
	    // --------------------------------------------------------------------------
	    // --------------------------------------------------------------------------
	}
    }

    /**
     * Refresh network model.
     */
    public void refreshNetworkModel() {
		// --- Network model is updated/refreshed -----
		this.getGraphGUI().repaintGraph(this.getGraphController().getNetworkModel().getGraph());
		// --- Rebuilding the component table ---------
		this.showNumberOfComponents();
		this.getGraphGUI().clearPickedObjects();
    }

    /**
     * Invoked when a row of the components table is selected. Highlights the selected network component in the Graph.
     * 
     * @param e List selection event
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
		if (getJTableComponents().getSelectedRowCount() > 0) {
		    // Converting from view coordinates to model coordinates
		    int selectedIndex = getJTableComponents().convertRowIndexToModel(getJTableComponents().getSelectedRow());
		    String componentID = (String) jTableComponents.getModel().getValueAt(selectedIndex, 0);
		    NetworkComponent component = this.getGraphController().getNetworkModel().getNetworkComponent(componentID);
	
		    this.getGraphGUI().clearPickedObjects();
		    this.getGraphGUI().selectObject(component);
		}
    }

    /**
     * Given a network component, selects the corresponding row in the components display table
     * 
     * @param netComp
     */
    public void selectComponentInTable(NetworkComponent netComp) {
	int rowCount = getJTableComponents().getModel().getRowCount();
	int row = -1;
	// Searching all the rows in the table
	for (row = 0; row < rowCount; row++) {
	    String compId = (String) getJTableComponents().getModel().getValueAt(row, 0);
	    // Checking for the matching component Id
	    if (compId.equals(netComp.getId())) {
		// Converting from model cooardinates to view coordinates
		int viewRowIndex = getJTableComponents().convertRowIndexToView(row);
		int viewColumnIndex = getJTableComponents().convertColumnIndexToView(0);
		boolean toggle = false;
		boolean extend = false;
		// Selecting the cell in the table
		getJTableComponents().changeSelection(viewRowIndex, viewColumnIndex, toggle, extend);
		break;
	    }
	}
    }

    /**
     * This method initializes jTextFieldSearch Search box - Used for filtering the components
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
		    // Calling the table row filter for searching the components
		    tblFilter();
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
	    jButtonClearSearch.setIcon(new ImageIcon(this.getClass().getResource(pathImage + "ClearSearch.png")));
	    jButtonClearSearch.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    getJTextFieldSearch().setText(null);
		    tblFilter();
		}
	    });
	}
	return jButtonClearSearch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
	// --- Nothing to do here yet -----------
    }

} // @jve:decl-index=0:visual-constraint="33,19"
