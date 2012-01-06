/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */

package agentgui.envModel.p2Dsvg.controller;


import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.OntologyException;
import jade.core.Agent;

import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observer;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.batik.util.SVGConstants;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import agentgui.core.agents.AgentClassElement4SimStart;
import agentgui.core.application.Language;
import agentgui.core.common.FileCopier;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.environment.EnvironmentPanel;
import agentgui.core.project.Project;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.core.sim.setup.SimulationSetups;
import agentgui.core.sim.setup.SimulationSetupsChangeNotification;
import agentgui.envModel.p2Dsvg.display.SVGUtils;
import agentgui.envModel.p2Dsvg.ontology.ActiveObject;
import agentgui.envModel.p2Dsvg.ontology.EnvironmentOntology;
import agentgui.envModel.p2Dsvg.ontology.Movement;
import agentgui.envModel.p2Dsvg.ontology.Physical2DEnvironment;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.ontology.PlaygroundObject;
import agentgui.envModel.p2Dsvg.ontology.Position;
import agentgui.envModel.p2Dsvg.ontology.Scale;
import agentgui.envModel.p2Dsvg.ontology.Size;
import agentgui.envModel.p2Dsvg.utils.EnvironmentHelper;
import agentgui.envModel.p2Dsvg.utils.EnvironmentWrapper;

/**
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 * This class controlls/manages the enviroment. You can get a copy of the current enviroment, load and save SVG files.
 *
 */
public class Physical2DEnvironmentController extends EnvironmentController implements Observer {
	/**
	 * Observable event code: New environment instance assigned
	 */
	public static final int ENVIRONMENT_CHANGED = 0;
	/**
	 * Observable event code: Scale settings changed
	 */
	public static final int SCALE_CHANGED = 1;
	/**
	 * Observable event code: New environment object created
	 */
	public static final int OBJECTS_CHANGED = 2;
	/**
	 * Observable event code: SVG document changed
	 */
	public static final int SVG_CHANGED = 3;
	/**
	 * An error occured  
	 */
	public static final int EC_ERROR = 4;
	
	/**
	 * This EnvironmentController's GUI
	 */
	private Physical2DEnvironmentControllerGUI myGUI = null;

	/**
	 * Path for saving the SVG
	 */
	private String currentSVGPath = null;
	/**
	 * Path for saving the environment
	 */
	private String currentEnvironmentPath = null;
	/**
	 * The environment instance encapsulated by this EnvironmentController
	 */
	private Physical2DEnvironment environment;
	/**
	 * Wrapper for easier handling of the environment
	 */
	private EnvironmentWrapper envWrap = null;
	/**
	 * The SVG document representing this environment; 
	 */
	private Document svgDoc = null;	
	/** 
	 * The SVG file name. 
	 */
	private String svgFileName = null;
	/**
	 * The Physical2DObject currently selected for editing
	 */
	private Physical2DObject selectedObject = null;
	
	private String lastErrorMessage;
	/**
	 * Constructor
	 * @param project The Agent.GUI project
	 */
	public Physical2DEnvironmentController(Project project){
		super(project);
		this.setDefaultFileNames();
		loadEnvironment();
	}
	
