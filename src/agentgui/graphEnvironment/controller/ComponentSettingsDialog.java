package agentgui.graphEnvironment.controller;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import agentgui.core.application.Language;
import agentgui.core.gui.projectwindow.OntologyTabClassView;

public class ComponentSettingsDialog extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel jLabelComponentID = null;
	private JSplitPane jSplitPaneOntologyObjects = null;
	private JButton jButtonConfirm = null;
	private JButton jButtonCancel = null;
	private JTree jTreeOntologyClasses = null;
	
	private HashMap<String, TreePath> selectionPathDictionary = null;
	
	private GraphEnvironmentControllerGUI parent = null;
	
	/**
	 * This is the default constructor
	 */
	public ComponentSettingsDialog(GraphEnvironmentControllerGUI parent) {
		super();
		this.parent = parent;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle(Language.translate("Komponenten-Einstellungen"));
		this.setSize(500, 300);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.anchor = GridBagConstraints.EAST;
			gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints3.gridy = 2;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.EAST;
			gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.gridwidth = 2;
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.gridy = 0;
			jLabelComponentID = new JLabel();
			jLabelComponentID.setText("Komponente:");
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(jLabelComponentID, gridBagConstraints);
			jContentPane.add(getJSplitPaneOntologyObjects(), gridBagConstraints1);
			jContentPane.add(getJButtonConfirm(), gridBagConstraints2);
			jContentPane.add(getJButtonCancel(), gridBagConstraints3);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jSplitPaneOntologyObjects	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPaneOntologyObjects() {
		if (jSplitPaneOntologyObjects == null) {
			jSplitPaneOntologyObjects = new JSplitPane();
			jSplitPaneOntologyObjects.setDividerLocation(200);
			jSplitPaneOntologyObjects.setLeftComponent(new JScrollPane(getJTreeOntologyClasses()));
			jSplitPaneOntologyObjects.setRightComponent(new JPanel());	// Dummy component
		}
		return jSplitPaneOntologyObjects;
	}

	/**
	 * This method initializes jButtonConfirm	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonConfirm() {
		if (jButtonConfirm == null) {
			jButtonConfirm = new JButton();
			jButtonConfirm.setText(Language.translate("Übernehmen"));
			jButtonConfirm.addActionListener(this);
		}
		return jButtonConfirm;
	}

	/**
	 * This method initializes jButtonCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText(Language.translate("Abbrechen"));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	/**
	 * This method initializes jTreeOntologyClasses	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getJTreeOntologyClasses() {
		if (jTreeOntologyClasses == null) {
			jTreeOntologyClasses = new JTree();
			jTreeOntologyClasses.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			jTreeOntologyClasses.addTreeSelectionListener(new TreeSelectionListener() {
				
				@Override
				public void valueChanged(TreeSelectionEvent e) {
					DefaultMutableTreeNode CurrNode = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
					int divLoc = getJSplitPaneOntologyObjects().getDividerLocation();
					getJSplitPaneOntologyObjects().setRightComponent(new OntologyTabClassView( CurrNode ));
					getJSplitPaneOntologyObjects().setDividerLocation(divLoc);
				}
			});
		}
		return jTreeOntologyClasses;
	}

	public void setOntologyTreeModel(DefaultTreeModel ontoTree){
		this.getJTreeOntologyClasses().setModel(ontoTree);
		selectionPathDictionary = new HashMap<String, TreePath>();
		buildSelectionPathDictionary((DefaultMutableTreeNode) ontoTree.getRoot());
	}
	
	public void setSelectedComponent(GraphNode comp){
		jLabelComponentID.setText("Komponente "+comp.getId());
		TreePath selectionPath = selectionPathDictionary.get(comp.getClass().getSimpleName());
		getJTreeOntologyClasses().setSelectionPath(selectionPath);
		getJTreeOntologyClasses().scrollPathToVisible(selectionPath);
	}
	
	private void buildSelectionPathDictionary(DefaultMutableTreeNode node){
		selectionPathDictionary.put(node.toString(), new TreePath(node.getPath()));
		for(int i=0; i<node.getChildCount(); i++){
			buildSelectionPathDictionary((DefaultMutableTreeNode) node.getChildAt(i));
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(getJButtonConfirm())){
			parent.setComponentType();
			this.setVisible(false);
		}else if(e.getSource().equals(getJButtonCancel())){
			parent.setSelectedElement(null);
			this.setVisible(false);
		}
		
	}

}
