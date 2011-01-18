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

public class FileProperties extends Properties {

	private static final long serialVersionUID = 7953205356494195952L;
	
	private GlobalInfo Global = Application.RunInfo;
	
	private String configFile = Global.getPathConfigFile();
	private String configFileDefaultComment = "";
	
	public final String DEF_RUNAS = "01_RUNAS";
	
	public final String DEF_BENCH_VALUE = "02_BENCH_VALUE";
	public final String DEF_BENCH_EXEC_ON = "03_BENCH_EXEC_ON";
	public final String DEF_BENCH_SKIP_ALLWAYS = "04_BENCH_SKIP_ALLWAYS";

	public final String DEF_LANGUAGE = "05_LANGUAGE";
	
	public final String DEF_AUTOSTART = "10_AUTOSTART";
	public final String DEF_MASTER_URL = "11_MASTER_URL";
	public final String DEF_MASTER_PORT = "12_MASTER_PORT";
	public final String DEF_MASTER_PORT4MTP = "13_MASTER_PORT4MTP";
	
	public final String DEF_MASTER_DB_HOST = "20_MASTER_DB_HOST";
	public final String DEF_MASTER_DB_NAME = "21_MASTER_DB_NAME";
	public final String DEF_MASTER_DB_USER = "22_MASTER_DB_USER";
	public final String DEF_MASTER_DB_PSWD = "23_MASTER_DB_PSWD";
	
	private String[] mandantoryProps = {this.DEF_RUNAS,
										this.DEF_BENCH_VALUE,
										this.DEF_BENCH_EXEC_ON,
										this.DEF_BENCH_SKIP_ALLWAYS,
										this.DEF_LANGUAGE,
										this.DEF_AUTOSTART,
										this.DEF_MASTER_URL,
										this.DEF_MASTER_PORT,
										this.DEF_MASTER_PORT4MTP
										};
	
	/**
	 * Default Constructor of this class. Uses the Default(!) config-file
	 */
	public FileProperties() {
		this.initialize();
//		println4SysProps();
//		println4EnvProps();
	}
	/**
	 * Constructor of this class, for an alternatively config-file
	 */
	public FileProperties( String fileName ) {
		this.configFile = fileName;
		this.initialize();
	}
	
