package application;

import javax.swing.plaf.basic.BasicInternalFrameUI;

import gui.ProjectWindow;

public class Project {

	public String ProjectFolder;
	
	
	//static ProjectWindow CurrProject;
	static ProjectWindow CurrProject;
	
	public Project() {
		
	};
	
	public static void addnew() {
		// --- Anlegen eines neuen Projekts -------
		System.out.println("Neues Projekt ");		
		Application.MainWindow.setStatusBar("Neues Projekt ");	
	};
	
	public static void open() {
		// --- Öffnen eines neuen Pojekts ---------
		// --- Projekt öffnen -------------------------- 
		System.out.println("Projekt öffnen ...");
		Application.MainWindow.setStatusBar("Projekt öffnen ... ");
		
		//CurrProject = new ProjectWindow();
		CurrProject = new ProjectWindow();
		CurrProject.setTitle("Project: ");
		CurrProject.setClosable(true);
		CurrProject.setMaximizable(true);
		CurrProject.setResizable(true);
		CurrProject.setAutoscrolls(true);
		
		CurrProject.setBorder(null);
		((BasicInternalFrameUI) CurrProject.getUI()).setNorthPane(null);
        
        
		Application.MainWindow.ProjectDesktop.add(CurrProject);		
		Application.MainWindow.ProjectDesktop.getDesktopManager().maximizeFrame(CurrProject);
		Application.MainWindow.setStatusBar("Projekt öffnen ... ");
		CurrProject.setVisible(true);
		

	}

	public static void save() {
		// --- Speichern des aktuellen Pojekts ----
		System.out.println("Projekt speichern ...");
		Application.MainWindow.setStatusBar("Projekt speichern ... ");
		
	}
	
	public static void close() {
		// --- Anlegen eines neuen Projekts -------
		System.out.println("Projekt schliessen ...");
		Application.MainWindow.setStatusBar("Projekt schliessen ... ");
	}

	
	
}
