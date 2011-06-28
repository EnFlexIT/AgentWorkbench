/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
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

package agentgui.graphEnvironment.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.environment.EnvironmentPanel;
import agentgui.core.gui.components.JTableButtonEditor;
import agentgui.core.gui.components.JTableButtonRenderer;
import agentgui.graphEnvironment.networkModel.GraphEdge;
import agentgui.graphEnvironment.networkModel.GraphElement;
import agentgui.graphEnvironment.networkModel.GraphNode;
import agentgui.graphEnvironment.networkModel.NetworkComponent;
import agentgui.graphEnvironment.networkModel.NetworkModel;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
/**
 * The GUI for a GraphEnvironmentController.
 * This contains a pane showing the NetworkComponents table and the BasicGraphGUI.
 * The main class which associates with the components table, the environment model and the Basic Graph GUI.
 * 
 * @see GraphEnvironmentController
 * @see BasicGraphGUI
 * @see agentgui.graphEnvironment.networkModel
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author <br>Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 */
public class GraphEnvironmentControllerGUI extends EnvironmentPanel implements Observer, ActionListener, ListSelectionListener, TableModelListener{
	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * JPanel containing the controls
	 */
	private JPanel pnlControlls = null;
	/**
	 * JScrollPane containing the components table
	 */
	private JScrollPane scpComponentTable = null;
	/**
	 * The components table
	 */
	private JTable tblComponents = null;
	/**
	 *Used for showing buttons in the components table 
	 */
	private TableColumnModel colModel = null;
	/**
	 *Used for sorting and filtering components table 
	 */
	private TableRowSorter<DefaultTableModel> tblSorter = null;

	/**
	 * The configure component types button
	 */
	private JButton btnSetClasses = null;
	/**
	 * The Dialog for setting the component types
	 */
	private ClassSelectionDialog classSelectorDialog = null;  //  @jve:decl-index=0:visual-constraint="333,23"
	
	/**
	 * The Dialog for adding a new network component to the graph 
	 */
	private AddComponentDialog addComponentDialog = null;

	/**
	 * The GUI's GraphEnvironmentController 
	 */
	private GraphEnvironmentController controller = null;
	
	private JLabel lblTable = null;
	/**
	 * The SplitPane containing this GUI's components
	 */
	private JSplitPane jSplitPaneRoot = null;
	/**
	 * The graph visualization component
	 */
	private BasicGraphGUI graphGUI = null;
	private JMenuItem menuAdd = null;
	private JTextField jTextFieldSearch = null;
	
