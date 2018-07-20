package de.enflexit.common.swing.fileSelection;

import java.io.File;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTree;

/**
 * The Class DirectoryPanel uses the {@link DirectoryEvaluator} to display
 * the files found for visualization and selection.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DirectoryPanel extends JPanel {

	private static final long serialVersionUID = 6678108777304905085L;
	
	private DirectoryEvaluator directoryEvaluator;
	private JScrollPane jScrollPaneFileTree;
	private JTree fileTree;
	
	
	/**
	 * Instantiates a new directory panel.
	 */
	public DirectoryPanel() {
		this.initialize();
	}
	/**
	 * Instantiates a new directory panel.
	 * @param rootDirectory the root directory
	 */
	public DirectoryPanel(File rootDirectory) {
		this.initialize();
		this.setRootDirectory(rootDirectory);
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout(0, 0));
		this.add(this.getJScrollPaneFileTree(), BorderLayout.CENTER);
	}
	
	private JScrollPane getJScrollPaneFileTree() {
		if (jScrollPaneFileTree == null) {
			jScrollPaneFileTree = new JScrollPane();
			jScrollPaneFileTree.setViewportView(this.getFileTree());
		}
		return jScrollPaneFileTree;
	}
	private JTree getFileTree() {
		if (fileTree == null) {
			fileTree = new JTree();
			fileTree.setModel(this.getDirectoryEvaluator().getFileTreeModel());
		}
		return fileTree;
	}
	
	/**
	 * Returns the DirectoryEvaluator used to show the information.
	 * @return the directory evaluator
	 */
	private DirectoryEvaluator getDirectoryEvaluator() {
		if (directoryEvaluator==null) {
			directoryEvaluator = new DirectoryEvaluator(null);
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
}
