package agentproject;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import agentgui.core.application.Application;
import agentgui.core.plugin.PlugIn;
import agentgui.core.plugin.PlugInNotification;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetupNotification;


/**
 * This is an example tab that prints all update notifications that are 
 * coming from the current {@link Project} (Observer Pattern)
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ObserverOutputTab extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JTextPane jTextPane = null;
	private JLabel jLabel = null;

	private String newLine = Application.getGlobalInfo().getNewLineSeparator();  //  @jve:decl-index=0:
	private MutableAttributeSet blue;  //  @jve:decl-index=0:
	private Document jTextPaneDocument = null;
	
	private Project currProject;
	
	
	/**
	 * This is the default constructor
	 */
	public ObserverOutputTab(Project project) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.insets = new Insets(10, 10, 0, 10);
		gridBagConstraints1.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(10, 10, 10, 10);
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1.0;

		jLabel = new JLabel();
		jLabel.setText("Observer-Output");
		jLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		
		this.setSize(530, 225);
		this.setLayout(new GridBagLayout());
		this.add(getJScrollPane(), gridBagConstraints);
		this.add(jLabel, gridBagConstraints1);
	}

	/**
	 * This method initializes jScrollPane	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTextPane());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTextPane	
	 * @return javax.swing.JTextPane	
	 */
	private JTextPane getJTextPane() {
		if (jTextPane == null) {
			jTextPane = new JTextPane();
			jTextPane.setContentType("text/html");
			jTextPane.setFont(new Font("Courier", Font.PLAIN, 14));
			jTextPane.setBackground(new Color(255, 255, 255));
			jTextPane.setEditable(false);
			jTextPane.setText("<p style='margin-top:0px;margin-bottom:0px'>");			
			
			blue = new SimpleAttributeSet();
		    StyleConstants.setForeground(blue, Color.blue);
		    StyleConstants.setFontFamily(blue, "Courier");
		    
			jTextPaneDocument = jTextPane.getDocument(); 
		}
		return jTextPane;
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {
		
		String appendText = "";
		
		// ----------------------------------------------------------
		// --- Changes in the Project-Configuration -----------------
		// ----------------------------------------------------------
		if (updateObject==null) {
			return;			
		} else if (updateObject.equals(Project.SAVED)) {
			appendText = Project.SAVED + ": Project was saved!";
			
		} else if (updateObject.equals(Project.CHANGED_ProjectName)) {
			appendText = Project.CHANGED_ProjectName + ": Project name was changed!";
			
		} else if (updateObject.equals(Project.CHANGED_ProjectDescription)) {
			appendText = Project.CHANGED_ProjectDescription + ": Project description was changed!";
			
		} else if (updateObject.equals(Project.CHANGED_ProjectView)) {
			appendText = Project.CHANGED_ProjectView + ": The view to the project (for developer / for end user was changed!";
			
		} else if (updateObject.equals(Project.CHANGED_EnvironmentModelType)) {
			appendText = Project.CHANGED_EnvironmentModelType + ": The type of the environment model was changed (non / physical 2D / Grid)!";
			
		} else if (updateObject.equals(Project.CHANGED_ProjectOntology)) {
			appendText = Project.CHANGED_ProjectOntology + ": The ontology configuration for the project was changed!";

		} else if (updateObject.equals(Project.CHANGED_StartArguments4BaseAgent)) {
			appendText = Project.CHANGED_StartArguments4BaseAgent + ": The agents start configuration was changed (start arguments for agents)!";

		} else if (updateObject.equals(Project.CHANGED_ProjectResources)) {
			appendText = Project.CHANGED_ProjectResources + ": The configuration of the external project ressources have changed!";

			
		// ----------------------------------------------------------
		// --- Changes with the SimulationSetups --------------------			
		// ----------------------------------------------------------
		} else if (updateObject instanceof SimulationSetupNotification) {
			
			appendText = "Simulation setup changed:";
			
			SimulationSetupNotification sscn = (SimulationSetupNotification) updateObject;
			switch (sscn.getUpdateReason()) {
			case SIMULATION_SETUP_ADD_NEW:
				appendText += newLine + "=> New simulation setup was added";
				break;
			case SIMULATION_SETUP_COPY:
				appendText += newLine + "=> Simulation setup was copied";
				break;
			case SIMULATION_SETUP_LOAD:
				appendText += newLine + "=> Simulation setup was loaded";
				break;
			case SIMULATION_SETUP_PREPARE_SAVING:
				appendText += newLine + "=> Simulation setup: Prepare for save action now!";
				break;
			case SIMULATION_SETUP_REMOVE:
				appendText += newLine + "=> Simulation setup was removed";
				break;
			case SIMULATION_SETUP_RENAME:
				appendText += newLine + "=> Simulation setup: Name was changed";
				break;
			case SIMULATION_SETUP_SAVED:
				appendText += newLine + "=> Simulation setup was saved";
				break;
			}
			
		// ----------------------------------------------------------
		// --- Changes with the Project-PlugIns ---------------------			
		// ----------------------------------------------------------
		} else if (updateObject.toString().equals(PlugIn.CHANGED)) {
			
			appendText = PlugIn.CHANGED + ": PlugIn configuration changed:";
			
			PlugInNotification pin = (PlugInNotification) updateObject;
			int pinUpdate = pin.getUpdateReason();
			PlugIn plugIn = pin.getPlugIn();
			if (pinUpdate == PlugIn.ADDED) {
				appendText += newLine + "=> " + PlugIn.ADDED + ": PlugIn added - " + plugIn.getName() + " [" + plugIn.getClassReference() + "]";
			} else if (pinUpdate == PlugIn.REMOVED) {
				appendText += newLine + "=> " + PlugIn.REMOVED + ": PlugIn removed - " + plugIn.getName() + " [" + plugIn.getClassReference() + "]";
			}			
			
		} else {
			appendText = "Unknown Notification from Observer " + observable.toString() + ": " + updateObject.toString();
		}			
			
		if (appendText!=null & appendText.equals("")==false) {
			// ---- append the text to the output window --
			try {
				jTextPaneDocument.insertString(jTextPaneDocument.getLength(), appendText + newLine, blue);
				jTextPane.setCaretPosition(jTextPaneDocument.getLength());
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		}
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
