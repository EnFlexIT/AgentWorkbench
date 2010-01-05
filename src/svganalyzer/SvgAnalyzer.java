package svganalyzer;

import javax.swing.JSplitPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTree;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
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
import mas.environment.AgentObject;
import mas.environment.BasicObject;
import mas.environment.Playground;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;


import application.Project;
import javax.swing.JTextField;

/**
 * Umgebungssetup im Projektfenster, zur Bearbeitung der Umgebung
 * @author Nils
 *
 */
public class SvgAnalyzer extends JSplitPane {
	
	private static String svgNs = SVGDOMImplementation.SVG_NAMESPACE_URI;

	private static final long serialVersionUID = 1L;
	private JSVGCanvas canvas = null;
		
	private JSplitPane splitControlls = null;
	private JPanel pnlTop = null;
	private JButton btnOpen = null;
	private JTree treeEnvironment = null;
	private JPanel pnlBottom = null;
	private JComboBox cbType = null;
	private JComboBox cbClass = null;
	private JButton btnApply = null;
	private JFileChooser fcOpen = null;
	private Document svgDoc = null;
	private Project currentProject = null;
	
	
	private Playground mainPlayground = null;
	private Element selectedElement = null;
	
	

	private JTextField tfId = null;

	private JLabel lblType = null;

	private JLabel lblAgentClass = null;

	private JLabel lblId = null;

	private JButton btnDelete = null;

	private JLabel lblSettings = null;
	/**
	 * This is the default constructor
	 */
	public SvgAnalyzer(Project project) {
		super();
		this.currentProject = project;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(500, 300);
		this.setDividerLocation(250);
		this.setRightComponent(getSplitControlls());
		this.canvas = getCanvas();
		this.setLeftComponent(new BasicSvgGUI(canvas));
		this.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent ce){
				SvgAnalyzer.this.setDividerLocation(SvgAnalyzer.this.getWidth()-200);
				splitControlls.setDividerLocation(0.5);				
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
			splitControlls.setDividerLocation(100);
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
			pnlTop.add(getTreeEnvironment(), BorderLayout.CENTER);
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
			btnOpen.setText("Open SVG file");
			btnOpen.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(SvgAnalyzer.this.getFcOpen().showOpenDialog(SvgAnalyzer.this)==JFileChooser.APPROVE_OPTION){
						try {
							File file = fcOpen.getSelectedFile();
							svgDoc = SVGDOMImplementation.getDOMImplementation().createDocument(svgNs, "svg", null);
							canvas.setURI(file.toURI().toURL().toString());
							
						} catch (MalformedURLException e) {							
							e.printStackTrace();
						}
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
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("Playground");
			root.add(new DefaultMutableTreeNode("Agents"));
			root.add(new DefaultMutableTreeNode("Objects"));
			root.add(new DefaultMutableTreeNode("Sub-Playgrounds"));
			treeEnvironment.setModel(new DefaultTreeModel(root));
			treeEnvironment.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			treeEnvironment.addTreeSelectionListener(new TreeSelectionListener(){

				@Override
				public void valueChanged(TreeSelectionEvent arg0) {
					if(treeEnvironment.getLastSelectedPathComponent()!=null){
						String selection = treeEnvironment.getLastSelectedPathComponent().toString();
						setSelectedObject(selection);
						SvgAnalyzer.this.setSelectedElement(canvas.getSVGDocument().getElementById(selection));
						canvas.paint(canvas.getGraphics());
					}
				}
				
			});
		}
		return treeEnvironment;
	}	
	

