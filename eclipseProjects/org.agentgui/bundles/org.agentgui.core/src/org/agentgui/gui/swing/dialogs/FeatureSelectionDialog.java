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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;

import agentgui.core.application.Language;
import de.enflexit.common.featureEvaluation.FeatureEvaluator;
import de.enflexit.common.featureEvaluation.FeatureInfo;

/**
 * The Class FeatureSelectionDialog.
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class FeatureSelectionDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -9120650393031177126L;
	
	private Vector<FeatureInfo> featureVector;

	private boolean canceled;

	private JScrollPane jScrollPaneFeaturesList;
	private JPanel jPanelButtons;
	private JButton jButtonOk;
	private JButton jButtonCancel;
	private JList<IInstallableUnit> jListFeatures;
	private DefaultListModel<IInstallableUnit> listModelFeatures;
	private JLabel jLabelHeader;

	private List<IInstallableUnit> availableFeatures;
	private JLabel jLabelInfo;

	
	/**
	 * Instantiates a new feature selection dialog.
	 *
	 * @param ownerFrame the owner frame
	 * @param featureVector the feature vector
	 * @param availableFeatures the available features
	 */
	public FeatureSelectionDialog(Frame ownerFrame, Vector<FeatureInfo> featureVector, List<IInstallableUnit> availableFeatures) {
		super(ownerFrame);
		this.featureVector = featureVector;
		this.availableFeatures = availableFeatures;
		this.initialize();
	}

	/**
	 * Initialize.
	 */
	private void initialize() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.NORTH;
		gbc_jLabelHeader.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelHeader.insets = new Insets(10, 10, 0, 10);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		getContentPane().add(getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jScrollPaneFeaturesList = new GridBagConstraints();
		gbc_jScrollPaneFeaturesList.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneFeaturesList.insets = new Insets(5, 10, 0, 10);
		gbc_jScrollPaneFeaturesList.gridx = 0;
		gbc_jScrollPaneFeaturesList.gridy = 1;
		getContentPane().add(getJScrollPaneFeaturesList(), gbc_jScrollPaneFeaturesList);
		GridBagConstraints gbc_jLabelInfo = new GridBagConstraints();
		gbc_jLabelInfo.anchor = GridBagConstraints.WEST;
		gbc_jLabelInfo.insets = new Insets(5, 10, 0, 10);
		gbc_jLabelInfo.gridx = 0;
		gbc_jLabelInfo.gridy = 2;
		getContentPane().add(getLabelInfo(), gbc_jLabelInfo);
		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.insets = new Insets(10, 10, 15, 10);
		gbc_jPanelButtons.anchor = GridBagConstraints.NORTH;
		gbc_jPanelButtons.fill = GridBagConstraints.HORIZONTAL;
		gbc_jPanelButtons.gridx = 0;
		gbc_jPanelButtons.gridy = 3;
		getContentPane().add(getJPanelButtons(), gbc_jPanelButtons);

		this.setTitle(Language.translate("Features auswählen"));
		this.setSize(800, 450);
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.registerEscapeKeyStroke();
		this.setVisible(true);
	}

	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
    			setVisible(false);
    			setCanceled(true);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
	
	/**
	 * Gets the j label header.
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
	 * Gets the j scroll pane features list.
	 * @return the j scroll pane features list
	 */
	private JScrollPane getJScrollPaneFeaturesList() {
		if (jScrollPaneFeaturesList == null) {
			jScrollPaneFeaturesList = new JScrollPane();
			jScrollPaneFeaturesList.setViewportView(this.getJListFeatures());
		}
		return jScrollPaneFeaturesList;
	}
	
	/**
	 * Gets the j list features.
	 * @return the j list features
	 */
	private JList<IInstallableUnit> getJListFeatures() {
		if (jListFeatures == null) {
			jListFeatures = new JList<IInstallableUnit>(this.getListModelFeatures());
			jListFeatures.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			jListFeatures.setCellRenderer(new CheckBoxListCellRenderer());
			jListFeatures.setSelectionModel(new ListSelectionModelForJCheckBox());
			this.setFeatureSelection(this.featureVector);
		}
		return jListFeatures;
	}
	/**
	 * Gets the list model features.
	 * @return the list model features
	 */
	private DefaultListModel<IInstallableUnit> getListModelFeatures() {
		if (listModelFeatures == null) {
			listModelFeatures = new DefaultListModel<IInstallableUnit>();
			Collections.sort(this.availableFeatures, new IUComparator());
			for (IInstallableUnit feature : this.availableFeatures) {
				listModelFeatures.addElement(feature);
			}
		}
		return listModelFeatures;
	}

	/**
	 * Sets the selection in the {@link JList} according to the given list of features
	 * 
	 * @param activeFeatures the list of features
	 */
	private void setFeatureSelection(List<FeatureInfo> activeFeatures) {
		if (activeFeatures.isEmpty() == false) {
			int[] selectedIndices = new int[activeFeatures.size()];
			int currIndex = 0;
			for (int i = 0; i < this.getListModelFeatures().size(); i++) {
				String featureID = this.getListModelFeatures().getElementAt(i).getId();
				if (this.isFeatureInList(activeFeatures, featureID)) {
					selectedIndices[currIndex++] = i;
				}
			}

			this.getJListFeatures().setSelectedIndices(selectedIndices);
		}
	}

	/**
	 * Checks if the feature with the given ID is in the list
	 * 
	 * @param featuresList The list
	 * @param featureID The ID
	 * @return
	 */
	private boolean isFeatureInList(List<FeatureInfo> featuresList, String featureID) {
		for (FeatureInfo feature : featuresList) {
			if (feature.getId().equals(featureID)) {
				return true;
			}
		}
		return false;
	}

	private JLabel getLabelInfo() {
		if (jLabelInfo == null) {
			jLabelInfo = new JLabel(Language.translate("'#' markiert alle Features, die zur Basisinstallation gehören."));
			jLabelInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelInfo;
	}
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
			gbc_jButtonOk.insets = new Insets(5, 0, 5, 20);
			gbc_jButtonOk.gridx = 0;
			gbc_jButtonOk.gridy = 0;
			jPanelButtons.add(getJButtonOk(), gbc_jButtonOk);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(5, 20, 5, 0);
			gbc_jButtonCancel.anchor = GridBagConstraints.WEST;
			gbc_jButtonCancel.weightx = 1.0;
			gbc_jButtonCancel.gridx = 1;
			gbc_jButtonCancel.gridy = 0;
			jPanelButtons.add(getJButtonCancel(), gbc_jButtonCancel);
		}
		return jPanelButtons;
	}
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton("OK");
			jButtonOk.setForeground(new Color(0, 153, 0));
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.setPreferredSize(new Dimension(80, 26));
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setPreferredSize(new Dimension(80, 26));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
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
	 * Sets the action in the dialog to be canceled.
	 * @param canceled the new canceled
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
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
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == this.getJButtonOk()) {
			this.setCanceled(false);
			this.dispose();
		} else {
			this.setCanceled(true);
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
			
			boolean isBasePart = FeatureEvaluator.getInstance().isFeatureOfBaseInstallation(iu);
			this.setText(FeatureSelectionDialog.this.getDisplayTextForInstallableUnit(iu));
			
			if (isBasePart==true) {
				this.setSelected(true);
				this.setEnabled(false);
			} else {
				this.setSelected(isSelected);	
				this.setEnabled(list.isEnabled());
			}
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

	/**
	 * A {@link Comparator} implementation that compares {@link IInstallableUnit}s by their name or ID
	 * 
	 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class IUComparator implements Comparator<IInstallableUnit> {
		@Override
		public int compare(IInstallableUnit iu1, IInstallableUnit iu2) {
			String displayText1 = FeatureSelectionDialog.this.getDisplayTextForInstallableUnit(iu1);
			String displayText2 = FeatureSelectionDialog.this.getDisplayTextForInstallableUnit(iu2);
			return displayText1.compareTo(displayText2);
		}
	}
	/**
	 * Gets the display text for an {@link IInstallableUnit}, which is either the name or the ID.
	 *
	 * @param installableUnit the installable unit
	 * @return the display text for the installable unit
	 */
	private String getDisplayTextForInstallableUnit(IInstallableUnit installableUnit) {

		// --- Try to get the description -----------------
		String displalyText = FeatureEvaluator.getInstance().getIInstallableUnitDescription(installableUnit);
		if (displalyText==null || displalyText.isEmpty()) {
			// --- Get the IU's name property -------------
			displalyText = installableUnit.getProperty(IInstallableUnit.PROP_NAME);
			
			// --- If not set, use the ID instead ---------
			if (displalyText==null || displalyText.equals("%featureName")) {
				displalyText = installableUnit.getId();
			}
		}
		return displalyText;
	}

}
