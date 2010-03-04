package contmas.main;
import java.awt.Font;
import java.lang.reflect.Method;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;

public class AgentDesktop {
	
	private JDesktopPane DP = null;
	private String ProjectFolder = null;
	
	public AgentDesktop() {
		this.setDesktopPane();		
	}
	public JDesktopPane getDesktopPane() {
		return DP;
	}
	public void setDesktopPane() {
		
		String ClassRef = this.getClass().getName();
		int FolderCut = ClassRef.indexOf(".");
		ProjectFolder = ClassRef.substring(0, FolderCut);
		try {
			// ------------------------------------------------------
			// --- Klasse gefunden => innerhalb von Agent GUI -------
			// Application.Projects.get(CurrProject.getProjectFolder()).ProjectDesktop
			Class<?> Clazz = Class.forName("application.Application");
			try {
				Object projects = Clazz.getField("Projects").get(null);
				Method getMethod = projects.getClass().getMethod("get", new Class[] {String.class});
				
				Object ret = getMethod.invoke(projects, new Object[] { ProjectFolder });
				DP = (JDesktopPane) ret.getClass().getField("ProjectDesktop").get(ret);
				System.out.println( ProjectFolder + ": AgentGUI Environment found" );

			} catch (Exception ErrInv) {
				// ------------------------------------------------------
				// --- Methodenaufruf fehlgeschlagen => ausserhalb von AgentGUI -
				System.out.println( ProjectFolder + ": Standalone" );
				getDesktopPaneInJFrame();
			}
			
		} catch (ClassNotFoundException ErrDP) {
			// ------------------------------------------------------
			// --- Klasse nicht gefunden => ausserhalb von AgentGUI -
			// ErrDP.printStackTrace();
			System.out.println( ProjectFolder + ": Standalone" );
			getDesktopPaneInJFrame();
		}		
	}
	

	private void getDesktopPaneInJFrame() {

		DP = new JDesktopPane();
		
		JFrame Frame = new JFrame();
		Frame.setFont(new Font("Dialog",Font.PLAIN,12));
		Frame.setTitle(ProjectFolder);
		Frame.add(DP);
		Frame.setSize(315,322);
		Frame.setVisible(true);
	}

}
