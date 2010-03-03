package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
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
		addProjectTab( "Projekt-Info", null, new gui.projectwindow.ProjectInfo( CurrProject ), "Projekt-Info" );

		// --- Die (optionalen) Karteikarten einblenden ----------
		addProjectTab(Language.translate("Agenten"), null, new gui.projectwindow.BaseAgents(CurrProject), Language.translate("Agenten"));
		addProjectTab(Language.translate("Ontologie"), null, new gui.projectwindow.OntologyTab(CurrProject), Language.translate("Kommunikation"));		
		addProjectTab(Language.translate("Umgebungs-Setup"), null, new mas.environment.EnvironmentControllerGUI(this.CurrProject), Language.translate("Umgebungs-Setup"));
		addProjectTab(Language.translate("Simulations-Setup"), null, new gui.projectwindow.SetupSimulation(CurrProject), Language.translate("Simulations-Setup"));
		addProjectTab(Language.translate("Simulation"), null, new mas.display.DisplayAgentGUI(this.CurrProject), Language.translate("Simulation"));
		addProjectTab(Language.translate("Simulationsmeldungen"), null, new gui.projectwindow.SimulationMessages(CurrProject), Language.translate("Simulationsmeldungen"));

		// --- Basis-Verzeichnisse im ProjectTree anzeigen -------
		ProjectTreeExpand2Level(2, true);
		
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
					//System.out.println( PathLevel + " => "  + ts.getPath().toString() );
					
					// ----------------------------------------------------------
					if ( PathLevel == 2 ) {
						// ------------------------------------------------------
						// --- Fokus auf die entsprechende Karteikarte setzen ---
						// ------------------------------------------------------
						TreeNode BaseNode = (TreeNode) ts.getPath().getPathComponent(1);
						String BaseNodeName = BaseNode.toString();
						for (int i=0; i<ProjectViewRightTabs.getComponentCount();  i++ ) {
							if ( ProjectViewRightTabs.getComponent(i).getName() == BaseNodeName ) {
								// --- Fokus setzen -----------------------------
								ProjectViewRightTabs.setSelectedIndex(i);								
							}							
						}					
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
		ProjectViewRightTabs.addTab( title, icon, component, tip);	// --- Component anhï¿½ngen ---
		// --- Neuen Basisknoten einfügen ------------------
		CurrentNode = new DefaultMutableTreeNode( title );
		RootNode.add( CurrentNode );		
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
