package de.enflexit.common.swing.fileSelection;

import java.io.File;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * The Class DirectoryEvaluator can be used to evaluate all files and directories
 * starting from a specified source directory. It provides methods for selecting 
 * specific files or directories and will return them as corresponding list.
 * Further, starting from the root directory, a tree model is provided that can 
 * be used in visualizations.    
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class DirectoryEvaluator {

	private boolean debugFileEvaluation = true;
	
	private File rootDirectory; 
	private ArrayList<FileDescriptor> filesFound;
	private DefaultMutableTreeNode rootNode; 
	private DefaultTreeModel fileTreeModel;
	
	
	/**
	 * Instantiates a new directory evaluator.
	 */
	public DirectoryEvaluator() {
		this(null);
	}
	/**
	 * Instantiates a new directory evaluator.
	 * @param rootDirectory the root directory
	 */
	public DirectoryEvaluator(File rootDirectory) {
		this.setRootDirectory(rootDirectory);
	}
	
	/**
	 * Sets the root directory of the evlauator.
	 * @param rootDirectory the new root directory
	 */
	public void setRootDirectory(File rootDirectory) {
		this.rootDirectory = rootDirectory;
		if (this.rootDirectory!=null) {
			this.getFilesFound().clear();
			this.removeAllTreeNodes();
			this.getRootNode().setUserObject(new FileDescriptor(rootDirectory));
			this.evaluateDirectory(this.rootDirectory, this.getRootNode());
		}
	}
	/**
	 * Returns the currently used root directory or null.
	 * @return the root directory
	 */
	public File getRootDirectory() {
		return this.rootDirectory;
	}
	
	
	/**
	 * Evaluates the specified directory for file and directories.
	 *
	 * @param directory the directory to evaluate
	 * @param parentNode the parent node
	 */
	private void evaluateDirectory(File directory, DefaultMutableTreeNode parentNode) {
		
		File[] fileArray = directory.listFiles();
		for (int i = 0; i < fileArray.length; i++) {
			
			// --- Get current file and create node -------
			File file = fileArray[i];
			if (this.debugFileEvaluation==true) {
				System.out.println("=> " + file.getAbsolutePath());
			}
			
			this.getFilesFound().add(new FileDescriptor(file));
			DefaultMutableTreeNode fileNode = this.createTreeNode(file); 
			parentNode.add(fileNode);

			if (file.isDirectory()) {
				this.evaluateDirectory(file, fileNode);
			}
		}
	}
	
	/**
	 * Creates the tree node.
	 *
	 * @param fileFound the file found
	 * @return the default mutable tree node
	 */
	private DefaultMutableTreeNode createTreeNode(File fileFound) {
		return new DefaultMutableTreeNode(new FileDescriptor(fileFound));
	}
	
	/**
	 * Returns the list of files found.
	 * @return the files found
	 */
	public ArrayList<FileDescriptor> getFilesFound() {
		if (filesFound==null) {
			filesFound = new ArrayList<>();
		}
		return filesFound;
	}
	
	/**
	 * Returns the root node of the tree model.
	 * @return the root node
	 */
	public DefaultMutableTreeNode getRootNode() {
		if (rootNode==null) {
			rootNode = new DefaultMutableTreeNode(new FileDescriptor(this.rootDirectory));
		}
		return rootNode;
	}
	/**
	 * Return the tree model for the files found.
	 * @return the file tree model
	 */
	public DefaultTreeModel getFileTreeModel() {
		if (fileTreeModel==null) {
			fileTreeModel = new DefaultTreeModel(this.getRootNode());
		}
		return fileTreeModel;
	}
	/**
	 * Removes the all tree nodes.
	 */
	private void removeAllTreeNodes() {
		for (int i=0; i<this.getRootNode().getChildCount(); i++) {
			this.getRootNode().remove(i);
		}
	}
	
	/**
	 * The Class FileDescriptor.
	 */
	public class FileDescriptor {
		
		private File file;
		private boolean selected;
		
		/**
		 * Instantiates a new file descriptor.
		 */
		public FileDescriptor(File file) {
			this.setFile(file);
		}
		
		
		public File getFile() {
			return file;
		}
		public void setFile(File file) {
			this.file = file;
		}
		
		public boolean isDirectory() {
			return this.getFile().isDirectory();
		}
		
		public boolean isSelected() {
			return selected;
		}
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			if (this.getFile()==null) {
				return "Unknown";
			}
			return this.getFile().getName();
		}
	}

}
