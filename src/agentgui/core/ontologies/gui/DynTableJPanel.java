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
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellEditor;

import agentgui.core.gui.ProjectWindow;

/**
 * The Class DynTableJPanel.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DynTableJPanel extends JPanel {

	private static final long serialVersionUID = 6175657447251980498L;

	private static int EXPANSION_Horizontal = 0;
	private static int EXPANSION_Vertical = 1;
	
	private int expansionDirection = -1;
	
	private Container mainFrame = null;
	private Dimension mainFrameOldSize = null;
	private boolean expanded = false;
	private JSplitPane jSplitPaneExpanded = null;
	
	private DynForm dynForm = null;
	private JScrollPane jScrollPaneDynTable = null;
	private DynTable dynTable = null;
	  
	
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
	 * Sets the ontology class-visualsation visible.
	 * @param visible the new ontology class-visualsation visible
	 */
	public void setOntologyClassVisualsationVisible(boolean visible) {
		if (this.expanded==true) {
			this.setJPanelConfiguration(false);	
		} else {
			this.setJPanelConfiguration(true);
		}

		this.validate();
		this.repaint();
	}

	/**
	 * Sets the panel configuration.
	 * @param doExpand the new frame configuration
	 */
	private void setJPanelConfiguration(boolean doExpand) {
		
		if (doExpand==this.expanded) return; // --- nothing to do here ---
		
		this.removeAll();
		if (doExpand==true) {
			this.configureExpansionDirection();
			this.add(this.getJSplitPaneExpanded(), BorderLayout.CENTER);
			
		} else {
			this.jSplitPaneExpanded=null;
			this.add(this.getJScrollPaneDynTable(), BorderLayout.CENTER);
			
		}
		this.expandMainFrame(doExpand);
		this.expanded=doExpand;
	}
	
	/**
	 * Expand main frame.
	 * @param doExpand the do expand
	 */
	private void expandMainFrame(boolean doExpand) {
		
		if (this.mainFrame!=null && this.expansionDirection==EXPANSION_Horizontal) {
			
			if (doExpand==true) {
				double newWidth = this.mainFrameOldSize.getWidth() + (4.0/3.0) * this.mainFrameOldSize.getHeight();

				Dimension mainFrameNewSize = new Dimension();
				mainFrameNewSize.setSize(newWidth, this.mainFrameOldSize.getHeight());
				this.mainFrame.setSize(mainFrameNewSize);
				
			} else {
				this.mainFrame.setSize(mainFrameOldSize);
			}	

			// --- Center, if dialog ------------------------------------
			if (this.mainFrame instanceof JDialog) {
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
				int top = (screenSize.height - this.mainFrame.getHeight()) / 2; 
			    int left = (screenSize.width - this.mainFrame.getWidth()) / 2; 
			    this.mainFrame.setLocation(left, top);	
			}

		}
		
	}
	
	/**
	 * Sets the expansion direction.
	 */
	private void configureExpansionDirection() {
		
		boolean isExtraDialog = false;
		this.mainFrame=null;
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

	/**
	 * Gets the JSplitPane for the expanded view.
	 *
	 * @param orientation the orientation
	 * @return the j split pane expanded
	 */
	private JSplitPane getJSplitPaneExpanded() {
		
		this.jSplitPaneExpanded = new JSplitPane(); 
		this.jSplitPaneExpanded.setOneTouchExpandable(true);
		this.jSplitPaneExpanded.setBorder(BorderFactory.createEmptyBorder());
		
		if (this.expansionDirection==EXPANSION_Horizontal) {
			this.jSplitPaneExpanded.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			this.jSplitPaneExpanded.setDividerLocation((int)this.mainFrameOldSize.getWidth()-this.jSplitPaneExpanded.getDividerSize());
			this.jSplitPaneExpanded.setLeftComponent(this.getJScrollPaneDynTable());
			//splitPaneExpanded.setRightComponent();
			
		} else {
			int dividerPosition = (int) (this.mainFrameOldSize.getHeight() - ((9.0/16.0) * this.mainFrameOldSize.getWidth()));
			this.jSplitPaneExpanded.setOrientation(JSplitPane.VERTICAL_SPLIT);
			this.jSplitPaneExpanded.setDividerLocation(dividerPosition);
			this.jSplitPaneExpanded.setTopComponent(this.getJScrollPaneDynTable());
			//splitPaneExpanded.setBottomComponent();
		}
		return jSplitPaneExpanded;
	}
	
	
}
