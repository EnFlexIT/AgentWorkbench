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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
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
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import agentgui.core.application.Application;
import agentgui.core.application.Project;
import agentgui.core.environment.EnvironmentType;

/**
 * @author derksen
 *
 */
public class ProjectResources extends JPanel {

	private static final long serialVersionUID = 1L;
	final static String PathImage = Application.RunInfo.PathImageIntern();
	private Project currProject = null;
	
	private JPanel jPanelRight = null;
	private JScrollPane jScrollPane = null;
	private DefaultListModel myModel = null;
	
	private JList jListResources = null;
	private JButton jButtonAdd = null;
	private JButton jButtonRemove = null;
	private JButton jButtonRefresh = null;

	private JPanel jPanelCustomize = null;
	private JLabel jLabelCustomize = null;
	private JTextField jTextFieldCustomizeClass = null;
	private JButton jButtonDefaultClass = null;
	private JButton jButtonDefaultClassCustomize = null;
	private JPanel jPanelSimulationEnvironment = null;
	
	private JComboBox jComboBoxEnvironmentModelSelector = null;
	private JLabel jLabelEnvTyp = null;
	private JLabel jLabelResources = null;
	private JLabel jLabelSeperator = null;

	
	/**
	 * This is the default constructor
	 */
	public ProjectResources(Project cp) {
		super();
		currProject = cp;
		initialize();
		myModel = new DefaultListModel();
		jListResources.setModel(myModel);
		for (String file : currProject.projectResources) {
			myModel.addElement(file);
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
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.gridx = 0;
		gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints13.insets = new Insets(10, 10, 15, 5);
		gridBagConstraints13.anchor = GridBagConstraints.WEST;
		gridBagConstraints13.gridy = 5;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.weighty = 1.0;
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 4;
		gridBagConstraints11.insets = new Insets(5, 10, 0, 5);
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
		this.setSize(727, 327);
		this.setLayout(new GridBagLayout());
		this.add(getJPanelRight(), gridBagConstraints12);
		this.add(getJScrollPane(), gridBagConstraints11);
		this.add(getJPanelCustomize(), gridBagConstraints13);
		this.add(getJPanelSimulationEnvironment(), gridBagConstraints21);
		this.add(jLabelResources, gridBagConstraints32);
		this.add(jLabelSeperator, gridBagConstraints41);
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
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAdd() {
		if (jButtonAdd == null) {
			jButtonAdd = new JButton();
			jButtonAdd.setPreferredSize(new Dimension(45, 26));
			jButtonAdd.setIcon(new ImageIcon(getClass().getResource(PathImage + "ListPlus.png")));
			jButtonAdd.setToolTipText("Add");
			jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					if (Application.JadePlatform.jadeStopAskUserBefore()) {
						
						JFileChooser chooser = new JFileChooser();
						chooser.setCurrentDirectory(new File(currProject.getProjectFolderFullPath()));
						FileNameExtensionFilter filter = new FileNameExtensionFilter("jar", "JAR");
						chooser.setFileFilter(filter);
						chooser.setMultiSelectionEnabled(true);
						chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
						chooser.setAcceptAllFileFilterUsed(false);
						chooser.showDialog(jButtonAdd, "Load Files");
						Vector<String> names = adjustPaths(chooser.getSelectedFiles());
						currProject.projectResources.addAll(names);

						for (String name : names) {
							myModel.addElement(name);
						
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
			jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {

							if (Application.JadePlatform.jadeStopAskUserBefore()) {
								//Remove from the classpath
								Object[] values = jListResources.getSelectedValues();
								
								for (Object file : values) {
									myModel.removeElement(file);
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
			jButtonRefresh.setIcon(new ImageIcon(getClass().getResource(
					PathImage + "Refresh.png")));
			jButtonRefresh.setPreferredSize(new Dimension(45, 26));
			jButtonRefresh.setToolTipText("Refresh");
			jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							
							if (Application.JadePlatform.jadeStopAskUserBefore()) {
								Application.MainWindow.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
								currProject.resourcesReLoad();
								Application.ClassDetector.reStartSearch(currProject, null);
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
			jScrollPane.setViewportView(getJListResources());
		}
		return jScrollPane;
	}


	/**
	 * This method initializes jPanelCustomize	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelCustomize() {
		if (jPanelCustomize == null) {
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.gridx = 1;
			gridBagConstraints28.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints28.gridy = 1;
			GridBagConstraints gridBagConstraints52 = new GridBagConstraints();
			gridBagConstraints52.fill = GridBagConstraints.NONE;
			gridBagConstraints52.gridx = 2;
			gridBagConstraints52.gridy = 1;
			gridBagConstraints52.insets = new Insets(5, 5, 0, 0);
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.anchor = GridBagConstraints.WEST;
			gridBagConstraints31.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.gridy = 1;
			gridBagConstraints31.weightx = 1.0;
			gridBagConstraints31.fill = GridBagConstraints.BOTH;
			GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
			gridBagConstraints42.anchor = GridBagConstraints.WEST;
			gridBagConstraints42.gridy = 0;
			gridBagConstraints42.gridwidth = 3;
			gridBagConstraints42.gridx = 0;
			jLabelCustomize = new JLabel();
			jLabelCustomize.setText("Java-Klasse für individuelle Agent.GUI-Erweiterung");
			jLabelCustomize.setFont(new Font("Dialog", Font.BOLD, 12));
			jPanelCustomize = new JPanel();
			jPanelCustomize.setLayout(new GridBagLayout());
			jPanelCustomize.add(jLabelCustomize, gridBagConstraints42);
			jPanelCustomize.add(getJTextFieldCustomizeClass(), gridBagConstraints31);
			jPanelCustomize.add(getJButtonDefaultClass(), gridBagConstraints52);
			jPanelCustomize.add(getJButtonDefaultClassCustomize(), gridBagConstraints28);
		}
		return jPanelCustomize;
	}
	/**
	 * This method initializes jTextFieldCustomizeClass	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldCustomizeClass() {
		if (jTextFieldCustomizeClass == null) {
			jTextFieldCustomizeClass = new JTextField();
			jTextFieldCustomizeClass.setPreferredSize(new Dimension(400, 26));
			jTextFieldCustomizeClass.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent kR) {
					super.keyReleased(kR);
					isValidClass(jTextFieldCustomizeClass, jButtonDefaultClassCustomize);
				}
			});
		}
		return jTextFieldCustomizeClass;
	}
	/**
	 * This method initializes jButtonDefaultClass	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDefaultClass() {
		if (jButtonDefaultClass == null) {
			jButtonDefaultClass = new JButton();
			jButtonDefaultClass.setToolTipText("Agent.GUI - Standard verwenden");			
			jButtonDefaultClass.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonDefaultClass.setPreferredSize(new Dimension(45, 26));
			jButtonDefaultClass.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBreset.png")));
			jButtonDefaultClass.setActionCommand("StatLoadBalancingDefault");
			jButtonDefaultClass.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		return jButtonDefaultClass;
	}
	
	/**
	 * This method initializes jButtonDefaultClassCustomize	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDefaultClassCustomize() {
		if (jButtonDefaultClassCustomize == null) {
			jButtonDefaultClassCustomize = new JButton();
			jButtonDefaultClassCustomize.setToolTipText("Klassenangabe überprüfen");
			jButtonDefaultClassCustomize.setPreferredSize(new Dimension(45, 26));
			jButtonDefaultClassCustomize.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBcheckGreen.png")));
			jButtonDefaultClassCustomize.setActionCommand("StatLoadBalancingCheck");
			jButtonDefaultClassCustomize.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		return jButtonDefaultClassCustomize;
	}
	
	/**
	 * This method checks if a given classs reference is valid
	 * @param className: reference to the class  
	 * @param classType: static(0) or dynamic(1)
	 */
	private void isValidClass(JTextField jTextField, JButton jButton) {
		
		String className = jTextField.getText().trim();
		try {
			@SuppressWarnings("unused")
			Class<?> clazz = Class.forName(className);
			jButton.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBcheckGreen.png")));
			
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			jButton.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBcheckRed.png")));
		}
	}

	/**
	 * This method initializes jPanelSimulationEnvironment	
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
	 * This method initializes jComboBoxEnvironmentModelSelector
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxEnvironmentModelSelector(){
		if(jComboBoxEnvironmentModelSelector == null){
			
			// --- Get current definitions --------------------------
			String currEnvTypeKey = currProject.getEnvironmentModel();
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
					String oldEnvModel = currProject.getEnvironmentModel();
					if (newEnvModel.equals(oldEnvModel)==false) {
						currProject.setEnvironmentModel(newEnvModel);
					}
				}
			});
		}
		return jComboBoxEnvironmentModelSelector;
	}
	
} //  @jve:decl-index=0:visual-constraint="-105,-76"
