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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.project.Project;
import de.enflexit.common.classSelection.ClassElement2Display;
import de.enflexit.common.classSelection.JListClassSearcher;
import de.enflexit.common.ontology.AgentStartArgument;
import de.enflexit.common.ontology.OntologyClassTreeObject;
import de.enflexit.common.ontology.gui.OntologyInstanceDialog;
import jade.core.Agent;

/**
 * Represents the JPanel/Tab 'Configuration' - 'Agents'
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BaseAgents extends JSplitPane implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private Project currProject;
	private OntologyClassTreeObject currOntoObject;
	
	private String agentReference;
	private String ontoReference;
	
	private JLabel jLabelAgent;
	private JTextField jTextAgent;
	private JLabel jLabelStart;
	private JTextField jTextAgentStartAs;
	private JButton jButtonStartAgent;
	private JListClassSearcher jAgentList;
	private JPanel jPanelReferences;
	private JScrollPane jScrollReferences;
	private JList<AgentStartArgument> jListReferences;
	private JButton jButtonMoveUp;
	private JButton jButtonMoveDown;
	private JButton jButtonRemoveAll;
	private JButton jButtonReferencesAdd;
	private JButton jButtonReferencesRemove;
	private JSplitPane jSplitEast;
	private JScrollPane jScrollOntology;
	private JTree jTreeOntology;
	private JPanel jPanelOntology;
	private JLabel jLabelRecerence;
	private JLabel jLabelOntologie;
	private JSplitPane jSplitOntologie;
	private JPanel jPanelOntoSlots;
	private JPanel jPanelWest;
	private JPanel jPanelEast;
	private JPanel jPanelEastTop;
	private JButton jButtonRename;
	
	/**
	 * This is the default constructor
	 */
	public BaseAgents(Project project) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);	
		this.initialize();	
		
		// --- Translate text modules ---------------------
		jLabelAgent.setText(Language.translate("Agent:"));
		jLabelStart.setText(Language.translate("Starten als:"));
		jLabelRecerence.setText(Language.translate("Start-Objekte aus Projekt-Ontologie"));
		jLabelOntologie.setText(Language.translate("Projekt-Ontologie"));
		
		jButtonStartAgent.setToolTipText(Language.translate("Agent starten..."));
		jButtonMoveUp.setToolTipText(Language.translate("Objekt nach oben"));
		jButtonMoveDown.setToolTipText(Language.translate("Objekt nach unten"));
		jButtonRename.setToolTipText(Language.translate("Ontologie Referenz benennen"));
		jButtonRemoveAll.setToolTipText(Language.translate("Alle Objekte löschen"));
		jButtonReferencesAdd.setToolTipText(Language.translate("Objekt hinzufügen"));
		jButtonReferencesRemove.setToolTipText(Language.translate("Objekt entfernen"));
		
		this.OntoTreeExpand2Level(3, true);
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		jLabelAgent = new JLabel();
		jLabelAgent.setText("Agent:");
		jLabelAgent.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelStart = new JLabel();
		jLabelStart.setText("Starten als:");
		jLabelStart.setFont(new Font("Dialog", Font.BOLD, 12));

		
		this.setOneTouchExpandable(false);
		this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		this.setDividerSize(3);
		this.setResizeWeight(0.5);
		this.setSize(new Dimension(1003, 568));
		this.setLeftComponent(this.getJPanelWest());
		this.setRightComponent(this.getJPanelEast());
	}
	/**
	 * This method initializes jPanelEast	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelEast() {
		if (jPanelEast == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.weightx = 1.0;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = GridBagConstraints.BOTH;
			gridBagConstraints16.gridy = 1;
			gridBagConstraints16.weightx = 0.0;
			gridBagConstraints16.weighty = 0.1;
			gridBagConstraints16.gridwidth = 1;
			gridBagConstraints16.gridx = 0;
			jPanelEast = new JPanel();
			jPanelEast.setLayout(new GridBagLayout());
			jPanelEast.add(getJSplitEast(), gridBagConstraints16);
			jPanelEast.add(getJPanelEastTop(), gridBagConstraints5);
		}
		return jPanelEast;
	}
	/**
	 * This method initializes jPanelWest	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelWest() {
		if (jPanelWest == null) {
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.fill = GridBagConstraints.BOTH;
			gridBagConstraints18.gridy = 1;
			gridBagConstraints18.weightx = 1.0;
			gridBagConstraints18.weighty = 1.0;
			gridBagConstraints18.gridwidth = 2;
			gridBagConstraints18.insets = new Insets(0, 10, 0, 5);
			gridBagConstraints18.ipady = 0;
			gridBagConstraints18.gridx = 0;
			
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 0.0;
			gridBagConstraints.insets = new Insets(10, 0, 10, 5);
			
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.insets = new Insets(10, 10, 10, 5);
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.gridx = 0;
			
			jPanelWest = new JPanel();
			jPanelWest.setLayout(new GridBagLayout());
			jPanelWest.add(getJAgentList(), gridBagConstraints18);
			jPanelWest.add(jLabelAgent, gridBagConstraints9);
			jPanelWest.add(getJTextAgent(), gridBagConstraints);
		}
		return jPanelWest;
	}
	/**
	 * This method initializes jPanelEastTop	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelEastTop() {
		if (jPanelEastTop == null) {
			
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.NONE;
			gridBagConstraints3.gridx = -1;
			gridBagConstraints3.gridy = -1;
			gridBagConstraints3.insets = new Insets(0, 0, 0, 10);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.gridy = -1;
			gridBagConstraints2.weightx = 0.1;
			gridBagConstraints2.insets = new Insets(10, 0, 10, 5);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(10, 5, 10, 5);
			gridBagConstraints1.gridy = -1;
			gridBagConstraints1.gridx = -1;
			jPanelEastTop = new JPanel();
			jPanelEastTop.setLayout(new GridBagLayout());
			jPanelEastTop.add(jLabelStart, gridBagConstraints1);
			jPanelEastTop.add(getJTextAgentStartAs(), gridBagConstraints2);
			jPanelEastTop.add(getJButtonStartAgent(), gridBagConstraints3);
		}
		return jPanelEastTop;
	}
	/**
	 * This method initializes jSplitEast	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitEast() {
		if (jSplitEast == null) {
			jSplitEast = new JSplitPane();
			jSplitEast.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitEast.setDividerSize(1);
			jSplitEast.setResizeWeight(0.0D);
			jSplitEast.setDividerLocation(180);
			jSplitEast.setBottomComponent(getJSplitOntologie());
			jSplitEast.setTopComponent(getJPanelReferences());
		}
		return jSplitEast;
	}
	/**
	 * This method initializes jPanelOntology	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelOntology() {
		if (jPanelOntology == null) {
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints14.gridy = 0;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.BOTH;
			gridBagConstraints13.weighty = 1.0;
			gridBagConstraints13.insets = new Insets(0, 5, 0, 10);
			gridBagConstraints13.gridy = 1;
			gridBagConstraints13.weightx = 1.0;
			
			jLabelOntologie = new JLabel();
			jLabelOntologie.setText("Projekt-Ontologie");
			jLabelOntologie.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelOntology = new JPanel();
			jPanelOntology.setLayout(new GridBagLayout());
			jPanelOntology.add(getJScrollOntology(), gridBagConstraints13);
			jPanelOntology.add(jLabelOntologie, gridBagConstraints14);
		}
		return jPanelOntology;
	}
	/**
	 * This method initializes jSplitOntologie	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitOntologie() {
		if (jSplitOntologie == null) {
			jSplitOntologie = new JSplitPane();
			jSplitOntologie.setDividerSize(3);
			jSplitOntologie.setDividerLocation(300);
			jSplitOntologie.setResizeWeight(0.5D);
			jSplitOntologie.setBottomComponent(getJPanelOntoSlots());
			jSplitOntologie.setTopComponent(getJPanelOntology());
		}
		return jSplitOntologie;
	}

	/**
	 * This method initializes jPanelOntoSlots	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelOntoSlots() {
		if (jPanelOntoSlots == null) {
			jPanelOntoSlots = new JPanel();
			jPanelOntoSlots.setLayout(new GridBagLayout());
		}
		return jPanelOntoSlots;
	}
	/**
	 * This method initializes jTextAgent	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextAgent() {
		if (jTextAgent == null) {
			jTextAgent = new JTextField();
			jTextAgent.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextAgent.setEditable(false);
			jTextAgent.setPreferredSize(new Dimension(200, 26));
		}
		return jTextAgent;
	}
	/**
	 * This method initializes jTextAgentStartAs	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextAgentStartAs() {
		if (jTextAgentStartAs == null) {
			jTextAgentStartAs = new JTextField();
			jTextAgentStartAs.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextAgentStartAs.setPreferredSize(new Dimension(100, 26));
			jTextAgentStartAs.setEditable(true);
		}
		return jTextAgentStartAs;
	}
	/**
	 * This method initializes jButtonStartAgent	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonStartAgent() {
		if (jButtonStartAgent == null) {
			jButtonStartAgent = new JButton();
			jButtonStartAgent.setText("OK");
			jButtonStartAgent.setToolTipText("Agent starten...");
			jButtonStartAgent.setPreferredSize(new Dimension(50, 26));			
			jButtonStartAgent.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonStartAgent.setActionCommand("AgentStart");
			jButtonStartAgent.addActionListener(this);
		}
		return jButtonStartAgent;
	}
	
	/**
	 * This method initializes jPanelReferences	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelReferences() {
		if (jPanelReferences == null) {
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 3;
			gridBagConstraints17.anchor = GridBagConstraints.SOUTH;
			gridBagConstraints17.insets = new Insets(0, 0, 5, 10);
			gridBagConstraints17.gridy = 3;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.anchor = GridBagConstraints.WEST;
			gridBagConstraints15.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints15.gridwidth = 2;
			gridBagConstraints15.gridy = 0;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.gridy = 4;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.anchor = GridBagConstraints.EAST;
			gridBagConstraints11.insets = new Insets(5, 0, 5, 5);
			gridBagConstraints11.ipadx = 0;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.gridy = 4;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 2;
			gridBagConstraints10.insets = new Insets(5, 0, 5, 5);
			gridBagConstraints10.anchor = GridBagConstraints.SOUTH;
			gridBagConstraints10.gridy = 4;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 3;
			gridBagConstraints8.insets = new Insets(0, 0, 5, 10);
			gridBagConstraints8.gridy = 2;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 3;
			gridBagConstraints7.insets = new Insets(0, 0, 5, 10);
			gridBagConstraints7.gridy = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints6.gridheight = 3;
			gridBagConstraints6.gridwidth = 3;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.weightx = 1.0;

			jLabelRecerence = new JLabel();
			jLabelRecerence.setText("Start-Objekte aus Projekt-Ontologie");
			jLabelRecerence.setFont(new Font("Dialog", Font.BOLD, 12));

			jPanelReferences = new JPanel();
			jPanelReferences.setLayout(new GridBagLayout());
			jPanelReferences.add(getJScrollReferences(), gridBagConstraints6);
			jPanelReferences.add(getJButtonMoveUp(), gridBagConstraints7);
			jPanelReferences.add(getJButtonMoveDown(), gridBagConstraints8);
			jPanelReferences.add(getJButtonRename(), gridBagConstraints17);
			jPanelReferences.add(getJButtonRemoveAll(), gridBagConstraints10);
			jPanelReferences.add(getJButtonReferencesAdd(), gridBagConstraints11);
			jPanelReferences.add(getJButtonReferencesRemove(), gridBagConstraints12);
			jPanelReferences.add(jLabelRecerence, gridBagConstraints15);
		}
		return jPanelReferences;
	}

	/**
	 * This method initializes jScrollReferences	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollReferences() {
		if (jScrollReferences == null) {
			jScrollReferences = new JScrollPane();
			jScrollReferences.setViewportView(getJListReferences());
		}
		return jScrollReferences;
	}
	/**
	 * This method initializes jListReferences	
	 * @return javax.swing.JList	
	 */
	private JList<AgentStartArgument> getJListReferences() {
		if (jListReferences == null) {
			jListReferences = new JList<AgentStartArgument>();
			jListReferences.addMouseListener( new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if (me.getClickCount() == 2 ) {
						jButtonRename.doClick();	
					}
				}
			});
		}
		return jListReferences;
	}

	/**
	 * This method initializes jButtonMoveUp	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonMoveUp() {
		if (jButtonMoveUp == null) {
			jButtonMoveUp = new JButton();
			jButtonMoveUp.setIcon(GlobalInfo.getInternalImageIcon("ArrowUp.png"));
			jButtonMoveUp.setPreferredSize(new Dimension(50, 26));
			jButtonMoveUp.setToolTipText("Objekt nach oben");
			jButtonMoveUp.setActionCommand("OntoObjectUp");
			jButtonMoveUp.addActionListener(this);
		}
		return jButtonMoveUp;
	}

	/**
	 * This method initializes jButtonMoveDown	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonMoveDown() {
		if (jButtonMoveDown == null) {
			jButtonMoveDown = new JButton();
			jButtonMoveDown.setIcon(GlobalInfo.getInternalImageIcon("ArrowDown.png"));
			jButtonMoveDown.setPreferredSize(new Dimension(50, 26));
			jButtonMoveDown.setToolTipText("Objekt nach unten");
			jButtonMoveDown.setActionCommand("OntoObjectDown");
			jButtonMoveDown.addActionListener(this);
		}
		return jButtonMoveDown;
	}
	
	/**
	 * This method initialises jButtonRename	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRename() {
		if (jButtonRename == null) {
			jButtonRename = new JButton();
			jButtonRename.setIcon(GlobalInfo.getInternalImageIcon("Rename.gif"));
			jButtonRename.setPreferredSize(new Dimension(50, 26));
			jButtonRename.setToolTipText("Ontologie Referenz benennen");
			jButtonRename.setActionCommand("OntoRename");
			jButtonRename.addActionListener(this);
		}
		return jButtonRename;
	}
	
	/**
	 * This method initialises jButtonRemoveAll	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRemoveAll() {
		if (jButtonRemoveAll == null) {
			jButtonRemoveAll = new JButton();
			jButtonRemoveAll.setIcon(GlobalInfo.getInternalImageIcon("Delete.png"));
			jButtonRemoveAll.setPreferredSize(new Dimension(50, 26));
			jButtonRemoveAll.setToolTipText("Alle Objekte löschen");
			jButtonRemoveAll.setActionCommand("OntoObjectsRemoveAll");
			jButtonRemoveAll.addActionListener(this);
		}
		return jButtonRemoveAll;
	}

	/**
	 * This method initializes jButtonReferencesAdd	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonReferencesAdd() {
		if (jButtonReferencesAdd == null) {
			jButtonReferencesAdd = new JButton();
			jButtonReferencesAdd.setIcon(GlobalInfo.getInternalImageIcon("ArrowUp.png"));
			jButtonReferencesAdd.setSize(15, 15);
			jButtonReferencesAdd.setToolTipText("Objekt hinzufügen");
			jButtonReferencesAdd.setActionCommand("OntoObjectAdd");
			jButtonReferencesAdd.addActionListener(this);
		}
		return jButtonReferencesAdd;
	}

	/**
	 * This method initializes jButtonReferencesRemove	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonReferencesRemove() {
		if (jButtonReferencesRemove == null) {
			jButtonReferencesRemove = new JButton();
			jButtonReferencesRemove.setIcon(GlobalInfo.getInternalImageIcon("ArrowDown.png"));
			jButtonReferencesRemove.setSize(15, 15);
			jButtonReferencesRemove.setToolTipText("Objekt entfernen");
			jButtonReferencesRemove.setActionCommand("OntoObjectRemsove");
			jButtonReferencesRemove.addActionListener(this);
		}
		return jButtonReferencesRemove;
	}
	/**
	 * This method initializes jScrollOntology	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollOntology() {
		if (jScrollOntology == null) {
			jScrollOntology = new JScrollPane();
			jScrollOntology.setViewportView(getJTreeOntology());
		}
		return jScrollOntology;
	}

	/**
	 * This method initializes jTreeOntology	
	 * @return javax.swing.JTree	
	 */
	private JTree getJTreeOntology() {
		if (jTreeOntology == null) {
			jTreeOntology = new JTree(this.currProject.getOntologyVisualisationHelper().getOntologyTree());
			jTreeOntology.setName("OntoTree");
			jTreeOntology.setShowsRootHandles(false);
			jTreeOntology.setRootVisible(true);
			jTreeOntology.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			jTreeOntology.addTreeSelectionListener(new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent ts) {
					
					// ----------------------------------------------------------
					// --- Tree-Selection abfangen --- S T A R T ----------------
					// ----------------------------------------------------------
					// --- Node auslesen und Slots anzeigen ---------------------
					DefaultMutableTreeNode currNode = (DefaultMutableTreeNode)ts.getPath().getLastPathComponent();
					currOntoObject = (OntologyClassTreeObject) currNode.getUserObject();
					JPanel NewSlotView = new OntologyTabClassView(currNode); 
					int DivLoc = jSplitOntologie.getDividerLocation();
					jSplitOntologie.setBottomComponent( NewSlotView );
					jSplitOntologie.setDividerLocation(DivLoc);
					jPanelOntoSlots = NewSlotView; 					
					// ----------------------------------------------------------
					// --- Tree-Selection abfangen --- S T O P ------------------
					// ----------------------------------------------------------
				}// End - valueChanged
			});
			jTreeOntology.setSelectionRow(0);
		}
		return jTreeOntology;
	}
	
	/**
	 * If expand is true, expands all nodes in the tree, otherwise, collapses all nodes in the tree.
	 * @param Up2TreeLevel the up2 tree level
	 * @param expand the expand
	 */
	public void OntoTreeExpand2Level(Integer Up2TreeLevel, boolean expand ) {
    	Integer CurrNodeLevel = 1;
    	if ( Up2TreeLevel == null ) 
    		Up2TreeLevel = 1000;
    	OntoTreeExpand( new TreePath( currProject.getOntologyVisualisationHelper().getOntologyTree().getRoot() ), expand, CurrNodeLevel, Up2TreeLevel);
    }
    /**
     * 
     * @param parent
     * @param expand
     * @param CurrNodeLevel
     * @param Up2TreeLevel
     */
	private void OntoTreeExpand(TreePath parent, boolean expand, Integer CurrNodeLevel, Integer Up2TreeLevel) {
    
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (CurrNodeLevel >= Up2TreeLevel) {
        	return;
        }
        if (node.getChildCount() >= 0) {
            for ( Enumeration<?> e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                OntoTreeExpand(path, expand, CurrNodeLevel+1, Up2TreeLevel);
            }
        }    
        // Expansion or collapse must be done bottom-up
        if (expand) {
        	jTreeOntology.expandPath(parent);
        } else {
        	jTreeOntology.collapsePath(parent);
        }
    }
    
	/**
	 * This method initializes jAgentList	
	 * @return javax.swing.JListClassSearcher
	 */
	private JListClassSearcher getJAgentList() {
		if (jAgentList == null) {
			jAgentList = new JListClassSearcher(Agent.class, this.currProject.getBundleNames());
			jAgentList.setToolTipText(Language.translate("Agenten in diesem Projekt"));
			jAgentList.setPreferredSize(new Dimension(333, 300));
			jAgentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jAgentList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent se) {

					if (se.getValueIsAdjusting()==true && jAgentList.getSelectedValue()!=null) {
						
						updateView4AgentConfig();
						
						// -----------------------------------------------------
						// --- Set entry to the current agent ------------------
						int maxLenght = 80;
						if (agentReference.length() > maxLenght) {
							String toDisplayStart = agentReference.substring(0, 4);
							String toDisplayEnd   = agentReference.substring(agentReference.length() - maxLenght );
							agentReference = toDisplayStart + "..." + toDisplayEnd;
						}
						getJTextAgent().setText(agentReference);
						
						
						// -----------------------------------------------------
						// --- Propose name for the agent ----------------------
						String startAs = jAgentList.getSelectedValue().toString();
						startAs = startAs.substring(startAs.lastIndexOf(".")+1);
						// --- Filter capital letters --------------------------
						String regExp = "[A-Z]";	
						String startAsNew = ""; 
						for (int i = 0; i < startAs.length(); i++) {
							String sngChar = "" + startAs.charAt(i);
							if (sngChar.matches(regExp)==true) {
								startAsNew = startAsNew + sngChar;	
								// --- Take the second character --------------
								if ( i < startAs.length()-1 ) {
									String SngCharN = "" + startAs.charAt(i+1);
									if ( SngCharN.matches( regExp ) == false ) {
										startAsNew = startAsNew + SngCharN;	
									}
								}	
								// ---------------------------------------------
							}						
					    }
						if ( startAsNew != "" && startAsNew.length() >= 4 ) {
							startAs = startAsNew;
						}
						// --- Check, if this agent is already running ---------
						int i = 1;
						startAsNew = startAs;
						while ( Application.getJadePlatform().isAgentRunning( startAs, currProject.getProjectFolder() ) == true ){
							startAs = startAsNew + i;
							i++; 
						}
						// --- Set proposal name -------------------------------
						getJTextAgentStartAs().setText(startAs);
					}
					// ----------------------------------------------------
					// --- Fertig -----------------------------------------
					// ----------------------------------------------------
				}
			});
		}
		return jAgentList;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		String ActCMD = ae.getActionCommand();
		Object trigger = ae.getSource();

		if (trigger==jButtonStartAgent) {
			// ------------------------------------------------------
			// --- Start the selected agent -------------------------
			// ------------------------------------------------------
			ClassElement2Display selection = (ClassElement2Display)jAgentList.getSelectedValue();
			if (selection==null) {
				String head = Language.translate("Agent auswählen!");
				String msg  = Language.translate("Bitte wählen Sie den Agenten aus, den Sie starten wollen.");
				JOptionPane.showMessageDialog(Application.getMainWindow(), msg, head, JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			String agentClassName = selection.getClassElement();
			String agentClassNameSimple = selection.getClassElementSimpleName();
			String agentName = jTextAgentStartAs.getText();
			if (agentName.length()!=0 && agentClassName!=null) {
				Object [] startArgs = null;
				Vector<AgentStartArgument> startArgsConfigured = this.currProject.getAgentStartConfiguration().get(agentClassName);
				if (startArgsConfigured!=null) {
					// --- If start-arguments are set, get them now -----
					OntologyInstanceDialog oid = new OntologyInstanceDialog(Application.getMainWindow(), this.currProject.getOntologyVisualisationHelper(), this.currProject.getAgentStartConfiguration(), agentClassName);
					oid.setTitle(Language.translate("Startargument definieren für Agent") + " '" + agentName + "' (" + agentClassNameSimple + ")");
					oid.setVisible(true);
					// --- Wait ---
					if (oid.isCancelled()) return;
					startArgs = oid.getObjectConfiguration();
					oid.dispose();
					oid = null;	
				}
				
				// --- Start the Agent now --------------------------
				Application.getJadePlatform().startAgent(agentName, agentClassName, startArgs, currProject.getProjectFolder());	
				jTextAgent.setText(null);
				jTextAgentStartAs.setText(null);
				jAgentList.clearSelection();
			}
			
		} else if (trigger == jButtonReferencesAdd ) {
			// --- Add start argument ---------------------
			this.readSelectionFromForm();
			if (agentReference==null || ontoReference==null) {
				String head = Language.translate("Agent und Ontologie-Referenz auswählen!");
				String msg  = Language.translate("Bitte wählen Sie einen Agenten und eine Ontologie-Referenz aus den entsprechenden Listen.");
				JOptionPane.showMessageDialog(Application.getMainWindow(), msg, head, JOptionPane.WARNING_MESSAGE);
				return;
			}
			int newPos = this.currProject.getAgentStartConfiguration().addReference(agentReference, ontoReference);
			this.currProject.setAgentStartConfigurationUpdated();
			if (newPos!=-1) {
				this.getJListReferences().setSelectedIndex(newPos);
			}
			
		} else if (trigger == jButtonReferencesRemove ) {
			// --- Remove start argument ------------------
			this.readSelectionFromForm();
			int newPos = this.currProject.getAgentStartConfiguration().removeReference(this.agentReference, this.getJListReferences().getSelectedIndex());
			this.currProject.setAgentStartConfigurationUpdated();		
			if (newPos!=-1) {
				this.getJListReferences().setSelectedIndex(newPos);
			}
		
		} else if (trigger == jButtonRemoveAll) {
			// --- Remove start arguments completely ------
			this.readSelectionFromForm();
			this.currProject.getAgentStartConfiguration().removeAllReferences(agentReference);
			this.currProject.setAgentStartConfigurationUpdated();
					
		} else if (trigger == jButtonMoveUp ) {
			// --- Move start argument up -----------------
			this.readSelectionFromForm();
			int newPos = this.currProject.getAgentStartConfiguration().movePosition(agentReference, jListReferences.getSelectedIndex(), -1); 
			this.currProject.setAgentStartConfigurationUpdated();
			if (newPos!=-1){
				this.jListReferences.setSelectedIndex(newPos);
			}
			
		} else if (trigger == jButtonMoveDown ) {
			// --- Move start argument down ---------------
			this.readSelectionFromForm();
			int newPos = this.currProject.getAgentStartConfiguration().movePosition(agentReference, jListReferences.getSelectedIndex(), 1); 
			this.currProject.setAgentStartConfigurationUpdated();
			if (newPos!=-1){
				this.jListReferences.setSelectedIndex(newPos);
			}
			
		} else if (trigger == jButtonRename) {
			// --- mask ontology-reference ----------------
			String input = null;
			String head  = null;
			String msg   = null;
			int selectedIndex = this.getJListReferences().getSelectedIndex(); 
			
			if (selectedIndex==-1) {
				head = Language.translate("Ontologie-Referenz auswählen!");
				msg  = Language.translate("Bitte wählen Sie eine der zugeordneten Ontologie-Referenzen aus der Liste.");
				JOptionPane.showMessageDialog(Application.getMainWindow(), msg, head, JOptionPane.WARNING_MESSAGE);
				return;
			}
			this.readSelectionFromForm();
			
			input = null;
			head = Language.translate("Ontologie Referenz maskieren");
			msg  = Language.translate("Bitte geben Sie einen Bezeichner für die Referenz an!");
			
			AgentStartArgument ageStartArg = this.currProject.getAgentStartConfiguration().get(this.agentReference).get(selectedIndex);
			String displayTitle = ageStartArg.getDisplayTitle();
			if (displayTitle==null) {
				input = (String) JOptionPane.showInputDialog(Application.getMainWindow(), msg, head, JOptionPane.QUESTION_MESSAGE);	
			} else {
				input = (String) JOptionPane.showInputDialog(Application.getMainWindow(), msg, head, JOptionPane.QUESTION_MESSAGE, null, null, displayTitle);	
			}
			if (input==null) {
				return;
			} else {
				input = input.trim();
				if (input.equals("")) input = null;
			}
			ageStartArg.setDisplayTitle(input);
			
			this.currProject.setAgentStartConfigurationUpdated();
			
		} else {
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + ActCMD);	
		};
		this.repaint();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object notifyObject) {
		
		String ObjectName = notifyObject.toString();
		if ( ObjectName.equalsIgnoreCase(Project.CHANGED_StartArguments4BaseAgent) ) {
			this.updateView4AgentConfig();
			
		} else if ( ObjectName.equalsIgnoreCase( Project.CHANGED_ProjectOntology ) ) {
			this.jTreeOntology.setModel( currProject.getOntologyVisualisationHelper().getOntologyTree() );
			this.OntoTreeExpand2Level(3, true);
		
		} else {
			//System.out.println("Unbekannte Meldung vom Observer: " + ObjectName);
		}
	}

	/**
	 * Updates the List of the AgetnReferences
	 */
	private void updateView4AgentConfig() {
		
		this.readSelectionFromForm();
		if (this.agentReference==null) {
			this.getJListReferences().setListData(new Vector<AgentStartArgument>());
		} else {
			Vector<AgentStartArgument> arguments = this.currProject.getAgentStartConfiguration().get(this.agentReference);
			if (arguments!=null) {
				this.getJListReferences().setListData(arguments);		
			} else {
				this.getJListReferences().setListData(new Vector<AgentStartArgument>());
			}
		}
		
	}
	
	/**
	 * Read selection from form.
	 */
	private void readSelectionFromForm() {
		// -- configure Var. agentReference -------------------------
		if (jAgentList.getSelectedValue()==null) {
			this.agentReference = null;
		} else {
			this.agentReference = this.jAgentList.getSelectedValue().toString();
		}
		// -- configure Var. ontoReference --------------------------		
		if (this.currOntoObject==null) {
			this.ontoReference = null;
		} else {
			if (this.currOntoObject.isClass()) {
				this.ontoReference = this.currOntoObject.getOntologySubClass().getName();	
			} else {
				this.ontoReference = null;	
			}
		}
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
