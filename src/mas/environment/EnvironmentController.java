package mas.environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMImageElement;
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
import sma.ontology.Scale;
import sma.ontology.Size;

import mas.display.SvgTypes;
import mas.environment.OntoUtilities;

/**
 * This class is responsible for managing the environment definition
 * @author Nils
 *
 */
public class EnvironmentController{
	/**
	 * The current project
	 */
	private Project currentProject;
	/**
	 * The project's environment
	 */
	private Environment environment;
	/**
	 * The GUI for interacting with this environment controller
	 */
	private EnvironmentControllerGUI myGUI;
	/**
	 * HashMap for object access via id
	 */
	private HashMap<String, AbstractObject> objectHash = null;
	
	/**
	 * Constructor
	 * @param project The project the environment belongs to
	 * @param gui The GUI to interact with this EnvironmentController
	 */
	public EnvironmentController(Project project, EnvironmentControllerGUI gui){
		
		String svgPath, envPath;
		
		this.currentProject = project;
		currentProject.setEnvironmentController(this);
		this.myGUI = gui;
		// Load environment from file if specified
		if((envPath = currentProject.getEnvPath()) != null){
			loadEnvironment(new File(envPath));
			if(this.environment != null){
				rebuildObjectHash();
				
			}else{
				System.err.println(Language.translate("Umgebungsdatei")+" "+envPath+" "+Language.translate("nicht gefunden!"));
			}
		}
		// Load SVG from file if specified
		if((svgPath = currentProject.getSvgPath()) != null){
			Document svgDoc = loadSVG(new File(svgPath));
			if(svgDoc != null){
				this.setSvgDoc(svgDoc);
			}else{
				System.err.println(Language.translate("SVG-Datei")+" "+svgPath+" "+Language.translate("nicht gefunden!"));
			}
		}else{
			Language.translate("Keine SVG-Datei definiert");
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

	public Document getSvgDoc() {
		return myGUI.getSVGDoc();
	}

	public void setSvgDoc(Document svgDoc) {
		this.myGUI.setSVGDoc(svgDoc);
		this.environment.setSvgDoc(svgDoc);
	}

	public EnvironmentControllerGUI getMyGUI() {
		return myGUI;
	}

	public void setMyGUI(EnvironmentControllerGUI myGUI) {
		this.myGUI = myGUI;
	}
	
	/**
	 * Sets the projects SVG and environment file paths and copies the SVG to the right folder if necessary
	 * @param svgFile The SVG file
	 */
	public void setSVGFile(File svgFile){
		// Copy to the default folder if necessary
		if(!(svgFile.getParent().equals(currentProject.getEnvSetupPath()))){
			File targetFile = new File(currentProject.getEnvSetupPath()+File.separator+svgFile.getName());
			copyTextFile(svgFile, targetFile);
		}
		// Set project variables
		String svgFileName = svgFile.getName();
		currentProject.setSvgFile(svgFileName);
		String envFileName = svgFileName.substring(0, svgFileName.lastIndexOf('.'));
		currentProject.setEnvFile(envFileName+".xml");
		
		Document svgDoc = loadSVG(svgFile);
		
		prepareSVGDoc(svgDoc);
		myGUI.setSVGDoc(svgDoc);
		createNewEnvironment();
		environment.setSvgDoc(svgDoc);
	}
	
	/**
	 * - Copies embedded images to the project's resource folder
	 * - Sets relative href paths
	 * @param images
	 */
	private void checkEmbeddedImages(NodeList images){
		for(int i=0; i<images.getLength(); i++){
			SVGOMImageElement image = (SVGOMImageElement) images.item(i);
			String href = image.getHref().getBaseVal();
			
			int lastSlash = href.lastIndexOf('/');
			
			if(!href.substring(0, lastSlash).equals("../resources")){
				try {
					String targetPath = currentProject.getProjectFolderFullPath()+"resources"+File.separator+href.substring(lastSlash+1);
					URL sourceURL = new URL(href);
					File sourceFile = new File(sourceURL.toURI());
					File targetFile = new File(targetPath);
					copyBinFile(sourceFile, targetFile);
					image.getHref().setBaseVal("../resources"+href.substring(lastSlash));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Utility method for copying text files
	 * @param from The source file
	 * @param to The target file
	 */
	private void copyTextFile(File from, File to){
		try {
			if(!to.exists()){
				to.createNewFile();
				FileReader reader = new FileReader(from);
				FileWriter writer = new FileWriter(to);
				
				int c;
				while((c = reader.read()) != -1){
					writer.write(c);
				}
				
				reader.close();
				writer.close();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Utility method for copying non-text files
	 * @param from The source file
	 * @param to The target file
	 */
	private void copyBinFile(File from, File to){
		try {
			if(!to.exists()){
				to.createNewFile();
				
				FileInputStream reader = new FileInputStream(from);
				FileOutputStream writer = new FileOutputStream(to);
				
				int c;
				while((c = reader.read()) != -1){
					writer.write(c);
				}
				
				reader.close();
				writer.close();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Loading a SVG document from a file
	 * @param svgFile The file to load the SVG from
	 */
	public Document loadSVG(File svgFile){
		Document svgDoc = null;
		
		if(svgFile.exists()){
			System.out.println(Language.translate("Lade SVG-Datei")+" "+svgFile.getName());
			SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
			
			try {
				svgDoc = factory.createDocument(svgFile.toURI().toURL().toString());
				
								
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 			
		}else{
			System.err.println(Language.translate("SVG-Datei")+" "+svgFile.getAbsolutePath()+" "+Language.translate("nicht gefunden!"));
		}		
		return svgDoc;
	}
	
	/**
	 * This method prepares the SVG doc for being used for display
	 * - Adding the border
	 * - Calling checkEmbeddedImages to prepare the embedded images (if any)
	 */
	private void prepareSVGDoc(Document svgDoc){
		Element svgRoot = svgDoc.getDocumentElement();
		// Adding a border if not already present
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
		
		NodeList images = svgDoc.getElementsByTagName("image");
		if(images.getLength()>0){
			checkEmbeddedImages(images);
		}
	}
	
	/**
	 * Saving the SVG document to a file 
	 * @param svgFile The file to save the SVG to
	 */
	public void saveSVG(){
		File svgFile = new File(currentProject.getSvgPath());
		try {
			System.out.println(Language.translate("Speichere SVG nach")+" "+svgFile.getName());
			FileWriter fw = new FileWriter(svgFile);
			PrintWriter writer = new PrintWriter(fw);
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			writer.write("<!DOCTYPE svg PUBLIC '");
			writer.write(SVGConstants.SVG_PUBLIC_ID);
			writer.write("' '");
			writer.write(SVGConstants.SVG_SYSTEM_ID);
			writer.write("'>\n\n");
			SVGTranscoder t = new SVGTranscoder();
			t.transcode(new TranscoderInput(myGUI.getSVGDoc()), new TranscoderOutput(writer));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TranscoderException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creating a new environment using the SVG root element as root playground
	 */
	private void createNewEnvironment(){
		
		this.environment = new Environment();
		this.environment.setProjectName(currentProject.getProjectName());
		
		// Set default scale
		Scale scale = new Scale();
		scale.setValue(1);
		scale.setUnit("m");
		scale.setPixel(10);
		this.environment.setScale(scale);
		myGUI.setScale(scale);
		
		// Create root playground from SVG root
		PlaygroundObject rootPg = new PlaygroundObject();
		initFromSVG(rootPg, myGUI.getSVGDoc().getDocumentElement());
		rootPg.setId("rootPlayground");
		this.environment.setRootPlayground(rootPg);
		
		// 
		rebuildObjectHash();	
	}
	
	public HashMap<String, AbstractObject> getObjectHash() {
		return objectHash;
	}
	/**
	 * Gets the environment object with the specified id, or null if there is no object with this id in the environment
	 * @param id String The id to search for
	 * @return AbstractObject The object if found, else null
	 */
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
				case IMAGE:
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
			object.getPosition().setX((int)xPos);
			object.getPosition().setY((int)yPos);
			
			object.setSize(new Size());
			object.getSize().setWidth((int)width);
			object.getSize().setHeight((int)height);
			
			object.setId(svg.getAttributeNS(null, "id"));
		}
	}
	
	/**
	 * Rebuilding the objectHash with the objects currently present in the environment
	 */
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
	 * @param svg The SVG element representing the new object
	 * @param settings HashMap containing the properties of the new object
	 */
	public void createObject(Element svg, HashMap<String, String> settings){
			
		AbstractObject object = null;
		
		// If there is another object using the same SVG element, delete it first
		deleteObject(svg.getAttributeNS(null, "id"), false);
		
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
	 * @param id String The id of the object to be deleted
	 * @param removeChildren boolean If true, child objects will be deleted too. If false, they will be moved to the parent playground
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
	 * @param object The object to be moved
	 * @param target The destination playground 
	 * @return True if moving was possible, false if not (if the object is outside the playgrounds area
	 */
	public boolean moveObject(AbstractObject object, PlaygroundObject target){
		
		boolean success = false;
		if(success = OntoUtilities.pgContains(target, object)){
			object.getParent().removeChildObjects(object);
			target.addChildObjects(object);
			object.setParent(target);
		}else{
			System.out.println(Language.translate("Objekt")+" "+object.getId()+" "+Language.translate("liegt außerhalb von Playground")+" "+target.getId());
		}
		return success;
		
	}
	
	/**
	 * Save the environment to a XML file
	 * @param envFile Path of the file to save the environment to
	 */
	public void saveEnvironment(){
		
		try {
			
			// Create the XML Document
			DOMImplementation impl = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
			Document envDoc = impl.createDocument(null, "environment", null);
			Element envRoot = envDoc.getDocumentElement();
			envRoot.setAttribute("project", currentProject.getProjectName());
			envRoot.setAttribute("date", new java.util.Date().toString());
			Scale scaleObject = environment.getScale();
			if(scaleObject != null){
				Element scaleElement = envDoc.createElement("scale");
				scaleElement.setAttribute("value", ""+scaleObject.getValue());
				scaleElement.setAttribute("unit", scaleObject.getUnit());
				scaleElement.setAttribute("pixel", ""+scaleObject.getPixel());
				envRoot.appendChild(scaleElement);
			}
			
			// Save the root playground (including child objects 
			envRoot.appendChild(saveObject(environment.getRootPlayground(), envDoc));
			
			File envFile = new File(currentProject.getEnvPath());
			if(!envFile.exists()){
				envFile.createNewFile();
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
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Creating a XML element to save an environment object
	 * @param object The AbstractObject subclass instance to be saved
	 * @param document The XML document  the element will be part of
	 * @return An XML element containing the objects data
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
		element.setAttribute("xPos", ""+object.getPosition().getX());
		element.setAttribute("yPos", ""+object.getPosition().getY());
		element.setAttribute("width", ""+object.getSize().getWidth());
		element.setAttribute("height", ""+object.getSize().getHeight());
		
		return element;
	}
	
	/**
	 * Loading the environment from the passed file object
	 * @param envFile The path of the file containing the environment
	 */
	public void loadEnvironment(File envFile){
		
		
		
		if(envFile.exists()){
			try {
				DOMParser parser = new DOMParser();
				
				FileReader reader = new FileReader(envFile);
				InputSource source = new InputSource(reader);
				parser.parse(source);
				Document envDoc = parser.getDocument();
				Element rootPg = (Element) envDoc.getElementsByTagName("playground").item(0);
				if(rootPg != null){
					System.out.println(Language.translate("Lade Umgebung aus")+" "+envFile.getName()+"...");
					this.environment = new Environment();
					this.environment.setProjectName(currentProject.getProjectName());
					this.environment.setRootPlayground((PlaygroundObject) loadObject(rootPg));
					Element scaleElement = (Element) envDoc.getElementsByTagName("scale").item(0);
					if(scaleElement != null){
						Scale scaleObject = new Scale();
						scaleObject.setValue(Float.parseFloat(scaleElement.getAttribute("value")));
						scaleObject.setUnit(scaleElement.getAttribute("unit"));
						scaleObject.setPixel(Float.parseFloat(scaleElement.getAttribute("pixel")));
						this.environment.setScale(scaleObject);
						myGUI.setScale(scaleObject);
						
					}
					
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
	 * @param element The XML element containing the object data
	 * @return The AbstractObject created
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
		object.getPosition().setX(Float.parseFloat(element.getAttribute("xPos")));
		object.getPosition().setY(Float.parseFloat(element.getAttribute("yPos")));
		object.setSize(new Size());
		object.getSize().setWidth(Float.parseFloat(element.getAttribute("width")));
		object.getSize().setHeight(Float.parseFloat(element.getAttribute("height")));
		
		environment.addObjects(object);
		return object;
	}
	
	public void setScale(Scale scale){
		this.environment.setScale(scale);
		System.out.println(Language.translate("Lege Maßstad fest:")+" "+scale.getValue()+scale.getUnit()+":"+scale.getPixel()+"px");
	}
	
}
