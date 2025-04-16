package de.enflexit.common.transfer;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * This class can be used to recursively copy folder structures, optionally excluding specific subfolders.
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class RecursiveFolderCopier {
	
	private StandardCopyOption copyOption = StandardCopyOption.REPLACE_EXISTING;
	
	/**
	 * Copy a folder recursively.
	 *
	 * @param sourcePath the source path
	 * @param destPath the destination path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void copyFolder(String sourcePath, String destPath) throws IOException {
		this.copyFolder(sourcePath, destPath, null);
	}
	
	/**
	 * Copy a folder recursively, excluding the subfolders specified in skipList.
	 *
	 * @param sourcePath the source path
	 * @param destPath the destination path
	 * @param skipList list of subfolders to be excluded
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void copyFolder(String sourcePath, String destPath, String[] skipList) throws IOException {
		
		// --- Create Path objects from the Strings --------
		Path source = new File(sourcePath).toPath();
		Path dest = new File(destPath).toPath();
		List<Path> skip = null;
		if(skipList!=null) {
			skip = new ArrayList<>();
			for(int i=0; i<skipList.length; i++) {
				skip.add(new File(skipList[i]).toPath());
			}
		}
		
		this.copyFolder(source, dest, skip);
	}
	
	/**
	 * Copy a folder recursively.
	 *
	 * @param sourcePath the source path
	 * @param destPath the destination path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void copyFolder(Path sourcePath, Path destPath) throws IOException {
		this.copyFolder(sourcePath, destPath, null);
	}
	
	/**
	 * Copy a folder recursively, excluding the subfolders specified in skipList.
	 *
	 * @param sourcePath the source path
	 * @param destPath the destination path
	 * @param skipList list of subfolders to be excluded
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void copyFolder(Path sourcePath, Path destPath, List<Path> skipList) throws IOException {
		Files.walkFileTree(sourcePath, new CopyFolderVisitor(sourcePath, destPath, this.copyOption, skipList));
	}
	
	
	
	/**
	 * Visitor object to recursively copy directories.
	 * Based on Bill Bejeck's example code at http://codingjunkie.net/java-7-copy-move/
	 *
	 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
	 *
	 */
	private class CopyFolderVisitor extends SimpleFileVisitor<Path>{
		
		private Path fromPath;
    	private Path toPath;
    	private StandardCopyOption copyOption;
    	private List<Path> skipList;
	    
	    /**
    	 * Creates a new {@link CopyFolderVisitor} that recursively copies sourcePath to destPath, excluding the {@link Path}s in skipList.
    	 *
    	 * @param sourcePath The source directory
    	 * @param destPath The target directory
    	 * @param copyOption The {@link StandardCopyOption}
    	 * @param skipList List of {@link Path}s to exclude
    	 */
	    public CopyFolderVisitor(Path sourcePath, Path destPath, StandardCopyOption copyOption, List<Path> skipList) {
	    	this.fromPath = sourcePath;
	    	this.toPath = destPath;
	    	this.copyOption = copyOption;
	    	this.skipList = skipList;
	    }

	    
    	/* (non-Javadoc)
	     * @see java.nio.file.SimpleFileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
	     */
	    @Override
	    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    		
    		// --- Check if the current folder is in the skipList ------
	    	if(this.skipList != null){
	    		if (skipList.contains(dir)) {
	    			return FileVisitResult.SKIP_SUBTREE;
		    	}
	    	}
	    	
	    	// --- If not, create a corresponding folder in the target path ---------------
	        Path targetPath = toPath.resolve(fromPath.relativize(dir));
	        if(!Files.exists(targetPath)){
	            Files.createDirectory(targetPath);
	        }
	        
	        return FileVisitResult.CONTINUE;
	    }

	    /* (non-Javadoc)
    	 * @see java.nio.file.SimpleFileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
    	 */
    	@Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    		
    		// --- Check if the current file is in the skipList ------
    		if(this.skipList != null){
		    	if (skipList.contains(file)) {
	    			return FileVisitResult.SKIP_SUBTREE;
		    	}
	    	}
    		
    		// --- If not, copy it ---------------
    		Files.copy(file, toPath.resolve(fromPath.relativize(file)), copyOption);
	        return FileVisitResult.CONTINUE;
	    }
	    
	}

}
