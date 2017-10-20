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
package agentgui.core.gui.options;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;

/**
 * The Class OIDCOptions extends an {@link AbstractOptionTab} and is
 * used in order to visually configure the OpenID Connect settings of Agent.GUI.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OIDCOptions extends AbstractOptionTab implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel jPanelOIDCIssuerURI;
	private JPanel jPanelDummy;
	
	private JLabel jLabelOIDCIssuerURILabel;	
	private JTextField jTextFieldIssuerURI;
	
	private JButton jButtonOIDCIssuerURIApply;
	private JButton jButtonOIDCIssuerURIDefault;	

	/**
	 * This is the default constructor
	 */
	public OIDCOptions() {
		super();
		this.initialize();
		this.setGlobalData2Form();
		
		// --- Configure translation ----------------------
		this.getJButtonOIDCIssuerURIApply().setText(Language.translate("Speichern"));
		this.getJButtonOIDCIssuerURIDefault().setToolTipText(Language.translate("Standard verwenden"));
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTitleAddition()
	 */
	@Override
	public String getTitle() {
		return "OpenID Connect";
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText() {
		return "OpenID Connect";
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		gridBagConstraints22.gridx = 0;
		gridBagConstraints22.anchor = GridBagConstraints.WEST;
		gridBagConstraints22.insets = new Insets(5, 20, 0, 0);
		gridBagConstraints22.gridy = 6;
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 0;
		gridBagConstraints12.anchor = GridBagConstraints.WEST;
		gridBagConstraints12.insets = new Insets(10, 20, 0, 0);
		gridBagConstraints12.gridy = 5;
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.insets = new Insets(0, 20, 20, 20);
		gridBagConstraints21.fill = GridBagConstraints.BOTH;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.weighty = 1.0;
		gridBagConstraints21.gridy = 7;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.insets = new Insets(10, 20, 0, 0);
		gridBagConstraints11.gridy = 1;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.insets = new Insets(20, 20, 10, 20);
		gridBagConstraints4.weightx = 1.0;
		gridBagConstraints4.gridy = 0;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.insets = new Insets(5, 20, 0, 0);
		gridBagConstraints2.gridy = 4;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.insets = new Insets(5, 20, 0, 0);
		gridBagConstraints1.gridy = 3;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5, 20, 0, 0);
		gridBagConstraints.gridy = 2;
		
		this.setSize(555, 307);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.setLayout(new GridBagLayout());
		this.add(getJPanelOIDCIssuerURI(), gridBagConstraints4);
		this.add(getJPanelDummy(), gridBagConstraints21);		
	}

	/**
	 * This method initializes getJPanelOIDCIssuerURI	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelOIDCIssuerURI() {
		if (jPanelOIDCIssuerURI == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 3;
			gridBagConstraints7.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints7.gridy = 0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 2;
			gridBagConstraints6.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints6.gridy = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = -1;
			gridBagConstraints3.gridy = -1;
			
			jLabelOIDCIssuerURILabel = new JLabel();
			jLabelOIDCIssuerURILabel.setText("OpenID Connect Issuer-URI:");
			jLabelOIDCIssuerURILabel.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelOIDCIssuerURI = new JPanel();
			jPanelOIDCIssuerURI.setLayout(new GridBagLayout());
			jPanelOIDCIssuerURI.add(jLabelOIDCIssuerURILabel, gridBagConstraints3);
			jPanelOIDCIssuerURI.add(getJTextFieldOIDCIssuerURI(), gridBagConstraints5);
			jPanelOIDCIssuerURI.add(getJButtonOIDCIssuerURIApply(), gridBagConstraints6);
			jPanelOIDCIssuerURI.add(getJButtonOIDCIssuerURIDefault(), gridBagConstraints7);
		}
		return jPanelOIDCIssuerURI;
	}
	/**
	 * This method initializes jTextFieldIssuerURI	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldOIDCIssuerURI() {
		if (jTextFieldIssuerURI == null) {
			jTextFieldIssuerURI = new JTextField();
		}
		return jTextFieldIssuerURI;
	}
	/**
	 * This method initializes jButtonOIDCIssuerURIApply	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOIDCIssuerURIApply() {
		if (jButtonOIDCIssuerURIApply == null) {
			jButtonOIDCIssuerURIApply = new JButton();
			jButtonOIDCIssuerURIApply.setText("Speichern");
			jButtonOIDCIssuerURIApply.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOIDCIssuerURIApply.setForeground(new Color(0, 153, 0));
			jButtonOIDCIssuerURIApply.setPreferredSize(new Dimension(100, 26));
			jButtonOIDCIssuerURIApply.addActionListener(this);
		}
		return jButtonOIDCIssuerURIApply;
	}
	/**
	 * This method initializes jButtonOIDCIssuerURIDefault	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOIDCIssuerURIDefault() {
		if (jButtonOIDCIssuerURIDefault == null) {
			jButtonOIDCIssuerURIDefault = new JButton();
			jButtonOIDCIssuerURIDefault.setToolTipText(Language.translate("Standard verwenden"));
			jButtonOIDCIssuerURIDefault.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonOIDCIssuerURIDefault.setPreferredSize(new Dimension(45, 26));
			jButtonOIDCIssuerURIDefault.addActionListener(this);
		}
		return jButtonOIDCIssuerURIDefault;
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
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object actionFrom = ae.getSource();
		if (actionFrom==getJButtonOIDCIssuerURIApply()) {
			this.setFormData2Global();
		} else if (actionFrom==getJButtonOIDCIssuerURIDefault()) {
			this.getJTextFieldOIDCIssuerURI().setText(GlobalInfo.DEFAULT_OIDC_ISSUER_URI);
		}
	}
	
	/**
	 * Writes the form data to the properties file.
	 */
	private void setFormData2Global() {
		Application.getGlobalInfo().setOIDCIssuerURI(this.getJTextFieldOIDCIssuerURI().getText().trim());	
	}
	/**
	 * Fills the form with data from the properties file.
	 */
	private void setGlobalData2Form() {
		// --- Set OpenID Connect issuer URI ------------------------------
		this.getJTextFieldOIDCIssuerURI().setText(Application.getGlobalInfo().getOIDCIssuerURI());
	}
	
}  
