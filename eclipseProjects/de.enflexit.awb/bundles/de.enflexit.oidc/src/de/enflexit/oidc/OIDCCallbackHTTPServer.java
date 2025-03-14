package de.enflexit.oidc;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

import com.nimbusds.oauth2.sdk.Response;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * This class provides a simple HTTP server to listen for callbacks from the OIDC provider.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class OIDCCallbackHTTPServer {
	
	private int port;
	private String callbackEndpoint;
	
	private OIDCCallbackListener callbackListener;
	
	private HttpServer httpServer;
	
	private boolean stopServer;

	/**
	 * Instantiates a new OIDC callback HTTP server.
	 *
	 * @param port the port
	 * @param callbackEndpoint the callback endpoint
	 * @param callbackListener the callback listener
	 */
	public OIDCCallbackHTTPServer(int port, String callbackEndpoint, OIDCCallbackListener callbackListener) {
		this.port = port;
		this.callbackEndpoint = callbackEndpoint;
		this.callbackListener = callbackListener;
	}

	private HttpServer getHttpServer() {
		if (httpServer==null) {
			try {
				httpServer = HttpServer.create(new InetSocketAddress(this.port), 0);
				httpServer.createContext(this.callbackEndpoint, new HttpHandler() {

					@Override
					public void handle(HttpExchange exchange) throws IOException {
						OIDCCallbackHTTPServer.this.handleCallback(exchange);
						
						String response = "Authentication successful, please close this browser window/tab!";
						exchange.sendResponseHeaders(200, response.length());
						OutputStream os = exchange.getResponseBody();
						os.write(response.getBytes());
						os.close();
						
						OIDCCallbackHTTPServer.this.stopServer = true;
					}
					
				});
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return httpServer;
	}
	
	private void handleCallback(HttpExchange exchange) {
		this.callbackListener.callbackReceived(exchange);
	}
	
	public void startInThread() {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpServer httpServer = OIDCCallbackHTTPServer.this.getHttpServer();
				if (httpServer!=null) {
					httpServer.start();
					
					// --- Wait till the callback has been processed
					while(OIDCCallbackHTTPServer.this.stopServer==false) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
					
					httpServer.stop(0);
				}
				
			}
		}).start();
		
	}
	
	
}
