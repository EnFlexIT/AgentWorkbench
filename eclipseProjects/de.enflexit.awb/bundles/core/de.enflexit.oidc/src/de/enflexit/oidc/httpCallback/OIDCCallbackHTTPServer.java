package de.enflexit.oidc.httpCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import de.enflexit.oidc.OIDCCallbackListener;
import de.enflexit.oidc.httpCallback.PropertyContentProvider.FileToProvide;

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
	
	private PropertyContentProvider propertyContentProvider;

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
						
						File htmlFile = PathHandling.getPropertiesPath(true).resolve(FileToProvide.LOGIN_SUCCESS_HTML.toString()).toFile();
						
						if (htmlFile!=null && htmlFile.exists()==true) {
							FileInputStream fis = new FileInputStream(htmlFile);
							exchange.getResponseHeaders().set("Content-Type", "text/html");
							exchange.sendResponseHeaders(200, htmlFile.length());
							
							OutputStream os = exchange.getResponseBody();
							
							byte[] buf = new byte[4096];
							int n;
							while ((n = fis.read(buf)) >= 0) {
								os.write(buf, 0, n);
							}
							os.close();
							fis.close();
						} else {
							System.err.println("[" + this.getClass().getSimpleName() + "] HTML file not found!");
							exchange.getResponseHeaders().set("Content-Type", "text/plain");
							String response = "HTTP Error 404 - File not found";
							exchange.sendResponseHeaders(404, response.length());
							
							OutputStream os = exchange.getResponseBody();
							os.write(response.getBytes());
							os.close();
							
						}

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
	 * Makes sure the callback page is available. If the server is started from a jar bundle, it is copied to the application's properties folder.
	 */
	private void prepareCallbackPage() {
		File propFile = PathHandling.getPropertiesPath(true).toFile();
		propertyContentProvider = new PropertyContentProvider(propFile);
		for (FileToProvide fileToProvide : FileToProvide.values()) {
			propertyContentProvider.checkAndProvidePropertyContent(fileToProvide, false);
		}
	}
	
	/**
	 * Starts the server in a separate thread.
	 */
	public void startInThread() {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				OIDCCallbackHTTPServer.this.start();
			}
		}).start();
	}
	
	/**
	 * Starts the sever.
	 */
	private void start() {
		this.prepareCallbackPage();
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
			
			httpServer.stop(60);
//			this.disposeCallbackPage();
		}
	}
	
	/**
	 * Stop the server.
	 */
	public void stop() {
		this.stopServer = true;
	}
	
}
