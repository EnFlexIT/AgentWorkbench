package de.enflexit.awb.desktop.project.setup;

import java.awt.BorderLayout;

import de.enflexit.awb.core.project.Project;
import de.enflexit.common.Observable;
import de.enflexit.common.Observer;

import javax.swing.JPanel;


/**
 * Represents the JPanel/Tab 'EnvironmentModel' in the setup path of the project.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentModelSetup extends JPanel implements Observer {

	private static final long serialVersionUID = 3230313372954316520L;
	
	private Project currProject;
	private JPanel environmentControllerGUI = null;
	
	/**
	 * Constructor.
	 * @param project the current project
	 */
	public EnvironmentModelSetup(Project project){
		this.currProject = project;
		this.currProject.addObserver(this);
		this.initialize();
	}
	
	/** Initialize. */
	private void initialize(){
		this.setLayout(new BorderLayout());
		this.add(this.getEnvironmentControllerGUI(), BorderLayout.CENTER);
	}
	
	/**
	 * Depending on the currently configured (predefined) environment/visualisation model, this
	 * method returns the EnvironmentPanel, which has to be displayed here on this JPanel.
	 *
	 * @return the currently configured Environment-Display
	 */
	private JPanel getEnvironmentControllerGUI(){
		if (environmentControllerGUI==null) {
			// --- Create an empty panel as default -----------------
			environmentControllerGUI = new JPanel();
			// --- Try to get the current environment controller ----
			if (this.currProject.getEnvironmentController()!=null) {
				environmentControllerGUI = this.currProject.getEnvironmentController().getOrCreateEnvironmentPanel();	
			}
		}
		return environmentControllerGUI;
	}
	
	/**
	 * Switch environment mode.
	 */
	private void reloadEnvironmentControllerGUI(){
		this.remove(this.getEnvironmentControllerGUI());
		this.environmentControllerGUI = null;
		this.add(this.getEnvironmentControllerGUI(), BorderLayout.CENTER);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {
		if(observable.equals(currProject) && updateObject.equals(Project.CHANGED_EnvironmentModelType)){
			this.reloadEnvironmentControllerGUI();
		}
	}

}
