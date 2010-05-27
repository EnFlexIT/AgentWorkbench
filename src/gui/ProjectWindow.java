package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import application.Application;
import application.Language;
import application.Project;

/**
 * @author: Christian Derksen
 *
 */
public class ProjectWindow extends JInternalFrame implements Observer {

	private static final long serialVersionUID = 1L;
	
	private Project CurrProject;
	private DefaultTreeModel ProjectTreeModel;
	private DefaultMutableTreeNode RootNode;
	private DefaultMutableTreeNode CurrentNode;  //  @jve:decl-index=0:
	private TreeMap<Integer, String[]> additionalNodes = new TreeMap<Integer, String[]>();  //  @jve:decl-index=0:
	
	private JTabbedPane ProjectViewRightTabs = null;
	private JSplitPane ProjectViewSplit = null;
	private JScrollPane jScrollPane = null;
	private JTree ProjectTree = null;

	/**
	 * This is the default constructor
	 */
	public ProjectWindow( Project CP ) {
		super();
		this.CurrProject = CP;		
		this.CurrProject.addObserver(this);		
		
		// --- TreeModel initialisieren --------------------------
		RootNode = new DefaultMutableTreeNode( CurrProject.getProjectName() );
		ProjectTreeModel = new DefaultTreeModel( RootNode );	
		
		// --- Projektfenster zusammenbauen ----------------------
		this.initialize();		
		
		// --- Anzeige der Basisinformationen immer einblenden ---
		this.addProjectTab(Language.translate("Projekt-Info"), null, new gui.projectwindow.ProjectInfo( CurrProject ), Language.translate("Projekt-Info"));

		// --- Die (optionalen) Karteikarten einblenden ----------
		this.addProjectTab(Language.translate("Ontologie"), null, new gui.projectwindow.OntologyTab(CurrProject), Language.translate("Kommunikation"));
		this.addProjectTab(Language.translate("Basis-Agenten"), null, new gui.projectwindow.BaseAgents(CurrProject), Language.translate("Basis-Agenten"));
		this.addProjectTab(Language.translate("Umgebungs-Setup"), null, new mas.environment.guiComponents.EnvironmentControllerGUI(CurrProject), Language.translate("Umgebungs-Setup"));
		this.addProjectTab(Language.translate("Simulations-Setup"), null, new gui.projectwindow.SetupSimulation(CurrProject, this), Language.translate("Simulations-Setup"));
		this.addProjectTab(Language.translate("Simulation"), null, new gui.projectwindow.Simulation(this.CurrProject), Language.translate("Simulation"));
		this.addProjectTab(Language.translate("Simulationsmeldungen"), null, new gui.projectwindow.SimulationMessages(CurrProject), Language.translate("Simulationsmeldungen"));
		
		// --- Ggf. noch fehlende Nodes hinzufügen ---------------
		if (additionalNodes.size()!=0) {
			this.addAdditionalNodes();
		}
		
		// --- Basis-Verzeichnisse im ProjectTree anzeigen -------
		this.ProjectTreeExpand2Level(3, true);
		
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
		Application.MainWindow.ProjectDesktop.add(this);		
	}

	/**
	 * This method initializes ProjectViewSplit	
	 * 	
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
	 * 	
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
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getProjectTree() {
		if (ProjectTree == null) {
			ProjectTree = new JTree( ProjectTreeModel );
			ProjectTree.setName("ProjectTree");
			ProjectTree.setShowsRootHandles(false);
			ProjectTree.setRootVisible(true);
			ProjectTree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
			ProjectTree.addTreeSelectionListener( new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent ts) {
					// ----------------------------------------------------------
					// --- Tree-Selection abfangen --- S T A R T ----------------
					// ----------------------------------------------------------
					TreePath PathSelected = ts.getPath();
					Integer PathLevel = PathSelected.getPathCount();

					// ----------------------------------------------------------
					if ( PathLevel >= 2 ) {
						// ------------------------------------------------------
						// --- Fokus auf die entsprechende Karteikarte setzen ---
						// ------------------------------------------------------
						Component currComp = null;
						JPanel subJPanel = null;
						JTabbedPane subJTabs = null;
						String FocusNodeName = PathSelected.getPathComponent(1).toString();
						
						// --- Nach entsprechender Karteikarte suchen -----------
						for (int i=0; i<ProjectViewRightTabs.getComponentCount();  i++ ) {
							currComp = ProjectViewRightTabs.getComponent(i);
							if ( currComp.getName() == FocusNodeName ) {
								ProjectViewRightTabs.setSelectedIndex(i);
								if ( currComp instanceof JPanel ) {
									subJPanel = (JPanel) ProjectViewRightTabs.getComponent(i);	
								}
							}							
						}	
						// ------------------------------------------------------
						// --- Falls ein Aufruf aus einer tieferen Ebene kam ----
						// ------------------------------------------------------
						if (PathLevel>2 && subJPanel!=null) {
							// --- Suche nach einer JTabbedPane -----------------
							for (int i=0; i<subJPanel.getComponentCount();  i++ ) {
								currComp = subJPanel.getComponent(i);
								if ( currComp instanceof JTabbedPane ) {
									subJTabs = (JTabbedPane) currComp;
									break;									
								}							
							}	
							FocusNodeName = PathSelected.getPathComponent(2).toString();
							if (subJTabs!=null) {
								// --- Fokus auf Karteikarte setzen -------------
								for (int i=0; i<subJTabs.getComponentCount();  i++ ) {
									if ( subJTabs.getComponent(i).getName() == FocusNodeName ) {
										subJTabs.setSelectedIndex(i);
									}							
								}	
							}
						}
						// ------------------------------------------------------
					} 
					// ----------------------------------------------------------
					// --- Tree-Selection abfangen --- S T O P ------------------
					// ----------------------------------------------------------
				}// End - valueChanged
			});

		}
		return ProjectTree;
	}
	
	// If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
    public void ProjectTreeExpand2Level(Integer Up2TreeLevel, boolean expand ) {
    	
    	Integer CurrNodeLevel = 1;
    	if ( Up2TreeLevel == null ) 
    		Up2TreeLevel = 1000;

    	ProjectTreeExpand( new TreePath(RootNode), expand, CurrNodeLevel, Up2TreeLevel);
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
            ProjectTree.expandPath(parent);
        } else {
        	ProjectTree.collapsePath(parent);
        }
    }
	
	
	@Override
	/**
	 * Get the notification of the ObjectModel
	 */
	public void update(Observable arg0, Object OName) {
		
		String ObjectName = OName.toString();
		if ( ObjectName.equalsIgnoreCase( "ProjectName" ) ) {
			CurrentNode = (DefaultMutableTreeNode) ProjectTree.getModel().getRoot();
			CurrentNode.setUserObject( CurrProject.getProjectName() );
			ProjectTreeModel.nodeChanged(CurrentNode);
			ProjectTree.repaint();
			Application.setTitelAddition( CurrProject.getProjectName() );
		}			
		else {
			//System.out.println("Unbekannter Updatebefehl vom Observerable ...");
		};
		this.repaint();
	}
		
