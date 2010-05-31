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
import jade.gui.GuiEvent;
import jade.gui.AgentTree.AgentNode;
import jade.gui.AgentTree.Node;
import jade.util.leap.List;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.tree.TreePath;

import contmas.agents.MonitorAgent;
import contmas.ontology.BayMap;
import contmas.ontology.ContainerHolder;
import javax.swing.JScrollPane;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class MonitorGUI extends JInternalFrame implements ActionListener{
	private MonitorAgent myAgent;
	private HashMap<AID, AgentView> monitoredCHs=new HashMap<AID, AgentView>(); //  @jve:decl-index=0:
//	private java.util.List<AID> monitoredCHs=new ArrayList<AID>();  //  @jve:decl-index=0:
	private JDesktopPane canvas;
	private JSplitPane masterSplit=null;
	private JDesktopPane agentViewDesktop=null;
	private JScrollPane agentListScroller=null;
	private AgentTree AT=null; //  @jve:decl-index=0:
	private JSplitPane configSplit=null;
	private JPanel configPanel=null;
	private JButton monitorButton=null;
	private JButton unmonitorButton=null;
	private JTextField treshholdInput=null;
	private JLabel jLabel=null;
	private JCheckBox autoMonitorCheckbox=null;
	private JLabel jLabel1=null;
	/**
	 * @param monitorAgent
	 * @param canvas 
	 */
	public MonitorGUI(MonitorAgent monitorAgent,JDesktopPane canvas){
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
	private void initialize(){
		this.setSize(new Dimension(500,370));
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
			masterSplit.setResizeWeight(0.0D);
			masterSplit.setRightComponent(getAgentViewDesktop());
			masterSplit.setLeftComponent(getConfigSplit());
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
			configSplit.setResizeWeight(1.0D);
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
			GridBagConstraints gridBagConstraints4=new GridBagConstraints();
			gridBagConstraints4.gridx=1;
			gridBagConstraints4.anchor=GridBagConstraints.WEST;
			gridBagConstraints4.gridy=4;
			jLabel1=new JLabel();
			jLabel1.setText("Auto monitor");
			GridBagConstraints gridBagConstraints3=new GridBagConstraints();
			gridBagConstraints3.gridx=0;
			gridBagConstraints3.anchor=GridBagConstraints.EAST;
			gridBagConstraints3.gridy=4;
			GridBagConstraints gridBagConstraints2=new GridBagConstraints();
			gridBagConstraints2.gridx=0;
			gridBagConstraints2.anchor=GridBagConstraints.EAST;
			gridBagConstraints2.gridy=3;
			jLabel=new JLabel();
			jLabel.setText("Threshold");
			GridBagConstraints gridBagConstraints11=new GridBagConstraints();
			gridBagConstraints11.fill=GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy=3;
			gridBagConstraints11.weightx=1.0D;
			gridBagConstraints11.gridwidth=1;
			gridBagConstraints11.anchor=GridBagConstraints.WEST;
			gridBagConstraints11.gridx=1;
			GridBagConstraints gridBagConstraints1=new GridBagConstraints();
			gridBagConstraints1.gridx=1;
			gridBagConstraints1.anchor=GridBagConstraints.WEST;
			gridBagConstraints1.gridy=2;
			GridBagConstraints gridBagConstraints=new GridBagConstraints();
			gridBagConstraints.gridx=0;
			gridBagConstraints.gridwidth=2;
			gridBagConstraints.gridheight=2;
			gridBagConstraints.anchor=GridBagConstraints.WEST;
			gridBagConstraints.gridy=0;
			configPanel=new JPanel();
			configPanel.setLayout(new GridBagLayout());
			configPanel.add(getMonitorButton(),gridBagConstraints);
			configPanel.add(getUnmonitorButton(),gridBagConstraints1);
			configPanel.add(getTreshholdInput(),gridBagConstraints11);
			configPanel.add(jLabel,gridBagConstraints2);
			configPanel.add(getAutoMonitorCheckbox(),gridBagConstraints3);
			configPanel.add(jLabel1,gridBagConstraints4);
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
				if(getAutoMonitorCheckbox().isSelected()){
					monitorAgent(curAgent);
				}
			}else{
				agentTree.removeAgentNode("contmas",curAgent.getLocalName());
				unmonitorAgent(curAgent);
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
			monitorButton.addActionListener(new java.awt.event.ActionListener(){
				public void actionPerformed(java.awt.event.ActionEvent e){
					monitorSelected();
				}

			});
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
			unmonitorButton.addActionListener(new java.awt.event.ActionListener(){
				public void actionPerformed(java.awt.event.ActionEvent e){
					unmonitorSelected(); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return unmonitorButton;
	}

	/**
	 * This method initializes treshholdInput	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTreshholdInput(){
		if(treshholdInput == null){
			treshholdInput=new JTextField();
			treshholdInput.setName("Threshold");
			treshholdInput.setText("2");
		}
		return treshholdInput;
	}

	/**
	 * This method initializes autoMonitorCheckbox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getAutoMonitorCheckbox(){
		if(autoMonitorCheckbox == null){
			autoMonitorCheckbox=new JCheckBox();
			autoMonitorCheckbox.setSelected(true);
		}
		return autoMonitorCheckbox;
	}

	private java.util.List<AgentNode> getSelectedAgentNodes(){
		java.util.List<AgentNode> nodes=new ArrayList<AgentNode>();
		TreePath paths[];
		paths=this.AT.tree.getSelectionPaths();
		if(paths != null){
			for(int i=0;i < paths.length;i++){
				Node now=(Node) (paths[i].getLastPathComponent());
				if(now instanceof AgentNode){
					nodes.add((AgentNode) now);
				}
			}
		}
		return nodes;
	}

	private void monitorSelected(){
		java.util.List<AgentNode> nodes=getSelectedAgentNodes();
		for(Iterator<AgentNode> iterator=nodes.iterator();iterator.hasNext();){
			AgentNode agentNode=iterator.next();
			AID aid=new AID();
			aid.setLocalName(agentNode.getName());
			monitorAgent(aid,null);
		}
	}

	protected void unmonitorSelected(){
		java.util.List<AgentNode> nodes=getSelectedAgentNodes();
		for(Iterator<AgentNode> iterator=nodes.iterator();iterator.hasNext();){
			AgentNode agentNode=iterator.next();
			AID aid=new AID();
			aid.setLocalName(agentNode.getName());
			unmonitorAgent(aid);
		}
	}

	public void monitorAgent(AID aid){
		GuiEvent ge=new GuiEvent(this,MonitorAgent.REFRESH);
		ge.addParameter(aid);
		this.myAgent.postGuiEvent(ge);

	}

	/**
	 * @param agent
	 * @param recieved
	 */
	public void monitorAgent(AID aid,ContainerHolder recieved){
		if( !monitoredCHs.containsKey(aid)){
			Integer treshhold=Integer.valueOf(this.getTreshholdInput().getText());
			Integer cap=treshhold;
			if(recieved != null){
				BayMap map=recieved.getContains();
				cap=map.getX_dimension() * map.getY_dimension() * map.getZ_dimension();
			}

			if(cap >= treshhold){
				AgentView curView=new AgentView(myAgent,aid,recieved);
				curView.addInternalFrameListener(new InternalFrameAdapter(){
					@Override
					public void internalFrameClosing(InternalFrameEvent e){
						unmonitorAgent(((AgentView) e.getInternalFrame()).getMonitoredAgent());
					}
				});
				getAgentViewDesktop().add(curView);
				monitoredCHs.put(aid,curView);
			}
		}
	}

	private void unmonitorAgent(AID aid){
		if(monitoredCHs.containsKey(aid)){
			AgentView curView=monitoredCHs.get(aid);
			curView.dispose();
			monitoredCHs.remove(aid);

		}
	}

	public HashMap<AID, AgentView> getMonitoredAgents(){
		return monitoredCHs;
	}

} //  @jve:decl-index=0:visual-constraint="10,10"
