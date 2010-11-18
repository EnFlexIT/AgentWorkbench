package agentgui.simulationService.distribution;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Download {

	private String srcFileURL;
	private String destFileLocale;
	private Integer downloadProgress;
	private boolean downloadSuccessful = false;
	private boolean downloadFinished = false;
	
	
	public Download( String sourceFileURL, String destinationFileLocal ) {
		// --- Set the local variables ----
		this.srcFileURL  = sourceFileURL;
		this.destFileLocale = destinationFileLocal;
		// --- Download the required file -----------------
		this.downloadSuccessful = this.downloadFile();
		this.downloadFinished = true;
	}
	
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
	 * Retruns if a download is finished or not
	 * @return true / false
	 */
	public boolean isFinished() {
		return this.downloadFinished;
	}

	/**
	 * Retruns if a download was successful or not
	 * @return true / false
	 */
	public boolean wasSuccessful() {
		return this.downloadSuccessful;
	}

	/**
	 * @return the downloadProgress
	 */
	public Integer getDownloadProgress() {
		return downloadProgress;
	}

}
