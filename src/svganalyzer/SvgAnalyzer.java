package svganalyzer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import application.Project;
import java.awt.Dimension;

public class SvgAnalyzer extends JSplitPane implements ActionListener{
	
	public static String svgNs=SVGDOMImplementation.SVG_NAMESPACE_URI;
	
	/**
	 * Types of objects a SVG element can be associated with
	 */
	private String[] objectTypes = {"Playground", "Agent", "Static"};
	
	
	
	private Project currentProject = null;
		
	private JSVGCanvas canvas = null;
	private JSplitPane controllsPane = null;
	private JPanel canvasPanel = null;			// Contains canvas and view control
	private JPanel zoomPanel = null;			// Contains zoom buttons
	private JPanel elementsPanel = null;		// Contains open button and DOM tree
	private JPanel settingsPanel = null;		// Contains everything related to mapping SVG elements to objects 
	private JTree tree = null;	
	private JButton btnOpen = null;
	private JButton btnZoomIn = null;
	private JButton btnZoomOut = null;
	private JButton btnSet = null;
	private JLabel lblSettings = null;
	private JLabel lblElement = null;
	private JLabel lblType = null;
	private JLabel lblClass = null;
	private JComboBox cbElement = null;
	private JComboBox cbType = null;
	private JComboBox cbAgentClass = null;	
	
	private JFileChooser fcOpen = null;
	
	private Document svgDoc = null;
	private Element selectedElement = null;
	
	private Vector<String> elementIds = null;

	

	
	
	public SvgAnalyzer(Project currentProject){
		super(JSplitPane.HORIZONTAL_SPLIT);
		this.currentProject = currentProject;
		initialize();
	}
	
	/**
	 * Initializing JSplitpane
	 */
	private void initialize(){
		this.setSize(new Dimension(600, 400));
		this.setDividerLocation(300);
		canvas = new JSVGCanvas();
		canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		canvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter(){
			public void documentLoadingCompleted(SVGDocumentLoaderEvent e){
				svgDoc = canvas.getSVGDocument();
				elementIds = new Vector<String>();
				
				DefaultMutableTreeNode root = buildTree(svgDoc.getDocumentElement());
				tree.setModel(new DefaultTreeModel(root));
				setComboBoxModels();
				
				setElementControlsEnabled(true);
				
			}
		});
		
		zoomPanel = new JPanel();
		zoomPanel.setLayout(new FlowLayout());
		btnZoomIn = new JButton("+");
		btnZoomIn.addActionListener(canvas.new ZoomAction(1.2));
		btnZoomOut = new JButton("-");
		btnZoomOut.addActionListener(canvas.new ZoomAction(0.8));
		zoomPanel.add(btnZoomIn);
		zoomPanel.add(btnZoomOut);
		
		canvasPanel = new JPanel();
		canvasPanel.setLayout(new BorderLayout());
		canvasPanel.add("North", zoomPanel);
		canvasPanel.add("Center", canvas);
		
		
		
		
		btnOpen = new JButton ("Open SVG file");
		btnOpen.addActionListener(this);
				
		
		
