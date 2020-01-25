package agentproject;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.agentgui.gui.swing.project.ProjectWindowTab;
import org.agentgui.gui.swing.project.TabForSubPanels;

import agentgui.core.application.Application;
//import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.plugin.PlugIn;
import agentgui.core.project.Project;
import jade.core.ProfileImpl;

/**
 * The Class PlugInExample.
 */
public class PlugInExample extends PlugIn {

	private static String plugInName= "PlugIn-Example";
	
	private JMenu myMenu = new MyMenu("! My-Menue !");
	
	
	public PlugInExample(Project currProject) {
		super(currProject);
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.plugin.PlugIn#getName()
	 */
	@Override
	public String getName() {
		return plugInName;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.plugin.PlugIn#onPlugIn()
	 */
	@Override
	public void onPlugIn() {
		
		// ------------------------------------------------		
		// --- Extending the main application window ------
		// ------------------------------------------------
		if (Application.getMainWindow()==null) return;
		
		// --- adding a custom Menu -----------------------
		this.addJMenu(myMenu, 1);
		
		// --- adding a MenueItem to an available menue ---
		JMenuItem myJMenuItem = new JMenuItem("! Here I am  !");
		myJMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Great, you found me! That was the 'Here I am'-action !");
			}
		});
		
		JMenu menu = Application.getMainWindow().getJMenuMainSimulation();
		this.addJMenuItemComponent(menu, myJMenuItem, 0);
		
		// --- adding a Button to the toolbar -------------
		this.addJToolbarComponent(this.getJButtonExample(), 5);
		
		// ------------------------------------------------		
		// --- Extending the project window / tabs --------
		// ------------------------------------------------
		this.addMyTabs();
		
		// --- Call super method to print to console ------ 
		super.onPlugIn();
		
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.plugin.PlugIn#onPlugOut()
	 */
	@Override
	public void onPlugOut() {
		super.onPlugOut();
	}
	
	/**
	 * Gets an example JButton.
	 * @return the JButton example
	 */
	private JButton getJButtonExample() {
		
		JButton myButton = new JButton();
		myButton.setText("! My customized button !");
		myButton.setToolTipText("This is the tool tip text of your customized toolbar button");
		myButton.setSize(36, 36);
		// --------------------------------------------------------------------
		// Usually we took this Dimension and left the setText() empty !!
		// myButton.setPreferredSize( new Dimension(26,26) ); 
		// --------------------------------------------------------------------
		myButton.setPreferredSize( new Dimension(170, 26) );
		// --------------------------------------------------------------------
		myButton.setIcon(GlobalInfo.getInternalImageIcon("Refresh.png"));
		myButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("This event came from your customized toolbar-button !");
			}
		});
		return myButton;
	}
	
	
	/**
	 * Adds my tabs to the project window.
	 */
	private void addMyTabs() {
		
		// ------------------------------------------------
		// --- 1.Add a Tab without Sub-Tabs ---------------
		// ------------------------------------------------
		ProjectWindowTab pwt = new ProjectWindowTab(this.project, ProjectWindowTab.DISPLAY_4_END_USER, "! MyTab Single !", null, null, new ObserverOutputTab(this.project), null);
		this.addProjectWindowTab(pwt, 2);
		// ------------------------------------------------
		// --- 2.Add a Tab where Sub-Tabs can be added ----
		// ------------------------------------------------

		// --- The class to use ! ---------------
		String tabPane4SubPanesTitle = "! MyTab with SubTabs !";
		//String tabPane4SubPanesTitle = Language.translate("! MyTab with SubTabs !", Language.DE);
		
		// --- Add the main Tab -----------------
		pwt = new ProjectWindowTab(this.project, ProjectWindowTab.DISPLAY_4_DEVELOPER, tabPane4SubPanesTitle, null, null, new TabForSubPanels(project), null);
		this.addProjectWindowTab(pwt, 4);
		
			// --- Add a Sub-Tab ----------------
			pwt = new ProjectWindowTab(this.project, ProjectWindowTab.DISPLAY_4_DEVELOPER, "Observer-Output", null, null, new ObserverOutputTab(this.project), tabPane4SubPanesTitle);
			this.addProjectWindowTab(pwt);
		
		// ------------------------------------------------
		// --- 3.Add Tab to the Agent.GUI SimulationSetup -
		// ------------------------------------------------
		ProjectWindowTab parentPWT = this.project.getProjectEditorWindow().getTabForSubPanels(ProjectWindowTab.TAB_4_SUB_PANES_Setup);
		
		pwt = new ProjectWindowTab(this.project, ProjectWindowTab.DISPLAY_4_END_USER,"! My Setup-Tab!", "This is my tip text for a Simulation-Setup-Tab!", null, new JPanel(), parentPWT.getTitle());
		this.addProjectWindowTab(pwt, 1);
			
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.plugin.PlugIn#getJadeProfile(jade.core.ProfileImpl)
	 */
	@Override
	public ProfileImpl getJadeProfile(ProfileImpl jadeContainerProfile) {
		System.out.println("Hi! - I can change the JADE Profile !");
		return super.getJadeProfile(jadeContainerProfile);
	}
}
