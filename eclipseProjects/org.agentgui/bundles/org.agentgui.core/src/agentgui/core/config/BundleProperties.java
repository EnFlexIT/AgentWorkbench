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

import java.util.Vector;

import org.agentgui.PlugInActivator;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;

import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo.DeviceSystemExecutionMode;
import agentgui.core.config.GlobalInfo.EmbeddedSystemAgentVisualisation;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.config.GlobalInfo.MtpProtocol;
import agentgui.core.project.PlatformJadeConfig;
import agentgui.core.project.PlatformJadeConfig.MTP_Creation;

/**
 * The Class BundleProperties organizes the storage of application configuration data.
 * It replaces the class {@link FileProperties} that stored these information previously 
 * in the file agentgui.ini.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BundleProperties {

	private boolean debug = false;
	
	public static final String PLUGIN_ID = PlugInActivator.PLUGIN_ID;
	
	public static final String DEF_PROJECTS_DIRECTORY = "00_PROJECTS_DIRECTORY";
	
	public static final String DEF_RUNAS = "01_RUNAS";
	
	public static final String DEF_BENCH_VALUE = "02_BENCH_VALUE";
	public static final String DEF_BENCH_EXEC_ON = "03_BENCH_EXEC_ON";
	public static final String DEF_BENCH_SKIP_ALLWAYS = "04_BENCH_SKIP_ALLWAYS";

	public static final String DEF_LANGUAGE = "05_LANGUAGE";
	public static final String DEF_MAXIMiZE_MAIN_WINDOW = "06_MAXIMiZE_MAIN_WINDOW";
	
	public static final String DEF_LOGGING_ENABLED = "07_LOGGING_ENABLED";
	public static final String DEF_LOGGING_BASE_PATH = "08_LOGGING_BASE_PATH";
	
	public static final String DEF_AUTOSTART = "10_AUTOSTART";
	public static final String DEF_MASTER_URL = "11_MASTER_URL";
	public static final String DEF_MASTER_PORT = "12_MASTER_PORT";
	public static final String DEF_MASTER_PORT4MTP = "13_MASTER_PORT4MTP";
	public static final String DEF_MASTER_PROTOCOL = "14_MASTER_PROTOCOL";

	public static final String DEF_OWN_MTP_CREATION = "15_OWN_MTP_CREATION";
	public static final String DEF_OWN_MTP_IP = "16_OWN_MTP_IP";
	public static final String DEF_OWN_MTP_PORT = "17_OWN_MTP_PORT";
	public static final String DEF_OWN_MTP_PROTOCOL = "18_OWN_MTP_PROTOCOL";
	
	public static final String DEF_MASTER_DB_HOST = "20_MASTER_DB_HOST";
	public static final String DEF_MASTER_DB_NAME = "21_MASTER_DB_NAME";
	public static final String DEF_MASTER_DB_USER = "22_MASTER_DB_USER";
	public static final String DEF_MASTER_DB_PSWD = "23_MASTER_DB_PSWD";
	
	public static final String DEF_GOOGLE_API_KEY  = "30_GOOGLE_API_KEY";
	public static final String DEF_GOOGLE_HTTP_REF = "31_GOOGLE_HTTP_REF";
	
	public static final String DEF_UPDATE_AUTOCONFIG = "36_UPDATE_AUTOCONFIG";
	public static final String DEF_UPDATE_KEEP_DICTIONARY = "37_UPDATE_KEEP_DICTIONARY";
	public static final String DEF_UPDATE_DATE_LAST_CHECKED = "38_UPDATE_DATE_LAST_CHECKED";
	
	public static final String DEF_DEVICE_SERVICE_PROJECT_FOLDER = "40_DEVICE_SERVICE_PROJECT";
	public static final String DEF_DEVICE_SERVICE_EXEC_AS = "41_DEVICE_SERVICE_EXEC_AS";
	public static final String DEF_DEVICE_SERVICE_SETUP = "42_DEVICE_SERVICE_SETUP";
	public static final String DEF_DEVICE_SERVICE_AGENT_CLASS = "43_DEVICE_SERVICE_AGENT";
	public static final String DEF_DEVICE_SERVICE_AGENT_NAME = "44_DEVICE_SERVICE_AGENT_Name";
	public static final String DEF_DEVICE_SERVICE_VISUALIZATION = "45_DEVICE_SERVICE_VISUALISATION";
	
	public static final String DEF_KEYSTORE_FILE = "50_KEY_STORE_FILE";
	public static final String DEF_KEYSTORE_PASSWORD = "51_KEY_STORE_PASSWORD";
	public static final String DEF_TRUSTSTORE_FILE = "52_TRUST_STORE_FILE";
	public static final String DEF_TRUSTSTORE_PASSWORD = "53_TRUST_STORE_PASSWORD";
	
	public static final String DEF_OIDC_USERNAME = "60_OIDC_USERNAME";
	public static final String DEF_OIDC_ISSUER_URI = "61_OIDC_ISSUER_URI";
	
	public static final String DEF_LOCAL_PROJECT_REPOSITORY = "70_LOCAL_PROJECT_REPOSITORY";
	public static final String DEF_PROJECT_REPOSITORIES = "71_PROJECT_REPOSITORIES";
	
	public static final String DEF_PRODUCT_INSTALLATION_DIRECTORY = "100_PRODUCT_INSTALLATION_DIRECTORY";
	public static final String DEF_PRODUCT_DIRECTORY_FOR_INSTALLATION_PACKAGES = "101_PRODUCT_DIRECTORY_FOR_INSTALLATION_PACKAGES";
	
	
	
	private GlobalInfo globalInfo;
	private IEclipsePreferences eclipsePreferences;
	private IEclipsePreferences.IPreferenceChangeListener changeListener;
	
	
	/**
	 * Instantiates the new bundle properties and set them to the global configuration.
	 * @param globalInfo the instance of the {@link GlobalInfo}
	 */
	public BundleProperties(GlobalInfo globalInfo) {
		this(globalInfo, true);
	}
	/**
	 * Instantiates a new bundle properties.
	 * @param globalInfo the instance of the {@link GlobalInfo}
	 * @param isSetPreferencesToGlobal set <code>true</code> in order to directly set the preferences to the {@link GlobalInfo}
	 */
	public BundleProperties(GlobalInfo globalInfo, boolean isSetPreferencesToGlobal) {
		this.globalInfo = globalInfo;
		if (isSetPreferencesToGlobal==true) {
			this.setPreferencesGlobal();
		}
	}
	
	/**
	 * Returns the eclipse preferences.
	 * @return the eclipse preferences
	 */
	public IEclipsePreferences getEclipsePreferences() {
		if (eclipsePreferences==null) {
			IScopeContext iScopeContext = ConfigurationScope.INSTANCE;
			eclipsePreferences = iScopeContext.getNode(PlugInActivator.PLUGIN_ID);
			eclipsePreferences.addPreferenceChangeListener(this.getChangeListener());
		}
		return eclipsePreferences;
	}
	/**
	 * return the listener for preferences changes.
	 * @return the change listener
	 */
	private IEclipsePreferences.IPreferenceChangeListener getChangeListener() {
		if (changeListener==null) {
			changeListener = new IEclipsePreferences.IPreferenceChangeListener() {
				@Override
				public void preferenceChange(PreferenceChangeEvent pce) {
					
					if (debug==true) System.out.println("Property '" + pce.getKey() + "' changed from " + pce.getOldValue() + " to "+ pce.getNewValue());
					
					switch(pce.getKey()) {
					case DEF_RUNAS:
						boolean changedExecutionMode = (pce.getOldValue()!=null && pce.getNewValue()!=pce.getOldValue());
						if (changedExecutionMode==true) {
							if (debug==true) System.out.println("Changed Execution Mode: changed from " + pce.getOldValue() + " to "+ pce.getNewValue());
							//TODO if the preference dialog was set to SWT!
						}
					}
				}
			};
		}
		return changeListener;
	}
	
	/**
	 * Saves the bundle properties.
	 */
	public void save() {
		this.setGlobal2Preferences();
		try {
			this.getEclipsePreferences().flush();
		} catch (BackingStoreException bsEx) {
			bsEx.printStackTrace();
		}
	}
	
	/**
	 * This method sets the values from the preference file to the {@link GlobalInfo}.
	 */
	public void setPreferencesGlobal() {
		
		IEclipsePreferences eclipsePreferences = this.getEclipsePreferences();
		
		boolean booleanPrefValue;
		int integerPrefValue;
		long longPrefValue;
		float floatPrefValue;
		String stringPrefValue;
		MtpProtocol mtpProtocol;

		
		// --- this.DEF_PROJECT_DIRECTORY ------------
		stringPrefValue = eclipsePreferences.get(DEF_PROJECTS_DIRECTORY, this.globalInfo.getDefaultProjectsDirectory());
		if (stringPrefValue!=null && stringPrefValue.equalsIgnoreCase("")==true) {
			this.globalInfo.setPathProjects(this.globalInfo.getDefaultProjectsDirectory());
		} else {
			this.globalInfo.setPathProjects(stringPrefValue);
		}


		// --- this.DEF_RUNAS ------------------------
		stringPrefValue = eclipsePreferences.get(DEF_RUNAS, ExecutionMode.APPLICATION.toString());
		ExecutionMode execMode = ExecutionMode.valueOf(stringPrefValue);
		this.globalInfo.setExecutionMode(execMode);


		// --- this.DEF_BENCH_VALUE ------------------
		floatPrefValue = eclipsePreferences.getFloat(DEF_BENCH_VALUE, 0f);
		this.globalInfo.setBenchValue(floatPrefValue);
		// --- this.DEF_BENCH_EXEC_ON ----------------
		stringPrefValue = eclipsePreferences.get(DEF_BENCH_EXEC_ON, null);
		this.globalInfo.setBenchExecOn(stringPrefValue);
		// --- this.DEF_BENCH_SKIP_ALLWAYS -----------
		booleanPrefValue = eclipsePreferences.getBoolean(DEF_BENCH_SKIP_ALLWAYS, true);
		this.globalInfo.setBenchAlwaysSkip(booleanPrefValue);


		// --- this.DEF_LANGUAGE ---------------------
		stringPrefValue = eclipsePreferences.get(DEF_LANGUAGE, Language.EN);
		this.globalInfo.setLanguage(stringPrefValue);


		// --- this.DEF_MAXIMiZE_MAIN_WINDOW ---------
		booleanPrefValue = eclipsePreferences.getBoolean(DEF_MAXIMiZE_MAIN_WINDOW, false);
		this.globalInfo.setMaximzeMainWindow(booleanPrefValue);


		// --- this.DEF_LOGGING_ENABLED --------------
		booleanPrefValue = eclipsePreferences.getBoolean(DEF_LOGGING_ENABLED, false);
		this.globalInfo.setLoggingEnabled(booleanPrefValue);
		// --- this.DEF_LOGGING_BASE_PATH ------------
		stringPrefValue = eclipsePreferences.get(DEF_LOGGING_BASE_PATH, null);
		this.globalInfo.setLoggingBasePath(stringPrefValue);


		// --- this.DEF_AUTOSTART --------------------
		booleanPrefValue = eclipsePreferences.getBoolean(DEF_AUTOSTART, false);
		this.globalInfo.setServerAutoRun(booleanPrefValue);
		// --- this.DEF_MASTER_URL -------------------
		stringPrefValue = eclipsePreferences.get(DEF_MASTER_URL, "");
		this.globalInfo.setServerMasterURL(stringPrefValue.trim());
		// --- this.DEF_MASTER_PORT ------------------
		integerPrefValue = eclipsePreferences.getInt(DEF_MASTER_PORT, 1099);
		this.globalInfo.setServerMasterPort(integerPrefValue);
		// --- this.DEF_MASTER_PORT4MTP --------------
		integerPrefValue = eclipsePreferences.getInt(DEF_MASTER_PORT4MTP, 7778);
		this.globalInfo.setServerMasterPort4MTP(integerPrefValue);
		// --- this.DEF_MASTER_PROTOCOL --------------
		stringPrefValue = eclipsePreferences.get(DEF_MASTER_PROTOCOL, MtpProtocol.HTTP.toString());
		mtpProtocol = MtpProtocol.valueOf(stringPrefValue);
		this.globalInfo.setServerMasterProtocol(mtpProtocol);
		
		
		// --- this.DEF_OWN_MTP_CREATION -------------
		stringPrefValue = eclipsePreferences.get(DEF_OWN_MTP_CREATION, MTP_Creation.ConfiguredByJADE.toString());
		MTP_Creation ownMtpCreation = MTP_Creation.valueOf(stringPrefValue.trim());
		this.globalInfo.setOwnMtpCreation(ownMtpCreation);
		// --- this.DEF_OWN_MTP_IP -------------------
		stringPrefValue = eclipsePreferences.get(DEF_OWN_MTP_IP, PlatformJadeConfig.MTP_IP_AUTO_Config);
		this.globalInfo.setOwnMtpIP(stringPrefValue.trim());
		// --- this.DEF_OWN_MTP_PORT -------------------
		integerPrefValue = eclipsePreferences.getInt(DEF_OWN_MTP_PORT, 7778);
		this.globalInfo.setOwnMtpPort(integerPrefValue);
		// --- this.DEF_OWN_MTP_PROTOCOL -------------------
		stringPrefValue = eclipsePreferences.get(DEF_OWN_MTP_PROTOCOL, MtpProtocol.HTTP.toString());
		mtpProtocol = MtpProtocol.valueOf(stringPrefValue);
		this.globalInfo.setMtpProtocol(mtpProtocol);
		
		
		// --- this.DEF_MASTER_DB_HOST ---------------
		stringPrefValue = eclipsePreferences.get(DEF_MASTER_DB_HOST, "");
		this.globalInfo.setServerMasterDBHost(stringPrefValue.trim());
		// --- this.DEF_MASTER_DB_NAME ---------------
		stringPrefValue = eclipsePreferences.get(DEF_MASTER_DB_NAME, "");
		this.globalInfo.setServerMasterDBName(stringPrefValue.trim());
		// --- this.DEF_MASTER_DB_USER ---------------
		stringPrefValue = eclipsePreferences.get(DEF_MASTER_DB_USER, "");
		this.globalInfo.setServerMasterDBUser(stringPrefValue.trim());
		// --- this.DEF_MASTER_DB_PSWD ---------------
		stringPrefValue = eclipsePreferences.get(DEF_MASTER_DB_PSWD, "");
		this.globalInfo.setServerMasterDBPswd(stringPrefValue.trim());
		
		
		// --- this.DEF_GOOGLE_API_KEY ---------------
		stringPrefValue = eclipsePreferences.get(DEF_GOOGLE_API_KEY, "");
		this.globalInfo.setGoogleKey4API(stringPrefValue.trim());
		// --- this.DEF_GOOGLE_HTTP_REF --------------
		stringPrefValue = eclipsePreferences.get(DEF_GOOGLE_HTTP_REF, "");
		this.globalInfo.setGoogleHttpRef(stringPrefValue.trim());
		
		
		// --- this.DEF_UPDATE_AUTOCONFIG -------------
		integerPrefValue = eclipsePreferences.getInt(DEF_UPDATE_AUTOCONFIG, 0);
		this.globalInfo.setUpdateAutoConfiguration(integerPrefValue);
		// --- this.DEF_UPDATE_KEEP_DICTIONARY --------
		integerPrefValue = eclipsePreferences.getInt(DEF_UPDATE_KEEP_DICTIONARY, 1);
		this.globalInfo.setUpdateKeepDictionary(integerPrefValue);
		// --- this.DEF_UPDATE_DATE_LAST_CHECKED ------
		longPrefValue = eclipsePreferences.getLong(DEF_UPDATE_DATE_LAST_CHECKED, 0);
		this.globalInfo.setUpdateDateLastChecked(longPrefValue);
		
		
		// --- this.DEF_DeviceServcie_ProjectFolder ---
		stringPrefValue = eclipsePreferences.get(DEF_DEVICE_SERVICE_PROJECT_FOLDER, "");
		this.globalInfo.setDeviceServiceProjectFolder(stringPrefValue);	
		// --- this.DEF_DeviceServcie_ExecAs ----------
		stringPrefValue = eclipsePreferences.get(DEF_DEVICE_SERVICE_EXEC_AS, DeviceSystemExecutionMode.SETUP.toString());
		DeviceSystemExecutionMode dsem = DeviceSystemExecutionMode.valueOf(stringPrefValue);
		this.globalInfo.setDeviceServiceExecutionMode(dsem);
		// --- this.DEF_DeviceServcie_Setup -----------
		stringPrefValue = eclipsePreferences.get(DEF_DEVICE_SERVICE_SETUP, "");
		this.globalInfo.setDeviceServiceSetupSelected(stringPrefValue);
		// --- this.DEF_DeviceServcie_AgentName ------
		stringPrefValue = eclipsePreferences.get(DEF_DEVICE_SERVICE_AGENT_NAME, "");
		this.globalInfo.setDeviceServiceAgents(this.getDeviceAgentsVector(stringPrefValue.trim()));
		// --- this.DEF_DeviceServcie_Vis ------------
		stringPrefValue = eclipsePreferences.get(DEF_DEVICE_SERVICE_VISUALIZATION, EmbeddedSystemAgentVisualisation.NONE.toString());
		EmbeddedSystemAgentVisualisation esaVis = EmbeddedSystemAgentVisualisation.valueOf(stringPrefValue);
		this.globalInfo.setDeviceServiceAgentVisualisation(esaVis);
		
		
		// --- this.DEF_KEYSTORE_FILE ----------------
		stringPrefValue = eclipsePreferences.get(DEF_KEYSTORE_FILE, null);
		this.globalInfo.setKeyStoreFile(stringPrefValue);
		// --- this.DEF_KEYSTORE_PASSWORD ------------
		stringPrefValue = eclipsePreferences.get(DEF_KEYSTORE_PASSWORD, null);
		this.globalInfo.setKeyStorePasswordEncrypted(stringPrefValue);
		// --- this.DEF_TRUSTSTORE_FILE --------------
		stringPrefValue = eclipsePreferences.get(DEF_TRUSTSTORE_FILE, null);
		this.globalInfo.setTrustStoreFile(stringPrefValue);
		// --- this.DEF_TRUSTSTORE_PASSWORD ----------
		stringPrefValue = eclipsePreferences.get(DEF_TRUSTSTORE_PASSWORD, null);
		this.globalInfo.setTrustStorePasswordEncrypted(stringPrefValue);
		
		
		// --- this.DEF_OIDC_ISSUER_URI -------------------
		stringPrefValue = eclipsePreferences.get(DEF_OIDC_ISSUER_URI, null);
		this.globalInfo.setOIDCIssuerURI(stringPrefValue);
		// --- this.DEF_OIDC_USERNAME -------------------
		stringPrefValue = eclipsePreferences.get(DEF_OIDC_USERNAME, null);
		this.globalInfo.setOIDCUsername(stringPrefValue);
		
	}
	
	/**
	 * This method sets the settings from {@link GlobalInfo} to this preference file.  
	 */
	public void setGlobal2Preferences() {
		
		IEclipsePreferences eclipsePreferences = this.getEclipsePreferences();
		
		// --- this.DEF_PROJECT_DIRECTORY ------------
		if (this.globalInfo.getPathProjects()!=null) eclipsePreferences.put(DEF_PROJECTS_DIRECTORY, this.globalInfo.getPathProjects());

		
		// --- this.DEF_RUNAS ------------------------
		eclipsePreferences.put(DEF_RUNAS, this.globalInfo.getExecutionMode().toString()); 

		
		// --- this.DEF_BENCH_VALUE ------------------
		eclipsePreferences.putFloat(DEF_BENCH_VALUE, this.globalInfo.getBenchValue());
		// --- this.DEF_BENCH_EXEC_ON ----------------
		if (this.globalInfo.getBenchExecOn()!=null) eclipsePreferences.put(DEF_BENCH_EXEC_ON, this.globalInfo.getBenchExecOn());	
		// --- this.DEF_BENCH_SKIP_ALLWAYS -----------
		eclipsePreferences.putBoolean(DEF_BENCH_SKIP_ALLWAYS, this.globalInfo.isBenchAlwaysSkip());
		
		
		// --- this.DEF_LANGUAGE ---------------------
		if (this.globalInfo.getLanguage()!=null) eclipsePreferences.put(DEF_LANGUAGE, this.globalInfo.getLanguage());

		
		// --- this.DEF_MAXIMiZE_MAIN_WINDOW ---------
		eclipsePreferences.putBoolean(DEF_MAXIMiZE_MAIN_WINDOW, this.globalInfo.isMaximzeMainWindow());			
		
		
		// --- this.DEF_LOGGING_ENABLED --------------
		eclipsePreferences.putBoolean(DEF_LOGGING_ENABLED, this.globalInfo.isLoggingEnabled());			
		// --- this.DEF_LOGGING_BASE_PATH ------------
		if (this.globalInfo.getLoggingBasePath()!=null)eclipsePreferences.put(DEF_LOGGING_BASE_PATH, this.globalInfo.getLoggingBasePath());
		
		
		// --- this.DEF_AUTOSTART --------------------
		eclipsePreferences.putBoolean(DEF_AUTOSTART, this.globalInfo.isServerAutoRun());
		// --- this.DEF_MASTER_URL -------------------
		if (this.globalInfo.getServerMasterURL()!=null) eclipsePreferences.put(DEF_MASTER_URL, this.globalInfo.getServerMasterURL());
		// --- this.DEF_MASTER_PORT ------------------
		eclipsePreferences.putInt(DEF_MASTER_PORT, this.globalInfo.getServerMasterPort());
		// --- this.DEF_MASTER_PORT4MTP --------------
		eclipsePreferences.putInt(DEF_MASTER_PORT4MTP, this.globalInfo.getServerMasterPort4MTP());
		// --- this.DEF_MASTER_PROTOCOL --------------
		eclipsePreferences.put(DEF_MASTER_PROTOCOL, this.globalInfo.getServerMasterProtocol().toString());

		
		// --- this.DEF_OWN_MTP_CREATION -------------
		eclipsePreferences.put(DEF_OWN_MTP_CREATION, this.globalInfo.getOwnMtpCreation().toString());
		// --- this.DEF_OWN_MTP_IP -------------------
		if (this.globalInfo.getOwnMtpIP()!=null) eclipsePreferences.put(DEF_OWN_MTP_IP, this.globalInfo.getOwnMtpIP());
		// --- this.DEF_OWN_MTP_PORT -----------------
		eclipsePreferences.putInt(DEF_OWN_MTP_PORT, this.globalInfo.getOwnMtpPort());
		// --- this.DEF_OWN_MTP_PROTOCOL -----------------
		eclipsePreferences.put(DEF_OWN_MTP_PROTOCOL, this.globalInfo.getMtpProtocol().toString());
		
		
		// --- this.DEF_MASTER_DB_HOST ---------------
		if (this.globalInfo.getServerMasterDBHost()!=null) eclipsePreferences.put(DEF_MASTER_DB_HOST, this.globalInfo.getServerMasterDBHost());	
		// --- this.DEF_MASTER_DB_NAME ---------------
		if (this.globalInfo.getServerMasterDBName()!=null) eclipsePreferences.put(DEF_MASTER_DB_NAME, this.globalInfo.getServerMasterDBName());	
		// --- this.DEF_MASTER_DB_USER ---------------
		if (this.globalInfo.getServerMasterDBUser()!=null) eclipsePreferences.put(DEF_MASTER_DB_USER, this.globalInfo.getServerMasterDBUser());	
		// --- this.DEF_MASTER_DB_PSWD ---------------
		if (this.globalInfo.getServerMasterDBPswd()!=null) eclipsePreferences.put(DEF_MASTER_DB_PSWD, this.globalInfo.getServerMasterDBPswd());
		
		
		// --- this.DEF_GOOGLE_API_KEY ---------------
		if (this.globalInfo.getGoogleKey4API()!=null) eclipsePreferences.put(DEF_GOOGLE_API_KEY, this.globalInfo.getGoogleKey4API());	
		// --- this.DEF_GOOGLE_HTTP_REF --------------
		if (this.globalInfo.getGoogleHttpRef()!=null) eclipsePreferences.put(DEF_GOOGLE_HTTP_REF, this.globalInfo.getGoogleHttpRef());	
		
		
		// --- this.DEF_UPDATE_AUTOCONFIG -------------
		eclipsePreferences.putInt(DEF_UPDATE_AUTOCONFIG, this.globalInfo.getUpdateAutoConfiguration());	
		// --- this.DEF_UPDATE_KEEP_DICTIONAR ---------		
		eclipsePreferences.putInt(DEF_UPDATE_KEEP_DICTIONARY, this.globalInfo.getUpdateKeepDictionary());
		// --- this.DEF_UPDATE_DATE_LAST_CHECKED ------
		eclipsePreferences.putLong(DEF_UPDATE_DATE_LAST_CHECKED, this.globalInfo.getUpdateDateLastChecked());

		
		// --- this.DEF_KEYSTORE_FILE ------
		if (this.globalInfo.getKeyStoreFile()!=null) eclipsePreferences.put(DEF_KEYSTORE_FILE, this.globalInfo.getKeyStoreFile());
		// --- this.DEF_KEYSTORE_PASSWORD ------
		if (this.globalInfo.getKeyStorePasswordEncrypted()!=null) eclipsePreferences.put(DEF_KEYSTORE_PASSWORD, this.globalInfo.getKeyStorePasswordEncrypted());
		// --- this.DEF_TRUSTSTORE_FILE ------
		if (this.globalInfo.getTrustStoreFile()!=null) eclipsePreferences.put(DEF_TRUSTSTORE_FILE, this.globalInfo.getTrustStoreFile());
		// --- this.DEF_TRUSTSTORE_PASSWORD ------
		if (this.globalInfo.getTrustStorePasswordEncrypted()!=null) eclipsePreferences.put(DEF_TRUSTSTORE_PASSWORD, this.globalInfo.getTrustStorePasswordEncrypted());
		
		
		// --- this.DEF_DeviceServcie_ProjectFolder ---
		if (this.globalInfo.getDeviceServiceProjectFolder()!=null) eclipsePreferences.put(DEF_DEVICE_SERVICE_PROJECT_FOLDER, this.globalInfo.getDeviceServiceProjectFolder());
		// --- this.DEF_DeviceServcie_ExecAs ----------
		eclipsePreferences.put(DEF_DEVICE_SERVICE_EXEC_AS, this.globalInfo.getDeviceServiceExecutionMode().toString());
		// --- this.DEF_DeviceServcie_ProjectFolder ---
		if (this.globalInfo.getDeviceServiceSetupSelected()!=null) eclipsePreferences.put(DEF_DEVICE_SERVICE_SETUP, this.globalInfo.getDeviceServiceSetupSelected());
		// --- this.DEF_DeviceServcie_Agents ----------
		if (this.globalInfo.getDeviceServiceAgents()!=null) eclipsePreferences.put(DEF_DEVICE_SERVICE_AGENT_NAME, this.getDeviceAgentsPropertyValue(this.globalInfo.getDeviceServiceAgents()));
		// --- this.DEF_DeviceServcie_Vis -------------
		eclipsePreferences.put(DEF_DEVICE_SERVICE_VISUALIZATION, this.globalInfo.getDeviceServiceAgentVisualisation().toString());
		
		
		// --- this.DEF_OIDC_ISSUER_URI ------------------
		if (this.globalInfo.getOIDCIssuerURI()!=null) eclipsePreferences.put(DEF_OIDC_ISSUER_URI, this.globalInfo.getOIDCIssuerURI());	
		// --- this.DEF_OIDC_USERNAME -------------
		if (this.globalInfo.getOIDCUsername()!=null) eclipsePreferences.put(DEF_OIDC_USERNAME, this.globalInfo.getOIDCUsername());
		
	}	

	/**
	 * Gets the device agents property value.
	 *
	 * @param deviceAgentVector the device agent vector
	 * @return the device agents property value
	 */
	private String getDeviceAgentsPropertyValue(Vector<DeviceAgentDescription> deviceAgentVector) {
		
		String propValue = "";
		for (int i = 0; i < deviceAgentVector.size(); i++) {
			
			String singleAgent = deviceAgentVector.get(i).toString();
			if (propValue==null || propValue.isEmpty()==true) {
				propValue = singleAgent;
			} else {
				propValue += ", " + singleAgent;
			}
		}
		return propValue;
	}
	/**
	 * Return the device agent vector.
	 *
	 * @param propValue the prop value
	 * @return the device agents
	 */
	private Vector<DeviceAgentDescription> getDeviceAgentsVector(String propValue) {
		
		if (propValue==null || propValue.isEmpty()) return null;
		
		Vector<DeviceAgentDescription> deviceAgents = new Vector<>();
		if (propValue.contains(",")==false) {
			deviceAgents.add(new DeviceAgentDescription(propValue));
		} else {
			String[] agentDesc = propValue.split(",");
			for (int i = 0; i < agentDesc.length; i++) {
				deviceAgents.add(new DeviceAgentDescription(agentDesc[i]));
			}
		}
		return deviceAgents;
	}
	
}
