package agentgui.core.ontologies.gui;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.ontologies.OntologyClassTree;
import agentgui.core.ontologies.OntologyClassTreeObject;
import agentgui.core.ontologies.OntologySingleClassDescription;
import agentgui.core.ontologies.OntologySingleClassSlotDescription;

public class DynForm extends JPanel {

	private static final long serialVersionUID = 7942028680794127910L;

	private boolean debug = false;

	// --- Based on this vector the display will be created ---------
	private Vector<String> currOnotologyClassReferenceList = new Vector<String>();
	
	// --- parameters which are comming from the constructor -------- 
	private Project currProject = null;
	private String currAgentReference = null;

	// --- Runtime parameters of this  
	private JLabel lastComponent = null;
	private int einrueckungProUntereEbene = 5;
	
	private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root");
	private DefaultTreeModel objectTree = new DefaultTreeModel(rootNode);
	
	private Object[] ontoArgsInstance = null; 
	private String[] ontoArgsXML = null; 
	
	/**
	 * Constructor of this class by uising a project and an agent reference
	 * @param project
	 * @param agentReference
	 */
	public DynForm(Project project, String agentReference) {
		
		super();
		this.currProject = project;
		this.currAgentReference = agentReference;
	
		// --- Set the preferences for the Main Panel ---------------
		this.setLayout(null);
		
		// --- Prevent errors through empty agent references --------
		if (currAgentReference!=null) {
			// --- Find Agent in AgentConfig ------------------------
			if (currProject.agentConfig.containsKey(currAgentReference)==true) {
				
				// --- Which classes are configured for the Agent? -- 
				TreeMap<Integer,String> startObjectList = currProject.agentConfig.getReferencesAsTreeMap(currAgentReference);
				Vector<Integer> v = new Vector<Integer>(startObjectList.keySet()); 
				Collections.sort(v);
				
				// --- Take them to the local Vector ----------------
				Iterator<Integer> it = v.iterator();
				while (it.hasNext()) {
					Integer startPosition = it.next();
					String startObjectClass = startObjectList.get(startPosition);
					currOnotologyClassReferenceList.add(startObjectClass);
				}
				
				// --- Start building the GUI -----------------------
				this.buildGUI();
				// --- If wanted show some debug informations -------
				if (debug==true) {
					this.objectTreeShow();
				}
			}
		}
	}
	
	/**
	 * Constructor of this class by uising an instance of an ontology and 
	 * the reference of a needed class out of the ontology
	 * @param ontology
	 * @param ontologyClassReference
	 */
	public DynForm(Project project, String[] ontologyClassReferences) {

		super();
		this.currProject = project;

		// --- Set the preferences for the Main Panel ---------------
		this.setLayout(null);
		
		// --- Was something set? -----------------------------------
		if (ontologyClassReferences!= null && ontologyClassReferences.length>0) {
			// --- Take that to the local Vector --------------------
			for (int i = 0; i < ontologyClassReferences.length; i++) {
				this.currOnotologyClassReferenceList.add(ontologyClassReferences[i]);
			}

			// --- Start building the GUI -----------------------
			this.buildGUI();
			// --- If wanted show some debug informations -------
			if (debug==true) {
				this.objectTreeShow();
			}
			
		}
	}
	
	/**
	 * This class starts building the GUI
	 * @param agentReference
	 */
	public void buildGUI(){
		
		// --- Maybe a debug print to the console ---------
		if (debug==true && currOnotologyClassReferenceList.size()!=0) {
			System.out.println("Creating GUI");	
		}	
		
		// --- Iterate over the available Start-Objects ---
		for (int i = 0; i < currOnotologyClassReferenceList.size(); i++) {
			
			String startObjectClass = currOnotologyClassReferenceList.get(i);
			
			// --- Get the info about the slots --------------------
			OntologySingleClassDescription osc = currProject.ontologies4Project.getSlots4ClassAsObject(startObjectClass);
			if(osc!=null) {
				this.createGUI(osc, startObjectClass, this, 0, (DefaultMutableTreeNode) rootNode);
			} else {
				System.out.println("Could not get OntologySingleClassDescription for "+startObjectClass);
			}
		}
		// --- Add a last invisible component -------------
		this.addLastComponent();
		// --- Justify the Preferred Size of this Panle ---
		this.setPreferredSize(new Dimension(this.getBounds().width + 10, this.getBounds().height + 10));
	}
	
