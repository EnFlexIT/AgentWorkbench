package de.enflexit.oidc;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * This class provides a simple HTTP server to listen for callbacks from the OIDC provider.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class OIDCCallbackHTTPServer {
	
	// A minimal HTML page that invokes a JavaScript to close the window/tab 
	private static final String REDIRECT_PAGE_CONTENT = "<html><head></head><body><script type=\"text/javascript\">window.close();</script><p>OIDC redirect successful, please close this window/tab and return to the application!</p></body></html>"; 
	
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

						String response = REDIRECT_PAGE_CONTENT;
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
	
	/**
	 * Starts the server in a separate thread.
	 */
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
	
	/**
	 * Stop the server.
	 */
	public void stop() {
		this.stopServer = true;
	}
	
}
