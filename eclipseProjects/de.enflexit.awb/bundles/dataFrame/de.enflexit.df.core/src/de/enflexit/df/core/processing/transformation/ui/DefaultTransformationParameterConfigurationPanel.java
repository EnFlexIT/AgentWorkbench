package de.enflexit.df.core.processing.transformation.ui;

import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.PropertiesPanel;
import de.enflexit.df.core.processing.transformation.DataTransformationService;
import java.awt.BorderLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;

import javax.swing.JLabel;

/**
 * Default transformation configuration panel, providing a table with one row for each required
 * parameter. All returned parameter values will be strings, parse as required if necessary.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DefaultTransformationParameterConfigurationPanel extends AbstractTransformationParameterConfigurationPanel {

	private static final long serialVersionUID = 3783681403788015570L;
	private JLabel jLabelTest;
	private PropertiesPanel parameterPropertiesPanel;

	/**
	 * Instantiates a new default transformation parameter configuration panel.
	 * @param transformation the transformation
	 */
	public DefaultTransformationParameterConfigurationPanel(DataTransformationService transformation) {
		super(transformation);
		initialize();
    }
	
	/**
	 * Initializes the UI components.
	 */
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getJLabelTransformationName(), BorderLayout.NORTH);
		add(getParameterPropertiesPanel(), BorderLayout.CENTER);
	}

	/**
	 * Gets the j label transformation name.
	 * @return the j label transformation name
	 */
	private JLabel getJLabelTransformationName() {
		if (jLabelTest == null) {
			jLabelTest = new JLabel("Configuration Panel for " + this.getTransformation().getName());
			jLabelTest.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelTest;
	}
	
	/**
	 * Gets the parameter properties panel.
	 * @return the parameter properties panel
	 */
	private PropertiesPanel getParameterPropertiesPanel() {
		if (parameterPropertiesPanel == null) {
			parameterPropertiesPanel = new PropertiesPanel();
		}
		return parameterPropertiesPanel;
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// --- No action required ---------------
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel#setConfiguredParameters(de.enflexit.common.properties.Properties)
	 */
	@Override
	public void setConfiguredParameters(Properties parametersAsProperties) {
		this.getParameterPropertiesPanel().setProperties(parametersAsProperties);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel#getConfiguredParameters()
	 */
	@Override
	public Properties getConfiguredParameters() {
		return this.getParameterPropertiesPanel().getProperties();
	}

}
