package de.enflexit.common.transfer;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * This class can be used to recursively delete folder structures
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 *
 */
public class RecursiveFolderDeleter {
	
	/**
	 * Deletes the specified folder, including all its contents and subfolders
	 * @param path The folder to be deleted
	 * @throws IOException Deleting failed
	 */
	public void deleteFolder(String path) throws IOException {
		deleteFolder(new File(path).toPath());
	}
	
	/**
	 * Deletes the specified folder, including all its contents and subfolders
	 * @param path The folder to be deleted
	 * @throws IOException Deleting failed
	 */
	public void deleteFolder(Path path) throws IOException {
		Files.walkFileTree(path, new DeleteFolderVisitor());
	}
	
	/**
	 * Visitor object to recursively delete directories.
	 * Based on example code by Bill Bejeck at http://codingjunkie.net/java-7-copy-move/
	 *
	 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
	 *
	 */
	private class DeleteFolderVisitor extends SimpleFileVisitor<Path> {

	    @Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
	        try {
	        	Files.delete(file);
	        } catch (IOException ioEx) {
	        	System.err.println(RecursiveFolderDeleter.this.getClass().getSimpleName() + " - Error deleteing file: " + file.getFileName());
	        	throw ioEx;
	        }
	        return FileVisitResult.CONTINUE;
	    }

	    @Override
	    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
	        if (exc==null) {
	        	try {
	        		Files.delete(dir);
				} catch (IOException ioEx) {
					System.err.println(RecursiveFolderDeleter.this.getClass().getSimpleName() + " - Error deleteing diretory: " + dir.getFileName());
					throw ioEx;
				}
	            return FileVisitResult.CONTINUE;
	        }
	        throw exc;
	    }
	}
}
