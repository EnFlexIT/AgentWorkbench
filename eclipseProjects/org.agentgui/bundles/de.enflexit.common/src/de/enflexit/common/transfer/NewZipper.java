package de.enflexit.common.transfer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class NewZipper.
 */
public class NewZipper {
	
	/**
	 * The Enum ArchiveFormat.
	 */
	public enum ArchiveFormat{
		ZIP, TAR_GZ;
	}
	
	/** The archive format. */
	private ArchiveFormat archiveFormat;
	
	/** The buffer size. */
	private int bufferSize = 2048;
	
	/**
	 * Compresses a folder. The archive format is determined from the target file name.
	 *
	 * @param sourceFolder the source folder
	 * @param targetFile the target file
	 * @return true, if successful
	 */
	public boolean compressFolder(File sourceFolder, File targetFile) {
		ArchiveFormat archiveFormat = this.determineArchiveFormat(targetFile);
		if (archiveFormat != null) {
			return this.compressFolder(sourceFolder, targetFile, archiveFormat);
		}else {
			System.err.println("Archive format could not be determined for file " + targetFile.getName());
			return false;
		}
	}

	/**
	 * Compresses a folder.
	 *
	 * @param sourceFolder the source folder
	 * @param targetFile the target file
	 * @param archiveFormat the archive format
	 * @return true, if successful
	 */
	public boolean compressFolder(File sourceFolder, File targetFile, ArchiveFormat archiveFormat) {
		
		boolean success;
		
		if(sourceFolder.isDirectory() == false) {
			System.err.println(sourceFolder.getName() + " is not a folder!");
			return false;
		}
		
		this.archiveFormat = archiveFormat;
		
		ArchiveOutputStream outputStream = null;
		
		try {
			outputStream = this.createArchiveOutputStream(targetFile);
			this.addFolderToArchive(sourceFolder, sourceFolder, outputStream);
			success = true;
		} catch (IOException e) {
			System.err.println("Error creating archive from folder " + sourceFolder);
			e.printStackTrace();
			success = false;
		} finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					System.err.println("Error closing archive output stream!");
					e.printStackTrace();
				}
			}
		}
		
		return success;
		
	}
	
	/**
	 * Decompresses an archive. The archive format is determined from the file name.
	 *
	 * @param archiveFile the archive file
	 * @param targetFolder the target folder
	 * @return true, if successful
	 */
	public boolean decompressFolder(File archiveFile, String targetFolder) {
		ArchiveFormat archiveFormat = this.determineArchiveFormat(archiveFile);
		if (archiveFormat != null) {
			return this.decompressFolder(archiveFile, targetFolder, this.determineArchiveFormat(archiveFile));
		} else {
			System.err.println("Archive format could not be determined for file " + archiveFile.getName());
			return false;
		}
	}
	
	/**
	 * Decompresses an archive.
	 *
	 * @param archiveFile the archive file
	 * @param targetFolder the target folder
	 * @param archiveFormat the archive format
	 * @return true, if successful
	 */
	public boolean decompressFolder(File archiveFile, String targetFolder, ArchiveFormat archiveFormat) {
		this.archiveFormat = archiveFormat;
		//TODO implement
		return false;
	}
	
	/**
	 * Adds a folder to an archive. The archive format is determined from the file name.
	 *
	 * @param folder the folder
	 * @param archiveFile the archive file
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean addFolderToArchive(File folder, File archiveFile) throws IOException {
		ArchiveFormat archiveFormat = this.determineArchiveFormat(archiveFile);
		if (archiveFormat != null) {
			return this.addFolderToArchive(folder, archiveFile, this.determineArchiveFormat(archiveFile));
		} else {
			System.err.println("Archive format could not be determined for file " + archiveFile.getName());
			return false;
		}
	}
	
	/**
	 * Adds a folder to an archive.
	 *
	 * @param folder the folder
	 * @param archiveFile the archive file
	 * @param archiveFormat the archive format
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean addFolderToArchive(File folder, File archiveFile, ArchiveFormat archiveFormat) throws IOException {
		this.archiveFormat = archiveFormat;
		//TODO implement
		return false;
	}
	
	/**
	 * Determine archive format.
	 *
	 * @param archiveFile the archive file
	 * @return the archive format
	 */
	private ArchiveFormat determineArchiveFormat(File archiveFile) {
		String fileName = archiveFile.getName();
		String suffix = fileName.substring(fileName.indexOf('.')+1);
		if(fileName.endsWith("zip") || suffix.endsWith("agui")) {
			return ArchiveFormat.ZIP;
		} else if (suffix.endsWith("tar.gz")){
			return ArchiveFormat.TAR_GZ;
		} else {
			return null;
		}
	}
	
	/**
	 * Creates the archive input stream.
	 *
	 * @param archiveFile the archive file
	 * @return the archive input stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private ArchiveInputStream createArchiveInputStream(File archiveFile) throws IOException {
		FileInputStream fIn = new FileInputStream(archiveFile);
		BufferedInputStream bIn = new BufferedInputStream(fIn);
		ArchiveInputStream aIn = null;
		if (this.archiveFormat == ArchiveFormat.ZIP) {
			aIn = new ZipArchiveInputStream(bIn);
		} else if (this.archiveFormat == ArchiveFormat.TAR_GZ) {
			GzipCompressorInputStream gIn = new GzipCompressorInputStream(bIn);
			TarArchiveInputStream tIn = new TarArchiveInputStream(gIn);
		}
		return aIn;
	}
	
	/**
	 * Creates the archive output stream.
	 *
	 * @param archiveFile the archive file
	 * @return the archive output stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private ArchiveOutputStream createArchiveOutputStream(File archiveFile) throws IOException {
		
		ArchiveOutputStream aOut = null;
		
		FileOutputStream fOut = new FileOutputStream(archiveFile);
		BufferedOutputStream bOut = new BufferedOutputStream(fOut);

		if (this.archiveFormat == ArchiveFormat.ZIP) {
			aOut = new ZipArchiveOutputStream(bOut);
		} else if (this.archiveFormat == ArchiveFormat.TAR_GZ) {
			GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(bOut);
			aOut = new TarArchiveOutputStream(gzOut);
			((TarArchiveOutputStream)aOut).setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);
		}
		  
		return aOut;
		
	}
	
	/**
	 * Creates the archive entry.
	 *
	 * @param baseDir the base dir
	 * @param file the file
	 * @return the archive entry
	 */
	private ArchiveEntry createArchiveEntry(File baseDir, File file) {
		ArchiveEntry archiveEntry = null;
		if (this.archiveFormat == ArchiveFormat.ZIP) {
			archiveEntry = new ZipArchiveEntry(file, baseDir.getParentFile().toURI().relativize(file.toURI()).getPath());
		} else if (this.archiveFormat == ArchiveFormat.TAR_GZ) {
			archiveEntry = new TarArchiveEntry(file, baseDir.getParentFile().toURI().relativize(file.toURI()).getPath());
		}
		return archiveEntry;
	}
	
	/**
	 * Adds the folder to archive.
	 *
	 * @param baseDir the base dir
	 * @param folder the folder
	 * @param archiveOutputStream the archive output stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void addFolderToArchive(File baseDir, File folder, ArchiveOutputStream archiveOutputStream) throws IOException {
		List<File> filesList = Arrays.asList(folder.listFiles());
		
		if(filesList != null) {
			for (File file : filesList) {
				if (file.isDirectory() == true) {
					this.addFolderToArchive(baseDir, file, archiveOutputStream);
				} else {
					this.addFileToArchive(baseDir, file, archiveOutputStream);
				}
			}
		}
		
	}
	
	/**
	 * Adds the file to archive.
	 *
	 * @param baseDir the base dir
	 * @param file the file
	 * @param archiveOutputStream the archive output stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void addFileToArchive(File baseDir, File file, ArchiveOutputStream archiveOutputStream) throws IOException {
		
		ArchiveEntry archiveEntry = this.createArchiveEntry(baseDir, file);
		archiveOutputStream.putArchiveEntry(archiveEntry);
		
		FileInputStream fi = new FileInputStream(file);
		BufferedInputStream sourceStream = new BufferedInputStream(fi, bufferSize);
		
		int count;
		byte data[] = new byte[bufferSize];
		while ((count = sourceStream.read(data, 0, bufferSize)) != -1) {
			archiveOutputStream.write(data, 0, count);
		}
		sourceStream.close();
		
		archiveOutputStream.closeArchiveEntry();
		
	}
		
}
