package application;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ProjectsLoaded {

	// --- Listing of the open projects -------------------------------
	private ArrayList<Project> ProjectsOpen = new ArrayList<Project>();
	
	/**
	 * Adding a new Project to the Application
	 * @param Project2Add
	 * @return
	 */
	public Project add ( Project Project2Add ) {
		ProjectsOpen.add( Project2Add );
		return Project2Add;
	}

	/**
	 * Returns the Project-Object
	 * @param String ProjectName
	 * @return
	 */
	public Project get ( String ProjectName ) {
		int Index = getIndexByName( ProjectName );
		return get(Index);
	}
	/**
	 * Returns the Project-Object
	 * @param int Index
	 * @return
	 */
	public Project get( int Index ) {
		return ProjectsOpen.get(Index);
	}

	/**
	 * Removes a single Project
	 * @param String Project2Remove
	 */
	public void remove( Project Project2Remove ) {
		int Index = getIndexByName( Project2Remove.getProjectName() );		
		ProjectsOpen.remove(Index);
	}
	/**
	 * Removes all Projects from the (Array) ProjectList
	 */
	public void removeAll( ) {
		ProjectsOpen.clear();
		Application.ProjectCurr = null;
		Application.Projects.setProjectMenuItems();		
	}

	/**
	 * Identifies a Project by his name and returns the Array-/Window-Index
	 * @param ProjectName
	 * @return
	 */
	public int getIndexByName ( String ProjectName ) {
		int Index = -1;
		for(int i=0; i<this.count(); i++) {
			if( ProjectsOpen.get(i).getProjectName().equalsIgnoreCase(ProjectName) ) {
				Index = i;
				break;
			}	
		}
		return Index;
	}

	/**
	 * Counts the actual open projects
	 */
	public int count() {
		return ProjectsOpen.size();		
	}

	/**
	 * Create's the Window=>MenuItems depending on the open projects 
	 */
	public void setProjectMenuItems() {
		
		boolean SetFontBold = true;
		
		JMenu WindowMenu = Application.MainWindow.jMenuMainWindow;
		WindowMenu.removeAll();
		if (this.count() == 0 ){
			WindowMenu.add( new JMenuItmen_Window( Language.translate("Kein Projekt geöffnet !"), -1, SetFontBold ) );
		}
		else {
			for(int i=0; i<this.count(); i++) {
				String ProjectName = ProjectsOpen.get(i).getProjectName();
				if ( ProjectName.equalsIgnoreCase( Application.ProjectCurr.getProjectName() ) ) 
					SetFontBold = true;
				else 
					SetFontBold = false;
				WindowMenu.add( new JMenuItmen_Window( ProjectName, i, SetFontBold) );
			}		
		}
	}	
	
	/**
	 * Creates a single MenueItem for the Window-Menu depending on the open projects  
	 * @author derksen
	 */
	// --- Unterklasse für die Menüelemente "Fenster" => Projekte --------
	private class JMenuItmen_Window extends JMenuItem  {
 
		private static final long serialVersionUID = 1L;
		
		private JMenuItmen_Window( String ProjectName, int WindowIndex, boolean setBold  ) {
			
			final int WinIdx = WindowIndex;
			int WinNo = WindowIndex + 1;
			
			if ( WinNo <= 0 ) {
				this.setText( ProjectName );
			}
			else {
				this.setText( WinNo + ": " + ProjectName );
			}		
			if ( setBold ) {
				Font cfont = this.getFont();
				if ( cfont.isBold() == true ) {
					this.setForeground( Application.RunInfo.ColorMenuHighLight() );	
				}
				else {
					this.setFont( cfont.deriveFont(Font.BOLD) );
				}
			}
			this.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Application.Projects.setFocus( WinIdx );							
				}
			});		
		}
	}
	private void setFocus( int Index ) {
		this.get(Index).setFocus();		
	}


		
}
