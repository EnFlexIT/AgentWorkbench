package mas.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.script.Window;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import svganalyzer.SvgAnalyzer;
/**
 * GUI for DisplayAgent
 * @author nils
 *
 */
public class DisplayAgentGUI extends JFrame {
	
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
	
	Map <String, AnimAgent> knownAgents;
	
	public DisplayAgentGUI(DisplayAgent myAgent){
		this.myAgent=myAgent;
		this.knownAgents = new HashMap<String, AnimAgent>();
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
				window.setInterval(new Animation(), 20);
				
			}
		});
		this.canvas.addComponentListener(new ComponentAdapter(){
			// Resizing document when the canvas is resized
			public void componentResized(ComponentEvent ce){
				JSVGCanvas canvas = (JSVGCanvas) ce.getSource();				
				Element svgRoot = canvas.getSVGDocument().getDocumentElement();
				svgRoot.setAttributeNS(null, "width", ""+canvas.getWidth());
				svgRoot.setAttributeNS(null, "height", ""+canvas.getHeight());				
			}
		});
		this.svgDoc = SVGDOMImplementation.getDOMImplementation().createDocument(svgNs, "svg", null);
		this.svgRoot = svgDoc.getDocumentElement();
		this.svgRoot.setAttributeNS(null, "width", "300");
		this.svgRoot.setAttributeNS(null, "height", "300");
		this.canvas.setMySize(new Dimension(300,300));
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
		
		this.pack();		
		this.setVisible(true);
	}
	
	/**
	 * Add an agents SVG representation to the SVG document 
	 * @param element SVG element representing the agent
	 */
	public void addAgent(String name, String x, String y){
		Element newAgent = svgDoc.createElementNS(svgNs, "rect");
		newAgent.setAttributeNS(null, "id", name);
		newAgent.setAttributeNS(null, "x", x);
		newAgent.setAttributeNS(null, "y", y);
		newAgent.setAttributeNS(null, "width", "50");
		newAgent.setAttributeNS(null, "height", "20");
		newAgent.setAttributeNS(null, "style", "fill:red");
		((EventTarget)newAgent).addEventListener("click", new OnClickAction(), false);
		knownAgents.put(name, new AnimAgent(name, newAgent, Integer.parseInt(x), Integer.parseInt(y)));
		svgRoot.appendChild(newAgent);				
	}
	
	public void updateAgent(String name, String x, String y){
		AnimAgent agent = knownAgents.get(name);
		agent.setXPos(Integer.parseInt(x));
		agent.setYPos(Integer.parseInt(y));
	}
	
	/**
	 * Remove an agents SVG representation from the SVG document
	 * @param name Agents local name (= the SVG elements id attribute)
	 */
	public void removeAgent(String name){
		knownAgents.remove(name);
		this.svgRoot.removeChild(svgDoc.getElementById(name));		
	}	
	
	// Controlling periodic display updates
	private class Animation implements Runnable{

		@Override
		public void run() {
			Iterator<AnimAgent> agents = knownAgents.values().iterator();
			while(agents.hasNext()){
				AnimAgent aa = agents.next();
				aa.getElem().setAttributeNS(null, "x", ""+aa.getXPos());
				aa.getElem().setAttributeNS(null, "y", ""+aa.getYPos());
			}
			
		}		
	}
	
	private class OnClickAction implements EventListener{

		@Override
		public void handleEvent(Event evt) {
			Element target = (Element) evt.getTarget();
			String message = "Id: "+target.getAttributeNS(null, "id");
			
			// Build non-blocking JOptionPane
			final JDialog jd = new JDialog(null, ModalityType.MODELESS);
			final JOptionPane jop = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
			jop.addPropertyChangeListener(new PropertyChangeListener(){

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if(evt.getSource()==jop&&evt.getPropertyName().equals(JOptionPane.VALUE_PROPERTY))
						jd.setVisible(false);
				}				
			});			
			jd.setContentPane(jop);	
			jd.pack();
			jd.setVisible(true);			
		}		
	}
}
