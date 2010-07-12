package gui.projectwindow.simsetup;

import gui.AgentSelector;

import java.awt.Color;
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
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;

import mas.agents.AgentClassElement;
import mas.agents.AgentClassElement4SimStart;
import application.Application;
import application.Project;

/**
 * @author: Christian Derksen
 *
 */
public class StartSetup extends JPanel implements Observer, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3929823093128900880L;
	
	private Project currProject;
	private DefaultListModel jListModelAgents2Start = new DefaultListModel();
	
	private JScrollPane jScrollPaneStartList = null;
	private JList jListStartList = null;
	private JButton jButtonAgentAdd = null;
	private JButton jButtonAgentRemove = null;
	private JButton jButtonMoveUp = null;
	private JButton jButtonMoveDown = null;
	private JPanel jPanelRight = null;
	private JPanel jPanelButtons = null;
	private JLabel jLabelHeader = null;
	private JTextField jTextFieldStartAs = null;
	private JButton jButtonStartOK = null;

	private JCheckBox jCheckBoxIsMobileAgent = null;
	private JLabel jLabelIsMobileAgent = null;
	private JPanel jPanelTop = null;
	private JComboBox jComboBoxSetupSelector = null;
	private JLabel jLabelSetupSelector = null;
	private JButton jButtonSetupNew = null;
	private JButton jButtonSetupDelete = null;

	
	/**
	 * This is the default constructor
	 */
	public StartSetup( Project project ) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);		
		initialize();	
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.insets = new Insets(10, 10, 0, 10);
		gridBagConstraints11.gridwidth = 4;
		gridBagConstraints11.gridy = 0;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 2;
		gridBagConstraints6.anchor = GridBagConstraints.NORTH;
		gridBagConstraints6.fill = GridBagConstraints.NONE;
		gridBagConstraints6.gridy = 1;
		GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
		gridBagConstraints51.gridx = 3;
		gridBagConstraints51.weighty = 1.0;
		gridBagConstraints51.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints51.weightx = 1.0;
		gridBagConstraints51.insets = new Insets(10, 10, 10, 10);
		gridBagConstraints51.anchor = GridBagConstraints.NORTH;
		gridBagConstraints51.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(10, 10, 0, 10);
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(766, 350);
		this.setAutoscrolls(true);		
		this.setBorder(null);
		this.setFocusable(true);
		this.setVisible(true);
		this.add(getJScrollPaneStartList(), gridBagConstraints);
		this.add(getJPanelRight(), gridBagConstraints51);
		this.add(getJPanelButtons(), gridBagConstraints6);
		this.add(getJPanelTop(), gridBagConstraints11);
		
	}
	
	/**
	 * This method initializes jPanelTop	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelTop() {
		if (jPanelTop == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 3;
			gridBagConstraints15.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints15.gridy = 0;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 2;
			gridBagConstraints14.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints14.gridy = 0;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.gridy = 0;
			jLabelSetupSelector = new JLabel();
			jLabelSetupSelector.setText("Konfiguration:");
			jLabelSetupSelector.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.BOTH;
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints12.weightx = 1.0;
			jPanelTop = new JPanel();
			jPanelTop.setLayout(new GridBagLayout());
			jPanelTop.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanelTop.add(getJComboBoxSetupSelector(), gridBagConstraints12);
			jPanelTop.add(jLabelSetupSelector, gridBagConstraints13);
			jPanelTop.add(getJButtonSetupNew(), gridBagConstraints14);
			jPanelTop.add(getJButtonSetupDelete(), gridBagConstraints15);
		}
		return jPanelTop;
	}

	/**
	 * This method initializes jComboBoxSetupSelector	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxSetupSelector() {
		if (jComboBoxSetupSelector == null) {
			jComboBoxSetupSelector = new JComboBox();
			jComboBoxSetupSelector.setPreferredSize(new Dimension(100, 26));
			jComboBoxSetupSelector.setEditable(true);
		}
		return jComboBoxSetupSelector;
	}

	/**
	 * This method initializes jButtonSetupNew	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSetupNew() {
		if (jButtonSetupNew == null) {
			jButtonSetupNew = new JButton();
			jButtonSetupNew.setPreferredSize(new Dimension(45, 26));
			jButtonSetupNew.setIcon(new ImageIcon(getClass().getResource("/img/ListPlus.png")));
			jButtonSetupNew.setActionCommand("SetupNew");
			jButtonSetupNew.addActionListener(this);
		}
		return jButtonSetupNew;
	}

	/**
	 * This method initializes jButtonSetupDelete	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSetupDelete() {
		if (jButtonSetupDelete == null) {
			jButtonSetupDelete = new JButton();
			jButtonSetupDelete.setPreferredSize(new Dimension(45, 26));
			jButtonSetupDelete.setIcon(new ImageIcon(getClass().getResource("/img/ListMinus.png")));
			jButtonSetupDelete.setActionCommand("SetupDelete");
			jButtonSetupDelete.addActionListener(this);
		}
		return jButtonSetupDelete;
	}
	
	/**
	 * This method initializes jScrollPaneStartList	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneStartList() {
		if (jScrollPaneStartList == null) {
			jScrollPaneStartList = new JScrollPane();
			jScrollPaneStartList.setPreferredSize(new Dimension(330, 131));
			jScrollPaneStartList.setViewportView(getJListStartList());
		}
		return jScrollPaneStartList;
	}

	/**
	 * This method initializes jListStartList	
	 * @return javax.swing.JList	
	 */
	private JList getJListStartList() {
		if (jListStartList == null) {
			
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
			            jListModelAgents2Start.insertElementAt(draggedObject, toIndex);
			            fromIndex = toIndex;
			            agentRenumberList();
			        }
			    }
			};
			
			jListStartList = new JList(jListModelAgents2Start);
			jListStartList.addMouseListener(mouseHandler);
			jListStartList.addMouseMotionListener(mouseHandler);			
			
		}
		return jListStartList;
	}

	/**
	 * This method initializes jButtonAgentAdd	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAgentAdd() {
		if (jButtonAgentAdd == null) {
			jButtonAgentAdd = new JButton();
			jButtonAgentAdd.setPreferredSize(new Dimension(45, 26));
			jButtonAgentAdd.setIcon(new ImageIcon(getClass().getResource("/img/ListPlus.png")));
			jButtonAgentAdd.setToolTipText("Add Start-Element");
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
			jButtonAgentRemove.setIcon(new ImageIcon(getClass().getResource("/img/ListMinus.png")));
			jButtonAgentRemove.setToolTipText("Remove Start-Element");
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
			jButtonMoveUp.setIcon(new ImageIcon(getClass().getResource("/img/ArrowUp.png")));
			jButtonMoveUp.setToolTipText("Objekt nach oben");
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
			jButtonMoveDown.setToolTipText("Objekt nach unten");
			jButtonMoveDown.setIcon(new ImageIcon(getClass().getResource("/img/ArrowDown.png")));
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
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.gridy = 1;
			jLabelIsMobileAgent = new JLabel();
			jLabelIsMobileAgent.setText("Mobiler Agent:");
			jLabelIsMobileAgent.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(10, 5, 0, 0);
			gridBagConstraints8.gridy = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 2;
			gridBagConstraints7.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridy = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints5.gridx = 1;
			jLabelHeader = new JLabel();
			jLabelHeader.setText("Starten als:");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			jPanelRight = new JPanel();
			jPanelRight.setLayout(new GridBagLayout());
			jPanelRight.add(jLabelHeader, gridBagConstraints10);
			jPanelRight.add(getJTextFieldStartAs(), gridBagConstraints5);
			jPanelRight.add(getJButtonStartOK(), gridBagConstraints7);
			jPanelRight.add(getJCheckBoxIsMobileAgent(), gridBagConstraints8);
			jPanelRight.add(jLabelIsMobileAgent, gridBagConstraints9);
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
			gridBagConstraints1.insets = new Insets(10, 0, 0, 0);
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
		}
		return jTextFieldStartAs;
	}

	/**
	 * This method initializes jButtonStartOK	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonStartOK() {
		if (jButtonStartOK == null) {
			jButtonStartOK = new JButton();
			jButtonStartOK.setPreferredSize(new Dimension(50, 20));
			jButtonStartOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonStartOK.setText("OK");
			jButtonStartOK.setActionCommand("Save");
			jButtonStartOK.addActionListener(this);
		}
		return jButtonStartOK;
	}

	/**
	 * This method initializes jCheckBoxIsMobileAgent	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxIsMobileAgent() {
		if (jCheckBoxIsMobileAgent == null) {
			jCheckBoxIsMobileAgent = new JCheckBox();
			jCheckBoxIsMobileAgent.setText("");
		}
		return jCheckBoxIsMobileAgent;
	}
	
	/**
	 * This Method handles all ActionEvents from this part of the User-View
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		// --- Das ActionCommand und den Auslöser des Events ermitteln ---
		Object Trigger = ae.getSource();
		
		// --- Fallunterscheidung 'Auslöser' -----------------------------
		if ( Trigger == jButtonSetupNew ) {
			this.setupAdd();
		} else if ( Trigger == jButtonSetupDelete ) {
			this.setupRemove();
		} else if ( Trigger == jButtonAgentAdd ) {
			this.agentAdd();			
		} else if ( Trigger == jButtonAgentRemove ) {
			this.agentRemove();
		} else if ( Trigger == jButtonMoveUp ) {
			this.agentMovePosition(-1);
		} else if ( Trigger == jButtonMoveDown ) {
			this.agentMovePosition(1);
		} else {
			
		};
	}

	/**
	 * Listens to the Data-Model of this Project 
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method adds a new Simulation-Setup to this project
	 */
	private void setupAdd() {
		
	}

	/**
	 * This method removes the current Simulation-Setup from to this project
	 */
	private void setupRemove() {
		
	}
	
	/**
	 * This method opens the 'AgentSelector' - dialog to select the agents
	 * for this Simulation-Setup
	 */
	private void agentAdd() {
		
		Integer startCounter = jListModelAgents2Start.size() + 1;

		// ==================================================================
		AgentSelector agentSelector = new AgentSelector(Application.MainWindow);
		agentSelector.setVisible(true);
		// ==================================================================
		
		// ==================================================================
		if (agentSelector.isCanceled()==false) {
			Object[] agentsSelected = agentSelector.getSelectedAgentClasses();
			for (int i = 0; i < agentsSelected.length; i++) {
				
				AgentClassElement4SimStart ac4s = new AgentClassElement4SimStart((AgentClassElement) agentsSelected[i]); 
				ac4s.setPostionNo(startCounter);
				startCounter++;
				
				jListModelAgents2Start.addElement(ac4s);
			}
		}
		agentSelector.dispose();
		agentSelector = null;		
		// ==================================================================
		
	}
	
	/**
	 * This method removes all selected Agents from the StarterList
	 */
	private void agentRemove() {
		
		Object[] agentsSelected = null;
		if (jListStartList.getSelectedValues().length==0) {
			agentsSelected = null;
			return;
		} else {
			agentsSelected = jListStartList.getSelectedValues();
		}
		for (int i = 0; i < agentsSelected.length; i++) {
			AgentClassElement4SimStart ac4s = (AgentClassElement4SimStart) agentsSelected[i]; 
			jListModelAgents2Start.removeElement(ac4s);
		}
		this.agentRenumberList();
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
		
		if (jListStartList.getSelectedValues().length == 1) {

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
			 jListModelAgents2Start.setElementAt(ac4s, i);
		}

	}
	
}  //  @jve:decl-index=0:visual-constraint="20,8"
