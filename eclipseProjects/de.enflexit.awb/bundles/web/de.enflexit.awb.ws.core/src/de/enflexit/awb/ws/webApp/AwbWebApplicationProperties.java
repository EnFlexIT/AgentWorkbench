package de.enflexit.awb.ws.webApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

import de.enflexit.common.PathHandling;
import de.enflexit.common.properties.Properties;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * The Class AwbWebApplicationProperties represents the base setting of a web application 
 * that is stored in a file within the properties directory.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlRootElement
public class AwbWebApplicationProperties {

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
