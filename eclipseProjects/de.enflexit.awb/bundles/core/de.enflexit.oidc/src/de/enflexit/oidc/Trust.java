package de.enflexit.oidc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * This class helps managing the trust to a specific key/certificate for a given connection.
 */
public class Trust {

	public static final String OIDC_TRUST_STORE = "oidcTrustStore.jks";
	private static final String TRUSTSTORE_PASSWORD = "HoYp8FfLLVJJFX1APMQA";

	/**
	 * Trust a specific certificate (or set of) for a specific connection.
	 *
	 * @param connection the connection
	 * @param trustStoreFile the trust store file
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws CertificateException the certificate exception
	 * @throws FileNotFoundException the file not found exception
	 * @throws KeyStoreException the key store exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void trustSpecific(HttpsURLConnection connection, File trustStoreFile) throws KeyManagementException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, KeyStoreException, IOException {
		connection.setSSLSocketFactory(getSocketFactory(trustStoreFile));
	}

	/**
	 * Gets the socket factory.
	 *
	 * @param trustStoreFile the trust store file
	 * @return the socket factory
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws CertificateException the certificate exception
	 * @throws FileNotFoundException the file not found exception
	 * @throws KeyStoreException the key store exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static SSLSocketFactory getSocketFactory(File trustStoreFile) throws KeyManagementException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, KeyStoreException, IOException {
		SSLContext trustableSSLContext = getTrustableSSLContext(trustStoreFile);
		return trustableSSLContext.getSocketFactory();
	}

	/**
	 * Gets a SSLContext to be trusted.
	 *
	 * @param trustStoreFile the trust store file
	 * @return the trustable SSL context
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws CertificateException the certificate exception
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws KeyStoreException the key store exception
	 * @throws KeyManagementException the key management exception
	 */
	private static SSLContext getTrustableSSLContext(File trustStoreFile) throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyStoreException, KeyManagementException {
		SSLContext sslContext = null;

		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		trustStore.load(new FileInputStream(trustStoreFile), TRUSTSTORE_PASSWORD.toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustStore);
		TrustManager[] tms = tmf.getTrustManagers();

		sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, tms, null);
		return sslContext;
	}
}
