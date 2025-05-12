package de.enflexit.oidc.httpCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import de.enflexit.awb.core.jade.PortChecker;
import de.enflexit.oidc.OIDCCallbackListener;
import de.enflexit.oidc.httpCallback.PropertyContentProvider.FileToProvide;

/**
 * This class provides a simple HTTP server to listen for callbacks from the OIDC provider.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class OIDCCallbackHTTPServer {
	
	private static final String URL_PREFIX = "/oauth/callback/";
	private static final int DEFAULT_PORT = 8888;
	
	private int port;
	private String callbackEndpoint;
	
	private OIDCCallbackListener callbackListener;
	
	private HttpServer httpServer;
	
	private boolean stopServer;
	
	private PropertyContentProvider propertyContentProvider;
	
	private HashMap<String, String> supportedMimeTypes;

	/**
	 * Instantiates a new OIDC callback HTTP server.
	 *
	 * @param port the port
	 * @param callbackEndpoint the callback endpoint
	 * @param callbackListener the callback listener
	 */
	public OIDCCallbackHTTPServer(int port, String callbackEndpoint, OIDCCallbackListener callbackListener) {
		this.port = new PortChecker(DEFAULT_PORT).getFreePort();
		this.callbackEndpoint = URL_PREFIX;
		this.callbackListener = callbackListener;
	}

	private HttpServer getHttpServer() {
		if (httpServer==null) {
			try {
				httpServer = HttpServer.create(new InetSocketAddress(this.port), 0);
				httpServer.createContext(this.callbackEndpoint, new HttpHandler() {

					@Override
					public void handle(HttpExchange exchange) throws IOException {
						
						
						String wholeUrlPath = exchange.getRequestURI().getPath();
						String filePath;
				        if (wholeUrlPath.endsWith("/")) {
				            filePath = FileToProvide.LOGIN_SUCCESS_HTML.toString();
				            OIDCCallbackHTTPServer.this.handleCallback(exchange);
				        } else {
				        	filePath = wholeUrlPath.substring(URL_PREFIX.length());
				        }
						
						File file = PathHandling.getPropertiesPath(true).resolve(filePath).toFile();
						
						if (file!=null && file.exists()==true) {
							FileInputStream fis = new FileInputStream(file);
							exchange.getResponseHeaders().set("Content-Type", OIDCCallbackHTTPServer.this.getMimeTypeForFile(file.getName()));
							exchange.sendResponseHeaders(200, file.length());
							
							OutputStream os = exchange.getResponseBody();
							
							byte[] buf = new byte[4096];
							int n;
							while ((n = fis.read(buf)) >= 0) {
								os.write(buf, 0, n);
							}
							os.close();
							fis.close();
						} else {
							System.err.println("[" + this.getClass().getSimpleName() + "] File not found!");
							OIDCCallbackHTTPServer.this.sendFileNotFound(exchange);
							
						}

						OIDCCallbackHTTPServer.this.stopServer = true;
					}
					
				});
				
//				httpServer.createContext(this.callbackEndpoint, new StaticFileHandler("/resources/", "/oauth/callback/", FileToProvide.LOGIN_SUCCESS_HTML.toString()));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return httpServer;
	}
	
	private void sendFileNotFound(HttpExchange exchange) throws IOException {
		
		exchange.getResponseHeaders().set("Content-Type", "text/plain");
		String response = "HTTP Error 404 - File not found";
		exchange.sendResponseHeaders(404, response.length());
		
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
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
		}, OIDCCallbackHTTPServer.class.getSimpleName() + "-Thread").start();
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
	
	/**
	 * Gets the mime type for the provided file, if supported.
	 * @param fileName the file name
	 * @return the mime type, "text/plain" if unknown or not supported.
	 */
	private String getMimeTypeForFile(String fileName) {
		String suffix = fileName.substring(fileName.lastIndexOf('.'));
		String mimeType = this.getSupportedMimeTypes().get(suffix);
		if (mimeType==null) {
			// --- Fallback -----------------------------------------
			mimeType = "text/plain";
		}
		
		return mimeType;
	}
	
	/**
	 * Gets the supported mime types by the corresponding file suffix.
	 * @return the supported mime types
	 */
	private HashMap<String, String> getSupportedMimeTypes() {
		if (supportedMimeTypes==null) {
			supportedMimeTypes = new HashMap<String, String>();
			supportedMimeTypes.put(".html", "text/html");
			supportedMimeTypes.put(".png", "image/png");
			supportedMimeTypes.put(".jpg", "image/jpeg");
		}
		return supportedMimeTypes;
	}
	
//	private class StaticFileHandler implements HttpHandler {
//		
//		private String filesystemRoot;
//	    private String urlPrefix;
//	    private String directoryIndex;
//
//		public StaticFileHandler(String filesystemRoot, String urlPrefix, String directoryIndex) {
//			try {
//				this.filesystemRoot = new File(filesystemRoot).getCanonicalPath();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			this.urlPrefix = urlPrefix;
//			this.directoryIndex = directoryIndex;
//		}
//
//
//		@Override
//		public void handle(HttpExchange exchange) throws IOException {
//			String wholeUrlPath = exchange.getRequestURI().getPath();
//	        if (wholeUrlPath.endsWith("/")) {
//	            wholeUrlPath += directoryIndex;
//	        }
//	        
//	        String urlPath = wholeUrlPath.substring(urlPrefix.length());
//	        File file = PathHandling.getPropertiesPath(true).resolve(urlPath).toFile();
//	        File canonicalFile;
//	        canonicalFile = file.getCanonicalFile();
//	        
//	        FileInputStream fis;
//	        try {
//	            fis = new FileInputStream(canonicalFile);
//	        } catch (FileNotFoundException e) {
//	            // The file may also be forbidden to us instead of missing, but we're leaking less information this way 
//	        	String errorContent = "File not found";
//	        	exchange.getResponseHeaders().set("Content-Type", "text/plain");
//	        	exchange.sendResponseHeaders(404, errorContent.length());
//	        	OutputStream os = exchange.getResponseBody();
//	            os.write(errorContent.getBytes());
//	            os.close();
//	            return;
//	        }
//	        
//	        String mimeType = OIDCCallbackHTTPServer.this.getMimeTypeForFile(canonicalFile.getName());
//	        exchange.getRequestHeaders().set("Content-Type", mimeType);
//	        exchange.sendResponseHeaders(200, canonicalFile.length());            
//            OutputStream os = exchange.getResponseBody();
//            byte[] buf = new byte[4096];
//            int n;
//            while ((n = fis.read(buf)) >= 0) {
//                os.write(buf, 0, n);
//            }
//            os.close();
//            fis.close();
//		}
//		
//	}
	
}
