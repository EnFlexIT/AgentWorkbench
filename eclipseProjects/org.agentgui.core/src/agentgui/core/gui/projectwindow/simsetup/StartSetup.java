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
package agentgui.core.gui.projectwindow.simsetup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.gui.AgentSelector;
import agentgui.core.project.Project;
import agentgui.core.project.setup.AgentClassElement4SimStart;
import agentgui.core.project.setup.SimulationSetup;
import agentgui.core.project.setup.SimulationSetupNotification;
import agentgui.core.project.setup.SimulationSetups;
import agentgui.core.project.setup.SimulationSetupNotification.SimNoteReason;
import de.enflexit.common.classSelection.ClassElement2Display;
import de.enflexit.common.ontology.gui.OntologyInstanceViewer;

/**
 * Represents the JPanel/Tab 'Configuration' - 'Agent-Start' 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class StartSetup extends JPanel implements Observer, ActionListener {

	private static final long serialVersionUID = -3929823093128900880L;
	
	private String newLine = Application.getGlobalInfo().getNewLineSeparator();  

	private Project currProject;
	private SimulationSetup currSimSetup; 
	private AgentClassElement4SimStart agentSelected;
	private AgentClassElement4SimStart agentSelectedLast;
	
	private DefaultListModel<AgentClassElement4SimStart> jListModelAgents2Start = new DefaultListModel<AgentClassElement4SimStart>();
	
	private JSplitPane jSplitPane = null;
	private JPanel jPanelRightSplit = null;
	private JPanel jPanelLeftSplit = null;
	
	private JPanel jPanelButtons = null;
	private JPanel jPanelRight = null;
	private JPanel jPanelOntoloInstView = null;
	private OntologyInstanceViewer currOntoInstViewer = null;
	
	private JScrollPane jScrollPaneStartList = null;
	
	private JList<AgentClassElement4SimStart> jListStartList = null;
	private JButton jButtonAgentAdd = null;
	private JButton jButtonAgentRemove = null;
	private JButton jButtonMoveUp = null;
	private JButton jButtonMoveDown = null;
	
	private JTextField jTextFieldStartAs = null;
	private JButton jButtonStartOK = null;
	private JLabel jLabelHeader = null;

	private JLabel jLabelStartList = null;
	private JComboBox<String> jComboBoxStartLists = null;
	
	
	/**
	 * This is the default constructor
	 */
	public StartSetup(Project project) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);		
		this.initialize();	
		
		// --- Das aktuelle Setup laden -------------------
		this.setupLoad();
		
		// --- Translate ----------------------------------
		jComboBoxStartLists.setToolTipText(Language.translate(jComboBoxStartLists.getToolTipText()));
		jButtonAgentAdd.setToolTipText(Language.translate("Agenten hinzufügen"));
		jButtonAgentRemove.setToolTipText(Language.translate("Agenten entfernen"));
		jButtonMoveUp.setToolTipText(Language.translate("Agent nach oben verschieben"));
		jButtonMoveDown.setToolTipText(Language.translate("Agent nach unten verschieben"));
		
		jLabelHeader.setText(Language.translate("Starten als") + ":");
		jLabelStartList.setText(Language.translate("Start-Liste:"));
		
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.weighty = 1.0;
		gridBagConstraints1.weightx = 1.0;
		
		this.setLayout(new GridBagLayout());
		this.setSize(800, 350);
		this.setPreferredSize(new Dimension(550, 176));
		this.setAutoscrolls(true);		
		this.setBorder(null);
		this.setFocusable(true);
		this.setVisible(true);
		this.add(getJSplitPane(), gridBagConstraints1);
		
	}
	
	/**
	 * This method initializes jSplitPane	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerLocation(400);
			jSplitPane.setResizeWeight(0.5);
			jSplitPane.setDividerSize(10);
			jSplitPane.setLeftComponent(getJPanelLeftSplit());
			jSplitPane.setRightComponent(getJPanelRightSplit());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jPanelRightSplit	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelRightSplit() {
		if (jPanelRightSplit == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = GridBagConstraints.NORTH;
			gridBagConstraints6.insets = new Insets(5, 10, 0, 10);
			gridBagConstraints6.gridx = -1;
			gridBagConstraints6.gridy = -1;
			gridBagConstraints6.weightx = 0.5;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.NORTH;
			gridBagConstraints.gridx = -1;
			gridBagConstraints.gridy = -1;
			gridBagConstraints.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints.fill = GridBagConstraints.NONE;
			jPanelRightSplit = new JPanel();
			jPanelRightSplit.setLayout(new GridBagLayout());
			jPanelRightSplit.add(getJPanelButtons(), gridBagConstraints);
			jPanelRightSplit.add(getJPanelRight(), gridBagConstraints6);
		}
		return jPanelRightSplit;
	}

	/**
	 * This method initializes jPanelLeftSplit	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelLeftSplit() {
		if (jPanelLeftSplit == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints9.gridy = 0;
			jLabelStartList = new JLabel();
			jLabelStartList.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelStartList.setText("Start-Liste:");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.BOTH;
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.insets = new Insets(5, 5, 0, 5);
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.insets = new Insets(5, 10, 2, 5);
			gridBagConstraints11.gridwidth = 2;
			gridBagConstraints11.gridx = 0;
			jPanelLeftSplit = new JPanel();
			jPanelLeftSplit.setLayout(new GridBagLayout());
			jPanelLeftSplit.add(getJScrollPaneStartList(), gridBagConstraints11);
			jPanelLeftSplit.add(getJComboBoxStartLists(), gridBagConstraints8);
			jPanelLeftSplit.add(jLabelStartList, gridBagConstraints9);
		}
		return jPanelLeftSplit;
	}
	
	/**
	 * This method initializes jScrollPaneStartList	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneStartList() {
		if (jScrollPaneStartList == null) {
			jScrollPaneStartList = new JScrollPane();
			jScrollPaneStartList.setPreferredSize(new Dimension(250, 131));
			jScrollPaneStartList.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jScrollPaneStartList.setViewportView(getJListStartList());
		}
		return jScrollPaneStartList;
	}

	/**
	 * This method initializes jComboBoxStartLists	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox<String> getJComboBoxStartLists() {
		if (jComboBoxStartLists == null) {
			jComboBoxStartLists = new JComboBox<String>();
			if (currSimSetup!=null) {
				jComboBoxStartLists.setModel(currSimSetup.getComboBoxModel4AgentLists());	
			}
			//jComboBoxStartLists.setFont(new Font("Dialog", Font.BOLD, 12));
			jComboBoxStartLists.setToolTipText("Auswahl Startliste");
			jComboBoxStartLists.addActionListener(this);
		}
		return jComboBoxStartLists;
	}
	
	/**
	 * This method initializes jListStartList	
	 * @return javax.swing.JList	
	 */
	private JList<AgentClassElement4SimStart> getJListStartList() {
		if (jListStartList == null) {
			// --- Create MouseInputAdapter for Drag and Drop ------------------ 
			MouseInputAdapter mouseHandler = new MouseInputAdapter() {
			
				private Object draggedObject;
			    private int fromIndex;
			    
			    public void mousePressed(final MouseEvent evt) {
			        draggedObject = jListStartList.getSelectedValue();
			        fromIndex = jListStartList.getSelectedIndex();
			    }
			    public void mouseDragged(final MouseEvent evt) {
			        int toIndex = jListStartList.locationToIndex(evt.getPoint());
			        if (toIndex != fromIndex) {
			            jListModelAgents2Start.removeElementAt(fromIndex);
			            jListModelAgents2Start.insertElementAt((AgentClassElement4SimStart) draggedObject, toIndex);
			            fromIndex = toIndex;
			            agentRenumberList();
			        }
			    }
			};
			
			// -----------------------------------------------------------------
			// --- Create renderer in order to display the icons ---------------  
			// -----------------------------------------------------------------
			ListCellRenderer<AgentClassElement4SimStart> cellRenderer = new ListCellRenderer<AgentClassElement4SimStart>() {
				
				@Override
				public Component getListCellRendererComponent(JList<? extends AgentClassElement4SimStart> list, AgentClassElement4SimStart value, int index, boolean isSelected, boolean cellHasFocus) {

					// --- Datenobjekt -----------------------------------------
					AgentClassElement4SimStart agentInfo = value;
					
					// --- Layout - Werte --------------------------------------
					Color  bgcolBlue = new Color(57, 105, 138);
					Border lineBlueFocus = BorderFactory.createLineBorder(new Color(115, 164, 209), 1);
					Border lineBlueNoFocus = BorderFactory.createLineBorder(bgcolBlue);
					Border lineWhite = BorderFactory.createLineBorder(Color.white);
										
					JLabel jlab = new JLabel(); 
					jlab.setFont(new Font("Dialog", Font.PLAIN, 13));
					jlab.setText( agentInfo.toString() );
					jlab.setToolTipText( agentInfo.getAgentClassReference() );
					jlab.setOpaque(true);

					jlab.setIcon(GlobalInfo.getInternalImageIcon("StatGreen.png"));	
					
					if (isSelected) {
						jlab.setForeground(Color.white);
						jlab.setBackground(bgcolBlue);
						jlab.setBorder(lineBlueNoFocus);
					} else {
						jlab.setForeground(Color.black);
						jlab.setBackground(Color.white);
						jlab.setBorder(lineWhite);
					}
					
					if (cellHasFocus) {
						jlab.setBorder(lineBlueFocus);
					}

					return jlab; 
				}
			};
			
			// -----------------------------------------------------------------
			// --- Hier nun endlich die JList erstellen ------------------------
			// -----------------------------------------------------------------
			jListStartList = new JList<AgentClassElement4SimStart>(jListModelAgents2Start);
			jListStartList.addMouseListener(mouseHandler);
			jListStartList.addMouseMotionListener(mouseHandler);
			jListStartList.setCellRenderer(cellRenderer);
			jListStartList.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (jListStartList.getSelectedValue()!= null) {

						agentSelected = (AgentClassElement4SimStart) jListStartList.getSelectedValue();
						if (agentSelected.equals(agentSelectedLast)==false) {
							// --- NEW SELECTION IN THE LIST ------------------
							jTextFieldStartAs.setText(agentSelected.getStartAsName());

							// --- Show OntologyInstanceViewer for this agent -
							OntologyInstanceViewer oiv = new OntologyInstanceViewer(currProject.getOntologyVisualisationHelper(), currProject.getAgentStartConfiguration(), agentSelected.getAgentClassReference());
							oiv.setConfigurationXML(agentSelected.getStartArguments());
							setOntologyInstView(oiv);

							// --- Set reminder for the last selected object --
							agentSelectedLast = agentSelected;
						}
					
					}
				}
			});	
			
			
		}
		return jListStartList;
	}

	/**
	 * This method will remove the selections from {@link #jListStartList} and will 
	 * set the needed blank {@link OntologyInstanceViewer} 
	 */
	private void setJListStartListEmptySelection() {
		this.agentSelectedLast = null;
		this.jListStartList.setSelectedValue(null, false);
		OntologyInstanceViewer oiv = new OntologyInstanceViewer( currProject.getOntologyVisualisationHelper());
		setOntologyInstView(oiv);
	}

	/**
	 * This method initializes jButtonAgentAdd	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAgentAdd() {
		if (jButtonAgentAdd == null) {
			jButtonAgentAdd = new JButton();
			jButtonAgentAdd.setPreferredSize(new Dimension(45, 26));
			jButtonAgentAdd.setIcon(GlobalInfo.getInternalImageIcon("ListPlus.png"));
			jButtonAgentAdd.setToolTipText("Agenten hinzufügen");
			jButtonAgentAdd.setActionCommand("AgentAdd");
			jButtonAgentAdd.addActionListener(this);
		}
		return jButtonAgentAdd;
	}

	/**
	 * This method initializes jButtonAgentRemove	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAgentRemove() {
		if (jButtonAgentRemove == null) {
			jButtonAgentRemove = new JButton();
			jButtonAgentRemove.setPreferredSize(new Dimension(45, 26));
			jButtonAgentRemove.setIcon(GlobalInfo.getInternalImageIcon("ListMinus.png"));
			jButtonAgentRemove.setToolTipText("Agenten entfernen");
			jButtonAgentRemove.setActionCommand("AgentRemove");
			jButtonAgentRemove.addActionListener(this);
		}
		return jButtonAgentRemove;
	}

	/**
	 * This method initializes jButtonMoveUp	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonMoveUp() {
		if (jButtonMoveUp == null) {
			jButtonMoveUp = new JButton();
			jButtonMoveUp.setIcon(GlobalInfo.getInternalImageIcon("ArrowUp.png"));
			jButtonMoveUp.setToolTipText("Agent nach oben verschieben");
			jButtonMoveUp.setPreferredSize(new Dimension(45, 26));
			jButtonMoveUp.setActionCommand("AgentUp");
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
			jButtonMoveDown.setPreferredSize(new Dimension(45, 26));
			jButtonMoveDown.setToolTipText("Agent nach unten verschieben");
			jButtonMoveDown.setIcon(GlobalInfo.getInternalImageIcon("ArrowDown.png"));
			jButtonMoveDown.setActionCommand("AgentDown");
			jButtonMoveDown.addActionListener(this);
		}
		return jButtonMoveDown;
	}

	/**
	 * This method initializes jPanelRight	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelRight() {
		if (jPanelRight == null) {
			
			
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			
			
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 2;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new Insets(0, 5, 0, 0);

			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 3;
			gridBagConstraints2.gridwidth = 3;
			gridBagConstraints2.weightx = 0.0;
			
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.anchor = GridBagConstraints.CENTER;
			gridBagConstraints2.insets = new Insets(5, 0, 0, 0);
			
			
			jLabelHeader = new JLabel();
			jLabelHeader.setText("Starten als:");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelRight = new JPanel();
			jPanelRight.setLayout(new GridBagLayout());
			jPanelRight.add(jLabelHeader, gridBagConstraints10);
			jPanelRight.add(getJTextFieldStartAs(), gridBagConstraints5);
			jPanelRight.add(getJButtonStartOK(), gridBagConstraints7);
			jPanelRight.add(getJPanelOntoloInstView(), gridBagConstraints2);
		}
		return jPanelRight;
	}

	/**
	 * This method initializes jPanelButtons	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints4.gridy = 3;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new Insets(20, 0, 0, 0);
			gridBagConstraints3.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints1.gridy = -1;
			gridBagConstraints1.gridx = -1;
			jPanelButtons = new JPanel();
			jPanelButtons.setLayout(new GridBagLayout());
			jPanelButtons.add(getJButtonAgentAdd(), gridBagConstraints1);
			jPanelButtons.add(getJButtonAgentRemove(), gridBagConstraints2);
			jPanelButtons.add(getJButtonMoveUp(), gridBagConstraints3);
			jPanelButtons.add(getJButtonMoveDown(), gridBagConstraints4);
		}
		return jPanelButtons;
	}

	/**
	 * This method initializes jTextFieldStartAs	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldStartAs() {
		if (jTextFieldStartAs == null) {
			jTextFieldStartAs = new JTextField();
			jTextFieldStartAs.setPreferredSize(new Dimension(4, 26));
			jTextFieldStartAs.addActionListener(this);
		}
		return jTextFieldStartAs;
	}

	/**
	 * This method initialises jButtonStartOK	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonStartOK() {
		if (jButtonStartOK == null) {
			jButtonStartOK = new JButton();
			jButtonStartOK.setPreferredSize(new Dimension(45, 26));
			jButtonStartOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonStartOK.setToolTipText(Language.translate("Speichern"));
			jButtonStartOK.setIcon(GlobalInfo.getInternalImageIcon("MBsave.png"));			
			jButtonStartOK.setActionCommand("Save");
			jButtonStartOK.addActionListener(this);
		}
		return jButtonStartOK;
	}

	/**
	 * This method initializes jScrollPaneDynForm	
	 * @return javax.swing.JScrollPane	
	 */
	private JPanel getJPanelOntoloInstView() {
		if (jPanelOntoloInstView == null) {
			jPanelOntoloInstView = new JPanel();
			jPanelOntoloInstView.setPreferredSize(new Dimension(100, 100));
			jPanelOntoloInstView.setLayout(new BorderLayout());
		}
		return jPanelOntoloInstView;
	}
	/**
	 * This method sets the view to the current OntologyInstanceViewer, depending
	 * on the selected agent on the left
	 * @param ontoInstView
	 */
	private void setOntologyInstView(OntologyInstanceViewer ontoInstView) {
		
		// --- Remind the current Viewer ------------------
		this.currOntoInstViewer = ontoInstView;
		// --- Set the view to the current Viewer ---------
		this.getJPanelOntoloInstView().removeAll();
		this.getJPanelOntoloInstView().add(ontoInstView);
		this.getJPanelOntoloInstView().validate();
		this.getJPanelOntoloInstView().repaint();
	}
	
	/**
	 * Setup load.
	 */
	private void setupLoad() {
	
		// --- Load the current SimulationSetup ---------------------
		currSimSetup = currProject.getSimulationSetups().getCurrSimSetup();
		if ( currSimSetup==null ) {
			SimulationSetups setups = currProject.getSimulationSetups();
			String currSimSetupName = currProject.getSimulationSetupCurrent();
			setups.setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_LOAD, currSimSetupName, false);
			currSimSetup = currProject.getSimulationSetups().getCurrSimSetup();
		}
		// --- Load the current DefaultListModel laden --------------
		this.jListModelAgents2Start = this.currSimSetup.getAgentDefaultListModel(this.jListModelAgents2Start, SimulationSetup.AGENT_LIST_ManualConfiguration);
		this.getJListStartList().setModel(this.jListModelAgents2Start);
		
		// --- Set the ComboBoxModel for the StartList Selector -----
		DefaultComboBoxModel<String> cbm = currSimSetup.getComboBoxModel4AgentLists();
		cbm.setSelectedItem(SimulationSetup.AGENT_LIST_ManualConfiguration);
		this.getJComboBoxStartLists().setModel(cbm);
				
		this.setJListStartListEmptySelection();
	}
	
	/**
	 * This method opens the 'AgentSelector' - dialog to select the agents
	 * for this Simulation-Setup
	 */
	private void agentAdd() {
		
		Integer startCounter = jListModelAgents2Start.size() + 1;

		// ==================================================================
		AgentSelector agentSelector = new AgentSelector(Application.getMainWindow());
		agentSelector.setVisible(true);
		// ==================================================================
		
		// ==================================================================
		if (agentSelector.isCanceled()==false) {
			Object[] agentsSelected = agentSelector.getSelectedAgentClasses();
			for (int i = 0; i < agentsSelected.length; i++) {
				
				AgentClassElement4SimStart ac4s = new AgentClassElement4SimStart((ClassElement2Display) agentsSelected[i], SimulationSetup.AGENT_LIST_ManualConfiguration); 
				// --- Set the order number -------------------------
				ac4s.setPostionNo(startCounter);
				startCounter++;
				// --- Check the name to prevent name clashes -------
				String nameSuggestion = ac4s.getStartAsName();
				if (currSimSetup.isAgentNameExists(nameSuggestion)==true) {
					ac4s.setStartAsName(currSimSetup.getAgentNameUnique(nameSuggestion));
				}								
				// --- Add to start-list ----------------------------
				jListModelAgents2Start.addElement(ac4s);
			}
		}
		agentSelector.dispose();
		this.save();
		// ==================================================================
	}
	
	/**
	 * This method sets the given new name for the currently selected agent
	 * @param newAgentName
	 */
	private void agentNameSet(String newAgentName) {
		
		String msgHead = "";
		String msgText = "";
		
		if (newAgentName==null || newAgentName.equals("")) {
			msgHead += Language.translate("Name ungültig!");
			msgText += Language.translate("Bitte wählen Sie einen Namen für den Agenten!");
			JOptionPane.showMessageDialog(null, msgText, msgHead, JOptionPane.OK_OPTION);
			return;
		}
		
		// --- new name == old name ? --------------------------------
		if (agentSelected.getStartAsName().equalsIgnoreCase(newAgentName)==false) {
			// --- new name entered ----------------------------------
			if (currSimSetup.isAgentNameExists(newAgentName)==false) {
				agentSelected.setStartAsName(newAgentName);
				jListStartList.repaint();
			} else {
				
				msgHead += Language.translate("Name bereits vorhanden!");
				msgText += "Der Name '@' wird in der Startliste bereits verwendet !" + newLine +
						   "Bitte wählen Sie einen anderen Namen für den Agenten.";
				msgText = Language.translate(msgText);
				msgText = msgText.replace("@", newAgentName);
				
				JOptionPane.showMessageDialog(null, msgText, msgHead, JOptionPane.OK_OPTION);
			}
		}
		this.save();
	}
	
	/**
	 * This method removes all selected Agents from the StarterList
	 */
	private void agentRemove() {
		
		for (int i = 0; i < jListStartList.getSelectedValuesList().size(); i++) {
			AgentClassElement4SimStart ac4s = (AgentClassElement4SimStart) jListStartList.getSelectedValuesList().get(i); 
			jListModelAgents2Start.removeElement(ac4s);
		}
		this.agentRenumberList();
		this.save();
		
		agentSelected = null;
		agentSelectedLast = null;

		jTextFieldStartAs.setText(null);
		OntologyInstanceViewer oiv = new OntologyInstanceViewer(currProject.getOntologyVisualisationHelper());
		this.setOntologyInstView(oiv);
		
	}
	
	/**
	 * This Method changes the postion / order of a selected  
	 * single agent in the list of the agents to start
	 * @param direction 
	 */
	private void agentMovePosition(Integer direction) {
		
		AgentClassElement4SimStart ac4s = null;
		Integer position = 0;
		Integer positionNew = 0;
		
		if (jListStartList.getSelectedValuesList().size()==1) {

			ac4s = (AgentClassElement4SimStart) jListStartList.getSelectedValue();
			position = jListStartList.getSelectedIndex();
			positionNew = position + direction;
			if ( positionNew>=0 && positionNew<jListModelAgents2Start.size() ) {
				jListModelAgents2Start.removeElementAt(position);
	            jListModelAgents2Start.insertElementAt(ac4s, positionNew);
	            this.agentRenumberList();
	            jListStartList.setSelectedIndex(positionNew);
			}
		}
		this.save();
	}
	
	/**
	 * This Method renews the enumeration of the selected Agents
	 */
	private void agentRenumberList() {
		
		Integer startCounter = 1;
		AgentClassElement4SimStart ac4s = null;
		
		for (int i=0; i<jListModelAgents2Start.size(); i++) {
			 ac4s = (AgentClassElement4SimStart) jListModelAgents2Start.getElementAt(i); 
			 ac4s.setPostionNo(startCounter);
			 startCounter++;
		}

	}

	/**
	 * Saves the current configuration to the Simulations-Setup 
	 */
	private void save() {
		
		if (this.currOntoInstViewer!=null) {
			// --- Save the current configuration -----------------------------
			this.currOntoInstViewer.save();
			// --- Set the current configuration to the simulation setup ------
			if (this.agentSelectedLast!=null) {
				this.agentSelectedLast.setStartArguments(currOntoInstViewer.getConfigurationXML());	
			}
		}
		this.currSimSetup.save();
		
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {
		
		if (updateObject instanceof SimulationSetupNotification) {
			// --- Change inside the simulation setup ---------------
			SimulationSetupNotification scn = (SimulationSetupNotification) updateObject;
			switch (scn.getUpdateReason()) {
			case SIMULATION_SETUP_SAVED:
				break;
			case SIMULATION_SETUP_ADD_NEW:
				this.jListModelAgents2Start = new DefaultListModel<AgentClassElement4SimStart>();
				this.currSimSetup.setAgentDefaultListModel(SimulationSetup.AGENT_LIST_ManualConfiguration, this.jListModelAgents2Start);
				this.setupLoad();
				break;
			default:
				this.setupLoad();	
				break;
			}
			
		} else {
			//System.out.println( this.getClass().getName() + ": " + arg1.toString() );	
		}
	}
	
	/**
	 * This Method handles all ActionEvents from this part of the User-View
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		Object trigger = ae.getSource();
		
		if (trigger==jComboBoxStartLists) {
			String selectedItem = (String) jComboBoxStartLists.getSelectedItem(); 
			if (selectedItem!=null) {
				DefaultListModel<AgentClassElement4SimStart> dlm = this.currSimSetup.getAgentDefaultListModel(selectedItem);
				this.jListModelAgents2Start = dlm;
				this.jListStartList.setModel(dlm);
				this.setJListStartListEmptySelection();
				if (selectedItem.equals(SimulationSetup.AGENT_LIST_ManualConfiguration)) {
					this.jButtonAgentAdd.setEnabled(true);
					this.jButtonAgentRemove.setEnabled(true);
				} else {
					this.jButtonAgentAdd.setEnabled(false);
					this.jButtonAgentRemove.setEnabled(false);
				}
			}
			
		}else if (trigger == jButtonAgentAdd) {
			this.agentAdd();			
		} else if (trigger == jButtonAgentRemove ) {
			this.agentRemove();
		} else if (trigger == jButtonMoveUp) {
			this.agentMovePosition(-1);
		} else if (trigger == jButtonMoveDown) {
			this.agentMovePosition(1);
		} else if (trigger == jTextFieldStartAs) {
			jButtonStartOK.requestFocus();
			this.agentNameSet(jTextFieldStartAs.getText());
		} else if (trigger == jButtonStartOK) {
			this.agentNameSet(jTextFieldStartAs.getText());
		} else {
			System.out.println(ae.toString());
		};
	}

	
	
}  //  @jve:decl-index=0:visual-constraint="20,8"
