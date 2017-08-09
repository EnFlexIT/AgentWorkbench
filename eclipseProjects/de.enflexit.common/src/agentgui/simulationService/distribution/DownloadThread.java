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
package agentgui.simulationService.distribution;

/**
 * The Class DownloadThread uses the class {@link Download} to ensure 
 * a decoupled download of specified files.
 * 
 * @see Download
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DownloadThread extends Thread {

	private String srcFileURL;
	private String destFileLocal;
	private Download download = null;
	
	/**
	 * Instantiates a new download thread.
	 *
	 * @param sourceFileURL the source file url
	 * @param destinationFileLocal the destination file local
	 */
	public DownloadThread(String sourceFileURL, String destinationFileLocal) {
		this.setName("DownloadThread");
		this.srcFileURL  = sourceFileURL;
		this.destFileLocal = destinationFileLocal;
		this.download = new Download(this.srcFileURL, destFileLocal);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		this.download.startDownload(); 
	}
	
	/**
	 * Cancels the currently running download.
	 */
	public synchronized void doCancel() {
		this.download.doCancel();
	}
	
	/**
	 * Informs about a finished download.
	 * @return true, if the download is finished
	 */
	public boolean isFinished() {
		return this.download.isFinished();
	}

	/**
	 * Informs if the download was successful.
	 * @return true, if the download was successful 
	 */
	public boolean wasSuccessful() {
		return this.download.wasSuccessful();
	}

	/**
	 * Informs about the current download progress.
	 * @return the downloadProgress
	 */
	public Integer getDownloadProgress() {
		return this.download.getDownloadProgress();
	}

}
