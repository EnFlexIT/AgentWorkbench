package agentgui.core.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
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
import agentgui.core.application.Project;
import agentgui.core.environment.EnvironmentPanel;

/**
 * @author: Christian Derksen
 */
public class ProjectWindow extends JInternalFrame implements Observer {

	private static final long serialVersionUID = 1L;
	
	private Project currProject;
	
	private JSplitPane ProjectViewSplit = null;
	private JScrollPane jScrollPane = null;
	
	private JTree projectTree = null;
	private DefaultTreeModel projectTreeModel;
	private DefaultMutableTreeNode rootNode;
	
	private JTabbedPane projectViewRightTabs = null;
	private ChangeListener tabSelectionListener = null;  //  @jve:decl-index=0:
	private MouseListener tabMouseListener = null;  //  @jve:decl-index=0:
	
	private boolean pauseTreeSelectionListener = false;
	private boolean pauseTabSelectionListener = false;
	
	private ProjectWindowTab projectWindowTab4Configuration = null;
	private ProjectWindowTab projectWindowTab4SimulationSetup = null;
	private Vector<ProjectWindowTab> tabVector = new Vector<ProjectWindowTab>();  //  @jve:decl-index=0:
	private int oldNumberOfNodes = 0;
	
	
	/**
	 * This is the default constructor
	 */
	public ProjectWindow(Project cp) {
		
		super();
		this.currProject = cp;		
		this.currProject.addObserver(this);		
		
		// --- TreeModel initialisieren --------------------------
		rootNode = new DefaultMutableTreeNode( currProject.getProjectName() );
		projectTreeModel = new DefaultTreeModel( rootNode );	
		
		// --- Instanciate Listerner for Tab-Changes -------------
		this.setTabMouseListener();
		this.setTabSelectionListener();
		// --- Projektfenster zusammenbauen ----------------------
		this.initialize();		

	}

	/**
	 * This method initializes this
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
		Application.MainWindow.getJDesktopPane4Projects().add(this);		
	}

	/**
	 * This method initializes ProjectViewSplit	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getProjectViewSplit() {
		if (ProjectViewSplit == null) {
			ProjectViewSplit = new JSplitPane();
			ProjectViewSplit.setOneTouchExpandable(true);
			ProjectViewSplit.setDividerLocation(200);
			ProjectViewSplit.setDividerSize(10);			
			ProjectViewSplit.setLeftComponent(getJScrollPane());			
			ProjectViewSplit.setRightComponent( getProjectViewRightTabs() );
		}
		return ProjectViewSplit;
	}
	
	/**
	 * This method initializes jScrollPane	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getProjectTree());
		}
		return jScrollPane;
	}
	/**
	 * This method initializes ProjectTree	
	 * @return javax.swing.JTree	
	 */
	private JTree getProjectTree() {
		if (projectTree == null) {
			projectTree = new JTree( projectTreeModel );
			projectTree.setName("ProjectTree");
			projectTree.setShowsRootHandles(false);
			projectTree.setRootVisible(true);
			projectTree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
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

		}
		return projectTree;
	}
	
