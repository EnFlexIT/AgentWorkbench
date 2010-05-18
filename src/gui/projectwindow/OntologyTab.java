package gui.projectwindow;

import gui.OntologieSelector;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import mas.onto.OntologyClass;
import mas.onto.OntologyClassTreeObject;

import application.Application;
import application.Language;
import application.Project;

/**
 * @author: Christian Derksen
 *
 */
public class OntologyTab extends JPanel implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private Project CurrProject;
	private JSplitPane OntoSplitPane = null;
	private JScrollPane TreeScrollPane = null;

	private JTree OntoTree = null;
	private JPanel OntoMain = null;

	private JPanel jPanelLeft = null;

	private JPanel jPanelLeftNavi = null;

	private JButton jButtonAddOntology = null;

	private JButton jButtonRemoveOntology = null;

	/**
	 * This is the default constructor
	 */
	public OntologyTab( Project CP ) {
		super();
		this.CurrProject = CP;
		this.CurrProject.addObserver(this);		
		
		// --- Form aufbauen -------------------------------------
		this.initialize();	
		
		// --- Basis-Verzeichnisse im OntoTree anzeigen -------
		OntoTreeExpand2Level(4, true);
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


	/**
	 * This method initializes OntoSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getOntoSplitPane() {
		if (OntoSplitPane == null) {
			OntoSplitPane = new JSplitPane();
			OntoSplitPane.setDividerLocation(250);
			OntoSplitPane.setLeftComponent(getJPanelLeft());
			OntoSplitPane.setRightComponent(getOntoMain());
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
			//OntoTree = new JTree( CurrProject.Ontology.getOntologyTree() );
			OntoTree = new JTree( CurrProject.ontologies4Project.getOntologyTree() );
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
					// --- Node auslesen und Slots anzeigen ---------------------
					DefaultMutableTreeNode currNode = (DefaultMutableTreeNode)ts.getPath().getLastPathComponent();
					JPanel NewSlotView = new OntologyTabClassView( currNode ); 
					int DivLoc = OntoSplitPane.getDividerLocation();
					OntoSplitPane.setRightComponent( NewSlotView );
					OntoSplitPane.setDividerLocation(DivLoc);
					OntoMain = NewSlotView; 
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
    	OntoTreeExpand( new TreePath( CurrProject.ontologies4Project.getOntologyTree().getRoot() ), expand, CurrNodeLevel, Up2TreeLevel);
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
		}
		return OntoMain;
	}

	/**
	 * This method initializes jPanelLeft	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelLeft() {
		if (jPanelLeft == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.weighty = 0.0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.weightx = 1.0;
			jPanelLeft = new JPanel();
			jPanelLeft.setLayout(new GridBagLayout());
			jPanelLeft.add(getJPanelLeftNavi(), gridBagConstraints1);
			jPanelLeft.add(getTreeScrollPane(), gridBagConstraints);
		}
		return jPanelLeft;
	}

	/**
	 * This method initializes jPanelLeftNavi	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelLeftNavi() {
		if (jPanelLeftNavi == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(10, 10, 10, 0);
			gridBagConstraints2.gridy = 0;
			jPanelLeftNavi = new JPanel();
			jPanelLeftNavi.setLayout(new GridBagLayout());
			jPanelLeftNavi.setPreferredSize(new Dimension(20, 46));
			jPanelLeftNavi.add(getJButtonAddOntology(), gridBagConstraints2);
			jPanelLeftNavi.add(getJButtonRemoveOntology(), gridBagConstraints3);
		}
		return jPanelLeftNavi;
	}

	/**
	 * This method initializes jButtonAddOntology	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAddOntology() {
		if (jButtonAddOntology == null) {
			jButtonAddOntology = new JButton();
			jButtonAddOntology.setPreferredSize(new Dimension(45, 26));
			jButtonAddOntology.setActionCommand("OntologieAdd");
			jButtonAddOntology.setIcon(new ImageIcon(getClass().getResource("/img/ListPlus.png")));
			jButtonAddOntology.setToolTipText("Add Ontologie...");
			jButtonAddOntology.addActionListener(this);
		}
		return jButtonAddOntology;
	}

	/**
	 * This method initializes jButtonRemoveOntology	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRemoveOntology() {
		if (jButtonRemoveOntology == null) {
			jButtonRemoveOntology = new JButton();
			jButtonRemoveOntology.setPreferredSize(new Dimension(45, 26));
			jButtonRemoveOntology.setActionCommand("OntologieRemove");
			jButtonRemoveOntology.setIcon(new ImageIcon(getClass().getResource("/img/ListMinus.png")));
			jButtonRemoveOntology.setToolTipText("Remove Ontology");
			jButtonRemoveOntology.addActionListener(this);
		}
		return jButtonRemoveOntology;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// --- Das ActionCommand und den Auslöser des Events ermitteln ---
		String ActCMD = ae.getActionCommand();
		String MsgHead = null;
		String MsgText = null;
		//Object Trigger = ae.getSource();
		//System.out.println( "ActCMD/Wert => " + ActCMD );
		//System.out.println( "Auslöser => " + Trigger );

		if ( ActCMD == "OntologieAdd" ) {
			// --- Ontologie hinzufgen ------------------------------
			String ActionTitel = Language.translate("Ontologie hinzufügen"); 
			OntologieSelector onSel = new OntologieSelector( Application.MainWindow,
															CurrProject.getProjectName() + ": " + ActionTitel,
															true,
															CurrProject
															);			
			onSel.setVisible(true);
			// === Hier geht's weiter, wenn der Dialog wieder geschlossen ist ===
			if ( onSel.isCanceled() == true ) {
				Application.setStatusBar( Language.translate("Fertig") );
				return;
			}
			String newOntologie = onSel.getNewOntologySelected();
			onSel.dispose();
			onSel = null;
			// --- Neu gewählte Ontologie hinzufügen ---------------- 
			CurrProject.subOntologyAdd(newOntologie);
		}
		else if ( ActCMD == "OntologieRemove" ) {
			// --- Ontologie entfernen ------------------------------
			if ( OntoTree.isSelectionEmpty() ) {
				MsgHead = Language.translate("Fehlende Auswahl !");
				MsgText = Language.translate("Zum Löschen, wählen Sie bitte eine der dargestellten Ontologie aus!");			
				JOptionPane.showInternalMessageDialog( Application.MainWindow.getContentPane(), MsgText, MsgHead, JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			DefaultMutableTreeNode currNode = (DefaultMutableTreeNode) OntoTree.getSelectionPath().getLastPathComponent();
			OntologyClassTreeObject octo = (OntologyClassTreeObject) currNode.getUserObject();
			OntologyClass oc = octo.getOntologyClass();
			if ( oc==null ) {
				return;
			}
			// --- Gewählte Ontologie entfernen ---------------------
			CurrProject.subOntologyRemove(oc.getOntologyMainClass());
		}
		else {
			System.out.println( "Unknown ActionCommand: " + ActCMD );
		};
	}

	@Override
	public void update(Observable arg0, Object OName) {
		
		String ObjectName = OName.toString();
		if ( ObjectName.equalsIgnoreCase( "ProjectOntology" ) ) {
			// --- Ansicht auf die projekt-Ontologie aktualisieren --
			this.OntoTree.setModel( CurrProject.ontologies4Project.getOntologyTree() );
			this.OntoTreeExpand2Level(4, true);
		} else {
			System.out.println("Unbekannte Meldung vom Observer: " + ObjectName);
		}
		
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