	/**
	 * This method adds a last component in order to scale the prefered size later on
	 */
	private void addLastComponent() {
		lastComponent = new JLabel();
		lastComponent.setBounds(new Rectangle(5, this.getHeight() + 10, 100, 25));
		this.add(lastComponent);
		this.setPanelBounds(this);
	}
	/**
	 * This Method sets the PreferredSize of this Panel according 
	 * to the Position of the 'submitButton'
	 */
	private void setPreferredSize() {
		int newHeight = lastComponent.getBounds().y + lastComponent.getBounds().height + 10; 
		this.setPreferredSize(new Dimension(this.getBounds().width, newHeight));
	}
	
	/**
	 * This method can be invoked to generate the instance of the current configuration  
	 * @return
	 */
	public void save(boolean fromForm) {
		
		if (fromForm==true) {
			this.setInstancesFromForm();	
		} else {
			this.setInstancesFromXML();
		}
	}
	
	/**
	 * 
	 */
	private void setInstancesFromForm() {
		
		int numOfChilds = rootNode.getChildCount();
		this.ontoArgsInstance = new Object[numOfChilds];
		this.ontoArgsXML = new String[numOfChilds];
		
		// ----------------------------------------------------------
		// --- Walk through the objectTree, which was generated ----- 
		// --- during the creation of the DynForm - object 		-----
		// ----------------------------------------------------------
		for (int i = 0; i < numOfChilds; i++) {
			
			// --- Get DynType (userObject) of this node ------------
			DefaultMutableTreeNode currNode =  (DefaultMutableTreeNode) rootNode.getChildAt(i);
			DynType dt = (DynType) currNode.getUserObject();
			String className = dt.getClassName();
			
			// --- Get the corresponding Ontology-Instance ----------			
			OntologyClassTreeObject octo = this.currProject.ontologies4Project.getClassTreeObject(className);
			Ontology onto = octo.getOntologyClass().getOntologyInstance();
			
			// --- Generate instance of this object -----------------
			Object obj = this.getNewClassInstance(className);
			
			// --- configure the object instance --------------------
			this.setObjectState(obj, currNode);
			
			// --- Remind object state as instance and XML ---------- 
			ontoArgsInstance[i] = obj;
			ontoArgsXML[i] = getXMLOfInstance(obj, onto);
			
		}
	}
	
	/**
	 * 
	 */
	private void setInstancesFromXML() {
		
		if (this.ontoArgsXML==null) return;
		
		int numOfXMLArgs = ontoArgsXML.length;
		this.ontoArgsInstance = new Object[numOfXMLArgs];
		
		// ----------------------------------------------------------
		// --- Walk through the objectTree, which was generated ----- 
		// --- during the creation of the DynForm - object 		-----
		// ----------------------------------------------------------
		int numOfChilds = rootNode.getChildCount();
		for (int i = 0; i < numOfChilds; i++) {
			
			// --- Get DynType (userObject) of this node ------------
			DefaultMutableTreeNode currNode =  (DefaultMutableTreeNode) rootNode.getChildAt(i);
			DynType dt = (DynType) currNode.getUserObject();
			String className = dt.getClassName();
			
			// --- Get the corresponding Ontology-Instance ----------			
			OntologyClassTreeObject octo = this.currProject.ontologies4Project.getClassTreeObject(className);
			Ontology onto = octo.getOntologyClass().getOntologyInstance();
			
			// --- Generate instance for this argument --------------
			if ((i+1)<=numOfXMLArgs) {
				Object obj = getInstanceOfXML(ontoArgsXML[i], onto);
				if (obj!=null) {
					// --- Set the current instance to the form -----
					this.setFormState(obj, currNode);
					// --- Remind object state as instance ----------
					ontoArgsInstance[i] = obj;
				}
			}

		} // --- end for ---
	}
	
	/**
	 * This method translates an object instance to a String by the
	 * given instance of the current ontology. The object must be a 
	 * part of this ontology. 
	 * @param obj
	 * @param onto
	 * @return
	 */
	private String getXMLOfInstance(Object obj, Ontology onto) {
		
		XMLCodec codec = new XMLCodec();
		String xmlRepresentation = null;
		try {
			xmlRepresentation = codec.encodeObject(onto, obj, true);
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}	
		if (debug) {
			System.out.println(xmlRepresentation);
		}
		return xmlRepresentation;
	}
	/**
	 * This method translates an XML String to an object instance by the
	 * given instance of the current ontology. The translated object must 
	 * be a part of this ontology. 
	 * @param xmlString
	 * @param onto
	 * @return
	 */
	private Object getInstanceOfXML(String xmlString, Ontology onto) {
		
		XMLCodec codec = new XMLCodec();
		Object objectInstance = null;
		
		try {
			objectInstance = codec.decodeObject(onto, xmlString);
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}		
		return objectInstance;
	}
	
