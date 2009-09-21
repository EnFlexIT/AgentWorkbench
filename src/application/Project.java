package application;

import java.io.FileWriter;
import java.io.Writer;
import java.util.Observable;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import gui.ProjectWindow;

@XmlRootElement public class Project extends Observable {

	// --- Konstanten ------------------------------------------
	private static String NewLine = Application.RunInfo.AppNewLineString();
	
	// --- GUI der aktuellen Projekt-Instanz -------------------
	@XmlTransient public ProjectWindow ProjectGUI = new ProjectWindow(this);
	
	// --- Objekt- / Projektvariablen --------------------------
	@XmlTransient public boolean ProjectUnsaved = false;
	
	// --- Speichervariablen der Projektdatei ------------------ 
	private String ProjectFolder;
	private String ProjectName;
	private String ProjectDescription;
	
	
	/**
	 * Creating a new MAS-Project
	 */
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
		// --- Projektnamen für diese Instanz festlegen --- 
		setProjectName( ProjectNameTest );
		ProjectUnsaved = false;
		
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
	/**
	 * Open an existing MAS-Project
	 */
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

	    
		this.setMaximized();
		//Application.Projects.add();

	}
	/**
	 * Save the current MAS-Project
	 */
	public boolean save() {
		// --- Speichern des aktuellen Pojekts ------------
		Application.MainWindow.setStatusBar( ProjectName + ": " + Language.translate("speichern") + " ... ");
		
		if ( ProjectFolder == null) {
			JFileChooser fc = new JFileChooser( Application.RunInfo.PathProjects(true) );
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setMultiSelectionEnabled(false);		 
			fc.setDialogTitle( Language.translate("Projekt öffnen") );
			if ( fc.showOpenDialog( Application.MainWindow ) != JFileChooser.APPROVE_OPTION ){
				return false;
			}
		}
		
		
		try {			
			// --- Kontext und Marshaller vorbereiten 
			JAXBContext pc = JAXBContext.newInstance( getClass() ); 
			Marshaller pm = pc.createMarshaller(); 
			pm.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE ); 
			pm.marshal( this, System.out );
			// --- Objektwerte in xml-Datei schreiben -----
			Writer pw = new FileWriter( "club-jaxb.xml" );
			pm.marshal( this, pw );
			
		} 
		catch (Exception e) {
			System.out.println("XML - Fehler !");
			e.printStackTrace();
		}
		Application.MainWindow.setStatusBar("");
		return true;		
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
			
			Integer MsgAnswer = JOptionPane.showInternalConfirmDialog ( 
					Application.MainWindow.getContentPane(), 
					MsgText, MsgHead, JOptionPane.YES_NO_CANCEL_OPTION );
			if ( MsgAnswer == JOptionPane.CANCEL_OPTION ) {
				return;
			}
			else if ( MsgAnswer == JOptionPane.YES_OPTION ) {
				if ( save()== false ) return;
			}
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

	


	/**
	 * @param projectFolder the projectName to set
	 */
	public void setProjectName(String projectName) {
		ProjectName = projectName;
		setChanged();
		notifyObservers( "ProjectName" );
	}
	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return ProjectName;
	}
	
	/**
	 * @param projectDescription the projectDescription to set
	 */
	public void setProjectDescription(String projectDescription) {
		ProjectDescription = projectDescription;
		setChanged();
		notifyObservers( "ProjectDescription" );
	}
	/**
	 * @return the projectDescription
	 */
	public String getProjectDescription() {
		return ProjectDescription;
	}
	
	/**
	 * @param projectFolder the projectFolder to set
	 */
	public void setProjectFolder(String projectFolder) {
		ProjectFolder = projectFolder;
		setChanged();
		notifyObservers( "ProjectFolder" );
	}
	/**
	 * @return the projectFolder
	 */
	public String getProjectFolder() {
		return ProjectFolder;
	}
	
}
