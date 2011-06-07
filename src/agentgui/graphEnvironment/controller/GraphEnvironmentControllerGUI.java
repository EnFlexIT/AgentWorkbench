package agentgui.graphEnvironment.controller;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.environment.EnvironmentPanel;
import agentgui.graphEnvironment.networkModel.GraphEdge;
import agentgui.graphEnvironment.networkModel.GraphElement;
import agentgui.graphEnvironment.networkModel.GraphNode;
import agentgui.graphEnvironment.networkModel.NetworkComponent;
import agentgui.graphEnvironment.networkModel.NetworkModel;
/**
 * The GUI for a GraphEnvironmentController
 * @author Nils
 *
 */
public class GraphEnvironmentControllerGUI extends EnvironmentPanel implements Observer, ActionListener, ListSelectionListener{
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
	 * The import graph button
	 */
	private JButton btnLoadGraph = null;
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
	private JPopupMenu popup = null;  //  @jve:decl-index=0:visual-constraint="608,53"
	private JMenuItem menuAdd = null;
	
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
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 2;
			lblTable = new JLabel();
			lblTable.setText(Language.translate("Netz-Komponenten"));
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridheight = 1;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.gridwidth = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 3;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridheight = 1;
			gridBagConstraints.gridwidth = 0;
			gridBagConstraints.gridx = 0;
			pnlControlls = new JPanel();
			pnlControlls.setLayout(new GridBagLayout());
			pnlControlls.add(getScpComponentTable(), gridBagConstraints);
			pnlControlls.add(getBtnImportGraph(), gridBagConstraints1);
			pnlControlls.add(getBtnSetClasses(), gridBagConstraints7);
			pnlControlls.add(lblTable, gridBagConstraints6);
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
	 * This method initializes tblComponents	
	 * @return javax.swing.JTable	
	 */
	private JTable getTblComponents() {
		if (tblComponents == null) {
			// Column titles
			Vector<String> titles = new Vector<String>();
			titles.add(Language.translate("Komponente"));
			titles.add(Language.translate("Typ"));
			
			tblComponents = new JTable(getComponentTableContents(), titles);
			tblComponents.getSelectionModel().addListSelectionListener(this);
			tblComponents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tblComponents.setShowGrid(true);
			tblComponents.setFillsViewportHeight(true);
		}
		return tblComponents;
	}
	
	/**
	 * Creates a JTable containing the IDs and types of all Components from the GridModel 
	 */
	private void rebuildTblComponents(){
		// Column titles
		Vector<String> titles = new Vector<String>();
		titles.add(Language.translate("Komponente"));
		titles.add(Language.translate("Typ"));
		
		getTblComponents().setModel(new DefaultTableModel(getComponentTableContents(), titles));
	}
	
