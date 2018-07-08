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
import agentgui.core.config.GlobalInfo.ExecutionEnvironment;

/**
 * The Class MirrorTools can be used to mirror p2 or project repositories.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class MirrorTools {

	/**
	 * The P2DownloadType.
	 */
	public enum P2DownloadType {
		MetaData ("org.eclipse.equinox.p2.metadata.repository.mirrorApplication"),
		Artifact("org.eclipse.equinox.p2.artifact.repository.mirrorApplication");
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
	protected static void mirrorP2RepositoryDebug() {
		MirrorTools.mirrorP2Repository(P2DownloadType.Artifact, debugP2Source, debugP2Destin);
	}
	
	/**
	 * Mirrors the specified P2 repositories metadata or artifacts to the destination directory.
	 *
	 * @param downloadType the download type
	 * @param sourceLocation the source location
	 * @param destinationDirectory the destination directory
	 */
	public static void mirrorP2Repository(P2DownloadType downloadType, String sourceLocation, String destinationDirectory) {
		
		File execFile = Application.getGlobalInfo().getEclipseLauncher();
		if (execFile==null) return;
		
		String executable = execFile.getAbsolutePath();
		if (debugP2Mirroring==true) System.out.println("=> Exec: " + executable);
		String execute = executable + " -nosplash -verbose -application " + downloadType.toString() + " -source " + sourceLocation + " -destination \"" + destinationDirectory + "\""; 
		if (debugP2Mirroring==true) System.out.println("=> cmdLine: " + execute);
		
		// --------------------------------------
		Scanner in = null;
		Scanner err = null;
		try {
			
			String[] arg = execute.split(" ");
			ProcessBuilder proBui = new ProcessBuilder(arg);
			proBui.redirectErrorStream(true);
			proBui.directory(new File(Application.getGlobalInfo().getPathBaseDir()));
			
			Process process = proBui.start();
			
			in = new Scanner(process.getInputStream());
			in.useDelimiter("\\Z");
			
			err = new Scanner(process.getErrorStream());
			err.useDelimiter("\\Z");
			
			while (in.hasNextLine() || err.hasNextLine() ) {
				if (in.hasNextLine() && debugP2Mirroring==true) {
					System.out.println("[MirrorProcess]: " + in.nextLine());	
				}
				if (err.hasNextLine() && debugP2Mirroring==true){
					System.err.println("[MirrorProcess]: " + err.nextLine());	
				}
			}
			System.out.println("Finalized P2-Mirroring Process");
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in!=null) in.close();
			if (err!=null) err.close();	
		}
		
	}
	
}
