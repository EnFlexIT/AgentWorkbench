package de.enflexit.common.transfer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
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

/**
 * Class for handling archive files. Currently able to handle zip and tar.gz files.
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 *
 */
public class ArchiveFileHandler {

	/**
	 * The Enum ArchiveFormat.
	 */
	public enum ArchiveFormat {
		ZIP, TAR_GZ;
	}

	private ArchiveFormat archiveFormat;
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
		} else {
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

		if (sourceFolder.isDirectory() == false) {
			System.err.println(sourceFolder.getName() + " is not a folder!");
			return false;
		}

		this.archiveFormat = archiveFormat;

		ArchiveOutputStream outputStream = null;

		try {
			outputStream = this.createArchiveOutputStream(targetFile);
			this.addFileToArchive(sourceFolder, sourceFolder, null, outputStream);
			success = true;
		} catch (IOException e) {
			System.err.println("Error creating archive from folder " + sourceFolder);
			e.printStackTrace();
			success = false;
		} finally {
			if (outputStream != null) {
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
	public boolean decompressFolder(File archiveFile, File targetFolder) {
		ArchiveFormat archiveFormat = this.determineArchiveFormat(archiveFile);
		if (archiveFormat != null) {
			return this.decompressFolder(archiveFile, targetFolder, this.determineArchiveFormat(archiveFile));
		} else {
			System.err.println("Archive format could not be determined for file " + archiveFile.getName());
			return false;
		}
	}

	/**
	 * Decompresses an archive file to the specified target folder.
	 *
	 * @param archiveFile the archive file
	 * @param targetFolder the target folder
	 * @param archiveFormat the archive format
	 * @return true, if successful
	 */
	public boolean decompressFolder(File archiveFile, File targetFolder, ArchiveFormat archiveFormat) {

		this.archiveFormat = archiveFormat;
		boolean success;

		ArchiveInputStream inputStream = null;
		try {

			// --- Get an input stream for the current archive format
			inputStream = this.createArchiveInputStream(archiveFile);

			// --- Handle the archive entries -----------
			ArchiveEntry entry;

			while ((entry = inputStream.getNextEntry()) != null) {
				Path targetPath = targetFolder.toPath().resolve(entry.getName());

				if (entry.isDirectory() == true) {

					// --- Folders ---------------
					Files.createDirectories(targetPath);

				} else {

					// --- Files ---------------
					int count;
					byte data[] = new byte[this.bufferSize];

					FileOutputStream fileOutputStream = new FileOutputStream(targetPath.toFile());
					BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, this.bufferSize);

					while ((count = inputStream.read(data, 0, this.bufferSize)) != -1) {
						bufferedOutputStream.write(data, 0, count);
					}

					bufferedOutputStream.close();

				}

			}

			success = true;

		} catch (IOException e) {

			System.err.println("Error extracting archive file " + archiveFile.getName() + " to " + targetFolder);
			e.printStackTrace();
			success = false;

		} finally {

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					System.err.println("Error closing input stream!");
					e.printStackTrace();
				}

			}
		}

		return success;
	}

	/**
	 * Adds a folder to an archive. The archive format is determined from the file name.
	 *
	 * @param folder the folder
	 * @param archiveFile the archive file
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean appendFolderToArchive(File folder, File archiveFile, File targetFile) throws IOException {
		return this.appendFolderToArchive(folder, archiveFile, targetFile, null);
	}

	/**
	 * Appends a folder to an existing archive. The archive format is determined from the file name.
	 *
	 * @param folder the folder to add
	 * @param archiveFile the archive file the original archive
	 * @param targetFile the new extended archive
	 * @param pathInsideArchive the path inside archive for the new content
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean appendFolderToArchive(File folder, File archiveFile, File targetFile, String pathInsideArchive) throws IOException {
		ArchiveFormat archiveFormat = this.determineArchiveFormat(archiveFile);
		if (archiveFormat != null) {
			return this.appendFolderToArchive(folder, archiveFile, targetFile, pathInsideArchive, this.determineArchiveFormat(archiveFile));
		} else {
			System.err.println("Archive format could not be determined for file " + archiveFile.getName());
			return false;
		}
	}

	/**
	 * Appends a folder to an existing archive.
	 *
	 * @param folder the folder to add
	 * @param archiveFile the archive file the original archive
	 * @param targetFile the new extended archive
	 * @param pathInsideArchive the path inside archive for the new content
	 * @param archiveFormat the archive format
	 * @return true, if successful
	 */
	public boolean appendFolderToArchive(File folder, File archiveFile, File targetFile, String pathInsideArchive, ArchiveFormat archiveFormat) {

		this.archiveFormat = archiveFormat;

		boolean success;
		ArchiveOutputStream outputStream = null;
		try {

			outputStream = this.createArchiveOutputStream(targetFile);
			this.copyArchiveContents(archiveFile, outputStream);
			this.addFileToArchive(folder, folder, pathInsideArchive, outputStream);
			success = true;

		} catch (IOException e) {

			System.err.println("Error appending folder " + folder.getName() + " to archive " + archiveFile.getName());
			e.printStackTrace();
			success = false;

		} finally {

			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					System.err.println("Error closing output stream");
					e.printStackTrace();
				}
			}

		}

		return success;
	}

	/**
	 * Appends multiple folders to an existing archive. The archive format is determines from the file extension.
	 * @param archiveSourceFile The original archive
	 * @param archiveTargetFile The resulting extended archive
	 * @param folders {@link HashMap} containing the folders to add as keys and their paths inside the archive as values
	 * @param deleteAfterwards if true, the folders will be deleted after integrating them into the archive
	 * @return successful?
	 */
	public boolean appendFoldersToArchive(File archiveSourceFile, File archiveTargetFile, HashMap<File, String> folders, boolean deleteAfterwards) {
		return this.appendFoldersToArchive(archiveSourceFile, archiveTargetFile, folders, this.determineArchiveFormat(archiveSourceFile), deleteAfterwards);
	}

	/**
	 * Appends multiple folders to an existing archive
	 * @param archiveSourceFile The original archive
	 * @param archiveTargetFile The resulting extended archive
	 * @param folders {@link HashMap} containing the folders to add as keys and their paths inside the archive as values
	 * @param archiveFormat The archive format
	 * @param deleteAfterwards if true, the folders will be deleted after integrating them into the archive
	 * @return successful?
	 */
	public boolean appendFoldersToArchive(File archiveSourceFile, File archiveTargetFile, HashMap<File, String> folders, ArchiveFormat archiveFormat, boolean deleteAfterwards) {
		this.archiveFormat = archiveFormat;

		boolean success;
		ArchiveOutputStream outputStream = null;
		try {
			
			RecursiveFolderDeleter deleter = null;
			if (deleteAfterwards == true) {
				deleter = new RecursiveFolderDeleter();
			}

			outputStream = this.createArchiveOutputStream(archiveTargetFile);
			this.copyArchiveContents(archiveSourceFile, outputStream);
			for (File folder : folders.keySet()) {
				String pathInsideArchive = folders.get(folder);
				this.addFileToArchive(folder, folder, pathInsideArchive, outputStream);
				if (deleteAfterwards == true) {
					deleter.deleteFolder(folder.toPath());
				}
			}
			success = true;

		} catch (IOException e) {

			System.err.println("Error appending folders to archive " + archiveSourceFile.getName());
			e.printStackTrace();
			success = false;

		} finally {

			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					System.err.println("Error closing output stream");
					e.printStackTrace();
				}
			}

		}

		return success;
	}

	/**
	 * Determines the archive format of the given file based on its suffix.
	 *
	 * @param archiveFile the archive file
	 * @return the archive format, null if not recognized or not supported
	 */
	private ArchiveFormat determineArchiveFormat(File archiveFile) {
		String fileName = archiveFile.getName();
		String suffix = fileName.substring(fileName.indexOf('.') + 1);
		if (fileName.endsWith("zip") || suffix.endsWith("agui")) {
			return ArchiveFormat.ZIP;
		} else if (suffix.endsWith("tar.gz")) {
			return ArchiveFormat.TAR_GZ;
		} else {
			return null;
		}
	}

	/**
	 * Creates an archive input stream for the current ArchiveFormat.
	 *
	 * @param archiveFile the archive file
	 * @return the archive input stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private ArchiveInputStream createArchiveInputStream(File archiveFile) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(archiveFile);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

		ArchiveInputStream archiveInputStream = null;
		if (this.archiveFormat == ArchiveFormat.ZIP) {
			archiveInputStream = new ZipArchiveInputStream(bufferedInputStream);
		} else if (this.archiveFormat == ArchiveFormat.TAR_GZ) {
			GzipCompressorInputStream gzipInputStream = new GzipCompressorInputStream(bufferedInputStream);
			archiveInputStream = new TarArchiveInputStream(gzipInputStream);
		}
		return archiveInputStream;
	}

	/**
	 * Creates an archive output stream for the current ArchiveFormat.
	 *
	 * @param archiveFile the archive file
	 * @return the archive output stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private ArchiveOutputStream createArchiveOutputStream(File archiveFile) throws IOException {

		FileOutputStream fileOutputStream = new FileOutputStream(archiveFile);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

		ArchiveOutputStream archiveOutputStream = null;
		if (this.archiveFormat == ArchiveFormat.ZIP) {
			archiveOutputStream = new ZipArchiveOutputStream(bufferedOutputStream);
		} else if (this.archiveFormat == ArchiveFormat.TAR_GZ) {
			GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(bufferedOutputStream);
			archiveOutputStream = new TarArchiveOutputStream(gzipOutputStream);
			((TarArchiveOutputStream) archiveOutputStream).setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);
		}

		return archiveOutputStream;

	}

	/**
	 * Creates an archive entry of the current ArchiveFormat for the given file.
	 *
	 * @param baseDir The base directory of the zipped content. Paths inside the archive will be relative to this.
	 * @param file the file to be added to the archive
	 * @param pathInsideArchive the path inside the archive
	 * @return the archive entry
	 */
	private ArchiveEntry createArchiveEntry(File baseDir, File file, String pathInsideArchive) {
		ArchiveEntry archiveEntry = null;
		String entryName = baseDir.getParentFile().toURI().relativize(file.toURI()).getPath();
		if (pathInsideArchive != null) {
			entryName = pathInsideArchive + File.separator + entryName;
		}
		if (this.archiveFormat == ArchiveFormat.ZIP) {
			archiveEntry = new ZipArchiveEntry(file, entryName);
		} else if (this.archiveFormat == ArchiveFormat.TAR_GZ) {
			archiveEntry = new TarArchiveEntry(file, entryName);
		}
		return archiveEntry;
	}

	/**
	 * Adds a the file to archive. If the file is a folder, its contents will be added recursively.
	 *
	 * @param baseDir the base directory of the zipped content. Paths inside the archive will be relative to this.
	 * @param file the file to be added
	 * @param pathInsideArchive the path inside the archive
	 * @param archiveOutputStream the archive output stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void addFileToArchive(File baseDir, File file, String pathInsideArchive, ArchiveOutputStream archiveOutputStream) throws IOException {

		if (file.isDirectory() == true) {

			ArchiveEntry archiveEntry = this.createArchiveEntry(baseDir, file, pathInsideArchive);
			archiveOutputStream.putArchiveEntry(archiveEntry);
			archiveOutputStream.closeArchiveEntry();

			// --- If it is a folder, add all the contents to the archive ---------

			List<File> filesList = Arrays.asList(file.listFiles());
			if (filesList != null) {
				for (File currFile : filesList) {
					this.addFileToArchive(baseDir, currFile, pathInsideArchive, archiveOutputStream);
				}
			}
		} else {

			// --- If it is a file, create and add an archive entry ----------

			ArchiveEntry archiveEntry = this.createArchiveEntry(baseDir, file, pathInsideArchive);
			archiveOutputStream.putArchiveEntry(archiveEntry);

			// --- Write the actual data to the archive -----------------------

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

	/**
	 * Copies the contents of an archive file to the specified output stream
	 * 
	 * @param archiveFile the archive file
	 * @param outputStream the output stream
	 * @throws IOException error copying the archive contents
	 */
	private void copyArchiveContents(File archiveFile, ArchiveOutputStream outputStream) throws IOException {
		ArchiveInputStream inputStream = this.createArchiveInputStream(archiveFile);

		ArchiveEntry entry;
		byte[] buffer = new byte[this.bufferSize];

		while ((entry = inputStream.getNextEntry()) != null) {
			outputStream.putArchiveEntry(entry);
			int count;
			while ((count = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, count);
			}
			outputStream.closeArchiveEntry();
		}
	}

}
