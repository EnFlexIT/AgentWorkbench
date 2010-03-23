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
import javax.swing.tree.TreePath;

import contmas.agents.ControlGUIAgent;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ControlGUI extends JInternalFrame implements ActionListener{

	private static final long serialVersionUID=1L;
	private JPanel jContentPane=null;
	private JButton ButtonCreateShip=null;
	private JLabel ShipLabelX=null;
	private JTextField TFShipX=null;
	private JLabel ShipLabelY=null;
	private JTextField TFShipY=null;
	private JTextField TFShipZ=null;
	private JLabel ShipLabelZ=null;
	private JLabel ShipLabelName=null;
	private JTextField TFShipName=null;
	private ControlGUIAgent myAgent;
	private JLabel Heading=null;
	private JLabel ShipLabelLength=null;
	private JTextField TFShipLength=null;
	private JDesktopPane canvas=null;
	private JTextArea console=null;
	private JScrollPane consoleScrollPane=null;
	private JScrollPane AgentTreeScrollPane=null;
	private AgentTree AT=null;
	private JScrollPane systemConsoleScrollPane=null;
	private JTextArea systemConsole=null;
	private JButton ButtonGetOntRep=null;
	private JButton ButtonSetOntRep=null;
	private JScrollPane ontRepScrollPane=null;
	private JTextArea ontRep=null;

	/**
	 * This is the default constructor
	 */
	public ControlGUI(ControlGUIAgent a){
		super();
		this.myAgent=a;
		this.initialize();
	}

	public void actionPerformed(ActionEvent ae){
		if(ae.getSource() == this.ButtonCreateShip){

		}
	}

	public void displayOn(JDesktopPane canvas){
		this.canvas=canvas;
		this.canvas.add(this);
	}

	/**
	 * This method initializes ButtonCreateShip
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getButtonCreateShip(){
		if(this.ButtonCreateShip == null){
			this.ButtonCreateShip=new JButton();

			this.ButtonCreateShip.setBounds(new Rectangle(5,185,196,20));
			this.ButtonCreateShip.setText("Start ShipAgent");

			ButtonCreateShip.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					GuiEvent ge=new GuiEvent(this,1);
					ge.addParameter(TFShipName.getText());
					ge.addParameter(TFShipX.getText());
					ge.addParameter(TFShipY.getText());
					ge.addParameter(TFShipZ.getText());
					ge.addParameter(TFShipLength.getText());
					myAgent.postGuiEvent(ge);
				}
			});
		}
		return this.ButtonCreateShip;
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
			this.ShipLabelLength=new JLabel();
			this.ShipLabelLength.setText("Length");
			this.ShipLabelLength.setBounds(new Rectangle(5,158,55,16));
			this.Heading=new JLabel();
			this.Heading.setFont(new Font("Dialog",Font.BOLD,14));
			this.Heading.setBounds(new Rectangle(5,5,196,15));
			this.Heading.setText("Create new Ship");
			this.ShipLabelName=new JLabel();
			this.ShipLabelName.setText("Name");
			this.ShipLabelName.setBounds(new Rectangle(5,38,55,16));
			this.ShipLabelZ=new JLabel();
			this.ShipLabelZ.setText("Size Z");
			this.ShipLabelZ.setBounds(new Rectangle(5,128,55,16));
			this.ShipLabelY=new JLabel();
			this.ShipLabelY.setText("Size Y");
			this.ShipLabelY.setBounds(new Rectangle(5,98,55,16));
			this.ShipLabelX=new JLabel();
			this.ShipLabelX.setText("Size X");
			this.ShipLabelX.setBounds(new Rectangle(5,68,55,16));
			this.jContentPane=new JPanel();
			this.jContentPane.setLayout(null);
			jContentPane.add(getTFShipName(),null);
			this.jContentPane.add(this.getTFShipX(),null);
			this.jContentPane.add(this.ShipLabelX,null);
			this.jContentPane.add(this.ShipLabelY,null);
			this.jContentPane.add(this.getTFShipY(),null);
			this.jContentPane.add(this.getTFShipZ(),null);
			this.jContentPane.add(this.ShipLabelZ,null);
			this.jContentPane.add(this.ShipLabelName,null);
			this.jContentPane.add(this.Heading,null);
			this.jContentPane.add(this.ShipLabelLength,null);
			this.jContentPane.add(this.getTFShipLength(),null);
			jContentPane.add(getButtonCreateShip(),null);
			this.jContentPane.add(this.getConsoleScrollPane(),null);
			this.jContentPane.add(this.getAgentTreeScrollPane(),null);
			this.jContentPane.add(this.getSystemConsoleScrollPane(),null);
			jContentPane.add(getButtonGetOntRep(),null);
			jContentPane.add(getButtonSetOntRep(),null);
			jContentPane.add(getOntRepScrollPane(),null);
			this.jContentPane.add(this.ShipLabelLength,null);
			this.jContentPane.add(this.Heading,null);
			this.jContentPane.add(this.ShipLabelName,null);
			this.jContentPane.add(this.ShipLabelZ,null);
			this.jContentPane.add(this.ShipLabelY,null);
			this.jContentPane.add(this.ShipLabelX,null);
			this.jContentPane.add(this.ShipLabelLength,null);
			this.jContentPane.add(this.Heading,null);
			this.jContentPane.add(this.ShipLabelName,null);
			this.jContentPane.add(this.ShipLabelZ,null);
			this.jContentPane.add(this.ShipLabelX,null);
			this.jContentPane.add(this.ShipLabelY,null);
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
			this.TFShipLength.setBounds(new Rectangle(65,155,46,25));
			this.TFShipLength.setText("120.5");
		}
		return this.TFShipLength;
	}

	/**
	 * This method initializes TFShipName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTFShipName(){
		if(this.TFShipName == null){
			this.TFShipName=new JTextField();
			this.TFShipName.setBounds(new Rectangle(65,35,136,25));
			this.TFShipName.setText("MV Ship");
		}
		return this.TFShipName;
	}

	/**
	 * This method initializes TFShipX
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTFShipX(){
		if(this.TFShipX == null){
			this.TFShipX=new JTextField();
			this.TFShipX.setBounds(new Rectangle(65,65,46,25));
			this.TFShipX.setText("2");
		}
		return this.TFShipX;
	}

	/**
	 * This method initializes TFShipY
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTFShipY(){
		if(this.TFShipY == null){
			this.TFShipY=new JTextField();
			this.TFShipY.setBounds(new Rectangle(65,95,46,25));
			this.TFShipY.setText("1");
		}
		return this.TFShipY;
	}

	/**
	 * This method initializes TFShipZ
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTFShipZ(){
		if(this.TFShipZ == null){
			this.TFShipZ=new JTextField();
			this.TFShipZ.setBounds(new Rectangle(65,125,46,25));
			this.TFShipZ.setText("1");
		}
		return this.TFShipZ;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(){
		this.setSize(777,511);
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
		/*
		 * this.addWindowListener(new WindowAdapter() { public void
		 * windowClosing(WindowEvent e) { shutDown(); } });
		 */
//		new setConsole(this.getSystemConsole());
	}

	void shutDown(){
		// -----------------  Control the closing of this gui
		/*
		 * int
		 * rep=JOptionPane.showConfirmDialog(this,"Wirklich schlieﬂen?",this.
		 * myAgent.getLocalName(),JOptionPane.YES_NO_CANCEL_OPTION); if(rep ==
		 * JOptionPane.YES_OPTION){
		 */
		GuiEvent ge=new GuiEvent(this, -1);
		this.myAgent.postGuiEvent(ge);
		//}
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
//			console.setAutoscrolls(true);
		}
		return this.console;
	}

	/**
	 * @param content
	 */
	public void writeLogMsg(final String content){
		Runnable addIt=new Runnable(){
			public void run(){
//	        	getJTextPane().setText(content);
				Document doc=ControlGUI.this.getConsole().getDocument();
				try{
					doc.insertString(doc.getLength(),content,null);
//	    			getJTextPane().setCaretPosition(doc.getLength());
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
	public void printMessageContent(final String content){
		Runnable addIt=new Runnable(){
			public void run(){
				Document doc=ControlGUI.this.getOntRep().getDocument();
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
	 * This method initializes consoleScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getConsoleScrollPane(){
		if(this.consoleScrollPane == null){
			this.consoleScrollPane=new JScrollPane();
			this.consoleScrollPane.setBounds(new Rectangle(5,208,429,115));
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
		this.getAgentTree();
		Iterator<AID> agentIter=newAgents.iterator();
		while(agentIter.hasNext()){
			AID curAgent=agentIter.next();
			this.AT.addAgentNode(curAgent.getLocalName(),"","contmas");
		}
		ControlGUI.expandTree(this.AT.tree);
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
			this.systemConsoleScrollPane.setBounds(new Rectangle(9,335,422,135));
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
			systemConsole.setEditable(false);
		}
		return this.systemConsole;
	}

	/**
	 * This method initializes ButtonGetOntRep	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButtonGetOntRep(){
		if(ButtonGetOntRep == null){
			ButtonGetOntRep=new JButton();
			ButtonGetOntRep.setBounds(new Rectangle(443,10,314,23));
			ButtonGetOntRep.setText("> Get ontologyRepresentation");
			ButtonGetOntRep.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					TreePath paths[];
					paths=AT.tree.getSelectionPaths();
					if(paths != null){
						for(int i=0;i < paths.length;i++){
							Node now=(Node) (paths[i].getLastPathComponent());
							if(now instanceof AgentNode){
								GuiEvent ge=new GuiEvent(this,2);
								ge.addParameter(((AgentNode) now).getName());
								myAgent.postGuiEvent(ge);
							}
						}
					}
				}
			});
		}
		return ButtonGetOntRep;
	}

	/**
	 * This method initializes ButtonSetOntRep	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButtonSetOntRep(){
		if(ButtonSetOntRep == null){
			ButtonSetOntRep=new JButton();
			ButtonSetOntRep.setBounds(new Rectangle(442,239,309,26));
			ButtonSetOntRep.setText("< Set ontologyRepresentation");
		}
		return ButtonSetOntRep;
	}

	/**
	 * This method initializes ontRepScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getOntRepScrollPane(){
		if(ontRepScrollPane == null){
			ontRepScrollPane=new JScrollPane();
			ontRepScrollPane.setBounds(new Rectangle(447,40,305,193));
			ontRepScrollPane.setViewportView(getOntRep());
		}
		return ontRepScrollPane;
	}

	/**
	 * This method initializes ontRep	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getOntRep(){
		if(ontRep == null){
			ontRep=new JTextArea();
			ontRep.setEditable(false);
		}
		return ontRep;
	}

} //  @jve:decl-index=0:visual-constraint="30,15"
