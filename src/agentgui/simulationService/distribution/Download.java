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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class enables the download from a given URL to a local destination folder.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Download {

	private String srcFileURL;
	private String destFileLocale;
	private Integer downloadProgress;
	private boolean downloadSuccessful = false;
	private boolean downloadFinished = false;
	
	
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
		// --- Download the required file -----------------
		this.downloadSuccessful = this.downloadFile();
		this.downloadFinished = true;
	}
	
	/**
	 * Will download the configured file.
	 *
	 * @return true, if successful
	 */
	private boolean downloadFile(){
		
		URL url;
		HttpURLConnection huc;
		byte[] buffer = new byte[4096] ;
		int totBytes, bytes, sumBytes = 0;
		
		try {
			// --- Connect to the SourceObject -----------
			url = new URL(srcFileURL);
			huc = (HttpURLConnection) url.openConnection();
			huc.connect();
			
			// --- Checking the connection ----------------
			InputStream is = huc.getInputStream() ;
			int code = huc.getResponseCode() ; 
			if ( code == HttpURLConnection.HTTP_OK )   {  

				// --- Define Output-File -----------------
				File output = new File(destFileLocale) ; 
				FileOutputStream outputStream= new FileOutputStream(output) ; 
				
				// --- Proceed ----------------------------
				totBytes = huc.getContentLength() ; 
				while ((bytes = is.read(buffer)) > 0) {
					outputStream.write(buffer, 0, bytes);
	                sumBytes+= bytes; 
	                downloadProgress = (Math.round( (((float)sumBytes / (float)totBytes) * 100) ));
				}
				huc.disconnect() ;
				return true;
		    }
		    huc.disconnect () ; 
		    return false;
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Informs about a finished download.
	 * @return true, if the download is finished
	 */
	public boolean isFinished() {
		return this.downloadFinished;
	}

	/**
	 * Informs if the download was successful.
	 * @return true, if the download was successful 
	 */
	public boolean wasSuccessful() {
		return this.downloadSuccessful;
	}

	/**
	 * Informs about the current download progress.
	 * @return the downloadProgress
	 */
	public Integer getDownloadProgress() {
		return downloadProgress;
	}

}