	/**
	 * This method initializes pnlBottom	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlBottom() {
		if (pnlBottom == null) {
			lblSettings = new JLabel();
			lblSettings.setText("Object Settings");
			lblSettings.setSize(new Dimension(87, 16));
			lblSettings.setLocation(new Point(9, 5));
			lblId = new JLabel();
			lblId.setText("Object ID");
			lblId.setSize(new Dimension(51, 16));
			lblId.setLocation(new Point(11, 35));
			lblAgentClass = new JLabel();
			lblAgentClass.setText("Agent class");
			lblAgentClass.setSize(new Dimension(67, 16));
			lblAgentClass.setLocation(new Point(5, 113));
			lblType = new JLabel();
			lblType.setText("Object type");
			lblType.setSize(new Dimension(64, 16));
			lblType.setLocation(new Point(4, 68));
			pnlBottom = new JPanel();
			pnlBottom.setLayout(null);
			pnlBottom.add(getCbType(), null);
			pnlBottom.add(getCbClass(), null);
			pnlBottom.add(getBtnApply(), null);
			pnlBottom.add(getTfId(), null);
			pnlBottom.add(lblType, null);
			pnlBottom.add(lblAgentClass, null);
			pnlBottom.add(lblId, null);
			pnlBottom.add(getBtnDelete(), null);
			pnlBottom.add(lblSettings, null);
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
			String[] types = {"Not specified", "Agent", "Object"};
			cbType.setModel(new DefaultComboBoxModel(types));
			cbType.setLocation(new Point(90, 62));
			cbType.setSize(new Dimension(100, 25));
			cbType.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(cbType.getSelectedItem().equals("Agent")){
						cbClass.setEnabled(true);
					}else{
						cbClass.setEnabled(false);
					}
					if(cbType.getSelectedItem().equals("Not specified")){
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
			cbClass.setLocation(new Point(90, 104));
			
			Vector<Class<?>> agentClasses = currentProject.getProjectAgents();
			Vector<String> agentNames = new Vector<String>();		
			for (int i =0; i<agentClasses.size();i++) {
				String name = agentClasses.get(i).getName();
				name = name.substring(name.lastIndexOf('.')+1);
				agentNames.add(name);
				
			}
			cbClass.setModel(new DefaultComboBoxModel(agentNames));
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
			btnApply.setText("Apply");
			btnApply.setSize(new Dimension(65, 26));
			btnApply.setLocation(new Point(9, 149));
			btnApply.addActionListener(new ActionListener(){

				
				@Override
				/**
				 * Erzeugt zu dem ausgew�hlten SVG Element ein Umgebungsobjekt und f�gt es zum Haupt-Playground hinzu
				 */
				public void actionPerformed(ActionEvent e) {
					String id = tfId.getText();
					if(id.length()>0){
						selectedElement.setAttributeNS(null, "id", id);
					}else{
						id = selectedElement.getAttributeNS(null, "id");
					}
					if(cbType.getSelectedItem().equals("Object")){
						BasicObject newObject = new BasicObject(id, selectedElement);
						System.out.println("Neues Objekt "+id);
						System.out.println("Position "+newObject.getX()+":"+newObject.getY());
						System.out.println("Gr��e "+newObject.getWidth()+"x"+newObject.getHeight());
						mainPlayground.addObject(newObject);					
					}else if(cbType.getSelectedItem().equals("Agent")){
						AgentObject newAgent = new AgentObject(id, selectedElement, cbClass.getSelectedItem().toString());
						System.out.println("Neuer Agent "+id);
						System.out.println("Position "+newAgent.getX()+":"+newAgent.getY());
						System.out.println("Gr��e "+newAgent.getWidth()+"x"+newAgent.getHeight());
						mainPlayground.addAgent(newAgent);						
					}
					// Aktualisiere Umgebungsbaum
					treeEnvironment.setModel(new DefaultTreeModel(buildPlaygroundTree(mainPlayground)));
					treeEnvironment.paint(treeEnvironment.getGraphics());
					setSelectedObject(id);
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
			fcOpen.setFileFilter(new FileNameExtensionFilter("SVG Files", "svg"));
			fcOpen.setCurrentDirectory(new File(currentProject.getProjectFolderFullPath()+"/ressources"));
		}
		return fcOpen;
	}

	/**
	 * This method initializes canvas	
	 * 	
	 * @return org.apache.batik.swing.JSVGCanvas	
	 */
	private JSVGCanvas getCanvas(){
		if(canvas == null){
			canvas = new JSVGCanvas();
			canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
			canvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter(){
				// Wird aufgerufen, wenn das SVG vollst�ndig geladen ist
				public void documentLoadingCompleted(SVGDocumentLoaderEvent e){
					svgDoc = canvas.getSVGDocument();
					// Erzeugt den Haupt-Playground aus dem SVG Root Objekt 
					mainPlayground = new Playground(svgDoc.getDocumentElement());
					// Erzeugt onClick-Listener f�r die SVG-Elemente
					addElementListeners(svgDoc.getDocumentElement());
				};
			});
		}
		return canvas;
	}
	
	/**
	 * Erzeugt onClick-Listener f�r das �bergebene Element und seine Kindelemente, falls sinnvoll  
	 * @param root 
	 */
	private void addElementListeners(Node root){
		// Element-Typen, die Listener bekommen 
		String tags[] = {"rect", "circle", "ellipse", "path"};
		if(Arrays.asList(tags).contains(root.getNodeName())){
			((EventTarget) root).addEventListener("click", new EventListener(){

				@Override
				public void handleEvent(Event arg0) {
					// Markiert das Element
					setSelectedElement((Element)arg0.getTarget());
					
					// �berpr�fung, ob zu dem SVG-Element schon ein Umgebungsobjekt existiert
					// und passende Belegung der GUI-Elemente
					String id = selectedElement.getAttributeNS(null, "id");
					if(mainPlayground.getObjects().get(id) != null){						
						setSelectedObject(selectedElement.getAttributeNS(null, "id"));
					}else{						
						tfId.setText(id);
						cbType.setSelectedItem("Not specified");
						cbClass.setEnabled(false);
						btnDelete.setEnabled(false);
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
	 * Analysiert den �bergebenen Playground und erzeugt eine Baumstruktur der enthaltenen Objekte
	 * @param pg Der zu verarbeitende PlayGround
	 * @return Teilbaum mit den Objekten des PlayGround 
	 */
	private DefaultMutableTreeNode buildPlaygroundTree(Playground pg){
		String id = pg.getId();
		if(id.length()==0){
			id="Playground";
		}
		// Playground Root
		DefaultMutableTreeNode pgRoot = new DefaultMutableTreeNode(id);
		// �ste f�r die Objektarten
		DefaultMutableTreeNode objectsRoot = new DefaultMutableTreeNode("Objects");
		DefaultMutableTreeNode agentsRoot = new DefaultMutableTreeNode("Agents");
		DefaultMutableTreeNode playgroundsRoot = new DefaultMutableTreeNode("Sub-Playgrounds");
		
		// Objekte werden ausgelesen und je nach Klasse in den Teilbaum eingeh�ngt
		Iterator<BasicObject> objects = pg.getObjects().values().iterator();
		while(objects.hasNext()){
			BasicObject object = objects.next();
			if(object instanceof Playground){
				playgroundsRoot.add(buildPlaygroundTree((Playground) object));				
			}else if(object instanceof AgentObject){
				agentsRoot.add(new DefaultMutableTreeNode(object.getId()));
			}else{
				objectsRoot.add(new DefaultMutableTreeNode(object.getId()));
			}
			
		}
		
		pgRoot.add(agentsRoot);
		pgRoot.add(objectsRoot);
		pgRoot.add(playgroundsRoot);
		
		return pgRoot;
	}

	/**
	 * This method initializes tfId	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfId() {
		if (tfId == null) {
			tfId = new JTextField();
			tfId.setLocation(new Point(90, 32));
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
	private JButton getBtnDelete() {
		if (btnDelete == null) {
			btnDelete = new JButton();
			btnDelete.setText("Remove");
			btnDelete.setSize(new Dimension(80, 26));
			btnDelete.setEnabled(false);
			btnDelete.setLocation(new Point(93, 150));
			btnDelete.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					mainPlayground.removeElement(tfId.getText());
					treeEnvironment.setModel(new DefaultTreeModel(buildPlaygroundTree(mainPlayground)));
				}
				
			});
		}
		return btnDelete;
	}
	
	/**
	 * Setzt bei Auswahl eines Umgebungsobjektes den Status der GUI-Elemente passend 
	 * @param id
	 */
	private void setSelectedObject(String id){
		BasicObject object = mainPlayground.getObjects().get(id);
		if(object != null){
			tfId.setText(object.getId());
			if(object instanceof AgentObject){
				cbType.setSelectedItem("Agent");
				cbClass.setSelectedItem(((AgentObject) object).getAgentClass());
				cbClass.setEnabled(true);
			}else{
				cbType.setSelectedItem("Object");
				cbClass.setEnabled(false);
			}
			btnDelete.setEnabled(true);
		}
	}
	
	/**
	 * Setzt und markiert das aktuell ausgew�hlte SVG-Element 
	 * @param element
	 */
	private void setSelectedElement(Element element){
		// Entferne Markierung des vorherigen Elements
		if(selectedElement!=null){
			selectedElement.setAttributeNS(null, "stroke", "none");
		}
		selectedElement = element;
				
		selectedElement.setAttributeNS(null, "stroke", "black");
		selectedElement.setAttributeNS(null, "stroke-width", "5px");
	}


}
