package agentgui.graphEnvironment.controller;

import gasmas.ontology.GridComponent;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.physical2Denvironment.display.BasicSVGGUI;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 * GUI class for EnvironmentController controlling grid simulation projects
 * @author Nils
 *
 */
public class GraphEnvironmentControllerGUI extends JSplitPane implements Observer, ActionListener {
	/**
	 * The currently selected SVG element
	 */
	private Element selectedElement = null;
	/**
	 * The selectedElement's style attribute before it was selected
	 */
	private String originalStyle;
	/**
	 * The style attribute used for highlighting selected elements
	 */
	private String selectionStyle = "fill:white;stroke:orange;stroke-width:3;stroke-miterlimit:4;stroke-dasharray:none";

	private static final long serialVersionUID = 1L;
	private JPanel pnlControlls = null;
	private JButton btnLoadGraph = null;
	private JButton btnComponentTypes = null;
	/**
	 * The component type selection dialog
	 */
	private ComponentTypeDialog compTypeDialog = null;
	
	private BasicSVGGUI svgGUI = null;
	/**
	 * The GraphEnvironmentController this GUI interacts with
	 */
	private GraphEnvironmentController controller = null;

	/**
	 * This is the default constructor
	 */
	public GraphEnvironmentControllerGUI(Project project) {
		super();
		initialize(project);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(Project project) {
		this.setSize(300, 200);

		this.setDividerLocation(150);
		this.setLeftComponent(getPnlControlls());
		this.setRightComponent(getSVGGUI());
		
		this.controller = new GraphEnvironmentController(project);
		this.controller.addObserver(this);
		if(this.controller.getSvgDoc() != null){
			this.setSVGDoc(controller.getSvgDoc());
		}
	}
	
	private BasicSVGGUI getSVGGUI(){
		if(svgGUI == null){
			svgGUI = new BasicSVGGUI();
		}
		return svgGUI;
	}

	/**
	 * This method initializes pnlControlls	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlControlls() {
		if (pnlControlls == null) {
			pnlControlls = new JPanel();
			pnlControlls.setLayout(null);
			pnlControlls.add(getBtnLoadGraph(), null);
			pnlControlls.add(getBtnComponentTypes(), null);
		}
		return pnlControlls;
	}

	/**
	 * This method initializes btnLoadGraph	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnLoadGraph() {
		if (btnLoadGraph == null) {
			btnLoadGraph = new JButton();
			btnLoadGraph.setText(Language.translate("Graph Laden"));
			btnLoadGraph.setLocation(new Point(10, 10));
			btnLoadGraph.setSize(new Dimension(120, 26));
			btnLoadGraph.addActionListener(this);
		}
		return btnLoadGraph;
	}
	
	private JButton getBtnComponentTypes(){
		if(btnComponentTypes == null){
			btnComponentTypes = new JButton();
			btnComponentTypes.setText(Language.translate("Komponenten"));
			btnComponentTypes.setLocation(new Point(10, 40));
			btnComponentTypes.setSize(new Dimension(120, 26));
			btnComponentTypes.addActionListener(this);
		}
		return btnComponentTypes;
	}

	/**
	 * This method initiates the compTypeDialog
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ComponentTypeDialog getCompTypeDialog(){
		if(compTypeDialog == null){
			Iterator<GridComponent> components = controller.getGridModel().getComponents().iterator();
			Vector<Vector<String>> rows = new Vector<Vector<String>>();
			
			while(components.hasNext()){
				GridComponent comp = components.next();
				
				Vector<String> compData = new Vector<String>();
				compData.add(comp.getId());
				compData.add(comp.getClass().getSimpleName());
				compData.add("Test");
				rows.add(compData);
				
			}
			
			compTypeDialog = new ComponentTypeDialog(rows, this);
		}
		return compTypeDialog;
	}

	/**
	 *  This method sets the GUIs SVG document.
	 * @param doc The SVG document 
	 */
	public void setSVGDoc(Document doc){
		addOnclickHandler(doc.getDocumentElement());
		this.svgGUI.setSVGDoc(doc);
	}
	
	/**
	 * This method adds onClick handler to a SVG element if it represents a graph node.
	 * An element represents a graph node if it is of type g and has an ID like "y.node.{number}"
	 * @param node A XML node 
	 */
	public void addOnclickHandler(Node node){
		String regex = "^y\\.node\\.\\d+$";		// Matches y.node.<nr>
		if(node instanceof Element 
				&& ((Element)node).getTagName().equals("g")
				&& ((Element)node).getAttributeNS(null, "id").matches(regex)
		){
			((EventTarget)node).addEventListener("click", new EventListener() {
				
				@Override
				public void handleEvent(Event evt) {
					String svgID = ((Element)evt.getCurrentTarget()).getAttributeNS(null, "id");
					String componentID = "n"+svgID.substring(svgID.lastIndexOf('.')+1);
					GridComponent selectedComponent = controller.getGridModel().getComponent(componentID);
					System.out.println(selectedComponent.getClass().getSimpleName());
					setSelectedElement((Element)evt.getTarget());
				}
			}, false);
		}
		if(node.hasChildNodes()){
			for(int i=0;i<node.getChildNodes().getLength();i++){
				addOnclickHandler(node.getChildNodes().item(i));
			}
		}
	}
	
	/**
	 * Sets the currently selected SVG element
	 * @param element The SVG element
	 */
	private void setSelectedElement(Element element){
		if(selectedElement != null){
			svgGUI.getCanvas().getUpdateManager().getUpdateRunnableQueue().invokeLater(new Runnable() {
				
				@Override
				public void run() {
					selectedElement.setAttributeNS(null, "style", originalStyle);
					
				}
			});
		}
		if(element != null){
			originalStyle = element.getAttributeNS(null, "style");
			this.selectedElement = element;
			svgGUI.getCanvas().getUpdateManager().getUpdateRunnableQueue().invokeLater(new Runnable() {
				
				@Override
				public void run() {
					selectedElement.setAttributeNS(null, "style", selectionStyle);
				}
			});
		}
	}
	
	/**
	 * This method finds the SVG element corresponding to the graph element with the given ID and selects it
	 * @param componentID The graph element's ID
	 */
	void setSelectedElementByComponentID(String componentID){
		int number = Integer.parseInt(componentID.substring(1));
		String svgElementID = "svg"+(number+1)+".NodeBackground";
		Element svgElement = svgGUI.getSVGDoc().getElementById(svgElementID);
		setSelectedElement(svgElement);
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o.equals(controller) && arg.equals(GraphEnvironmentController.EVENT_SVG_LOADED)){
			this.setSVGDoc(controller.getSvgDoc());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Loading a new graph
		if(e.getSource().equals(getBtnLoadGraph())){
			JFileChooser graphFC = new JFileChooser();
			graphFC.setFileFilter(new FileNameExtensionFilter(Language.translate("GraphML-Dateien"), "graphml"));
			if(graphFC.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				File graphMLFile = graphFC.getSelectedFile();
				this.controller.loadGridModel(graphMLFile);
			}
		// Show the compTypeDialog
		}else if(e.getSource().equals(getBtnComponentTypes())){
			getCompTypeDialog().setVisible(true);
		// Handle type changes
		}else if(e.getSource().equals(getCompTypeDialog().getCbTypeEditor())){
			System.out.println("Die Änderung des Komponententyps ist noch nicht implementiert");
		}
	}
	

}
