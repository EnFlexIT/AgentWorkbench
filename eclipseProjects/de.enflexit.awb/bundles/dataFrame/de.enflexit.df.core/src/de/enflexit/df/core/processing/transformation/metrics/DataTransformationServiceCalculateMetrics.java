package de.enflexit.df.core.processing.transformation.metrics;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformation.AbstractDataTransformation.InputType;

import java.util.List;

import de.enflexit.df.core.processing.transformation.DataTransformationService;
import de.enflexit.df.core.processing.transformation.metrics.ui.MetricsCalculationConfigurationPanel;
import de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel;

public class DataTransformationServiceCalculateMetrics implements DataTransformationService {
	
	public static final String TRANSFORMATION_NAME = "Calculate Metrics";
	private static final String TRANSFORMATION_DESCRIPTION = "This transformation calculates common deviation/error metrics for two or more data columns.";

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getName()
	 */
	@Override
	public String getName() {
		return TRANSFORMATION_NAME;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getDescription()
	 */
	@Override
	public String getDescription() {
		return TRANSFORMATION_DESCRIPTION;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getInputType()
	 */
	@Override
	public InputType getInputType() {
		return InputType.SINGLE_TABLE;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getRequiredParameters()
	 */
	@Override
	public List<String> getRequiredParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getNewInstance()
	 */
	@Override
	public AbstractDataTransformation getNewInstance() {
		return new DataTransformationCalculateMetrics();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getParameterConfigurationPanel()
	 */
	@Override
	public AbstractTransformationParameterConfigurationPanel getParameterConfigurationPanel() {
		return new MetricsCalculationConfigurationPanel(this);
	}

}
