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

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.common.Zipper;
import agentgui.simulationService.distribution.Download;
import agentgui.simulationService.distribution.DownloadThread;

/**
 * The Class AgentGuiUpdater.
 */
public class AgentGuiUpdater extends Thread {

	private final String updateSiteAddition = "?key=xml";
	
	private String updateSite = null;
	private Integer updateAutoConfiguration = null;
	
	private String downloadPath = null;
	private String propertiesPath = null;
	
	private UpdateInformation updateInformation = null;
	private String latestVersionInfoFile = "latestVersion.xml";
	private String latestVersionInfoFullPath = null;
	
	private DownloadThread downloadThread4Update = null;
	private AgentGuiUpdaterMonitor progressMonitor = null;

	private String localUpdateZipFile = null;
	private String localUpdateExtractedFolder = null;
	
	
	/**
	 * Instantiates a new Agent.GUI updater process.
	 */
	public AgentGuiUpdater() {
	
		this.setName("Agent.GUI-Updater");
		
		Application.getGlobalInfo();
		updateSite = Application.getGlobalInfo().getUpdateSite();
		updateAutoConfiguration = Application.getGlobalInfo().getUpdateAutoConfiguration();
		
		downloadPath = Application.getGlobalInfo().PathDownloads(true);
		propertiesPath = Application.getGlobalInfo().PathProperty(true);
	}
	
	@Override
	public void run() {
		super.run();
		
		// --- Download information about latest version -- 
		String srcFileURL = this.updateSite + this.updateSiteAddition;
		this.latestVersionInfoFullPath = this.downloadPath + this.latestVersionInfoFile; 
		Download infoDownload = new Download(srcFileURL, this.latestVersionInfoFullPath);
		infoDownload.startDownload();
		boolean loadUpdateInformation = infoDownload.isFinished() && infoDownload.wasSuccessful();
		infoDownload = null;
		
		if (loadUpdateInformation==true) {
			// --------------------------------------------
			// --- Load the UpdateInformation -------------
			this.updateInformation = new UpdateInformation();
			this.updateInformation.loadUpdateInformation(this.latestVersionInfoFullPath);
			if (this.updateInformation!=null) {
				// --- Set name of the update zip file ----
				this.localUpdateZipFile = this.downloadPath + this.updateInformation.getDownloadFile();
				this.localUpdateExtractedFolder = this.updateInformation.getDownloadFile().replace(".zip", "");
				
				// ----------------------------------------
				// --- Download now -----------------------
				boolean readyToInstall = this.downloadUpdateFile();
//				boolean readyToInstall = true; // --- For debugging later executions ---
				if (readyToInstall==true) {
					// ------------------------------------
					// --- Copy latest info to properties --
					String destPath = this.propertiesPath + this.latestVersionInfoFile;
					File latestVersionInfoFileProperties = new File(destPath);
					if (latestVersionInfoFileProperties.exists()) {
						latestVersionInfoFileProperties.delete();
					}
					File latestVersionInfoFile = new File(this.latestVersionInfoFullPath);
					latestVersionInfoFile.renameTo(latestVersionInfoFileProperties);
					
					// ------------------------------------
					// --- Unzip the downloaded file ------
					if (this.unzipUpdateFile()==true){
						if (isPreparedForInstallation()==true) {
							// --- Do installation --------
							this.executeAgentGuiUpdater();	
							// --- ShutDown ---------------
							System.out.println("Finalize update / starting AgentGuiUpdate.jar / shut down application!");
							System.exit(0);		
							return;
							
						} else {
							System.out.println("Agent.GUI-Update: Not prepared for installation! Please close all open projects.");
						}
					} else {
						System.err.println("Agent.GUI-Update: Unsuccessful unzipping!");
					}
					
				} else {
					// ------------------------------------
					// --- Download unsuccessful ----------
					System.err.println("Agent.GUI-Update: Unsuccessful download !");
					File downloadedFile = new File(this.localUpdateZipFile);
					if (downloadedFile.exists()) {
						downloadedFile.delete();
					}
				} // end readyToInstall==true after download
			}// end this.updateInformation!=null
		}//end loadUpdateInformation==true

	}
	
	/**
	 * Executes the AgentGuiUpdater.jar in the same folder.
	 */
	private void executeAgentGuiUpdater() {
		
		System.out.println("Doing installation now!");
		// --- create execute statement -----------------------------
		String execute = "java -jar " + Application.getGlobalInfo().getFileNameUpdater(false) + " -updated -?";
		System.out.println( "=> Re-Execute: Agent.GUI");
		// ----------------------------------------------------------
		try {
			String[] arg = execute.split(" ");
			ProcessBuilder proBui = new ProcessBuilder(arg);
			proBui.redirectErrorStream(false);
			proBui.directory(new File(Application.getGlobalInfo().PathBaseDir()));
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
		Application.getGlobalInfo().getFileProperties().save();
		Language.saveDictionaryFile();
		return true;
	}
	
	/**
	 * Unzip the local update file.
	 * @return true, if successful
	 */
	private boolean unzipUpdateFile() {
		
		boolean done = false;
		
		File zipFolderFile = new File(this.localUpdateZipFile);
		if (zipFolderFile.exists()==true) {

			String extractFolder = zipFolderFile.getParent() + File.separator + this.localUpdateExtractedFolder;
			
			Zipper zipper = new Zipper();
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
				
				this.progressMonitor = new AgentGuiUpdaterMonitor();
				this.progressMonitor.setVisible(true);
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
	
}
