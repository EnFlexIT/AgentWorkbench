package de.enflexit.awb.ws.core.util;

import java.io.File;
import java.nio.file.Path;

import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.common.transfer.Download;
import de.enflexit.common.transfer.RecursiveFolderDeleter;
import de.enflexit.common.transfer.Zipper;

/**
 * The Class WebApplicationUpdateProcess.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class WebApplicationUpdateProcess extends Thread {

	private WebApplicationVersion webAppVersion;
	private boolean isRestartServer;

	private WebApplicationUpdateProcessListener processListener;
	
	/**
	 * Instantiates a new web application update process.
	 * @param webAppVersion the actual version of the web application to install
	 */
	public WebApplicationUpdateProcess(WebApplicationVersion webAppVersion) {
		this(webAppVersion, false, null);
	}
	/**
	 * Instantiates a new web application update process.
	 *
	 * @param webAppVersion the actual version of the web application to install
	 * @param listener the WebApplicationUpdateProcessListener
	 */
	public WebApplicationUpdateProcess(WebApplicationVersion webAppVersion, WebApplicationUpdateProcessListener listener) {
		this(webAppVersion, false, listener);	
	}
	/**
	 * Instantiates a new web application update process.
	 *
	 * @param webAppVersion the actual version of the web application to install
	 * @param isRestartServer the is restart server
	 */
	public WebApplicationUpdateProcess(WebApplicationVersion webAppVersion, boolean isRestartServer) {
		this(webAppVersion, isRestartServer, null);
	}
	/**
	 * Instantiates a new web application update process.
	 *
	 * @param webAppVersion the actual version of the web application to install
	 * @param isRestartServer the is restart server
	 * @param listener the WebApplicationUpdateProcessListener
	 */
	public WebApplicationUpdateProcess(WebApplicationVersion webAppVersion, boolean isRestartServer, WebApplicationUpdateProcessListener listener) {
		this.setWebApplicationVersion(webAppVersion);
		this.setRestartServer(isRestartServer);
		this.processListener = listener;
		this.setName(this.getClass().getSimpleName());
	}
	
	/**
	 * Sets the actual version of the web application to install.
	 * @param webAppVersion the new web app version
	 */
	public void setWebApplicationVersion(WebApplicationVersion webAppVersion) {
		this.webAppVersion = webAppVersion;
	}
	/**
	 * Return the actual version of the web application to install.
	 * @return the web app version
	 */
	public WebApplicationVersion getWebApplicationVersion() {
		return webAppVersion;
	}
	
	/**
	 * Checks if is restart server.
	 * @return true, if is restart server
	 */
	public boolean isRestartServer() {
		return isRestartServer;
	}
	/**
	 * Sets the restart server.
	 * @param isRestartServer the new restart server
	 */
	public void setRestartServer(boolean isRestartServer) {
		this.isRestartServer = isRestartServer;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		
		try {
			// --- Check, if the downloadLink is valid ------------------------
			String downloadLink = this.getWebApplicationVersion().getDownloadLink();
			if (WebApplicationUpdate.isValidDownloadURL(downloadLink)==false) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Invalid download URL '" + downloadLink + "' - Exit update process");
				return;
			}
			
			// --- Download the new web application ---------------------------
			String webAppArchiveNew = BundleHelper.getWebDownloadDirectory(true).toPath().resolve(this.getWebApplicationVersion().getFileName()).toString();
			if (this.fileExists(webAppArchiveNew)==false) {
				Download download = new Download(downloadLink, webAppArchiveNew);
				download.startDownload();
			}
			
			// ----------------------------------------------------------------
			// --- Old content handling ---------------------------------------
			// ----------------------------------------------------------------
			// --- Archive the current web application ------------------------
			String archiveFileName = this.getArchiveFileNameForCurrentWebApplication();
			String webAppArchiveOld = BundleHelper.getWebApplicationArchiveDirectory(true).toPath().resolve(archiveFileName).toString();
			if (this.fileExists(webAppArchiveOld)==true) {
				// --- Delete previous version --------------------------------
				new File(webAppArchiveOld).delete();
			}
			
			// --- Collect all files and directories within web root ----------
			File file = new File(BundleHelper.getWebRootDirectory(true).getAbsolutePath());
			File[] subFileArray = file.listFiles();
			if (subFileArray!=null && subFileArray.length>0) {
				// --- Start to zip the content ------------------------------- 
				Zipper zipper = new Zipper();
				zipper.setRunInThread(false);
				zipper.setHideProgress(true);
				zipper.setZipFolder(webAppArchiveOld);
				for (File subFile : subFileArray) {
					zipper.addZipSourceFileOrDirectory(subFile);
				}
				zipper.doZipFolder();
			}
			
			// --- Empty the web directory ------------------------------------
			for (File subFile : file.listFiles()) {
				if (subFile.isDirectory()==true) {
					new RecursiveFolderDeleter().deleteFolder(subFile.toPath());
				} else {
					subFile.delete();
				}
			}

			// ----------------------------------------------------------------
			// --- New content area -------------------------------------------
			// ----------------------------------------------------------------
			// --- Save the version information beside the application --------
			this.getWebApplicationVersion().save(WebApplicationUpdate.getCurrentWebApplicationVersionFilePath().toString());
			// --- Extract the new web application to the destination folder ------
			Zipper unZipper = new Zipper();
			unZipper.setRunInThread(false);
			unZipper.setHideProgress(true);
			unZipper.setUnzipZipFolder(webAppArchiveNew);
			unZipper.setUnzipDestinationFolder(BundleHelper.getWebRootDirectory(true).getAbsolutePath());
			unZipper.doUnzipFolder();
			
			// ----------------------------------------------------------------
			// --- Listener actions--------------------------------------------
			// ----------------------------------------------------------------
			if (this.processListener!=null) {
				this.processListener.onUpdateProcessFinalized();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
		
	/**
	 * Returns the archive file name for current web application.
	 * @return the archive file name for current web application
	 */
	private String getArchiveFileNameForCurrentWebApplication() {
		
		String fileName = "WebApplicationArchive.zip";
		
		// --- Try getting the WebApplicationVersion ----------------
		Path xmlFileNamePath = WebApplicationUpdate.getCurrentWebApplicationVersionFilePath();
		if (xmlFileNamePath.toFile().exists()==true) {
			WebApplicationVersion waVersion = WebApplicationVersion.load(xmlFileNamePath.toString());
			if (waVersion!=null) {
				fileName = waVersion.getFileName();
			}
		}
		return fileName;
	}
	/**
	 * File exists.
	 *
	 * @param fileNamePath the file name path
	 * @return true, if successful
	 */
	private boolean fileExists(String fileNamePath) {
		return new File(fileNamePath).exists();
	}
	
}
