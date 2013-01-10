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
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.commands.RenameNetworkComponent.NetworkComponentRenamed;
import agentgui.envModel.graph.components.TableCellEditor4TableButton;
import agentgui.envModel.graph.components.TableCellRenderer4Button;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;

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
public class BasicGraphGuiRootJSplitPane extends JInternalFrame implements ListSelectionListener, TableModelListener, Observer {

    private static final long serialVersionUID = 7376906096627051173L;

    private final static String pathImage = GraphGlobals.getPathImages();
    private String newLine = Application.getGlobalInfo().getNewLineSeparator();  //  @jve:decl-index=0:
    
    private GraphEnvironmentController graphEnvironmentController = null;
    
    private JSplitPane jSplitPaneRoot = null;
    
    private JPanel jPanelControls = null;
    private JScrollPane jScrollPaneComponentsTable = null;
    private JLabel jLabelTable = null;
    private JTextField jTextFieldSearch = null;
    private JButton jButtonClearSearch = null;

    private JTable jTableComponents = null;
    private DefaultTableModel componentsTableModel = null;
    private boolean quiteTabelModelListener = false;
    
    /** The graph visualization component */
    private BasicGraphGui graphGUI = null;

    private NetworkComponent currNetworkComponent = null;
    
    /**
     * This is the default constructor for just displaying the current environment model during a running simulation
     */
    public BasicGraphGuiRootJSplitPane(GraphEnvironmentController envController) {
		this.graphEnvironmentController = envController;
		this.graphEnvironmentController.addObserver(this);
		this.initialize();
    }
    
