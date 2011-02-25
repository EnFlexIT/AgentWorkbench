package agentgui.core.ontologies.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JSplitPane;

public class DynTreeViewer extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane = null;
	private JTree jTreeDynForm = null;
	
	private DefaultTreeModel treeModel = null;
	private JSplitPane jSplitPane = null;
	private JScrollPane jScrollPaneRight = null;
	

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
	 * Adds a specified component to the right 
	 * @param component
	 */
	public void addPanel(JComponent component) {
		jScrollPaneRight.setViewportView(component);
		jScrollPaneRight.validate();
		jScrollPaneRight.repaint();
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(900, 300);
		this.setContentPane(getJSplitPane());
		this.setTitle("DynForm - SlotView");
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

	/**
	 * This method initializes jSplitPane	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerLocation(395);
			jSplitPane.setRightComponent(getJScrollPaneRight());
			jSplitPane.setLeftComponent(getJContentPane());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jScrollPaneRight	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneRight() {
		if (jScrollPaneRight == null) {
			jScrollPaneRight = new JScrollPane();
		}
		return jScrollPaneRight;
	}

}
