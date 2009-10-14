package svganalyzer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
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
import org.w3c.dom.svg.SVGDocument;

public class SvgAnalyzer extends JSplitPane implements ActionListener{
	
	public static String svgNs="http://www.w3.org/2000/svg";	// SVG Namespace
	
	JFrame parent;
	private JSVGCanvas canvas = null;
	private JPanel panel=null;		// Containing open button and DOM tree 
	private JTree tree = null;
	private JButton btnOpen = null;
	private JFileChooser fcOpen = null;
	
	private Document svgDoc = null;
	
	private DefaultMutableTreeNode root;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame ("SvgAnalyzer");
		SvgAnalyzer svga = new SvgAnalyzer();
		svga.parent=frame;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(svga);
		frame.pack();
		frame.setVisible(true);		
	}
	
	public SvgAnalyzer(){
		super(JSplitPane.HORIZONTAL_SPLIT);
		canvas = new JSVGCanvas();
		canvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter(){
			public void documentLoadingCompleted(SVGDocumentLoaderEvent e){
				DefaultMutableTreeNode root = buildTree(canvas.getSVGDocument().getDocumentElement());
				tree.setModel(new DefaultTreeModel(root));				
			}
		});
		
		this.setLeftComponent(canvas);
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		btnOpen = new JButton ("Open SVG file");
		btnOpen.addActionListener(this);
		panel.add("North", btnOpen);
		
		tree = new JTree(new DefaultMutableTreeNode("No SVG loaded"));
		panel.add(tree);
		this.setRightComponent(panel);		
	}
	
	private DefaultMutableTreeNode buildTree(Element element){
		String description = element.getNodeName();
		description+=" "+element.getAttributeNS(null, "id");
		System.out.println("Analyzing "+description);
		DefaultMutableTreeNode self=new DefaultMutableTreeNode(description);
		if(element.hasChildNodes()){
			NodeList children = element.getChildNodes();
			for(int i=0;i<children.getLength();i++){
				Node child = children.item(i);
				if(child instanceof Element){
					self.add(buildTree((Element) child));
				}
			}
		}
		return self;
	}	

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
}
