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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.DesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.agentgui.gui.AwbProjectEditorWindow;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.gui.MainWindow;
import agentgui.core.gui.projectwindow.simsetup.EnvironmentModelSetup;
import agentgui.core.gui.projectwindow.simsetup.StartSetup;
import agentgui.core.project.Project;

/**
 * This extended JInternalFrame graphically encapsulates the the project in the main window (class CoreWindow)
 * 
 * @see MainWindow
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectWindow extends JInternalFrame implements AwbProjectEditorWindow, Observer {

	private static final long serialVersionUID = -1462483441246136949L;

	private Project currProject;

	private JSplitPane jSplitPaneProjectView;
	private JScrollPane jScrollPane;

	private JTree jTreeProject;
	private DefaultTreeModel projectTreeModel;
	private DefaultMutableTreeNode rootNode;
	private DefaultMutableTreeNode startNode;

	private JPopupMenu jPopupMenuTree;
	private JMenuItem jMenueItemMaximizeSelectedTab;
	private JMenuItem jMenueItemDefineAsProjectStartTab;

	private JTabbedPane projectViewRightTabs;
	private ChangeListener tabSelectionListener;
	private MouseListener tabMouseListener;

	private boolean pauseTreeSelectionListener = false;
	private boolean pauseTabSelectionListener = false;

	private HashMap<String, ProjectWindowTab> tab4SubPanel = new HashMap<String, ProjectWindowTab>(); // @jve:decl-index=0:
	private Vector<ProjectWindowTab> tabVector = new Vector<ProjectWindowTab>(); // @jve:decl-index=0:

	private boolean isMaximizedTab = false;
	private MaximizedTab maxTab;
	private ProjectWindowTab maxProjectWindowTab;

	/**
	 * This is the default constructor for a new project window.
	 * 
	 * @param project the current {@link Project}
	 */
	public ProjectWindow(Project project) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);
		this.initialize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.agentgui.gui.AwbProjectEditorWindow#getProject()
	 */
	@Override
	public Project getProject() {
		return this.currProject;
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {

		this.setSize(850, 500);
		this.setContentPane(this.getJSplitPaneProjectView());
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setResizable(false);
		this.setAutoscrolls(true);
		this.setBorder(null);
		this.setFocusable(true);
		((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
	}

	/**
	 * Adds the default project window tabs.
	 */
	public void addDefaultTabs() {

		ProjectWindowTab pwt = null;
		// ------------------------------------------------
		// --- General Informations -----------------------
		pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_END_USER, Language.translate("Info"), null, null, new ProjectInfo(this.currProject), null);
		pwt.add();

		// ------------------------------------------------
		// --- Configuration ------------------------------
		pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Konfiguration"), null, null, new TabForSubPanels(this.currProject), null);
		pwt.add();
		this.registerTabForSubPanels(ProjectWindowTab.TAB_4_SUB_PANES_Configuration, pwt);

		// --- External Resources ---------------------
		pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Ressourcen"), null, null, new ProjectResources(this.currProject), Language.translate("Konfiguration"));
		pwt.add();
		// --- Environment and time model -------------
		pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Agentenumgebung"), null, null, new EnvironmentModel(this.currProject), Language.translate("Konfiguration"));
		pwt.add();
		// --- Used Ontologies ------------------------
		pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Ontologien"), null, null, new OntologyTab(this.currProject), Language.translate("Konfiguration"));
		pwt.add();
		// --- Project Agents -------------------------
		pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Agenten"), null, null, new BaseAgents(this.currProject), Language.translate("Konfiguration"));
		pwt.add();
		// --- JADE-Services --------------------------
		pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_DEVELOPER, "JADE-Services", null, null, new JadeSetupServices(this.currProject), Language.translate("Konfiguration"));
		pwt.add();
		// --- JADE-MTP configuration -----------------
		pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_DEVELOPER, "JADE-Settings", null, null, new JadeSetupMTP(this.currProject), Language.translate("Konfiguration"));
		pwt.add();
		// --- Distribution + Thresholds --------------
		pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Verteilung + Grenzwerte"), null, null, new Distribution(this.currProject), Language.translate("Konfiguration"));
		pwt.add();
		// --- Agent Load Metrics ---------------------
		pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Agenten-Lastmetrik"), null, null, new AgentClassLoadMetricsPanel(this.currProject), Language.translate("Konfiguration"));
		pwt.add();

		// ------------------------------------------------
		// --- Simulations-Setup --------------------------
		pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_END_USER, Language.translate(ProjectWindowTab.TAB_4_SUB_PANES_Setup), null, null, new TabForSubPanels(this.currProject), null);
		pwt.add();
		this.registerTabForSubPanels(ProjectWindowTab.TAB_4_SUB_PANES_Setup, pwt);

		// --- start configuration for agents ---------
		pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_END_USER, Language.translate("Agenten-Start"), null, null, new StartSetup(this.currProject), Language.translate(ProjectWindowTab.TAB_4_SUB_PANES_Setup));
		pwt.add();
		// --- simulation environment -----------------
		pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_END_USER_VISUALIZATION, Language.translate("Umgebungsmodell"), null, null, new EnvironmentModelSetup(this.currProject), Language.translate(ProjectWindowTab.TAB_4_SUB_PANES_Setup));
		pwt.add();

		// ------------------------------------------------
		// --- Visualisation ------------------------------
		pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_END_USER_VISUALIZATION, Language.translate(ProjectWindowTab.TAB_4_RUNTIME_VISUALIZATION), null, null, this.currProject.getVisualizationTab4SetupExecution(), null);
		pwt.add();

		// ------------------------------------------------
		// --- Project Desktop ----------------------------
		pwt = new ProjectWindowTab(this.currProject, ProjectWindowTab.DISPLAY_4_END_USER, Language.translate("Projekt-Desktop"), null, null, new ProjectDesktop(this.currProject), null);
		pwt.add();

		this.projectTreeExpand2Level(3, true);

		this.setVisible(true);
		this.moveToFront();

		// --- Add this ProjectWindow to the JDesktopPane of the MainWindow ---
		if (Application.getMainWindow() != null) {
			Application.getMainWindow().getJDesktopPane4Projects().add(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JInternalFrame#dispose()
	 */
	@Override
	public void dispose() {
		if (this.maxTab != null) {
			this.tabRestore();
		}
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectEditorWindow#getUserFeedbackForClosingProject(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public ProjectCloseUserFeedback getUserFeedbackForClosingProject(String msgTitle, String msgText, Object parentVisualizationComponent) {

		ProjectCloseUserFeedback userFeedback = null;
		
		Component parentComponent = null;
		if (parentVisualizationComponent==null) {
			parentComponent = Application.getMainWindow(); // as default
		} else {
			if (parentVisualizationComponent instanceof Component) {
				 parentComponent = (Component) parentVisualizationComponent;
			}
		}
		
		int msgAnswer = JOptionPane.showConfirmDialog(parentComponent, msgText, msgTitle, JOptionPane.YES_NO_CANCEL_OPTION);
		if (msgAnswer == JOptionPane.CANCEL_OPTION) {
			userFeedback = ProjectCloseUserFeedback.CancelCloseAction;
		} else if (msgAnswer == JOptionPane.YES_OPTION) {
			userFeedback = ProjectCloseUserFeedback.SaveProject;
		} else if (msgAnswer == JOptionPane.NO_OPTION) {
			userFeedback = ProjectCloseUserFeedback.DoNotSaveProject;
		}
		return userFeedback;
	}

	/**
	 * This method initializes ProjectViewSplit.
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPaneProjectView() {
		if (jSplitPaneProjectView == null) {
			jSplitPaneProjectView = new JSplitPane();
			jSplitPaneProjectView.setOneTouchExpandable(true);
			jSplitPaneProjectView.setDividerLocation(210);
			jSplitPaneProjectView.setDividerSize(10);
			jSplitPaneProjectView.setLeftComponent(this.getJScrollPane());
			jSplitPaneProjectView.setRightComponent(this.getProjectViewRightTabs());
		}
		return jSplitPaneProjectView;
	}

	/**
	 * This method initializes jScrollPane.
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBorder(null);
			jScrollPane.setMinimumSize(new Dimension(200, 50));
			jScrollPane.setViewportView(this.getJTreeProject());
		}
		return jScrollPane;
	}

	public DefaultMutableTreeNode getRootNode() {
		if (rootNode == null) {
			rootNode = new DefaultMutableTreeNode(currProject.getProjectName());
		}
		return rootNode;
	}

	private DefaultTreeModel getTreeModel() {
		if (projectTreeModel == null) {
			projectTreeModel = new DefaultTreeModel(this.getRootNode());
		}
		return projectTreeModel;
	}

	public JTree getJTreeProject() {
		if (jTreeProject == null) {
			jTreeProject = new JTree(this.getTreeModel());
			jTreeProject.setName("ProjectTree");
			jTreeProject.setShowsRootHandles(false);
			jTreeProject.setRootVisible(true);
			jTreeProject.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			// ----------------------------------------------------------------
			jTreeProject.addTreeSelectionListener(new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent ts) {
					if (pauseTreeSelectionListener)
						return;
					TreePath pathSelected = ts.getPath();
					if (pathSelected.getPathCount() >= 2) {
						// --- Focus corresponding tab ------------------------
						pauseTabSelectionListener = true;
						DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jTreeProject.getLastSelectedPathComponent();
						setFocus2Tab(selectedNode);
						pauseTabSelectionListener = false;
					}
				}
			});
			// ----------------------------------------------------------------
			// ----------------------------------------------------------------
			jTreeProject.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {

					JTree tree = (JTree) me.getSource();
					TreePath treePath = tree.getPathForLocation(me.getX(), me.getY());
					if (treePath == null)
						return;

					if (SwingUtilities.isRightMouseButton(me)) {
						// --- Display the context menu -----------------------
						Rectangle pathBounds = tree.getUI().getPathBounds(tree, treePath);
						if (pathBounds != null && pathBounds.contains(me.getX(), me.getY())) {
							tree.setSelectionPath(treePath);
							tree.scrollPathToVisible(treePath);
							getJPopupMenuTabTree().show(tree, pathBounds.x, pathBounds.y + pathBounds.height);
						}

					} else if (me.getClickCount() == 2 & SwingUtilities.isLeftMouseButton(me)) {
						// --- Catch double click on node --- -----------------
						currProject.setNotChangedButNotify(Project.VIEW_Maximize);
					}

				}// End - mouseClicked

			});
			// ------------------------------------------------------------------
		}
		return jTreeProject;
	}

	private JPopupMenu getJPopupMenuTabTree() {
		if (jPopupMenuTree == null) {
			jPopupMenuTree = new JPopupMenu();
			jPopupMenuTree.add(this.getJMenuItemMaximizeSelectedTab());
			jPopupMenuTree.add(this.getJMenuItemDefineAsProjectStartTab());
		}
		return jPopupMenuTree;
	}

	private JMenuItem getJMenuItemMaximizeSelectedTab() {
		if (jMenueItemMaximizeSelectedTab == null) {
			jMenueItemMaximizeSelectedTab = new JMenuItem();
			jMenueItemMaximizeSelectedTab.setText(Language.translate("Maximieren"));
			jMenueItemMaximizeSelectedTab.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					currProject.setNotChangedButNotify(Project.VIEW_Maximize);
				}
			});
		}
		return jMenueItemMaximizeSelectedTab;
	}

	private JMenuItem getJMenuItemDefineAsProjectStartTab() {
		if (jMenueItemDefineAsProjectStartTab == null) {
			jMenueItemDefineAsProjectStartTab = new JMenuItem();
			jMenueItemDefineAsProjectStartTab.setText(Language.translate("Als Projekt Start-Tab verwenden"));
			jMenueItemDefineAsProjectStartTab.setIcon(GlobalInfo.getInternalImageIcon("ArrowRight.png"));
			jMenueItemDefineAsProjectStartTab.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					setSelectionAsStartTab();
				}
			});
		}
		return jMenueItemDefineAsProjectStartTab;
	}

	// If expand is true, expands all nodes in the tree.
	// Otherwise, collapses all nodes in the tree.
	/**
	 * Project tree expand2 level.
	 *
	 * @param up2TreeLevel the up2 tree level
	 * @param expand the expand
	 */
	public void projectTreeExpand2Level(Integer up2TreeLevel, boolean expand) {
		if (up2TreeLevel == null) {
			up2TreeLevel = 1000;
		}
		this.projectTreeExpand(new TreePath(this.getRootNode()), expand, 1, up2TreeLevel);
	}

	/**
	 * Project tree expand.
	 *
	 * @param parent the parent
	 * @param expand the expand
	 * @param currNodeLevel the current node level
	 * @param up2TreeLevel the up2 tree level
	 */
	private void projectTreeExpand(TreePath parent, boolean expand, Integer currNodeLevel, Integer up2TreeLevel) {

		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (currNodeLevel >= up2TreeLevel) {
			return;
		}
		if (node.getChildCount() >= 0) {
			for (@SuppressWarnings("rawtypes")
			Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				projectTreeExpand(path, expand, currNodeLevel + 1, up2TreeLevel);
			}
		}
		// Expansion or collapse must be done bottom-up
		if (expand) {
			jTreeProject.expandPath(parent);
		} else {
			jTreeProject.collapsePath(parent);
		}
	}

	/**
	 * This method initializes ProjectViewRight.
	 *
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getProjectViewRightTabs() {
		if (projectViewRightTabs == null) {
			projectViewRightTabs = new JTabbedPane();
			projectViewRightTabs.setName("ProjectTabs");
			projectViewRightTabs.setTabPlacement(JTabbedPane.TOP);
			projectViewRightTabs.setPreferredSize(new Dimension(126, 72));
			projectViewRightTabs.setFont(new Font("Dialog", Font.BOLD, 12));
			projectViewRightTabs.addMouseListener(this.getTabMouseListener());
			projectViewRightTabs.addChangeListener(this.getTabSelectionListener());
		}
		return projectViewRightTabs;
	}

	/**
	 * This method instantiates the MouseListener for the Tab-Selections.
	 *
	 * @return the tab mouse listener
	 */
	public MouseListener getTabMouseListener() {
		if (tabMouseListener == null) {
			tabMouseListener = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {
					if (me.getClickCount() == 2 & SwingUtilities.isLeftMouseButton(me)) {
						if (isMaximizedTab == true) {
							currProject.setNotChangedButNotify(Project.VIEW_Restore);
						} else {
							currProject.setNotChangedButNotify(Project.VIEW_Maximize);
						}
					} else if (me.getClickCount() == 1 & SwingUtilities.isRightMouseButton(me)) {
						getTabPopupMenu().show(me.getComponent(), me.getX(), me.getY());
					}
				}
			};
		}
		return tabMouseListener;
	}

	/**
	 * Returns the JPopupMenu for the tabs.
	 * 
	 * @return the JPopupMenu
	 */
	private JPopupMenu getTabPopupMenu() {

		JMenuItem jme = new JMenuItem();
		if (isMaximizedTab) {
			jme.setText(Language.translate("Wiederherstellen"));
		} else {
			jme.setText(Language.translate("Maximieren"));
		}
		jme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isMaximizedTab) {
					currProject.setNotChangedButNotify(Project.VIEW_Restore);
				} else {
					currProject.setNotChangedButNotify(Project.VIEW_Maximize);
				}
			}
		});

		JPopupMenu pop = new JPopupMenu();
		pop.add(jme);
		return pop;

	}

	/**
	 * Maximise the Project-Window within the AgenGUI-Application
	 */
	public void setMaximized() {

		MainWindow mainWindow = Application.getMainWindow();
		if (mainWindow != null) {
			// --- Validate the main application window -----------------
			mainWindow.validate();
			// --- Be sure that everything is there as needed -----------
			if (this.getParent() != null) {
				((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
				DesktopManager dtm = mainWindow.getJDesktopPane4Projects().getDesktopManager();
				if (dtm != null) {
					dtm.maximizeFrame(this);
				}
			}
			mainWindow.setCloseButtonPosition(true);
		}
	}

	/**
	 * Returns the MaximizedTab (extends JInteralFrame), with the currently selected tab.
	 * 
	 * @return the MaximizedTab (extends JInteralFrame)
	 */
	private MaximizedTab getMaximizedTab() {
		if (maxTab == null) {
			// --- Get and remove the currently selected tab --------
			this.maxProjectWindowTab = this.getProjectWindowTabSelected();
			this.maxProjectWindowTab.updateIndexPosition();
			this.removeProjectTab(this.maxProjectWindowTab);
			// --- Create the extra JInternalFrame ------------------
			maxTab = new MaximizedTab(this, this.maxProjectWindowTab.getTitle());
			maxTab.add(this.maxProjectWindowTab.getJComponentForVisualization());
		}
		return maxTab;
	}

	/**
	 * Tab maximize.
	 */
	private void tabMaximize() {

		// --- Maximize the main window ---------------------------------------
		// Application.getMainWindow().setExtendedState(JFrame.MAXIMIZED_BOTH);

		// --- Open a new JInteraFrame with the current tab enlarged ----------
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JDesktopPane appDesktop = Application.getMainWindow().getJDesktopPane4Projects();
				if (maxTab == null) {
					appDesktop.add(getMaximizedTab());
					Application.getMainWindow().addJToolbarComponent(getMaximizedTab().getJButtonRestore4MainToolBar());
				}

				DesktopManager dtm = Application.getMainWindow().getJDesktopPane4Projects().getDesktopManager();
				if (dtm != null) {
					dtm.activateFrame(getMaximizedTab());
					dtm.maximizeFrame(getMaximizedTab());
				}
			}
		});
	}

	/**
	 * Tab restore.
	 */
	public void tabRestore() {

		if (this.maxTab != null) {

			// --- Remove toolbar button --------------------------------------
			Application.getMainWindow().removeJToolbarComponent(this.getMaximizedTab().getJButtonRestore4MainToolBar());
			this.maxTab.setVisible(false);

			// --- Place the enlarged tab back to the other one ---------------
			if (this.maxProjectWindowTab != null) {
				this.addProjectTab(this.maxProjectWindowTab, this.maxProjectWindowTab.getIndexPosition());
				this.maxProjectWindowTab.getJComponentForVisualization().validate();
				this.setFocus2Tab(this.maxProjectWindowTab.getTitle());
				this.maxProjectWindowTab = null;

			}
			// --- Destroy the extra JInteralFrame ----------------------------
			this.maxTab.dispose();

			// --- Final configuration for the next time ----------------------
			this.maxTab = null;
			this.isMaximizedTab = false;

			// --- Refresh view -----------------------------------------------
			Application.getMainWindow().validate();
			Application.getMainWindow().repaint();

		}
	}

	/**
	 * This method instantiates the ChangeListener for Tab-Selections.
	 * 
	 * @return the tab selection listener
	 */
	public ChangeListener getTabSelectionListener() {

		if (tabSelectionListener == null) {
			tabSelectionListener = new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent evt) {

					if (pauseTabSelectionListener)
						return;

					ProjectWindowTab pwt = getProjectWindowTabSelected();
					if (pwt != null) {
						DefaultMutableTreeNode selectedNode = getTreeNode(pwt.getTitle());
						DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) jTreeProject.getLastSelectedPathComponent();
						if (selectedNode != currentNode) {
							pauseTreeSelectionListener = true;
							jTreeProject.setSelectionPath(new TreePath(selectedNode.getPath()));
							pauseTreeSelectionListener = false;
						}
					}
				}
			};
		}
		return tabSelectionListener;
	}

	/**
	 * Gets the currently selected tab as instance of ProjectWindowTab.
	 * 
	 * @return the tab selected
	 */
	private ProjectWindowTab getProjectWindowTabSelected() {

		ProjectWindowTab tabSelected = null;

		int selIndex = this.projectViewRightTabs.getSelectedIndex();
		if (selIndex > -1) {
			String tabTitle = this.projectViewRightTabs.getTitleAt(selIndex);
			tabSelected = getProjectWindowTab(tabTitle);
			if (tabSelected != null) {
				JTabbedPane subPane = tabSelected.getCompForChildComp();
				if (subPane != null) {
					int selSubIndex = subPane.getSelectedIndex();
					if (selSubIndex != -1) {
						tabTitle = subPane.getTitleAt(selSubIndex);
						tabSelected = this.getProjectWindowTab(tabTitle);
					}
				}
			}
		}
		return tabSelected;

	}

	/**
	 * Returns the ProjectWindowTab specified by its name.
	 *
	 * @param tabTitle the tab title
	 * @return the instance of ProjectWindowTab
	 */
	private ProjectWindowTab getProjectWindowTab(String tabTitle) {
		for (ProjectWindowTab pwt : tabVector) {
			if (pwt.getTitle().equals(tabTitle)) {
				return pwt;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.agentgui.gui.AwbProjectEditorWindow#addProjectTab(agentgui.core.gui.projectwindow.ProjectWindowTab)
	 */
	@Override
	public void addProjectTab(ProjectWindowTab projectWindowTab) {
		this.addProjectTab(projectWindowTab, -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.agentgui.gui.AwbProjectEditorWindow#addProjectTab(agentgui.core.gui.projectwindow.ProjectWindowTab, int)
	 */
	@Override
	public void addProjectTab(ProjectWindowTab projectWindowTab, int indexPosition) {

		int newIndexPos = indexPosition;

		if (newIndexPos < 0) {
			newIndexPos = projectViewRightTabs.getTabCount(); // Default
			String parentName = projectWindowTab.getParentName();
			if (parentName != null) {
				ProjectWindowTab parentPWT = this.getProjectWindowTab(parentName);
				newIndexPos = parentPWT.getCompForChildComp().getTabCount();
			}
		}
		projectWindowTab.setIndexPosition(newIndexPos);

		// --- add to reminder vector -----------
		this.tabVector.add(projectWindowTab);

		// --- use the private function ---------
		this.addProjectTabInternal(projectWindowTab);

	}

	/**
	 * Adds a Project-Tab and a new node (child of a specified parent) to the ProjectWindow.
	 *
	 * @param projectWindowTab the project window tab
	 * @return the default mutable tree node
	 */
	private DefaultMutableTreeNode addProjectTabInternal(ProjectWindowTab projectWindowTab) {

		// --- create Node ----------------------
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(projectWindowTab);
		DefaultMutableTreeNode pareNode = null;
		JTabbedPane tabbedPaneParent = null;

		String parentName = projectWindowTab.getParentName();

		// --- add to the TreeModel -------------
		if (parentName != null) {
			pareNode = getTreeNode(parentName);
			ProjectWindowTab pareNodePWT = (ProjectWindowTab) pareNode.getUserObject();
			tabbedPaneParent = pareNodePWT.getCompForChildComp();
			// --- add ChangeListener -----------
			this.addChangeListener(tabbedPaneParent);

		} else {
			pareNode = this.getRootNode();
			tabbedPaneParent = projectViewRightTabs;
		}

		if (projectWindowTab.getIndexPosition() != -1 && projectWindowTab.getIndexPosition() < pareNode.getChildCount()) {
			// --- Add to parent node/tab at index position ----
			pareNode.insert(newNode, projectWindowTab.getIndexPosition());
			tabbedPaneParent.insertTab(projectWindowTab.getTitle(), projectWindowTab.getIcon(), projectWindowTab.getJComponentForVisualization(), projectWindowTab.getTipText(), projectWindowTab.getIndexPosition());
		} else {
			// --- Just add to parent node/tab -----------------
			pareNode.add(newNode);
			tabbedPaneParent.addTab(projectWindowTab.getTitle(), projectWindowTab.getIcon(), projectWindowTab.getJComponentForVisualization(), projectWindowTab.getTipText());
		}

		// --- refresh view ---------------------
		this.getTreeModel().reload();
		this.projectTreeExpand2Level(3, true);
		return newNode;
	}

	/**
	 * Adds the local ChangeListener named 'tabSelectionListener' to a JTabbedPane if not already there.
	 *
	 * @param pane the pane
	 */
	private void addChangeListener(JTabbedPane pane) {

		boolean listenerFound = false;
		ChangeListener[] listener = pane.getChangeListeners();
		for (int i = 0; i < listener.length; i++) {
			ChangeListener cl = listener[i];
			if (cl == this.getTabSelectionListener()) {
				listenerFound = true;
			}
		}

		if (listenerFound == false) {
			pane.addChangeListener(this.getTabSelectionListener());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.agentgui.gui.AwbProjectEditorWindow#removeProjectTab(agentgui.core.gui.projectwindow.ProjectWindowTab)
	 */
	@Override
	public void removeProjectTab(ProjectWindowTab projectWindowTab) {

		DefaultMutableTreeNode node = this.getTreeNode(projectWindowTab.getTitle());
		if (node != null) {
			DefaultMutableTreeNode pareNode = (DefaultMutableTreeNode) node.getParent();
			pareNode.remove(node);
		}

		JComponent component = projectWindowTab.getJComponentForVisualization();
		Container container = component.getParent();
		if (container != null) {
			container.remove(component);
		}
		this.tabVector.remove(projectWindowTab);

		this.getTreeModel().reload();
		this.projectTreeExpand2Level(3, true);
	}

	/**
	 * This method removes all ProjectWindowTabs from the current display.
	 */
	private void removeAllProjectWindowTabsTemporary4Rebuilding() {

		Vector<ProjectWindowTab> pwtVector = new Vector<ProjectWindowTab>(this.tabVector);
		for (int i = 0; i < pwtVector.size(); i++) {
			ProjectWindowTab pwt = pwtVector.get(i);
			if (pwt.getCompForChildComp()!=null) {
				pwt.getCompForChildComp().removeAll();
			}
		}
		this.getRootNode().removeAllChildren();
		this.projectViewRightTabs.removeAll();
	}

	/**
	 * Returns the Tree-Node requested by the Reference.
	 *
	 * @param searchFor the search for
	 * @return the tree node
	 */
	@SuppressWarnings("unchecked")
	public DefaultMutableTreeNode getTreeNode(String searchFor) {

		DefaultMutableTreeNode nodeFound = null;
		DefaultMutableTreeNode currNode = null;
		String currNodeText;

		for (Enumeration<DefaultMutableTreeNode> e = this.getRootNode().breadthFirstEnumeration(); e.hasMoreElements();) {
			currNode = e.nextElement();
			currNodeText = currNode.getUserObject().toString();
			if (currNodeText.equals(searchFor)) {
				nodeFound = currNode;
				break;
			}
		}
		return nodeFound;
	}

	/**
	 * Sets the focus to a specified Tab of the project Window.
	 * 
	 * @param searchFor the new focus2 tab
	 */
	public void setFocus2Tab(String searchFor) {
		DefaultMutableTreeNode currNode = getTreeNode(searchFor);
		this.setFocus2Tab(currNode);
	}

	/**
	 * Sets the focus to a specified Tab of the project Window.
	 * 
	 * @param pwt the new focus2 tab
	 */
	public void setFocus2Tab(ProjectWindowTab pwt) {
		String tabCaption = pwt.getTitle();
		setFocus2Tab(tabCaption);
	}

	/**
	 * Sets the focus to a specified Tab of the {@link ProjectWindow}, where the node represents the corresponding node of
	 * the project tree.
	 * 
	 * @param node2Focus the new focus2 tab
	 */
	public void setFocus2Tab(DefaultMutableTreeNode node2Focus) {

		if (node2Focus == null)
			return;

		Vector<DefaultMutableTreeNode> nodeArray = new Vector<DefaultMutableTreeNode>();
		nodeArray.add(node2Focus);

		DefaultMutableTreeNode currNode = node2Focus;
		while (currNode.getParent() != null) {
			currNode = (DefaultMutableTreeNode) currNode.getParent();
			if (currNode != this.getRootNode()) {
				nodeArray.add(currNode);
			}
		}
		for (int i = nodeArray.size() - 1; i > -1; i--) {
			currNode = nodeArray.get(i);
			if (currNode.getUserObject() instanceof ProjectWindowTab) {
				ProjectWindowTab pwt = (ProjectWindowTab) currNode.getUserObject();
				if (pwt != null && pwt.getJComponentForVisualization() != null) {
					Component displayElement = pwt.getJComponentForVisualization();
					if (displayElement.getParent() != null) {
						((JTabbedPane) displayElement.getParent()).setSelectedComponent(displayElement);
					}
				}
			}
		}
	}

	/**
	 * Sets the current selection as the start tab for the project.
	 */
	private void setSelectionAsStartTab() {
		TreePath treePath = getJTreeProject().getSelectionPath();
		if (treePath != null) {
			DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
			this.setStartTabNode(selectedTreeNode);
		}
	}

	/**
	 * Sets the focus to the defined projects start tab.
	 */
	private void setFocus2StartTab() {
		this.startNode = this.getStartTabNode();
		this.setFocus2Tab(this.startNode);
	}

	/**
	 * Sets the specified tree node as as the start tab for the project.
	 * 
	 * @param selectedTreeNode the selected tree node that is to be used as start tab
	 */
	public void setStartTabNode(DefaultMutableTreeNode selectedTreeNode) {

		String nodeIndexPath = null;
		if (selectedTreeNode != null && selectedTreeNode != getRootNode()) {
			// --- Remind the path of the start tab -----------
			while (selectedTreeNode.getParent() != null) {
				Integer indexPostion = selectedTreeNode.getParent().getIndex(selectedTreeNode);
				if (nodeIndexPath == null) {
					nodeIndexPath = indexPostion.toString();
				} else {
					nodeIndexPath = indexPostion.toString() + "|" + nodeIndexPath;
				}
				selectedTreeNode = (DefaultMutableTreeNode) selectedTreeNode.getParent();
			}
		}
		this.currProject.setProjectStartTab(nodeIndexPath);
	}

	/**
	 * Returns the start tab node.
	 * 
	 * @return the start tab node
	 */
	public DefaultMutableTreeNode getStartTabNode() {

		DefaultMutableTreeNode currNode = this.getRootNode();
		String nodeIndexPath = this.currProject.getProjectStartTab();
		if (nodeIndexPath != null) {
			String[] nodeIndexPathArray = nodeIndexPath.split("\\|");
			for (int i = 0; i < nodeIndexPathArray.length; i++) {
				String nodeIndexString = nodeIndexPathArray[i];
				if (nodeIndexString != null && nodeIndexString.equals("") == false) {
					Integer nodeIndex = Integer.parseInt(nodeIndexString);
					if (nodeIndex < 0 || nodeIndex > (currNode.getChildCount() - 1)) {
						break;
					} else {
						currNode = (DefaultMutableTreeNode) currNode.getChildAt(nodeIndex);
					}
				}
			}
		}
		return currNode;
	}

	/**
	 * Returns the start tab information.
	 * 
	 * @return the start tab information
	 */
	public String getStartTabInformation() {

		DefaultMutableTreeNode currNode = this.getRootNode();
		String startTabInfo = "/ " + currNode.getUserObject().toString();

		String nodeIndexPath = this.currProject.getProjectStartTab();
		if (nodeIndexPath != null) {
			String[] nodeIndexPathArray = nodeIndexPath.split("\\|");
			for (int i = 0; i < nodeIndexPathArray.length; i++) {
				String nodeIndexString = nodeIndexPathArray[i];
				if (nodeIndexString != null && nodeIndexString.equals("") == false) {
					Integer nodeIndex = Integer.parseInt(nodeIndexString);
					if (nodeIndex < 0 || nodeIndex > (currNode.getChildCount() - 1)) {
						break;
					} else {
						currNode = (DefaultMutableTreeNode) currNode.getChildAt(nodeIndex);
						startTabInfo = startTabInfo + " / " + currNode.getUserObject().toString();
					}
				}
			}
		}
		return startTabInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.agentgui.gui.AwbProjectEditorWindow#validateStartTab()
	 */
	@Override
	public void validateStartTab() {
		// --- Find and set the new start tab ---------
		DefaultMutableTreeNode newStartNode = this.getTreeNode(this.startNode.getUserObject().toString());
		this.setStartTabNode(newStartNode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.agentgui.gui.AwbProjectEditorWindow#setViewForDeveloperOrEndUser()
	 */
	@Override
	public void setViewForDeveloperOrEndUser() {

		// --- Which view should be displayed ? ---------------------
		int viewToSet = 0;
		if (this.currProject.getProjectView().equals(Project.VIEW_User)) {
			viewToSet = ProjectWindowTab.DISPLAY_4_END_USER;
		} else {
			viewToSet = ProjectWindowTab.DISPLAY_4_DEVELOPER;
		}

		// --- Remind the current selection -------------------------
		DefaultMutableTreeNode lastSelectedNode = null;
		if (this.getJTreeProject().getSelectionPath() != null) {
			lastSelectedNode = (DefaultMutableTreeNode) this.getJTreeProject().getSelectionPath().getLastPathComponent();
		}

		// --- Exists a panel for the current environment model? ----
		Class<? extends EnvironmentController> envController = this.currProject.getEnvironmentModelType().getEnvironmentControllerClass();
		// --- Pause the TreeSelectionListener ----------------------
		this.pauseTreeSelectionListener = true;

		// --- Rebuild the display out of the tabVector -------------
		this.getJTreeProject().setSelectionPath(null);
		this.removeAllProjectWindowTabsTemporary4Rebuilding();

		Vector<ProjectWindowTab> pwtVector = new Vector<ProjectWindowTab>(this.tabVector);
		for (int i = 0; i < pwtVector.size(); i++) {
			
			ProjectWindowTab pwt = pwtVector.get(i);
			int displayType = pwt.getDisplayType();

			// --- Which view to the project is needed ? ------------
			if (viewToSet == ProjectWindowTab.DISPLAY_4_DEVELOPER) {
				// --- show everything ------------------------------
				this.addProjectTabInternalCheckVisualization(pwt, envController);

			} else if (viewToSet == ProjectWindowTab.DISPLAY_4_END_USER) {
				// --- show only the end user displays --------------
				if (displayType < ProjectWindowTab.DISPLAY_4_DEVELOPER) {
					this.addProjectTabInternalCheckVisualization(pwt, envController);
				}
			}
		}

		// --- update view of the tree ------------------------------
		this.getTreeModel().reload();
		this.jTreeProject.revalidate();
		this.jTreeProject.repaint();
		this.projectTreeExpand2Level(3, true);

		// --- update view of the tabs ------------------------------
		this.revalidate();
		this.updateUI();
		this.repaint();
		this.currProject.setMaximized();

		// --- Set the focus to the last selected Tab ---------------
		if (lastSelectedNode != null) {
			this.setFocus2Tab(lastSelectedNode);
		}

		// --- Reactivate the TreeSelectionListener -----------------
		this.pauseTreeSelectionListener = false;
	}

	/**
	 * This method adds a ProjectWindowTab to the window and does the check for an {@link EnvironmentController} and it's
	 * visualisation .
	 *
	 * @param pwt the ProjectWindowTab to add
	 * @param envControllerClass the env controller class
	 * @return the default mutable tree node
	 */
	private DefaultMutableTreeNode addProjectTabInternalCheckVisualization(ProjectWindowTab pwt, Class<? extends EnvironmentController> envControllerClass) {

		DefaultMutableTreeNode newNode = null;
		if (pwt.getDisplayType() == ProjectWindowTab.DISPLAY_4_END_USER_VISUALIZATION) {
			// --- Show Visualisation-Tab only in case of ----------
			// --- an available defined Visualisation-Panel ---------
			if (envControllerClass != null) {
				newNode = this.addProjectTabInternal(pwt);
			}
		} else {
			newNode = this.addProjectTabInternal(pwt);
		}
		return newNode;
	}

	/**
	 * Can be used in order to register a tab which can hold sub panes, by using a JTabbedPane. So it allows the later
	 * access.
	 *
	 * @param name the name
	 * @param tab4SubPanes the tab4 sub panes
	 */
	public void registerTabForSubPanels(String name, ProjectWindowTab tab4SubPanes) {
		this.tab4SubPanel.put(name, tab4SubPanes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.agentgui.gui.AwbProjectEditorWindow#getTabForSubPanels(java.lang.String)
	 */
	@Override
	public ProjectWindowTab getTabForSubPanels(String name) {
		return this.tab4SubPanel.get(name);
	}

	/**
	 * Can be used in order to unregister a tab which can hold sub panes, by using a JTabbedPane.
	 *
	 * @param name the name
	 */
	public void deregisterTabForSubPanels(String name) {
		this.tab4SubPanel.remove(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {

		String ObjectName = updateObject.toString();
		if (ObjectName.equalsIgnoreCase(Project.CHANGED_ProjectName)) {
			this.getRootNode().setUserObject(currProject.getProjectName());
			this.getTreeModel().nodeChanged(this.getRootNode());
			this.getJTreeProject().repaint();
			Application.setTitelAddition(currProject.getProjectName());

		} else if (ObjectName.equals(Project.CHANGED_ProjectView)) {
			this.setViewForDeveloperOrEndUser();

		} else if (ObjectName.equals(Project.CHANGED_EnvironmentModelType)) {
			this.setViewForDeveloperOrEndUser();

		} else if (ObjectName.equals(Project.VIEW_Maximize)) {
			this.tabMaximize();

		} else if (ObjectName.equals(Project.VIEW_Restore)) {
			this.tabRestore();

		} else if (ObjectName.equals(Project.VIEW_TabsLoaded)) {
			this.setFocus2StartTab();
		}
		this.validate();
		this.repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.agentgui.gui.AwbProjectEditorWindow#showErrorMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public void showErrorMessage(String msgText, String msgHead) {
		JOptionPane.showMessageDialog(this, msgText, msgHead, JOptionPane.ERROR_MESSAGE);
	}

}
