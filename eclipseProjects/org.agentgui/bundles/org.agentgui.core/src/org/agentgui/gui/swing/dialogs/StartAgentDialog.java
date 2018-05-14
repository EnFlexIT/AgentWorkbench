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
package org.agentgui.gui.swing.dialogs;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.core.gui.AgentSelector;
import de.enflexit.common.classSelection.ClassElement2Display;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JPanel;


/**
 * The StartAgentDialog can be used to start any known Agent.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class StartAgentDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -6796170387263292923L;
	
	private static ArrayList<String> agentClasssReminder = new ArrayList<>();
	
	private boolean isCanceled;
	private String errorText;
	
	private JLabel jLabelAgentName;
	private JTextField jTextFieldAgentName;
	
	private JLabel jLabelgentClass;
	private DefaultComboBoxModel<String> comboBoxModelAgentClass;
	private JComboBox<String> jComboBoxAgentClass;
	private JButton jButtonSelectAgentClass;

	private JLabel jLabelArguments;
	private JTextField jTextFieldArguments;
	
	private JLabel jLabelContainer;
	private DefaultComboBoxModel<String> comboBoxModelContainer;
	private JComboBox<String> jComboBoxContainer;

	private Dimension editFieldDimension = new Dimension(40, 26);
	private JPanel jPanelButtons;
	private JButton jButtonOK;
	private JButton jButtonCancel;
	
	
	/**
	 * Instantiates a new start agent dialog.
	 */
	public StartAgentDialog() {
		this.initilaize();
	}
	/**
	 * Instantiates a new start agent dialog.
	 * @param ownerDialog the owner dialog
	 */
	public StartAgentDialog(Dialog ownerDialog) {
		super(ownerDialog);
		this.initilaize();
	}
	/**
	 * Instantiates a new start agent dialog.
	 * @param ownerFrame the owner frame
	 */
	public StartAgentDialog(Frame ownerFrame) {
		super(ownerFrame);
		this.initilaize();
	}
	
	/**
	 * Initilaize.
	 */
	private void initilaize() {
		
		this.setTitle("Start an Agent");
		this.setModal(true);
		this.registerEscapeKeyStroke();
		this.setSize(600, 230);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelAgentName = new GridBagConstraints();
		gbc_jLabelAgentName.anchor = GridBagConstraints.WEST;
		gbc_jLabelAgentName.insets = new Insets(10, 10, 5, 5);
		gbc_jLabelAgentName.gridx = 0;
		gbc_jLabelAgentName.gridy = 0;
		getContentPane().add(getJLabelAgentName(), gbc_jLabelAgentName);
		GridBagConstraints gbc_jTextFieldAgentName = new GridBagConstraints();
		gbc_jTextFieldAgentName.gridwidth = 2;
		gbc_jTextFieldAgentName.insets = new Insets(10, 5, 5, 10);
		gbc_jTextFieldAgentName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldAgentName.gridx = 1;
		gbc_jTextFieldAgentName.gridy = 0;
		getContentPane().add(getJTextFieldAgentName(), gbc_jTextFieldAgentName);
		GridBagConstraints gbc_jLabelgentClass = new GridBagConstraints();
		gbc_jLabelgentClass.anchor = GridBagConstraints.WEST;
		gbc_jLabelgentClass.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelgentClass.gridx = 0;
		gbc_jLabelgentClass.gridy = 1;
		getContentPane().add(getJLabelgentClass(), gbc_jLabelgentClass);
		GridBagConstraints gbc_jComboBoxAgentClass = new GridBagConstraints();
		gbc_jComboBoxAgentClass.insets = new Insets(0, 5, 5, 5);
		gbc_jComboBoxAgentClass.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxAgentClass.gridx = 1;
		gbc_jComboBoxAgentClass.gridy = 1;
		getContentPane().add(getJComboBoxAgentClass(), gbc_jComboBoxAgentClass);
		GridBagConstraints gbc_jButtonSelectAgentClass = new GridBagConstraints();
		gbc_jButtonSelectAgentClass.insets = new Insets(0, 0, 5, 10);
		gbc_jButtonSelectAgentClass.gridx = 2;
		gbc_jButtonSelectAgentClass.gridy = 1;
		getContentPane().add(getJButtonSelectAgentClass(), gbc_jButtonSelectAgentClass);
		GridBagConstraints gbc_jLabelArguments = new GridBagConstraints();
		gbc_jLabelArguments.anchor = GridBagConstraints.WEST;
		gbc_jLabelArguments.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelArguments.gridx = 0;
		gbc_jLabelArguments.gridy = 2;
		getContentPane().add(getJLabelArguments(), gbc_jLabelArguments);
		GridBagConstraints gbc_jTextFieldArguments = new GridBagConstraints();
		gbc_jTextFieldArguments.gridwidth = 2;
		gbc_jTextFieldArguments.insets = new Insets(0, 5, 5, 10);
		gbc_jTextFieldArguments.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldArguments.gridx = 1;
		gbc_jTextFieldArguments.gridy = 2;
		getContentPane().add(getJTextFieldArguments(), gbc_jTextFieldArguments);
		GridBagConstraints gbc_jLabelContainer = new GridBagConstraints();
		gbc_jLabelContainer.anchor = GridBagConstraints.WEST;
		gbc_jLabelContainer.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelContainer.gridx = 0;
		gbc_jLabelContainer.gridy = 3;
		getContentPane().add(getJLabelContainer(), gbc_jLabelContainer);
		GridBagConstraints gbc_jComboBoxContainer = new GridBagConstraints();
		gbc_jComboBoxContainer.gridwidth = 2;
		gbc_jComboBoxContainer.insets = new Insets(0, 5, 5, 10);
		gbc_jComboBoxContainer.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxContainer.gridx = 1;
		gbc_jComboBoxContainer.gridy = 3;
		getContentPane().add(getJComboBoxContainer(), gbc_jComboBoxContainer);
		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.gridwidth = 3;
		gbc_jPanelButtons.insets = new Insets(10, 10, 10, 10);
		gbc_jPanelButtons.gridx = 0;
		gbc_jPanelButtons.gridy = 4;
		getContentPane().add(getJPanelButtons(), gbc_jPanelButtons);
		
		// --- Set Dialog position ----------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);		
		
	}

    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
            	setCanceled(true);
    			setVisible(false);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
	private JLabel getJLabelAgentName() {
		if (jLabelAgentName == null) {
			jLabelAgentName = new JLabel("Agent Name");
			jLabelAgentName.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelAgentName;
	}
	private JLabel getJLabelgentClass() {
		if (jLabelgentClass == null) {
			jLabelgentClass = new JLabel("Agent Class");
			jLabelgentClass.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelgentClass;
	}
	private JLabel getJLabelArguments() {
		if (jLabelArguments == null) {
			jLabelArguments = new JLabel("Arguments");
			jLabelArguments.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelArguments;
	}
	private JLabel getJLabelContainer() {
		if (jLabelContainer == null) {
			jLabelContainer = new JLabel("Container");
			jLabelContainer.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelContainer;
	}
	private JTextField getJTextFieldAgentName() {
		if (jTextFieldAgentName == null) {
			jTextFieldAgentName = new JTextField();
			jTextFieldAgentName.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldAgentName.setSize(this.editFieldDimension);
		}
		return jTextFieldAgentName;
	}
	private DefaultComboBoxModel<String> getComboBoxModelAgentClass() {
		if (comboBoxModelAgentClass==null) {
			comboBoxModelAgentClass = new DefaultComboBoxModel<>();
			Collections.sort(agentClasssReminder);
			for (String agentClass : agentClasssReminder) {
				comboBoxModelAgentClass.addElement(agentClass);
			}
			
		}
		return comboBoxModelAgentClass;
	}
	private JComboBox<String> getJComboBoxAgentClass() {
		if (jComboBoxAgentClass == null) {
			jComboBoxAgentClass = new JComboBox<>(this.getComboBoxModelAgentClass());
			jComboBoxAgentClass.setEditable(true);
			jComboBoxAgentClass.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxAgentClass.setSize(this.editFieldDimension);
		}
		return jComboBoxAgentClass;
	}
	private JButton getJButtonSelectAgentClass() {
		if (jButtonSelectAgentClass == null) {
			jButtonSelectAgentClass = new JButton("...");
			jButtonSelectAgentClass.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonSelectAgentClass.setSize(new Dimension(26, 26));
			jButtonSelectAgentClass.addActionListener(this);
		}
		return jButtonSelectAgentClass;
	}
	private JTextField getJTextFieldArguments() {
		if (jTextFieldArguments == null) {
			jTextFieldArguments = new JTextField();
			jTextFieldArguments.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldArguments.setToolTipText("Use comma separated string values here.");
			jTextFieldArguments.setSize(this.editFieldDimension);
		}
		return jTextFieldArguments;
	}
	private DefaultComboBoxModel<String> getComboBoxModelContainer() {
		if (comboBoxModelContainer==null) {
			comboBoxModelContainer = new DefaultComboBoxModel<>();
			if (Application.getJadePlatform().isMainContainerRunning(false)==true) {
				// --- Get the known container ------------
				try {
					comboBoxModelContainer.addElement(Application.getJadePlatform().getMainContainer().getContainerName());
					for (AgentContainer container :  Application.getJadePlatform().getAgentContainerList()) {
							comboBoxModelContainer.addElement(container.getContainerName());
					}
				} catch (ControllerException e) {
					e.printStackTrace();
				}
				
			} else {
				// --- Just take the Main-Container -------
				comboBoxModelContainer.addElement(jade.core.AgentContainer.MAIN_CONTAINER_NAME);
			}
		}
		return comboBoxModelContainer;
	}
	private JComboBox<String> getJComboBoxContainer() {
		if (jComboBoxContainer == null) {
			jComboBoxContainer = new JComboBox<>(this.getComboBoxModelContainer());
			jComboBoxContainer.setEditable(true);
			jComboBoxContainer.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxContainer.setSize(this.editFieldDimension);
		}
		return jComboBoxContainer;
	}
	
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_jPanelButtons = new GridBagLayout();
			gbl_jPanelButtons.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelButtons.rowHeights = new int[]{0, 0};
			gbl_jPanelButtons.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelButtons.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelButtons.setLayout(gbl_jPanelButtons);
			GridBagConstraints gbc_jButtonOK = new GridBagConstraints();
			gbc_jButtonOK.insets = new Insets(0, 0, 0, 20);
			gbc_jButtonOK.gridx = 0;
			gbc_jButtonOK.gridy = 0;
			jPanelButtons.add(getJButtonOK(), gbc_jButtonOK);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(0, 20, 0, 0);
			gbc_jButtonCancel.gridx = 1;
			gbc_jButtonCancel.gridy = 0;
			jPanelButtons.add(getJButtonCancel(), gbc_jButtonCancel);
		}
		return jPanelButtons;
	}
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton("OK");
			jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOK.setForeground(new Color(0, 153, 0));
			jButtonOK.setPreferredSize(new Dimension(80, 26));
			jButtonOK.addActionListener(this);
		}
		return jButtonOK;
	}
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setPreferredSize(new Dimension(80, 26));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonSelectAgentClass()) {
			// --- Select an Agent class ----------------------------
			AgentSelector agentSelector = new AgentSelector(this);
			agentSelector.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			agentSelector.setVisible(true);
			// --- Wait here --------
			if (agentSelector.isCanceled()==false) {
				ClassElement2Display agentClassDisplay = (ClassElement2Display) agentSelector.getSelectedAgentClass();
				this.getJComboBoxAgentClass().setSelectedItem(agentClassDisplay.getClassElement());
			}
			agentSelector.dispose();
			
		} else if (ae.getSource()==this.getJButtonCancel()) {
			// --- Cancel the action / close dialog -----------------
			this.setCanceled(true);
			this.setVisible(false);
			
		} else if (ae.getSource()==this.getJButtonOK()) {
			// --- Start the new agent ------------------------------
			this.setCanceled(false);
			if (this.hasErrors()==true) {
				String title = Language.translate("Configuration Error", Language.EN);
				JOptionPane.showMessageDialog(this, Language.translate(this.errorText, Language.EN), title, JOptionPane.ERROR_MESSAGE);
				return;
			} else { 
				if (agentClasssReminder.contains(this.getAgentClass())==false) {
					agentClasssReminder.add(this.getAgentClass());
				}
			}
			this.setVisible(false);
		}
	}
	
	/**
	 * Checks if is canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return isCanceled;
	}
	/**
	 * Sets the canceled.
	 * @param isCanceled the new canceled
	 */
	public void setCanceled(boolean isCanceled) {
		this.isCanceled = isCanceled;
	}
	/**
	 * Checks for errors.
	 * @return true, if successful
	 */
	public boolean hasErrors() {
		
		if (this.getAgentName()==null) {
			this.errorText = "No agent name was specified.";
			return true;
		}
		
		String agentClassName = this.getAgentClass(); 
		if (agentClassName==null) {
			this.errorText = "No agent class was specified.";
			return true;
		} else {
			Class<?> agentClass = null;
			try {
				agentClass = ClassLoadServiceUtility.forName(agentClassName);
			} catch (ClassNotFoundException | NoClassDefFoundError cnfEx) {
				//cnfEx.printStackTrace();
			}
			if (agentClass==null) {
				this.errorText = "The specified agent class could not be found.";
				return true;
			}
		}
		
		if (this.getAgentContainer()==null) {
			this.errorText = "No container for the agent was specified.";
			return true;
		}
		
		return false;
	}
	
	/**
	 * Gets the agent name.
	 * @return the agent name
	 */
	public String getAgentName() {
		String agentName = this.getJTextFieldAgentName().getText();
		if (agentName!=null && agentName.trim().isEmpty()==true) {
			agentName = null;
		}
		return agentName;
	}
	/**
	 * Gets the agent class.
	 * @return the agent class
	 */
	public String getAgentClass() {
		String agentClass = (String) this.getJComboBoxAgentClass().getSelectedItem();
		if (agentClass!=null && agentClass.trim().isEmpty()==true) {
			agentClass = null;
		}
		return agentClass;
	}
	/**
	 * Gets the agent start arguments.
	 * @return the agent start arguments
	 */
	public Object[] getAgentStartArguments() {
		String arguments = this.getJTextFieldArguments().getText();
		if (arguments!=null && arguments.trim().isEmpty()==true) {
			arguments = null;
		}

		Object[] argArray = null;
		if (arguments!=null) {
			argArray = arguments.split(",");
		}
		return argArray;
	}
	/**
	 * Gets the agent container.
	 * @return the agent container
	 */
	public String getAgentContainer() {
		String agentContainer = (String) this.getJComboBoxContainer().getSelectedItem();
		if (agentContainer!=null && agentContainer.trim().isEmpty()==true) {
			agentContainer = null;
		}
		return agentContainer;
	}
	
}
