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
package agentgui.core.update;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import agentgui.core.application.Application;

/**
 * The Class MirrorTools can be used to mirror p2 or project repositories.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class MirrorTool implements MirrorToolListener {

	public enum MirrorToolsJob {
		MirrorP2MetaData,
		MirrorP2Artifact,
		MirrorProjectRepository		
	}
	
	public enum P2DownloadType {
		MetaData ("org.eclipse.equinox.p2.metadata.repository.mirrorApplication"),
		Artifacts("org.eclipse.equinox.p2.artifact.repository.mirrorApplication");
		private final String appID;
		private P2DownloadType(final String appID) {
			this.appID = appID;
		}
		@Override
		public String toString() {
			return this.appID;
		}
	}
	
	private static boolean debugP2Mirroring = false;
	private static String debugP2Source = "http://p2.enflex.it/awb/2.1";
	private static String debugP2Destin = "D:\\00 Tmp\\01 AWB P2 Mirror";
	
	
	
	/**
	 * Mirror a P2 repository for debug purposes.
	 */
	protected static void mirrorDebugger() {
		MirrorTool mt = new MirrorTool();
		MirrorTool.mirrorP2Repository(P2DownloadType.MetaData, debugP2Source, debugP2Destin, mt);
		MirrorTool.mirrorP2Repository(P2DownloadType.Artifacts, debugP2Source, debugP2Destin, mt);
	}
	/* (non-Javadoc)
	 * @see agentgui.core.update.MirrorToolListener#onMirroringFinaliized(agentgui.core.update.MirrorTool.MirrorToolsJob, boolean)
	 */
	@Override
	public void onMirroringFinaliized(MirrorToolsJob job, boolean successful) {
		System.out.println("[" + this.getClass().getSimpleName() + "] Job finalized: " + job.toString() + ", Successful: " + successful);
	}
	
	
	/**
	 * Mirrors the specified P2 repositories metadata or artifacts to the destination directory.
	 *
	 * @param downloadType the download type
	 * @param sourceLocation the source location
	 * @param destinationDirectory the destination directory
	 */
	public static void mirrorP2Repository(final P2DownloadType downloadType, final String sourceLocation, final String destinationDirectory, final MirrorToolListener listener) {
		
		if (downloadType==null) throw new NullPointerException("No specified download type for the p2 mirroring.");
		if (sourceLocation==null) throw new NullPointerException("No specified source location for the p2 mirroring.");
		if (destinationDirectory==null) throw new NullPointerException("No specified destination directory for the p2 mirroring.");
		
		Thread mirrorThread = new Thread(new Runnable() {
			@Override
			public void run() {

				// --- Define return values -----------------------------------
				boolean successful = false;
				MirrorToolsJob job = null;
				switch (downloadType) {
				case Artifacts:
					job = MirrorToolsJob.MirrorP2Artifact;
					break;
				case MetaData:
					job = MirrorToolsJob.MirrorP2MetaData;
					break;
				}
				
				// --- Get the platform executable ----------------------------
				File execFile = Application.getGlobalInfo().getEclipseLauncher();
				if (execFile!=null) {
					// --- Define the command line to execute the process -----
					String executable = execFile.getAbsolutePath();
					String execute = executable + " -nosplash -verbose -application " + downloadType.toString() + " -source " + sourceLocation + " -destination \"" + destinationDirectory + "\""; 
					if (debugP2Mirroring==true) System.out.println("=> cmdLine: " + execute);
					
					// --- Define and execute the process ---------------------
					Scanner in = null;
					Scanner err = null;
					try {
						
						String[] arguments = execute.split(" ");
						ProcessBuilder proBui = new ProcessBuilder(arguments);
						proBui.redirectErrorStream(true);
						proBui.directory(new File(Application.getGlobalInfo().getPathBaseDir()));
						
						Process process = proBui.start();
						
						in = new Scanner(process.getInputStream());
						in.useDelimiter("\\Z");
						
						err = new Scanner(process.getErrorStream());
						err.useDelimiter("\\Z");
						
						while (in.hasNextLine() || err.hasNextLine() ) {
							if (in.hasNextLine()) {
								String inLine = in.nextLine();
								if (debugP2Mirroring==true)  System.out.println("[MirrorProcess]: " + inLine);	
							}
							if (err.hasNextLine()){
								String errLine = err.nextLine();
								if (debugP2Mirroring==true) System.err.println("[MirrorProcess Error]: " + errLine);	
							}
						}
						successful = true;
						
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (in!=null) in.close();
						if (err!=null) err.close();	
					}				
				}

				// --- Inform listener about the result of the job ------------
				if (listener!=null) {
					listener.onMirroringFinaliized(job, successful);
				}
				if (debugP2Mirroring==true) System.out.println("Finalized P2-Mirroring Job: " + job.toString() + ", Successful: " + successful);
				
			}
		});
		
		// --- Name and start the thread --------------------------------------
		mirrorThread.setName("Mirror " + downloadType.name() + " " + sourceLocation);
		mirrorThread.start();
	}

	
}
