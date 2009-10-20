package svganalyzer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class SvgAnalyzer extends JSplitPane implements ActionListener{
	
	public static String svgNs="http://www.w3.org/2000/svg";	// SVG Namespace
	
	private JSVGCanvas canvas = null;
	private JScrollPane canvasPane = null;
	private JPanel panel=null;		// Containing open button and DOM tree
	private JPanel zoomPanel = null;	// Containing zoom buttons
	private JTree tree = null;
	private JButton btnOpen = null;
	private JButton btnZoomIn = null;
	private JButton btnZoomOut = null;
	private JFileChooser fcOpen = null;
	
	private Document svgDoc = null;
	
	public SvgAnalyzer(){
		super(JSplitPane.HORIZONTAL_SPLIT);
		initialize();
	}
	
	/**
	 * Initializing JSplitpane
	 */
	private void initialize(){
		canvas = new JSVGCanvas();
		canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		canvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter(){
			public void documentLoadingCompleted(SVGDocumentLoaderEvent e){
				svgDoc = canvas.getSVGDocument();
				DefaultMutableTreeNode root = buildTree(svgDoc.getDocumentElement());
				tree.setModel(new DefaultTreeModel(root));				
			}
		});
		
		this.setLeftComponent(canvas);
		
		
		btnOpen = new JButton ("Open SVG file");
		btnOpen.addActionListener(this);
				
		zoomPanel = new JPanel();
		zoomPanel.setLayout(new FlowLayout());
		btnZoomIn = new JButton("+");
		btnZoomIn.addActionListener(canvas.new ZoomAction(1.2));
		btnZoomOut = new JButton("-");
		btnZoomOut.addActionListener(canvas.new ZoomAction(0.8));
		zoomPanel.add(btnZoomIn);
		zoomPanel.add(btnZoomOut);
		
		tree = new JTree(new DefaultMutableTreeNode("No SVG loaded"));
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add("North", btnOpen);
		panel.add(tree);
		panel.add("South", zoomPanel);
		this.setRightComponent(panel);
		
		this.setSize(650, 500);
		this.setDividerLocation(this.getWidth()-200);
		
		System.out.println(this.getSize());
		System.out.println(this.getDividerLocation());
	}
	
	/**
	 * Building a JTree representation of the documents DOM tree via depth first search
	 * @param element DOM element to be added to the tree
	 * @return Root element of the built subtree starting from the given element 
	 */
	private DefaultMutableTreeNode buildTree(Element element){
		String type = element.getNodeName();	// SVG element name
		String id = element.getAttributeNS(null, "id");		// Id attribute
		System.out.println("Analyzing "+type+" "+id);
		
		DefaultMutableTreeNode self=new DefaultMutableTreeNode(type+" "+id);
		if(element.hasChildNodes()){
			NodeList children = element.getChildNodes();
			for(int i=0;i<children.getLength();i++){
				Node child = children.item(i);
				if(child instanceof Element){
					self.add(buildTree((Element) child));
				}
			}
		}
		
		if((type!="svg")&&(type!="text")){	// If non-root element
			EventTarget et = (EventTarget) element;
			et.addEventListener("click", new OnClickAction(), false);
//			addLabel(element, id);			
		}
		
		return self;
	}
	
//	private void addLabel(Element element, String text){
//		Element textElement = svgDoc.createElementNS(svgNs, "text");
//		Text textContent = svgDoc.createTextNode(text);
//		textElement.setAttributeNS(null, "font-size", "24");
//		textElement.setAttributeNS(null, "x", element.getAttributeNS(null, "x"));
//		textElement.setAttributeNS(null, "y", element.getAttributeNS(null, "y"));
//		textElement.appendChild(textContent);
//		svgDoc.getDocumentElement().appendChild(textElement);
//	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource()==btnOpen){
			if(fcOpen==null){
				fcOpen = new JFileChooser();			
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
	
	private class OnClickAction implements EventListener{

		@Override
		public void handleEvent(Event evt) {
			Element target = (Element) evt.getTarget();
			JOptionPane.showMessageDialog(SvgAnalyzer.this, target.getAttribute("id"));		
		}
		
	}
}
