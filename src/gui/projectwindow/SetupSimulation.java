package gui.projectwindow;

import gui.ProjectWindow;
import gui.projectwindow.simsetup.EnvironmentSetup;
import gui.projectwindow.simsetup.JadeSetup;
import gui.projectwindow.simsetup.StartSetup;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


import application.Language;
import application.Project;

public class SetupSimulation extends JPanel  {

	private static final long serialVersionUID = 1L;

	private Project currProject = null;
	private ProjectWindow pareComp = null;
	
	private JTabbedPane jTabbedPaneSimSetup = null;

	/**
	 * This is the default constructor
	 */
	public SetupSimulation(Project project, ProjectWindow parentComponent) {
		super();
		this.currProject = project;
		this.pareComp = parentComponent;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.ipadx = 0;
		gridBagConstraints.ipady = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(5, 0, 0, 0);
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(421, 239);
		this.add(getJTabbedPaneSimSetup(), gridBagConstraints);
		
		// --- Die (optionalen) Karteikarten einblenden ----------
		addProjectTab(Language.translate("Agenten-Start"), null, new StartSetup(currProject), Language.translate("Agenten-Konfiguration"));
		addProjectTab(Language.translate("Simulationsumgebung"), null, new EnvironmentSetup(currProject), Language.translate("Umgebungskonfiguration"));
		addProjectTab(Language.translate("JADE-Konfiguration"), null, new JadeSetup(currProject), Language.translate("JADE-Konfiguration"));
		
	}

	/**
	 * This method initializes jTabbedPaneSimSetup	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPaneSimSetup() {
		if (jTabbedPaneSimSetup == null) {
			jTabbedPaneSimSetup = new JTabbedPane();
			jTabbedPaneSimSetup.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jTabbedPaneSimSetup;
	}
	
	/**
	 * This method adds a Tab to the Simulation-Setup 
	 * @param title
	 * @param icon
	 * @param component
	 * @param tip
	 */
	public void addProjectTab( String title, Icon icon, Component component, String tip ) {
		// --- GUI-Komponente in das TabbedPane-Objekt einfügen ------------------
		component.setName( title ); 								// --- Component benennen ----
		jTabbedPaneSimSetup.addTab( title, icon, component, tip);	// --- Component anhängen ----
		// --- Neuen Unterknoten in Projektbaum einfügen -------------------------
		pareComp.addProjectTabNode("Simulations-Setup", title);
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,21"