		tree = new JTree(new DefaultMutableTreeNode("No SVG loaded"));
		tree.addTreeSelectionListener(new TreeSelectionListener(){
			
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				String selectedItem = tree.getLastSelectedPathComponent().toString();
				String id = selectedItem.split(" ")[1];
				setSelectedElement(svgDoc.getElementById(id),tree);				
			}
			
		});
		
		
		elementsPanel = new JPanel();
		elementsPanel.setLayout(new BorderLayout());
		elementsPanel.add("North", btnOpen);
		elementsPanel.add(tree);
		
		settingsPanel = getSettingsPanel();
		
		
		controllsPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		controllsPane.setTopComponent(elementsPanel);
		controllsPane.setBottomComponent(settingsPanel);
		controllsPane.setDividerLocation(0.5);
		
		this.setLeftComponent(canvasPanel);
		this.setRightComponent(controllsPane);		
				
		this.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent ce){
				SvgAnalyzer.this.setDividerLocation(SvgAnalyzer.this.getWidth()-250);
				controllsPane.setDividerLocation(controllsPane.getHeight()-300);				
			}
		});
	}
	
	private JPanel getSettingsPanel(){
		if(settingsPanel == null){
			settingsPanel = new JPanel();
			settingsPanel.setLayout(null);
			
			lblClass = new JLabel();
			lblClass.setText("Agent Class");
			lblClass.setSize(new Dimension(68, 16));
			lblClass.setLocation(new Point(15, 120));
			settingsPanel.add(lblClass);
			lblType = new JLabel();
			lblType.setText("Type");
			lblType.setSize(new Dimension(27, 16));
			lblType.setLocation(new Point(15, 85));
			settingsPanel.add(lblType);
			lblElement = new JLabel();
			lblElement.setText("Element");
			lblElement.setSize(new Dimension(46, 16));
			lblElement.setLocation(new Point(15, 50));
			settingsPanel.add(lblElement);
			lblSettings = new JLabel();
			lblSettings.setText("Element Settings");
			lblSettings.setSize(new Dimension(96, 16));
			lblSettings.setLocation(new Point(15, 16));
			settingsPanel.add(lblSettings);
			
			cbElement = new JComboBox();
			cbElement.setLocation(new Point(120, 45));
			cbElement.setSize(new Dimension(100, 25));
			cbElement.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					String selectedItem = cbElement.getSelectedItem().toString();
					String id = selectedItem.split(" ")[1];
					setSelectedElement(svgDoc.getElementById(id), cbElement);
					
				}
				
			});
			settingsPanel.add(cbElement);
			cbType = new JComboBox();
			cbType.setSize(new Dimension(100, 25));
			cbType.setLocation(new Point(120, 80));
			cbType.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(cbType.getSelectedItem().equals("Agent")){
						cbAgentClass.setEnabled(true);
					}else{
						cbAgentClass.setEnabled(false);
					}
				}
				
			});
			settingsPanel.add(cbType);
			cbAgentClass = new JComboBox();
			cbAgentClass.setSize(new Dimension(100, 25));
			cbAgentClass.setLocation(new Point(120, 115));
			settingsPanel.add(cbAgentClass);
			
			btnSet = new JButton("Set");
			btnSet.setSize(53, 26);
			btnSet.setLocation(170, 260);
			btnSet.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(selectedElement!=null){
						
					}
					
				}
				
			});
			settingsPanel.add(btnSet);
			
			setElementControlsEnabled(false);
			
		}
		return settingsPanel;
	}
	
	private void setComboBoxModels(){
		cbElement.setModel(new DefaultComboBoxModel(elementIds));
		cbType.setModel(new DefaultComboBoxModel(objectTypes));
		
		Vector<Class<?>> agentClasses = currentProject.getProjectAgents();
		Vector<String> agentNames = new Vector<String>();		
		for (int i =0; i<agentClasses.size();i++) {
			String name = agentClasses.get(i).getName();
			name = name.substring(name.lastIndexOf('.')+1);
			agentNames.add(name);
			
		}
		cbAgentClass.setModel(new DefaultComboBoxModel(agentNames));		
	}
	
	private void setElementControlsEnabled(boolean enabled){
		cbElement.setEnabled(enabled);
		cbType.setEnabled(enabled);
		if(enabled && cbType.getSelectedItem().equals("Agent")){
			cbAgentClass.setEnabled(true);
		}else{
			cbAgentClass.setEnabled(false);
		}		
	}
	
	/**
	 * Building a JTree representation of the documents DOM tree via depth first search
	 * @param element DOM element to be added to the tree
	 * @return Root element of the built subtree starting from the given element 
	 */
	private DefaultMutableTreeNode buildTree(Element element){
		String type = element.getNodeName();	// SVG element name
		String id = element.getAttributeNS(null, "id");		// Id attribute
//		System.out.println("Analyzing "+type+" "+id);
		DefaultMutableTreeNode self = null;
//		if(Arrays.asList(relevantElementTypes).contains(type)){
			self=new DefaultMutableTreeNode(type+" "+id);
			elementIds.add(type+" "+id);
			if(element.hasChildNodes()){
				NodeList children = element.getChildNodes();
				for(int i=0;i<children.getLength();i++){
					Node child = children.item(i);
					if(child instanceof Element){
						self.add(buildTree((Element) child));
					}
				}
			}
//		}
		
		if((type!="svg")&&(type!="text")&&(type!="g")){	// If non-root element
			EventTarget et = (EventTarget) element;
			et.addEventListener("click", new EventListener(){

				@Override
				public void handleEvent(Event arg0) {					
					Element target = (Element) arg0.getTarget();
					setSelectedElement(target, target);					
				}
				
			}, false);			
		}
		
		return self;
	}
	


	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource()==btnOpen){
			if(fcOpen==null){
				fcOpen = new JFileChooser();
				fcOpen.setFileFilter(new FileNameExtensionFilter("SVG Files", "svg"));
				fcOpen.setCurrentDirectory(new File(currentProject.getProjectFolderFullPath()+"/ressources"));
			}
			if(fcOpen.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
				try {
					File file = fcOpen.getSelectedFile();
					canvas.setURI(file.toURI().toURL().toString());					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}		
	}
	
	private void setSelectedElement(Element selection, Object source){
		selectedElement = selection;		
	}
}
