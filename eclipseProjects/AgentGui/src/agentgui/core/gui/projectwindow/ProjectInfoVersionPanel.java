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
package agentgui.core.gui.projectwindow;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import agentgui.core.application.Language;
import agentgui.core.common.KeyAdapter4Numbers;
import agentgui.core.config.GlobalInfo;
import agentgui.core.project.Project;
import agentgui.core.update.VersionInformation;

/**
 * The Class ProjectInfoVersionPanel can be used in order to edit the current {@link VersionInformation}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectInfoVersionPanel extends JPanel {

	private static final long serialVersionUID = -2572793810709664030L;
	
	private ProjectInfo projectInfo;
	private Project currProject;
	private VersionInformation version;
	
	private JButton jButtonEditVersion;

	private JLabel jLabelMajorVersion;
	private JTextField jTextFieldMajorVersion;

	private JLabel jLableMinorVersion;
	private JTextField jTextFieldMinorVersion;
	
	private JLabel jLabelBuildNo;
	private JTextField jTextFieldBuildNo;

	private KeyAdapter4Numbers keyAdapter4Numbers;
	private JLabel jLabelVersion;
	
	/**
	 * Instantiates a new project info version panel.
	 *
	 * @param project the project
	 * @param projectInfo the tab with the project info
	 */
	public ProjectInfoVersionPanel(Project project, ProjectInfo projectInfo) {
		this.currProject = project;
		this.projectInfo = projectInfo;
		this.updateVersionInformation();
		this.initialize();
	}
	/**
	 * Updates the local version information.
	 */
	public void updateVersionInformation() {
		this.version = this.currProject.getVersionInformation().getCopy();
		this.getJTextFieldMajorVersion().setText(this.version.getMajorRevision().toString());
		this.getJTextFieldMinorVersion().setText(this.version.getMinorRevision().toString());
		this.getJTextFieldBuildNo().setText(this.version.getBuild().toString());
	}
	
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelVersion = new GridBagConstraints();
		gbc_jLabelVersion.anchor = GridBagConstraints.WEST;
		gbc_jLabelVersion.gridwidth = 2;
		gbc_jLabelVersion.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelVersion.gridx = 0;
		gbc_jLabelVersion.gridy = 0;
		add(getJLabelVersion(), gbc_jLabelVersion);
		GridBagConstraints gbc_jButtonEditVersion = new GridBagConstraints();
		gbc_jButtonEditVersion.anchor = GridBagConstraints.EAST;
		gbc_jButtonEditVersion.insets = new Insets(0, 0, 5, 0);
		gbc_jButtonEditVersion.gridx = 2;
		gbc_jButtonEditVersion.gridy = 0;
		add(getJButtonEditVersion(), gbc_jButtonEditVersion);
		GridBagConstraints gbc_jLabelMajorVersion = new GridBagConstraints();
		gbc_jLabelMajorVersion.insets = new Insets(0, 0, 0, 5);
		gbc_jLabelMajorVersion.anchor = GridBagConstraints.WEST;
		gbc_jLabelMajorVersion.gridx = 0;
		gbc_jLabelMajorVersion.gridy = 1;
		add(getJLabelMajorVersion(), gbc_jLabelMajorVersion);
		GridBagConstraints gbc_jTextFieldMajorVersion = new GridBagConstraints();
		gbc_jTextFieldMajorVersion.gridwidth = 2;
		gbc_jTextFieldMajorVersion.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldMajorVersion.gridx = 1;
		gbc_jTextFieldMajorVersion.gridy = 1;
		add(getJTextFieldMajorVersion(), gbc_jTextFieldMajorVersion);
		GridBagConstraints gbc_jLableMinorVersion = new GridBagConstraints();
		gbc_jLableMinorVersion.anchor = GridBagConstraints.WEST;
		gbc_jLableMinorVersion.insets = new Insets(0, 0, 5, 5);
		gbc_jLableMinorVersion.gridx = 0;
		gbc_jLableMinorVersion.gridy = 2;
		add(getJLableMinorVersion(), gbc_jLableMinorVersion);
		GridBagConstraints gbc_jTextFieldMinorVersion = new GridBagConstraints();
		gbc_jTextFieldMinorVersion.gridwidth = 2;
		gbc_jTextFieldMinorVersion.insets = new Insets(0, 0, 5, 0);
		gbc_jTextFieldMinorVersion.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldMinorVersion.gridx = 1;
		gbc_jTextFieldMinorVersion.gridy = 2;
		add(getJTextFieldMinorVersion(), gbc_jTextFieldMinorVersion);
		GridBagConstraints gbc_jLabelBuildNo = new GridBagConstraints();
		gbc_jLabelBuildNo.anchor = GridBagConstraints.SOUTHWEST;
		gbc_jLabelBuildNo.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelBuildNo.gridx = 0;
		gbc_jLabelBuildNo.gridy = 3;
		add(getJLabelBuildNo(), gbc_jLabelBuildNo);
		GridBagConstraints gbc_jTextFieldBuildNo = new GridBagConstraints();
		gbc_jTextFieldBuildNo.anchor = GridBagConstraints.SOUTH;
		gbc_jTextFieldBuildNo.gridwidth = 2;
		gbc_jTextFieldBuildNo.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldBuildNo.gridx = 1;
		gbc_jTextFieldBuildNo.gridy = 3;
		add(getJTextFieldBuildNo(), gbc_jTextFieldBuildNo);
	}

	private JLabel getJLabelVersion() {
		if (jLabelVersion == null) {
			jLabelVersion = new JLabel();
			jLabelVersion.setText(Language.translate("Version"));
			jLabelVersion.setFont(new Font("Dialog", Font.BOLD, 14));
			jLabelVersion.setPreferredSize(new Dimension(100, 26));
			jLabelVersion.setMinimumSize(new Dimension(100, 26));
		}
		return jLabelVersion;
	}
	private JButton getJButtonEditVersion() {
		if (jButtonEditVersion == null) {
			jButtonEditVersion = new JButton();
			jButtonEditVersion.setToolTipText(Language.translate("Versionnummer speichern"));
			jButtonEditVersion.setIcon(GlobalInfo.getInternalImageIcon("MBsave.png"));
			jButtonEditVersion.setPreferredSize(new Dimension(45, 26));
			jButtonEditVersion.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					boolean isErrorExit = false;
					if (getJTextFieldMajorVersion().getText()==null || getJTextFieldMajorVersion().getText().equals("")) {
						getJTextFieldMajorVersion().setText(version.getMajorRevision().toString());
						isErrorExit = true;
					}
					if (getJTextFieldMinorVersion().getText()==null || getJTextFieldMinorVersion().getText().equals("")) {
						getJTextFieldMinorVersion().setText(version.getMinorRevision().toString());
						isErrorExit = true;
					}
					if (getJTextFieldBuildNo().getText()==null || getJTextFieldBuildNo().getText().equals("")) {
						getJTextFieldBuildNo().setText(version.getBuild().toString());
						isErrorExit = true;
					}
					if (isErrorExit==true) return;
					
					version.setMajorRevision(Integer.parseInt(getJTextFieldMajorVersion().getText()));
					version.setMinorRevision(Integer.parseInt(getJTextFieldMinorVersion().getText()));
					version.setBuild(Integer.parseInt(getJTextFieldBuildNo().getText()));
					currProject.setVersionInformation(version);
					
					projectInfo.switchJPanelVersion();
				}
			});
		}
		return jButtonEditVersion;
	}
	
	private JLabel getJLabelMajorVersion() {
		if (jLabelMajorVersion == null) {
			jLabelMajorVersion = new JLabel("Major");
			jLabelMajorVersion.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelMajorVersion;
	}
	private JTextField getJTextFieldMajorVersion() {
		if (jTextFieldMajorVersion == null) {
			jTextFieldMajorVersion = new JTextField(this.version.getMajorRevision().toString());
			jTextFieldMajorVersion.setHorizontalAlignment(SwingConstants.CENTER);
			jTextFieldMajorVersion.addKeyListener(this.getKeyAdapter4Numbers());
		}
		return jTextFieldMajorVersion;
	}
	
	private JLabel getJLableMinorVersion() {
		if (jLableMinorVersion == null) {
			jLableMinorVersion = new JLabel("Minor");
			jLableMinorVersion.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableMinorVersion;
	}
	private JTextField getJTextFieldMinorVersion() {
		if (jTextFieldMinorVersion == null) {
			jTextFieldMinorVersion = new JTextField(this.version.getMinorRevision().toString());
			jTextFieldMinorVersion.setHorizontalAlignment(SwingConstants.CENTER);
			jTextFieldMinorVersion.addKeyListener(this.getKeyAdapter4Numbers());
		}
		return jTextFieldMinorVersion;
	}
	
	private JLabel getJLabelBuildNo() {
		if (jLabelBuildNo == null) {
			jLabelBuildNo = new JLabel("Build");
			jLabelBuildNo.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelBuildNo;
	}
	private JTextField getJTextFieldBuildNo() {
		if (jTextFieldBuildNo == null) {
			jTextFieldBuildNo = new JTextField(this.version.getBuild().toString());
			jTextFieldBuildNo.setHorizontalAlignment(SwingConstants.CENTER);
			jTextFieldBuildNo.addKeyListener(this.getKeyAdapter4Numbers());
		}
		return jTextFieldBuildNo;
	}
	
	private KeyAdapter4Numbers getKeyAdapter4Numbers() {
		if (keyAdapter4Numbers==null) {
			keyAdapter4Numbers = new KeyAdapter4Numbers(false);
		}
		return keyAdapter4Numbers;
	}
	
	
}
