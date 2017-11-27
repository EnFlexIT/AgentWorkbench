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
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;

import agentgui.core.application.Language;

/**
 * The Class FeatureSelectionDialog.
 */
public class FeatureSelectionDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -9120650393031177126L;
	private JScrollPane jScrollPaneFeaturesList;
	private JPanel jPanelButtons;
	private JButton jButtonOk;
	private JButton jButtonCancel;
	private JList<IInstallableUnit> jListFeatures;
	private DefaultListModel<IInstallableUnit> listModelFeatures;
	private JLabel jLabelHeader;

	private List<IInstallableUnit> availableFeatures;

	private boolean canceled;

	/**
	 * Instantiates a new feature selection dialog.
	 *
	 * @param availableFeatures the available features
	 */
	public FeatureSelectionDialog(List<IInstallableUnit> availableFeatures) {
		this.availableFeatures = availableFeatures;
		this.initialize();
	}

	/**
	 * Initialize.
	 */
	private void initialize() {

		this.setTitle(Language.translate("Features auswählen"));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.NORTH;
		gbc_jLabelHeader.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelHeader.insets = new Insets(10, 10, 5, 0);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		getContentPane().add(getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jScrollPaneFeaturesList = new GridBagConstraints();
		gbc_jScrollPaneFeaturesList.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneFeaturesList.insets = new Insets(5, 5, 5, 5);
		gbc_jScrollPaneFeaturesList.gridx = 0;
		gbc_jScrollPaneFeaturesList.gridy = 1;
		getContentPane().add(getJScrollPaneFeaturesList(), gbc_jScrollPaneFeaturesList);
		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.insets = new Insets(5, 5, 5, 5);
		gbc_jPanelButtons.anchor = GridBagConstraints.NORTH;
		gbc_jPanelButtons.fill = GridBagConstraints.HORIZONTAL;
		gbc_jPanelButtons.gridx = 0;
		gbc_jPanelButtons.gridy = 2;
		getContentPane().add(getJPanelButtons(), gbc_jPanelButtons);

		this.setSize(450, 350);
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * Gets the j scroll pane features list.
	 *
	 * @return the j scroll pane features list
	 */
	private JScrollPane getJScrollPaneFeaturesList() {
		if (jScrollPaneFeaturesList == null) {
			jScrollPaneFeaturesList = new JScrollPane();
			jScrollPaneFeaturesList.setViewportView(getJListFeatures());
		}
		return jScrollPaneFeaturesList;
	}

	/**
	 * Gets the j panel buttons.
	 *
	 * @return the j panel buttons
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_jPanelButtons = new GridBagLayout();
			gbl_jPanelButtons.columnWidths = new int[] { 0, 0, 0 };
			gbl_jPanelButtons.rowHeights = new int[] { 0, 0 };
			gbl_jPanelButtons.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
			gbl_jPanelButtons.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			jPanelButtons.setLayout(gbl_jPanelButtons);
			GridBagConstraints gbc_jButtonOk = new GridBagConstraints();
			gbc_jButtonOk.anchor = GridBagConstraints.EAST;
			gbc_jButtonOk.weightx = 1.0;
			gbc_jButtonOk.insets = new Insets(5, 0, 5, 15);
			gbc_jButtonOk.gridx = 0;
			gbc_jButtonOk.gridy = 0;
			jPanelButtons.add(getJButtonOk(), gbc_jButtonOk);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(5, 15, 5, 0);
			gbc_jButtonCancel.anchor = GridBagConstraints.WEST;
			gbc_jButtonCancel.weightx = 1.0;
			gbc_jButtonCancel.gridx = 1;
			gbc_jButtonCancel.gridy = 0;
			jPanelButtons.add(getJButtonCancel(), gbc_jButtonCancel);
		}
		return jPanelButtons;
	}

	/**
	 * Gets the j button ok.
	 *
	 * @return the j button ok
	 */
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton("OK");
			jButtonOk.setForeground(new Color(0, 153, 0));
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}

	/**
	 * Gets the j button cancel.
	 *
	 * @return the j button cancel
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	/**
	 * Gets the j list features.
	 *
	 * @return the j list features
	 */
	private JList<IInstallableUnit> getJListFeatures() {
		if (jListFeatures == null) {
			jListFeatures = new JList<IInstallableUnit>();
			jListFeatures.setModel(getListModelFeatures());
			jListFeatures.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			jListFeatures.setCellRenderer(new CheckBoxListCellRenderer());
			jListFeatures.setSelectionModel(new ListSelectionModelForJCheckBox());
		}
		return jListFeatures;
	}

	/**
	 * Gets the list model features.
	 *
	 * @return the list model features
	 */
	private DefaultListModel<IInstallableUnit> getListModelFeatures() {
		if (listModelFeatures == null) {
			listModelFeatures = new DefaultListModel<IInstallableUnit>();
			for (IInstallableUnit feature : this.availableFeatures) {
				listModelFeatures.addElement(feature);
			}
		}
		return listModelFeatures;
	}

	/**
	 * Gets the j label header.
	 *
	 * @return the j label header
	 */
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel(Language.translate("Vom Projekt benötigte Features:"));
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}

	/**
	 * Checks if is canceled.
	 *
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * Gets the selected features.
	 *
	 * @return the selected features
	 */
	public List<IInstallableUnit> getSelectedFeatures() {
		return this.getJListFeatures().getSelectedValuesList();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.getJButtonOk()) {
			this.canceled = false;
			this.dispose();
		} else {
			this.canceled = true;
			this.dispose();
		}
	}

	/**
	 * {@link ListCellRenderer} implementation based on {@link JCheckBox}.
	 *
	 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class CheckBoxListCellRenderer extends JCheckBox implements ListCellRenderer<IInstallableUnit> {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 6526933726761540148L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean,
		 * boolean)
		 */
		@Override
		public Component getListCellRendererComponent(JList<? extends IInstallableUnit> list, IInstallableUnit iu, int index, boolean isSelected, boolean cellHasFocus) {
			String displayText = iu.getProperty(IInstallableUnit.PROP_NAME);
			if (displayText.equals("%featureName")) {
				// --- Name not defined, use id instead ----------
				displayText = iu.getId();
			}
			this.setText(displayText);
			this.setSelected(isSelected);
			this.setEnabled(list.isEnabled());
			return this;
		}
	}

	/**
	 * {@link ListSelectionModel} that allows to add/remove items to/from the current selection without pressing Crtl.
	 * Useful e.g. for lists using {@link JCheckBox} as {@link ListCellRenderer}
	 * 
	 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class ListSelectionModelForJCheckBox extends DefaultListSelectionModel {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1067181018028459081L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.DefaultListSelectionModel#setSelectionInterval(int, int)
		 */
		@Override
		public void setSelectionInterval(int index0, int index1) {
			if (super.isSelectedIndex(index0)) {
				super.removeSelectionInterval(index0, index1);
			} else {
				super.addSelectionInterval(index0, index1);
			}
		}
	}

}
