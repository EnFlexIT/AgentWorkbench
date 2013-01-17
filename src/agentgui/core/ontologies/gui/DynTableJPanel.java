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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.ProjectWindow;

/**
 * The Class DynTableJPanel.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DynTableJPanel extends JPanel {

	private static final long serialVersionUID = 6175657447251980498L;

	private static final int EXPANSION_Horizontal = 0;
	private static final int EXPANSION_Vertical = 1;
	
	private Container mainFrame = null;
	private Dimension mainFrameOldSize = null;
	private int expansionDirection = -1;
	
	private boolean expanded = false;
	private JSplitPane jSplitPaneExpanded = null;
	
	private DynForm dynForm = null;
	private JScrollPane jScrollPaneDynTable = null;
	private DynTable dynTable = null;
	private DynType dynType = null;  //  @jve:decl-index=0:
	
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
	}
	
	// --------------------------------------------------------------
	// --- Below methods for the expansion view and revert ---------- 
	// --------------------------------------------------------------	
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
	 *
	 * @param dynType the DynType
	 * @param startArgIndex the start argument index
	 */
	public void setOntologyClassVisualsationVisible(DynType dynType) {
		
		if (this.getDynType()==dynType) {
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
		
		this.removeAll();
		this.jSplitPaneExpanded=null;
		
		if (doExpand==true) {
			// --- Expand view and show widget or editor panel ----------------
			this.add(this.getJSplitPaneExpanded(), BorderLayout.CENTER);
			
			// --- Find the corresponding startArgIndex -----------------------
			int startArgIndex = this.getStartArgumentIndex(this.getDynType());
			
			// --- Configure the component for the display --------------------
			JComponent jComponent2Add = null;	
			String missingText = Language.translate("Die Klasse vom Typ 'OntologyClassVisualisation' wurde nicht gefunden!");
			OntologyClassVisualisation ontoClassVis = Application.getGlobalInfo().getOntologyClassVisualisation(this.getDynType().getClassName());
			if (ontoClassVis!=null) {
				switch (this.getExpansionDirection()) {
				case DynTableJPanel.EXPANSION_Horizontal:
					missingText = Language.translate("Das Editor-Panel für die Klasse ist nicht definiert!");
					OntologyClassEditorJPanel ocej = ontoClassVis.getEditorJPanel(this.dynForm, startArgIndex);
					jComponent2Add = ocej;
					break;

				case DynTableJPanel.EXPANSION_Vertical:
					missingText = Language.translate("Das Widget für die Klasse ist nicht definiert!");
					OntologyClassWidget ocw = ontoClassVis.getWidget(this.dynForm, startArgIndex);
					jComponent2Add = ocw;
					break;
				}
			} 
			if (jComponent2Add==null) {
				jComponent2Add = getJPanelEmpty(missingText);
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
				if (doExpand==true) {
					double newWidth = this.getContainerMainFrameOldSize().getWidth() + (4.0/3.0) * this.getContainerMainFrameOldSize().getHeight();

					Dimension mainFrameNewSize = new Dimension();
					mainFrameNewSize.setSize(newWidth, this.getContainerMainFrameOldSize().getHeight());
					this.getContainerMainFrame().setSize(mainFrameNewSize);
					
				} else {
					this.getContainerMainFrame().setSize(this.getContainerMainFrameOldSize());
				}	
				this.expanded=doExpand;
			}
			
			// --- Center, if dialog ------------------------------------
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
	 * Returns the old size of main container .
	 * @return the returns the container main frame old size
	 */
	private Dimension getContainerMainFrameOldSize() {
		if (mainFrameOldSize==null) {
			this.configureExpansionDirection();
		}
		return mainFrameOldSize;
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
