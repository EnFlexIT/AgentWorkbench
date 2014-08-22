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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.ProjectWindow;

/**
 * The Class DynTableJPanel is the base for the visualisation of the {@link DynTable}.
 * It manages among others to show {@link OntologyClassWidget}'s related the DynTable.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DynTableJPanel extends JPanel {

	private static final long serialVersionUID = 6175657447251980498L;

	private static final int EXPANSION_Horizontal = 0;
	private static final int EXPANSION_Vertical = 1;
	
	private Container mainFrame = null;
	private Dimension mainFrameOldSize = null;  //  @jve:decl-index=0:
	private int expansionDirection = -1;
	
	private boolean expanded = false;
	private JSplitPane jSplitPaneExpanded = null;
	
	private DynForm dynForm = null;
	private JScrollPane jScrollPaneDynTable = null;
	private DynTable dynTable = null;
	private DynType dynType = null;  //  @jve:decl-index=0:
	
	private JComponent jComponent2Add = null;
	
	private JToolBar jToolBar4UserFunction = null;
	private JToolBar.Separator jSeparator4UserFunctions1 = null;
	private JToolBar.Separator jSeparator4UserFunctions2 = null;
	private JLabel jLabelHeader4UserFunctions = null;
	private Vector<Component> stolenComponentsFromJToolBar = null;  //  @jve:decl-index=0:
	
	/**
	 * This is the default constructor
	 */
	public DynTableJPanel(DynForm dynForm) {
		super();
		this.dynForm = dynForm;
		initialize();
	}
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.configureExpansionDirection();
		this.add(this.getJScrollPaneDynTable(), BorderLayout.CENTER);
	}
	/**
	 * This method initialises jScrollPaneDynTable.
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneDynTable() {
		if (jScrollPaneDynTable == null) {
			jScrollPaneDynTable = new JScrollPane();
			jScrollPaneDynTable.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jScrollPaneDynTable.setViewportView(this.getDynTable());
		}
		return jScrollPaneDynTable;
	}
	/**
	 * Returns the current DynTable instance.
	 * @return the DynTable
	 */
	private DynTable getDynTable() {
		if (dynTable==null) {
			dynTable = new DynTable(this.dynForm, this);
		}
		return dynTable;
	}
	/**
	 * Stops the DynTable cell editing.
	 */
	public void stopDynTableCellEditing() {
		TableCellEditor cellEditor = this.getDynTable().getCellEditor();
		if (cellEditor!=null) {
			cellEditor.stopCellEditing();
		}
	}
	/**
	 * Refreshes the TableModel.
	 */
	public void refreshTableModel() {
		this.getDynTable().refreshTableModel();
		this.setOntologyClassInstanceToOntologyClassVisualisation();
	}
	
	// --------------------------------------------------------------
	// --- Below methods for the expansion view and revert ---------- 
	// --------------------------------------------------------------	
	/**
	 * Sets the current instance of the ontology class to the OntologyClassWidget or OntologyClassEditorJPanel.
	 * 
	 * @see OntologyClassWidget
	 * @see OntologyClassEditorJPanel
	 */
	public void setOntologyClassInstanceToOntologyClassVisualisation(){
		// --- Only do the following if the DynTableJPanel is expanded ---
		if (this.expanded==true && this.jComponent2Add!=null) {
			
			DefaultMutableTreeNode node = this.dynForm.getTreeNodeByDynType(this.getDynType());
			OntologyClassWidget dynFormWidget = this.dynForm.getOntologyClassWidget(node);
			Object classInstance = dynFormWidget.invokeGetOntologyClassInstance();
			
			if (this.jComponent2Add instanceof OntologyClassWidget) {
				OntologyClassWidget widget = (OntologyClassWidget) this.jComponent2Add; 
				widget.invokeSetOntologyClassInstance(classInstance);
			} else if (this.jComponent2Add instanceof OntologyClassEditorJPanel) {
				OntologyClassEditorJPanel editorPanel = (OntologyClassEditorJPanel) this.jComponent2Add;
				editorPanel.invokeSetOntologyClassInstance(classInstance);
			}
		
		}
	}
	
	/**
	 * Sets the ontology class instance to the DynForm.
	 */
	public void setOntologyClassInstanceToDynForm() {
		// --- Only do the following if the DynTableJPanel is expanded ---
		if (this.expanded==true) {
			
			int argumentIndex = this.getStartArgumentIndex(this.getDynType());
			DefaultMutableTreeNode node = this.dynForm.getTreeNodeByDynType(this.getDynType());
			OntologyClassWidget dynFormWidget = this.dynForm.getOntologyClassWidget(node);
			
			Object classInstance = this.getOntologyClassInstanceOfOntologyClassVisualisation();
			this.dynForm.getOntoArgsInstance()[argumentIndex] = classInstance;
			this.dynForm.setFormState(classInstance, node);
			dynFormWidget.invokeSetOntologyClassInstance(classInstance);
		}
	}

	/**
	 * Gets the current instance of the ontology class edited in the OntologyClassWidget or OntologyClassEditorJPanel.
	 * 
	 * @see OntologyClassWidget
	 * @see OntologyClassEditorJPanel
	 * @return the ontology class instance of widget or panel
	 */
	private Object getOntologyClassInstanceOfOntologyClassVisualisation(){
		Object classInstance = null;
		if (this.jComponent2Add!=null) {
			if (this.jComponent2Add instanceof OntologyClassWidget) {
				OntologyClassWidget widget = (OntologyClassWidget) this.jComponent2Add; 
				classInstance = widget.invokeGetOntologyClassInstance();
			} else if (this.jComponent2Add instanceof OntologyClassEditorJPanel) {
				OntologyClassEditorJPanel editorPanel = (OntologyClassEditorJPanel) this.jComponent2Add;
				classInstance = editorPanel.invokeGetOntologyClassInstance();
			}
		}
		return classInstance;
	}

	/**
	 * Sets the current DynType.
	 * @param dynType the new DynType
	 */
	private void setDynType(DynType dynType) {
		this.dynType = dynType;
	}
	/**
	 * Returns the current DynType.
	 * @return the DynType
	 */
	private DynType getDynType() {
		return dynType;
	}
	
	/**
	 * Sets the ontology class-visualsation visible.
	 * @param dynType the DynType
	 */
	public void setOntologyClassVisualsationVisible(DynType dynType) {
		
		// --- Save an open OntologyClassVisualisation to the DynForm ---------
		this.setOntologyClassInstanceToDynForm();
		
		if (dynType==null || this.getDynType()==dynType) {
			// --- Same element, disappear ----------------
			this.setJPanelConfiguration(false);	
			this.setDynType(null);
			
		} else {
			// --- Display element ------------------------
			this.setDynType(dynType);
			this.setJPanelConfiguration(true);
			
		}
		this.validate();
		this.repaint();
	}

	/**
	 * Sets the panel configuration.
	 *
	 * @param dynType the DynType
	 * @param startArgIndex the start argument index
	 * @param doExpand the new frame configuration
	 */
	private void setJPanelConfiguration(boolean doExpand) {
		
		if (this.getDynType()==null) doExpand=false;

		// --- Clean up the old instances -------------------------------------
		this.removeAll();
		this.jSplitPaneExpanded=null;
		
		this.removeUserFunctions2JToolBar4UserFunction();
		this.stolenComponentsFromJToolBar=null;
		this.jComponent2Add=null;
		
		// --- Do expand the view (or not) ------------------------------------
		if (doExpand==true) {
			// --- Expand view and show widget or editor panel ----------------
			this.add(this.getJSplitPaneExpanded(), BorderLayout.CENTER);
			
			// --- Find the corresponding startArgIndex -----------------------
			int startArgIndex = this.getStartArgumentIndex(this.getDynType());
			
			// --- Configure the component for the display --------------------
			String missingText = Language.translate("Die Klasse vom Typ 'OntologyClassVisualisation' wurde nicht gefunden!");
			OntologyClassVisualisation ontoClassVis = Application.getGlobalInfo().getOntologyClassVisualisation(this.getDynType().getClassName());
			if (ontoClassVis!=null) {
				switch (this.getExpansionDirection()) {
				case DynTableJPanel.EXPANSION_Horizontal:
					missingText = Language.translate("Das Editor-Panel für die Klasse ist nicht definiert!");
					OntologyClassEditorJPanel ocej = ontoClassVis.getEditorJPanel(this.dynForm, startArgIndex);
					this.jComponent2Add = ocej;
					
					DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) this.dynForm.getObjectTree().getRoot();
					DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) rootNode.getChildAt(startArgIndex);
					DynType dynType = (DynType) childNode.getUserObject();
					
					String clazzName = dynType.getClassName();
					if (clazzName.contains(".")==true) {
						clazzName = clazzName.substring(clazzName.lastIndexOf(".")+1);
					}
					this.stealJToolBarFromOntologyClassEditorJPanel(ocej);
					this.addUserFunctions2JToolBar4UserFunction(clazzName);
					break;

				case DynTableJPanel.EXPANSION_Vertical:
					missingText = Language.translate("Das Widget für die Klasse ist nicht definiert!");
					OntologyClassWidget ocw = ontoClassVis.getWidget(this.dynForm, startArgIndex);
					this.jComponent2Add = ocw;
					break;
				}
			} 
			if (this.jComponent2Add==null) {
				this.jComponent2Add = getJPanelEmpty(missingText);
			}
			
			// --- Add the component to the JSplitPane ------------------------
			switch (this.getExpansionDirection()) {
			case DynTableJPanel.EXPANSION_Horizontal:
				this.getJSplitPaneExpanded().setRightComponent(jComponent2Add);
				break;

			case DynTableJPanel.EXPANSION_Vertical:
				this.getJSplitPaneExpanded().setBottomComponent(jComponent2Add);
				break;
			}
						
		} else {
			// --- Remove the Split view --------------------------------------
			this.add(this.getJScrollPaneDynTable(), BorderLayout.CENTER);
			
		}
		// --- Control the expansion of the mainFraime ------------------------
		this.expandMainFrame(doExpand);

	}
	
	/**
	 * Gets the current OntologyClassEditorJPanel if the current one applies for 
	 * the specified data model index.
	 *
	 * @param targetDataModelIndex the target data model index
	 * @return the OntologyClassEditorJPanel
	 */
	public OntologyClassEditorJPanel getOntologyClassEditorJPanel(int targetDataModelIndex) {

		OntologyClassEditorJPanel ocep = null;
		if (this.jComponent2Add!=null && this.jComponent2Add instanceof OntologyClassEditorJPanel) {
			if (this.getStartArgumentIndex(this.dynType) == targetDataModelIndex) {
				ocep = (OntologyClassEditorJPanel) this.jComponent2Add; 
			}
		}
		return ocep;
	}
	
	/**
	 * Can be used to specify a JToolBar where user functions of 
	 * a {@link OntologyClassEditorJPanel} can be placed.
	 *
	 * @param jToolBar4UserFunction the new JToolBar for user function
	 */
	public void setJToolBar4UserFunctions(JToolBar jToolBar4UserFunction) {
		this.jToolBar4UserFunction = jToolBar4UserFunction;
	}
	
	/**
	 * Steal the components of a JToolBar for a {@link OntologyClassEditorJPanel}.
	 * @param ocep the OntologyClassEditorJPanel
	 */
	private void stealJToolBarFromOntologyClassEditorJPanel(OntologyClassEditorJPanel ocep) {
		
		if (ocep==null) return;
		if (this.jToolBar4UserFunction==null) return;
		
		JToolBar jToolBarUserFunctions = ocep.getJToolBarUserFunctions();
		if (jToolBarUserFunctions!=null) {
			Container containerUserFunctions = jToolBarUserFunctions.getParent();
			while (jToolBarUserFunctions.getComponentCount()>0) {
				Component comp = jToolBarUserFunctions.getComponent(0);
				this.getStolenComponentsFromJToolBarOfOntologyClassEditorJPanel().add(comp);
				jToolBarUserFunctions.remove(comp);
			}
			if (containerUserFunctions!=null) {
				containerUserFunctions.remove(jToolBarUserFunctions);	
			}
			ocep.validate();
			ocep.repaint();	
		}
		
	}
	/**
	 * Returns the Vector of the stolen components from the customized JToolBar of a OntologyClassEditorJPanel.
	 * @return the stolen components from a JToolBar of a OntologyClassEditorJPanel
	 */
	private Vector<Component> getStolenComponentsFromJToolBarOfOntologyClassEditorJPanel() {
		if (this.stolenComponentsFromJToolBar==null) {
			this.stolenComponentsFromJToolBar = new Vector<Component>();
		}
		return stolenComponentsFromJToolBar;
	}
	
	/**
	 * Adds the user functions to the locally specified jToolBar4UserFunction.
	 * @param headerText the header text
	 */
	private void addUserFunctions2JToolBar4UserFunction(String headerText) {
		
		if (this.jToolBar4UserFunction==null) return;
		if (this.getStolenComponentsFromJToolBarOfOntologyClassEditorJPanel().size()==0) return;
		
		this.jSeparator4UserFunctions1 = new JToolBar.Separator();
		this.jToolBar4UserFunction.add(this.jSeparator4UserFunctions1);
		
		this.jLabelHeader4UserFunctions = new JLabel(headerText + ":");
		this.jLabelHeader4UserFunctions.setFont(new Font("Arial", Font.BOLD, 12));
		this.jToolBar4UserFunction.add(this.jLabelHeader4UserFunctions);
		
		this.jSeparator4UserFunctions2 = new JToolBar.Separator();
		this.jToolBar4UserFunction.add(this.jSeparator4UserFunctions2);
		
		Vector<Component> comps = this.getStolenComponentsFromJToolBarOfOntologyClassEditorJPanel();
		for (int i = 0; i < comps.size(); i++) {
			this.jToolBar4UserFunction.add(comps.get(i));
		}
		this.jToolBar4UserFunction.validate();
		this.jToolBar4UserFunction.repaint();
	}
	/**
	 * Removes the user functions from the locally specified jToolBar4UserFunction.
	 */
	private void removeUserFunctions2JToolBar4UserFunction() {
		
		if (this.jToolBar4UserFunction==null) return;
		if (this.getStolenComponentsFromJToolBarOfOntologyClassEditorJPanel().size()==0) return;
		
		Vector<Component> comps = this.getStolenComponentsFromJToolBarOfOntologyClassEditorJPanel();
		for (int i = 0; i < comps.size(); i++) {
			this.jToolBar4UserFunction.remove(comps.get(i));
		}
		
		this.jToolBar4UserFunction.remove(this.jSeparator4UserFunctions2);
		this.jToolBar4UserFunction.remove(this.jLabelHeader4UserFunctions);
		this.jToolBar4UserFunction.remove(this.jSeparator4UserFunctions1);
		
		this.jToolBar4UserFunction.validate();
		this.jToolBar4UserFunction.repaint();
	}
	
	/**
	 * Gets the start argument index of the current {@link DynType}.
	 * @param dynType the DynType
	 * @return the start argument index
	 */
	private int getStartArgumentIndex(DynType dynType) {
		DefaultMutableTreeNode child = this.dynForm.getTreeNodeByDynType(dynType);
		DefaultMutableTreeNode parent= (DefaultMutableTreeNode) child.getParent(); 
		return parent.getIndex(child);
	}
	
	/**
	 * Returns an empty JPanel with an specified test on it.
	 *
	 * @param text the text to display
	 * @return the JPanel
	 */
	private JPanel getJPanelEmpty(String text) {

		JLabel jLabelText = new JLabel(text);
		jLabelText.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelText.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel emptyPanel = new JPanel();
		emptyPanel.setLayout(new BorderLayout());
		emptyPanel.add(jLabelText, BorderLayout.CENTER);
		return emptyPanel;
	}
	
	/**
	 * Expand main frame.
	 * @param doExpand the do expand
	 */
	private void expandMainFrame(boolean doExpand) {
		
		if (this.getContainerMainFrame()!=null && this.getExpansionDirection()==EXPANSION_Horizontal) {
			
			if (doExpand!=this.expanded) {
				Dimension newMainFrameSize = new Dimension();
				if (doExpand==true) {
					// --- Expand view ------------------------------
					this.setContainerMainFrameOldSize(this.getContainerMainFrame().getSize());
					double newWidth = this.getContainerMainFrameOldSize().getWidth() + (4.0/3.0) * this.getContainerMainFrameOldSize().getHeight();
					newMainFrameSize.setSize(newWidth, this.getContainerMainFrameOldSize().getHeight());
				} else {
					// --- Restore view -----------------------------
					newMainFrameSize.setSize(this.getContainerMainFrameOldSize().getWidth(), this.getContainerMainFrame().getSize().getHeight());
				}	
				this.getContainerMainFrame().setSize(newMainFrameSize);
				this.expanded=doExpand;
			}
			
			// --- Center, if dialog --------------------------------
			if (this.getContainerMainFrame() instanceof JDialog) {
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
				int top = (screenSize.height - this.getContainerMainFrame().getHeight()) / 2; 
			    int left = (screenSize.width - this.getContainerMainFrame().getWidth()) / 2; 
			    this.getContainerMainFrame().setLocation(left, top);	
			}

		}
		
	}
	
	/**
	 * Gets the JSplitPane for the expanded view.
	 *
	 * @param orientation the orientation
	 * @return the j split pane expanded
	 */
	private JSplitPane getJSplitPaneExpanded() {
		
		if (this.jSplitPaneExpanded==null) {
			this.jSplitPaneExpanded = new JSplitPane(); 
			this.jSplitPaneExpanded.setOneTouchExpandable(true);
			this.jSplitPaneExpanded.setResizeWeight(0);
			this.jSplitPaneExpanded.setBorder(BorderFactory.createEmptyBorder());
			
			if (this.getExpansionDirection()==EXPANSION_Horizontal) {
				this.jSplitPaneExpanded.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
				this.jSplitPaneExpanded.setDividerLocation((int)this.getContainerMainFrameOldSize().getWidth()-this.jSplitPaneExpanded.getDividerSize());
				this.jSplitPaneExpanded.setLeftComponent(this.getJScrollPaneDynTable());
				
			} else {
				int dividerPosition = (int) (this.getContainerMainFrameOldSize().getHeight() - ((9.0/16.0) * this.getContainerMainFrameOldSize().getWidth()));
				this.jSplitPaneExpanded.setOrientation(JSplitPane.VERTICAL_SPLIT);
				this.jSplitPaneExpanded.setDividerLocation(dividerPosition);
				this.jSplitPaneExpanded.setTopComponent(this.getJScrollPaneDynTable());
			}

		}
		return jSplitPaneExpanded;
	}
	
	/**
	 * Gets the corresponding main container for this Swing construction.
	 * @return the main frame container
	 */
	private Container getContainerMainFrame() {
		if (mainFrame==null) {
			this.configureExpansionDirection();
		}
		return mainFrame;
	}
	/**
	 * Sets the old size of main frame container .
	 * @param oldSize the new old size of the main frame container 
	 */
	private void setContainerMainFrameOldSize(Dimension oldSize) {
		if (oldSize==null) return;
		this.mainFrameOldSize=oldSize;
	}
	/**
	 * Returns the old size of main container .
	 * @return the returns the container main frame old size
	 */
	private Dimension getContainerMainFrameOldSize() {
		if (this.mainFrameOldSize==null) {
			this.configureExpansionDirection();
		}
		return this.mainFrameOldSize;
	}
	/**
	 * Returns the expansion direction.
	 * @return the expansion direction
	 */
	private int getExpansionDirection() {
		if (this.expansionDirection==-1) {
			this.configureExpansionDirection();
		}
		return this.expansionDirection;
	}
	/**
	 * Sets the expansion direction.
	 */
	private void configureExpansionDirection() {
		
		if (this.getParent()==null) return;
		
		boolean isExtraDialog = false;
		this.mainFrame = null;
		this.mainFrameOldSize = this.getSize();
		
		// --- Find out how the OntologyInstanceViewer is embedded --
		Container frame = this.getParent();
		while (frame.getParent()!=null) {
			frame = frame.getParent();
			if (frame instanceof ProjectWindow) {
				isExtraDialog=false;
				break;	
			} else {
				if (frame instanceof JDialog) {
					isExtraDialog=true;
					this.mainFrame=frame;
					this.mainFrameOldSize = this.mainFrame.getSize();
					break;
				} else if (frame instanceof JInternalFrame) {
					isExtraDialog=true;
					this.mainFrame=frame;
					this.mainFrameOldSize = this.mainFrame.getSize();
					break;
				}
			}
		}
		
		// --- Set the expansion direction --------------------------
		if (isExtraDialog==true) {
			this.expansionDirection = EXPANSION_Horizontal;
		} else {
			this.expansionDirection = EXPANSION_Vertical;
		}	
	}
	
}
