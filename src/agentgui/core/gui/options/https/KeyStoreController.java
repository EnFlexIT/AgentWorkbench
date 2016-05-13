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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class allows the user to manage a KeyStore.
 * 
 * @author Mohamed Amine JEDIDI <mohamedamine_jedidi@outlook.com>
 * @version 1.0
 * @since 05-04-2016
 */
public class KeyStoreController {

	private static Runtime runtime = Runtime.getRuntime();
	private static StringBuilder builder;
	private static Process process;
	private static BufferedReader bufferedReader;
	private static KeyStore keystore;
	private static InputStream inputStream;
	private static Enumeration<String> enumeration;
	private static String Alias;
	private static String line;
	private static JFrame frame = null;

	/**
	 * This Initializes the KeyStoreController.
	 */
	public KeyStoreController() {
	}

	/**
	 * This method allows the user to create a KeyStore and protect its
	 * integrity with a password
	 * 
	 * @param Informations of the provider
	 * @param Alias for the KeyStore
	 * @param KeyStoreName
	 * @param KeystorePassword
	 * @param keyStorePath 
	 */
	public static void CreateKeyStore(String Informations, String Alias, String KeyStoreName, String KeystorePassword, String keyStorePath) {
		/*
		 * ---------------------------------------------------------------------
		 * ------- Execute the command line to create the KeyStore -------------
		 * ----------------------- ---------------------------------------------
		 * 
		 * keytool -genkey -dname {informations} -alias {alias} -keypass
		 * {key_password} -keystore {keystore_name} -storepass
		 * {keystore_password}
		 */

		try {
			process = runtime.exec(
					"keytool -genkey -dname \"" + Informations + "\" -alias " + Alias + " -keystore " + keyStorePath + KeyStoreName
							+ "KeyStore.jks " + " -storepass " + KeystorePassword + " -keypass " + KeystorePassword);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * This method returns the content of a KeyStore
	 * 
	 * @param KeyStoreName
	 * @param keyStorePassword
	 * @return the content
	 */
	public static String ListKeyStoreContent(String KeyStoreName, String keyStorePassword) {
		String content = null;
		builder = new StringBuilder();
		line = null;
		bufferedReader = null;
		/*
		 * ---------------------------------------------------------------------
		 * --------- Execute the command line to list the content --------------
		 * ----------------------- of the KeyStore -----------------------------
		 * ---------------------------------------------------------------------
		 * 
		 * keytool -list -v -keystore {keystore_name} -storepass
		 * {keystore_password}
		 */
		try {
			process = runtime.exec("keytool -list -v -keystore " + KeyStoreName + " -storepass " + keyStorePassword);

		} catch (IOException e) {
			e.printStackTrace();
		}
		// ---- Create a BufferedReader ----------------------------------------
		bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		try {
			while ((line = bufferedReader.readLine()) != null)
				builder.append(line);

		} catch (IOException e) {
			e.printStackTrace();
		}
		// ---- Get the content ------------------------------------------------
		content = builder.toString().replace(" ", "");
		return content;
	}

	/**
	 * This method returns the KeyStore alias
	 * 
	 * @param KeyStoreName
	 * @param KeyStorePassword
	 * @return Alias
	 */
	public static String GetKeyStoreAlias(String KeyStoreName, String KeyStorePassword) {
		try {
			// --- Specify the KeyStore type -----------------------------------
			keystore = KeyStore.getInstance(KeyStore.getDefaultType());

			// --- Creates a FileInputStream from the KeyStore -----------------
			inputStream = new FileInputStream(KeyStoreName);

			// --- Loads the KeyStore from the given InputStream ---------------
			keystore.load(inputStream, KeyStorePassword.toCharArray());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			/*
			 * -------- Open a warning Message dialog if the password ---------
			 * --------------------- is incorrect -----------------------------
			 */
			JOptionPane.showMessageDialog(frame, e.getMessage() + " !");
			e.printStackTrace();
			return null;
		}
		// --- Get KeyStore's Alias -------------------------------------------
		try {
			enumeration = keystore.aliases();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		while (enumeration.hasMoreElements()) {
			Alias = (String) enumeration.nextElement();
		}
		return Alias;
	}

	/**
	 * This method allows the user to change the password and alias of a
	 * KeyStore
	 * 
	 * @param KeyStoreName
	 * @param OldAlias
	 * @param NewAlias
	 * @param OldKeyStorePassword
	 * @param NewKeyStorePassword
	 */
	public static void EditKeyStore(String KeyStoreName, String OldAlias, String NewAlias, String NewKeyStorePassword,
			String OldKeyStorePassword) {

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
			process1 = runtime.exec("keytool -keypasswd -alias " + OldAlias + " -keypass " + OldKeyStorePassword
					+ " -new " + NewKeyStorePassword + " -keystore " + KeyStoreName + " -storepass "
					+ OldKeyStorePassword);
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
			process2 = runtime
					.exec("keytool -changealias -alias " + OldAlias + " -destalias " + NewAlias + " -keystore "
							+ KeyStoreName + " -storepass " + OldKeyStorePassword + " -keypass " + NewKeyStorePassword);
		} catch (IOException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(frame, e1.getMessage() + " !");
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
			process3 = runtime.exec("keytool -alias " + NewAlias + " -storepasswd -new " + NewKeyStorePassword
					+ " -keystore " + KeyStoreName + " -storepass " + OldKeyStorePassword);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			process3.waitFor();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(frame, e1.getMessage() + " !");
		}
	}
	/**
	 * This method allows the user to export certificate from KeyStore
	 * 
	 * @param KeyStoreName
	 * @param KeyStorePassword
	 * @param CertificateName
	 */
	public static void ExportCertificate(String KeyStoreName, String KeyStorePassword, String CertificateName) {
		/*
		 * ---------------------------------------------------------------------
		 * ------- Execute the command line to create the certificate ----------
		 * ---------------------------------------------------------------------
		 * 
		 * keytool -export -alias {alias} -keystore {keystore_name} -storepass
		 * {keystore_password} -file {certificate_name}
		 */
		process = null;
		try {
			process = runtime.exec("keytool -export -alias " + Alias + " -keystore " + KeyStoreName + " -storepass "
					+ KeyStorePassword + " -file " + CertificateName + ".cer");
		} catch (IOException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(frame, e1.getMessage() + " !");
		}
	}
}
