package de.enflexit.df.core.processing.transformation;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation.InputType;

/**
 * {@link DataTransformationService} implementation for table joins / merges.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationServiceJoinImpl implements DataTransformationService {
	
	public static final String TRANSFORMATION_NAME = "Merge Tables";
	
	private static final String TRANSFORMATION_DESCRIPTION = "This data transformation merges two or more tables into a single table.";

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
		return InputType.MULTI_TABLE;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getRequiredParameters()
	 */
	@Override
	public List<String> getRequiredParameters() {
		ArrayList<String> requiredParameters = new ArrayList<String>();
		requiredParameters.add("join_columns (String list, one column for each table)");
		return requiredParameters;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getNewInstance()
	 */
	@Override
	public AbstractDataTransformation getNewInstance() {
		return new DataTransformationJoin();
	}

}
