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

	private boolean debug = true;
	
	private static final String DEF_PROJECTS_DIRECTORY = "00_PROJECTS_DIRECTORY";
	
	private static final String DEF_RUNAS = "01_RUNAS";
	private static final String DEF_RUNAS_Server = "Server";
	private static final String DEF_RUNAS_EmeddedSystemAgent = "EmbeddedSystemAgent";
	
	private static final String DEF_BENCH_VALUE = "02_BENCH_VALUE";
	private static final String DEF_BENCH_EXEC_ON = "03_BENCH_EXEC_ON";
	private static final String DEF_BENCH_SKIP_ALLWAYS = "04_BENCH_SKIP_ALLWAYS";

	private static final String DEF_LANGUAGE = "05_LANGUAGE";
	private static final String DEF_MAXIMiZE_MAIN_WINDOW = "06_MAXIMiZE_MAIN_WINDOW";
	
	private static final String DEF_LOGGING_ENABLED = "07_LOGGING_ENABLED";
	private static final String DEF_LOGGING_BASE_PATH = "08_LOGGING_BASE_PATH";
	
	private static final String DEF_AUTOSTART = "10_AUTOSTART";
	private static final String DEF_MASTER_URL = "11_MASTER_URL";
	private static final String DEF_MASTER_PORT = "12_MASTER_PORT";
	private static final String DEF_MASTER_PORT4MTP = "13_MASTER_PORT4MTP";
	private static final String DEF_MASTER_PROTOCOL = "14_MASTER_PROTOCOL";

	private static final String DEF_OWN_MTP_CREATION = "15_OWN_MTP_CREATION";
	private static final String DEF_OWN_MTP_IP = "16_OWN_MTP_IP";
	private static final String DEF_OWN_MTP_PORT = "17_OWN_MTP_PORT";
	private static final String DEF_OWN_MTP_PROTOCOL = "18_OWN_MTP_PROTOCOL";
	
	private static final String DEF_MASTER_DB_HOST = "20_MASTER_DB_HOST";
	private static final String DEF_MASTER_DB_NAME = "21_MASTER_DB_NAME";
	private static final String DEF_MASTER_DB_USER = "22_MASTER_DB_USER";
	private static final String DEF_MASTER_DB_PSWD = "23_MASTER_DB_PSWD";
	
	private static final String DEF_GOOGLE_API_KEY  = "30_GOOGLE_API_KEY";
	private static final String DEF_GOOGLE_HTTP_REF = "31_GOOGLE_HTTP_REF";
	
	private static final String DEF_UPDATE_SITE = "35_UPDATE_SITE";
	private static final String DEF_UPDATE_AUTOCONFIG = "36_UPDATE_AUTOCONFIG";
	private static final String DEF_UPDATE_KEEP_DICTIONARY = "37_UPDATE_KEEP_DICTIONARY";
	private static final String DEF_UPDATE_DATE_LAST_CHECKED = "38_UPDATE_DATE_LAST_CHECKED";
	
	private static final String DEF_DeviceServcie_ProjectFolder = "40_DEVICE_SERVICE_PROJECT";
	private static final String DEF_DeviceServcie_ExecAs = "41_DEVICE_SERVICE_EXEC_AS";
	private static final String DEF_DeviceServcie_Setup = "42_DEVICE_SERVICE_SETUP";
	private static final String DEF_DeviceServcie_Agent = "43_DEVICE_SERVICE_AGENT";
	private static final String DEF_DeviceServcie_AgentName = "44_DEVICE_SERVICE_AGENT_Name";
	private static final String DEF_DeviceServcie_Vis = "45_DEVICE_SERVICE_VISUALISATION";
	
	private static final String DEF_KEYSTORE_FILE = "50_KEY_STORE_FILE";
	private static final String DEF_KEYSTORE_PASSWORD = "51_KEY_STORE_PASSWORD";
	private static final String DEF_TRUSTSTORE_FILE = "52_TRUST_STORE_FILE";
	private static final String DEF_TRUSTSTORE_PASSWORD = "53_TRUST_STORE_PASSWORD";
	
	private static final String DEF_OIDC_USERNAME = "60_OIDC_USERNAME";
	private static final String DEF_OIDC_ISSUER_URI = "61_OIDC_ISSUER_URI";
	
	
	private GlobalInfo globalInfo;
	private IEclipsePreferences eclipsePreferences;
	private IEclipsePreferences.IPreferenceChangeListener changeListener;
	
	
	/**
	 * Instantiates the new bundle properties.
	 * @param globalInfo the global info
	 */
	public BundleProperties(GlobalInfo globalInfo) {
		this.globalInfo = globalInfo;
		this.setPreferencesGlobal();
	}

	/**
	 * Returns the eclipse preferences.
	 * @return the eclipse preferences
	 */
	private IEclipsePreferences getEclipsePreferences() {
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
	 * This method sets the values from the config-file to the
	 * Runtime Variables in class Global ('Application.RunInfo')
	 */
	private void setPreferencesGlobal() {
		
		boolean booleanPrefValue;
		int integerPrefValue;
		long longPrefValue;
		float floatPrefValue;
		String stringPrefValue;
		MtpProtocol mtpProtocol;

		
		// --- this.DEF_PROJECT_DIRECTORY ------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_PROJECTS_DIRECTORY, this.globalInfo.getDefaultProjectsDirectory());
		if (stringPrefValue!=null && stringPrefValue.equalsIgnoreCase("")==true) {
			this.globalInfo.setPathProjects(this.globalInfo.getDefaultProjectsDirectory());
		} else {
			this.globalInfo.setPathProjects(stringPrefValue);
		}


		// --- this.DEF_RUNAS ------------------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_RUNAS, ExecutionMode.APPLICATION.toString());
		if (stringPrefValue.equalsIgnoreCase(DEF_RUNAS_Server)==true) {
			this.globalInfo.setExecutionMode(ExecutionMode.SERVER);
		} else if (stringPrefValue.equalsIgnoreCase(DEF_RUNAS_EmeddedSystemAgent)==true) {
			this.globalInfo.setExecutionMode(ExecutionMode.DEVICE_SYSTEM);
		} else {
			this.globalInfo.setExecutionMode(ExecutionMode.APPLICATION);
		}


		// --- this.DEF_BENCH_VALUE ------------------
		floatPrefValue = this.getEclipsePreferences().getFloat(DEF_BENCH_VALUE, 0f);
		this.globalInfo.setBenchValue(floatPrefValue);
		// --- this.DEF_BENCH_EXEC_ON ----------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_BENCH_EXEC_ON, null);
		this.globalInfo.setBenchExecOn(stringPrefValue);
		// --- this.DEF_BENCH_SKIP_ALLWAYS -----------
		booleanPrefValue = this.getEclipsePreferences().getBoolean(DEF_BENCH_SKIP_ALLWAYS, false);
		this.globalInfo.setBenchAllwaysSkip(booleanPrefValue);


		// --- this.DEF_LANGUAGE ---------------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_LANGUAGE, Language.EN);
		this.globalInfo.setLanguage(stringPrefValue);


		// --- this.DEF_MAXIMiZE_MAIN_WINDOW ---------
		booleanPrefValue = this.getEclipsePreferences().getBoolean(DEF_MAXIMiZE_MAIN_WINDOW, false);
		this.globalInfo.setMaximzeMainWindow(booleanPrefValue);


		// --- this.DEF_LOGGING_ENABLED --------------
		booleanPrefValue = this.getEclipsePreferences().getBoolean(DEF_LOGGING_ENABLED, false);
		this.globalInfo.setLoggingEnabled(booleanPrefValue);
		// --- this.DEF_LOGGING_BASE_PATH ------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_LOGGING_BASE_PATH, null);
		this.globalInfo.setLoggingBasePath(stringPrefValue);


		// --- this.DEF_AUTOSTART --------------------
		booleanPrefValue = this.getEclipsePreferences().getBoolean(DEF_AUTOSTART, false);
		this.globalInfo.setServerAutoRun(booleanPrefValue);
		// --- this.DEF_MASTER_URL -------------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_MASTER_URL, "");
		this.globalInfo.setServerMasterURL(stringPrefValue.trim());
		// --- this.DEF_MASTER_PORT ------------------
		integerPrefValue = this.getEclipsePreferences().getInt(DEF_MASTER_PORT, 1099);
		this.globalInfo.setServerMasterPort(integerPrefValue);
		// --- this.DEF_MASTER_PORT4MTP --------------
		integerPrefValue = this.getEclipsePreferences().getInt(DEF_MASTER_PORT4MTP, 7778);
		this.globalInfo.setServerMasterPort4MTP(integerPrefValue);
		// --- this.DEF_MASTER_PROTOCOL --------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_MASTER_PROTOCOL, MtpProtocol.HTTP.toString());
		mtpProtocol = MtpProtocol.valueOf(stringPrefValue);
		this.globalInfo.setServerMasterProtocol(mtpProtocol);
		
		
		// --- this.DEF_OWN_MTP_CREATION -------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_OWN_MTP_CREATION, MTP_Creation.ConfiguredByJADE.toString());
		MTP_Creation ownMtpCreation = MTP_Creation.valueOf(stringPrefValue.trim());
		this.globalInfo.setOwnMtpCreation(ownMtpCreation);
		// --- this.DEF_OWN_MTP_IP -------------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_OWN_MTP_IP, PlatformJadeConfig.MTP_IP_AUTO_Config);
		this.globalInfo.setOwnMtpIP(stringPrefValue.trim());
		// --- this.DEF_OWN_MTP_PORT -------------------
		integerPrefValue = this.getEclipsePreferences().getInt(DEF_OWN_MTP_PORT, 7778);
		this.globalInfo.setOwnMtpPort(integerPrefValue);
		// --- this.DEF_OWN_MTP_PROTOCOL -------------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_OWN_MTP_PROTOCOL, MtpProtocol.HTTP.toString());
		mtpProtocol = MtpProtocol.valueOf(stringPrefValue);
		this.globalInfo.setMtpProtocol(mtpProtocol);
		
		
		// --- this.DEF_MASTER_DB_HOST ---------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_MASTER_DB_HOST, "");
		this.globalInfo.setServerMasterDBHost(stringPrefValue.trim());
		// --- this.DEF_MASTER_DB_NAME ---------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_MASTER_DB_NAME, "");
		this.globalInfo.setServerMasterDBName(stringPrefValue.trim());
		// --- this.DEF_MASTER_DB_USER ---------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_MASTER_DB_USER, "");
		this.globalInfo.setServerMasterDBUser(stringPrefValue.trim());
		// --- this.DEF_MASTER_DB_PSWD ---------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_MASTER_DB_PSWD, "");
		this.globalInfo.setServerMasterDBPswd(stringPrefValue.trim());
		
		
		// --- this.DEF_GOOGLE_API_KEY ---------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_GOOGLE_API_KEY, "");
		this.globalInfo.setGoogleKey4API(stringPrefValue.trim());
		// --- this.DEF_GOOGLE_HTTP_REF --------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_GOOGLE_HTTP_REF, "");
		this.globalInfo.setGoogleHttpRef(stringPrefValue.trim());
		
		
		// --- this.DEF_UPDATE_SITE ------------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_UPDATE_SITE, "");
		this.globalInfo.setUpdateSite(stringPrefValue.trim());
		// --- this.DEF_UPDATE_AUTOCONFIG -------------
		integerPrefValue = this.getEclipsePreferences().getInt(DEF_UPDATE_AUTOCONFIG, 0);
		this.globalInfo.setUpdateAutoConfiguration(integerPrefValue);
		// --- this.DEF_UPDATE_KEEP_DICTIONARY --------
		integerPrefValue = this.getEclipsePreferences().getInt(DEF_UPDATE_KEEP_DICTIONARY, 1);
		this.globalInfo.setUpdateKeepDictionary(integerPrefValue);
		// --- this.DEF_UPDATE_DATE_LAST_CHECKED ------
		longPrefValue = this.getEclipsePreferences().getLong(DEF_UPDATE_DATE_LAST_CHECKED, 0);
		this.globalInfo.setUpdateDateLastChecked(longPrefValue);
		
		
		// --- this.DEF_DeviceServcie_ProjectFolder ---
		stringPrefValue = this.getEclipsePreferences().get(DEF_DeviceServcie_ProjectFolder, null);
		this.globalInfo.setDeviceServiceProjectFolder(stringPrefValue);	
		// --- this.DEF_DeviceServcie_ExecAs ----------
		stringPrefValue = this.getEclipsePreferences().get(DEF_DeviceServcie_ExecAs, DeviceSystemExecutionMode.SETUP.toString());
		DeviceSystemExecutionMode dsem = DeviceSystemExecutionMode.valueOf(stringPrefValue);
		this.globalInfo.setDeviceServiceExecutionMode(dsem);
		// --- this.DEF_DeviceServcie_Setup -----------
		stringPrefValue = this.getEclipsePreferences().get(DEF_DeviceServcie_Setup, "");
		this.globalInfo.setDeviceServiceSetupSelected(stringPrefValue);
		// --- this.DEF_DeviceServcie_Agent -----------
		stringPrefValue = this.getEclipsePreferences().get(DEF_DeviceServcie_Agent, "");
		this.globalInfo.setDeviceServiceAgentClassName(stringPrefValue.trim());
		// --- this.DEF_DeviceServcie_AgentName ------
		stringPrefValue = this.getEclipsePreferences().get(DEF_DeviceServcie_AgentName, "");
		this.globalInfo.setDeviceServiceAgentName(stringPrefValue.trim());
		// --- this.DEF_DeviceServcie_Vis ------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_DeviceServcie_Vis, EmbeddedSystemAgentVisualisation.NONE.toString());
		EmbeddedSystemAgentVisualisation esaVis = EmbeddedSystemAgentVisualisation.valueOf(stringPrefValue);
		this.globalInfo.setDeviceServiceAgentVisualisation(esaVis);
		
		
		// --- this.DEF_KEYSTORE_FILE ----------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_KEYSTORE_FILE, null);
		this.globalInfo.setKeyStoreFile(stringPrefValue);
		// --- this.DEF_KEYSTORE_PASSWORD ------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_KEYSTORE_PASSWORD, null);
		this.globalInfo.setKeyStorePasswordEncrypted(stringPrefValue);
		// --- this.DEF_TRUSTSTORE_FILE --------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_TRUSTSTORE_FILE, null);
		this.globalInfo.setTrustStoreFile(stringPrefValue);
		// --- this.DEF_TRUSTSTORE_PASSWORD ----------
		stringPrefValue = this.getEclipsePreferences().get(DEF_TRUSTSTORE_PASSWORD, null);
		this.globalInfo.setTrustStorePasswordEncrypted(stringPrefValue);
		
		
		// --- this.DEF_OIDC_ISSUER_URI -------------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_OIDC_ISSUER_URI, null);
		this.globalInfo.setOIDCIssuerURI(stringPrefValue);
		// --- this.DEF_OIDC_USERNAME -------------------
		stringPrefValue = this.getEclipsePreferences().get(DEF_OIDC_USERNAME, null);
		this.globalInfo.setOIDCUsername(stringPrefValue);
		
	}
	
	/**
	 * This method sets the values from the Runtime Variables in class Global ('Application.RunInfo')
	 * to this property-file / config-file / 'agentgui.xml' 
	 */
	private void setGlobal2Preferences() {
		
		// --- this.DEF_PROJECT_DIRECTORY ------------
		this.getEclipsePreferences().put(DEF_PROJECTS_DIRECTORY, this.globalInfo.getPathProjects());

		
		// --- this.DEF_RUNAS ------------------------
		this.getEclipsePreferences().put(DEF_RUNAS, this.globalInfo.getExecutionMode().toString()); 

		
		// --- this.DEF_BENCH_VALUE ------------------
		this.getEclipsePreferences().putFloat(DEF_BENCH_VALUE, this.globalInfo.getBenchValue());
		// --- this.DEF_BENCH_EXEC_ON ----------------
		if (this.globalInfo.getBenchExecOn()!=null) {
			this.getEclipsePreferences().put(DEF_BENCH_EXEC_ON, this.globalInfo.getBenchExecOn());	
		}
		// --- this.DEF_BENCH_SKIP_ALLWAYS -----------
		this.getEclipsePreferences().putBoolean(DEF_BENCH_SKIP_ALLWAYS, this.globalInfo.isBenchAllwaysSkip());
		
		
		// --- this.DEF_LANGUAGE ---------------------
		this.getEclipsePreferences().put(DEF_LANGUAGE, this.globalInfo.getLanguage());

		
		// --- this.DEF_MAXIMiZE_MAIN_WINDOW ---------
		this.getEclipsePreferences().putBoolean(DEF_MAXIMiZE_MAIN_WINDOW, this.globalInfo.isMaximzeMainWindow());			
		
		
		// --- this.DEF_LOGGING_ENABLED --------------
		this.getEclipsePreferences().putBoolean(DEF_LOGGING_ENABLED, this.globalInfo.isLoggingEnabled());			
		// --- this.DEF_LOGGING_BASE_PATH ------------
		this.getEclipsePreferences().put(DEF_LOGGING_BASE_PATH, this.globalInfo.getLoggingBasePath());
		
		
		// --- this.DEF_AUTOSTART --------------------
		this.getEclipsePreferences().putBoolean(DEF_AUTOSTART, this.globalInfo.isServerAutoRun());
		// --- this.DEF_MASTER_URL -------------------
		this.getEclipsePreferences().put(DEF_MASTER_URL, this.globalInfo.getServerMasterURL());
		// --- this.DEF_MASTER_PORT ------------------
		this.getEclipsePreferences().putInt(DEF_MASTER_PORT, this.globalInfo.getServerMasterPort());
		// --- this.DEF_MASTER_PORT4MTP --------------
		this.getEclipsePreferences().putInt(DEF_MASTER_PORT4MTP, this.globalInfo.getServerMasterPort4MTP());
		// --- this.DEF_MASTER_PROTOCOL --------------
		this.getEclipsePreferences().put(DEF_MASTER_PROTOCOL, this.globalInfo.getServerMasterProtocol().toString());

		
		// --- this.DEF_OWN_MTP_CREATION -------------
		this.getEclipsePreferences().put(DEF_OWN_MTP_CREATION, this.globalInfo.getOwnMtpCreation().toString());
		// --- this.DEF_OWN_MTP_IP -------------------
		this.getEclipsePreferences().put(DEF_OWN_MTP_IP, this.globalInfo.getOwnMtpIP());
		// --- this.DEF_OWN_MTP_PORT -----------------
		this.getEclipsePreferences().putInt(DEF_OWN_MTP_PORT, this.globalInfo.getOwnMtpPort());
		// --- this.DEF_OWN_MTP_PROTOCOL -----------------
		this.getEclipsePreferences().put(DEF_OWN_MTP_PROTOCOL, this.globalInfo.getMtpProtocol().toString());
		
		
		// --- this.DEF_MASTER_DB_HOST ---------------
		this.getEclipsePreferences().put(DEF_MASTER_DB_HOST, this.globalInfo.getServerMasterDBHost());	
		// --- this.DEF_MASTER_DB_NAME ---------------
		this.getEclipsePreferences().put(DEF_MASTER_DB_NAME, this.globalInfo.getServerMasterDBName());	
		// --- this.DEF_MASTER_DB_USER ---------------
		this.getEclipsePreferences().put(DEF_MASTER_DB_USER, this.globalInfo.getServerMasterDBUser());	
		// --- this.DEF_MASTER_DB_PSWD ---------------
		this.getEclipsePreferences().put(DEF_MASTER_DB_PSWD, this.globalInfo.getServerMasterDBPswd());
		
		
		// --- this.DEF_GOOGLE_API_KEY ---------------
		this.getEclipsePreferences().put(DEF_GOOGLE_API_KEY, this.globalInfo.getGoogleKey4API());	
		// --- this.DEF_GOOGLE_HTTP_REF --------------
		this.getEclipsePreferences().put(DEF_GOOGLE_HTTP_REF, this.globalInfo.getGoogleHttpRef());	
		
		
		// --- this.DEF_UPDATE_SITE ------------------
		this.getEclipsePreferences().put(DEF_UPDATE_SITE, this.globalInfo.getUpdateSite());	
		// --- this.DEF_UPDATE_AUTOCONFIG -------------
		this.getEclipsePreferences().putInt(DEF_UPDATE_AUTOCONFIG, this.globalInfo.getUpdateAutoConfiguration());	
		// --- this.DEF_UPDATE_KEEP_DICTIONAR ---------		
		this.getEclipsePreferences().putInt(DEF_UPDATE_KEEP_DICTIONARY, this.globalInfo.getUpdateKeepDictionary());
		// --- this.DEF_UPDATE_DATE_LAST_CHECKED ------
		this.getEclipsePreferences().putLong(DEF_UPDATE_DATE_LAST_CHECKED, this.globalInfo.getUpdateDateLastChecked());

		
		// --- this.DEF_KEYSTORE_FILE ------
		this.getEclipsePreferences().put(DEF_KEYSTORE_FILE, this.globalInfo.getKeyStoreFile());
		// --- this.DEF_KEYSTORE_PASSWORD ------
		this.getEclipsePreferences().put(DEF_KEYSTORE_PASSWORD, this.globalInfo.getKeyStorePasswordEncrypted());
		// --- this.DEF_TRUSTSTORE_FILE ------
		this.getEclipsePreferences().put(DEF_TRUSTSTORE_FILE, this.globalInfo.getTrustStoreFile());
		// --- this.DEF_TRUSTSTORE_PASSWORD ------
		this.getEclipsePreferences().put(DEF_TRUSTSTORE_PASSWORD, this.globalInfo.getTrustStorePasswordEncrypted());
		
		
		// --- this.DEF_DeviceServcie_ProjectFolder ---
		this.getEclipsePreferences().put(DEF_DeviceServcie_ProjectFolder, this.globalInfo.getDeviceServiceProjectFolder());
		// --- this.DEF_DeviceServcie_ExecAs ----------
		this.getEclipsePreferences().put(DEF_DeviceServcie_ExecAs, this.globalInfo.getDeviceServiceExecutionMode().toString());
		// --- this.DEF_DeviceServcie_ProjectFolder ---
		this.getEclipsePreferences().put(DEF_DeviceServcie_Setup, this.globalInfo.getDeviceServiceSetupSelected());
		// --- this.DEF_DeviceServcie_Agent -----------
		this.getEclipsePreferences().put(DEF_DeviceServcie_Agent, this.globalInfo.getDeviceServiceAgentClassName());
		// --- this.DEF_DeviceServcie_AgentName -----------
		this.getEclipsePreferences().put(DEF_DeviceServcie_AgentName, this.globalInfo.getDeviceServiceAgentName());
		// --- this.DEF_DeviceServcie_Vis -------------
		this.getEclipsePreferences().put(DEF_DeviceServcie_Vis, this.globalInfo.getDeviceServiceAgentVisualisation().toString());
		
		
		// --- this.DEF_OIDC_ISSUER_URI ------------------
		this.getEclipsePreferences().put(DEF_OIDC_ISSUER_URI, this.globalInfo.getOIDCIssuerURI());	
		// --- this.DEF_OIDC_USERNAME -------------
		this.getEclipsePreferences().put(DEF_OIDC_USERNAME, this.globalInfo.getOIDCUsername());
		
	}	
	
}
