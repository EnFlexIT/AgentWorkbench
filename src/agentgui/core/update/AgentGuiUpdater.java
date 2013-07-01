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
package agentgui.core.update;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.common.Zipper;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.config.VersionInfo;
import agentgui.core.gui.ProgressMonitor;
import agentgui.simulationService.distribution.Download;
import agentgui.simulationService.distribution.DownloadThread;

/**
 * The Class AgentGuiUpdater.
 */
public class AgentGuiUpdater extends Thread {

	// --- For the debugging of this class set true -------------
	private boolean debuggingInIDE = false;
	
	public static final long UPDATE_CHECK_PERIOD = 1000*60*60*24; 	// - once a day -
	public static final String UPDATE_SUB_FOLDER = "updates"; 		// - subfolder in the web server directory -
	public static final String UPDATE_VERSION_INFO_FILE = "latestVersion.xml";
	
	private final String updateSiteAddition = "?key=xml";

	private GlobalInfo globalInfo = null;
	private VersionInfo versionInfo = null;
	private ExecutionMode executionMode = null;
	
	private String updateSite = null;
	private Integer updateAutoConfiguration = null;
	private Integer updateKeepDictionary = 1;
	private long updateDateLastChecked = 0;
	
	
	private String alternativeInfoLink = null;
	
	private String localDownloadPath = null;
	private String localWebServerPath = null;
	private String localPropertiesPath = null;
	
	private UpdateInformation updateInformation = null;
	
	private String latestVersionInfoFullPath = null;

	private String localUpdateZipFile = null;
	private String localUpdateExtractedFolder = null;

	private DownloadThread downloadThread4Update = null;
	private ProgressMonitor progressMonitor = null;

	private boolean manualyExecutedByUser = false;
	private boolean doUpdateProcedure = true;
	private boolean askBeforeDownload = false;
	private boolean askBeforeProjectShutdownAndUnzip = false;
	
	private boolean storeUpdateLocally = false;
	
	
	/**
	 * Instantiates a new Agent.GUI updater process.
	 */
	public AgentGuiUpdater() {
		this.initialize();
	}
	/**
	 * Instantiates a new Agent.GUI updater process.
	 * @param userExecuted indicates that execution was manually chosen by user
	 */
	public AgentGuiUpdater(boolean userExecuted) {
		this.manualyExecutedByUser = userExecuted;
		this.initialize();
	}
	/**
	 * Instantiates a new Agent.GUI updater process.
	 * @param userExecuted indicates that execution was manually chosen by user
	 * @param alternativeInfoLink the alternative download link for the update
	 */
	public AgentGuiUpdater(boolean userExecuted, String alternativeInfoLink) {
		this.manualyExecutedByUser = userExecuted;
		this.alternativeInfoLink = alternativeInfoLink;
		this.initialize();
	}
	
	/**
	 * Initialize and set needed local variables.
	 */
	private void initialize() {
		
		this.setName("Agent.GUI-Updater");
		
		this.globalInfo = Application.getGlobalInfo();
		this.versionInfo = this.globalInfo.getVersionInfo();
		this.executionMode = this.globalInfo.getExecutionMode();

		this.updateSite = this.globalInfo.getUpdateSite();
		this.updateAutoConfiguration = this.globalInfo.getUpdateAutoConfiguration();
		this.updateKeepDictionary = this.globalInfo.getUpdateKeepDictionary();
		this.updateDateLastChecked = this.globalInfo.getUpdateDateLastChecked();
		
		this.localDownloadPath = this.globalInfo.PathDownloads(true);
		this.localWebServerPath = this.globalInfo.PathWebServer(true);
		this.localPropertiesPath = this.globalInfo.PathProperty(true);
		
		this.latestVersionInfoFullPath = this.localDownloadPath + AgentGuiUpdater.UPDATE_VERSION_INFO_FILE;
		
		this.setUpdateConfiguration();
	}
	
