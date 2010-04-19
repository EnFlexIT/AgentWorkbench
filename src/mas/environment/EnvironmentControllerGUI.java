package mas.environment;

import javax.swing.JSplitPane;
import java.awt.Dimension;
import javax.swing.JPanel;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import sma.ontology.AbstractObject;
import sma.ontology.AgentObject;
import sma.ontology.PlaygroundObject;

import application.Language;
import application.Project;

import mas.display.BasicSVGGUI;
import mas.display.SvgTypes;

public class EnvironmentControllerGUI extends JSplitPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BasicSVGGUI svgGUI = null;
	private JSplitPane splitControlls = null;
	private JScrollPane scpTree = null;
	private JTree treeEnvironment = null;
	private JPanel pnlEnvironment = null;
	private JButton btnLoadSVG = null;
	private JTextField tfId = null;
	private JComboBox cbType = null;
	private JComboBox cbClass = null;
	private JButton btnApply = null;
	private JLabel lblId = null;
	private JLabel lblType = null;
	private JLabel lblClass = null;
	private JFileChooser fcLoadSVG = null;
		
	private Element selectedElement = null;
	/**
	 * Style attribute for marking selected elements
	 */
	private String selectionStyle = "stroke:orange;stroke-width:5px";
	/**
	 * Saving the original style for reconstruction when deselected
	 */
	private String originalStyle = null;
	
	private HashMap<String, String> agentClasses = null;
	
	private EnvironmentController controller = null;
	private JButton btnRemove = null;
	/**
	 * This method initializes 
	 * 
	 */
	public EnvironmentControllerGUI(Project project) {
		super();
		initialize(project);
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize(Project project) {
		this.controller = new EnvironmentController(project, this);
        this.setLeftComponent(getSplitControlls());
        this.setRightComponent(getSvgGUI());
        this.addComponentListener(new ComponentAdapter(){
        	public void componentResized(ComponentEvent ce){
        		setDividerLocation(200);
        		splitControlls.setDividerLocation(splitControlls.getHeight()-260);
        	}
        });
        
        if(controller.getEnvironment() != null && controller.getEnvironment().getRootPlayground() != null){
        	rebuildEnvironmentTree();
        }
			
	}

	/**
	 * This method initializes svgGUI	
	 * 	
	 * @return BasicSVGGUI	
	 */
	private BasicSVGGUI getSvgGUI() {
		if (svgGUI == null) {
			svgGUI = new BasicSVGGUI();
			svgGUI.getCanvas().setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		}
		return svgGUI;
	}
	
	/**
	 * This method sets the svgDoc
	 * @param doc SVG Document
	 */
	public void setSVGDoc(Document doc){
		addElementListeners(doc.getDocumentElement());
		this.getSvgGUI().getCanvas().setDocument(doc);		
	}
	
	public Document getSVGDoc(){
		return getSvgGUI().getCanvas().getSVGDocument();
	}

	/**
	 * This method initializes pnlControlls	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getSplitControlls() {
		if (splitControlls == null) {
			splitControlls = new JSplitPane();
			splitControlls.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitControlls.setTopComponent(getScpTree());
			splitControlls.setBottomComponent(getPnlEnvironment());
		}
		return splitControlls;
	}

	/**
	 * This method initializes scpTree	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScpTree() {
		if (scpTree == null) {
			scpTree = new JScrollPane();
			scpTree.setViewportView(getTreeEnvironment());
		}
		return scpTree;
	}

	/**
	 * This method initializes treeEnvironment	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getTreeEnvironment() {
		if (treeEnvironment == null) {
			treeEnvironment = new JTree();
			treeEnvironment.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(Language.translate("Keine Umgebung definiert"))));
			
			// Node selection
			treeEnvironment.addTreeSelectionListener(new TreeSelectionListener(){

				@Override
				public void valueChanged(TreeSelectionEvent arg0) {
					if(treeEnvironment.getLastSelectedPathComponent()!=null){
						String selection = treeEnvironment.getLastSelectedPathComponent().toString();
						setSelectedElement(svgGUI.getCanvas().getSVGDocument().getElementById(selection));
					}
				}
				
			});
			
			// Drag and Drop
			treeEnvironment.setDragEnabled(true);
			treeEnvironment.setDropMode(DropMode.USE_SELECTION);
			treeEnvironment.setDropTarget(new DropTarget(treeEnvironment, TransferHandler.MOVE, new DropTargetAdapter(){

				@Override
				public void drop(DropTargetDropEvent dtde) {
					
					
					DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) treeEnvironment.getSelectionPath().getLastPathComponent();
					
					// Get environment object to be moved
					String selectionId = selectionNode.toString();					
					AbstractObject selectionObject = controller.getObjectById(selectionId);
					if(selectionObject != null){
						// Get target TreePath 
						Point dropLocation = dtde.getLocation();
						TreePath targetPath = treeEnvironment.getClosestPathForLocation(dropLocation.x, dropLocation.y);
						DefaultMutableTreeNode targetNode = (DefaultMutableTreeNode) targetPath.getLastPathComponent();
						
						// Find the playground the target node belongs to
						boolean targetPgFound = false;
						AbstractObject targetObject = null;
						while(targetNode != null && !targetPgFound){
							targetObject = controller.getObjectById(targetNode.toString());
							if(targetObject != null && ObjectTypes.getType(targetObject) == ObjectTypes.PLAYGROUND){
								targetPgFound = true;
							}else{
								targetNode = (DefaultMutableTreeNode) targetNode.getParent();
							}
						}
						
						// Move the environment object to the target playground
						if(targetPgFound){							
							dtde.dropComplete(controller.moveObject(selectionObject, (PlaygroundObject) targetObject));							
							rebuildEnvironmentTree();
						}else{
							dtde.dropComplete(false);
						}
						setSelectedElement(null);
						
					}else{
						dtde.dropComplete(false);
					}
				}
			}));
		}
		return treeEnvironment;
	}
	
	/**
	 * Rebuilding the environment tree
	 */
	public void rebuildEnvironmentTree(){

		treeEnvironment.setModel(new DefaultTreeModel(getSubTree(controller.getEnvironment().getRootPlayground())));
		// Expand all nodes
		for(int row=0; row<treeEnvironment.getRowCount(); row++){
			treeEnvironment.expandRow(row);
		}	
	}
	
	
	/**
	 * Building the subtree for a playground
	 * @param pg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DefaultMutableTreeNode getSubTree(PlaygroundObject pg){
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(pg.getId());
		
		DefaultMutableTreeNode agents = new DefaultMutableTreeNode(Language.translate("Agenten"));
		DefaultMutableTreeNode obstacles = new DefaultMutableTreeNode(Language.translate("Hindernisse"));
		DefaultMutableTreeNode playgrounds = new DefaultMutableTreeNode("Playgrounds");
		
		Iterator<AbstractObject> children = pg.getAllChildObjects();
		while(children.hasNext()){
			AbstractObject childObject = children.next();
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childObject.getId());
			ObjectTypes type = ObjectTypes.getType(childObject);
			if(type != null){
				switch(type){
					case AGENT:
						agents.add(childNode);
					break;
					
					case OBSTACLE:
						obstacles.add(childNode);
					break;
					
					case PLAYGROUND:
						playgrounds.add(getSubTree((PlaygroundObject) childObject));
					break;
				}	
			}			
		}
		
		root.add(agents);
		root.add(obstacles);
		root.add(playgrounds);
		
		return root;
	}

	/**
	 * This method initializes pnlEnvironment	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlEnvironment() {
		if (pnlEnvironment == null) {
			pnlEnvironment = new JPanel();
			pnlEnvironment.setLayout(null);
			lblClass = new JLabel();
			lblClass.setText(Language.translate("Klasse"));
			lblClass.setSize(new Dimension(45, 16));
			lblClass.setLocation(new Point(10, 80));
			lblType = new JLabel();
			lblType.setText(Language.translate("Typ"));
			lblType.setSize(new Dimension(30, 16));
			lblType.setLocation(new Point(10, 40));
			lblId = new JLabel();
			lblId.setText("ID");
			lblId.setLocation(new Point(10, 10));
			lblId.setSize(new Dimension(15, 16));
			pnlEnvironment.add(lblId, null);
			pnlEnvironment.add(lblType, null);
			pnlEnvironment.add(lblClass, null);			
			pnlEnvironment.add(getBtnLoadSVG(), null);
			pnlEnvironment.add(getTfId());
			pnlEnvironment.add(getCbType());
			pnlEnvironment.add(getCbClass());
			pnlEnvironment.add(getBtnApply());
			pnlEnvironment.add(getBtnRemove());
			
		}
		return pnlEnvironment;
	}

	/**
	 * This method initializes btnLoadSVG	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnLoadSVG() {
		if (btnLoadSVG == null) {
			btnLoadSVG = new JButton();
			btnLoadSVG.setText(Language.translate("SVG zuweisen"));
			btnLoadSVG.setSize(new Dimension(150, 26));
			btnLoadSVG.setLocation(new Point(10, 180));
			btnLoadSVG.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(getFcLoadSVG().showOpenDialog(EnvironmentControllerGUI.this) == JFileChooser.APPROVE_OPTION){
						controller.setSVGFile(fcLoadSVG.getSelectedFile());
						
						rebuildEnvironmentTree();
					}					
				}
				
			});
		}
		return btnLoadSVG;
	}
	
	private JFileChooser getFcLoadSVG(){
		if(fcLoadSVG == null){
			fcLoadSVG = new JFileChooser();
			fcLoadSVG.setFileFilter(new FileNameExtensionFilter(Language.translate("SVG Dateien"), "svg"));
			fcLoadSVG.setCurrentDirectory(new File(controller.getCurrentProject().getProjectFolderFullPath()+"resources"));
		}
		return fcLoadSVG;
	}

	/**
	 * Adding onClick listeners to all relevant SVG elements
	 * @param root
	 */
	private void addElementListeners(Node root){
		if((root instanceof Element) 
				&& (SvgTypes.getType((Element) root) != null) 
				&& (((Element)root).getAttributeNS(null, "id") != "border")){
			((EventTarget) root).addEventListener("click", new EventListener(){
				public void handleEvent(Event arg0) {
					setSelectedElement((Element) arg0.getTarget());					
				}

								
			}, true);

		}
		
		if(root.hasChildNodes()){
			NodeList children = root.getChildNodes();
			for(int i=0; i<children.getLength(); i++){
				addElementListeners(children.item(i));
			}
		}
	}	

	/**
	 * This method initializes tfId	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfId() {
		if (tfId == null) {
			tfId = new JTextField();
			tfId.setSize(new Dimension(80, 22));
			tfId.setLocation(new Point(70, 10));
		}
		return tfId;
	}

	/**
	 * This method initializes cbType	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbType() {
		if (cbType == null) {
			cbType = new JComboBox();
			cbType.setSize(new Dimension(100, 25));
			cbType.setLocation(new Point(70, 38));
			cbType.setEnabled(false);
			Vector<String> types = new Vector<String>();
			types.add(Language.translate("Keiner"));
			for(ObjectTypes type : ObjectTypes.values()){
				types.add(type.toString());
			}
			cbType.setModel(new DefaultComboBoxModel(types));
			cbType.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(ObjectTypes.getType(cbType.getSelectedItem().toString()) != null){
						btnApply.setEnabled(true);						
					}else{
						btnApply.setEnabled(false);						
					}
					
					if(cbType.getSelectedItem().equals("AGENT")){
						cbClass.setEnabled(true);
					}else{
						cbClass.setEnabled(false);
					}
				}
				
			});
		}
		
		return cbType;
	}

	/**
	 * This method initializes cbClass	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbClass() {
		if (cbClass == null) {
			cbClass = new JComboBox();
			cbClass.setLocation(new Point(70, 73));
			cbClass.setSize(new Dimension(100, 25));
			cbClass.setEnabled(false);
			Vector<Class<?>> classes = controller.getCurrentProject().getProjectAgents();
			Vector<String> names = new Vector<String>();
			
			this.agentClasses = new HashMap<String, String>();
			
			for(int i=0; i<classes.size(); i++){
				names.add(classes.get(i).getSimpleName());
				this.agentClasses.put(classes.get(i).getSimpleName(), classes.get(i).getName());
				
			}
			cbClass.setModel(new DefaultComboBoxModel(names));
		}
		return cbClass;
	}

	/**
	 * Collecting all important input values for creating a new environment object 
	 * @return HashMap<String, String>
	 */
	public HashMap<String, String> getObjectSettings(){
		HashMap<String, String> settings = new HashMap<String, String>();
		settings.put("id", tfId.getText());
		settings.put("type", cbType.getSelectedItem().toString());
		if(cbClass.isEnabled()){
			settings.put("class", agentClasses.get(cbClass.getSelectedItem().toString()));			
		}
		
		return settings;
	}

	/**
	 * Setting the values of all controls when an object/element is selected 
	 */
	public void setObjectSettings(){
		String id = null;
		AbstractObject object = null;
		if(selectedElement != null){
			id = selectedElement.getAttributeNS(null, "id");
			object = controller.getObjectHash().get(id);
			cbType.setEnabled(true);
		}else{
			cbType.setEnabled(false);
		}
		tfId.setText(id);
		if(object != null){
			ObjectTypes type = ObjectTypes.getType(object);
			cbType.setSelectedItem(type.toString());
			if(type == ObjectTypes.AGENT){
				cbClass.setSelectedItem(((AgentObject)object).getAgentClass());
			}
			btnRemove.setEnabled(true);
		}else{
			cbType.setSelectedItem(Language.translate("Keiner"));
			btnRemove.setEnabled(false);
		}
	}

	/**
	 * This method initializes btnApply	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnApply() {
		if (btnApply == null) {
			btnApply = new JButton();
			btnApply.setText(Language.translate("Anwenden"));
			btnApply.setSize(new Dimension(150, 26));
			btnApply.setLocation(new Point(10, 110));
			btnApply.setEnabled(false);
			btnApply.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					controller.createObject(selectedElement, getObjectSettings());
					setSelectedElement(null);
				}
				
			});
		}
		return btnApply;
	}
	
	/**
	 * This method initializes btnRemove	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnRemove() {
		if (btnRemove == null) {
			btnRemove = new JButton();
			btnRemove.setLocation(new Point(10, 145));
			btnRemove.setText(Language.translate("Objekt entfernen"));
			btnRemove.setEnabled(false);
			btnRemove.setSize(new Dimension(150, 26));
			btnRemove.addActionListener(new ActionListener(){
	
				@Override
				public void actionPerformed(ActionEvent arg0) {
					controller.deleteObject(selectedElement.getAttributeNS(null, "id"), false);
					setSelectedElement(null);
				}
				
			});
		}
		return btnRemove;
	}

	/**
	 * Sets the selected element
	 * @param element
	 */
	public void setSelectedElement(Element element){
		UpdateManager um = this.svgGUI.getCanvas().getUpdateManager();
		
		um.getUpdateRunnableQueue().invokeLater(new ElementSelector(element));
	}	
	
	/**
	 * Helper class for changing elements via update manager
	 * @author Nils
	 *
	 */
	private class ElementSelector implements Runnable{
		
		private Element element = null;
		
		private ElementSelector(Element element){
			this.element = element;
		}

		@Override
		public void run() {
			setSelectedElement(element);			
		}
		
		/**
		 * Sets and highlights the selected svg element
		 * @param element
		 */
		private void setSelectedElement(Element element){
			// Reset the previously selected element
			if(selectedElement!=null){
				selectedElement.setAttributeNS(null, "style", originalStyle);
			}
			
			
			if(element!=null){
				String fill = "";
				originalStyle = element.getAttributeNS(null, "style");
				
				// Keep fill color if defined via style attribute
				if(originalStyle != null){
					String[] parts = originalStyle.split(";");
					for(int i=0; i<parts.length; i++){
						if(parts[i].contains("fill:")){
							fill = parts[i];
						}
					}
				}
				
				// Highlight selected element
				String newStyle=fill+";"+selectionStyle;
				element.setAttributeNS(null, "style", newStyle);
			}
			
			// Set selected element and input values
			selectedElement = element;			
			setObjectSettings();
		}
		
	}

}
