package de.enflexit.awb.samples.ws.restapi.client;

import org.glassfish.jersey.client.JerseyClientBuilder;

import de.enflexit.awb.samples.ws.restapi.client.gen.handler.ApiClient;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

/**
 * The Class JerseyApiClient is basically identical to the the originally used {@link ApiClient} (which actually is the super class of this class).<br>
 * The only part overwritten is the method {@link #buildHttpClient()}.<br><br> 
 * 
 * Due to the problem of using 'ClientBuilder.newBuilder()' in our OSGI environment, it was necessary to directly call the {@link JerseyClientBuilder} 
 * in that method and avoid the usage of 'META-INF/services/...'  
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JerseyApiClient extends ApiClient {

	/* (non-Javadoc)
	 * @see de.enflexit.awb.samples.ws.restapi.client.gen.handler.ApiClient#buildHttpClient()
	 */
	@Override
	protected Client buildHttpClient() {
		// --- Create ClientConfig if it has not been initialized yet ----
		if (clientConfig == null) {
			clientConfig = getDefaultClientConfig();
		}

		ClientBuilder clientBuilder = new JerseyClientBuilder();
		clientBuilder = clientBuilder.withConfig(clientConfig);
		customizeClientBuilder(clientBuilder);
		return clientBuilder.build();
	}
}
