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
		
		// Temporary solution, to be integrated in christian's GUI
		JFrame frame = new JFrame("DA GUI Test");		
		frame.setContentPane(this);
		frame.pack();		
		frame.setVisible(true);
	}
	
	/**
	 * Add an agents SVG representation to the SVG document 
	 * @param element SVG element representing the agent
	 */
	public void addAgent(Element element){
		Element newAgent = element;
		((EventTarget)newAgent).addEventListener("click", new OnClickAction(), false);
		this.svgRoot.appendChild(newAgent);		
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
						String tagName = agentSVG.getTagName();
						if(tagName.equals("circle")){
							agentSVG.getAttributeNodeNS(null, "cx").setValue(""+agent.getXPos());
							agentSVG.getAttributeNodeNS(null, "cy").setValue(""+agent.getYPos());
						}else{
							agentSVG.getAttributeNodeNS(null, "x").setValue(""+agent.getXPos());
							agentSVG.getAttributeNodeNS(null, "y").setValue(""+agent.getYPos());
						}
					}else{
						System.out.println("SVG representation of "+agent.getId()+" not found");
					}
				}
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
