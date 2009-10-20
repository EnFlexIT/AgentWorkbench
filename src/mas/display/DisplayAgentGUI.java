package mas.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.script.Window;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
/**
 * GUI for DisplayAgent
 * @author nils
 *
 */
public class DisplayAgentGUI extends JPanel {
	
	// SVG Namespace
	public static final String svgNs="http://www.w3.org/2000/svg";
	
	// DisplayAgent controlling this GUI
	DisplayAgent myAgent = null;
	
	// Swing stuff
	JPanel zoomPanel = null;
	JButton btnZoomIn = null;
	JButton btnZoomOut = null;
	JSVGCanvas canvas = null;
	
	// SVG stuff
	Document svgDoc = null;
	Element svgRoot = null;
	Window window = null;
	
	public DisplayAgentGUI(DisplayAgent myAgent){
		this.myAgent=myAgent;
		this.initialize();
	}
	
	private void initialize(){
		// Initialize canvas and SVG root
		this.canvas = new JSVGCanvas();
		this.canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		this.canvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter(){
			public void gvtRenderingCompleted(GVTTreeRendererEvent re) {
				// Animation initialization
				window = canvas.getUpdateManager().getScriptingEnvironment().createWindow();
				window.setInterval(new Animation(), 50);				
			}
		});
		this.svgDoc = SVGDOMImplementation.getDOMImplementation().createDocument(svgNs, "svg", null);
		this.svgRoot = svgDoc.getDocumentElement();
		this.canvas.setDocument(svgDoc);
		
		// Initialize buttons and panel
		this.setLayout(new BorderLayout());
		this.zoomPanel = new JPanel();
		this.zoomPanel.setLayout(new FlowLayout());
		this.btnZoomIn = new JButton("+");
		this.btnZoomIn.addActionListener(canvas.new ZoomAction(1.2));
		this.btnZoomOut = new JButton("-");
		this.btnZoomOut.addActionListener(canvas.new ZoomAction(0.8));
		this.zoomPanel.add(btnZoomIn);
		this.zoomPanel.add(btnZoomOut);
		
		this.add("North", zoomPanel);
		this.add(canvas);
		
		// Temporary solution, to be integrated in Christian's GUI
		JFrame frame = new JFrame("DA GUI Test");
//		frame.addComponentListener(new ComponentAdapter(){
//			public void componentResized(ComponentEvent ce){
//				canvas.getSVGDocument().getDocumentElement().setAttributeNS(null, "x", ""+canvas.getWidth());
//				canvas.getSVGDocument().getDocumentElement().setAttributeNS(null, "y", ""+canvas.getHeight());
//				System.out.println(((JFrame)ce.getSource()).getSize());
//				System.out.println(DisplayAgentGUI.this.getSize());
//				System.out.println(canvas.getSize());
//				System.out.println(canvas.getSVGDocumentSize());				
//			}
//		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(this);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Add an agents SVG representation to the SVG document 
	 * @param element SVG element representing the agent
	 */
	public void addAgent(Element element){
		this.svgRoot.appendChild(element);		
	}
	
	/**
	 * Remove an agents SVG representation from the SVG document
	 * @param name Agents local name (= the SVG elements id attribute)
	 */
	public void removeAgent(String name){
		this.svgRoot.removeChild(svgDoc.getElementById(name));		
	}	
	
	// Controlling periodic display updates
	private class Animation implements Runnable{

		@Override
		public void run() {
			Map <String, AnimAgent> agents = myAgent.animAgents;
			if(agents!=null){
				Iterator<String> keys=agents.keySet().iterator();
				while(keys.hasNext()){
					AnimAgent agent=agents.get(keys.next());		
					Element agentSVG = svgDoc.getElementById(agent.getId());
					if(agentSVG!=null){
						agentSVG.getAttributeNodeNS(null, "x").setValue(""+agent.getXPos());
						agentSVG.getAttributeNodeNS(null, "y").setValue(""+agent.getYPos());
					}else{
						System.out.println("SVG representation of "+agent.getId()+" not found");
					}
				}
			}			
		}		
	}
}
