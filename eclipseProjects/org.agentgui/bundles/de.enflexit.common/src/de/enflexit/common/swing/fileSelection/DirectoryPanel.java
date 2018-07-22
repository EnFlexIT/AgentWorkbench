package de.enflexit.common.swing.fileSelection;

import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Insets;

/**
 * The Class DirectoryPanel uses the {@link DirectoryEvaluator} to display
 * the files found for visualization and selection.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DirectoryPanel extends JPanel implements FileTreeListener, DirectoryEvaluatorListener {

	private static final long serialVersionUID = 6678108777304905085L;
	
	private DirectoryEvaluator directoryEvaluator;
	private JScrollPane jScrollPaneFileTree;
	private FileTree fileTree;
	private JLabel jLabelFileInfo;
	
	
	/**
	 * Instantiates a new directory panel.
	 */
	public DirectoryPanel() {
		this(null);
	}
	/**
	 * Instantiates a new directory panel.
	 * @param rootDirectory the root directory
	 */
	public DirectoryPanel(File rootDirectory) {
		this.setRootDirectory(rootDirectory);
		this.initialize();
	}
	
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{450, 0};
		gridBagLayout.rowHeights = new int[]{0, 300, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelFileInfo = new GridBagConstraints();
		gbc_jLabelFileInfo.insets = new Insets(0, 0, 5, 0);
		gbc_jLabelFileInfo.anchor = GridBagConstraints.WEST;
		gbc_jLabelFileInfo.gridx = 0;
		gbc_jLabelFileInfo.gridy = 0;
		add(getJLabelFileInfo(), gbc_jLabelFileInfo);
		GridBagConstraints gbc_jScrollPaneFileTree = new GridBagConstraints();
		gbc_jScrollPaneFileTree.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneFileTree.gridx = 0;
		gbc_jScrollPaneFileTree.gridy = 1;
		this.add(this.getJScrollPaneFileTree(), gbc_jScrollPaneFileTree);
	}
	
	private JScrollPane getJScrollPaneFileTree() {
		if (jScrollPaneFileTree == null) {
			jScrollPaneFileTree = new JScrollPane();
			jScrollPaneFileTree.setBorder(BorderFactory.createEtchedBorder());
			jScrollPaneFileTree.setViewportView(this.getFileTree());
		}
		return jScrollPaneFileTree;
	}
	private FileTree getFileTree() {
		if (fileTree == null) {
			fileTree = new FileTree(this.getDirectoryEvaluator().getFileTreeModel());
			fileTree.addFileTreeListener(this);
		}
		return fileTree;
	}
	
	/**
	 * Returns the DirectoryEvaluator used to show the information. 
	 * Just use it to get a list of selected or unselected file elements.
	 * @return the directory evaluator
	 */
	public DirectoryEvaluator getDirectoryEvaluator() {
		if (directoryEvaluator==null) {
			directoryEvaluator = new DirectoryEvaluator();
			directoryEvaluator.addDirectoryEvaluatorListener(this);
		}
		return directoryEvaluator;
	}

	/**
	 * Returns the currently used root directory.
	 * @return the root directory
	 */
	public File getRootDirectory() {
		return this.getDirectoryEvaluator().getRootDirectory();
	}
	/**
	 * Sets the root directory for the tree node.
	 * @param rootDirectory the new root directory
	 */
	public void setRootDirectory(File rootDirectory) {
		this.getDirectoryEvaluator().setRootDirectory(rootDirectory);
	}
	
	
	/**
	 * Return the JLabel with the file info.
	 * @return the JLabel file info
	 */
	private JLabel getJLabelFileInfo() {
		if (jLabelFileInfo == null) {
			jLabelFileInfo = new JLabel(" ");
			jLabelFileInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelFileInfo;
	}
	/**
	 * Updates file info text.
	 */
	private void updateFileInfoText() {
		int filesFound  = this.getDirectoryEvaluator().getFilesFound().size();
		int filesSeleted = this.getDirectoryEvaluator().getFileList(true).size(); 
		int filesExclude = this.getDirectoryEvaluator().getFileList(false).size();
		this.getJLabelFileInfo().setText("Files found: " + filesFound + " - Files selected: " + filesSeleted + " - Files excluded " + filesExclude);
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.fileSelection.FileTreeListener#onFileSelectionChanged()
	 */
	@Override
	public void onFileSelectionChanged() {
		this.updateFileInfoText();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.fileSelection.DirectoryEvaluatorListener#onEvaluationWasFinalized()
	 */
	@Override
	public void onEvaluationWasFinalized() {
		this.updateFileInfoText();
	}
}
