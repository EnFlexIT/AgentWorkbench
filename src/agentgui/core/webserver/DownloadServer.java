package agentgui.core.webserver;

import java.io.*;
import java.net.*;
import java.util.*;

import agentgui.core.application.Application;
import agentgui.core.application.Project;
import agentgui.core.common.FileCopier;
import agentgui.core.network.PortChecker;

public class DownloadServer implements HttpConstants, Runnable {

    private Vector<DownloadWorker> threads = new Vector<DownloadWorker>();		/* Where worker threads stand idle */
    
    private File root = null;													/* the web server's virtual root */
    private int port = 8080;													/* default port for this web server */
    private int timeout = 5000;													/* timeout on client connections */
    private int workers = 5;													/* max # worker threads */
    
    private ServerSocket currSocket = null; 
    private boolean stopServer = false;
    
    private HashMap<String, File> openResources = new HashMap<String, File>();
    
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
    
    /**
     * This method stops the Download - Webserver
     */
    public synchronized void stop() {
	
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
    	// --- Delete all open resource folder ------------
    	for (String projectFolder : openResources.keySet()) {
    		File folderDelete = openResources.get(projectFolder);
    		if (folderDelete.exists()==true) {
        		deleteFolder(folderDelete);
        		folderDelete.delete();
        	}
		}
    	openResources.clear();
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
     * This method will place the specific project-resources (jar-files)
     * in the download-folder and will leave the http-Addresses of this
     * files as complete download-link in Project.downloadResources 									
     */
    public void setProjectDownloadResources(Project project) {
    	 
    	String pathSep = Application.RunInfo.AppPathSeparatorString();
    	String sourceDirName = Application.RunInfo.PathProjects(true, false);
    	String destinDirName = Application.RunInfo.PathServer(true) + project.getProjectFolder();    	
    	
    	File destinDir = null;
    	boolean destinDirNewlyCreated = false;
    	
    	String link2Server = this.getHTTPAddress();
    	Vector<String> downloadLinks = new Vector<String>();
    	
    	// -------------------------------------------------------------------------
    	// --- Look for external jar-resources of the project ----------------------
    	// -------------------------------------------------------------------------
    	for (Iterator<String> iterator = project.projectResources.iterator(); iterator.hasNext();) {
			
    		// ---------------------------------------------------------------------
    		// --- Create destination directory newly, even if it already exists --- 
    		if (destinDirNewlyCreated==false) {
    			destinDir = new File(destinDirName);
            	if (destinDir.exists()==true) {
            		deleteFolder(destinDir);
            		destinDir.delete();
            	}
            	destinDir.mkdir();
            	
            	// --- Reminder for the later delete - action ----------------------
            	openResources.put(project.getProjectFolder(), new File(destinDirName));
            	
            	// --- This task is done this time ---------------------------------
            	destinDirNewlyCreated = true;
    		}
    		
    		// ---------------------------------------------------------------------
    		// --- Copy the resource files to the destination Folder ---------------
    		String linkAddition = iterator.next();
    		String singelSource = linkAddition;
    		
			// --- Just in case, that we have a absolute/relative path indicator --- 
			if (new File(singelSource).exists()==false) {
				singelSource = sourceDirName + singelSource;
				singelSource = singelSource.replace(pathSep+pathSep, pathSep);
			}

			// --- If the source file exists ... -----------------------------------
			File singelSourceFile = new File(singelSource); 
			if (singelSourceFile.exists()) {
				
				String singleFile   = singelSourceFile.getAbsoluteFile().getName();
				String singleDestin = destinDirName + pathSep + singleFile;
				// --- Execute copy process for the file ---------------------------
				FileCopier fc = new FileCopier();
	    		fc.copyFile(singelSource, singleDestin);
	    		
	    		// --- Remind this file as a link to the Download-Server -----------
	    		String singleLink = link2Server + "/" + project.getProjectFolder() + "/" + singleFile;
	    		downloadLinks.addElement(singleLink);
			}

    	} // --- end for
    	
    	
    	// -------------------------------------------------------------------------
		// --- If we're running in the IDE of Agent.GUI ----------------------------
    	// -------------------------------------------------------------------------
		if (Application.RunInfo.AppExecutedOver().equalsIgnoreCase("IDE")) {
			
			if (destinDirNewlyCreated==false) {
    			destinDir = new File(destinDirName);
            	if (destinDir.exists()==true) {
            		deleteFolder(destinDir);
            		destinDir.delete();
            	}
            	destinDir.mkdir();
            	
            	// --- Reminder for the later delete - action ----------------------
            	openResources.put(project.getProjectFolder(), new File(destinDirName));
            	
            	// --- This task is done this time ---------------------------------
            	destinDirNewlyCreated = true;
    		}
			
			// --- Pack a jar of all class-files of the current project ------------
			String pathBin = Application.RunInfo.PathBaseDirIDE_BIN();
			JarFileCreater jarCreator = new JarFileCreater(pathBin, project.getProjectFolder());
			
			String jarArchiveName = project.getProjectFolder() + "_IDE.jar";
			String jarArchivePath = destinDirName + pathSep + jarArchiveName;
			File jarArchiveFile = new File(jarArchivePath);
			jarCreator.createJarArchive(jarArchiveFile);
			
			// --- Remind this file as a link to the Download-Server -----------
    		String singleLink = link2Server + "/" + project.getProjectFolder() + "/" + jarArchiveName;
    		downloadLinks.addElement(singleLink);
		}
    	
    	// --- Store the download links of the resources in the Project-Instance ---
    	project.downloadResources = downloadLinks;
    }
    /**
     * Deletes a folder and all subelements
     * @param directory
     */
    private void deleteFolder(File directory) {
    	
    	for (File file : directory.listFiles()) {
    		if (file.isDirectory()) {
	    		deleteFolder(file);
	    	}
	    	file.delete();
		}
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
