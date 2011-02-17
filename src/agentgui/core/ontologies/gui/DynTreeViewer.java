package agentgui.core.ontologies.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

public class DynTreeViewer extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane = null;
	private JTree jTreeDynForm = null;
	
	private DefaultTreeModel treeModel = null;
	

	/**
	 * @param owner
	 */
	public DynTreeViewer(DefaultTreeModel objectTree) {
		super();
		treeModel = objectTree;
		initialize();
		this.jTreeDynForm.setModel(treeModel);
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setTitle("DynForm - SlotView");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJScrollPane(), gridBagConstraints);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jScrollPane	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTreeDynForm());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTreeDynForm	
	 * @return javax.swing.JTree	
	 */
	private JTree getJTreeDynForm() {
		if (jTreeDynForm == null) {
			jTreeDynForm = new JTree();
		}
		return jTreeDynForm;
	}

}
