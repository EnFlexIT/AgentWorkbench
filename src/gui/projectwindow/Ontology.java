package gui.projectwindow;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import mas.onto.OntologieTree;

import application.Language;
import application.Project;

/**
 * @author: Christian Derksen
 *
 */
public class Ontology extends JPanel implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private Project CurrProject;
	private JSplitPane OntoSplitPane = null;
	private JScrollPane TreeScrollPane = null;

	private JTree OntoTree = null;
	private DefaultTreeModel OntoTreeModel;
	private DefaultMutableTreeNode RootNode;
	
	private JPanel OntoMain = null;

	/**
	 * This is the default constructor
	 */
	public Ontology( Project CP ) {
		super();
		this.CurrProject = CP;
		this.CurrProject.addObserver(this);		
		
		// --- TreeModel initialisieren --------------------------
		RootNode = new DefaultMutableTreeNode( Language.translate("Ontologie") );
		OntoTreeModel = new OntologieTree( RootNode, CurrProject );	
		// --- Form aufbauen -------------------------------------
		this.initialize();	

		// --- Basis-Verzeichnisse im OntoTree anzeigen -------
		OntoTreeExpand2Level(3, true);

	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(50);
		borderLayout.setVgap(50);
		this.setLayout(borderLayout);
		this.setSize(850, 500);
		this.setAutoscrolls(true);		
		this.setBorder(null);
		this.setFocusable(true);
		this.setVisible(true);
		this.add(getOntoSplitPane(), BorderLayout.CENTER);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// --- Das ActionCommand und den Auslöser des Events ermitteln ---
		String ActCMD = ae.getActionCommand();
		Object Trigger = ae.getSource();
		System.out.println( "ActCMD/Wert => " + ActCMD );
		System.out.println( "Auslöser => " + Trigger );

		// --- Fallunterscheidung 'Auslöser' -----------------------------
		if ( Trigger == "ProjectName" ) {
			CurrProject.setProjectName( ae.getActionCommand() );
		}
		else if ( Trigger == "ProjectFolder" ) {
			CurrProject.setProjectFolder( ae.getActionCommand() );
		}
		else if ( Trigger == "ProjectDescription" ) {
			CurrProject.setProjectDescription( ae.getActionCommand() );
		}
		else {
			
		};
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method initializes OntoSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getOntoSplitPane() {
		if (OntoSplitPane == null) {
			OntoSplitPane = new JSplitPane();
			OntoSplitPane.setDividerLocation(250);
			OntoSplitPane.setRightComponent(getOntoMain());
			OntoSplitPane.setLeftComponent(getTreeScrollPane());
		}
		return OntoSplitPane;
	}

	/**
	 * This method initializes TreeScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTreeScrollPane() {
		if (TreeScrollPane == null) {
			TreeScrollPane = new JScrollPane();
			TreeScrollPane.setViewportView(getOntoTree());
		}
		return TreeScrollPane;
	}

	/**
	 * This method initializes OntoTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getOntoTree() {
		if (OntoTree == null) {
			OntoTree = new JTree( OntoTreeModel );
			OntoTree.setName("OntoTree");
			OntoTree.setShowsRootHandles(false);
			OntoTree.setRootVisible(true);
			OntoTree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
			OntoTree.addTreeSelectionListener( new TreeSelectionListener() {
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
											
					} 
					// ----------------------------------------------------------
					// --- Tree-Selection abfangen --- S T O P ------------------
					// ----------------------------------------------------------
				}// End - valueChanged
			});

		}
		return OntoTree;
	}
	
	// If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
    public void OntoTreeExpand2Level(Integer Up2TreeLevel, boolean expand ) {
    	
    	Integer CurrNodeLevel = 1;
    	if ( Up2TreeLevel == null ) 
    		Up2TreeLevel = 1000;

    	OntoTreeExpand( new TreePath(RootNode), expand, CurrNodeLevel, Up2TreeLevel);
    }
    @SuppressWarnings("unchecked")
	private void OntoTreeExpand( TreePath parent, boolean expand, Integer CurrNodeLevel, Integer Up2TreeLevel) {
    
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (CurrNodeLevel >= Up2TreeLevel) {
        	return;
        }
        if (node.getChildCount() >= 0) {
            for ( Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                OntoTreeExpand(path, expand, CurrNodeLevel+1, Up2TreeLevel);
            }
        }    
        // Expansion or collapse must be done bottom-up
        if (expand) {
            OntoTree.expandPath(parent);
        } else {
        	OntoTree.collapsePath(parent);
        }
    }
	

	/**
	 * This method initializes OntoMain	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getOntoMain() {
		if (OntoMain == null) {
			OntoMain = new JPanel();
			OntoMain.setLayout(new GridBagLayout());
		}
		return OntoMain;
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
