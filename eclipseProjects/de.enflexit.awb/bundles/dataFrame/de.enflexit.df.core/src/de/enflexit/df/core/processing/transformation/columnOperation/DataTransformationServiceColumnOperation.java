package de.enflexit.df.core.processing.transformation.columnOperation;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformation.AbstractDataTransformation.InputType;
import de.enflexit.df.core.processing.transformation.columnOperation.ui.ColumnOperationConfigurationPanel;
import de.enflexit.df.core.processing.transformation.DataTransformationService;
import de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel;

/**
 * {@link DataTransformationService} implementation for data manipulations.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationServiceColumnOperation implements DataTransformationService {
	
	public static final String TRANSFORMATION_NAME = "Column Operations";
	private static final String TRANSFORMATION_DESCRIPTION = "This transformation allows to perform operations on colun data.";
	
	public static final String PARAM_NAME_TRANSFORMATIONS_LIST = "TransformationsList";

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
		ArrayList<String> requiredParameters = new ArrayList<String>();
		requiredParameters.add(PARAM_NAME_TRANSFORMATIONS_LIST);
		return requiredParameters;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getNewInstance()
	 */
	@Override
	public AbstractDataTransformation getNewInstance() {
		return new DataTransformationColumnOperation();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getParameterConfigurationPanel()
	 */
	@Override
	public AbstractTransformationParameterConfigurationPanel getParameterConfigurationPanel() {
		return new ColumnOperationConfigurationPanel(this);
	}

}
