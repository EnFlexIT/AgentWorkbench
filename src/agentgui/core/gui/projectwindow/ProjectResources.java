package agentgui.core.gui.projectwindow;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.environment.EnvironmentType;
import agentgui.core.gui.ClassSelector;
import agentgui.core.plugin.PlugIn;
import agentgui.core.plugin.PlugInListElement;
import agentgui.core.plugin.PlugInNotification;

/**
 * @author Christian Derksen, Tim Lewen
 */
public class ProjectResources extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	final static String PathImage = Application.RunInfo.PathImageIntern();
	private Project currProject = null;
	
	private JPanel jPanelRight = null;
	private JScrollPane jScrollPane = null;
	private DefaultListModel resourcesListModel = null;
	
	private JList jListResources = null;
	private JButton jButtonAdd = null;
	private JButton jButtonRemove = null;
	private JButton jButtonRefresh = null;

	private JPanel jPanelSimulationEnvironment = null;
	
	private JComboBox jComboBoxEnvironmentModelSelector = null;
	private JLabel jLabelEnvTyp = null;
	private JLabel jLabelResources = null;
	private JLabel jLabelSeperator = null;
	private JLabel jLabelPlugIns = null;
	
	private JScrollPane jScrollPanePlugIns = null;
	private JPanel jPanelRightPlugIns = null;
	private JButton jButtonAddPlugIns = null;
	private JButton jButtonRemovePlugIns = null;
	private JButton jButtonRefreshPlugIns = null;
	private JList jListPlugIns = null;
	
	private DefaultListModel plugInsListModel = new DefaultListModel();

	
	/**
	 * This is the default constructor
	 */
	public ProjectResources(Project cp) {
		super();
		this.currProject = cp;
		this.currProject.addObserver(this);
		
		initialize();
		resourcesListModel = new DefaultListModel();
		jListResources.setModel(resourcesListModel);
		for (String file : currProject.projectResources) {
			resourcesListModel.addElement(file);
		}

	}

	private String adjustString(String path) {
		final String projectFolder = currProject.getProjectFolderFullPath();
		if (path.startsWith(projectFolder)) {
			int cut = projectFolder.length();
			String returnPath = path.substring(cut - 1); 
			return returnPath;
		}
		return path;
	}

	private boolean alreay_there(String path) {
		return currProject.projectResources.contains(path);
	}

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
						if (!alreay_there(resourcCheck)) {
							result.add(this.adjustString(foreignJar)); // Use relative paths within projects
						}
					}

				} else	{
					// --- If this is a jar-file ----------
					String resourcCheck = this.adjustString(path);
					if (!alreay_there(resourcCheck)) {
						result.add(this.adjustString(path)); // Use absolut within projects
					}
				}

			}
		}
		return result;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
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
		jLabelPlugIns = new JLabel();
		jLabelPlugIns.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelPlugIns.setText("PlugIns");
		GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
		gridBagConstraints41.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints41.gridx = 0;
		gridBagConstraints41.gridy = 2;
		gridBagConstraints41.insets = new Insets(15, 10, 0, 10);
		gridBagConstraints41.gridwidth = 2;
		GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
		gridBagConstraints32.gridx = 0;
		gridBagConstraints32.anchor = GridBagConstraints.WEST;
		gridBagConstraints32.insets = new Insets(10, 10, 0, 0);
		gridBagConstraints32.gridy = 3;
		jLabelResources = new JLabel();
		jLabelResources.setText("Externe jar-Ressourcen");
		jLabelResources.setFont(new Font("Dialog", Font.BOLD, 12));
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
		this.setSize(727, 381);
		this.setLayout(new GridBagLayout());
		this.add(getJPanelRight(), gridBagConstraints12);
		this.add(getJScrollPane(), gridBagConstraints11);
		this.add(getJPanelSimulationEnvironment(), gridBagConstraints21);
		this.add(jLabelResources, gridBagConstraints32);
		this.add(jLabelSeperator, gridBagConstraints41);
		this.add(jLabelPlugIns, gridBagConstraints13);
		this.add(getJScrollPanePlugIns(), gridBagConstraints22);
		this.add(getJPanelRightPlugIns(), gridBagConstraints31);
	}

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
		if (jButtonAdd == null) {
			jButtonAdd = new JButton();
			jButtonAdd.setPreferredSize(new Dimension(45, 26));
			jButtonAdd.setIcon(new ImageIcon(getClass().getResource(PathImage + "ListPlus.png")));
			jButtonAdd.setToolTipText("Add");
			jButtonAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (Application.JadePlatform.jadeStopAskUserBefore()) {
						
						FileNameExtensionFilter filter = new FileNameExtensionFilter("jar", "JAR");
						
						JFileChooser chooser = new JFileChooser();
						chooser.setFileFilter(filter);
						chooser.setCurrentDirectory(Application.RunInfo.getLastSelectedFolder());
						chooser.setMultiSelectionEnabled(true);
						chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
						chooser.setAcceptAllFileFilterUsed(false);
						
						int answerChooser = chooser.showDialog(jButtonAdd, Language.translate("Dateien einbinden"));
						if (answerChooser==JFileChooser.CANCEL_OPTION) return;
						Application.RunInfo.setLastSelectedFolder(chooser.getCurrentDirectory());
						
						Vector<String> names = adjustPaths(chooser.getSelectedFiles());
						currProject.projectResources.addAll(names);

						for (String name : names) {
							resourcesListModel.addElement(name);
						
						}
						currProject.resourcesReLoad();
						jListResources.updateUI();
					}
				} // end actionPerformed
			}); // end addActionListener
		}
		return jButtonAdd;
	}

	/**
	 * This method initializes jButtonRemove	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRemove() {
		if (jButtonRemove == null) {
			jButtonRemove = new JButton();
			jButtonRemove.setIcon(new ImageIcon(getClass().getResource(PathImage + "ListMinus.png")));
			jButtonRemove.setPreferredSize(new Dimension(45, 26));
			jButtonRemove.setToolTipText("Remove");
			jButtonRemove.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {

							if (Application.JadePlatform.jadeStopAskUserBefore()) {
								//Remove from the classpath
								Object[] values = jListResources.getSelectedValues();
								
								for (Object file : values) {
									resourcesListModel.removeElement(file);
									currProject.projectResources.remove(file);
								}
								currProject.resourcesReLoad();
							}
						}
					});
		}
		return jButtonRemove;
	}

	/**
	 * This method initializes jButtonRefresh	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRefresh() {
		if (jButtonRefresh == null) {
			jButtonRefresh = new JButton();
			jButtonRefresh.setIcon(new ImageIcon(getClass().getResource(PathImage + "Refresh.png")));
			jButtonRefresh.setPreferredSize(new Dimension(45, 26));
			jButtonRefresh.setToolTipText("Refresh");
			jButtonRefresh.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							
							if (Application.JadePlatform.jadeStopAskUserBefore()) {
								Application.MainWindow.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
								currProject.resourcesReLoad();
								Application.MainWindow.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							}
						}
					});
		}
		return jButtonRefresh;
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
			jLabelSeperator = new JLabel();
			jLabelSeperator.setText(" ");
			jLabelSeperator.setPreferredSize(new Dimension(200, 2));
			jLabelSeperator.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
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
			String currEnvTypeKey = currProject.getEnvironmentModelName();
			EnvironmentType envType = Application.RunInfo.getKnowEnvironmentTypes().getEnvironmentTypeByKey(currEnvTypeKey);
			
			jComboBoxEnvironmentModelSelector = new JComboBox();
			jComboBoxEnvironmentModelSelector.setModel(Application.RunInfo.getKnowEnvironmentTypes().getComboBoxModel());
			jComboBoxEnvironmentModelSelector.setPreferredSize(new Dimension(400, 25));
			jComboBoxEnvironmentModelSelector.setSelectedItem(envType);
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
					
					ClassSelector cs = new ClassSelector(Application.MainWindow, search4Class, search4CurrentValue, search4DefaultValue, search4Description);
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
						currProject.isUnsaved=true;
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
					PlugIn pi = currProject.plugIns_Loaded.getPlugIn(pile.getPlugInName());

					// --- Remove the PlugIn --------------
					currProject.plugInRemove(pi, true);
					currProject.isUnsaved=true;
					
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
			
		} else {
			// ----
		}
		
	}

	
} //  @jve:decl-index=0:visual-constraint="-105,-76"
