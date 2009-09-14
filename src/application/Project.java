package application;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import gui.ProjectWindow;

public class Project extends Object{

	private String NewLine = Application.RunInfo.AppNewLineString();
	
	public ProjectWindow ProjectGUI;
	public boolean ProjectUnsaved = false;
	
	public String ProjectFolder;
	public String ProjectName;
	public String ProjectDescription;
	
	
	public void addnew() {
		// --- Anlegen eines neuen Projekts ---------------
		String ProjectNamePrefix = Language.translate("Neues Projekt");
		Application.MainWindow.setStatusBar(ProjectNamePrefix + " ...");

		// --- Neuen, allgemeinen Projektnamen finden -----		
		String ProjectNameTest = ProjectNamePrefix;
		int Index = Application.Projects.getIndexByName(ProjectNameTest);
		int i = 2;
		while ( Index != -1 ) {
			ProjectNameTest = ProjectNamePrefix + " " + i;
			Index = Application.Projects.getIndexByName( ProjectNameTest );
			i++;
		}
		ProjectName = ProjectNameTest;
		
		// --- Entprechendes GUI öffnen -------------------
		ProjectGUI = new ProjectWindow();		
		ProjectGUI.ProjectTitel.setText(ProjectName);
		
		// --- Objekt an die Projektauflistung hängen -----
		Application.Projects.add( this );
		Application.ProjectCurr = this;
		Application.Projects.setProjectMenuItems();
		Application.MainWindow.setCloseButtonPosition( true );

		// --- Anzeige anpassen ---------------------------
		setMaximized();
		Application.setTitelAddition( ProjectName );
		Application.setStatusBar( "" );		
	};
	
	public void open() {
		// --- Öffnen eines neuen Pojekts -----------------
		JFileChooser fc = new JFileChooser( Application.RunInfo.PathProjects(true) );
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);		 
		fc.setDialogTitle( Language.translate("Projekt öffnen") );
		if ( fc.showOpenDialog( Application.MainWindow ) != JFileChooser.APPROVE_OPTION ){
			return;
		}
		String SelectedFile = fc.getSelectedFile().getName();
	    System.out.println( SelectedFile );

	    
	    
		ProjectGUI = new ProjectWindow();
		setMaximized();
		//Application.Projects.add();

	}

	public void save() {
		// --- Speichern des aktuellen Pojekts ----
		Application.MainWindow.setStatusBar("Projekt speichern ... ");
		
	}
	
	public void close() {
		// --- Projekt schliessen ? -----------------------
		String MsgHead = null;
		String MsgText = null;

		Application.MainWindow.setStatusBar(Language.translate("Projekt schliessen") + " ...");
		if ( ProjectUnsaved ) {
			MsgHead = Language.translate("Projekt '@' speichern?");
			MsgHead = MsgHead.replace( "'@'", "'" + ProjectName + "'");
			
			MsgText = Language.translate(
						"Das aktuelle Projekt '@' ist noch nicht gespeichert!" + NewLine + 
						"Möchten Sie es nun speichern ?");
			MsgText = MsgText.replace( "'@'", "'" + ProjectName + "'");
			
			Integer MsgAnswer = JOptionPane.showInternalConfirmDialog( Application.MainWindow.getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
			if ( MsgAnswer == 0 ) 
				save();
		}
		// --- Projekt kann geschlossen werden ------------
		int Index = Application.Projects.getIndexByName( ProjectName );
		
		ProjectGUI.dispose();
		Application.Projects.remove(this);
		
		int NProjects = Application.Projects.count();
		if ( NProjects > 0 ) {
			if ( Index+1 > NProjects ) Index = NProjects-1;  
			Application.ProjectCurr = Application.Projects.get(Index);
			Application.ProjectCurr.setFocus();
			Application.setTitelAddition( Application.ProjectCurr.ProjectName );
		}
		else {
			Application.ProjectCurr = null;
			Application.Projects.setProjectMenuItems();
			Application.MainWindow.setCloseButtonPosition( false );
			Application.setTitelAddition( "" );
		}
		Application.setStatusBar( "" );
	}
	/**
	 * Moves the requested Projectwindow to the front
	 */
	public void setFocus() {
		ProjectGUI.moveToFront();
		Application.setTitelAddition( ProjectName );
		Application.ProjectCurr = this;
		Application.Projects.setProjectMenuItems();
		setMaximized();
	}

	public void setMaximized() {
		((BasicInternalFrameUI) Application.ProjectCurr.ProjectGUI.getUI()).setNorthPane(null);
		Application.MainWindow.ProjectDesktop.getDesktopManager().maximizeFrame( ProjectGUI );		
	}
	
	
}
