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
import java.util.Properties;
import java.util.Vector;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo.DeviceSystemExecutionMode;
import agentgui.core.config.GlobalInfo.EmbeddedSystemAgentVisualisation;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.config.GlobalInfo.MtpProtocol;
import agentgui.core.project.PlatformJadeConfig;
import agentgui.core.project.PlatformJadeConfig.MTP_Creation;

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
	
	private GlobalInfo globalInfo;
	
	private String configFile;
	private String configFileDefaultComment = "";

	private final String DEF_PROJECTS_DIRECTORY = "00_PROJECTS_DIRECTORY";
	private final String DEF_RUNAS = "01_RUNAS";
	private final String DEF_RUNAS_Application = "Application";
	private final String DEF_RUNAS_Server = "Server";
	private final String DEF_RUNAS_EmeddedSystemAgent = "EmbeddedSystemAgent";
	
	private final String DEF_BENCH_VALUE = "02_BENCH_VALUE";
	private final String DEF_BENCH_EXEC_ON = "03_BENCH_EXEC_ON";
	private final String DEF_BENCH_SKIP_ALLWAYS = "04_BENCH_SKIP_ALLWAYS";

	private final String DEF_LANGUAGE = "05_LANGUAGE";
	private final String DEF_MAXIMiZE_MAIN_WINDOW = "06_MAXIMiZE_MAIN_WINDOW";
	
	private final String DEF_LOGGING_ENABLED = "07_LOGGING_ENABLED";
	private final String DEF_LOGGING_BASE_PATH = "08_LOGGING_BASE_PATH";
	
	private final String DEF_AUTOSTART = "10_AUTOSTART";
	private final String DEF_MASTER_URL = "11_MASTER_URL";
	private final String DEF_MASTER_PORT = "12_MASTER_PORT";
	private final String DEF_MASTER_PORT4MTP = "13_MASTER_PORT4MTP";
	private final String DEF_MASTER_PROTOCOL = "14_MASTER_PROTOCOL";

	private final String DEF_OWN_MTP_CREATION = "15_OWN_MTP_CREATION";
	private final String DEF_OWN_MTP_IP = "16_OWN_MTP_IP";
	private final String DEF_OWN_MTP_PORT = "17_OWN_MTP_PORT";
	private final String DEF_OWN_MTP_PROTOCOL = "18_OWN_MTP_PROTOCOL";
	
	private final String DEF_MASTER_DB_HOST = "20_MASTER_DB_HOST";
	private final String DEF_MASTER_DB_NAME = "21_MASTER_DB_NAME";
	private final String DEF_MASTER_DB_USER = "22_MASTER_DB_USER";
	private final String DEF_MASTER_DB_PSWD = "23_MASTER_DB_PSWD";
	
	private final String DEF_GOOGLE_API_KEY  = "30_GOOGLE_API_KEY";
	private final String DEF_GOOGLE_HTTP_REF = "31_GOOGLE_HTTP_REF";
	
	private final String DEF_UPDATE_AUTOCONFIG = "36_UPDATE_AUTOCONFIG";
	private final String DEF_UPDATE_KEEP_DICTIONARY = "37_UPDATE_KEEP_DICTIONARY";
	private final String DEF_UPDATE_DATE_LAST_CHECKED = "38_UPDATE_DATE_LAST_CHECKED";
	
	private final String DEF_DeviceServcie_ProjectFolder = "40_DEVICE_SERVICE_PROJECT";
	private final String DEF_DeviceServcie_ExecAs = "41_DEVICE_SERVICE_EXEC_AS";
	private final String DEF_DeviceServcie_ExecAsService = "Service";
	private final String DEF_DeviceServcie_ExecAsAgent = "Agent";
	private final String DEF_DeviceServcie_Setup = "42_DEVICE_SERVICE_SETUP";
	private final String DEF_DeviceServcie_Vis = "45_DEVICE_SERVICE_VISUALISATION";
	private final String DEF_DeviceServcie_Vis_None = "None";
	private final String DEF_DeviceServcie_Vis_TrayIcon = "TrayIcon";
	
	private final String DEF_KEYSTORE_FILE = "50_KEY_STORE_FILE";
	private final String DEF_KEYSTORE_PASSWORD = "51_KEY_STORE_PASSWORD";
	private final String DEF_TRUSTSTORE_FILE = "52_TRUST_STORE_FILE";
	private final String DEF_TRUSTSTORE_PASSWORD = "53_TRUST_STORE_PASSWORD";
	
	private final String DEF_OIDC_USERNAME = "60_OIDC_USERNAME";
	private final String DEF_OIDC_ISSUER_URI = "61_OIDC_ISSUER_URI";

	private String[] mandatoryProps = {	this.DEF_PROJECTS_DIRECTORY,
										this.DEF_RUNAS,
										this.DEF_BENCH_VALUE,
										this.DEF_BENCH_EXEC_ON,
										this.DEF_BENCH_SKIP_ALLWAYS,
										this.DEF_LANGUAGE,
										this.DEF_AUTOSTART,
										this.DEF_MASTER_URL,
										this.DEF_MASTER_PORT,
										this.DEF_MASTER_PORT4MTP,
										this.DEF_MASTER_PROTOCOL,
										this.DEF_OWN_MTP_CREATION,
										this.DEF_OWN_MTP_PROTOCOL,
										this.DEF_OWN_MTP_IP,
										this.DEF_OWN_MTP_PORT,
										this.DEF_UPDATE_AUTOCONFIG,
										this.DEF_UPDATE_KEEP_DICTIONARY,
										this.DEF_UPDATE_DATE_LAST_CHECKED,
										this.DEF_KEYSTORE_FILE,
										this.DEF_KEYSTORE_PASSWORD,
										this.DEF_TRUSTSTORE_FILE,
										this.DEF_TRUSTSTORE_PASSWORD
										};
	
	/**
	 * Instantiates the file properties and transfers the file properties directly to the {@link GlobalInfo}.
	 * @param globalInfo the instance of the {@link GlobalInfo}
	 */
	public FileProperties(GlobalInfo globalInfo) {
		this(globalInfo, true);
	}
	/**
	 * Instantiates the file properties.
	 *
	 * @param globalInfo the instance of the {@link GlobalInfo}
	 * @param isSetPropertiesToGlobal set <code>true</code> in order to directly set the preferences to the {@link GlobalInfo}
	 */
	public FileProperties(GlobalInfo globalInfo, boolean isSetPropertiesToGlobal) {
		this.globalInfo = globalInfo;
		this.configFile = this.globalInfo.getPathConfigFile(true);
		if (isSetPropertiesToGlobal==true) {
			this.initialize();
		}
	}

	/**
	 * Initializes the instance of this class
	 */
	private void initialize() {

		//println4SysProps();
		//println4EnvProps();
		
		// --- Set the Default-Comment for the file -------------
		this.setDefaultComment();
		
		// --- Does the configFile exists ? ---------------------
		if (new File(configFile).exists()==true) {
			// --- configFile found -----------------------------
			this.load();
			this.checkDefaultConfigValues();
		} else {
			// --- configFile NOT found -------------------------
			this.setDefaultConfigValues();
			this.setConfig2Global();
			this.save();
		}				
		// --- Set values of the file to global -----------------
		this.setConfig2Global();
	
	}
	
	/**
	 * Loads the file properties from file to this instance.
	 */
	public void load() {
		
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(configFile);
			this.load(fis);	
		} catch (FileNotFoundException fnfEx) {
			fnfEx.printStackTrace();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} finally {
			try {
				if (fis!=null) fis.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	/**
	 * This method saves the current settings to the property file
	 */
	public void save() {
		// --- getting the current values of the mandatory variables -----
		this.setGlobal2Config();
		// --- Save the configuration-file -------------------------------
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(configFile);
			this.store(fos, configFileDefaultComment);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos!=null) {
					fos.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	/**
	 * Overrides the super-method in order to sort the entries, when 
	 * the store-method will be invoked during the save()-method is
	 * invoked.
	 */
	@Override
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
		
		// --- this.DEF_PROJECT_DIRECTORY ------------
		propValue = this.getProperty(this.DEF_PROJECTS_DIRECTORY).trim();
		if (propValue!=null && propValue.equalsIgnoreCase("")==true) {
			this.globalInfo.setPathProjects(this.globalInfo.getDefaultProjectsDirectory());
		} else {
			this.globalInfo.setPathProjects(propValue);
		}
		
		
		// --- this.DEF_RUNAS ------------------------
		propValue = this.getProperty(this.DEF_RUNAS).trim();
		if (propValue.equalsIgnoreCase(this.DEF_RUNAS_Server)==true) {
			this.globalInfo.setExecutionMode(ExecutionMode.SERVER);
		} else if (propValue.equalsIgnoreCase(this.DEF_RUNAS_EmeddedSystemAgent)==true) {
			this.globalInfo.setExecutionMode(ExecutionMode.DEVICE_SYSTEM);
		} else {
			this.globalInfo.setExecutionMode(ExecutionMode.APPLICATION);
		}
		
		
		// --- this.DEF_BENCH_VALUE ------------------
		propValue = this.getProperty(this.DEF_BENCH_VALUE).trim();
		if (propValue!=null && propValue.equalsIgnoreCase("") == true ) {
			this.globalInfo.setBenchValue(0);
		} else {
			this.globalInfo.setBenchValue(Float.parseFloat(propValue));
		}
		// --- this.DEF_BENCH_EXEC_ON ----------------
		propValue = this.getProperty(this.DEF_BENCH_EXEC_ON).trim();
		if ( propValue!=null && propValue.equalsIgnoreCase("") == true ) {
			this.globalInfo.setBenchExecOn(null);
		} else {
			this.globalInfo.setBenchExecOn(propValue);
		}
		// --- this.DEF_BENCH_SKIP_ALLWAYS -----------
		propValue = this.getProperty(this.DEF_BENCH_SKIP_ALLWAYS).trim();
		if ( propValue.equalsIgnoreCase("true") == true ) {
			this.globalInfo.setBenchAlwaysSkip(true);
		} else {
			this.globalInfo.setBenchAlwaysSkip(false);
		}
		
		// --- this.DEF_LANGUAGE ---------------------
		propValue = this.getProperty(this.DEF_LANGUAGE).trim();
		if ( propValue!=null ) {
			this.globalInfo.setLanguage(propValue);
		} else {
			this.globalInfo.setLanguage("en");
		}
		
		// --- this.DEF_MAXIMiZE_MAIN_WINDOW ---------
		propValue = this.getProperty(this.DEF_MAXIMiZE_MAIN_WINDOW);
		if (propValue!=null && propValue.trim().equalsIgnoreCase("true") == true) {
			this.globalInfo.setMaximzeMainWindow(true);
		} else {
			this.globalInfo.setMaximzeMainWindow(false);
		}
		
		// --- this.DEF_LOGGING_ENABLED --------------
		propValue = this.getProperty(this.DEF_LOGGING_ENABLED);
		if (propValue!=null && propValue.trim().equalsIgnoreCase("true") == true) {
			this.globalInfo.setLoggingEnabled(true);
		} else {
			this.globalInfo.setLoggingEnabled(false);
		}
		// --- this.DEF_LOGGING_BASE_PATH ------------
		propValue = this.getProperty(this.DEF_LOGGING_BASE_PATH);
		if (propValue!=null && propValue.equalsIgnoreCase("")==false) {
			this.globalInfo.setLoggingBasePath(propValue);
		} else {
			this.globalInfo.setLoggingBasePath(null);
		}
				
		// --- this.DEF_AUTOSTART --------------------
		propValue = this.getProperty(this.DEF_AUTOSTART).trim();
		if ( propValue.equalsIgnoreCase("true") == true ) {
			this.globalInfo.setServerAutoRun(true);
		} else {
			this.globalInfo.setServerAutoRun(false);
		}
		// --- this.DEF_MASTER_URL -------------------
		propValue = this.getProperty(this.DEF_MASTER_URL).trim();
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			this.globalInfo.setServerMasterURL(propValue.trim());
		} else {
			this.globalInfo.setServerMasterURL(null);
		}
		// --- this.DEF_MASTER_PORT ------------------
		propValue = this.getProperty(this.DEF_MASTER_PORT).trim();
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			Integer propValueInt = Integer.parseInt(propValue.trim());
			this.globalInfo.setServerMasterPort(propValueInt);
		} else {
			this.globalInfo.setServerMasterPort(1099);
		}
		// --- this.DEF_MASTER_PORT4MTP --------------
		propValue = this.getProperty(this.DEF_MASTER_PORT4MTP);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			Integer propValueInt = Integer.parseInt(propValue.trim());
			this.globalInfo.setServerMasterPort4MTP(propValueInt);
		} else {
			this.globalInfo.setServerMasterPort4MTP(7778);
		}
		// --- this.DEF_MASTER_PROTOCOL --------------
		propValue = this.getProperty(this.DEF_MASTER_PROTOCOL);
		if (propValue!=null && propValue.equalsIgnoreCase("") == false) {
			MtpProtocol ownMtpProtocol = MtpProtocol.valueOf(propValue);
			this.globalInfo.setServerMasterProtocol(ownMtpProtocol);
		} else {
			this.globalInfo.setServerMasterProtocol(MtpProtocol.HTTP);
		}
		
		// --- this.DEF_OWN_MTP_CREATION -------------
		propValue = this.getProperty(this.DEF_OWN_MTP_CREATION).trim();
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			MTP_Creation ownMtpCreation = MTP_Creation.valueOf(propValue.trim());
			this.globalInfo.setOwnMtpCreation(ownMtpCreation);
		} else {
			this.globalInfo.setOwnMtpCreation(MTP_Creation.ConfiguredByJADE);
		}
		// --- this.DEF_OWN_MTP_IP -------------------
		propValue = this.getProperty(this.DEF_OWN_MTP_IP);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			String ipAddress = propValue.trim();
			this.globalInfo.setOwnMtpIP(ipAddress);
		} else {
			this.globalInfo.setOwnMtpIP(PlatformJadeConfig.MTP_IP_AUTO_Config);
		}
		// --- this.DEF_OWN_MTP_PORT -------------------
		propValue = this.getProperty(this.DEF_OWN_MTP_PORT);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			Integer propValueInt = Integer.parseInt(propValue.trim());
			this.globalInfo.setOwnMtpPort(propValueInt);
		} else {
			this.globalInfo.setOwnMtpPort(7778);
		}
		// --- this.DEF_OWN_MTP_PROTOCOL -------------------
		propValue = this.getProperty(this.DEF_OWN_MTP_PROTOCOL);
		if (propValue!=null && propValue.equalsIgnoreCase("") == false) {
			MtpProtocol ownMtpProtocol = MtpProtocol.valueOf(propValue);
			this.globalInfo.setMtpProtocol(ownMtpProtocol);
		} else {
			this.globalInfo.setMtpProtocol(MtpProtocol.HTTP);
		}
		
		// --- this.DEF_MASTER_DB_HOST ---------------
		propValue = this.getProperty(this.DEF_MASTER_DB_HOST);
		if ( propValue!=null &&  propValue.equalsIgnoreCase("") == false ) {
			this.globalInfo.setServerMasterDBHost(propValue.trim());
		} else {
			this.globalInfo.setServerMasterDBHost(null);
		}
		// --- this.DEF_MASTER_DB_NAME ---------------
		propValue = this.getProperty(this.DEF_MASTER_DB_NAME);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			this.globalInfo.setServerMasterDBName(propValue.trim());
		} else {
			this.globalInfo.setServerMasterDBName(null);
		}
		// --- this.DEF_MASTER_DB_USER ---------------
		propValue = this.getProperty(this.DEF_MASTER_DB_USER);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			this.globalInfo.setServerMasterDBUser(propValue.trim());
		} else {
			this.globalInfo.setServerMasterDBUser(null);
		}
		// --- this.DEF_MASTER_DB_PSWD ---------------
		propValue = this.getProperty(this.DEF_MASTER_DB_PSWD);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			this.globalInfo.setServerMasterDBPswd(propValue.trim());
		} else {
			this.globalInfo.setServerMasterDBPswd(null);
		}
		
		
		// --- this.DEF_GOOGLE_API_KEY ---------------
		propValue = this.getProperty(this.DEF_GOOGLE_API_KEY);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			this.globalInfo.setGoogleKey4API(propValue.trim());
		} else {
			this.globalInfo.setGoogleKey4API(null);
		}
		// --- this.DEF_GOOGLE_HTTP_REF --------------
		propValue = this.getProperty(this.DEF_GOOGLE_HTTP_REF);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			this.globalInfo.setGoogleHttpRef(propValue.trim());
		} else {
			this.globalInfo.setGoogleHttpRef(null);
		}
		
		
		// --- this.DEF_UPDATE_AUTOCONFIG -------------
		propValue = this.getProperty(this.DEF_UPDATE_AUTOCONFIG);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			Integer propValueInt = Integer.parseInt(propValue.trim());
			this.globalInfo.setUpdateAutoConfiguration(propValueInt);
		} else {
			this.globalInfo.setUpdateAutoConfiguration(0);
		}
		// --- this.DEF_UPDATE_KEEP_DICTIONARY --------
		propValue = this.getProperty(this.DEF_UPDATE_KEEP_DICTIONARY);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			Integer propValueInt = Integer.parseInt(propValue.trim());
			this.globalInfo.setUpdateKeepDictionary(propValueInt);
		} else {
			this.globalInfo.setUpdateKeepDictionary(1);
		}
		// --- this.DEF_UPDATE_DATE_LAST_CHECKED ------
		propValue = this.getProperty(this.DEF_UPDATE_DATE_LAST_CHECKED);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			Long propValueInt = Long.parseLong(propValue.trim());
			this.globalInfo.setUpdateDateLastChecked(propValueInt);
		} else {
			this.globalInfo.setUpdateDateLastChecked(0);
		}
		
		
		// --- this.DEF_DeviceServcie_ProjectFolder ---
		propValue = this.getProperty(this.DEF_DeviceServcie_ProjectFolder);
		if (propValue==null || propValue.equals("")) {
			this.globalInfo.setDeviceServiceProjectFolder(null);
		} else {
			this.globalInfo.setDeviceServiceProjectFolder(propValue);	
		}
		
		// --- this.DEF_DeviceServcie_ExecAs ----------
		propValue = this.getProperty(this.DEF_DeviceServcie_ExecAs);
		if (propValue==null || propValue.equals("")) {
			this.globalInfo.setDeviceServiceExecutionMode(DeviceSystemExecutionMode.SETUP);
		} else if (propValue.equalsIgnoreCase(this.DEF_DeviceServcie_ExecAsService)) {
			this.globalInfo.setDeviceServiceExecutionMode(DeviceSystemExecutionMode.SETUP);
		} else if (propValue.equalsIgnoreCase(this.DEF_DeviceServcie_ExecAsAgent)) {
			this.globalInfo.setDeviceServiceExecutionMode(DeviceSystemExecutionMode.AGENT);
		} else {
			this.globalInfo.setDeviceServiceExecutionMode(DeviceSystemExecutionMode.SETUP);
		}
		// --- this.DEF_DeviceServcie_Setup -----------
		propValue = this.getProperty(this.DEF_DeviceServcie_Setup);
		if (propValue==null || propValue.equals("")) {
			this.globalInfo.setDeviceServiceSetupSelected(null);
		} else {
			this.globalInfo.setDeviceServiceSetupSelected(propValue);
		}
		// --- this.DEF_DeviceServcie_Vis -------------
		propValue = this.getProperty(this.DEF_DeviceServcie_Vis);
		if (propValue==null || propValue.equals("")) {
			this.globalInfo.setDeviceServiceAgentVisualisation(EmbeddedSystemAgentVisualisation.NONE);
		} else if (propValue.equalsIgnoreCase(this.DEF_DeviceServcie_Vis_None)) {
			this.globalInfo.setDeviceServiceAgentVisualisation(EmbeddedSystemAgentVisualisation.NONE);
		} else if (propValue.equalsIgnoreCase(this.DEF_DeviceServcie_Vis_TrayIcon)) {
			this.globalInfo.setDeviceServiceAgentVisualisation(EmbeddedSystemAgentVisualisation.TRAY_ICON);
		} else {
			this.globalInfo.setDeviceServiceAgentVisualisation(EmbeddedSystemAgentVisualisation.NONE);
		}
		// --- this.DEF_KEYSTORE_FILE -------------------
		propValue = this.getProperty(this.DEF_KEYSTORE_FILE);
		if (propValue!=null && propValue.equalsIgnoreCase("") == false) {
			this.globalInfo.setKeyStoreFile(propValue);
		} else {
			this.globalInfo.setKeyStoreFile(null);
		}
		// --- this.DEF_KEYSTORE_PASSWORD -------------------
		propValue = this.getProperty(this.DEF_KEYSTORE_PASSWORD);
		if (propValue!=null && propValue.equalsIgnoreCase("") == false) {
			this.globalInfo.setKeyStorePasswordEncrypted(propValue);
		} else {
			this.globalInfo.setKeyStorePasswordEncrypted(null);
		}
		// --- this.DEF_TRUSTSTORE_FILE -------------------
		propValue = this.getProperty(this.DEF_TRUSTSTORE_FILE);
		if (propValue.equalsIgnoreCase("") == false) {
			this.globalInfo.setTrustStoreFile(propValue);
		} else {
			this.globalInfo.setTrustStoreFile(null);
		}
		// --- this.DEF_TRUSTSTORE_PASSWORD -------------------
		propValue = this.getProperty(this.DEF_TRUSTSTORE_PASSWORD);
		if (propValue.equalsIgnoreCase("") == false) {
			this.globalInfo.setTrustStorePasswordEncrypted(propValue);
		} else {
			this.globalInfo.setTrustStorePasswordEncrypted(null);
		}
		// --- this.DEF_OIDC_ISSUER_URI -------------------
		propValue = this.getProperty(this.DEF_OIDC_ISSUER_URI);
		if (propValue !=null && propValue.equalsIgnoreCase("") == false) {
			this.globalInfo.setOIDCIssuerURI(propValue);
		} else {
			this.globalInfo.setOIDCIssuerURI(null);
		}
		// --- this.DEF_OIDC_USERNAME -------------------
		propValue = this.getProperty(this.DEF_OIDC_USERNAME);
		if (propValue !=null && propValue.equalsIgnoreCase("") == false) {
			this.globalInfo.setOIDCUsername(propValue);
		} else {
			this.globalInfo.setOIDCUsername(null);
		}
	}

	/**
	 * This method sets the values from the Runtime Variables in class Global ('Application.RunInfo')
	 * to this property-file / config-file / 'agentgui.xml' 
	 */
	private void setGlobal2Config() {
		
		
		// --- this.DEF_PROJECT_DIRECTORY ------------
		this.setProperty(this.DEF_PROJECTS_DIRECTORY, this.globalInfo.getPathProjects());
		
		// --- this.DEF_RUNAS ------------------------
		switch (this.globalInfo.getExecutionMode()) {
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
		this.setProperty(this.DEF_BENCH_VALUE, this.globalInfo.getBenchValue().toString());
		// --- this.DEF_BENCH_EXEC_ON ----------------
		if (this.globalInfo.getBenchExecOn()!=null) {
			this.setProperty(this.DEF_BENCH_EXEC_ON, this.globalInfo.getBenchExecOn());	
		}
		// --- this.DEF_BENCH_SKIP_ALLWAYS -----------
		if ( this.globalInfo.isBenchAlwaysSkip() == true ) {
			this.setProperty(this.DEF_BENCH_SKIP_ALLWAYS,"true");	
		} else {
			this.setProperty(this.DEF_BENCH_SKIP_ALLWAYS,"false");
		}
		
		// --- this.DEF_LANGUAGE ---------------------
		if (this.globalInfo.getLanguage()==null) {
			this.setProperty(this.DEF_LANGUAGE, "en");			
		} else {
			this.setProperty(this.DEF_LANGUAGE, this.globalInfo.getLanguage());
		}

		// --- this.DEF_MAXIMiZE_MAIN_WINDOW ---------
		if (this.globalInfo.isMaximzeMainWindow()==true) {
			this.setProperty(this.DEF_MAXIMiZE_MAIN_WINDOW, "true");			
		} else {
			this.setProperty(this.DEF_MAXIMiZE_MAIN_WINDOW, "false");
		}
		
		// --- this.DEF_LOGGING_ENABLED --------------
		if (this.globalInfo.isLoggingEnabled()) {
			this.setProperty(this.DEF_LOGGING_ENABLED, "true");			
		} else {
			this.setProperty(this.DEF_LOGGING_ENABLED, "false");
		}
		// --- this.DEF_LOGGING_BASE_PATH ------------
		if (this.globalInfo.getLoggingBasePath()==null || this.globalInfo.getLoggingBasePath().equals(GlobalInfo.getLoggingBasePathDefault())) {
			this.setProperty(this.DEF_LOGGING_BASE_PATH, "");
		} else {
			this.setProperty(this.DEF_LOGGING_BASE_PATH, this.globalInfo.getLoggingBasePath());	
		}
		
		// --- this.DEF_AUTOSTART --------------------
		if (this.globalInfo.isServerAutoRun()==true) {
			this.setProperty(this.DEF_AUTOSTART, "true");
		} else {
			this.setProperty(this.DEF_AUTOSTART, "false");
		}
		// --- this.DEF_MASTER_URL -------------------
		if (this.globalInfo.getServerMasterURL()==null) {
			this.setProperty(this.DEF_MASTER_URL, "");
		} else {
			this.setProperty(this.DEF_MASTER_URL, this.globalInfo.getServerMasterURL());	
		}

		// --- this.DEF_MASTER_PORT ------------------
		this.setProperty(this.DEF_MASTER_PORT, this.globalInfo.getServerMasterPort().toString());
		// --- this.DEF_MASTER_PORT4MTP --------------
		this.setProperty(this.DEF_MASTER_PORT4MTP, this.globalInfo.getServerMasterPort4MTP().toString());
		// --- this.DEF_MASTER_PROTOCOL --------------
		this.setProperty(this.DEF_MASTER_PROTOCOL, this.globalInfo.getServerMasterProtocol().toString());
		
		// --- this.DEF_OWN_MTP_CREATION -------------
		this.setProperty(this.DEF_OWN_MTP_CREATION, this.globalInfo.getOwnMtpCreation().toString());
		// --- this.DEF_OWN_MTP_IP -------------------
		this.setProperty(this.DEF_OWN_MTP_IP, this.globalInfo.getOwnMtpIP());
		// --- this.DEF_OWN_MTP_PORT -----------------
		this.setProperty(this.DEF_OWN_MTP_PORT, this.globalInfo.getOwnMtpPort().toString());
		// --- this.DEF_OWN_MTP_PROTOCOL -----------------
		this.setProperty(this.DEF_OWN_MTP_PROTOCOL, this.globalInfo.getMtpProtocol().toString());
		
		// --- this.DEF_MASTER_DB_HOST ---------------
		if (this.globalInfo.getServerMasterDBHost() == null) {
			this.setProperty(this.DEF_MASTER_DB_HOST, "");
		} else {
			this.setProperty(this.DEF_MASTER_DB_HOST, this.globalInfo.getServerMasterDBHost());	
		}
		// --- this.DEF_MASTER_DB_NAME ---------------
		if (this.globalInfo.getServerMasterDBName() == null) {
			this.setProperty(this.DEF_MASTER_DB_NAME, "");
		} else {
			this.setProperty(this.DEF_MASTER_DB_NAME, this.globalInfo.getServerMasterDBName());	
		}
		// --- this.DEF_MASTER_DB_USER ---------------
		if (this.globalInfo.getServerMasterDBUser() == null) {
			this.setProperty(this.DEF_MASTER_DB_USER, "");
		} else {
			this.setProperty(this.DEF_MASTER_DB_USER, this.globalInfo.getServerMasterDBUser());	
		}
		// --- this.DEF_MASTER_DB_PSWD ---------------
		if (this.globalInfo.getServerMasterDBPswd() == null) {
			this.setProperty(this.DEF_MASTER_DB_PSWD, "");
		} else {
			this.setProperty(this.DEF_MASTER_DB_PSWD, this.globalInfo.getServerMasterDBPswd());	
		}
		
		// --- this.DEF_GOOGLE_API_KEY ---------------
		if (this.globalInfo.getGoogleKey4API() == null) {
			this.setProperty(this.DEF_GOOGLE_API_KEY, "");
		} else {
			this.setProperty(this.DEF_GOOGLE_API_KEY, this.globalInfo.getGoogleKey4API());	
		}
		// --- this.DEF_GOOGLE_HTTP_REF --------------
		if (this.globalInfo.getGoogleKey4API() == null) {
			this.setProperty(this.DEF_GOOGLE_HTTP_REF, "");
		} else {
			this.setProperty(this.DEF_GOOGLE_HTTP_REF, this.globalInfo.getGoogleHttpRef());	
		}
		
		
		// --- this.DEF_UPDATE_AUTOCONFIG -------------
		this.setProperty(this.DEF_UPDATE_AUTOCONFIG, this.globalInfo.getUpdateAutoConfiguration().toString());	
		// --- this.DEF_UPDATE_KEEP_DICTIONAR ---------		
		this.setProperty(this.DEF_UPDATE_KEEP_DICTIONARY, this.globalInfo.getUpdateKeepDictionary().toString());
		// --- this.DEF_UPDATE_DATE_LAST_CHECKED ------
		this.setProperty(this.DEF_UPDATE_DATE_LAST_CHECKED, this.globalInfo.getUpdateDateLastChecked().toString());
		// --- this.DEF_KEYSTORE_FILE ------
		if (this.globalInfo.getKeyStoreFile()==null || this.globalInfo.getMtpProtocol().equals(MtpProtocol.HTTP)) {
			this.setProperty(this.DEF_KEYSTORE_FILE, "");
		} else {
			this.setProperty(this.DEF_KEYSTORE_FILE, this.globalInfo.getKeyStoreFile());
		}
		// --- this.DEF_KEYSTORE_PASSWORD ------
		if (this.globalInfo.getKeyStorePasswordEncrypted()==null || this.globalInfo.getMtpProtocol().equals(MtpProtocol.HTTP)) {
			this.setProperty(this.DEF_KEYSTORE_PASSWORD, "");
		} else {
			this.setProperty(this.DEF_KEYSTORE_PASSWORD, this.globalInfo.getKeyStorePasswordEncrypted());
		}
		// --- this.DEF_TRUSTSTORE_FILE ------
		if (this.globalInfo.getTrustStoreFile()==null || this.globalInfo.getMtpProtocol().equals(MtpProtocol.HTTP)) {
			this.setProperty(this.DEF_TRUSTSTORE_FILE, "");
		} else {
			this.setProperty(this.DEF_TRUSTSTORE_FILE, this.globalInfo.getTrustStoreFile());
		}
		// --- this.DEF_TRUSTSTORE_PASSWORD ------
		if (this.globalInfo.getTrustStorePasswordEncrypted()==null || this.globalInfo.getMtpProtocol().equals(MtpProtocol.HTTP)) {
			this.setProperty(this.DEF_TRUSTSTORE_PASSWORD, "");
		} else {
			this.setProperty(this.DEF_TRUSTSTORE_PASSWORD, this.globalInfo.getTrustStorePasswordEncrypted());
		}
		
		// --- this.DEF_DeviceServcie_ProjectFolder ---
		if (this.globalInfo.getDeviceServiceProjectFolder()==null) {
			this.setProperty(this.DEF_DeviceServcie_ProjectFolder, "");
		} else {
			this.setProperty(this.DEF_DeviceServcie_ProjectFolder, this.globalInfo.getDeviceServiceProjectFolder());
		}
		// --- this.DEF_DeviceServcie_ExecAs ----------
		switch (this.globalInfo.getDeviceServiceExecutionMode()) {
		case SETUP:
			this.setProperty(this.DEF_DeviceServcie_ExecAs, this.DEF_DeviceServcie_ExecAsService);
			break;
		case AGENT:
			this.setProperty(this.DEF_DeviceServcie_ExecAs, this.DEF_DeviceServcie_ExecAsAgent);
			break;
		}
		// --- this.DEF_DeviceServcie_ProjectFolder ---
		if (this.globalInfo.getDeviceServiceSetupSelected()==null) {
			this.setProperty(this.DEF_DeviceServcie_Setup, "");
		} else {
			this.setProperty(this.DEF_DeviceServcie_Setup, this.globalInfo.getDeviceServiceSetupSelected());
		}
		// --- this.DEF_DeviceServcie_Vis -------------
		switch (this.globalInfo.getDeviceServiceAgentVisualisation()) {
		case NONE:
			this.setProperty(this.DEF_DeviceServcie_Vis, this.DEF_DeviceServcie_Vis_None);
			break;
		case TRAY_ICON:
			this.setProperty(this.DEF_DeviceServcie_Vis, this.DEF_DeviceServcie_Vis_TrayIcon);
			break;
		}
		
		// --- this.DEF_OIDC_ISSUER_URI ------------------
		if (this.globalInfo.getOIDCIssuerURI() == null) {
			this.setProperty(this.DEF_OIDC_ISSUER_URI, "");
		} else {
			this.setProperty(this.DEF_OIDC_ISSUER_URI, this.globalInfo.getOIDCIssuerURI());	
		}
		
		// --- this.DEF_OIDC_USERNAME -------------
		if(this.globalInfo.getOIDCUsername()!=null){
			this.setProperty(this.DEF_OIDC_USERNAME, this.globalInfo.getOIDCUsername());
		}

	}	
	
	/**
	 * This method sets the mandatory properties with default values to this properties 
	 */
	private void setDefaultConfigValues() {

		for (int i = 0; i < mandatoryProps.length; i++) {
			
			// --- Here the mandatory properties in general --------- 
			this.setProperty( mandatoryProps[i], "" );

			// --- Here some special mandatory properties ----------- 
			if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_PROJECTS_DIRECTORY) ) {				
				this.setProperty(this.DEF_PROJECTS_DIRECTORY, this.globalInfo.getDefaultProjectsDirectory());
			
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_LANGUAGE) ) {				
				this.setProperty(this.DEF_LANGUAGE, Language.EN);
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_MASTER_PORT) ) {				
				this.setProperty(this.DEF_MASTER_PORT, this.globalInfo.getJadeLocalPort().toString());
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_MASTER_PORT4MTP) ) {				
				this.setProperty(this.DEF_MASTER_PORT4MTP, this.globalInfo.getServerMasterPort4MTP().toString());
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_MASTER_PROTOCOL) ) {				
				this.setProperty(this.DEF_MASTER_PROTOCOL, this.globalInfo.getServerMasterProtocol().toString());
			
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_AUTOSTART) ) {
				if (this.globalInfo.isServerAutoRun()==true) {
					this.setProperty(this.DEF_AUTOSTART, "true");
				} else {
					this.setProperty(this.DEF_AUTOSTART, "false");	
				}

				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_OWN_MTP_CREATION) ) {
				this.setProperty(this.DEF_OWN_MTP_CREATION, MTP_Creation.ConfiguredByJADE.toString());
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_OWN_MTP_IP) ) {
				this.setProperty(this.DEF_OWN_MTP_IP, PlatformJadeConfig.MTP_IP_AUTO_Config);
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_OWN_MTP_PORT) ) {
				this.setProperty(this.DEF_OWN_MTP_PORT, this.globalInfo.getOwnMtpPort().toString());
			
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_OWN_MTP_PROTOCOL) ) {
				this.setProperty(this.DEF_OWN_MTP_PROTOCOL, this.globalInfo.getMtpProtocol().toString());
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_UPDATE_AUTOCONFIG) ) {				
				this.setProperty(this.DEF_UPDATE_AUTOCONFIG, "0");
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_UPDATE_KEEP_DICTIONARY) ) {
				this.setProperty(this.DEF_UPDATE_KEEP_DICTIONARY, "1");
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_UPDATE_DATE_LAST_CHECKED) ) {
				this.setProperty(this.DEF_UPDATE_DATE_LAST_CHECKED, "0");
				
			} else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_KEYSTORE_FILE) ) {
				this.setProperty(this.DEF_KEYSTORE_FILE, "0");
			
			}else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_KEYSTORE_PASSWORD) ) {
				this.setProperty(this.DEF_KEYSTORE_PASSWORD, "0");
			
			}else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_TRUSTSTORE_FILE) ) {
				this.setProperty(this.DEF_TRUSTSTORE_FILE, "0");
			
			}else if ( mandatoryProps[i].equalsIgnoreCase(this.DEF_TRUSTSTORE_PASSWORD) ) {
				this.setProperty(this.DEF_TRUSTSTORE_PASSWORD, "0");
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
				if ( mandatoryProps[i].equals(this.DEF_PROJECTS_DIRECTORY) ) {				
					this.setProperty(mandatoryProps[i], this.globalInfo.getDefaultProjectsDirectory());
				} else if (mandatoryProps[i].equals(this.DEF_LANGUAGE)) {
					this.setProperty(mandatoryProps[i], Language.EN);
				} else if (mandatoryProps[i].equals(this.DEF_AUTOSTART)) {
					this.setProperty(mandatoryProps[i], "true");
				} else if (mandatoryProps[i].equals(this.DEF_OWN_MTP_CREATION)) {
					this.setProperty(mandatoryProps[i],  MTP_Creation.ConfiguredByJADE.toString());
				} else if (mandatoryProps[i].equals(this.DEF_OWN_MTP_IP)) {
					this.setProperty(mandatoryProps[i], PlatformJadeConfig.MTP_IP_AUTO_Config);
				} else if (mandatoryProps[i].equals(this.DEF_BENCH_SKIP_ALLWAYS)) {
					this.setProperty(mandatoryProps[i], "true");
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
		defaultComment = defaultComment + " Configuration of " + globalInfo.getApplicationTitle() + " (Version: " + this.globalInfo.getVersionInfo().getFullVersionInfo(false, " ") + ")\n"; 
		defaultComment = defaultComment + " by Christian Derksen - DAWIS - ICB - University Duisburg-Essen\n";
		defaultComment = defaultComment + " Email: christian.derksen@icb.uni-due.de\n";
		configFileDefaultComment = defaultComment;
	}
	
}