	/**
	 * This method sets an object-instance to the state as configured in the form 
	 * @param object
	 * @param node
	 */
	private void setObjectState(Object object, DefaultMutableTreeNode node) {
		
		Method methodFound = null;
		Object[] subObjectOrValue = new Object[1];
		
		int numOfChilds = node.getChildCount();
		for (int i = 0; i < numOfChilds; i++) {
			
			DefaultMutableTreeNode currNode =  (DefaultMutableTreeNode) node.getChildAt(i);
			DynType dt = (DynType) currNode.getUserObject();
			String typeName = dt.getTypeName();
			String className = dt.getClassName();
			OntologySingleClassSlotDescription oscsd = dt.getOSCD();
			
			methodFound = null;
			subObjectOrValue[0] = null;
			                 
			if (typeName.equals(DynType.typeClass) || typeName.equals(DynType.typeInnerClassType)) {
				// --- Generate Instance of this class --------------
				subObjectOrValue[0] = this.getNewClassInstance(className);
				// --- Set their values -----------------------------
				this.setObjectState(subObjectOrValue[0], currNode);				
				// --- Get the set-method for this slot -------------
				methodFound = this.getSingleSetMethod(oscsd);
				// --- Set the value for this slot ------------------
				
			} else if (typeName.equals(DynType.typeRawType)) {
				// --- Get the value of the underlying field --------
				subObjectOrValue[0] = this.getSingleValue(dt);
				// --- Get the set-method for this slot -------------
				methodFound = this.getSingleSetMethod(oscsd);
				
			} else if (typeName.equals(DynType.typeCyclic)) {
				// --- Nothing to do here ---------------------------
			}
			
			// -----------------------------------------------------------
			// --- Invoke the method with the object/value ---------------
			// -----------------------------------------------------------
			if (methodFound!=null && subObjectOrValue!=null) {
				// --- Invoke the found method to the current object ----- 
				try {
					methodFound.setAccessible(true);
					@SuppressWarnings("unused")
					Object result = methodFound.invoke(object, subObjectOrValue);
					
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			// -----------------------------------------------------------
			
		} // --- end for ---
		
	}
	
	/**
	 * This method sets the object state to the form
	 */
	private void setFormState(Object object, DefaultMutableTreeNode node) {
		
		int numOfChilds = node.getChildCount();
		for (int i = 0; i < numOfChilds; i++) {
			
			DefaultMutableTreeNode currNode =  (DefaultMutableTreeNode) node.getChildAt(i);
			DynType dt = (DynType) currNode.getUserObject();
			String typeName = dt.getTypeName();
			OntologySingleClassSlotDescription oscsd = dt.getOSCD();
			
			// -----------------------------------------------------------
			// --- Invoke the method to get the object/value -------------
			// -----------------------------------------------------------
			Object slotValue = null;
			Method methodFound = this.getGetMethod(oscsd, object);
			if (methodFound!=null & this.hasObjectMethod(object, methodFound)) {
				
				// --- Invoke the found method to the current object ----- 
				try {
					methodFound.setAccessible(true);
					slotValue = methodFound.invoke(object, new Object[0]);
					
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}

			// --- work on the slot value -------------------------------
			if (slotValue!=null) {
				// ------------------------------------------------------
				// --- If we have a ArrayList instance here -------------
				// ------------------------------------------------------
				if (slotValue instanceof String[]) {
					ArrayList newList = new ArrayList();
					String[] tmp = (String[]) slotValue;
					for (int j = 0; j < tmp.length; j++) {
						newList.add(tmp[j]);
					}
					slotValue = newList;
				}
				// ------------------------------------------------------
				// --- If we have a list instance here ------------------
				// ------------------------------------------------------
				boolean multipleValues = false;
				if (slotValue instanceof List) {
					if (((List) slotValue).size()==1) {
						slotValue = ((List) slotValue).get(0);
						multipleValues = false;
					} else {
						multipleValues = true;
					}
				} else {
					multipleValues = false;
				}
				// ------------------------------------------------------
				// --- Now, set the value to the form -------------------
				// ------------------------------------------------------
				if (typeName.equals(DynType.typeClass) || typeName.equals(DynType.typeInnerClassType)) {
					// --- Set their values -----------------------------
					if (multipleValues==true) {
						System.out.println("TODO: Real multiple Instances");	
					} else {
						this.setFormState(slotValue, currNode);	
					}
					
				} else if (typeName.equals(DynType.typeRawType)) {
					// --- Get the value of the underlying field --------
					if (multipleValues==true) {
						System.out.println("TODO: Real multiple Instances");
					} else {
						this.setSingleValue(dt, slotValue);	
					}
					
				} else if (typeName.equals(DynType.typeCyclic)) {
					// --- Nothing to do here ---------------------------
				}	
			}
			
		} // --- end for ---
	}
	
	/**
	 * This Method checks if an instance of an object has a asked method
	 * @param obj
	 * @param methode2check
	 * @return
	 */
	private boolean hasObjectMethod(Object obj, Method methode2check) {
		
		Method[] meths = obj.getClass().getMethods();
		for (int i = 0; i < meths.length; i++) {
			Method meth = meths[i];
			if (meth.equals(methode2check)) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * This method reads the value given through the DynType and  
	 * the inherent reference to the Swing component
	 * @param dt
	 * @return
	 */
	private Object getSingleValue(DynType dt) {
		
		Object returnValue = null;
		
		String valueType = dt.className;
		if(valueType.equals("String")){
			JTextField jt = dt.fieldValue;
			returnValue = jt.getText();
			
		} else if(valueType.equals("float")){
			JTextField jt = dt.fieldValue;
			String stringValue = jt.getText();
			stringValue = stringValue.replace(",", ".");
			if (stringValue==null | stringValue.equals("")) {
				returnValue = 0;
			} else {
				returnValue = new Float(stringValue);	
			}
			
		} else if(valueType.equals("int")){
			JTextField jt = dt.fieldValue;
			String stringValue = jt.getText();
			stringValue = stringValue.replace(",", ".");
			if (stringValue==null | stringValue.equals("")) {
				returnValue = 0;
			} else {
				if (stringValue.contains(".")) {
					returnValue = Math.round(new Double(stringValue));
					jt.setText(returnValue.toString());
				} else {
					returnValue = new Integer(stringValue);	
				}
			}
			
		} else if(valueType.equals("boolean")){
			JTextField jt = dt.fieldValue;
			returnValue = jt.getText();

		}
		return returnValue;
	}
	
	/**
	 * This method sets the value given through the DynType and  
	 * the inherent reference to the Swing component
	 * @param dt
	 * @return
	 */
	private void setSingleValue(DynType dt, Object obj) {
		
		String valueType = dt.className;
		if(valueType.equals("String")){
			JTextField jt = dt.fieldValue;
			jt.setText(obj.toString());
			
		} else if(valueType.equals("float")){
			JTextField jt = dt.fieldValue;
			jt.setText(obj.toString());
			
		} else if(valueType.equals("int")){
			JTextField jt = dt.fieldValue;
			jt.setText(obj.toString());
			
		} else if(valueType.equals("boolean")){
			JTextField jt = dt.fieldValue;
			jt.setText(obj.toString());

		}
	}
	
	/**
	 * This method selects the method of the slot, which has to be 
	 * used to set this single slot value
	 * @param currOSCSD
	 * @return
	 */
	private Method getSingleSetMethod(OntologySingleClassSlotDescription currOSCSD) {
		
		String slotName = currOSCSD.getSlotName();
		String methodSearcher = null;
		Method methodFound = null; 
		
		// --- define what we look for --------------------------
		if (currOSCSD.getSlotCardinality().equals("multiple")) {
			// --- multiple instances possible for this slot ----
			// --- => "add" needed 
			methodSearcher = "add" + slotName;
		} else {
			// --- single instances possible for this slot ------
			// --- => "set" needed
			methodSearcher = "set" + slotName;
		}
		methodSearcher = methodSearcher.toLowerCase();

		// --- run through the list of all known methods -------- 
		Vector<String> keyVec = new Vector<String>(currOSCSD.getSlotMethodList().keySet());
		for (Iterator<String> iterator = keyVec.iterator(); iterator.hasNext();) {
			
			String methKey = iterator.next(); 
			Method meth = currOSCSD.getSlotMethodList().get(methKey);
			String methName = meth.getName();
			
			if (methName.toLowerCase().equals(methodSearcher)) {
				methodFound = meth;
				break;
			}
		}
		return methodFound;		
	}
	
	/**
	 * This method selects the method of the slot, which has to be 
	 * used to Get the current values of the current object instance
	 * @param currOSCSD
	 * @return
	 */
	private Method getGetMethod(OntologySingleClassSlotDescription currOSCSD, Object object) {
		
		String slotName = currOSCSD.getSlotName();
		String methodSearcher = null;
		Method methodFound = null; 
		
		// -----------------------------------------------------
		// --- Special case AID --------------------------------
		if (object instanceof jade.core.AID) {
			Method[] meths = jade.core.AID.class.getMethods();
			for (int i = 0; i < meths.length; i++) {
				Method meth = meths[i];
				if (slotName.equals("addresses")) {
					if (meth.getName().equals("getAddressesArray")) {
						return meth;
					}
				}
			}
		}
		
		// --- define what we look for --------------------------
		if (currOSCSD.getSlotCardinality().equals("multiple")) {
			// --- multiple instances possible for this slot ----
			// --- => "add" needed 
			methodSearcher = "get" + slotName;
		} else {
			// --- single instances possible for this slot ------
			// --- => "set" needed
			methodSearcher = "get" + slotName;
		}
		methodSearcher = methodSearcher.toLowerCase();

		// --- run through the list of all known methods -------- 
		Vector<String> keyVec = new Vector<String>(currOSCSD.getSlotMethodList().keySet());
		for (Iterator<String> iterator = keyVec.iterator(); iterator.hasNext();) {
			
			String methKey = iterator.next(); 
			Method meth = currOSCSD.getSlotMethodList().get(methKey);
			String methName = meth.getName();
			
			if (methName.toLowerCase().equals(methodSearcher)) {
				methodFound = meth;
				break;
			}
		}
		return methodFound;		
	}

	/**
	 * This method returns a new instance of a given class given by its full classname
	 * @param className
	 * @return
	 */
	private Object getNewClassInstance(String className) {
		
		Class<?> clazz = null;
		Object obj = null;
		try {
			clazz = Class.forName(className);
			obj = clazz.newInstance();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	
	
	/**
	 * Shows the object tree in a visual way in  a dialog
	 */
	private void objectTreeShow() {
		DynTreeViewer dtv = new DynTreeViewer(objectTree);
		dtv.setVisible(true);
	}

	/**
	 * 
	 * @param pan
	 */
	public void setPanelBounds(JPanel pan){
		
		int maxX = 0;
		int maxY = 0;
		
		Component[] components = pan.getComponents();
		for (int i = 0; i < components.length; i++) {
			
			int currXMax = components[i].getX() + components[i].getWidth();
			int currYMax = components[i].getY() + components[i].getHeight();

			if (currXMax > maxX) maxX = currXMax;
			if (currYMax > maxY) maxY = currYMax;
		}
		
		maxX += 10;
		maxY += 2;
		pan.setBounds(new Rectangle(maxX, maxY));

	}
	
	/**
	 * This method adds the class to a panel and initiates to add
	 * possible inner classes and fields also to the panel or its subpanels
	 * @param osc
	 * @param startObjectClassName
	 * @param pan
	 * @param tiefe
	 */
	public void createGUI(OntologySingleClassDescription osc, String startObjectClass, JPanel pan, int tiefe, DefaultMutableTreeNode node){
		
		String startObjectClassName = startObjectClass.substring(startObjectClass.lastIndexOf(".") + 1, startObjectClass.length());
		String startObjectPackage = startObjectClass.substring(0, startObjectClass.lastIndexOf("."));

		JLabel objectLabelName = new JLabel();		
		if (osc != null) {
			// --- if we are on the main panel -> add the class name to it !
			// --- Create a JPanel in which the class name (JLabel) and its innerclasses
			// --- and/or fields are added instead of mainPanel - class name - innerclasses/fields
			
			if(tiefe == 0){
				// --- Set the label for the class --- //
				objectLabelName.setBounds(new Rectangle(10, pan.getHeight()+5 , 200, 16));
				objectLabelName.setText(startObjectClassName);
				objectLabelName.setFont(new Font(objectLabelName.getFont().getName(),Font.BOLD,objectLabelName.getFont().getSize()));
				
				// --- Define a node for current class ------------------
				DynType dynType = new DynType(null, DynType.typeClass, startObjectClass, startObjectClassName);
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dynType);
				node.add(newNode);
				node = newNode;
				
			} else {
				// --- Set the label for the class --- //
				objectLabelName.setBounds(new Rectangle(10, 23 , 200, 16));
				objectLabelName.setText(startObjectClassName.substring(startObjectClassName.lastIndexOf(".")+1));
				objectLabelName.setFont(new Font(objectLabelName.getFont().getName(),Font.BOLD,objectLabelName.getFont().getSize()));
				
			}

			// --- add the name to the mainPanel ----------
			pan.add(objectLabelName);
			this.setPanelBounds(pan);
			
			// --- go through each field / inner class ----
			Iterator<OntologySingleClassSlotDescription> iterator = osc.osdArr.iterator();
			while (iterator.hasNext()) {
				
				OntologySingleClassSlotDescription oscsd = iterator.next();
				
				String dataItemCardinality = oscsd.getSlotCardinality();
				String dataItemName = oscsd.getSlotName();
				String dataItemVarType = oscsd.getSlotVarType();
				String clazz = dataItemVarType;
				if(dataItemVarType.matches("Instance of (.)*")){
					clazz = dataItemVarType.substring(12);	
				}
				
				// --------------------------------------------------------------------------------
				// --- if we have a field to which an object is assigned --> inner class ----------
				// --------------------------------------------------------------------------------
				if(isSpecialType(clazz)){
					// --- Was this classs already displayed in the current tree path? --
					String clazzCheck = null;
					if (clazz.equals("AID")) {
						clazzCheck = OntologyClassTree.BaseClassAID;
					} else {
						clazzCheck = startObjectPackage+"."+clazz;	
					}					
					if(objectAlreadyInPath(clazzCheck, node) == false) {
						// --- Create Sub-Panel for the according class -----------------
						this.createInnerElements(oscsd, dataItemName, dataItemVarType, dataItemCardinality, clazzCheck, tiefe+1, pan, node);
					} else {
						// --- Create Sub-Panel that shows the cylic reference ----------
						this.createOuterDeadEnd(oscsd, dataItemName, dataItemVarType, dataItemCardinality, clazz, pan, tiefe, node);
					}
					
				} else {
					// --- Here we have a field with a final type (String, int, ...) ----
					this.createOuterElements(oscsd, dataItemName, dataItemVarType, dataItemCardinality, clazz, pan, tiefe, node);
				}
				// --------------------------------------------------------------------------------
			}
		
		} else {
			System.out.println("Could not create DefaultTableModel ("+startObjectClassName+")");
		}
		
	}	

	/**
	 * 
	 * @param objectClass
	 * @param currNode
	 * @return
	 */
	public boolean objectAlreadyInPath(String objectClass, DefaultMutableTreeNode startNode){
		
		DefaultMutableTreeNode currNode = startNode; 
		while (currNode!=this.rootNode) {
			DynType dt = (DynType) currNode.getUserObject();
			if (dt.getClassName().equals(objectClass)) {
				return true;
			}
			currNode = (DefaultMutableTreeNode) currNode.getParent();
		}
		return false;
	}

	/**
	 * This method creates inner elements (for inner classes)
	 * @param dataItemName
	 * @param dataItemCardinality
	 * @param startObjectClassName
	 * @param tiefe
	 * @param pan
	 * @param node 
	 */
	public void createInnerElements(OntologySingleClassSlotDescription oscsd, String dataItemName, String dataItemVarType, String dataItemCardinality, String startObjectClassName, int tiefe, final JPanel pan, DefaultMutableTreeNode node){
		
		DynType dynType = new DynType(oscsd, DynType.typeInnerClassType, startObjectClassName, dataItemName);
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dynType);
		node.add(newNode);
		node = newNode;
		
		// --- create a JPanel which will contain further inner classes and fields
		final JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		int innerX  = 0;

		// set the value of how much the panel shall be shifted (Einrückung)
		if(tiefe > 1)
			innerX = (einrueckungProUntereEbene*(tiefe-1));
		else
			innerX = (einrueckungProUntereEbene*(tiefe));
		dataPanel.setBounds(new Rectangle(innerX, 0 , 270, 20));
		dataPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		dataPanel.setToolTipText(startObjectClassName + " Inner Panel");
		
		// --- create two JLabels: first displays the field name
		// --- the second displays the inner class name
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText(dataItemName + " ["+dataItemVarType+"]");
		valueFieldText.setBounds(new Rectangle(10, 5, 300, 16));
		
		dataPanel.add(valueFieldText, null);
		this.setPanelBounds(dataPanel);

		// --- if the inner class has got a multi cardinality create an add-button		
		if(dataItemCardinality.equals("multiple")) {

			JButton multipleButton = new JButton("+");
			multipleButton.setBounds(new Rectangle(310, 2, 35, 25));
			multipleButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//showPanelComponents(dataPanel);
					addMultiple(dataPanel);
				}
			});	
			dataPanel.add(multipleButton, null);
			this.setPanelBounds(dataPanel);
		}
		
		// --- create the inner fields of the current inner class
		OntologySingleClassDescription osc = currProject.ontologies4Project.getSlots4ClassAsObject(startObjectClassName);
		this.createGUI(osc, startObjectClassName, dataPanel, tiefe, node);
		
		// --- set the correct height of the parent of this inner class according to the
		// --- inner class's height
		Rectangle r = dataPanel.getBounds();
		dataPanel.setBounds(10, pan.getHeight(), r.width, r.height);
		
		// --- finally add the inner class to its parent panel
		pan.add(dataPanel);
		this.setPanelBounds(pan);
		
	}

	/**
	 * This method creates the panels for fields which have no inner classes
	 * @param dataItemName
	 * @param dataItemCardinality
	 * @param pan
	 * @param tiefe
	 * @param node 
	 */
	public void createOuterElements(OntologySingleClassSlotDescription oscsd, String dataItemName, String dataItemVarType, String dataItemCardinality, String className, JPanel pan, int tiefe, DefaultMutableTreeNode node){
		
		// --- this outer element has no parents which are inner classes
		// --- so its added to the mainPanel
		final JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		dataPanel.setToolTipText(dataItemName + "-Panel");
		
		// --- add a JLabel to display the field's name
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText(dataItemName + " ["+dataItemVarType+"]");
		valueFieldText.setBounds(new Rectangle(0, 4, 130, 16));
		
		// --- add a JTextField for the value being entered
		// --- TODO check the type of the field and generate the right 
		// --- valueFields (Textfield, Checkbox (for boolean) , ... )
		JTextField valueField = new JTextField();
		valueField.setBounds(new Rectangle(140, 0, 100, 25));
		
		// --- add both GUI elements to the panel
		dataPanel.add(valueFieldText, null);
		dataPanel.add(valueField);
		this.setPanelBounds(dataPanel);
		
		// --- if the inner class has got a multi cardinality create an add-button
		if(dataItemCardinality.equals("multiple")) {

			JButton multipleButton = new JButton("+");
			multipleButton.setBounds(new Rectangle(260, 0, 35, 25));
			multipleButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//showPanelComponents(dataPanel);
					addMultiple(dataPanel);
				}
			});	
			dataPanel.add(multipleButton, null);
			this.setPanelBounds(dataPanel);
		}
		
