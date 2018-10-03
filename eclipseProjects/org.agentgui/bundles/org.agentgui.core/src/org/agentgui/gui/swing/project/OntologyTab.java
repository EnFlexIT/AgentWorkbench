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
package org.agentgui.gui.swing.project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.gui.OntologieSelector;
import agentgui.core.project.Project;
import de.enflexit.common.ontology.OntologyClass;
import de.enflexit.common.ontology.OntologyClassTreeObject;

/**
 * Represents the JPanel/Tab 'Configuration' - 'Ontologies'
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologyTab extends JPanel implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private Project currProject;
	private JSplitPane ontoSplitPane = null;
	private JScrollPane scrollPaneTree = null;

	private JTree ontoTree = null;
	private JPanel ontoMain = null;
	private JPanel jPanelLeft = null;
	private JPanel jPanelLeftNavi = null;
	private JButton jButtonAddOntology = null;
	private JButton jButtonRemoveOntology = null;

	
	/**
	 * This is the default constructor
	 */
	public OntologyTab(Project project) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);		
		
		// --- Form aufbauen -------------------------------------
		this.initialize();	
		
		// --- Basis-Verzeichnisse im OntoTree anzeigen -------
		OntoTreeExpand2Level(4, true);
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(50);
		borderLayout.setVgap(50);
		
		this.setLayout(borderLayout);
		this.setSize(850, 500);
		this.setAutoscrolls(true);		
		this.setBorder(null);
		this.setFocusable(true);
		this.setVisible(true);
		this.add(getOntoSplitPane(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes OntoSplitPane	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getOntoSplitPane() {
		if (ontoSplitPane == null) {
			ontoSplitPane = new JSplitPane();
			ontoSplitPane.setDividerLocation(250);
			ontoSplitPane.setLeftComponent(getJPanelLeft());
			ontoSplitPane.setRightComponent(getOntoMain());
		}
		return ontoSplitPane;
	}

	/**
	 * This method initializes TreeScrollPane	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTreeScrollPane() {
		if (scrollPaneTree == null) {
			scrollPaneTree = new JScrollPane();
			scrollPaneTree.setViewportView(getOntoTree());
		}
		return scrollPaneTree;
	}

	/**
	 * This method initializes OntoTree	
	 * @return javax.swing.JTree	
	 */
	private JTree getOntoTree() {
		if (ontoTree == null) {
			ontoTree = new JTree(this.currProject.getOntologyVisualisationHelper().getOntologyTree());
			ontoTree.setName("OntoTree");
			ontoTree.setShowsRootHandles(false);
			ontoTree.setRootVisible(true);
			ontoTree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
			ontoTree.addTreeSelectionListener( new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent ts) {
					// ----------------------------------------------------------
					// --- Tree-Selection abfangen --- S T A R T ----------------
					// ----------------------------------------------------------
					// --- Node auslesen und Slots anzeigen ---------------------
					DefaultMutableTreeNode currNode = (DefaultMutableTreeNode)ts.getPath().getLastPathComponent();
					JPanel NewSlotView = new OntologyTabClassView( currNode ); 
					int DivLoc = ontoSplitPane.getDividerLocation();
					ontoSplitPane.setRightComponent( NewSlotView );
					ontoSplitPane.setDividerLocation(DivLoc);
					ontoMain = NewSlotView; 
					// ----------------------------------------------------------
					// --- Tree-Selection abfangen --- S T O P ------------------
					// ----------------------------------------------------------
				}// End - valueChanged
			});

		}
		return ontoTree;
	}
	
	// If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
    public void OntoTreeExpand2Level(Integer Up2TreeLevel, boolean expand ) {
    	
    	Integer CurrNodeLevel = 1;
    	if ( Up2TreeLevel == null ) 
    		Up2TreeLevel = 1000;
    	OntoTreeExpand( new TreePath( currProject.getOntologyVisualisationHelper().getOntologyTree().getRoot() ), expand, CurrNodeLevel, Up2TreeLevel);
    }
    
	private void OntoTreeExpand( TreePath parent, boolean expand, Integer CurrNodeLevel, Integer Up2TreeLevel) {
    
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (CurrNodeLevel >= Up2TreeLevel) {
        	return;
        }
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e=node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                OntoTreeExpand(path, expand, CurrNodeLevel+1, Up2TreeLevel);
            }
        }    
        // Expansion or collapse must be done bottom-up
        if (expand) {
            ontoTree.expandPath(parent);
        } else {
        	ontoTree.collapsePath(parent);
        }
    }
	

	/**
	 * This method initializes OntoMain	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getOntoMain() {
		if (ontoMain == null) {
			ontoMain = new JPanel();			
		}
		return ontoMain;
	}

	/**
	 * This method initializes jPanelLeft	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelLeft() {
		if (jPanelLeft == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.weighty = 0.0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints.weightx = 1.0;
			jPanelLeft = new JPanel();
			jPanelLeft.setLayout(new GridBagLayout());
			jPanelLeft.add(getJPanelLeftNavi(), gridBagConstraints1);
			jPanelLeft.add(getTreeScrollPane(), gridBagConstraints);
		}
		return jPanelLeft;
	}

	/**
	 * This method initializes jPanelLeftNavi	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelLeftNavi() {
		if (jPanelLeftNavi == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(10, 10, 10, 0);
			gridBagConstraints2.gridy = 0;
			jPanelLeftNavi = new JPanel();
			jPanelLeftNavi.setLayout(new GridBagLayout());
			jPanelLeftNavi.setPreferredSize(new Dimension(20, 46));
			jPanelLeftNavi.add(getJButtonAddOntology(), gridBagConstraints2);
			jPanelLeftNavi.add(getJButtonRemoveOntology(), gridBagConstraints3);
		}
		return jPanelLeftNavi;
	}

	/**
	 * This method initialises jButtonAddOntology	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAddOntology() {
		if (jButtonAddOntology == null) {
			jButtonAddOntology = new JButton();
			jButtonAddOntology.setPreferredSize(new Dimension(45, 26));
			jButtonAddOntology.setActionCommand("OntologieAdd");
			jButtonAddOntology.setIcon(GlobalInfo.getInternalImageIcon("ListPlus.png"));
			jButtonAddOntology.setToolTipText("Add Ontologie...");
			jButtonAddOntology.addActionListener(this);
		}
		return jButtonAddOntology;
	}

	/**
	 * This method initialises jButtonRemoveOntology	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRemoveOntology() {
		if (jButtonRemoveOntology == null) {
			jButtonRemoveOntology = new JButton();
			jButtonRemoveOntology.setPreferredSize(new Dimension(45, 26));
			jButtonRemoveOntology.setActionCommand("OntologieRemove");
			jButtonRemoveOntology.setIcon(GlobalInfo.getInternalImageIcon("ListMinus.png"));
			jButtonRemoveOntology.setToolTipText("Remove Ontology");
			jButtonRemoveOntology.addActionListener(this);
		}
		return jButtonRemoveOntology;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String actCMD = ae.getActionCommand();
		String msgHead = null;
		String msgText = null;

		if (actCMD.equals("OntologieAdd")) {
			// --- Add Ontology -------------------------------------
			String ActionTitel = Language.translate("Ontologie hinzufügen"); 
			OntologieSelector onSel = new OntologieSelector(Application.getMainWindow(), currProject.getProjectName() + ": " + ActionTitel, true);			
			onSel.setVisible(true);
			// === Wait for user ===
			if (onSel.isCanceled()==true) {
				Application.setStatusBarMessageReady();
				return;
			}
			String newOntologie = onSel.getNewOntologySelected();
			onSel.dispose();
			onSel = null;
			// --- Add ontology ------------------------------------- 
			currProject.subOntologyAdd(newOntologie);
			
		} else if (actCMD.equals("OntologieRemove")) {
			// --- Remove Ontology ----------------------------------
			if (ontoTree.isSelectionEmpty()) {
				msgHead = Language.translate("Fehlende Auswahl !");
				msgText = Language.translate("Zum Löschen, wählen Sie bitte eine der dargestellten Ontologien aus!");			
				JOptionPane.showMessageDialog( Application.getMainWindow().getContentPane(), msgText, msgHead, JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			DefaultMutableTreeNode currNode = (DefaultMutableTreeNode) ontoTree.getSelectionPath().getLastPathComponent();
			OntologyClassTreeObject octo = (OntologyClassTreeObject) currNode.getUserObject();
			OntologyClass oc = octo.getOntologyClass();
			if (oc==null) {
				return;
			}
			currProject.subOntologyRemove(oc.getOntologyMainClass());
		
		} else {
			System.out.println("Unknown ActionCommand: " + actCMD);
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object OName) {
		
		String ObjectName = OName.toString();
		if ( ObjectName.equalsIgnoreCase( Project.CHANGED_ProjectOntology ) ) {
			// --- Ansicht auf die projekt-Ontologie aktualisieren --
			this.ontoTree.setModel( currProject.getOntologyVisualisationHelper().getOntologyTree() );
			this.OntoTreeExpand2Level(4, true);
		} else {
			//System.out.println("Unbekannte Meldung vom Observer: " + ObjectName);
		}
		
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
