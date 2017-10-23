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
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.ExecutionMode;

/**
 * The Class LogFileOptions extends an {@link AbstractOptionTab} and is
 * used in order to visually configure the log file writing of Agent.GUI.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LogFileOptions extends AbstractOptionTab implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel jPanelLoggingBasePath;
	private JLabel jLabelLoggingDirectory;	
	private JTextField jTextFieldLogingDirectory;
	private JButton jButtonApply;
	private JButton jButtonDefault;	
	
	private JCheckBox jCheckBoxLoggingEnabled;
	private JLabel jLabelDefaultLogLocation;
	private JLabel jLabelLoggingExplanation;

	private JPanel jPanelDummy;
	
	
	/**
	 * This is the default constructor
	 */
	public LogFileOptions() {
		super();
		this.initialize();
		this.setGlobalData2Form();
		
		// --- Configure translation ----------------------
		this.getJButtonApply().setText(Language.translate("Speichern"));
		this.getJButtonDefault().setToolTipText(Language.translate("Standard verwenden"));
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTitleAddition()
	 */
	@Override
	public String getTitle() {
		return Language.translate("Log Datei");
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText() {
		return Language.translate("Log-Datei Einstellungen");
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
		gridBagConstraints21.gridy = 3;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.insets = new Insets(10, 20, 0, 0);
		gridBagConstraints11.gridy = 1;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.insets = new Insets(20, 20, 0, 20);
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
		GridBagConstraints gbc_jCheckBoxLoggingEnabled = new GridBagConstraints();
		gbc_jCheckBoxLoggingEnabled.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxLoggingEnabled.insets = new Insets(20, 20, 0, 20);
		gbc_jCheckBoxLoggingEnabled.gridx = 0;
		gbc_jCheckBoxLoggingEnabled.gridy = 1;
		add(getJCheckBoxLoggingEnabled(), gbc_jCheckBoxLoggingEnabled);
		GridBagConstraints gbc_jLabelLoggingExplanation = new GridBagConstraints();
		gbc_jLabelLoggingExplanation.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelLoggingExplanation.insets = new Insets(5, 20, 0, 20);
		gbc_jLabelLoggingExplanation.gridx = 0;
		gbc_jLabelLoggingExplanation.gridy = 2;
		add(getJLabelLoggingExplanation(), gbc_jLabelLoggingExplanation);
		this.add(getJPanelDummy(), gridBagConstraints21);		
	}

	/**
	 * This method initializes getJPanelOIDCIssuerURI	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelOIDCIssuerURI() {
		if (jPanelLoggingBasePath == null) {
			GridBagConstraints gbc_jButtonDefault = new GridBagConstraints();
			gbc_jButtonDefault.gridx = 3;
			gbc_jButtonDefault.insets = new Insets(0, 5, 0, 0);
			gbc_jButtonDefault.gridy = 0;
			GridBagConstraints gbc_jButtonApply = new GridBagConstraints();
			gbc_jButtonApply.gridx = 2;
			gbc_jButtonApply.insets = new Insets(0, 5, 0, 0);
			gbc_jButtonApply.gridy = 0;
			GridBagConstraints gbc_jTextFieldLogingDirectory = new GridBagConstraints();
			gbc_jTextFieldLogingDirectory.fill = GridBagConstraints.BOTH;
			gbc_jTextFieldLogingDirectory.gridy = 0;
			gbc_jTextFieldLogingDirectory.weightx = 1.0;
			gbc_jTextFieldLogingDirectory.insets = new Insets(0, 5, 0, 0);
			gbc_jTextFieldLogingDirectory.gridx = 1;
			GridBagConstraints gbc_jLabelLoggingDirectory = new GridBagConstraints();
			gbc_jLabelLoggingDirectory.insets = new Insets(0, 0, 0, 5);
			gbc_jLabelLoggingDirectory.gridx = 0;
			gbc_jLabelLoggingDirectory.gridy = 0;
			
			jLabelLoggingDirectory = new JLabel();
			jLabelLoggingDirectory.setText(Language.translate("Log-Verzeichnis"));
			jLabelLoggingDirectory.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelLoggingBasePath = new JPanel();
			jPanelLoggingBasePath.setLayout(new GridBagLayout());
			jPanelLoggingBasePath.add(jLabelLoggingDirectory, gbc_jLabelLoggingDirectory);
			jPanelLoggingBasePath.add(getJTextLoggingBasePath(), gbc_jTextFieldLogingDirectory);
			jPanelLoggingBasePath.add(getJButtonApply(), gbc_jButtonApply);
			jPanelLoggingBasePath.add(getJButtonDefault(), gbc_jButtonDefault);
			GridBagConstraints gbc_jLabelDefaultLogLocation = new GridBagConstraints();
			gbc_jLabelDefaultLogLocation.gridwidth = 3;
			gbc_jLabelDefaultLogLocation.anchor = GridBagConstraints.WEST;
			gbc_jLabelDefaultLogLocation.insets = new Insets(5, 5, 0, 5);
			gbc_jLabelDefaultLogLocation.gridx = 1;
			gbc_jLabelDefaultLogLocation.gridy = 1;
			jPanelLoggingBasePath.add(getJLabelDefaultLogLocation(), gbc_jLabelDefaultLogLocation);
		}
		return jPanelLoggingBasePath;
	}
	private JTextField getJTextLoggingBasePath() {
		if (jTextFieldLogingDirectory == null) {
			jTextFieldLogingDirectory = new JTextField();
		}
		return jTextFieldLogingDirectory;
	}

	private JButton getJButtonApply() {
		if (jButtonApply == null) {
			jButtonApply = new JButton();
			jButtonApply.setText("Speichern");
			jButtonApply.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApply.setForeground(new Color(0, 153, 0));
			jButtonApply.setPreferredSize(new Dimension(100, 26));
			jButtonApply.addActionListener(this);
		}
		return jButtonApply;
	}
	private JButton getJButtonDefault() {
		if (jButtonDefault == null) {
			jButtonDefault = new JButton();
			jButtonDefault.setToolTipText(Language.translate("Standard verwenden"));
			jButtonDefault.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonDefault.setPreferredSize(new Dimension(45, 26));
			jButtonDefault.addActionListener(this);
		}
		return jButtonDefault;
	}
	
	private JLabel getJLabelDefaultLogLocation() {
		if (jLabelDefaultLogLocation == null) {
			String displayText = "<html><b>" + Language.translate("Standard Log-Verzeichnis:") + ":</b> '" + GlobalInfo.getLoggingBasePathDefault() + File.separator + "[MONTH]'</html>";
			jLabelDefaultLogLocation = new JLabel(displayText);
			jLabelDefaultLogLocation.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelDefaultLogLocation;
	}
	private JCheckBox getJCheckBoxLoggingEnabled() {
		if (jCheckBoxLoggingEnabled == null) {
			jCheckBoxLoggingEnabled = new JCheckBox(Language.translate("Log-Datei immer erstellen"));
			jCheckBoxLoggingEnabled.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jCheckBoxLoggingEnabled;
	}
	private JLabel getJLabelLoggingExplanation() {
		if (jLabelLoggingExplanation == null) {
			
			String dt1 = "Falls ausgewählt, wird die Log-Datei in jedem Ausführungsmodus erstellt.";
			dt1 = Language.translate(dt1);
			
			String dt2 = "Falls nicht ausgewählt, wird die Log-Datei immer nur im 'Headless-Modus' erstellt (z.B. im Modus '";
			dt2 = Language.translate(dt2) + Application.getGlobalInfo().getExecutionModeDescription(ExecutionMode.DEVICE_SYSTEM) + "').";
			
			String dt3 = "Änderungen der Log-Datei - Einstellungen werden erst mit dem nächsten Start der Anwendung wirksam.";  
			dt3 = Language.translate(dt3);
			
			jLabelLoggingExplanation = new JLabel("<html>" + dt1 + "<br>" + dt2 + "<br><br>" + dt3 + "</html>");
			jLabelLoggingExplanation.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelLoggingExplanation;
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
		if (actionFrom==getJButtonApply()) {
			this.setFormData2Global();
		} else if (actionFrom==getJButtonDefault()) {
			this.getJTextLoggingBasePath().setText(null);
			this.getJCheckBoxLoggingEnabled().setSelected(false);
		}
	}

	
	/**
	 * Fills the form with data from the properties file.
	 */
	private void setGlobalData2Form() {
		String logBasePath = Application.getGlobalInfo().getLoggingBasePath();
		if (logBasePath.equals(GlobalInfo.getLoggingBasePathDefault())) {
			this.getJTextLoggingBasePath().setText(null);
		} else {
			this.getJTextLoggingBasePath().setText(logBasePath);
		}
		this.getJCheckBoxLoggingEnabled().setSelected(Application.getGlobalInfo().isLoggingEnabled());
	}
	/**
	 * Writes the form data to the properties file.
	 */
	private void setFormData2Global() {
		String logBasePath = this.getJTextLoggingBasePath().getText();
		if (logBasePath==null || (logBasePath!=null && logBasePath.trim().equals(""))) {
			Application.getGlobalInfo().setLoggingBasePath(null);
		} else {
			// --- Check the settings -------------------------------
			String notValidMessage = this.isValidDestinationDirectory(logBasePath);
			if (notValidMessage!=null) {
				// --- Error with the current settings --------------
				String title = Language.translate("Log-Datei Konfiguration");
				JOptionPane.showMessageDialog(this, notValidMessage, title, JOptionPane.ERROR_MESSAGE);
				return;
			}
			Application.getGlobalInfo().setLoggingBasePath(logBasePath.trim());
		}
		Application.getGlobalInfo().setLoggingEnabled(this.getJCheckBoxLoggingEnabled().isSelected());
	}
	/**
	 * Checks if the current setting describes a valid destination directory for the logging.
	 *
	 * @param newLogBasePath the new logging base path
	 * @return null, if is valid destination folder
	 */
	private String isValidDestinationDirectory(String newLogBasePath) {
		
		String notValidMessage = null;
		try {
			// --- Check the given path ----------------------------- 
			File dirToCheck = new File(newLogBasePath);
			if (dirToCheck!=null) {
				
				// --------------------------------------------------
				// --- Check parent directory first -----------------
				// --------------------------------------------------
				File dirToCheckParent = dirToCheck.getParentFile(); 
				if (dirToCheckParent==null) {
					// --- Wrong entry ------------------------------
					notValidMessage = Language.translate("Fehlerhafte Verzeichnisangabe"); 
				} else if (dirToCheckParent.exists()==false) {
					// --- Parent of base log path does not exists --
					notValidMessage = "'" + dirToCheckParent.getAbsolutePath() + "': " + Language.translate("Das Verzeichnis konnte nicht gefunden werden.");
				} else if (dirToCheckParent.isDirectory()==false) {
					// --- Parent of base log path does not exists --
					notValidMessage = "'" + dirToCheckParent.getAbsolutePath() + "': " + Language.translate("Das Verzeichnis konnte nicht gefunden werden.");
				} 
				
				// --------------------------------------------------
				// --- Check if path exists or can be created -------
				// --------------------------------------------------
				if (notValidMessage==null) {
					if (dirToCheck.exists()==false) {
						if (dirToCheck.mkdir()==false) {
							notValidMessage = "'" + dirToCheck.getAbsolutePath() + "': " + Language.translate("Das Verzeichnis konnte nicht erstellt werden.");
						}
					}
				}
				
			}
			
		} catch (Exception ex) {
			notValidMessage = ex.getLocalizedMessage();
			ex.printStackTrace();
		}
		return notValidMessage;
	}
	
}  
