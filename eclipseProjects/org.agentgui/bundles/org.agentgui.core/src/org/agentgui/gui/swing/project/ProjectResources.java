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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import de.enflexit.common.Observable;
import de.enflexit.common.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.plugin.PlugIn;
import agentgui.core.plugin.PlugInError;
import agentgui.core.plugin.PlugInListElement;
import agentgui.core.plugin.PlugInNotification;
import agentgui.core.plugin.PlugInsLoaded;
import agentgui.core.project.Project;
import de.enflexit.common.classSelection.ClassSelectionDialog;
import de.enflexit.common.featureEvaluation.FeatureInfo;

/**
 * Represents the JPanel/Tab 'Configuration' - 'Resources'
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectResources extends JScrollPane implements Observer {

	private static final long serialVersionUID = 1L;

	private Dimension preferredListFirstRow = new Dimension(180, 180);
	private Dimension preferredListSecondRow = new Dimension(180, 220);

	private Project currProject;

	private JPanel jPanelContent;
	private JPanel jPanelPlugInButtons;

	private JScrollPane jScrollPanePlugIns;

	private JList<PlugInListElement> jListPlugIns;
	private DefaultListModel<PlugInListElement> listModelPlugIns;
	
	private JPanel jPanelOsgiBundles;
	private JScrollPane jScrollPaneBundelJars;
	private JList<String> jListBundleJars;
	private JLabel jLabelBundleResources;
	
	private JSeparator jSeparatorTop;

	private JLabel jLabelFeatures;
	private FeaturePanel jPanelFeatureList;

	private JLabel jLabelPlugIns;
	private JButton jButtonAddPlugIns;
	private JButton jButtonRemovePlugIns;
	private JButton jButtonRefreshPlugIns;
	private JPanel jPanelProjectPlugIns;
	private JPanel jPanelFeatures;
	private EnvironmentModelSelector environmentModelSelector;
	private JLabel jLabelResourcesHeader;
	
	
	/**
	 * Instantiates a new visual representation for the project resources.
	 * 
	 * @param project the current project
	 */
	public ProjectResources(Project project) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);

		this.initialize();
		this.setViewAccordingToProject();
	}

	/**
	 * Initialize this JPanel.
	 */
	private void initialize() {
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setViewportView(this.getJPanelContent());
	}

	/**
	 * Returns the JPanel for the content.
	 * @return the j panel content
	 */
	private JPanel getJPanelContent() {
		if (jPanelContent == null) {

			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
			gridBagLayout.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
			gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
			gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
			
			jPanelContent = new JPanel();
			jPanelContent.setLayout(gridBagLayout);
			GridBagConstraints gbc_jLabelResourcesHeader = new GridBagConstraints();
			gbc_jLabelResourcesHeader.gridwidth = 2;
			gbc_jLabelResourcesHeader.insets = new Insets(10, 10, 0, 10);
			gbc_jLabelResourcesHeader.anchor = GridBagConstraints.WEST;
			gbc_jLabelResourcesHeader.gridx = 0;
			gbc_jLabelResourcesHeader.gridy = 0;
			jPanelContent.add(getJLabelResourcesHeader(), gbc_jLabelResourcesHeader);
			GridBagConstraints gbc_jPanelFeatures = new GridBagConstraints();
			gbc_jPanelFeatures.gridwidth = 2;
			gbc_jPanelFeatures.insets = new Insets(10, 10, 0, 10);
			gbc_jPanelFeatures.fill = GridBagConstraints.BOTH;
			gbc_jPanelFeatures.gridx = 0;
			gbc_jPanelFeatures.gridy = 1;
			jPanelContent.add(getJPanelFeatures(), gbc_jPanelFeatures);
			GridBagConstraints gbc_jPanelOsgiBundles = new GridBagConstraints();
			gbc_jPanelOsgiBundles.insets = new Insets(5, 10, 0, 5);
			gbc_jPanelOsgiBundles.fill = GridBagConstraints.BOTH;
			gbc_jPanelOsgiBundles.gridx = 0;
			gbc_jPanelOsgiBundles.gridy = 2;
			jPanelContent.add(getJPanelOsgiBundles(), gbc_jPanelOsgiBundles);
			GridBagConstraints gbc_jPanelProjectPlugIns = new GridBagConstraints();
			gbc_jPanelProjectPlugIns.fill = GridBagConstraints.BOTH;
			gbc_jPanelProjectPlugIns.insets = new Insets(5, 5, 0, 10);
			gbc_jPanelProjectPlugIns.gridx = 1;
			gbc_jPanelProjectPlugIns.gridy = 2;
			jPanelContent.add(getJPanelProjectPlugIns(), gbc_jPanelProjectPlugIns);
			GridBagConstraints gbc_jSeparatorTop = new GridBagConstraints();
			gbc_jSeparatorTop.gridwidth = 2;
			gbc_jSeparatorTop.fill = GridBagConstraints.HORIZONTAL;
			gbc_jSeparatorTop.insets = new Insets(10, 10, 0, 10);
			gbc_jSeparatorTop.gridx = 0;
			gbc_jSeparatorTop.gridy = 3;
			jPanelContent.add(getJSeparatorTop(), gbc_jSeparatorTop);
			GridBagConstraints gbc_environmentModel = new GridBagConstraints();
			gbc_environmentModel.gridwidth = 2;
			gbc_environmentModel.insets = new Insets(10, 10, 15, 10);
			gbc_environmentModel.fill = GridBagConstraints.BOTH;
			gbc_environmentModel.gridx = 0;
			gbc_environmentModel.gridy = 4;
			jPanelContent.add(getEnvironmentModelSelector(), gbc_environmentModel);

		}
		return jPanelContent;
	}
	private JLabel getJLabelResourcesHeader() {
		if (jLabelResourcesHeader == null) {
			jLabelResourcesHeader = new JLabel(Language.translate("Eingebundene Ressourcen"));
			jLabelResourcesHeader.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return jLabelResourcesHeader;
	}
	private JPanel getJPanelFeatures() {
		if (jPanelFeatures == null) {
			jPanelFeatures = new JPanel();
			
			GridBagLayout gbl_jPanelFeatures = new GridBagLayout();
			gbl_jPanelFeatures.columnWidths = new int[]{0, 0};
			gbl_jPanelFeatures.rowHeights = new int[]{0, 0, 0};
			gbl_jPanelFeatures.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_jPanelFeatures.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			jPanelFeatures.setLayout(gbl_jPanelFeatures);
			
			GridBagConstraints gbc_jLabelFeatures = new GridBagConstraints();
			gbc_jLabelFeatures.insets = new Insets(0, 0, 5, 0);
			gbc_jLabelFeatures.anchor = GridBagConstraints.WEST;
			gbc_jLabelFeatures.gridx = 0;
			gbc_jLabelFeatures.gridy = 0;
			jPanelFeatures.add(this.getJLabelFeatures(), gbc_jLabelFeatures);
			
			GridBagConstraints gbc_jPanelFeatureList = new GridBagConstraints();
			gbc_jPanelFeatureList.fill = GridBagConstraints.HORIZONTAL;
			gbc_jPanelFeatureList.gridx = 0;
			gbc_jPanelFeatureList.gridy = 1;
			jPanelFeatures.add(this.getFeaturePanel(), gbc_jPanelFeatureList);
		}
		return jPanelFeatures;
	}
	private JLabel getJLabelFeatures() {
		if (jLabelFeatures == null) {
			jLabelFeatures = new JLabel("AWB-Features");
			jLabelFeatures.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelFeatures;
	}
	private FeaturePanel getFeaturePanel() {
		if (jPanelFeatureList == null) {
			jPanelFeatureList = new FeaturePanel(this.currProject.getProjectFeatures()) {
				private static final long serialVersionUID = 7544963863606226622L;
				@Override
				public void addedFeatureInfo(FeatureInfo addedFeatureInfo) {
					ProjectResources.this.currProject.addProjectFeature(addedFeatureInfo);
				}
				@Override
				public void updatedFeatureInfo(FeatureInfo editedFeatureInfo) {
					ProjectResources.this.currProject.setChangedAndNotify(Project.CHANGED_ProjectResources);
				}
				@Override
				public void removedFeatureInfo(FeatureInfo removedFeatureInfo) {
					ProjectResources.this.currProject.removeProjectFeature(removedFeatureInfo);
				}
				@Override
				public void updateFeatureInfos() {
					ProjectResources.this.currProject.determineRequiredFeatures();
				}
				
			};
			jPanelFeatureList.setPreferredSize(this.preferredListFirstRow);
		}
		return jPanelFeatureList;
	}
	
	
	private JPanel getJPanelOsgiBundles() {
		if (jPanelOsgiBundles == null) {
			jPanelOsgiBundles = new JPanel();
			GridBagLayout gbl_jPanelOsgiBundles = new GridBagLayout();
			gbl_jPanelOsgiBundles.columnWidths = new int[] { 0, 0 };
			gbl_jPanelOsgiBundles.rowHeights = new int[] { 0, 0, 0 };
			gbl_jPanelOsgiBundles.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			gbl_jPanelOsgiBundles.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
			jPanelOsgiBundles.setLayout(gbl_jPanelOsgiBundles);
			GridBagConstraints gbc_jLabelBundleResources = new GridBagConstraints();
			gbc_jLabelBundleResources.insets = new Insets(0, 0, 5, 0);
			gbc_jLabelBundleResources.anchor = GridBagConstraints.WEST;
			gbc_jLabelBundleResources.gridx = 0;
			gbc_jLabelBundleResources.gridy = 0;
			jPanelOsgiBundles.add(getJLabelBundleResources(), gbc_jLabelBundleResources);
			GridBagConstraints gbc_jScrollPaneBundelJars = new GridBagConstraints();
			gbc_jScrollPaneBundelJars.fill = GridBagConstraints.BOTH;
			gbc_jScrollPaneBundelJars.gridx = 0;
			gbc_jScrollPaneBundelJars.gridy = 1;
			jPanelOsgiBundles.add(getJScrollPaneBundelJars(), gbc_jScrollPaneBundelJars);
		}
		return jPanelOsgiBundles;
	}
	private JLabel getJLabelBundleResources() {
		if (jLabelBundleResources == null) {
			jLabelBundleResources = new JLabel(Language.translate("Projekt") + " OSGI-Bundles");
			jLabelBundleResources.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelBundleResources;
	}
	private JScrollPane getJScrollPaneBundelJars() {
		if (jScrollPaneBundelJars == null) {
			jScrollPaneBundelJars = new JScrollPane();
			jScrollPaneBundelJars.setPreferredSize(this.preferredListSecondRow);
			jScrollPaneBundelJars.setViewportView(getJListBundleJars());
		}
		return jScrollPaneBundelJars;
	}
	private JList<String> getJListBundleJars() {
		if (jListBundleJars == null) {
			jListBundleJars = new JList<String>(this.currProject.getProjectBundleLoader().getBundleJarsListModel());
			jListBundleJars.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jListBundleJars;
	}


	/**
	 * This method initializes jScrollPanePlugIns
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPanePlugIns() {
		if (jScrollPanePlugIns == null) {
			jScrollPanePlugIns = new JScrollPane();
			jScrollPanePlugIns.setPreferredSize(this.preferredListSecondRow);
			jScrollPanePlugIns.setViewportView(this.getJListPlugIns());
		}
		return jScrollPanePlugIns;
	}

	
	private JPanel getJPanelProjectPlugIns() {
		if (jPanelProjectPlugIns == null) {
			jPanelProjectPlugIns = new JPanel();
			GridBagLayout gbl_jPanelProjectPlugIns = new GridBagLayout();
			gbl_jPanelProjectPlugIns.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelProjectPlugIns.rowHeights = new int[]{0, 0, 0};
			gbl_jPanelProjectPlugIns.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelProjectPlugIns.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			jPanelProjectPlugIns.setLayout(gbl_jPanelProjectPlugIns);
			GridBagConstraints gbc_jLabelPlugIns = new GridBagConstraints();
			gbc_jLabelPlugIns.insets = new Insets(0, 0, 5, 0);
			gbc_jLabelPlugIns.anchor = GridBagConstraints.WEST;
			gbc_jLabelPlugIns.gridx = 0;
			gbc_jLabelPlugIns.gridy = 0;
			jPanelProjectPlugIns.add(getJLabelPlugIns(), gbc_jLabelPlugIns);
			GridBagConstraints gbc_jScrollPanePlugIns = new GridBagConstraints();
			gbc_jScrollPanePlugIns.insets = new Insets(0, 0, 0, 10);
			gbc_jScrollPanePlugIns.fill = GridBagConstraints.BOTH;
			gbc_jScrollPanePlugIns.gridx = 0;
			gbc_jScrollPanePlugIns.gridy = 1;
			jPanelProjectPlugIns.add(getJScrollPanePlugIns(), gbc_jScrollPanePlugIns);
			GridBagConstraints gbc_jPanelPlugInButtons = new GridBagConstraints();
			gbc_jPanelPlugInButtons.fill = GridBagConstraints.VERTICAL;
			gbc_jPanelPlugInButtons.gridx = 1;
			gbc_jPanelPlugInButtons.gridy = 1;
			jPanelProjectPlugIns.add(getJPanelPlugInButtons(), gbc_jPanelPlugInButtons);
		}
		return jPanelProjectPlugIns;
	}
	public JLabel getJLabelPlugIns() {
		if (jLabelPlugIns == null) {
			jLabelPlugIns = new JLabel();
			jLabelPlugIns.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelPlugIns.setText(Language.translate("AWB Projekt-PlugIns"));
		}
		return jLabelPlugIns;
	}
	/**
	 * This method initializes jListPlugIns
	 * @return javax.swing.JList
	 */
	private JList<PlugInListElement> getJListPlugIns() {
		if (jListPlugIns == null) {
			jListPlugIns = new JList<PlugInListElement>();
			jListPlugIns.setFont(new Font("Dialog", Font.PLAIN, 12));
			jListPlugIns.setModel(this.getListModelPlugIns());
		}
		return jListPlugIns;
	}
	private DefaultListModel<PlugInListElement> getListModelPlugIns() {
		if (listModelPlugIns==null) {
			listModelPlugIns = new DefaultListModel<PlugInListElement>();
		}
		return listModelPlugIns;
	}
	/**
	 * This methods adds a Plugin to the plugInListModel, so that it is displayed
	 * @param plugIn
	 */
	private void addPlugInElement2List(PlugIn plugIn) {
		PlugInListElement pile = new PlugInListElement(plugIn.getName(), plugIn.getClassReference());
		// --- In case of a PlugInError instance ----------
		if (plugIn instanceof PlugInError) {
			PlugInError errorPlugIn = (PlugInError) plugIn;
			pile.setPlugInLoadMessage(errorPlugIn.getThrowable().toString());
		}
		if (this.getListModelPlugIns().contains(pile)==false) {
			this.getListModelPlugIns().addElement(pile);
		}
	}
	/**
	 * This methods removes a Plugin from the plugInListModel
	 * @param plugIn
	 */
	private void removePlugInElement2List(PlugIn plugIn) {
		String plugInRef = plugIn.getClassReference();
		for (int i = 0; i < this.getListModelPlugIns().size(); i++) {
			PlugInListElement pile = this.getListModelPlugIns().get(i);
			if (pile.getPlugInClassReference().equals(plugInRef)) {
				this.getListModelPlugIns().remove(i);
				return;
			}
		}
	}
	/**
	 * Sets the plug ins according to project.
	 */
	private void setPlugInsAccordingToProject() {
		PlugInsLoaded pluginVector = this.currProject.getPlugInsLoaded();
		for (int i = 0; i < pluginVector.size(); i++) {
			this.addPlugInElement2List(pluginVector.get(i));
		}
	}
	
	
	private JPanel getJPanelPlugInButtons() {
		if (jPanelPlugInButtons == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.SOUTH;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints3.gridy = 2;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = -1;
			gridBagConstraints1.insets = new Insets(0, 0, 5, 0);
			gridBagConstraints1.gridy = -1;
			jPanelPlugInButtons = new JPanel();
			GridBagLayout gbl_jPanelPlugInButtons = new GridBagLayout();
			gbl_jPanelPlugInButtons.rowWeights = new double[] { 0.0, 0.0, 1.0 };
			jPanelPlugInButtons.setLayout(gbl_jPanelPlugInButtons);
			jPanelPlugInButtons.add(getJButtonAddPlugIns(), gridBagConstraints1);
			jPanelPlugInButtons.add(getJButtonRemovePlugIns(), gridBagConstraints2);
			jPanelPlugInButtons.add(getJButtonRefreshPlugIns(), gridBagConstraints3);
		}
		return jPanelPlugInButtons;
	}
	
	private JButton getJButtonAddPlugIns() {
		if (jButtonAddPlugIns == null) {
			jButtonAddPlugIns = new JButton();
			jButtonAddPlugIns.setPreferredSize(new Dimension(45, 26));
			jButtonAddPlugIns.setIcon(GlobalInfo.getInternalImageIcon("ListPlus.png"));
			jButtonAddPlugIns.setToolTipText(Language.translate("Hinzufügen"));
			jButtonAddPlugIns.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					Class<?> search4Class = PlugIn.class;
					String search4CurrentValue = null;
					String search4DefaultValue = null;
					String search4Description = Language.translate("PlugIn für Agent.GUI");

					ClassSelectionDialog cs = new ClassSelectionDialog(Application.getMainWindow(), search4Class, search4CurrentValue, search4DefaultValue, search4Description, false);
					cs.setVisible(true);
					// --- act in the dialog ... --------------------
					if (cs.isCanceled() == true) return;

					// ----------------------------------------------
					// --- Class was selected. Proceed it -----------
					String classSelected = cs.getClassSelected();
					cs.dispose();
					cs = null;
					// ----------------------------------------------

					if (classSelected.equals("") == false) {
						// --- add the class to the project PlugIns -
						currProject.plugInLoad(classSelected, true);
						currProject.setUnsaved(true);
					}

				}
			});
		}
		return jButtonAddPlugIns;
	}

	private JButton getJButtonRemovePlugIns() {
		if (jButtonRemovePlugIns == null) {
			jButtonRemovePlugIns = new JButton();
			jButtonRemovePlugIns.setIcon(GlobalInfo.getInternalImageIcon("ListMinus.png"));
			jButtonRemovePlugIns.setPreferredSize(new Dimension(45, 26));
			jButtonRemovePlugIns.setToolTipText(Language.translate("Entfernen"));
			jButtonRemovePlugIns.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					PlugInListElement pile = (PlugInListElement) jListPlugIns.getSelectedValue();
					if (pile == null) return;

					// --- Get the PlugIn -----------------
					PlugIn pi = currProject.getPlugInsLoaded().getPlugIn(pile.getPlugInName());

					// --- Remove the PlugIn --------------
					currProject.plugInRemove(pi, true);
					currProject.setUnsaved(true);
				}
			});

		}
		return jButtonRemovePlugIns;
	}
	private JButton getJButtonRefreshPlugIns() {
		if (jButtonRefreshPlugIns == null) {
			jButtonRefreshPlugIns = new JButton();
			jButtonRefreshPlugIns.setIcon(GlobalInfo.getInternalImageIcon("Refresh.png"));
			jButtonRefreshPlugIns.setPreferredSize(new Dimension(45, 26));
			jButtonRefreshPlugIns.setToolTipText(Language.translate("Neu laden"));
			jButtonRefreshPlugIns.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					currProject.plugInVectorReload();
				}
			});
		}
		return jButtonRefreshPlugIns;
	}
	
	
	private JSeparator getJSeparatorTop() {
		if (jSeparatorTop == null) {
			jSeparatorTop = new JSeparator();
		}
		return jSeparatorTop;
	}
	private EnvironmentModelSelector getEnvironmentModelSelector() {
		if (environmentModelSelector == null) {
			environmentModelSelector = new EnvironmentModelSelector(this.currProject);
		}
		return environmentModelSelector;
	}
	

	/**
	 * Sets the view according to the project.
	 */
	private void setViewAccordingToProject() {
		this.getJListBundleJars().setModel(this.currProject.getProjectBundleLoader().getBundleJarsListModel());
		this.getFeaturePanel().setFeatureVector(this.currProject.getProjectFeatures());
		this.setPlugInsAccordingToProject();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updated) {

		if (updated.toString().equals(PlugIn.CHANGED)) {
			// --- Something happened with a plugIn -------
			PlugInNotification pin = (PlugInNotification) updated;
			int updateReason = pin.getUpdateReason();
			if (updateReason == PlugIn.ADDED) {
				this.addPlugInElement2List(pin.getPlugIn());
			} else if (updateReason == PlugIn.REMOVED) {
				this.removePlugInElement2List(pin.getPlugIn());
			}

		} else if (updated.equals(Project.CHANGED_ProjectResources)) {
			// --- Changes in the resources ---------------
			this.setViewAccordingToProject();
			
		} else if (updated.equals(Project.CHANGED_ProjectFeatures)) {
			// --- Feature changes ------------------------
			this.getFeaturePanel().setFeatureVector(this.currProject.getProjectFeatures());
			
		} 
	}

	
}