	/**
	 * Sets this EnvironmentController's GUI
	 * @param gui
	 */
	public void setGUI(Physical2DEnvironmentControllerGUI gui){
		myGUI = gui;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#createEnvironmentPanel()
	 */
	@Override
	protected EnvironmentPanel createEnvironmentPanel() {
		Physical2DEnvironmentControllerGUI p2dGUI = new Physical2DEnvironmentControllerGUI(this);
		return p2dGUI;
	}
	
	/**
	 * Setting the selected Physical2DObject
	 * @param id The id of the selected object
	 */
	public void setSelectedObject(String id) {
		this.selectedObject = envWrap.getObjectById(id);
	}
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#setEnvironment(java.lang.Object)
	 */
	@Override
	public void setEnvironmentModel(Object environmentObject) {
	}
	/**
	 * @return The current projects Environment
	 */
	public Physical2DEnvironment getEnvironmentModel() {
		return environment;
	}
		
	public Physical2DEnvironment getEnvironmentModelCopy() {
		// --- Datei kopieren ---
		String fileSrc   = getEnvFolderPath() + getCurrentSimSetup().getEnvironmentFileName();
		String fileDest  = fileSrc.substring(0, fileSrc.length()-4) + "_tmp.xml";
		
		FileCopier fc = new FileCopier();
		fc.copyFile(fileSrc, fileDest);

		// --- Load Env. --------
		Physical2DEnvironment p2de = this.loadEnvironmentFromXML(new File(fileDest));
		
		// --- Rückgabe ---------
		return p2de;
	}
	/**
	 * @return EnvironmentWrapped containing the current project's environment 
	 */
	public EnvironmentWrapper getEnvWrap() {
		return envWrap;
	}
	
	private Physical2DEnvironment initEnvironment(){
		Physical2DEnvironment newEnv = null;
		if(svgDoc != null){
			Element svgRoot = svgDoc.getDocumentElement();
			float width = Float.parseFloat(svgRoot.getAttributeNS(null, "width"));
			float height = Float.parseFloat(svgRoot.getAttributeNS(null, "height"));
			
			Size rootPgSize = new Size();
			rootPgSize.setWidth(width);
			rootPgSize.setHeight(height);
			
			Position rootPgPos = new Position();
			rootPgPos.setXPos(rootPgSize.getWidth()/2);
			rootPgPos.setYPos(rootPgSize.getHeight()/2);
			
			PlaygroundObject rootPg = new PlaygroundObject();
			rootPg.setPosition(rootPgPos);
			rootPg.setSize(rootPgSize);
			rootPg.setId("RootPlayground");
			
			Scale defaultScale = new Scale();
			defaultScale.setPixelValue(10);
			defaultScale.setRealWorldUnitValue(1);
			defaultScale.setRealWorldUntiName("m");
			
			newEnv = new Physical2DEnvironment();
			newEnv.setRootPlayground(rootPg);
			newEnv.setScale(defaultScale);
			newEnv.setProjectName(this.getProject().getProjectName());
			
			String envFileName = this.getProject().simulationSetupCurrent+".xml";
			getCurrentSimSetup().setEnvironmentFileName(envFileName);
		}
		return newEnv;
	}
	
	/**
	 * This method loads a SVG document from a SVG file
	 * @param svgFile The file containing the document
	 * @return The SVG document, or null if not successful
	 */
	private Document loadSVG(File svgFile){
		Document doc = null;
		
		if(svgFile.exists()){
			doc = SVGDOMImplementation.getDOMImplementation().createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
			System.out.println(Language.translate("Lade SVG-Datei")+" "+svgFile.getName());
			SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
			
			try {
				doc = factory.createDocument(svgFile.toURI().toURL().toString());
			} catch (MalformedURLException e) {
//				System.err.println(Language.translate("Fehler beim Laden der SVG-Datei"));
				this.setLastErrorMessage(Language.translate("Fehler beim Laden der SVG-Datei")+" "+svgFile.getPath());
				setChanged();
				notifyObservers(new Integer(EC_ERROR));
			} catch (IOException e) {
//				System.err.println(Language.translate("Fehler beim Laden der SVG-Datei"));
				this.setLastErrorMessage(Language.translate("Fehler beim Laden der SVG-Datei")+" "+svgFile.getPath());
				setChanged();
				notifyObservers(new Integer(EC_ERROR));
			}			
		}else{
			System.out.println(Language.translate("SVG-Datei")+" "+svgFile.getPath()+" "+Language.translate("nicht gefunden"));
//			this.setLastErrorMessage(Language.translate("SVG-Datei")+" "+svgFile.getPath()+" "+Language.translate("nicht gefunden"));
//			setChanged();
//			notifyObservers(new Integer(EC_ERROR));
		}
		
		return doc;
	}
	
	public void setSVGFile(File file){
		setSvgDoc(loadSVG(file));
		if(!(file.getParentFile().getAbsolutePath()+File.separator).equals(getEnvFolderPath())){
			file = new File(currentSVGPath);
			saveSVG(file);
		}
		this.setSvgFileName(file.getName());
		setEnvironment(initEnvironment());
	}
	/**
	 * Saving the current SVG document to a file
	 * @param svgFile The file
	 */
	private void saveSVG(File svgFile){
		if(svgDoc != null){
			if(myGUI != null){
				myGUI.setSelectedElement(null);
			}
			try {
				System.out.println(Language.translate("Speichere SVG nach ")+" "+svgFile.getName());
				if(!svgFile.exists()){
					svgFile.createNewFile();
				}
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
//				System.err.println(Language.translate("Fehler beim Erzeugen der Datei")+" "+svgFile.getAbsolutePath());
				this.setLastErrorMessage(Language.translate("Fehler beim Erzeugen der Datei")+" "+svgFile.getAbsolutePath());
				setChanged();
				notifyObservers(new Integer(EC_ERROR));
				
			} catch (TranscoderException e) {
//				System.err.println(Language.translate("Fehler beim speichern des SVG-Dokuments"));
				this.setLastErrorMessage(Language.translate("Fehler beim speichern des SVG-Dokuments"));
				setChanged();
				notifyObservers(new Integer(EC_ERROR));
			}
		}
		
	}
	
	/**
	 * This method prepares the SVG document for Agent.GUI visualization use
	 * - Adds a border if not already there
	 * @param doc The SVG document to prepare
	 * @return The prepared SVG document
	 */
	private Document prepareSVG(Document doc){
		Element svgRoot = doc.getDocumentElement();
		SVGUtils.removeTransform(svgRoot, 0, 0);
		if(doc.getElementById("border") == null){
			Element border = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "rect");
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
		return doc;
	}
	/**
	 * Getter method for svgDoc
	 * @return The SVG document assigned to the current environment 
	 */
	public Document getSvgDoc() {
		return svgDoc;
	}
	
	public Document getSvgDocCopy() {
		if (svgDoc==null) {
			return null;
		} else {
			Document svgDocCopy = (Document) svgDoc.cloneNode(true);
			return svgDocCopy;	
		}		
	}
	/**
	 * Prepares the SVG document and assigns it to the current environment
	 * @param doc
	 */
	private void setSvgDoc(Document doc){
		if(doc != null){
			this.svgDoc = prepareSVG(doc);		
		}
		this.svgDoc = doc;
		setChanged();
		notifyObservers(new Integer(SVG_CHANGED));
	}
	
	/**
	 * Saves the current environment to a file
	 * @param envFile The file to save to
	 */
	private void savePhysical2DEnvironment(File envFile){
		
		if(environment != null){
			try {
				System.out.println(Language.translate("Speichere Umgebung nach ")+" "+envFile.getName());
				if(!envFile.exists()){
					envFile.createNewFile();
				}
				XMLCodec codec = new XMLCodec();
				String xmlRepresentation = codec.encodeObject(EnvironmentOntology.getInstance(), environment, true);
				if(!envFile.exists()){
					envFile.createNewFile();
				}
				FileWriter fw = new FileWriter(envFile);
				fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				fw.append(xmlRepresentation);
				fw.close();
			} catch (CodecException e) {
					e.printStackTrace();
			} catch (OntologyException e) {
					e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * Loading a Physical2DEnvironment from a XML File
	 * @param envFile The XML file
	 * @return The environment
	 */
	private Physical2DEnvironment loadEnvironmentFromXML(File envFile){
		Physical2DEnvironment env = null;
		if(envFile.exists()){
			try {
				System.out.println(Language.translate("Lade Umgebungsdatei")+" "+envFile.getName());
				BufferedReader br = new BufferedReader(new FileReader(envFile));
				StringBuffer xmlString = new StringBuffer();
				boolean keep = false;
				String line;
				while((line = br.readLine()) != null){
					// Read Physical2DEnvironment and child nodes only, skip metadata
					if(line.contains("<Physical2DEnvironment")){
						keep=true;
					}
					if(keep){
						xmlString.append(line);
					}
					if(line.contains("</Physical2DEnvironment"));
				}
				br.close();
				
				XMLCodec codec = new XMLCodec();
				env = (Physical2DEnvironment) codec.decodeObject(EnvironmentOntology.getInstance(), xmlString.toString());
			} catch (FileNotFoundException e) {
//				System.err.println(Language.translate("Umgebungsdatei")+" "+envFile.getName()+" "+Language.translate("nicht gefunden"));
				this.setLastErrorMessage(Language.translate("Umgebungsdatei")+" "+envFile.getName()+" "+Language.translate("nicht gefunden"));
				setChanged();
				notifyObservers(new Integer(EC_ERROR));
			} catch (IOException e) {
//				System.err.println(Language.translate("Fehler beim Lesen der Umgebungsdatei")+" "+envFile.getName());
				this.setLastErrorMessage(Language.translate("Fehler beim Lesen der Umgebungsdatei")+" "+envFile.getName());
				setChanged();
				notifyObservers(new Integer(EC_ERROR));
			} catch (CodecException e) {
//				System.err.println("Fehler beim Parsen der Umgebungsdatei!");
				this.setLastErrorMessage(Language.translate("Fehler beim Parsen der Umgebungsdatei!"));
				setChanged();
				notifyObservers(new Integer(EC_ERROR));
			} catch (OntologyException e) {
//				System.err.println("Fehler beim Parsen der Umgebungsdatei!");
				this.setLastErrorMessage(Language.translate("Fehler beim Parsen der Umgebungsdatei!"));
				setChanged();
				notifyObservers(new Integer(EC_ERROR));
			}
			
		} else {
			System.out.println(Language.translate("Umgebungsdatei")+" "+envFile.getPath()+" "+Language.translate("nicht gefunden"));

		}
		return env;
	}
	/**
	 * This method sets the environment and notifies the observers
	 * @param env The environment
	 */
	private void setEnvironment(Physical2DEnvironment env){
		this.environment = env;
		if(env != null){
			this.envWrap = new EnvironmentWrapper(env);
		}else{
			this.envWrap = null;
		}
		setChanged();
		notifyObservers(new Integer(ENVIRONMENT_CHANGED));
	}
	
	
	/**
	 * Adds the a new agent to the agent-list.
	 *
	 * @param p2DObj the Physical2DObject
	 */
	private void addToAgentList(Physical2DObject p2DObj) {	
		
	
		if(p2DObj instanceof ActiveObject) {					
	
	        String className = ((ActiveObject) p2DObj).getAgentClassName();
	        if(className.equals("")||className==null||className.equals("None")) {
	        	System.out.println("Class name not set");
	        	return;
	        }
	        
	        try {
	        	@SuppressWarnings("unchecked")
				Class<? extends Agent> cls = (Class<? extends Agent>) Class.forName(className);
	        	
	 			AgentClassElement4SimStart simStart=new AgentClassElement4SimStart(cls, SimulationSetup.AGENT_LIST_EnvironmentConfiguration);
	 		
	 			simStart.setStartAsName(p2DObj.getId());
	 			simStart.setPostionNo(this.getAgents2Start().size()+1);
	 			
	 			if(isInListForAgents2Start(p2DObj.getId())==false) {
	 				this.getAgents2Start().addElement(simStart);
		 			this.updatePositionNr();
	 			} 
	 			
				
		    } catch(Exception e)  {
		       	e.printStackTrace();
		    }
		    
		}
	}
	

	 /**
 	 * Updates the agent list if an element is changed.
 	 *
 	 * @param object the object
 	 * @param settings the settings
 	 */
 	private void changeElementFromAgentList(Physical2DObject  object, HashMap<String, Object> settings) {		
			this.removeFromAgentList(selectedObject);
			String id = settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_ID).toString();
			this.addToAgentList(createObjectForList(settings));
		
	}
	
	private void removeFromAgentList(Physical2DObject  obj)
	{	
		if(obj instanceof ActiveObject)
		{	        	
			
				for(int i=0;i<this.getAgents2Start().size();i++)
				{
					
				AgentClassElement4SimStart cmprElement= (AgentClassElement4SimStart) this.getAgents2Start().get(i);
				
				if(cmprElement.getStartAsName().equals(obj.getId()))
				{
					this.getAgents2Start().remove(i);
					
					break;
				}
				
		 }
				
		  this.updatePositionNr();	
		   
		}
	}
	
	/**
	 * Update position nr.
	 */
	private void updatePositionNr() {
		for(int i=0;i<this.getAgents2Start().size();i++) {
			AgentClassElement4SimStart cmprElement= (AgentClassElement4SimStart) this.getAgents2Start().get(i);
			cmprElement.setPostionNo(i);
		}
	}
	
	/**
	 * This method is called by the EnvironmentSetup to create or change Physical2DObjects 
	 * @param settings
	 */
	public boolean createOrChange(HashMap<String, Object> settings){
		
		boolean success = false;
		if(selectedObject == null){
			
			// Create mode
			Physical2DObject newObject = createObject(settings);
			envWrap.addObject(newObject);
			success = (newObject != null);
			this.addToAgentList(newObject);
			
		}else{
			this.changeElementFromAgentList(selectedObject,settings);
			changeObject(selectedObject, settings);
			
			envWrap.rebuildLists();
			success = true;
		}
		if(success){
			setChanged();
			notifyObservers(new Integer(OBJECTS_CHANGED));
			
			this.getProject().isUnsaved = true;
		}
		
		return success;
	}
	/**
	 * This method creates a new Physical2DObject according to the given settings
	 * 
	 * @param settings The object properties
	 * @return The created Physical2DObject
	 */
	private Physical2DObject createObject(HashMap<String, Object> settings){
		Physical2DObject newObject = null;
		
		// Check if the specified ID is available
		if(checkID((String) settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_ID))){
		
			try {
				Class<?> ontologyClass = (Class<?>) settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_ONTO_CLASS);
				newObject = (Physical2DObject) ontologyClass.newInstance();
				newObject.setId(settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_ID).toString());
				newObject.setPosition((Position) settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_POSITION));
				newObject.setSize((Size) settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_SIZE));
				newObject.setParentPlaygroundID(environment.getRootPlayground().getId());
				if(newObject instanceof ActiveObject){
					((ActiveObject)newObject).setMovement(new Movement());
					if(! settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_AGENT_MAX_SPEED).toString().isEmpty()){
						((ActiveObject)newObject).setMaxSpeed(Float.parseFloat(settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_AGENT_MAX_SPEED).toString()));
						String agentClassName = settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_AGENT_CLASSNAME).toString();
						((ActiveObject)newObject).setAgentClassName(agentClassName);
					}
					
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}else{	// ID already in use -> don't create the object 
//			System.err.println(Language.translate("Fehler: Die gewählte ID ist bereits vergeben!"));
			this.setLastErrorMessage(Language.translate("Fehler: Die gewählte ID ist bereits vergeben!"));
			setChanged();
			notifyObservers(new Integer(EC_ERROR));
		}
		
		return newObject;
	}
	private Physical2DObject createObjectForList(HashMap<String, Object> settings){
		Physical2DObject newObject = null;
		
		// Check if the specified ID is available
		
		
			try {
				Class<?> ontologyClass = (Class<?>) settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_ONTO_CLASS);
				newObject = (Physical2DObject) ontologyClass.newInstance();
				newObject.setId(settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_ID).toString());
				newObject.setPosition((Position) settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_POSITION));
				newObject.setSize((Size) settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_SIZE));
				newObject.setParentPlaygroundID(environment.getRootPlayground().getId());
				if(newObject instanceof ActiveObject){
					((ActiveObject)newObject).setMovement(new Movement());
					if(! settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_AGENT_MAX_SPEED).toString().isEmpty()){
						((ActiveObject)newObject).setMaxSpeed(Float.parseFloat(settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_AGENT_MAX_SPEED).toString()));
						String agentClassName = settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_AGENT_CLASSNAME).toString();
						((ActiveObject)newObject).setAgentClassName(agentClassName);
					}
					
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		
		
		return newObject;
	}
	
	/**
	 * This method changes an existing Physical2DObject according to the given settings.
	 * @param object The Physical2DObject
	 * @param settings The settings
	 */
	private void changeObject(Physical2DObject object, HashMap<String, Object> settings){
		// Change mode
		
		String id = settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_ID).toString();
		// The ID is not changed or the new ID is available 
		if(id.equals(selectedObject.getId()) || checkID(id)){
		 
			if(selectedObject.getClass().equals(settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_ONTO_CLASS))){
				// Same object type, just change attributes
				selectedObject.setId((String) settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_ID));
				selectedObject.setPosition((Position) settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_POSITION));
				selectedObject.setSize((Size) settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_SIZE));
				if(selectedObject instanceof ActiveObject){
					((ActiveObject)selectedObject).setMaxSpeed(Float.parseFloat((String) settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_AGENT_MAX_SPEED)));
					((ActiveObject)selectedObject).setAgentClassName(settings.get(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_AGENT_CLASSNAME).toString());
				}
				envWrap.rebuildLists();
			}else{
				envWrap.removeObject(selectedObject);
				envWrap.addObject(createObject(settings));
			}
		}else{		// The new ID is not available
//			System.err.println(Language.translate("Fehler: Die gewählte ID ist bereits vergeben!"));
			this.setLastErrorMessage(Language.translate("Fehler: Die gewählte ID ist bereits vergeben!"));
			setChanged();
			notifyObservers(new Integer(EC_ERROR));
		}
	}
	
	/**
	 * Checks if the given ID is available
	 * @param id The ID to check
	 * @return True if available, false if already in use
	 */
	private boolean checkID(String id){
		return (envWrap.getObjectById(id) == null);
	}
	
	/**
	 * Checks if the given ID is available
	 * @param id The ID to check
	 * @return True if available, false if already in use
	 */
	private boolean isInListForAgents2Start(String id){
		
		for(int i=0;i<this.getAgents2Start().size();i++) {
			AgentClassElement4SimStart tmp=(AgentClassElement4SimStart) this.getAgents2Start().get(i);
			if(tmp.getStartAsName().equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removing the currently selected object from the environment
	 */
	public void removeObject(){
		this.removeFromAgentList(selectedObject);
		envWrap.removeObject(selectedObject);
		
		setChanged();
		notifyObservers(new Integer(OBJECTS_CHANGED));
	}
	/**
	 * @return the selectedObject
	 */
	public Physical2DObject getSelectedObject() {
		return selectedObject;
	}
	
	/**
	 * Adds the object with the given objectID to the playground with the given playgroundID
	 * @param objectId
	 * @param playgroundId
	 * @return true if the change can be performed otherweise false.
	 */
	public boolean moveObjectToPlayground(String objectId, String playgroundId){
		Physical2DObject object = envWrap.getObjectById(objectId);
		PlaygroundObject target = (PlaygroundObject) envWrap.getObjectById(playgroundId);
		if(playgroundContainsObject(object, target)){
			PlaygroundObject oldParent = (PlaygroundObject) envWrap.getObjectById(object.getParentPlaygroundID());
			oldParent.removeChildObjects(object);
			target.addChildObjects(object);
			object.setParentPlaygroundID(target.getId());
			setChanged();
			notifyObservers(new Integer(OBJECTS_CHANGED));
			return true;
		}else{
			this.setLastErrorMessage(Language.translate("Fehler: Objekt")+" "+objectId+" "+Language.translate("liegt nicht im Bereich der Teilumgebung")+" "+playgroundId+".");
			setChanged();
			notifyObservers(new Integer(EC_ERROR));
			return false;
		}
		
	}
	
	/**
	 * Checks if the given PlaygroundObject completely contains the given Physical2DObject
	 * @param object The Physical2DObject
	 * @param playground The PlaygroundObject
	 * @return The result
	 */
	private boolean playgroundContainsObject(Physical2DObject object, PlaygroundObject playground){
		float objTopLeftX = object.getPosition().getXPos() - object.getSize().getWidth()/2;
		float objTopLeftY = object.getPosition().getYPos() - object.getSize().getHeight()/2;
		Rectangle2D.Float objRect = new Rectangle2D.Float(objTopLeftX, objTopLeftY, object.getSize().getWidth(), object.getSize().getHeight());
		
		float pgTopLeftX = playground.getPosition().getXPos() - playground.getSize().getWidth()/2;
		float pgTopLeftY = playground.getPosition().getYPos() - playground.getSize().getHeight()/2;
		Rectangle2D.Float pgRect = new Rectangle2D.Float(pgTopLeftX, pgTopLeftY, playground.getSize().getWidth(), playground.getSize().getHeight());
		
		return pgRect.contains(objRect);
	}
	
	/**
	 * Sets the default file names.
	 */
	private void setDefaultFileNames(){

		String baseFileName = this.getProject().simulationSetupCurrent;
		this.getCurrentSimSetup().setEnvironmentFileName(baseFileName+".xml");
		this.setSvgFileName(baseFileName+".svg");
		
		this.currentEnvironmentPath = this.getEnvFolderPath() + getCurrentSimSetup().getEnvironmentFileName();
		this.currentSVGPath = this.getEnvFolderPath() + this.getSvgFileName();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#handleSimSetupChange(agentgui.core.sim.setup.SimulationSetupsChangeNotification)
	 */
	@Override
	protected void handleSimSetupChange(SimulationSetupsChangeNotification sscn){
		
		switch(sscn.getUpdateReason()){

			case SimulationSetups.SIMULATION_SETUP_SAVED:
				this.saveEnvironment();
			break;
			
			case SimulationSetups.SIMULATION_SETUP_COPY:
				this.setDefaultFileNames();
				this.saveEnvironment();
			break;
			
			case SimulationSetups.SIMULATION_SETUP_ADD_NEW:
				this.setDefaultFileNames();
				this.setEnvironment(null);
				this.setSvgDoc(null);
				this.registerDefaultListModel4SimulationStart(SimulationSetup.AGENT_LIST_EnvironmentConfiguration);
			break;
			
			case SimulationSetups.SIMULATION_SETUP_REMOVE:
				File envFile = new File(this.currentEnvironmentPath);
				File svgFile = new File(this.currentSVGPath);
				if(envFile.exists()){
					envFile.delete();
				}
				if(svgFile.exists()){
					svgFile.delete();
				}
				this.setDefaultFileNames();
			break;
			
			case SimulationSetups.SIMULATION_SETUP_LOAD:
				this.setDefaultFileNames();
				setEnvironment(loadEnvironmentFromXML(new File(this.currentEnvironmentPath)));
				setSvgDoc(loadSVG(new File(this.currentSVGPath)));
				this.registerDefaultListModel4SimulationStart(SimulationSetup.AGENT_LIST_EnvironmentConfiguration);
			break;
			
			case SimulationSetups.SIMULATION_SETUP_RENAME:
				File oldEnvFile = new File(this.currentEnvironmentPath);
				File oldSVGFile = new File(this.currentSVGPath);
				
				setDefaultFileNames();
				if(oldEnvFile.exists()){
					File newEnvFile = new File(this.getEnvFolderPath()+getCurrentSimSetup().getEnvironmentFileName());
					oldEnvFile.renameTo(newEnvFile);
				}
				
				if(oldSVGFile.exists()){
					File newSvgFile = new File(this.getEnvFolderPath() + this.getSvgFileName());
					oldSVGFile.renameTo(newSvgFile);
				}
				this.setDefaultFileNames();
				
			break;
		}
		
	}
	
	public void setScale(Scale scale){
		this.environment.setScale(scale);
		Iterator<Physical2DObject> allObjects = envWrap.getObjectysById().values().iterator();
		while(allObjects.hasNext()){
			Physical2DObject object = allObjects.next();
			Element elem;
			if(object.getId().equals("RootPlayground")){
				elem = svgDoc.getDocumentElement();
			}else{
				elem = svgDoc.getElementById(object.getId());
			}
			object.setPosition(EnvironmentHelper.getPosFromElement(elem, scale));
			object.setSize(EnvironmentHelper.getSizeFromElement(elem, scale));
		}
		setChanged();
		notifyObservers(new Integer(SCALE_CHANGED));
		this.getProject().isUnsaved = true;
	}


	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#loadEnvironment()
	 */
	@Override
	protected void loadEnvironment() {
	
		SimulationSetup currentSetup = getCurrentSimSetup(); 
		
				
		// Load SVG file if specified
		if(this.getSvgFileName() != null && this.getSvgFileName().length() >0){
			currentSVGPath = getEnvFolderPath() + this.getSvgFileName();
			setSvgDoc(loadSVG(new File(currentSVGPath)));
		}
		// Load environment file if specified
		if(currentSetup.getEnvironmentFileName() != null && currentSetup.getEnvironmentFileName().length() >0){
			currentEnvironmentPath = getEnvFolderPath() + currentSetup.getEnvironmentFileName();
			setEnvironment(loadEnvironmentFromXML(new File(currentEnvironmentPath)));
		}
		// If SVG present and environment not, create a new blank environment 
		if(this.svgDoc != null && this.environment == null){
			setEnvironment(initEnvironment());
		}
		
		this.registerDefaultListModel4SimulationStart(SimulationSetup.AGENT_LIST_EnvironmentConfiguration);
	}


	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentController#saveEnvironment()
	 */
	@Override
	protected void saveEnvironment() {
		if (currentSVGPath!=null){
			saveSVG(new File(currentSVGPath));	
		}
		if (currentEnvironmentPath!=null) {
			savePhysical2DEnvironment(new File(currentEnvironmentPath));	
		}
	}


	/**
	 * @return the lastErrorMessage
	 */
	public String getLastErrorMessage() {
		return lastErrorMessage;
	}
	/**
	 * @param lastErrorMessage the lastErrorMessage to set
	 */
	private void setLastErrorMessage(String lastErrorMessage) {
		this.lastErrorMessage = lastErrorMessage;
	}
	
	/**
	 * Provides the svg file name.
	 *
	 * @return the svgFileName
	 */
	public String getSvgFileName() {
		return svgFileName;
	}
	/**
	 * Sets the svg file name.
	 *
	 * @param svgFileName the svgFileName to set
	 */
	public void setSvgFileName(String svgFileName) {
		this.svgFileName = svgFileName;
	}

	
}
