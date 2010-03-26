package mas.environment;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.batik.util.SVGConstants;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import application.Language;
import application.Project;

import sma.ontology.AbstractObject;
import sma.ontology.AgentObject;
import sma.ontology.Environment;
import sma.ontology.ObstacleObject;
import sma.ontology.PlaygroundObject;
import sma.ontology.Position;
import sma.ontology.Size;

import mas.display.SvgTypes;

public class EnvironmentController{
	
	private Project currentProject;
	
	private Environment environment;
	
	private EnvironmentControllerAgent controllerAgent;
	
	private Document svgDoc;
	
	private EnvironmentControllerGUI myGUI;
	
	private HashMap<String, AbstractObject> objectHash = null;
	
	public EnvironmentController(Project project, EnvironmentControllerGUI gui){
		
		boolean newEnv = false;
		this.currentProject = project;
		this.myGUI = gui;
		
		if(currentProject.getEnvFile() != null){
			loadEnvironment(new File(currentProject.getEnvPath()));
		}else{
			System.out.println(Language.translate("Keine Umgebungsdatei definiert"));
		}
		if(this.environment == null){
			newEnv = true;
		}else{
			currentProject.setEnvironment(this.environment);
		}
		
		
		if(currentProject.getSvgFile() != null){
			loadSVG(new File(currentProject.getSvgPath()), newEnv);
		}else{
			System.out.println(Language.translate("Keine SVG-Datei definiert"));
		}
		
	}

	public Project getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public EnvironmentControllerAgent getControllerAgent() {
		return controllerAgent;
	}

	public void setControllerAgent(EnvironmentControllerAgent controllerAgent) {
		this.controllerAgent = controllerAgent;
	}

	public Document getSvgDoc() {
		return svgDoc;
	}

	public void setSvgDoc(Document svgDoc) {
		this.svgDoc = svgDoc;
	}

	public EnvironmentControllerGUI getMyGUI() {
		return myGUI;
	}

	public void setMyGUI(EnvironmentControllerGUI myGUI) {
		this.myGUI = myGUI;
	}
	
	/**
	 * Loading a new SVG file. If newEnv = true, creating a new Environment from the SVGs root element.
	 * @param svgFile
	 * @param newEnv
	 */
	public void loadSVG(File svgFile, boolean newEnv){
		if(svgFile.exists()){
			if(!svgFile.getName().equals(currentProject.getSvgFile())){
				currentProject.setSvgFile(svgFile.getName());
			}
			
			System.out.println(Language.translate("Lade SVG-Datei")+" "+svgFile.getName());
			
			SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
			
			try {
				svgDoc = factory.createDocument(svgFile.toURI().toURL().toString());
				
				Element svgRoot = svgDoc.getDocumentElement();
				
				if(svgDoc.getElementById("border") == null){
					Element border = svgDoc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "rect");
					float width = Float.parseFloat(svgRoot.getAttributeNS(null, "width"))-1;
					float height = Float.parseFloat(svgRoot.getAttributeNS(null, "height"))-1;
					border.setAttributeNS(null, "id", "border");
					border.setAttributeNS(null, "width", ""+(int)width);
					border.setAttributeNS(null, "height", ""+(int)height);
					border.setAttributeNS(null, "fill", "none");
					border.setAttributeNS(null, "stroke", "black");
					border.setAttributeNS(null, "stroke-width", "1");
					svgRoot.appendChild(border);
				}
				
				myGUI.setSVGDoc(svgDoc);
				
				if(newEnv){
					createNewEnvironment();
					currentProject.setEnvFile(null);
				}
				
				environment.setSvgDoc(svgDoc);
				
				rebuildObjectHash();
				
				
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 			
		}else{
			System.err.println(Language.translate("SVG-Datei")+" "+svgFile.getAbsolutePath()+" "+Language.translate("nicht gefunden!"));
		}
	}
	