	/**
	 * This method initializes ProjectViewRight	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getProjectViewRightTabs() {
		if (ProjectViewRightTabs == null) {
			ProjectViewRightTabs = new JTabbedPane();
			ProjectViewRightTabs.setName("ProjectTabs");
			ProjectViewRightTabs.setTabPlacement(JTabbedPane.TOP);
			ProjectViewRightTabs.setPreferredSize(new Dimension(126, 72));
			ProjectViewRightTabs.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return ProjectViewRightTabs;
	}	

	/**
	 * Adds a Project-Tab and a new Base Folder 
	 * (child of root!) to the ProjectWindow
	 * 
	 * @param title
	 * @param icon
	 * @param component
	 * @param tip
	 */	
	public void addProjectTab( String title, Icon icon, Component component, String tip ) {
		// --- GUI-Komponente in das TabbedPane-Objekt einfï¿½gen -------------
		component.setName( title ); 								// --- Component benennen ----
		ProjectViewRightTabs.addTab( title, icon, component, tip);	// --- Component anhängen ----
		// --- Neuen Basisknoten einfügen ------------------
		addProjectTabNode(title);
	}

	/**
	 * Adds a new node to the left Project-Tree
	 * @param newNode
	 */
	public void addProjectTabNode( String newNode ) {
		RootNode.add( new DefaultMutableTreeNode( newNode ) );
	}
	/**
	 * Adds a child-node to a given parent node of the left Project-Tree.
	 * If the node can not be found, the methode adds the textual node-definition
	 * to the local TreeMap 'additionalNodes', for a later addition to the Tree
	 * @param parentNode
	 * @param newNode
	 */
	public void addProjectTabNode( String parentNodeName, String newNodeName ) {
		DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode( newNodeName );
		DefaultMutableTreeNode parentNode  = getTreeNode(parentNodeName); 
		if (parentNode!=null) {
			parentNode.add( currentNode );			
		} else {
			String[] newNodeDef = new String[2];
			newNodeDef[0] = parentNodeName;
			newNodeDef[1] = newNodeName;
			additionalNodes.put(additionalNodes.size()+1, newNodeDef);
		}
	}
	/**
	 * Adds some further nodes to the left Project-Tree if recommended
	 * through the TreeMap 'additionalNodes' at the end of the 
	 * constructor-method
	 */
	private void addAdditionalNodes() {
		
		Vector<Integer> nodeKeys = new Vector<Integer>( additionalNodes.keySet() );
		Collections.sort(nodeKeys);
		Iterator<Integer> it = nodeKeys.iterator();
	    while (it.hasNext()) {
	    	Integer key = it.next();
	    	String[] newNodeDef = additionalNodes.get(key);
	    	this.addProjectTabNode(newNodeDef[0], newNodeDef[1]);
	    	additionalNodes.remove(key);
	    }
		if (additionalNodes.size()==0) {
			additionalNodes = null;
		}
	    
	}
	/**
	 * Returns the Tree-Node requested by the Reference 
	 * @param Reference
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DefaultMutableTreeNode getTreeNode( String Reference ) {
		
		DefaultMutableTreeNode NodeFound = null;
		DefaultMutableTreeNode CurrNode = null;
		String CurrNodeText;
		
		for (Enumeration<DefaultMutableTreeNode> e = RootNode.breadthFirstEnumeration(); e.hasMoreElements();) {
			CurrNode = e.nextElement();
			CurrNodeText = CurrNode.getUserObject().toString(); 
			if ( CurrNodeText.equals(Reference) ) {				
				NodeFound = CurrNode;
				break;
			} 
		}
		return NodeFound;
	}
	
	/**
	 * Setzt den Fokus auf eine bestimmte Karteikarte
	 * @param title
	 */
	public void setFocusOnProjectTab ( String title ) {
		for (int i=0; i<ProjectViewRightTabs.getComponentCount();  i++ ) {
			Component Comp = ProjectViewRightTabs.getComponent(i);
			if ( Comp.getName().equalsIgnoreCase( Language.translate(title) ) ) {
				ProjectViewRightTabs.setSelectedIndex(i);		
			}
		}	
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
