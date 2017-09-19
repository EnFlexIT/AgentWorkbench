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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.environment.EnvironmentType;
import agentgui.core.plugin.PlugIn;
import agentgui.core.plugin.PlugInListElement;
import agentgui.core.plugin.PlugInNotification;
import agentgui.core.project.Project;
import agentgui.core.project.ProjectResource2Display;
import agentgui.simulationService.time.TimeModel;
import de.enflexit.common.classSelection.ClassSelectionDialog;

/**
 * Represents the JPanel/Tab 'Configuration' - 'Resources'
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectResources extends JScrollPane implements Observer {

	private static final long serialVersionUID = 1L;
	
	private Dimension preferredListSizeSmall = new Dimension(180, 100);
	private Dimension preferredListSizeLarge = new Dimension(180, 140);
	
	private Project currProject;
	
	private JPanel jPanelContent;
	
	private JPanel jPanelSimulationEnvironment;
	private JPanel jPanelPlugInButtons;
	private JPanel jPanelBinResourceButton;
	
	private JComboBox<EnvironmentType> jComboBoxEnvironmentModelSelector;

	private JScrollPane jScrollPaneBinResources;
	private JScrollPane jScrollPanePlugIns;
	
	private JList<ProjectResource2Display> jListBinResources;
	private JList<PlugInListElement> jListPlugIns;
	private DefaultListModel<PlugInListElement> plugInsListModel = new DefaultListModel<PlugInListElement>();
	
	private JButton jButtonBinResourcesAdd;
	private JButton jButtonBinResourcesRemove;
	private JButton jButtonBinRecourcesRefresh;

	private JButton jButtonAddPlugIns;
	private JButton jButtonRemovePlugIns;
	private JButton jButtonRefreshPlugIns;

	private JLabel jLabelEnvTyp;
	private JLabel jLabelBinResources;
	private JLabel jLabelPlugIns;

	private JPanel jPanelTimeModelSelection;
	private JLabel jLabelTimeModelClass;
	private JTextField jTextFieldTimeModelClass;
	private JButton jButtonDefaultTimeModel;
	private JButton jButtonSelectTimeModel;
	private JCheckBox jCheckboxManifest;
	private JPanel jPanelJarResourcen;
	private JScrollPane jScrollPanePlainJars;
	private JList<String> jListJarResources;
	private JScrollPane jScrollPaneBundelJars;
	private JList<String> jListBundleJars;
	private JLabel jLabelJarResources;
	private JLabel jLabelBundleResources;
	private JPanel jPanelJarFileHeader;
	private JSeparator jSeparatorBottom;
	private JSeparator jSeparatorTop;
	
	
	/**
	 * Instantiates a new visual representation for the project resources.
	 * @param project the current project
	 */
	public ProjectResources(Project project) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);
		
		this.initialize();
		
		// --- Fill the list model for the external resources -------
		this.getJListBinResources().setModel(currProject.getProjectResources().getResourcesListModel());
		// --- Display the current TimeModel class ------------------
		this.getJTextFieldTimeModelClass().setText(this.currProject.getTimeModelClass());

		
		// --- Set the translations ---------------------------------
		this.getJCheckboxManifest().setText(Language.translate("Projekt-MANIFEST.MF beim Öffnen eines Projekts erneuern"));
		
		jLabelEnvTyp.setText(Language.translate("Umgebungstyp bzw. -modell für Simulation und Visualisierung"));
		jLabelBinResources.setText(Language.translate("Externe bin-Ressourcen"));
		jLabelPlugIns.setText(Language.translate("Projekt-PlugIns"));
		
		this.getJLabelJarResources().setText(Language.translate("Einfache jar-Dateien"));
		
		this.getJButtonBinResourcesAdd().setToolTipText(Language.translate("Hinzufügen"));
		this.getJButtonBinResourcesRemove().setToolTipText(Language.translate("Entfernen"));
		this.getJButtonBinResourcesRefresh().setToolTipText(Language.translate("Neu laden"));
		
		this.getJButtonAddPlugIns().setToolTipText(Language.translate("Hinzufügen"));
		this.getJButtonRemovePlugIns().setToolTipText(Language.translate("Entfernen"));
		this.getJButtonRefreshPlugIns().setToolTipText(Language.translate("Neu laden"));
		
	}
	
	/**
	 * Initialize this JPanel.
	 */
	private void initialize() {
		this.setViewportView(this.getjPanelContent());
	}
	/**
	 * Returns the JPanel for the content.
	 * @return the j panel content
	 */
	private JPanel getjPanelContent() {
		if (jPanelContent==null) {
			
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[]{0, 0, 0};
			gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
			gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			
			GridBagConstraints gbcjCheckboxManifest = new GridBagConstraints();
			gbcjCheckboxManifest.anchor = GridBagConstraints.WEST;
			gbcjCheckboxManifest.insets = new Insets(10, 10, 0, 0);
			gbcjCheckboxManifest.gridx = 0;
			gbcjCheckboxManifest.gridy = 0;
			GridBagConstraints gbcJLabelResources = new GridBagConstraints();
			gbcJLabelResources.gridx = 0;
			gbcJLabelResources.anchor = GridBagConstraints.WEST;
			gbcJLabelResources.insets = new Insets(10, 15, 0, 0);
			gbcJLabelResources.gridy = 1;
			GridBagConstraints gbc_jScrollPaneBinResources = new GridBagConstraints();
			gbc_jScrollPaneBinResources.fill = GridBagConstraints.BOTH;
			gbc_jScrollPaneBinResources.gridx = 0;
			gbc_jScrollPaneBinResources.gridy = 2;
			gbc_jScrollPaneBinResources.insets = new Insets(5, 10, 0, 0);
			GridBagConstraints gbc_jPanelBinResourceButton = new GridBagConstraints();
			gbc_jPanelBinResourceButton.fill = GridBagConstraints.VERTICAL;
			gbc_jPanelBinResourceButton.gridx = 1;
			gbc_jPanelBinResourceButton.insets = new Insets(5, 5, 0, 10);
			gbc_jPanelBinResourceButton.gridy = 2;
			GridBagConstraints gbc_jPanelJarFileHeader = new GridBagConstraints();
			gbc_jPanelJarFileHeader.insets = new Insets(10, 10, 0, 0);
			gbc_jPanelJarFileHeader.fill = GridBagConstraints.BOTH;
			gbc_jPanelJarFileHeader.gridx = 0;
			gbc_jPanelJarFileHeader.gridy = 3;
			GridBagConstraints gbc_jPanelJarResourcen = new GridBagConstraints();
			gbc_jPanelJarResourcen.insets = new Insets(5, 10, 0, 0);
			gbc_jPanelJarResourcen.fill = GridBagConstraints.BOTH;
			gbc_jPanelJarResourcen.gridx = 0;
			gbc_jPanelJarResourcen.gridy = 4;
			GridBagConstraints gbc_jSeparatorTop = new GridBagConstraints();
			gbc_jSeparatorTop.gridwidth = 2;
			gbc_jSeparatorTop.fill = GridBagConstraints.HORIZONTAL;
			gbc_jSeparatorTop.insets = new Insets(10, 10, 0, 10);
			gbc_jSeparatorTop.gridx = 0;
			gbc_jSeparatorTop.gridy = 5;
			GridBagConstraints gbcJLabelPlugins = new GridBagConstraints();
			gbcJLabelPlugins.gridx = 0;
			gbcJLabelPlugins.insets = new Insets(10, 15, 0, 0);
			gbcJLabelPlugins.anchor = GridBagConstraints.WEST;
			gbcJLabelPlugins.gridy = 6;
			GridBagConstraints gbcJScrollPanePlugIns = new GridBagConstraints();
			gbcJScrollPanePlugIns.fill = GridBagConstraints.BOTH;
			gbcJScrollPanePlugIns.gridx = 0;
			gbcJScrollPanePlugIns.insets = new Insets(5, 10, 0, 0);
			gbcJScrollPanePlugIns.gridy = 7;
			GridBagConstraints gbcJPanelPlugInButtons = new GridBagConstraints();
			gbcJPanelPlugInButtons.fill = GridBagConstraints.VERTICAL;
			gbcJPanelPlugInButtons.gridx = 1;
			gbcJPanelPlugInButtons.insets = new Insets(5, 5, 0, 10);
			gbcJPanelPlugInButtons.gridy = 7;
			GridBagConstraints gbc_jSeparatorBottom = new GridBagConstraints();
			gbc_jSeparatorBottom.gridwidth = 2;
			gbc_jSeparatorBottom.fill = GridBagConstraints.HORIZONTAL;
			gbc_jSeparatorBottom.insets = new Insets(10, 10, 0, 10);
			gbc_jSeparatorBottom.gridx = 0;
			gbc_jSeparatorBottom.gridy = 8;
			GridBagConstraints gbcJPanelSimulationEnvironment = new GridBagConstraints();
			gbcJPanelSimulationEnvironment.fill = GridBagConstraints.HORIZONTAL;
			gbcJPanelSimulationEnvironment.anchor = GridBagConstraints.NORTH;
			gbcJPanelSimulationEnvironment.gridx = 0;
			gbcJPanelSimulationEnvironment.insets = new Insets(10, 10, 5, 5);
			gbcJPanelSimulationEnvironment.gridy = 9;
			GridBagConstraints gbcJPanelTimeModelSelection = new GridBagConstraints();
			gbcJPanelTimeModelSelection.fill = GridBagConstraints.HORIZONTAL;
			gbcJPanelTimeModelSelection.anchor = GridBagConstraints.NORTH;
			gbcJPanelTimeModelSelection.gridx = 0;
			gbcJPanelTimeModelSelection.insets = new Insets(10, 10, 30, 5);
			gbcJPanelTimeModelSelection.gridy = 10;
			
			jPanelContent = new JPanel();
			jPanelContent.setLayout(gridBagLayout);
			jPanelContent.add(getJCheckboxManifest(), gbcjCheckboxManifest);
			jPanelContent.add(getJScrollPaneBinResources(), gbc_jScrollPaneBinResources);
			jPanelContent.add(getJPanelBinResourceButton(), gbc_jPanelBinResourceButton);
			jPanelContent.add(getJLabelBinResources(), gbcJLabelResources);
			jPanelContent.add(getJPanelJarFileHeader(), gbc_jPanelJarFileHeader);
			jPanelContent.add(getJPanelJarResourcen(), gbc_jPanelJarResourcen);
			jPanelContent.add(getJSeparatorTop(), gbc_jSeparatorTop);
			jPanelContent.add(getJLabelPlugIns(), gbcJLabelPlugins);
			jPanelContent.add(getJScrollPanePlugIns(), gbcJScrollPanePlugIns);
			jPanelContent.add(getJPanelPlugInButtons(), gbcJPanelPlugInButtons);
			jPanelContent.add(getJSeparatorBottom(), gbc_jSeparatorBottom);
			jPanelContent.add(getJPanelSimulationEnvironment(), gbcJPanelSimulationEnvironment);
			jPanelContent.add(getJPanelTimeModelSelection(), gbcJPanelTimeModelSelection);
			
		}
		return jPanelContent;
	}
	
	
	private JCheckBox getJCheckboxManifest() {
		if (jCheckboxManifest == null) {
			jCheckboxManifest = new JCheckBox("Projekt-MANIFEST.MF beim Öffnen eines Projekts erneuern");
			jCheckboxManifest.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckboxManifest.setSelected(this.currProject.isReCreateProjectManifest());
			jCheckboxManifest.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					currProject.setReCreateProjectManifest(getJCheckboxManifest().isSelected());
				}
			});
		}
		return jCheckboxManifest;
	}
	
	private JLabel getJLabelBinResources() {
		if (jLabelBinResources==null) {
			jLabelBinResources = new JLabel();
			jLabelBinResources.setText("Externe bin-Ressourcen");
			jLabelBinResources.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelBinResources;
	}
	/**
	 * This method initializes jScrollPane	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneBinResources() {
		if (jScrollPaneBinResources == null) {
			jScrollPaneBinResources = new JScrollPane();
			jScrollPaneBinResources.setPreferredSize(this.preferredListSizeSmall);
			jScrollPaneBinResources.setViewportView(this.getJListBinResources());
		}
		return jScrollPaneBinResources;
	}
	/**
	 * This method initializes jListResources	
	 * @return javax.swing.JList	
	 */
	private JList<ProjectResource2Display> getJListBinResources() {
		if (jListBinResources == null) {
			jListBinResources = new JList<ProjectResource2Display>();
			jListBinResources.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jListBinResources;
	}

	/**
	 * This method initializes jButtonAdd	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonBinResourcesAdd() {
		if (jButtonBinResourcesAdd == null) {
			jButtonBinResourcesAdd = new JButton();
			jButtonBinResourcesAdd.setPreferredSize(new Dimension(45, 26));
			jButtonBinResourcesAdd.setIcon(GlobalInfo.getInternalImageIcon("ListPlus.png"));
			jButtonBinResourcesAdd.setToolTipText("Add");
			jButtonBinResourcesAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (Application.getJadePlatform().stopAskUserBefore()==true) {

						// --- Check for rebuild of the MANIFEST.MF -----------
						if (currProject.isReCreateProjectManifest()==false) {
							String title = Language.translate("Auktualisierung der Projekt-MANIFEST.MF");
							String msg = "Zum Einbinden von externen bin-Ressourcen, ist\n";
							msg += "eine Aktualisierung der Projekt-MANIFEST.MF erforderlich!\n\n";
							msg += "Soll diese Option jetzt aktualisiert werden?";
							msg = Language.translate(msg);
							
							int answer = JOptionPane.showInternalConfirmDialog (getJListBinResources(), msg, title, JOptionPane.YES_NO_OPTION);
							if (answer!=JOptionPane.YES_OPTION) return;
							// --- Set rebuild of MANIFEST.MF to 'true' -------
							currProject.setReCreateProjectManifest(true);
						}
						
						// --- Select the directory ---------------------------
						JFileChooser chooser = new JFileChooser();
						chooser.setDialogTitle(Language.translate("Build-Verzeichnis aus Java Projekt einbinden (Verzeichnis mit *.class-Dateien)"));
						chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						chooser.setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
						chooser.setMultiSelectionEnabled(false);
						
						int answerChooser = chooser.showDialog(ProjectResources.this, Language.translate("Verzeichnis einbinden"));
						if (answerChooser==JFileChooser.CANCEL_OPTION) return;
						Application.getGlobalInfo().setLastSelectedFolder(chooser.getCurrentDirectory());
						
						// --- Add to bin resources ---------------------------
						File selectedDirectory = chooser.getSelectedFile();
						String selectedDirectoryPath = adjustPathAccordngToProjectsDirectory(selectedDirectory.getAbsolutePath());
						if (isProjectResources(selectedDirectoryPath)==false) {
							currProject.getProjectResources().add(selectedDirectoryPath);
							currProject.resourcesReLoad();
							jListBinResources.updateUI();							
						}
					}
				} // end actionPerformed
			}); // end addActionListener
		}
		return jButtonBinResourcesAdd;
	}

	/**
	 * This method initializes jButtonRemove	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonBinResourcesRemove() {
		if (jButtonBinResourcesRemove == null) {
			jButtonBinResourcesRemove = new JButton();
			jButtonBinResourcesRemove.setIcon(GlobalInfo.getInternalImageIcon("ListMinus.png"));
			jButtonBinResourcesRemove.setPreferredSize(new Dimension(45, 26));
			jButtonBinResourcesRemove.setToolTipText("Remove");
			jButtonBinResourcesRemove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (Application.getJadePlatform().stopAskUserBefore()==true) {
						// --- Remove from the ClassPath ----
						Vector<String> selection = new Vector<String>();
						for (Object fileR2DObject : jListBinResources.getSelectedValuesList()) {
							ProjectResource2Display fileR2D = (ProjectResource2Display) fileR2DObject;
							selection.add(fileR2D.getFileOrFolderResource());
						}
						if (selection.size()>0) {
							currProject.getProjectResources().removeAll(selection);
							currProject.resourcesReLoad();
						}
					}
				}
			});
		}
		return jButtonBinResourcesRemove;
	}

	/**
	 * This method initializes jButtonRefresh	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonBinResourcesRefresh() {
		if (jButtonBinRecourcesRefresh == null) {
			jButtonBinRecourcesRefresh = new JButton();
			jButtonBinRecourcesRefresh.setIcon(GlobalInfo.getInternalImageIcon("Refresh.png"));
			jButtonBinRecourcesRefresh.setPreferredSize(new Dimension(45, 26));
			jButtonBinRecourcesRefresh.setToolTipText("Refresh");
			jButtonBinRecourcesRefresh.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					if (Application.getJadePlatform().stopAskUserBefore()) {
						Application.getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						currProject.resourcesReLoad();
						Application.getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				}
			});
		}
		return jButtonBinRecourcesRefresh;
	}

	
	private JPanel getJPanelJarFileHeader() {
		if (jPanelJarFileHeader == null) {
			jPanelJarFileHeader = new JPanel();
			GridBagLayout gbl_jPanelJarFileHeader = new GridBagLayout();
			gbl_jPanelJarFileHeader.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelJarFileHeader.rowHeights = new int[]{0, 0};
			gbl_jPanelJarFileHeader.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelJarFileHeader.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelJarFileHeader.setLayout(gbl_jPanelJarFileHeader);
			GridBagConstraints gbc_jLabelJarResources = new GridBagConstraints();
			gbc_jLabelJarResources.anchor = GridBagConstraints.WEST;
			gbc_jLabelJarResources.insets = new Insets(0, 5, 0, 0);
			gbc_jLabelJarResources.gridx = 0;
			gbc_jLabelJarResources.gridy = 0;
			jPanelJarFileHeader.add(getJLabelJarResources(), gbc_jLabelJarResources);
			GridBagConstraints gbc_jLabelBundleResources = new GridBagConstraints();
			gbc_jLabelBundleResources.anchor = GridBagConstraints.WEST;
			gbc_jLabelBundleResources.gridx = 1;
			gbc_jLabelBundleResources.gridy = 0;
			jPanelJarFileHeader.add(getJLabelBundleResources(), gbc_jLabelBundleResources);
		}
		return jPanelJarFileHeader;
	}
	private JLabel getJLabelJarResources() {
		if (jLabelJarResources == null) {
			jLabelJarResources = new JLabel("Einfache jar-Dateien");
			jLabelJarResources.setPreferredSize(new Dimension(120, 18));
			jLabelJarResources.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelJarResources;
	}
	private JLabel getJLabelBundleResources() {
		if (jLabelBundleResources == null) {
			jLabelBundleResources = new JLabel("OSGI-Bundles");
			jLabelBundleResources.setPreferredSize(new Dimension(120, 18));
			jLabelBundleResources.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelBundleResources;
	}
	
	private JPanel getJPanelJarResourcen() {
		if (jPanelJarResourcen == null) {
			jPanelJarResourcen = new JPanel();
			GridBagLayout gbl_jPanelJarResourcen = new GridBagLayout();
			gbl_jPanelJarResourcen.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelJarResourcen.rowHeights = new int[]{0, 0};
			gbl_jPanelJarResourcen.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelJarResourcen.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelJarResourcen.setLayout(gbl_jPanelJarResourcen);
			GridBagConstraints gbc_jScrollPanePlainJars = new GridBagConstraints();
			gbc_jScrollPanePlainJars.fill = GridBagConstraints.BOTH;
			gbc_jScrollPanePlainJars.insets = new Insets(0, 0, 0, 5);
			gbc_jScrollPanePlainJars.gridx = 0;
			gbc_jScrollPanePlainJars.gridy = 0;
			jPanelJarResourcen.add(getJScrollPanePlainJars(), gbc_jScrollPanePlainJars);
			GridBagConstraints gbc_jScrollPaneBundelJars = new GridBagConstraints();
			gbc_jScrollPaneBundelJars.fill = GridBagConstraints.BOTH;
			gbc_jScrollPaneBundelJars.gridx = 1;
			gbc_jScrollPaneBundelJars.gridy = 0;
			jPanelJarResourcen.add(getJScrollPaneBundelJars(), gbc_jScrollPaneBundelJars);
		}
		return jPanelJarResourcen;
	}
	private JScrollPane getJScrollPanePlainJars() {
		if (jScrollPanePlainJars == null) {
			jScrollPanePlainJars = new JScrollPane();
			jScrollPanePlainJars.setPreferredSize(this.preferredListSizeLarge);
			jScrollPanePlainJars.setViewportView(getJListJarResources());
		}
		return jScrollPanePlainJars;
	}
	private JList<String> getJListJarResources() {
		if (jListJarResources == null) {
			jListJarResources = new JList<String>(this.currProject.getProjectBundleLoader().getRegularJarsListModel());
			jListJarResources.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jListJarResources;
	}
	private JScrollPane getJScrollPaneBundelJars() {
		if (jScrollPaneBundelJars == null) {
			jScrollPaneBundelJars = new JScrollPane();
			jScrollPaneBundelJars.setPreferredSize(this.preferredListSizeLarge);
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
	 * This method initializes jPanelRight	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBinResourceButton() {
		if (jPanelBinResourceButton == null) {
			GridBagLayout gbl_jPanelBinResourceButton = new GridBagLayout();
			gbl_jPanelBinResourceButton.rowWeights = new double[]{0.0, 0.0, 1.0};

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
			
			jPanelBinResourceButton = new JPanel();
			jPanelBinResourceButton.setLayout(gbl_jPanelBinResourceButton);
			jPanelBinResourceButton.add(getJButtonBinResourcesAdd(), gridBagConstraints1);
			jPanelBinResourceButton.add(getJButtonBinResourcesRemove(), gridBagConstraints2);
			jPanelBinResourceButton.add(getJButtonBinResourcesRefresh(), gridBagConstraints3);
		}
		return jPanelBinResourceButton;
	}

	/**
	 * This method initializes jScrollPanePlugIns	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPanePlugIns() {
		if (jScrollPanePlugIns == null) {
			jScrollPanePlugIns = new JScrollPane();
			jScrollPanePlugIns.setPreferredSize(this.preferredListSizeLarge);
			jScrollPanePlugIns.setViewportView(getJListPlugIns());
		}
		return jScrollPanePlugIns;
	}
	
	/**
	 * This method initializes jButtonAdd	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAddPlugIns() {
		if (jButtonAddPlugIns == null) {
			jButtonAddPlugIns = new JButton();
			jButtonAddPlugIns.setPreferredSize(new Dimension(45, 26));
			jButtonAddPlugIns.setIcon(GlobalInfo.getInternalImageIcon("ListPlus.png"));
			jButtonAddPlugIns.setToolTipText("Add");
			jButtonAddPlugIns.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					Class<?> search4Class = PlugIn.class;
					String 	 search4CurrentValue = null;
					String 	 search4DefaultValue = null;
					String   search4Description = Language.translate("PlugIn für Agent.GUI");
					
					ClassSelectionDialog cs = new ClassSelectionDialog(Application.getMainWindow(), search4Class, search4CurrentValue, search4DefaultValue, search4Description, false);
					cs.setVisible(true);
					// --- act in the dialog ... --------------------
					if (cs.isCanceled()==true) return;
					
					// ----------------------------------------------
					// --- Class was selected. Proceed it -----------
					String classSelected = cs.getClassSelected();
					cs.dispose();
					cs = null;
					// ----------------------------------------------
					
					if (classSelected.equals("")==false) {
						// --- add the class to the project PlugIns -
						currProject.plugInLoad(classSelected, true);
						currProject.setUnsaved(true);
					}
					
				}
			});					
		}
		return jButtonAddPlugIns;
	}

	/**
	 * This method initializes jButtonRemove	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRemovePlugIns() {
		if (jButtonRemovePlugIns == null) {
			jButtonRemovePlugIns = new JButton();
			jButtonRemovePlugIns.setIcon(GlobalInfo.getInternalImageIcon("ListMinus.png"));
			jButtonRemovePlugIns.setPreferredSize(new Dimension(45, 26));
			jButtonRemovePlugIns.setToolTipText("Remove");
			jButtonRemovePlugIns.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					PlugInListElement pile = (PlugInListElement) jListPlugIns.getSelectedValue();
					if (pile==null) return;
					
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

	/**
	 * This method initializes jButtonRefresh	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRefreshPlugIns() {
		if (jButtonRefreshPlugIns == null) {
			jButtonRefreshPlugIns = new JButton();
			jButtonRefreshPlugIns.setIcon(GlobalInfo.getInternalImageIcon("Refresh.png"));
			jButtonRefreshPlugIns.setPreferredSize(new Dimension(45, 26));
			jButtonRefreshPlugIns.setToolTipText("Refresh");
			jButtonRefreshPlugIns.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					currProject.plugInVectorReload();
				}
			});
		}
		return jButtonRefreshPlugIns;
	}
	
	/**
	 * This method initializes jPanelPlugInButtons
	 * @return javax.swing.JPanel	
	 */
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
			gbl_jPanelPlugInButtons.rowWeights = new double[]{0.0, 0.0, 1.0};
			jPanelPlugInButtons.setLayout(gbl_jPanelPlugInButtons);
			jPanelPlugInButtons.add(getJButtonAddPlugIns(), gridBagConstraints1);
			jPanelPlugInButtons.add(getJButtonRemovePlugIns(), gridBagConstraints2);
			jPanelPlugInButtons.add(getJButtonRefreshPlugIns(), gridBagConstraints3);
		}
		return jPanelPlugInButtons;
	}

	/**
	 * This method initializes jListPlugIns	
	 * @return javax.swing.JList	
	 */
	private JList<PlugInListElement> getJListPlugIns() {
		if (jListPlugIns == null) {
			jListPlugIns = new JList<PlugInListElement>();
			jListPlugIns.setFont(new Font("Dialog", Font.PLAIN, 12));
			jListPlugIns.setModel(plugInsListModel);
		}
		return jListPlugIns;
	}
	
	private JSeparator getJSeparatorTop() {
		if (jSeparatorTop == null) {
			jSeparatorTop = new JSeparator();
		}
		return jSeparatorTop;
	}	
	public JLabel getJLabelPlugIns() {
		if (jLabelPlugIns==null) {
			jLabelPlugIns = new JLabel();
			jLabelPlugIns.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelPlugIns.setText("Projekt-PlugIns");
		}
		return jLabelPlugIns;
	}

	/**
	 * This methods adds a Plugin to the plugInListModel, so that it is displayed 
	 * @param plugIn
	 */
	private void addPlugInElement2List(PlugIn plugIn) {
		PlugInListElement pile = new PlugInListElement(plugIn.getName(), plugIn.getClassReference());
		this.plugInsListModel.addElement(pile);
	}
	/**
	 * This methods removes a Plugin from the plugInListModel 
	 * @param plugIn
	 */
	private void removePlugInElement2List(PlugIn plugIn) {
		String plugInRef = plugIn.getClassReference();
		for (int i = 0; i < plugInsListModel.size(); i++) {
			PlugInListElement pile =  (PlugInListElement) plugInsListModel.get(i);
			if (pile.getPlugInClassReference().equals(plugInRef)) {
				plugInsListModel.remove(i);
				return;
			}
		}
	}

	private JSeparator getJSeparatorBottom() {
		if (jSeparatorBottom == null) {
			jSeparatorBottom = new JSeparator();
		}
		return jSeparatorBottom;
	}
	
	/**
	 * This method initializes jPanelSimulationEnvironment	
	 * 
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSimulationEnvironment() {
		if (jPanelSimulationEnvironment == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints5.gridy = 0;
			jLabelEnvTyp = new JLabel();
			jLabelEnvTyp.setText("Umgebungstyp bzw. -modell für Simulation und Visualisierung");
			jLabelEnvTyp.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(5, 0, 0, 0);
			jPanelSimulationEnvironment = new JPanel();
			GridBagLayout gbl_jPanelSimulationEnvironment = new GridBagLayout();
			gbl_jPanelSimulationEnvironment.columnWeights = new double[]{1.0};
			jPanelSimulationEnvironment.setLayout(gbl_jPanelSimulationEnvironment);
			jPanelSimulationEnvironment.add(getJComboBoxEnvironmentModelSelector(), gridBagConstraints4);
			jPanelSimulationEnvironment.add(jLabelEnvTyp, gridBagConstraints5);
		}
		return jPanelSimulationEnvironment;
	}
	
	/**
	 * This method initialises jComboBoxEnvironmentModelSelector
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox<EnvironmentType> getJComboBoxEnvironmentModelSelector(){
		if(jComboBoxEnvironmentModelSelector == null){
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
					if (newEnvModel.equals(oldEnvModel)==false) {
						currProject.setEnvironmentModelName(newEnvModel);
					}
				}
			});
		}
		return jComboBoxEnvironmentModelSelector;
	}
	
	/**
	 * This method initializes jPanelTimeModelSelection	
	 * @return javax.swing.JPanel	
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
			jLabelTimeModelClass.setText("Zeitmodell");
			jLabelTimeModelClass.setText(Language.translate(jLabelTimeModelClass.getText()));
			
			jPanelTimeModelSelection = new JPanel();
			GridBagLayout gbl_jPanelTimeModelSelection = new GridBagLayout();
			gbl_jPanelTimeModelSelection.columnWeights = new double[]{1.0, 0.0, 0.0};
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
		String 	 search4CurrentValue = null;
		String 	 search4DefaultValue = null;
		String   search4Description = null;

		search4Class = TimeModel.class;
		search4CurrentValue = this.currProject.getTimeModelClass();
		search4DefaultValue = null;
		search4Description = jLabelTimeModelClass.getText();

		ClassSelectionDialog cs = new ClassSelectionDialog(Application.getMainWindow(), search4Class, search4CurrentValue, search4DefaultValue, search4Description, false);
		cs.setVisible(true);
		// --- act in the dialog ... --------------------
		if (cs.isCanceled()==false) {
			if (cs.getClassSelected()!=null && cs.getClassSelected().length()!=0) {
				this.currProject.setTimeModelClass(cs.getClassSelected());	
			}
		}
		cs.dispose();
		cs = null;
		// ----------------------------------------------
	}
	
	/**
	 * Adjust string.
	 * @param path the path
	 * @return the string
	 */
	private String adjustPathAccordngToProjectsDirectory(String path) {
		final String projectFolder = currProject.getProjectFolderFullPath();
		if (path.startsWith(projectFolder)) {
			int cut = projectFolder.length();
			return path.substring(cut - 1);
		}
		return path;
	}
	/**
	 * Already there.
	 * @param path the path
	 * @return true, if successful
	 */
	private boolean isProjectResources(String path) {
		return currProject.getProjectResources().contains(path);
	}
	
	/**
	 * Sets the view according to the project.
	 */
	private void setViewAccordingToProject() {
		this.getJCheckboxManifest().setSelected(this.currProject.isReCreateProjectManifest());
		this.getJListBinResources().setModel(this.currProject.getProjectResources().getResourcesListModel());
		this.getJListJarResources().setModel(this.currProject.getProjectBundleLoader().getRegularJarsListModel());
		this.getJListBundleJars().setModel(this.currProject.getProjectBundleLoader().getBundleJarsListModel());
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object updated) {
		
		if (updated.toString().equals(PlugIn.CHANGED)) {
			// --- Something happend with a plugIn --------
			PlugInNotification pin = (PlugInNotification) updated;
			int updateReason = pin.getUpdateReason();
			if (updateReason==PlugIn.ADDED) {
				this.addPlugInElement2List(pin.getPlugIn());
			} else if (updateReason==PlugIn.REMOVED) {
				this.removePlugInElement2List(pin.getPlugIn());
			}
			
		} else if (updated.equals(Project.CHANGED_EnvironmentModelType)) {
			EnvironmentType envTypeOld = (EnvironmentType) this.getJComboBoxEnvironmentModelSelector().getSelectedItem();
			EnvironmentType envTypeNew = this.currProject.getEnvironmentModelType();
			if (envTypeOld.equals(envTypeNew)==false) {
				this.getJComboBoxEnvironmentModelSelector().setSelectedItem(this.currProject.getEnvironmentModelType());	
			}
		
		} else if (updated.equals(Project.CHANGED_ProjectResources)) {
			this.setViewAccordingToProject();
			
		} else if (updated.equals(Project.CHANGED_TimeModelClass)) {
			this.getJTextFieldTimeModelClass().setText(this.currProject.getTimeModelClass());
			
		} 
		
	}
	
} 
