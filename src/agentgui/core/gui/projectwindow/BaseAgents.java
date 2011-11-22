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
package agentgui.core.gui.projectwindow;

import jade.core.Agent;

import java.awt.BorderLayout;
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

import javax.swing.ImageIcon;
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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import agentgui.core.agents.AgentClassElement;
import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.components.JListClassSearcher;
import agentgui.core.jade.ClassSearcher;
import agentgui.core.ontologies.OntologyClassTreeObject;
import agentgui.core.ontologies.gui.OntologyInstanceDialog;
import agentgui.core.project.Project;

/**
 * Represents the JPanel/Tab 'Configuration' - 'Agents'
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BaseAgents extends JPanel implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;
	private final static String PathImage = Application.RunInfo.PathImageIntern();
	
	private Project currProject;
	private OntologyClassTreeObject CurrOntoObject = null;
	
	private String agentConfig = null;  //  @jve:decl-index=0:
	private Integer agentConfigPos = null;  //  @jve:decl-index=0:
	private String agentConfigMask = null;  //  @jve:decl-index=0:
	private String agentReference = null;  //  @jve:decl-index=0:
	private String ontoReference = null;  //  @jve:decl-index=0:
	
	private JLabel jLabelAgent = null;
	private JTextField jTextAgent = null;
	private JLabel jLabelStart = null;
	private JTextField jTextAgentStartAs = null;
	private JButton jButtonStartAgent = null;
	private JButton jButtonAgentListRefresh = null;
	private JListClassSearcher jAgentList = null;
	private JPanel jPanelReferences = null;
	private JScrollPane jScrollReferences = null;
	private JList jListReferences = null;
	private JButton jButtonMoveUp = null;
	private JButton jButtonMoveDown = null;
	private JButton jButtonRemoveAll = null;
	private JButton jButtonReferencesAdd = null;
	private JButton jButtonReferencesRemove = null;
	private JSplitPane jSplitEast = null;
	private JScrollPane jScrollOntology = null;
	private JTree jTreeOntology = null;
	private JPanel jPanelOntology = null;
	private JLabel jLabelRecerence = null;
	private JLabel jLabelOntologie = null;
	private JSplitPane jSplitOntologie = null;
	private JPanel jPanelOntoSlots = null;
	private JPanel jPanelWest = null;
	private JPanel jPanelEast = null;
	private JPanel jPanelEastTop = null;
	private JButton jButtonRename = null;
	
	/**
	 * This is the default constructor
	 */
	public BaseAgents( Project CP ) {
		super();
		this.currProject = CP;
		this.currProject.addObserver(this);		
		initialize();	
		
		// --- Übersetzung anschmeissen -------------------
		jLabelAgent.setText(Language.translate("Agent:"));
		jLabelStart.setText(Language.translate("Starten als:"));
		jLabelRecerence.setText(Language.translate("Start-Objekte aus Projekt-Ontologie"));
		jLabelOntologie.setText(Language.translate("Projekt-Ontologie"));
		
		jButtonStartAgent.setToolTipText(Language.translate("Agent starten..."));
		jButtonAgentListRefresh.setToolTipText(Language.translate("Agentenliste aktualisieren"));
		jButtonMoveUp.setToolTipText(Language.translate("Objekt nach oben"));
		jButtonMoveDown.setToolTipText(Language.translate("Objekt nach unten"));
		jButtonRename.setToolTipText(Language.translate("Ontologie Referenz benennen"));
		jButtonRemoveAll.setToolTipText(Language.translate("Alle Objekte löschen"));
		jButtonReferencesAdd.setToolTipText(Language.translate("Objekt hinzufügen"));
		jButtonReferencesRemove.setToolTipText(Language.translate("Objekt entfernen"));
		
		// --- Basis-Verzeichnisse im OntoTree anzeigen -------
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
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(777, 400));
		this.add(getJPanelWest(), BorderLayout.WEST);
		this.add(getJPanelEast(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes jTextAgent	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextAgent() {
		if (jTextAgent == null) {
			jTextAgent = new JTextField();
			jTextAgent.setEditable(false);
			jTextAgent.setPreferredSize(new Dimension(200, 26));
		}
		return jTextAgent;
	}

	/**
	 * This method initializes jTextAgentStartAs	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextAgentStartAs() {
		if (jTextAgentStartAs == null) {
			jTextAgentStartAs = new JTextField();
			jTextAgentStartAs.setPreferredSize(new Dimension(150, 26));
			jTextAgentStartAs.setEditable(true);
		}
		return jTextAgentStartAs;
	}

	/**
	 * This method initializes jButtonStartAgent	
	 * 	
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
	 * This method initializes jButtonAgentListRefresh	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAgentListRefresh() {
		if (jButtonAgentListRefresh == null) {
			jButtonAgentListRefresh = new JButton();
			jButtonAgentListRefresh.setIcon(new ImageIcon(getClass().getResource(PathImage + "Refresh.png")));
			jButtonAgentListRefresh.setPreferredSize(new Dimension(39, 26));
			jButtonAgentListRefresh.setToolTipText("Agentenliste aktualisieren");
			jButtonAgentListRefresh.setActionCommand("AgentsRefresh");
			jButtonAgentListRefresh.addActionListener(this);			
		}
		return jButtonAgentListRefresh;
	}

	/**
	 * This method initializes jAgentList	
	 * @return javax.swing.JListClassSearcher
	 */
	private JListClassSearcher getJAgentList() {
		if (jAgentList == null) {
			jAgentList = new JListClassSearcher(ClassSearcher.CLASSES_AGENTS, currProject);
			jAgentList.setToolTipText(Language.translate("Agenten in diesem Projekt"));
			jAgentList.setPreferredSize(new Dimension(333, 300));
			jAgentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jAgentList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent se) {

					if ( jAgentList.getSelectedValue() != null ) {
						String ToDisplay = jAgentList.getSelectedValue().toString();
						//System.out.println("Agent: " + ToDisplay);
						// -----------------------------------------------------
						// --- Definierte Start-Referenzen anzeigen ------------ 
						jListReferences.setListData( currProject.agentConfig.getListData(ToDisplay) );
						// -----------------------------------------------------
						// --- Eintrag für den aktuellen Agenten vornehmen -----
						int maxLenght = 30;
						if ( ToDisplay.length() > maxLenght ) {
							String ToDisplayStart = ToDisplay.substring(0, 4);
							String ToDisplayEnde = ToDisplay.substring( ToDisplay.length() - maxLenght );
							ToDisplay = ToDisplayStart + "..." + ToDisplayEnde;
						}
						jTextAgent.setText( ToDisplay );
						// -----------------------------------------------------
						// --- Vorschlag für den Ausführungsnamen finden -------
						String StartAs = jAgentList.getSelectedValue().toString();
						StartAs = StartAs.substring(StartAs.lastIndexOf(".")+1);
						// -----------------------------------------------------
						// --- Alle Großbuchstaben filtern ---------------------
						String RegExp = "[A-Z]";	
						String StartAsNew = ""; 
						for (int i = 0; i < StartAs.length(); i++) {
							String SngChar = "" + StartAs.charAt(i);
							if ( SngChar.matches( RegExp ) == true ) {
								StartAsNew = StartAsNew + SngChar;	
								// --- ggf. den zweiten Buchstaben mitnehmen ---
								if ( i < StartAs.length() ) {
									String SngCharN = "" + StartAs.charAt(i+1);
									if ( SngCharN.matches( RegExp ) == false ) {
										StartAsNew = StartAsNew + SngCharN;	
									}
								}	
								// ---------------------------------------------
							}						
					    }
						if ( StartAsNew != "" && StartAsNew.length() >= 4 ) {
							StartAs = StartAsNew;
						}
						// -----------------------------------------------------
						// --- Check, ob es dieser Agent schon läuft -----------
						int i = 1;
						StartAsNew = StartAs;
						while ( Application.JadePlatform.jadeAgentIsRunning( StartAs, currProject.getProjectFolder() ) == true ){
							StartAs = StartAsNew + i;
							i++; 
						}
						// -----------------------------------------------------
						// --- Vorschlagsnamen einstellen ----------------------
						jTextAgentStartAs.setText(StartAs);
					}
					// ----------------------------------------------------
					// --- Fertig -----------------------------------------
					// ----------------------------------------------------
				}
			});
		}
		return jAgentList;
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
			gridBagConstraints17.insets = new Insets(0, 0, 0, 10);
			gridBagConstraints17.gridy = 8;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.anchor = GridBagConstraints.WEST;
			gridBagConstraints15.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints15.gridwidth = 2;
			gridBagConstraints15.gridy = 1;
			jLabelRecerence = new JLabel();
			jLabelRecerence.setText("Start-Objekte aus Projekt-Ontologie");
			jLabelRecerence.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.gridy = 9;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.anchor = GridBagConstraints.EAST;
			gridBagConstraints11.insets = new Insets(5, 0, 0, 5);
			gridBagConstraints11.ipadx = 0;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.gridy = 9;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 2;
			gridBagConstraints10.insets = new Insets(5, 0, 0, 5);
			gridBagConstraints10.anchor = GridBagConstraints.SOUTH;
			gridBagConstraints10.gridy = 9;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 3;
			gridBagConstraints8.insets = new Insets(0, 0, 0, 10);
			gridBagConstraints8.gridy = 4;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 3;
			gridBagConstraints7.insets = new Insets(0, 0, 1, 10);
			gridBagConstraints7.gridy = 2;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.insets = new Insets(0, 10, 0, 5);
			gridBagConstraints6.gridheight = 7;
			gridBagConstraints6.gridwidth = 3;
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.weightx = 1.0;
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
	private JList getJListReferences() {
		if (jListReferences == null) {
			jListReferences = new JList();
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
			jButtonMoveUp.setPreferredSize(new Dimension(15, 15));
			jButtonMoveUp.setIcon(new ImageIcon(getClass().getResource(PathImage + "ArrowUp.png")));
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
			jButtonMoveDown.setIcon(new ImageIcon(getClass().getResource(PathImage + "ArrowDown.png")));
			jButtonMoveDown.setPreferredSize(new Dimension(15, 15));
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
			jButtonRename.setIcon(new ImageIcon(getClass().getResource(PathImage + "Rename.png")));
			jButtonRename.setPreferredSize(new Dimension(15, 15));
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
			jButtonRemoveAll.setIcon(new ImageIcon(getClass().getResource(PathImage + "Delete.png")));
			jButtonRemoveAll.setPreferredSize(new Dimension(15, 15));
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
			jButtonReferencesAdd.setIcon(new ImageIcon(getClass().getResource(PathImage + "ArrowUp.png")));
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
			jButtonReferencesRemove.setIcon(new ImageIcon(getClass().getResource(PathImage + "ArrowDown.png")));
			jButtonReferencesRemove.setSize(15, 15);
			jButtonReferencesRemove.setToolTipText("Objekt entfernen");
			jButtonReferencesRemove.setActionCommand("OntoObjectRemsove");
			jButtonReferencesRemove.addActionListener(this);
		}
		return jButtonReferencesRemove;
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
			jSplitEast.setDividerLocation(160);
			jSplitEast.setBottomComponent(getJSplitOntologie());
			jSplitEast.setTopComponent(getJPanelReferences());
		}
		return jSplitEast;
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
			jTreeOntology = new JTree();
			//jTreeOntology = new JTree( CurrProject.Ontology.getOntologyTree() );
			jTreeOntology = new JTree( currProject.ontologies4Project.getOntologyTree() );
			jTreeOntology.setName("OntoTree");
			jTreeOntology.setShowsRootHandles(false);
			jTreeOntology.setRootVisible(true);
			jTreeOntology.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
			jTreeOntology.addTreeSelectionListener( new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent ts) {
					
					// ----------------------------------------------------------
					// --- Tree-Selection abfangen --- S T A R T ----------------
					// ----------------------------------------------------------
					// --- Node auslesen und Slots anzeigen ---------------------
					DefaultMutableTreeNode CurrNode = (DefaultMutableTreeNode)ts.getPath().getLastPathComponent();
					CurrOntoObject = (OntologyClassTreeObject) CurrNode.getUserObject();
					JPanel NewSlotView = new OntologyTabClassView( CurrNode ); 
					int DivLoc = jSplitOntologie.getDividerLocation();
					jSplitOntologie.setBottomComponent( NewSlotView );
					jSplitOntologie.setDividerLocation(DivLoc);
					jPanelOntoSlots = NewSlotView; 					
					// ----------------------------------------------------------
					// --- Tree-Selection abfangen --- S T O P ------------------
					// ----------------------------------------------------------
				}// End - valueChanged
			});
		}
		return jTreeOntology;
	}
	// If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
    public void OntoTreeExpand2Level(Integer Up2TreeLevel, boolean expand ) {
    	Integer CurrNodeLevel = 1;
    	if ( Up2TreeLevel == null ) 
    		Up2TreeLevel = 1000;
    	OntoTreeExpand( new TreePath( currProject.ontologies4Project.getOntologyTree().getRoot() ), expand, CurrNodeLevel, Up2TreeLevel);
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
	 * This method initializes jPanelOntology	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelOntology() {
		if (jPanelOntology == null) {
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints14.gridy = 0;
			jLabelOntologie = new JLabel();
			jLabelOntologie.setText("Projekt-Ontologie");
			jLabelOntologie.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.BOTH;
			gridBagConstraints13.weighty = 1.0;
			gridBagConstraints13.insets = new Insets(0, 10, 0, 10);
			gridBagConstraints13.gridy = 1;
			gridBagConstraints13.weightx = 1.0;
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
			jSplitOntologie.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitOntologie.setDividerSize(1);
			jSplitOntologie.setDividerLocation(150);
			jSplitOntologie.setResizeWeight(1.0D);
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
	 * updates the List of the AgetnReferences
	 */
	private void updateView4AgentConfig() {
		jListReferences.setListData( currProject.agentConfig.getListData(agentReference) );
	}
	/**
	 * reads the currently selected AgentReference to var. agentConfig
	 */
	private void setCurrAgentConfig() {
		if ( jListReferences.getSelectedValue() == null ) {
			agentConfig = null;
		} else {
			agentConfig = jListReferences.getSelectedValue().toString();
			String[] agentConfigArr = agentConfig.split(":");
			agentConfigPos = Integer.valueOf(agentConfigArr[0].trim()).intValue();
			agentConfig = agentConfigArr[1].trim();
			if (agentConfig.contains("]")) {
				int cutPos = agentConfig.indexOf("]");
				agentConfigMask = agentConfig.substring(1, cutPos);
			} else {
				agentConfigMask = null;
			}
		}
	}
	
	private void setTmpAgentConfig() {
		// -- configure Var. agentReference -------------------------
		if ( jAgentList.getSelectedValue() == null ) {
			agentReference = null;
		} else {
			agentReference = jAgentList.getSelectedValue().toString();
		}
		// -- configure Var. ontoReference --------------------------		
		if ( CurrOntoObject == null  ) {
			ontoReference = null;
		} else {
			if ( CurrOntoObject.isClass() ) {
				ontoReference = CurrOntoObject.getOntologySubClass().getName();	
			} else {
				ontoReference = null;	
			}
		}
	}
	
	/**
	 * This method initializes jPanelWest	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelWest() {
		if (jPanelWest == null) {
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.fill = GridBagConstraints.BOTH;
			gridBagConstraints18.gridy = 2;
			gridBagConstraints18.weightx = 0.0;
			gridBagConstraints18.weighty = 1.0;
			gridBagConstraints18.gridwidth = 2;
			gridBagConstraints18.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints18.gridx = 0;
			
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.weightx = 0.0;
			gridBagConstraints.insets = new Insets(10, 1, 10, 0);
			
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.insets = new Insets(10, 10, 10, 5);
			gridBagConstraints9.gridy = 1;
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
	 * This method initializes jPanelEastTop	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelEastTop() {
		if (jPanelEastTop == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.NONE;
			gridBagConstraints4.gridx = -1;
			gridBagConstraints4.gridy = -1;
			gridBagConstraints4.insets = new Insets(0, 0, 0, 13);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.NONE;
			gridBagConstraints3.gridx = -1;
			gridBagConstraints3.gridy = -1;
			gridBagConstraints3.insets = new Insets(0, 5, 0, 5);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.gridy = -1;
			gridBagConstraints2.weightx = 0.1;
			gridBagConstraints2.insets = new Insets(10, 0, 10, 0);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(10, 10, 10, 5);
			gridBagConstraints1.gridy = -1;
			gridBagConstraints1.gridx = -1;
			jPanelEastTop = new JPanel();
			jPanelEastTop.setLayout(new GridBagLayout());
			jPanelEastTop.add(jLabelStart, gridBagConstraints1);
			jPanelEastTop.add(getJTextAgentStartAs(), gridBagConstraints2);
			jPanelEastTop.add(getJButtonStartAgent(), gridBagConstraints3);
			jPanelEastTop.add(getJButtonAgentListRefresh(), gridBagConstraints4);
		}
		return jPanelEastTop;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		// --- Das ActionCommand und den Auslöser des Events ermitteln ---
		String ActCMD = ae.getActionCommand();
		Object trigger = ae.getSource();

		if ( trigger == jButtonAgentListRefresh ) {
			// --- AgentList aktualisieren ----------------
			Application.ClassDetector.reStartSearch(currProject, ClassSearcher.RESTART_AGENT_SEARCH);
			
			jTextAgent.setText(null);
			jTextAgentStartAs.setText(null);
			jListReferences.setListData(new Vector<String>());
			this.setCurrAgentConfig();
			this.setTmpAgentConfig();
		}
		else if ( trigger == jButtonStartAgent ) {
			// ------------------------------------------------------
			// --- Start the selected agent -------------------------
			// ------------------------------------------------------
			Class<? extends Agent> selectedAgentClass = null;
			String selectedAgentReference 			  = null;
			String selectedAgentName				  = null;
			selectedAgentClass		= ((AgentClassElement)jAgentList.getSelectedValue()).getElementClass();
			selectedAgentReference 	= selectedAgentClass.getName();
			selectedAgentName		= jTextAgentStartAs.getText();
			
			if ( selectedAgentName.length() != 0 && selectedAgentClass != null) {
				Object [] startArgs = null;
				String startArgsConfigured = currProject.agentConfig.get(selectedAgentReference);
				if (startArgsConfigured!=null) {
					// --- If start-arguments are set, get them now -----
					OntologyInstanceDialog oid = new OntologyInstanceDialog(Application.MainWindow, this.currProject, selectedAgentReference);
					oid.setTitle(Language.translate("Startargument definieren für Agent") + " '" + selectedAgentName + "' (" + selectedAgentClass.getSimpleName() + ")");
					oid.setVisible(true);
					// --- Wait ---
					if (oid.isCancelled()) return;
					startArgs = oid.getObjectConfiguration();
					oid.dispose();
					oid = null;	
				}
				
				// --- Start the Agent now --------------------------
				Application.JadePlatform.jadeAgentStart(jTextAgentStartAs.getText(), selectedAgentClass, startArgs, currProject.getProjectFolder());	
				jTextAgent.setText(null);
				jTextAgentStartAs.setText(null);
				jAgentList.clearSelection();
			}
		} else if (trigger == jButtonMoveUp ) {
			// --- Agenten-Referenz eins höher ----------------------
			setTmpAgentConfig();
			setCurrAgentConfig();
			if (currProject.agentConfig.movePosition(agentReference, agentConfig, agentConfigPos, -1) ){
				jListReferences.setSelectedIndex(agentConfigPos-2);
			}
		} else if (trigger == jButtonMoveDown ) {
			// --- Agenten-Referenz eins runter -----------
			setTmpAgentConfig();
			setCurrAgentConfig();
			if (currProject.agentConfig.movePosition(agentReference, agentConfig, agentConfigPos, 1) ){
				jListReferences.setSelectedIndex(agentConfigPos);
			}
		} else if (trigger == jButtonRename) {
			// --- Ontologie-Referenz maskieren -----------
			setTmpAgentConfig();
			setCurrAgentConfig();
			
			String input = null;
			String head = Language.translate("Ontologie Referenz maskieren");
			String msg  = Language.translate("Bitte geben Sie einen Bezeichner für die Referenz an!");
			
			if (agentConfigMask== null) {
				input = (String) JOptionPane.showInputDialog(Application.MainWindow, msg, head, JOptionPane.OK_CANCEL_OPTION);	
			} else {
				input = (String) JOptionPane.showInputDialog(Application.MainWindow, msg, head, JOptionPane.OK_CANCEL_OPTION, null, null, agentConfigMask);	
			}
			if (input==null) {
				return;
			} else {
				input = input.trim();
				if (input.equals("")) input = null;
			}
			
			currProject.agentConfig.setReferenceMask(agentReference, agentConfigPos-1, input);
			jListReferences.setSelectedIndex(agentConfigPos-1);
			
		} else if (trigger == jButtonRemoveAll) {
			// --- Agenten-Referenzen komplett entfernen --
			setTmpAgentConfig();
			currProject.agentConfig.removeReferencesComplete(agentReference);
		} else if (trigger == jButtonReferencesAdd ) {
			// --- Agenten-Referenzen hinzufügen ----------
			setTmpAgentConfig();
			currProject.agentConfig.addReference(agentReference, ontoReference);
		} else if (trigger == jButtonReferencesRemove ) {
			// --- Agenten-Referenzen entfernen -----------
			setTmpAgentConfig();
			setCurrAgentConfig();
			currProject.agentConfig.removeReference(agentReference, agentConfig);
		}
		else {
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + ActCMD);	
		};
		this.repaint();
	}
	
	@Override
	public void update(Observable arg0, Object notifyObject) {
		
		String ObjectName = notifyObject.toString();
		if ( ObjectName.equalsIgnoreCase( Project.CHANGED_StartArguments4BaseAgent ) ) {
			// --- Liste der Agenten-Referenzen aktualisieren ---
			this.updateView4AgentConfig();
			
		} else if ( ObjectName.equalsIgnoreCase( Project.CHANGED_ProjectOntology ) ) {
			// --- Ansicht auf die projekt-Ontologie aktualisieren --
			this.jTreeOntology.setModel( currProject.ontologies4Project.getOntologyTree() );
			this.OntoTreeExpand2Level(3, true);
		
		} else if ( ObjectName.equalsIgnoreCase( Project.CHANGED_ProjectResources ) ) {
			this.getJAgentList().reSetListModel();
			
		} else {
			//System.out.println("Unbekannte Meldung vom Observer: " + ObjectName);
		}
	}

	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
