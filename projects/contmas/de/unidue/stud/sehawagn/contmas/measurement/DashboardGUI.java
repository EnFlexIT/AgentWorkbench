/**
 * @author Hanno - Felix Wagner, 08.06.2010
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
package contmas.de.unidue.stud.sehawagn.contmas.measurement;

import jade.core.AID;
import jade.gui.GuiEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import contmas.agents.MonitorAgent;
import contmas.main.ControlGUI;
import contmas.main.MonitorGUI;

import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JTextPane;
import javax.swing.JButton;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class DashboardGUI extends JInternalFrame implements ActionListener{

	
	private JDesktopPane canvas;
	private DashboardAgent myAgent;
	private JPanel layoutPanel = null;
	private JScrollPane exampleOutputScroll = null;
	private JTextArea exampleOutput = null;
	private JButton exampleButton = null;



	public DashboardGUI(DashboardAgent dashboardAgent,JDesktopPane canvas){
		initialize();
		myAgent=dashboardAgent;
		displayOn(canvas);
	}
	
	public void displayOn(JDesktopPane canvas){
		this.canvas=canvas;
		this.canvas.add(this);
		this.grabFocus();
	}

	private void initialize(){
        this.setSize(new Dimension(306, 198));
        this.setContentPane(getLayoutPanel());
        this.setTitle("Metrics Measurement Dashboard");
        this.setResizable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setClosable(true);
        this.setVisible(true);
		this.addInternalFrameListener(new InternalFrameAdapter(){
			@Override
			public void internalFrameClosing(InternalFrameEvent e){
				DashboardGUI.this.shutDown();
			}
		});
	}
	
	private void shutDown(){
		GuiEvent ge=new GuiEvent(this, DashboardAgent.SHUT_DOWN_EVENT);
		this.myAgent.postGuiEvent(ge);
	}
	
	public void exampleAction(String content){
		GuiEvent ge=new GuiEvent(this,DashboardAgent.EXAMPLEBUTTON_EVENT);
		ge.addParameter(content);
		this.myAgent.postGuiEvent(ge);

	}


	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0){
		// TODO Auto-generated method stub

	}

	/**
	 * This method initializes layoutPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLayoutPanel(){
		if(layoutPanel == null){
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			layoutPanel=new JPanel();
			layoutPanel.setLayout(new GridBagLayout());
			layoutPanel.add(getExampleButton(), gridBagConstraints);
			layoutPanel.add(getExampleOutputScroll(), gridBagConstraints1);
		}
		return layoutPanel;
	}

	/**
	 * This method initializes exampleOutputScroll	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getExampleOutputScroll(){
		if(exampleOutputScroll == null){
			exampleOutputScroll=new JScrollPane();
			exampleOutputScroll.setViewportView(getExampleOutput());
			exampleOutputScroll.setAutoscrolls(true);

		}
		return exampleOutputScroll;
	}

	/**
	 * This method initializes exampleOutput	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextArea getExampleOutput(){
		if(exampleOutput == null){
			exampleOutput=new JTextArea();
			exampleOutput.setText("");
			exampleOutput.setSize(new Dimension(1000,112));
			exampleOutput.setEditable(false);
			exampleOutput.setAutoscrolls(true);
		}
		return exampleOutput;
	}

	/**
	 * This method initializes exampleButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getExampleButton(){
		if(exampleButton == null){
			exampleButton=new JButton();
			exampleButton.setText("ExampleButton");
			exampleButton.addActionListener(new java.awt.event.ActionListener(){
				public void actionPerformed(java.awt.event.ActionEvent e){
					exampleAction(getExampleOutput().getText());
				}

			});
		}
		return exampleButton;
	}

	/**
	 * @param text
	 */
	public void echoApp(final String text){
		Runnable addIt=new Runnable(){
			public void run(){
				Document doc=getExampleOutput().getDocument();
				try{
					doc.insertString(doc.getLength(),text+"\n",null);
				}catch(BadLocationException ex){
					ex.printStackTrace();
				}
			}
		};
		SwingUtilities.invokeLater(addIt);

	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
