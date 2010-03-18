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

import jade.gui.AgentTree;
import jade.gui.GuiEvent;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.TreePath;

import contmas.agents.ControlGUIAgent;
import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

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
	private JLabel jLabel=null;
	private JLabel jLabel1=null;
	private JTextField TFShipLength=null;
	private JDesktopPane canvas=null;
	private JTextArea console = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane AgentTreeScrollPane = null;
	private AgentTree AT = null;

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
			GuiEvent ge=new GuiEvent(this,1);
			ge.addParameter(this.TFShipName.getText());
			ge.addParameter(this.TFShipX.getText());
			ge.addParameter(this.TFShipY.getText());
			ge.addParameter(this.TFShipZ.getText());
			ge.addParameter(this.TFShipLength.getText());
			this.myAgent.postGuiEvent(ge);
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
			this.ButtonCreateShip.addActionListener(this);
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
			this.jLabel1=new JLabel();
			this.jLabel1.setText("Length");
			this.jLabel1.setBounds(new Rectangle(5,158,55,16));
			this.jLabel=new JLabel();
			this.jLabel.setFont(new Font("Dialog",Font.BOLD,14));
			this.jLabel.setBounds(new Rectangle(5,5,196,15));
			this.jLabel.setText("Create new Ship");
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
			this.jContentPane.add(this.getTFShipX(),null);
			this.jContentPane.add(this.getButtonCreateShip(),null);
			this.jContentPane.add(this.ShipLabelX,null);
			this.jContentPane.add(this.ShipLabelY,null);
			this.jContentPane.add(this.getTFShipY(),null);
			this.jContentPane.add(this.getTFShipZ(),null);
			this.jContentPane.add(this.ShipLabelZ,null);
			this.jContentPane.add(this.ShipLabelName,null);
			this.jContentPane.add(this.getTFShipName(),null);
			this.jContentPane.add(this.jLabel,null);
			this.jContentPane.add(this.jLabel1,null);
			this.jContentPane.add(this.getTFShipLength(),null);
			jContentPane.add(getJScrollPane(), null);
			jContentPane.add(getAgentTreeScrollPane(), null);
			this.jContentPane.add(this.jLabel1,null);
			this.jContentPane.add(this.jLabel,null);
			this.jContentPane.add(this.ShipLabelName,null);
			this.jContentPane.add(this.ShipLabelZ,null);
			this.jContentPane.add(this.ShipLabelY,null);
			this.jContentPane.add(this.ShipLabelX,null);
			this.jContentPane.add(this.jLabel1,null);
			this.jContentPane.add(this.jLabel,null);
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
		this.setSize(449, 361);
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
		if(console == null){
			console=new JTextArea();
			console.setText("");
			console.setSize(new Dimension(1000, 112));
			console.setEditable(false);
//			console.setAutoscrolls(true);
		}
		return console;
	}

	/**
	 * @param content
	 */
	public void writeLogMsg(final String content){
	    Runnable addIt = new Runnable() {  
	        public void run() {
//	        	getJTextPane().setText(content);
	    		Document doc = getConsole().getDocument();
	    		try {
	    			doc.insertString(doc.getLength(), content, null);
//	    			getJTextPane().setCaretPosition(doc.getLength());
	    		} catch (BadLocationException ex) {
	    			ex.printStackTrace();
	    		}
	        }  
	      };

	      SwingUtilities.invokeLater(addIt);
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane(){
		if(jScrollPane == null){
			jScrollPane=new JScrollPane();
			jScrollPane.setBounds(new Rectangle(5, 208, 429, 115));
			jScrollPane.setViewportView(getConsole());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes AgentTreeScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getAgentTreeScrollPane(){
		if(AgentTreeScrollPane == null){
			AgentTree AT=getAgentTree();

			AgentTreeScrollPane=new JScrollPane(AT.tree);
			AgentTreeScrollPane.setBounds(new Rectangle(233, 6, 194, 191));
		}
		return AgentTreeScrollPane;
	}
	
	private AgentTree getAgentTree(){
	if(AT == null){
		AT=new AgentTree();
		AT.addContainerNode("contmas",null);
		AT.addAgentNode("bla","","contmas");
		AT.refreshLocalPlatformName("Contmas");
		/*
		TreePath test=TreePath.this;
		test.;
		AT.tree.setExpandsSelectedPaths(arg0);
		AT.tree.getModel().
		AT.tree.expandPath(arg0);
		*/
	}
	return AT ;
}

} //  @jve:decl-index=0:visual-constraint="30,15"
