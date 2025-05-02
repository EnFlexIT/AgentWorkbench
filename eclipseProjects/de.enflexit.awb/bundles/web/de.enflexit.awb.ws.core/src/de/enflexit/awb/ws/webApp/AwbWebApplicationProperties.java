package de.enflexit.awb.ws.webApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import de.enflexit.awb.ws.core.JettyWebApplicationSettings.UpdateStrategy;
import de.enflexit.common.PathHandling;
import de.enflexit.common.properties.Properties;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The Class AwbWebApplicationProperties represents the base setting of a web application 
 * that is stored in a file within the properties directory.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "awbWebApplicationProperties", propOrder = {
    "webAppName",
    "properties"
})
public class AwbWebApplicationProperties {

	public static final String DEFAULT_WEB_APP_DOWNLOAD_URL = "web.app.download-url";
	public static final String DEFAULT_WEB_APP_UPDATE_STRATEGY = "web.app.update-strategy";
	
	private String webAppName;
	private Properties properties;
	
	/**
	 * Instantiates a new instance of AWB web application properties.
	 */
	public AwbWebApplicationProperties() {
		this(null, null);
	}
	/**
	 * Instantiates a new instance of AWB web application properties.
	 *
	 * @param webAppName the web application name
	 * @param properties the properties
	 */
	public AwbWebApplicationProperties(String webAppName, Properties properties) {
		this.setWebApplicationName(webAppName);
		this.setProperties(properties);
	}
	
	/**
	 * Returns the web application name.
	 * @return the web application name
	 */
	public String getWebApplicationName() {
		return webAppName;
	}
	/**
	 * Sets the web application name.
	 * @param webAppName the new web application name
	 */
	public void setWebApplicationName(String webAppName) {
		this.webAppName = webAppName;
	}
	
	/**
	 * Returns the properties.
	 * @return the properties
	 */
	public Properties getProperties() {
		if (properties==null) {
			properties = new Properties();
		}
		return properties;
	}
	/**
	 * Sets the properties.
	 * @param properties the new properties
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	/**
	 * Adds the default web application properties.
	 */
	public void addDefaultWebApplicationProperties() {
	
		Properties webAppProps = this.getProperties();
		List<String> idList = webAppProps.getIdentifierList();

		// --- Application settings -----------------------
		if (idList.contains(DEFAULT_WEB_APP_DOWNLOAD_URL)==false)		webAppProps.setStringValue(DEFAULT_WEB_APP_DOWNLOAD_URL, "");
		if (idList.contains(DEFAULT_WEB_APP_UPDATE_STRATEGY)==false)	webAppProps.setStringValue(DEFAULT_WEB_APP_UPDATE_STRATEGY, UpdateStrategy.Automatic.toString());
		
		// --- DB-settings --------------------------------
		boolean containsDbEntries = idList.contains(HibernateDatabaseService.HIBERNATE_PROPERTY_DriverClass);
		if (containsDbEntries==false) {
			// --- Get available DB systems ---------------
			List<String> dbSystemList = HibernateUtilities.getDatabaseSystemList(false);
			if (dbSystemList.size()>0 ) {
				// --- Create default DB settings ---------
				String dbSystemName = dbSystemList.get(0);
				HibernateDatabaseService dbService = HibernateUtilities.getDatabaseService(dbSystemName);
				
				java.util.Properties dbDefaultProps = dbService.getHibernateDefaultPropertySettings();
				List<Object> keyList = new ArrayList<>(dbDefaultProps.keySet());
				for (int i = 0; i < keyList.size(); i++) {
					String key   = (String) keyList.get(i);
					String value = (String) dbDefaultProps.get(key);
					if (idList.contains(key)==false) {
						webAppProps.setStringValue(key, value);
					}
				}
				
			} else {
				AwbWebApplicationManager.LOGGER.error("Not a single database service could be found: Add one of the available database systems to the activated bundles");
				
			}
		}
		
	}
	
	
	/**
	 * Saves the current settings.
	 */
	public boolean save() {
		return AwbWebApplicationProperties.save(this, getFileWebApplicationProperties(this.getWebApplicationName()));
	}
	
