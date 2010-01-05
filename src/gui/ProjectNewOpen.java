package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import application.Application;
import application.Language;


public class ProjectNewOpen extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static String NewLine = Application.RunInfo.AppNewLineString();  //  @jve:decl-index=0:
	
	private JPanel jContentPane = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabelLine = null;
	private JLabel jLabelLine1 = null;
	
	private JTextField ProjectName = null;
	private JTextField ProjectFolder = null;
	
	private JScrollPane jScrollTree = null;
	private JTree ProTree = null;
	private DefaultTreeModel ProjectTreeModel;
	private DefaultMutableTreeNode RootNode;
	private DefaultMutableTreeNode CurrentNode;  
	
	private JButton jButtonOK = null;
	private JButton jButtonCancel = null;
	
	private boolean NewProject;
	private boolean Canceled = false;
	
	/**
	 * @param owner
	 */
	public ProjectNewOpen(Frame owner, String titel, boolean modal, boolean NewPro) {
		super(owner, titel, modal);		
		NewProject = NewPro;
		
		//--- TreeModel initialisieren --------------------------
		RootNode = new DefaultMutableTreeNode( "... " + Application.RunInfo.PathProjects(false, false) );
		ProjectTreeModel = new DefaultTreeModel( RootNode );	
		initialize();

		// --- Dialog zentrieren ------------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);	

	    // --- Fallunterscheidung "Neues Projekt?" ------------------
	    if ( NewProject==true ) {
	    	jButtonOK.setText( Language.translate("OK") );
	    	ProjectName.setEnabled(true);
	    	ProjectFolder.setEnabled(true);
	    }
	    else {
	    	jButtonOK.setText( Language.translate("Öffnen") );
	    	ProjectName.setEnabled(false);
	    	ProjectFolder.setEnabled(false);	    	
	    };	    
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(506, 362);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				Canceled = true;
				setVisible(false);
			}
		});
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(15, 15, 100, 19));
			jLabel.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabel.setText( Language.translate("Projekttitel") );
			
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(15, 51, 100, 19));
			jLabel1.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabel1.setText( Language.translate("Verzeichnis") );
			
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(15, 110, 260, 19));
			jLabel2.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabel2.setText( Language.translate("Vorhandene Projektverzeichnisse") );
			
			jLabelLine = new JLabel();
			jLabelLine.setBounds(new Rectangle(15, 92, 470, 2));
			jLabelLine.setText("");
			jLabelLine.setBorder( BorderFactory.createEtchedBorder() );
			
			jLabelLine1 = new JLabel();
			jLabelLine1.setBounds(new Rectangle(300, 110, 2, 205));
			jLabelLine1.setBorder(BorderFactory.createEtchedBorder());
			
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(jLabel, null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(jLabel2, null);
			
			jContentPane.add(jLabelLine, null);
			jContentPane.add(jLabelLine1, null);
			
			jContentPane.add(getProjectName());
			jContentPane.add(getProjectFolder());
			jContentPane.add(getJScrollTree(), null);
			jContentPane.add(getJButtonOK(), null);
			jContentPane.add(getJButtonCancel(), null);
			
		}
		return jContentPane;
	}

	
	private JTextField getProjectName() {
		if (ProjectName == null) {
			ProjectName = new JTextField();
			ProjectName.setName("ProjectTitel");
			ProjectName.setBounds(new Rectangle(120, 15, 363, 26));			
			ProjectName.setFont(new Font("Dialog", Font.PLAIN, 12));
			ProjectName.addActionListener(this);			
		}
		return ProjectName;
	}
	/**
	 * This method initializes jTextField	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getProjectFolder() {
		if (ProjectFolder == null) {
			ProjectFolder = new JTextField();
			ProjectFolder.setName("ProjectFolder");
			ProjectFolder.setBounds(new Rectangle(120, 51, 363, 26));
			ProjectFolder.setFont(new Font("Dialog", Font.PLAIN, 12));		
			ProjectFolder.addActionListener(this);
		}
		return ProjectFolder;
	}

	/**
	 * This method initializes jScrollTree	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollTree() {
		if (jScrollTree == null) {
			jScrollTree = new JScrollPane();
			jScrollTree.setBounds(new Rectangle(16, 135, 258, 179));
			jScrollTree.setViewportView( getProTree() );
		}
		return jScrollTree;
	}
	
	/**
	 * This method initializes ProTree	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTree getProTree() {
		if (ProTree == null) {
			ProTree = new JTree( ProjectTreeModel );
			ProTree.setName("ProTree");
			ProTree.setBounds(new Rectangle(16, 135, 258, 179));			
			ProTree.setShowsRootHandles(false);
			ProTree.setRootVisible(true);
			ProTree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
			// --- Node-Objekte einfügen ------------------
			String[] IDEProjects = Application.RunInfo.getIDEProjects();
			for ( String Pro : IDEProjects ) {
				CurrentNode = new DefaultMutableTreeNode( Pro );
				RootNode.add( CurrentNode );
			}
			TreePath TreePathRoot = new TreePath(RootNode);
			ProTree.expandPath( TreePathRoot );	
			
			// --- Falls ein Projekt geöffnet werden soll - START ---
			if (NewProject == false ) {
				ProTree.addTreeSelectionListener(new TreeSelectionListener() {
					@Override
					public void valueChanged(TreeSelectionEvent ts) {
						
						TreePath PathSelected = ts.getPath();
						Integer PathLevel = PathSelected.getPathCount();
						String EndPath = ts.getPath().getLastPathComponent().toString();
						
						if (PathLevel == 2) {
							setVarProjectFolder( EndPath );
						}
						else {
							setVarProjectFolder(null);
						}		
						
					}
				});
				ProTree.addMouseListener( new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						if (me.getClickCount() == 2 ) {
							jButtonOK.doClick();	
						}
					}
				});
			};
			// --- Falls ein Projekt geöffnet werden soll - ENDE ----
			
		}
		return ProTree;
	}

	/**
	 * This method initializes jButtonOK	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setBounds(new Rectangle(330, 135, 136, 29));
			jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOK.setActionCommand( "OK" );
			jButtonOK.addActionListener(this);
		}
		return jButtonOK;
	}

	/**
	 * This method initializes jButtonCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setBounds(new Rectangle(330, 195, 136, 29));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setText( Language.translate("Abbruch") );
			jButtonCancel.setActionCommand("Cancel");
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	public boolean isCanceled(){
		return Canceled;
	}
	public void setVarProjectName(String text) {
		ProjectName.setText(text);
	}
	public String getVarProjectName() {
		return ProjectName.getText();
	}
	public void setVarProjectFolder(String text) {
		ProjectFolder.setText(text);
	}
	public String getVarProjectFolder() {
		return ProjectFolder.getText();
	}
	public String getVarProjectFolderFullPath() {
		return ProjectFolder.getText();
	}

	@Override
	public void actionPerformed(ActionEvent AC) {

		Object Act = AC.getSource();
		if ( Act == jButtonOK ) {
			if (ProjectError() == true) return;
			Canceled = false;
			setVisible(false);
		}
		else if ( Act == jButtonCancel ) {
			Canceled = true;
			setVisible(false);
		}
		else if ( Act == ProjectName ) {
			// do nothing
		}
		else if ( Act == ProjectFolder ) {
			jButtonOK.doClick();
		}
		else {
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + Act);
		}		
	}

	/**
	 * 
	 * @return true or false
	 */
	public boolean ProjectError () {
		// ----------------------------------------------------
		// --- Fehlerbehandlung bevor der Thread wieder -------
		// --- an die Klasse "Project" zurückgegeben wird -----
		// ----------------------------------------------------		
		String ProName = getVarProjectName();
		String ProFolder = getVarProjectFolder();
		
		boolean ProError = false;
		String ProErrorSrc = null;
		String MsgHead = null;
		String MsgText = null;
		
		
		if ( NewProject == true ) {
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++
			// +++ Anlegen eines neuen Projekts +++++++++++++++++++
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++
			// ----------------------------------------------------
			// --- Untersuchungen zum Projektnamen ----------------		
			if ( ProError==false && ProName == null ) {
				ProErrorSrc = "ProName";
				ProError = true;
			}
			else if ( ProError==false && ProName.length() == 0 ) {
				ProErrorSrc = "ProName";
				ProError = true;
			}
			// ----------------------------------------------------
			// --- Untersuchungen zum Projektverzeichnis ----------		
			if ( ProError==false && ProFolder == null ) {
				ProErrorSrc = "ProFolder";
				ProError = true;
			}
			else if ( ProError==false && ProFolder.length() == 0 ) {
				ProErrorSrc = "ProFolder";
				ProError = true;
			}
			// ----------------------------------------------------
			// --- Verzeichniseingabe überarbeiten ----------------
			ProFolder = ProFolder.trim();
			ProFolder = ProFolder.toLowerCase();
			ProFolder = ProFolder.replaceAll(" ", "_");
			while ( ProFolder.contains( "__" ) ) {
				ProFolder = ProFolder.replaceAll("__", "_");	
			}
			setVarProjectFolder(ProFolder);		
			// ----------------------------------------------------
			// --- Regulären Ausdruck abtesten --------------------
			String RegExp = "[a-z_]{3,}";
			if ( ProError==false && ProFolder.matches( RegExp ) == false ) {
				ProErrorSrc = "ProFolderRegEx";
				ProError = true;
			}
			// ----------------------------------------------------
			// --- Gibt es das gewünschte Verzeichnis bereits? ----
			if ( ProError==false ) {
				String[] IDEProjects = Application.RunInfo.getIDEProjects();
				for ( String Pro : IDEProjects ) {
					if ( Pro.equalsIgnoreCase(ProFolder) ) {
						ProErrorSrc = "ProFolderDouble";
						ProError = true;	
						break;
					}			
				}
			}
			// ----------------------------------------------------
			// --- Test: Basis-Verzeichnis anlegen ----------------
			if ( ProError==false ) {
				String NewDirName = Application.RunInfo.PathProjects(true, false) + ProFolder;
				File f = new File(NewDirName);
				if ( f.isDirectory() ) {
					ProErrorSrc = "ProFolderDouble";
					ProError = true;	
				} 
				else {
					if ( f.mkdir() == false ) {
						ProErrorSrc = "ProFolderCreate";
						ProError = true;	
					}
				}
			}		
			
			// ----------------------------------------------------
			// --- Show Error-Msg, if an error occurs -------------
			if (ProError==true) {
				if ( ProErrorSrc == "ProName" ) {
					MsgHead = Language.translate("Fehler - Projektname !");
					MsgText = Language.translate(
								 "Bitte geben Sie einen Projektnamen an!" + NewLine + NewLine + 
								 "Zulässig sind beliebige Zeichen " + NewLine +
								 "sowie Leerzeichen." );			
				}
				else if ( ProErrorSrc == "ProFolder" ) {
					MsgHead = Language.translate("Fehler - Projektverzeichnis !");
					MsgText = Language.translate(
								 "Bitte geben Sie ein korrektes Projektverzeichnis an!" + NewLine + NewLine + 
								 "Zulässig sind beliebige Zeichen (Kleinbuchstaben), die den " + NewLine +
								 "Konventionen für Verzeichnisse in Ihrem Betriebssystems" + NewLine +
								 "entsprechen. " + NewLine + NewLine +
								 "Leerzeichen sind nicht zulässig!" );			
				}
				else if ( ProErrorSrc == "ProFolderRegEx" ) {
					MsgHead = Language.translate("Fehler - Projektverzeichnis !" );
					MsgText = Language.translate(
							 "Der gewählte Bezeichner für das Projektverzeichnis enthält" + NewLine +  
							 "unzulässige oder zu wenige Zeichen! " + NewLine + NewLine +
							 "Es werden min. 3 bis max. 20 Zeichen benötigt:" + NewLine +
							 "Erlaubt sind nur Kleinbuchstaben. Umlaute und" + NewLine +
							 "Leerzeichen sind nicht zulässig (verwenden Sie " + NewLine +
							 "stattdessen _ 'Unterstrich').");
				}
				else if ( ProErrorSrc == "ProFolderDouble" ) {
					MsgHead = Language.translate("Fehler - Projektverzeichnis !" );
					MsgText = Language.translate(
							 "Das von Ihnen gewählte Projektverzeichnis wird bereits verwendet!" + NewLine + NewLine + 
							 "Bitte wählen Sie einen anderen Namen für Ihr Verzeichnis" );
				}
				else if ( ProErrorSrc == "ProFolderCreate" ) {
					MsgHead = Language.translate("Fehler - Projektverzeichnis !" );
					MsgText = Language.translate("Das Verzeichnis konnte nicht aangelegt werden !" );
				}
				JOptionPane.showInternalMessageDialog( this.getContentPane(), MsgText, MsgHead, JOptionPane.ERROR_MESSAGE);
			}					
		}
		else {
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++
			// +++ Öffnen eines vorhandnen Projekts +++++++++++++++
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++
			// --- Test, ob ein Projektordner gewählt wurde -------
			if ( ProError==false && (ProFolder == null || ProFolder == "" || ProFolder.length() == 0) ) {
				System.out.println("Fehler gefunden ");
				ProErrorSrc = "ProFolder";
				ProError = true;
			}
			// --- Test, ob die Projektdatei gefunden wurde -------
			if ( ProError==false ) {				
				String XMLFileName = Application.RunInfo.PathProjects(true, false) + 
									 ProFolder + 
									 Application.RunInfo.AppPathSeparatorString() +
									 Application.RunInfo.MASFile();
				File f = new File( XMLFileName );
				if ( f.isFile() == false ) {
					ProErrorSrc = "ProFolderAgentGUIxml";
					ProError = true;
				}
			}
			
			// ----------------------------------------------------
			// --- Show Error-Msg, if an error occurs -------------
			if (ProError==true) {
				if ( ProErrorSrc == "ProFolder" ) {
					MsgHead = Language.translate("Fehler - Projektauswahl !");
					MsgText = Language.translate("Bitte wählen Sie das gewünschte Projekt aus!");			
				}
				else if ( ProErrorSrc == "ProFolderAgentGUIxml" ) {
					MsgHead = Language.translate("Fehler - '@'");
					MsgText = Language.translate("Die Datei '@' wurde nicht gefunden!");	
					MsgHead = MsgHead.replace("@", Application.RunInfo.MASFile() );
					MsgText = MsgText.replace("@", Application.RunInfo.MASFile() );					
				}				
				JOptionPane.showInternalMessageDialog( this.getContentPane(), MsgText, MsgHead, JOptionPane.ERROR_MESSAGE);
			}
		}

		return ProError;
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
