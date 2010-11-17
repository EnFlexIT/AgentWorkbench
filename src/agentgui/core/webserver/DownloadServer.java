package agentgui.core.webserver;

import java.io.*;
import java.net.*;
import java.util.*;

import agentgui.core.network.PortChecker;

public class DownloadServer implements HttpConstants, Runnable {

    private Vector<DownloadWorker> threads = new Vector<DownloadWorker>();		/* Where worker threads stand idle */
    
    private File root = null;													/* the web server's virtual root */
    private int port = 8080;													/* default port for this web server */
    private int timeout = 5000;												/* timeout on client connections */
    private int workers = 5;													/* max # worker threads */
    
    private ServerSocket currSocket = null; 
    private boolean stopServer = false;
    
    /**
     * Constructor of this class 
     */
    public DownloadServer() {
    }
    
    @Override
	public void run() {
    	
    	Thread.currentThread().setName("DownloadServer");
    	// --- start worker threads -----------------------
        for (int i = 0; i < workers; ++i) {
        	DownloadWorker w = new DownloadWorker(this);
            new Thread(w, "DownloadWorker"+i).start();
            threads.addElement(w);
        }

		try {
			
			PortChecker pc = new PortChecker(port);
			currSocket = new ServerSocket(pc.getFreePort());
			while (true) {
	            Socket s = currSocket.accept();
	            if (this.stopServer==true) {
	            	break;
	            }
	            DownloadWorker w = null;
	            synchronized (threads) {
	                if (threads.isEmpty()) {
	                	DownloadWorker ws = new DownloadWorker(this);
	                    ws.setSocket(s);
	                    (new Thread(ws, "additional worker")).start();
	                } else {
	                    w = (DownloadWorker) threads.elementAt(0);
	                    threads.removeElementAt(0);
	                    w.setSocket(s);
	                }
	            }
	        }
		
		} catch (IOException exept) {
			if (exept instanceof SocketException) {
				SocketException soEx = (SocketException) exept;
				if (soEx.getMessage().equals("socket closed")==false) {
					exept.printStackTrace();	
				}
			} else {
				exept.printStackTrace();	
			}
			
		}
	}
    
    synchronized public void stop() {
	
    	// --- Terminate the web server -------------------
    	this.stopServer = true;
		try {
			this.currSocket.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
    	
		// --- Terminate the worker - Threads -------------
    	synchronized (threads) {
    		// --- Stop the worker Threads ----------------
    		for (int i = 0; i < threads.size(); i++) {
        		synchronized (threads.get(i)) {
        			threads.get(i).stopExecution();
        			threads.get(i).notify();
				}
    		}
		}
    	
	}
    
    /**
     * Returns the Webserver-Address including the currently used port 
     * @return
     */
    public String getHTTPAddress() {
    	
    	String httpAddress = null;
    	try {
			InetAddress addressLocal = InetAddress.getLocalHost();
			httpAddress = addressLocal.getCanonicalHostName();
			httpAddress = "http://" + httpAddress + ":" + port;
			
    	} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	return httpAddress;
    }
    
	/**
	 * @return the threads
	 */
	public Vector<DownloadWorker> getThreads() {
		return threads;
	}
	/**
	 * @param threads the threads to set
	 */
	public void setThreads(Vector<DownloadWorker> threads) {
		this.threads = threads;
	}
	/**
	 * @return the root
	 */
	public File getRoot() {
		return root;
	}
	/**
	 * @param root the root to set
	 */
	public void setRoot(File root) {
		this.root = root;
	}
	public void setRoot(String rootDirectory) {
		this.root = new File(rootDirectory);
	}
	
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}
	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * @return the workers
	 */
	public int getWorkers() {
		return workers;
	}
	/**
	 * @param workers the workers to set
	 */
	public void setWorkers(int workers) {
		this.workers = workers;
	}

}
