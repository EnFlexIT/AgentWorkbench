/**
 * @author Christian Derksen - DAWIS - ICB - University Duisburg-Essen
 * Copyright 2010 Christian Derksen
 * 
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this file. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package contmas.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;

public class AgentDesktop{

	private JDesktopPane DP=null;
	private String ProjectFolder=null;
	private String FrameTitle=null;
	private Boolean standaloneMode=null;
	public static final Boolean AGENTDESKTOP_AGENTGUI=false;
	public static final Boolean AGENTDESKTOP_STANDALONE=true;

	private static JDesktopPane getDesktopPaneInJFrame(String FrameTitle){

		JFrame Frame=new JFrame();
		Frame.setTitle(FrameTitle);
		JDesktopPane DP=new JDesktopPane();
		Frame.add(DP);
		Frame.setSize(300,300);

		Frame.setVisible(true);

		Frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		return DP;
	}

	public AgentDesktop(){
		this(null);
	}

	public AgentDesktop(String FrameTitle){
		String ClassRef=this.getClass().getName();
		int FolderCut=ClassRef.indexOf(".");
		this.ProjectFolder=ClassRef.substring(0,FolderCut);
		this.FrameTitle=FrameTitle;
		if(this.FrameTitle == null){
			this.FrameTitle=this.ProjectFolder;
		}
		try{
			// ------------------------------------------------------
			// --- Klasse gefunden => innerhalb von Agent GUI -------
			// Application.Projects.get(CurrProject.getProjectFolder()).ProjectDesktop
			Class<?> Clazz=Class.forName("application.Application");
			try{
				Object projects=Clazz.getField("Projects").get(null);
				Method getMethod=projects.getClass().getMethod("get",new Class[] {String.class});

				Object ret=getMethod.invoke(projects,new Object[] {this.ProjectFolder});
				this.DP=(JDesktopPane) ret.getClass().getField("ProjectDesktop").get(ret);
//				System.out.println(this.FrameTitle + ": AgentGUI Environment found");
				this.standaloneMode=AgentDesktop.AGENTDESKTOP_AGENTGUI;

			}catch(Exception ErrInv){
				// ------------------------------------------------------
				// --- Methodenaufruf fehlgeschlagen => ausserhalb von AgentGUI -
//				System.out.println(this.FrameTitle + ": Standalone");
				this.DP=AgentDesktop.getDesktopPaneInJFrame(this.FrameTitle);
				this.standaloneMode=AgentDesktop.AGENTDESKTOP_STANDALONE;
			}

		}catch(ClassNotFoundException ErrDP){
			// ------------------------------------------------------
			// --- Klasse nicht gefunden => ausserhalb von AgentGUI -
			// ErrDP.printStackTrace();
//			System.out.println(this.FrameTitle + ": Standalone");
			this.DP=AgentDesktop.getDesktopPaneInJFrame(this.FrameTitle);
			this.standaloneMode=AgentDesktop.AGENTDESKTOP_STANDALONE;
		}
	}

	public JDesktopPane getDesktopPane(){
		return this.DP;
	}

	public Boolean getStandaloneMode(){
		return this.standaloneMode;
	}
}
