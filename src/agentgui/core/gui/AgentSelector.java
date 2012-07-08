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
package agentgui.core.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.components.JListClassSearcher;
import agentgui.core.jade.ClassSearcher;

/**
 * The Class AgentSelector provides a GUI in order to select an agent 
 * known in the current JVM
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AgentSelector extends JDialog implements ActionListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Path image. */
	private final String PathImage = Application.getGlobalInfo().PathImageIntern();  //  @jve:decl-index=0:
	private ImageIcon imageIcon = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUI.png") );
	private Image image = imageIcon.getImage();
	
	/** The selected agent classes. */
	private Object[] selectedAgentClasses = null; 
	private boolean canceled = false;
	
	private JPanel jContentPane = null;
	private JPanel jPanelBottom = null;
	private JListClassSearcher jListAgents = null;
	private JLabel jLabelSearchCaption = null;
	private JTextField jTextFieldSearch = null;
	private JButton jButtonOk = null;
	private JButton jButtonCancel = null;

	
	/**
	 * Instantiates a new agent selector.
	 */
	public AgentSelector() {
		super(Application.getMainWindow());
		initialize();
	}
	
	/**
	 * Instantiates a new agent selector.
	 *
	 * @param owner the owner
	 */
	public AgentSelector(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this.
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(721, 500);
		this.setTitle("Auswahl - Agenten");
		this.setIconImage( image );
		this.setModal(true);
		this.setContentPane(getJContentPane());
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				canceled = true;
				setVisible(false);
			}
		});
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);	
	 
	    // --- Übersetzungen einstellen -----------------------------
		this.setTitle(Language.translate("Auswahl - Agenten"));
		jLabelSearchCaption.setText(Language.translate("Suche"));
		jButtonOk.setText(Language.translate("Hinzufügen"));
		jButtonCancel.setText(Language.translate("Abbrechen"));
	}

	/**
	 * This method initializes jContentPane.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.insets = new Insets(20, 20, 0, 0);
			gridBagConstraints21.anchor = GridBagConstraints.WEST;
			gridBagConstraints21.gridy = 0;
			
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.insets = new Insets(5, 20, 0, 20);
			gridBagConstraints11.gridx = 0;
			
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.insets = new Insets(10, 20, 25, 20);
			gridBagConstraints3.gridy = 3;
			
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.insets = new Insets(20, 20, 10, 20);
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridx = 0;
			
			jLabelSearchCaption = new JLabel();
			jLabelSearchCaption.setText("Suche");
			jLabelSearchCaption.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(jLabelSearchCaption, gridBagConstraints21);
			jContentPane.add(getJTextFieldSearch(), gridBagConstraints11);
			jContentPane.add(getJListAgents(), gridBagConstraints);
			jContentPane.add(getJPanelBottom(), gridBagConstraints3);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTextFieldSearch.
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldSearch() {
		if (jTextFieldSearch == null) {
			jTextFieldSearch = new JTextField();
			jTextFieldSearch.setPreferredSize(new Dimension(250, 26));
			jTextFieldSearch.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(java.awt.event.KeyEvent e) {
					filterAgentList( jTextFieldSearch.getText());
				}
			});
		}
		return jTextFieldSearch;
	}
	
	/**
	 * This method initializes jListAgents.
	 *
	 * @return javax.swing.JList
	 */
	private JListClassSearcher getJListAgents() {
		if (jListAgents == null) {
			jListAgents = new JListClassSearcher(ClassSearcher.CLASSES_AGENTS);
			jListAgents.jListLoading.addMouseListener( new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if (me.getClickCount() == 2 ) {
						jButtonOk.doClick();	
					}
				}
			});
		}
		return jListAgents;
	}
	
	/**
	 * This will filter the list of Agents depending on
	 * the content of the Input-Parameter.
	 *
	 * @param filter4 the filter4
	 */
	private void filterAgentList(String filter4) {
		jListAgents.setModelFiltered(filter4);
	}
	
	/**
	 * This method initializes jButtonOk.
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton();
			jButtonOk.setPreferredSize(new Dimension(120, 26));
			jButtonOk.setForeground(new Color(0, 102, 0));
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.setText("Hinzufügen");
			jButtonOk.setActionCommand("Ok");
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}

	/**
	 * This method initializes jButtonCancel.
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setPreferredSize(new Dimension(120, 26));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setText("Abbrechen");
			jButtonCancel.setActionCommand("Cancel");
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	/**
	 * This method initializes jPanelBottom.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.insets = new Insets(0, 40, 0, 0);
			gridBagConstraints2.weightx = 0.0;
			gridBagConstraints2.gridy = -1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = -1;
			gridBagConstraints1.anchor = GridBagConstraints.CENTER;
			gridBagConstraints1.fill = GridBagConstraints.NONE;
			gridBagConstraints1.insets = new Insets(0, 0, 0, 40);
			gridBagConstraints1.gridy = -1;
			jPanelBottom = new JPanel();
			jPanelBottom.setLayout(new GridBagLayout());
			jPanelBottom.add(getJButtonOk(), gridBagConstraints1);
			jPanelBottom.add(getJButtonCancel(), gridBagConstraints2);
		}
		return jPanelBottom;
	}

	/**
	 * Checks if is canceled.
	 *
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	
	/**
	 * Gets the selected agent classes.
	 *
	 * @return the selectedAgentClasses
	 */
	public Object[] getSelectedAgentClasses() {
		return selectedAgentClasses;
	}
	
	/**
	 * Saves the selected Agents in the ResultArray.
	 *
	 * @return true, if successful
	 */
	private boolean okAction() {

		if (jListAgents.getSelectedValues().length==0) {
			selectedAgentClasses = null;
			return false;
		} else {
			selectedAgentClasses = jListAgents.getSelectedValues();
			return true;
		}
	}
	
	
	/**
	 * This manages the incomming Action-Calls.
	 *
	 * @param ae the ae
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		Object Trigger = ae.getSource();
		// --- Fallunterscheidung 'Auslöser' -----------------------------
		if ( Trigger == jButtonOk ) {
			canceled = false;
			if (okAction()==false) return;
			this.setVisible(false);
		} else if ( Trigger == jButtonCancel ) {
			canceled = true;
			this.setVisible(false);
		} else {
			
		};
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