	/**
	 * Sets the update configuration.
	 */
	private void setUpdateConfiguration() {
		
		if (this.alternativeInfoLink==null && this.manualyExecutedByUser==false) {
			long timeNow = System.currentTimeMillis();
			long time4NextCheck = this.updateDateLastChecked + AgentGuiUpdater.UPDATE_CHECK_PERIOD;
			if (timeNow<time4NextCheck) {
				doUpdateProcedure=false;
			} else {
				Application.getGlobalInfo().setUpdateDateLastChecked(timeNow);
				Application.getGlobalInfo().getFileProperties().save();
			}
		}
		
		switch (this.executionMode) {
		case APPLICATION:
			// --------------------------------------------
			switch (this.updateAutoConfiguration) {
			case 0:
				this.askBeforeDownload=false;
				this.askBeforeProjectShutdownAndUnzip=true;
				break;
			case 1:
				this.askBeforeDownload=true;
				this.askBeforeProjectShutdownAndUnzip=true;
				break;
			default:
				doUpdateProcedure = false;
				break;
			}
			// --------------------------------------------
			break;
			
		case SERVER:
			// --------------------------------------------
			doUpdateProcedure = false;
			// --------------------------------------------
			break;
			
		case SERVER_MASTER:
			// --------------------------------------------
			switch (this.updateAutoConfiguration) {
			case 0:
				this.askBeforeDownload=false;
				this.askBeforeProjectShutdownAndUnzip=false;
				break;
			default:
				doUpdateProcedure = false;
				break;
			}
			this.storeUpdateLocally = true;
			// --------------------------------------------
			break;
			
		case SERVER_SLAVE:
			// --------------------------------------------
			switch (this.updateAutoConfiguration) {
			case 0:
				this.askBeforeDownload=false;
				this.askBeforeProjectShutdownAndUnzip=false;
				break;
			default:
				doUpdateProcedure = false;
				break;
			}
			// --------------------------------------------
			break;
		}
		
		// ------------------------------------------------
		// --- Manual execution? --------------------------
		if (this.manualyExecutedByUser==true) {
			this.doUpdateProcedure=true;
			this.askBeforeDownload=true;
			this.askBeforeProjectShutdownAndUnzip=true;
		}
		
		// ------------------------------------------------
		// --- Execution out of the IDE? ------------------
		if (this.globalInfo.AppExecutedOver().equals(GlobalInfo.ExecutedOverIDE)) {
			this.doUpdateProcedure=false; // set to 'true' for further developments of the AgentGuiUpdater class 
			this.askBeforeDownload=true;
			this.askBeforeProjectShutdownAndUnzip=true;
			System.out.println("Agent.GUI-Update: No updates in the IDE environment available.");
		}
		
		// ------------------------------------------------
		// --- Currently debugging this class ? -----------
		if (this.debuggingInIDE==true) {
			this.doUpdateProcedure=true;
			this.askBeforeDownload=true;
			this.askBeforeProjectShutdownAndUnzip=true;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		
		if (this.doUpdateProcedure==false) {
			// --- No download, no update --------------------------- 
			return;
		}
		
		// --- If running, wait for the end of the benchmark --------
		this.waitForTheEndOfBenchmark();
		
		// ----------------------------------------------------------
		// --- Get latest version information -----------------------
		boolean loadUpdateInformation = false;
		boolean skipUpdateDownload = false;
		boolean readyToUnZip = false;
		if (this.isUpdateAlreadyLocallyAvailable()) {
			// --- Locally (downloaded, but not yet installed) ------
			loadUpdateInformation = true;
			skipUpdateDownload = true;
			readyToUnZip = true;
		} else {
			// --- From update site ---------------------------------
			String srcFileURL = null;
			if (this.alternativeInfoLink!=null) {
				// --- From a server.master -------------------------
				srcFileURL = this.alternativeInfoLink;
			} else {
				// --- From the Agent.GUI web site ------------------
				srcFileURL = this.updateSite + this.updateSiteAddition;	
			}			
			Download infoDownload = new Download(srcFileURL, this.latestVersionInfoFullPath);
			infoDownload.startDownload();
			loadUpdateInformation = infoDownload.isFinished() && infoDownload.wasSuccessful();
			infoDownload = null;
		}
		
		if (loadUpdateInformation==true) {
			// ------------------------------------------------------
			// --- Load the UpdateInformation -----------------------
			this.updateInformation = new UpdateInformation();
			this.updateInformation.loadUpdateInformation(this.latestVersionInfoFullPath);
			if (this.updateInformation!=null && this.updateInformation.isError()==false && this.updateInformation.isNewerVersion(this.versionInfo)==true) {
				// --------------------------------------------------
				// --- Set name of the update zip file --------------
				this.localUpdateZipFile = this.localDownloadPath + this.updateInformation.getDownloadFile();
				this.localUpdateExtractedFolder = this.updateInformation.getDownloadFile().replace(".zip", "");
				// --- Set version info string ----------------------
				String updateVersion = "Version " + this.updateInformation.getMajorRevision() + "." + this.updateInformation.getMinorRevision() + " revision " + this.updateInformation.getBuild();
 				// --------------------------------------------------
				// --- Ask user -------------------------------------
				if (this.askBeforeDownload==true && skipUpdateDownload==false) {
					String title = "Agent.GUI-" + updateVersion + " " + Language.translate("is available", Language.EN) + "!";
					String message = Language.translate("An Agent.GUI-Update is available. Download now?", Language.EN);
					int answer = JOptionPane.showConfirmDialog(Application.getMainWindow(), message, title, JOptionPane.YES_NO_OPTION);
					if (answer==JOptionPane.NO_OPTION) {
						return;
					}
				}
				// --------------------------------------------------
				// --- Download now ---------------------------------
				if (skipUpdateDownload==false) {
					readyToUnZip = this.downloadUpdateFile();
				}
				if (readyToUnZip==true) {
					// ----------------------------------------------
					// --- Copy latest info to properties -----------
					String destPath = this.localPropertiesPath + AgentGuiUpdater.UPDATE_VERSION_INFO_FILE;
					File latestVersionInfoFileProperties = new File(destPath);
					if (latestVersionInfoFileProperties.exists()) {
						latestVersionInfoFileProperties.delete();
					}
					File latestVersionInfoFile = new File(this.latestVersionInfoFullPath);
					latestVersionInfoFile.renameTo(latestVersionInfoFileProperties);
					// ----------------------------------------------
					// --- Ask user ---------------------------------
					if (this.askBeforeProjectShutdownAndUnzip==true) {
						String title = "Agent.GUI-Update " + updateVersion + " " + Language.translate("was downloaded", Language.EN) + "!";
						String message = Language.translate("Agent.GUI-Update was downloaded. Install now?", Language.EN);
						int answer = JOptionPane.showConfirmDialog(Application.getMainWindow(), message, title, JOptionPane.YES_NO_OPTION);
						if (answer==JOptionPane.NO_OPTION) {
							return;
						}
					}
					// ----------------------------------------------
					// --- Stop JADE, close Projects ----------------
					if (isPreparedForInstallation()==true) {
						// ------------------------------------------
						// --- Unzip the downloaded file ------------
						if (this.unzipUpdateFile(this.askBeforeProjectShutdownAndUnzip)==true){
							// --- Clean up or Move to Server directory
							this.handleDownloadedFilesAfterExtraction();
							// --- Move AgentGuiUpdate.jar ---------- 
							if (this.moveAgentGuiUpdaterJar()==true) {
								// --- Do installation --------------
								this.executeAgentGuiUpdater();
								// --- ShutDown ---------------------
								Application.quit();
								return;
								
							} else {
								System.err.println("Agent.GUI-Update: Could not find 'AgentGuiUpdate.jar' in installation package!");
								// --- Cleanup download folder ------
								this.cleanUpDownloadFolder(new File(this.localDownloadPath));
								if (this.askBeforeProjectShutdownAndUnzip==true) {
									String title = "Error while updating Agent.GUI!";
									String message = Language.translate("Could not find 'AgentGuiUpdate.jar' in installation package!", Language.EN);
									JOptionPane.showMessageDialog(Application.getMainWindow(), message, title, JOptionPane.INFORMATION_MESSAGE);	
								}
								// --- Restart Agent.GUI if Server --
								switch (this.executionMode) {
								case SERVER_MASTER:
								case SERVER_SLAVE:
									Application.startAgentGUI();	
									break;
								}
								
							}
														
						} else {
							System.err.println("Agent.GUI-Update: Unsuccessful unzipping!");
						}
						
					} else {
						System.out.println("Agent.GUI-Update: Not prepared for installation! Please close all open projects.");
					}
					
				} else {
					// ----------------------------------------------
					// --- Download unsuccessful --------------------
					System.err.println("Agent.GUI-Update: Unsuccessful download !");
					File downloadedFile = new File(this.localUpdateZipFile);
					if (downloadedFile.exists()) {
						downloadedFile.delete();
					}
				} // end readyToInstall==true after download
			
			} else {
				// --- Delete info file -----------------------------
				File latestVersionInfoFile = new File(this.latestVersionInfoFullPath);
				latestVersionInfoFile.delete();
				if (this.manualyExecutedByUser==true) {
					// --- Inform user ------------------------------
					String title = "Agent.GUI-Updater!";
					String message = Language.translate("There is no Agent.GUI-Update available!", Language.EN);
					if (this.updateInformation.isError()==true) {
						message = this.updateInformation.getErrorMessage();
					}
					JOptionPane.showMessageDialog(Application.getMainWindow(), message, title, JOptionPane.INFORMATION_MESSAGE);	
				}
				
			}// end this.updateInformation!=null && this.updateInformation.isNewerVersion(this.versionInfo)
			
		}//end loadUpdateInformation==true

	}
	
	/**
	 * Moves file 'AgentGuiUpdater.jar' to the main folder.
	 */
	private boolean moveAgentGuiUpdaterJar() {
		
		String extractedFolder = globalInfo.PathDownloads(true) + this.localUpdateExtractedFolder;
		String updaterFilePath = extractedFolder + File.separator + this.globalInfo.getFileNameUpdater(false);
		System.out.println("Trying to move file " + updaterFilePath);
		File updaterFile = new File(updaterFilePath);
		if (updaterFile.exists()==true) {
			
			String updaterRootFilePath = globalInfo.PathBaseDir();
			if (updaterRootFilePath.endsWith(File.separator)==false) {
				updaterRootFilePath = updaterRootFilePath + File.separator;
			}
			updaterRootFilePath = updaterRootFilePath + this.globalInfo.getFileNameUpdater(false);
			File updaterRootFile = new File(updaterRootFilePath);	
			if (updaterRootFile.exists()==true) {
				updaterRootFile.delete();
			}
			updaterFile.renameTo(updaterRootFile);
			return true;
		}
		return false;
	}
	/**
	 * Handle download after extraction.
	 */
	private void handleDownloadedFilesAfterExtraction() {

		// --- File server/update directory ---------------------------------------------
		String serverUpdateDirectory = this.localWebServerPath + UPDATE_SUB_FOLDER + File.separator;
		File serverUpdateDirectoryFile = new File(serverUpdateDirectory);
		
		// --- File of the latest version information in the properties directory -------
		File latestVersionInfoFileProperties = new File(this.localPropertiesPath + AgentGuiUpdater.UPDATE_VERSION_INFO_FILE);
		File latestVersionInfoFileOnServer = new File(serverUpdateDirectory + AgentGuiUpdater.UPDATE_VERSION_INFO_FILE);
		
		// --- File of the downloaded zip archive ---------------------------------------
		String zipFolderName = this.updateInformation.getDownloadFile();
		File zipFolderFile = new File(this.localUpdateZipFile);
		File zipFolderFileOnServer = new File(serverUpdateDirectory + zipFolderName);
		
		// --- Application, Master, Slave ? ---------------------------------------------
		if (this.storeUpdateLocally==true) {
			// --- Just in the case that currently the server.master is running ---------
			if (serverUpdateDirectoryFile.exists()==false) {
				serverUpdateDirectoryFile.mkdirs();
			}
			if (latestVersionInfoFileOnServer.exists()==true) {
				latestVersionInfoFileOnServer.delete();
			}
			if (zipFolderFileOnServer.exists()==true) {
				zipFolderFileOnServer.delete();
			}
			latestVersionInfoFileProperties.renameTo(latestVersionInfoFileOnServer);
			zipFolderFile.renameTo(zipFolderFileOnServer);
			
		} else {
			// --- For case Application and Slave, just delete --------------------------
			latestVersionInfoFileProperties.delete();
			zipFolderFile.delete();
		}
		
	}
	
	/**
	 * Executes the AgentGuiUpdater.jar in the same folder.
	 */
	private void executeAgentGuiUpdater() {
		
		// --- Create execute statement -----------------------------
		String execute = "java -jar " + this.globalInfo.getFileNameUpdater(false) + " -update " + this.localUpdateExtractedFolder;
		if (this.updateKeepDictionary==0) {
			execute += " -deleteDictionary";
		}
		System.out.println( "Execute AgentGuiUpdate.jar: " + execute);
		// ----------------------------------------------------------
		try {
			String[] arg = execute.split(" ");
			ProcessBuilder proBui = new ProcessBuilder(arg);
			proBui.redirectErrorStream(false);
			proBui.directory(new File(this.globalInfo.PathBaseDir()));
			proBui.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Will stop JADE, if running, and close all open project.
	 * @return true, if successful
	 */
	private boolean isPreparedForInstallation() {
		// --- Stop Jade ------------------------
		Application.getJadePlatform().jadeStop();
		// --- Close open projects --------------
		if (Application.getProjectsLoaded().closeAll()==false) {
			return false;	
		}
		// --- Save FileProperties --------------
		this.globalInfo.getFileProperties().save();
		Language.saveDictionaryFile();
		return true;
	}
	
	/**
	 * Unzip the local update file.
	 * @return true, if successful
	 */
	private boolean unzipUpdateFile(boolean visualizeUnzipping) {
		
		boolean done = false;
		
		File zipFolderFile = new File(this.localUpdateZipFile);
		if (zipFolderFile.exists()==true) {

			String extractFolder = zipFolderFile.getParent() + File.separator + this.localUpdateExtractedFolder;
			
			Zipper zipper = new Zipper();
			zipper.setUnzipZipFolder(this.localUpdateZipFile);
			zipper.setUnzipDestinationFolder(extractFolder);
			zipper.setVisible(visualizeUnzipping);
			zipper.doUnzipFolder();

			while(zipper.isDone()==false) {
				try {
					sleep(200);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
			zipper = null;
			done = true;
		}
		return done;
	}
	
	/**
	 * Download the update file in an extra thread.
	 * @return true, if successful
	 */
	private boolean downloadUpdateFile() {
		
		boolean readyToInstall = false;
		
		if (this.updateInformation!=null && this.localUpdateZipFile!=null) {
			if (this.updateInformation.getDownloadLink()!=null) {
				
				System.out.println("Agent.GUI-Update: Start download ...");
				this.progressMonitor = new ProgressMonitor("Agent.GUI - Update", "Agent.GUI - Update", "Download");
				if (this.askBeforeDownload==true) {
					this.progressMonitor.setVisible(true);	
				}
				this.progressMonitor.setProgress(0);
				
				this.downloadThread4Update = new DownloadThread(this.updateInformation.getDownloadLink(), this.localUpdateZipFile);
				this.downloadThread4Update.start();
				while(this.downloadThread4Update.isFinished()==false) {
					this.progressMonitor.setProgress(this.downloadThread4Update.getDownloadProgress());
					if (this.progressMonitor.isCanceled()) {
						this.downloadThread4Update.doCancel();
					}
					try {
						sleep(200);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
				} // end while
				
				this.progressMonitor.setProgress(100);
				this.progressMonitor.setVisible(false);
				this.progressMonitor.dispose();
				this.progressMonitor = null;

				// --- Successful download ? --------------
				readyToInstall = this.downloadThread4Update.wasSuccessful();
				this.downloadThread4Update = null;
				
			}
		}
		return readyToInstall;
		
	}
	
	/**
	 * Checks if is update already locally available.
	 * @return true, if is update already locally available
	 */
	private boolean isUpdateAlreadyLocallyAvailable() {
		
		boolean available = false;
		
		String pathCheckLatestVersionInfo = this.localPropertiesPath + AgentGuiUpdater.UPDATE_VERSION_INFO_FILE;
		File fileCheckLatestVersionInfo = new File(pathCheckLatestVersionInfo); 
		if (fileCheckLatestVersionInfo.exists()==true) {
			// --- There is a File with update information available ----------
			UpdateInformation updateInfo = new UpdateInformation();
			updateInfo.loadUpdateInformation(pathCheckLatestVersionInfo);
			// --- Check if the zip-File is there -----------------------------
			String downloadFile = updateInfo.getDownloadFile();
			String pathDownloadFile = this.localDownloadPath + downloadFile;
			File fileDownloadFile = new File(pathDownloadFile); 
			if (fileDownloadFile.exists()==true) {
				// --- Is the size the same ? ---------------------------------
				Integer normalSize = updateInfo.getDownloadSize();
				long currentSize = fileDownloadFile.length();
				if (currentSize==normalSize) {
					File moveTo = new File(this.latestVersionInfoFullPath);
					fileCheckLatestVersionInfo.renameTo(moveTo);
					available = true;
				} else {
					fileCheckLatestVersionInfo.delete();
					fileDownloadFile.delete();
				}
				
			} else {
				fileCheckLatestVersionInfo.delete();
			}
		}
		return available;
	}

	
	
	/**
	 * Clean up update folder.
	 * @param file the file
	 */
	private void cleanUpDownloadFolder(File file) {
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			this.deleteFileOrFolder(files[i]);	
		}// end for
	}
	
	/**
	 * Delete file or folder.
	 * @param file the file
	 */
	private void deleteFileOrFolder(File file) {
		if (file.isFile()) {
			file.delete();	
		} else {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				File deleteFile = files[i];
				this.deleteFileOrFolder(deleteFile);
			}
			file.delete();
		}
	}
	
	/**
	 * Wait for benchmark.
	 */
	private void waitForTheEndOfBenchmark() {
		while (Application.isBenchmarkRunning()==true) {
			try {
				sleep(1000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
}
