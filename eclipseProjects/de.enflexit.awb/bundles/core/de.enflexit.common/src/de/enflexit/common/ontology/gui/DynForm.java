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
package de.enflexit.common.ontology.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.language.Language;
import de.enflexit.common.ontology.AgentStartArgument;
import de.enflexit.common.ontology.AgentStartConfiguration;
import de.enflexit.common.ontology.OntologyClassTree;
import de.enflexit.common.ontology.OntologySingleClassDescription;
import de.enflexit.common.ontology.OntologySingleClassSlotDescription;
import de.enflexit.common.ontology.OntologyVisualisationConfiguration;
import de.enflexit.common.ontology.OntologyVisualizationHelper;

/**
 * This class can be used in order to generate a Swing based user form, that represents
 * the structure of one or more ontology-classes.
 * 
 * @author Marvin Steinberg - University of Duisburg - Essen
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DynForm extends DynFormBase {

	private int indentPerLevel = 5;
	
	private JPanel jPanelOntologyVisualization;
	
	
	/**
	 * Constructor of this class by using a project and an agent reference.
	 *
	 * @param ontologyVisualisationHelper the {@link OntologyVisualizationHelper}
	 * @param agentStartConfiguration the {@link AgentStartConfiguration}
	 * @param agentReference the agent reference
	 */
	public DynForm(OntologyVisualizationHelper ontologyVisualisationHelper, AgentStartConfiguration agentStartConfiguration, String agentReference) {
		
		super();
		this.ontologyVisualisationHelper = ontologyVisualisationHelper;
		this.agentStartConfiguration = agentStartConfiguration;
	
		// --- Prevent errors through empty agent references --------
		if (agentReference!=null) {
			// --- Find Agent in AgentConfig ------------------------
			if (this.agentStartConfiguration.containsKey(agentReference)==true) {
				
				// --- Which classes are configured for the Agent? -- 
				Vector<AgentStartArgument> startArgs = this.agentStartConfiguration.get(agentReference);
				for (int i = 0; i < startArgs.size(); i++) {
					this.currOntologyClassReferenceList.add(startArgs.get(i));
				}

				// --- Start building the GUI -----------------------
				this.getJPanelOntologyVisualization();
				this.setIsEmptyForm(false);
				// --- If wanted show some debug informations -------
				if (debug==true) {
					this.showObjectTree();
				}
			}
		}
	}
	
	/**
	 * Constructor of this class by using an instance of an ontology and
	 * the reference(s) of a needed class out of the ontology.
	 *
	 * @param ontologyVisualisationHelper the {@link OntologyVisualizationHelper}
	 * @param ontologyClassReferences the ontology class references
	 */
	public DynForm(OntologyVisualizationHelper ontologyVisualisationHelper, String[] ontologyClassReferences) {

		super();
		this.ontologyVisualisationHelper = ontologyVisualisationHelper;
		
		// --- Was something set? -----------------------------------
		if (ontologyClassReferences!= null && ontologyClassReferences.length>0) {
			// --- Take that to the local Vector --------------------
			for (int i = 0; i < ontologyClassReferences.length; i++) {
				AgentStartArgument startArgument = new AgentStartArgument(i+1, ontologyClassReferences[i]);
				this.currOntologyClassReferenceList.add(startArgument);
			}

			// --- Start building the GUI -----------------------
			this.getJPanelOntologyVisualization();
			this.setIsEmptyForm(false);
			// --- If wanted show some debug informations -------
			if (debug==true) {
				this.showObjectTree();
			}
			
		}
	}
	
	/**
	 * This method starts building the Swing GUI.
	 * @return the j panel ontology visualisation
	 */
	public JPanel getJPanelOntologyVisualization(){
		if (jPanelOntologyVisualization==null) {
			jPanelOntologyVisualization = new JPanel();
			jPanelOntologyVisualization.setLayout(null);
			
			int yPos = 0;

			// --- Maybe a debug print to the console ---------
			if (this.debug==true && this.currOntologyClassReferenceList.size()!=0) {
				System.out.println("Creating GUI");	
			}	
			
			// --- Iterate over the available Start-Objects ---
			for (int i = 0; i < this.currOntologyClassReferenceList.size(); i++) {
				AgentStartArgument agentStartArgument = this.currOntologyClassReferenceList.get(i); 
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
						Sorter.sortSlotDescriptionArray(osc.getArrayList4SlotDescriptions());
						this.createGUI(osc, i, 0, startObjectClass, startObjectClassMask, (DefaultMutableTreeNode) this.getRootNode(), startObjectPanel);
					} else {
						System.out.println("Could not get OntologySingleClassDescription for " + startObjectClass);
					}

					// --- 
					this.setPanelBounds(startObjectPanel);
					startObjectPanel.setLocation(0, yPos);
					jPanelOntologyVisualization.add(startObjectPanel);
					
					// --- Configure the next position for a panel ----------
					yPos = yPos + ((int)startObjectPanel.getBounds().getHeight()) + 2;
				}
				
			}
			// --- Justify the Preferred Size of this Panel ---
			this.adjustPreferredSize();
			this.save(true);
		}
		return jPanelOntologyVisualization;
	}
	
	/**
	 * This method adds the class to a panel and initiates to add
	 * possible inner classes and fields also to the panel or its sub panels.
	 *
	 * @param oscd the OntologySingleClassDescription
	 * @param startArgIndex the start arg index
	 * @param depth the depth of the node
	 * @param startObjectClass the start object class
	 * @param startObjectClassMask the start object class mask
	 * @param parentNode the parent node
	 * @param parentPanel the parent panel
	 */
	private void createGUI(OntologySingleClassDescription oscd, int startArgIndex, int depth, String startObjectClass, String startObjectClassMask,  DefaultMutableTreeNode parentNode, JPanel parentPanel){
		
		String startObjectClassName = startObjectClass.substring(startObjectClass.lastIndexOf(".") + 1, startObjectClass.length());
		String startObjectPackage = startObjectClass.substring(0, startObjectClass.lastIndexOf("."));

		JLabel objectLabelName = new JLabel();		
		if (oscd != null) {
			// --- if we are on the main panel -> add the class name to it !
			// --- Create a JPanel in which the class name (JLabel) and its inner classes
			// --- and/or fields are added instead of mainPanel - class name - inner classes/fields
			
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
				this.getTreeNodesByDynType().put(dynType, newNode);
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
			for (int i = 0; i < oscd.getArrayList4SlotDescriptions().size(); i++) {

				OntologySingleClassSlotDescription oscsd = oscd.getArrayList4SlotDescriptions().get(i);
				if (this.isRawType(oscsd.getSlotVarTypeCorrected())){
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
		if (OntologyVisualisationConfiguration.isRegisteredOntologyClassVisualisation(startObjectClass)==true) {
			// -----------------------------------------------------------------
			// --- Removed since the form view will not be shown anymore -------
			// -----------------------------------------------------------------
			//this.createOuterElement4OntologyClassVisualisation(startArgIndex, oscd.getClazz(), parentNode, parentPanel);
			// -----------------------------------------------------------------
		}
		// ---------------------------------------------------------------------
	}	
	/**
	 * Creates the outer element for a Agent.GUI special class.
	 *
	 * @param startArgIndex the start arg index
	 * @param specialClass the special class
	 * @param curentNode the parent node
	 * @param parentPanel the parent panel
	 */
	@SuppressWarnings("unused")
	private void createOuterElement4OntologyClassVisualisation(int startArgIndex, Class<?> specialClass, DefaultMutableTreeNode curentNode, JPanel parentPanel){
		
		this.save(true);
		
		// --- Make all of the created panels invisible and reduce their height ---------
		Rectangle feBounds = this.setJPanelInvisibleAndSmall(curentNode);
		
		// --- Show the widget for the special type -------------------------------------
		OntologyClassVisualisation ontoClassVis = OntologyVisualisationConfiguration.getOntologyClassVisualisation(specialClass);
		OntologyClassWidget widget = ontoClassVis.getWidget(this, startArgIndex);
		if (widget!=null) {
			widget.setBounds(feBounds.x, feBounds.y, widget.getWidth(), widget.getHeight());
			parentPanel.add(widget);
			
			this.getOntologyClassWidgets().put(curentNode, widget);
		}
		this.setPanelBounds(parentPanel);
	}
	
	/**
	 * This Method sets the preferred size of a specified panel according
	 * to the position of the 'submitButton'.
	 */
	private void adjustPreferredSize() {
		JPanel panel = this.jPanelOntologyVisualization;
		this.setPanelBounds(panel);
		this.jPanelOntologyVisualization.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight() + 20));
		this.jPanelOntologyVisualization.validate();
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
		while (currNode!=this.getRootNode()) {
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
			innerX = (indentPerLevel*(tiefe-1));
		else
			innerX = (indentPerLevel*(tiefe));
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
		this.getTreeNodesByDynType().put(dynType, newNode);
		
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
		this.getTreeNodesByDynType().put(dynType, newNode);

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
	 * @param className the class name
	 * @param depth the depth
	 * @param pan the pan
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
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.ontology.gui.DynFormAbstractBase#addSingleMultipleNode(javax.swing.tree.DefaultMutableTreeNode, boolean)
	 */
	@Override
	protected DefaultMutableTreeNode addSingleMultipleNode(DefaultMutableTreeNode node, boolean isInnerClass){
		
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
		this.adjustPreferredSize();
		
		return newNode;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.ontology.gui.DynFormAbstractBase#removeMultiple(javax.swing.tree.DefaultMutableTreeNode)
	 */
	@Override
	protected void removeMultiple(DefaultMutableTreeNode node){
		
		// --- Remind all needed informations -------------------------------------------
		DefaultMutableTreeNode previousNode = node.getPreviousNode();
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
		
		DynType dt = (DynType) node.getUserObject();
		JPanel deletePanel = dt.getPanel();
		JPanel parentPanel = (JPanel) deletePanel.getParent();
		
		int movement = (deletePanel.getHeight() + 2) * (-1);
		
		// --- Remove node from the parent node and panel -------------------------------
		DynType dyntype = (DynType) node.getUserObject();
		node.setUserObject(null);
		parentNode.remove(node);
		this.getTreeNodesByDynType().remove(dyntype);
		
		// --- remove the panel from the parent -----------------------------------------
		parentPanel.remove(deletePanel);
		parentPanel.validate();
		this.setPanelBounds(parentPanel);
		
		// --- Now move the rest of the elements on the form ----------------------------
		this.moveAfterAddOrRemove(movement, previousNode);

		// --- refresh the GUI ----------------------------------------------------------
		this.adjustPreferredSize();
		
	}

	/**
	 * Move all elements which are available after the node given by the parameter node.
	 *
	 * @param movement the movement
	 * @param node the node
	 */
	private void moveAfterAddOrRemove(int movement, DefaultMutableTreeNode node) {
		
		if (node==this.getRootNode()) return;
		
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
	 * Sets the invisible.
	 *
	 * @param parentNode the new invisible
	 * @return the rectangle
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
	 * Returns the instances of the ontology arguments.
	 * @return the agentArgsInstance
	 */
	public Object[] getOntoArgsInstance() {
		return ontoArgsInstance;
	}
	/**
	 * Returns a copy of the current ontology arguments instances.
	 * @return the agentArgsInstance
	 */
	public Object[] getOntoArgsInstanceCopy() {
		return this.getInstancesFromXML(ontoArgsXML);
	}
	
	/**
	 * Sets the instances of the ontology arguments.
	 * @param ontologyInstances the new instances of the ontology arguments
	 */
	public void setOntoArgsInstance(Object[] ontologyInstances) {
		if (ontologyInstances==null) {
			this.resetValuesOnForm();
		} else {
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

}
