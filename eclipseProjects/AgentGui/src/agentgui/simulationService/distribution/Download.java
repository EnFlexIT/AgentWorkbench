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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * This class enables the download from a given URL to a local destination folder.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Download {

	private String srcFileURL;
	private String destFileLocale;
	
	private Integer downloadProgress = 0;
	private boolean downloadSuccessful = false;
	private boolean downloadFinished = false;
	
	private boolean cancel = false;
	
	/**
	 * Instantiates a new download.
	 *
	 * @param sourceFileURL the URL of the source file 
	 * @param destinationFileLocal the local destination file 
	 */
	public Download(String sourceFileURL, String destinationFileLocal) {
		// --- Set the local variables ----
		this.srcFileURL  = sourceFileURL;
		this.destFileLocale = destinationFileLocal;
	}
	
	/**
	 * Starts the download.
	 */
	public void startDownload() {
		// --- Download the required file -----------------
		this.downloadSuccessful = this.downloadFile();
		this.downloadFinished = true;
	}
	
	/**
	 * Will download the configured file.
	 * @return true, if successful
	 */
	private boolean downloadFile(){
		
		URL url;
		HttpURLConnection huc;
		byte[] buffer = new byte[4096] ;
		int totBytes, bytes, sumBytes = 0;

		String host = null;
		InputStream is = null;
		try {
			// --- Connect to the SourceObject -----------
			url = new URL(srcFileURL);
			host = url.getHost();
			huc = (HttpURLConnection) url.openConnection();
			huc.connect();
			
			// --- Checking the connection ----------------
			is = huc.getInputStream() ;
			int code = huc.getResponseCode() ; 
			if ( code == HttpURLConnection.HTTP_OK )   {  

				// --- Define Output-File -----------------
				File fileDownloaded = new File(destFileLocale) ; 
				FileOutputStream outputStream = new FileOutputStream(fileDownloaded) ; 
				
				// --- Proceed ----------------------------
				totBytes = huc.getContentLength() ; 
				while ((bytes = is.read(buffer)) > 0) {
					if (this.cancel==true) {
						break;
					}
					outputStream.write(buffer, 0, bytes);
	                sumBytes+= bytes; 
	                downloadProgress = (Math.round( (((float)sumBytes / (float)totBytes) * 100) ));
				}
				// --- Done -------------------------------
				outputStream.close();
				huc.disconnect();
				if (this.cancel==true) {
					fileDownloaded.delete();
					return false;
				} else {
					return true;	
				}
				
		    }
		    huc.disconnect () ; 
		    return false;
		   
		} catch (FileNotFoundException fnf) {
			System.out.println("File not found on host! Please retry later.");
		} catch (UnknownHostException uh) {
			System.out.println("Host not found: " + host);
		} catch (NoRouteToHostException nr2h) {
			System.out.println("No Route to host found: " + host);
		} catch (ConnectException ce) {
			System.out.println("Connection to '" + host + "' timed out: ");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is!=null) is.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return false;
	}
	
	
	/**
	 * Cancels the currently running download.
	 */
	public synchronized void doCancel() {
		this.cancel = true;
	}

	/**
	 * Informs about a finished download.
	 * @return true, if the download is finished
	 */
	public synchronized boolean isFinished() {
		return this.downloadFinished;
	}

	/**
	 * Informs if the download was successful.
	 * @return true, if the download was successful 
	 */
	public synchronized boolean wasSuccessful() {
		return this.downloadSuccessful;
	}

	/**
	 * Informs about the current download progress.
	 * @return the downloadProgress
	 */
	public synchronized Integer getDownloadProgress() {
		return downloadProgress;
	}

}
