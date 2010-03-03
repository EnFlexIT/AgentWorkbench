package mas.environment;

import javax.swing.JSplitPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.w3c.dom.events.EventListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import mas.display.BasicSvgGUI;


import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

import application.Language;
import application.Project;
import javax.swing.JTextField;

/**
 * Umgebungssetup im Projektfenster, zur Bearbeitung der Umgebung
 * @author Nils
 *
 */
public class EnvironmentControllerGUI extends JSplitPane {
	
	

	private static final long serialVersionUID = 1L;
	private JSVGCanvas canvas = null;
		
	private JSplitPane splitControlls = null;
	private JPanel pnlTop = null;
	private JButton btnOpen = null;
	private JTree treeEnvironment = null;
	private JScrollPane scrollEnvironment = null;
	private JPanel pnlBottom = null;
	private JComboBox cbType = null;
	private JComboBox cbClass = null;
	private JButton btnApply = null;
	private JFileChooser fcOpen = null;
	private JFileChooser fcSave = null;
		
	private Project currentProject = null;	
	private Element selectedElement = null;
	
	private EnvironmentController ec = null;

	private JTextField tfId = null;

	private JLabel lblType = null;

	private JLabel lblAgentClass = null;

	private JLabel lblId = null;

	private JButton btnDelete = null;

	private JLabel lblSettings = null;

	private JButton btnSave = null;
	
	private HashMap<String, String> agentClasses = null;