    /**
     * This method initializes this
     * @return void
     */
    private void initialize() {
	
		this.setLayout(new BorderLayout());
		
		((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setSize(new Dimension(600, 450));
		
		this.setContentPane(this.getJSplitPaneRoot());
		this.setNumberOfComponents();
		
		this.setVisible(true);
		
    }
    /**
     * ReLoads the network model.
     */
    private void reLoad() {
    	
    	// --- Refresh the list of components -------------
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
		    	componentsTableModel = getDefaultTableModel4ComponentsNew();
		    	getJTableComponents().setModel(componentsTableModel);
		    	setLayout4JTableComponents();
				// --- Clear search field -----------------
		    	getJTextFieldSearch().setText(null);
				// --- Refresh number of components -------
		    	setNumberOfComponents();
			}
		});
    	
    }
    
    /**
     * Returns the graph environment controller.
     * @return the graph environment controller
     */
    private GraphEnvironmentController getGraphController() {
    	return this.graphEnvironmentController;
    }

	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentPanel#dispose()
	 */
	@Override
	public void dispose() {

		// --- Kill the current GraphGui --------------------------------------
		if (this.graphGUI!=null) {
			this.graphGUI.dispose();	
		}
		
	}
    
    /**
     * This method initializes jSplitPaneRoot
     * @return javax.swing.JSplitPane
     */
    public JSplitPane getJSplitPaneRoot() {
		if (jSplitPaneRoot == null) {
		    jSplitPaneRoot = new JSplitPane();
		    jSplitPaneRoot.setOneTouchExpandable(true);
		    jSplitPaneRoot.setLeftComponent(this.getJPanelControlls());
		    jSplitPaneRoot.setRightComponent(this.getBasicGraphGui());
		    jSplitPaneRoot.setDividerLocation(230);
		}
		return jSplitPaneRoot;
    }

    /**
     * This method initializes pnlControlls
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelControlls() {
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
		    GridBagConstraints gridBagConstraints = new GridBagConstraints();
		    gridBagConstraints.fill = GridBagConstraints.BOTH;
		    gridBagConstraints.gridy = 2;
		    gridBagConstraints.weightx = 1.0;
		    gridBagConstraints.weighty = 1.0;
		    gridBagConstraints.gridheight = 1;
		    gridBagConstraints.gridwidth = 0;
		    gridBagConstraints.insets = new Insets(0, 10, 0, 5);
		    gridBagConstraints.gridx = 0;

		    jLabelTable = new JLabel();
		    jLabelTable.setText("Search Components");
		    jLabelTable.setFont(new Font("Dialog", Font.BOLD, 12));
		    jLabelTable.setText(Language.translate(jLabelTable.getText(), Language.EN));

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
     * This method builds the tblComponents' contents based on the controllers GridModel
     * @return The grid components' IDs and class names
     */
    private Vector<Vector<String>> getComponentTableContents() {

		Vector<Vector<String>> componentVector = new Vector<Vector<String>>();
		if (this.getGraphController().getNetworkModelAdapter() != null) {
	
		    // Get the components from the controllers GridModel
		    Iterator<NetworkComponent> components = this.getGraphController().getNetworkModelAdapter().getNetworkComponents().values().iterator();
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
     * Returns the DefaultTableModel for the network components.
     * @return the DefaultTableModel for the network components
     */
    private DefaultTableModel getDefaultTableModel4Components() {
    	if (componentsTableModel==null) {
    		componentsTableModel = this.getDefaultTableModel4ComponentsNew();
    	}
    	return componentsTableModel;
    }
    
    /**
     * Returns a new DefaultTableModel for the NetworkComponents.
     * @return a new DefaultTableModel for the NetworkComponents
     */
    private DefaultTableModel getDefaultTableModel4ComponentsNew() {
    	
		// --- Set column titles --------------------------------
	    Vector<String> titles = new Vector<String>();
	    titles.add(Language.translate("Komponente"));
	    titles.add(Language.translate("Typ"));
	    titles.add(Language.translate("Options", Language.EN));

	    // --- Set DataVector -----------------------------------
	    final Vector<Vector<String>> data = getComponentTableContents();
	    DefaultTableModel tableModel = new DefaultTableModel(data, titles) {
			private static final long serialVersionUID = 1636744550817904118L;
			@Override
			public boolean isCellEditable(int row, int col) {
			    if (col == 1) {
			    	return false;
			    } else {
			    	if (getGraphController().getProject()!=null) {
			    		// --- During Simulation Setup ----------
			    		return true;	
			    	} else {
			    		// --- During simulation runtime --------
			    		if (col==0) {
			    			return false;
			    		} else {
			    			return true;
			    		}
			    	}
			    }
			}
	    };
    	return tableModel;
    }
    
    /**
     * Given a network component, selects the corresponding row in the components display table
     * @param networkComponent
     */
    private void networkComponentSelect(NetworkComponent networkComponent) {
		
    	if (networkComponent==null) return;
    	if (networkComponent.getId()==null) return;
    	
    	int rowCount = this.getDefaultTableModel4Components().getRowCount();
		int row = -1;
		// Searching all the rows in the table
		for (row = 0; row < rowCount; row++) {
		    String compId = (String) getJTableComponents().getModel().getValueAt(row, 0);
		    if (compId!=null) {
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
    
    /**
     * Adds a new network component to the list of components in the table.
     * @param networkComponent the network component
     */
    private void networkComponentAdd(NetworkComponent networkComponent) {
		Vector<String> compData = new Vector<String>();
		compData.add(networkComponent.getId());
		compData.add(networkComponent.getType());
		compData.add("Edit"); // For the edit properties button
		this.getDefaultTableModel4Components().addRow(compData);
		this.setNumberOfComponents();
		
    }
    /**
     * Removes a network component from the list of components in the table.
     * @param networkComponent the network component
     */
    private void networkComponentRemove(NetworkComponent networkComponent) {
    	int rowCount = this.getDefaultTableModel4Components().getRowCount();
    	for (int row = 0; row < rowCount; row++) {
    		String entry = (String) this.getDefaultTableModel4Components().getValueAt(row, 0);
    		if (entry.equals(networkComponent.getId())) {
    			this.getDefaultTableModel4Components().removeRow(row);
    			this.setNumberOfComponents();
    			return;
    		}
		}
    }
    
    /**
     * This method initializes network components table tblComponents and the TabelModel
     * @return javax.swing.JTable
     */
    private JTable getJTableComponents() {
		if (jTableComponents == null) {
	
		    jTableComponents = new JTable(this.getDefaultTableModel4Components());
		    jTableComponents.setFillsViewportHeight(true);
		    jTableComponents.setCellSelectionEnabled(true);
		    jTableComponents.setShowGrid(false);
		    jTableComponents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		    jTableComponents.getTableHeader().setReorderingAllowed(false);
		    
		    jTableComponents.getModel().addTableModelListener(this);
		    jTableComponents.getSelectionModel().addListSelectionListener(this);

		    this.setLayout4JTableComponents();
		    
		}
		return jTableComponents;
    }
    
    /**
     * Sets the layout for the JTable of the NetworkComponents.
     */
    private void setLayout4JTableComponents() {
    	
    	// --- Define Comparators for Component table -----
//    	Comparator<String> col1Comparator = new Comparator<String>() {
//			@Override
//			public int compare(String o1, String o2) {
//				Long o1Int = extractNumericalValue(o1);
//				Long o2Int = extractNumericalValue(o2);
//				if (o1Int!=null && o2Int!=null) {
//					return o1Int.compareTo(o2Int);
//				} else if (o1Int==null && o2Int!=null) {
//					return -1;
//				} else if (o1Int!=null && o2Int==null) {
//					return 1;
//				} else {
//					return o1.compareTo(o2);	
//				}
//			}
//		};
		
    	// --- Set Sorter for the table -------------------
		TableRowSorter<DefaultTableModel> tblSorter = new TableRowSorter<DefaultTableModel>(this.getDefaultTableModel4Components());
    	//tblSorter.setComparator(0, col1Comparator);
	    this.getJTableComponents().setRowSorter(tblSorter);		    

	    // --- Define the first sort order ----------------
		List<SortKey> sortKeys = new ArrayList<SortKey>();
		for (int i = 0; i < this.getJTableComponents().getColumnCount(); i++) {
		    sortKeys.add(new SortKey(i, SortOrder.ASCENDING));
		}
		this.getJTableComponents().getRowSorter().setSortKeys(sortKeys);
		
		// --- Define the column widths -------------------
		TableColumnModel colModel = this.getJTableComponents().getColumnModel();
		colModel.getColumn(0).setPreferredWidth(20);
		colModel.getColumn(2).setPreferredWidth(30);
		colModel.getColumn(2).setCellEditor(new TableCellEditor4TableButton(getGraphController(), jTableComponents));			
		colModel.getColumn(2).setCellRenderer(new TableCellRenderer4Button());
		
    }
    
    
    /**
     * Extract the numerical value from a String.
     * @param expression the expression
     * @return the integer value
     */
    @SuppressWarnings("unused")
	private Long extractNumericalValue(String expression) {
    	String  numericString = "";
    	Long 	numeric = null;
    	for (int i = 0; i < expression.length(); i++) {
    		String letter = Character.toString(expression.charAt(i));
    		if (letter.matches("[0-9]")) {
    			numericString += letter;	
    		}
		}
    	if (numericString.equals("")==false) {
    		try {
    			numeric = Long.parseLong(numericString);	
    			
			} catch (Exception ex) {
				numeric = new Long(-1);
			}
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
		this.setNumberOfComponents();

    }

    /**
     * Show number of components.
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

    /* (non-Javadoc)
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent le) {
		if (getJTableComponents().getSelectedRowCount() > 0) {
		    // Converting from view coordinates to model coordinates
		    int selectedIndex = getJTableComponents().convertRowIndexToModel(getJTableComponents().getSelectedRow());
		    String componentID = (String) jTableComponents.getModel().getValueAt(selectedIndex, 0);
		    this.currNetworkComponent = this.getGraphController().getNetworkModelAdapter().getNetworkComponent(componentID);
		    this.getGraphController().getNetworkModelAdapter().selectNetworkComponent(this.currNetworkComponent);	
		}
    }
    
    /*
     * (non-Javadoc)
     * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event. TableModelEvent)
     */
    @Override
    public void tableChanged(TableModelEvent tme) {

    	if (this.quiteTabelModelListener==true) return;
    	
		int row = tme.getFirstRow();
		int column = tme.getColumn();
	
		DefaultTableModel model = (DefaultTableModel) tme.getSource();
		if (column == 0 && row >= 0 && row < model.getRowCount()) {
	
		    String oldCompID = this.currNetworkComponent.getId();
			String newCompID = (String) model.getValueAt(row, column);
		    if (!oldCompID.equals(newCompID)) {
	
		    	String message = null;
		    	String title = "Warning";
		    	
				if (newCompID == null || newCompID.length() == 0) {
				    // --- Check if the component id is empty
					message = "Enter a valid name";
				    JOptionPane.showMessageDialog(this, Language.translate(message, Language.EN), Language.translate(title, Language.EN), JOptionPane.WARNING_MESSAGE);
				    getJTableComponents().getModel().setValueAt(oldCompID, row, column);
		
				} else if (newCompID.contains(" ")) {
				    // --- Check for spaces
					message = "Enter the name without spaces";
				    JOptionPane.showMessageDialog(this, Language.translate(message, Language.EN), Language.translate(title, Language.EN), JOptionPane.WARNING_MESSAGE);
				    getJTableComponents().getModel().setValueAt(oldCompID, row, column);
		
				} else if (getGraphController().getNetworkModelAdapter().getNetworkComponent(newCompID) != null) {
				    // --- Check if a network component name already exists
					message = "The component name already exists!" + newLine + "Choose a different one.";
				    JOptionPane.showMessageDialog(this, Language.translate(message, Language.EN), Language.translate(title, Language.EN), JOptionPane.WARNING_MESSAGE);
				    getJTableComponents().getModel().setValueAt(oldCompID, row, column);
		
				} else if (this.getGraphController().getProject().getSimulationSetups().getCurrSimSetup().isAgentNameExists(newCompID)) {
				    // --- Check if the agent name already exists in the simulation setup
					message = "An agent with the name already exists in the simulation setup!" + newLine + " Choose a different one.";
					JOptionPane.showMessageDialog(this, Language.translate(message, Language.EN), Language.translate(title, Language.EN), JOptionPane.WARNING_MESSAGE);
				    getJTableComponents().getModel().setValueAt(oldCompID, row, column);
		
				} else {
				    // --- All validations done, rename the component and update the network model
					// --- renaming NetworkComponents and GraphElements
					this.getGraphController().getNetworkModelAdapter().renameNetworkComponent(oldCompID, newCompID);
				}
		    }
		}
		// System.out.println(row+","+column);
    }
   
    /**
     * The action, when a Network component was renamed.
     * @param renamedNetworkComponent the renamed network component
     */
    private void networkComponentRenamed(NetworkComponentRenamed renamedNetworkComponent) {
    	
    	if (renamedNetworkComponent==null) return;
    	
    	String oldID = renamedNetworkComponent.getOldNetworkComponentID();
    	String newID = renamedNetworkComponent.getNewNetworkComponentID();
    	
    	int rowCount = this.getDefaultTableModel4Components().getRowCount();
		int row = -1;
		// --- Searching all the rows in the table ----------------------------
		for (row = 0; row < rowCount; row++) {
		    String compId = (String) getJTableComponents().getModel().getValueAt(row, 0);
		    if (compId!=null) {
			    // --- Checking for the matching to the old ID ----------------
			    if (compId.equals(oldID)) {
			    	// --- Set the new ID in the table model ------------------
			    	this.quiteTabelModelListener = true;
			    	this.getJTableComponents().getModel().setValueAt(newID, row, 0);
			    	this.quiteTabelModelListener = false;
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
     * Get the visualization component
     * @return the basic graph GUI which contains the graph visualization component
     */
    public BasicGraphGui getBasicGraphGui() {
		if (graphGUI == null) {
		    graphGUI = new BasicGraphGui(this.getGraphController());
		}
		return graphGUI;
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
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable observable, Object object) {
    	
    	if (object instanceof NetworkModelNotification) {
    		
    		NetworkModelNotification nmNotification = (NetworkModelNotification) object;
    		int reason = nmNotification.getReason();
    		Object infoObject = nmNotification.getInfoObject();
    		NetworkComponent networkComponent = null;
    		
    		switch (reason) {
    		case NetworkModelNotification.NETWORK_MODEL_ComponentTypeSettingsChanged:
    		case NetworkModelNotification.NETWORK_MODEL_Reload:
    		case NetworkModelNotification.NETWORK_MODEL_Merged_With_Supplement_NetworkModel:
    			this.reLoad();
    			break;
				
    		case NetworkModelNotification.NETWORK_MODEL_Component_Added:
				networkComponent = (NetworkComponent) infoObject;
				this.networkComponentAdd(networkComponent);
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_Component_Removed:
				networkComponent = (NetworkComponent) infoObject;
				this.networkComponentRemove(networkComponent);				
				break;
			
			case NetworkModelNotification.NETWORK_MODEL_Component_Select:
				networkComponent = (NetworkComponent) infoObject;
				this.networkComponentSelect(networkComponent);
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_EditComponentSettings:
				break;
			
			case NetworkModelNotification.NETWORK_MODEL_Component_Renamed:
				NetworkComponentRenamed renamed = (NetworkComponentRenamed) infoObject;
				this.networkComponentRenamed(renamed);
				break;
			default:
				break;
			}
    		
    		
    	}
    	
    }

}  //  @jve:decl-index=0:visual-constraint="9,2"
