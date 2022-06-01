package de.enflexit.awb.ws.core;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.common.crypto.KeyStoreType;

/**
 * The Class SSLJettyConfiguration creates all necessary artifacts and entries
 * to enable a Jetty server to use HTTPS/SSL for its communication.
 * This includes a corresponding {@link KeyStore} and a inherent certificate.<br><br>
 * <b>The certificate created, only serves as an example and for test purposes (e.g. on localhost)!</b>
 * <br>
 * To proper operate Jetty using SSL/TLS, you may 
 * <ul>
 * 		<li>either use a tool such as <a href="https://keystore-explorer.org/">KeyStore Explorer</a> to add a suitable certificate to a KeyStore</li>
 * 		<li>or operate the AWB-WS Jetty behind a reverse proxy, like (see <a href="https://www.nginx.com/">NGINX</a>)</li>
 * </ul>
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SSLJettyConfiguration {

	private static final KeyStoreType DEFAULT_KEYSTORE_TYPE = KeyStoreType.PKCS12;
	private static final String DEFAULT_SSL_PROTOCOL = (String) JettyConstants.SSL_PROTOCOL.getDefaultValue();
	
	private static final String DEFAULT_DOMAIN  = "localhost";
	
		
	/**
	 * Creates default SSL settings for the specified .
	 *
	 * @param jettyConfig the {@link JettyConfiguration} to work on.
	 * @param password the password to be used for the KeyStore
	 */
	public static boolean createDefaultSettingsForSSL(JettyConfiguration jettyConfig, char[] password) {
		
		boolean success = false;
		try {
			File keyStoreFile = SSLJettyConfiguration.createKeyStore(jettyConfig.getServerName(), password);
			if (keyStoreFile!=null) {
				SSLJettyConfiguration.adjustJettyConfiguration(jettyConfig, keyStoreFile, password);
				success = true;
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}
	
	/**
	 * Returns the default key store file.
	 *
	 * @param serverName the server name
	 * @return the default key store file
	 */
	public static File getDefaultKeyStoreFile(String serverName) {
		return new File(BundleHelper.getPathProperties() + serverName + "." + DEFAULT_KEYSTORE_TYPE.getFileExtension());
	}
	
	/**
	 * Returns the key store file as relative path based on the AWB properties directory.
	 *
	 * @param keyStoreFile the key store file
	 * @return the default key store relative path
	 */
	public static String getKeyStoreRelativePath(File keyStoreFile) {
		return keyStoreFile.getAbsolutePath().substring(BundleHelper.getPathProperties().length());
	}
	/**
	 * Returns the key store file from relative path.
	 *
	 * @param keyStoreRelativePath the key store relative path
	 * @return the default key store file from relative path
	 */
	public static File getKeyStoreFileFromRelativePath(String keyStoreRelativePath) {
		return new File(BundleHelper.getPathProperties() + keyStoreRelativePath);
	}
	
	/**
	 * Will adjust the specified jetty configuration after the KeyStore was successfully created.
	 *
	 * @param jettyConfig the jetty configuration
	 * @param keyStoreFile the key store file
	 * @param password the password for key store and certificate
	 */
	private static void adjustJettyConfiguration(JettyConfiguration jettyConfig, File keyStoreFile, char[] password) {
		
		jettyConfig.get(JettyConstants.HTTPS_ENABLED).setValue(true);
		
		jettyConfig.get(JettyConstants.SSL_KEYSTORE).setValue(SSLJettyConfiguration.getKeyStoreRelativePath(keyStoreFile));
		jettyConfig.get(JettyConstants.SSL_KEYSTORETYPE).setValue(DEFAULT_KEYSTORE_TYPE.getType());
		
		jettyConfig.get(JettyConstants.SSL_PASSWORD).setValue(new String(password));
		jettyConfig.get(JettyConstants.SSL_KEYPASSWORD).setValue(new String(password));
		
		jettyConfig.get(JettyConstants.SSL_PROTOCOL).setValue(DEFAULT_SSL_PROTOCOL);
//		jettyConfig.get(JettyConstants.SSL_ALGORITHM).setValue("");
	}

	/**
	 * Creates the key store with the default certificate and returns the KeyStore file..
	 *
	 * @param serverName the server name
	 * @param password the password
	 * @return the file instance of the KeyStore
	 */
	private static File createKeyStore(String serverName, char[] password) {
		
		File keyStoreFile = null;
		try {
			// --- Create self signed certificate -----------------------------
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(4096);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			X509Certificate cert = SSLJettyConfiguration.generateSelfSignedCertificate(keyPair, "SHA256withRSA", DEFAULT_DOMAIN, 730);
			Certificate[] certArray = {cert};
			
			// --- Define destination file ------------------------------------
			keyStoreFile = SSLJettyConfiguration.getDefaultKeyStoreFile(serverName);
			if (keyStoreFile.exists()==true) keyStoreFile.delete();

			// --- Create the KeyStore ----------------------------------------
			KeyStore keyStore = KeyStore.getInstance(DEFAULT_KEYSTORE_TYPE.getType());
			keyStore.load(null, password);
			keyStore.setKeyEntry(DEFAULT_DOMAIN, keyPair.getPrivate(), password, certArray);
			keyStore.store(new FileOutputStream(keyStoreFile), password);
			
		} catch (Exception ex) {
			keyStoreFile = null;
			ex.printStackTrace();
		}
    	return keyStoreFile;
	}

	/**
	 * Generates a self signed certificate using the BouncyCastle lib.
	 *
	 * @param keyPair       used for signing the certificate with PrivateKey
	 * @param hashAlgorithm Hash function
	 * @param cn            Common Name to be used in the subject dn
	 * @param days          validity period in days of the certificate
	 * @return self-signed X509Certificate
	 * @throws OperatorCreationException on creating a key id
	 * @throws CertificateException      on getting certificate from provider
	 * @throws CertIOException           on building JcaContentSignerBuilder
	 */
	public static X509Certificate generateSelfSignedCertificate(final KeyPair keyPair, final String hashAlgorithm, final String cn, final int days) throws OperatorCreationException, CertificateException, CertIOException {
		
		final Instant now = Instant.now();
		final Date notBefore = Date.from(now);
		final Date notAfter = Date.from(now.plus(Duration.ofDays(days)));

		final ContentSigner contentSigner = new JcaContentSignerBuilder(hashAlgorithm).build(keyPair.getPrivate());
		final X500Name x500Name = new X500Name("CN=" + cn);
		final X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(x500Name,
				BigInteger.valueOf(now.toEpochMilli()), notBefore, notAfter, x500Name, keyPair.getPublic())
				.addExtension(Extension.subjectKeyIdentifier, false, createSubjectKeyId(keyPair.getPublic()))
				.addExtension(Extension.authorityKeyIdentifier, false, createAuthorityKeyId(keyPair.getPublic()))
				.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));

		return new JcaX509CertificateConverter().setProvider(new BouncyCastleProvider()).getCertificate(certificateBuilder.build(contentSigner));
	}

	/**
	 * Creates the hash value of the public key.
	 *
	 * @param publicKey of the certificate
	 * @return SubjectKeyIdentifier hash
	 * @throws OperatorCreationException the operator creation exception
	 */
	private static SubjectKeyIdentifier createSubjectKeyId(final PublicKey publicKey) throws OperatorCreationException {
		final SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
		final DigestCalculator digCalc = new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));
		return new X509ExtensionUtils(digCalc).createSubjectKeyIdentifier(publicKeyInfo);
	}

	/**
	 * Creates the hash value of the authority public key.
	 *
	 * @param publicKey of the authority certificate
	 * @return AuthorityKeyIdentifier hash
	 * @throws OperatorCreationException the operator creation exception
	 */
	private static AuthorityKeyIdentifier createAuthorityKeyId(final PublicKey publicKey) throws OperatorCreationException {
		final SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
		final DigestCalculator digCalc = new BcDigestCalculatorProvider() .get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));
		return new X509ExtensionUtils(digCalc).createAuthorityKeyIdentifier(publicKeyInfo);
	}

}
