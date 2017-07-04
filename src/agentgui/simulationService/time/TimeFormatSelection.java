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
package agentgui.simulationService.time;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.gui.components.JComboBoxWide;
import agentgui.core.gui.components.JPanelForActions;

/**
 * The Class TimeFormatSelection can be used as widget in order to
 * configure Strings of time format. Just add this to a appropriate parent
 * container and add an ActionListener to register changes in the widget. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeFormatSelection extends JPanelForActions {

	private static final long serialVersionUID = 1L;
	
	private boolean showLable = false;
	private String defaultTimeFormat = TimeModelDateBased.DEFAULT_TIME_FORMAT;  //  @jve:decl-index=0:
	
	private JLabel jLabelFormat = null;
	private JTextField jTextFieldTimeFormat = null;
	private JButton jButtonTimeFormatDefault = null;
	private JComboBoxWide<TimeFormat> jComboBoxTimeFormat = null;

	private JPanel jPanelDummy = null;

	
	/**
	 * Instantiates a new time format selection.
	 */
	public TimeFormatSelection() {
		this(true);
	}
	/**
	 * Instantiates a new time format selection.
	 * @param showFormatLabel set true if the introductory label is to be displayed
	 */
	public TimeFormatSelection(boolean showFormatLabel) {
		super();
		this.showLable=showFormatLabel;
		initialize();
	}
	
	/**
	 * This method initializes this.
	 * @return void
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraintLabel = new GridBagConstraints();
		gridBagConstraintLabel.gridx = 0;
		gridBagConstraintLabel.insets = new Insets(0, 5, 0, 0);
		gridBagConstraintLabel.anchor = GridBagConstraints.WEST;
		gridBagConstraintLabel.gridy = 1;
		
		GridBagConstraints gridBagConstraintButtonDefault = new GridBagConstraints();
		gridBagConstraintButtonDefault.gridx = 2;
		gridBagConstraintButtonDefault.insets = new Insets(0, 5, 0, 5);
		gridBagConstraintButtonDefault.gridy = 1;
		
		GridBagConstraints gridBagConstraintTextField = new GridBagConstraints();
		gridBagConstraintTextField.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintTextField.gridx = 1;
		gridBagConstraintTextField.gridy = 1;
		gridBagConstraintTextField.weightx = 1.0;
		if (this.showLable==true) {
			gridBagConstraintTextField.insets = new Insets(0, 5, 0, 0);			
		} else {
			gridBagConstraintTextField.insets = new Insets(0, 0, 0, 0);
		}
		
		GridBagConstraints gridBagConstraintComboBox = new GridBagConstraints();
		gridBagConstraintComboBox.fill = GridBagConstraints.BOTH;
		gridBagConstraintComboBox.gridx = 1;
		gridBagConstraintComboBox.gridy = 2;
		gridBagConstraintComboBox.weightx = 1.0;
		if (this.showLable==true) {
			gridBagConstraintComboBox.insets = new Insets(7, 5, 0, 0);
		} else {
			gridBagConstraintComboBox.insets = new Insets(7, 0, 0, 0);
		}
		
		GridBagConstraints gridBagConstraintDummy = new GridBagConstraints();
		gridBagConstraintDummy.gridx = 0;
		gridBagConstraintDummy.fill = GridBagConstraints.BOTH;
		gridBagConstraintDummy.weightx = 1.0;
		gridBagConstraintDummy.weighty = 1.0;
		gridBagConstraintDummy.gridwidth = 3;
		gridBagConstraintDummy.insets = new Insets(0, 0, 0, 0);
		gridBagConstraintDummy.gridy = 3;
		
		this.setSize(427, 65);
		this.setLayout(new GridBagLayout());
		if (showLable==true) {
			this.add(this.getJLabelFormatHeader(), gridBagConstraintLabel);	
		}
		this.add(getJTextFieldTimeFormat(), gridBagConstraintTextField);
		this.add(getJComboBoxTimeFormat(), gridBagConstraintComboBox);
		this.add(getJButtonTimeFormatDefault(), gridBagConstraintButtonDefault);
		this.add(getJPanelDummy(), gridBagConstraintDummy);
	}

	/**
	 * Gets the JLabel format header.
	 * @return the JLabel format header
	 */
	public JLabel getJLabelFormatHeader() {
		if (jLabelFormat==null) {
			jLabelFormat = new JLabel();
			jLabelFormat.setText("Format");
			jLabelFormat.setText(Language.translate(jLabelFormat.getText()) + ":");
			jLabelFormat.setPreferredSize(new Dimension(40, 16));
		}
		return jLabelFormat;
	}
	
	/**
	 * This method initializes jTextFieldTimeFormat.
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldTimeFormat() {
		if (jTextFieldTimeFormat == null) {
			jTextFieldTimeFormat = new JTextField();
			jTextFieldTimeFormat.setPreferredSize(new Dimension(30, 26));
			jTextFieldTimeFormat.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					fireActionEvent();
				}
				@Override
				public void insertUpdate(DocumentEvent e) {
					fireActionEvent();
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
					fireActionEvent();
				}
			});
		}
		return jTextFieldTimeFormat;
	}
	/**
	 * This method initializes jButtonTimeFormatDefault.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonTimeFormatDefault() {
		if (jButtonTimeFormatDefault == null) {
			jButtonTimeFormatDefault = new JButton();
			jButtonTimeFormatDefault.setPreferredSize(new Dimension(45, 26));
			jButtonTimeFormatDefault.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonTimeFormatDefault.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonTimeFormatDefault.setToolTipText("Agent.GUI - Standard verwenden");
			jButtonTimeFormatDefault.setToolTipText(Language.translate(jButtonTimeFormatDefault.getToolTipText()));
			jButtonTimeFormatDefault.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setPauseFireUpdates(true);
					getJTextFieldTimeFormat().setText(getDefaultTimeFormat());
					setPauseFireUpdates(false);
					fireActionEvent();
				}
			});
		}
		return jButtonTimeFormatDefault;
	}
	/**
	 * This method initializes jComboBoxTimeFormat.
	 * @return javax.swing.JComboBox
	 */
	private JComboBoxWide<TimeFormat> getJComboBoxTimeFormat() {
		if (jComboBoxTimeFormat == null) {
			final DefaultComboBoxModel<TimeFormat> cbm = new DefaultComboBoxModel<TimeFormat>(new TimeFormatVector());
			jComboBoxTimeFormat = new JComboBoxWide<TimeFormat>(cbm);
			jComboBoxTimeFormat.setToolTipText("Vorlagen");
			jComboBoxTimeFormat.setToolTipText(Language.translate(jComboBoxTimeFormat.getToolTipText()));
			jComboBoxTimeFormat.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					TimeFormat tf = (TimeFormat) cbm.getSelectedItem();
					setPauseFireUpdates(true);
					getJTextFieldTimeFormat().setText(tf.getFormat());
					setPauseFireUpdates(false);
					fireActionEvent();
				}
			});
		}
		return jComboBoxTimeFormat;
	}
	/**
	 * This method initializes jPanelDummy	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelDummy() {
		if (jPanelDummy == null) {
			jPanelDummy = new JPanel();
			jPanelDummy.setLayout(new GridBagLayout());
		}
		return jPanelDummy;
	}

	/**
	 * Sets the time format.
	 * @param newTimeFormat the new time format
	 */
	public void setTimeFormat(String newTimeFormat) {
		if (newTimeFormat==null) {
			this.getJTextFieldTimeFormat().setText(this.getDefaultTimeFormat());
		} else {
			this.getJTextFieldTimeFormat().setText(newTimeFormat);	
		}
	}
	/**
	 * Gets the time format.
	 * @return the time format
	 */
	public String getTimeFormat() {
		if (this.getJTextFieldTimeFormat().getText()==null) {
			this.setTimeFormat(this.getDefaultTimeFormat());
		}
		return this.getJTextFieldTimeFormat().getText();
	}
	
	/**
	 * Sets the default time format.
	 * @param defaultTimeFormat the new default time format
	 */
	public void setDefaultTimeFormat(String defaultTimeFormat) {
		this.defaultTimeFormat = defaultTimeFormat;
	}
	/**
	 * Gets the default time format.
	 * @return the default time format
	 */
	public String getDefaultTimeFormat() {
		return defaultTimeFormat;
	}
	
	/**
	 * Fires a action event for this panel.
	 */
	private void fireActionEvent() {
		ActionEvent ae = new ActionEvent(this, 0, "DateFormatChanged");
		this.fireUpdate(ae);
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
