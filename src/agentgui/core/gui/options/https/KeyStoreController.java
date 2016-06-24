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
package agentgui.core.gui.options.https;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.Enumeration;
import java.awt.Dialog;
import javax.swing.JOptionPane;

/**
 * This class allows the user to manage a KeyStore.
 * 
 * @author Mohamed Amine JEDIDI <mohamedamine_jedidi@outlook.com>
 * @version 1.0
 * @since 05-04-2016
 */
public class KeyStoreController {


	/**
	 * This Initializes the KeyStoreController.
	 */
	public KeyStoreController() {
	}

	/**
	 * This method allows the user to create a KeyStore and protect its
	 * integrity with a password
	 * 
	 * @param informations of the provider
	 * @param alias for the KeyStore
	 * @param keyStoreName
	 * @param keystorePassword
	 * @param keyStorePath 
	 */
	public void createKeyStore(String informations, String alias, String keyStoreName, String keystorePassword, String keyStorePath) {
		/*
		 * ---------------------------------------------------------------------
		 * ------- Execute the command line to create the KeyStore -------------
		 * ----------------------- ---------------------------------------------
		 * 
		 * keytool -genkey -dname {informations} -alias {alias} -keypass
		 * {key_password} -keystore {keystore_name} -storepass {keystore_password}
		 */
		try {
			Runtime runtime = Runtime.getRuntime();
			runtime.exec("keytool -genkey -dname \"" + informations + "\" -alias " + alias + " -keystore " + keyStorePath + keyStoreName + "KeyStore.jks " + " -storepass " + keystorePassword + " -keypass " + keystorePassword);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * This method returns the content of a KeyStore
	 * 
	 * @param keyStoreName
	 * @param keyStorePassword
	 * @return the content
	 */
	public KeyStoreSettings listKeyStoreContent(String keyStoreName, String keyStorePassword) {
		KeyStoreSettings keyStoreSettings = new KeyStoreSettings();
		FileInputStream fileInputStream = null;
		String provider = null;
		try {
			// --- Creates a FileInputStream from the TrustStore ---
			File file = new File(keyStoreName);
			fileInputStream = new FileInputStream(file);
			// --- Loads the KeyStore from the given InputStream ---
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(fileInputStream, keyStorePassword.toCharArray());
			// --- Get KeyStore alias ------------------------------
			Enumeration<String> enumeration = keystore.aliases();
			// --- Get KeyStore provider ------------------------------
			String alias = (String) enumeration.nextElement();
			Certificate certificate = keystore.getCertificate(alias);
	        provider = ( (X509Certificate) certificate).getIssuerDN().getName();
			
		} catch (java.security.cert.CertificateException | NoSuchAlgorithmException | FileNotFoundException | KeyStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ---- Get the content ------------------------------------------------
		keyStoreSettings.setFullName(provider.substring(provider.indexOf("CN=") + 3, provider.indexOf(", OU")));
		keyStoreSettings.setOrginazationalUnit(provider.substring(provider.indexOf("OU=") + 3, provider.indexOf(", O=")));
		keyStoreSettings.setOrganization(provider.substring(provider.indexOf("O=") + 2, provider.indexOf(", L")));
		keyStoreSettings.setCityOrLocality(provider.substring(provider.indexOf("L=") + 2, provider.indexOf(", ST")));
		keyStoreSettings.setStateOrProvince(provider.substring(provider.indexOf("ST=") + 3, provider.indexOf(", C")));
		keyStoreSettings.setCoutryCode(provider.substring(provider.indexOf("C=") + 2, provider.indexOf("C=") + 4));
		
		return keyStoreSettings;
	}

	/**
	 * This method returns the KeyStore alias.
	 *
	 * @param keyStoreName the key store name
	 * @param keyStorePassword the key store password
	 * @param ownerDialog the owner dialog
	 * @return Alias
	 */
	public String getKeyStoreAlias(String keyStoreName, String keyStorePassword, Dialog ownerDialog) {

		KeyStore keystore = null;
		String alias = null;
		try {
			// --- Specify the KeyStore type -----------------------------------
			keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			// --- Creates a FileInputStream from the KeyStore -----------------
			InputStream inputStream = new FileInputStream(keyStoreName);
			// --- Loads the KeyStore from the given InputStream ---------------
			keystore.load(inputStream, keyStorePassword.toCharArray());
			// --- Get KeyStore's Alias -------------------------------------------
			Enumeration<String> enumeration = keystore.aliases();
			if (enumeration.hasMoreElements()) {
				alias = (String) enumeration.nextElement();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			 // --- Open a warning Message dialog if the password is incorrect --
			if (ownerDialog!=null) {
				JOptionPane.showMessageDialog(ownerDialog, e.getMessage() + " !");	
			}
			e.printStackTrace();
			return null;
		}
		return alias;
	}

	/**
	 * This method allows the user to change the password and alias of a
	 * KeyStore.
	 *
	 * @param keyStoreName the KeyStore name
	 * @param oldAlias the old alias
	 * @param newAlias the new alias
	 * @param newKeyStorePassword the new KeyStore password
	 * @param oldKeyStorePassword the old KeyStore password
	 * @param ownerDialog the owner dialog
	 */
	public void editKeyStore(String keyStoreName, String oldAlias, String newAlias, String newKeyStorePassword, String oldKeyStorePassword, Dialog ownerDialog) {

		Runtime runtime = Runtime.getRuntime();
		Process process1 = null;
		Process process2 = null;
		Process process3 = null;
		/*
		 * ---------------------------------------------------------------------
		 * ------------- Execute the command line to update --------------------
		 * ---------------------- the Key password -----------------------------
		 * ---------------------------------------------------------------------
		 * 
		 * keytool -keypasswd -alias {alias} -keypass {key_password} -new
		 * {new_password} -keystore {keystore_name} -storepass {old_password}
		 */
		try {
			process1 = runtime.exec("keytool -keypasswd -alias " + oldAlias + " -keypass " + oldKeyStorePassword + " -new " + newKeyStorePassword + " -keystore " + keyStoreName + " -storepass " + oldKeyStorePassword);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			process1.waitFor();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		/*
		 * ---------------------------------------------------------------------
		 * --------- Execute the command line to update the alias --------------
		 * ---------------------------------------------------------------------
		 * 
		 * keytool -changealias -alias {old_alias} -destalias {new_alias}
		 * -keystore {keystore_name} -storepass {old_password} -keypass
		 * {key_password}
		 */
		try {
			process2 = runtime.exec("keytool -changealias -alias " + oldAlias + " -destalias " + newAlias + " -keystore " + keyStoreName + " -storepass " + oldKeyStorePassword + " -keypass " + newKeyStorePassword);
		} catch (IOException e1) {
			e1.printStackTrace();
			if (ownerDialog!=null) {
				JOptionPane.showMessageDialog(ownerDialog, e1.getMessage() + " !");	
			}
		}
		try {
			process2.waitFor();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		/*
		 * ---------------------------------------------------------------------
		 * ------ Execute the command line to update KeyStore password ---------
		 * ---------------------------------------------------------------------
		 * 
		 * keytool -alias {new_alias} -storepasswd -new {new_password} -keystore
		 * {keystore_name} -storepass {old_password}
		 * 
		 */
		try {
			process3 = runtime.exec("keytool -alias " + newAlias + " -storepasswd -new " + newKeyStorePassword + " -keystore " + keyStoreName + " -storepass " + oldKeyStorePassword);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			process3.waitFor();
		} catch (InterruptedException e1) {
			if (ownerDialog!=null) {
				JOptionPane.showMessageDialog(ownerDialog, e1.getMessage() + " !");	
			}
			e1.printStackTrace();
		}
	}
	
	/**
	 * This method allows the user to export certificate from KeyStore.
	 *
	 * @param keyStoreName the KeyStore name
	 * @param alias the alias
	 * @param keyStorePassword the KeyStore password
	 * @param certificateName the certificate name
	 * @param ownerDialog the owner dialog
	 */
	public void exportCertificate(String keyStoreName, String alias, String keyStorePassword, String certificateName , String validity, Dialog ownerDialog) {
		Runtime runtime = Runtime.getRuntime();
		Process process1 = null;
		/*
		 * ---------------------------------------------------------------------
		 * ------- Execute the command line to generate the certificate --------
		 * --------------------- in the KeyStore file --------------------------
		 * ---------------------------------------------------------------------
		 * 
		 * keytool -selfcert -alias {alias} -keystore {keystore_name} -storepass
		 * {keystore_password} -keyalg RSA -validity {validity}
		 */
		
		try {
			process1 = runtime.exec("keytool -selfcert -alias " + alias + " -keystore " + keyStoreName + " -storepass " + keyStorePassword + " -keyalg RSA -validity " + validity );
		} catch (IOException e1) {
			e1.printStackTrace();
			if (ownerDialog!=null) {
				JOptionPane.showMessageDialog(ownerDialog, e1.getMessage() + " !");	
			}
		}
		
		try {
			process1.waitFor();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		
		/* 
		 * -------------------------------------------------------------------
		 * ----- Execute the command line to export the certificate ----------
		 * -------------------------------------------------------------------
		 * 
		 * keytool -export -alias {alias} -keystore {keystore_name} -storepass
		 * {keystore_password} -file {certificate_name}
		 */
		try {
			runtime.exec("keytool -export -alias " + alias + " -keystore " + keyStoreName + " -storepass " + keyStorePassword + " -file " + certificateName + ".cer"); 
		} catch (IOException e1) {
			e1.printStackTrace();
			if (ownerDialog!=null) {
				JOptionPane.showMessageDialog(ownerDialog, e1.getMessage() + " !");	
			}
		}
	}
}