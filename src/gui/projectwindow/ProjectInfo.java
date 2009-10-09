package gui.projectwindow;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import application.Application;
import application.Project;
import javax.swing.JComboBox;

/**
 * @author: Christian Derksen
 *
 */
public class ProjectInfo extends JScrollPane implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private Project CurrProject;
	
	private JTextField ProjectName = null;
	private DocumentListener ProjectNameDocumentListener;  //  @jve:decl-index=0:
	private JTextArea ProjectDescription = null;
	private JTextField ProjectFolder = null;
	
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JScrollPane jScrollPane = null;

	private JComboBox ProjectFolderList = null;  //  @jve:decl-index=0:visual-constraint="724,41"

	/**
	 * This is the default constructor
	 */
	public ProjectInfo( Project CP ) {
		super();
		this.CurrProject = CP;
		this.CurrProject.addObserver(this);		
		initialize();	
	}
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setSize(699, 326);
		this.setAutoscrolls(true);		
		this.setBorder(null);
		this.setFocusable(true);
		this.setLayout(null);		
		this.setName("ProjectBaseInfo");
		
		jLabel1 = new JLabel();
		jLabel1.setBounds(new Rectangle(15, 15, 156, 19));
		jLabel1.setFont(new Font("Dialog", Font.BOLD, 14));
		jLabel1.setText("Projekttitel");
		jLabel2 = new JLabel();
		jLabel2.setBounds(new Rectangle(15, 45, 156, 21));
		jLabel2.setFont(new Font("Dialog", Font.BOLD, 14));
		jLabel2.setText("Projektbeschreibung");
		jLabel = new JLabel();
		jLabel.setBounds(new Rectangle(15, 285, 156, 21));
		jLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		jLabel.setText("Basisverzeichnis");
		
		this.add(jLabel, null);
		this.add(jLabel1, null);
		this.add(jLabel2, null);
		this.add( getProjectTitel() );
		
		if ( Application.RunInfo.AppExecutedOver().equalsIgnoreCase("IDE") ) {
			// ------------------------------------------------------------
			// --- Entwicklungsumgebung -----------------------------------
			// ------------------------------------------------------------
			// => Auswahlfeld auf die einzelnen Verzeichnis aus PathProjects
			this.add( getProjectFolderList() );	
		}
		else if ( Application.RunInfo.AppExecutedOver().equalsIgnoreCase("Executable") ) {
			// ------------------------------------------------------------
			// --- Executable jar-File ------------------------------------
			// ------------------------------------------------------------
			// => Verzeichnisangabe des gewählten Projekts anzeigen
			this.add( getProjectFolder() );		
		}
		this.add( getJScrollPane() );
		this.setVisible(true);

	}

	/**
	 * This method initializes ProjectTitel	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getProjectTitel() {
		if (ProjectName == null) {
			ProjectName = new JTextField();
			ProjectName.setBounds(new Rectangle(180, 15, 480, 26));
			ProjectName.setName("ProjectTitel");
			ProjectName.setFont(new Font("Dialog", Font.PLAIN, 12));
			ProjectNameDocumentListener = new DocumentListener() {
				public void removeUpdate(DocumentEvent e) {
					CurrProject.setProjectName( ProjectName.getText() );
				}
				public void insertUpdate(DocumentEvent e) {
					CurrProject.setProjectName( ProjectName.getText() );
				}
				public void changedUpdate(DocumentEvent e) {
					CurrProject.setProjectName( ProjectName.getText() );
				}
			};
			ProjectName.getDocument().addDocumentListener(ProjectNameDocumentListener);
		}
		return ProjectName;
	}

	/**
	 * This method initializes ProjectDescription	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getProjectDescription() {
		if (ProjectDescription == null) {
			ProjectDescription = new JTextArea();
			ProjectDescription.setName("ProjectDescription");
			ProjectDescription.setColumns(0);
			ProjectDescription.setLineWrap(true);
			ProjectDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
			ProjectDescription.getDocument().addDocumentListener( new DocumentListener() {
				public void removeUpdate(DocumentEvent e) {
					CurrProject.setProjectDescription( ProjectDescription.getText() );
				}
				public void insertUpdate(DocumentEvent e) {
					CurrProject.setProjectDescription( ProjectDescription.getText() );
				}
				public void changedUpdate(DocumentEvent e) {
					CurrProject.setProjectDescription( ProjectDescription.getText() );
				}
			});
		}
		return ProjectDescription;
	}

	/**
	 * This method initializes ProjectFolder	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getProjectFolder() {
		if (ProjectFolder == null) {
			ProjectFolder = new JTextField();
			ProjectFolder.setBounds(new Rectangle(180, 285, 480, 26));
			ProjectFolder.setText("");
			ProjectFolder.setName("ProjectFolder");
			ProjectFolder.setFont(new Font("Dialog", Font.PLAIN, 12));
			ProjectFolder.setEditable( false );
		}
		return ProjectFolder;
	}

	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getProjectFolderList() {
		if (ProjectFolderList == null) {
			ProjectFolderList = new JComboBox();
			ProjectFolderList.setName("ProjectFolder");
			ProjectFolderList.setBounds(new Rectangle(180, 285, 480, 26));
			ProjectFolderList.setFont(new Font("Dialog", Font.PLAIN, 12));
			ProjectFolderList.setEditable( false );
			String[] IDEProjects = Application.RunInfo.getIDEProjects();
			for ( String Pro : IDEProjects ) {
				ProjectFolderList.addItem( Pro );
			}		
		}
		return ProjectFolderList;
	}

	
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(180, 45, 480, 221));
			jScrollPane.setViewportView(getProjectDescription());
		}
		return jScrollPane;
	}

	@Override
	/**
	 * Get the notyfication of the ObjectModel
	 */
	public void update(Observable arg0, Object OName) {
		
		CurrProject.ProjectUnsaved = true;
		String ObjectName = OName.toString();
		
		if ( ObjectName.equalsIgnoreCase( "ProjectName" ) ) {
			if ( ProjectName.isFocusOwner() == false ) {
				// --- DocumentListener kurzzeitig entfernen, da sonst bei --- 
				// --- Initialisierung eines Projektes eine Endlosschleife ---
				// --- entsteht ...										   ---
				ProjectName.getDocument().removeDocumentListener(ProjectNameDocumentListener);
				ProjectName.setText( CurrProject.getProjectName() );				
				ProjectName.getDocument().addDocumentListener(ProjectNameDocumentListener);
			}								
		}			
		else if ( ObjectName.equalsIgnoreCase( "ProjectDescription" ) ) {
			if ( ProjectDescription.isFocusOwner() == false ) 
				ProjectDescription.setText( CurrProject.getProjectDescription() );
		}			
		else {
			System.out.println("Unbekannter Updatebefehl vom Observerable ...");
		};
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// --- Das ActionCommand und den Auslöser des Events ermitteln ---
		String ActCMD = ae.getActionCommand();
		Object Trigger = ae.getSource();
		System.out.println( "ActCMD/Wert => " + ActCMD );
		System.out.println( "Auslöser => " + Trigger );

		// --- Fallunterscheidung 'Auslöser' -----------------------------
		if ( Trigger == ProjectName ) {
			CurrProject.setProjectName( ae.getActionCommand() );
		}
		else if ( Trigger == ProjectFolder ) {
			CurrProject.setProjectFolder( ae.getActionCommand() );
		}
		else if ( Trigger == ProjectDescription ) {
			CurrProject.setProjectDescription( ae.getActionCommand() );
		}
		else {
			
		};
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
