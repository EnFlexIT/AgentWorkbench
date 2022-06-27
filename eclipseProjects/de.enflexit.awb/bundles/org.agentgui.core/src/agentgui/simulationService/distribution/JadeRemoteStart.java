/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.simulationService.distribution;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Scanner;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import agentgui.core.application.Application;
import agentgui.core.project.Project;
import agentgui.simulationService.agents.ServerSlaveAgent;
import agentgui.simulationService.ontology.RemoteContainerConfig;
import de.enflexit.common.SystemEnvironmentHelper;
import de.enflexit.common.transfer.ArchiveFileHandler;
import de.enflexit.common.transfer.ArchiveFileHandler.ArchiveFormat;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Profile;
import jade.misc.FileInfo;
import jade.misc.FileManagerClient;

/**
 * This class is only used by the {@link ServerSlaveAgent} of the background
 * system. It enables the agent to start a new process in order to extend
 * a remote JADE platform and join that platform with a new container.
 * 
 * @see ServerSlaveAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JadeRemoteStart {

	/** Constant for a memory of 16 MB. */
	public static final String jvmMemo16MB 	= "16m";
	/** Constant for a memory of 32 MB. */
	public static final String jvmMemo32MB 	= "32m";
	/** Constant for a memory of 64 MB. */
	public static final String jvmMemo64MB 	= "64m";
	/** Constant for a memory of 128 MB. */
	public static final String jvmMemo128MB = "128m";
	/** Constant for a memory of 256 MB. */
	public static final String jvmMemo256MB = "256m";
	/** Constant for a memory of 512 MB. */
	public static final String jvmMemo512MB = "512m";
	/** Constant for a memory of 1024 MB. */
	public static final String jvmMemo1GB 	= "1g";
	/** Constant for a memory of 2048 MB. */
	public static final String jvmMemo2GB 	= "2g";
	/** Constant for a memory of 4096 MB. */
	public static final String jvmMemo4GB 	= "4g";
	/** Constant for a memory of 8192 MB. */
	public static final String jvmMemo8GB 	= "8g";
	/** Constant for a memory of 16384 MB. */
	public static final String jvmMemo16GB 	= "16g";
	/** Constant for a memory of 32768 MB. */
	public static final String jvmMemo32GB 	= "32g";
	
	
	private boolean debug = false;
	
	private Agent myAgent;
	private RemoteContainerConfig reCoCo;
	
	private boolean jvmMemAllocUseDefaults = true;
	private String jvmMemAllocInitial = JadeRemoteStart.jvmMemo128MB;
	private String jvmMemAllocMaximum = JadeRemoteStart.jvmMemo2GB;
	
	private boolean jadeIsRemoteContainer = true; 
	private boolean jadeShowGUI = true; 
	private String jadeShowGUIAgentName = "rma."; 
	private String jadeServices = "jade.core.event.NotificationService;jade.core.mobility.AgentMobilityService;agentgui.simulationService.SimulationService;agentgui.simulationService.LoadService";
	private String jadeHost = "localhost";
	private String jadePort = Application.getGlobalInfo().getJadeLocalPort().toString();
	private String jadeContainerName = "remote";
	
	private File rcProjectDirectory = null; 
	
	
	/**
	 * Instantiates a new jade remote start.
	 *
	 * @param agent the current agent that uses this class
	 * @param remoteStartConfiguration the remote start configuration
	 */
	public JadeRemoteStart(Agent agent, JadeRemoteStartConfiguration remoteStartConfiguration) {
		this(agent, remoteStartConfiguration.getRemoteContainerConfig());
		this.rcProjectDirectory = remoteStartConfiguration.getProjectPath();
	}
	/**
	 * Instantiates a new jade remote start.
	 *
	 * @param agent the current agent that uses this class
	 * @param remoteContainerConfig the RemoteContainerConfig
	 */
	public JadeRemoteStart(Agent agent, RemoteContainerConfig remoteContainerConfig) {
		
		this.myAgent = agent;
		this.reCoCo = remoteContainerConfig;
		
		if (this.debug) {
			System.out.println("Class '" + this.getClass().getName() + "' in debug modue ...");
		}
		
		// --- Configure JVM arguments ------------------------------
		if (this.reCoCo.getJvmMemAllocInitial()==null && this.reCoCo.getJvmMemAllocMaximum()==null) {
			this.jvmMemAllocUseDefaults = true;	
			this.jvmMemAllocInitial = JadeRemoteStart.jvmMemo32MB;
			this.jvmMemAllocMaximum = JadeRemoteStart.jvmMemo128MB;
		} else {
			this.jvmMemAllocUseDefaults = false;
			this.jvmMemAllocInitial = this.reCoCo.getJvmMemAllocInitial();
			this.jvmMemAllocMaximum = this.reCoCo.getJvmMemAllocMaximum();
		}

		// --- Configure Jade settings ------------------------------
		this.jadeIsRemoteContainer = this.reCoCo.getJadeIsRemoteContainer();
		this.jadeShowGUI = this.reCoCo.getJadeShowGUI();	
		
		if (this.reCoCo.getJadeServices()!=null) {
			this.jadeServices = this.reCoCo.getJadeServices();
		}
		if (this.reCoCo.getJadeHost()!=null) {
			this.jadeHost = this.reCoCo.getJadeHost();	
		}
		if (this.reCoCo.getJadePort()!=null) {
			this.jadePort = this.reCoCo.getJadePort();	
		}
		if (this.reCoCo.getJadeContainerName()!=null) {
			this.jadeContainerName = this.reCoCo.getJadeContainerName();	
		}
	}	
	
	/**
	 * Checks if is ready to start remote container.
	 * @return true, if is ready to start remote container
	 */
	public boolean isReadyToStartRemoteContainer() {
		
		// --- If not already available, download project files -----
		if (this.rcProjectDirectory==null) {
			if (this.reCoCo.getFileManagerAgent()==null) {
				// --- No project for download ----------------------
				return true;
			} else {
				// --- Download project resources -------------------
				this.rcProjectDirectory = this.downloadFilesFromFileManagerAgent(this.reCoCo.getFileManagerAgent(), this.myAgent);
			}
		}
		// --- If a remote container project can be found ----------- 
		if (this.rcProjectDirectory!=null) {
			System.out.println(this.getClass().getSimpleName() + ": Found installed remote project '" + this.rcProjectDirectory.getName() + "'");
			// --- Load project XML and check required features -----
			final Project remoteProject = Project.loadProjectXml(this.rcProjectDirectory);
			if (remoteProject.requiresFeatureInstallation()==true) {
				// --- Remind the start configuration ---------------
				String configFilePath = JadeRemoteStartConfiguration.getDefaultConfigurationFile().getAbsolutePath();
				boolean isSavedConfig = JadeRemoteStartConfiguration.saveRemoteStartConfiguration(new JadeRemoteStartConfiguration(this.rcProjectDirectory, this.reCoCo));
				if (isSavedConfig==true) {
					System.out.println(this.getClass().getSimpleName() + ": Saved remote start configuration to " + configFilePath);
				}
				// --- Install the required features ---------------- 
				System.out.println(this.getClass().getSimpleName() + ": Install required project features ...");
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							remoteProject.installRequiredFeatures();
						} catch (Exception e) {
							System.err.println("Not all required features have been installed successfully:");
							System.err.println(e.getMessage());
							//TODO figure out how to handle this
						}
					}
				}).start();
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * This Method starts a Jade-Platform within a new Java Virtual Machine.
	 */
	public void startJade() {
		
		String os = SystemEnvironmentHelper.getOperatingSystem();
		
		String java = "";
		String javaVMArgs = "";
		String equinoxLauncherJar = "";
		String project = "";
		String jade = "";
		String jadeArgs = "";
		
		// --------------------------------------
		// --- Java-Config ----------------------
		java = "java";
		if (this.jvmMemAllocUseDefaults==false) {
			javaVMArgs = "-Xms" + this.jvmMemAllocInitial + " -Xmx" + this.jvmMemAllocMaximum;
		} 
		
		// --------------------------------------
		// --- Class-Path configuration ---------
		equinoxLauncherJar = this.getEquinoxLauncherJar();
		// +++ Check for operating system +++
		if (os.toLowerCase().contains("windows")==true) {
			// --- nothing to do here ---
		} else if (os.toLowerCase().contains("linux")==true) {
			equinoxLauncherJar = equinoxLauncherJar.replaceAll(";", ":");
		}

		// --------------------------------------
		// --- The project to open --------------
		if (this.rcProjectDirectory!=null) {
			project += "-project " + this.rcProjectDirectory.getName();
		}
		
		// --------------------------------------
		// --- Jade configuration ---------------
		jade += "-jade" + " ";
		if (this.jadeServices!=null) {
			jadeArgs += "-services " + this.jadeServices + " ";
		}
		if (this.jadeIsRemoteContainer) {
			jadeArgs += "-container ";
			jadeArgs += "-container-name " + this.jadeContainerName + " ";
		} 
		jadeArgs += "-host " + this.jadeHost + " ";
		jadeArgs += "-port " + this.jadePort + " ";
		// -- Configure -local-host -------------
		String localHost = Application.getGlobalInfo().getJadeDefaultProfile().getProperties().getProperty(Profile.LOCAL_HOST);
		if (localHost!=null && localHost.equals("")==false) {
			jadeArgs += "-local-host " + localHost + " ";	
		}
		// --- Show GUI? ------------------------
		if (this.jadeShowGUI==true) {
			jadeArgs += this.jadeShowGUIAgentName + this.jadeContainerName + ":jade.tools.rma.rma ";
		}		
		jadeArgs = jadeArgs.trim();
		
		// --------------------------------------
		// --- merge execute statement ----------
		String execute = java + " " + javaVMArgs + " " + equinoxLauncherJar +  " " + project + " " + jade  + " " + jadeArgs;
		execute = execute.replace("  ", " ");
		System.out.println( "Execute: " + execute);
		
		// --------------------------------------
		Scanner in = null;
		Scanner err = null;
		try {
			
			String[] arg = execute.split(" ");
			ProcessBuilder proBui = new ProcessBuilder(arg);
			proBui.redirectErrorStream(true);
			proBui.directory(new File(Application.getGlobalInfo().getPathBaseDir()));
			
			Process process = proBui.start();
			
			in = new Scanner(process.getInputStream());
			in.useDelimiter("\\Z");
			
			err = new Scanner(process.getErrorStream());
			err.useDelimiter("\\Z");
			
			while (in.hasNextLine() || err.hasNextLine() ) {
				if (in.hasNextLine()) {
					System.out.println("[" + this.jadeContainerName + "]: " + in.nextLine());	
				}
				if (err.hasNextLine()){
					System.err.println("[" + this.jadeContainerName + "]: " + err.nextLine());	
				}
			}
			System.out.println("Killed Container [" + this.jadeContainerName + "]");
		    
			// ------------------------------------------------------
			// --- Remove the 'rc-*' project directory --------------
			// ------------------------------------------------------
			Application.getProjectsLoaded().projectDelete(this.rcProjectDirectory);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in!=null) in.close();
			if (err!=null) err.close();	
		}

	}

	/**
	 * Returns the command line part for the equinox launcher jar.
	 * @return the equinox launcher jar
	 */
	private String getEquinoxLauncherJar() {
		// --- Get the equinox launcher ------------------------
		String execJar = Application.getGlobalInfo().getFileRunnableJar();
		execJar = execJar.replace("\\", "/");
		return "-jar " + execJar;
	}
	
	/**
	 * Checks if is memory allocation for the new JVM will be the default one.
	 *
	 * @return the jvmMemAllocUseDefaults
	 */
	public boolean isJVMMemAllocUseDefaults() {
		return jvmMemAllocUseDefaults;
	}
	/**
	 * Sets the memory allocation for the new JVM to be the default value or not.
	 *
	 * @param useDefaults the new jVM mem alloc use defaults
	 */
	public void setJVMMemAllocUseDefaults(boolean useDefaults) {
		this.jvmMemAllocUseDefaults = useDefaults;
	}

	/**
	 * @return the jvmMemAllocInitial
	 */
	public String getJVMMemAllocInitial() {
		return jvmMemAllocInitial;
	}
	/**
	 * @param jvmMemAllocInitial the jvmMemAllocInitial to set
	 */
	public void setJVMMemAllocInitial(String jvmMemAllocInitial) {
		this.jvmMemAllocInitial = jvmMemAllocInitial;
	}

	/**
	 * @return the jvmMemAllocMaximum
	 */
	public String getJVMMemAllocMaximum() {
		return jvmMemAllocMaximum;
	}
	/**
	 * @param jvmMemAllocMaximum the jvmMemAllocMaximum to set
	 */
	public void setJVMMemAllocMaximum(String jvmMemAllocMaximum) {
		this.jvmMemAllocMaximum = jvmMemAllocMaximum;
	}

	/**
	 * @return the jadeIsRemoteContainer
	 */
	public boolean isJADEIsRemoteContainer() {
		return jadeIsRemoteContainer;
	}
	/**
	 * @param jadeIsRemoteContainer the jadeIsRemoteContainer to set
	 */
	public void setJADEIsRemoteContainer(boolean jadeIsRemoteContainer) {
		this.jadeIsRemoteContainer = jadeIsRemoteContainer;
	}

	/**
	 * @return the jadeShowGUI
	 */
	public boolean isJADEShowGUI() {
		return jadeShowGUI;
	}
	/**
	 * @param jadeShowGUI the jadeShowGUI to set
	 */
	public void setJADEShowGUI(boolean jadeShowGUI) {
		this.jadeShowGUI = jadeShowGUI;
	}

	/**
	 * @return the jadeShowGUIAgentName
	 */
	public String getJadeShowGUIAgentName() {
		return jadeShowGUIAgentName;
	}
	/**
	 * @param jadeShowGUIAgentName the jadeShowGUIAgentName to set
	 */
	public void setJadeShowGUIAgentName(String jadeShowGUIAgentName) {
		this.jadeShowGUIAgentName = jadeShowGUIAgentName;
	}

	/**
	 * @return the jadeHost
	 */
	public String getJADEHost() {
		return jadeHost;
	}
	/**
	 * @param jadeHost the jadeHost to set
	 */
	public void setJADEHost(String jadeHost) {
		this.jadeHost = jadeHost;
	}

	/**
	 * @return the jadePort
	 */
	public String getJADEPort() {
		return jadePort;
	}
	/**
	 * @param jadePort the jadePort to set
	 */
	public void setJADEPort(String jadePort) {
		this.jadePort = jadePort;
	}

	/**
	 * @return the jadeContainerName
	 */
	public String getJADEContainerName() {
		return jadeContainerName;
	}
	/**
	 * @param jadeContainerName the jadeContainerName to set
	 */
	public void setJADEContainerName(String jadeContainerName) {
		this.jadeContainerName = jadeContainerName;
	}

	/**
	 * @param jadeServices the jadeServices to set
	 */
	public void setJADEServices(String jadeServices) {
		this.jadeServices = jadeServices;
	}
	/**
	 * @return the jadeServices
	 */
	public String getJADEServices() {
		return jadeServices;
	}

	
	// ------------------------------------------------------------------------
	// --- From here, methods for the file download and ----------------------- 
	// --- the handling of project files can be found -------------------------
	// ------------------------------------------------------------------------
	/**
	 * Downloads the provided files from the file manager agent (coming from a client application), installs 
	 * the received project in the project directory and returns the actual project directory as file,
	 * 
	 * @param fileMangerAID the file manger AID
	 * @param myAgent the current agent that uses this class
	 */
	private File downloadFilesFromFileManagerAgent(AID fileMangerAID, Agent myAgent) {

		if (fileMangerAID==null) return null;

		// -- Initiate a FileManagerClient --------------------------
		FileManagerClient fmClient = new FileManagerClient(fileMangerAID, myAgent);
		try {
			// --- List the available files -------------------------
			List<FileInfo> fileInfoList = this.getFileListFromFileManager(fmClient, null, null);
			if (fileInfoList!=null && fileInfoList.size()>0) {
				// --- Convert to list of path names ----------------
				List<String> pathList = this.convertToDirPathNameList(fileInfoList);
				// --- Download the files ---------------------------
				File downloadFile = this.doZipDownload(fmClient, pathList);
				// --- Extract the zip file -------------------------
				this.doZipExtraction(downloadFile, fileInfoList);
				// --- Delete the zip file --------------------------
				downloadFile.delete();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// --- Extract the *.agui file to the projects directory ----
		File targetProjectDirectory = null;
		String dirPathProjects = Application.getGlobalInfo().getPathProjects();
		String dirPathDownload = Application.getGlobalInfo().getFileManagerDownloadPath(true);
		File dirDownload = new File(dirPathDownload);
		if (dirDownload.exists()==true) {
			
			// --- Get the *.agui file ------------------------------ 
			File fileProjectTransfer = this.getProjectTransferFile(dirDownload);
			
			// --- Get the regular projects sub directory name ------
			String projectSubDirectoryPath = fileProjectTransfer.getName();
			int pos = projectSubDirectoryPath.lastIndexOf(".");
			if (pos > 0) {
				projectSubDirectoryPath = projectSubDirectoryPath.substring(0, pos);
			}
			
			// --- Define target directory --------------------------
			Integer prefixCounter = 1;
			while (targetProjectDirectory==null || targetProjectDirectory.exists()==true) {
				String projectPrefix = "rc" + String.format("%02d", prefixCounter) + "-";
				targetProjectDirectory = new File(dirPathProjects + projectPrefix + projectSubDirectoryPath);
				prefixCounter++;
			}
			
			// --- Extract project file into download directory -----
			ArchiveFileHandler extractor = new ArchiveFileHandler();
			extractor.decompressFolder(fileProjectTransfer, dirDownload, ArchiveFormat.ZIP);
			
			// --- Move extracted to project directory --------------
			try {
				File dirExtracted = new File(dirPathDownload + projectSubDirectoryPath);
				Files.move(dirExtracted.toPath(), targetProjectDirectory.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
			
			// --- Remove project file ------------------------------
			if (fileProjectTransfer.delete()==false) {
				fileProjectTransfer.deleteOnExit();
			}
			
		}
		return targetProjectDirectory;
	}

	/**
	 * Returns the FileInfo list that can be provided by the file manager.
	 *
	 * @param fmClient the fm client
	 * @param initialFileList the initial file list
	 * @param subDirectory the sub directory
	 * @return the file list from file manager
	 */
	private List<FileInfo> getFileListFromFileManager(FileManagerClient fmClient, List<FileInfo> initialFileList, String subDirectory) {
		
		List<FileInfo> fileList = null;
		if (initialFileList!=null) {
			fileList = initialFileList;
		} else {
			fileList = new java.util.ArrayList<>();
		}
		
		try {
			List<FileInfo> fileInfoList = fmClient.listFiles(subDirectory);
			if (fileInfoList!=null && fileInfoList.size()>0) {
				for (int i = 0; i < fileInfoList.size(); i++) {
					FileInfo fileInfo = fileInfoList.get(i);
					// --- Create the file description ------------------------
					String dirPathName = this.convertToDirPathName(fileInfo);
					if (fileInfo.isDirectory()) {
						this.getFileListFromFileManager(fmClient, fileList, dirPathName);
					} else {
						fileList.add(fileInfo);
					}
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fileList;
	}
	/**
	 * Converts the specified FileInfo to a directory path name.
	 *
	 * @param currSubDirPath the current sub directory path
	 * @param fileInfo the file info
	 * @return the string
	 */
	private String convertToDirPathName(FileInfo fileInfo) {
		String dirPathName = fileInfo.getPath() + "/" + fileInfo.getName();
		while (dirPathName.startsWith(".")) {
			dirPathName = dirPathName.substring(1);
		}
		return dirPathName;
	}
	/**
	 * Converts the specified list with FileInfo's to a list of path names.
	 *
	 * @param fileInfoList the file info list
	 * @return the list with path names
	 */
	private List<String> convertToDirPathNameList(List<FileInfo> fileInfoList) {
		
		if (fileInfoList==null || fileInfoList.size()==0) return null;
		
		List<String> pathList = new java.util.ArrayList<>();
		for (int i = 0; i < fileInfoList.size(); i++) {
			String filePathName = this.convertToDirPathName(fileInfoList.get(i));
			pathList.add(filePathName);
		}
		return pathList;
	}
	
	/**
	 * Does the actual download of the specified file list.
	 *
	 * @param fmClient the current FileManagerClient
	 * @param pathList the path list
	 * @return the file
	 * @throws Exception 
	 */
	private File doZipDownload(FileManagerClient fmClient, List<String> pathList) throws Exception {
		
		// --- Download the file that is either compressed or a single file -----
		String downLoadFileName = "rcsaDownload.zip";
		File downloadFile = new File(Application.getGlobalInfo().getFileManagerDownloadPath(true) + downLoadFileName);
		// --- Request the InputStream  for the download ------------------------
		InputStream inputStream = fmClient.downloadMultiple(pathList);
		OutputStream outputStream = null;
		try {
			
			// --- Download the file --------------------------------------------
			outputStream = new FileOutputStream(downloadFile);
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read=inputStream.read(bytes))!=-1) {
				outputStream.write(bytes, 0, read);
			}

		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} finally {
			if (inputStream!=null) {
				try {
					inputStream.close();
				} catch (IOException ioEx) {
					ioEx.printStackTrace();
				}
			}
			if (outputStream!=null) {
				try {
					//outputStream.flush();
					outputStream.close();
				} catch (IOException ioEx) {
					ioEx.printStackTrace();
				}
			}
		}
		return downloadFile;
	}
	
	/**
	 * Does the download extraction.
	 *
	 * @param archiveFile the archive file (zip)
	 * @param downloadFileList the download file list
	 */
	private void doZipExtraction(File archiveFile, List<FileInfo> downloadFileList) {
		
		ZipFile zipFile = null;
		try {
			// --- Access jar file ----------------------------------
			zipFile = new JarFile(archiveFile);
			String basePath = Application.getGlobalInfo().getFileManagerDownloadPath(true);
			
			for (int i = 0; i < downloadFileList.size(); i++) {
				
				FileInfo fi = downloadFileList.get(i);
				String destinationFilePath = basePath + this.convertToDirPathName(fi);
				// --- Define destination file ----------------------
				File destinationFile = new File(destinationFilePath);
				// --- Check destination directory ------------------
				File destinationDir = destinationFile.getParentFile();
				if (destinationDir.exists()==false) {
					destinationDir.mkdirs();
				}
				// --- Ensure to overwrite the files ---------------- 
				if (destinationFile.exists()==true) {
					destinationFile.delete();
				}
				
				// --- Get zip entry and extract it -----------------
				ZipEntry zEntry = zipFile.getEntry(fi.getName());
				if (zEntry!=null) {
					this.doZipFileExtract(zipFile, zEntry, destinationFile);
				}
			}
			
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} finally {
			if (zipFile!=null) {
				try {
					zipFile.close();
				} catch (IOException ioEx) {
					ioEx.printStackTrace();
				}
			}
		}
		
	}
	/**
	 * Extracts the specified file from a zip file.
	 *
	 * @param zipFile the jar
	 * @param zipEntry the specific zip entry
	 * @param destinationFile the destination file
	 */
	private void doZipFileExtract(ZipFile zipFile, ZipEntry zipEntry, File destinationFile) {
		
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(zipFile.getInputStream(zipEntry));
			out = new BufferedOutputStream(new FileOutputStream(destinationFile));
			byte[] buffer = new byte[2048];
			for (;;) {
				int nBytes = in.read(buffer);
				if (nBytes <= 0)
					break;
				out.write(buffer, 0, nBytes);
			}
			out.flush();
			out.close();
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in!=null) {
				try {
					in.close();
				} catch (IOException ioEx) {
					ioEx.printStackTrace();
				}
			}
			if (out!=null) {
				try {
					out.close();
				} catch (IOException ioEx) {
					ioEx.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Returns the project transfer file that can be found in the specified directory.
	 *
	 * @param searchDirectory the search directory
	 * @return the project transfer file
	 */
	private File getProjectTransferFile(File searchDirectory) {
		
		if (searchDirectory==null) return null;
	
		// --- Search for a packed project file ---------------------
		File[] filesFound =  searchDirectory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("." + Application.getGlobalInfo().getFileEndProjectZip());
			}
		});
		
		File aguiFile = null;
		if (filesFound.length>0) {
			// --- Take first , since only one file is expected ------
			aguiFile = filesFound[0];
		}
		return aguiFile;
	}
	
	
}
