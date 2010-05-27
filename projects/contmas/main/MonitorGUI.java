/**
 * @author Hanno - Felix Wagner, 27.05.2010
 * Copyright 2010 Hanno - Felix Wagner
 * 
 * This file is part of ContMAS.
 *
 * ContMAS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ContMAS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ContMAS.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package contmas.main;

import jade.core.AID;
import jade.gui.AgentTree;
import jade.util.leap.List;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import contmas.agents.MonitorAgent;

import java.awt.Dimension;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class MonitorGUI extends JInternalFrame implements ActionListener{
	private MonitorAgent myAgent;
	private java.util.List<AID> monitoredCHs=new ArrayList<AID>();
	private JDesktopPane canvas;
	private JSplitPane masterSplit = null;
	private JDesktopPane agentViewDesktop = null;
	private JScrollPane agentListScroller = null;
	private AgentTree AT= null;  //  @jve:decl-index=0:
	private JSplitPane configSplit = null;
	private JPanel configPanel = null;
	private JButton monitorButton = null;
	private JButton unmonitorButton = null;
	private JTextField jTextField = null;
	private JLabel jLabel = null;
	private JCheckBox jCheckBox = null;
	private JLabel jLabel1 = null;
	
	/**
	 * @param monitorAgent
	 * @param canvas 
	 */
	public MonitorGUI(MonitorAgent monitorAgent, JDesktopPane canvas){
		initialize();
		myAgent=monitorAgent;
		displayOn(canvas);
	}

	public void displayOn(JDesktopPane canvas){
		this.canvas=canvas;
		this.canvas.add(this);
		this.grabFocus();
	}


	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new Dimension(500, 370));
        this.setResizable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setContentPane(getMasterSplit());
        this.setTitle("Monitor");
			
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0){
		// TODO Auto-generated method stub

	}

	/**
	 * This method initializes masterSplit	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getMasterSplit(){
		if(masterSplit == null){
			masterSplit=new JSplitPane();
			masterSplit.setDividerLocation(180);
			masterSplit.setDividerSize(5);
			masterSplit.setLeftComponent(getConfigSplit());
			masterSplit.setRightComponent(getAgentViewDesktop());
//			masterSplit.setLeftComponent();
		}
		return masterSplit;
	}
	
	private AgentTree getAgentTree(){
		if(this.AT == null){
			this.AT=new AgentTree();
			this.AT.refreshLocalPlatformName("Contmas");
			this.AT.addContainerNode("contmas",null);
			Const.expandTree(this.AT.tree);
		}
		return this.AT;
	}

	/**
	 * This method initializes agentViewDesktop	
	 * 	
	 * @return javax.swing.JDesktopPane	
	 */
	private JDesktopPane getAgentViewDesktop(){
		if(agentViewDesktop == null){
			agentViewDesktop=new JDesktopPane();
		}
		return agentViewDesktop;
	}

	/**
	 * This method initializes agentListScroller	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getAgentListScroller(){
		if(agentListScroller == null){
			AgentTree AT=this.getAgentTree();

			this.agentListScroller=new JScrollPane(AT.tree);
		}
		return agentListScroller;
	}

	/**
	 * This method initializes configSplit	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getConfigSplit(){
		if(configSplit == null){
			configSplit=new JSplitPane();
			configSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
			configSplit.setDividerLocation(200);
			configSplit.setDividerSize(5);
			configSplit.setTopComponent(getAgentListScroller());
			configSplit.setBottomComponent(getConfigPanel());
		}
		return configSplit;
	}

	/**
	 * This method initializes configPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getConfigPanel(){
		if(configPanel == null){
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 4;
			jLabel1 = new JLabel();
			jLabel1.setText("Auto monitor");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = GridBagConstraints.EAST;
			gridBagConstraints3.gridy = 4;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.EAST;
			gridBagConstraints2.gridy = 3;
			jLabel = new JLabel();
			jLabel.setText("Threshold");
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 3;
			gridBagConstraints11.weightx = 1.0D;
			gridBagConstraints11.gridwidth = 1;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 2;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.gridheight = 2;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			configPanel=new JPanel();
			configPanel.setLayout(new GridBagLayout());
			configPanel.add(getMonitorButton(), gridBagConstraints);
			configPanel.add(getUnmonitorButton(), gridBagConstraints1);
			configPanel.add(getJTextField(), gridBagConstraints11);
			configPanel.add(jLabel, gridBagConstraints2);
			configPanel.add(getJCheckBox(), gridBagConstraints3);
			configPanel.add(jLabel1, gridBagConstraints4);
		}
		return configPanel;
	}
	
	public void updateAgentTree(List newAgents,Boolean remove){
		AgentTree agentTree=this.getAgentTree();
		Iterator<AID> agentIter=newAgents.iterator();

		while(agentIter.hasNext()){
			AID curAgent=agentIter.next();
			if( !remove){
				agentTree.addAgentNode(curAgent.getLocalName(),"","contmas");
			}else{
				agentTree.removeAgentNode("contmas",curAgent.getLocalName());

			}
		}
		Const.expandTree(agentTree.tree);
	}

	/**
	 * This method initializes monitorButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getMonitorButton(){
		if(monitorButton == null){
			monitorButton=new JButton();
			monitorButton.setText("Monitor selected agent(s)");
		}
		return monitorButton;
	}

	/**
	 * This method initializes unmonitorButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getUnmonitorButton(){
		if(unmonitorButton == null){
			unmonitorButton=new JButton();
			unmonitorButton.setText("stop monitoring");
		}
		return unmonitorButton;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField(){
		if(jTextField == null){
			jTextField=new JTextField();
			jTextField.setName("Threshold");
			jTextField.setText("2");
		}
		return jTextField;
	}

	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox(){
		if(jCheckBox == null){
			jCheckBox=new JCheckBox();
			jCheckBox.setSelected(true);
		}
		return jCheckBox;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
