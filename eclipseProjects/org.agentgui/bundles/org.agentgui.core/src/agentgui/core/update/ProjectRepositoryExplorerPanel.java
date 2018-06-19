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
package agentgui.core.update;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import agentgui.core.application.Application;
import agentgui.core.config.BundleProperties;
import agentgui.core.config.GlobalInfo;
import agentgui.core.update.repositoryModel.ProjectRepository;
import agentgui.core.update.repositoryModel.ProjectRepositoryEntries;
import agentgui.core.update.repositoryModel.RepositoryEntry;
import agentgui.core.update.repositoryModel.RepositoryTagVersions;

/**
 * The Class ProjectRepositoryExplorer provides .
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectRepositoryExplorerPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -8466682987118948831L;
	
	
	
	private ProjectRepositoryExplorerPanelListener panelListener;
	
	private ProjectRepository projectRepository;
	private RepositoryEntry repositoryEntrySelected;
	private boolean showAllVersions;

	private ArrayList<String> localAvailableProjectIDs;
	
	
	private JLabel JLabelHeader;
	private JLabel jLabelRepository;
	private JComboBox<String> jComboBoxRepositorySelection;
	private DefaultComboBoxModel<String> comboBoxModel;
	private JButton jButtonRepositoryRefresh;
	
	private JPanel jPanelRepositoryView;

	private JScrollPane jScrollPaneRepositoryView;
	private JTree jTreeRepositoryView;
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel treeModelRepositoryView;
	
	private JPanel jPanelProjectDetails;
	private JCheckBox jCheckBoxShowAllVersions;
	private JSeparator separator;
	private JLabel JLabelProject;
	private JLabel jLabelTag;
	private JTextField jTextFieldProject;
	private JTextField jTextFieldTag;
	private JLabel lblDescription;
	private JScrollPane jScrollPaneProjectDescription;
	private JTextArea jTextAreaProjectDescription;
	private JLabel jLabelVersion;
	private JTextField jTextFieldProjectVersion;
	private JButton jButtonInstallUpdate;

	/**
	 * Instantiates a new project repository explorer.
	 * @param listener the {@link ProjectRepositoryExplorerPanelListener}
	 */
	public ProjectRepositoryExplorerPanel(ProjectRepositoryExplorerPanelListener listener) {
		this.panelListener = listener;
		this.initialize();
		this.setSelectedTreeNode(null);
	}
	
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		GridBagConstraints gbc_JLabelHeader = new GridBagConstraints();
		gbc_JLabelHeader.gridwidth = 2;
		gbc_JLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_JLabelHeader.insets = new Insets(0, 0, 10, 5);
		gbc_JLabelHeader.gridx = 0;
		gbc_JLabelHeader.gridy = 0;
		this.add(getJLabelHeader(), gbc_JLabelHeader);
		GridBagConstraints gbc_jLabelRepository = new GridBagConstraints();
		gbc_jLabelRepository.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelRepository.anchor = GridBagConstraints.WEST;
		gbc_jLabelRepository.fill = GridBagConstraints.VERTICAL;
		gbc_jLabelRepository.gridx = 0;
		gbc_jLabelRepository.gridy = 1;
		this.add(getJLabelRepository(), gbc_jLabelRepository);
		GridBagConstraints gbc_jComboBoxRepositorySelection = new GridBagConstraints();
		gbc_jComboBoxRepositorySelection.insets = new Insets(0, 0, 5, 5);
		gbc_jComboBoxRepositorySelection.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxRepositorySelection.gridx = 1;
		gbc_jComboBoxRepositorySelection.gridy = 1;
		this.add(getJComboBoxRepositorySelection(), gbc_jComboBoxRepositorySelection);
		GridBagConstraints gbc_jButtonRepositoryRefresh = new GridBagConstraints();
		gbc_jButtonRepositoryRefresh.fill = GridBagConstraints.VERTICAL;
		gbc_jButtonRepositoryRefresh.insets = new Insets(0, 0, 5, 0);
		gbc_jButtonRepositoryRefresh.gridx = 2;
		gbc_jButtonRepositoryRefresh.gridy = 1;
		this.add(getJButtonRepositoryRefresh(), gbc_jButtonRepositoryRefresh);
		GridBagConstraints gbc_jPanelRepositoryView = new GridBagConstraints();
		gbc_jPanelRepositoryView.gridwidth = 3;
		gbc_jPanelRepositoryView.insets = new Insets(5, 0, 0, 0);
		gbc_jPanelRepositoryView.fill = GridBagConstraints.BOTH;
		gbc_jPanelRepositoryView.gridx = 0;
		gbc_jPanelRepositoryView.gridy = 2;
		add(getJPanelRepositoryView(), gbc_jPanelRepositoryView);
	}
	
	private JLabel getJLabelHeader() {
		if (JLabelHeader == null) {
			JLabelHeader = new JLabel("Project-Repository Explorer");
			JLabelHeader.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return JLabelHeader;
	}
	private JLabel getJLabelRepository() {
		if (jLabelRepository == null) {
			jLabelRepository = new JLabel("Project-Repository:");
			jLabelRepository.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelRepository;
	}
	private JComboBox<String> getJComboBoxRepositorySelection() {
		if (jComboBoxRepositorySelection == null) {
			jComboBoxRepositorySelection = new JComboBox<>(this.getComboBoxModel());
			jComboBoxRepositorySelection.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxRepositorySelection.setPreferredSize(new Dimension(200, 26));
			jComboBoxRepositorySelection.setEditable(true);
			jComboBoxRepositorySelection.setSelectedItem(null);
			jComboBoxRepositorySelection.addActionListener(this);
		}
		return jComboBoxRepositorySelection;
	}
	private DefaultComboBoxModel<String> getComboBoxModel() {
		if (comboBoxModel==null) {
			
			String[] projectRepositoryArray = null;
			String projectRepositories = Application.getGlobalInfo().getStringFromConfiguration(BundleProperties.DEF_PROJECT_REPOSITORIES, null);
			if (projectRepositories!=null) {
				projectRepositoryArray = projectRepositories.split(","); 
			}
			if (projectRepositoryArray==null) {
				comboBoxModel = new DefaultComboBoxModel<>();
			} else {
				comboBoxModel = new DefaultComboBoxModel<>(projectRepositoryArray);
			}
			// --- Ensure that the default repository is available --
			if (comboBoxModel.getIndexOf(GlobalInfo.DEFAULT_AWB_PROJECT_REPOSITORY)==-1) {
				comboBoxModel.addElement(GlobalInfo.DEFAULT_AWB_PROJECT_REPOSITORY);
			}
		}
		return comboBoxModel;
	}
	/**
	 * Adds the specified new project repository as link.
	 * @param newRepository the new repository
	 */
	private void addProjectRepository(String newRepository) {
		
		if (newRepository==null || newRepository.isEmpty()==true) return;
		
		if (this.getComboBoxModel().getIndexOf(newRepository)==-1) {
			this.getComboBoxModel().addElement(newRepository);

			String projectRepositories = Application.getGlobalInfo().getStringFromConfiguration(BundleProperties.DEF_PROJECT_REPOSITORIES, null);
			if (projectRepositories==null || projectRepositories.isEmpty()==true) {
				Application.getGlobalInfo().putStringToConfiguration(BundleProperties.DEF_PROJECT_REPOSITORIES, newRepository);

			} else if (projectRepositories.contains(newRepository)==false) {
				projectRepositories += "," + newRepository;
				Application.getGlobalInfo().putStringToConfiguration(BundleProperties.DEF_PROJECT_REPOSITORIES, projectRepositories);
			}
		}
	}
	
	
	private JButton getJButtonRepositoryRefresh() {
		if (jButtonRepositoryRefresh == null) {
			jButtonRepositoryRefresh = new JButton();
			jButtonRepositoryRefresh.setIcon(GlobalInfo.getInternalImageIcon("Refresh.png"));
			jButtonRepositoryRefresh.setToolTipText("Refresh Repository View");
			jButtonRepositoryRefresh.setSize(26, 26);
			jButtonRepositoryRefresh.addActionListener(this);
		}
		return jButtonRepositoryRefresh;
	}
	
	private JPanel getJPanelRepositoryView() {
		if (jPanelRepositoryView == null) {
			jPanelRepositoryView = new JPanel();
			GridBagLayout gbl_jPanelRepositoryView = new GridBagLayout();
			gbl_jPanelRepositoryView.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelRepositoryView.rowHeights = new int[]{0, 0};
			gbl_jPanelRepositoryView.columnWeights = new double[]{2.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelRepositoryView.rowWeights = new double[]{1.0, Double.MIN_VALUE};
			jPanelRepositoryView.setLayout(gbl_jPanelRepositoryView);
			GridBagConstraints gbc_jScrollPaneRepositoryView = new GridBagConstraints();
			gbc_jScrollPaneRepositoryView.fill = GridBagConstraints.BOTH;
			gbc_jScrollPaneRepositoryView.insets = new Insets(0, 0, 0, 5);
			gbc_jScrollPaneRepositoryView.gridx = 0;
			gbc_jScrollPaneRepositoryView.gridy = 0;
			jPanelRepositoryView.add(getJScrollPaneRepositoryView(), gbc_jScrollPaneRepositoryView);
			GridBagConstraints gbc_jPanelProjectDetails = new GridBagConstraints();
			gbc_jPanelProjectDetails.fill = GridBagConstraints.BOTH;
			gbc_jPanelProjectDetails.gridx = 1;
			gbc_jPanelProjectDetails.gridy = 0;
			jPanelRepositoryView.add(getJPanelProjectDetails(), gbc_jPanelProjectDetails);
		}
		return jPanelRepositoryView;
	}
	private JScrollPane getJScrollPaneRepositoryView() {
		if (jScrollPaneRepositoryView == null) {
			jScrollPaneRepositoryView = new JScrollPane();
			jScrollPaneRepositoryView.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			jScrollPaneRepositoryView.setViewportView(getJTreeRepositoryView());
		}
		return jScrollPaneRepositoryView;
	}
	private JTree getJTreeRepositoryView() {
		if (jTreeRepositoryView == null) {
			jTreeRepositoryView = new JTree(this.getTreeModel());
			jTreeRepositoryView.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTreeRepositoryView.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			jTreeRepositoryView.addTreeSelectionListener(new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent tse) {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jTreeRepositoryView.getLastSelectedPathComponent();
					setSelectedTreeNode(selectedNode);
				}
			});
			
		}
		return jTreeRepositoryView;
	}
	private DefaultMutableTreeNode getRootNode() {
		if (rootNode==null) {
			rootNode = new DefaultMutableTreeNode("Project & Versions");
		}
		return rootNode;
	}
	private DefaultTreeModel getTreeModel() {
		if (treeModelRepositoryView==null) {
			treeModelRepositoryView = new DefaultTreeModel(this.getRootNode());
		}
		return treeModelRepositoryView;
	}
	private void expandAllNodes(int startingIndex, int rowCount) {
	    for (int i=startingIndex; i<rowCount; ++i) {
	    	getJTreeRepositoryView().expandRow(i);
	    }
	    if (getJTreeRepositoryView().getRowCount()!=rowCount){
	        this.expandAllNodes(rowCount, getJTreeRepositoryView().getRowCount());
	    }
	}
	
	private JPanel getJPanelProjectDetails() {
		if (jPanelProjectDetails == null) {
			jPanelProjectDetails = new JPanel();
			jPanelProjectDetails.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			GridBagLayout gbl_jPanelProjectDetails = new GridBagLayout();
			gbl_jPanelProjectDetails.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelProjectDetails.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
			gbl_jPanelProjectDetails.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelProjectDetails.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			jPanelProjectDetails.setLayout(gbl_jPanelProjectDetails);
			GridBagConstraints gbc_jCheckBoxShowAllVersions = new GridBagConstraints();
			gbc_jCheckBoxShowAllVersions.gridwidth = 2;
			gbc_jCheckBoxShowAllVersions.anchor = GridBagConstraints.WEST;
			gbc_jCheckBoxShowAllVersions.insets = new Insets(5, 5, 5, 5);
			gbc_jCheckBoxShowAllVersions.gridx = 0;
			gbc_jCheckBoxShowAllVersions.gridy = 0;
			jPanelProjectDetails.add(getJCheckBoxShowAllVersions(), gbc_jCheckBoxShowAllVersions);
			GridBagConstraints gbc_separator = new GridBagConstraints();
			gbc_separator.gridwidth = 2;
			gbc_separator.fill = GridBagConstraints.HORIZONTAL;
			gbc_separator.insets = new Insets(0, 5, 5, 5);
			gbc_separator.gridx = 0;
			gbc_separator.gridy = 1;
			jPanelProjectDetails.add(getSeparator(), gbc_separator);
			GridBagConstraints gbc_JLabelProject = new GridBagConstraints();
			gbc_JLabelProject.anchor = GridBagConstraints.WEST;
			gbc_JLabelProject.insets = new Insets(5, 5, 5, 5);
			gbc_JLabelProject.gridx = 0;
			gbc_JLabelProject.gridy = 2;
			jPanelProjectDetails.add(getJLabelProject(), gbc_JLabelProject);
			GridBagConstraints gbc_jTextFieldProject = new GridBagConstraints();
			gbc_jTextFieldProject.insets = new Insets(5, 0, 5, 0);
			gbc_jTextFieldProject.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldProject.gridx = 1;
			gbc_jTextFieldProject.gridy = 2;
			jPanelProjectDetails.add(getJTextFieldProject(), gbc_jTextFieldProject);
			GridBagConstraints gbc_lblDescription = new GridBagConstraints();
			gbc_lblDescription.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblDescription.insets = new Insets(2, 5, 5, 5);
			gbc_lblDescription.gridx = 0;
			gbc_lblDescription.gridy = 3;
			jPanelProjectDetails.add(getLblDescription(), gbc_lblDescription);
			GridBagConstraints gbc_jScrollPaneProjectDescription = new GridBagConstraints();
			gbc_jScrollPaneProjectDescription.insets = new Insets(0, 0, 5, 0);
			gbc_jScrollPaneProjectDescription.fill = GridBagConstraints.BOTH;
			gbc_jScrollPaneProjectDescription.gridx = 1;
			gbc_jScrollPaneProjectDescription.gridy = 3;
			jPanelProjectDetails.add(getJScrollPaneProjectDescription(), gbc_jScrollPaneProjectDescription);
			GridBagConstraints gbc_jLabelTag = new GridBagConstraints();
			gbc_jLabelTag.insets = new Insets(0, 5, 5, 5);
			gbc_jLabelTag.anchor = GridBagConstraints.WEST;
			gbc_jLabelTag.gridx = 0;
			gbc_jLabelTag.gridy = 4;
			jPanelProjectDetails.add(getJLabelTag(), gbc_jLabelTag);
			GridBagConstraints gbc_jTextFieldTag = new GridBagConstraints();
			gbc_jTextFieldTag.insets = new Insets(0, 0, 5, 0);
			gbc_jTextFieldTag.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldTag.gridx = 1;
			gbc_jTextFieldTag.gridy = 4;
			jPanelProjectDetails.add(getJTextFieldTag(), gbc_jTextFieldTag);
			GridBagConstraints gbc_jLabelVersion = new GridBagConstraints();
			gbc_jLabelVersion.anchor = GridBagConstraints.WEST;
			gbc_jLabelVersion.insets = new Insets(0, 5, 5, 5);
			gbc_jLabelVersion.gridx = 0;
			gbc_jLabelVersion.gridy = 5;
			jPanelProjectDetails.add(getJLabelVersion(), gbc_jLabelVersion);
			GridBagConstraints gbc_jTextFieldProjectVersion = new GridBagConstraints();
			gbc_jTextFieldProjectVersion.insets = new Insets(0, 0, 5, 0);
			gbc_jTextFieldProjectVersion.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldProjectVersion.gridx = 1;
			gbc_jTextFieldProjectVersion.gridy = 5;
			jPanelProjectDetails.add(getJTextFieldProjectVersion(), gbc_jTextFieldProjectVersion);
			GridBagConstraints gbc_jButtonInstallUpdate = new GridBagConstraints();
			gbc_jButtonInstallUpdate.insets = new Insets(10, 5, 10, 0);
			gbc_jButtonInstallUpdate.gridwidth = 2;
			gbc_jButtonInstallUpdate.gridx = 0;
			gbc_jButtonInstallUpdate.gridy = 6;
			jPanelProjectDetails.add(getJButtonInstallUpdate(), gbc_jButtonInstallUpdate);
		}
		return jPanelProjectDetails;
	}
	
	private JCheckBox getJCheckBoxShowAllVersions() {
		if (jCheckBoxShowAllVersions == null) {
			jCheckBoxShowAllVersions = new JCheckBox("Show all project versions");
			jCheckBoxShowAllVersions.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxShowAllVersions.addActionListener(this);
		}
		return jCheckBoxShowAllVersions;
	}
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
		}
		return separator;
	}
	private JLabel getJLabelProject() {
		if (JLabelProject == null) {
			JLabelProject = new JLabel("Project");
			JLabelProject.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return JLabelProject;
	}
	private JLabel getJLabelTag() {
		if (jLabelTag == null) {
			jLabelTag = new JLabel("Tag");
			jLabelTag.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelTag;
	}
	private JTextField getJTextFieldProject() {
		if (jTextFieldProject == null) {
			jTextFieldProject = new JTextField();
			jTextFieldProject.setEditable(false);
			jTextFieldProject.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldProject.setColumns(10);
		}
		return jTextFieldProject;
	}
	private JTextField getJTextFieldTag() {
		if (jTextFieldTag == null) {
			jTextFieldTag = new JTextField();
			jTextFieldTag.setEditable(false);
			jTextFieldTag.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldTag.setColumns(10);
		}
		return jTextFieldTag;
	}
	private JLabel getLblDescription() {
		if (lblDescription == null) {
			lblDescription = new JLabel("Description");
			lblDescription.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return lblDescription;
	}
	private JScrollPane getJScrollPaneProjectDescription() {
		if (jScrollPaneProjectDescription == null) {
			jScrollPaneProjectDescription = new JScrollPane();
			jScrollPaneProjectDescription.setViewportView(this.getJTextAreaProjectDescription());
		}
		return jScrollPaneProjectDescription;
	}
	private JTextArea getJTextAreaProjectDescription() {
		if (jTextAreaProjectDescription == null) {
			jTextAreaProjectDescription = new JTextArea();
			jTextAreaProjectDescription.setEditable(false);
			jTextAreaProjectDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextAreaProjectDescription.setLineWrap(true);
		}
		return jTextAreaProjectDescription;
	}
	private JLabel getJLabelVersion() {
		if (jLabelVersion == null) {
			jLabelVersion = new JLabel("Version");
			jLabelVersion.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelVersion;
	}
	private JTextField getJTextFieldProjectVersion() {
		if (jTextFieldProjectVersion == null) {
			jTextFieldProjectVersion = new JTextField();
			jTextFieldProjectVersion.setEditable(false);
			jTextFieldProjectVersion.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldProjectVersion.setColumns(10);
		}
		return jTextFieldProjectVersion;
	}
	private JButton getJButtonInstallUpdate() {
		if (jButtonInstallUpdate == null) {
			jButtonInstallUpdate = new JButton("Install");
			jButtonInstallUpdate.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonInstallUpdate.setForeground(new Color(0, 153, 0));
			jButtonInstallUpdate.setPreferredSize(new Dimension(120, 26));
			jButtonInstallUpdate.addActionListener(this);
		}
		return jButtonInstallUpdate;
	}
	
	/**
	 * Gets the repository directory link (for a local directory or a web directory).
	 * @return the repository directory link
	 */
	private String getRepositoryDirectoryLink() {
		String link = (String) this.getJComboBoxRepositorySelection().getSelectedItem();
		if (link==null || link.isEmpty()==true) return null;
		return link;
	}
	/**
	 * Loads the project repository from combo box reference.
	 */
	public void loadProjectRepositoryFromComboBoxReference() {
		
		// --- Assign to local variable -----------------------------
		String link = this.getRepositoryDirectoryLink();
		ProjectRepository repo = ProjectRepository.loadProjectRepository(link);
		this.setProjectRepository(repo);
		if (repo!=null) {
			// --- Remind this link ---------------------------------
			this.addProjectRepository(link);
		}
	}

	/**
	 * Sets the project repository to the view.
	 * @param projectRepository the new project repository
	 */
	private void setProjectRepository(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
		this.buildRepositoryTree();
	}
	
	/**
	 * Builds the repository tree.
	 */
	private void buildRepositoryTree() {
		
		// --- remove all sub-nodes from the root node -------------- 
		this.getRootNode().removeAllChildren();
		
		if (this.projectRepository!=null) {
			// --- Add all project nodes ----------------------------
			ArrayList<String> projectIDs = new ArrayList<>(this.projectRepository.getProjectRepositories().keySet()); 
			for (int i = 0; i < projectIDs.size(); i++) {
				
				String projectID = projectIDs.get(i);
				ProjectRepositoryEntries repoEntries = this.projectRepository.getProjectRepositories().get(projectID);
				
				DefaultMutableTreeNode projectTreeNode = new DefaultMutableTreeNode("Project ID: " + projectID);
				this.addRepositoryTagNodes(projectTreeNode, repoEntries);
				this.getRootNode().add(projectTreeNode);
			}
		}
		// --- Reload and expand the view ---------------------------
		this.getTreeModel().reload();
		this.expandAllNodes(0, this.getJTreeRepositoryView().getRowCount());
	}
	/**
	 * Adds the tag nodes.
	 *
	 * @param projectTreeNode the project tree node
	 * @param repoEntries the ProjectRepositoryEntries
	 */
	private void addRepositoryTagNodes(DefaultMutableTreeNode projectTreeNode, ProjectRepositoryEntries repoEntries) {
		
		if (repoEntries!=null) {

			ArrayList<String> tags = new ArrayList<>(repoEntries.getRepositoryEntries().keySet());
			for (int i = 0; i < tags.size(); i++) {
				
				String tag = tags.get(i);
				RepositoryTagVersions tagVersions = repoEntries.getRepositoryEntries().get(tag);

				int nVersions = tagVersions.getRepositoryTagVector().size();
				String versionDescription = nVersions + " versions";
				if (nVersions==1) versionDescription = nVersions + " version";
				
				DefaultMutableTreeNode tagTreeNode = new DefaultMutableTreeNode("#" + tag + " (" + versionDescription + ")");
				this.addRepositoryTagVersionNodes(tagTreeNode, tagVersions);
				projectTreeNode.add(tagTreeNode);
			}
		}
	}
	/**
	 * Adds the repository tag version nodes.
	 *
	 * @param tagTreeNode the tag tree node
	 * @param tagVersions the tag versions
	 */
	private void addRepositoryTagVersionNodes(DefaultMutableTreeNode tagTreeNode, RepositoryTagVersions tagVersions) {
		
		if (tagVersions!=null) {
			Vector<RepositoryEntry> reVector = tagVersions.getRepositoryTagVectorSorted(false);
			for (int i = 0; i < reVector.size(); i++) {
				tagTreeNode.add(new DefaultMutableTreeNode(reVector.get(i)));
				if (this.showAllVersions==false) break;
			}
		}
	}
	
	/**
	 * Returns the available project IDs of the local projects.
	 * @return the local available project ID's
	 */
	private ArrayList<String> getLocalAvailableProjectIDs() {
		if (localAvailableProjectIDs==null) {
			String[] localProjectDirectories = Application.getGlobalInfo().getProjectSubDirectories();
			if (localProjectDirectories==null) {
				localAvailableProjectIDs = new ArrayList<>();
			} else {
				localAvailableProjectIDs = new ArrayList<>(Arrays.asList(localProjectDirectories));
			}
		}
		return localAvailableProjectIDs;
	}
	
	/**
	 * Sets the selected tree node.
	 * @param selectedNode the new selected tree node
	 */
	private void setSelectedTreeNode(DefaultMutableTreeNode selectedNode) {
		
		boolean isRepositoryEntry = false;
		if (selectedNode==null || selectedNode.getUserObject()==null) {
			isRepositoryEntry = false;
		} else if ((selectedNode.getUserObject() instanceof RepositoryEntry)) {
			isRepositoryEntry = true;
		}
		
		this.getJTextFieldProject().setEnabled(isRepositoryEntry);
		this.getJTextAreaProjectDescription().setEnabled(isRepositoryEntry);
		this.getJTextFieldTag().setEnabled(isRepositoryEntry);
		this.getJTextFieldProjectVersion().setEnabled(isRepositoryEntry);
		this.getJButtonInstallUpdate().setEnabled(isRepositoryEntry);
		
		if (isRepositoryEntry==true) {
			
			RepositoryEntry re = (RepositoryEntry) selectedNode.getUserObject();
			
			this.getJTextFieldProject().setText(re.getProjectName() + " (" + re.getProjectID() + ")");
			this.getJTextAreaProjectDescription().setText(re.getProjectDescription());
			this.getJTextFieldTag().setText(re.getVersionTag());
			this.getJTextFieldProjectVersion().setText(re.getVersion());
			
			if (this.getLocalAvailableProjectIDs().contains(re.getProjectID())) {
				this.getJButtonInstallUpdate().setEnabled(false);
			}
			this.repositoryEntrySelected = re;
			
		} else {
			this.getJTextFieldProject().setText(null);
			this.getJTextAreaProjectDescription().setText(null);
			this.getJTextFieldTag().setText(null);
			this.getJTextFieldProjectVersion().setText(null);
			this.repositoryEntrySelected = null;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJCheckBoxShowAllVersions()) {
			// --- Show / hide all versions -------------------------
			this.showAllVersions = !this.showAllVersions;
			this.buildRepositoryTree();
			
		} else if (ae.getSource()==this.getJButtonRepositoryRefresh() || ae.getSource()==this.getJComboBoxRepositorySelection()) {
			// --- Reload the repository view -----------------------
			this.loadProjectRepositoryFromComboBoxReference();
			
		} else if (ae.getSource()==this.getJButtonInstallUpdate()) {
			// --- Install or Update the project --------------------
			this.installProject();
		}
	}
	
	/**
	 * Installs the currently selected project from the repository.
	 */
	private void installProject() {
		
		String link = this.getRepositoryDirectoryLink();
		if (link==null) return;
		if (this.repositoryEntrySelected==null) return;

		// --- Initiate a ProjectRepositoryUpdate ------------------- 
		ProjectRepositoryUpdate pru = new ProjectRepositoryUpdate(null);
		pru.setProjectRepository(this.projectRepository);
		
		// --- Try to download or copy the file ---------------------
		String downloadFileName = pru.getLinkOrPathWithDirectorySuffix(Application.getGlobalInfo().getPathProjects(true), this.repositoryEntrySelected.getFileName());
		if (pru.downloadOrCopyProjectArchiveFromRepository(link, this.repositoryEntrySelected, downloadFileName)==true) {

			// --- Install the project ------------------------------
			if (pru.importProjectFromArchive(downloadFileName)==true) {
				// --- Close the repository explorer ----------------
				if (this.panelListener!=null) {
					this.panelListener.closeProjectRepositoryExplorer();
				}
				
			} else {
				JOptionPane.showMessageDialog(this, "The installation of the project '" + this.repositoryEntrySelected.getProjectName() + "' failed", "Installation failed", JOptionPane.ERROR_MESSAGE);
				
			}
			
			// --- Delete the archive -------------------------------
			File downloadFile = new File(downloadFileName);
			if (downloadFile.exists()==true) {
				downloadFile.delete();
			}
			
		}
	}
	
	
}
