package de.enflexit.awb.core.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.nio.file.Path;
import java.util.HashSet;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.project.Project;
import de.enflexit.common.StringHelper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The Class AwbUiConfiguration enables to configure the {@link AwbMainWindow} as well as the {@link AwbProjectWindow}.
 * The corresponding file ('uiConfig.xml') is either stored in the properties directory or in the root of a project directory.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AwbUiConfiguration", propOrder = {
    "applicationTitle",
    "applicationIconFileName",
    "hiddenMenus",
    "hiddenToolBarGroups",
    "hideProjectTree",
    "hideProjectTabHeader",
    "hiddenProjectTabs"
})
public class AwbUiConfiguration implements Serializable {

	private static final long serialVersionUID = -8469276950440040073L;

	public static String MAIN_WINDOW_CONFIGURATION_FILE_NAME = "uiConfig.xml";

	private transient File xmlConfigurationFile;
	
	@XmlElement(name = "applicationTitle")
	private String applicationTitle;
	@XmlElement(name = "applicationIconFileName")
	private String applicationIconFileName;
	
	@XmlElementWrapper(name = "hiddenMenus")
	@XmlElement(name = "hiddenMenu")
	private HashSet<AwbMainWindowMenu> hiddenMenus;

	@XmlElementWrapper(name = "hiddenToolbars")
	@XmlElement(name = "hiddenToolbar")
	private HashSet<AwbMainWindowToolBarGroup> hiddenToolBarGroups;
	
	@XmlElement(name = "hideProjectTree")
	private boolean hideProjectTree;
	@XmlElement(name = "hideProjectTabHeader")
	private boolean hideProjectTabHeader;
	
	@XmlElementWrapper(name = "hiddenProjectTabs")
	@XmlElement(name = "hiddenProjectTab")
	private HashSet<AwbProjectTab> hiddenProjectTabs;
	
	
	/**
	 * Instantiates a new main window configuration.
	 */
	public AwbUiConfiguration() { }
	/**
	 * Instantiates a new main window configuration.
	 * @param configurationFile the actual file instance to use for saving the XML file
	 */
	public AwbUiConfiguration(File configurationFile) {
		this.setXmlConfigurationFile(configurationFile);
	}
	/**
	 * Instantiates a new main window configuration.
	 * @param project the project 
	 */
	public AwbUiConfiguration(Project project) {
		this(AwbUiConfiguration.getProjectConfigurationFile(project));
	}

	/**
	 * Returns the file instance of the XML configuration file.
	 * @return the XML configuration file
	 */
	public File getXmlConfigurationFile() {
		return xmlConfigurationFile;
	}
	/**
	 * Sets the file instance of the XML configuration file..
	 * @param xmlConfigurationFile the new XML configuration file
	 */
	public void setXmlConfigurationFile(File newXmlConfigurationFile) {
		this.xmlConfigurationFile = newXmlConfigurationFile;
	}
	
	
	/**
	 * Returns the application title.
	 * @return the application title
	 */
	@XmlTransient
	public String getApplicationTitle() {
		return applicationTitle;
	}
	/**
	 * Sets the application title.
	 * @param applicationTitle the new application title
	 */
	public void setApplicationTitle(String applicationTitle) {
		this.applicationTitle = applicationTitle;
	}
	
	
	/**
	 * Returns the application icon file name.
	 * @return the application icon file name
	 */
	@XmlTransient
	public String getApplicationIconFileName() {
		return applicationIconFileName;
	}
	/**
	 * Sets the application icon file name.
	 * @param applicationIconFileName the new application icon file name
	 */
	public void setApplicationIconFileName(String applicationIconFileName) {
		this.applicationIconFileName = applicationIconFileName;
	}
	
	
	/**
	 * Returns the hidden menus.
	 * @return the hidden menus
	 */
	@XmlTransient
	public HashSet<AwbMainWindowMenu> getHiddenMenus() {
		if (hiddenMenus==null) {
			hiddenMenus = new HashSet<>();
		}
		return hiddenMenus;
	}
	/**
	 * Sets the hidden menus.
	 * @param hiddenMenus the new hidden menu
	 */
	public void setHiddenMenus(HashSet<AwbMainWindowMenu> hiddenMenus) {
		this.hiddenMenus = hiddenMenus;
	}
	
	
	/**
	 * Returns the hidden tool bar groups.
	 * @return the hidden tool bar groups
	 */
	@XmlTransient
	public HashSet<AwbMainWindowToolBarGroup> getHiddenToolBarGroups() {
		if (hiddenToolBarGroups==null) {
			hiddenToolBarGroups = new HashSet<>();
		}
		return hiddenToolBarGroups;
	}
	/**
	 * Sets the hidden tool bar groups.
	 * @param hiddenToolBarGroups the new hidden tool bar groups
	 */
	public void setHiddenToolBarGroups(HashSet<AwbMainWindowToolBarGroup> hiddenToolBarGroups) {
		this.hiddenToolBarGroups = hiddenToolBarGroups;
	}
	
	
	/**
	 * Checks if the is hide project tree.
	 * @return true, if is show project tree
	 */
	@XmlTransient
	public boolean isHideProjectTree() {
		return hideProjectTree;
	}
	/**
	 * Sets the hide project tree.
	 * @param hideProjectTree the new show project tree
	 */
	public void setHideProjectTree(boolean showProjectTree) {
		this.hideProjectTree = showProjectTree;
	}