	// If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
    public void projectTreeExpand2Level(Integer Up2TreeLevel, boolean expand ) {
    	
    	Integer CurrNodeLevel = 1;
    	if ( Up2TreeLevel == null ) 
    		Up2TreeLevel = 1000;

    	ProjectTreeExpand( new TreePath(rootNode), expand, CurrNodeLevel, Up2TreeLevel);
    }
    @SuppressWarnings("unchecked")
	private void ProjectTreeExpand( TreePath parent, boolean expand, Integer CurrNodeLevel, Integer Up2TreeLevel) {
    
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (CurrNodeLevel >= Up2TreeLevel) {
        	return;
        }
        if (node.getChildCount() >= 0) {
            for ( Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                ProjectTreeExpand(path, expand, CurrNodeLevel+1, Up2TreeLevel);
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
	 * This method initializes ProjectViewRight	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getProjectViewRightTabs() {
		if (projectViewRightTabs == null) {
			projectViewRightTabs = new JTabbedPane();
			projectViewRightTabs.setName("ProjectTabs");
			projectViewRightTabs.setTabPlacement(JTabbedPane.TOP);
			projectViewRightTabs.setPreferredSize(new Dimension(126, 72));
			projectViewRightTabs.setFont(new Font("Dialog", Font.BOLD, 12));
			projectViewRightTabs.addMouseListener(this.tabMouseListener);
			projectViewRightTabs.addChangeListener(this.tabSelectionListener);
		}
		return projectViewRightTabs;
	}	
	
	/**
	 * This method instanciates the MouseListener for the Tab-Selections
	 */
	private void setTabMouseListener() {
		
		tabMouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				
				if (me.getClickCount()==2 & SwingUtilities.isLeftMouseButton(me)) {
					System.out.println("double click on Tab ...");
				}
				if (me.getClickCount()==1 & SwingUtilities.isRightMouseButton(me)) {
					System.out.println("right click on Tab ...");
				}
			}
		};
	}
	
	/**
	 * This method instantiates the ChangeListener for Tab-Selections
	 */
	private void setTabSelectionListener() {
		
		tabSelectionListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				
				if (pauseTabSelectionListener) return;
				
				// --- To prevent, that an add-action came in ----
				int newNumberOfNodes = getNumberOfNodes();
				if (newNumberOfNodes==oldNumberOfNodes) {
					
					JTabbedPane pane = (JTabbedPane)evt.getSource();
			        int selIndex = pane.getSelectedIndex();
			        if (selIndex>-1) {
				        String title = pane.getTitleAt(selIndex);
				        DefaultMutableTreeNode selectedNode = getTreeNode(title);

				        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) projectTree.getLastSelectedPathComponent();
				        if (selectedNode!=currentNode) {
				        	pauseTreeSelectionListener=true;
				        	projectTree.setSelectionPath(new TreePath(selectedNode.getPath()));
				        	pauseTreeSelectionListener=false;
				        }
			        }
			        
				} else {
					oldNumberOfNodes = newNumberOfNodes; 
				}
			}
		};
	}
	
	/**
	 * Get the notification of the ObjectModel
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
			
		} else {
			//System.out.println("Unbekannter Updatebefehl vom Observerable ...");
		};
		this.repaint();
	}

	/**
	 * Adds a Project-Tab and a new node (child of a specified parent) to the ProjectWindow
	 * @param projectWindowTab
	 */
	public void addProjectTab(ProjectWindowTab projectWindowTab) {
		this.addProjectTab(projectWindowTab, -1);
	}
	/**
	 * Adds a Project-Tab and a new node (child of a specified parent) to the ProjectWindow
	 * at the specified index position. The index position has to be greater than 1, in order 
	 * to keep the 'Info'-Tab and the 'Configuration'-Tab at its provided position! 

	 * @param projectWindowTab
	 * @param indexPosotionGreaterOne
	 */
	public void addProjectTab(ProjectWindowTab projectWindowTab, int indexPositionGreaterOne) {

		int newIndexPos = indexPositionGreaterOne;
		if (newIndexPos > -1 && newIndexPos < 2) {
			// ----------------------------------------------------------------
			// --- if we adding at the root node, we have to make sure that --- 
			// --- we don't use index position 0 or 1 ! 					---
			// ----------------------------------------------------------------
			String parentName = projectWindowTab.getParentName();
			if (parentName!=null) {
				DefaultMutableTreeNode pareNode = getTreeNode(parentName);
				if (pareNode==rootNode) {
					newIndexPos = 2;
				}
			} else {
				newIndexPos = 2;	
			}
			// ----------------------------------------------------------------
		}
		projectWindowTab.setIndexPosition(newIndexPos);
		
		// --- add to reminder vector -----------
		this.tabVector.add(projectWindowTab);
		
		// --- use the private function ---------
		this.addProjectTabInternal(projectWindowTab);

	}
	
	/**
	 * Adds a Project-Tab and a new node (child of a specified parent) to 
	 * the ProjectWindow
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
	 * to a JTabbedPane if not already there
	 */
	private void addChangeListener(JTabbedPane pane) {
		
		boolean listenerFound = false;
		ChangeListener[] listener = pane.getChangeListeners();
		for (int i = 0; i < listener.length; i++) {
			ChangeListener cl = listener[i];
			if (cl==tabSelectionListener) {
				listenerFound = true;
			}
		} 
		
		if (listenerFound==false) {
			pane.addChangeListener(tabSelectionListener);
		}
	}
	/**
	 * This removes a given ProjectWindowTab from this ProjectWindow
	 * @param projectWindowTab
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
	 * This method removes all ProjectWindowTabs from the current display
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
	 * Returns the Tree-Node requested by the Reference 
	 * @param Reference
	 * @return
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
	 * @return the number of nodes in the tree
	 */
	@SuppressWarnings("unchecked")
	private int getNumberOfNodes() {
		
		int counter = 0;
		for (Enumeration<DefaultMutableTreeNode> e = rootNode.breadthFirstEnumeration(); e.hasMoreElements();) {
			counter++;
			e.nextElement();
		}
		return counter;
	}
	
	/**
	 * Sets the focus to a specified Tab of the project Window
	 */
	public void setFocus2Tab(String searchFor) {
		DefaultMutableTreeNode currNode = getTreeNode(searchFor);
		this.setFocus2Tab(currNode);
	}
	/**
	 * Sets the focus to a specified Tab of the project Window
	 */
	public void setFocus2Tab(ProjectWindowTab pwt) {
		String tabCaption = pwt.getTitle();
		setFocus2Tab(tabCaption);
	}
	/**
	 * Sets the focus to a specified Tab of the project Window
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
	 * @param viewToSet
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
		Class<? extends EnvironmentPanel> displayPanel = this.currProject.getEnvironmentModelType().getDisplayPanel();
		
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
				currCreatedNode = this.setViewFilterVisualization(pwt, displayPanel);
				
			} else if (viewToSet == ProjectWindowTab.DISPLAY_4_END_USER) {
				// --- show only the end user displays --------------
				if (displayType < ProjectWindowTab.DISPLAY_4_DEVELOPER) {
					currCreatedNode = this.setViewFilterVisualization(pwt, displayPanel);							
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
	 * projects has a predefined visualization or not
	 * @param pwt
	 * @param displayPanel
	 */
	private DefaultMutableTreeNode setViewFilterVisualization(ProjectWindowTab pwt, Class<? extends EnvironmentPanel> displayPanel) {
		
		DefaultMutableTreeNode newNode = null;
		if (pwt.getDisplayType() == ProjectWindowTab.DISPLAY_4_END_USER_VISUALIZATION) {
			// --- Show Visualization-Tab only in case of  ----------
			// --- an available defined Visualization-Panel ---------
			if (displayPanel!=null) {
				newNode = this.addProjectTabInternal(pwt);
			}
		} else {
			newNode = this.addProjectTabInternal(pwt);	
		}
		return newNode;
	}

	/**
	 * @param projectWindowTab4Configuration the projectWindowTab4Configuration to set
	 */
	public void setProjectWindowTab4Configuration(ProjectWindowTab projectWindowTab4Configuration) {
		this.projectWindowTab4Configuration = projectWindowTab4Configuration;
	}
	/**
	 * @return the projectWindowTab4Configuration
	 */
	public ProjectWindowTab getProjectWindowTab4Configuration() {
		return projectWindowTab4Configuration;
	}

	/**
	 * @param projectWindowTab4SimulationSetup the projectWindowTab4SimulationSetup to set
	 */
	public void setProjectWindowTab4SimulationSetup(ProjectWindowTab projectWindowTab4SimulationSetup) {
		this.projectWindowTab4SimulationSetup = projectWindowTab4SimulationSetup;
	}
	/**
	 * @return the projectWindowTab4SimulationSetup
	 */
	public ProjectWindowTab getProjectWindowTab4SimulationSetup() {
		return projectWindowTab4SimulationSetup;
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
