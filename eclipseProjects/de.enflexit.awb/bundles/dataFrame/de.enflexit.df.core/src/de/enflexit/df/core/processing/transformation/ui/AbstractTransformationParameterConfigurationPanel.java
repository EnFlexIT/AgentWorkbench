package de.enflexit.df.core.processing.transformation.ui;

import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import de.enflexit.common.properties.Properties;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.processing.transformation.DataTransformationService;

/**
 * Abstract superclass for transformation-specific configuration components.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class AbstractTransformationParameterConfigurationPanel extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = -2488414551927207932L;
	
	private DataTransformationService transformation;
	
	private DataController dataController;
	
	/**
	 * Instantiates a new abstract transformation parameter configuration component.
	 * @param transformation the transformation
	 */
	public AbstractTransformationParameterConfigurationPanel(DataTransformationService transformation) {
		this.transformation = transformation;
	}

	/**
	 * Returns the parameters as configured in the component UI.  
	 * @return the configured parameters
	 */
	public abstract Properties getConfiguredParameters();
	
	/**
	 * Sets the configured parameters to be displayed by the panel.
	 * @param parameters the parameters
	 */
	public abstract void setConfiguredParameters(Properties parametersAsProperties);

	/**
	 * Gets the transformation.
	 * @return the transformation
	 */
	public DataTransformationService getTransformation() {
		return transformation;
	}

	/**
	 * Sets the transformation.
	 * @param transformation the new transformation
	 */
	public void setTransformation(DataTransformationService transformation) {
		this.transformation = transformation;
	}

	/**
	 * Gets the data controller.
	 * @return the data controller
	 */
	public DataController getDataController() {
		return dataController;
	}
	/**
	 * Sets the data controller.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
	}
	
	

}
