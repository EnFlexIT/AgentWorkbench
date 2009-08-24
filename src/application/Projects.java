package application;

import gui.ProjectWindow;

public class Projects {

	static ProjectWindow CurrProject;
	
	public Projects() {
		
	};
	
	public static void addnew() {
		// --- Anlegen eines neuen Projekts -------
		System.out.println("Neues Projekt ");		
		Application.MainWindow.setStatusBar("Neues Projekt ");	
	};
	
	public static void open() {
		// --- �ffnen eines neuen Pojekts ---------
		// --- Projekt �ffnen -------------------------- 
		System.out.println("Projekt �ffnen ...");
		Application.MainWindow.setStatusBar("Projekt �ffnen ... ");
		
		CurrProject = new ProjectWindow();
		CurrProject.setTitle("Project: ");
		//CurrProject.setBorder(new SoftBevelBorder( SoftBevelBorder.LOWERED, null, null, null, null ) );
		CurrProject.setAutoscrolls(true);
		
		//((BasicInternalFrameUI) CurrProject.getUI()).setNorthPane(null);
		
		Application.MainWindow.ProjectDesktop.add(CurrProject);		
		Application.MainWindow.ProjectDesktop.getDesktopManager().maximizeFrame(CurrProject);
		Application.MainWindow.setStatusBar("Projekt �ffnen ... ");
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