	// --------------------------------------------------------------
	// --- From here static help methods for loading and saving -----
	// --------------------------------------------------------------
	/**
	 * Returns the web application file name.
	 *
	 * @param webAppName the web application name
	 * @return the web application file name
	 */
	private static String getWebApplicationPropertiesFileName(String webAppName) {
		String webApplicationFileName = webAppName.trim();
		webApplicationFileName = webApplicationFileName.replace(" ", "-");
		return webApplicationFileName + ".xml";
	}
	/**
	 * Returns the path web application property settings.
	 *
	 * @param webAppName the web app name
	 * @return the path web application settings
	 */
	public static Path getPathWebApplicationProperties(String webAppName) {
		Path pathProperties = PathHandling.getPropertiesPath(AwbWebApplicationManager.getAwbWebApplication().getClass(), true, true);
		return pathProperties.resolve(AwbWebApplicationProperties.getWebApplicationPropertiesFileName(webAppName));
	}
	/**
	 * Returns the file for the web application property settings.
	 *
	 * @param webAppName the web app name
	 * @return the file web application settings
	 */
	public static File getFileWebApplicationProperties(String webAppName) {
		return AwbWebApplicationProperties.getPathWebApplicationProperties(webAppName).toFile();
	}
	
	
	/**
	 * Saves the specified instance of web app properties to the specified file.
	 *
	 * @param appProperties the AwbWebApplicationProperties to save
	 * @param fileToSave the file to save
	 * @return true, if successful
	 */
	public static boolean save(AwbWebApplicationProperties appProperties, File fileToSave) {
		
		boolean success = false;
		
		// --- Prepare Context and Marshaller ---------
		Writer writer = null;
		try {
			JAXBContext cont = JAXBContext.newInstance(AwbWebApplicationProperties.class);
			Marshaller marsh = cont.createMarshaller();
			marsh.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			// --- Write values to xml-File ---------------
			writer = new FileWriter(fileToSave);
			marsh.marshal(appProperties, writer);
			success = true;
			
		} catch (JAXBException | IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (writer!=null) writer.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
		return success;
	}
	
	/**
	 * Loads the AwbWebApplicationProperties for the specified web application name.
	 *
	 * @param fileToLoad the file to load
	 * @return the awb web application properties
	 */
	public static AwbWebApplicationProperties load(String webAppName) {
		return load(AwbWebApplicationProperties.getFileWebApplicationProperties(webAppName));
	}
	/**
	 * Loads the AwbWebApplicationProperties from the specified file.
	 *
	 * @param fileToLoad the file to load
	 * @return the awb web application properties
	 */
	public static AwbWebApplicationProperties load(File fileToLoad) {

		// --- Check for file errors --------------------------------
		if (fileToLoad==null) {
			AwbWebApplicationManager.LOGGER.error("No file was specified to load the web application properties!");
			return null;
		} else if (fileToLoad.exists()==false) {
			AwbWebApplicationManager.LOGGER.error("The specified file to load the web application properties does not exists! => File is: '" + fileToLoad.getAbsolutePath() + "'");
			return null;
		}
		
		// --- Load / Read the XML file -----------------------------
		AwbWebApplicationProperties propsLoaded = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(fileToLoad);
			JAXBContext pc = JAXBContext.newInstance(AwbWebApplicationProperties.class);
			Unmarshaller um = pc.createUnmarshaller();
			propsLoaded = (AwbWebApplicationProperties) um.unmarshal(fileReader);

		} catch (JAXBException | FileNotFoundException ex) {
			ex.printStackTrace();
			return null;
			
		} finally {
			try {
				if (fileReader!=null) fileReader.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return propsLoaded;
	}
	
}
