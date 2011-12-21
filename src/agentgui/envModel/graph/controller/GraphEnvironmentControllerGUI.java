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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
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
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The GUI for a GraphEnvironmentController.
 * This contains a pane showing the NetworkComponents table and the BasicGraphGUI.
 * The main class which associates with the components table, the environment model and the Basic Graph GUI.
 * 
 * @see GraphEnvironmentController
 * @see BasicGraphGui
 * @see agentgui.envModel.graph.networkModel
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author <br>Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 */
public class GraphEnvironmentControllerGUI extends EnvironmentPanel implements Observer, ListSelectionListener, TableModelListener {
	
	private static final long serialVersionUID = 7376906096627051173L;

	private final static String pathImage = GraphGlobals.getPathImages();
	
	/** The SplitPane containing this GUI's components */
	private JSplitPane jSplitPaneRoot = null;
	
	private JPanel jPanelControls = null;
	private JScrollPane jScrollPaneComponentsTable = null;
	private JLabel jLabelTable = null;
	private JTextField jTextFieldSearch = null;
	private JButton jButtonClearSearch = null;

	private JTable jTableComponents = null;
	private TableColumnModel colModel = null;
	private TableRowSorter<DefaultTableModel> tblSorter = null;

	/** The graph visualization component */
	private BasicGraphGui graphGUI = null;
	
	
	/**
	 * This is the default constructor for just displaying the current
	 * environment model during a running simulation
	 */
	public GraphEnvironmentControllerGUI(EnvironmentController envController) {
		super(envController);
		this.initialize();
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getJSplitPaneRoot(), null);
		this.showNumberOfComponents();
	}

	/**
	 * Returns the graph environment controller.
	 * @return the graph environment controller
	 */
	public GraphEnvironmentController getGraphController() {
		return (GraphEnvironmentController) this.environmentController;
	}
	/**
	 * Gets the current project.
	 * @return the current project
	 */
	public Project getCurrentProject() {
		return this.getGraphController().getProject();
	}

	/**
	 * This method initializes jSplitPaneRoot	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPaneRoot() {
		if (jSplitPaneRoot == null) {
			jSplitPaneRoot = new JSplitPane();
			jSplitPaneRoot.setOneTouchExpandable(true);
			jSplitPaneRoot.setLeftComponent(getPnlControlls());
			jSplitPaneRoot.setRightComponent(getGraphGUI());
			jSplitPaneRoot.setDividerLocation(230);
		}
		return jSplitPaneRoot;
	}
	/**
	 * This method initializes pnlControlls	
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
			jLabelTable.setText(Language.translate(jLabelTable.getText(),Language.EN));
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
	 * This method initializes the table column model.
	 * Responsible for showing edit buttons in the 3rd column
	 * @return javax.swing.table.TableColumnModel
	 */
	private TableColumnModel getColumnModel(){
		
		colModel = jTableComponents.getColumnModel();
        colModel.getColumn(2).setCellRenderer(new TableCellRenderer4Button());	        
        colModel.getColumn(2).setCellEditor(new TableCellEditor4TableButton(jTableComponents){
			private static final long serialVersionUID = 1L;
			/* (non-Javadoc)
        	 * @see agentgui.core.gui.components.JTableButtonEditor#actionPerformed(java.awt.event.ActionEvent)
        	 */
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		super.actionPerformed(e);
        		int row = jTableComponents.getEditingRow();
               
                //converting view coordinates to model coordinates
                int modelRowIndex = jTableComponents.convertRowIndexToModel(row);
                String compID = (String) jTableComponents.getModel().getValueAt(modelRowIndex, 0);
        		NetworkComponent comp = getGraphController().getNetworkModel().getNetworkComponent(compID);
        		new OntologySettingsDialog(getCurrentProject(), getGraphController(), comp).setVisible(true);
        	}
        });
        
        //Setting column widths
        colModel.getColumn(0).setPreferredWidth(20);
        colModel.getColumn(2).setPreferredWidth(30);
		return colModel;
	}
	
	/**
	 * This method initializes network components table tblComponents and the TabelModel	
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
				public boolean isCellEditable(int row, int col) {
				        if (col != 1) {
				            return true;
				        } else {
				            return false;
				        }
				    }
			};
			
			tblSorter = new TableRowSorter<DefaultTableModel>(model);	
			jTableComponents = new JTable(model);
			jTableComponents.setRowSorter(tblSorter);
			jTableComponents.setColumnModel(getColumnModel());
			
			jTableComponents.getSelectionModel().addListSelectionListener(this);
			jTableComponents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableComponents.setShowGrid(true);
			jTableComponents.setFillsViewportHeight(true);							
			jTableComponents.setCellSelectionEnabled(true);	    
	        
	        jTableComponents.getModel().addTableModelListener(this);

		}
		return jTableComponents;
	}
	
	/**
	 * Row filter for updating table view based on the expression in the text box
	 * Used for searching components
	 */
	public void tblFilter(){
		RowFilter<DefaultTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter("(?i).*(" + getJTextFieldSearch().getText() + ").*",  0, 1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        tblSorter.setRowFilter(rf);
        
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
	
	/**
	 * Rebuilds the components table by updating the table data model with a new vector 
	 */
	private void rebuildTblComponents(){
		// Column titles
		Vector<String> titles = new Vector<String>();
		titles.add(Language.translate("Komponente"));
		titles.add(Language.translate("Typ"));
		titles.add(Language.translate("Options",Language.EN));

		DefaultTableModel tblModel  = (DefaultTableModel) this.getJTableComponents().getModel();
		tblModel.setDataVector(this.getComponentTableContents(), titles);
		getJTableComponents().setColumnModel(getColumnModel());		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	/**
	 * Invoked when a table data model changes.
	 * Used for renaming the network component.
	 */
	@Override
	public void tableChanged(TableModelEvent e) {
			
		int row = e.getFirstRow();
	        int column = e.getColumn(); //In model coordinates
	    
	        DefaultTableModel model = (DefaultTableModel)e.getSource();
	        // If the component name is changed
	        if(column == 0 && row >=0 && row < model.getRowCount()){
	        	String newCompID = (String) model.getValueAt(row, column);	    
	        	//Getting the corresponding comp from the network model
	        	NetworkComponent comp = (NetworkComponent) getGraphController().getNetworkModel().getNetworkComponents().values().toArray()[row];
	        	//The Old component ID before the change
	        	String oldCompID = comp.getId();
	        	
	        	//If new ID is not equal to old ID
	        	if(!oldCompID.equals(newCompID)){
	        		
		        	if(newCompID == null || newCompID.length() == 0){
		        		// --- Check if the  component id is empty
		        		JOptionPane.showMessageDialog(this,Language.translate("Enter a valid name", Language.EN),
								Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);	 
	        			getJTableComponents().getModel().setValueAt(oldCompID, row, column);
	        			
		        	} else if(newCompID.contains(" ")){
		        		// --- Check for spaces
		        		JOptionPane.showMessageDialog(this,Language.translate("Enter the name without spaces", Language.EN),
								Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);	 
	        			getJTableComponents().getModel().setValueAt(oldCompID, row, column);
	        			
		        	} else if(getGraphController().getNetworkModel().getNetworkComponent(newCompID)!=null){
			        	// --- Check if a network component name already exists
		        		JOptionPane.showMessageDialog(this,Language.translate("The component name already exists!\n Choose a different one.", Language.EN),
								Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);	 
	        			getJTableComponents().getModel().setValueAt(oldCompID, row, column);
	        			
	        		} else if(getCurrentProject().simulationSetups.getCurrSimSetup().isAgentNameExists(newCompID)) {
		        		// --- Check if the agent name already exists in the simulation setup
		        		JOptionPane.showMessageDialog(this,Language.translate("An agent with the name already exists in the simulation setup!\n Choose a different one.", Language.EN),
								Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);	 
	        			getJTableComponents().getModel().setValueAt(oldCompID, row, column);
	        			
		        	} else {
	        			// --- All validations done, rename the component and update the network model	
	        			handleRenameComponent(oldCompID,newCompID);
	        		}
	        	}
	        }
	        //System.out.println(row+","+column);		
	}

	
	/**
	 * Changes the network component Id from old to new 
	 * and updates the graph and the network model accordingly
	 * @param oldCompID
	 * @param newCompID
	 */
	private void handleRenameComponent(String oldCompID, String newCompID) {
		//Environment network model
		NetworkModel networkModel = this.getGraphController().getNetworkModel();
		NetworkComponent comp = networkModel.getNetworkComponent(oldCompID);
		
		//Temporary set
		HashSet<String> newGraphElementIDs = new HashSet<String>(comp.getGraphElementIDs());
		//Renaming the corresponding edges of the network component
		Iterator<String> elementIter = comp.getGraphElementIDs().iterator();
		//For each Graph Element of the component
		while(elementIter.hasNext()){
			String elementID = elementIter.next();
			GraphElement element = networkModel.getGraphElement(elementID);
			if(element instanceof GraphEdge){
				String newElementID = elementID.replaceFirst(oldCompID, newCompID);
				//Updating the graph
				element.setId(newElementID);		
				
				newGraphElementIDs.remove(elementID);
				newGraphElementIDs.add(newElementID);			
				
				//Updating the GraphElement HashMap of the network model
				networkModel.getGraphElements().remove(elementID);
				networkModel.getGraphElements().put(newElementID,element);
			}
		}
		networkModel.removeNetworkComponent(comp);
		
		//Updating the network component
		comp.setGraphElementIDs(newGraphElementIDs);
		comp.setId(newCompID);
		
		networkModel.addNetworkComponent(comp);
		
		this.getGraphController().refreshNetworkModel();
		
		//Renaming the agent in the agent start list of the simulation setup
		int i=0;
		for( i=0; i<this.getGraphController().getAgents2Start().size(); i++) {
			AgentClassElement4SimStart ac4s = (AgentClassElement4SimStart) this.getGraphController().getAgents2Start().get(i);
			if(ac4s.getStartAsName().equals(oldCompID)){
				ac4s.setStartAsName(newCompID);
				break;
			}
		}
		
	}

	/**
	 * This method builds the tblComponents' contents based on the controllers GridModel 
	 * @return The grid components' IDs and class names
	 */
	private Vector<Vector<String>> getComponentTableContents(){
		
		Vector<Vector<String>> componentVector = new Vector<Vector<String>>();
		if(this.getGraphController().getNetworkModel() != null){
			
			// Get the components from the controllers GridModel
			Iterator<NetworkComponent> components = this.getGraphController().getNetworkModel().getNetworkComponents().values().iterator();
			// Add component ID and class name to the data vector
			while(components.hasNext()){
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
	 * Get the visualization component
	 * @return the basic graph GUI which contains the graph visualization component
	 */
	public BasicGraphGui getGraphGUI(){
		if(graphGUI == null){
			graphGUI = new BasicGraphGui(this.getGraphController());
			NetworkModel networkModel = this.getGraphController().getNetworkModel();
			if(networkModel!=null && networkModel.getGraph()!=null){
				graphGUI.setGraph(networkModel.getGraph());
			}
		}
		return graphGUI;
	}
	
	/**
	 * Sets the graph GUI.
	 * @param basicGraphGui the new graph GUI
	 */
	public void setGraphGUI(BasicGraphGui basicGraphGui) {
		
		// --- remove the old one ---------------
		JPanel comp = (JPanel) this.getJSplitPaneRoot().getRightComponent();
		if (comp!=null) {
			this.getJSplitPaneRoot().remove(comp);	
		}
		// --- set the new one ------------------
		this.graphGUI = basicGraphGui;
		this.getJSplitPaneRoot().setRightComponent(basicGraphGui);
		
	}
	
	/**
	 * Very important method which implements handlers for various notifications sent by observables 
	 * 	({@link GraphEnvironmentController} and {@link BasicGraphGui} ). <br>
	 * Handles Network model updates, and button clicks of Graph GUI and mouse interactions.
	 * 
	 * @param o The observable which notifies this class
	 * @param arg Argument passed
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		if(o.equals(this.getGraphController()) && arg.equals(GraphEnvironmentController.EVENT_NETWORKMODEL_LOADED)){
			// --- The network model loaded --------------- 
			// Create new graph visualization viewer --
			graphGUI.setGraph(this.getGraphController().getNetworkModel().getGraph()); 

			this.rebuildTblComponents();
			this.showNumberOfComponents();
			
		} else if(o.equals(this.getGraphController()) && arg.equals(GraphEnvironmentController.EVENT_NETWORKMODEL_REFRESHED)) {			
			// --- Network model is updated/refreshed -----
			graphGUI.repaintGraph(this.getGraphController().getNetworkModel().getGraph());
			// --- Rebuilding the component table ---------
			this.rebuildTblComponents();
			this.showNumberOfComponents();
			graphGUI.clearPickedObjects();
			
		}
	}

	
	/**
	 * Invoked when a row of the components table is selected.
	 * Highlights the selected network component in the Graph. 
	 * @param e List selection event
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(getJTableComponents().getSelectedRowCount() > 0){
			//Converting from view coordinates to model coordinates
			int selectedIndex = getJTableComponents().convertRowIndexToModel(getJTableComponents().getSelectedRow());
			String componentID = (String) jTableComponents.getModel().getValueAt(selectedIndex, 0);
			NetworkComponent component = this.getGraphController().getNetworkModel().getNetworkComponent(componentID);
			
			graphGUI.clearPickedObjects();
			graphGUI.selectObject(component);			
		}
	}
	
	/**
	 * Given a network component, selects the corresponding row in the components display table
	 * @param netComp
	 */
	public void selectComponentInTable(NetworkComponent netComp) {
		int rowCount = getJTableComponents().getModel().getRowCount();
		int row = -1;
		//Searching all the rows in the table
		for(row=0 ; row < rowCount ; row++){			
			String compId = (String) getJTableComponents().getModel().getValueAt(row, 0);
			//Checking for the matching component Id 
			if(compId.equals(netComp.getId())){
				//Converting from model cooardinates to view coordinates
				int viewRowIndex = getJTableComponents().convertRowIndexToView(row);
				int viewColumnIndex = getJTableComponents().convertColumnIndexToView(0);				
				boolean toggle = false;
				boolean extend = false;
				//Selecting the cell in the table
				getJTableComponents().changeSelection(viewRowIndex,viewColumnIndex,toggle,extend );
				break;
			}
		}							
	}

	/**
	 * This method initializes jTextFieldSearch	
	 * 	Search box - Used for filtering the components
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldSearch() {
		if (jTextFieldSearch == null) {
			jTextFieldSearch = new JTextField();
			jTextFieldSearch.setPreferredSize(new Dimension(100, 20));
			jTextFieldSearch.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(java.awt.event.KeyEvent e) {
					//Calling the table row filter for searching the components
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
			jButtonClearSearch.setIcon(new ImageIcon(this.getClass().getResource( pathImage + "ClearSearch.png")));
			jButtonClearSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getJTextFieldSearch().setText(null);
					tblFilter();
				}
			});
		}
		return jButtonClearSearch;
	}
	
}  //  @jve:decl-index=0:visual-constraint="33,19"
