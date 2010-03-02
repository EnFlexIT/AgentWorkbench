package gui.projectwindow;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import application.Language;
import application.Project;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class ProjectInfo extends JPanel implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;

	private Project CurrProject;
	
	private JTextField ProjectName = null;
	private DocumentListener ProjectNameDocumentListener;  //  @jve:decl-index=0:
	private JLabel TitleProject = null;
	private JScrollPane jScrollPane = null;
	private JTextArea ProjectDescription = null;
	private JTextField ProjectFolder = null;
	private JLabel TitleDescription = null;
	private JLabel TitleFolder = null;

	/**
	 * This is the default constructor
	 */
	public ProjectInfo( Project CP ) {
		super();
		this.CurrProject = CP;
		this.CurrProject.addObserver(this);		
		initialize();
		
		// --- Sprachspezifische Einstellungen -----------------
		TitleProject.setText( Language.translate("Projekttitel") );
		TitleDescription.setText( Language.translate("Beschreibung") );
		TitleFolder.setText( Language.translate("Verzeichnis") );
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 0;
		gridBagConstraints5.insets = new Insets(0, 10, 10, 5);
		gridBagConstraints5.anchor = GridBagConstraints.WEST;
		gridBagConstraints5.gridy = 2;
		TitleFolder = new JLabel();
		TitleFolder.setText("JLabel");
		TitleFolder.setFont(new Font("Dialog", Font.BOLD, 14));
		TitleFolder.setText( Language.translate("Beschreibung") );
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints4.insets = new Insets(2, 10, 0, 5);
		gridBagConstraints4.gridy = 1;
		TitleDescription = new JLabel();
		TitleDescription.setText("JLabel");
		TitleDescription.setFont(new Font("Dialog", Font.BOLD, 14));
		TitleDescription.setText( Language.translate("Beschreibung") );		
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.gridy = 2;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.gridwidth = 2;
		gridBagConstraints3.insets = new Insets(0, 0, 10, 10);
		gridBagConstraints3.gridx = 2;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.weighty = 1.0;
		gridBagConstraints2.insets = new Insets(0, 0, 10, 10);
		gridBagConstraints2.gridx = 2;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.insets = new Insets(0, 10, 0, 5);
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.gridy = 0;
		TitleProject = new JLabel();
		TitleProject.setText("JLabel");
		TitleProject.setFont(new Font("Dialog", Font.BOLD, 14));
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 2;
		gridBagConstraints.insets = new Insets(10, 0, 10, 10);
		gridBagConstraints.weightx = 1.0;
		this.setSize(557, 329);
		this.setLayout(new GridBagLayout());
		this.add(getProjectName(), gridBagConstraints);
		this.add(TitleProject, gridBagConstraints1);
		this.add(getJScrollPane(), gridBagConstraints2);
		this.add(getProjectFolder(), gridBagConstraints3);
		this.add(TitleDescription, gridBagConstraints4);
		this.add(TitleFolder, gridBagConstraints5);
	}

	/**
	 * This method initializes ProjectName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getProjectName() {
		if (ProjectName == null) {
			ProjectName = new JTextField();
			ProjectName.setBounds(new Rectangle(140, 15, 520, 26));
			ProjectName.setName("ProjectTitel");
			ProjectName.setFont(new Font("Dialog", Font.PLAIN, 12));
			ProjectName.setText( CurrProject.getProjectName() );
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
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getProjectDescription());
		}
		return jScrollPane;
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
			ProjectDescription.setWrapStyleWord(true);
			ProjectDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
			ProjectDescription.setText( CurrProject.getProjectDescription() );
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
			ProjectFolder.setName("ProjectFolder");
			ProjectFolder.setBounds(new Rectangle(140, 285, 520, 26));
			ProjectFolder.setFont(new Font("Dialog", Font.PLAIN, 12));
			ProjectFolder.setEditable( false );
			ProjectFolder.setText( CurrProject.getProjectFolderFullPath() );			
		}
		return ProjectFolder;
	}

	
	@Override
	/**
	 * Get the notyfication of the ObjectModel
	 */
	public void update(Observable arg0, Object OName) {
		
		String ObjectName = OName.toString();
		if ( ObjectName.equalsIgnoreCase( "ProjectName" ) ) {
			if ( ProjectName.isFocusOwner() == false ) {
				ProjectName.setText( CurrProject.getProjectName() );				
			}								
		}			
		else if ( ObjectName.equalsIgnoreCase( "ProjectDescription" ) ) {
			if ( ProjectDescription.isFocusOwner() == false ) 
				ProjectDescription.setText( CurrProject.getProjectDescription() );
		}			
		else {
			//System.out.println("Unbekannter Updatebefehl vom Observerable ...");
		};
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// --- Das ActionCommand und den Auslöser des Events ermitteln ---
		String ActCMD = ae.getActionCommand();
		Object Trigger = ae.getSource();
		//System.out.println( "ActCMD/Wert => " + ActCMD );
		//System.out.println( "Auslöser => " + Trigger );

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
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + ActCMD);
		};
	}
	
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
