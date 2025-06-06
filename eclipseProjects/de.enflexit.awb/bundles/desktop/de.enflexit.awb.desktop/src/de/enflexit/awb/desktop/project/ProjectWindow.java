package de.enflexit.awb.desktop.project;

import java.awt.BorderLayout;
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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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


import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.environment.EnvironmentController;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.ui.AwbProjectWindow;
import de.enflexit.awb.core.ui.AwbProjectWindowTab;
import de.enflexit.awb.desktop.mainWindow.MainWindow;
import de.enflexit.awb.desktop.project.setup.EnvironmentModelSetup;
import de.enflexit.awb.desktop.project.setup.SetupPropertiesPanel;
import de.enflexit.awb.desktop.project.setup.StartSetup;
import de.enflexit.common.Observable;
import de.enflexit.common.Observer;
import de.enflexit.common.swing.AwbTabbedPaneHeaderPainter;
import de.enflexit.language.Language;

/**
 * This extended JInternalFrame graphically encapsulates the the project in the main window (class CoreWindow)
 * 
 * @see MainWindow
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectWindow extends JInternalFrame implements AwbProjectWindow, Observer {

	private static final long serialVersionUID = -1462483441246136949L;

	private Project currProject;

	private JPanel jPanelContent;
	private JSplitPane jSplitPaneProjectView;
	private JScrollPane jScrollPane;

	private JTree jTreeProject;
	private DefaultTreeModel projectTreeModel;
	private DefaultMutableTreeNode rootNode;
	private DefaultMutableTreeNode startNode;
	
	private Vector<JInternalFrame> projectDesktopFrameVector;

	private JPopupMenu jPopupMenuTree;
	private JMenuItem jMenueItemMaximizeSelectedTab;
	private JMenuItem jMenueItemDefineAsProjectStartTab;

	private JTabbedPane jTabbedPaneProjectWindowTabs;
	private ChangeListener tabSelectionListener;
	private MouseListener tabMouseListener;

	private boolean pauseTreeSelectionListener;
	private boolean pauseTabSelectionListener;

	private HashMap<String, ProjectWindowTab> tab4SubPanelHash;
	private Vector<ProjectWindowTab> pwtVector;

	private boolean isMaximizedTab;
	private MaximizedTab maxTab;
	private ProjectWindowTab maxProjectWindowTab;


	/** This panel holds the instance of environment model display */
	private JPanel4Visualization visualizationTab4SetupExecution;

	
	/**
	 * This is the default constructor for a new project window.
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
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setResizable(false);
		this.setAutoscrolls(true);
		this.setBorder(null);
		this.setFocusable(true);
		
		((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
		this.setBorder(BorderFactory.createEmptyBorder());

		this.setContentPane(this.getJPanelContent());
	}

	/*
	 * (non-Javadoc)
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
			parentComponent = (MainWindow) Application.getMainWindow(); // as default
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
	
	private JPanel getJPanelContent() {
		if (jPanelContent==null) {
			jPanelContent = new JPanel();
			jPanelContent.setLayout(new BorderLayout());
			jPanelContent.setBorder(BorderFactory.createEmptyBorder());
			jPanelContent.add(this.getJSplitPaneProjectView(), BorderLayout.CENTER);
		}
		return jPanelContent;
	}
	private JSplitPane getJSplitPaneProjectView() {
		if (jSplitPaneProjectView == null) {
			jSplitPaneProjectView = new JSplitPane();
			jSplitPaneProjectView.setOneTouchExpandable(true);
			jSplitPaneProjectView.setDividerLocation(210);
			jSplitPaneProjectView.setDividerSize(10);
			jSplitPaneProjectView.setLeftComponent(this.getJScrollPane());
			jSplitPaneProjectView.setRightComponent(this.getJTabbedPaneProjectWindowTabs());
		}
		return jSplitPaneProjectView;
	}
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
					if (pauseTreeSelectionListener) return;
					
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
					if (treePath == null) return;

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

	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectEditorWindow#setProjectTreeVisible(boolean)
	 */
	@Override
	public void setProjectTreeVisible(boolean isTreeVisible) {
		this.getJPanelContent().removeAll();
		if (isTreeVisible==true) {
			this.getJPanelContent().add(this.getJSplitPaneProjectView(), BorderLayout.CENTER);
			this.getJSplitPaneProjectView().setLeftComponent(this.getJScrollPane());
			this.getJSplitPaneProjectView().setRightComponent(this.getJTabbedPaneProjectWindowTabs());
		} else {
			this.getJPanelContent().add(this.getJTabbedPaneProjectWindowTabs(), BorderLayout.CENTER);
		}
		this.getJPanelContent().validate();
		this.getJPanelContent().repaint();
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

	/**
	 * Will expand all nodes in the tree until the specified tree level.
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
				this.projectTreeExpand(path, expand, currNodeLevel + 1, up2TreeLevel);
			}
		}
		// Expansion or collapse must be done bottom-up
		if (expand) {
			jTreeProject.expandPath(parent);
		} else {
			jTreeProject.collapsePath(parent);
		}
	}

	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectEditorWindow#setProjectTabHeaderVisible(boolean)
	 */
	@Override
	public void setProjectTabHeaderVisible(boolean isProjectTabHeaderVisible) {
		
		this.setTabHeaderVisible(this.getJTabbedPaneProjectWindowTabs(), isProjectTabHeaderVisible);
		if (this.getContentPane().getComponent(0)==this.getJSplitPaneProjectView()) {
			// --- Project tree is visible ----------------
			this.getJSplitPaneProjectView().setRightComponent(this.getJTabbedPaneProjectWindowTabs());
		} else {
			// --- Project tree is NOT visible ------------
			this.getContentPane().add(this.getJTabbedPaneProjectWindowTabs(), 0);
		}
	}
	/**
	 * Sets the specified tab header visible or not.
	 *
	 * @param jTabbedPane the JTabbedPane to adjust
	 * @param isProjectTabHeaderVisible the is project tab header visible
	 */
	private void setTabHeaderVisible(JTabbedPane jTabbedPane, boolean isTabHeaderVisible) {
		
		if (jTabbedPane.getUI() instanceof AwbTabbedPaneHeaderPainter) {
			AwbTabbedPaneHeaderPainter tabUI = (AwbTabbedPaneHeaderPainter) jTabbedPane.getUI();
			tabUI.setTabHeaderVisible(isTabHeaderVisible);
		}

		for (int i=0; i<jTabbedPane.getTabCount(); i++) {
			Component subComp = jTabbedPane.getComponentAt(i);
			if (subComp instanceof TabForSubPanels) {
				TabForSubPanels tabForSubPanels = (TabForSubPanels) subComp;
				this.setTabHeaderVisible(tabForSubPanels.getJTabbedPane(), isTabHeaderVisible);
			}
		}
	}
	
	
	/**
	 * This method initializes ProjectViewRight.
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPaneProjectWindowTabs() {
		if (jTabbedPaneProjectWindowTabs == null) {
			jTabbedPaneProjectWindowTabs = new JTabbedPane();
			jTabbedPaneProjectWindowTabs.setTabPlacement(JTabbedPane.TOP);
			jTabbedPaneProjectWindowTabs.setName("ProjectTabs");
			jTabbedPaneProjectWindowTabs.setFont(new Font("Dialog", Font.BOLD, 13));
			jTabbedPaneProjectWindowTabs.addMouseListener(this.getTabMouseListener());
			jTabbedPaneProjectWindowTabs.addChangeListener(this.getTabSelectionListener());
		}
		return jTabbedPaneProjectWindowTabs;
	}
	/**
	 * This method instantiates the MouseListener for the Tab-Selections.
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
	 * This method instantiates the ChangeListener for Tab-Selections.
	 * @return the tab selection listener
	 */
	public ChangeListener getTabSelectionListener() {

		if (tabSelectionListener == null) {
			tabSelectionListener = new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent evt) {

					if (pauseTabSelectionListener) return;

					AwbProjectWindowTab pwt = getProjectWindowTabSelected();
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
	 * Maximize the Project-Window within the AgenGUI-Application
	 */
	public void setMaximized() {

		MainWindow mainWindow = (MainWindow) Application.getMainWindow();
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
			mainWindow.setCloseProjectButtonVisible(true);
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

		// --- Open a new JInteraFrame with the current tab enlarged ----------
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				MainWindow mainWindow = ((MainWindow)Application.getMainWindow());
				JDesktopPane appDesktop = mainWindow.getJDesktopPane4Projects();
				if (maxTab == null) {
					appDesktop.add(getMaximizedTab());
					mainWindow.addToolbarComponent(getMaximizedTab().getJButtonRestore4MainToolBar());
				}

				DesktopManager dtm = mainWindow.getJDesktopPane4Projects().getDesktopManager();
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
			Application.getMainWindow().removeComponent(this.getMaximizedTab().getJButtonRestore4MainToolBar());
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
			MainWindow mw = (MainWindow) Application.getMainWindow(); 
			mw.validate();
			mw.repaint();

		}
	}


	/**
	 * Gets the currently selected tab as instance of ProjectWindowTab.
	 * @return the tab selected
	 */
	private ProjectWindowTab getProjectWindowTabSelected() {

		ProjectWindowTab tabSelected = null;

		int selIndex = this.getJTabbedPaneProjectWindowTabs().getSelectedIndex();
		if (selIndex > -1) {
			String tabTitle = this.getJTabbedPaneProjectWindowTabs().getTitleAt(selIndex);
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
	 * Gets the project window tab vector.
	 * @return the project window tab vector
	 */
	public Vector<ProjectWindowTab> getProjectWindowTabVector() {
		if (pwtVector==null) {
			pwtVector = new Vector<>();
		}
		return pwtVector;
	}
	/**
	 * Returns the ProjectWindowTab specified by its name.
	 *
	 * @param tabTitle the tab title
	 * @return the instance of ProjectWindowTab
	 */
	private ProjectWindowTab getProjectWindowTab(String tabTitle) {
		for (int i = 0; i < this.getProjectWindowTabVector().size(); i++) {
			ProjectWindowTab pwt = this.getProjectWindowTabVector().get(i);
			if (pwt.getTitle().equals(tabTitle)) {
				return pwt;
			}
		}
		return null;
	}

	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectEditorWindow#addDefaultTabs()
	 */
	@Override
	public void addDefaultTabs() {

		ProjectWindowTab pwt = null;
		// ------------------------------------------------
		// --- General Informations -----------------------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_END_USER, Language.translate("Info"), null, null, new ProjectInfo(this.currProject), null);
		pwt.add();

		
		// ------------------------------------------------
		// --- Configuration ------------------------------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Konfiguration"), null, null, new TabForSubPanels(this.currProject), null);
		pwt.add();
		this.registerTabForSubPanels(AwbProjectWindowTab.TAB_4_SUB_PANES_Configuration, pwt);

		// --- External Resources ---------------------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Ressourcen"), null, null, new ProjectResources(this.currProject), Language.translate("Konfiguration"));
		pwt.add();
		// --- Used Ontologies ------------------------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Ontologien"), null, null, new OntologyTab(this.currProject), Language.translate("Konfiguration"));
		pwt.add();
		// --- Project Agents -------------------------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Agenten"), null, null, new BaseAgents(this.currProject), Language.translate("Konfiguration"));
		pwt.add();
		// --- JADE-Services --------------------------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_DEVELOPER, "JADE-Services", null, null, new JadeSetupServices(this.currProject), Language.translate("Konfiguration"));
		pwt.add();
		// --- JADE-MTP configuration -----------------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_DEVELOPER, "JADE-Settings", null, null, new JadeSetupMTP(this.currProject), Language.translate("Konfiguration"));
		pwt.add();
		// --- Distribution + Thresholds --------------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Verteilung + Grenzwerte"), null, null, new Distribution(this.currProject), Language.translate("Konfiguration"));
		pwt.add();
		// --- Agent Load Metrics ---------------------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Agenten-Lastmetrik"), null, null, new AgentClassLoadMetricsPanel(this.currProject), Language.translate("Konfiguration"));
		pwt.add();
		// --- Project Properties ---------------------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Projekt-Eigenschaften"), null, null, new ProjectPropertiesPanel(this.currProject, Language.translate("Projekt-Eigenschaften")), Language.translate("Konfiguration"));
		pwt.add();
		
		
		// ------------------------------------------------
		// --- Simulations-Setup --------------------------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_END_USER, Language.translate(AwbProjectWindowTab.TAB_4_SUB_PANES_Setup), null, null, new TabForSubPanels(this.currProject), null);
		pwt.add();
		this.registerTabForSubPanels(AwbProjectWindowTab.TAB_4_SUB_PANES_Setup, pwt);

		// --- Setup Properties -----------------------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Setup-Eigenschaften"), null, null, new SetupPropertiesPanel(this.currProject, Language.translate("Setup-Eigenschaften")), Language.translate(AwbProjectWindowTab.TAB_4_SUB_PANES_Setup));
		pwt.add();
		// --- start configuration for agents ---------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_END_USER, Language.translate("Agenten-Start"), null, null, new StartSetup(this.currProject), Language.translate(AwbProjectWindowTab.TAB_4_SUB_PANES_Setup));
		pwt.add();
		// --- simulation environment -----------------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_END_USER_VISUALIZATION, Language.translate("Umgebungsmodell"), null, null, new EnvironmentModelSetup(this.currProject), Language.translate(AwbProjectWindowTab.TAB_4_SUB_PANES_Setup));
		pwt.add();
		
		
		// ------------------------------------------------
		// --- Visualization ------------------------------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_END_USER_VISUALIZATION, Language.translate(AwbProjectWindowTab.TAB_4_RUNTIME_VISUALIZATION), null, null, this.getRuntimeVisualizationContainer(), null);
		pwt.add();

		
		// ------------------------------------------------
		// --- Project Desktop ----------------------------
		pwt = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_END_USER, Language.translate("Projekt-Desktop"), null, null, new ProjectDesktop(this.currProject), null);
		pwt.add();

		this.projectTreeExpand2Level(3, true);

		this.setProjectTreeVisible(this.currProject.isProjectTreeVisible());
		this.setProjectTabHeaderVisible(this.currProject.isProjectTabHeaderVisible());
		
		this.setVisible(true);
		this.moveToFront();

		// --- Add this ProjectWindow to the JDesktopPane of the MainWindow ---
		if (Application.isMainWindowInitiated()==true && Application.getMainWindow() instanceof MainWindow) {
			((MainWindow)Application.getMainWindow()).getJDesktopPane4Projects().add(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbProjectWindow#addProjectTab(de.enflexit.awb.core.ui.AwbProjectWindowTab)
	 */
	@Override
	public void addProjectTab(AwbProjectWindowTab awbProjectWindowTab) {
		this.addProjectTab(awbProjectWindowTab, -1);
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbProjectWindow#addProjectTab(de.enflexit.awb.core.ui.AwbProjectWindowTab, int)
	 */
	@Override
	public void addProjectTab(AwbProjectWindowTab awbProjectWindowTab, int indexPosition) {
		
		if (awbProjectWindowTab instanceof ProjectWindowTab == false) {
			System.err.println("[" + this.getClass().getSimpleName() + "] The specified project window tab is not of the correct type.");
			return;
		}
		
		// --- Cast to Swing version of tab -----
		ProjectWindowTab pwt = (ProjectWindowTab) awbProjectWindowTab;
		
		int newIndexPos = indexPosition;

		if (newIndexPos < 0) {
			newIndexPos = this.getJTabbedPaneProjectWindowTabs().getTabCount(); // Default
			String parentName = pwt.getParentName();
			if (parentName != null) {
				AwbProjectWindowTab parentPWT = this.getProjectWindowTab(parentName);
				newIndexPos = parentPWT.getCompForChildComp().getTabCount();
			}
		}
		pwt.setIndexPosition(newIndexPos);

		// --- add to reminder vector -----------
		this.getProjectWindowTabVector().add(pwt);

		// --- use the private function ---------
		this.addProjectTabInternal(pwt);
	}
	
	/**
	 * Adds a Project-Tab and a new node (child of a specified parent) to the ProjectWindow.
	 *
	 * @param projectWindowTab the project window tab
	 * @return the default mutable tree node
	 */
	private DefaultMutableTreeNode addProjectTabInternal(AwbProjectWindowTab projectWindowTab) {

		// --- create Node ------------------------------------------
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(projectWindowTab);
		DefaultMutableTreeNode pareNode = null;
		JTabbedPane tabbedPaneParent = null;

		String parentName = projectWindowTab.getParentName();

		// --- Add to the TreeModel ---------------------------------
		if (parentName != null) {
			// --- Parent available? --------------------------------
			pareNode = this.getTreeNode(parentName);
			if (pareNode==null) return null;
			
			// --- Add to the parent --------------------------------
			AwbProjectWindowTab pareNodePWT = (AwbProjectWindowTab) pareNode.getUserObject();
			tabbedPaneParent = pareNodePWT.getCompForChildComp();
			// --- add ChangeListener -------------------------------
			this.addChangeListener(tabbedPaneParent);

		} else {
			pareNode = this.getRootNode();
			tabbedPaneParent = this.getJTabbedPaneProjectWindowTabs();
		}

		// --- Set empty border for visualization -------------------  
		
		if (projectWindowTab.getIndexPosition()!=-1 && projectWindowTab.getIndexPosition()<pareNode.getChildCount()) {
			// --- Add to parent node/tab at index position ---------
			pareNode.insert(newNode, projectWindowTab.getIndexPosition());
			tabbedPaneParent.insertTab(projectWindowTab.getTitle(), projectWindowTab.getIcon(), projectWindowTab.getJComponentForVisualization(), projectWindowTab.getTipText(), projectWindowTab.getIndexPosition());
		} else {
			// --- Just add to parent node/tab ----------------------
			pareNode.add(newNode);
			tabbedPaneParent.addTab(projectWindowTab.getTitle(), projectWindowTab.getIcon(), projectWindowTab.getJComponentForVisualization(), projectWindowTab.getTipText());
		}

		// --- refresh view -----------------------------------------
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
	public void removeProjectTab(AwbProjectWindowTab projectWindowTab) {

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
		this.getProjectWindowTabVector().remove(projectWindowTab);

		this.getTreeModel().reload();
		this.projectTreeExpand2Level(3, true);
	}

	/**
	 * This method removes all ProjectWindowTabs from the current display.
	 */
	private void removeAllProjectWindowTabsTemporary4Rebuilding() {

		Vector<ProjectWindowTab> pwtVector = new Vector<ProjectWindowTab>(this.getProjectWindowTabVector());
		for (int i = 0; i < pwtVector.size(); i++) {
			AwbProjectWindowTab pwt = pwtVector.get(i);
			if (pwt.getCompForChildComp()!=null) {
				pwt.getCompForChildComp().removeAll();
			}
		}
		this.getRootNode().removeAllChildren();
		this.getJTabbedPaneProjectWindowTabs().removeAll();
	}

	/**
	 * Returns the Tree-Node requested by the Reference.
	 *
	 * @param searchFor the search for
	 * @return the tree node
	 */
	public DefaultMutableTreeNode getTreeNode(String searchFor) {

		DefaultMutableTreeNode nodeFound = null;
		String currNodeText;

		for (Enumeration<?> en = this.getRootNode().breadthFirstEnumeration(); en.hasMoreElements();) {
			DefaultMutableTreeNode currNode = (DefaultMutableTreeNode) en.nextElement();
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
	 * @param pwt the new focus2 tab
	 */
	public void setFocus2Tab(AwbProjectWindowTab pwt) {
		String tabCaption = pwt.getTitle();
		this.setFocus2Tab(tabCaption);
	}
	/**
	 * Sets the focus to a specified Tab of the project Window.
	 * @param searchFor the new focus2 tab
	 */
	public void setFocus2Tab(String searchFor) {
		DefaultMutableTreeNode currNode = getTreeNode(searchFor);
		this.setFocus2Tab(currNode);
	}
	/**
	 * Sets the focus to a specified Tab of the {@link ProjectWindow}, where the node represents the corresponding node of
	 * the project tree.
	 * 
	 * @param node2Focus the new focus2 tab
	 */
	public void setFocus2Tab(DefaultMutableTreeNode node2Focus) {

		if (node2Focus == null) return;

		Vector<DefaultMutableTreeNode> nodeVector = new Vector<DefaultMutableTreeNode>();
		nodeVector.add(node2Focus);

		DefaultMutableTreeNode currNode = node2Focus;
		while (currNode.getParent() != null) {
			currNode = (DefaultMutableTreeNode) currNode.getParent();
			if (currNode != this.getRootNode()) {
				nodeVector.add(currNode);
			}
		}
		for (int i = nodeVector.size() - 1; i > -1; i--) {
			currNode = nodeVector.get(i);
			if (currNode.getUserObject() instanceof ProjectWindowTab) {
				AwbProjectWindowTab pwt = (AwbProjectWindowTab) currNode.getUserObject();
				if (pwt!=null && pwt.getJComponentForVisualization()!=null) {
					Component displayElement = pwt.getJComponentForVisualization();
					if (displayElement.getParent()!=null) {
						// --- Check parent container -------------------------
						Container parentContainer = displayElement.getParent(); 
						if (parentContainer instanceof ProjectDesktop) {
							// --- Set the focus to the project desktop -------
							ProjectDesktop desktop = (ProjectDesktop) parentContainer; 
							JTabbedPane desktopParent = (JTabbedPane)parentContainer.getParent(); 
							desktopParent.setSelectedComponent(desktop);
							// --- Move internal frame to front --------------- 
							JInternalFrame intFrame = (JInternalFrame) pwt.getJComponentForVisualization();
							intFrame.moveToFront();
							
						} else if (parentContainer instanceof JTabbedPane) {
							// --- Set the focus to the right tab -------------
							((JTabbedPane) parentContainer).setSelectedComponent(displayElement);
							
						} else {
							// --- An yet unknown parent container type 
							System.err.println("[" + this.getClass().getSimpleName() + "] Unknown parent container type " + parentContainer.getClass().getName());
						}
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
			viewToSet = AwbProjectWindowTab.DISPLAY_4_END_USER;
		} else {
			viewToSet = AwbProjectWindowTab.DISPLAY_4_DEVELOPER;
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

		Vector<ProjectWindowTab> pwtVector = new Vector<ProjectWindowTab>(this.getProjectWindowTabVector());
		for (int i = 0; i < pwtVector.size(); i++) {
			
			AwbProjectWindowTab pwt = pwtVector.get(i);
			int displayType = pwt.getDisplayType();

			// --- Which view to the project is needed ? ------------
			if (viewToSet == AwbProjectWindowTab.DISPLAY_4_DEVELOPER) {
				// --- show everything ------------------------------
				this.addProjectTabInternalCheckVisualization(pwt, envController);

			} else if (viewToSet == AwbProjectWindowTab.DISPLAY_4_END_USER) {
				// --- show only the end user displays --------------
				if (displayType < AwbProjectWindowTab.DISPLAY_4_DEVELOPER) {
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
	 * This method adds a ProjectWindowTab to the window and does the check for an {@link EnvironmentController} and it's visualization .
	 *
	 * @param pwt the ProjectWindowTab to add
	 * @param envControllerClass the EnvironmentController class
	 * @return the default mutable tree node
	 */
	private DefaultMutableTreeNode addProjectTabInternalCheckVisualization(AwbProjectWindowTab pwt, Class<? extends EnvironmentController> envControllerClass) {

		DefaultMutableTreeNode newNode = null;
		if (pwt.getDisplayType()==AwbProjectWindowTab.DISPLAY_4_END_USER_VISUALIZATION) {
			// --- Show Visualization-Tab only in case of ----------
			// --- an available defined Visualization-Panel ---------
			if (envControllerClass!=null) {
				newNode = this.addProjectTabInternal(pwt);
			}
		} else {
			newNode = this.addProjectTabInternal(pwt);
		}
		return newNode;
	}

	/**
	 * Returns the project window tab for sub panel hash.
	 * @return the project window tab for sub panel hash
	 */
	public HashMap<String, ProjectWindowTab> getProjectWindowTabForSubPanelHash() {
		if (tab4SubPanelHash==null) {
			tab4SubPanelHash = new HashMap<>(); 
		}
		return tab4SubPanelHash;
	}
	/*
	 * (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectEditorWindow#getTabForSubPanels(java.lang.String)
	 */
	@Override
	public AwbProjectWindowTab getTabForSubPanels(String name) {
		return this.getProjectWindowTabForSubPanelHash().get(name);
	}
	
	/**
	 * Can be used in order to register a tab which can hold sub panels, by using a JTabbedPane and thus allows a later access.
	 *
	 * @param name the name
	 * @param tab4SubPanes the tab4 sub panes
	 */
	public void registerTabForSubPanels(String name, ProjectWindowTab tab4SubPanes) {
		this.getProjectWindowTabForSubPanelHash().put(name, tab4SubPanes);
	}
	/**
	 * Can be used in order to unregister a tab which can hold sub panes, by using a JTabbedPane.
	 * @param name the name
	 */
	public void deregisterTabForSubPanels(String name) {
		this.getProjectWindowTabForSubPanelHash().remove(name);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {

		String updateReason = updateObject.toString();
		if (updateReason.equalsIgnoreCase(Project.CHANGED_ProjectName)) {
			this.getRootNode().setUserObject(currProject.getProjectName());
			this.getTreeModel().nodeChanged(this.getRootNode());
			this.getJTreeProject().repaint();
			Application.setTitleAddition(currProject.getProjectName());
			
		} else if (updateReason.equals(Project.CHANGED_ProjectView)) {
			this.setViewForDeveloperOrEndUser();

		} else if (updateReason.equals(Project.CHANGED_EnvironmentModelType)) {
			this.setViewForDeveloperOrEndUser();

		} else if (updateReason.equals(Project.VIEW_Maximize)) {
			this.tabMaximize();

		} else if (updateReason.equals(Project.VIEW_Restore)) {
			this.tabRestore();

		} else if (updateReason.equals(Project.VIEW_TabsLoaded)) {
			this.setFocus2StartTab();
			
		} else if (updateReason.equals(Project.PROJECT_DESKTOP_COMPONENT_ADDED)) {
			this.updateProjectDesktopChilds();
			
		} else if (updateReason.equals(Project.PROJECT_DESKTOP_COMPONENT_REMOVED)) {
			this.updateProjectDesktopChilds();
			
		}
		
	}

	/**
	 * Returns the vector of already known internal frames of the project desktop.
	 * @return the project desktop frame vector
	 */
	private Vector<JInternalFrame> getProjectDesktopFrameVector() {
		if (projectDesktopFrameVector==null) {
			projectDesktopFrameVector = new Vector<>();
		}
		return projectDesktopFrameVector;
	}
	/**
	 * Update project desktop child's.
	 */
	private void updateProjectDesktopChilds() {
		
		AwbProjectWindowTab desktopTab = this.getProjectWindowTab(Language.translate("Projekt-Desktop"));
		DefaultMutableTreeNode pdTreeNode = this.getTreeNode(desktopTab.toString());
		if (pdTreeNode!=null) {
			// --- Create temporary vector for removed internal frames -------- 
			Vector<JInternalFrame> tmpRemovedInternalFrames = new Vector<>(this.getProjectDesktopFrameVector());

			// --- Check all internal frames shown on the project desktop ----- 
			JDesktopPane desktopPane = (JDesktopPane) desktopTab.getJComponentForVisualization();
			JInternalFrame[] intFrameArray = desktopPane.getAllFrames();
			for (int i = 0; i < intFrameArray.length; i++) {
				JInternalFrame intFrame = intFrameArray[i];
				if (tmpRemovedInternalFrames.contains(intFrame)==false) {
					// --- => Found a new internal frame ----------------------
					this.addedToProjectDesktop(pdTreeNode, intFrame);
				} else {
					// --- => Found an already known internal frame -----------
					tmpRemovedInternalFrames.remove(intFrame);
				}
			} // end for
			
			// --- Remove the removed internal frames from local reminder vector
			for (int i = 0; i < tmpRemovedInternalFrames.size(); i++) {
				this.removedFromProjectDesktop(pdTreeNode, tmpRemovedInternalFrames.get(i));
			}
		}
	}
	/**
	 * Will be called if an internal frame was added to the project desktop.
	 *
	 * @param pdTreeNode the projects desktop tree node
	 * @param intFrameToAdd the internal frame to add
	 */
	private void addedToProjectDesktop(DefaultMutableTreeNode pdTreeNode, JInternalFrame intFrameToAdd) {
		
		this.getProjectDesktopFrameVector().add(intFrameToAdd);

		String reducedTitle = this.getReducedTitleForInternalFrame(intFrameToAdd);
		AwbProjectWindowTab pwtInternal = new ProjectWindowTab(this.currProject, AwbProjectWindowTab.DISPLAY_4_END_USER, reducedTitle, intFrameToAdd.getTitle(), null, intFrameToAdd, null);
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(pwtInternal);
		pdTreeNode.add(newNode);
		this.getTreeModel().reload();
		this.projectTreeExpand2Level(3, true);
	}
	/**
	 * Will be called if an internal frame was removed from the project desktop.
	 *
	 * @param pdTreeNode the projects desktop tree node
	 * @param intFrameToRemoved the removed internal frame to remove
	 */
	private void removedFromProjectDesktop(DefaultMutableTreeNode pdTreeNode , JInternalFrame intFrameToRemoved) {
		
		this.getProjectDesktopFrameVector().remove(intFrameToRemoved);
		
		for (int i = 0; i < pdTreeNode.getChildCount(); i++) {
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) pdTreeNode.getChildAt(i);
			AwbProjectWindowTab pwt = (AwbProjectWindowTab) childNode.getUserObject();
			if (pwt.getJComponentForVisualization()==intFrameToRemoved) {
				pdTreeNode.remove(i);
				this.getTreeModel().reload();
				this.projectTreeExpand2Level(3, true);
				break;
			}
		}
	}
	/**
	 * Return a reduced title for an internal frame.
	 *
	 * @param intFrame the internal frame
	 * @return the reduced title for internal frame
	 */
	private String getReducedTitleForInternalFrame(JInternalFrame intFrame) {
		
		String title = intFrame.getTitle(); 
		if (title==null || title.isEmpty()==true) {
			// --- If not tile was specified for the internal frame -----------
			title = "Internal Frame [No Title]";
			
		} else if (title.length()>25) {
			// --- Reduce the title here --------------------------------------
			String reducedTitleBase = title.replaceAll("[^A-Za-z0-9 ]", "");
			reducedTitleBase = reducedTitleBase.replace("  ", " ");
			
			String reducedTitle = "";
			String[] titleFragArray = reducedTitleBase.trim().split(" ");
			for (int i = 0; i < titleFragArray.length; i++) {
				String titleFrag = titleFragArray[i].trim();
				if (titleFrag.matches("[0-9]+")==true) {
					// --- Numeric string -------------------------------------
					reducedTitle += " " + titleFrag + " ";
				} else {
					// --- Non-numeric string ---------------------------------
					// --- Split by upper case letters ------------------------
					String[] tfucArray = titleFrag.split("(?=\\p{Lu})");
					if (tfucArray.length>0) {
						String tfucResult = "";
						for (String tfuc : tfucArray) {
							if (tfuc.length()>3) {
								tfuc = tfuc.substring(0,3);
							}
							tfucResult += tfuc;
						}
						reducedTitle += tfucResult + ".";
					} else {
						if (titleFrag.length()>3) {
							titleFrag = titleFrag.substring(0, 3);
						}
						reducedTitle += titleFrag + ".";
					}
					
				}
			} // end for
			
			if (reducedTitle.isEmpty()==false) {
				title = reducedTitle.trim();
			}
		}
		return title;
	}

	/*
	 * (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectEditorWindow#showErrorMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public void showErrorMessage(String msgText, String msgHead) {
		JOptionPane.showMessageDialog(this, msgText, msgHead, JOptionPane.ERROR_MESSAGE);
	}


	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbProjectEditorWindow#getRuntimeVisualizationContainer()
	 */
	@Override
	public JPanel4Visualization getRuntimeVisualizationContainer() {
		if (this.visualizationTab4SetupExecution == null) {
			this.visualizationTab4SetupExecution = new JPanel4Visualization(this.currProject, Language.translate(AwbProjectWindowTab.TAB_4_RUNTIME_VISUALIZATION));
		}
		return this.visualizationTab4SetupExecution;
	}
	
	
}
