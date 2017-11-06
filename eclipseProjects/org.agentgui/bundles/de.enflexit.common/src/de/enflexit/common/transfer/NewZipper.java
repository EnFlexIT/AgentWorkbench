package de.enflexit.common.transfer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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

public class NewZipper {
	
	public enum ArchiveFormat{
		ZIP, TAR_GZ;
	}
	
	private ArchiveFormat archiveFormat;

	public boolean compressFolder(File sourceFolder, File targetFile, ArchiveFormat archiveFormat) {
		
		if(sourceFolder.isDirectory() == false) {
			System.err.println(sourceFolder.getName() + " is not a folder!");
			return false;
		}
		
		this.archiveFormat = archiveFormat;
		
		ArchiveOutputStream outputStream = null;
		try {
			outputStream = this.createArchiveOutputStream(targetFile);
		} catch (IOException e) {
			System.err.println("Error creating output stream for file " + targetFile.getName());
			e.printStackTrace();
			return false;
		}
		
		List<File> filesList = new ArrayList<>(Arrays.asList(sourceFolder.listFiles()));
		
		//TODO implement
		return true;
	}
	
	public boolean decompressFolder(File archiveFile, String targetFolder) {
		ArchiveFormat archiveFormat = this.determineArchiveFormat(archiveFile);
		if (archiveFormat != null) {
			return this.decompressFolder(archiveFile, targetFolder, this.determineArchiveFormat(archiveFile));
		} else {
			System.err.println("Archive format could not be determined for file " + archiveFile.getName());
			return false;
		}
	}
	
	public boolean decompressFolder(File archiveFile, String targetFolder, ArchiveFormat archiveFormat) {
		this.archiveFormat = archiveFormat;
		//TODO implement
		return false;
	}
	
	public boolean addFolderToArchive(String folder, File archiveFile) {
		ArchiveFormat archiveFormat = this.determineArchiveFormat(archiveFile);
		if (archiveFormat != null) {
			return this.addFolderToArchive(folder, archiveFile, this.determineArchiveFormat(archiveFile));
		} else {
			System.err.println("Archive format could not be determined for file " + archiveFile.getName());
			return false;
		}
	}
	
	public boolean addFolderToArchive(String folder, File archiveFile, ArchiveFormat archiveFormat) {
		this.archiveFormat = archiveFormat;
		//TODO implement
		return false;
	}
	
	private ArchiveFormat determineArchiveFormat(File archiveFile) {
		String fileName = archiveFile.getName();
		String suffix = fileName.substring(fileName.lastIndexOf('.')+1);
		if(suffix.equalsIgnoreCase("zip")) {
			return ArchiveFormat.ZIP;
		} else if (suffix.equalsIgnoreCase("tar.gz")){
			return ArchiveFormat.TAR_GZ;
		} else {
			return null;
		}
	}
	
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
	
	private ArchiveOutputStream createArchiveOutputStream(File archiveFile) throws IOException {
		
		ArchiveOutputStream aOut = null;
		
		FileOutputStream fOut = new FileOutputStream(archiveFile);
		BufferedOutputStream bOut = new BufferedOutputStream(fOut);

		if (this.archiveFormat == ArchiveFormat.ZIP) {
			aOut = new ZipArchiveOutputStream(bOut);
		} else if (this.archiveFormat == ArchiveFormat.TAR_GZ) {
			GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(bOut);
			aOut = new TarArchiveOutputStream(gzOut);
		}
		  
		return aOut;
		
	}
	
	private ArchiveEntry createArchiveEntry(File file) {
		ArchiveEntry archiveEntry = null;
		if (this.archiveFormat == ArchiveFormat.ZIP) {
			archiveEntry = new ZipArchiveEntry(file.getName());
		} else if (this.archiveFormat == ArchiveFormat.TAR_GZ) {
			archiveEntry = new TarArchiveEntry(file);
		}
		return archiveEntry;
	}
}
