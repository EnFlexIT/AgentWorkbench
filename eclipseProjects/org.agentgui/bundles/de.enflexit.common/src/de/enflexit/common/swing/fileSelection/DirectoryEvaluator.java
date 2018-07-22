package de.enflexit.common.swing.fileSelection;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JCheckBox;
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

	private File rootDirectory; 
	private ArrayList<FileDescriptor> filesFound;
	private ArrayList<DirectoryEvaluatorListener> directoryEvaluatorListener;
	
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
	 * Sets the root directory of the evaluator.
	 * @param rootDirectory the new root directory
	 */
	public void setRootDirectory(File rootDirectory) {
		this.rootDirectory = rootDirectory;
		if (this.rootDirectory!=null) {
			this.getFilesFound().clear();
			this.removeAllTreeNodes();
			this.getRootNode().setUserObject(new FileDescriptor(rootDirectory));
			this.evaluateDirectoryInThread(this.rootDirectory, this.getRootNode());
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
	 * Evaluate directory in thread.
	 *
	 * @param rootDirectory the directory
	 * @param parentNode the parent node
	 */
	private void evaluateDirectoryInThread(final File rootDirectory, final DefaultMutableTreeNode rootNode) {
		
		Thread searcher = new Thread(new Runnable() {
			@Override
			public void run() {
				DirectoryEvaluator.this.evaluateDirectory(rootDirectory, rootNode);
				DirectoryEvaluator.this.fireEvaluationWasFinalized();
			}
		});
		searcher.setName(DirectoryEvaluator.this.getClass().getSimpleName());
		searcher.start();
	}
	
	/**
	 * Evaluates the specified directory for file and directories.
	 *
	 * @param directory the directory to evaluate
	 * @param parentNode the parent node
	 */
	private void evaluateDirectory(File directory, DefaultMutableTreeNode parentNode) {
		
		File[] fileArray = directory.listFiles();
		
		// ------------------------------------------------
		// --- Sort the file / directory list first -------
		Arrays.sort(fileArray, new Comparator<File>() {
			@Override
			public int compare(File file1, File file2) {
				if (file1.isDirectory()==true & file2.isDirectory()==true) {
					return file1.getName().compareTo(file2.getName());
				} else if (file1.isDirectory()==false & file2.isDirectory()==true) {
					return 1;
				} else if (file1.isDirectory()==true & file2.isDirectory()==false) {
					return -1;
				} 
				return file1.getName().compareTo(file2.getName());
			}
		});
		
		// ------------------------------------------------
		// --- Add nodes and search deeper ---------------- 
		for (int i = 0; i < fileArray.length; i++) {
			
			File file = fileArray[i];
			String relativePath = this.getRelativePathToRoot(file);
			
			FileDescriptor fileDescriptor = new FileDescriptor(file);
			fileDescriptor.setRelativePathtoRoot(relativePath);
			
			this.getFilesFound().add(fileDescriptor);

			DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(fileDescriptor);
			parentNode.add(fileNode);

			if (file.isDirectory()) {
				this.evaluateDirectory(file, fileNode);
			}
		}
	}
	/**
	 * Returns the relative path to root path.
	 *
	 * @param file the file
	 * @return the relative path to root
	 */
	private String getRelativePathToRoot(File file) {
		String relativePath = file.getAbsolutePath().substring(this.getRootDirectory().getAbsolutePath().length());
		relativePath = relativePath.replace(File.separator, "/");
		return relativePath; 
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
	 * Returns the file list as specified. All selected or all not-selected files can be returned.
	 *
	 * @param getSelectedFiles set true if you want to return selected files only; otherwise false
	 * @return the file list as specified
	 */
	public ArrayList<File> getFileList(boolean getSelectedFiles) {
		ArrayList<File> filesList = new ArrayList<>();
		for (int i = 0; i < this.getFilesFound().size(); i++) {
			FileDescriptor fd = this.getFilesFound().get(i);
			if (fd.isSelected()==getSelectedFiles) {
				filesList.add(fd.getFile());
			}
		}
		return filesList;
	}
	
	
	/**
	 * Returns the root node of the tree model.
	 * @return the root node
	 */
	private DefaultMutableTreeNode getRootNode() {
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
	 * Return the registered DirectoryEvaluatorListener.
	 * @return the file tree listener
	 */
	public ArrayList<DirectoryEvaluatorListener> getDirectoryEvaluatorListener() {
		if (directoryEvaluatorListener==null) {
			directoryEvaluatorListener = new ArrayList<>();
		}
		return directoryEvaluatorListener;
	}
	/**
	 * Adds the specified DirectoryEvaluatorListener.
	 * @param directoryEvaluatorListener the directory evaluator listener
	 */
	public void addDirectoryEvaluatorListener(DirectoryEvaluatorListener directoryEvaluatorListener) {
		if (this.getDirectoryEvaluatorListener().contains(directoryEvaluatorListener)==false) {
			this.getDirectoryEvaluatorListener().add(directoryEvaluatorListener);
		}
	}
	/**
	 * Removes the specified DirectoryEvaluatorListener.
	 * @param directoryEvaluatorListener the directory evaluator listener
	 */
	public void removeDirectoryEvaluatorListener(DirectoryEvaluatorListener directoryEvaluatorListener) {
		this.getDirectoryEvaluatorListener().remove(directoryEvaluatorListener);
	}
	/**
	 * Fires that the evaluation was finalized.
	 */
	private void fireEvaluationWasFinalized() {
		for (int i = 0; i < this.getDirectoryEvaluatorListener().size(); i++) {
			this.getDirectoryEvaluatorListener().get(i).onEvaluationWasFinalized();
		}
	}
	
	
	/**
	 * The Class FileDescriptor.
	 */
	public class FileDescriptor {
		
		private File file;
		private String relativePathtoRoot;
		private JCheckBox checkBox;
		
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
		
		public String getRelativePathtoRoot() {
			return relativePathtoRoot;
		}
		public void setRelativePathtoRoot(String relativePathtoRoot) {
			this.relativePathtoRoot = relativePathtoRoot;
		}
		
		public boolean isDirectory() {
			return this.getFile().isDirectory();
		}
		
		public JCheckBox getCheckBox() {
			if (checkBox==null) {
				checkBox = new JCheckBox();
			}
			return checkBox;
		}
		
		public boolean isSelected() {
			return this.getCheckBox().isSelected();
		}
		public void setSelected(boolean isSelected) {
			this.getCheckBox().setSelected(isSelected);
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
