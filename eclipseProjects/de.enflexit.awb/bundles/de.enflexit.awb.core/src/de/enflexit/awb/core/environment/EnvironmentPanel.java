package de.enflexit.awb.core.environment;

import de.enflexit.common.Observer;

import javax.swing.JPanel;

/**
 * In order to build an user interface, where environments can be defined by
 * end users in a visual way, this class has to be extended.  
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class EnvironmentPanel extends JPanel implements Observer {

	private static final long serialVersionUID = -5522022346976174783L;

	/** The environment controller that is used for the management of the environment model. */
	protected EnvironmentController environmentController = null;

	
	/**
	 * Constructor for displaying the current environment model during a running simulation.
	 * @param controller the current EnvironmentController 
	 */
	public EnvironmentPanel(EnvironmentController controller){
		super();
		this.setEnvironmentController(controller);
		this.getEnvironmentController().addObserver(this);
	}
	/**
	 * Returns the {@link EnvironmentController}
	 * @return the environmentController
	 */
	public EnvironmentController getEnvironmentController() {
		return environmentController;
	}
	/**
	 * Sets the new {@link EnvironmentController}.
	 * @param environmentController the environmentController to set
	 */
	public void setEnvironmentController(EnvironmentController environmentController) {
		this.environmentController = environmentController;
	}
	
	/**
	 * Should be invoked before an EnvironmentPanel has to be destroyed.
	 */
	public abstract void dispose();
	
	
}
