package de.enflexit.common.transfer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;

import de.enflexit.common.http.WebResourcesAuthorization;
import de.enflexit.common.http.WebResourcesAuthorization.AuthorizationType;

/**
 * This class enables the download from a given URL to a local destination folder.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Download {

	private String srcFileURL;
	private String destFileLocale;
	private WebResourcesAuthorization webResAuth;
	
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
		this(sourceFileURL, destinationFileLocal, null);
	}
	/**
	 * Instantiates a new download.
	 *
	 * @param sourceFileURL the URL of the source file 
	 * @param destinationFileLocal the local destination file 
	 * @param webResAuth the settings for authorization 
	 */
	public Download(String sourceFileURL, String destinationFileLocal, WebResourcesAuthorization webResAuth) {
		// --- Set the local variables ----
		this.srcFileURL  = sourceFileURL;
		this.destFileLocale = destinationFileLocal;
		this.webResAuth = webResAuth;
		this.checkAndCorrectSourceURL();
	}
	/**
	 * Will check the source URL (e.g. for blanks) and will correct it as required.
	 */
	private void checkAndCorrectSourceURL() {
		
		try {
			//URL url = new URL(URLDecoder.decode(this.srcFileURL, StandardCharsets.UTF_8.toString()));
			URL url = URI.create(srcFileURL).toURL();
	        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
	        String urlStringCleaned = uri.toString();
	        if (urlStringCleaned!=null && urlStringCleaned.equals(this.srcFileURL)==false) {
	        	this.srcFileURL = urlStringCleaned;
	        }
		} catch (MalformedURLException | URISyntaxException urlEx) {
			//urlEx.printStackTrace();
		}
	}
	
	/**
	 * Returns the {@link WebResourcesAuthorization}.
	 * @return the webResAuth
	 */
	public WebResourcesAuthorization getWebResAuth() {
		return webResAuth;
	}
	/**
	 * Sets the web res auth.
	 * @param webResAuth the webResAuth to set
	 */
	public void setWebResAuth(WebResourcesAuthorization webResAuth) {
		this.webResAuth = webResAuth;
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
		byte[] buffer = new byte[4096];
		int totBytes, bytes, sumBytes = 0;

		String host = null;
		InputStream is = null;
		try {
			// --- Connect to the SourceObject -----------
			url = URI.create(srcFileURL).toURL();
			host = url.getHost();
			huc = (HttpURLConnection) url.openConnection();
			WebResourcesAuthorization auth = this.getWebResAuth();
			if (auth!=null && auth.getType()==AuthorizationType.BASIC) {
				huc.setRequestProperty("Authorization", auth.getEncodedHeader());
			}
			huc.connect();
			
			// --- Checking the connection ----------------
			is = huc.getInputStream();
			int code = huc.getResponseCode(); 
			if ( code == HttpURLConnection.HTTP_OK )   {  

				// --- Define Output-File -----------------
				File fileDownloaded = new File(destFileLocale) ; 
				FileOutputStream outputStream = new FileOutputStream(fileDownloaded) ; 
				
				// --- Proceed ----------------------------
				totBytes = huc.getContentLength(); 
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
