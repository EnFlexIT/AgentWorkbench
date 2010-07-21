package mas.environment;

import gui.projectwindow.simsetup.EnvironmentSetup;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.OntologyException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMImageElement;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.batik.util.SVGConstants;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import application.Language;
import application.Project;


import mas.display.SvgTypes;
import mas.display.ontology.AbstractObject;
import mas.display.ontology.AgentObject;
import mas.display.ontology.DisplayOntology;
import mas.display.ontology.Environment;
import mas.display.ontology.GenericObject;
import mas.display.ontology.ObstacleObject;
import mas.display.ontology.PlaygroundObject;
import mas.display.ontology.Position;
import mas.display.ontology.Scale;
import mas.display.ontology.Size;
import mas.display.ontology.Speed;

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
	private EnvironmentSetup myGUI;
	/**
	 * HashMap for object access via id
	 */
	private HashMap<String, AbstractObject> objectHash = null;
	
	/**
	 * Constructor
	 * @param project The project the environment belongs to
	 * @param gui The GUI to interact with this EnvironmentController
	 */
	public EnvironmentController(Project project, EnvironmentSetup gui){
		
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
		this.environment.setSvgDoc(svg2String(svgDoc));
	}

	public EnvironmentSetup getMyGUI() {
		return myGUI;
	}

	public void setMyGUI(EnvironmentSetup myGUI) {
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
		environment.setSvgDoc(svg2String(svgDoc));
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
//			System.out.println(Language.translate("Lade SVG-Datei")+" "+svgFile.getName());
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
		System.out.println("PrepareSVG called");
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
		
		TransformRemover.removeTransform(svgRoot, 0, 0);
		
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
//			System.out.println(Language.translate("Speichere SVG nach")+" "+svgFile.getName());
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
	 * Gets the SVG documents XML code as String
	 * @param svg The SVG document
	 * @return The XML code
	 */
	private String svg2String(Document svg){
		StringWriter writer = new StringWriter();
				
		try {
			SVGTranscoder t = new SVGTranscoder();
//			PrintWriter writer = new PrintWriter(svgString);
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			writer.write("<!DOCTYPE svg PUBLIC '");
			writer.write(SVGConstants.SVG_PUBLIC_ID);
			writer.write("' '");
			writer.write(SVGConstants.SVG_SYSTEM_ID);
			writer.write("'>\n\n");			
			t.transcode(new TranscoderInput(myGUI.getSVGDoc()), new TranscoderOutput(writer));
		} catch (TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return writer.toString();
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
			Scale scale = environment.getScale();
			// Get position and size from the SVG, converting pixel to real world units 
			switch(type){
				
				case SVG:
//					width = OntoUtilities.calcRWU(Float.parseFloat(svg.getAttributeNS(null, "width")), scale);
//					height = OntoUtilities.calcRWU(Float.parseFloat(svg.getAttributeNS(null, "height")), scale);
					width = scale.calcRwu(Float.parseFloat(svg.getAttributeNS(null, "width")));
					height = scale.calcRwu(Float.parseFloat(svg.getAttributeNS(null, "height")));
					xPos = 0;
					yPos = 0;
				break;
				
				case RECT:
				case IMAGE:
//					width = OntoUtilities.calcRWU(Float.parseFloat(svg.getAttributeNS(null, "width")), scale);
//					height = OntoUtilities.calcRWU(Float.parseFloat(svg.getAttributeNS(null, "height")), scale);
//					xPos = OntoUtilities.calcRWU(Float.parseFloat(svg.getAttributeNS(null, "x")), scale);
//					yPos = OntoUtilities.calcRWU(Float.parseFloat(svg.getAttributeNS(null, "y")), scale);
					
					width = scale.calcRwu(Float.parseFloat(svg.getAttributeNS(null, "width")));
					height = scale.calcRwu(Float.parseFloat(svg.getAttributeNS(null, "height")));
					xPos = scale.calcRwu(Float.parseFloat(svg.getAttributeNS(null, "x")));
					yPos = scale.calcRwu(Float.parseFloat(svg.getAttributeNS(null, "y")));
				break;
				
				case CIRCLE:
//					width = height = OntoUtilities.calcRWU(Float.parseFloat(svg.getAttributeNS(null, "r")), scale);
//					xPos = OntoUtilities.calcRWU(Float.parseFloat(svg.getAttributeNS(null, "cx"))-(width/2), scale);
//					yPos = OntoUtilities.calcRWU(Float.parseFloat(svg.getAttributeNS(null, "cy"))-(height/2), scale);
					
					width = height = scale.calcRwu(Float.parseFloat(svg.getAttributeNS(null, "r")));
					xPos = scale.calcRwu(Float.parseFloat(svg.getAttributeNS(null, "cx"))-(width/2));
					yPos = scale.calcRwu(Float.parseFloat(svg.getAttributeNS(null, "cy"))-(height/2));
				break;
				
				case ELLIPSE:
//					width = OntoUtilities.calcRWU(Float.parseFloat(svg.getAttributeNS(null, "rx")), scale);
//					height = OntoUtilities.calcRWU(Float.parseFloat(svg.getAttributeNS(null, "ry")), scale);
//					xPos = OntoUtilities.calcRWU(Float.parseFloat(svg.getAttributeNS(null, "cx"))-(width/2), scale);
//					yPos = OntoUtilities.calcRWU(Float.parseFloat(svg.getAttributeNS(null, "cy"))-(height/2), scale);
					
					width = scale.calcRwu(Float.parseFloat(svg.getAttributeNS(null, "rx")));
					height = scale.calcRwu(Float.parseFloat(svg.getAttributeNS(null, "ry")));
					xPos = scale.calcRwu(Float.parseFloat(svg.getAttributeNS(null, "cx"))-(width/2));
					yPos = scale.calcRwu(Float.parseFloat(svg.getAttributeNS(null, "cy"))-(height/2));
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
				Speed speed = new Speed();
				speed.setSpeed(100.0F);
				if(!settings.get("speed").isEmpty()){
					speed.setSpeed(Float.parseFloat(settings.get("speed")));
				}
				((AgentObject)object).setCurrentSpeed(speed);
			break;
			
			case OBSTACLE:
				object = new ObstacleObject();
			break;
			
			case PLAYGROUND:
				object = new PlaygroundObject();
			break;
			
			case GENERIC:
				object = new GenericObject();
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
		if(success = target.contains(object)){
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
		
		if(environment != null){
			try {
				XMLCodec codec = new XMLCodec();
				this.environment.getRootPlayground().unsetParent();
				String xmlRepresentation = codec.encodeObject(DisplayOntology.getInstance(), environment, true);
//				System.out.println(xmlRepresentation);
				this.environment.getRootPlayground().setParent();
				File envFile = new File(currentProject.getEnvPath());
				if(!envFile.exists()){
					envFile.createNewFile();
				}
				FileWriter fw = new FileWriter(envFile);
				fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				fw.append(xmlRepresentation);
				fw.close();
				
				
				
			} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * Loading the environment from the passed file object
	 * @param envFile The path of the file containing the environment
	 */
	public void loadEnvironment(File envFile){		
		try {
			BufferedReader br = new BufferedReader(new FileReader(envFile));
			StringBuffer xmlRepresentation = new StringBuffer();
			boolean keep = false;
			String line;
			while((line = br.readLine()) != null){
				if(line.contains("<Environment")){
					keep=true;
				}
				if(keep){
					xmlRepresentation.append(line);
				}
				if(line.contains("</Environment")){
					keep=false;
				}				
			}
			br.close();			
			
			XMLCodec codec = new XMLCodec();
			this.environment = (Environment) codec.decodeObject(DisplayOntology.getInstance(), xmlRepresentation.toString());
			this.environment.getRootPlayground().setParent();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setScale(Scale scale){
		this.environment.setScale(scale);
		System.out.println(Language.translate("Lege Maßstad fest:")+" "+scale.getValue()+scale.getUnit()+":"+scale.getPixel()+"px");
	}
	
}
