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

import java.awt.Dialog;
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
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

/**
 * This class allows the user to manage a TrustStore.
 * 
 * @author Mohamed Amine JEDIDI <mohamedamine_jedidi@outlook.com>
 * @version 1.0
 * @since 05-04-2016
 */
public class TrustStoreController {
	 private DefaultTableModel tableModel;
	
	 /**
	 * This Initializes the TrustStoreController.
	 */
	public TrustStoreController() {

	}

	/**
	 * This method allows the user to create a TrustStore and protect its
	 * integrity with a password.
	 *
	 * @param trustStoreName the TrustStore name
	 * @param trustStorePassword the TrustStore password
	 * @param trustStorePath the TrustStore path
	 * @param ownerDialog the owner dialog
	 */
	public void createTrustStore(String trustStoreName, String trustStorePassword, String trustStorePath, Dialog ownerDialog) {

		try {
			// ----- Specify the TrustStore file type -------------------------
			KeyStore emptyTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			// ----- Create an empty TrustStore file --------------------------
			emptyTrustStore.load(null, trustStorePassword.toCharArray());
			// ----- Create a FileOutputStream --------------------------------
			FileOutputStream fos = new FileOutputStream(trustStorePath);
			// ----- Store the empty TrustStore -------------------------------
			emptyTrustStore.store(fos, trustStorePassword.toCharArray());
			// ----- Close the FileOutputStream -------------------------------
			fos.close();
			
		} catch (FileNotFoundException | KeyStoreException | NoSuchAlgorithmException | CertificateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
			if (ownerDialog!=null) {
				JOptionPane.showMessageDialog(ownerDialog, e1.getMessage() + " !");	
			}
		}
	}

	/**
	 * This method open a TrustStore. It returns true if the password is correct.
	 *
	 * @param trustStoreName the TrustStore name
	 * @param trustStorePassword the TrustStore password
	 * @param ownerDialog the owner dialog
	 * @return true, if successful
	 */
	public boolean openTrustStore(String trustStoreName, String trustStorePassword, Dialog ownerDialog) {
		FileInputStream fileInputStream =null;
		boolean password = true;
		// --- Creates a FileInputStream from the TrustStore -------------------
		File file = new File(trustStoreName);
		KeyStore trustStore = null;
		try {
			fileInputStream = new FileInputStream(file);
			// --- Loads the TrustStore from the given InputStream -------------
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(fileInputStream, trustStorePassword.toCharArray());
		} catch (FileNotFoundException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// --- Open a warning Message dialog if the password is incorrect --
			if (ownerDialog!=null) {
				JOptionPane.showMessageDialog(ownerDialog, e.getMessage() + " !");	
			}
			password = false;
		}
		return password;
	}

