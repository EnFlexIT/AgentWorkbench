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
package agentgui.core.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.DesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
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

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.gui.projectwindow.MaximizedTab;
import agentgui.core.gui.projectwindow.ProjectWindowTab;
import agentgui.core.project.Project;

/**
 * This extended JInternalFrame graphically encapsulates the the project in the
 * main window (class CoreWindow)
 *  
 * @see CoreWindow
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectWindow extends JInternalFrame implements Observer {
	
	private static final long serialVersionUID = -1462483441246136949L;

	private Project currProject;
	
	private JSplitPane jSplitPaneProjectView = null;
	private JScrollPane jScrollPane = null;
	
	private JTree projectTree = null;
	private DefaultTreeModel projectTreeModel;
	private DefaultMutableTreeNode rootNode;
	
	private JTabbedPane projectViewRightTabs = null;
	private ChangeListener tabSelectionListener = null;  //  @jve:decl-index=0:
	private MouseListener tabMouseListener = null;  //  @jve:decl-index=0:
	
	private boolean pauseTreeSelectionListener = false;
	private boolean pauseTabSelectionListener = false;
	
	private HashMap<String, ProjectWindowTab> tab4SubPanel = new HashMap<String, ProjectWindowTab>();  //  @jve:decl-index=0:
	private Vector<ProjectWindowTab> tabVector = new Vector<ProjectWindowTab>();  //  @jve:decl-index=0:
	
	private boolean isMaximizedTab = false;
	private MaximizedTab maxTab = null;
	private ProjectWindowTab maxProjectWindowTab = null;
	
	/**
	 * This is the default constructor.
	 *
	 * @param project the current Project instance
	 */
	public ProjectWindow(Project project) {
		
		super();
		this.currProject = project;		
		this.currProject.addObserver(this);		
		
		// --- TreeModel initialisieren --------------------------
		this.rootNode = new DefaultMutableTreeNode(currProject.getProjectName());
		this.projectTreeModel = new DefaultTreeModel(rootNode);	
		
		// --- Projektfenster zusammenbauen ----------------------
		this.initialize();		

	}

	/**
	 * This method initializes this.
	 * @return void
	 */
	private void initialize() {
		
		this.setSize(850, 500);
		this.setContentPane( getProjectViewSplit() );
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setResizable(true);
		this.setAutoscrolls(true);		
		this.setBorder(null);
		this.setFocusable(true);
		((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
		
		this.setVisible(true);
		this.moveToFront();		
		Application.getMainWindow().getJDesktopPane4Projects().add(this);		
	}

	/* (non-Javadoc)
	 * @see javax.swing.JInternalFrame#dispose()
	 */
	@Override
	public void dispose() {
		if (this.maxTab!=null) {
			this.tabRestore();
		}
		super.dispose();
	}
	/**
	 * This method initializes ProjectViewSplit.
	 *
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getProjectViewSplit() {
		if (jSplitPaneProjectView == null) {
			jSplitPaneProjectView = new JSplitPane();
			jSplitPaneProjectView.setOneTouchExpandable(true);
			jSplitPaneProjectView.setDividerLocation(210);
			jSplitPaneProjectView.setDividerSize(10);			
			jSplitPaneProjectView.setLeftComponent(getJScrollPane());			
			jSplitPaneProjectView.setRightComponent( getProjectViewRightTabs() );
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
			jScrollPane.setViewportView(getProjectTree());
		}
		return jScrollPane;
	}
	
	/**
	 * This method initializes ProjectTree.
	 *
	 * @return javax.swing.JTree
	 */
	private JTree getProjectTree() {
		if (projectTree == null) {
			projectTree = new JTree( projectTreeModel );
			projectTree.setName("ProjectTree");
			projectTree.setShowsRootHandles(false);
			projectTree.setRootVisible(true);
			projectTree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
			// ------------------------------------------------------------------
			// ------------------------------------------------------------------
			projectTree.addTreeSelectionListener( new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent ts) {
					
					if (pauseTreeSelectionListener) return;
					
					// ----------------------------------------------------------
					// --- Tree-Selection abfangen --- S T A R T ----------------
					// ----------------------------------------------------------
					TreePath pathSelected = ts.getPath();
					if ( pathSelected.getPathCount() >= 2 ) {
						// ------------------------------------------------------
						// --- Fokus auf die entsprechende Karteikarte setzen ---
						// ------------------------------------------------------
						pauseTabSelectionListener=true;
						DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) projectTree.getLastSelectedPathComponent();
						setFocus2Tab(selectedNode);
						pauseTabSelectionListener=false;
					} 
					// ----------------------------------------------------------
					// --- Tree-Selection abfangen --- S T O P ------------------
					// ----------------------------------------------------------
				}// End - valueChanged
			});
			// ------------------------------------------------------------------
			// ------------------------------------------------------------------
			projectTree.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {
					if (me.getClickCount()==2 & SwingUtilities.isLeftMouseButton(me)) {
						// --- Catch double click on node ---
						TreePath tp = projectTree.getPathForLocation(me.getX(), me.getY());
						if (tp != null) {
							currProject.setNotChangedButNotify(Project.VIEW_Maximize);
						}
					}
					
				}//End - mouseClicked
			});
			// ------------------------------------------------------------------
		}
		return projectTree;
	}
	
	// If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
    /**
	 * Project tree expand2 level.
	 *
	 * @param Up2TreeLevel the up2 tree level
	 * @param expand the expand
	 */
	public void projectTreeExpand2Level(Integer Up2TreeLevel, boolean expand ) {
    	
    	Integer CurrNodeLevel = 1;
    	if ( Up2TreeLevel == null ) 
    		Up2TreeLevel = 1000;

    	projectTreeExpand(new TreePath(rootNode), expand, CurrNodeLevel, Up2TreeLevel);
    }
	
	/**
	 * Project tree expand.
	 *
	 * @param parent the parent
	 * @param expand the expand
	 * @param CurrNodeLevel the curr node level
	 * @param Up2TreeLevel the up2 tree level
	 */
	private void projectTreeExpand( TreePath parent, boolean expand, Integer CurrNodeLevel, Integer Up2TreeLevel) {
    
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (CurrNodeLevel >= Up2TreeLevel) {
        	return;
        }
        if (node.getChildCount() >= 0) {
            for ( @SuppressWarnings("rawtypes") Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                projectTreeExpand(path, expand, CurrNodeLevel+1, Up2TreeLevel);
            }
        }    
        // Expansion or collapse must be done bottom-up
        if (expand) {
            projectTree.expandPath(parent);
        } else {
        	projectTree.collapsePath(parent);
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
		
		if (tabMouseListener==null) {
			
			this.tabMouseListener = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {
					
					if (me.getClickCount()==2 & SwingUtilities.isLeftMouseButton(me)) {
						if (isMaximizedTab) {
							currProject.setNotChangedButNotify(Project.VIEW_Restore);
						} else {
							currProject.setNotChangedButNotify(Project.VIEW_Maximize);
						}
					}
					if (me.getClickCount()==1 & SwingUtilities.isRightMouseButton(me)) {
						getTabPopupMenu().show(me.getComponent(), me.getX(), me.getY());
					}
				}
			};
		}
		return tabMouseListener;
	}
	
	/**
	 * Returns the JPopupMenu for the tabs.
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
	 * Returns the MaximizedTab (extends JInteralFrame), 
	 * with the currently selected tab.
	 * @return the MaximizedTab (extends JInteralFrame)
	 */
	private MaximizedTab getMaximizedTab() {
		if (maxTab==null) {
			// --- Get and remove the currently selcted tab ---------
			this.maxProjectWindowTab = this.getProjectWindowTabSelected();
			this.remove(this.maxProjectWindowTab);
			// --- Create the extra JInternalFrame ------------------
			this.maxTab = new MaximizedTab(this, this.maxProjectWindowTab.getTitle());
			this.maxTab.add(this.maxProjectWindowTab.getComponent());
		}
		return maxTab;
	}
	
	/**
	 * Tab maximize.
	 */
	private void tabMaximize() {

		// --- Maximize the main window ---------------------------------------
		Application.getMainWindow().setExtendedState(JFrame.MAXIMIZED_BOTH);

		// --- Open a new JInteraFrame with the current tab enlarged ---------- 
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JDesktopPane appDesktop = Application.getMainWindow().getJDesktopPane4Projects();
				if (maxTab==null) {
					appDesktop.add(getMaximizedTab());	
					Application.getMainWindow().addJToolbarComponent(getMaximizedTab().getJButtonRestore4MainToolBar());
				}
				
				DesktopManager dtm = Application.getMainWindow().getJDesktopPane4Projects().getDesktopManager();
				if (dtm!=null) {
					dtm.activateFrame(getMaximizedTab());
					dtm.maximizeFrame(getMaximizedTab());
				}
			}
		});
		
	}
	
	/**
	 * Tab restore.
	 */
	public void tabRestore(){

		if (this.maxTab!=null) {

			// --- Remove toolbar button --------------------------------------
			Application.getMainWindow().removeJToolbarComponent(getMaximizedTab().getJButtonRestore4MainToolBar());
			this.maxTab.setVisible(false);

			// --- Place the enlarged tab back to the other one ---------------
			if (this.maxProjectWindowTab!=null) {
				this.addProjectTab(this.maxProjectWindowTab, this.maxProjectWindowTab.getIndexPosition());
				this.maxProjectWindowTab.getComponent().validate();
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
		
		if (tabSelectionListener==null) {
			tabSelectionListener = new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent evt) {
					
					if (pauseTabSelectionListener) return;
					
					ProjectWindowTab pwt = getProjectWindowTabSelected();
			        if (pwt!=null) {
				        DefaultMutableTreeNode selectedNode = getTreeNode(pwt.getTitle());
				        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) projectTree.getLastSelectedPathComponent();
				        if (selectedNode!=currentNode) {
				        	pauseTreeSelectionListener=true;
				        	projectTree.setSelectionPath(new TreePath(selectedNode.getPath()));
				        	pauseTreeSelectionListener=false;
				        }
			        }
				}
			};
		}
		return tabSelectionListener;
	}
	
	/**
	 * Gets the currently selected tab as instance of ProjectWindowTab.
	 * @return the tab selected
	 */
	private ProjectWindowTab getProjectWindowTabSelected() {
		
		ProjectWindowTab tabSelected = null;
		
        int selIndex = this.projectViewRightTabs.getSelectedIndex();
        if (selIndex>-1) {
        	String tabTitle = this.projectViewRightTabs.getTitleAt(selIndex);
        	tabSelected = getProjectWindowTab(tabTitle);
        	if (tabSelected!=null) {
        		JTabbedPane subPane = tabSelected.getCompForChildComp();
        		if (subPane!=null) {
        			int selSubIndex = subPane.getSelectedIndex();
        			if (selSubIndex!=-1) {
        				tabTitle = subPane.getTitleAt(selSubIndex);
            			tabSelected = getProjectWindowTab(tabTitle);	
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
	
	/**
	 * Get the notification of the ObjectModel.
	 *
	 * @param arg0 the arg0
	 * @param OName the o name
	 */
	public void update(Observable arg0, Object OName) {
		
		String ObjectName = OName.toString();
		if ( ObjectName.equalsIgnoreCase( Project.CHANGED_ProjectName ) ) {
			rootNode.setUserObject( currProject.getProjectName() );
			projectTreeModel.nodeChanged(rootNode);
			projectTree.repaint();
			Application.setTitelAddition( currProject.getProjectName() );
			
		} else if (ObjectName.equals(Project.CHANGED_ProjectView)) {
			this.setView();			
			
		} else if (ObjectName.equals(Project.CHANGED_EnvironmentModel)) {
			this.setView();			
			
		} else if (ObjectName.equals(Project.VIEW_Maximize)) {
			this.tabMaximize();
			
		} else if (ObjectName.equals(Project.VIEW_Restore)) {
			this.tabRestore();
			
		}
		this.validate();
		this.repaint();
	}

	/**
	 * Adds a Project-Tab and a new node (child of a specified parent) to the ProjectWindow.
	 *
	 * @param projectWindowTab the project window tab
	 */
	public void addProjectTab(ProjectWindowTab projectWindowTab) {
		this.addProjectTab(projectWindowTab, -1);
	}
	
	/**
	 * Adds a Project-Tab and a new node (child of a specified parent) to the ProjectWindow
	 * at the specified index position. The index position has to be greater than 1, in order
	 * to keep the 'Info'-Tab and the 'Configuration'-Tab at its provided position!
	 *
	 * @param projectWindowTab the project window tab
	 * @param indexPositionGreaterOne the index position greater one
	 */
	public void addProjectTab(ProjectWindowTab projectWindowTab, int indexPositionGreaterOne) {

		int newIndexPos = indexPositionGreaterOne;
		boolean add2RootNode = false;
		
		String parentName = projectWindowTab.getParentName();
		if (parentName==null) {
			add2RootNode = true;
		} else  {
			DefaultMutableTreeNode pareNode = getTreeNode(parentName);
			if (pareNode==this.rootNode) {
				add2RootNode = true;
			}
		} 

		if (add2RootNode==true && (newIndexPos==0 || newIndexPos==1)) {
			newIndexPos = 2;
		} else if (newIndexPos==-1) {
			newIndexPos = projectViewRightTabs.getTabCount(); // Default
			if (parentName!=null) {
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
	 * Adds a Project-Tab and a new node (child of a specified parent) to
	 * the ProjectWindow.
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
		if (parentName!=null) {
			pareNode = getTreeNode(parentName);
			ProjectWindowTab pareNodePWT = (ProjectWindowTab) pareNode.getUserObject();
			tabbedPaneParent = pareNodePWT.getCompForChildComp();
			// --- add ChangeListener -----------
			this.addChangeListener(tabbedPaneParent);

		} else {
			pareNode = rootNode;
			tabbedPaneParent = projectViewRightTabs;
		}

		if (projectWindowTab.getIndexPosition() != -1 && projectWindowTab.getIndexPosition()<pareNode.getChildCount()) {
			// --- add to parent node/tab at index position ----
			pareNode.insert(newNode, projectWindowTab.getIndexPosition());
			tabbedPaneParent.insertTab(projectWindowTab.getTitle(), projectWindowTab.getIcon(), projectWindowTab.getComponent(), projectWindowTab.getTipText(), projectWindowTab.getIndexPosition());
		} else {
			// --- just add to parent node/tab -----------------
			pareNode.add(newNode);
			tabbedPaneParent.addTab( projectWindowTab.getTitle(), projectWindowTab.getIcon(), projectWindowTab.getComponent(), projectWindowTab.getTipText());
		}
		
		// --- refresh view ---------------------
		this.projectTreeModel.reload();
		this.projectTreeExpand2Level(3, true);
		return newNode;
	}
	
	/**
	 * Adds the local ChangeListener named 'tabSelectionListener'
	 * to a JTabbedPane if not already there.
	 *
	 * @param pane the pane
	 */
	private void addChangeListener(JTabbedPane pane) {
		
		boolean listenerFound = false;
		ChangeListener[] listener = pane.getChangeListeners();
		for (int i = 0; i < listener.length; i++) {
			ChangeListener cl = listener[i];
			if (cl==this.getTabSelectionListener()) {
				listenerFound = true;
			}
		} 
		
		if (listenerFound==false) {
			pane.addChangeListener(this.getTabSelectionListener());
		}
	}
	
	/**
	 * This removes a given ProjectWindowTab from this ProjectWindow.
	 *
	 * @param projectWindowTab the project window tab
	 */
	public void remove(ProjectWindowTab projectWindowTab) {
	
		DefaultMutableTreeNode node = this.getTreeNode(projectWindowTab.getTitle());
		if (node!=null) {
			DefaultMutableTreeNode pareNode = (DefaultMutableTreeNode) node.getParent();
			pareNode.remove(node);
		}

		JComponent component = projectWindowTab.getComponent();
		Container container = component.getParent();
		if (container!=null) {
			container.remove(component);	
		}
		this.tabVector.remove(projectWindowTab);

		this.projectTreeModel.reload();
		this.projectTreeExpand2Level(3, true);
	}
	
	/**
	 * This method removes all ProjectWindowTabs from the current display.
	 */
	private void removeAllProjectWindowTabsTemporary4Rebuilding() {
		
		Vector<ProjectWindowTab> pwtVector = new Vector<ProjectWindowTab>(this.tabVector); 
		for (Iterator<ProjectWindowTab> it = pwtVector.iterator(); it.hasNext();) {
			
			ProjectWindowTab pwt = (ProjectWindowTab) it.next();
			if (pwt.getCompForChildComp()!=null) {
				pwt.getCompForChildComp().removeAll();
			}
		}
		this.rootNode.removeAllChildren();
		this.projectViewRightTabs.removeAll();
	}
	
	/**
	 * Returns the Tree-Node requested by the Reference.
	 *
	 * @param searchFor the search for
	 * @return the tree node
	 */
	@SuppressWarnings("unchecked")
	private DefaultMutableTreeNode getTreeNode(String searchFor) {
		
		DefaultMutableTreeNode nodeFound = null;
		DefaultMutableTreeNode currNode = null;
		String currNodeText;
		
		for (Enumeration<DefaultMutableTreeNode> e = rootNode.breadthFirstEnumeration(); e.hasMoreElements();) {
			currNode = e.nextElement();
			currNodeText = currNode.getUserObject().toString(); 
			if ( currNodeText.equals(searchFor) ) {				
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
	 * Sets the focus to a specified Tab of the project Window.
	 *
	 * @param node2Focus the new focus2 tab
	 */
	public void setFocus2Tab(DefaultMutableTreeNode node2Focus) {

		if (node2Focus== null) return;
		
		Vector<DefaultMutableTreeNode> nodeArray = new Vector<DefaultMutableTreeNode>();
		nodeArray.add(node2Focus);
		
		DefaultMutableTreeNode currNode = node2Focus;
		while (currNode.getParent()!= null) {
			currNode = (DefaultMutableTreeNode) currNode.getParent();
			if (currNode!=rootNode) {
				nodeArray.add(currNode);	
			}
		}
		for (int i = nodeArray.size()-1; i > -1; i--) {
			currNode = nodeArray.get(i);
			ProjectWindowTab pwt = (ProjectWindowTab) currNode.getUserObject();
			Component displayElement = pwt.getComponent();
			((JTabbedPane) displayElement.getParent()).setSelectedComponent(displayElement);
		}
	}
	
	/**
	 * Rebuilds the ProjectWindow depending on the selected view.
	 * View can be, up to now, the developer view or the end user view
	 *
	 */
	public void setView() {
		
		// --- Which view should be displayed ? ---------------------
		int viewToSet = 0;
		if (this.currProject.getProjectView().equals(Project.VIEW_User)) {
			viewToSet = ProjectWindowTab.DISPLAY_4_END_USER;
		} else {
			viewToSet = ProjectWindowTab.DISPLAY_4_DEVELOPER;
		}
		
		// --- Exists a Panle for the current environment model? ---- 
		Class<? extends EnvironmentController> envController = this.currProject.getEnvironmentModelType().getEnvironmentControllerClass();
		
		// --- Remind the current selection -------------------------
		DefaultMutableTreeNode lastSelectedNode = null;
		String lastSelectedTabTitle = this.projectViewRightTabs.getTitleAt(this.projectViewRightTabs.getSelectedIndex());
		
		// --- Pause the TreeSelectionListener ----------------------
		this.pauseTreeSelectionListener = true;
		
		// --- Rebuild the display out of the tabVector -------------
		this.projectTree.setSelectionPath(null);
		this.removeAllProjectWindowTabsTemporary4Rebuilding();
		
		Vector<ProjectWindowTab> pwtVector = new Vector<ProjectWindowTab>(this.tabVector); 
		for (Iterator<ProjectWindowTab> it = pwtVector.iterator(); it.hasNext();) {
			
			ProjectWindowTab pwt = (ProjectWindowTab) it.next();
			int displayType = pwt.getDisplayType();
			String displayTitle = pwt.getTitle();
			
			DefaultMutableTreeNode currCreatedNode = null;
			
			// --- Which view to the project is needed ? ------------ 
			if (viewToSet == ProjectWindowTab.DISPLAY_4_DEVELOPER) {
				// --- show everything ------------------------------
				currCreatedNode = this.setViewFilterVisualization(pwt, envController);
				
			} else if (viewToSet == ProjectWindowTab.DISPLAY_4_END_USER) {
				// --- show only the end user displays --------------
				if (displayType < ProjectWindowTab.DISPLAY_4_DEVELOPER) {
					currCreatedNode = this.setViewFilterVisualization(pwt, envController);							
				}
			}
			// --- Remind this as last selected node ----------------
			if (currCreatedNode!=null && displayTitle.equals(lastSelectedTabTitle)) {
				lastSelectedNode = currCreatedNode;
			}
		}
		
		// --- update view of the tree ------------------------------
		this.projectTreeModel.reload();
		this.projectTree.revalidate();
		this.projectTree.repaint();
		this.projectTreeExpand2Level(3, true);
		
		// --- update view of the tabs ------------------------------
		this.revalidate();
		this.updateUI();		
		this.repaint();		
		this.currProject.setMaximized();
		
		// --- Set the focus to the last selected Tab ---------------
		if (lastSelectedNode!=null) {
			this.setFocus2Tab(lastSelectedNode);	
		}
		
		// --- Reactivate the TreeSelectionListener -----------------
		this.pauseTreeSelectionListener = false;
		
	}
	
	/**
	 * This method adds a ProjectWindowTab to the window, considering if a
	 * projects has a predefined visualization or not.
	 *
	 * @param pwt the pwt
	 * @param displayPanel the display panel
	 * @return the default mutable tree node
	 */
	private DefaultMutableTreeNode setViewFilterVisualization(ProjectWindowTab pwt, Class<? extends EnvironmentController> envController) {
		
		DefaultMutableTreeNode newNode = null;
		if (pwt.getDisplayType() == ProjectWindowTab.DISPLAY_4_END_USER_VISUALIZATION) {
			// --- Show Visualization-Tab only in case of  ----------
			// --- an available defined Visualization-Panel ---------
			if (envController!=null) {
				newNode = this.addProjectTabInternal(pwt);
			}
		} else {
			newNode = this.addProjectTabInternal(pwt);	
		}
		return newNode;
	}

	/**
	 * Can be used in order to register a tab which can hold sub panes, by
	 * using a JTabbedPane. So it allows the later access.
	 *
	 * @param name the name
	 * @param tab4SubPanes the tab4 sub panes
	 */
	public void registerTabForSubPanels(String name, ProjectWindowTab tab4SubPanes) {
		this.tab4SubPanel.put(name, tab4SubPanes);
	}
	
	/**
	 * Can be used in order to access a special tab, which can hold sub tabs.
	 *
	 * @param name the name
	 * @return the tab for sub panels
	 */
	public ProjectWindowTab getTabForSubPanels(String name) {
		return this.tab4SubPanel.get(name);
	}
	
	/**
	 * Can be used in order to unregister a tab which can hold sub panes, by
	 * using a JTabbedPane.
	 *
	 * @param name the name
	 */
	public void deregisterTabForSubPanels(String name) {
		this.tab4SubPanel.remove(name);
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
