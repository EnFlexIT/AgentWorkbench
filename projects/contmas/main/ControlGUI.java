/**
 * @author Hanno - Felix Wagner Copyright 2010 Hanno - Felix Wagner This file is
 *         part of ContMAS. ContMAS is free software: you can redistribute it
 *         and/or modify it under the terms of the GNU Lesser General Public
 *         License as published by the Free Software Foundation, either version
 *         3 of the License, or (at your option) any later version. ContMAS is
 *         distributed in the hope that it will be useful, but WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *         License for more details. You should have received a copy of the GNU
 *         Lesser General Public License along with ContMAS. If not, see
 *         <http://www.gnu.org/licenses/>.
 */

package contmas.main;

import jade.core.AID;
import jade.gui.AgentTree;
import jade.gui.GuiEvent;
import jade.gui.AgentTree.AgentNode;
import jade.gui.AgentTree.Node;
import jade.util.leap.List;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.*;

import contmas.agents.ControlGUIAgent;
import contmas.ontology.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class ControlGUI extends JInternalFrame implements ActionListener{

	private static final long serialVersionUID=1L;
	private JPanel jContentPane=null;
	private JButton ButtonCreateAgent=null;
	private JLabel AgentLabelX=null;
	private JTextField TFAgentX=null;
	private JLabel AgentLabelY=null;
	private JTextField TFAgentY=null;
	private JTextField TFAgentZ=null;
	private JLabel AgentLabelZ=null;
	private JLabel AgentLabelName=null;
	private JTextField TFAgentName=null;
	private ControlGUIAgent myAgent;
	private JLabel Heading=null;
	private JLabel AgentLabelLength=null;
	private JTextField TFShipLength=null;
	private JDesktopPane canvas=null;
	private JTextArea console=null;
	private JScrollPane consoleScrollPane=null;
	private JScrollPane AgentTreeScrollPane=null;
	private AgentTree AT=null;
	private JScrollPane systemConsoleScrollPane=null;
	private JTextArea systemConsole=null;
	private JButton ButtonGetOntRep=null;
	private JScrollPane ontRepScrollPane=null;
	private JTextArea ontRep=null;
	private JScrollPane loadListScrollPane=null;
	private JLabel ontRepAgentName=null;
	private JList loadList=null;
	private JCheckBox populateCheckBox=null;
	private JCheckBox randomizeCheckBox=null;
	private JScrollPane harbourSetupScrollPane=null;
	private JTree HarbourSetupTree=null;
	private JComboBox agentType = null;
	private JLabel AgentLabelType = null;
	private JList habitatList = null;
	private JScrollPane capabilitiesScrollPane = null;
	private JList capabilitiesList = null;
	private JButton ButtonSetHabitat = null;
	private JButton ButtonAddCapability = null;
	private JButton ButtonCapabilityRemove = null;

	/**
	 * This is the default constructor
	 */
	public ControlGUI(ControlGUIAgent a){
		super();
		this.myAgent=a;
		this.initialize();
	}

	public void displayOn(JDesktopPane canvas){
		this.canvas=canvas;
		this.canvas.add(this);
	}

	/**
	 * This method initializes ButtonCreateAgent
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getButtonCreateAgent(){
		if(this.ButtonCreateAgent == null){
			this.ButtonCreateAgent=new JButton();

			this.ButtonCreateAgent.setBounds(new Rectangle(8, 327, 126, 20));
			this.ButtonCreateAgent.setText("Start Agent");

			this.ButtonCreateAgent.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					GuiEvent ge=new GuiEvent(this,1);
					ge.addParameter(ControlGUI.this.TFAgentName.getText());
					ge.addParameter(ControlGUI.this.TFAgentX.getText());
					ge.addParameter(ControlGUI.this.TFAgentY.getText());
					ge.addParameter(ControlGUI.this.TFAgentZ.getText());
					ge.addParameter(ControlGUI.this.randomizeCheckBox.isSelected());
					ge.addParameter(ControlGUI.this.populateCheckBox.isSelected());
					ge.addParameter(ControlGUI.this.TFShipLength.getText());
					ge.addParameter(((AgentClassElement) getAgentType().getSelectedItem()).getAgentClass());
					ge.addParameter(((DefaultListModel) getHabitatList().getModel()).toArray()[0]);
					ge.addParameter(((DefaultListModel) getCapabilitiesList().getModel()).toArray());

					ControlGUI.this.myAgent.postGuiEvent(ge);
				}
			});
		}
		return this.ButtonCreateAgent;
	}

	public JDesktopPane getCanvas(){
		return this.canvas;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane(){
		if(this.jContentPane == null){
			AgentLabelType = new JLabel();
			AgentLabelType.setBounds(new Rectangle(6, 38, 38, 16));
			AgentLabelType.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			AgentLabelType.setText("Type");
			ontRepAgentName=new JLabel();
			ontRepAgentName.setBounds(new Rectangle(439,67,199,16));
			ontRepAgentName.setName("ontRepAgentName");
			ontRepAgentName.setText("[No Agent Yet]");
			this.AgentLabelLength=new JLabel();
			this.AgentLabelLength.setText("Length");
			AgentLabelLength.setBounds(new Rectangle(2, 186, 55, 16));
			this.Heading=new JLabel();
			this.Heading.setFont(new Font("Dialog",Font.BOLD,14));
			Heading.setBounds(new Rectangle(6, 10, 196, 15));
			this.Heading.setText("Create new Agent");
			this.AgentLabelName=new JLabel();
			this.AgentLabelName.setText("Name");
			AgentLabelName.setBounds(new Rectangle(5, 69, 55, 16));
			this.AgentLabelZ=new JLabel();
			this.AgentLabelZ.setText("Size Z");
			AgentLabelZ.setBounds(new Rectangle(4, 154, 55, 16));
			this.AgentLabelY=new JLabel();
			this.AgentLabelY.setText("Size Y");
			AgentLabelY.setBounds(new Rectangle(6, 127, 55, 16));
			this.AgentLabelX=new JLabel();
			this.AgentLabelX.setText("Size X");
			AgentLabelX.setBounds(new Rectangle(5, 100, 55, 16));
			this.jContentPane=new JPanel();
			this.jContentPane.setLayout(null);
			this.jContentPane.add(this.getTFAgentName(),null);
			this.jContentPane.add(this.getTFAgentX(),null);
			this.jContentPane.add(this.AgentLabelX,null);
			this.jContentPane.add(this.AgentLabelY,null);
			this.jContentPane.add(this.getTFAgentY(),null);
			this.jContentPane.add(this.getTFAgentZ(),null);
			this.jContentPane.add(this.AgentLabelZ,null);
			this.jContentPane.add(this.AgentLabelName,null);
			this.jContentPane.add(this.Heading,null);
			this.jContentPane.add(this.AgentLabelLength,null);
			this.jContentPane.add(this.getTFShipLength(),null);
			this.jContentPane.add(this.getButtonCreateAgent(),null);
			this.jContentPane.add(this.getConsoleScrollPane(),null);
			this.jContentPane.add(this.getAgentTreeScrollPane(),null);
			this.jContentPane.add(this.getSystemConsoleScrollPane(),null);
			this.jContentPane.add(this.getButtonGetOntRep(),null);
			this.jContentPane.add(this.getOntRepScrollPane(),null);
			jContentPane.add(getLoadListScrollPane(),null);
			jContentPane.add(ontRepAgentName,null);
			jContentPane.add(getPopulateCheckBox(),null);
			jContentPane.add(getRandomizeCheckBox(),null);
			jContentPane.add(getHarbourSetupScrollPane(),null);
			jContentPane.add(getAgentType(), null);
			jContentPane.add(AgentLabelX, null);
			jContentPane.add(AgentLabelY, null);
			jContentPane.add(Heading, null);
			jContentPane.add(AgentLabelLength, null);
			jContentPane.add(AgentLabelName, null);
			jContentPane.add(AgentLabelZ, null);
			jContentPane.add(AgentLabelType, null);
			jContentPane.add(getHabitatList(), null);
			jContentPane.add(getCapabilitiesScrollPane(), null);
			jContentPane.add(getButtonSetHabitat(), null);
			jContentPane.add(getButtonAddCapability(), null);
			jContentPane.add(getButtonCapabilityRemove(), null);
			this.jContentPane.add(this.AgentLabelLength,null);
			this.jContentPane.add(this.Heading,null);
			this.jContentPane.add(this.AgentLabelName,null);
			this.jContentPane.add(this.AgentLabelZ,null);
			this.jContentPane.add(this.AgentLabelY,null);
			this.jContentPane.add(this.AgentLabelX,null);
			this.jContentPane.add(this.AgentLabelLength,null);
			this.jContentPane.add(this.Heading,null);
			this.jContentPane.add(this.AgentLabelName,null);
			this.jContentPane.add(this.AgentLabelZ,null);
			this.jContentPane.add(this.AgentLabelX,null);
			this.jContentPane.add(this.AgentLabelY,null);
		}
		return this.jContentPane;

	}

	/**
	 * This method initializes TFShipLength
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTFShipLength(){
		if(this.TFShipLength == null){
			this.TFShipLength=new JTextField();
			this.TFShipLength.setBounds(new Rectangle(65, 185, 46, 25));
			this.TFShipLength.setText("120.5");
		}
		return this.TFShipLength;
	}

	/**
	 * This method initializes TFAgentName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTFAgentName(){
		if(this.TFAgentName == null){
			this.TFAgentName=new JTextField();
			this.TFAgentName.setBounds(new Rectangle(65, 65, 136, 25));
			this.TFAgentName.setText("MV Ship");
		}
		return this.TFAgentName;
	}

	/**
	 * This method initializes TFAgentX
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTFAgentX(){
		if(this.TFAgentX == null){
			this.TFAgentX=new JTextField();
			this.TFAgentX.setBounds(new Rectangle(65, 95, 46, 25));
			this.TFAgentX.setText("2");
		}
		return this.TFAgentX;
	}

	/**
	 * This method initializes TFAgentY
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTFAgentY(){
		if(this.TFAgentY == null){
			this.TFAgentY=new JTextField();
			this.TFAgentY.setBounds(new Rectangle(65, 125, 46, 25));
			this.TFAgentY.setText("1");
		}
		return this.TFAgentY;
	}

	/**
	 * This method initializes TFAgentZ
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTFAgentZ(){
		if(this.TFAgentZ == null){
			this.TFAgentZ=new JTextField();
			this.TFAgentZ.setBounds(new Rectangle(65, 155, 46, 25));
			this.TFAgentZ.setText("1");
		}
		return this.TFAgentZ;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(){
		this.setSize(653,511);
		this.setClosable(true);

		this.setMaximizable(true);
		this.setContentPane(this.getJContentPane());
		this.setTitle("ControlGUI");
		this.addInternalFrameListener(new InternalFrameAdapter(){
			@Override
			public void internalFrameClosing(InternalFrameEvent e){
				ControlGUI.this.shutDown();
			}
		});

//		new setConsole(this.getSystemConsole());
	}

	void shutDown(){
		GuiEvent ge=new GuiEvent(this, -1);
		this.myAgent.postGuiEvent(ge);
	}

	/**
	 * This method initializes console	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextArea getConsole(){
		if(this.console == null){
			this.console=new JTextArea();
			this.console.setText("");
			this.console.setSize(new Dimension(1000,112));
			this.console.setEditable(false);
			this.console.setAutoscrolls(true);
		}
		return this.console;
	}

	/**
	 * @param content
	 */
	public void writeLogMsg(final String content){
		Runnable addIt=new Runnable(){
			public void run(){
				Document doc=ControlGUI.this.getConsole().getDocument();
				try{
					doc.insertString(doc.getLength(),content,null);
				}catch(BadLocationException ex){
					ex.printStackTrace();
				}
			}
		};

		SwingUtilities.invokeLater(addIt);
	}

	/**
	 * @param content
	 */
	public void printOntRep(final String content){
		Runnable addIt=new Runnable(){
			public void run(){
				Document doc=ControlGUI.this.getOntRep().getDocument();
				try{
					doc.remove(0,doc.getLength());
					doc.insertString(0,content + "\n",null);
				}catch(BadLocationException ex){
					ex.printStackTrace();
				}
			}
		};
		SwingUtilities.invokeLater(addIt);
	}

	/**
	 * This method initializes consoleScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getConsoleScrollPane(){
		if(this.consoleScrollPane == null){
			this.consoleScrollPane=new JScrollPane();
			this.consoleScrollPane.setBounds(new Rectangle(5, 351, 429, 120));
			this.consoleScrollPane.setViewportView(this.getConsole());
		}
		return this.consoleScrollPane;
	}

	/**
	 * This method initializes AgentTreeScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getAgentTreeScrollPane(){
		if(this.AgentTreeScrollPane == null){
			AgentTree AT=this.getAgentTree();

			this.AgentTreeScrollPane=new JScrollPane(AT.tree);
			this.AgentTreeScrollPane.setBounds(new Rectangle(239,7,194,198));
		}
		return this.AgentTreeScrollPane;
	}

	private AgentTree getAgentTree(){
		if(this.AT == null){
			this.AT=new AgentTree();
			this.AT.refreshLocalPlatformName("Contmas");
			this.AT.addContainerNode("contmas",null);
			ControlGUI.expandTree(this.AT.tree);
		}
		return this.AT;
	}

	public void updateAgentTree(List newAgents){
		AgentTree agentTree=this.getAgentTree();
		Iterator<AID> agentIter=newAgents.iterator();
		while(agentIter.hasNext()){
			AID curAgent=agentIter.next();
			agentTree.addAgentNode(curAgent.getLocalName(),"","contmas");
		}
		ControlGUI.expandTree(agentTree.tree);
	}

	public static JTree expandTree(JTree inputTree){
		for(Integer i=0;i < inputTree.getRowCount();i++){
			inputTree.expandRow(i);
		}
		return inputTree;
	}

	/**
	 * This method initializes systemConsoleScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSystemConsoleScrollPane(){
		if(this.systemConsoleScrollPane == null){
			this.systemConsoleScrollPane=new JScrollPane();
			this.systemConsoleScrollPane.setBounds(new Rectangle(583, 365, 26, 31));
			this.systemConsoleScrollPane.setViewportView(this.getSystemConsole());
		}
		return this.systemConsoleScrollPane;
	}

	/**
	 * This method initializes systemConsole	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getSystemConsole(){
		if(this.systemConsole == null){
			this.systemConsole=new JTextArea();
			this.systemConsole.setEditable(false);
		}
		return this.systemConsole;
	}

	/**
	 * This method initializes ButtonGetOntRep	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButtonGetOntRep(){
		if(this.ButtonGetOntRep == null){
			this.ButtonGetOntRep=new JButton();
			this.ButtonGetOntRep.setBounds(new Rectangle(437,7,202,23));
			this.ButtonGetOntRep.setText("> Get ontologyRepresentation");
			this.ButtonGetOntRep.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					TreePath paths[];
					paths=ControlGUI.this.AT.tree.getSelectionPaths();
					if(paths != null){
						for(int i=0;i < paths.length;i++){
							Node now=(Node) (paths[i].getLastPathComponent());
							if(now instanceof AgentNode){
								GuiEvent ge=new GuiEvent(this,2);
								ge.addParameter(((AgentNode) now).getName());
								ControlGUI.this.myAgent.postGuiEvent(ge);
							}
						}
					}
				}
			});
		}
		return this.ButtonGetOntRep;
	}

	/**
	 * This method initializes ontRepScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getOntRepScrollPane(){
		if(this.ontRepScrollPane == null){
			this.ontRepScrollPane=new JScrollPane();
			this.ontRepScrollPane.setBounds(new Rectangle(436,88,203,115));
			this.ontRepScrollPane.setViewportView(this.getOntRep());
		}
		return this.ontRepScrollPane;
	}

	/**
	 * This method initializes ontRep	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getOntRep(){
		if(this.ontRep == null){
			this.ontRep=new JTextArea();
			this.ontRep.setEditable(false);
			ontRep.setTabSize(2);
		}
		return this.ontRep;
	}

	/**
	 * This method initializes loadListScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getLoadListScrollPane(){
		if(loadListScrollPane == null){
			loadListScrollPane=new JScrollPane();
			loadListScrollPane.setBounds(new Rectangle(437,209,201,113));
			loadListScrollPane.setViewportView(getLoadList());
		}
		return loadListScrollPane;
	}

	/**
	 * This method initializes loadList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getLoadList(){
		if(loadList == null){
			loadList=new JList();
			loadList.setModel(new DefaultListModel());
		}
		return loadList;
	}

	/**
	 * @param localName
	 */
	public void setOntRepAgent(String localName){
		this.ontRepAgentName.setText(localName);
	}

	/**
	 * @param containerList
	 */
	public void setContainerList(ListModel containerList){
		this.getLoadList().setModel(containerList);

	}

	/**
	 * This method initializes populateCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getPopulateCheckBox(){
		if(populateCheckBox == null){
			populateCheckBox=new JCheckBox();
			populateCheckBox.setBounds(new Rectangle(145, 147, 91, 21));
			populateCheckBox.setSelected(true);
			populateCheckBox.setText("populate");
		}
		return populateCheckBox;
	}

	/**
	 * This method initializes randomizeCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getRandomizeCheckBox(){
		if(randomizeCheckBox == null){
			randomizeCheckBox=new JCheckBox();
			randomizeCheckBox.setBounds(new Rectangle(145, 119, 91, 24));
			randomizeCheckBox.setText("randomize");
			randomizeCheckBox.setEnabled(true);
			randomizeCheckBox.setSelected(false);
		}
		return randomizeCheckBox;
	}

	/**
	 * This method initializes harbourSetupScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getHarbourSetupScrollPane(){
		if(harbourSetupScrollPane == null){
			harbourSetupScrollPane=new JScrollPane();
			harbourSetupScrollPane.setBounds(new Rectangle(239, 206, 195, 141));
			harbourSetupScrollPane.setViewportView(getHarbourSetupTree());
		}
		return harbourSetupScrollPane;
	}

	/**
	 * This method initializes HarbourSetupTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getHarbourSetupTree(){
		if(HarbourSetupTree == null){
			HarbourSetupTree=new JTree();
			HarbourSetupTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
		}
		return HarbourSetupTree;
	}

	/**
	 * @param root
	 */
	public void displayHarbourLayout(final TreeNode root){
		Runnable addIt=new Runnable(){
			public void run(){
				JTree domainTree=getHarbourSetupTree();
				domainTree.setModel(new DefaultTreeModel(root));
				ControlGUI.expandTree(domainTree);
			}
		};
		SwingUtilities.invokeLater(addIt);		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0){
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method initializes agentType	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getAgentType(){
		if(agentType == null){
			agentType=new JComboBox();
			agentType.setBounds(new Rectangle(66, 32, 134, 22));
			DefaultComboBoxModel modl=new DefaultComboBoxModel();
			modl.addElement(new AgentClassElement(Ship.class));
			modl.addElement(new AgentClassElement(Crane.class));
			modl.addElement(new AgentClassElement(Apron.class));
			modl.addElement(new AgentClassElement(StraddleCarrier.class));
			modl.addElement(new AgentClassElement(Yard.class));
			agentType.setModel(modl);
		}
		return agentType;
	}

	/**
	 * This method initializes habitatList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getHabitatList(){
		if(habitatList == null){
			habitatList=new JList();
			habitatList.setBounds(new Rectangle(5, 217, 175, 24));
			habitatList.setModel(new DefaultListModel());
		}
		return habitatList;
	}

	/**
	 * This method initializes capabilitiesScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getCapabilitiesScrollPane(){
		if(capabilitiesScrollPane == null){
			capabilitiesScrollPane=new JScrollPane();
			capabilitiesScrollPane.setBounds(new Rectangle(5, 244, 174, 75));
			capabilitiesScrollPane.setViewportView(getCapabilitiesList());
		}
		return capabilitiesScrollPane;
	}

	/**
	 * This method initializes capabilitiesList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getCapabilitiesList(){
		if(capabilitiesList == null){
			capabilitiesList=new JList();
			capabilitiesList.setModel(new DefaultListModel());
		}
		return capabilitiesList;
	}

	/**
	 * This method initializes ButtonSetHabitat	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButtonSetHabitat(){
		if(ButtonSetHabitat == null){
			ButtonSetHabitat=new JButton();
			ButtonSetHabitat.setBounds(new Rectangle(182, 217, 55, 24));
			ButtonSetHabitat.setHorizontalTextPosition(SwingConstants.LEFT);
			ButtonSetHabitat.setPreferredSize(new Dimension(53, 50));
			ButtonSetHabitat.setHorizontalAlignment(SwingConstants.LEFT);
			ButtonSetHabitat.setText("Set");
			ButtonSetHabitat.addActionListener(new java.awt.event.ActionListener(){
				public void actionPerformed(java.awt.event.ActionEvent e){
					TreePath paths[];
					paths=getHarbourSetupTree().getSelectionPaths();
					if(paths != null){
						for(int i=0;i < paths.length;i++){
							MutableTreeNode now=(MutableTreeNode) (paths[i].getLastPathComponent());
							if(now instanceof MutableTreeNode){
								((DefaultListModel) getHabitatList().getModel()).removeAllElements();
								((DefaultListModel) getHabitatList().getModel()).addElement(now);
							}
						}
					}
				}
			});
		}
		return ButtonSetHabitat;
	}

	/**
	 * This method initializes ButtonAddCapability	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButtonAddCapability(){
		if(ButtonAddCapability == null){
			ButtonAddCapability=new JButton();
			ButtonAddCapability.setBounds(new Rectangle(181, 245, 57, 23));
			ButtonAddCapability.setText("Add");
			ButtonAddCapability.addActionListener(new java.awt.event.ActionListener(){
				public void actionPerformed(java.awt.event.ActionEvent e){
					TreePath paths[];
					paths=getHarbourSetupTree().getSelectionPaths();
					if(paths != null){
						for(int i=0;i < paths.length;i++){
							MutableTreeNode now=(MutableTreeNode) (paths[i].getLastPathComponent());
							if(now instanceof MutableTreeNode){
								((DefaultListModel) getCapabilitiesList().getModel()).addElement(now);
							}
						}
					}
				}
			});
		}
		return ButtonAddCapability;
	}

	/**
	 * This method initializes ButtonCapabilityRemove	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButtonCapabilityRemove(){
		if(ButtonCapabilityRemove == null){
			ButtonCapabilityRemove=new JButton();
			ButtonCapabilityRemove.setBounds(new Rectangle(180, 271, 60, 22));
			ButtonCapabilityRemove.setText("Rmv");
			ButtonCapabilityRemove.addActionListener(new java.awt.event.ActionListener(){
				public void actionPerformed(java.awt.event.ActionEvent e){
					Object[] paths;
					paths= getCapabilitiesList().getSelectedValues();
					if(paths != null){
						for(int i=0;i < paths.length;i++){
							((DefaultListModel) getCapabilitiesList().getModel()).removeElement(paths[i]);
						}
					}
				}
			});
		}
		return ButtonCapabilityRemove;
	}

} //  @jve:decl-index=0:visual-constraint="30,15"
