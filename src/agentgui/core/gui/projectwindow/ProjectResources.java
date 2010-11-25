package agentgui.core.gui.projectwindow;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import agentgui.core.application.Application;
import agentgui.core.application.Project;

public class ProjectResources extends JPanel {

	private static final long serialVersionUID = 1L;
	final static String PathImage = Application.RunInfo.PathImageIntern();
	private Project currProject = null;
	private JList jListResources = null;
	private JButton jButtonAdd = null;
	private JButton jButtonRemove = null;
	private DefaultListModel myModel = null;
	private JButton jButtonRefresh = null;
	private JPanel jPanelRight = null;
	private JScrollPane jScrollPane = null;

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

					Vector<String> directoryFiles = handleDirectories(file);
					
					for (String foreignJar : directoryFiles) {
						
						if (!alreay_there(foreignJar)) {
							result.add(this.adjustString(foreignJar)); // Use relative paths within projects
						}

					}

				} else	{
					if (!alreay_there(path)) {
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
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.weighty = 1.0;
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 0;
		gridBagConstraints11.insets = new Insets(10, 10, 2, 5);
		gridBagConstraints11.weightx = 1.0;
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 28;
		gridBagConstraints12.fill = GridBagConstraints.NONE;
		gridBagConstraints12.insets = new Insets(10, 5, 5, 10);
		gridBagConstraints12.anchor = GridBagConstraints.NORTH;
		gridBagConstraints12.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(10, 10, 5, 10);
		gridBagConstraints.gridx = 0;
		this.setSize(581, 273);
		this.setLayout(new GridBagLayout());
		this.add(getJPanelRight(), gridBagConstraints12);
		this.add(getJScrollPane(), gridBagConstraints11);
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
								Application.classDetector.reStartSearch(currProject, null);
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
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJListResources());
		}
		return jScrollPane;
	}

} //  @jve:decl-index=0:visual-constraint="-105,-76"
