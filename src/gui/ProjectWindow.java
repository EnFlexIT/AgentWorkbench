package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.tree.TreeSelectionModel;

import application.Application;
import application.Project;
import javax.swing.JSplitPane;
import javax.swing.JTree;

/**
 * @author: Christian Derksen
 *
 */
public class ProjectWindow extends JInternalFrame implements Observer, ActionListener {

	private Project CurrProject;
	
	private JTextField ProjectName = null;
	private DocumentListener ProjectNameDocumentListener;  //  @jve:decl-index=0:
	private JTextArea ProjectDescription = null;
	private JTextField ProjectFolder = null;
	
	private static final long serialVersionUID = 1L;
	private JPanel ProjectBaseInfo = null;
	private JTabbedPane ProjectViewRight = null;
	private JPanel ProjectAgents = null;
	private JPanel ProjectEnvironment = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JScrollPane jScrollPane = null;
	private JPanel ProjectOntology = null;
	private JScrollPane ProjectSimulation = null;
	private JPanel ProjectSimulationSetup = null;

	private JSplitPane ProjectViewSplit = null;

	private JScrollPane ProjectViewLeft = null;

	private JTree ProjectTree = null;

	/**
	 * This is the default constructor
	 */
	public ProjectWindow( Project CP ) {
		super();
		initialize();	
		this.CurrProject = CP;
		this.CurrProject.addObserver(this);		
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(850, 500);
		this.setContentPane(getProjectViewSplit());
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setResizable(true);
		this.setAutoscrolls(true);		
		this.setBorder(null);
		this.setFocusable(true);
		((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
		this.setVisible(true);
		this.moveToFront();		
		Application.MainWindow.ProjectDesktop.add(this);		
	}

	/**
	 * This method initializes ProjectBaseInfo	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getProjectBaseInfo() {
		if (ProjectBaseInfo == null) {
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(15, 45, 156, 21));
			jLabel2.setFont(new Font("Dialog", Font.BOLD, 14));
			jLabel2.setText("Projektbeschreibung");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(15, 15, 156, 19));
			jLabel1.setFont(new Font("Dialog", Font.BOLD, 14));
			jLabel1.setText("Projekttitel");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(15, 285, 156, 21));
			jLabel.setFont(new Font("Dialog", Font.BOLD, 14));
			jLabel.setText("Basisverzeichnis");
			ProjectBaseInfo = new JPanel();
			ProjectBaseInfo.setLayout(null);
			ProjectBaseInfo.setName("ProjectBaseInfo");
			ProjectBaseInfo.setFont(new Font("Verdana", Font.PLAIN, 12));
			ProjectBaseInfo.add(jLabel, null);
			ProjectBaseInfo.add(jLabel1, null);
			ProjectBaseInfo.add(jLabel2, null);
			ProjectBaseInfo.add(getProjectTitel(), null);
			ProjectBaseInfo.add(getProjectFolder(), null);
			ProjectBaseInfo.add(getJScrollPane(), null);
		}
		return ProjectBaseInfo;
	}

	/**
	 * This method initializes ProjectViewSplit	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getProjectViewSplit() {
		if (ProjectViewSplit == null) {
			ProjectViewSplit = new JSplitPane();
			ProjectViewSplit.setOneTouchExpandable(true);
			ProjectViewSplit.setDividerLocation(180);
			ProjectViewSplit.setDividerSize(10);
			ProjectViewSplit.setLeftComponent(getProjectViewLeft());
			ProjectViewSplit.setRightComponent(getProjectViewRight());
		}
		return ProjectViewSplit;
	}

	/**
	 * This method initializes ProjectViewLeft	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getProjectViewLeft() {
		if (ProjectViewLeft == null) {
			ProjectViewLeft = new JScrollPane();
			ProjectViewLeft.setViewportView(getProjectTree());
		}
		return ProjectViewLeft;
	}

	/**
	 * This method initializes ProjectTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getProjectTree() {
		if (ProjectTree == null) {
			ProjectTree = new JTree();
			ProjectTree.setName("ProjectTree");
			ProjectTree.setShowsRootHandles(false);
			ProjectTree.setRootVisible(true);
			ProjectTree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
			ProjectTree.addTreeSelectionListener( new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent ts) {
					System.out.println( ts.getPath().toString() );
					System.out.println( ts.getPath().toString() );
				}
			});
		}
		return ProjectTree;
	}

	/**
	 * This method initializes ProjectViewRight	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getProjectViewRight() {
		if (ProjectViewRight == null) {
			ProjectViewRight = new JTabbedPane();
			ProjectViewRight.setName("ProjectTabs");
			ProjectViewRight.setTabPlacement(JTabbedPane.TOP);
			ProjectViewRight.setPreferredSize(new Dimension(126, 72));
			ProjectViewRight.setFont(new Font("Dialog", Font.BOLD, 12));
			ProjectViewRight.addTab("Basis-Info", null, getProjectBaseInfo(), null);
			ProjectViewRight.addTab("Basis-Agenten", null, getProjectAgents(), null);
			ProjectViewRight.addTab("Kommunikation", null, getProjectOntology(), null);
			ProjectViewRight.addTab("Umgebungs - Setup", null, getProjectEnvironment(), null);
			ProjectViewRight.addTab("Simulations-Setup", null, getProjectSimulationSetup(), null);
			ProjectViewRight.addTab("Simulation", null, getProjectSimulation(), null);
		}
		return ProjectViewRight;
	}

	/**
	 * This method initializes ProjectAgents	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getProjectAgents() {
		if (ProjectAgents == null) {
			ProjectAgents = new JPanel();
			ProjectAgents.setLayout(new GridBagLayout());
			ProjectAgents.setName("ProjectAgents");
		}
		return ProjectAgents;
	}

	/**
	 * This method initializes ProjectEnvironment	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getProjectEnvironment() {
		if (ProjectEnvironment == null) {
			ProjectEnvironment = new JPanel();
			ProjectEnvironment.setLayout(new GridBagLayout());
			ProjectEnvironment.setName("ProjectEnvironment");
		}
		return ProjectEnvironment;
	}

	/**
	 * This method initializes ProjectTitel	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getProjectTitel() {
		if (ProjectName == null) {
			ProjectName = new JTextField();
			ProjectName.setBounds(new Rectangle(195, 15, 491, 26));
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
			ProjectFolder.setBounds(new Rectangle(195, 285, 491, 26));
			ProjectFolder.setText("");
			ProjectFolder.setName("ProjectFolder");
			ProjectFolder.setFont(new Font("Dialog", Font.PLAIN, 12));
			ProjectFolder.setEditable( false );
		}
		return ProjectFolder;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(195, 45, 491, 221));
			jScrollPane.setViewportView(getProjectDescription());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes ProjectOntology	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getProjectOntology() {
		if (ProjectOntology == null) {
			ProjectOntology = new JPanel();
			ProjectOntology.setLayout(new GridBagLayout());
		}
		return ProjectOntology;
	}

	/**
	 * This method initializes ProjectSimulation	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getProjectSimulation() {
		if (ProjectSimulation == null) {
			ProjectSimulation = new JScrollPane();
		}
		return ProjectSimulation;
	}

	/**
	 * This method initializes ProjectSimulationSetup	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getProjectSimulationSetup() {
		if (ProjectSimulationSetup == null) {
			ProjectSimulationSetup = new JPanel();
			ProjectSimulationSetup.setLayout(new GridBagLayout());
		}
		return ProjectSimulationSetup;
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
