package mas.environment;

import javax.swing.JSplitPane;
import java.awt.Dimension;
import javax.swing.JPanel;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
import sma.ontology.PlaygroundObject;
import sma.ontology.Position;
import sma.ontology.Scale;
import sma.ontology.Size;

import application.Language;
import application.Project;

import mas.display.BasicSVGGUI;
import mas.display.SvgTypes;
import mas.environment.guiComponents.PnlObject;

import java.awt.Rectangle;

public class EnvironmentControllerGUI extends JSplitPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BasicSVGGUI svgGUI = null;
	private JSplitPane splitControlls = null;
	private JScrollPane scpTree = null;
	private JTree treeEnvironment = null;
	private JPanel pnlEnvSettings = null;
	private JButton btnLoadSVG = null;

	private JFileChooser fcLoadSVG = null;
	private JTabbedPane tpSettings;
	private JLabel lblScale = null;
	private JTextField tfRwu = null;
	private JTextField tfPx = null;
	private JComboBox cbUnit = null;
	private JLabel lblPx = null;
	private JButton btnSetScale = null;
		
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
	
	private PnlObject objectSettings;
	
	// Initialization of global stuff
	
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
        		splitControlls.setDividerLocation(splitControlls.getHeight()-350);
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
	public BasicSVGGUI getSvgGUI() {
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
	
	// Initialization of the controlls section
	
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
			splitControlls.setBottomComponent(getTpSettings());
		}
		return splitControlls;
	}
	
	// Environment tree related stuff

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
	
	private JTabbedPane getTpSettings(){
		if(tpSettings == null){
			tpSettings = new JTabbedPane();
			tpSettings.addTab(Language.translate("Umgebung"), getPnlEnvSettings());
			this.objectSettings = new PnlObject(this);
			tpSettings.addTab(Language.translate("Objekt"), this.objectSettings);
		}
		return tpSettings;
	}
	
	// Environment settings tab

	private JPanel getPnlEnvSettings(){
		if(pnlEnvSettings == null){
			lblPx = new JLabel();
			lblPx.setBounds(new Rectangle(70, 80, 38, 16));
			lblPx.setText("Pixel");
			lblScale = new JLabel();
			lblScale.setText(Language.translate("Maßstab"));
			lblScale.setSize(new Dimension(50, 16));
			lblScale.setLocation(new Point(10, 10));
			pnlEnvSettings = new JPanel();
			pnlEnvSettings.setLayout(null);
			pnlEnvSettings.add(lblScale, null);
			pnlEnvSettings.add(getTfRwu(), null);
			pnlEnvSettings.add(getTfPx(), null);
			pnlEnvSettings.add(getCbUnit(), null);
			pnlEnvSettings.add(lblPx, null);
			pnlEnvSettings.add(getBtnSetScale(), null);
			pnlEnvSettings.add(getBtnLoadSVG(), null);
		}
		return pnlEnvSettings;
	}

	/**
	 * This method initializes tfRwu	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfRwu() {
		if (tfRwu == null) {
			tfRwu = new JTextField();
			tfRwu.setLocation(new Point(10, 40));
			tfRwu.setSize(new Dimension(50, 25));			
		}
		return tfRwu;
	}

	/**
	 * This method initializes cbUnit	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbUnit() {
		if (cbUnit == null) {
			cbUnit = new JComboBox();
			cbUnit.setSize(90, 30);
			cbUnit.setLocation(new Point(70,40));
			String[] units = {"m", "cm", "mm", "inch", "feet"};
			cbUnit.setModel(new DefaultComboBoxModel(units));			
		}
		return cbUnit;
	}

	/**
	 * This method initializes tfPx	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfPx() {
		if (tfPx == null) {
			tfPx = new JTextField();
			tfPx.setLocation(new Point(10, 75));
			tfPx.setSize(new Dimension(50, 25));			
		}
		return tfPx;
	}

	private JButton getBtnSetScale(){
		if(btnSetScale == null){
			btnSetScale = new JButton();
			btnSetScale.setText(Language.translate("Maßstab festlegen"));
			btnSetScale.setSize(new Dimension(150, 26));
			btnSetScale.setLocation(new Point(10,115));
			btnSetScale.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					Scale scale = new Scale();
					boolean error = false;
					
					try{
						scale.setValue(Float.parseFloat(tfRwu.getText().replace(',', '.')));
					}catch(NumberFormatException ex){
						System.err.println(Language.translate("Ungültige Eingabe, ausschließlich zahlenwerte zulässig!"));
						error=true;
					}					
					scale.setUnit(cbUnit.getSelectedItem().toString());
					try{
						scale.setPixel(Float.parseFloat(tfPx.getText().replace(',', '.')));
					}catch(NumberFormatException ex){
						System.err.println(Language.translate("Ungültige Eingabe, ausschließlich zahlenwerte zulässig!"));
						error=true;
					}
					if(!error){
						controller.setScale(scale);
					}
				}
				
			});
		}
		return btnSetScale;
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
			btnLoadSVG.setLocation(new Point(10, 150));
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
	
	// Sets the scale inputs after the scale has been changed from
	void setScale(Scale scale){
		getTfRwu().setText(""+scale.getValue());
		getCbUnit().setSelectedItem(scale.getUnit());
		getTfPx().setText(""+scale.getPixel());
	}
	
	// Object settings tab

	

	
	
	public void setAgentClasses(HashMap<String, String> agentClasses) {
		this.agentClasses = agentClasses;
	}

	public HashMap<String, String> getAgentClasses() {
		return agentClasses;
	}

	public EnvironmentController getController() {
		return controller;
	}

	public Element getSelectedElement() {
		return selectedElement;
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
	 * Sets the selected element
	 * @param element
	 */
	public void setSelectedElement(Element element){
		tpSettings.setSelectedIndex(1);
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
			objectSettings.setObjectSettings();
		}
		
	}
	
	public class ElementChanger implements Runnable{
		
		Position pos;
		Size size;
		
		public ElementChanger(Position pos, Size size){
			this.pos = pos;
			this.size = size;
		}

		@Override
		public void run() {
			
			Scale scale = controller.getEnvironment().getScale();
			
			float x = pos.getX();
			float y = pos.getY();
			float width = size.getWidth();
			float height = size.getHeight();
			
			x = OntoUtilities.calcPixel(x, scale);
			y = OntoUtilities.calcPixel(y, scale);
			width = OntoUtilities.calcPixel(width, scale);
			height = OntoUtilities.calcPixel(height, scale);
			
			switch(SvgTypes.getType(selectedElement)){
				case RECT:
				case IMAGE:
					selectedElement.setAttributeNS(null, "x", ""+x);
					selectedElement.setAttributeNS(null, "y", ""+y);
					selectedElement.setAttributeNS(null, "width", ""+width);
					selectedElement.setAttributeNS(null, "height", ""+height);
				break;
					
				case CIRCLE:
					selectedElement.setAttributeNS(null, "cx", ""+(x+width/2));
					selectedElement.setAttributeNS(null, "cy", ""+(y+height/2));
					selectedElement.setAttributeNS(null, "r", ""+(width/2));
				break;
					
				case ELLIPSE:
					selectedElement.setAttributeNS(null, "cx", ""+(x+width/2));
					selectedElement.setAttributeNS(null, "cy", ""+(y+height/2));
					selectedElement.setAttributeNS(null, "r1", ""+(width/2));
					selectedElement.setAttributeNS(null, "r2", ""+(height/2));
				break;				
			}
		}
		
	}

}
