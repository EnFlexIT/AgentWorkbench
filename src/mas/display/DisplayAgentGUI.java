package mas.display;

import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import org.apache.batik.swing.JSVGScrollPane;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import application.Application;
import application.Project;

import svganalyzer.SvgAnalyzer;
/**
 * GUI for DisplayAgent
 * @author nils
 *
 */
public class DisplayAgentGUI extends JPanel {
	
	// Currrent Agent-Project of AgentGUI
	Project CurrentProject = null;
	
	// SVG Namespace
	public static final String svgNs="http://www.w3.org/2000/svg";
	
	// DisplayAgent controlling this GUI
	DisplayAgent myAgent = null;
	
	// Swing stuff
	JPanel zoomPanel = null;
	JButton btnZoomIn = null;
	JButton btnZoomOut = null;
	JButton btnZoomReset = null;
	JSVGCanvas canvas = null;
	JSVGScrollPane scrollPane = null;
	
	// Pre-start components
	JLabel lblStart = null;
	JButton btnStart = null;
		
	// SVG stuff
	Document svgDoc = null;
	Element svgRoot = null;
	Window window = null;	
	
	Map <String, AnimAgent> knownAgents;
	
	/**
	 * This constructor is called when the GUI is created first (embedded mode)
	 */
	public DisplayAgentGUI( Project CP ){
		
		this.CurrentProject = CP;
		
		final int btnWidth = 150;
		final int btnHeight = 25;
		lblStart = new JLabel();
		lblStart.setBounds(new Rectangle(354, 201, 149, 16));
		lblStart.setText("DisplayAgent not started");
		this.setLayout(null);

				
		btnStart = new JButton();
		btnStart.setText("Start");
		btnStart.addActionListener(new ActionListener(){
			// Starting a display agent and enabling the GUI

			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				String agentNameBase = "DA";
				String agentName = agentNameBase;
				int agentNameSuffix = 0;				

				if ( Application.JadePlatform.jadeMainContainerIsRunning(true) == true ) {
					while( Application.JadePlatform.jadeAgentIsRunning(agentName) == true ){
						agentName = agentNameBase + agentNameSuffix++;
					}	
					System.out.println("Agent name "+agentNameBase);				
					
					initialize();
					Object[] args = new Object[1];
					args[0] = DisplayAgentGUI.this;
					Application.JadePlatform.jadeAgentStart(agentName, "mas.display.DisplayAgent", args, DisplayAgentGUI.this.CurrentProject.getProjectFolder() );
					
					DisplayAgentGUI.this.remove(lblStart);
					DisplayAgentGUI.this.remove(btnStart);
					}
				
			}
			
		});
		this.add(lblStart, null);
		this.add(btnStart, null);
		this.addComponentListener(new ComponentAdapter(){
			// Fit button position to pane size
			public void componentResized(ComponentEvent ce){
				int btnX = (DisplayAgentGUI.this.getWidth()/2-btnWidth/2);
				int btnY = (DisplayAgentGUI.this.getHeight()/2-btnHeight/2);
				lblStart.setLocation(btnX, btnY-30);		
				btnStart.setBounds(new Rectangle(btnX, btnY, btnWidth, btnHeight));
				
			}
		
			
		});
		this.setVisible(true);
	}
	
	/**
	 * This constructor is called when the DisplayAgent is created first (stand alone mode)
	 * @param myAgent Displayagent object controlling this GUI
	 */
	public DisplayAgentGUI(DisplayAgent myAgent){
		this.myAgent=myAgent;			
		this.initialize();
	}
	
	/**
	 * Initializing the SVG related components and starting the GUI
	 */
	private void initialize(){
		this.setVisible(false);
		this.knownAgents = new HashMap<String, AnimAgent>();
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

		this.svgDoc = SVGDOMImplementation.getDOMImplementation().createDocument(svgNs, "svg", null);
		this.svgRoot = svgDoc.getDocumentElement();
		this.svgRoot.setAttributeNS(null, "width", "500");
		this.svgRoot.setAttributeNS(null, "height", "300");
//		Element backGround = svgDoc.createElementNS(svgNs, "rect");
//		backGround.setAttributeNS(null, "id", "background");
//		backGround.setAttributeNS(null, "x", "0");
//		backGround.setAttributeNS(null, "y", "0");
//		backGround.setAttributeNS(null, "width", "500");
//		backGround.setAttributeNS(null, "height", "300");
//		backGround.setAttributeNS(null, "style", "fill:lightskyblue");
//		this.svgRoot.appendChild(backGround);
		
		Element cornerMarker1 = svgDoc.createElementNS(svgNs, "rect");
		cornerMarker1.setAttributeNS(null, "x", "1");
		cornerMarker1.setAttributeNS(null, "y", "1");
		cornerMarker1.setAttributeNS(null, "width", "1");
		cornerMarker1.setAttributeNS(null, "height", "1");
		svgRoot.appendChild(cornerMarker1);
		
		Element cornerMarker2 = svgDoc.createElementNS(svgNs, "rect");
		cornerMarker2.setAttributeNS(null, "x", "850");
		cornerMarker2.setAttributeNS(null, "y", "1");
		cornerMarker2.setAttributeNS(null, "width", "1");
		cornerMarker2.setAttributeNS(null, "height", "1");
		svgRoot.appendChild(cornerMarker2);
		
		Element cornerMarker3 = svgDoc.createElementNS(svgNs, "rect");
		cornerMarker3.setAttributeNS(null, "x", "1");
		cornerMarker3.setAttributeNS(null, "y", "500");
		cornerMarker3.setAttributeNS(null, "width", "1");
		cornerMarker3.setAttributeNS(null, "height", "1");
		svgRoot.appendChild(cornerMarker3);
		
		Element cornerMarker4 = svgDoc.createElementNS(svgNs, "rect");
		cornerMarker4.setAttributeNS(null, "x", "850");
		cornerMarker4.setAttributeNS(null, "y", "500");
		cornerMarker4.setAttributeNS(null, "width", "1");
		cornerMarker4.setAttributeNS(null, "height", "1");
		svgRoot.appendChild(cornerMarker4);
		
		this.canvas.setMySize(new Dimension(500,300));
		this.canvas.setDocument(svgDoc);
		
		
		// Initialize buttons and panel
		
		this.setLayout(new BorderLayout());
		this.zoomPanel = new JPanel();
		this.zoomPanel.setLayout(new FlowLayout());
		this.btnZoomIn = new JButton("+");
		this.btnZoomIn.addActionListener(canvas.new ZoomAction(1.2));
		
		this.btnZoomOut = new JButton("-");
		this.btnZoomOut.addActionListener(canvas.new ZoomAction(0.8));
		this.btnZoomReset = new JButton("100%");
		this.btnZoomReset.addActionListener(canvas.new ResetTransformAction());
		this.zoomPanel.add(btnZoomIn);
		this.zoomPanel.add(btnZoomOut);
		this.zoomPanel.add(btnZoomReset);
		this.add("North", zoomPanel);
		
		scrollPane=new JSVGScrollPane(canvas);
		scrollPane.setScrollbarsAlwaysVisible(true);
		this.add("Center", scrollPane);
				
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
	
	private class UpdateScrollsAction implements EventListener{

		@Override
		public void handleEvent(Event arg0) {
			DisplayAgentGUI.this.scrollPane.reset();
			
		}
		
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
