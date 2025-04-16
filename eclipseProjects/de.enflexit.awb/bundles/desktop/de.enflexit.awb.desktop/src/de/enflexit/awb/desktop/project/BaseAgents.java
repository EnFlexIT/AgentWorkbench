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
package de.enflexit.awb.desktop.project;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.ButtonGroup;
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

import de.enflexit.language.Language;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.desktop.mainWindow.MainWindow;
import de.enflexit.common.Observable;
import de.enflexit.common.Observer;
import de.enflexit.common.classSelection.ClassElement2Display;
import de.enflexit.common.classSelection.JListClassSearcher;
import de.enflexit.common.ontology.AgentStartArgument;
import de.enflexit.common.ontology.OntologyClassTreeObject;
import de.enflexit.common.ontology.gui.OntologyInstanceDialog;
import jade.core.Agent;
import javax.swing.JRadioButton;

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
	
	private JLabel jLabelSearchAgent;
	private JTextField jTextAgentSearch;
	private JLabel jLabelStart;
	private JTextField jTextAgentStartAs;
	private JButton jButtonStartAgent;
	private JListClassSearcher jListAgentFound;
	
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
	private JPanel jPanelListSeelction;
	private JLabel jLabelBundleSelection;
	private JRadioButton jRadioButtonAgentsShowAll;
	private JRadioButton jRadioButtonAgentsShowFromProjectBundle;
	
	/**
	 * This is the default constructor.
	 * @param project the current project
	 */
	public BaseAgents(Project project) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);	
		this.initialize();	
		
		// --- Translate text modules ---------------------
		jLabelSearchAgent.setText(Language.translate("Suche Agenten") + ":");
		jLabelStart.setText(Language.translate("Starten als:")); 
		jLabelRecerence.setText(Language.translate("Start-Objekte aus Projekt-Ontologie"));
		jLabelOntologie.setText(Language.translate("Projekt-Ontologie"));

		this.getJLabelBundleSelection().setText(Language.translate("Zeige Agenten") + ":");
		this.getJRadioButtonAgentsShowAll().setText(Language.translate("Aus allen Bundeln"));
		this.getJRadioButtonAgentsShowFromProjectBundle().setText(Language.translate("Nur aus Projekt-Bundeln"));
		
		this.getJButtonStartAgent().setToolTipText(Language.translate("Agent starten..."));
		this.getJButtonMoveUp().setToolTipText(Language.translate("Objekt nach oben"));
		this.getJButtonMoveDown().setToolTipText(Language.translate("Objekt nach unten"));
		this.getJButtonRename().setToolTipText(Language.translate("Ontologie Referenz benennen"));
		this.getJButtonRemoveAll().setToolTipText(Language.translate("Alle Objekte löschen"));
		this.getJButtonReferencesAdd().setToolTipText(Language.translate("Objekt hinzufügen"));
		this.getJButtonReferencesRemove().setToolTipText(Language.translate("Objekt entfernen"));
		
		this.OntoTreeExpand2Level(3, true);
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setOneTouchExpandable(false);
		this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		this.setDividerSize(3);
		this.setResizeWeight(0.5);
		this.setSize(new Dimension(1003, 568));
		this.setLeftComponent(this.getJPanelWest());
		this.setRightComponent(this.getJPanelEast());
		
		// --- Disable radio buttons? -------------------------------
		if (this.currProject.getBundles().size()==0) {
			this.getJLabelBundleSelection().setEnabled(false);
			this.getJRadioButtonAgentsShowAll().setEnabled(false);
			this.getJRadioButtonAgentsShowFromProjectBundle().setEnabled(false);
		}
		
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
			
			jLabelSearchAgent = new JLabel();
			jLabelSearchAgent.setText("Suche Agenten:");
			jLabelSearchAgent.setFont(new Font("Dialog", Font.BOLD, 12));
			
			ButtonGroup bgBundleSelection = new ButtonGroup();  
			bgBundleSelection.add(this.getJRadioButtonAgentsShowAll());
			bgBundleSelection.add(this.getJRadioButtonAgentsShowFromProjectBundle());
			
			GridBagLayout gbl_jPanelWest = new GridBagLayout();
			gbl_jPanelWest.rowWeights = new double[]{0.0, 0.0, 0.0};
			gbl_jPanelWest.columnWeights = new double[]{0.0, 0.0};
			
			GridBagConstraints gbc_JLabelAgentSearch = new GridBagConstraints();
			gbc_JLabelAgentSearch.insets = new Insets(10, 10, 0, 5);
			gbc_JLabelAgentSearch.gridx = 0;
			gbc_JLabelAgentSearch.gridy = 0;
			
			GridBagConstraints gbc_jTextAgentSearch = new GridBagConstraints();
			gbc_jTextAgentSearch.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextAgentSearch.gridwidth = 1;
			gbc_jTextAgentSearch.insets = new Insets(10, 0, 0, 5);
			gbc_jTextAgentSearch.weightx = 0.0;
			gbc_jTextAgentSearch.gridx = 1;
			gbc_jTextAgentSearch.gridy = 0;
			
			GridBagConstraints gbc_jPanelListSeelction = new GridBagConstraints();
			gbc_jPanelListSeelction.insets = new Insets(10, 10, 0, 5);
			gbc_jPanelListSeelction.gridwidth = 2;
			gbc_jPanelListSeelction.fill = GridBagConstraints.BOTH;
			gbc_jPanelListSeelction.gridx = 0;
			gbc_jPanelListSeelction.gridy = 1;
			
			GridBagConstraints gbc_JListAgentsFound = new GridBagConstraints();
			gbc_JListAgentsFound.fill = GridBagConstraints.BOTH;
			gbc_JListAgentsFound.weightx = 1.0;
			gbc_JListAgentsFound.weighty = 1.0;
			gbc_JListAgentsFound.gridwidth = 2;
			gbc_JListAgentsFound.insets = new Insets(10, 10, 0, 5);
			gbc_JListAgentsFound.ipady = 0;
			gbc_JListAgentsFound.gridx = 0;
			gbc_JListAgentsFound.gridy = 2;
			
			jPanelWest = new JPanel();
			jPanelWest.setLayout(gbl_jPanelWest);
			jPanelWest.add(jLabelSearchAgent, gbc_JLabelAgentSearch);
			jPanelWest.add(getJPanelListSeelction(), gbc_jPanelListSeelction);
			jPanelWest.add(getJListAgentsFound(), gbc_JListAgentsFound);
			jPanelWest.add(getJTextAgentSearch(), gbc_jTextAgentSearch);
		}
		return jPanelWest;
	}
	
	private JPanel getJPanelListSeelction() {
		if (jPanelListSeelction == null) {
			jPanelListSeelction = new JPanel();
			GridBagLayout gbl_jPanelListSeelction = new GridBagLayout();
			gbl_jPanelListSeelction.columnWidths = new int[]{0, 0, 0, 0};
			gbl_jPanelListSeelction.rowHeights = new int[]{0, 0};
			gbl_jPanelListSeelction.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelListSeelction.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelListSeelction.setLayout(gbl_jPanelListSeelction);
			GridBagConstraints gbc_jLabelListSeledctionCaption = new GridBagConstraints();
			gbc_jLabelListSeledctionCaption.gridx = 0;
			gbc_jLabelListSeledctionCaption.gridy = 0;
			jPanelListSeelction.add(getJLabelBundleSelection(), gbc_jLabelListSeledctionCaption);
			GridBagConstraints gbc_jRadioButtonAgentsShowAll = new GridBagConstraints();
			gbc_jRadioButtonAgentsShowAll.insets = new Insets(0, 10, 0, 0);
			gbc_jRadioButtonAgentsShowAll.gridx = 1;
			gbc_jRadioButtonAgentsShowAll.gridy = 0;
			jPanelListSeelction.add(getJRadioButtonAgentsShowAll(), gbc_jRadioButtonAgentsShowAll);
			GridBagConstraints gbc_jRadioButtonAgentsShowFromProjectBundle = new GridBagConstraints();
			gbc_jRadioButtonAgentsShowFromProjectBundle.insets = new Insets(0, 10, 0, 0);
			gbc_jRadioButtonAgentsShowFromProjectBundle.gridx = 2;
			gbc_jRadioButtonAgentsShowFromProjectBundle.gridy = 0;
			jPanelListSeelction.add(getJRadioButtonAgentsShowFromProjectBundle(), gbc_jRadioButtonAgentsShowFromProjectBundle);
		}
		return jPanelListSeelction;
	}
	private JLabel getJLabelBundleSelection() {
		if (jLabelBundleSelection == null) {
			jLabelBundleSelection = new JLabel("Zeige Agenten:");
			jLabelBundleSelection.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelBundleSelection;
	}
	private JRadioButton getJRadioButtonAgentsShowAll() {
		if (jRadioButtonAgentsShowAll == null) {
			jRadioButtonAgentsShowAll = new JRadioButton("Aus allen Bundeln");
			jRadioButtonAgentsShowAll.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonAgentsShowAll.setSelected(true);
			jRadioButtonAgentsShowAll.addActionListener(this);
		}
		return jRadioButtonAgentsShowAll;
	}
	private JRadioButton getJRadioButtonAgentsShowFromProjectBundle() {
		if (jRadioButtonAgentsShowFromProjectBundle == null) {
			jRadioButtonAgentsShowFromProjectBundle = new JRadioButton("Nur aus Projekt-Bundeln");
			jRadioButtonAgentsShowFromProjectBundle.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonAgentsShowFromProjectBundle.setSelected(true);
			jRadioButtonAgentsShowFromProjectBundle.addActionListener(this);
		}
		return jRadioButtonAgentsShowFromProjectBundle;
	}
	
	/**
	 * This method initializes jPanelEastTop	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelEastTop() {
		if (jPanelEastTop == null) {
			
			jLabelStart = new JLabel();
			jLabelStart.setText("Starten als:");
			jLabelStart.setFont(new Font("Dialog", Font.BOLD, 12));
			
			GridBagConstraints gbc_JLabelStart = new GridBagConstraints();
			gbc_JLabelStart.insets = new Insets(10, 5, 10, 5);
			gbc_JLabelStart.gridy = -1;
			gbc_JLabelStart.gridx = -1;

			GridBagConstraints gbc_JButtonStartAgent = new GridBagConstraints();
			gbc_JButtonStartAgent.fill = GridBagConstraints.NONE;
			gbc_JButtonStartAgent.gridx = -1;
			gbc_JButtonStartAgent.gridy = -1;
			gbc_JButtonStartAgent.insets = new Insets(0, 0, 0, 10);
			
			GridBagConstraints gbc_JTextAgentStartAs = new GridBagConstraints();
			gbc_JTextAgentStartAs.fill = GridBagConstraints.HORIZONTAL;
			gbc_JTextAgentStartAs.gridx = -1;
			gbc_JTextAgentStartAs.gridy = -1;
			gbc_JTextAgentStartAs.weightx = 0.1;
			gbc_JTextAgentStartAs.insets = new Insets(10, 0, 10, 5);
			
			jPanelEastTop = new JPanel();
			jPanelEastTop.setLayout(new GridBagLayout());
			jPanelEastTop.add(jLabelStart, gbc_JLabelStart);
			jPanelEastTop.add(getJTextAgentStartAs(), gbc_JTextAgentStartAs);
			jPanelEastTop.add(getJButtonStartAgent(), gbc_JButtonStartAgent);
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
	private JTextField getJTextAgentSearch() {
		if (jTextAgentSearch == null) {
			jTextAgentSearch = new JTextField();
			jTextAgentSearch.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextAgentSearch.setPreferredSize(new Dimension(200, 26));
			jTextAgentSearch.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent ke) {
					BaseAgents.this.getJListAgentsFound().setModelFiltered(BaseAgents.this.getJTextAgentSearch().getText());
				}
			});
		}
		return jTextAgentSearch;
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
			gridBagConstraints6.insets = new Insets(10, 5, 5, 5);
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
	 * Return the JList of the agents found.
	 * @return the JList to show the agents found
	 */
	private JListClassSearcher getJListAgentsFound() {
		if (jListAgentFound == null) {
			jListAgentFound = new JListClassSearcher(Agent.class, this.currProject.getBundleNames());
			jListAgentFound.setToolTipText(Language.translate("Agenten in diesem Projekt"));
			jListAgentFound.setPreferredSize(new Dimension(333, 300));
			jListAgentFound.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListAgentFound.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent se) {

					if (se.getValueIsAdjusting()==true && jListAgentFound.getSelectedValue()!=null) {
						
						updateView4AgentConfig();
						
						// -----------------------------------------------------
						// --- Set entry to the current agent ------------------
						int maxLenght = 80;
						if (agentReference.length() > maxLenght) {
							String toDisplayStart = agentReference.substring(0, 4);
							String toDisplayEnd   = agentReference.substring(agentReference.length() - maxLenght );
							agentReference = toDisplayStart + "..." + toDisplayEnd;
						}
						getJTextAgentSearch().setText(agentReference);
						
						
						// -----------------------------------------------------
						// --- Propose name for the agent ----------------------
						String startAs = jListAgentFound.getSelectedValue().toString();
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
		return jListAgentFound;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		String ActCMD = ae.getActionCommand();
		Object trigger = ae.getSource();

		if (trigger==this.getJRadioButtonAgentsShowAll()) {
			// --- Show agents from all Bundles available -----------
			this.getJListAgentsFound().setExclusiveBundleNames(null);
			
		} else if (trigger==this.getJRadioButtonAgentsShowFromProjectBundle()) {
			// --- Show agents from project Bundles only ------------
			this.getJListAgentsFound().setExclusiveBundleNames(this.currProject.getBundleNames());
			
		} else if (trigger==this.getJButtonStartAgent()) {
			// ------------------------------------------------------
			// --- Start the selected agent -------------------------
			// ------------------------------------------------------
			ClassElement2Display selection = (ClassElement2Display)this.getJListAgentsFound().getSelectedValue();
			if (selection==null) {
				String head = Language.translate("Agent auswählen!");
				String msg  = Language.translate("Bitte wählen Sie den Agenten aus, den Sie starten wollen.");
				JOptionPane.showMessageDialog((MainWindow) Application.getMainWindow(), msg, head, JOptionPane.WARNING_MESSAGE);
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
					OntologyInstanceDialog oid = new OntologyInstanceDialog((MainWindow)Application.getMainWindow(), this.currProject.getOntologyVisualisationHelper(), this.currProject.getAgentStartConfiguration(), agentClassName);
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
				jTextAgentSearch.setText(null);
				jTextAgentStartAs.setText(null);
				this.getJListAgentsFound().clearSelection();
			}
			
		} else if (trigger == this.getJButtonReferencesAdd()) {
			// --- Add start argument ---------------------
			this.readSelectionFromForm();
			if (agentReference==null || ontoReference==null) {
				String head = Language.translate("Agent und Ontologie-Referenz auswählen!");
				String msg  = Language.translate("Bitte wählen Sie einen Agenten und eine Ontologie-Referenz aus den entsprechenden Listen.");
				JOptionPane.showMessageDialog((MainWindow)Application.getMainWindow(), msg, head, JOptionPane.WARNING_MESSAGE);
				return;
			}
			int newPos = this.currProject.getAgentStartConfiguration().addReference(agentReference, ontoReference);
			this.currProject.setAgentStartConfigurationUpdated();
			if (newPos!=-1) {
				this.getJListReferences().setSelectedIndex(newPos);
			}
			
		} else if (trigger == this.getJButtonReferencesRemove()) {
			// --- Remove start argument ------------------
			this.readSelectionFromForm();
			int newPos = this.currProject.getAgentStartConfiguration().removeReference(this.agentReference, this.getJListReferences().getSelectedIndex());
			this.currProject.setAgentStartConfigurationUpdated();		
			if (newPos!=-1) {
				this.getJListReferences().setSelectedIndex(newPos);
			}
		
		} else if (trigger == this.getJButtonRemoveAll()) {
			// --- Remove start arguments completely ------
			this.readSelectionFromForm();
			this.currProject.getAgentStartConfiguration().removeAllReferences(agentReference);
			this.currProject.setAgentStartConfigurationUpdated();
					
		} else if (trigger == this.getJButtonMoveUp()) {
			// --- Move start argument up -----------------
			this.readSelectionFromForm();
			int newPos = this.currProject.getAgentStartConfiguration().movePosition(agentReference, jListReferences.getSelectedIndex(), -1); 
			this.currProject.setAgentStartConfigurationUpdated();
			if (newPos!=-1){
				this.jListReferences.setSelectedIndex(newPos);
			}
			
		} else if (trigger == this.getJButtonMoveDown()) {
			// --- Move start argument down ---------------
			this.readSelectionFromForm();
			int newPos = this.currProject.getAgentStartConfiguration().movePosition(agentReference, jListReferences.getSelectedIndex(), 1); 
			this.currProject.setAgentStartConfigurationUpdated();
			if (newPos!=-1){
				this.jListReferences.setSelectedIndex(newPos);
			}
			
		} else if (trigger == this.getJButtonRename()) {
			// --- mask ontology-reference ----------------
			String input = null;
			String head  = null;
			String msg   = null;
			int selectedIndex = this.getJListReferences().getSelectedIndex(); 
			
			if (selectedIndex==-1) {
				head = Language.translate("Ontologie-Referenz auswählen!");
				msg  = Language.translate("Bitte wählen Sie eine der zugeordneten Ontologie-Referenzen aus der Liste.");
				JOptionPane.showMessageDialog((MainWindow)Application.getMainWindow(), msg, head, JOptionPane.WARNING_MESSAGE);
				return;
			}
			this.readSelectionFromForm();
			
			input = null;
			head = Language.translate("Ontologie Referenz maskieren");
			msg  = Language.translate("Bitte geben Sie einen Bezeichner für die Referenz an!");
			
			AgentStartArgument ageStartArg = this.currProject.getAgentStartConfiguration().get(this.agentReference).get(selectedIndex);
			String displayTitle = ageStartArg.getDisplayTitle();
			if (displayTitle==null) {
				input = (String) JOptionPane.showInputDialog((MainWindow)Application.getMainWindow(), msg, head, JOptionPane.QUESTION_MESSAGE);	
			} else {
				input = (String) JOptionPane.showInputDialog((MainWindow)Application.getMainWindow(), msg, head, JOptionPane.QUESTION_MESSAGE, null, null, displayTitle);	
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
		}
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
		if (this.getJListAgentsFound().getSelectedValue()==null) {
			this.agentReference = null;
		} else {
			ClassElement2Display selection = (ClassElement2Display) this.getJListAgentsFound().getSelectedValue();
			this.agentReference = selection.getClassElement();
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
	
} 
