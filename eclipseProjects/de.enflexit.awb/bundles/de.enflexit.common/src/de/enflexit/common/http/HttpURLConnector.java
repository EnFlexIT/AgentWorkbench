package de.enflexit.common.http;

import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import de.enflexit.common.http.WebResourcesAuthorization.AuthorizationType;
import de.enflexit.language.Language;

/**
 * The Class HttpURLConnector provides static methods to establish a {@link HttpURLConnection}.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class HttpURLConnector {

	/**
	 * Open http connection to update site.
	 *
	 * @param sourceURL the source URL
	 * @param auth authorization details
	 * @return the http URL connection
	 * @throws HttpURLConnectorException the project repository update exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	public static HttpURLConnection openConnection(String sourceURL, WebResourcesAuthorization auth) throws HttpURLConnectorException,  IOException, IllegalArgumentException, URISyntaxException{
		return HttpURLConnector.openConnection(new URI(sourceURL).toURL(), auth);
	}
	/**
	 * Open http connection to update site.
	 *
	 * @param sourceURL the source URL
	 * @return the http URL connection
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws HttpURLConnectorException the project repository update exception
	 * @throws URISyntaxException 
	 */
	private static HttpURLConnection openConnection(URL sourceURL, WebResourcesAuthorization auth) throws IOException, HttpURLConnectorException, IllegalArgumentException, URISyntaxException {		
  	
		HttpURLConnection connection = null;
		Authenticator.setDefault(null);
	
		// --- Open http or https connection --------------------
		if (sourceURL.getProtocol().equals("https")) {
			connection = (HttpsURLConnection) sourceURL.openConnection(); 
		} else if (sourceURL.getProtocol().equals("http")) {
			connection = (HttpURLConnection) sourceURL.openConnection(); 
		} else {
			throw new IOException("[" + HttpURLConnector.class.getSimpleName() + "] Update site must use http or https protocol");
		}

		// --- Configure HTTP request ----------------------------
		connection.addRequestProperty("User-Agent", "Mozilla/4.76"); 
		connection.setAllowUserInteraction(false);
		connection.setDoOutput(true);
		if(auth != null && auth.getType() != AuthorizationType.NONE) {
			connection.setRequestProperty("Authorization", auth.getEncodedHeader());
		}
		// --- Throwing exception to inform user after a failed http request --------------------------- 
		switch (connection.getResponseCode()) {
		case HttpURLConnection.HTTP_OK:
			break;
		case HttpURLConnection.HTTP_MULT_CHOICE:
		case HttpURLConnection.HTTP_MOVED_PERM:
		case HttpURLConnection.HTTP_MOVED_TEMP:
			throw new HttpURLConnectorException(Language.translate("Die angeforderte Ressource steht nicht mehr unter der gewählten Update-Site zur Verfügung und wurde umgeleitet"));
		case HttpURLConnection.HTTP_BAD_REQUEST:
			throw new HttpURLConnectorException(Language.translate("Die Anfrage war fehlerhaft"));
		case HttpURLConnection.HTTP_UNAUTHORIZED:
			throw new HttpURLConnectorAuthorizationException(Language.translate("Authorisierungsdaten fehlerhaft oder nicht vorhanden."));
		case HttpURLConnection.HTTP_FORBIDDEN:
			throw new HttpURLConnectorException(Language.translate("Authentifizierung ist nicht möglich. Bitte Authorisierungseinstellungen überprüfen"));
		case HttpURLConnection.HTTP_NOT_FOUND:
			throw new HttpURLConnectorException(Language.translate("Angegebene Update-Site enthält keine Repositorydatei"));
		case HttpURLConnection.HTTP_INTERNAL_ERROR:
			throw new HttpURLConnectorException(Language.translate("Serverfehler aufgetreten"));
		case 418:
			throw new HttpURLConnectorException(Language.translate("Ich bin eine Teekanne"));
		default:
			throw new HttpURLConnectorException(Language.translate("Unbekannter Fehler aufgetreten. Response-Code: ")+ connection.getResponseCode());					
		}
		return connection;
	}
	
}
