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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class allows the user to manage a TrustStore.
 * 
 * @author Mohamed Amine JEDIDI <mohamedamine_jedidi@outlook.com>
 * @version 1.0
 * @since 05-04-2016
 */
public class TrustStoreController {

	private Runtime runtime = Runtime.getRuntime();
	private InputStream certInputStream;
	private FileInputStream fileInputStream;
	private FileOutputStream fileOutputStream;
	private File trustStoreFile;
	private KeyStore trustStore;
	private CertificateFactory certificateFactory;
	private Certificate certificate;
	private DefaultListModel<String> certificatesAliasModel;
	private JFrame frame;

	/**
	 * This Initializes the TrustStoreController.
	 */
	public TrustStoreController() {

	}

	/**
	 * This method allows the user to create a TrustStore and protect its
	 * integrity with a password.
	 *
	 * @param TrustStoreName the trust store name
	 * @param TrustStorePassword the trust store password
	 * @param trustStorePath the trust store path
	 */
	public void CreateTrustStore(String TrustStoreName, String TrustStorePassword, String trustStorePath) {

		try {
			// ----- Specify the TrustStore file type -------------------------
			KeyStore emptyTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			// ----- Create an empty TrustStore file --------------------------
			emptyTrustStore.load(null, TrustStorePassword.toCharArray());

			// ----- Create a FileOutputStream --------------------------------
			FileOutputStream fos = new FileOutputStream(trustStorePath);
			// ----- Store the empty TrustStore -------------------------------
			emptyTrustStore.store(fos, TrustStorePassword.toCharArray());
			// ----- Close the FileOutputStream -------------------------------
			fos.close();
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (KeyStoreException e1) {
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (CertificateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(frame, e1.getMessage() + " !");
		}
	}


	/**
	 * This method open a TrustStore. It returns true if the password is correct.
	 *
	 * @param TrustStoreName the TrustStore name
	 * @param TrustStorePassword the TrustStore password
	 * @return true, if successful
	 */
	public boolean OpenTrustStore(String TrustStoreName, String TrustStorePassword) {
		boolean password = true;
		// --- Creates a FileInputStream from the TrustStore -------------------
		File file = new File(TrustStoreName);
		KeyStore trustStore = null;
		try {
			fileInputStream = new FileInputStream(file);

			// --- Loads the TrustStore from the given InputStream -------------
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(fileInputStream, TrustStorePassword.toCharArray());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// --- Open a warning Message dialog if the password is incorrect --
			JOptionPane.showMessageDialog(frame, e.getMessage() + " !");
			e.printStackTrace();
			password = false;
		}
		return password;
	}

	/**
	 * This method allows the user to change the password of a TrustStore
	 * 
	 * @param TrustStoreName
	 * @param OldTrustStorePassword
	 * @param NewTrustStorePassword
	 */
	public void EditTrustStore(String TrustStoreName, String OldTrustStorePassword,
			String NewTrustStorePassword) {

		Process process1 = null;
		Process process2 = null;

		/*
		 * ---------------------------------------------------------------------
		 * ---------------- Execute the command line to update -----------------
		 * -------------------------- the Key password -------------------------
		 * 
		 * keytool -keypasswd -alias {alias} -keypass {key_password} -new
		 * {new_password} -keystore {truststore_name} -storepass {old_password}
		 */

		try {
			process1 = runtime.exec("keytool -keypasswd -keypass " + OldTrustStorePassword + " -new "
					+ NewTrustStorePassword + " -keystore " + TrustStoreName + " -storepass " + OldTrustStorePassword);

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// ---- Wait until the process1 terminate -----------------------------
		try {
			process1.waitFor();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}

		/*
		 * --------------------------------------------------------------------
		 * ------------- Execute the command line to update -------------------
		 * --------------------- truststore password --------------------------
		 * 
		 * keytool -storepasswd -new {new_password} -keystore {truststore_name}
		 * -storepass {old_password}
		 */

		try {
			process2 = runtime.exec("keytool -storepasswd -new " + NewTrustStorePassword + " -keystore "
					+ TrustStoreName + " -storepass " + OldTrustStorePassword);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// ---- Wait until the process2 terminate -----------------------------
		try {
			process2.waitFor();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * This method allows the user to add certificate to a TrustStore
	 * 
	 * @param TrustStoreName
	 * @param TrustStorePassword
	 * @param CertificateToAdd
	 * @param CertificateAlias
	 */
	public void AddCertificateToTrustStore(String TrustStoreName, String TrustStorePassword,
			String CertificateToAdd, String CertificateAlias) {

		try {
			// ----- Create a FileInputStream --------------------------------
			trustStoreFile = new File(TrustStoreName);
			fileInputStream = new FileInputStream(TrustStoreName);
			// ----- Specify the TrustStore file type ------------------------
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			// ----- Load the TrustStore -------------------------------------
			trustStore.load(fileInputStream, TrustStorePassword.toCharArray());
			// ----- Specify the certificate type ----------------------------
			certificateFactory = CertificateFactory.getInstance("X.509");
			// ----- Get the trusted certificate file ------------------------
			certInputStream = Stream(CertificateToAdd);
			certificate = certificateFactory.generateCertificate(certInputStream);
			// ----- Close the FileInputStream -------------------------------
			fileInputStream.close();
			// ----- Assign the trusted certificate to the given alias -------
			trustStore.setCertificateEntry(CertificateAlias, certificate);
			// ----- Create a FileOutputStream -------------------------------
			fileOutputStream = new FileOutputStream(trustStoreFile);
			// ----- Store the TrustStore ------------------------------------
			trustStore.store(fileOutputStream, TrustStorePassword.toCharArray());
			// ----- Close the FileOutputStream ------------------------------
			fileOutputStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			JOptionPane.showMessageDialog(frame, "Please choose a certificate file!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, e.getMessage() + " !");
		}
	}

	/**
	 * This method allows the user to delete certificate from TrustStore
	 * 
	 * @param TrustStoreName
	 * @param TrustStorePassword
	 * @param CertificateAliasToDelete
	 */
	public void DeleteCertificateFromTrustStore(String TrustStoreName, String TrustStorePassword,String CertificateAliasToDelete) {

		try {
			// --- Creates a FileInputStream from the TrustStore -----------
			trustStoreFile = new File(TrustStoreName);
			fileInputStream = new FileInputStream(trustStoreFile);
			// --- Loads the TrustStore from the given InputStream ---------
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(fileInputStream, TrustStorePassword.toCharArray());
			// ---- Delete the certificate from the TrustStore -------------
			trustStore.deleteEntry(CertificateAliasToDelete);
			// ----- Create a FileOutputStream -----------------------------
			fileOutputStream = new FileOutputStream(trustStoreFile);
			// ----- Store the TrustStore ----------------------------------
			trustStore.store(fileOutputStream, TrustStorePassword.toCharArray());
			// ----- Close the FileOutputStream ----------------------------
			fileOutputStream.close();

		} catch (java.security.cert.CertificateException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, e.getMessage() + " !");
		} finally {
			if (null != fileInputStream)
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, e.getMessage() + " !");
				}
		}
	}

	/**
	 * This method returns a ByteArrayInputStream that contains an internal
	 * buffer of bytes that may be read from the stream
	 */
	@SuppressWarnings("resource")
	private InputStream Stream(String fileName) throws IOException {
		// ---- Create a FileInputStream ------------------------------
		FileInputStream fileInputStream = new FileInputStream(fileName);
		// ---- Create a DataInputStream ------------------------------
		DataInputStream dataInputStream = new DataInputStream(fileInputStream);
		// ---- Put the dataInputStream in Byte[] ---------------------
		byte[] bytes = new byte[dataInputStream.available()];
		// ---- Read the bytes ----------------------------------------
		dataInputStream.readFully(bytes);
		// ---- Create a ByteArrayInputStream -------------------------
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

		return byteArrayInputStream;
	}
	/**
	 * This method returns Certificate's alias for the TrustStore.
	 */
	public DefaultListModel<String> CertificatesAliaslist(String TrustStoreName, String TrustStorePassword) {
		// ----- Create model for the jListCertificatesAlias -------
		certificatesAliasModel = new DefaultListModel<String>();
		try {
			// --- Creates a FileInputStream from the TrustStore ---
			File file = new File(TrustStoreName);
			fileInputStream = new FileInputStream(file);
			// --- Loads the KeyStore from the given InputStream ---
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(fileInputStream, TrustStorePassword.toCharArray());
			
			// --- Get All TrustStore's Certificates Alias ---------
			Enumeration<String> enumeration = trustStore.aliases();
			while (enumeration.hasMoreElements()) {
				String alias = (String) enumeration.nextElement();
				certificatesAliasModel.addElement(alias);
			}
		} catch (java.security.cert.CertificateException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, e.getMessage() + " !");
		} finally {
			if (null != fileInputStream)
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, e.getMessage() + " !");
				}
		}
		return certificatesAliasModel;
	}
}
