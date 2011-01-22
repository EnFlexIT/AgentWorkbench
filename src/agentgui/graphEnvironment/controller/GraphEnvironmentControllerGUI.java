package agentgui.graphEnvironment.controller;

import java.util.Observable;
import java.util.Observer;

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

	private static final long serialVersionUID = 1L;
	private JPanel pnlControlls = null;
	private JButton btnLoadGraph = null;
	
	private BasicSVGGUI svgGUI = null;
	private GraphEnvironmentController controller = null;

	@Override
	public void update(Observable o, Object arg) {
		if(o.equals(controller) && arg.equals(GraphEnvironmentController.EVENT_SVG_LOADED)){
			this.setSVGDoc(controller.getSvgDoc());
		}
	}

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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(getBtnLoadGraph())){
			JFileChooser graphFC = new JFileChooser();
			graphFC.setFileFilter(new FileNameExtensionFilter(Language.translate("GraphML-Dateien"), "graphml"));
			if(graphFC.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				File graphMLFile = graphFC.getSelectedFile();
				this.controller.loadGridModel(graphMLFile);
			}
		}
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
		String regex = "^y\\.node\\.\\d+$";
		if(node instanceof Element 
				&& ((Element)node).getTagName().equals("g")
				&& ((Element)node).getAttributeNS(null, "id").matches(regex)
		){
			((EventTarget)node).addEventListener("click", new EventListener() {
				
				@Override
				public void handleEvent(Event evt) {
					System.out.println("SVG-Element "+((Element)evt.getTarget()).getAttributeNS(null, "id"));
					
				}
			}, true);
		}
		if(node.hasChildNodes()){
			for(int i=0;i<node.getChildNodes().getLength();i++){
				addOnclickHandler(node.getChildNodes().item(i));
			}
		}
	}
	
	

}
