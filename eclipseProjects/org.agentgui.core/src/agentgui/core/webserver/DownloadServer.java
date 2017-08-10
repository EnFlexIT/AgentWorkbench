/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.webserver;

import java.io.*;
import java.net.*;
import java.util.*;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo.ExecutionEnvironment;
import agentgui.core.network.PortChecker;
import agentgui.core.project.PlatformJadeConfig;
import agentgui.core.project.Project;
import de.enflexit.common.transfer.FileCopier;

/**
 * This class provides the main class of the <b>Agent.GUI</b> web server for the
 * resources download to remote hosts.<br>
 * During instantiation of the web server 5 further worker threads will be started
 * in order to handle the client request.  
 * 
 * @see DownloadWorker
 */
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
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
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
			System.out.println("Started HTTP download server (for load balancing/agent mobility resources or update propagation) on port "+currSocket.getLocalPort()+" of ALL network adresses/devices.");
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
				if (soEx.getMessage().equalsIgnoreCase("socket closed")==false) {
					exept.printStackTrace();	
				}
			} else {
				exept.printStackTrace();	
			}
			
		}
	}
    
    /**
     * This method stops the download-web server.
     */
    public synchronized void stop() {
	
    	// --- Terminate the web server -------------------
    	this.stopServer = true;
    	if (this.currSocket!=null) {
    		try {
    			this.currSocket.close();
    		} catch (IOException e) {
    			//e.printStackTrace();
    		}	
    	}
		
		// --- Terminate the worker - Threads -------------
    	if (threads!=null) {
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
     * Returns the webserver-Address including the currently used port.
     * @return the HTTP address
     */
    public String getHTTPAddress() {
    	String httpAddress = null;
    	String httpIpAddress = null;
    	try {
			PlatformJadeConfig jadeConfig = null;
			if (Application.getProjectFocused()==null) {
				jadeConfig = Application.getGlobalInfo().getJadeDefaultPlatformConfig();
			} else {
				jadeConfig = Application.getProjectFocused().getJadeConfiguration();
			}
			httpIpAddress = jadeConfig.getMtpIpAddress();
    		
			if (httpIpAddress==null || httpIpAddress.equals("") || httpIpAddress.equals(PlatformJadeConfig.MTP_IP_AUTO_Config)) {
    			InetAddress addressLocal = InetAddress.getLocalHost();
    			httpIpAddress = addressLocal.getCanonicalHostName();
    		}
			httpAddress = "http://" + httpIpAddress + ":" + port;
			
    	} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	return httpAddress;
    }
    
    /**
     * This method will place the specific project-resources (jar-files)
     * in the download-folder and will leave the http-Addresses of this
     * files as complete download-link in Project.downloadResources
     *
     * @param project the project with its download resources
     */
    public void setProjectDownloadResources(Project project) {
    	 
    	String pathSep = File.separator;
    	String sourceDirName = project.getProjectFolderFullPath();
    	String destinDirName = Application.getGlobalInfo().getPathWebServer() + project.getProjectFolder();    	
    	
    	File destinDir = null;
    	boolean destinDirNewlyCreated = false;
    	
    	String link2Server = this.getHTTPAddress();
    	Vector<String> downloadLinks = new Vector<String>();
    	
    	// -------------------------------------------------------------------------
    	// --- Look for external jar-resources of the project ----------------------
    	// -------------------------------------------------------------------------
    	for (Iterator<String> iterator = project.getProjectResources().iterator(); iterator.hasNext();) {
			
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
				
				if (singelSourceFile.isDirectory()) {
					// ----------------------------------------------------------------
					// --- Here we have a directory entry -----------------------------
					// ----------------------------------------------------------------
					// --- Pack a jar of all class-files of the current project -------
					String pathBin = singelSourceFile.getAbsolutePath();
					String pathBinHash = ((Integer)pathBin.hashCode()).toString();
					JarFileCreator jarCreator = new JarFileCreator(pathBin);
					
					String jarArchiveName = "BIN_DUMP_" + pathBinHash + ".jar";
					String jarArchivePath = destinDirName + pathSep + jarArchiveName;
					File jarArchiveFile = new File(jarArchivePath);
					jarCreator.createJarArchive(jarArchiveFile);
					
					// --- Remind this file as a link to the Download-Server -----------
		    		String singleLink = link2Server + "/" + project.getProjectFolder() + "/" + jarArchiveName;
		    		downloadLinks.addElement(singleLink);
					
				} else {
					// ----------------------------------------------------------------
					// --- Here we have a explicitly defined jar-file ----------------- 
					// ----------------------------------------------------------------
					String singleFile   = singelSourceFile.getAbsoluteFile().getName();
					String singleDestin = destinDirName + pathSep + singleFile;
					// --- Execute copy process for the file ---------------------------
					FileCopier fc = new FileCopier();
		    		fc.copyFile(singelSource, singleDestin);
		    		
		    		// --- Remind this file as a link to the Download-Server -----------
		    		String singleLink = link2Server + "/" + project.getProjectFolder() + "/" + singleFile;
		    		downloadLinks.addElement(singleLink);
				}
			}

    	} // --- end for
    	
    	
    	// -------------------------------------------------------------------------
		// --- If we're running in the IDE of Agent.GUI ----------------------------
    	// -------------------------------------------------------------------------
		if (Application.getGlobalInfo().getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverIDE) {
			
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
			String pathBin = Application.getGlobalInfo().getPathBaseDirIDE_BIN();
			JarFileCreator jarCreator = new JarFileCreator(pathBin, project.getProjectFolder());
			
			String jarArchiveName = project.getProjectFolder() + "_IDE.jar";
			String jarArchivePath = destinDirName + pathSep + jarArchiveName;
			File jarArchiveFile = new File(jarArchivePath);
			jarCreator.createJarArchive(jarArchiveFile);
			
			// --- Remind this file as a link to the Download-Server -----------
    		String singleLink = link2Server + "/" + project.getProjectFolder() + "/" + jarArchiveName;
    		downloadLinks.addElement(singleLink);
		}
    	
    	// --- Store the download links of the resources in the Project-Instance ---
    	project.setDownloadResources(downloadLinks);
    }
    
    /**
     * Deletes a folder and all sub elements.
     * @param directory the directory
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
     * Gets the threads.
     * @return the threads
     */
	public Vector<DownloadWorker> getThreads() {
		return threads;
	}
	/**
	 * Sets the threads.
	 * @param threads the threads to set
	 */
	public void setThreads(Vector<DownloadWorker> threads) {
		this.threads = threads;
	}
	
	/**
	 * Gets the root.
	 * @return the root
	 */
	public File getRoot() {
		return root;
	}
	/**
	 * Sets the root.
	 * @param root the root to set
	 */
	public void setRoot(File root) {
		this.root = root;
	}
	/**
	 * Sets the root.
	 * @param rootDirectory the new root
	 */
	public void setRoot(String rootDirectory) {
		this.root = new File(rootDirectory);
	}
	
	/**
	 * Gets the port.
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * Sets the port.
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * Gets the timeout.
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}
	/**
	 * Sets the timeout.
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * Gets the workers.
	 * @return the workers
	 */
	public int getWorkers() {
		return workers;
	}
	/**
	 * Sets the workers.
	 * @param workers the workers to set
	 */
	public void setWorkers(int workers) {
		this.workers = workers;
	}

}
