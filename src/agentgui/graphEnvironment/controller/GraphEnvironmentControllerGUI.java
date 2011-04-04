package agentgui.graphEnvironment.controller;

import javax.swing.JFileChooser;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.environment.EnvironmentPanel;
import javax.swing.JLabel;

public class GraphEnvironmentControllerGUI extends EnvironmentPanel implements Observer, ActionListener, ListSelectionListener{

	private static final long serialVersionUID = 1L;
	private JPanel pnlControlls = null;
	private JScrollPane scpComponentTable = null;
	private JTable tblComponents = null;
	private JButton btnLoadGraph = null;
	private JButton btnSetClasses = null;
	
	private ClassSelectorDialog classSelectorDialog = null;  //  @jve:decl-index=0:visual-constraint="333,23"
	private GraphEnvironmentController controller = null;
	
	private JLabel lblTable = null;
	private JSplitPane jSplitPaneRoot = null;
	
	private BasicGraphGUI graphGUI = null;
	
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
	 * This method builds the tblComponents' contents based on the controllers GridModel 
	 * @return The grid components' IDs and class names
	 */
	private Vector<Vector<String>> getComponentTableContents(){
		Vector<Vector<String>> componentVector = new Vector<Vector<String>>();
		
		if(controller.getGridModel() != null){
			
			// Get the components from the controllers GridModel
			Iterator<GridComponent> components = controller.getGridModel().getComponents().iterator();
			
			// Add component ID and class name to the data vector
			while(components.hasNext()){
				GridComponent comp = components.next();
				
				Vector<String> compData = new Vector<String>();
				compData.add(comp.getAgentID());
				compData.add(comp.getType());
				compData.add("Test");
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
			btnSetClasses.setText(Language.translate("Klassenzuordnung"));
			btnSetClasses.addActionListener(this);
		}
		return btnSetClasses;
	}
	
	private ClassSelectorDialog getClassSelectorDialog(){
		if(classSelectorDialog == null){
			classSelectorDialog = new ClassSelectorDialog(this, controller.getAgentClasses());
		}
		return classSelectorDialog;
	}
	
	private BasicGraphGUI getGraphGUI(){
		if(graphGUI == null){
			graphGUI = new BasicGraphGUI(this);
			if(controller.getGridModel() != null && controller.getGridModel().getGraph() != null){
				graphGUI.setGraph(controller.getGridModel().getGraph());
			}
		}
		return graphGUI;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(o.equals(controller) && arg.equals(GraphEnvironmentController.EVENT_GRIDMODEL_CHANGED)){
			graphGUI.setGraph(controller.getGridModel().getGraph());
			rebuildTblComponents();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(getBtnImportGraph())){
			JFileChooser graphFC = new JFileChooser();
			graphFC.setFileFilter(new FileNameExtensionFilter(Language.translate(controller.getGraphFileImporter().getTypeString()), controller.getGraphFileImporter().getGraphFileExtension()));
			if(graphFC.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				File graphMLFile = graphFC.getSelectedFile();
				this.controller.importGridModel(graphMLFile);
			}
		}else if(event.getSource().equals(getBtnSetClasses())){
			getClassSelectorDialog().setVisible(true);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(getTblComponents().getSelectedRowCount() > 0){
			String componentID = (String) tblComponents.getModel().getValueAt(e.getFirstIndex(), 0);
			GridComponent selectedComponent = controller.getGridModel().getComponent(componentID);
//			graphGUI.setPickedObject(selectedComponent);
			new ComponentSettingsDialog(controller.getProject(), this, selectedComponent).setVisible(true);
		}
	}
	
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
	
	public void showComponentSettingsDialog(Object component){
		if(component instanceof PropagationPoint){
			new ComponentSettingsDialog(currProject, this, (PropagationPoint)component).setVisible(true);
		}else if(component instanceof GridComponent){
			new ComponentSettingsDialog(currProject, this, (GridComponent)component).setVisible(true);
			
		}
	}
	
	void selectComponent(Object component){
		if(getTblComponents().getSelectedRow() == -1){
		}
	}
	
	void componentSettingsChanged(){
		graphGUI.clearPickedObjects();
		getTblComponents().getSelectionModel().clearSelection();
	}
	
	void componentSettingsChangeAborted(){
		graphGUI.clearPickedObjects();
		getTblComponents().getSelectionModel().clearSelection();
	}
}  //  @jve:decl-index=0:visual-constraint="33,19"
