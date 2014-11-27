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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.environment.EnvironmentType;
import agentgui.core.gui.ClassSelector;
import agentgui.core.plugin.PlugIn;
import agentgui.core.plugin.PlugInListElement;
import agentgui.core.plugin.PlugInNotification;
import agentgui.core.project.Project;
import agentgui.core.resources.Resources2Display;
import agentgui.simulationService.time.TimeModel;

/**
 * Represents the JPanel/Tab 'Configuration' - 'Resources'
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectResources extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	
	final static String PathImage = Application.getGlobalInfo().PathImageIntern();
	private Project currProject = null;
	
	private JPanel jPanelSimulationEnvironment = null;
	private JPanel jPanelRightPlugIns = null;
	private JPanel jPanelRight = null;
	
	private JComboBox jComboBoxEnvironmentModelSelector = null;

	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPanePlugIns = null;
	
	private JList jListResources = null;
	private JList jListPlugIns = null;
	private DefaultListModel plugInsListModel = new DefaultListModel();
	
	private JButton jButtonResourcesAdd = null;
	private JButton jButtonResourcesRemove = null;
	private JButton jButtonRecourcesRefresh = null;

	private JButton jButtonAddPlugIns = null;
	private JButton jButtonRemovePlugIns = null;
	private JButton jButtonRefreshPlugIns = null;

	private JLabel jLabelEnvTyp = null;
	private JLabel jLabelResources = null;
	private JLabel jLabelPlugIns = null;

	private JPanel jPanelTimeModelSelection = null;
	private JLabel jLabelTimeModelClass = null;
	private JTextField jTextFieldTimeModelClass = null;
	private JButton jButtonDefaultTimeModel = null;
	private JButton jButtonSelectTimeModel = null;
	
	
	/**
	 * This is the default constructor
	 */
	public ProjectResources(Project project) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);
		
		this.initialize();
		
		// --- Fill the list model for the external resources -------
		jListResources.setModel(currProject.getProjectResources().getResourcesListModel());
		
		// --- Display the current TimeModel class ------------------
		jTextFieldTimeModelClass.setText(this.currProject.getTimeModelClass());
		
		// --- Set the translations ---------------------------------
		jLabelEnvTyp.setText(Language.translate("Umgebungstyp bzw. -modell für Simulation und Visualisierung"));
		jLabelResources.setText(Language.translate("Externe jar-Ressourcen"));
		jLabelPlugIns.setText(Language.translate("Plug-Ins"));
		
		jButtonResourcesAdd.setToolTipText(Language.translate("Hinzufügen"));
		jButtonResourcesRemove.setToolTipText(Language.translate("Entfernen"));
		jButtonRecourcesRefresh.setToolTipText(Language.translate("Neu laden"));
		
		jButtonAddPlugIns.setToolTipText(Language.translate("Hinzufügen"));
		jButtonRemovePlugIns.setToolTipText(Language.translate("Entfernen"));
		jButtonRefreshPlugIns.setToolTipText(Language.translate("Neu laden"));
		
	}

	/**
	 * Adjust string.
	 * @param path the path
	 * @return the string
	 */
	private String adjustString(String path) {
		final String projectFolder = currProject.getProjectFolderFullPath();
		if (path.startsWith(projectFolder)) {
			int cut = projectFolder.length();
			String returnPath = path.substring(cut - 1); 
			return returnPath;
		}
		return path;
	}
	/**
	 * Already there.
	 * @param path the path
	 * @return true, if successful
	 */
	private boolean alreadyThere(String path) {
		return currProject.getProjectResources().contains(path);
	}
	/**
	 * Adjust paths.
	 * @param files the files
	 * @return the vector
	 */
	private Vector<String> adjustPaths(File[] files) {
		
		Vector<String> result = new Vector<String>();
		if (files != null) {

			for (File file : files) {
				
				String path = file.getAbsolutePath();
				if (!path.contains(".jar")) {
					// --- If this is a folder ------------					
					Vector<String> directoryFiles = handleDirectories(file);
					for (String foreignJar : directoryFiles) {
						String resourcCheck = this.adjustString(foreignJar);
						if (!alreadyThere(resourcCheck)) {
							result.add(this.adjustString(foreignJar)); // Use relative paths within projects
						}
					}

				} else	{
					// --- If this is a jar-file ----------
					String resourcCheck = this.adjustString(path);
					if (!alreadyThere(resourcCheck)) {
						result.add(this.adjustString(path)); // Use absolut within projects
					}
				}

			}
		}
		return result;
	}
	/**
	 * Handle directories.
	 * @param dir the dir
	 * @return the vector
	 */
	private Vector<String> handleDirectories(File dir) {
		Vector<String> result = new Vector<String>();
		try {
			result.add(dir.getAbsolutePath());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Initialize this JPanel.
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
		gridBagConstraints34.gridx = 0;
		gridBagConstraints34.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints34.insets = new Insets(10, 10, 0, 5);
		gridBagConstraints34.gridy = 2;
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.gridx = 1;
		gridBagConstraints31.anchor = GridBagConstraints.NORTH;
		gridBagConstraints31.insets = new Insets(5, 5, 5, 10);
		gridBagConstraints31.gridy = 6;
		GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		gridBagConstraints22.fill = GridBagConstraints.BOTH;
		gridBagConstraints22.gridy = 6;
		gridBagConstraints22.weightx = 1.0;
		gridBagConstraints22.weighty = 0.5;
		gridBagConstraints22.insets = new Insets(5, 10, 0, 5);
		gridBagConstraints22.anchor = GridBagConstraints.NORTH;
		gridBagConstraints22.gridx = 0;
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.gridx = 0;
		gridBagConstraints13.insets = new Insets(10, 10, 0, 0);
		gridBagConstraints13.anchor = GridBagConstraints.WEST;
		gridBagConstraints13.gridy = 5;
		GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
		gridBagConstraints32.gridx = 0;
		gridBagConstraints32.anchor = GridBagConstraints.WEST;
		gridBagConstraints32.insets = new Insets(10, 10, 0, 0);
		gridBagConstraints32.gridy = 3;
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.insets = new Insets(10, 10, 0, 5);
		gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints21.gridy = 1;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.weighty = 0.5;
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 4;
		gridBagConstraints11.insets = new Insets(5, 10, 0, 5);
		gridBagConstraints11.anchor = GridBagConstraints.NORTH;
		gridBagConstraints11.weightx = 1.0;
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 1;
		gridBagConstraints12.fill = GridBagConstraints.NONE;
		gridBagConstraints12.insets = new Insets(5, 5, 5, 10);
		gridBagConstraints12.anchor = GridBagConstraints.NORTH;
		gridBagConstraints12.gridy = 4;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(10, 10, 5, 10);
		gridBagConstraints.gridx = 0;
		
		jLabelPlugIns = new JLabel();
		jLabelPlugIns.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelPlugIns.setText("PlugIns");
		jLabelResources = new JLabel();
		jLabelResources.setText("Externe jar-Ressourcen");
		jLabelResources.setFont(new Font("Dialog", Font.BOLD, 12));
		
		this.setSize(727, 392);
		this.setLayout(new GridBagLayout());
		this.add(getJPanelRight(), gridBagConstraints12);
		this.add(getJScrollPane(), gridBagConstraints11);
		this.add(getJPanelSimulationEnvironment(), gridBagConstraints21);
		this.add(jLabelResources, gridBagConstraints32);
		this.add(jLabelPlugIns, gridBagConstraints13);
		this.add(getJScrollPanePlugIns(), gridBagConstraints22);
		this.add(getJPanelRightPlugIns(), gridBagConstraints31);
		this.add(getJPanelTimeModelSelection(), gridBagConstraints34);
	}

	/**
	 * This method initializes jListResources	
	 * @return javax.swing.JList	
	 */
	private JList getJListResources() {
		if (jListResources == null) {
			jListResources = new JList();
		}
		return jListResources;
	}

	/**
	 * This method initializes jButtonAdd	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAdd() {
		if (jButtonResourcesAdd == null) {
			jButtonResourcesAdd = new JButton();
			jButtonResourcesAdd.setPreferredSize(new Dimension(45, 26));
			jButtonResourcesAdd.setIcon(new ImageIcon(getClass().getResource(PathImage + "ListPlus.png")));
			jButtonResourcesAdd.setToolTipText("Add");
			jButtonResourcesAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (Application.getJadePlatform().jadeStopAskUserBefore()==true) {
						
						FileNameExtensionFilter filter = new FileNameExtensionFilter("jar", "JAR");
						
						JFileChooser chooser = new JFileChooser();
						chooser.setFileFilter(filter);
						chooser.setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
						chooser.setMultiSelectionEnabled(true);
						chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
						chooser.setAcceptAllFileFilterUsed(false);
						
						int answerChooser = chooser.showDialog(jButtonResourcesAdd, Language.translate("Dateien einbinden"));
						if (answerChooser==JFileChooser.CANCEL_OPTION) return;
						Application.getGlobalInfo().setLastSelectedFolder(chooser.getCurrentDirectory());
						
						Vector<String> names = adjustPaths(chooser.getSelectedFiles());
						currProject.getProjectResources().addAll(names);
						currProject.resourcesReLoad();
						jListResources.updateUI();
					}
				} // end actionPerformed
			}); // end addActionListener
		}
		return jButtonResourcesAdd;
	}

	/**
	 * This method initializes jButtonRemove	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRemove() {
		if (jButtonResourcesRemove == null) {
			jButtonResourcesRemove = new JButton();
			jButtonResourcesRemove.setIcon(new ImageIcon(getClass().getResource(PathImage + "ListMinus.png")));
			jButtonResourcesRemove.setPreferredSize(new Dimension(45, 26));
			jButtonResourcesRemove.setToolTipText("Remove");
			jButtonResourcesRemove.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {

							if (Application.getJadePlatform().jadeStopAskUserBefore()==true) {
								// --- Remove from the ClassPath ----
								Vector<String> selection = new Vector<String>();
								Object[] selectionArray = jListResources.getSelectedValues();
								for (Object fileR2DObject : selectionArray) {
									Resources2Display fileR2D = (Resources2Display) fileR2DObject;
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
		return jButtonResourcesRemove;
	}

	/**
	 * This method initializes jButtonRefresh	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRefresh() {
		if (jButtonRecourcesRefresh == null) {
			jButtonRecourcesRefresh = new JButton();
			jButtonRecourcesRefresh.setIcon(new ImageIcon(getClass().getResource(PathImage + "Refresh.png")));
			jButtonRecourcesRefresh.setPreferredSize(new Dimension(45, 26));
			jButtonRecourcesRefresh.setToolTipText("Refresh");
			jButtonRecourcesRefresh.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							
							if (Application.getJadePlatform().jadeStopAskUserBefore()) {
								Application.getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
								currProject.resourcesReLoad();
								Application.getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							}
						}
					});
		}
		return jButtonRecourcesRefresh;
	}

	/**
	 * This method initializes jPanelRight	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelRight() {
		if (jPanelRight == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new Insets(20, 0, 0, 0);
			gridBagConstraints3.gridy = 2;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = -1;
			gridBagConstraints1.insets = new Insets(0, 0, 5, 0);
			gridBagConstraints1.gridy = -1;
			jPanelRight = new JPanel();
			jPanelRight.setLayout(new GridBagLayout());
			jPanelRight.add(getJButtonAdd(), gridBagConstraints1);
			jPanelRight.add(getJButtonRemove(), gridBagConstraints2);
			jPanelRight.add(getJButtonRefresh(), gridBagConstraints3);
		}
		return jPanelRight;
	}

	/**
	 * This method initializes jScrollPane	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(260, 120));
			jScrollPane.setViewportView(getJListResources());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jPanelSimulationEnvironment	
	 * 
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSimulationEnvironment() {
		if (jPanelSimulationEnvironment == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints5.gridy = 0;
			jLabelEnvTyp = new JLabel();
			jLabelEnvTyp.setText("Umgebungstyp bzw. -modell für Simulation und Visualisierung");
			jLabelEnvTyp.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints4.weightx = 1.0;
			jPanelSimulationEnvironment = new JPanel();
			jPanelSimulationEnvironment.setLayout(new GridBagLayout());
			jPanelSimulationEnvironment.add(getJComboBoxEnvironmentModelSelector(), gridBagConstraints4);
			jPanelSimulationEnvironment.add(jLabelEnvTyp, gridBagConstraints5);
		}
		return jPanelSimulationEnvironment;
	}
	
	/**
	 * This method initialises jComboBoxEnvironmentModelSelector
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxEnvironmentModelSelector(){
		if(jComboBoxEnvironmentModelSelector == null){
			
			// --- Get current definitions --------------------------
			jComboBoxEnvironmentModelSelector = new JComboBox();
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
	 * This method initializes jScrollPanePlugIns	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPanePlugIns() {
		if (jScrollPanePlugIns == null) {
			jScrollPanePlugIns = new JScrollPane();
			jScrollPanePlugIns.setPreferredSize(new Dimension(260, 120));
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
			jButtonAddPlugIns.setIcon(new ImageIcon(getClass().getResource(PathImage + "ListPlus.png")));
			jButtonAddPlugIns.setToolTipText("Add");
			jButtonAddPlugIns.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					Class<?> search4Class = PlugIn.class;
					String 	 search4CurrentValue = null;
					String 	 search4DefaultValue = null;
					String   search4Description = Language.translate("PlugIn für Agent.GUI");
					
					ClassSelector cs = new ClassSelector(Application.getMainWindow(), search4Class, search4CurrentValue, search4DefaultValue, search4Description, false);
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
			jButtonRemovePlugIns.setIcon(new ImageIcon(getClass().getResource(PathImage + "ListMinus.png")));
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
			jButtonRefreshPlugIns.setIcon(new ImageIcon(getClass().getResource(PathImage + "Refresh.png")));
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
	 * This method initializes jPanelRightPlugIns
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelRightPlugIns() {
		if (jPanelRightPlugIns == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new Insets(20, 0, 0, 0);
			gridBagConstraints3.gridy = 2;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = -1;
			gridBagConstraints1.insets = new Insets(0, 0, 5, 0);
			gridBagConstraints1.gridy = -1;
			jPanelRightPlugIns = new JPanel();
			jPanelRightPlugIns.setLayout(new GridBagLayout());
			jPanelRightPlugIns.add(getJButtonAddPlugIns(), gridBagConstraints1);
			jPanelRightPlugIns.add(getJButtonRemovePlugIns(), gridBagConstraints2);
			jPanelRightPlugIns.add(getJButtonRefreshPlugIns(), gridBagConstraints3);
		}
		return jPanelRightPlugIns;
	}

	/**
	 * This method initializes jListPlugIns	
	 * @return javax.swing.JList	
	 */
	private JList getJListPlugIns() {
		if (jListPlugIns == null) {
			jListPlugIns = new JList();
			jListPlugIns.setModel(plugInsListModel);
		}
		return jListPlugIns;
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
			gridBagConstraints33.weightx = 1.0;
			gridBagConstraints33.fill = GridBagConstraints.BOTH;
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.anchor = GridBagConstraints.WEST;
			gridBagConstraints41.gridy = 0;
			gridBagConstraints41.gridx = 0;
			
			jLabelTimeModelClass = new JLabel();
			jLabelTimeModelClass.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelTimeModelClass.setText("Zeitmodell");
			jLabelTimeModelClass.setText(Language.translate(jLabelTimeModelClass.getText()));
			
			jPanelTimeModelSelection = new JPanel();
			jPanelTimeModelSelection.setLayout(new GridBagLayout());
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
			jButtonDefaultTimeModel.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBreset.png")));
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
			jButtonSelectTimeModel.setIcon(new ImageIcon(getClass().getResource(PathImage + "Search.png")));
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

		ClassSelector cs = new ClassSelector(Application.getMainWindow(), search4Class, search4CurrentValue, search4DefaultValue, search4Description, false);
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
		
		} else if (updated.equals(Project.CHANGED_TimeModelClass)) {
			this.getJTextFieldTimeModelClass().setText(this.currProject.getTimeModelClass());
			
		} 
		
	}
	
} //  @jve:decl-index=0:visual-constraint="-105,-76"
