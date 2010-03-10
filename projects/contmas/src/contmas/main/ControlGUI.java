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

import jade.gui.GuiEvent;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import contmas.agents.ControlGUIAgent;
import java.awt.Dimension;

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
	
	public JDesktopPane getCanvas(){
		return canvas;
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

	/**
	 * This method initializes ButtonCreateShip
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getButtonCreateShip(){
		if(this.ButtonCreateShip == null){
			this.ButtonCreateShip=new JButton();

			this.ButtonCreateShip.setBounds(new Rectangle(5, 185, 196, 20));
			this.ButtonCreateShip.setText("Start ShipAgent");
			this.ButtonCreateShip.addActionListener(this);
		}
		return this.ButtonCreateShip;
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
			jLabel1.setBounds(new Rectangle(5, 158, 55, 16));
			this.jLabel=new JLabel();
			this.jLabel.setFont(new Font("Dialog",Font.BOLD,14));
			jLabel.setBounds(new Rectangle(5, 5, 196, 15));
			this.jLabel.setText("Create new Ship");
			this.ShipLabelName=new JLabel();
			this.ShipLabelName.setText("Name");
			ShipLabelName.setBounds(new Rectangle(5, 38, 55, 16));
			this.ShipLabelZ=new JLabel();
			this.ShipLabelZ.setText("Size Z");
			ShipLabelZ.setBounds(new Rectangle(5, 128, 55, 16));
			this.ShipLabelY=new JLabel();
			this.ShipLabelY.setText("Size Y");
			ShipLabelY.setBounds(new Rectangle(5, 98, 55, 16));
			this.ShipLabelX=new JLabel();
			this.ShipLabelX.setText("Size X");
			ShipLabelX.setBounds(new Rectangle(5, 68, 55, 16));
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
			jContentPane.add(jLabel1, null);
			jContentPane.add(jLabel, null);
			jContentPane.add(ShipLabelName, null);
			jContentPane.add(ShipLabelZ, null);
			jContentPane.add(ShipLabelY, null);
			jContentPane.add(ShipLabelX, null);
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
			this.TFShipLength.setBounds(new Rectangle(65, 155, 46, 25));
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
			this.TFShipName.setBounds(new Rectangle(65, 35, 136, 25));
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
			this.TFShipX.setBounds(new Rectangle(65, 65, 46, 25));
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
			this.TFShipY.setBounds(new Rectangle(65, 95, 46, 25));
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
			this.TFShipZ.setBounds(new Rectangle(65, 125, 46, 25));
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
		this.setSize(216, 243);
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

} //  @jve:decl-index=0:visual-constraint="30,15"
