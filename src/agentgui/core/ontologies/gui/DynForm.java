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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import agentgui.core.application.Language;
import agentgui.core.charts.timeseriesChart.gui.TimeSeriesWidget;
import agentgui.core.charts.xyChart.gui.XyWidget;
import agentgui.core.ontologies.OntologyClassTree;
import agentgui.core.ontologies.OntologyClassTreeObject;
import agentgui.core.ontologies.OntologySingleClassDescription;
import agentgui.core.ontologies.OntologySingleClassSlotDescription;
import agentgui.core.ontologies.OntologyVisualisationHelper;
import agentgui.core.project.AgentStartArgument;
import agentgui.core.project.AgentStartConfiguration;
import agentgui.ontology.Chart;
import agentgui.ontology.Formula;
import agentgui.ontology.TimeSeriesChart;
import agentgui.ontology.XyChart;

/**
 * This class can be used in order to generate a Swing based user form, that represents
 * the structure of one or more ontology-classes.
 * 
 * @author Marvin Steinberg - University of Duisburg - Essen
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DynForm extends JPanel {

	private static final long serialVersionUID = 7942028680794127910L;

	private boolean debug = false;
	private DynTreeViewer dtv = null;
	private boolean emptyForm = true;
	
	// --- Based on this vector the display will be created ---------
	private Vector<AgentStartArgument> currOnotologyClassReferenceList = new Vector<AgentStartArgument>();
	
	// --- parameters which are coming from the constructor --------- 
	private OntologyVisualisationHelper ontologyVisualisationHelper = null;
	private AgentStartConfiguration agentStartConfiguration = null;
	private String currAgentReference = null;

	// --- Runtime parameters of this  class ------------------------
	private int einrueckungProUntereEbene = 5;
	private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root");
	private DefaultTreeModel objectTree = new DefaultTreeModel(rootNode);
	private Object[] ontoArgsInstance = null; 
	private String[] ontoArgsXML = null; 
	
	
	private NumberWatcher numWatcherFloat = new NumberWatcher(true);
	private NumberWatcher numWatcherInteger = new NumberWatcher(false);
	
	private HashMap<DefaultMutableTreeNode, Object> userFormElements = new HashMap<DefaultMutableTreeNode, Object>();

	
	/**
	 * Constructor of this class by using a project and an agent reference.
	 *
	 * @param ontologyVisualisationHelper the {@link OntologyVisualisationHelper}
	 * @param agentStartConfiguration the {@link AgentStartConfiguration}
	 * @param agentReference the agent reference
	 */
	public DynForm(OntologyVisualisationHelper ontologyVisualisationHelper, AgentStartConfiguration agentStartConfiguration, String agentReference) {
		
		super();
		this.ontologyVisualisationHelper = ontologyVisualisationHelper;
		this.agentStartConfiguration = agentStartConfiguration;
		this.currAgentReference = agentReference;
	
		// --- Set the preferences for the Main Panel ---------------
		this.setLayout(null);
		
		// --- Prevent errors through empty agent references --------
		if (this.currAgentReference!=null) {
			// --- Find Agent in AgentConfig ------------------------
			if (this.agentStartConfiguration.containsKey(this.currAgentReference)==true) {
				
				// --- Which classes are configured for the Agent? -- 
				Vector<AgentStartArgument> startArgs = this.agentStartConfiguration.get(currAgentReference);
				for (int i = 0; i < startArgs.size(); i++) {
					currOnotologyClassReferenceList.add(startArgs.get(i));
				}

				// --- Start building the GUI -----------------------
				this.buildGUI();
				this.emptyForm = false;
				// --- If wanted show some debug informations -------
				if (debug==true) {
					this.objectTreeShow();
				}
			}
		}
	}
	
	/**
	 * Constructor of this class by using an instance of an ontology and
	 * the reference(s) of a needed class out of the ontology.
	 *
	 * @param ontologyVisualisationHelper the {@link OntologyVisualisationHelper}
	 * @param ontologyClassReferences the ontology class references
	 */
	public DynForm(OntologyVisualisationHelper ontologyVisualisationHelper, String[] ontologyClassReferences) {

		super();
		this.ontologyVisualisationHelper = ontologyVisualisationHelper;
		// --- Set the preferences for the Main Panel ---------------
		this.setLayout(null);
		
		// --- Was something set? -----------------------------------
		if (ontologyClassReferences!= null && ontologyClassReferences.length>0) {
			// --- Take that to the local Vector --------------------
			for (int i = 0; i < ontologyClassReferences.length; i++) {
				AgentStartArgument startArgument = new AgentStartArgument(i+1, ontologyClassReferences[i]);
				this.currOnotologyClassReferenceList.add(startArgument);
			}

			// --- Start building the GUI -----------------------
			this.buildGUI();
			this.emptyForm = false;
			// --- If wanted show some debug informations -------
			if (debug==true) {
				this.objectTreeShow();
			}
			
		}
	}
	
	/**
	 * Indicates if the created form is empty.
	 * @return the emptyForm
	 */
	public boolean isEmptyForm() {
		return emptyForm;
	}
	/**
	 * This method starts building the Swing GUI.
	 */
	private void buildGUI(){
		
		int yPos = 0;
		
		// --- Maybe a debug print to the console ---------
		if (debug==true && currOnotologyClassReferenceList.size()!=0) {
			System.out.println("Creating GUI");	
		}	
		
		// --- Iterate over the available Start-Objects ---
		for (int i = 0; i < currOnotologyClassReferenceList.size(); i++) {
			AgentStartArgument agentStartArgument = currOnotologyClassReferenceList.get(i); 
			if (agentStartArgument!=null) {
				
				int startObjectPosition = agentStartArgument.getPosition(); 
				String startObjectClass = agentStartArgument.getOntologyReference();
				String startObjectClassMask = startObjectPosition + ": ";
				if (agentStartArgument.getDisplayTitle()!=null) {
					startObjectClassMask += agentStartArgument.getDisplayTitle();
				}
				
				JPanel startObjectPanel = new JPanel(null);
				
				// --- Get the info about the slots --------------------
				OntologySingleClassDescription osc = this.ontologyVisualisationHelper.getSlots4ClassAsObject(startObjectClass);
				if(osc!=null) {
					this.createGUI(osc, i, 0, startObjectClass, startObjectClassMask, (DefaultMutableTreeNode) rootNode, startObjectPanel);
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
			
		}
		// --- Justify the Preferred Size of this Panel ---
		this.setPreferredSize(this);
	}
	
	/**
	 * This method creates the XML form from the instances.
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
			OntologyClassTreeObject octo = this.ontologyVisualisationHelper.getClassTreeObject(className);
			Ontology onto = octo.getOntologyClass().getOntologyInstance();
						
			// --- Generate XML of this object ----------------------
			ontoArgsXML[i] = this.getXMLOfInstance(this.ontoArgsInstance[i], onto);
			
		}
		
	}
	
	/**
	 * This methods reads the current form configuration and creates
	 * the instances and the XML form of that configuration.
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
			OntologyClassTreeObject octo = this.ontologyVisualisationHelper.getClassTreeObject(className);
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
	 * This method reads the current XML configuration of the
	 * arguments, fills the form and creates the instances.
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
			OntologyClassTreeObject octo = this.ontologyVisualisationHelper.getClassTreeObject(className);
			Ontology onto = octo.getOntologyClass().getOntologyInstance();
			
			// --- Generate instance for this argument --------------
			if ((i+1)<=numOfXMLArgs) {
				Object obj = getInstanceOfXML(ontoArgsXML[i], onto);
				if (obj!=null) {
					// --- Set the current instance to the form -----
					this.setFormState(obj, currNode);
					// --- Remind object state as instance ----------
					this.ontoArgsInstance[i] = obj;
				}
			}

		} // --- end for ---
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
			xmlRepresentation = codec.encodeObject(ontology, ontologyObject, true);
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
	 *
	 * @param xmlString the xml string
	 * @param ontology the ontology
	 * @return the instance of xml
	 */
	private Object getInstanceOfXML(String xmlString, Ontology ontology) {
		
		XMLCodec codec = new XMLCodec();
		Object objectInstance = null;
		
		try {
			objectInstance = codec.decodeObject(ontology, xmlString);
		} catch (CodecException ce) {
			ce.printStackTrace();
		} catch (OntologyException oe) {
			oe.printStackTrace();
		}		
		return objectInstance;
	}
	
	/**
	 * This method sets an object-instance to the state as configured in the form.
	 *
	 * @param ontologyObject the ontology object
	 * @param node the node
	 */
	private void setObjectState(Object ontologyObject, DefaultMutableTreeNode node) {
		
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
	private void setFormState(Object object, DefaultMutableTreeNode node) {
		
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
		
		if (object instanceof TimeSeriesChart) {
			
			Object userFormElement = this.userFormElements.get(node);
			if (userFormElement!=null) {
				final TimeSeriesWidget tsWidget = (TimeSeriesWidget) userFormElement;
				final TimeSeriesChart tsChart = (TimeSeriesChart) object;
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						tsWidget.setChart(tsChart);
					}
				});
			}
			
		} else if (object instanceof XyChart) {
			Object userFormElement = this.userFormElements.get(node);
			if (userFormElement!=null) {
				final XyWidget xyWidget = (XyWidget) userFormElement;
				final XyChart xyChart = (XyChart) object;
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						xyWidget.setChart(xyChart);
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
	 * This method returns a new instance of a given class given by its full class name.
	 *
	 * @param className the class name
	 * @return the new class instance
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
	 * Shows the object tree in a visual way in a JDialog, if the internal debug value is set to be true.
	 */
	private void objectTreeShow() {
		this.dtv = new DynTreeViewer(objectTree);
		this.dtv.setVisible(true);
	}

	/**
	 * Sets the bounds of the given panel, depending on the child components on this panel.
	 *
	 * @param panel the new panel bounds
	 */
	private void setPanelBounds(JPanel panel){
		
		int xPos = panel.getX();
		int yPos = panel.getY();
		int maxX = 0;
		int maxY = 0;
		
		Component[] components = panel.getComponents();
		for (int i = 0; i < components.length; i++) {
			int currXMax = components[i].getX() + components[i].getWidth();
			int currYMax = components[i].getY() + components[i].getHeight();
			if (currXMax > maxX) maxX = currXMax;
			if (currYMax > maxY) maxY = currYMax;
		}
		
		maxX += 5;
		maxY += 2;
		panel.setBounds(xPos, yPos, maxX, maxY);

	}
	
	/**
	 * This method adds the class to a panel and initiates to add
	 * possible inner classes and fields also to the panel or its sub panels.
	 *
	 * @param osc the osc
	 * @param startObjectClass the start object class
	 * @param startObjectClassMask the start object class mask
	 * @param parentPanel the parent panel
	 * @param depth the depth
	 * @param parentNode the parent node
	 */
	private void createGUI(OntologySingleClassDescription osc, int startArgIndex, int depth, String startObjectClass, String startObjectClassMask,  DefaultMutableTreeNode parentNode, JPanel parentPanel){
		
		String startObjectClassName = startObjectClass.substring(startObjectClass.lastIndexOf(".") + 1, startObjectClass.length());
		String startObjectPackage = startObjectClass.substring(0, startObjectClass.lastIndexOf("."));

		JLabel objectLabelName = new JLabel();		
		if (osc != null) {
			// --- if we are on the main panel -> add the class name to it !
			// --- Create a JPanel in which the class name (JLabel) and its innerclasses
			// --- and/or fields are added instead of mainPanel - class name - innerclasses/fields
			
			if(depth == 0){
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
			Iterator<OntologySingleClassSlotDescription> iterator = osc.arrayList4SlotDescriptions.iterator();
			while (iterator.hasNext()) {
				
				OntologySingleClassSlotDescription oscsd = iterator.next();

				if(isRawType(oscsd.getSlotVarTypeCorrected())){
					// --- Here we have a field with a final type (String, int, ...) ----
					this.createOuterElements(oscsd, depth, parentPanel, parentNode, false);
					
				} else {
					// --- Here we have a field with an assigned object => inner class --					
					String clazz = oscsd.getSlotVarType();
					if(clazz.matches("Instance of (.)*")){
						clazz = clazz.substring(12);	
					}
					
					String clazzCheck = null;
					if (clazz.equals("AID")) {
						clazzCheck = OntologyClassTree.BaseClassAID;
					} else {
						clazzCheck = startObjectPackage+"."+clazz;	
					}					

					// --- Was this class already displayed in the current tree path? ---
					if(objectAlreadyInPath(clazzCheck, parentNode) == false) {
						// --- Create Sub-Panel for the according class -----------------
						this.createInnerElements(oscsd, clazzCheck, depth+1, parentPanel, parentNode, false);
					} else {
						// --- Create Sub-Panel that shows the cyclic reference ---------
						this.createOuterDeadEnd(oscsd, clazz, depth, parentPanel, parentNode);
					}
					
				}
				// --------------------------------------------------------------------------------
			}
		
		} else {
			System.out.println(this.getClass().getName() + ": Can not create element for '"+startObjectClassName+"'!");
		}
		
		// ---------------------------------------------------------------------
		// --- Is the current ontology class a special case of Agent.GUI ? -----
		Object specialClass = isAgentGUISpecialClass(startObjectPackage, startObjectClassName); 
		if (specialClass!=null){
			this.createOuterElement4AgentGUISpecialClass(startArgIndex, specialClass, parentNode, parentPanel);
			
		}
		// ---------------------------------------------------------------------
		
	}	

	/**
	 * This method search's for a similar node in the path (directed to the
	 * root node) to prevent a cyclic creation of form elements.
	 *
	 * @param objectClass the object class
	 * @param startNode the start node
	 * @return true, if successful
	 */
	private boolean objectAlreadyInPath(String objectClass, DefaultMutableTreeNode startNode){
		
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
	 * This method creates inner elements (for inner classes).
	 *
	 * @param oscsd the oscsd
	 * @param dataItemName the data item name
	 * @param dataItemVarType the data item var type
	 * @param dataItemCardinality the data item cardinality
	 * @param startObjectClassName the start object class name
	 * @param tiefe the depth
	 * @param parentPanel the parent panel
	 * @param parentNode the parent node
	 * @param addMultiple the add multiple
	 * @return the default mutable tree node
	 */
	private DefaultMutableTreeNode createInnerElements(OntologySingleClassSlotDescription oscsd, String startObjectClassName, int tiefe, final JPanel parentPanel, DefaultMutableTreeNode parentNode, boolean addMultiple){
		
		// --- create a JPanel which will contain further inner classes and fields
		final JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		int innerX  = 0;

		// set the value of how much the panel shall be shifted to the left
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
		valueFieldText.setText(oscsd.getSlotName() + " ["+oscsd.getSlotVarType()+"]");
		valueFieldText.setBounds(new Rectangle(10, 5, 300, 16));
		
		dataPanel.add(valueFieldText, null);
		this.setPanelBounds(dataPanel);

		// --- Create node for this element/panel ----------------------------- 
		DynType dynType = new DynType(oscsd, DynType.typeInnerClassType, startObjectClassName, dataPanel, oscsd.getSlotName());
		final DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dynType);
		parentNode.add(newNode);
		
		// --- if the inner class has got a multi cardinality create an add-button		
		if(oscsd.getSlotCardinality().equals("multiple")) {

			if (addMultiple==false) {
				// --- Add an add Button to the panel ---------------
				JButton multipleButton = new JButton("+");
				multipleButton.setBounds(new Rectangle(315, 2, 35, 25));
				multipleButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						addSingleMultipleNode(newNode, true);
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
		OntologySingleClassDescription osc = this.ontologyVisualisationHelper.getSlots4ClassAsObject(startObjectClassName);
		this.createGUI(osc, -1, tiefe, startObjectClassName, null, newNode, dataPanel);
		
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
	 * Creates the outer element for a Agent.GUI special class.
	 *
	 * @param specialClass the special class
	 * @param parentNode the parent node
	 * @param parentPanel the parent panel
	 */
	private void createOuterElement4AgentGUISpecialClass(int startArgIndex, Object specialClass, DefaultMutableTreeNode parentNode, JPanel parentPanel){
		
		// ------------------------------------------------------------------------------
		// --- Make all of the created panels invisible and reduce their height ---------
		// ------------------------------------------------------------------------------
		Rectangle feBounds = this.setJPanelInvisibleAndSmall(parentNode);
		
		// ------------------------------------------------------------------------------
		// --- Show the widget for the special type -------------------------------------
		// ------------------------------------------------------------------------------
		if (specialClass instanceof TimeSeriesChart) {
			// --- A TimeSeries has to be displayed -----------------
			TimeSeriesWidget tsWidget = new TimeSeriesWidget(this, startArgIndex);
			tsWidget.setBounds(feBounds.x, feBounds.y, feBounds.width, tsWidget.getHeight());
			parentPanel.add(tsWidget);
			
			this.userFormElements.put(parentNode, tsWidget);
			
		} else if (specialClass instanceof XyChart) {
			// --- A XY-Chart has to be displayed -------------------
			XyWidget xyWidget = new XyWidget(this, startArgIndex);
			xyWidget.setBounds(feBounds.x, feBounds.y, feBounds.width, xyWidget.getHeight());
			parentPanel.add(xyWidget);
		}
		
		this.setPanelBounds(parentPanel);
	}
	
	
	/**
	 * Sets the invisible.
	 * @param parentNode the new invisible
	 */
	private Rectangle setJPanelInvisibleAndSmall(DefaultMutableTreeNode parentNode) {
		
		Rectangle feBounds = null; // --- First element Bounds ------
		DynType parentDT = (DynType) parentNode.getUserObject();
		
		for (int i = 0; i < parentNode.getChildCount(); i++) {
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
			if (childNode.getChildCount()>0) {
				// --- recursively edit all sub panels --------------
				feBounds = this.setJPanelInvisibleAndSmall(childNode);
			}
			DynType dt = (DynType) childNode.getUserObject();
			
			if (feBounds==null) {
				feBounds = dt.getPanel().getBounds();
			}
			dt.getPanel().setVisible(false);
			dt.getPanel().setBounds(feBounds);
		}
		
		this.setPanelBounds(parentDT.getPanel());		
		return feBounds;
	}
	
	
	/**
	 * This method creates the panels for fields which have no inner classes.
	 *
	 * @param oscsd the OntologySingleClassSlotDescription
	 * @param tiefe the depth
	 * @param parentPanel the parent panel
	 * @param parentNode the parent node
	 * @param addMultiple the add multiple
	 * @return the default mutable tree node
	 */
	private DefaultMutableTreeNode createOuterElements(OntologySingleClassSlotDescription oscsd, int tiefe, JPanel parentPanel, DefaultMutableTreeNode parentNode, boolean addMultiple){
		
		// --- this outer element has no parents which are inner classes
		// --- so its added to the mainPanel
		final JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		dataPanel.setToolTipText(oscsd.getSlotName() + "-Panel");
		dataPanel.setBounds(new Rectangle(0, 0 , 350, 10));
		
		// --- add a JLabel to display the field's name
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText(oscsd.getSlotName() + " ["+oscsd.getSlotVarType()+"]");
		valueFieldText.setBounds(new Rectangle(0, 4, 130, 16));
		
		// --- valueFields (Textfield, Checkbox (for boolean) , ... )
		JComponent valueDisplay = this.getVisualComponent(oscsd.getSlotVarType());
		
		// --- add both GUI elements to the panel
		dataPanel.add(valueFieldText, null);
		dataPanel.add(valueDisplay);
		this.setPanelBounds(dataPanel);

		// --- add node to parent -------------------------
		DynType dynType = new DynType(oscsd, DynType.typeRawType, oscsd.getSlotVarTypeCorrected(), dataPanel, oscsd.getSlotName(), valueDisplay);
		final DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dynType); 
		parentNode.add(newNode);

		// --- if the inner class has got a multiple cardinality create an add-button
		if(oscsd.getSlotCardinality().equals("multiple")) {
			
			if (addMultiple==false) {
				// --- Add an add Button to the panel ---------------
				JButton multipleButton = new JButton("+");
				multipleButton.setBounds(new Rectangle(315, 0, 35, 25));
				multipleButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						addSingleMultipleNode(newNode, false);
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
	 * This method creates a so called dead end. This means that originally a class
	 * should be displayed which was already displayed on a higher level in direction
	 * to the root node. This was realized to prevent the form generation to be run
	 * in an endless loop.
	 *
	 * @param oscsd the oscsd
	 * @param dataItemName the data item name
	 * @param dataItemVarType the data item var type
	 * @param dataItemCardinality the data item cardinality
	 * @param className the class name
	 * @param pan the pan
	 * @param depth the depth
	 * @param node the node
	 */
	private void createOuterDeadEnd(OntologySingleClassSlotDescription oscsd, String className, int depth, JPanel pan, DefaultMutableTreeNode node){
		
		// --- this outer element has no parents which are inner classes
		// --- so its added to the mainPanel
		final JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		dataPanel.setToolTipText(oscsd.getSlotName() + "-Panel");
		
		// --- add a JLabel to display the field's name
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText("<html>" + oscsd.getSlotName() + " ["+oscsd.getSlotVarType()+"] - <b>" + Language.translate("Zyklisch !") + "</b></html>");
		valueFieldText.setBounds(new Rectangle(0, 4, 330, 16));
		
		// --- add both GUI elements to the panel
		dataPanel.add(valueFieldText, null);
		this.setPanelBounds(dataPanel);
		
		DynType dynType = new DynType(oscsd, DynType.typeCyclic, className, dataPanel, oscsd.getSlotName(), null);
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
	 * displayed for an ontology slot.
	 *
	 * @param valueType the value type
	 * @return the visual component
	 */
	private JComponent getVisualComponent(String valueType) {
	
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
	 * This method creates a copy of the passed JPanel and adds it.
	 *
	 * @param node the node
	 * @param isInnerClass the is inner class
	 * @return the default mutable tree node
	 */
	private DefaultMutableTreeNode addSingleMultipleNode(DefaultMutableTreeNode node, boolean isInnerClass){
		
		// --- Get all needed information about the node, which has to be copied --------
		DynType dt = (DynType) node.getUserObject();
		String clazz = dt.getClassName();
		OntologySingleClassSlotDescription oscsd = dt.getOntologySingleClassSlotDescription();
		
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
			newNode = this.createInnerElements(oscsd, clazz, depth2WorkOn+1, blindPanel, parentNode, true);
			if (oldPanel.isVisible()==false) {
				// --- Case special class: set invisible and small ------------ 
				this.setJPanelInvisibleAndSmall(newNode);
			}
			
		} else {
			newNode = this.createOuterElements(oscsd, depth2WorkOn, blindPanel, parentNode, true);
		}
		
		// --- Place the node at the right position in the tree -------------------------
		newNode.removeFromParent();
		int nodeIndexPos = parentNode.getIndex(node)+1;
		objectTree.insertNodeInto(newNode, parentNode, nodeIndexPos);
		
		// --- Set the size of the new Panel --------------------------------------------
		DynType dtNew = (DynType) newNode.getUserObject();
		JPanel newPanel = dtNew.getPanel();
		
		// --- Layout the new panel -----------------------------------------------------
		if (oldPanel.isVisible()==true) {
			// ----------------------------------------------------------------
			// --- The normal case for visible classes ------------------------
			// ----------------------------------------------------------------			
			this.setPanelBounds(newPanel);
			newPanel.setPreferredSize(newPanel.getSize());

			// --- Now place the new sub panel on the right super panel -------
			int movement = oldPanel.getHeight() + 2;
			int xPos = oldPanel.getX();
			int yPos = oldPanel.getY() + movement;
			newPanel.setBounds(xPos, yPos, newPanel.getWidth(), newPanel.getHeight());
			
			// --- Add to parent panel ----------------------------------------
			parentPanel.add(newPanel);
			parentPanel.validate();
			this.setPanelBounds(parentPanel);
			
			// --- Now move the rest of the elements on the form ----------------------------
			this.moveAfterAddOrRemove(movement, newNode);
			
		} else {
			// ----------------------------------------------------------------
			// --- The case for special classes, that have to be invisible ----
			// ----------------------------------------------------------------
			newPanel.setVisible(false);
			newPanel.setBounds(oldPanel.getBounds());
			
		}
		
		// --- refresh the GUI ----------------------------------------------------------
		this.setPreferredSize(this);
		this.validate();
		
		return newNode;
	}
	
	/**
	 * This method removes the passed JPanel.
	 *
	 * @param node the node
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
		node.setUserObject(null);
		parentNode.remove(node);
		
		// --- remove the panel from the parent -----------------------------------------
		parentPanel.remove(deletePanel);
		parentPanel.validate();
		this.setPanelBounds(parentPanel);
		
		// --- Now move the rest of the elements on the form ----------------------------
		this.moveAfterAddOrRemove(movement, previousNode);

		// --- refresh the GUI ----------------------------------------------------------
		this.setPreferredSize(this);
		this.validate();
		
	}

	/**
	 * Move all elements which are available after the node given by the parameter node.
	 *
	 * @param movement the movement
	 * @param node the node
	 */
	private void moveAfterAddOrRemove(int movement, DefaultMutableTreeNode node) {
		
		if (node==rootNode) {
			return;
		}
		
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
		JPanel parentPanel = null;
		if (parentNode.getUserObject() instanceof DynType) {
			DynType dynType = (DynType) parentNode.getUserObject();
			parentPanel = dynType.getPanel();
		}
		
		int numOfChilds = parentNode.getChildCount();
		int indexOfNextNode = parentNode.getIndex(node) + 1;
		
		for (int i = indexOfNextNode; i < numOfChilds; i++) {
			
			DefaultMutableTreeNode currNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);

			DynType dt = (DynType) currNode.getUserObject();
			JPanel movePanel = dt.getPanel();
			movePanel.setBounds(movePanel.getX(), movePanel.getY()+movement, movePanel.getWidth(), movePanel.getHeight());
			
			JComponent parentComp = (JComponent) movePanel.getParent();
			parentComp.validate();
			if (parentComp instanceof JPanel) {
				this.setPanelBounds((JPanel) parentComp);	
			}
			
		}

		// --- Configure size of parent panel -----------------------
		if (parentPanel!=null) {
			parentPanel.validate();
			this.setPanelBounds(parentPanel);
		}
		
		// --- do the same at the parent node -----------------------
		this.moveAfterAddOrRemove(movement, parentNode);
		
	}
	
	/**
	 * This Method sets the preferred size of a specified panel according
	 * to the position of the 'submitButton'.
	 *
	 * @param panel the new preferred size
	 */
	private void setPreferredSize(JPanel panel) {
		this.setPanelBounds(panel);
		this.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight() + 20));
	}
	
	/**
	 * This method can be invoked to generate the instance of the current configuration.
	 *
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
	 * Checks if the current class is an Agent.GUI special 
	 * class, like for Formulas or Charts.
	 *
	 * @param packageName the package name
	 * @param objectType the object type
	 * @return true, if the current request was for an Agent.GUI special class
	 */
	private Object isAgentGUISpecialClass(String packageName, String objectType) {

		if (isRawType(objectType)==false) {
			
			String testReference = packageName + "." + objectType;
			Class<?> testClass = null;
			Object testObject = null; 
			try {
				testClass  = Class.forName(testReference);
				testObject = testClass.newInstance();
				
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
			} catch (InstantiationException e) {
				//e.printStackTrace();
			} catch (IllegalAccessException e) {
				//e.printStackTrace();
			}
			
			if (testObject!=null) {
				if (testObject instanceof Chart) {
					return testObject;
				} else if (testObject instanceof Formula) {
					return testObject;
				}
			}
		}
		return null;
	}
	
	/**
	 * This method checks if the type of the field is a raw type
	 * (String, int, float, ...)
	 *
	 * @param valueType the value type
	 * @return true, if is special type
	 */
	private boolean isRawType(String valueType){
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
	 * Returns the instances of the ontology arguments.
	 * @return the agentArgsInstance
	 */
	public Object[] getOntoArgsInstance() {
		return ontoArgsInstance;
	}
	/**
	 * Sets the instances of the ontology arguments.
	 * @param ontologyInstances the new instances of the ontology arguments
	 */
	public void setOntoArgsInstance(Object[] ontologyInstances) {
		if (this.ontoArgsInstance==null) {
			this.save(true);
		} 
		if (ontologyInstances!=null) {
			this.ontoArgsInstance = ontologyInstances;
			this.setXMLFromInstances();
			this.setInstancesFromXML();	
		}
	}

	/**
	 * Returns the ontology arguments as XML string-array.
	 * @return the agentArgsXML
	 */
	public String[] getOntoArgsXML() {
		return ontoArgsXML;
	}
	/**
	 * Sets the ontology arguments as XML string-array.
	 * @param ontoArgsXML the new onto args xml
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
		
		/**
		 * Instantiates a new number watcher.
		 * @param floatValue indicates, if this watcher is used for Float values. If not Integer values are assumed.
		 */
		public NumberWatcher (boolean floatValue) {
			this.isFloatValue = floatValue;
		}

		/* (non-Javadoc)
		 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
		 */
		public void keyTyped(KeyEvent kT) {
			
			char charackter = kT.getKeyChar();
			String singleChar = Character.toString(charackter);
			
			JTextField displayField = (JTextField) kT.getComponent();
			String currValue = displayField.getText();
			int caretPosition = displayField.getCaretPosition();

			// --- Allow negative values ------------------
			if (singleChar.equals("-") && caretPosition==0 && currValue.startsWith("-")==false) {
				return;
			}
			if (this.isFloatValue==true) {
				// --- Float values -----------------------
				if (singleChar.equals(".") || singleChar.equals(",")) {
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
				// --- Integer values ---------------------
				if ( singleChar.matches( "[0-9]" ) == false ) {
					kT.consume();	
					return;
				}
				
			} // --- end if -------------------------------
			
		 }				 
	} // --- end sub class ----
	
}