		DynType dynType = new DynType(oscsd, DynType.typeRawType, className, dataItemName, valueField);
		node.add(new DefaultMutableTreeNode(dynType));
		
		// --- set the new position (increment the height) for the parent panel of the 
		// --- newly created panel
		Rectangle pos = dataPanel.getBounds();
		pos.x = 10;//tiefe * einrueckungProUntereEbene;
		pos.y = pan.getHeight();
		dataPanel.setBounds(pos);

		pan.add(dataPanel);
		this.setPanelBounds(pan);
		
	}
	
	public void createOuterDeadEnd(OntologySingleClassSlotDescription oscsd, String dataItemName, String dataItemVarType, String dataItemCardinality, String className, JPanel pan, int tiefe, DefaultMutableTreeNode node){
		
		// --- this outer element has no parents which are inner classes
		// --- so its added to the mainPanel
		final JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		dataPanel.setToolTipText(dataItemName + "-Panel");
		
		// --- add a JLabel to display the field's name
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText("<html>" + dataItemName + " ["+dataItemVarType+"] - <b>" + Language.translate("Zyklisch !") + "</b></html>");
		valueFieldText.setBounds(new Rectangle(0, 4, 240, 16));
		
		// --- add both GUI elements to the panel
		dataPanel.add(valueFieldText, null);
		this.setPanelBounds(dataPanel);
		
		DynType dynType = new DynType(oscsd, DynType.typeCyclic, className, dataItemName, null);
		node.add(new DefaultMutableTreeNode(dynType));
		
		// --- set the new position (increment the height) for the parent panel of the 
		// --- newly created panel
		Rectangle pos = dataPanel.getBounds();
		pos.x = 10;//tiefe * einrueckungProUntereEbene;
		pos.y = pan.getHeight();
		dataPanel.setBounds(pos);

		pan.add(dataPanel);
		this.setPanelBounds(pan);
		
	}
	
	/**
	 * This method creates a copy of the passed JPanel and adds it
	 * @param dataPanel
	 */
	public void addMultiple(JPanel dataPanel){
		
		JPanel pan = (JPanel) dataPanel.getParent();
		int dataPanelHeight = dataPanel.getHeight();
		pan.setBounds(pan.getX(), pan.getY(), pan.getWidth(), pan.getHeight() + dataPanelHeight);
		
		// --- move surrounding panels into the y-direction by the the height of the
		// --- JPanel copy
		this.moveOtherPanels4Multiple(dataPanel, true);
		this.setPreferredSize();
		
		JPanel dataPanelCopy = new JPanel();
		dataPanelCopy.setLayout(null);
		dataPanelCopy.setBorder(dataPanel.getBorder());
		
		// --- now create the copy
		this.createPanelCopy(dataPanelCopy,dataPanel);
		dataPanelCopy.setBounds(dataPanel.getX(), dataPanel.getY() + dataPanelHeight, dataPanel.getWidth(), dataPanel.getHeight());
		
		// --- add the created copy
		pan.add(dataPanelCopy);
		
		// --- refresh the GUI
		this.validate();
	}
	
	/**
	 * This method removes the passed JPanel 
	 * @param dataPanel
	 */
	public void removeMultiple(JPanel dataPanel){
		
		JPanel parent = (JPanel) dataPanel.getParent();
		dataPanel.setVisible(false);
		moveOtherPanels4Multiple(dataPanel, false);
		parent.remove(dataPanel);
		parent.setBounds(parent.getX(), parent.getY(), parent.getWidth(), parent.getHeight() - dataPanel.getHeight());
		parent.validate();

		this.setPreferredSize();
		
	}
	
	/**
	 * This method tries to create a copy of the original Panel
	 * @param newPanel
	 * @param originalPanel
	 */
	private void createPanelCopy(final JPanel newPanel, JPanel originalPanel) {
		
		Component[] c = originalPanel.getComponents();
		for (Component component : c) {
			if(component instanceof JPanel) {
				JPanel pan = new JPanel();
				pan.setLayout(null);
				JPanel panOrig = (JPanel) component;
				pan.setBounds(panOrig.getX(), panOrig.getY(), panOrig.getWidth(), panOrig.getHeight());
				pan.setBorder(panOrig.getBorder());
				newPanel.add(pan);
				this.createPanelCopy(pan, (JPanel) component);
				
			} else if(component instanceof JLabel) {
				JLabel label = new JLabel();
				JLabel origLabel = (JLabel) component;
				label.setBounds(origLabel.getX(), origLabel.getY(), origLabel.getWidth(), origLabel.getHeight());
				label.setText(origLabel.getText());
				label.setFont(origLabel.getFont());
				newPanel.add(label);
				
			} else if (component instanceof JTextField) {
				JTextField text = new JTextField();
				JTextField origText = (JTextField) component;
				text.setBounds(origText.getX(), origText.getY(), origText.getWidth(), origText.getHeight());
				newPanel.add(text);
				
			} else if (component instanceof JButton) {
				JButton buttonOrig = (JButton) component;
				JButton removeComponent = new JButton();
				removeComponent.setBounds(buttonOrig.getX(), buttonOrig.getY(), buttonOrig.getWidth(), buttonOrig.getHeight());
				removeComponent.setText("-");
				removeComponent.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						removeMultiple(newPanel);
					}
				});
				newPanel.add(removeComponent);
				
			}
		}
	}

	/**
	 * This method moves the surrounding panels into the + Y-Direction or into 
	 * the - Y-Direction
	 * @param dataPanel
	 * @param add
	 */
	public void moveOtherPanels4Multiple(JPanel dataPanel, boolean add){
		
		if (dataPanel!=null && dataPanel.getParent() instanceof JPanel) {
			Component[] panChilds = ((JPanel) dataPanel.getParent()).getComponents();
			boolean flag = false;
			// --- go through the surrounding panels of the datapanel and move it
			// --- either to + Y-Dir (the panel is added) or to the - Y-Dir (the panel) 
			// --- shall be removed
			for (Component component : panChilds) {
				if(component == dataPanel) {
					flag = true;
				}
				if(add){
					if(flag && component != dataPanel){
						component.setBounds(component.getX(), component.getY() + dataPanel.getHeight(), component.getWidth(), component.getHeight());
					}
					
				} else {
					if(component.getY() > dataPanel.getY()) {
						component.setBounds(component.getX(), component.getY() - dataPanel.getHeight(), component.getWidth(), component.getHeight());
					}
					
				}
			}
			
			if(dataPanel.getParent() instanceof JPanel)
				moveOtherPanels4Multiple((JPanel)dataPanel.getParent(), add);
		}	
		
	}
	
	/**
	 * This method checks if the type of the field is a raw type
	 * (String, int, float, ...)
	 * @param valueType
	 * @return
	 */
	private boolean isSpecialType(String valueType){
		boolean flag = true;
		if(valueType.equals("String")){
			flag = false;
		} else if(valueType.equals("float")){
			flag = false;
		} else if(valueType.equals("int")){
			flag = false;
		} else if(valueType.equals("boolean")){
			flag = false;
		}
		return flag;
	}

	/**
	 * @return the agentArgsInstance
	 */
	public Object[] getOntoArgsInstance() {
		return ontoArgsInstance;
	}
	/**
	 * @param agentArgsInstance the agentArgsInstance to set
	 */
	public void setOntoArgsInstance(Object[] ontoArgsInstance) {
		this.ontoArgsInstance = ontoArgsInstance;
	}

	/**
	 * @return the agentArgsXML
	 */
	public String[] getOntoArgsXML() {
		return ontoArgsXML;
	}
	/**
	 * @param agentArgsXML the agentArgsXML to set
	 */
	public void setOntoArgsXML(String[] ontoArgsXML) {
//		int numOntoArgsXMLInput = ontoArgsXML.length;
//		int numOntoArgsXMLHere = this.ontoArgsXML.length;
//		if (numOntoArgsXMLInput == numOntoArgsXMLHere) {
			this.ontoArgsXML = ontoArgsXML;
			this.setInstancesFromXML();
//		}
	}
	
}
