package de.enflexit.common.transfer;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class can be used to recursively delete folder structures
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class RecursiveFolderDeleter {
	
	/**
	 * Deletes the specified folder, including all its contents and subfolders
	 * @param path The folder to be deleted
	 * @throws IOException Deleting failed
	 */
	public void deleteFolder(String path) throws IOException {
		deleteFolder(new File(path).toPath(), null);
	}
	/**
	 * Deletes the specified folder, including all its contents and subfolders, except those specified in the excludeList
	 * @param path The folder to be deleted
	 * @param excludeList List of files/folders that should not be deleted
	 * @throws IOException Deleting failed
	 */
	public void deleteFolder(String path, String[] excludeList) throws IOException {
		Path folderToDelete = new File(path).toPath();
		List<Path> exclude = null;
		if(excludeList!=null) {
			exclude = new ArrayList<>();
			for(int i=0; i<excludeList.length; i++) {
				exclude.add(new File(excludeList[i]).toPath());
			}
		}
		deleteFolder(folderToDelete, exclude);
	}
	
	/**
	 * Deletes the specified folder, including all its contents and subfolders
	 * @param path The folder to be deleted
	 * @throws IOException Deleting failed
	 */
	public void deleteFolder(Path path) throws IOException {
		this.deleteFolder(path, null);
	}
	
	
	/**
	 * Deletes the specified folder, including all its contents and subfolders, except those specified in the excludeList
	 * @param path The folder to be deleted
	 * @param excludeList List of files/folders that should not be deleted 
	 * @throws IOException Deleting failed
	 */
	public void deleteFolder(Path path, List<Path> excludeList) throws IOException {
		Files.walkFileTree(path, new DeleteFolderVisitor(excludeList));
	}
	
	/**
	 * Visitor object to recursively delete directories.
	 * Based on example code by Bill Bejeck at http://codingjunkie.net/java-7-copy-move/
	 *
	 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
	 *
	 */
	private class DeleteFolderVisitor extends SimpleFileVisitor<Path> {
		
		private List<Path> excludeList;

	    /**
    	 * Instantiates a new delete folder visitor.
    	 * @param excludeList the exclude list
    	 */
    	public DeleteFolderVisitor(List<Path> excludeList) {
	    	this.excludeList = processExcludeList(excludeList);
		}

		/* (non-Javadoc)
		 * @see java.nio.file.SimpleFileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
		 */
		@Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
	        try {
	        	if (this.excludeList.contains(file)==false) {
	        		Files.delete(file);
	        	}
	        } catch (IOException ioEx) {
	        	System.err.println(RecursiveFolderDeleter.this.getClass().getSimpleName() + " - Error deleting file: " + file.getFileName());
//	        	throw ioEx;
	        }
	        return FileVisitResult.CONTINUE;
	    }

	    /* (non-Javadoc)
    	 * @see java.nio.file.SimpleFileVisitor#postVisitDirectory(java.lang.Object, java.io.IOException)
    	 */
    	@Override
	    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
	        if (exc==null) {
	        	try {
	        		if (this.excludeList.contains(dir)==false) {
	        			Files.delete(dir);
	        		}
				} catch (IOException ioEx) {
					System.err.println(RecursiveFolderDeleter.this.getClass().getSimpleName() + " - Error deleting diretory: " + dir.getFileName());
//					throw ioEx;
				}
	            return FileVisitResult.CONTINUE;
	        }
	        throw exc;
	    }
	
	}

	
	/**
	 * Generates a new exclude list that also contains all parents of the excluded paths
	 * @param originalList the original list
	 * @return the processed list
	 */
	private List<Path> processExcludeList(List<Path> originalList) {
		
		List<Path> processedList = new ArrayList<>();
		if (originalList!=null) {
			
			for (int i=0; i<originalList.size(); i++) {
				// --- Add the original path to the list ----------------------
				Path path = originalList.get(i);
				if (path.toFile().exists()==true) {
					processedList.add(path);
	
					// --- Include contents of directories ------------------------
					if (path.toFile().isDirectory()==true) {
						try {
							List<Path> contents = Files.walk(path, FileVisitOption.FOLLOW_LINKS).collect(Collectors.toList());
							// --- Remove the directory itself --------------------
							contents.remove(0);
							// --- Add the contents to the exclude list -----------
							processedList.addAll(contents);
							
						} catch (IOException e) {
							System.err.println(this.getClass().getSimpleName() + ": Error listing contents of " + path);
							e.printStackTrace();
						}
					}
					
					// --- Add all parents to the list ------------------
					while (path.getParent()!=null) {
						path = path.getParent();
						if (processedList.contains(path)==false) {
							processedList.add(path);
						}
					}
				}
			} // end for 

		}
		return processedList;
	}
	
}
