package de.enflexit.df.core.processing.transformation;

/**
 * This {@link AbstractDataTransformation} merges two or more input tables to a single table.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationJoin extends AbstractDataTransformation {
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#performTransformation()
	 */
	@Override
	public void performTransformation() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#getTransformationName()
	 */
	@Override
	public String getTransformationName() {
		return DataTransformationServiceJoinImpl.TRANSFORMATION_NAME;
	}
}
