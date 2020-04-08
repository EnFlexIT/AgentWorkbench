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

import java.awt.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.Enumeration;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

/**
 * This class allows to manage a Java KeyStore.
 * 
 * @author Mohamed Amine JEDIDI (mohamedamine_jedidi@outlook.com)
 * @version 1.0
 * @since 05-04-2016
 */
public class KeyStoreController extends TrustStoreController {

	private static final int keysize = 1024;
	private static final String ALGO_RSA = "RSA";

	/**
	 * This Initializes the KeyStoreController.
	 * @param ownerWindow the owner window
	 */
	public KeyStoreController(Window ownerWindow) {
		super(ownerWindow);
	}

	/**
	 * This method allows the user to create a KeyStore and protect its
	 * integrity with a password.
	 *
	 * @param certificateProperties the certificate properties
	 * @param keyStoreFile the key store file
	 * @param keystorePassword the keystore password
	 * @param validity the validity
	 */
	public void createKeyStore(CertificateProperties certificateProperties, File keyStoreFile, String keystorePassword, String validity) {
		
		try {

			KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGO_RSA);
			kpg.initialize(keysize);

			KeyPair kp = kpg.genKeyPair();
			KeyFactory fact = KeyFactory.getInstance(ALGO_RSA);

			try {
				
				X509Certificate[] chain = new X509Certificate[1];
				RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);

				this.init(keyStoreFile, keystorePassword);

				ContentSigner sigGen = new JcaContentSignerBuilder("SHA1withRSA").setProvider(new BouncyCastleProvider()).build(kp.getPrivate());
				SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(new RSAKeyParameters(false, pub.getModulus(), pub.getPublicExponent()));

				Date startDate = new Date();
				Date endDate = new Date(System.currentTimeMillis() + Long.parseLong(validity) * 24 * 60 * 60 * 1000);

				X500NameBuilder namebuilder = new X500NameBuilder(X500Name.getDefaultStyle());
				namebuilder.addRDN(BCStyle.CN, certificateProperties.getCommonName());
				namebuilder.addRDN(BCStyle.OU, certificateProperties.getOrganizationalUnit());
				namebuilder.addRDN(BCStyle.O, certificateProperties.getOrganization());
				namebuilder.addRDN(BCStyle.L, certificateProperties.getCityOrLocality());
				namebuilder.addRDN(BCStyle.ST, certificateProperties.getStateOrProvince());
				namebuilder.addRDN(BCStyle.C, certificateProperties.getCountryCode());

				X500Name x500Name = namebuilder.build();
				X509v1CertificateBuilder v1CertGen = new X509v1CertificateBuilder(x500Name, BigInteger.ONE, startDate, endDate, x500Name, subPubKeyInfo);
				X509CertificateHolder certHolder = v1CertGen.build(sigGen);

				JcaX509CertificateConverter converter = new JcaX509CertificateConverter().setProvider(new BouncyCastleProvider());
				converter.getCertificate(certHolder);

				chain[0] = converter.getCertificate(certHolder);

				this.trustStore.setKeyEntry(certificateProperties.getAlias(), kp.getPrivate(), keystorePassword.toCharArray(), chain);
				this.saveTrustStore();

			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (OperatorCreationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CertificateException e) {
				e.printStackTrace();
			} catch (KeyStoreException e) {
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
	 * This method allows the user to export certificate from KeyStore. The name of the file will be alias + ".cer"
	 *
	 * @param alias the alias
	 * @param certificatePath the certificate path
	 */
	public void exportCertificate(String alias, String certificatePath){
		exportCertificate(alias, certificatePath, false);
	}

	/**
	 * This method allows the user to export certificate from KeyStore.
	 *
	 * @param alias the alias
	 * @param certificatePath the certificate path
	 * @param fullPath if true, use the certificatePath as full file name+path, so also containing the filename
	 */
	public void exportCertificate(String alias, String certificatePath, boolean fullPath) {
		X509Certificate cert = getCertificate(alias);

		try {
			if(!fullPath){
				certificatePath = certificatePath + alias + ".crt";
			}

			File certFile = new File(certificatePath);
			FileOutputStream os = new FileOutputStream(certFile);
			os.write(cert.getEncoded());
			os.flush();
			os.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CertificateEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