	/**
	 * Clear the components table by removing all the rows
	 */
	private void clearTblComponents(){
		Vector<String> titles = new Vector<String>();
		titles.add(Language.translate("Komponente"));
		titles.add(Language.translate("Typ"));
		getTblComponents().setModel(new DefaultTableModel(titles,0));
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
				componentVector.add(compData);
			}
		}
		return componentVector;
	}

	/**
	 * This method initializes btnLoadGraph	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnImportGraph() {
		if (btnLoadGraph == null) {
			btnLoadGraph = new JButton();
			btnLoadGraph.setText(Language.translate("Graph Importieren"));
			btnLoadGraph.addActionListener(this);
		}
		return btnLoadGraph;
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
		if(classSelectorDialog == null){
			classSelectorDialog = new ClassSelectionDialog(this);
		}
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
	private BasicGraphGUI getGraphGUI(){
		if(graphGUI == null){
			graphGUI = new BasicGraphGUI();
			graphGUI.addObserver(this);
			if(controller.getGridModel() != null && controller.getGridModel().getGraph() != null){
				graphGUI.setGraph(controller.getGridModel().getGraph());
			}
		}
		return graphGUI;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// The network model loaded 
		if(o.equals(controller) && arg.equals(GraphEnvironmentController.EVENT_NETWORKMODEL_LOADED)){
			graphGUI.setGraph(controller.getGridModel().getGraph());
			rebuildTblComponents();
		}		
		// Network model is updated/refreshed 
		else if(o.equals(controller) && arg.equals(GraphEnvironmentController.EVENT_NETWORKMODEL_REFRESHED)){			
			graphGUI.graphRepaint(controller.getGridModel().getGraph());
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
								JOptionPane.showMessageDialog(this,Language.translate("Select a valid vertex", Language.EN),Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);						
					}				
						//No vertex is picked
					else{
						JOptionPane.showMessageDialog(this,Language.translate("Right click a valid vertex first", Language.EN),Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
					}
				}
				else if(notification.getEvent().equals(BasicGraphGUI.EVENT_REMOVE_COMPONENT_CLICKED)){					
				//Remove Component Button clicked
					Set<GraphEdge> edgeSet = graphGUI.getVisView().getPickedEdgeState().getPicked();
					// Atleast one edge is picked
					if(edgeSet.size()>0){ 
						//Get the Network component from the picked edge
						NetworkComponent selectedComponent = getNetworkComponent(edgeSet.iterator().next().getId());												
						//Removing the component from the network model and updating the graph
						handleRemoveNetworkComponent(selectedComponent);
						
						getController().refreshNetworkModel();
					}
					// No edge is picked
					else{ 
						JOptionPane.showMessageDialog(this,Language.translate("Right click an edge first", Language.EN),Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
					}
				}
				else if(notification.getEvent().equals(BasicGraphGUI.EVENT_OBJECT_LEFT_CLICK)){					
				// A graph element was selected in the visualization				
					selectObject(notification.getArg());
				}
				else if(notification.getEvent().equals(BasicGraphGUI.EVENT_OBJECT_RIGHT_CLICK)){					
				//Right click
					Point currentPoint = MouseInfo.getPointerInfo().getLocation();
					Object obj= notification.getArg();
					
					graphGUI.clearPickedObjects();
					//Node is clicked
					if(obj instanceof GraphNode){ 
						//Check the constraints and if true, set node as picked 
						if(checkGridConstraints(obj)){						
							graphGUI.setPickedObject((GraphElement) obj);
						}
					}
					//Edge is clicked
					else if(obj instanceof GraphEdge){ 
						//Get the network component and pick the related elements
						NetworkComponent netComp = getNetworkComponent(((GraphElement)obj).getId());
						graphGUI.setPickedObjects(getNetworkComponentElements(netComp));
					}					
					
					//TODO  Show the right click context menu
					   //Problem: The menu does not hide when clicked else where 
					//getPopup().show(null, currentPoint.x, currentPoint.y);				
				}
		}
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
				Iterator<String> keyIter = networkModel.getNetworkComponents().keySet().iterator();
				int count = 0;
				//For each component in the model
				while(keyIter.hasNext()){
					NetworkComponent component = networkModel.getNetworkComponents().get(keyIter.next());
					//component contains the vertex
					if(component.getGraphElementIDs().contains(element.getId())){
						count++;
					}
				}
				
				// The vertex is only present in the selected component
				if(count==1){
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
	 * Checks the constraint for a given node in the network model
	 * Constraint - a node can be a member of maximum two network components
	 * @param object - selected node
	 * @return true if the node is a member of 0 or 1 components, false otherwise
	 */
	public boolean checkGridConstraints(Object object) {
		if(object instanceof GraphNode){
			GraphNode node = (GraphNode) object;
			if(controller.getGridModel() != null){				
				// Get the components from the controllers GridModel
				Iterator<NetworkComponent> components = controller.getGridModel().getNetworkComponents().values().iterator();
							
				int count =0;
				while(components.hasNext()){ // iterating through all network components
					NetworkComponent comp = components.next();
					// check if the component contains the current node
					if(comp.getGraphElementIDs().contains(node.getId())){
						count++;
					}
				}
				if(count<2)
					return true;				
			}			
		}	
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(getBtnImportGraph())){
			JFileChooser graphFC = new JFileChooser();
			graphFC.setFileFilter(new FileNameExtensionFilter(Language.translate(controller.getGraphFileImporter().getTypeString()), controller.getGraphFileImporter().getGraphFileExtension()));
			graphFC.setCurrentDirectory(Application.RunInfo.getLastSelectedFolder());
			if(graphFC.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				Application.RunInfo.setLastSelectedFolder(graphFC.getCurrentDirectory());
				File graphMLFile = graphFC.getSelectedFile();
				this.controller.importNetworkModel(graphMLFile);
			}
		}else if(event.getSource().equals(getBtnSetClasses())){
			getClassSelectorDialog().setVisible(true);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(getTblComponents().getSelectedRowCount() > 0){
			String componentID = (String) tblComponents.getModel().getValueAt(getTblComponents().getSelectedRow(), 0);
			NetworkComponent component = controller.getGridModel().getNetworkComponent(componentID);
			selectObject(component);
		}
	}
	/**
	 * This method get's the GUI's controller
	 * @return
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
	 * This method handles the selection of an object, i.e. highlights the related graph elements and shows a ComponentSettingsDialog
	 * @param object The object to select
	 */
	void selectObject(Object object){
		graphGUI.clearPickedObjects();
		if(object instanceof GraphNode){
			graphGUI.setPickedObject((GraphElement) object);
			new ComponentSettingsDialog(currProject, this, object).setVisible(true);
		}else if(object instanceof GraphEdge){
			NetworkComponent netComp = getNetworkComponent(((GraphElement)object).getId());
			graphGUI.setPickedObjects(getNetworkComponentElements(netComp));
			new ComponentSettingsDialog(currProject, this, netComp).setVisible(true);
		}else if(object instanceof NetworkComponent){
			graphGUI.setPickedObjects(getNetworkComponentElements((NetworkComponent)object));
			new ComponentSettingsDialog(currProject, this, (NetworkComponent)object).setVisible(true);
		}
	}
	
	/**
	 * @return the GraphNode which is picked. 
	 * If multiple nodes are picked, returns null
	 */
	public GraphNode getPickedVertex(){
			Set<GraphNode> nodeSet = graphGUI.getVisView().getPickedVertexState().getPicked();
			if(nodeSet.size()==1)
				return nodeSet.iterator().next();
			return null;
	}
	
	/**
	 * This method gets the NetworkComponent the GraphEdge eith the given ID belongs to
	 * @param edgeID The GraphEdge's ID
	 * @return The NetworkComponent
	 */
	private NetworkComponent getNetworkComponent(String edgeID){
		String netCompID;
		if(edgeID.indexOf('_')>=0){
			netCompID = edgeID.substring(0, edgeID.indexOf('_'));
		}else{
			netCompID = edgeID;
		}
		return controller.getGridModel().getNetworkComponent(netCompID);
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
	 * This method initializes popup	
	 * 	
	 * @return javax.swing.JPopupMenu	
	 */
	private JPopupMenu getPopup() {
		if (popup == null) {
			popup = new JPopupMenu();
			popup.add(getMenuAdd());
			
		}
		return popup;
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
}  //  @jve:decl-index=0:visual-constraint="33,19"
