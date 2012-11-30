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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.components.JComboBoxWide;
import agentgui.core.gui.components.JPanelForActions;

/**
 * The Class TimeFormatSelection.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeFormatSelection extends JPanelForActions {

	private static final long serialVersionUID = 1L;
	
	final static String PathImage = Application.getGlobalInfo().PathImageIntern();
	
	private JLabel jLabelFormat = null;
	private JTextField jTextFieldTimeFormat = null;
	private JButton jButtonTimeFormatDefault = null;
	private JComboBoxWide jComboBoxTimeFormat = null;

	private JPanel jPanelDummy = null;

	/**
	 * This is the default constructor.
	 */
	public TimeFormatSelection() {
		super();
		initialize();
	}

	/**
	 * This method initializes this.
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
		gridBagConstraints41.gridx = 0;
		gridBagConstraints41.fill = GridBagConstraints.BOTH;
		gridBagConstraints41.weightx = 1.0;
		gridBagConstraints41.weighty = 1.0;
		gridBagConstraints41.gridwidth = 3;
		gridBagConstraints41.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints41.gridy = 3;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.gridy = 2;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.insets = new Insets(7, 5, 0, 0);
		gridBagConstraints3.gridx = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 2;
		gridBagConstraints2.insets = new Insets(0, 5, 0, 5);
		gridBagConstraints2.gridy = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.insets = new Insets(0, 5, 0, 0);
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(0, 5, 0, 0);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridy = 1;
		
		jLabelFormat = new JLabel();
		jLabelFormat.setText("Format");
		jLabelFormat.setPreferredSize(new Dimension(40, 16));
		jLabelFormat.setText(Language.translate(jLabelFormat.getText()) + ":");
		
		this.setSize(427, 65);
		this.setLayout(new GridBagLayout());
		this.add(jLabelFormat, gridBagConstraints);
		this.add(getJTextFieldTimeFormat(), gridBagConstraints1);
		this.add(getJButtonTimeFormatDefault(), gridBagConstraints2);
		this.add(getJComboBoxTimeFormat(), gridBagConstraints3);
		this.add(getJPanelDummy(), gridBagConstraints41);
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
			jButtonTimeFormatDefault.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBreset.png")));
			jButtonTimeFormatDefault.setToolTipText("Agent.GUI - Standard verwenden");
			jButtonTimeFormatDefault.setToolTipText(Language.translate(jButtonTimeFormatDefault.getToolTipText()));
			jButtonTimeFormatDefault.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getJTextFieldTimeFormat().setText(TimeModelDateBased.DEFAULT_TIME_FORMAT);
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
	private JComboBoxWide getJComboBoxTimeFormat() {
		if (jComboBoxTimeFormat == null) {
			final DefaultComboBoxModel cbm = new DefaultComboBoxModel(new TimeFormatVector());
			jComboBoxTimeFormat = new JComboBoxWide(cbm);
			jComboBoxTimeFormat.setToolTipText("Vorlagen");
			jComboBoxTimeFormat.setToolTipText(Language.translate(jComboBoxTimeFormat.getToolTipText()));
			jComboBoxTimeFormat.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					TimeFormat tf = (TimeFormat) cbm.getSelectedItem();
					getJTextFieldTimeFormat().setText(tf.getFormat());
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
			this.getJTextFieldTimeFormat().setText(this.getTimeFormatDefault());
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
			this.setTimeFormat(this.getTimeFormatDefault());
		}
		return this.getJTextFieldTimeFormat().getText();
	}
	
	/**
	 * Returns the default time format.
	 * @return the default time format
	 */
	public String getTimeFormatDefault() {
		return TimeModelDateBased.DEFAULT_TIME_FORMAT;
	}
	
	/**
	 * Fires a action event for this panel.
	 */
	private void fireActionEvent() {
		ActionEvent ae = new ActionEvent(this, 0, "DateFormatChnaged");
		this.fireUpdate(ae);
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
