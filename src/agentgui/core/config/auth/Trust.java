package agentgui.core.config.auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class Trust {
	public static final String OIDC_TRUST_STORE = "oidcTrustStore.jks";
	private static final String TRUSTSTORE_PASSWORD = "HoYp8FfLLVJJFX1APMQA";

	// SECURITY BREACH!!! !!NEVER!! use this on production systems, only for debugging

	// https://stackoverflow.com/questions/13022717/java-and-https-url-connection-without-downloading-certificate
	// https://stackoverflow.com/questions/859111/how-do-i-accept-a-self-signed-certificate-with-a-java-httpsurlconnection
	public static HostnameVerifier getAllHostsValid() {
		// Create all-trusting host name verifier
		return new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
	}

	public static void trustSpecific(HttpsURLConnection connection, File trustStoreFile) {
		connection.setSSLSocketFactory(getSocketFactory(trustStoreFile));
		connection.setHostnameVerifier(getAllHostsValid());
	}

	public static SSLSocketFactory getSocketFactory(File trustStoreFile) {
		SSLContext trustableSSLContext = getTrustableSSLContext(trustStoreFile);
		return trustableSSLContext.getSocketFactory();
	}

	private static SSLContext getTrustableSSLContext(File trustStoreFile) {
		SSLContext sslContext = null;

		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(new FileInputStream(trustStoreFile), TRUSTSTORE_PASSWORD.toCharArray());

			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(trustStore);
			TrustManager[] tms = tmf.getTrustManagers();

			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, tms, null);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		return sslContext;
	}
}
