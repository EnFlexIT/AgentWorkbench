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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
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
	private DynTreeViewer dtv = null;
	
	// --- Based on this vector the display will be created ---------
	private Vector<String> currOnotologyClassReferenceList = new Vector<String>();
	
	// --- parameters which are coming from the constructor --------- 
	private Project currProject = null;
	private String currAgentReference = null;

	// --- Runtime parameters of this  
	private int einrueckungProUntereEbene = 5;
	
	private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root");
	private DefaultTreeModel objectTree = new DefaultTreeModel(rootNode);
	
	private Object[] ontoArgsInstance = null; 
	private String[] ontoArgsXML = null; 
	
	private NumberWatcher numWatcherFloat = new NumberWatcher(true);
	private NumberWatcher numWatcherInteger = new NumberWatcher(false);
	
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
		
		int yPos = 0;
		
		// --- Maybe a debug print to the console ---------
		if (debug==true && currOnotologyClassReferenceList.size()!=0) {
			System.out.println("Creating GUI");	
		}	
		
		// --- Iterate over the available Start-Objects ---
		for (int i = 0; i < currOnotologyClassReferenceList.size(); i++) {
			
			JPanel startObjectPanel = new JPanel(null);
			
			String startObjectClass = currOnotologyClassReferenceList.get(i);
			String startObjectClassMask = null;
			if (startObjectClass.contains("]")) {
				int cutPos = startObjectClass.indexOf("]");
				startObjectClassMask = startObjectClass.substring(1, cutPos);
				startObjectClass = startObjectClass.substring(cutPos+1).trim();
			}
			
			// --- Get the info about the slots --------------------
			OntologySingleClassDescription osc = currProject.ontologies4Project.getSlots4ClassAsObject(startObjectClass);
			if(osc!=null) {
				this.createGUI(osc, startObjectClass, startObjectClassMask, startObjectPanel, 0, (DefaultMutableTreeNode) rootNode);
			} else {
				System.out.println("Could not get OntologySingleClassDescription for " + startObjectClass);
			}

			// --- 
			this.setPanelBounds(startObjectPanel);
			startObjectPanel.setLocation(0, yPos);
			this.add(startObjectPanel);
			
			// --- Configure the next position for a panel ----------
			yPos = yPos + ((int)startObjectPanel.getBounds().getHeight()) + 2;
			
		}
		// --- Justify the Preferred Size of this Panel ---
		this.setPreferredSize(this);
	}
	
	/**
	 * This method creates the XML form from the instances 
	 */
	private void setXMLFromInstances(){
	
		int numOfChilds = rootNode.getChildCount();
		//this.ontoArgsInstance = new Object[numOfChilds];
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
						
			// --- Generate XML of this object ----------------------
			ontoArgsXML[i] = this.getXMLOfInstance(this.ontoArgsInstance[i], onto);
			
		}
		
	}
	
	/**
	 * This methods reads the current form configuration and creates 
	 * the instances and the XML form of that configuration 
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
	 * This method reads the current xml configuration of the 
	 * arguments, fills the form and creates the instances  
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
			OntologySingleClassSlotDescription oscsd = dt.getOSCSD();
			
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
					System.err.println("Error: " + className + " - " + object.getClass().getName() + " - " + methodFound.getName());
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
		
		int skipChilds = 0;
		int numOfChilds = node.getChildCount();
		for (int i = 0; i < numOfChilds; i++) {
			
			DefaultMutableTreeNode currNode =  (DefaultMutableTreeNode) node.getChildAt(i);
			DynType dt = (DynType) currNode.getUserObject();
			String typeName = dt.getTypeName();
			OntologySingleClassSlotDescription oscsd = dt.getOSCSD();
			String cardinality = oscsd.getSlotCardinality();
			
			if (skipChilds>0) {
				// --- Skip child's because of a multiple similar slots -- 
				skipChilds--;
			} else {
				// -----------------------------------------------------------
				// --- Invoke the method to get the object/value --- Start ---
				// -----------------------------------------------------------
				Object slotValue = null;
				Method methodFound = this.getGetMethod(oscsd, object);
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
	}
	
	
	private Vector<DefaultMutableTreeNode> getMultipleNodesAvailable(DefaultMutableTreeNode currNode) {
		
		// --- The result vector of all needed nodes ------------------------------------ 
		Vector<DefaultMutableTreeNode> nodesFound = new Vector<DefaultMutableTreeNode>();
		// --- Can we find the number of similar nodes to the current one? -------------- 
		DynType currDT = (DynType) currNode.getUserObject();
		
		// --- The current parentNode and the position of the current node --------------
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) currNode.getParent();
		int currNodePosition = parentNode.getIndex(currNode);

		// --- Search for all similar nodes --------------------------------------------- 
		int searchPostion = 0;
		int similarNodeCounter = 0;
		boolean similarNode = true;
		while (similarNode) {
			
			DefaultMutableTreeNode checkNode = (DefaultMutableTreeNode) parentNode.getChildAt(searchPostion);
			DynType checkDT = (DynType) checkNode.getUserObject();
			if (checkDT.equals(currDT)) {
				nodesFound.add(checkNode);
				similarNodeCounter++;	
			} else {
				if (searchPostion<currNodePosition==false) {
					similarNode=false;	
				}
			}
			searchPostion++;
		}
		return nodesFound;
		
	}
	
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
				DefaultMutableTreeNode newNode = this.addMultiple(currNode, isInnerClass);
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
		
		String valueType = dt.getClassName();
		if(valueType.equals("String")){
			JTextField jt = (JTextField) dt.getFieldDisplay();
			returnValue = jt.getText();
			
		} else if(valueType.equals("float")){
			JTextField jt = (JTextField) dt.getFieldDisplay();
			String stringValue = jt.getText();
			stringValue = stringValue.replace(",", ".");
			if (stringValue==null | stringValue.equals("")) {
				returnValue = 0;
			} else {
				returnValue = new Float(stringValue);	
			}
			
		} else if(valueType.equals("int")){
			JTextField jt = (JTextField) dt.getFieldDisplay();
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
			JCheckBox jt = (JCheckBox) dt.getFieldDisplay();
			returnValue = jt.isSelected();

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
		
		String valueType = dt.getClassName();
		if(valueType.equals("String")){
			JTextField jt = (JTextField) dt.getFieldDisplay();
			if (obj==null) {
				jt.setText("");
			} else {
				jt.setText(obj.toString());	
			}
			
		} else if(valueType.equals("float")){
			JTextField jt = (JTextField) dt.getFieldDisplay();;
			if (obj==null) {
				jt.setText("0.0");
			} else {
				jt.setText(obj.toString());
			}
			
		} else if(valueType.equals("int")){
			JTextField jt = (JTextField) dt.getFieldDisplay();;
			if (obj==null) {
				jt.setText("0");
			} else {
				jt.setText(obj.toString());	
			}
			
		} else if(valueType.equals("boolean")){
			JCheckBox jt = (JCheckBox) dt.getFieldDisplay();;
			if (obj==null) {
				jt.setSelected(false);
			} else {
				jt.setSelected((Boolean) obj);	
			}			

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
		this.dtv = new DynTreeViewer(objectTree);
		this.dtv.setVisible(true);
	}

	/**
	 * 
	 * @param pan
	 */
	public void setPanelBounds(JPanel pan){
		
		int xPos = pan.getX();
		int yPos = pan.getY();
		int maxX = 0;
		int maxY = 0;
		
		Component[] components = pan.getComponents();
		for (int i = 0; i < components.length; i++) {
			int currXMax = components[i].getX() + components[i].getWidth();
			int currYMax = components[i].getY() + components[i].getHeight();
			if (currXMax > maxX) maxX = currXMax;
			if (currYMax > maxY) maxY = currYMax;
		}
		
		maxX += 5;
		maxY += 2;
		pan.setBounds(xPos, yPos, maxX, maxY);

	}
	
	/**
	 * This method adds the class to a panel and initiates to add
	 * possible inner classes and fields also to the panel or its subpanels
	 * @param osc
	 * @param startObjectClassName
	 * @param pan
	 * @param tiefe
	 */
	public void createGUI(OntologySingleClassDescription osc, String startObjectClass, String startObjectClassMask, JPanel parentPanel, int tiefe, DefaultMutableTreeNode parentNode){
		
		String startObjectClassName = startObjectClass.substring(startObjectClass.lastIndexOf(".") + 1, startObjectClass.length());
		String startObjectPackage = startObjectClass.substring(0, startObjectClass.lastIndexOf("."));

		JLabel objectLabelName = new JLabel();		
		if (osc != null) {
			// --- if we are on the main panel -> add the class name to it !
			// --- Create a JPanel in which the class name (JLabel) and its innerclasses
			// --- and/or fields are added instead of mainPanel - class name - innerclasses/fields
			
			if(tiefe == 0){
				// --- Set the label for the class --- //
				objectLabelName.setBounds(new Rectangle(10, parentPanel.getHeight()+5 , 350, 16));
				if (startObjectClassMask==null) {
					objectLabelName.setText(startObjectClassName);	
				} else {
					objectLabelName.setText(startObjectClassMask + " (" + startObjectClassName + ")");
				}
				objectLabelName.setFont(new Font(objectLabelName.getFont().getName(),Font.BOLD,objectLabelName.getFont().getSize()));
				
				// --- Define a node for current class ------------------
				DynType dynType = new DynType(null, DynType.typeClass, startObjectClass, parentPanel, startObjectClassName);
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dynType);
				parentNode.add(newNode);
				parentNode = newNode;
				
			} else {
				// --- Set the label for the class --- //
				objectLabelName.setBounds(new Rectangle(10, 23 , 350, 16));
				if (startObjectClassMask==null) {
					objectLabelName.setText(startObjectClassName.substring(startObjectClassName.lastIndexOf(".")+1));
				} else {
					objectLabelName.setText(startObjectClassMask + " (" + startObjectClassName + ")");
				}
				objectLabelName.setFont(new Font(objectLabelName.getFont().getName(),Font.BOLD,objectLabelName.getFont().getSize()));
				
			}

			// --- add the name to the mainPanel ----------
			parentPanel.add(objectLabelName);
			this.setPanelBounds(parentPanel);
			
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
					if(objectAlreadyInPath(clazzCheck, parentNode) == false) {
						// --- Create Sub-Panel for the according class -----------------
						this.createInnerElements(oscsd, dataItemName, dataItemVarType, dataItemCardinality, clazzCheck, tiefe+1, parentPanel, parentNode, false);
					} else {
						// --- Create Sub-Panel that shows the cylic reference ----------
						this.createOuterDeadEnd(oscsd, dataItemName, dataItemVarType, dataItemCardinality, clazz, parentPanel, tiefe, parentNode);
					}
					
				} else {
					// --- Here we have a field with a final type (String, int, ...) ----
					this.createOuterElements(oscsd, dataItemName, dataItemVarType, dataItemCardinality, clazz, tiefe, parentPanel, parentNode, false);
				}
				// --------------------------------------------------------------------------------
			}
		
		} else {
			System.out.println("Could not create DefaultTableModel ("+startObjectClassName+")");
		}
		
	}	

	/**
	 * This method search's for a similar node in the path (directed to the  
	 * root node) to prevent a cyclic creation of form elements.   
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
	public DefaultMutableTreeNode createInnerElements(OntologySingleClassSlotDescription oscsd, String dataItemName, String dataItemVarType, String dataItemCardinality, String startObjectClassName, int tiefe, final JPanel parentPanel, DefaultMutableTreeNode parentNode, boolean addMultiple){
		
		// --- create a JPanel which will contain further inner classes and fields
		final JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		int innerX  = 0;

		// set the value of how much the panel shall be shifted (Einrückung)
		if(tiefe > 1)
			innerX = (einrueckungProUntereEbene*(tiefe-1));
		else
			innerX = (einrueckungProUntereEbene*(tiefe));
		dataPanel.setBounds(new Rectangle(innerX, 0 , 350, 20));
		dataPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		dataPanel.setToolTipText(startObjectClassName + " Inner Panel");
		
		// --- create two JLabels: first displays the field name
		// --- the second displays the inner class name
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText(dataItemName + " ["+dataItemVarType+"]");
		valueFieldText.setBounds(new Rectangle(10, 5, 300, 16));
		
		dataPanel.add(valueFieldText, null);
		this.setPanelBounds(dataPanel);

		// --- Create node for this element/panel ----------------------------- 
		DynType dynType = new DynType(oscsd, DynType.typeInnerClassType, startObjectClassName, dataPanel, dataItemName);
		final DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dynType);
		parentNode.add(newNode);
		//parentNode = newNode;
		
		// --- if the inner class has got a multi cardinality create an add-button		
		if(dataItemCardinality.equals("multiple")) {

			if (addMultiple==false) {
				// --- Add an add Button to the panel ---------------
				JButton multipleButton = new JButton("+");
				multipleButton.setBounds(new Rectangle(315, 2, 35, 25));
				multipleButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						addMultiple(newNode, true);
					}
				});	
				dataPanel.add(multipleButton, null);
				this.setPanelBounds(dataPanel);
				
			} else {
				// --- Add an remove Button to the panel ------------
				JButton multipleButton = new JButton("-");
				multipleButton.setBounds(new Rectangle(315, 2, 35, 25));
				multipleButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						removeMultiple(newNode);				
					}
				});	
				dataPanel.add(multipleButton, null);
				this.setPanelBounds(dataPanel);
				
			}
			
		}
		
		// --- create the inner fields of the current inner class
		OntologySingleClassDescription osc = currProject.ontologies4Project.getSlots4ClassAsObject(startObjectClassName);
		this.createGUI(osc, startObjectClassName, null, dataPanel, tiefe, newNode);
		
		// --- set the correct height of the parent of this inner class according to the
		// --- inner class's height
		Rectangle r = dataPanel.getBounds();
		dataPanel.setBounds(10, parentPanel.getHeight(), r.width, r.height);
		
		// --- finally add the inner class to its parent panel
		parentPanel.add(dataPanel);
		this.setPanelBounds(parentPanel);
		
		return newNode;
		
	}

	/**
	 * This method creates the panels for fields which have no inner classes
	 * @param dataItemName
	 * @param dataItemCardinality
	 * @param pan
	 * @param tiefe
	 * @param node 
	 */
	public DefaultMutableTreeNode createOuterElements(OntologySingleClassSlotDescription oscsd, String dataItemName, String dataItemVarType, String dataItemCardinality, String className, int tiefe, JPanel parentPanel, DefaultMutableTreeNode parentNode, boolean addMultiple){
		
		// --- this outer element has no parents which are inner classes
		// --- so its added to the mainPanel
		final JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		dataPanel.setToolTipText(dataItemName + "-Panel");
		dataPanel.setBounds(new Rectangle(0, 0 , 350, 10));
		
		// --- add a JLabel to display the field's name
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText(dataItemName + " ["+dataItemVarType+"]");
		valueFieldText.setBounds(new Rectangle(0, 4, 130, 16));
		
		// --- valueFields (Textfield, Checkbox (for boolean) , ... )
		JComponent valueDisplay = this.getVisualComponent(dataItemVarType);
		
		// --- add both GUI elements to the panel
		dataPanel.add(valueFieldText, null);
		dataPanel.add(valueDisplay);
		this.setPanelBounds(dataPanel);

		// --- add node to parent -------------------------
		DynType dynType = new DynType(oscsd, DynType.typeRawType, className, dataPanel, dataItemName, valueDisplay);
		final DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dynType); 
		parentNode.add(newNode);

		// --- if the inner class has got a multiple cardinality create an add-button
		if(dataItemCardinality.equals("multiple")) {
			
			if (addMultiple==false) {
				// --- Add an add Button to the panel ---------------
				JButton multipleButton = new JButton("+");
				multipleButton.setBounds(new Rectangle(315, 0, 35, 25));
				multipleButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						addMultiple(newNode, false);
					}
				});	
				dataPanel.add(multipleButton, null);
				this.setPanelBounds(dataPanel);

			} else {
				// --- Add an remove Button to the panel ------------
				JButton multipleButton = new JButton("-");
				multipleButton.setBounds(new Rectangle(315, 0, 35, 25));
				multipleButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						removeMultiple(newNode);
					}
				});	
				dataPanel.add(multipleButton, null);
				this.setPanelBounds(dataPanel);
				
			}
		}
		
		// --- set the new position (increment the height) for the parent panel of the 
		// --- newly created panel
		Rectangle pos = dataPanel.getBounds();
		pos.x = 10;//tiefe * einrueckungProUntereEbene;
		pos.y = parentPanel.getHeight();
		dataPanel.setBounds(pos);

		parentPanel.add(dataPanel);
		this.setPanelBounds(parentPanel);
		
		return newNode;
	}
	
	/**
	 * This method creates a so called dead end. This means that orgininally a class 
	 * should be displayed which was already displayed on a higher level in direction
	 * to the root node. This was realized to prevent the form generation to be run
	 * in an endless loop.  
	 *  
	 * @param oscsd
	 * @param dataItemName
	 * @param dataItemVarType
	 * @param dataItemCardinality
	 * @param className
	 * @param pan
	 * @param tiefe
	 * @param node
	 */
	public void createOuterDeadEnd(OntologySingleClassSlotDescription oscsd, String dataItemName, String dataItemVarType, String dataItemCardinality, String className, JPanel pan, int tiefe, DefaultMutableTreeNode node){
		
		// --- this outer element has no parents which are inner classes
		// --- so its added to the mainPanel
		final JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		dataPanel.setToolTipText(dataItemName + "-Panel");
		
		// --- add a JLabel to display the field's name
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText("<html>" + dataItemName + " ["+dataItemVarType+"] - <b>" + Language.translate("Zyklisch !") + "</b></html>");
		valueFieldText.setBounds(new Rectangle(0, 4, 330, 16));
		
		// --- add both GUI elements to the panel
		dataPanel.add(valueFieldText, null);
		this.setPanelBounds(dataPanel);
		
		DynType dynType = new DynType(oscsd, DynType.typeCyclic, className, dataPanel, dataItemName, null);
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
	 * This method creates the JComponent, which will be 
	 * displayed for an ontology slot
	 * @param valueType
	 * @return
	 */
	private JComponent getVisualComponent(String valueType) {
	
		JComponent valueField = null;
		if(valueType.equals("String")){
			valueField = new JTextField();
			valueField.setBounds(new Rectangle(140, 0, 170, 27));
			
		} else if(valueType.equals("float")){
			valueField = new JTextField("0.0");
			valueField.setBounds(new Rectangle(140, 0, 100, 26));
			valueField.addKeyListener(this.numWatcherFloat);
			
		} else if(valueType.equals("int")){
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
	 * This method creates a copy of the passed JPanel and adds it
	 * @param dataPanel
	 */
	public DefaultMutableTreeNode addMultiple(DefaultMutableTreeNode node, boolean isInnerClass){
		
		// --- Get all needed information about the node, which has to be copied --------
		DynType dt = (DynType) node.getUserObject();
		String clazz = dt.getClassName();
		
		OntologySingleClassSlotDescription oscsd = dt.getOSCSD();
		String dataItemName = oscsd.getSlotName();
		String dataItemVarType = oscsd.getSlotVarType();
		String dataItemCardinality = oscsd.getSlotCardinality();
		
		JPanel oldPanel = dt.getPanel();
		JPanel parentPanel = (JPanel) oldPanel.getParent();

		// --- Get parent node ----------------------------------------------------------
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent(); 
		int depth2WorkOn = parentNode.getLevel()-1;
		
		// --- Create the panel/node as needed as a copy of the current node ------------
		JPanel blindPanel = new JPanel();
		blindPanel.setLayout(null);
		DefaultMutableTreeNode newNode = null;
		if (isInnerClass==true) {
			newNode = this.createInnerElements(oscsd, dataItemName, dataItemVarType, dataItemCardinality, clazz, depth2WorkOn+1, blindPanel, parentNode, true);
		} else {
			newNode = this.createOuterElements(oscsd, dataItemName, dataItemVarType, dataItemCardinality, clazz, depth2WorkOn, blindPanel, parentNode, true);
		}
		
		// --- Place the node at the right position in the tree -------------------------
		newNode.removeFromParent();
		int nodeIndexPos = parentNode.getIndex(node)+1;
		objectTree.insertNodeInto(newNode, parentNode, nodeIndexPos);
		
		// --- Set the size of the new Panel --------------------------------------------
		DynType dtNew = (DynType) newNode.getUserObject();
		JPanel newPanel = dtNew.getPanel();
		this.setPanelBounds(newPanel);
		newPanel.setPreferredSize(newPanel.getSize());
		
		// --- Now place the new sub panel on the right super panel ---------------------
		int movement = oldPanel.getHeight() + 2;
		int xPos = oldPanel.getX();
		int yPos = oldPanel.getY() + movement;
		newPanel.setBounds(xPos, yPos, newPanel.getWidth(), newPanel.getHeight());
		parentPanel.add(newPanel);
		
		// --- Now move the rest of the elements on the form ----------------------------
		this.moveAfterAddOrRemove(movement, newNode);
		
		// --- refresh the GUI ----------------------------------------------------------
		this.setPreferredSize(this);
		this.validate();
		
		return newNode;
	}
	
	/**
	 * This method removes the passed JPanel 
	 * @param dataPanel
	 */
	public void removeMultiple(DefaultMutableTreeNode node){
		
		// --- Remind all needed informations -------------------------------------------
		DefaultMutableTreeNode previousNode = node.getPreviousNode();
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
		
		DynType dt = (DynType) node.getUserObject();
		JPanel deletePanel = dt.getPanel();
		JPanel parentPanel = (JPanel) deletePanel.getParent();
		
		int movement = (deletePanel.getHeight() + 2) * (-1);
		
		// --- Remove node from the parent node and panel -------------------------------
		parentPanel.remove(deletePanel);
		node.setUserObject(null);
		parentNode.remove(node);
		
		// --- Now move the rest of the elements on the form ----------------------------
		this.moveAfterAddOrRemove(movement, previousNode);

		// --- refresh the GUI ----------------------------------------------------------
		this.setPreferredSize(this);
		this.validate();
		
	}

	/**
	 * Move all elements which are available after the node given by the parameter node
	 * @param movement
	 * @param node
	 */
	private void moveAfterAddOrRemove(int movement, DefaultMutableTreeNode node) {
		
		if (node==rootNode) {
			return;
		}
		
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
		
		int numOfChilds = parentNode.getChildCount();
		int indexOfNextNode = parentNode.getIndex(node) + 1;
		for (int i = indexOfNextNode; i < numOfChilds; i++) {
			
			DefaultMutableTreeNode currNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);

			DynType dt = (DynType) currNode.getUserObject();
			JPanel movePanel = dt.getPanel();
			movePanel.setBounds(movePanel.getX(), movePanel.getY()+movement, movePanel.getWidth(), movePanel.getHeight());
			
			JComponent parentComp = (JComponent) movePanel.getParent();
			if (parentComp instanceof JPanel) {
				this.setPanelBounds((JPanel) parentComp);	
			}
			
		}

		// --- do the same at the parent node -----------------------
		this.moveAfterAddOrRemove(movement, parentNode);
		
	}
	
	/**
	 * This Method sets the PreferredSize of this Panel according 
	 * to the Position of the 'submitButton'
	 */
	private void setPreferredSize(JPanel panel) {
		this.setPanelBounds(panel);
		this.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight() + 20));
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
		this.setXMLFromInstances();
		this.setInstancesFromXML();
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
		this.ontoArgsXML = ontoArgsXML;
		this.setInstancesFromXML();
	}

	/**
	 * This class controls the input fields for number formats.
	 * For integer values just numbers are allowed. For floats
	 * numbers and just one separator character "," or "." is
	 * allowed.
	 * The class can be used by adding this as a KeyListener
	 * 
	 * @author Christian Derksen
	 */
	private class NumberWatcher extends KeyAdapter {
		
		private boolean isFloatValue = false;
		
		public NumberWatcher (boolean floatValue) {
			this.isFloatValue = floatValue;
		}

		public void keyTyped(KeyEvent kT) {
			
			char charackter = kT.getKeyChar();
			String singleChar = Character.toString(charackter);
			JTextField displayField = (JTextField) kT.getComponent();
			
			if (this.isFloatValue==true) {
				// ----------------------------------------
				// --- float values allowed ---------------
				// ----------------------------------------
				// --- Just one separator is allowed --
				if (singleChar.equals(".") || singleChar.equals(",")) {
					String currValue = displayField.getText();
					if (currValue!=null) {
						if ( currValue.contains(".") || currValue.contains("," )) {
							kT.consume();	
							return;
						}
					}
				} else  if ( singleChar.matches( "[0-9]" ) == false) {
					kT.consume();	
					return;
				}
				
			} else {
				// ----------------------------------------
				// --- int numbers only !!! ---------------
				// ----------------------------------------
				if ( singleChar.matches( "[0-9]" ) == false ) {
					kT.consume();	
					return;
				}
				
			} // --- end if -------------------------------
			
		 }				 
	} // --- end sub class ----
	
}
