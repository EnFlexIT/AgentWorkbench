package mas.display;

import java.util.Iterator;


import mas.environment.ontology.Physical2DEnvironment;
import mas.environment.ontology.Physical2DObject;
import mas.environment.ontology.PlaygroundObject;
import mas.environment.ontology.Position;
import mas.environment.ontology.Scale;

import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DisplayAgentGUI extends BasicSVGGUI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6509863549022282766L;

	private final int PERIOD = 100;
	
	private Scale scale;
	
	public DisplayAgentGUI(Document svgDoc, Physical2DEnvironment env){
		super();
		this.getCanvas().setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
//		this.getCanvas().addGVTTreeRendererListener(new GVTTreeRendererAdapter(){
//			public void gvtRenderingCompleted(GVTTreeRendererEvent re){
//				
//				Window window = getCanvas().getUpdateManager().getScriptingEnvironment().createWindow();
//				window.setInterval(new Animation(), PERIOD);
//			}
//		});
		this.scale = env.getScale();
		initPositions(svgDoc, env.getRootPlayground());
		this.setSVGDoc(svgDoc);
	}
	
	void setSVGDocument(Document doc, PlaygroundObject rootPlayground){
		initPositions(doc, rootPlayground);
		this.getCanvas().setDocument(doc);
	}
	

	
	@SuppressWarnings("unchecked")
	private void initPositions(Document svgDoc, PlaygroundObject pg){
		Iterator<Physical2DObject> objects = pg.getAllChildObjects();
		while(objects.hasNext()){
			Physical2DObject object = objects.next();
			Element element = svgDoc.getElementById(object.getId());
			if(element != null){
				setElementPosition(element, object.getPosition());
			}
			
			if(object instanceof PlaygroundObject){
				initPositions(svgDoc, (PlaygroundObject) object);
			}
		}
	}
	
	/**
	 * Sets the position of a SVG element according to a mas.environment.ontology.Position
	 * @param elem The SVG element
	 * @param pos The Position instance
	 */
	private void setElementPosition(Element elem, Position pos){
		// Convert to pixel values
		float xPos = pos.getXPos() / scale.getRealWorldUnitValue() * scale.getPixelValue();
		float yPos = pos.getYPos() / scale.getRealWorldUnitValue() * scale.getPixelValue();
		
		String elementType = elem.getTagName();	// Determine element type
		if(elementType.equals("rect")){
			// Convert to top left coordinates
			float width = Float.parseFloat(elem.getAttributeNS(null, "width"));
			float height = Float.parseFloat(elem.getAttributeNS(null, "height"));
			xPos -= width/2;
			yPos -= height/2;
			
			elem.setAttributeNS(null, "x", ""+xPos);
			elem.setAttributeNS(null, "y", ""+yPos);
		}else if(elementType.equals("circle") || elementType.equals("ellipse")){
			// No conversion necessary, circles and ellipses use center coordinates
			elem.setAttributeNS(null, "cx", ""+xPos);
			elem.setAttributeNS(null, "cy", ""+yPos);
		}
	}
}
