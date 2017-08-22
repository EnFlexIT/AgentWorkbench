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
package agentgui.core.ontologies.gui;

import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import agentgui.core.application.Application;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.ontologies.OntologyClassTreeObject;
import agentgui.core.ontologies.OntologySingleClassSlotDescription;
import agentgui.core.ontologies.OntologyVisualisationHelper;
import agentgui.core.project.AgentStartArgument;
import agentgui.core.project.AgentStartConfiguration;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelDateBased;
import de.enflexit.common.ExceptionHandling;
import de.enflexit.common.classLoadService.ClassLoadServiceUtility;
import de.enflexit.common.swing.KeyAdapter4Numbers;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

/**
 * This class can be used in order to generate a Swing based user form, that represents
 * the structure of one or more ontology-classes. Especially this abstract class is used
 * in order reduce the lines of code in one class. The basically used class is the class
 * {@link DynForm}.
 * 
 * @see DynForm
 * 
 * @author Marvin Steinberg - University of Duisburg - Essen
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class DynFormBase {

	/** Necessary parameter that comes from the constructor */
	protected OntologyVisualisationHelper ontologyVisualisationHelper = null;
	/** Constructor parameter  */
	protected AgentStartConfiguration agentStartConfiguration = null;

	/** Based on this vector the user interface will be created */
	protected Vector<AgentStartArgument> currOntologyClassReferenceList = new Vector<AgentStartArgument>();


	/** For debugging purposes  */
	protected boolean debug = false;
	/** For debugging purposes - appears if debug is set true  */
	private DynTreeViewer dtv = null;
	/** True, if no class has to be visualised on the DynForm */
	private boolean emptyForm = true;
	
	/** The KeyAdapter for float values */
	protected KeyAdapter4Numbers numWatcherFloat = new KeyAdapter4Numbers(true);
	/** The KeyAdapter for Integer values */
	protected KeyAdapter4Numbers numWatcherInteger = new KeyAdapter4Numbers(false);
 
	/** The TreeModel for the class hierarchy of the current ontology configuration */ 
	protected DefaultTreeModel objectTree;
	/** The root node for the class hierarchy of the current ontology configuration */ 
	private DefaultMutableTreeNode rootNode;
	
	
	/** Reminder Hash that stores an {@link OntologyClassWidget} for a specific tree node */
	private HashMap<DefaultMutableTreeNode, OntologyClassWidget> ontologyClassWidget = null;
	/** Reminder Hash that stores the tree node of a given {@link DynType} */
	private HashMap<DynType, DefaultMutableTreeNode> treeNodesByDynType = null;
	
	// --- In case that one deals with an environment (e. g. for the current time format ----------
	private EnvironmentController environmentController = null;


	/** The instances that are to be created by the DynForm */
	protected Object[] ontoArgsInstance = null; 
	/** The XML strings that are to be created by the DynForm */
	protected String[] ontoArgsXML = null; 

	
	// --------------------------------------------------------------
	// --- START - Method set for the DynForm -----------------------
	// --------------------------------------------------------------
	
	/**
	 * Gets the ontology class reference list.
	 * @return the ontology class reference list
	 */
	public Vector<AgentStartArgument> getOntologyClassReferenceList() {
		return this.currOntologyClassReferenceList;
	}
	
	
	/**
	 * Returns the object tree with all elements to display.
	 * @return the object tree
	 */
	protected DefaultTreeModel getObjectTree() {
		if (objectTree==null) {
			objectTree = new DefaultTreeModel(this.getRootNode());
		}
		return this.objectTree;
	}
	/**
	 * Returns the root node of the object tree.
	 * @return the root node
	 * @see #getObjectTree()
	 */
	protected DefaultMutableTreeNode getRootNode() {
		if (rootNode==null) {
			rootNode  = new DefaultMutableTreeNode("Root");
		}
		return rootNode;
	}
	
	/**
	 * Shows the object tree in a visual way in a JDialog, if the internal debug value is set to be true.
	 */
	protected void showObjectTree() {
		this.dtv = new DynTreeViewer(this.getObjectTree());
		this.dtv.setVisible(true);
	}

	
	/**
	 * Returns the OntologyClassWidget's for all current OntologyClassVisualisation's.
	 * @return the HashMap<DefaultMutableTreeNode, OntologyClassWidget>
	 */
	public HashMap<DefaultMutableTreeNode, OntologyClassWidget> getOntologyClassWidgets() {
		if (this.ontologyClassWidget==null) {
			this.ontologyClassWidget = new HashMap<DefaultMutableTreeNode, OntologyClassWidget>();
		}
		return this.ontologyClassWidget;
	}
	/**
	 * Returns the form component for a OntologyClassVisualisation.
	 * @param node the current node
	 * @return the corresponding OntologyClassWidget 
	 */
	public OntologyClassWidget getOntologyClassWidget(DefaultMutableTreeNode node) {
		return this.getOntologyClassWidgets().get(node);
	}
	
	/**
	 * Returns the HshMap of tree nodes by DynType.
	 * @return the HshMap of tree nodes by DynType
	 */
	public HashMap<DynType, DefaultMutableTreeNode> getTreeNodesByDynType() {
		if (this.treeNodesByDynType==null) {
			this.treeNodesByDynType = new HashMap<DynType, DefaultMutableTreeNode>();
		}
		return this.treeNodesByDynType;
	}
	/**
	 * Returns a tree node, specified by the user object ({@link DynType}).
	 *
	 * @param dynType the DynType
	 * @return the object tree node
	 */
	public DefaultMutableTreeNode getTreeNodeByDynType(DynType dynType) {
		return this.getTreeNodesByDynType().get(dynType);
	}
	
	/**
	 * Indicates if the created form is empty.
	 * @return the emptyForm
	 */
	public boolean isEmptyForm() {
		return emptyForm;
	}
	/**
	 * Sets the indicator if the DynForm is empty.
	 * @param isEmptyForm the indicator if the DynForm is empty or not
	 */
	protected void setIsEmptyForm(boolean isEmptyForm) {
		this.emptyForm = isEmptyForm;
	}
	
	/**
	 * This method checks if the type of the field is a raw type
	 * (String, int, float, ...)
	 *
	 * @param valueType the value type
	 * @return true, if is special type
	 */
	protected boolean isRawType(String valueType){
		boolean flag = false;
		if(valueType.equals("String")){
			flag = true;
		} else if(valueType.equals("float")){
			flag = true;
		} else if(valueType.equals("Float")){
			flag = true;
		} else if(valueType.equals("int")){
			flag = true;
		} else if(valueType.equals("Integer")){
			flag = true;
		} else if(valueType.equals("boolean")){
			flag = true;
		}
		return flag;
	}
	/**
	 * This method creates the JComponent, which will be
	 * displayed for an ontology slot.
	 *
	 * @param valueType the value type
	 * @return the visual component
	 */
	public JComponent getVisualComponent(String valueType) {
	
		JComponent valueField = null;
		if(valueType.equals("String")){
			valueField = new JTextField();
			valueField.setBounds(new Rectangle(140, 0, 170, 27));
			
		} else if(valueType.equals("float") || valueType.equals("Float")){
			valueField = new JTextField("0.0");
			valueField.setBounds(new Rectangle(140, 0, 100, 26));
			valueField.addKeyListener(this.numWatcherFloat);
			
		} else if(valueType.equals("int") || valueType.equals("Integer")){
			valueField = new JTextField(0);
			valueField.setBounds(new Rectangle(140, 0, 100, 26));
			valueField.addKeyListener(this.numWatcherInteger);
			
		} else if(valueType.equals("boolean")){
			valueField = new JCheckBox();
			valueField.setBounds(new Rectangle(140, 0, 25, 25));
		}
		return valueField;
	}
	
	/**
	 * Sets the current environment controller.
	 * @param environmentController the new environment controller
	 */
	public void setEnvironmentController(EnvironmentController environmentController) {
		this.environmentController = environmentController;
	}
	/**
	 * Returns the current environment controller.
	 * @return the environment controller
	 */
	public EnvironmentController getEnvironmentController() {
		return environmentController;
	}
	
	/**
	 * Returns the default time/date  format.
	 * @return the default time format
	 */
	public String getDefaultTimeFormat() {
		String defaultTimeModelFormat = TimeModelDateBased.DEFAULT_TIME_FORMAT;
		if (this.environmentController!=null) {
			TimeModel timeModel = this.environmentController.getTimeModel();
			if (timeModel!=null) {
				if (timeModel instanceof TimeModelDateBased) {
					defaultTimeModelFormat = ((TimeModelDateBased)timeModel).getTimeFormat();
				}	
			}
		}
		return defaultTimeModelFormat;
	}
	
	/**
	 * This method can be invoked to generate the instance of the current configuration.
	 * @param fromForm the from form
	 */
	public void save(boolean fromForm) {
		if (fromForm==true) {
			this.setInstancesFromForm();	
		} else {
			this.setInstancesFromXML();
		}
	}
	
	/**
	 * This method creates the XML form from the instances.
	 */
	protected void setXMLFromInstances(){
	
		int numOfChilds = this.getRootNode().getChildCount();
		this.ontoArgsXML = new String[numOfChilds];
		
		// ----------------------------------------------------------
		// --- Walk through the objectTree, which was generated ----- 
		// --- during the creation of the DynForm - object 		-----
		// ----------------------------------------------------------
		for (int i = 0; i < numOfChilds; i++) {
			
			// --- Get DynType (userObject) of this node ------------
			DefaultMutableTreeNode currNode =  (DefaultMutableTreeNode) this.getRootNode().getChildAt(i);
			DynType dt = (DynType) currNode.getUserObject();
			String className = dt.getClassName();
			
			// --- Get the corresponding Ontology-Instance ----------			
			OntologyClassTreeObject octo = this.ontologyVisualisationHelper.getClassTreeObject(className);
			Ontology onto = octo.getOntologyClass().getOntologyInstance();
						
			// --- Generate XML of this object ----------------------
			ontoArgsXML[i] = this.getXMLOfInstance(this.ontoArgsInstance[i], onto);
			
		}
		
	}
	
	/**
	 * This method reads the current XML configuration of the
	 * arguments, fills the form and creates the instances.
	 */
	protected void setInstancesFromXML() {
		this.ontoArgsInstance = this.getInstancesFromXML(this.ontoArgsXML);
	}

	/**
	 * This method reads the given XML configuration of the arguments, fills 
	 * the form and creates the instances.
	 *
	 * @param ontoArgsXML the ontology arguments as String array containing XML
	 * @return the concrete instances from the XML argument description
	 */
	protected Object[] getInstancesFromXML(String[] ontoArgsXML) {
		
		if (ontoArgsXML==null) return null;
		
		int numOfXMLArgs = ontoArgsXML.length;
		Object[] newOntoArgsInstance = new Object[numOfXMLArgs];
		
		// ----------------------------------------------------------
		// --- Walk through the objectTree, which was generated ----- 
		// --- during the creation of the DynForm - object 		-----
		// ----------------------------------------------------------
		int numOfChilds = this.getRootNode().getChildCount();
		for (int i = 0; i < numOfChilds; i++) {
			
			// --- Get DynType (userObject) of this node ------------
			DefaultMutableTreeNode currNode =  (DefaultMutableTreeNode) this.getRootNode().getChildAt(i);
			DynType dt = (DynType) currNode.getUserObject();
			String className = dt.getClassName();
			
			// --- Get the corresponding Ontology-Instance ----------			
			OntologyClassTreeObject octo = this.ontologyVisualisationHelper.getClassTreeObject(className);
			Ontology ontology = octo.getOntologyClass().getOntologyInstance();
			
			// --- Generate instance for this argument --------------
			if ((i+1)<=numOfXMLArgs) {
				// --- Convert XML to ontology class instance ------- 
				Object argumentInstance = this.getInstanceOfXML(ontoArgsXML[i], className, ontology);
				if (argumentInstance!=null) {
					// --- Set the current instance to the form -----
					this.setFormState(argumentInstance, currNode);
					if (Application.getGlobalInfo().isOntologyClassVisualisation(className)==true) {
						OntologyClassWidget widget = this.getOntologyClassWidget(currNode);
						if (widget!=null) {
							widget.invokeSetOntologyClassInstance(argumentInstance);	
						}
					}
					// --- Remind object state as instance ----------
					newOntoArgsInstance[i] = argumentInstance;
				}
				
			}

		} // --- end for ---
		return newOntoArgsInstance;
	}
	
	/**
	 * This method translates an object instance to a String by the
	 * given instance of the current ontology. The object must be a
	 * part of this ontology.
	 *
	 * @param ontologyObject the ontology object
	 * @param ontology the ontology
	 * @return the xML of instance
	 */
	private String getXMLOfInstance(Object ontologyObject, Ontology ontology) {
		
		XMLCodec codec = new XMLCodec();
		String xmlRepresentation = null;
		try {
//			String ontologyClassName = ontologyObject.getClass().getName();
//			if (ontologyClassName.contains(".impl.Default")==true) {
//				// --- OntologyBeanGenerator for Protege 3.4 -------------
//				ontologyClassName = ontologyClassName.replace(".impl.Default", ".");
//				Class<?> ontologyClass = ClassLoadServiceUtility.forName(ontologyClassName);
//			}
			// --- OntologyBeanGenerator for Protege 3.3.1 ---------------
			xmlRepresentation = codec.encodeObject(ontology, ontologyObject, true);
			
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		if (xmlRepresentation!=null) xmlRepresentation.trim();
		if (debug) {
			System.out.println(xmlRepresentation);
		}
		return xmlRepresentation;
	}
	
	/**
	 * This method translates an XML String to an object instance by the
	 * given instance of the current ontology. The translated object must
	 * be a part of this ontology.
	 *
	 * @param xmlString the xml string
	 * @param ontology the ontology
	 * @return the instance of xml
	 */
	protected Object getInstanceOfXML(String xmlString, String className, Ontology ontology) {
		
		Object objectInstance = null;

		if (xmlString!=null && xmlString.equals("")==false) {
			try {
				XMLCodec codec = new XMLCodec();
				objectInstance = codec.decodeObject(ontology, xmlString);
				
			} catch (CodecException ce) {
				ce.printStackTrace();
				
			} catch (OntologyException oe) {
				//oe.printStackTrace();
				// --- No object was created from XML ---------------
				// --- -> Try to create an empty instance -----------
				System.err.println("Ontology '" + ontology.getName() + "' -> XML to Object: " + ExceptionHandling.getFirstTextLineOfException(oe));
				objectInstance = this.getNewClassInstance(className);
				System.err.println("Ontology '" + ontology.getName() + "' -> XML to Object: Created an empty instance for class '" + className + "' !");
				
			}		
		}
		return objectInstance;
	}
	
	/**
	 * This methods reads the current form configuration and creates
	 * the instances and the XML form of that configuration.
	 */
	protected void setInstancesFromForm() {
		
		int numOfChilds = this.getRootNode().getChildCount();
		this.ontoArgsInstance = new Object[numOfChilds];
		this.ontoArgsXML = new String[numOfChilds];
		
		// ----------------------------------------------------------
		// --- Walk through the objectTree, which was generated ----- 
		// --- during the creation of the DynForm - object 		-----
		// ----------------------------------------------------------
		for (int i = 0; i < numOfChilds; i++) {
			
			// --- Get DynType (userObject) of this node ------------
			DefaultMutableTreeNode currNode =  (DefaultMutableTreeNode) this.getRootNode().getChildAt(i);
			DynType dt = (DynType) currNode.getUserObject();
			String className = dt.getClassName();
			
			// --- Get the corresponding Ontology-Instance ----------			
			OntologyClassTreeObject octo = this.ontologyVisualisationHelper.getClassTreeObject(className);
			Ontology onto = octo.getOntologyClass().getOntologyInstance();
			
			// --- Generate instance of this object -----------------
			Object argumentInstance = null;
			if (Application.getGlobalInfo().isOntologyClassVisualisation(className)==true) {
				// --- Get the instance from the widget -------------
				OntologyClassWidget widget = this.getOntologyClassWidget(currNode);
				if (widget==null) {
					// --- Generate an empty instance of the object -
					argumentInstance = this.getNewClassInstance(className);
					// --- Read and set from regular form -----------
					this.setObjectStateFromFormState(argumentInstance, currNode);

				} else {
					// --- The widget is available ----------------------
					argumentInstance = widget.invokeGetOntologyClassInstance();
					if (argumentInstance==null) {
						argumentInstance = this.getNewClassInstance(className);
					}
					this.setFormState(argumentInstance, currNode);
				}
				
			} else {
				// --- Generate an empty instance of the object -----
				argumentInstance = this.getNewClassInstance(className);
				// --- read and set from regular form ---------------
				this.setObjectStateFromFormState(argumentInstance, currNode);
				
			}
			
			// --- Remind object state as instance and XML ---------- 
			ontoArgsInstance[i] = argumentInstance;
			ontoArgsXML[i] = getXMLOfInstance(argumentInstance, onto);
		}
		
	}
	
	/**
	 * This method returns a new instance of a given class given by its full class name.
	 *
	 * @param className the class name
	 * @return the new class instance
	 */
	private Object getNewClassInstance(String className) {
		
		Object obj = null;
		try {
			
			Class<?> clazz = ClassLoadServiceUtility.forName(className);
			if (clazz.isInterface()==false) {
				// --- OntologyBeanGenerator for Protege 3.3.1 ---------------- 
				obj = ClassLoadServiceUtility.newInstance(className);	
				
			} else {
				// --- OntologyBeanGenerator  Protege 3.4 ---------------------
				String packageName = clazz.getPackage().getName();
				String clazzNameSimple = clazz.getSimpleName();
				String defaultClass = packageName + ".impl.Default" + clazzNameSimple;
				
				obj = ClassLoadServiceUtility.newInstance(defaultClass);
			}
			
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
	 * This method sets an object-instance to the state as configured in the form.
	 *
	 * @param ontologyObject the ontology object
	 * @param node the node
	 */
	private void setObjectStateFromFormState(Object ontologyObject, DefaultMutableTreeNode node) {
		
		Method methodFound = null;
		Object[] subObjectOrValue = new Object[1];
		
		int numOfChilds = node.getChildCount();
		for (int i = 0; i < numOfChilds; i++) {
			
			DefaultMutableTreeNode currNode =  (DefaultMutableTreeNode) node.getChildAt(i);
			DynType dt = (DynType) currNode.getUserObject();
			String typeName = dt.getTypeName();
			String className = dt.getClassName();
			OntologySingleClassSlotDescription oscsd = dt.getOntologySingleClassSlotDescription();
			
			methodFound = null;
			subObjectOrValue[0] = null;
			                 
			if (typeName.equals(DynType.typeClass) || typeName.equals(DynType.typeInnerClassType)) {
				// --- Generate Instance of this class --------------
				subObjectOrValue[0] = this.getNewClassInstance(className);
				// --- Set their values -----------------------------
				this.setObjectStateFromFormState(subObjectOrValue[0], currNode);				
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
					Object result = methodFound.invoke(ontologyObject, subObjectOrValue);
					
				} catch (IllegalArgumentException e) {
					System.err.println("Error: " + className + " - " + ontologyObject.getClass().getName() + " - " + methodFound.getName());
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
	 * This method sets the object state to the form.
	 *
	 * @param object the object
	 * @param node the node
	 */
	protected void setFormState(Object object, DefaultMutableTreeNode node) {
		
		int skipChilds = 0;
		int numOfChilds = node.getChildCount();
		for (int i = 0; i < numOfChilds; i++) {
			
			DefaultMutableTreeNode currNode =  (DefaultMutableTreeNode) node.getChildAt(i);
			DynType dt = (DynType) currNode.getUserObject();
			String typeName = dt.getTypeName();
			OntologySingleClassSlotDescription oscsd = dt.getOntologySingleClassSlotDescription();
			String cardinality = oscsd.getSlotCardinality();
			
			if (skipChilds>0) {
				// --- Skip child's because of a multiple similar slots -- 
				skipChilds--;
			} else {
				// -----------------------------------------------------------
				// --- Invoke the method to get the object/value --- Start ---
				// -----------------------------------------------------------
				Object slotValue = null;
				Method methodFound = this.getSingleGetMethod(oscsd, object);
				if (methodFound!=null & this.hasObjectMethod(object, methodFound)) {
					
					// --- Invoke the found method to the current object ---- 
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
				
				// --- work on the slot value --- Start ---------------------
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
					int numberOfSlotValues = 1;
					if (slotValue instanceof List) {
						numberOfSlotValues = ((List) slotValue).size();
						if (numberOfSlotValues==0) {
							slotValue = null;
							multipleValues = false;
						}else if (numberOfSlotValues==1) {
							slotValue = ((List) slotValue).get(0);
							multipleValues = false;
						} else {
							multipleValues = true;
						}
					} else {
						multipleValues = false;
					}
					
					// ------------------------------------------------------
					// --- check the defined cardinality of the slot --------
					// ------------------------------------------------------
					Vector<DefaultMutableTreeNode> nodeVector = null; 
					if (cardinality.equals("single")) {
						if (multipleValues==true) {
							// --- just take the first value ----------------
							numberOfSlotValues = 1;
							multipleValues=false;
							slotValue = ((List) slotValue).get(0);
						}
						
					} else if (cardinality.equals("multiple") ) {
						// --- Configure the number of needed nodes ---------
						nodeVector = this.getMultipleNodesAsNeeded(currNode, numberOfSlotValues);
						numOfChilds = node.getChildCount();
					}					
					
					// --- Debugging ...  -----------------------------------
					if (debug==true) {
						System.out.println("=> " + i + "/" + numOfChilds + " setze: " + currNode.toString() + " zu " + slotValue);
					}

					// ------------------------------------------------------
					// --- Now, set the value to the form -------------------
					// ------------------------------------------------------
					if (typeName.equals(DynType.typeClass) || typeName.equals(DynType.typeInnerClassType)) {
						// --- Set their values -----------------------------
						if (multipleValues==true) {
							// --- Work on the node vector ------------------
							for (int j = 0; j < nodeVector.size(); j++) {
								DefaultMutableTreeNode nodeVectorSingle = nodeVector.get(j);
								dt = (DynType) nodeVectorSingle.getUserObject();
								Object singleValue = ((List) slotValue).get(j);
								this.setFormState(singleValue, nodeVectorSingle);	
							}
							skipChilds = numberOfSlotValues-1; 
							
						} else {
							this.setFormState(slotValue, currNode);	
						}
						
					} else if (typeName.equals(DynType.typeRawType)) {
						// --- Get the value of the underlying field --------
						if (multipleValues==true) {
							// --- Work on the node vector ------------------
							for (int j = 0; j < nodeVector.size(); j++) {
								DefaultMutableTreeNode nodeVectorSingle = nodeVector.get(j);
								dt = (DynType) nodeVectorSingle.getUserObject();
								Object singleValue = ((List) slotValue).get(j);
								this.setSingleValue(dt, singleValue);
							}
							skipChilds = numberOfSlotValues-1; 
							
						} else {
							this.setSingleValue(dt, slotValue);	
						}
						
					} else if (typeName.equals(DynType.typeCyclic)) {
						// --- Nothing to do here ---------------------------
					}	
				}
				// --- work on the slot value --- End -----------------------
				// -----------------------------------------------------------
				// --- Invoke the method to get the object/value --- End -----
				// -----------------------------------------------------------
			} // --- end skipChilds ---
		} // --- end for ---
		
		
		// ----------------------------------------------------------->
		// --- Capture special classes, which are coming from the ----
		// --- Agent.GUI Base ontology							  ----
		// ----------------------------------------------------------->
		if (Application.getGlobalInfo().isOntologyClassVisualisation(object)) {
			JComponent userFormElement = this.getOntologyClassWidgets().get(node);
			if (userFormElement!=null) {
				final OntologyClassWidget widget = (OntologyClassWidget) userFormElement;
				final Object objectInstance = object;
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						widget.invokeSetOntologyClassInstance(objectInstance);
					}
				});
			}
		}
		
	}
	/**
	 * For a 'multiple' cardinality of a slot, the needed nodes will be created.
	 *
	 * @param currNode the curr node
	 * @param nodesNeeded the nodes needed
	 * @return the multiple nodes as needed
	 */
	private Vector<DefaultMutableTreeNode> getMultipleNodesAsNeeded(DefaultMutableTreeNode currNode, int nodesNeeded) {
		
		// --- Get the Vector of all currently available nodes of this kind -------------
		Vector<DefaultMutableTreeNode> nodesAvailableVector = this.getMultipleNodesAvailable(currNode);
		int nodesAvailableCounter = nodesAvailableVector.size();
		
		// --- Get the kind of the DynTyp-object ----------------------------------------
		DynType dt = (DynType) currNode.getUserObject();
		String typeName = dt.getTypeName();
		boolean isInnerClass = false;
		
		if (typeName.equals(DynType.typeClass) || typeName.equals(DynType.typeInnerClassType)) {
			isInnerClass = true;
		} else if (typeName.equals(DynType.typeRawType)) {
			isInnerClass = false;
		} else if (typeName.equals(DynType.typeCyclic)) {
			return nodesAvailableVector;
		}
		
		// --- If the number of nodes is different to the 'numberOfSlotsNeeded' ---------  
		if (nodesAvailableCounter<nodesNeeded) {
			// --- Consider the number of nodes found is smaller than needed -------
			Vector<DefaultMutableTreeNode> nodesNew = new Vector<DefaultMutableTreeNode>();
			while (nodesAvailableVector.size()+nodesNew.size()<nodesNeeded) {
				DefaultMutableTreeNode newNode = this.addSingleMultipleNode(currNode, isInnerClass);
				nodesNew.add(newNode);
			}
			Collections.reverse(nodesNew);
			nodesAvailableVector.addAll(nodesNew);
			
		} else if (nodesAvailableCounter>nodesNeeded) {
			// --- Consider the number of nodes found is greater than needed -------
			while (nodesAvailableVector.size()>nodesNeeded && nodesAvailableVector.size()>1) {
				DefaultMutableTreeNode delNode = nodesAvailableVector.get(nodesAvailableVector.size()-1);
				this.removeMultiple(delNode);
				nodesAvailableVector.remove(nodesAvailableVector.size()-1);
			}
		}
		return nodesAvailableVector;
	}
	/**
	 * This method has to remove the JPanel passed with the given tree node.
	 *
	 * @param delNode the node
	 */
	protected abstract void removeMultiple(DefaultMutableTreeNode delNode);

	/**
	 * This method has to create a copy of the passed JPanel and adds it.
	 *
	 * @param currNode the current node
	 * @param isInnerClass the is inner class
	 * @return the default mutable tree node
	 */
	protected abstract DefaultMutableTreeNode addSingleMultipleNode(DefaultMutableTreeNode currNode, boolean isInnerClass);

	/**
	 * Provides the Vector of all currently available nodes of the same kind as the current node.
	 *
	 * @param currNode The current node of the object structure
	 * @return the multiple nodes available
	 */
	private Vector<DefaultMutableTreeNode> getMultipleNodesAvailable(DefaultMutableTreeNode currNode) {
		
		// --- The result vector of all needed nodes ------------------------------------ 
		Vector<DefaultMutableTreeNode> nodesFound = new Vector<DefaultMutableTreeNode>();
		// --- Can we find the number of similar nodes to the current one? -------------- 
		DynType currDT = (DynType) currNode.getUserObject();
		
		// --- The current parentNode and the position of the current node --------------
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) currNode.getParent();

		// --- Search for all similar nodes --------------------------------------------- 
		for (int i = 0; i < parentNode.getChildCount(); i++) {
			DefaultMutableTreeNode checkNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
			DynType checkDT = (DynType) checkNode.getUserObject();
			if (checkDT.equals(currDT)) {
				nodesFound.add(checkNode);
			} 
		}
		return nodesFound;

	}
	
	/**
	 * This Method checks if an instance of an object has an asked method.
	 *
	 * @param ontologyObject the ontology object
	 * @param methode2check the methode2check
	 * @return true, if the method is available
	 */
	private boolean hasObjectMethod(Object ontologyObject, Method methode2check) {
		
		if (ontologyObject==null) return false;
		
		Method[] meths = ontologyObject.getClass().getMethods();
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
	 * the inherent reference to the Swing component.
	 *
	 * @param dynType the dyn type
	 * @return The single value
	 */
	private Object getSingleValue(DynType dynType) {
		
		Object returnValue = null;
		
		String valueType = dynType.getClassName();
		if(valueType.equals("String")){
			JTextField jt = (JTextField) dynType.getFieldDisplay();
			returnValue = jt.getText();
			
		} else if(valueType.equals("float") || valueType.equals("Float")){
			JTextField jt = (JTextField) dynType.getFieldDisplay();
			String stringValue = jt.getText();
			stringValue = stringValue.replace(",", ".");
			if (stringValue==null | stringValue.equals("")) {
				returnValue = 0;
			} else {
				returnValue = new Float(stringValue);	
			}
			
		} else if(valueType.equals("int") || valueType.equals("Integer")){
			JTextField jt = (JTextField) dynType.getFieldDisplay();
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
			JCheckBox jt = (JCheckBox) dynType.getFieldDisplay();
			returnValue = jt.isSelected();

		}
		return returnValue;
	}
	
	/**
	 * This method sets the value given through the DynType and
	 * the inherent reference to the Swing component.
	 *
	 * @param dynType the dyn type
	 * @param ontologyObject the ontology object
	 */
	private void setSingleValue(DynType dynType, Object ontologyObject) {
		
		String valueType = dynType.getClassName();
		if(valueType.equals("String")){
			JTextField jt = (JTextField) dynType.getFieldDisplay();
			if (ontologyObject==null) {
				jt.setText("");
			} else {
				jt.setText(ontologyObject.toString());	
			}
			
		} else if(valueType.equals("float") || valueType.equals("Float")){
			JTextField jt = (JTextField) dynType.getFieldDisplay();;
			if (ontologyObject==null) {
				jt.setText("0.0");
			} else {
				jt.setText(ontologyObject.toString());
			}
			
		} else if(valueType.equals("int") || valueType.equals("Integer")){
			JTextField jt = (JTextField) dynType.getFieldDisplay();;
			if (ontologyObject==null) {
				jt.setText("0");
			} else {
				jt.setText(ontologyObject.toString());	
			}
			
		} else if(valueType.equals("boolean")){
			JCheckBox jt = (JCheckBox) dynType.getFieldDisplay();;
			if (ontologyObject==null) {
				jt.setSelected(false);
			} else {
				jt.setSelected((Boolean) ontologyObject);	
			}			

		}
	}
	
	/**
	 * This method selects the method of the slot, which has to be
	 * used to set this single slot value.
	 *
	 * @param currOSCSD the curr oscsd
	 * @return the single set method
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
	 * used to Get the current values of the current object instance.
	 *
	 * @param currOSCSD the curr oscsd
	 * @param object the object
	 * @return the single get method
	 */
	private Method getSingleGetMethod(OntologySingleClassSlotDescription currOSCSD, Object object) {
		
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
	 * Resets the form values.
	 */
	protected void resetValuesOnForm() {
		
		int noOfSubNodes = rootNode.getChildCount();
		for (int i=0; i < noOfSubNodes; i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.getRootNode().getChildAt(i);
			this.resetValuesOnSubForm(node);
		}
		this.save(true);
	}
	
	/**
	 * Reset sub object.
	 * @param node the node
	 */
	private void resetValuesOnSubForm(DefaultMutableTreeNode node) {
		
		// --- Set the value to null ----------------------
		DynType dynType = (DynType) node.getUserObject(); 
		this.setSingleValue(dynType, null);
		
		// --- Is there a multiple button to remove? ------
		JButton multipleButton = dynType.getJButtonMultipleOnDynFormPanel();
		if (multipleButton!=null) {
			if (multipleButton.getText().equals("+")==false) {
				multipleButton.doClick();
			}
		}

		// --- Are there any sub nodes available? ---------
		if (node.getChildCount()>0) {
			for (int i=0; i < node.getChildCount(); i++) {
				DefaultMutableTreeNode subNode = (DefaultMutableTreeNode) node.getChildAt(i);
				this.resetValuesOnSubForm(subNode);
			}
		}
		
	}	
}