	/**
	 * initialize the instance of this class
	 */
	private void initialize() {

		// --- set the Default-Comment for config file --------------
		this.setDefaultComment();
		
		// --- open or create the config file -----------------------
		try {
			// --- Does the configFile exists ? ---------------------
			if ( new File( configFile ).exists() == true) {
				// --- configFile found -----------------------------
				this.load(new FileInputStream( configFile ));	
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
	 * Overrides the super-method to sort the entries, called by the store method
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
	 * Runtime Variables in class Global
	 */
	private void setConfig2Global() {
		
		String propValue = "";
		
		// --- this.DEF_RUNAS ------------------------
		propValue = this.getProperty(this.DEF_RUNAS).trim();
		if ( propValue.equalsIgnoreCase("server") == true ) {
			Application.RunInfo.setRunAsServer(true);
		} else {
			Application.RunInfo.setRunAsServer(false);
		}
		
		// --- this.DEF_BENCH_VALUE ------------------
		propValue = this.getProperty(this.DEF_BENCH_VALUE).trim();
		if ( propValue.equalsIgnoreCase("") == true ) {
			Application.RunInfo.setBenchValue(0);
		} else {
			Application.RunInfo.setBenchValue(Float.parseFloat(propValue));
		}
		// --- this.DEF_BENCH_EXEC_ON ----------------
		propValue = this.getProperty(this.DEF_BENCH_EXEC_ON).trim();
		if ( propValue.equalsIgnoreCase("") == true ) {
			Application.RunInfo.setBenchExecOn(null);
		} else {
			Application.RunInfo.setBenchExecOn(propValue);
		}
		// --- this.DEF_BENCH_SKIP_ALLWAYS -----------
		propValue = this.getProperty(this.DEF_BENCH_SKIP_ALLWAYS).trim();
		if ( propValue.equalsIgnoreCase("true") == true ) {
			Application.RunInfo.setBenchAllwaysSkip(true);
		} else {
			Application.RunInfo.setBenchAllwaysSkip(false);
		}
		
		// --- this.DEF_LANGUAGE ---------------------
		propValue = this.getProperty(this.DEF_LANGUAGE).trim();
		if ( propValue!=null ) {
			Application.RunInfo.setLanguage(propValue);
		} else {
			Application.RunInfo.setLanguage("en");
		}
		
		// --- this.DEF_AUTOSTART --------------------
		propValue = this.getProperty(this.DEF_AUTOSTART).trim();
		if ( propValue.equalsIgnoreCase("true") == true ) {
			Application.RunInfo.setServerAutoRun(true);
		} else {
			Application.RunInfo.setServerAutoRun(false);
		}
		// --- this.DEF_MASTER_URL -------------------
		propValue = this.getProperty(this.DEF_MASTER_URL).trim();
		if ( propValue.equalsIgnoreCase("") == false ) {
			Application.RunInfo.setServerMasterURL(propValue.trim());
		} else {
			Application.RunInfo.setServerMasterURL(null);
		}
		// --- this.DEF_MASTER_PORT ------------------
		propValue = this.getProperty(this.DEF_MASTER_PORT).trim();
		if ( propValue.equalsIgnoreCase("") == false ) {
			Integer propValueInt = Integer.parseInt(propValue.trim());
			Application.RunInfo.setServerMasterPort(propValueInt);
		} else {
			Application.RunInfo.setServerMasterPort(0);
		}
		// --- this.DEF_MASTER_PORT4MTP --------------
		propValue = this.getProperty(this.DEF_MASTER_PORT4MTP);
		if ( propValue.equalsIgnoreCase("") == false ) {
			Integer propValueInt = Integer.parseInt(propValue.trim());
			Application.RunInfo.setServerMasterPort4MTP(propValueInt);
		} else {
			Application.RunInfo.setServerMasterPort4MTP(0);
		}
		
		// --- this.DEF_MASTER_DB_HOST ---------------
		propValue = this.getProperty(this.DEF_MASTER_DB_HOST);
		if ( propValue!=null &&  propValue.equalsIgnoreCase("") == false ) {
			Application.RunInfo.setServerMasterDBHost(propValue.trim());
		} else {
			Application.RunInfo.setServerMasterDBHost(null);
		}
		// --- this.DEF_MASTER_DB_NAME ---------------
		propValue = this.getProperty(this.DEF_MASTER_DB_NAME);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			Application.RunInfo.setServerMasterDBName(propValue.trim());
		} else {
			Application.RunInfo.setServerMasterDBName(null);
		}
		// --- this.DEF_MASTER_DB_USER ---------------
		propValue = this.getProperty(this.DEF_MASTER_DB_USER);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			Application.RunInfo.setServerMasterDBUser(propValue.trim());
		} else {
			Application.RunInfo.setServerMasterDBUser(null);
		}
		// --- this.DEF_MASTER_DB_PSWD ---------------
		propValue = this.getProperty(this.DEF_MASTER_DB_PSWD);
		if ( propValue!=null && propValue.equalsIgnoreCase("") == false ) {
			Application.RunInfo.setServerMasterDBPswd(propValue.trim());
		} else {
			Application.RunInfo.setServerMasterDBPswd(null);
		}
		
	}

	/**
	 * This method sets the values from the Runtime Variables 
	 * in class Global ot the config-file 
	 */
	private void setGlobal2Config() {
		
		// --- this.DEF_RUNAS ------------------------
		if ( Application.RunInfo.isRunAsServer() == true ) {
			this.setProperty(this.DEF_RUNAS, "Server");
		} else {
			this.setProperty(this.DEF_RUNAS, "Application");
		}
		
		
		// --- this.DEF_BENCH_VALUE ------------------
		this.setProperty(this.DEF_BENCH_VALUE, Application.RunInfo.getBenchValue().toString());
		// --- this.DEF_BENCH_EXEC_ON ----------------
		if (Application.RunInfo.getBenchExecOn()!=null) {
			this.setProperty(this.DEF_BENCH_EXEC_ON, Application.RunInfo.getBenchExecOn());	
		}
		// --- this.DEF_BENCH_SKIP_ALLWAYS -----------
		if ( Application.RunInfo.isBenchAllwaysSkip() == true ) {
			this.setProperty(this.DEF_BENCH_SKIP_ALLWAYS,"true");	
		} else {
			this.setProperty(this.DEF_BENCH_SKIP_ALLWAYS,"false");
		}
		
		// --- this.DEF_LANGUAGE ---------------------
		if (Application.RunInfo.getLanguage()==null) {
			this.setProperty(this.DEF_LANGUAGE, "en");			
		} else {
			this.setProperty(this.DEF_LANGUAGE, Application.RunInfo.getLanguage());
		}

		
		// --- this.DEF_AUTOSTART --------------------
		if ( Application.RunInfo.isServerAutoRun() == true ) {
			this.setProperty(this.DEF_AUTOSTART, "true");
		} else {
			this.setProperty(this.DEF_AUTOSTART, "false");
		}
		// --- this.DEF_MASTER_URL -------------------
		if (Application.RunInfo.getServerMasterURL() == null) {
			this.setProperty(this.DEF_MASTER_URL, "");
		} else {
			this.setProperty(this.DEF_MASTER_URL, Application.RunInfo.getServerMasterURL());	
		}

		// --- this.DEF_MASTER_PORT ------------------
		this.setProperty(this.DEF_MASTER_PORT, Application.RunInfo.getServerMasterPort().toString());

		// --- this.DEF_MASTER_PORT4MTP --------------
		this.setProperty(this.DEF_MASTER_PORT4MTP, Application.RunInfo.getServerMasterPort4MTP().toString());

		
		// --- this.DEF_MASTER_DB_HOST ---------------
		if (Application.RunInfo.getServerMasterDBHost() == null) {
			this.setProperty(this.DEF_MASTER_DB_HOST, "");
		} else {
			this.setProperty(this.DEF_MASTER_DB_HOST, Application.RunInfo.getServerMasterDBHost());	
		}
		// --- this.DEF_MASTER_DB_NAME ---------------
		if (Application.RunInfo.getServerMasterDBName() == null) {
			this.setProperty(this.DEF_MASTER_DB_NAME, "");
		} else {
			this.setProperty(this.DEF_MASTER_DB_NAME, Application.RunInfo.getServerMasterDBName());	
		}
		// --- this.DEF_MASTER_DB_USER ---------------
		if (Application.RunInfo.getServerMasterDBUser() == null) {
			this.setProperty(this.DEF_MASTER_DB_USER, "");
		} else {
			this.setProperty(this.DEF_MASTER_DB_USER, Application.RunInfo.getServerMasterDBUser());	
		}
		// --- this.DEF_MASTER_DB_PSWD ---------------
		if (Application.RunInfo.getServerMasterDBPswd() == null) {
			this.setProperty(this.DEF_MASTER_DB_PSWD, "");
		} else {
			this.setProperty(this.DEF_MASTER_DB_PSWD, Application.RunInfo.getServerMasterDBPswd());	
		}
		
	}	
	
	/**
	 * This method sets the mandantory properties to the config-file 
	 */
	private void setDefaultConfigValues() {
		for (int i = 0; i < mandantoryProps.length; i++) {
			// ----------------------------------------
			// --- Here are some mandantory Props ----- 
			this.setProperty( mandantoryProps[i], "" );
			// ----------------------------------------
			// --- Here are some mandantory Values ---- 
			if ( mandantoryProps[i].equalsIgnoreCase(this.DEF_MASTER_PORT) ) {				
				this.setProperty(this.DEF_MASTER_PORT, Application.RunInfo.getJadeLocalPort().toString());
			}
			if ( mandantoryProps[i].equalsIgnoreCase(this.DEF_MASTER_PORT4MTP) ) {				
				this.setProperty(this.DEF_MASTER_PORT4MTP, Application.RunInfo.getServerMasterPort4MTP().toString());
			}
			// ----------------------------------------
		}
	}

	/**
	 * This method check if some mandantory properties in the
	 * the config-file are available. If not they will be added. 
	 */
	private void checkDefaultConfigValues() {
		
		boolean somethingMissed = false;
		// --- Search all mandantory property-values ------
		for (int i = 0; i < mandantoryProps.length; i++) {
			if ( this.containsKey(mandantoryProps[i])  == false ) {
				
				// ----------------------------------------
				// --- Here are some Mandantory Values ---- 
				if ( mandantoryProps[i].equals(this.DEF_LANGUAGE) ) {
					this.setProperty( this.DEF_LANGUAGE, "en" );
				} else {
					this.setProperty( mandantoryProps[i], "" );	
				}
				// ----------------------------------------
				somethingMissed = true;
			}			
		}
		// --- If something was not there, save the file --
		if ( somethingMissed == true ) {
			this.setConfig2Global();
			this.save();
		}
	}
	
	/**
	 * Set's the default comment for the Local configFile 'config.ini'.
	 * This text is hard coded !
	 */
	private void setDefaultComment() {

		String defaultComment = "";		
		defaultComment = defaultComment + " Configuration of " + Global.getApplicationTitle() + " (Version: " + Global.AppVersion() + ")\n"; 
		defaultComment = defaultComment + " by Christian Derksen - DAWIS - University Duisburg-Essen\n";
		defaultComment = defaultComment + " Email: christian.derksen@icb.uni-due.de\n";
		configFileDefaultComment = defaultComment;
	}
	
	/**
	 * This method saves the current state of the 
	 * configuration to the predetermined file   
	 */
	public void save() {
		// --- getting the current values of the mandantory variables ---
		this.setGlobal2Config();
		// --- Save the config-file -------------------------------------
		try {
			this.store(new FileOutputStream( configFile ), configFileDefaultComment);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This methd prints out every available value of the system properties
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
	 * This methd prints out every available value of the system environment
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
