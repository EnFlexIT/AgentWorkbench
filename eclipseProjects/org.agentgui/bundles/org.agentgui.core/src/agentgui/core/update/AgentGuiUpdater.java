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
import java.net.URI;
import javax.swing.JOptionPane;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.ProvisioningContext;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.RepositoryTracker;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.ui.ProvisioningUI;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.common.CommonComponentFactory;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.ExecutionEnvironment;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.common.VersionInfo;
import de.enflexit.common.swing.ProgressMonitor;
import de.enflexit.common.transfer.Download;
import de.enflexit.common.transfer.DownloadThread;
import de.enflexit.common.transfer.Zipper;

/**
 * The Class AgentGuiUpdater.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AgentGuiUpdater extends Thread {

	// --- For the debugging of this class set true -------------
	private boolean debuggingInIDE = false;
	
	public static final long UPDATE_CHECK_PERIOD = 1000*60*60*24; 	// - once a day -
	public static final String UPDATE_SUB_FOLDER = "updates"; 		// - subfolder in the web server directory -
	public static final String UPDATE_VERSION_INFO_FILE = "latestVersion.xml";
	
	//TODO Get repository locations from the preferences
//	private static final String P2_REPOSITORY_LOCATION = "file:///D:/Builds/V2.0.1/repository";
	
	private enum UpdateMode {
		UPDATE_FROM_FILE, UPDATE_FROM_P2_REPOSITORY
	}
	private static UpdateMode updateMode = UpdateMode.UPDATE_FROM_P2_REPOSITORY;
	
	private final String updateSiteAddition = "?key=xml";

	private GlobalInfo globalInfo;
	private VersionInfo versionInfo;
	private ExecutionMode executionMode;
	
	private String updateSite;
	private Integer updateAutoConfiguration;
	private Integer updateKeepDictionary = 1;
	private long updateDateLastChecked = 0;
	
	
	private String alternativeInfoLink;
	
	private String localDownloadPath;
	private String localWebServerPath;
	private String localPropertiesPath;
	
	private UpdateInformation updateInformation;
	
	private String latestVersionInfoFullPath;

	private String localUpdateZipFile;
	private String localUpdateExtractedFolder;

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
		
		this.localDownloadPath = this.globalInfo.getPathDownloads();
		this.localWebServerPath = this.globalInfo.getPathWebServer();
		this.localPropertiesPath = this.globalInfo.getPathProperty(true);
		
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
				Application.getGlobalInfo().doSavePersistedConfiguration();
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
			
		case DEVICE_SYSTEM:
			// --- TODO -------------
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
		if (this.globalInfo.getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverIDE) {
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
		
		if(updateMode == UpdateMode.UPDATE_FROM_P2_REPOSITORY) {
			
			// --- P2-based headless update ---------------
			triggerP2Update();
			
		}else {
			
		
			// --- File-based update ----------------------
			
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
							// --- Unzip the download -------------------
							boolean headlessUnzip = Application.isOperatingHeadless();
							if (headlessUnzip==false) {
								headlessUnzip = this.askBeforeProjectShutdownAndUnzip;
							}
							if (this.unzipUpdateFile(headlessUnzip)==true){
								// --- Clean up or Move to Server directory
								this.handleDownloadedFilesAfterExtraction();
								// --- Move AgentGuiUpdate.jar ---------- 
								if (this.moveAgentGuiUpdaterJar()==true) {
									// --- Do installation --------------
									this.executeAgentGuiUpdater();
									// --- ShutDown ---------------------
									Application.stop();
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
									default:
										// --- Nothing to do here ! ------
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

	}
	
	/**
	 * Moves file 'AgentGuiUpdater.jar' to the main folder.
	 */
	private boolean moveAgentGuiUpdaterJar() {
		
		String extractedFolder = globalInfo.getPathDownloads() + this.localUpdateExtractedFolder;
		String updaterFilePath = extractedFolder + File.separator + this.globalInfo.getFileNameUpdater(false);
		System.out.println("Trying to move file " + updaterFilePath);
		File updaterFile = new File(updaterFilePath);
		if (updaterFile.exists()==true) {
			
			String updaterRootFilePath = globalInfo.getPathBaseDir();
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
			proBui.directory(new File(this.globalInfo.getPathBaseDir()));
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
		Application.getJadePlatform().stop();
		// --- Close open projects --------------
		if (Application.getProjectsLoaded().closeAll()==false) {
			return false;	
		}
		// --- Save FileProperties --------------
		this.globalInfo.doSavePersistedConfiguration();
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
			
			Zipper zipper = CommonComponentFactory.getNewZipper();
			zipper.setHeadlessOperation(visualizeUnzipping);
			zipper.setUnzipZipFolder(this.localUpdateZipFile);
			zipper.setUnzipDestinationFolder(extractFolder);
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
				
				// --- Define download thread -----------------------
				DownloadThread downloadThread4Update = new DownloadThread(this.updateInformation.getDownloadLink(), this.localUpdateZipFile);
				System.out.println("Agent.GUI-Update: Start download ...");
				
				// --------------------------------------------------
				// --- Headless operation mode? ---------------------
				// --------------------------------------------------
				if (Application.isOperatingHeadless()==true) {
					// --- Headless mode ----------------------------
					downloadThread4Update.start();
					while(downloadThread4Update.isFinished()==false) {
						System.out.print(downloadThread4Update.getDownloadProgress() + " % ...");
						try {
							sleep(500);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
					} // end while
					System.out.println("100 %");
					
				} else {
					// --- Operating with user interface ------------
					ProgressMonitor progressMonitor = CommonComponentFactory.getNewProgressMonitor("Agent.GUI - Update", "Agent.GUI - Update", "Download");
					if (this.askBeforeDownload==true) {
						progressMonitor.setVisible(true);	
					}
					progressMonitor.setProgress(0);
					
					downloadThread4Update.start();
					while(downloadThread4Update.isFinished()==false) {
						progressMonitor.setProgress(downloadThread4Update.getDownloadProgress());
						if (progressMonitor.isCanceled()) {
							downloadThread4Update.doCancel();
						}
						try {
							sleep(200);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
					} // end while
					
					progressMonitor.setProgress(100);
					progressMonitor.setVisible(false);
					progressMonitor.dispose();
					progressMonitor = null;
					
				}
				
				// --- Successful download ? --------------
				readyToInstall = downloadThread4Update.wasSuccessful();
				downloadThread4Update = null;
				
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
	
	/**
	 * Triggers the p2-based update process
	 */
	public static void triggerP2Update() {
		
		IStatus updateResult = checkForP2Update(getProvisioningAgent(), getProgressMonitor());
		
		if(updateResult.getSeverity() == IStatus.OK) {
			System.out.println("Updates installed, application should be restarted");
			//TODO restart application
		}
		
	}
	
	/**
	 * Gets the provisioning agent for the update process
	 * @return The provisioning agent
	 */
	private static IProvisioningAgent getProvisioningAgent() {
		
		BundleContext bundleContext = FrameworkUtil.getBundle(AgentGuiUpdater.class).getBundleContext();
		ServiceReference serviceReference = bundleContext.getServiceReference(IProvisioningAgent.SERVICE_NAME);
		
		if(serviceReference == null) {
			return null;
		}
		
		IProvisioningAgent agent = (IProvisioningAgent) bundleContext.getService(serviceReference);
		return agent;
		
	}
	
	/**
	 * Gets a progress monitor to be used for the update process
	 * @return The progress monitor
	 */
	private static IProgressMonitor getProgressMonitor() {
		//TODO Add a case for non-headless execution providing a progress visualization
		return new NullProgressMonitor();
	}
	
	/**
	 * Checks for available p2 updates, if successful triggers the update installation
	 * @param agent The provisioning agent to be used for the update process
	 * @param monitor The progress monitor to be used to keep track of the update process 
	 * @return The update result status
	 */
	private static IStatus checkForP2Update(IProvisioningAgent agent, IProgressMonitor monitor) {
		
		 ProvisioningSession session = new ProvisioningSession(agent);
		 UpdateOperation operation = getUpdateOperation(session);
		 
		 SubMonitor subMonitor = SubMonitor.convert(monitor, "Checking for Updates...", 200);
		 IStatus status = operation.resolveModal(subMonitor.newChild(100));
		 
		 // --- No updates found ----------------
		 if(status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
			 System.out.println("P2 Update: No update found!");
			 return status;
		 }
		 
		 // --- Update canceled -----------------
		 if(status.getSeverity() == IStatus.CANCEL) {
			 throw new OperationCanceledException();
		 }
		 
		 // --- Everything fie, start updating ------
		 if(status.getSeverity() != IStatus.ERROR) {
			 performP2Update(operation, subMonitor.newChild(100));
		 }
		
		return status;
	}
	
	/**
	 * Performs the update.
	 * @param operation the {@link UpdateOperation} to be used
	 * @param monitor the monitor The progress monitor to be used
	 * @return the return status
	 */
	private static IStatus performP2Update(UpdateOperation operation, IProgressMonitor monitor) {
		System.out.println("P2 update: Updates found, starting update process...");
		
		// --- Initialize the provisioning job -------
		ProvisioningJob job = operation.getProvisioningJob(null);
		if (job == null) {
            System.err.println("Trying to update from the Eclipse IDE? This won't work!");
            return Status.CANCEL_STATUS;
        }
		
		// --- Run the update job --------------
		IStatus status = job.runModal(monitor);
		if(status.getSeverity() == IStatus.CANCEL) {
			throw new OperationCanceledException();
		}
		System.out.println("P2 update: Update done!");
		return status;
	}
	
	/**
	 * Create and initialize the {@link UpdateOperation}
	 * @param session the {@link ProvisioningSession} for this {@link UpdateOperation}
	 * @return The {@link UpdateOperation}
	 */
	private static UpdateOperation getUpdateOperation(ProvisioningSession session) {
		
		UpdateOperation operation = new UpdateOperation(session);
		
		RepositoryTracker repoTracker = ProvisioningUI.getDefaultUI().getRepositoryTracker();
		URI[] repos = repoTracker.getKnownRepositories(session);
		
		ProvisioningContext pc = operation.getProvisioningContext();
		
		pc.setArtifactRepositories(repos);
		pc.setMetadataRepositories(repos);
		
		return operation;
	}
	
}
