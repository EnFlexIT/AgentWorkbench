package de.enflexit.df.core.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import de.enflexit.df.core.model.DataController;

/**
 * The Class JPanelTree.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelTree extends JPanel implements ActionListener {

	private static final long serialVersionUID = 4514690381659493450L;
	
	private DataController dataController;
	
	private JPanel jPanelDataTree;
	private JTreeData jTreeData;
	private JScrollPane jScrollPaneTree;

	private JPanel jPanelSearch;
	private JLabel jLabelSearch;
	private JTextField jTextFieldSearch;
	private JButton jButtonResetSearch;
	
	/**
	 * Instantiates a new JPanelTree.
	 */
	@SuppressWarnings("unused")
	private JPanelTree() {
		this(null);
	}
	/**
	 * Instantiates a new JPanelTree.
	 * @param jPanelData the current JPanelData instance
	 */
	public JPanelTree(DataController dataController) {
		this.setDataController(dataController);
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(this.getJPanelDataTree(), BorderLayout.CENTER);
		this.add(this.getPanelSearch(), BorderLayout.SOUTH);
	}
	
	/**
	 * Sets the DataController.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
	}
	/**
	 * Returns the data controller.
	 * @return the data controller
	 */
	public DataController getDataController() {
		return dataController;
	}
	
	/**
	 * Returns the JPanel for the data tree.
	 * @return the j panel tree
	 */
	private JPanel getJPanelDataTree() {
		if (jPanelDataTree==null) {
			jPanelDataTree = new JPanel();
			GridBagLayout gbl_jPanelTree = new GridBagLayout();
			gbl_jPanelTree.columnWidths = new int[]{0, 0};
			gbl_jPanelTree.rowHeights = new int[]{0, 0};
			gbl_jPanelTree.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_jPanelTree.rowWeights = new double[]{1.0, Double.MIN_VALUE};
			jPanelDataTree.setLayout(gbl_jPanelTree);
			GridBagConstraints gbc_jTree = new GridBagConstraints();
			gbc_jTree.fill = GridBagConstraints.BOTH;
			gbc_jTree.insets = new Insets(0, 5, 0, 5);
			gbc_jTree.gridx = 0;
			gbc_jTree.gridy = 0;
			jPanelDataTree.add(getJScrollPaneTree(), gbc_jTree);
		}
		return jPanelDataTree;
	}
	
	private JScrollPane getJScrollPaneTree() {
		if (jScrollPaneTree==null) {
			jScrollPaneTree = new JScrollPane();
			jScrollPaneTree.setViewportView(this.getJTreeData());
		}
		return jScrollPaneTree;
	}
	/**
	 * Returns the JTree for the data.
	 * @return the JTree for the data
	 */
	private JTreeData getJTreeData() {
		if (jTreeData == null) {
			jTreeData = new JTreeData(this.getDataController());
		}
		return jTreeData;
	}

	
	private JPanel getPanelSearch() {
		if (jPanelSearch == null) {
			jPanelSearch = new JPanel();
			GridBagLayout gbl_jPanelSearch = new GridBagLayout();
			gbl_jPanelSearch.columnWidths = new int[]{0, 0, 0, 0};
			gbl_jPanelSearch.rowHeights = new int[]{0, 0};
			gbl_jPanelSearch.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelSearch.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelSearch.setLayout(gbl_jPanelSearch);
			GridBagConstraints gbc_jLabelSearch = new GridBagConstraints();
			gbc_jLabelSearch.anchor = GridBagConstraints.EAST;
			gbc_jLabelSearch.insets = new Insets(5, 5, 5, 0);
			gbc_jLabelSearch.gridx = 0;
			gbc_jLabelSearch.gridy = 0;
			jPanelSearch.add(getJLabelSearch(), gbc_jLabelSearch);
			GridBagConstraints gbc_jTextFieldSearch = new GridBagConstraints();
			gbc_jTextFieldSearch.insets = new Insets(5, 5, 5, 0);
			gbc_jTextFieldSearch.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldSearch.gridx = 1;
			gbc_jTextFieldSearch.gridy = 0;
			jPanelSearch.add(getJTextFieldSearch(), gbc_jTextFieldSearch);
			GridBagConstraints gbc_jButtonResetSearch = new GridBagConstraints();
			gbc_jButtonResetSearch.insets = new Insets(5, 5, 5, 5);
			gbc_jButtonResetSearch.gridx = 2;
			gbc_jButtonResetSearch.gridy = 0;
			jPanelSearch.add(getJButtonResetSearch(), gbc_jButtonResetSearch);
		}
		return jPanelSearch;
	}
	private JLabel getJLabelSearch() {
		if (jLabelSearch == null) {
			jLabelSearch = new JLabel("Search:");
			jLabelSearch.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSearch;
	}
	private JTextField getJTextFieldSearch() {
		if (jTextFieldSearch == null) {
			jTextFieldSearch = new JTextField();
			jTextFieldSearch.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldSearch.setColumns(10);
		}
		return jTextFieldSearch;
	}
	private JButton getJButtonResetSearch() {
		if (jButtonResetSearch == null) {
			jButtonResetSearch = new JButton("");
			jButtonResetSearch.setPreferredSize(new Dimension(26, 26));
		}
		return jButtonResetSearch;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonResetSearch()) {
			// --- Reset search -------------------------------------
			
			
		}
		
	}
	
}