	/**
	 * Saving the SVG document to a file 
	 * @param svgFile
	 */
	public void saveSVG(File svgFile){
		try {
			FileWriter fw = new FileWriter(svgFile);
			PrintWriter writer = new PrintWriter(fw);
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			writer.write("<!DOCTYPE svg PUBLIC '");
			writer.write(SVGConstants.SVG_PUBLIC_ID);
			writer.write("' '");
			writer.write(SVGConstants.SVG_SYSTEM_ID);
			writer.write("'>\n\n");
			SVGTranscoder t = new SVGTranscoder();
			t.transcode(new TranscoderInput(svgDoc), new TranscoderOutput(writer));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TranscoderException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creating a new environment using the SVG root element as root playground
	 * @return
	 */
	private void createNewEnvironment(){
		
		this.environment = new Environment();
		this.environment.setProjectName(currentProject.getProjectName());
		PlaygroundObject rootPg = new PlaygroundObject();
		initFromSVG(rootPg, svgDoc.getDocumentElement());
		rootPg.setId("rootPlayground");
		this.environment.setRootPlayground(rootPg);
		rebuildObjectHash();
		
		currentProject.setEnvironment(environment);
	}
	
	public HashMap<String, AbstractObject> getObjectHash() {
		return objectHash;
	}
	
	public AbstractObject getObjectById(String id){
		return objectHash.get(id);
	}

	/**
	 * This method initializes an environment object using data 
	 * @param object
	 * @param svg
	 */
	private void initFromSVG(AbstractObject object, Element svg){
		SvgTypes type = SvgTypes.getType(svg);
		if(type == null){
			System.out.println(Language.translate("Element-Typ")+" "+svg.getTagName()+" "+Language.translate("nicht unterstützt"));
		}else{
			float xPos=0, yPos=0, width=0, height=0;
			switch(type){
				
				case SVG:
					width = Float.parseFloat(svg.getAttributeNS(null, "width"));
					height = Float.parseFloat(svg.getAttributeNS(null, "height"));
					xPos = 0;
					yPos = 0;
				break;
				
				case RECT:
					width = Float.parseFloat(svg.getAttributeNS(null, "width"));
					height = Float.parseFloat(svg.getAttributeNS(null, "height"));
					xPos = Float.parseFloat(svg.getAttributeNS(null, "x"));
					yPos = Float.parseFloat(svg.getAttributeNS(null, "y"));					
				break;
				
				case CIRCLE:
					width = height = Float.parseFloat(svg.getAttributeNS(null, "r"));
					xPos = Float.parseFloat(svg.getAttributeNS(null, "cx"))-(width/2);
					yPos = Float.parseFloat(svg.getAttributeNS(null, "cy"))-(height/2);
				break;
				
				case ELLIPSE:
					width = Float.parseFloat(svg.getAttributeNS(null, "rx"));
					height = Float.parseFloat(svg.getAttributeNS(null, "ry"));
					xPos = Float.parseFloat(svg.getAttributeNS(null, "cx"))-(width/2);
					yPos = Float.parseFloat(svg.getAttributeNS(null, "cy"))-(height/2);
				break;
					
			}
			
			object.setPosition(new Position());
			object.getPosition().setXPos((int)xPos);
			object.getPosition().setYPos((int)yPos);
			
			object.setSize(new Size());
			object.getSize().setWidth((int)width);
			object.getSize().setHeight((int)height);
			
			object.setId(svg.getAttributeNS(null, "id"));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void rebuildObjectHash(){
		Iterator<AbstractObject> objects = environment.getAllObjects();
		objectHash = new HashMap<String, AbstractObject>();
		while(objects.hasNext()){
			AbstractObject object = objects.next();
			objectHash.put(object.getId(), object);
		}				
	}
	
	/**
	 * Creating a new environment object
	 * @param svg
	 * @param settings
	 */
	public void createObject(Element svg, HashMap<String, String> settings){
			
		AbstractObject object = null;
		
		// If there is another object using the same SVG element, delete it first
		deleteObject(settings.get("id"), false);
		
		ObjectTypes type = ObjectTypes.getType(settings.get("type"));
		
		// Type specific parts
		switch(type){
			case AGENT:
				object = new AgentObject();
				((AgentObject)object).setAgentClass(settings.get("class"));
			break;
			
			case OBSTACLE:
				object = new ObstacleObject();
			break;
			
			case PLAYGROUND:
				object = new PlaygroundObject();
			break;				
		}
		
		// Common parts
		initFromSVG(object, svg);
		svg.setAttributeNS(null, "id", settings.get("id"));
		object.setId(settings.get("id"));
		object.setParent(environment.getRootPlayground());
		
		// Add to environment and playground
		environment.addObjects(object);
		environment.getRootPlayground().addChildObjects(object);
		
		rebuildObjectHash();
		myGUI.rebuildEnvironmentTree();		
	}
	
	/**
	 * Deleting the selected environment object
	 * @param id
	 * @param removeChildren
	 */
	@SuppressWarnings("unchecked")
	public void deleteObject(String id, boolean removeChildren){
		AbstractObject object = objectHash.get(id);
		if(object != null){
			if(ObjectTypes.getType(object) == ObjectTypes.PLAYGROUND){
				Iterator<AbstractObject> children = ((PlaygroundObject)object).getAllChildObjects();
				while(children.hasNext()){
					if(removeChildren){
						deleteObject(children.next().getId(), removeChildren);
					}else{
						children.next().setParent(object.getParent());
					}
				}
			}
			
			object.getParent().removeChildObjects(object);
			environment.removeObjects(object);
			
			rebuildObjectHash();
			myGUI.rebuildEnvironmentTree();
		}
	}
	
	/**
	 * Moves an environment object to another playground 
	 * @param object
	 * @param target
	 * @return
	 */
	public boolean moveObject(AbstractObject object, PlaygroundObject target){
		
		boolean success = false;
		if(success = pgContains(target, object)){
			object.getParent().removeChildObjects(object);
			target.addChildObjects(object);
			object.setParent(target);
		}else{
			System.out.println(Language.translate("Objekt")+" "+object.getId()+" "+Language.translate("liegt außerhalb von Playground")+" "+target.getId());
		}
		return success;
		
	}
	
	/**
	 * Checks if the playgrounds area contains the object
	 * @param playground
	 * @param object
	 * @return
	 */
	private boolean pgContains(PlaygroundObject playground, AbstractObject object){
		
		Point pgPos = new Point(playground.getPosition().getXPos(), playground.getPosition().getYPos());
		Dimension pgSize = new Dimension(playground.getSize().getWidth(), playground.getSize().getHeight());
		Area pgArea = new Area(new Rectangle(pgPos, pgSize));
		
		Point objectPos = new Point(object.getPosition().getXPos(), object.getPosition().getYPos());
		Dimension objectSize = new Dimension(object.getSize().getWidth(), object.getSize().getHeight());
		Rectangle objectRect = new Rectangle(objectPos, objectSize);
		
		return pgArea.contains(objectRect);
		
		
	}
	
	/**
	 * Save the environment to a XML file
	 * @param envFile
	 */
	public void saveEnvironment(File envFile){
		
		try {
			// Create the XML Document
			DOMImplementation impl = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
			Document envDoc = impl.createDocument(null, "environment", null);
			Element envRoot = envDoc.getDocumentElement();
			envRoot.setAttribute("project", currentProject.getProjectName());
			envRoot.setAttribute("date", new java.util.Date().toString());
			
			// Save the root playground (including child objects 
			envRoot.appendChild(saveObject(environment.getRootPlayground(), envDoc));
			
			if(!envFile.exists()){
				envFile.createNewFile();
			}
			
			if(!envFile.getName().equals(currentProject.getEnvFile())){
				currentProject.setEnvFile(envFile.getName());
			}
			
			System.out.println(Language.translate("Speichere Umgebung nach")+" "+envFile.getName());
			
			
			// Format the document and save it 
			OutputFormat format = new OutputFormat(envDoc);
			FileOutputStream fos = new FileOutputStream(envFile);
			format.setIndenting(true);
			format.setIndent(2);
			XMLSerializer serializer = new XMLSerializer(fos, format);
		    serializer.serialize(envDoc);
		    fos.close();
		    
		    // Save the SVG
		    saveSVG(new File(currentProject.getSvgPath()));
			
			
			
			
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Creating a XML element saving an environment object
	 * @param object
	 * @param document
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Element saveObject(AbstractObject object, Document document){
		
		Element element = null;
		
		// Type specific parts
		switch(ObjectTypes.getType(object)){
			case AGENT:
				element = document.createElement("agent");
				element.setAttribute("class", ((AgentObject)object).getAgentClass());
			break;
			
			case OBSTACLE:
				element = document.createElement("obstacle");
			break;
			
			case PLAYGROUND:
				element = document.createElement("playground");
				
				Iterator<AbstractObject> children = ((PlaygroundObject)object).getAllChildObjects();
				while(children.hasNext()){
					element.appendChild(saveObject(children.next(), document));
				}
			break;
		}
		
		// Common parts
		element.setAttribute("id", ""+object.getId());
		element.setAttribute("xPos", ""+object.getPosition().getXPos());
		element.setAttribute("yPos", ""+object.getPosition().getYPos());
		element.setAttribute("width", ""+object.getSize().getWidth());
		element.setAttribute("height", ""+object.getSize().getHeight());
		
		return element;
	}
	
	/**
	 * Loading the environment from the passed file object
	 * @param envFile
	 */
	public void loadEnvironment(File envFile){
		
		if(envFile.exists()){
			try {
				DOMParser parser = new DOMParser();
				
				FileReader reader = new FileReader(envFile);
				InputSource source = new InputSource(reader);
				parser.parse(source);
				Element rootPg = (Element) parser.getDocument().getDocumentElement().getElementsByTagName("playground").item(0);
				if(rootPg != null){
					System.out.println(Language.translate("Lade Umgebung aus")+" "+envFile.getName()+"...");
					this.environment = new Environment();
					this.environment.setProjectName(currentProject.getProjectName());
					this.environment.setRootPlayground((PlaygroundObject) loadObject(rootPg));
					rebuildObjectHash();					
				}				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.err.println(Language.translate("Umgebungsdatei")+" "+envFile.getAbsolutePath()+" "+Language.translate("nicht gefunden!"));
		}
	}
	
	/**
	 * Creates an environment object from the passed XML element
	 * @param element
	 * @return
	 */
	private AbstractObject loadObject(Element element){
		AbstractObject object = null;
		
		// Type specific parts
		ObjectTypes type = ObjectTypes.getType(element.getTagName());
		switch(type){
			case OBSTACLE:
				object = new ObstacleObject();
			break;
			
			case AGENT:
				object = new AgentObject();
				((AgentObject)object).setAgentClass(element.getAttribute("class"));
			break;
			
			case PLAYGROUND:
				object = new PlaygroundObject();
				if(element.hasChildNodes()){
					NodeList children = element.getChildNodes();
					for(int i=0; i<children.getLength(); i++){
						if(children.item(i) instanceof Element){
							AbstractObject child = loadObject((Element) children.item(i));
							child.setParent((PlaygroundObject) object);
							((PlaygroundObject)object).addChildObjects(child);
						}
					}
				}
			break;
		}
		
		// Common parts
		object.setId(element.getAttribute("id"));
		object.setPosition(new Position());
		object.getPosition().setXPos(Integer.parseInt(element.getAttribute("xPos")));
		object.getPosition().setYPos(Integer.parseInt(element.getAttribute("yPos")));
		object.setSize(new Size());
		object.getSize().setWidth(Integer.parseInt(element.getAttribute("width")));
		object.getSize().setHeight(Integer.parseInt(element.getAttribute("height")));
		
		environment.addObjects(object);
		return object;
	}
	
}