	/**
	 * This is the default constructor
	 */
	public GraphEnvironmentControllerGUI(Project project) {
		super(project);
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {

		controller = new GraphEnvironmentController(this.currProject);
		controller.addObserver(this);
		
		this.setLayout(new BorderLayout());
		this.add(getJSplitPaneRoot(), null);
	}

	/**
	 * This method initializes pnlControlls	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlControlls() {
		if (pnlControlls == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 0.5;
			gridBagConstraints11.gridwidth = 1;
			gridBagConstraints11.gridx = 0;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 3;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 0;
			lblTable = new JLabel();
			lblTable.setText(Language.translate("Search Components",Language.EN));
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridheight = 1;
			gridBagConstraints.gridwidth = 0;
			gridBagConstraints.gridx = 0;
			pnlControlls = new JPanel();
			pnlControlls.setLayout(new GridBagLayout());
			pnlControlls.add(lblTable, gridBagConstraints6);
			pnlControlls.add(getJTextFieldSearch(), gridBagConstraints11);
			pnlControlls.add(getScpComponentTable(), gridBagConstraints);
			pnlControlls.add(getBtnSetClasses(), gridBagConstraints7);
		}
		return pnlControlls;
	}

	/**
	 * This method initializes scpComponentTable	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScpComponentTable() {
		if (scpComponentTable == null) {
			scpComponentTable = new JScrollPane();
			scpComponentTable.setViewportView(getTblComponents());
		}
		return scpComponentTable;
	}

	/**
	 * This method initializes the table column model.
	 * Responsible for showing edit buttons in the 3rd column
	 * @return javax.swing.table.TableColumnModel
	 */
	private TableColumnModel getColModel(){
		final GraphEnvironmentControllerGUI graphEnvironmentControllerGUI = this;		
		colModel = tblComponents.getColumnModel();
        colModel.getColumn(2).setCellRenderer(new JTableButtonRenderer());	        
        colModel.getColumn(2).setCellEditor(new JTableButtonEditor(tblComponents){
			private static final long serialVersionUID = 1L;

			/* (non-Javadoc)
        	 * @see agentgui.core.gui.components.JTableButtonEditor#actionPerformed(java.awt.event.ActionEvent)
        	 */
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		super.actionPerformed(e);
        		int row = tblComponents.getEditingRow();
               
                //converting view coordinates to model coordinates
                int modelRowIndex = tblComponents.convertRowIndexToModel(row);
                String compID = (String) tblComponents.getModel().getValueAt(modelRowIndex, 0);
        		NetworkComponent comp = controller.getGridModel().getNetworkComponent(compID);
        		new ComponentSettingsDialog(currProject, graphEnvironmentControllerGUI, comp).setVisible(true);
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
	private JTable getTblComponents() {
		if (tblComponents == null) {
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
			tblComponents = new JTable(model);
			tblComponents.setRowSorter(tblSorter);
			tblComponents.setColumnModel(getColModel());
			
			tblComponents.getSelectionModel().addListSelectionListener(this);
			tblComponents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tblComponents.setShowGrid(true);
			tblComponents.setFillsViewportHeight(true);							
			tblComponents.setCellSelectionEnabled(true);	    
	        
	        tblComponents.getModel().addTableModelListener(this);

		}
		return tblComponents;
	}
	
	/**
	 * Row filter for updating the table view based on the expression in the text box
	 * Used for searching components
	 */
	public void tblFilter(){
		RowFilter<DefaultTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(getJTextFieldSearch().getText(), 0, 1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        tblSorter.setRowFilter(rf);
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

		DefaultTableModel tblModel  = (DefaultTableModel) getTblComponents().getModel();
		tblModel.setDataVector(getComponentTableContents(), titles);
		getTblComponents().setColumnModel(getColModel());		
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
	        	NetworkComponent comp = (NetworkComponent) controller.getGridModel().getNetworkComponents().values().toArray()[row];
	        	//The Old component ID before the change
	        	String oldCompID = comp.getId();
	        	
	        	//If new ID is not equal to old ID
	        	if(!oldCompID.equals(newCompID)){
	        		//If there is a component with the name already
		        	if(newCompID == null || newCompID.length() == 0){
		        		JOptionPane.showMessageDialog(this,Language.translate("Enter a valid name", Language.EN),
								Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);	 
	        			getTblComponents().getModel().setValueAt(oldCompID, row, column);
		        	}
		        	else if(newCompID.contains(" ")){
		        		JOptionPane.showMessageDialog(this,Language.translate("Enter the name without spaces", Language.EN),
								Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);	 
	        			getTblComponents().getModel().setValueAt(oldCompID, row, column);
		        	}		        	
		        	else if(controller.getGridModel().getNetworkComponent(newCompID)!=null){
	        			JOptionPane.showMessageDialog(this,Language.translate("The component name already exists!\n Choose a different one.", Language.EN),
								Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);	 
	        			getTblComponents().getModel().setValueAt(oldCompID, row, column);
	        		}
	        		else
	        		{// All validations done, rename the component and update the network model	
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
		NetworkModel networkModel = controller.getGridModel();
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
		
		controller.refreshNetworkModel();
	}

	/**
	 * This method builds the tblComponents' contents based on the controllers GridModel 
	 * @return The grid components' IDs and class names
	 */
	private Vector<Vector<String>> getComponentTableContents(){
		Vector<Vector<String>> componentVector = new Vector<Vector<String>>();
		
		if(controller.getGridModel() != null){
			
			// Get the components from the controllers GridModel
			Iterator<NetworkComponent> components = controller.getGridModel().getNetworkComponents().values().iterator();
			
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
	 * This method initializes btnSetClasses	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnSetClasses() {
		if (btnSetClasses == null) {
			btnSetClasses = new JButton();
			btnSetClasses.setText(Language.translate("Komponenten-Typen"));
			btnSetClasses.addActionListener(this);
		}
		return btnSetClasses;
	}
	
	/**
	 * Get the component types definition dialog
	 * @return
	 */
	private ClassSelectionDialog getClassSelectorDialog(){
	//Always creating the new one, instead of hide and open
		//if(classSelectorDialog == null){
			classSelectorDialog = new ClassSelectionDialog(this);
		//}
		return classSelectorDialog;
	}
	
	/**
	 * Get the component types definition dialog.
	 * New dialog object is created each time. 
	 * @return
	 */
	private AddComponentDialog getAddComponentDialog(){
			addComponentDialog = new AddComponentDialog(this);
		return addComponentDialog;
	}
	
	/**
	 * Get the visualization component
	 * @return
	 */
	public BasicGraphGUI getGraphGUI(){
		if(graphGUI == null){
			graphGUI = new BasicGraphGUI(getController());
			graphGUI.addObserver(this);
			if(controller.getGridModel() != null && controller.getGridModel().getGraph() != null){
				graphGUI.setGraph(controller.getGridModel().getGraph());
			}
		}
		return graphGUI;
	}
	
	/**
	 * Very important method which implements handlers for various notifications sent by observables 
	 * 	({@link GraphEnvironmentController} and {@link BasicGraphGUI} ). <br>
	 * Handles Network model updates, and button clicks of Graph GUI and mouse interactions.
	 * 
	 * @param o The observable which notifies this class
	 * @param arg Argument passed
	 */
	@Override
	public void update(Observable o, Object arg) {
		// The network model loaded 
		if(o.equals(controller) && arg.equals(GraphEnvironmentController.EVENT_NETWORKMODEL_LOADED)){
			//Loading for the first time
			if(graphGUI.getVisView()==null)
				graphGUI.setGraph(controller.getGridModel().getGraph()); // New graph visualization viewer is created
			else
				graphGUI.repaintGraph(controller.getGridModel().getGraph()); // Same vis view, but graph is refreshed
			rebuildTblComponents();
		}		
		// Network model is updated/refreshed 
		else if(o.equals(controller) && arg.equals(GraphEnvironmentController.EVENT_NETWORKMODEL_REFRESHED)){			
			graphGUI.repaintGraph(controller.getGridModel().getGraph());
			// Rebuilding the component table
			rebuildTblComponents();
			graphGUI.clearPickedObjects();
		}
		
		
		// From BasicGraphGUI Observable
		else if(o.equals(graphGUI.getMyObservable())){
			    // Casting the argument into Notification class
				BasicGraphGUI.Notification notification = (BasicGraphGUI.Notification ) arg;
				
				if(notification.getEvent().equals(BasicGraphGUI.EVENT_NETWORKMODEL_CLEAR)){
				// Clearing the actual Network and Graph model
					controller.clearNetworkModel();			
				}
				else if(notification.getEvent().equals(BasicGraphGUI.EVENT_ADD_COMPONENT_CLICKED)){
				// Add Component Button Clicked
					
						//If the graph is empty - starting from scratch
					if(controller.getGridModel().getGraph().getVertexCount()==0){
						getAddComponentDialog().setVisible(true);	
					}
						//Picked a vertex
					else if(getPickedVertex()!=null){
							//System.out.println("vertex picked="+getPickedVertex().getId());
							if(checkGridConstraints(getPickedVertex()))
								getAddComponentDialog().setVisible(true);
							else
								JOptionPane.showMessageDialog(this,Language.translate("Select a valid vertex", Language.EN),
										Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);						
					}				
						//No vertex is picked
					else{
						JOptionPane.showMessageDialog(this,Language.translate("Select a valid vertex first", Language.EN),
								Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
					}
				}
				else if(notification.getEvent().equals(BasicGraphGUI.EVENT_REMOVE_COMPONENT_CLICKED)){					
				//Remove Component Button clicked
					
					Set<GraphEdge> edgeSet = graphGUI.getVisView().getPickedEdgeState().getPicked();
					// Atleast one edge is picked
					if(edgeSet.size()>0){ 
						//Get the Network component from the picked edge
						NetworkComponent selectedComponent = getNetworkComponentFromEdge(edgeSet.iterator().next());												
						//Removing the component from the network model and updating the graph
						handleRemoveNetworkComponent(selectedComponent);
						
						getController().refreshNetworkModel();
					}
					// No edge is picked
					else{ 
						JOptionPane.showMessageDialog(this,Language.translate("Select an edge first", Language.EN),
								Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
					}
				}
				else if(notification.getEvent().equals(BasicGraphGUI.EVENT_MERGE_NODES_CLICKED)){					
				//Merge Nodes Button clicked
					
					Set<GraphNode> nodeSet = graphGUI.getVisView().getPickedVertexState().getPicked();
					//Two nodes are picked
					if(nodeSet.size()==2){
						Iterator<GraphNode> nodeIter = nodeSet.iterator();
						GraphNode node1 = nodeIter.next();
						GraphNode node2 = nodeIter.next();
						//Valid nodes are picked
						if(checkGridConstraints(node1) && checkGridConstraints(node2)){
							handleMergeNodes(node1, node2);
						}
						//Invalid nodes are picked
						else{
							JOptionPane.showMessageDialog(this,Language.translate("Select two valid vertices", Language.EN),
									Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
						}
					}
					//Two nodes are not picked
					else{ 
						JOptionPane.showMessageDialog(this,Language.translate("Use Shift and click two vertices", Language.EN),
								Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
					}	
					
				}

				else if(notification.getEvent().equals(BasicGraphGUI.EVENT_SPLIT_NODE_CLICKED)){
				//Split node button clicked
					GraphNode pickedVertex = getPickedVertex();
					//One vertex is picked
					if(pickedVertex!=null){
						//Check whether it is in two network components
						if(getNetworkComponentCount(pickedVertex)==2){
							handleSplitNode(pickedVertex);
						}
						else{
						//The node is not in two components
							JOptionPane.showMessageDialog(this,Language.translate("Vertex should be in two components", Language.EN),
									Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
						}
							
					}
					//Multiple vertices are picked
					else{
						JOptionPane.showMessageDialog(this,Language.translate("Select one vertex", Language.EN),
								Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
					}
				}
				else if(notification.getEvent().equals(BasicGraphGUI.EVENT_IMPORT_GRAPH_CLICKED)){
					//Import Graph button clicked
					JFileChooser graphFC = new JFileChooser();
					graphFC.setFileFilter(new FileNameExtensionFilter(Language.translate(controller.getGraphFileImporter().getTypeString()), controller.getGraphFileImporter().getGraphFileExtension()));
					graphFC.setCurrentDirectory(Application.RunInfo.getLastSelectedFolder());
					if(graphFC.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
						Application.RunInfo.setLastSelectedFolder(graphFC.getCurrentDirectory());
						File graphMLFile = graphFC.getSelectedFile();
						this.controller.importNetworkModel(graphMLFile);
					}
				}
				else if(notification.getEvent().equals(BasicGraphGUI.EVENT_NODE_EDIT_PROPERTIES_CLICKED)){
					// Popup Menu Item Node properties clicked
					GraphNode pickedVertex = getPickedVertex();
					if(pickedVertex!=null){
						new ComponentSettingsDialog(currProject, this, pickedVertex).setVisible(true);
					}
				}
				else if(notification.getEvent().equals(BasicGraphGUI.EVENT_EDGE_EDIT_PROPERTIES_CLICKED)){
					// Popup Menu Item Edge properties clicked
					GraphEdge pickedEdge = getPickedEdge();
					if(pickedEdge!=null){
						NetworkComponent netComp = getNetworkComponentFromEdge(pickedEdge);						
						new ComponentSettingsDialog(currProject, this, netComp).setVisible(true);
					}
				}
				else if(notification.getEvent().equals(BasicGraphGUI.EVENT_OBJECT_LEFT_CLICK) ||
						notification.getEvent().equals(BasicGraphGUI.EVENT_OBJECT_RIGHT_CLICK)){					
				// A graph element was selected in the visualization		
					graphGUI.clearPickedObjects();
					selectObject(notification.getArg());
				}				
				//Double click (left or right) - Graph node or edge
				else if(notification.getEvent().equals(BasicGraphGUI.EVENT_OBJECT_DOUBLE_CLICK)){
					//Show component settings dialog too
					graphGUI.clearPickedObjects();
					selectObject(notification.getArg(), true);
				}
		}
	}
	/**
	 * Splits the node into two nodes, separating the two network components
	 * and updates the graph and the network model
	 * @param node
	 */
	private void handleSplitNode(GraphNode node) {		
		//Environment Network Model
		NetworkModel networkModel = getController().getGridModel();
		Graph<GraphNode,GraphEdge> graph = networkModel.getGraph();
		
		//Get the components containing the node
		Iterator<NetworkComponent> compIter = getNetworkComponentsFromNode(node).iterator();		
//		NetworkComponent comp1 = compIter.next();
		NetworkComponent comp2 = compIter.next();
		
		//Creating the new Graph node
		GraphNode newNode = new GraphNode();
		newNode.setId(networkModel.nextNodeID());
			//Shifting position a bit
		newNode.setPosition(new Point((int)node.getPosition().getX()-20, (int)node.getPosition().getY()-20));
		node.setPosition(new Point((int)node.getPosition().getX()+20, (int)node.getPosition().getY()+20));
		
		//Incident Edges on the node
		Collection<GraphEdge> incidentEdges = graph.getIncidentEdges(node);		
		Iterator<GraphEdge> edgeIter = incidentEdges.iterator();
		while(edgeIter.hasNext()){ // for each incident edge
			GraphEdge edge = edgeIter.next();
			//If the edge is in comp2
			if(comp2.getGraphElementIDs().contains(edge.getId())){						
				//Find the node on the other side of the edge
				GraphNode otherNode = graph.getOpposite(node,edge);
				//Create a new edge with the same ID and type
				GraphEdge newEdge = new GraphEdge(edge.getId(), edge.getComponentType());				
				//if the edge is directed
				if(graph.getSource(edge)!=null) 
				{
					if(graph.getSource(edge) == node)
						graph.addEdge(newEdge,newNode, otherNode, EdgeType.DIRECTED);
					else if(graph.getDest(edge) == node)
						graph.addEdge(newEdge,otherNode, newNode, EdgeType.DIRECTED);
				}
				// if the edge is undirected
				else 
					graph.addEdge(newEdge,newNode, otherNode, EdgeType.UNDIRECTED);
				
				//Removing the old edge from the graph and network model
				graph.removeEdge(edge);
				networkModel.getGraphElements().remove(edge.getId());
				networkModel.getGraphElements().put(newEdge.getId(),newEdge);
			}
		}
		
		//Updating the graph element IDs of the component
		comp2.getGraphElementIDs().remove(node.getId());
		comp2.getGraphElementIDs().add(newNode.getId());
		
		//Adding new node to the network model
		networkModel.getGraphElements().put(newNode.getId(),newNode);
		
		controller.refreshNetworkModel();
	}

	/**
	 * Merges the two nodes as a single node 
	 * and updates the graph and network model
	 * @param node1
	 * @param node2
	 */
	private void handleMergeNodes(GraphNode node1, GraphNode node2) {
		//Environment Network Model
		NetworkModel networkModel = getController().getGridModel();
		Graph<GraphNode,GraphEdge> graph = networkModel.getGraph();
		
		//Get the Network components from the nodes
		
		NetworkComponent comp1 = getNetworkComponentsFromNode(node1).iterator().next();
		NetworkComponent comp2 = getNetworkComponentsFromNode(node2).iterator().next();
		
		//Finding the intersection set of the Graph elements of the two network components
		HashSet<String> intersection = new HashSet<String>(comp1.getGraphElementIDs());
		intersection.retainAll(comp2.getGraphElementIDs());
		//Checking the constraint - Two network components can have maximum one node in common
		if(intersection.size()==0){
			// No common node
			
			//Incident Edges on node2
			Collection<GraphEdge> incidentEdges = graph.getIncidentEdges(node2);		
			Iterator<GraphEdge> edgeIter = incidentEdges.iterator();
			while(edgeIter.hasNext()){ // for each incident edge
				GraphEdge edge = edgeIter.next();
				//Find the node on the other side of the edge
				GraphNode otherNode = graph.getOpposite(node2,edge);
				//Create a new edge with the same ID and type
				GraphEdge newEdge = new GraphEdge(edge.getId(), edge.getComponentType());
				
				//if the edge is directed
				if(graph.getSource(edge)!=null) 
				{
					if(graph.getSource(edge) == node2)
						graph.addEdge(newEdge,node1, otherNode, EdgeType.DIRECTED);
					else if(graph.getDest(edge) == node2)
						graph.addEdge(newEdge,otherNode, node1, EdgeType.DIRECTED);
				}
				// if the edge is undirected
				else 
					graph.addEdge(newEdge,node1, otherNode, EdgeType.UNDIRECTED);
				
				//Removing the old edge from the graph and network model
				graph.removeEdge(edge);
				networkModel.getGraphElements().remove(edge.getId());
				networkModel.getGraphElements().put(newEdge.getId(),newEdge);
			}
			
			//Updating the graph element IDs of the component
			comp2.getGraphElementIDs().remove(node2.getId());
			comp2.getGraphElementIDs().add(node1.getId());
			
			//Removing node2 from the graph and network model
			graph.removeVertex(node2);
			networkModel.getGraphElements().remove(node2.getId());
			
			getController().refreshNetworkModel();
		}
		else
		{
			//Have a common node
			JOptionPane.showMessageDialog(this,Language.translate("The two network components selected already have a vertex in common", Language.EN),
					Language.translate("Constraint: Two components can have atmost one common vertex", Language.EN),JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * Gives the set of network components containing the given node.
	 * @param node - A GraphNode
	 * @return HashSet<NetworkComponent> - The set of components which contain the node
	 */
	public HashSet<NetworkComponent> getNetworkComponentsFromNode(GraphNode node){						
		// Get the components from the controllers GridModel
		HashSet<NetworkComponent>  compSet = new HashSet<NetworkComponent>();
		Iterator<NetworkComponent> components = controller.getGridModel().getNetworkComponents().values().iterator();						
		while(components.hasNext()){ // iterating through all network components
			NetworkComponent comp = components.next();
			// check if the component contains the given node
			if(comp.getGraphElementIDs().contains(node.getId())){
				compSet.add(comp);
			}
		}
		return compSet;		
	}
	
	/**
	 * Returns the number of network components which have the given node
	 * @param node Vertex in the Graph
	 * @return count No of network components containing the given node
	 */
	public int getNetworkComponentCount(GraphNode node){
		if(controller.getGridModel() != null){				
			// Get the components from the controllers GridModel
			Iterator<NetworkComponent> components = controller.getGridModel().getNetworkComponents().values().iterator();						
			int count = 0;
			while(components.hasNext()){ // iterating through all network components
				NetworkComponent comp = components.next();
				// check if the component contains the current node
				if(comp.getGraphElementIDs().contains(node.getId())){
					count++;
				}
			}
			return count;
		}
		return 0;
	}
	/**
	 * Removes the given network component from the network model,
	 * updates the graph accordingly and the hashmap of graphElements 
	 * @param selectedComponent The network component to be removed
	 */
	private void handleRemoveNetworkComponent(NetworkComponent selectedComponent) {
		NetworkModel networkModel = getController().getGridModel();
		
		//The IDs of the elements present in the given component 
		HashSet<String> graphElementIDs = selectedComponent.getGraphElementIDs();
		Iterator<String> idIter = graphElementIDs.iterator();
		
		//For each graph element of the network component
		while(idIter.hasNext()){ 
			String elementID = idIter.next();
			GraphElement element = networkModel.getGraphElement(elementID);
			//For an edge		
			if(element instanceof GraphEdge){ 
				//Remove from the Graph
				networkModel.getGraph().removeEdge((GraphEdge)element);
				//Remove from the HashMap of GraphElements
				networkModel.getGraphElements().remove(element.getId());
			}
			//For a node
			else if (element instanceof GraphNode){
				//Check whether the GraphNode is present in any other NetworkComponent
				
				// The vertex is only present in the selected component
				if(getNetworkComponentCount((GraphNode)element)==1){
					//Removing the vertex from the Graph
					networkModel.getGraph().removeVertex((GraphNode)element);
					//Remove from the HashMap of GraphElements
					networkModel.getGraphElements().remove(element.getId());
				}
			}
		}
		//Finally, remove the network component from the HashMap of components in the network model
		networkModel.removeNetworkComponent(selectedComponent);
	}
	
	
	/**
	 * Checks the constraint for a given node in the network model.
	 * Constraint - a node can be a member of maximum two network components.
	 * @param object - selected node
	 * @return true if the node is a member of 0 or 1 components, false otherwise
	 */
	public boolean checkGridConstraints(Object object) {
		if(object instanceof GraphNode){
			GraphNode node = (GraphNode) object;			
				if(getNetworkComponentCount(node)<2)
					return true;				
		}	
		return false;
	}

	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(getBtnSetClasses())){
			getClassSelectorDialog().setVisible(true);
		}
	}

	/**
	 * Invoked when a row of the components table is selected.
	 * Highlights the selected network component in the Graph. 
	 * @param e List selection event
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(getTblComponents().getSelectedRowCount() > 0){
			//Converting from view coordinates to model coordinates
			int selectedIndex = getTblComponents().convertRowIndexToModel(getTblComponents().getSelectedRow());
			String componentID = (String) tblComponents.getModel().getValueAt(selectedIndex, 0);
			NetworkComponent component = controller.getGridModel().getNetworkComponent(componentID);
			
			graphGUI.clearPickedObjects();
			selectObject(component);			
		}
	}
	/**
	 * This method get's the GUI's controller
	 * @return GraphEnvironmentController The controller of the environment network model.
	 */
	GraphEnvironmentController getController(){
		return controller;
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
			jSplitPaneRoot.setDividerLocation(200);
		}
		return jSplitPaneRoot;
	}
	/**
	 * This method is called when the component type settings have been changed
	 */
	void componentSettingsChanged(){
		graphGUI.clearPickedObjects();
		getTblComponents().getSelectionModel().clearSelection();
	}
	/**
	 * This method is called when changing the component type settings has been aborted
	 */
	void componentSettingsChangeAborted(){
		graphGUI.clearPickedObjects();
		getTblComponents().getSelectionModel().clearSelection();
	}
	
	/**
	 * This method handles the selection of an object, i.e. highlights the related graph elements
	 * @param object The object to select
	 */
	void selectObject(Object object){
		if(object instanceof GraphNode){
			graphGUI.setPickedObject((GraphElement) object);
		}else if(object instanceof GraphEdge){
			NetworkComponent netComp = getNetworkComponentFromEdge((GraphEdge)object);
			graphGUI.setPickedObjects(getNetworkComponentElements(netComp));
			selectComponentInTable(netComp);
		}else if(object instanceof NetworkComponent){
			graphGUI.setPickedObjects(getNetworkComponentElements((NetworkComponent)object));
		}
	}
	
	/**
	 * Same as selectObject but optionally shows component settings dialog 
	 * @param object 
	 * @param showComponentSettingsDialog - shows the dialog if true
	 */
	void selectObject(Object object, boolean showComponentSettingsDialog){
		selectObject(object);
		
		if(showComponentSettingsDialog){
			if(object instanceof GraphNode){
				new ComponentSettingsDialog(currProject, this, object).setVisible(true);			
			}else if(object instanceof GraphEdge){
				NetworkComponent netComp = getNetworkComponentFromEdge((GraphEdge)object);
				new ComponentSettingsDialog(currProject, this, netComp).setVisible(true);
			}else if(object instanceof NetworkComponent){
				new ComponentSettingsDialog(currProject, this, (NetworkComponent)object).setVisible(true);
			}		
		}
	}
	/**
	 * Given a network component, selects the corresponding row in the components display table
	 * @param netComp
	 */
	private void selectComponentInTable(NetworkComponent netComp) {
		int rowCount = getTblComponents().getModel().getRowCount();
		int row = -1;
		//Searching all the rows in the table
		for(row=0 ; row < rowCount ; row++){			
			String compId = (String) getTblComponents().getModel().getValueAt(row, 0);
			//Checking for the matching component Id 
			if(compId.equals(netComp.getId())){
				//Converting from model cooardinates to view coordinates
				int viewRowIndex = getTblComponents().convertRowIndexToView(row);
				int viewColumnIndex = getTblComponents().convertColumnIndexToView(0);				
				boolean toggle = false;
				boolean extend = false;
				//Selecting the cell in the table
				getTblComponents().changeSelection(viewRowIndex,viewColumnIndex,toggle,extend );
				break;
			}
		}							
	}

	/**
	 * Returns the node which is picked. If multiple nodes are picked, returns null.
	 * @return GraphNode - the GraphNode which is picked. 
	 * 
	 */
	public GraphNode getPickedVertex(){
			Set<GraphNode> nodeSet = graphGUI.getVisView().getPickedVertexState().getPicked();
			if(nodeSet.size()==1)
				return nodeSet.iterator().next();
			return null;
	}
	/**
	 * Returns the edge which is picked. If multiple nodes are picked, returns null.
	 * @return GraphEdge - the GraphNode which is picked. 
	 * 
	 */
	public GraphEdge getPickedEdge(){
			Set<GraphEdge> edgeSet = graphGUI.getVisView().getPickedEdgeState().getPicked();
			if(edgeSet.size()==1)
				return edgeSet.iterator().next();
			return null;
	}
	
	/**
	 * This method gets the NetworkComponent the GraphEdge with the given ID belongs to
	 * @param edge The GraphEdge's ID
	 * @return The NetworkComponent
	 */
	private NetworkComponent getNetworkComponentFromEdge(GraphEdge edge){
		// Get the components from the controllers GridModel
		Iterator<NetworkComponent> components = controller.getGridModel().getNetworkComponents().values().iterator();						
		while(components.hasNext()){ // iterating through all network components
			NetworkComponent comp = components.next();
			// check if the component contains the given node
			if(comp.getGraphElementIDs().contains(edge.getId())){
				return comp;
			}
		}
		return null;
	}
	/**
	 * This method gets the GraphElements that are part of the given NetworkComponent
	 * @param netComp The NetworkComponent
	 * @return The GraphElements
	 */
	private Vector<GraphElement> getNetworkComponentElements(NetworkComponent netComp){
		Vector<GraphElement> elements = new Vector<GraphElement>();
		
		Iterator<String> elementIDs = netComp.getGraphElementIDs().iterator();
		while(elementIDs.hasNext()){
			elements.add(controller.getGridModel().getGraphElement(elementIDs.next()));
		}
		
		return elements;
	}

	/**
	 * This method initializes menuAdd	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMenuAdd() {
		if (menuAdd == null) {
			menuAdd = new JMenuItem();
			menuAdd.setText("Add Component");
			menuAdd.addActionListener(this);
		}
		return menuAdd;
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

}  //  @jve:decl-index=0:visual-constraint="33,19"
