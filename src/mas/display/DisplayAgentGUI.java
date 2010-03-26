package mas.display;


import java.util.HashSet;
import java.util.Iterator;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.script.Window;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.w3c.dom.Element;

import sma.ontology.AbstractObject;

import application.Project;

/**
 * GUI for DisplayAgent
 * @author nils
 *
 */
public class DisplayAgentGUI extends BasicSvgGUI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	// Currrent Agent-Project of AgentGUI
	Project currentProject = null;
	
	// SVG Namespace
	public static final String svgNs=SVGDOMImplementation.SVG_NAMESPACE_URI;
	
	private DisplayAgent myAgent = null;
	
	public DisplayAgentGUI(){
		super();
		this.getCanvas().setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		this.getCanvas().addGVTTreeRendererListener(new GVTTreeRendererAdapter(){
			public void gvtRenderingCompleted(GVTTreeRendererEvent re){
				
				Window window = getCanvas().getUpdateManager().getScriptingEnvironment().createWindow();
				window.setInterval(new Animation(), 100);
			}
		});
		
	}
	
	public void setAgent(DisplayAgent agent){
		this.myAgent = agent;
	}
	
	/**
	 * Responsible for updating the SVG element positions
	 * @author Nils
	 *
	 */
	private class Animation implements Runnable{

		@Override
		public void run() {
			HashSet<AbstractObject> set = myAgent.getMovedObjects();
			Iterator<AbstractObject> iter = set.iterator();
			while(iter.hasNext()){
				AbstractObject object = iter.next();	
				
				Element element = DisplayAgentGUI.this.getCanvas().getSVGDocument().getElementById(object.getId());
				float posX = Float.parseFloat(element.getAttributeNS(null, "x"));
				float posY = Float.parseFloat(element.getAttributeNS(null, "y"));
				
				posX = object.getPosition().getXPos();
				posY = object.getPosition().getYPos();
				
				element.setAttributeNS(null, "x", ""+posX);
				element.setAttributeNS(null, "y", ""+posY);				
			}
			
			set.clear();			
		}
		
	}
			
}
