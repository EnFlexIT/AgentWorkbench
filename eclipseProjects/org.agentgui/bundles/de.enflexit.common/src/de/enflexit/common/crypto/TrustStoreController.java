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
package de.enflexit.common.crypto;

//import java.awt.Dialog;
import java.awt.Window;
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
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import de.enflexit.common.Language;

/**
 * This class allows the user to manage a TrustStore.
 * 
 * @author Mohamed Amine JEDIDI <mohamedamine_jedidi@outlook.com>
 * @version 1.0
 * @since 05-04-2016
 */
public class TrustStoreController {
	
	private Window ownerWindow;

	private boolean initialized = false;

	protected KeyStore trustStore;
	private String trustStorePassword;

	private DefaultTableModel tableModel;
	private File trustStoreFile;
	private InputStream trustStoreInputStream = null;
	private FileOutputStream trustStoreOutputStream;
	


	/**
	 * generates a blank controller, which still needs to initialize the trustStore
	 */
	public TrustStoreController(Window ownerDialog){
		this(ownerDialog, null, null, false);
	}
	/**
	 * This Initializes the TrustStoreController.
	 */
	public TrustStoreController(Window ownerWindow, File trustStoreFile, String trustStorePassword, boolean edit) {
		this.ownerWindow = ownerWindow;
		try {
			this.trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			if (trustStoreFile != null && trustStorePassword != null) {
				if(edit){
					openTrustStore(trustStoreFile, trustStorePassword);
				} else {
					createTrustStore(trustStoreFile, trustStorePassword);
				}
			}
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the owner window.
	 * @param ownerWindow the new owner window
	 */
	public void setOwnerWindow(Window ownerWindow) {
		this.ownerWindow = ownerWindow;
	}
	
	/**
	 * returns the key store object for direct interaction
	 * @return the key store object
	 */
	public KeyStore getKeyStore(){
		return trustStore;
	}
	/**
	 * Gets the trust store password.
	 * @return the trust store password
	 */
	public String getTrustStorePassword() {
		return trustStorePassword;
	}
	/**
	 * Sets the trust store password.
	 * @param trustStorePassword the new trust store password
	 */
	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}
	
	/**
	 * This method allows the user to create a TrustStore and protect its
	 * integrity with a password.
	 *
	 * @param trustStoreFile the TrustStore file
	 * @param trustStorePassword the TrustStore password
	 */
	public void createTrustStore(File trustStoreFile, String trustStorePassword) {
		this.init(trustStoreFile, trustStorePassword);
		this.openTrustStore(trustStoreFile, trustStorePassword);
		this.saveTrustStore();
	}

	/**
	 * Initializes the truststore controller.
	 * @param trustStoreFile the trust store file
	 * @param trustStorePassword the trust store password
	 */
	public void init(File trustStoreFile, String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
		this.trustStoreFile = trustStoreFile;
		try {
			trustStore.load(null, trustStorePassword.toCharArray());
			initialized = true;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method open a TrustStore. It returns true if the password is correct.
	 *
	 * @param trustStoreFile the TrustStore name
	 * @param trustStorePassword the TrustStore password
	 * @return true, if successful
	 */
	public boolean openTrustStore(File trustStoreFile, String trustStorePassword) {
		if (trustStoreFile!=null && trustStoreFile.exists()) {
			try {
				trustStoreInputStream = new FileInputStream(trustStoreFile);
				init(trustStoreFile, trustStorePassword);
				return this.openTrustStoreFromStream(trustStoreInputStream, trustStorePassword);
				
			} catch (FileNotFoundException fnfEx) {
				//System.err.println("[" + this.getClass().getSimpleName() + "] " + fnfEx.getLocalizedMessage());
				fnfEx.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * This method opens a TrustStore directly from stream. It returns true if the password is correct. It will NOT initialize this controller, because the file for write interaction is missing
	 *
	 * @param trustStoreInputStream the TrustStore name
	 * @param trustStorePassword the TrustStore password
	 * @return true, if successful
	 */
	public boolean openTrustStoreFromStream(InputStream trustStoreInputStream, String trustStorePassword) {
		
		boolean successful = false;
		this.trustStoreInputStream = trustStoreInputStream;
		try {
			// --- Loads the TrustStore from the given InputStream -------------
			trustStore.load(trustStoreInputStream, trustStorePassword.toCharArray());
			successful = true;
		
		} catch (FileNotFoundException | NoSuchAlgorithmException | CertificateException e) {
			e.printStackTrace();
		} catch (IOException ioEx) {
			// --- Open a warning Message dialog if the password is incorrect --
			if (ownerWindow!=null) {
				JOptionPane.showMessageDialog(this.ownerWindow, ioEx.getMessage() + "!", "Error reading key or trust store file!", JOptionPane.ERROR_MESSAGE);
			} else {
				ioEx.printStackTrace();
			}
		}
		return successful;
	}

	/**
	 * Save the trust store.
	 */
	public void saveTrustStore() {
		saveTrustStore(this.trustStorePassword);
	}

	/**
	 * Save the trust store.
	 * @param trustStorePassword the trust store password
	 */
	public void saveTrustStore(String trustStorePassword) {
		try {
			// ----- Create a FileOutputStream --------------------------------
			trustStoreOutputStream = new FileOutputStream(trustStoreFile);
			// ----- Store the TrustStore -------------------------------
			trustStore.store(trustStoreOutputStream, trustStorePassword.toCharArray());
			// ----- Close the FileOutputStream -------------------------------
			trustStoreOutputStream.close();
		} catch (FileNotFoundException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
			if (ownerWindow != null) {
				JOptionPane.showMessageDialog(ownerWindow, e1.getMessage() + "!");
			}
		}
	}

	/**
	 * This method allows the user to change the password of a TrustStore.
	 *
	 * @param newTrustStorePassword the new TrustStore password
	 */
	public void changeTrustStorePassword(String newTrustStorePassword) {
		saveTrustStore(newTrustStorePassword);
	}

	
	/**
	 * Change a certificate alias.
	 * @param oldAlias the old alias
	 * @param newAlias the new alias
	 */
	public void changeAlias(String oldAlias, String newAlias) {
		
		System.out.println("change alias from " + oldAlias + " to " + newAlias);
		try {
			if (trustStore.isCertificateEntry(oldAlias)) {
				System.out.println("isCertificateEntry");
			}
			Certificate cert = trustStore.getCertificate(oldAlias);
			trustStore.deleteEntry(oldAlias);
			trustStore.setCertificateEntry(newAlias, cert);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a certificate to the truststore
	 * @param certificate The certificate
	 * @param alias The certificate alias
	 * @throws KeyStoreException Error adding the certificate
	 */
	public void addCertificate(X509Certificate certificate, String alias) throws KeyStoreException{
		this.trustStore.setCertificateEntry(alias, certificate);
	}

	/**
	 * Loads a certificate from a file, adds it to the truststore and saves the truststore
	 * @param certificateToAdd The path to the certificate file
	 * @param certificateAlias the certificate alias The certificate alias
	 */
	public void addCertificateToTrustStore(String certificateToAdd, String certificateAlias) {
		InputStream certInputStream = null;
		CertificateFactory certificateFactory = null;
		Certificate certificate = null;
		try {
			// ----- Specify the certificate type ----------------------------
			certificateFactory = CertificateFactory.getInstance("X.509");
			// ----- Get the trusted certificate file ------------------------
			certInputStream = stream(certificateToAdd);
			certificate = certificateFactory.generateCertificate(certInputStream);
			// ----- Assign the trusted certificate to the given alias -------
			trustStore.setCertificateEntry(certificateAlias, certificate);
			// ----- Create a FileOutputStream -------------------------------
			trustStoreOutputStream = new FileOutputStream(trustStoreFile);
			// ----- Store the TrustStore ------------------------------------
			trustStore.store(trustStoreOutputStream, trustStorePassword.toCharArray());
			// ----- Close the FileOutputStream ------------------------------
			trustStoreOutputStream.close();

		} catch (FileNotFoundException | KeyStoreException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			if (ownerWindow != null) {
				JOptionPane.showMessageDialog(ownerWindow, "Please choose a certificate file!");
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			if (ownerWindow != null) {
				JOptionPane.showMessageDialog(ownerWindow, e.getMessage() + " !");
			}
		}
	}

	/**
	 * This method allows the user to delete certificate from TrustStore.
	 * @param certificateAliasToDelete the certificate alias to delete
	 */
	public void deleteCertificateFromTrustStore(String certificateAliasToDelete) {
		try {
			// ---- Delete the certificate from the TrustStore -------------
			trustStore.deleteEntry(certificateAliasToDelete);
			saveTrustStore();

		} catch (KeyStoreException e) {
			e.printStackTrace();
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
			header.add(Language.translate("Zertifikats-Alias"));
			header.add(Language.translate("Zertifikatsbesitzer"));
			header.add(Language.translate("Ablaufdatum"));

			tableModel = new DefaultTableModel(null, header) {
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
	 * @param certificateProperties the CertificateProperties
	 */
	public void addTableModelRow(CertificateProperties certificateProperties) {
		if (certificateProperties == null) {
			certificateProperties = new CertificateProperties();
		}
		Vector<Object> row = new Vector<Object>();
		row.add(certificateProperties.getAlias());
		row.add(certificateProperties.getCommonName());
		row.add(certificateProperties.getValidity());
		getTableModel().addRow(row);
	}

	/**
	 * Clears the table model.
	 */
	public void clearTableModel() {
		while (getTableModel().getRowCount() > 0) {
			getTableModel().removeRow(0);
		}
	}

	/**
	 * Get TrustedCertificates list .
	 * @return the trusted certificates list
	 */
	public Enumeration<String> getTrustedCertificatesList() {
		CertificateProperties certificateProperties = null;
		try {
			// --- Creates a FileInputStream from the TrustStore -----
			certificateProperties = new CertificateProperties();
			// --- Get All TrustStore's Certificates Alias -----------
			Enumeration<String> enumeration = trustStore.aliases();
			while (enumeration.hasMoreElements()) {
				String alias = enumeration.nextElement();
				Certificate cert = trustStore.getCertificate(alias);
				certificateProperties.parseFromCertificate((X509Certificate) cert);
				certificateProperties.setAlias(alias);

				addTableModelRow(certificateProperties);
			}
			return trustStore.aliases();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the first certificate properties.
	 * @return the first certificate properties
	 */
	public CertificateProperties getFirstCertificateProperties() {
		Enumeration<String> keyStoreContents = getTrustedCertificatesList();
		CertificateProperties certificateProperties = null;
		if (keyStoreContents.hasMoreElements()) {
			certificateProperties = getCertificateProperties(keyStoreContents.nextElement());
		}
		return certificateProperties;
	}

	/**
	 * Gets the certificate properties.
	 * @param alias the certificate alias
	 * @return the certificate properties
	 */
	public CertificateProperties getCertificateProperties(String alias) {
		CertificateProperties certificateProperties = new CertificateProperties();

		Certificate cert = getCertificate(alias);
		certificateProperties.parseFromCertificate((X509Certificate) cert);
		certificateProperties.setAlias(alias);
		return certificateProperties;
	}

	/**
	 * Gets the certificate with the specified alias.
	 * @param alias the alias
	 * @return the certificate
	 */
	public X509Certificate getCertificate(String alias) {
		Certificate cert = null;
		try {
			cert = trustStore.getCertificate(alias);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return (X509Certificate) cert;
	}
	
	/**
	 * Checks if is initialized.
	 * @return true, if is initialized
	 */
	public boolean isInitialized(){
		return initialized ;
	}
	
}
