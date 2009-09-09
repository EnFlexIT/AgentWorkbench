/**
 * 
 */
package gui;

import javax.swing.JPanel;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import application.Application;

/**
 * @author derksen
 *
 */
public class ProjectWindow extends JInternalFrame implements ActionListener{

	/**
	 * 
	 */
	public JTextField ProjectFolder = null;
	public JTextField ProjectTitel = null;
	public JTextArea ProjectDescription = null;
	
	private static final long serialVersionUID = 1L;
	private JPanel ProjectBaseInfo = null;
	private JTabbedPane ProjectTabs = null;
	private JPanel ProjectAgents = null;
	private JPanel ProjectEnvironment = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JButton jButton = null;
	private JScrollPane jScrollPane = null;
	private JPanel ProjectOntology = null;
	
	/**
	 * This is the xxx default constructor
	 */
	public ProjectWindow() {
		super();
		initialize();
		//this.setTitle("Project: ");
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(800, 600);
		this.setContentPane(getProjectTabs());
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
			jLabel2.setBounds(new Rectangle(20, 95, 156, 21));
			jLabel2.setFont(new Font("Dialog", Font.BOLD, 14));
			jLabel2.setText("Projektbeschreibung");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(20, 65, 156, 19));
			jLabel1.setFont(new Font("Dialog", Font.BOLD, 14));
			jLabel1.setText("Projekttitel");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(20, 20, 156, 21));
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
			ProjectBaseInfo.add(getJButton(), null);
			ProjectBaseInfo.add(getJScrollPane(), null);
		}
		return ProjectBaseInfo;
	}

	/**
	 * This method initializes ProjectTabs	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getProjectTabs() {
		if (ProjectTabs == null) {
			ProjectTabs = new JTabbedPane();
			ProjectTabs.setName("ProjectTabs");
			ProjectTabs.setTabPlacement(JTabbedPane.TOP);
			ProjectTabs.setPreferredSize(new Dimension(126, 72));
			ProjectTabs.setFont(new Font("Dialog", Font.BOLD, 12));
			ProjectTabs.addTab("Basis-Info", null, getProjectBaseInfo(), null);
			ProjectTabs.addTab("Basis-Agenten", null, getProjectAgents(), null);
			ProjectTabs.addTab("Umgebungs - Setup", null, getProjectEnvironment(), null);
			ProjectTabs.addTab("Kommunikation", null, getProjectOntology(), null);
		}
		return ProjectTabs;
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
		if (ProjectTitel == null) {
			ProjectTitel = new JTextField();
			ProjectTitel.setBounds(new Rectangle(185, 65, 491, 26));
			ProjectTitel.setName("ProjectTitel");
			ProjectTitel.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return ProjectTitel;
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
			ProjectFolder.setBounds(new Rectangle(185, 20, 491, 26));
			ProjectFolder.setText("");
			ProjectFolder.setName("ProjectFolder");
			ProjectFolder.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return ProjectFolder;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(680, 20, 21, 21));
			jButton.setText("*");
			jButton.addActionListener( this );
			jButton.setActionCommand("ProjectLoadFile");
		}
		return jButton;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(185, 95, 491, 221));
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

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		System.out.println( ae.getActionCommand() );
		String AC = ae.getActionCommand();
		if ( AC.equalsIgnoreCase("ProjectLoadFile") ) {
			System.out.println( "Projekt-Datei laden ..." );
		}
		else if ( AC.equalsIgnoreCase("") ) {
			
		}
		else {
			System.out.println( "Unbekannter Befehl" );
		}
		
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