	/**
	 * This is the default constructor
	 */
	public EnvironmentControllerGUI(Project project) {
		super();
		this.currentProject = project;
		this.ec = new EnvironmentController(this, currentProject);

				
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(500, 300);
		this.setRightComponent(getSplitControlls());
		this.canvas = getCanvas();
		this.setLeftComponent(new BasicSvgGUI(canvas));
		this.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent ce){
				EnvironmentControllerGUI.this.setDividerLocation(EnvironmentControllerGUI.this.getWidth()-220);
				splitControlls.setDividerLocation(EnvironmentControllerGUI.this.getHeight()-250);				
			}
		});
	}

	

	/**
	 * This method initializes splitControlls	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getSplitControlls() {
		if (splitControlls == null) {
			splitControlls = new JSplitPane();
			splitControlls.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitControlls.setTopComponent(getPnlTop());
			splitControlls.setBottomComponent(getPnlBottom());
			splitControlls.setDividerLocation(50);
		}
		return splitControlls;
	}

	/**
	 * This method initializes pnlTop	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlTop() {
		if (pnlTop == null) {
			pnlTop = new JPanel();
			pnlTop.setLayout(new BorderLayout());
			pnlTop.add(getBtnOpen(), BorderLayout.NORTH);
			pnlTop.add(getScrollEnvironment(), BorderLayout.CENTER);
		}
		return pnlTop;
	}

	/**
	 * This method initializes btnOpen	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnOpen() {
		if (btnOpen == null) {
			btnOpen = new JButton();
			btnOpen.setText(Language.translate("SVG-Datei zuweisen"));
			btnOpen.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(EnvironmentControllerGUI.this.getFcOpen().showOpenDialog(EnvironmentControllerGUI.this)==JFileChooser.APPROVE_OPTION){
						ec.setNewEnv(true);
						ec.loadSvgFile(fcOpen.getSelectedFile());
						// Speichere Dateiname im Projekt
						currentProject.setSvgFile(fcOpen.getSelectedFile().getName());
						currentProject.ProjectUnsaved=true;
					}					
				}				
			});
		}
		return btnOpen;
	}

	/**
	 * This method initializes treeEnvironment	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getTreeEnvironment() {
		if (treeEnvironment == null) {
			treeEnvironment = new JTree();
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(Language.translate("Kein SVG zugewiesen"));
			treeEnvironment.setModel(new DefaultTreeModel(root));
			treeEnvironment.setEnabled(false);
			treeEnvironment.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			treeEnvironment.addTreeSelectionListener(new TreeSelectionListener(){

				@Override
				public void valueChanged(TreeSelectionEvent arg0) {
					if(treeEnvironment.getLastSelectedPathComponent()!=null){
						String selection = treeEnvironment.getLastSelectedPathComponent().toString();
						BasicObject object = ec.getMainPlayground().getObjects().get(treeEnvironment.getLastSelectedPathComponent().toString());
						if(object!=null){
							
							UpdateManager um = canvas.getUpdateManager();
							if(um != null){
								um.getUpdateRunnableQueue().invokeLater(new ElementSelector(canvas.getSVGDocument().getElementById(selection)));
							}
						}
						canvas.paint(canvas.getGraphics());
					}
				}
				
			});
		}
		return treeEnvironment;
	}
	
	private JScrollPane getScrollEnvironment(){
		if(scrollEnvironment == null){
			scrollEnvironment = new JScrollPane(getTreeEnvironment());			
		}
		return scrollEnvironment;
		
	}
	

	/**
	 * This method initializes pnlBottom	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlBottom() {
		if (pnlBottom == null) {
			lblSettings = new JLabel();
			lblSettings.setText(Language.translate("Objekt-Eigenschaften"));
			lblSettings.setSize(new Dimension(122, 16));
			lblSettings.setLocation(new Point(5, 5));
			lblId = new JLabel();
			lblId.setText(Language.translate("Objekt-ID"));
			lblId.setSize(new Dimension(52, 16));
			lblId.setLocation(new Point(5, 35));
			lblAgentClass = new JLabel();
			lblAgentClass.setText(Language.translate("Agentenklasse"));
			lblAgentClass.setSize(new Dimension(85, 16));
			lblAgentClass.setLocation(new Point(5, 105));
			lblType = new JLabel();
			lblType.setText(Language.translate("Objekt-Typ"));
			lblType.setSize(new Dimension(64, 16));
			lblType.setLocation(new Point(5, 70));
			pnlBottom = new JPanel();
			pnlBottom.setLayout(null);
			pnlBottom.add(getCbType(), null);
			pnlBottom.add(getCbClass(), null);
			pnlBottom.add(getBtnApply(), null);
			pnlBottom.add(getTfId(), null);
			pnlBottom.add(lblType, null);
			pnlBottom.add(lblAgentClass, null);
			pnlBottom.add(lblId, null);
			pnlBottom.add(getBtnRemove(), null);
			pnlBottom.add(lblSettings, null);			
			pnlBottom.add(getBtnSave(), null);
		}
		return pnlBottom;
	}

	/**
	 * This method initializes cbType	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbType() {
		if (cbType == null) {
			cbType = new JComboBox();
			String[] types = {Language.translate("Undefiniert"), Language.translate("Agent"), Language.translate("Hindernis")};
			cbType.setModel(new DefaultComboBoxModel(types));
			cbType.setLocation(new Point(100, 62));
			cbType.setSize(new Dimension(100, 25));
			cbType.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(cbType.getSelectedItem().equals(Language.translate("Agent"))){
						cbClass.setEnabled(true);
					}else{
						cbClass.setEnabled(false);
					}
					if(cbType.getSelectedItem().equals(Language.translate("Undefiniert"))){
						btnApply.setEnabled(false);
					}else{
						btnApply.setEnabled(true);
					}
					
				}
				
			});
		}
		return cbType;
	}

	/**
	 * This method initializes cbAgentClass	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbClass() {
		if (cbClass == null) {
			cbClass = new JComboBox();
			cbClass.setVisible(true);
			cbClass.setSize(new Dimension(100, 25));
			cbClass.setLocation(new Point(100, 97));
			
			agentClasses = new HashMap<String, String>(); 
			
			Vector<Class<?>> classes = currentProject.getProjectAgents();
			Vector<String> names = new Vector<String>();		
			for (int i =0; i<classes.size();i++) {
				String fullName = classes.get(i).getName();
				String shortName = fullName.substring(fullName.lastIndexOf('.')+1);
				names.add(shortName);
				agentClasses.put(shortName, fullName);				
			}
			cbClass.setModel(new DefaultComboBoxModel(names));
			cbClass.setEnabled(false);
		}
		return cbClass;
	}

	/**
	 * This method initializes btnSet	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnApply() {
		if (btnApply == null) {
			btnApply = new JButton();
			btnApply.setText(Language.translate("Objekt übernehmen"));
			btnApply.setSize(new Dimension(150, 26));
			btnApply.setLocation(new Point(10, 135));
			btnApply.addActionListener(new ActionListener(){

				
				@Override
				/**
				 * Erzeugt zu dem ausgewählten SVG Element ein Umgebungsobjekt und fügt es zum Haupt-Playground hinzu
				 */
				public void actionPerformed(ActionEvent e) {
					String id = tfId.getText();
					if(id.length()>0){
						selectedElement.setAttributeNS(null, "id", id);
					}else{
						id = selectedElement.getAttributeNS(null, "id");
					}
					
					Playground pg = ec.getMainPlayground();
					// Falls für diese ID schon ein Objekt definiert war, vorher löschen
					if(pg.getObjects().get(id)!=null){
						pg.removeElement(id);
					}
					if(cbType.getSelectedItem().equals(Language.translate("Hindernis"))){
						ObstacleObject newObject = new ObstacleObject(id, selectedElement);
						System.out.println(Language.translate("Neues Hindernis")+" "+id);
						System.out.println(Language.translate("Position")+" "+newObject.getPosX()+":"+newObject.getPosY());
						System.out.println(Language.translate("Größe")+" "+newObject.getWidth()+"x"+newObject.getHeight());
						pg.addObstacle(newObject);
					}else if(cbType.getSelectedItem().equals(Language.translate("Agent"))){
						AgentObject newAgent = new AgentObject(id, selectedElement, agentClasses.get(cbClass.getSelectedItem().toString()));
						System.out.println(Language.translate("Neuer Agent")+" "+id);
						System.out.println(Language.translate("Position")+" "+newAgent.getPosX()+":"+newAgent.getPosY());
						System.out.println(Language.translate("Größe")+" "+newAgent.getWidth()+"x"+newAgent.getHeight());
						pg.addAgent(newAgent);
					}
					// Aktualisiere Umgebungsbaum
					rebuildEnvironmentTree();
					
					UpdateManager um = canvas.getUpdateManager();
					if(um != null){
						um.getUpdateRunnableQueue().invokeLater(new ElementSelector(null));
					}
				}
				
			});
			
		}
		return btnApply;
	}
	
	/**
	 * This method initializes canvas	
	 * 	
	 * @return javax.swing.JFileChooser	
	 */
	private JFileChooser getFcOpen(){
		if(fcOpen == null){
			fcOpen = new JFileChooser();
			fcOpen.setFileFilter(new FileNameExtensionFilter(Language.translate("SVG Files"), "svg"));
			fcOpen.setCurrentDirectory(new File(currentProject.getProjectFolderFullPath()+"/ressources"));
		}
		return fcOpen;
	}
	
	private JFileChooser getFcSave(){
		if(fcSave == null){
			fcSave = new JFileChooser();
			fcSave.setFileFilter(new FileNameExtensionFilter(Language.translate("XML Files"), "xml"));
			fcSave.setCurrentDirectory(new File(currentProject.getProjectFolderFullPath()+"/ressources"));
		}
		return fcSave;
	}
	
	

	/**
	 * This method initializes canvas	
	 * 	
	 * @return org.apache.batik.swing.JSVGCanvas	
	 */
	public JSVGCanvas getCanvas(){
		if(canvas == null){
			canvas = new JSVGCanvas();
			canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
			
			// EventListener wird ausgelöst, wenn die SVG-Datei vollständig geladen ist
			canvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter(){
				// Wird aufgerufen, wenn das SVG vollständig geladen ist
				public void documentLoadingCompleted(SVGDocumentLoaderEvent e){
					ec.setSvgDoc(canvas.getSVGDocument());
					// Erzeugt onClick-Listener für die SVG-Elemente
					addElementListeners(canvas.getSVGDocument().getDocumentElement());
					
					// Wenn neue Umgebung, erzeuge mainPlayground aus SVG root
					if(ec.isNewEnv()){
						ec.setMainPlayground  (new Playground(canvas.getSVGDocument().getDocumentElement()));
					}
					// Initialisiert den Baum					
					rebuildEnvironmentTree();
					treeEnvironment.setEnabled(true);
				};
			});
		}
		return canvas;
	}
	
	/**
	 * Erzeugt onClick-Listener für das übergebene Element und seine Kindelemente, falls sinnvoll  
	 * @param root 
	 */
	private void addElementListeners(Node root){
		// Element-Typen, die Listener bekommen 
		String tags[] = {"rect", "circle", "ellipse", "path"};
		if(Arrays.asList(tags).contains(root.getNodeName())){
			((EventTarget) root).addEventListener("click", new EventListener(){

				@Override
				public void handleEvent(Event arg0) {
					UpdateManager um = canvas.getUpdateManager();
					if(um != null){
						um.getUpdateRunnableQueue().invokeLater(new ElementSelector((Element)arg0.getTarget()));
					}
				}
				
			}, false);			
		}
		if(root.hasChildNodes()){
			NodeList children = root.getChildNodes();
			for(int i=0; i<children.getLength(); i++){
				addElementListeners(children.item(i));
			}
		}
	}
	
	/**
	 * Baut einen Teilbaum für einen Playground auf
	 * @param pg Der zu verarbeitende PlayGround
	 * @return Teilbaum für den übergebenen Playground 
	 */
	private DefaultMutableTreeNode buildSubTree(Playground pg){
		String id = pg.getId();
		if( (id == null) || (id.length())==0){
			id="Playground";
		}
		// Playground Root
		DefaultMutableTreeNode pgRoot = new DefaultMutableTreeNode(id);
		// Äste für die Objektarten
		DefaultMutableTreeNode objectsRoot = new DefaultMutableTreeNode(Language.translate("Hindernisse"));
		DefaultMutableTreeNode agentsRoot = new DefaultMutableTreeNode(Language.translate("Agenten"));
		DefaultMutableTreeNode playgroundsRoot = new DefaultMutableTreeNode(Language.translate("Kind-Umgebungen"));
		
		if(pg!=null){
			// Objekte werden ausgelesen und je nach Klasse in den Teilbaum eingehängt
			Iterator<BasicObject> objects = pg.getObjects().values().iterator();
			while(objects.hasNext()){
				BasicObject object = objects.next();
				if(object instanceof Playground){
					playgroundsRoot.add(buildSubTree((Playground) object));				
				}else if(object instanceof AgentObject){
					agentsRoot.add(new DefaultMutableTreeNode(object.getId()));
				}else if(object instanceof ObstacleObject){
					objectsRoot.add(new DefaultMutableTreeNode(object.getId()));
				}
				
			}
		}
		
		pgRoot.add(agentsRoot);
		pgRoot.add(objectsRoot);
		pgRoot.add(playgroundsRoot);		
		return pgRoot;
	}
	
	/**
	 * Baut das TreeModel der Umgebung neu auf
	 */
	private void rebuildEnvironmentTree(){
		// Erzeuge Baum für den mainPlayground
		treeEnvironment.setModel(new DefaultTreeModel(buildSubTree(ec.getMainPlayground())));
		// Expandiere alle Teilbäume
		for(int row=0; row<treeEnvironment.getRowCount(); row++){
			treeEnvironment.expandRow(row);
		}		
		treeEnvironment.paint(treeEnvironment.getGraphics());
	}

	/**
	 * This method initializes tfId	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfId() {
		if (tfId == null) {
			tfId = new JTextField();
			tfId.setLocation(new Point(100, 32));
			tfId.setText("");
			tfId.setPreferredSize(new Dimension(4, 20));
			tfId.setSize(new Dimension(100, 25));
		}
		return tfId;
	}

	/**
	 * This method initializes btnDelete	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnRemove() {
		if (btnDelete == null) {
			btnDelete = new JButton();
			btnDelete.setText(Language.translate("Objekt löschen"));
			btnDelete.setSize(new Dimension(150, 26));
			btnDelete.setEnabled(false);
			btnDelete.setLocation(new Point(10, 170));
			btnDelete.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					ec.getMainPlayground().removeElement(tfId.getText());		// Element aus Umgenung entfernen
					rebuildEnvironmentTree();
					
					UpdateManager um = canvas.getUpdateManager();
					if(um != null){
						um.getUpdateRunnableQueue().invokeLater(new ElementSelector(null));
					}
				}
				
			});
		}
		return btnDelete;
	}
	
	/**
	 * Setzt die Werte der GUI-Elemente  abhängig von selectedElement
	 */	
	private void setInputValues(){
		// Kein Element ausgewählt
		if(selectedElement == null){
			tfId.setText(null);
			cbType.setSelectedItem(Language.translate("Undefiniert"));
			cbClass.setEnabled(false);
			btnDelete.setEnabled(false);
		}else{
			String id = selectedElement.getAttributeNS(null, "id");
			tfId.setText(id);
			// Umgebungsobjekt, das von selectedElement repräsentiert wird
			BasicObject object = ec.getMainPlayground().getObjects().get(id);
			if(object != null){
				if(object instanceof ObstacleObject){
					cbType.setSelectedItem(Language.translate("Hindernis"));
					cbClass.setEnabled(false);
					btnDelete.setEnabled(true);
				}else if(object instanceof AgentObject){
					cbType.setSelectedItem(Language.translate("Agent"));
					cbClass.setSelectedItem(((AgentObject) object).getAgentClass());
					cbClass.setEnabled(true);
					btnDelete.setEnabled(true);
				}else{
					// Anderer Objekttyp, sollte nicht vorkommen
					cbType.setSelectedItem(Language.translate("Undefiniert"));
					cbClass.setEnabled(false);
					btnDelete.setEnabled(true);
				}
			}else{
				// Kein Umgebungsobjekt zugeordnet
				cbType.setSelectedItem(Language.translate("Undefiniert"));
				cbClass.setEnabled(false);
				btnDelete.setEnabled(false);
			}
		}
	}
	
	/**
	 * This method initializes btnSave	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnSave() {
		if (btnSave == null) {
			btnSave = new JButton();
			btnSave.setLocation(new Point(10, 205));
			btnSave.setText(Language.translate("Umgebung speichern"));
			btnSave.setSize(new Dimension(150, 26));
			btnSave.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					File envFile = null;
					if(getFcSave().showSaveDialog(EnvironmentControllerGUI.this) == JFileChooser.APPROVE_OPTION){
						envFile = getFcSave().getSelectedFile();
					}
					ec.saveEnvironment(envFile);
					currentProject.setEnvFile(envFile.getName());
					
				}
				
			});
		}
		return btnSave;
	}
	
	/**
	 * Macht die Funktion des ElementSelectors von Außen verfügbar 
	 * @param element
	 */
	public void setSelectedElement(Element element){
		UpdateManager um = canvas.getUpdateManager();
		if(um != null){
			um.getUpdateRunnableQueue().invokeLater(new ElementSelector(element));
		}
	}
	
	/**
	 * Hilfsklasse, nötig um setSelectedElement über den UpdateManager-Thread ausführen zu lassen
	 * @author Nils
	 *
	 */
	class ElementSelector implements Runnable{
		/**
		 * Ausgewähltes SVG-Element
		 */
		private Element element;

		
		public ElementSelector(Element element){
			this.element = element;
		}

		@Override
		public void run() {
			setSelectedElement(element);			
		}
		
		/**
		 * Setzt und markiert das aktuell ausgewählte SVG-Element 
		 * @param element
		 */
		private void setSelectedElement(Element element){
			
			// Entferne Markierung des vorherigen Elements
			if(selectedElement!=null){
				selectedElement.setAttributeNS(null, "stroke", "none");
			}
			selectedElement = element;			
			setInputValues();
			
			if(selectedElement!=null){
				selectedElement.setAttributeNS(null, "stroke", "black");
				selectedElement.setAttributeNS(null, "stroke-width", "5px");
			}
			
		}
		
	}
	
	
}
