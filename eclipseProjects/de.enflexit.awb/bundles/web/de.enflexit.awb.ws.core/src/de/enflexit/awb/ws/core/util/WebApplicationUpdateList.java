package de.enflexit.awb.ws.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.enflexit.common.http.HttpURLConnector;
import de.enflexit.common.http.HttpURLConnectorException;
import de.enflexit.common.http.WebResourcesAuthorization;
import de.enflexit.language.Language;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * The Class WebApplicationUpdateList is used to read the fileList version.php from an WebApp update site.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlRootElement(name= "WebApplicationUpdateList")
public class WebApplicationUpdateList {

	private final static String VERSION_SCRIPT = "version.php";
	
	@XmlElementWrapper(name = "FileList")
	@XmlElement(name = "File")
	private List<String> fileList;
	
	/**
	 * Returns the fileList list.
	 * @return the fileList list
	 */
	@XmlTransient
	public List<String> getFileList() {
		if (fileList==null) {
			fileList= new ArrayList<>();
		}
		return fileList;
	}
	/**
	 * Sets the fileList list.
	 * @param fileList the new fileList list
	 */
	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}
	
	
	/**
	 * Save web application update list.
	 */
	public boolean saveWebApplicationUpdateList(String xmlFileName) {
		
		boolean success = true;
		try {
		
			if (xmlFileName==null || xmlFileName.isBlank()==true) return false;
			
			// --- Prepare Context and Marshaller ---------
			JAXBContext pc = JAXBContext.newInstance(this.getClass());
			Marshaller pm = pc.createMarshaller();
			pm.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			pm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			// --- Write values to xml-File ---------------
			Writer pw = new FileWriter(xmlFileName);
			pm.marshal(this, pw);
			pw.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		}
		return success;
	}
	
	/**
	 * Load WebApplicationUpdateList from the specified fileList.
	 * @return the web application update list
	 */
	public static WebApplicationUpdateList loadWebApplicationUpdateList(String xmlFileName) {
		
		// --- Does the fileList exists -----------------------
		File xmlFile = new File(xmlFileName);
		if (xmlFile.exists()==false) {
			System.out.println(Language.translate("Datei oder Verzeichnis wurde nicht gefunden:") + " " + xmlFileName);
			return null;
		}

		// --- Read XML fileList ------------------------------
		WebApplicationUpdateList waul = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(xmlFileName);
			JAXBContext pc = JAXBContext.newInstance(WebApplicationUpdateList.class);
			Unmarshaller um = pc.createUnmarshaller();
			waul = (WebApplicationUpdateList) um.unmarshal(fileReader);

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return null;
		} catch (JAXBException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				if (fileReader!=null) fileReader.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return waul;
	}
	
	/**
	 * Loads a project repository from the specified URL.
	 *
	 * @param webURL the web URL
	 * @param auth the WebResourcesAuthorization
	 * @return the project repository
	 * @throws HttpURLConnectorException the project repository update exception
	 */
	public static WebApplicationUpdateList loadWebApplicationVersions(URL webURL, WebResourcesAuthorization auth) throws HttpURLConnectorException, IllegalArgumentException {
		
		if (webURL==null) return null;
		
		WebApplicationUpdateList webAppVersions = null;
		
		// --- Check for the right destination fileList ----------------- 
		String sourceURLPath = getLocationPathIncludingRepositoryFile(webURL.toString(), true);
		
		HttpURLConnection httpConnection = null;
		InputStream is = null;
		try {
			// --- Open Http connection ----------------------------
			httpConnection = HttpURLConnector.openConnection(sourceURLPath, auth); 
			is = httpConnection.getInputStream();

			// --- Define JAXB stuff -------------------------------- 
			JAXBContext pc = JAXBContext.newInstance(WebApplicationUpdateList.class);
			Unmarshaller um = pc.createUnmarshaller();
			webAppVersions = (WebApplicationUpdateList) um.unmarshal(is);
			
		} catch (Exception ex) {
			System.err.println("[" + WebApplicationUpdateList.class.getSimpleName() + "] Error while resolving available web application versions from " + webURL);
			//ex.printStackTrace();
			
		} finally {
			if (is!=null) {
				try {
					is.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				if (httpConnection!=null) {
					try {
						httpConnection.disconnect();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		return webAppVersions;
	}
	/**
	 * Returns the location path including the file list script.
	 *
	 * @param initialLocation the initial location
	 * @param isWebLink indicates if the specified initial location is a web link. if not a fileList is assumed
	 * @return the location including repository fileList
	 */
	public static String getLocationPathIncludingRepositoryFile(String initialLocation, boolean isWebLink) {
		String location = initialLocation;
		if (location.endsWith(VERSION_SCRIPT)==false) {
			String fileSeparator = File.separator;
			if (isWebLink==true)  fileSeparator = "/";
			if (location.endsWith(fileSeparator)==false) location+=fileSeparator; 
			location+=VERSION_SCRIPT;
		}
		return location; 
	}
	
	/**
	 * Returns the location path excluding the file list script.
	 *
	 * @param initialLocation the initial location
	 * @param isWebLink indicates if the specified initial location is a web link. if not a fileList is assumed
	 * @return the location including repository fileList
	 */
	public static String getLocationPathExcludingRepositoryFile(String initialLocation, boolean isWebLink) {
		String location = initialLocation;
		if (location.endsWith(VERSION_SCRIPT)==true) {
			location = location.replace(VERSION_SCRIPT, "");
			String fileSeparator = File.separator;
			if (isWebLink==true)  fileSeparator = "/";
			if (location.endsWith(fileSeparator)==false) location+=fileSeparator; 
		}
		return location; 
	}
	
}
