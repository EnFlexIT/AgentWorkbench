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
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.Enumeration;

import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.util.DerValue;
import sun.security.x509.CertificateExtensions;
import sun.security.x509.Extension;
import sun.security.x509.KeyUsageExtension;
import sun.security.x509.SubjectKeyIdentifierExtension;
import sun.security.x509.X500Name;

/**
 * This class allows the user to manage a KeyStore.
 * 
 * @author Mohamed Amine JEDIDI <mohamedamine_jedidi@outlook.com>
 * @version 1.0
 * @since 05-04-2016
 */
public class KeyStoreController extends TrustStoreController {

	private static final int keysize = 1024;
	private static final String ALGO_RSA = "RSA";

	/**
	 * This Initializes the KeyStoreController.
	 */
	public KeyStoreController(Dialog ownerDialog) {
		super(ownerDialog);
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
	public void createKeyStore(CertificateProperties certificateProperties, String keyStoreName, String keystorePassword, String keyStorePath, String validity) {
		KeyPairGenerator kpg;
		String filename;
		try {
			kpg = KeyPairGenerator.getInstance(ALGO_RSA);
			kpg.initialize(1024);

			KeyPair kp = kpg.genKeyPair();
//			kp.getPrivate().

			KeyFactory fact = KeyFactory.getInstance(ALGO_RSA);

			try {
				RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
				pub.getModulus();
				pub.getPublicExponent();

				RSAPrivateKeySpec priv;
				priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);
				priv.getModulus();
				priv.getPrivateExponent();

				filename = keyStorePath + keyStoreName + HttpsConfigWindow.KEYSTORE_FILENAME;

				init(filename, keystorePassword);

				CertAndKeyGen keypair = new CertAndKeyGen(ALGO_RSA, "SHA1WithRSA", null);

				X500Name x500Name = new X500Name(certificateProperties.getCommonName(), certificateProperties.getOrganizationalUnit(), certificateProperties.getOrganization(), certificateProperties.getCityOrLocality(), certificateProperties.getStateOrProvince(), certificateProperties.getCountryCode());

				keypair.generate(keysize);
				PrivateKey privKey = keypair.getPrivateKey();

				X509Certificate[] chain = new X509Certificate[1];
				
			    CertificateExtensions extensions = new CertificateExtensions();

			    // Example extension.
			    // See KeyTool source for more.
			    /*
			    boolean[] keyUsagePolicies = new boolean[9];
			    keyUsagePolicies[0] = true; // Digital Signature
			    keyUsagePolicies[2] = true; // Key encipherment
			    KeyUsageExtension kue = new KeyUsageExtension(keyUsagePolicies);*/
			    SubjectKeyIdentifierExtension skie = new SubjectKeyIdentifierExtension(keypair.getPublicKey().getEncodedInternal());
			    
			    byte[] keyUsageValue = new DerValue(DerValue.tag_OctetString, skie.getExtensionValue()).toByteArray();
			    extensions.set(KeyUsageExtension.NAME, new Extension(
			    		skie.getExtensionId(),
			            false, // Critical
			            keyUsageValue));

//				chain[0] = keypair.getSelfCertificate(x500Name, new Date(), Long.parseLong(validity) * 24 * 60 * 60, extensions);
				chain[0] = keypair.getSelfCertificate(x500Name, new Date(), Long.parseLong(validity) * 24 * 60 * 60);

				trustStore.setKeyEntry(certificateProperties.getAlias(), privKey, keystorePassword.toCharArray(), chain);

				saveTrustStore();

			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			} catch (KeyStoreException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (CertificateException e) {
				e.printStackTrace();
			} catch (SignatureException e) {
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * This method allows the user to change the password of the keystore
	 * and also change the password and alias of a key entry,
	 * which is the only entry in the store and has the same password as it,
	 * both before and after the change.
	 *
	 * @param oldKeyAlias the old alias
	 * @param newKeyAlias the new alias
	 * @param newPassword the new KeyStore password
	 * @param oldPassword the old KeyStore password
	 */
	public void editLonelyKeyEntry(String oldKeyAlias, String newKeyAlias, String newPassword, String oldPassword) {
		System.out.println("editLoneleyKeyEntry oldAlias=" + oldKeyAlias + ", newAlias=" + newKeyAlias + ", newKeyStorePassword=" + newPassword + ", oldKeyStorePassword=" + oldPassword);

		try {
			if (!trustStore.isKeyEntry(oldKeyAlias)) {
				System.err.println("this is not a key entry: " + oldKeyAlias);
				return;
			}
			if (trustStore.size() != 1) {
				System.err.println("this is not a lonely entry: " + oldKeyAlias);
				return;
			}
/* alternative method
			Key key = trustStore.getKey(oldKeyAlias, oldPassword.toCharArray());
			Certificate[] chain = trustStore.getCertificateChain(oldKeyAlias);
			System.out.println(key.getClass());
			System.out.println(key.getEncoded().toString());
			trustStore.setKeyEntry(newKeyAlias, key, newPassword.toCharArray(), chain);
*/

			Entry entry = trustStore.getEntry(oldKeyAlias, new PasswordProtection(oldPassword.toCharArray()));
			trustStore.deleteEntry(oldKeyAlias);
			trustStore.setEntry(newKeyAlias, entry, new PasswordProtection(newPassword.toCharArray()));

		} catch (KeyStoreException e1) {
			e1.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnrecoverableEntryException e) {
			e.printStackTrace();
		}

		saveTrustStore(newPassword);
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
	public void exportCertificate(String alias) {
		getCertificate(alias);
	}

	public void printAliasesList(String keyPasswd) {
		try {
			System.out.println("trustStoreType=" + trustStore.getType());
			System.out.println("size=" + trustStore.size());

			// --- Get All TrustStore's Certificates Alias -----------
			Enumeration<String> enumeration = trustStore.aliases();
			while (enumeration.hasMoreElements()) {
				String alias = enumeration.nextElement();
				System.out.println("alias=" + alias);
				// Entry entry = trustStore.getEntry(alias, null);

				Entry entry = trustStore.getEntry(alias, new PasswordProtection(keyPasswd.toCharArray()));

				System.out.println("entryClass=" + entry.getClass());
			}
		} catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
			e.printStackTrace();
		}
	}
}
