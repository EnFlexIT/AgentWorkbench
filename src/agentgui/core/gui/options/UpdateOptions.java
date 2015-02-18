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
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import javax.swing.JCheckBox;

public class UpdateOptions extends AbstractOptionTab implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final static String pathImage = Application.getGlobalInfo().getPathImageIntern();  //  @jve:decl-index=0:
	
	private JPanel jPanelUpdateLink = null;
	private JPanel jPanelDummy = null;
	
	private ButtonGroup updateAutoConfig = null;  //  @jve:decl-index=0:
	private JRadioButton jRadioButtonUpdateAutomated = null;
	private JRadioButton jRadioButtonUpdateDownloadAndAsk = null;
	private JRadioButton jRadioButtonUpdateDisabled = null;
	
	private JLabel jLabelUpdateSiteLable = null;
	private JLabel jLabelUpdateLable = null;
	
	private JTextField jTextFieldUpdateSite = null;
	
	private JButton jButtonUpdateSiteApply = null;
	private JButton jButtonUpdateSiteDefault = null;

	private JLabel jLabelUpdateDictionary = null;

	private JCheckBox jCheckBoxUpdateKeepDictionary = null;
	

	/**
	 * This is the default constructor
	 */
	public UpdateOptions() {
		super();
		this.initialize();
		this.setGlobalData2Form();
		
		// --- Configure translation ----------------------
		jButtonUpdateSiteApply.setText(Language.translate("Speichern"));
		jButtonUpdateSiteDefault.setToolTipText(Language.translate("Standard verwenden"));
		
		jLabelUpdateLable.setText(Language.translate("Nach Updates suchen") + ":");
		jRadioButtonUpdateAutomated.setText(Language.translate("Updates automatisch installieren"));
		jRadioButtonUpdateDownloadAndAsk.setText(Language.translate("Update automatisch herunterladen, Installationszeitpunkt manuell festlegen"));
		jRadioButtonUpdateDisabled.setText(Language.translate("Updates nicht automatisch herunterladen oder installieren"));
		
		jLabelUpdateDictionary.setText(Language.translate("Wörterbuch") + ":");
		jCheckBoxUpdateKeepDictionary.setText(Language.translate("Im Falle eines Updates, eigenes Wörterbuch beibehalten."));
		
	}

	/* (non-Javadoc)
	 * @see agentgui.core.gui.options.AbstractOptionTab#getTitleAddition()
	 */
	@Override
	public String getTitle() {
		return Language.translate("Update-Konfiguration");
	}
	/* (non-Javadoc)
	 * @see agentgui.core.gui.options.AbstractOptionTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText() {
		return Language.translate("Update-Konfiguration");
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
	
		jLabelUpdateLable = new JLabel();
		jLabelUpdateLable.setText("Nach Updates suchen");
		jLabelUpdateLable.setFont(new Font("Dialog", Font.BOLD, 12));
		
		jLabelUpdateDictionary = new JLabel();
		jLabelUpdateDictionary.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelUpdateDictionary.setText("Wörterbuch");
		
		this.setSize(555, 307);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.setLayout(new GridBagLayout());
		this.add(getJRadioButtonUpdateAutomated(), gridBagConstraints);
		this.add(getJRadioButtonUpdateDownloadAndAsk(), gridBagConstraints1);
		this.add(getJRadioButtonUpdateDisabled(), gridBagConstraints2);
		this.add(getJPanelUpdateLink(), gridBagConstraints4);
		this.add(jLabelUpdateLable, gridBagConstraints11);
		this.add(getJPanelDummy(), gridBagConstraints21);
		this.add(jLabelUpdateDictionary, gridBagConstraints12);
		this.add(getJCheckBoxUpdateKeepDictionary(), gridBagConstraints22);
		
		this.updateAutoConfig = new ButtonGroup();
		this.updateAutoConfig.add(this.getJRadioButtonUpdateAutomated());
		this.updateAutoConfig.add(this.getJRadioButtonUpdateDownloadAndAsk());
		this.updateAutoConfig.add(this.getJRadioButtonUpdateDisabled());
		
	}
	/**
	 * This method initializes jRadioButtonUpdateAutomated	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonUpdateAutomated() {
		if (jRadioButtonUpdateAutomated == null) {
			jRadioButtonUpdateAutomated = new JRadioButton();
			jRadioButtonUpdateAutomated.setText("Updates automatisch installieren");
			jRadioButtonUpdateAutomated.setActionCommand("0");
		}
		return jRadioButtonUpdateAutomated;
	}
	/**
	 * This method initializes jRadioButtonUpdateDownloadAndAsk	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonUpdateDownloadAndAsk() {
		if (jRadioButtonUpdateDownloadAndAsk == null) {
			jRadioButtonUpdateDownloadAndAsk = new JRadioButton();
			jRadioButtonUpdateDownloadAndAsk.setText("Update automatisch herunterladen, Installationszeitpunkt manuell festlegen");
			jRadioButtonUpdateDownloadAndAsk.setActionCommand("1");
		}
		return jRadioButtonUpdateDownloadAndAsk;
	}
	/**
	 * This method initializes jRadioButtonUpdateDisabled	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonUpdateDisabled() {
		if (jRadioButtonUpdateDisabled == null) {
			jRadioButtonUpdateDisabled = new JRadioButton();
			jRadioButtonUpdateDisabled.setText("Updates nicht automatisch herunterladen oder installieren");
			jRadioButtonUpdateDisabled.setActionCommand("2");
		}
		return jRadioButtonUpdateDisabled;
	}
	/**
	 * This method initializes jPanelUpdateLink	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelUpdateLink() {
		if (jPanelUpdateLink == null) {
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
			
			jLabelUpdateSiteLable = new JLabel();
			jLabelUpdateSiteLable.setText("Update-Site:");
			jLabelUpdateSiteLable.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelUpdateLink = new JPanel();
			jPanelUpdateLink.setLayout(new GridBagLayout());
			jPanelUpdateLink.add(jLabelUpdateSiteLable, gridBagConstraints3);
			jPanelUpdateLink.add(getJTextFieldUpdateSite(), gridBagConstraints5);
			jPanelUpdateLink.add(getJButtonUpdateSiteApply(), gridBagConstraints6);
			jPanelUpdateLink.add(getJButtonUpdateSiteDefault(), gridBagConstraints7);
		}
		return jPanelUpdateLink;
	}
	/**
	 * This method initializes jTextFieldUpdateSite	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldUpdateSite() {
		if (jTextFieldUpdateSite == null) {
			jTextFieldUpdateSite = new JTextField();
		}
		return jTextFieldUpdateSite;
	}
	/**
	 * This method initializes jButtonUpdateSiteApply	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonUpdateSiteApply() {
		if (jButtonUpdateSiteApply == null) {
			jButtonUpdateSiteApply = new JButton();
			jButtonUpdateSiteApply.setText("Speichern");
			jButtonUpdateSiteApply.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonUpdateSiteApply.setForeground(new Color(0, 153, 0));
			jButtonUpdateSiteApply.setPreferredSize(new Dimension(100, 26));
			jButtonUpdateSiteApply.addActionListener(this);
		}
		return jButtonUpdateSiteApply;
	}
	/**
	 * This method initializes jButtonUpdateSiteDefault	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonUpdateSiteDefault() {
		if (jButtonUpdateSiteDefault == null) {
			jButtonUpdateSiteDefault = new JButton();
			jButtonUpdateSiteDefault.setToolTipText(Language.translate("Standard verwenden"));
			jButtonUpdateSiteDefault.setIcon(new ImageIcon(getClass().getResource(pathImage + "MBreset.png")));
			jButtonUpdateSiteDefault.setPreferredSize(new Dimension(45, 26));
			jButtonUpdateSiteDefault.addActionListener(this);
		}
		return jButtonUpdateSiteDefault;
	}
	/**
	 * This method initializes jCheckBoxUpdateKeepDictionary	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxUpdateKeepDictionary() {
		if (jCheckBoxUpdateKeepDictionary == null) {
			jCheckBoxUpdateKeepDictionary = new JCheckBox();
			jCheckBoxUpdateKeepDictionary.setText("Im Falle eines Updates, eigenes Wörterbuch beibehalten.");
		}
		return jCheckBoxUpdateKeepDictionary;
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
		if (actionFrom==getJButtonUpdateSiteApply()) {
			this.setFromData2Global();
			
		} else if (actionFrom==getJButtonUpdateSiteDefault()) {
			this.getJTextFieldUpdateSite().setText("http://update.agentgui.org");
			this.getJRadioButtonUpdateAutomated().setSelected(true);
			this.getJCheckBoxUpdateKeepDictionary().setSelected(true);
		}
		
	}
	
	/**
	 * Sets the from data2 global.
	 */
	private void setFromData2Global() {
		
		Application.getGlobalInfo().setUpdateSite(this.getJTextFieldUpdateSite().getText().trim());
		
		String option = null;
		if (this.getJRadioButtonUpdateAutomated().isSelected()) {
			option = this.getJRadioButtonUpdateAutomated().getActionCommand();
		} else if (this.getJRadioButtonUpdateDownloadAndAsk().isSelected()) {
			option = this.getJRadioButtonUpdateDownloadAndAsk().getActionCommand();
		} else if (this.getJRadioButtonUpdateDisabled().isSelected()) {
			option = this.getJRadioButtonUpdateDisabled().getActionCommand();
		}
		Integer autoConfiguration = Integer.parseInt(option);
		Application.getGlobalInfo().setUpdateAutoConfiguration(autoConfiguration);
		
		if (this.getJCheckBoxUpdateKeepDictionary().isSelected()) {
			Application.getGlobalInfo().setUpdateKeepDictionary(1);
		} else {
			Application.getGlobalInfo().setUpdateKeepDictionary(0);
		}
		
	}
	/**
	 * Sets the global data2 form.
	 */
	private void setGlobalData2Form() {

		this.getJTextFieldUpdateSite().setText(Application.getGlobalInfo().getUpdateSite());
		
		int autoConfiguration = Application.getGlobalInfo().getUpdateAutoConfiguration();
		switch (autoConfiguration) {
		case 0:
			this.getJRadioButtonUpdateAutomated().setSelected(true);
			break;
		case 1:
			this.getJRadioButtonUpdateDownloadAndAsk().setSelected(true);
			break;
		case 2:
			this.getJRadioButtonUpdateDisabled().setSelected(true);
			break;
		default:
			this.getJRadioButtonUpdateAutomated().setSelected(true);
			break;
		}
		
		if (Application.getGlobalInfo().getUpdateKeepDictionary()==0) {
			this.getJCheckBoxUpdateKeepDictionary().setSelected(false);
		} else {
			this.getJCheckBoxUpdateKeepDictionary().setSelected(true);
		}
		
	}

	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
