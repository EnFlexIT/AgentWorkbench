package de.enflexit.awb.ws.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.osgi.framework.Version;

import de.enflexit.language.Language;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * The Class WebApplicationVersion.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlRootElement(name= "WebApplicationVersion")
public class WebApplicationVersion implements Comparable<WebApplicationVersion> {

	public static final String XM_FILE_NAME = "WebApplicationVersion.xml";
	
	private String fileName;
	private String updateURL;
	
	private transient Version version;

	
	/**
	 * Instantiates a new web application version.
	 */
	public WebApplicationVersion() {
		this(null, null);
	}
	/**
	 * Instantiates a new web application version.
	 *
	 * @param fileName the file name
	 * @param updateURL the update URL
	 */
	public WebApplicationVersion(String fileName, String updateURL) {
		this.setFileName(fileName);
		this.setUpdateURL(updateURL);
	}
	
	
	/**
	 * Sets the file name.
	 * @param fileName the new file name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * Returns the file name.
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}
	
	
	/**
	 * Sets the update URL.
	 * @param updateURL the new update URL
	 */
	public void setUpdateURL(String updateURL) {
		this.updateURL = updateURL;
	}
	/**
	 * Returns the update URL.
	 * @return the update URL
	 */
	public String getUpdateURL() {
		return updateURL;
	}
	
	
	/**
	 * Returns the version of the specified file name.
	 * @return the version
	 */
	public Version getVersion() {
		if (version==null) {
			String versionString = this.getFileName().substring(this.getFileName().indexOf("_")+1);
			versionString = versionString.substring(0, versionString.lastIndexOf("."));
			versionString = versionString.replace("_", ".");
			version = Version.parseVersion(versionString);
		}
		return version;
	}

	/**
	 * Returns the download link for the current web application version.
	 * @return the download link
	 */
	public String getDownloadLink() {
		String downloadDir = WebApplicationUpdateList.getLocationPathExcludingRepositoryFile(this.getUpdateURL(), true);
		downloadDir += this.getFileName();
		return downloadDir;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "v" + this.getVersion() + " - File: " + this.getFileName(); 
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(WebApplicationVersion wavComp) {
		
		if (wavComp==null) return -1;
		
		int vpComp = 0;
		vpComp = this.compareVersionPart(this.getVersion().getMajor(), wavComp.getVersion().getMajor()); 
		if (vpComp!=0) return vpComp;
		
		vpComp = this.compareVersionPart(this.getVersion().getMinor(), wavComp.getVersion().getMinor()); 
		if (vpComp!=0) return vpComp;
		
		vpComp = this.compareVersionPart(this.getVersion().getMicro(), wavComp.getVersion().getMicro()); 
		if (vpComp!=0) return vpComp;
		
		return this.getFileName().compareTo(wavComp.getFileName());
	}
	/**
	 * Compares to version parts.
	 *
	 * @param vp1 the vp 1
	 * @param vp2 the vp 2
	 * @return the int
	 */
	private int compareVersionPart(Integer vp1, Integer vp2) {
		return vp1.compareTo(vp2);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		
		if (compObj==null) return false;
		if (compObj instanceof WebApplicationVersion == false) return false;
		
		// --- Use compare method for equal check -----
		WebApplicationVersion waVersionComp = (WebApplicationVersion) compObj;
		return (this.compareTo(waVersionComp)==0);
	}
	
	
	// ------------------------------------------------------------------------
	// --- From here, methods to load and save this instances ----------------- 
	// ------------------------------------------------------------------------	
	/**
	 * Saves the current version information to the specified file.
	 *
	 * @param xmlFileName the xml file name
	 * @return true, if successful
	 */
	public boolean save(String xmlFileName) {

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
	 * Loads the specified version file.
	 *
	 * @param xmlFileName the xml file name
	 * @return the web application version
	 */
	public static WebApplicationVersion load(String xmlFileName) {
		
		// --- Does the fileList exists -----------------------
		File xmlFile = new File(xmlFileName);
		if (xmlFile.exists()==false) {
			System.out.println(Language.translate("Datei oder Verzeichnis wurde nicht gefunden:") + " " + xmlFileName);
			return null;
		}

		// --- Read XML fileList ------------------------------
		WebApplicationVersion waVersion = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(xmlFileName);
			JAXBContext pc = JAXBContext.newInstance(WebApplicationVersion.class);
			Unmarshaller um = pc.createUnmarshaller();
			waVersion = (WebApplicationVersion) um.unmarshal(fileReader);

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
		return waVersion;
	}
	
}
