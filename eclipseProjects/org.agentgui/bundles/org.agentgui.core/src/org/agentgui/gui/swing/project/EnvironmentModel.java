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
package org.agentgui.gui.swing.project;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo;
import agentgui.core.environment.EnvironmentType;
import agentgui.core.project.Project;
import agentgui.simulationService.time.TimeModel;
import de.enflexit.common.Language;
import de.enflexit.common.classSelection.ClassSelectionDialog;

/**
 * The Class EnvironmentModel.
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentModel extends JPanel implements Observer {
	
	private static final long serialVersionUID = 7115612858197530835L;

	private Project currProject;
	private JPanel jPanelSimulationEnvironment;
	private JLabel jLabelEnvTyp;
	private JComboBox<EnvironmentType> jComboBoxEnvironmentModelSelector;
	private JPanel jPanelTimeModelSelection;
	private JLabel jLabelTimeModelClass;
	private JTextField jTextFieldTimeModelClass;
	private JButton jButtonDefaultTimeModel;
	private JButton jButtonSelectTimeModel;

	
	/**
	 * Instantiates a new environment model.
	 * @param project the project
	 */
	public EnvironmentModel(Project project) {
		this.currProject = project;
		this.currProject.addObserver(this);
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jPanelSimulationEnvironment = new GridBagConstraints();
		gbc_jPanelSimulationEnvironment.insets = new Insets(10, 10, 0, 0);
		gbc_jPanelSimulationEnvironment.anchor = GridBagConstraints.NORTH;
		gbc_jPanelSimulationEnvironment.fill = GridBagConstraints.HORIZONTAL;
		gbc_jPanelSimulationEnvironment.gridx = 0;
		gbc_jPanelSimulationEnvironment.gridy = 0;
		add(getJPanelSimulationEnvironment(), gbc_jPanelSimulationEnvironment);
		GridBagConstraints gbc_jPanelTimeModelSelection = new GridBagConstraints();
		gbc_jPanelTimeModelSelection.anchor = GridBagConstraints.WEST;
		gbc_jPanelTimeModelSelection.insets = new Insets(10, 10, 0, 0);
		gbc_jPanelTimeModelSelection.fill = GridBagConstraints.VERTICAL;
		gbc_jPanelTimeModelSelection.gridx = 0;
		gbc_jPanelTimeModelSelection.gridy = 1;
		add(getJPanelTimeModelSelection(), gbc_jPanelTimeModelSelection);

	}

	/**
	 * Gets the j panel simulation environment.
	 * @return the j panel simulation environment
	 */
	private JPanel getJPanelSimulationEnvironment() {
		if (jPanelSimulationEnvironment == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints5.gridy = 0;
			jLabelEnvTyp = new JLabel();
			jLabelEnvTyp.setText(Language.translate("Umgebungstyp bzw. -modell für Simulation und Visualisierung"));
			jLabelEnvTyp.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(5, 0, 0, 0);
			jPanelSimulationEnvironment = new JPanel();
			GridBagLayout gbl_jPanelSimulationEnvironment = new GridBagLayout();
			gbl_jPanelSimulationEnvironment.columnWeights = new double[] { 1.0 };
			jPanelSimulationEnvironment.setLayout(gbl_jPanelSimulationEnvironment);
			jPanelSimulationEnvironment.add(getJComboBoxEnvironmentModelSelector(), gridBagConstraints4);
			jPanelSimulationEnvironment.add(jLabelEnvTyp, gridBagConstraints5);
		}
		return jPanelSimulationEnvironment;
	}

	/**
	 * Gets the j combo box environment model selector.
	 * @return the j combo box environment model selector
	 */
	private JComboBox<EnvironmentType> getJComboBoxEnvironmentModelSelector() {
		if (jComboBoxEnvironmentModelSelector == null) {
			jComboBoxEnvironmentModelSelector = new JComboBox<EnvironmentType>();
			jComboBoxEnvironmentModelSelector.setModel(this.currProject.getEnvironmentsComboBoxModel());
			jComboBoxEnvironmentModelSelector.setPreferredSize(new Dimension(400, 25));
			jComboBoxEnvironmentModelSelector.setSelectedItem(this.currProject.getEnvironmentModelType());
			jComboBoxEnvironmentModelSelector.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					EnvironmentType envType = (EnvironmentType) getJComboBoxEnvironmentModelSelector().getSelectedItem();
					String newEnvModel = envType.getInternalKey();
					String oldEnvModel = currProject.getEnvironmentModelName();
					if (newEnvModel.equals(oldEnvModel) == false) {
						currProject.setEnvironmentModelName(newEnvModel);
					}
				}
			});
		}
		return jComboBoxEnvironmentModelSelector;
	}

	/**
	 * Gets the j panel time model selection.
	 * @return the j panel time model selection
	 */
	private JPanel getJPanelTimeModelSelection() {
		if (jPanelTimeModelSelection == null) {
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints29.gridy = 1;
			gridBagConstraints29.gridx = 1;
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.fill = GridBagConstraints.NONE;
			gridBagConstraints51.gridx = 2;
			gridBagConstraints51.gridy = 1;
			gridBagConstraints51.insets = new Insets(5, 5, 0, 0);
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.anchor = GridBagConstraints.WEST;
			gridBagConstraints33.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints33.gridx = 0;
			gridBagConstraints33.gridy = 1;
			gridBagConstraints33.fill = GridBagConstraints.BOTH;
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints41.gridwidth = 3;
			gridBagConstraints41.gridy = 0;
			gridBagConstraints41.gridx = 0;

			jLabelTimeModelClass = new JLabel();
			jLabelTimeModelClass.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelTimeModelClass.setText(Language.translate("Zeitmodell"));

			jPanelTimeModelSelection = new JPanel();
			GridBagLayout gbl_jPanelTimeModelSelection = new GridBagLayout();
			gbl_jPanelTimeModelSelection.columnWeights = new double[] { 1.0, 0.0, 0.0 };
			jPanelTimeModelSelection.setLayout(gbl_jPanelTimeModelSelection);
			jPanelTimeModelSelection.add(jLabelTimeModelClass, gridBagConstraints41);
			jPanelTimeModelSelection.add(getJTextFieldTimeModelClass(), gridBagConstraints33);
			jPanelTimeModelSelection.add(getJButtonDefaultTimeModel(), gridBagConstraints51);
			jPanelTimeModelSelection.add(getJButtonSelectTimeModel(), gridBagConstraints29);
		}
		return jPanelTimeModelSelection;
	}

	/**
	 * This method initializes jTextFieldTimeModelClass
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldTimeModelClass() {
		if (jTextFieldTimeModelClass == null) {
			jTextFieldTimeModelClass = new JTextField();
			jTextFieldTimeModelClass.setPreferredSize(new Dimension(400, 26));
			jTextFieldTimeModelClass.setText(this.currProject.getTimeModelClass());
			jTextFieldTimeModelClass.setText(this.currProject.getTimeModelClass());
			jTextFieldTimeModelClass.setEditable(false);
		}
		return jTextFieldTimeModelClass;
	}

	/**
	 * This method initializes jButtonDefaultTimeModel
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonDefaultTimeModel() {
		if (jButtonDefaultTimeModel == null) {
			jButtonDefaultTimeModel = new JButton();
			jButtonDefaultTimeModel.setPreferredSize(new Dimension(45, 26));
			jButtonDefaultTimeModel.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonDefaultTimeModel.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonDefaultTimeModel.setToolTipText("Agent.GUI - Standard verwenden");
			jButtonDefaultTimeModel.setToolTipText(Language.translate(jButtonDefaultTimeModel.getToolTipText()));
			jButtonDefaultTimeModel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					currProject.setTimeModelClass(null);
				}
			});
		}
		return jButtonDefaultTimeModel;
	}

	/**
	 * This method initializes jButtonSelectTimeModel
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonSelectTimeModel() {
		if (jButtonSelectTimeModel == null) {
			jButtonSelectTimeModel = new JButton();
			jButtonSelectTimeModel.setPreferredSize(new Dimension(45, 26));
			jButtonSelectTimeModel.setIcon(GlobalInfo.getInternalImageIcon("Search.png"));
			jButtonSelectTimeModel.setToolTipText("Klasse auswählen");
			jButtonSelectTimeModel.setToolTipText(Language.translate(jButtonSelectTimeModel.getToolTipText()));
			jButtonSelectTimeModel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectTimeModelClass();
				}
			});

		}
		return jButtonSelectTimeModel;
	}

	/**
	 * Select the TimeModel class for this Project.
	 * @return the selected TimeModel class
	 */
	private void selectTimeModelClass() {

		Class<?> search4Class = null;
		String search4CurrentValue = null;
		String search4DefaultValue = null;
		String search4Description = null;

		search4Class = TimeModel.class;
		search4CurrentValue = this.currProject.getTimeModelClass();
		search4DefaultValue = null;
		search4Description = jLabelTimeModelClass.getText();

		ClassSelectionDialog cs = new ClassSelectionDialog(Application.getMainWindow(), search4Class, search4CurrentValue, search4DefaultValue, search4Description, false);
		cs.setVisible(true);
		// --- act in the dialog ... --------------------
		if (cs.isCanceled() == false) {
			if (cs.getClassSelected() != null && cs.getClassSelected().length() != 0) {
				this.currProject.setTimeModelClass(cs.getClassSelected());
			}
		}
		cs.dispose();
		cs = null;
		// ----------------------------------------------
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object updated) {
		if (updated.equals(Project.CHANGED_EnvironmentModelType)) {
			EnvironmentType envTypeOld = (EnvironmentType) this.getJComboBoxEnvironmentModelSelector().getSelectedItem();
			EnvironmentType envTypeNew = this.currProject.getEnvironmentModelType();
			if (envTypeOld.equals(envTypeNew) == false) {
				this.getJComboBoxEnvironmentModelSelector().setSelectedItem(this.currProject.getEnvironmentModelType());
			}

		} else if (updated.equals(Project.CHANGED_TimeModelClass)) {
			this.getJTextFieldTimeModelClass().setText(this.currProject.getTimeModelClass());

		}
	}
	
}