	/**
	 * This method allows the user to change the password of a TrustStore.
	 *
	 * @param trustStoreName the TrustStore name
	 * @param oldTrustStorePassword the old TrustStore password
	 * @param newTrustStorePassword the new TrustStore password
	 */
	public void editTrustStore(String trustStoreName, String oldTrustStorePassword, String newTrustStorePassword) {
		String os = System.getProperty("os.name");
		Process process1 = null;
		Process process2 = null;

		/*
		 * ---------------------------------------------------------------------
		 * ---------------- Execute the command line to update -----------------
		 * -------------------------- the Key password -------------------------
		 * 
		 * keytool -keypasswd -alias {alias} -keypass {oldTrustStorePassword} 
		 * -new {newTrustStorePassword} -keystore {trustStoreName} 
		 * -storepass {oldTrustStorePassword}
		 */

		try {
			if (os.toLowerCase().contains("windows") == true) {
				process1 = Runtime.getRuntime().exec("keytool -keypasswd -keypass " + oldTrustStorePassword + " -new " + newTrustStorePassword + " -keystore " + "\"" + trustStoreName + "\"" + " -storepass " + oldTrustStorePassword);
			} else if (os.toLowerCase().contains("linux") == true) {
				String[] command = {"/bin/sh", "-c","keytool -keypasswd -keypass " + oldTrustStorePassword + " -new " + newTrustStorePassword + " -keystore " + "\"" + trustStoreName + "\"" + " -storepass " + oldTrustStorePassword};
				process1 = Runtime.getRuntime().exec(command);
			}
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
		 * keytool -storepasswd -new {newTrustStorePassword} -keystore 
		 * {trustStoreName} -storepass {oldTrustStorePassword}
		 */

		try {
			if (os.toLowerCase().contains("windows") == true) {
				process2 = Runtime.getRuntime().exec("keytool -storepasswd -new " + newTrustStorePassword + " -keystore " + "\"" + trustStoreName + "\"" + " -storepass " + oldTrustStorePassword);
			} else if (os.toLowerCase().contains("linux") == true) {
				String[] command = {"/bin/sh", "-c","keytool -storepasswd -new " + newTrustStorePassword + " -keystore " + "\"" + trustStoreName + "\"" + " -storepass " + oldTrustStorePassword};
				process2 = Runtime.getRuntime().exec(command);
			}
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
	 * This method allows the user to add certificate to a TrustStore.
	 *
	 * @param trustStoreName the TrustStore name
	 * @param trustStorePassword the TrustStore password
	 * @param certificateToAdd the certificate to add
	 * @param certificateAlias the certificate alias
	 * @param ownerDialog the owner dialog
	 */
	public void addCertificateToTrustStore(String trustStoreName, String trustStorePassword, String certificateToAdd, String certificateAlias, Dialog ownerDialog) {
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		InputStream certInputStream = null;
		File trustStoreFile = null;
		KeyStore trustStore = null;
		CertificateFactory certificateFactory = null;
		Certificate certificate = null;
		try {
			// ----- Create a FileInputStream --------------------------------
			trustStoreFile = new File(trustStoreName);
			fileInputStream = new FileInputStream(trustStoreName);
			// ----- Specify the TrustStore file type ------------------------
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			// ----- Load the TrustStore -------------------------------------
			trustStore.load(fileInputStream, trustStorePassword.toCharArray());
			// ----- Specify the certificate type ----------------------------
			certificateFactory = CertificateFactory.getInstance("X.509");
			// ----- Get the trusted certificate file ------------------------
			certInputStream = stream(certificateToAdd);
			certificate = certificateFactory.generateCertificate(certInputStream);
			// ----- Close the FileInputStream -------------------------------
			fileInputStream.close();
			// ----- Assign the trusted certificate to the given alias -------
			trustStore.setCertificateEntry(certificateAlias, certificate);
			// ----- Create a FileOutputStream -------------------------------
			fileOutputStream = new FileOutputStream(trustStoreFile);
			// ----- Store the TrustStore ------------------------------------
			trustStore.store(fileOutputStream, trustStorePassword.toCharArray());
			// ----- Close the FileOutputStream ------------------------------
			fileOutputStream.close();

		} catch (FileNotFoundException | KeyStoreException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			if (ownerDialog!=null) {
				JOptionPane.showMessageDialog(ownerDialog, "Please choose a certificate file!");	
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			if (ownerDialog!=null) {
				JOptionPane.showMessageDialog(ownerDialog, e.getMessage() + " !");	
			}
		}
	}

	/**
	 * This method allows the user to delete certificate from TrustStore.
	 *
	 * @param trustStoreName the TrustStore name
	 * @param trustStorePassword the TrustStore password
	 * @param certificateAliasToDelete the certificate alias to delete
	 * @param ownerDialog the owner dialog
	 */
	public void deleteCertificateFromTrustStore(String trustStoreName, String trustStorePassword,String certificateAliasToDelete, Dialog ownerDialog) {
		FileInputStream fileInputStream =null;
		FileOutputStream fileOutputStream = null;
		File trustStoreFile = null;
		try {
			// --- Creates a FileInputStream from the TrustStore -----------
			trustStoreFile = new File(trustStoreName);
			fileInputStream = new FileInputStream(trustStoreFile);
			// --- Loads the TrustStore from the given InputStream ---------
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(fileInputStream, trustStorePassword.toCharArray());
			// ---- Delete the certificate from the TrustStore -------------
			trustStore.deleteEntry(certificateAliasToDelete);
			// ----- Create a FileOutputStream -----------------------------
			fileOutputStream = new FileOutputStream(trustStoreFile);
			// ----- Store the TrustStore ----------------------------------
			trustStore.store(fileOutputStream, trustStorePassword.toCharArray());
			// ----- Close the FileOutputStream ----------------------------
			fileOutputStream.close();

		} catch (java.security.cert.CertificateException | NoSuchAlgorithmException | FileNotFoundException | KeyStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			if (ownerDialog!=null) {
				JOptionPane.showMessageDialog(ownerDialog, e.getMessage() + " !");	
			}
		} finally {
			if (null != fileInputStream)
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					if (ownerDialog!=null) {
						JOptionPane.showMessageDialog(ownerDialog, e.getMessage() + " !");	
					}
				}
		}
	}

	/**
	 * This method returns a ByteArrayInputStream that contains an internal
	 * buffer of bytes that may be read from the stream
	 */
	@SuppressWarnings("resource")
	private InputStream stream(String fileName) throws IOException {
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
	 * Gets the tableModel.
	 * @return the tableModel
	 */
	public DefaultTableModel getTableModel() {
		if (tableModel == null) {
			// ---- Create header for the TableModel -----------------
			Vector<String> header = new Vector<String>();
			header.add("Certificate alias");
			header.add("Certificate owner");
			header.add("Expiration date");
			
			tableModel = new DefaultTableModel(null,header) {
				private static final long serialVersionUID = 1L;
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		}
		return tableModel;
	}
	
	/**
	 * Adds row to the TableModel
	 *
	 * @param certificateSettings the CertificateSettings
	 */
	public void addTableModelRow(CertificateSettings certificateSettings) {
		if(certificateSettings == null){
		   certificateSettings = new CertificateSettings();
		}
		Vector<Object> row = new Vector<Object>();
		row.add(certificateSettings.getCertificateAlias());
		row.add(certificateSettings.getKeyStoreSettings().getFullName());
		row.add(certificateSettings.getValidity());
		getTableModel().addRow(row); 
	}
	
	/**
	 * Clears the table model.
	 */
	public void clearTableModel() {
		while (getTableModel().getRowCount()>0) {
			getTableModel().removeRow(0);
		}
	}
	
	/**
	 * Get TrustedCertificates list .
	 *
	 * @param trustStoreName the TrustStore name
	 * @param trustStorePassword the TrustStore password
	 */
	public void getTrustedCertificatesList(String trustStoreName, String trustStorePassword) {
		CertificateSettings certificateSettings = null;
		FileInputStream fileInputStream = null;
		try {
			// --- Creates a FileInputStream from the TrustStore -----
			File file = new File(trustStoreName);
			fileInputStream = new FileInputStream(file);
			// --- Loads the TrustStore from the given InputStream ---
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(fileInputStream, trustStorePassword.toCharArray());
			certificateSettings = new CertificateSettings();
			// --- Get All TrustStore's Certificates Alias -----------
			Enumeration<String> enumeration = trustStore.aliases();
			while (enumeration.hasMoreElements()) {
				String alias = enumeration.nextElement();
				Certificate cert = trustStore.getCertificate(alias);
				certificateSettings = getCertificateSettings(cert);
				certificateSettings.setCertificateAlias(alias);
				
				addTableModelRow(certificateSettings);
			}
		} catch (java.security.cert.CertificateException | NoSuchAlgorithmException | FileNotFoundException | KeyStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fileInputStream)
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * Gets the certificate settings.
	 *
	 * @param alias the certificate alias
	 * @param trustStoreName the TrustStore name
	 * @param trustStorePassword TrustStore password
	 * @return the certificate settings
	 */
	public CertificateSettings getCertificateSettings(String alias, String trustStoreName, String trustStorePassword){
		CertificateSettings certificateSettings = new CertificateSettings();
		FileInputStream fileInputStream = null;

		try {
			// --- Creates a FileInputStream from the TrustStore -----
			File file = new File(trustStoreName);
			fileInputStream = new FileInputStream(file);
			// --- Loads the TrustStore from the given InputStream ---
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(fileInputStream, trustStorePassword.toCharArray());
			
			Certificate cert = trustStore.getCertificate(alias);
			certificateSettings = getCertificateSettings(cert);
			
		} catch (java.security.cert.CertificateException | NoSuchAlgorithmException | FileNotFoundException | KeyStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fileInputStream)
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return certificateSettings;
	}
	
	public CertificateSettings getCertificateSettings(Certificate cert){
		CertificateSettings certificateSettings = new CertificateSettings();
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

		
		String provider = ( (X509Certificate) cert).getSubjectX500Principal().getName();
		LdapName ldapDN;
		try {
			ldapDN = new LdapName(provider);
			HashMap<String,String> providerParts = new HashMap<String,String>();
			for(Rdn rdn: ldapDN.getRdns()) {
				providerParts.put(rdn.getType(), (String) rdn.getValue());
			}				

	        certificateSettings.getKeyStoreSettings().setFullName(providerParts.get("CN"));
	        certificateSettings.getKeyStoreSettings().setOrganizationalUnit(providerParts.get("OU"));
	        certificateSettings.getKeyStoreSettings().setOrganization(providerParts.get("O"));
	        certificateSettings.getKeyStoreSettings().setCityOrLocality(providerParts.get("L"));
	        certificateSettings.getKeyStoreSettings().setStateOrProvince(providerParts.get("ST"));
	        certificateSettings.getKeyStoreSettings().setCountryCode(providerParts.get("C"));
		} catch (InvalidNameException e) {
			e.printStackTrace();
		}
      
        Date date = ((X509Certificate) cert).getNotAfter();
		certificateSettings.setValidity(DATE_FORMAT.format(date));
		
		return certificateSettings;
	}

}
