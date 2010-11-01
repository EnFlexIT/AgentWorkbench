package mas.display;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SVGUtils {
	public static String svgToString(Document svgDoc){
		StringWriter sw = new StringWriter();
		SVGTranscoder trans = new SVGTranscoder();
		TranscoderInput ti = new TranscoderInput(svgDoc);
		TranscoderOutput to = new TranscoderOutput(sw);
		try {
			trans.transcode(ti, to);
		} catch (TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sw.getBuffer().toString();
	}
	
	public static Document stringToSVG(String svgString){
		
		Document doc = SVGDOMImplementation.getDOMImplementation().createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
//		try {
//			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			doc = db.parse(new InputSource(new StringReader(svgString)));
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		StringReader sr = new StringReader(svgString);
		SVGTranscoder trans = new SVGTranscoder();
		TranscoderInput ti = new TranscoderInput(sr);
		TranscoderOutput to = new TranscoderOutput(new StringWriter());
		to.setDocument(doc);
		try {
			trans.transcode(ti, to);
		} catch (TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return to.getDocument();
	}
	
	/**
	 * Removing the transform attribute from a node
	 * @param node The node
	 * @param xDiff The x coordinate difference from the parent elements transform=translate attributes
	 * @param yDiff The y coordinate difference from the parent elements transform=translate attributes
	 */
	public static void removeTransform(Node node, float xDiff, float yDiff){
		String regex = "-?\\d+\\.?\\d*,-?\\d+\\.?\\d*";
		String transform = ((Element) node).getAttributeNS(null, "transform");
		if(transform != null && transform.length()>0 && transform.contains("translate")){
			Matcher m = Pattern.compile(regex).matcher(transform);
			if(m.find()){
				String posChange = m.group();
				String x = posChange.substring(0, posChange.indexOf(','));
				String y = posChange.substring(posChange.indexOf(',')+1);
				xDiff += Float.parseFloat(x);
				yDiff += Float.parseFloat(y);
				
				((Element) node).setAttributeNS(null, "transform", "");
				
			}			
		}
		if(node instanceof Element){
			changePos((Element)node, xDiff, yDiff);
		}
		if(node.hasChildNodes()){
			NodeList children = node.getChildNodes();
			for(int i=0; i<children.getLength(); i++){
				if(children.item(i) instanceof Element){
					removeTransform(children.item(i), xDiff, yDiff);
				}
			}
		}
	}
	
	/**
	 * Adding the changes from the removed transform=translate attributes to the elements coordinates 
	 * @param element The element
	 * @param xDiff The x coordinate difference from the parent elements transform=translate attributes
	 * @param yDiff The y coordinate difference from the parent elements transform=translate attributes
	 */
	private static void changePos(Element element, float xDiff, float yDiff){
		String type = element.getTagName();
		if(type.equals("rect") || type.equals("image")){
			String xPos = element.getAttributeNS(null, "x");
			String yPos = element.getAttributeNS(null, "y");
			if(xPos != null && yPos != null && xPos.length()>0 && yPos.length()>0){
				Float x = Float.parseFloat(xPos);
				Float y = Float.parseFloat(yPos);
				x += xDiff;
				y += yDiff;
				element.setAttributeNS(null, "x", ""+x);
				element.setAttributeNS(null, "y", ""+y);
			}
			
		}else if (type.equals("circle") || type.equals("ellipse")){
			String xPos = element.getAttributeNS(null, "cx");
			String yPos = element.getAttributeNS(null, "cy");
			if(xPos != null && yPos != null && xPos.length()>0 && yPos.length()>0){
				Float x = Float.parseFloat(xPos);
				Float y = Float.parseFloat(yPos);
				x += xDiff;
				y += yDiff;
				element.setAttributeNS(null, "cx", ""+x);
				element.setAttributeNS(null, "cy", ""+y);
			}
			
		}
	}
	
	
}
