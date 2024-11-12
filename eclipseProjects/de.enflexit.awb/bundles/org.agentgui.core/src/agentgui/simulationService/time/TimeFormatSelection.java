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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.language.Language;
import agentgui.core.config.GlobalInfo;
import de.enflexit.common.swing.JComboBoxWide;
import de.enflexit.common.swing.JPanelForActions;

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
	private String defaultTimeFormat = TimeModelDateBased.DEFAULT_TIME_FORMAT;
	
	private JLabel jLabelFormat;
	private JTextField jTextFieldTimeFormat;
	private JButton jButtonTimeFormatDefault;
	private JComboBoxWide<TimeFormat> jComboBoxTimeFormat;

	private int horVertGap = 5;
	
	
	/**
	 * Instantiates a new time format selection.
	 */
	public TimeFormatSelection() {
		this(true, null, null);
	}
	/**
	 * Instantiates a new time format selection.
	 * @param showFormatLabel set true if the introductory label is to be displayed
	 */
	public TimeFormatSelection(boolean showFormatLabel) {
		this(showFormatLabel, null, null);
	}
	
	/**
	 * Instantiates a new time format selection.
	 *
	 * @param showFormatLabel the show format label
	 * @param horizontalVerticalGap the horizontal or vertical gap between the elements
	 * @param font the font to use as the base for the graphical representation
	 */
	public TimeFormatSelection(boolean showFormatLabel, Integer horizontalVerticalGap, Font font) {
		super();
		this.showLable=showFormatLabel;
		if (horizontalVerticalGap!=null) this.horVertGap = horizontalVerticalGap;
		if (font!=null) this.setFont(font);
		this.initialize();
	}
	
	/**
	 * This method initializes this.
	 * @return void
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		this.setSize(430, 60);

		
		GridBagConstraints gridBagConstraintLabel = new GridBagConstraints();
		gridBagConstraintLabel.gridx = 0;
		gridBagConstraintLabel.gridy = 0;
		gridBagConstraintLabel.insets = new Insets(0, this.horVertGap, 0, 0);
		gridBagConstraintLabel.anchor = GridBagConstraints.WEST;
		
		GridBagConstraints gridBagConstraintTextField = new GridBagConstraints();
		gridBagConstraintTextField.gridx = 1;
		gridBagConstraintTextField.gridy = 0;
		gridBagConstraintTextField.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintTextField.weightx = 1.0;
		if (this.showLable==true) {
			gridBagConstraintTextField.insets = new Insets(0, this.horVertGap, 0, 0);			
		} else {
			gridBagConstraintTextField.insets = new Insets(0, 0, 0, 0);
		}
		
		GridBagConstraints gridBagConstraintButtonDefault = new GridBagConstraints();
		gridBagConstraintButtonDefault.gridx = 2;
		gridBagConstraintButtonDefault.gridy = 0;
		gridBagConstraintButtonDefault.insets = new Insets(0, this.horVertGap, 0, this.horVertGap);
		
		GridBagConstraints gridBagConstraintComboBox = new GridBagConstraints();
		gridBagConstraintComboBox.gridx = 1;
		gridBagConstraintComboBox.gridy = 1;
		gridBagConstraintComboBox.weightx = 1.0;
		gridBagConstraintComboBox.fill = GridBagConstraints.BOTH;
		if (this.showLable==true) {
			gridBagConstraintComboBox.insets = new Insets(this.horVertGap, this.horVertGap, 0, 0);
		} else {
			gridBagConstraintComboBox.insets = new Insets(this.horVertGap, 0, 0, 0);
		}
		
		// --- Add the components -----------------------------------
		if (this.showLable==true) {
			this.add(this.getJLabelFormatHeader(), gridBagConstraintLabel);	
		}
		this.add(getJTextFieldTimeFormat(), gridBagConstraintTextField);
		this.add(getJComboBoxTimeFormat(), gridBagConstraintComboBox);
		this.add(getJButtonTimeFormatDefault(), gridBagConstraintButtonDefault);
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
			jButtonTimeFormatDefault.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonTimeFormatDefault.setToolTipText("Standard verwenden");
			jButtonTimeFormatDefault.setToolTipText(Language.translate(jButtonTimeFormatDefault.getToolTipText()));
			jButtonTimeFormatDefault.setPreferredSize(new Dimension(45, 26));
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
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setFont(java.awt.Font)
	 */
	@Override
	public void setFont(Font font) {
		
		super.setFont(font);
		
		Font fPlain = new Font(font.getName(), Font.PLAIN, font.getSize());
		Font fBold = new Font(font.getName(), Font.PLAIN, font.getSize());
		
		this.getJLabelFormatHeader().setFont(fBold);
		this.getJTextFieldTimeFormat().setFont(fPlain);
		this.getJComboBoxTimeFormat().setFont(fPlain);
		this.getJButtonTimeFormatDefault().setFont(fPlain);
		
		int height = 26;
		switch (font.getSize()) {
		case 11:
			height = 24;
			break;
		case 10:
			height = 22;
			break;
		}
		if (height!=26) {
			this.getJTextFieldTimeFormat().setPreferredSize(new Dimension(30, height));
			this.getJButtonTimeFormatDefault().setPreferredSize(new Dimension(21, height));
			this.getJComboBoxTimeFormat().setPreferredSize(new Dimension(30, height));
		}
	}
	
}