	/**
	 * Checks if is hide tab header.
	 * @return true, if is show tab header
	 */
	@XmlTransient
	public boolean isHideTabHeader() {
		return hideProjectTabHeader;
	}
	/**
	 * Sets to hide tab header.
	 * @param hideProjectTabHeader the new show tab header
	 */
	public void setHideTabHeader(boolean showTabHeader) {
		this.hideProjectTabHeader = showTabHeader;
	}

	/**
	 * Returns the hidden project tabs.
	 * @return the hidden project tabs
	 */
	@XmlTransient
	public HashSet<AwbProjectTab> getHiddenProjectTabs() {
		return hiddenProjectTabs;
	}
	/**
	 * Sets the hidden project tabs.
	 * @param hiddenProjectTabs the new hidden project tabs
	 */
	public void setHiddenProjectTabs(HashSet<AwbProjectTab> hiddenProjectTabs) {
		this.hiddenProjectTabs = hiddenProjectTabs;
	}
	
	
	/**
	 * Saves the current MainWindowConfiguraion.
	 * @return true, if successful
	 */
	public boolean save() {
		return AwbUiConfiguration.save(this);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		
		if (compObj==null || compObj instanceof AwbUiConfiguration == false) return false;
		if (compObj==this) return true;
		
		AwbUiConfiguration compConfig = (AwbUiConfiguration) compObj;
		
		if (compConfig.getXmlConfigurationFile().equals(this.getXmlConfigurationFile())==false) return false;
		
		if (StringHelper.isEqualString(compConfig.getApplicationTitle(), this.getApplicationTitle())==false) return false;
		if (StringHelper.isEqualString(compConfig.getApplicationIconFileName(), this.getApplicationIconFileName())==false) return false;
		
		if (compConfig.getHiddenMenus().equals(this.getHiddenMenus())==false) return false;
		if (compConfig.getHiddenToolBarGroups().equals(this.getHiddenToolBarGroups())==false) return false;
		
		if (compConfig.isHideProjectTree()!=this.isHideProjectTree()) return false;
		if (compConfig.isHideTabHeader()!=this.isHideTabHeader()) return false;
		if (compConfig.getHiddenProjectTabs().equals(this.getHiddenProjectTabs())==false) return false;
		
		return true;
	}
	
	
	
	/**
	 * Returns the default {@link AwbUiConfiguration} file.
	 * @return the default configuration file
	 */
	public static File getDefaultConfigurationFile() {
		return Application.getGlobalInfo().getPathProperty(true).resolve(MAIN_WINDOW_CONFIGURATION_FILE_NAME).toFile();
	}
	/**
	 * Returns the currents project {@link AwbUiConfiguration} file.
	 * @return the project configuration file
	 */
	public static File getProjectConfigurationFile(Project project) {
		if (project==null) return null;
		return Path.of(project.getProjectFolderFullPath()).resolve(MAIN_WINDOW_CONFIGURATION_FILE_NAME).toFile();
	}
	
	/**
	 * Saves the specified MainWindowConfiguraion to the specified file.
	 *
	 * @param mvc the MainWindowConfiguraion to save
	 * @param configFile the configuration file
	 * @return true, if successful
	 */
	public static boolean save(AwbUiConfiguration mvc) {

		if (mvc==null || mvc.getXmlConfigurationFile()==null) return false;

		boolean success = false;

		JAXBContext pc = null;
		Writer fw = null;;
		try {
			// --- Prepare Context and Marshaller ---------
			pc = JAXBContext.newInstance(AwbUiConfiguration.class);
			Marshaller pm = pc.createMarshaller();
			pm.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			pm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			// --- Write values to xml-File ---------------
			fw = new FileWriter(mvc.getXmlConfigurationFile());
			pm.marshal(mvc, fw);
			success = true;
			
		} catch (JAXBException jaxbEx) {
			jaxbEx.printStackTrace();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} finally {
			try {
				if (fw != null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}
	
	/**
	 * Loads the AwbUiConfiguration from the default file.
	 * @return may return the AwbUiConfiguration or <code>null</code> in case of errors or if the configuration file cannot be found.
	 */
	public static AwbUiConfiguration load() {
		return AwbUiConfiguration.load(AwbUiConfiguration.getDefaultConfigurationFile());
	}
	/**
	 * Loads the AwbUiConfiguration for the specified Project.
	 *
	 * @param project the project to load the UI configuration for
	 * @return may return the AwbUiConfiguration or <code>null</code> in case of errors or if the configuration file cannot be found.
	 */
	public static AwbUiConfiguration load(Project project) {
		File uiConigFile = AwbUiConfiguration.getProjectConfigurationFile(project); 
		if (uiConigFile==null) return null;
		return AwbUiConfiguration.load(AwbUiConfiguration.getProjectConfigurationFile(project));
	}
	/**
	 * Loads a AwbUiConfiguration from the specified file if the file cannot be found it return a new instance .
	 *
	 * @param configFile the configuration file to load
	 * @return may return the AwbUiConfiguration or <code>null</code> in case of errors or if the configuration file cannot be found.
	 */
	public static AwbUiConfiguration load(File configFile) {
		
		if (configFile==null || configFile.exists()==false) return null;
		
		// --- Read file 'uiConfig.xml' -------------------
		AwbUiConfiguration mwc = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(configFile);
			JAXBContext pc = JAXBContext.newInstance(AwbUiConfiguration.class);
			Unmarshaller um = pc.createUnmarshaller();

			mwc = (AwbUiConfiguration) um.unmarshal(fileReader);
			mwc.setXmlConfigurationFile(configFile);
			return mwc;
			
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (JAXBException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (fileReader!=null) fileReader.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return null;
	}
	
}
