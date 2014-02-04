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
package agentgui.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo.ExecutionMode;

/**
 * This class manages the properties that are located in the
 * file /AgentGUI/properties/agentgui.ini.
 * In the Application class the running instance can be accessed 
 * by accessing the reference Application.Properties.
 * 
 * @see Application#getGlobalInfo()
 * @see GlobalInfo#getFileProperties()
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class FileProperties extends Properties {

	private static final long serialVersionUID = 7953205356494195952L;
	private final String defaultUpdateSite = "http://update.agentgui.org";
	
	private GlobalInfo globalInfo = null;
	private VersionInfo versionInfo = null;
	
	private String configFile = null;
	private String configFileDefaultComment = "";

	private final String DEF_RUNAS = "01_RUNAS";
	private final String DEF_RUNAS_Application = "Application";
	private final String DEF_RUNAS_Server = "Server";
	private final String DEF_RUNAS_EmeddedSystemAgent = "EmbeddedSystemAgent";
	
	private final String DEF_BENCH_VALUE = "02_BENCH_VALUE";
	private final String DEF_BENCH_EXEC_ON = "03_BENCH_EXEC_ON";
	private final String DEF_BENCH_SKIP_ALLWAYS = "04_BENCH_SKIP_ALLWAYS";

	private final String DEF_LANGUAGE = "05_LANGUAGE";
	
	private final String DEF_AUTOSTART = "10_AUTOSTART";
	private final String DEF_MASTER_URL = "11_MASTER_URL";
	private final String DEF_MASTER_PORT = "12_MASTER_PORT";
	private final String DEF_MASTER_PORT4MTP = "13_MASTER_PORT4MTP";
	
	private final String DEF_MASTER_DB_HOST = "20_MASTER_DB_HOST";
	private final String DEF_MASTER_DB_NAME = "21_MASTER_DB_NAME";
	private final String DEF_MASTER_DB_USER = "22_MASTER_DB_USER";
	private final String DEF_MASTER_DB_PSWD = "23_MASTER_DB_PSWD";
	
	private final String DEF_GOOGLE_API_KEY  = "30_GOOGLE_API_KEY";
	private final String DEF_GOOGLE_HTTP_REF = "31_GOOGLE_HTTP_REF";
	
	private final String DEF_UPDATE_SITE = "35_UPDATE_SITE";
	private final String DEF_UPDATE_AUTOCONFIG = "36_UPDATE_AUTOCONFIG";
	private final String DEF_UPDATE_KEEP_DICTIONARY = "37_UPDATE_KEEP_DICTIONARY";
	private final String DEF_UPDATE_DATE_LAST_CHECKED = "38_UPDATE_DATE_LAST_CHECKED";
	
	private String[] mandatoryProps = {	this.DEF_RUNAS,
										this.DEF_BENCH_VALUE,
										this.DEF_BENCH_EXEC_ON,
										this.DEF_BENCH_SKIP_ALLWAYS,
										this.DEF_LANGUAGE,
										this.DEF_AUTOSTART,
										this.DEF_MASTER_URL,
										this.DEF_MASTER_PORT,
										this.DEF_MASTER_PORT4MTP,
										this.DEF_UPDATE_SITE,
										this.DEF_UPDATE_AUTOCONFIG,
										this.DEF_UPDATE_KEEP_DICTIONARY,
										this.DEF_UPDATE_DATE_LAST_CHECKED
										};
	
	/**
	 * Default constructor of this class. Will use the default config-file 'agentgui.xml'
	 */
	public FileProperties(GlobalInfo globalInfo) {
		
		this.globalInfo = globalInfo;
		this.versionInfo = this.globalInfo.getVersionInfo();
		this.configFile = this.globalInfo.PathConfigFile(true);
		
		this.initialize();
//		println4SysProps();
//		println4EnvProps();
	}
	/**
	 * Initialises the instance of this class
	 */
	private void initialize() {

		// --- set the Default-Comment for config file --------------
		this.setDefaultComment();
		
		// --- open or create the config file -----------------------
		try {
			// --- Does the configFile exists ? ---------------------
			if (new File(configFile).exists()==true) {
				// --- configFile found -----------------------------
				this.load(new FileInputStream(configFile));	
				this.checkDefaultConfigValues();
			} else {
				// --- configFile NOT found -------------------------
				this.setDefaultConfigValues();
				this.setConfig2Global();
				this.save();
			}				
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		// --- set values of the config-file to global --------------
		this.setConfig2Global();
		
	}
	
	/**
	 * Overrides the super-method in order to sort the entries, when 
	 * the store-method will be invoked during the save()-method is
	 * invoked.
	 */
	public synchronized Enumeration<Object> keys() {
		Enumeration<Object> keysEnum = super.keys();
		Vector<Object> keySorted = new Vector<Object>();
		Vector<String> keyList = new Vector<String>();
		while (keysEnum.hasMoreElements()) {
			keyList.add((String) keysEnum.nextElement());
		}
		Collections.sort(keyList);
		for (Iterator<String> iterator = keyList.iterator(); iterator.hasNext();) {
			String singelEntry = iterator.next();
			keySorted.add(singelEntry);
		}
		return keySorted.elements();
	}
	
	/**
	 * This method sets the values from the config-file to the
	 * Runtime Variables in class Global ('Application.RunInfo')
	 */
	private void setConfig2Global() {
		
		String propValue = "";
		
		// --- this.DEF_RUNAS ------------------------
		propValue = this.getProperty(this.DEF_RUNAS).trim();
		if (propValue.equalsIgnoreCase(this.DEF_RUNAS_Server)==true) {
			Application.getGlobalInfo().setExecutionMode(ExecutionMode.SERVER);
		} else if (propValue.equalsIgnoreCase(this.DEF_RUNAS_EmeddedSystemAgent)==true) {
			Application.getGlobalInfo().setExecutionMode(ExecutionMode.DEVICE_SYSTEM);
		} else {
			Application.getGlobalInfo().setExecutionMode(ExecutionMode.APPLICATION);
		}
		
		// --- this.DEF_BENCH_VALUE ------------------
		propValue = this.getProperty(this.DEF_BENCH_VALUE).trim();
		if ( propValue.equalsIgnoreCase("") == true ) {
			Application.getGlobalInfo().setBenchValue(0);
		} else {
			Application.getGlobalInfo().setBenchValue(Float.parseFloat(propValue));
		}
		// --- this.DEF_BENCH_EXEC_ON ----------------
		propValue = this.getProperty(this.DEF_BENCH_EXEC_ON).trim();
		if ( propValue.equalsIgnoreCase("") == true ) {
			Application.getGlobalInfo().setBenchExecOn(null);
		} else {
			Application.getGlobalInfo().setBenchExecOn(propValue);
		}
		// --- this.DEF_BENCH_SKIP_ALLWAYS -----------
		propValue = this.getProperty(this.DEF_BENCH_SKIP_ALLWAYS).trim();
		if ( propValue.equalsIgnoreCase("true") == true ) {
			Application.getGlobalInfo().setBenchAllwaysSkip(true);
		} else {
			Application.getGlobalInfo().setBenchAllwaysSkip(false);
		}
		
		// --- this.DEF_LANGUAGE ---------------------
		propValue = this.getProperty(this.DEF_LANGUAGE).trim();
		if ( propValue!=null ) {
			Application.getGlobalInfo().setLanguage(propValue);
		} else {
			Application.getGlobalInfo().setLanguage("en");
		}
		
		// --- this.DEF_AUTOSTART --------------------
		propValue = this.getProperty(this.DEF_AUTOSTART).trim();
		if ( propValue.equalsIgnoreCase("true") == true ) {
			Application.getGlobalInfo().setServerAutoRun(true);
		} else {
			Application.getGlobalInfo().setServerAutoRun(false);
		}
		// --- this.DEF_MASTER_URL -------------------
		propValue = this.getProperty(this.DEF_MASTER_URL).trim();
		if ( propValue.equalsIgnoreCase("") == false ) {
			Application.getGlobalInfo().setServerMasterURL(propValue.trim());
		} else {
			Application.getGlobalInfo().setServerMasterURL(null);
		}
		// --- this.DEF_MASTER_PORT ------------------
		propValue = this.getProperty(this.DEF_MASTER_PORT).trim();
		if ( propValue.equalsIgnoreCase("") == false ) {
			Integer propValueInt = Integer.parseInt(propValue.trim());
			Application.getGlobalInfo().setServerMasterPort(propValueInt);
		} else {
			Application.getGlobalInfo().setServerMasterPort(0);
		}
		// --- this.DEF_MASTER_PORT4MTP --------------
		propValue = this.getProperty(this.DEF_MASTER_PORT4MTP);
		if ( propValue.equalsIgnoreCase("") == false ) {
			Integer propValueInt = Integer.parseInt(propValue.trim());
			Application.getGlobalInfo().setServerMasterPort4MTP(propValueInt);
		} else {
			Application.getGlobalInfo().setServerMasterPort4MTP(0);
		}
		
		// --- this.DEF_MASTER_DB_HOST ---------------
		propValue = this.getProperty(this.DEF_MASTER_DB_HOST);
		if ( propValue!=null &&  propValue.equalsIgnoreCase("") == false ) {
			Application.getGlobalInfo().setServerMasterDBHost(propValue.trim());
		} else {
			Application.getGlobalInfo().setServerMasterDBHost(null);
		}
		// --- this.DEF_MASTER_DB_NAME ---------------
		propValue = this.getProperty(this.DEF_MASTER_DB_NAME);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			Application.getGlobalInfo().setServerMasterDBName(propValue.trim());
		} else {
			Application.getGlobalInfo().setServerMasterDBName(null);
		}
		// --- this.DEF_MASTER_DB_USER ---------------
		propValue = this.getProperty(this.DEF_MASTER_DB_USER);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			Application.getGlobalInfo().setServerMasterDBUser(propValue.trim());
		} else {
			Application.getGlobalInfo().setServerMasterDBUser(null);
		}
		// --- this.DEF_MASTER_DB_PSWD ---------------
		propValue = this.getProperty(this.DEF_MASTER_DB_PSWD);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			Application.getGlobalInfo().setServerMasterDBPswd(propValue.trim());
		} else {
			Application.getGlobalInfo().setServerMasterDBPswd(null);
		}
		
		// --- this.DEF_GOOGLE_API_KEY ---------------
		propValue = this.getProperty(this.DEF_GOOGLE_API_KEY);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			Application.getGlobalInfo().setGoogleKey4API(propValue.trim());
		} else {
			Application.getGlobalInfo().setGoogleKey4API(null);
		}
		// --- this.DEF_GOOGLE_HTTP_REF --------------
		propValue = this.getProperty(this.DEF_GOOGLE_HTTP_REF);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			Application.getGlobalInfo().setGoogleHttpRef(propValue.trim());
		} else {
			Application.getGlobalInfo().setGoogleHttpRef(null);
		}
		
		// --- this.DEF_UPDATE_SITE ------------------
		propValue = this.getProperty(this.DEF_UPDATE_SITE);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			Application.getGlobalInfo().setUpdateSite(propValue.trim());
		} else {
			Application.getGlobalInfo().setUpdateSite(null);
		}
		// --- this.DEF_UPDATE_AUTOCONFIG -------------
		propValue = this.getProperty(this.DEF_UPDATE_AUTOCONFIG);
		if ( propValue.equalsIgnoreCase("") == false ) {
			Integer propValueInt = Integer.parseInt(propValue.trim());
			Application.getGlobalInfo().setUpdateAutoConfiguration(propValueInt);
		} else {
			Application.getGlobalInfo().setUpdateAutoConfiguration(0);
		}
		// --- this.DEF_UPDATE_KEEP_DICTIONARY --------
		propValue = this.getProperty(this.DEF_UPDATE_KEEP_DICTIONARY);
		if ( propValue.equalsIgnoreCase("") == false ) {
			Integer propValueInt = Integer.parseInt(propValue.trim());
			Application.getGlobalInfo().setUpdateKeepDictionary(propValueInt);
		} else {
			Application.getGlobalInfo().setUpdateKeepDictionary(1);
		}
		// --- this.DEF_UPDATE_DATE_LAST_CHECKED ------
		propValue = this.getProperty(this.DEF_UPDATE_DATE_LAST_CHECKED);
		if ( propValue.equalsIgnoreCase("") == false ) {
			Long propValueInt = Long.parseLong(propValue.trim());
			Application.getGlobalInfo().setUpdateDateLastChecked(propValueInt);
		} else {
			Application.getGlobalInfo().setUpdateDateLastChecked(0);
		}
		
	}

	/**
	 * This method sets the values from the Runtime Variables in class Global ('Application.RunInfo')
	 * to this property-file / config-file / 'agentgui.xml' 
	 */
	private void setGlobal2Config() {
		
		// --- this.DEF_RUNAS ------------------------
		switch (Application.getGlobalInfo().getExecutionMode()) {
		case APPLICATION:
			this.setProperty(this.DEF_RUNAS, this.DEF_RUNAS_Application);
			break;
		case DEVICE_SYSTEM:
			this.setProperty(this.DEF_RUNAS, this.DEF_RUNAS_EmeddedSystemAgent);
			break;
		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			this.setProperty(this.DEF_RUNAS, this.DEF_RUNAS_Server);
			break;
		}
		
		// --- this.DEF_BENCH_VALUE ------------------
		this.setProperty(this.DEF_BENCH_VALUE, Application.getGlobalInfo().getBenchValue().toString());
		// --- this.DEF_BENCH_EXEC_ON ----------------
		if (Application.getGlobalInfo().getBenchExecOn()!=null) {
			this.setProperty(this.DEF_BENCH_EXEC_ON, Application.getGlobalInfo().getBenchExecOn());	
		}
		// --- this.DEF_BENCH_SKIP_ALLWAYS -----------
		if ( Application.getGlobalInfo().isBenchAllwaysSkip() == true ) {
			this.setProperty(this.DEF_BENCH_SKIP_ALLWAYS,"true");	
		} else {
			this.setProperty(this.DEF_BENCH_SKIP_ALLWAYS,"false");
		}
		
		// --- this.DEF_LANGUAGE ---------------------
		if (Application.getGlobalInfo().getLanguage()==null) {
			this.setProperty(this.DEF_LANGUAGE, "en");			
		} else {
			this.setProperty(this.DEF_LANGUAGE, Application.getGlobalInfo().getLanguage());
		}

		
		// --- this.DEF_AUTOSTART --------------------
		if ( Application.getGlobalInfo().isServerAutoRun() == true ) {
			this.setProperty(this.DEF_AUTOSTART, "true");
		} else {
			this.setProperty(this.DEF_AUTOSTART, "false");
		}
		// --- this.DEF_MASTER_URL -------------------
		if (Application.getGlobalInfo().getServerMasterURL() == null) {
			this.setProperty(this.DEF_MASTER_URL, "");
		} else {
			this.setProperty(this.DEF_MASTER_URL, Application.getGlobalInfo().getServerMasterURL());	
		}

		// --- this.DEF_MASTER_PORT ------------------
		this.setProperty(this.DEF_MASTER_PORT, Application.getGlobalInfo().getServerMasterPort().toString());

		// --- this.DEF_MASTER_PORT4MTP --------------
		this.setProperty(this.DEF_MASTER_PORT4MTP, Application.getGlobalInfo().getServerMasterPort4MTP().toString());

		
		// --- this.DEF_MASTER_DB_HOST ---------------
		if (Application.getGlobalInfo().getServerMasterDBHost() == null) {
			this.setProperty(this.DEF_MASTER_DB_HOST, "");
		} else {
			this.setProperty(this.DEF_MASTER_DB_HOST, Application.getGlobalInfo().getServerMasterDBHost());	
		}
		// --- this.DEF_MASTER_DB_NAME ---------------
		if (Application.getGlobalInfo().getServerMasterDBName() == null) {
			this.setProperty(this.DEF_MASTER_DB_NAME, "");
		} else {
			this.setProperty(this.DEF_MASTER_DB_NAME, Application.getGlobalInfo().getServerMasterDBName());	
		}
		// --- this.DEF_MASTER_DB_USER ---------------
		if (Application.getGlobalInfo().getServerMasterDBUser() == null) {
			this.setProperty(this.DEF_MASTER_DB_USER, "");
		} else {
			this.setProperty(this.DEF_MASTER_DB_USER, Application.getGlobalInfo().getServerMasterDBUser());	
		}
		// --- this.DEF_MASTER_DB_PSWD ---------------
		if (Application.getGlobalInfo().getServerMasterDBPswd() == null) {
			this.setProperty(this.DEF_MASTER_DB_PSWD, "");
		} else {
			this.setProperty(this.DEF_MASTER_DB_PSWD, Application.getGlobalInfo().getServerMasterDBPswd());	
		}
		
		// --- this.DEF_GOOGLE_API_KEY ---------------
		if (Application.getGlobalInfo().getGoogleKey4API() == null) {
			this.setProperty(this.DEF_GOOGLE_API_KEY, "");
		} else {
			this.setProperty(this.DEF_GOOGLE_API_KEY, Application.getGlobalInfo().getGoogleKey4API());	
		}
		// --- this.DEF_GOOGLE_HTTP_REF --------------
		if (Application.getGlobalInfo().getGoogleKey4API() == null) {
			this.setProperty(this.DEF_GOOGLE_HTTP_REF, "");
		} else {
			this.setProperty(this.DEF_GOOGLE_HTTP_REF, Application.getGlobalInfo().getGoogleHttpRef());	
		}
		
		// --- this.DEF_UPDATE_SITE ------------------
		if (Application.getGlobalInfo().getUpdateSite() == null) {
			this.setProperty(this.DEF_UPDATE_SITE, "");
		} else {
			this.setProperty(this.DEF_UPDATE_SITE, Application.getGlobalInfo().getUpdateSite());	
		}
		// --- this.DEF_UPDATE_AUTOCONFIG -------------
		this.setProperty(this.DEF_UPDATE_AUTOCONFIG, Application.getGlobalInfo().getUpdateAutoConfiguration().toString());	
		// --- this.DEF_UPDATE_KEEP_DICTIONAR ---------		
		this.setProperty(this.DEF_UPDATE_KEEP_DICTIONARY, Application.getGlobalInfo().getUpdateKeepDictionary().toString());
		// --- this.DEF_UPDATE_DATE_LAST_CHECKED ------
		this.setProperty(this.DEF_UPDATE_DATE_LAST_CHECKED, Application.getGlobalInfo().getUpdateDateLastChecked().toString());
		
	}	
	
	/**
	 * This method sets the mandatory properties with default values to this properties 
	 */
	private void setDefaultConfigValues() {

		for (int i = 0; i < mandatoryProps.length; i++) {
			
			// --- Here the mandatory properties in general --------- 
			this.setProperty( mandatoryProps[i], "" );

			// --- Here some special mandatory properties ----------- 
			if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_LANGUAGE) ) {				
				this.setProperty(this.DEF_LANGUAGE, Language.EN);
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_MASTER_PORT) ) {				
				this.setProperty(this.DEF_MASTER_PORT, Application.getGlobalInfo().getJadeLocalPort().toString());
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_MASTER_PORT4MTP) ) {				
				this.setProperty(this.DEF_MASTER_PORT4MTP, Application.getGlobalInfo().getServerMasterPort4MTP().toString());
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_UPDATE_SITE) ) {				
				this.setProperty(this.DEF_UPDATE_SITE, "http://update.agentgui.org");
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_UPDATE_AUTOCONFIG) ) {				
				this.setProperty(this.DEF_UPDATE_AUTOCONFIG, "0");
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_UPDATE_KEEP_DICTIONARY) ) {
				this.setProperty(this.DEF_UPDATE_KEEP_DICTIONARY, "1");
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_UPDATE_DATE_LAST_CHECKED) ) {
				this.setProperty(this.DEF_UPDATE_DATE_LAST_CHECKED, "0");
				
			}	
			
		} // end for
		
	}

	/**
	 * This method checks if some mandatory properties in the
	 * the config-file are available. If not, they will be added. 
	 */
	private void checkDefaultConfigValues() {
		
		boolean somethingMissed = false;
		// --- Search all mandantory property-values ------
		for (int i = 0; i < mandatoryProps.length; i++) {
			
			if (this.containsKey(mandatoryProps[i])==false) {
				// ----------------------------------------
				// --- Mandatory property is NOT there ----
				// --- => Set default valid value      ----
				// ----------------------------------------
				if (mandatoryProps[i].equals(this.DEF_LANGUAGE)) {
					this.setProperty(mandatoryProps[i], Language.EN);
				} else if (mandatoryProps[i].equals(this.DEF_BENCH_SKIP_ALLWAYS)) {
					this.setProperty(mandatoryProps[i], "true");
				} else if (mandatoryProps[i].equals(this.DEF_UPDATE_SITE)) {
					this.setProperty(mandatoryProps[i], this.defaultUpdateSite);
				} else {
					this.setProperty(mandatoryProps[i], "");	
				}
				somethingMissed = true;
				// ----------------------------------------
				
			} else {
				// ----------------------------------------
				// --- Mandatory property IS there --------
				// --- => Check for valid value    --------
				// ----------------------------------------
				String value = this.getProperty(mandatoryProps[i]);
				if (mandatoryProps[i].equals(this.DEF_LANGUAGE)) {
					if (value.equals("")) {
						this.setProperty(mandatoryProps[i], Language.EN);
						somethingMissed = true;
					}
				} else if (mandatoryProps[i].equals(this.DEF_BENCH_SKIP_ALLWAYS)) {
					if (value.equals("")) {
						this.setProperty(mandatoryProps[i], "true");
						somethingMissed = true;
					}
				} else if (mandatoryProps[i].equals(this.DEF_UPDATE_SITE)) {
					if (value.equals("")) {
						this.setProperty(mandatoryProps[i], this.defaultUpdateSite);
						somethingMissed = true;
					}
				}
				// ----------------------------------------
			}

		}
		// --- If something was not there, save the file --
		if ( somethingMissed == true ) {
			this.setConfig2Global();
			this.save();
		}
	}
	
	/**
	 * This method will set the default comments to the property file.
	 * This text is hard coded !
	 */
	private void setDefaultComment() {

		String defaultComment = "";		
		defaultComment = defaultComment + " Configuration of " + globalInfo.getApplicationTitle() + " (Version: " + versionInfo.getFullVersionInfo(false, " ") + ")\n"; 
		defaultComment = defaultComment + " by Christian Derksen - DAWIS - ICB - University Duisburg-Essen\n";
		defaultComment = defaultComment + " Email: christian.derksen@icb.uni-due.de\n";
		configFileDefaultComment = defaultComment;
	}
	
	/**
	 * This method saves the current settings to the property file
	 */
	public void save() {
		// --- getting the current values of the mandatory variables -----
		this.setGlobal2Config();
		// --- Save the configuration-file -------------------------------
		try {
			this.store(new FileOutputStream(configFile), configFileDefaultComment);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method prints out every available value of the system properties
	 */
	public void println4SysProps() {

		Properties sysprops = System.getProperties();
		System.out.println();
		System.out.println("------------------------------------");  
		System.out.println("------- System Properties: ---------");  
		System.out.println("------------------------------------");
		Enumeration<?> names = sysprops.propertyNames();  
		while (names.hasMoreElements()) {  
			String tmp = names.nextElement().toString();  
			System.out.println(tmp + "=" + sysprops.getProperty(tmp));  
		}  
	}

	/**
	 * This method prints out every available value of the system environment
	 */
	public void println4EnvProps() {
		
		System.out.println();
		System.out.println("------------------------------------");  
		System.out.println("-------  System Environment: -------"); 
		System.out.println("------------------------------------");
		Map<String, String> env = System.getenv();  
		for (String str : env.keySet()) {  
			System.out.println(str + "=" + System.getenv(str));  
		} 
	}

}
