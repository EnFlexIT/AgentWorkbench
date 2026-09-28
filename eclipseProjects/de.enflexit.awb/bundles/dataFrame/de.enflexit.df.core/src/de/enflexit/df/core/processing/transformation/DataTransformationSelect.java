package de.enflexit.df.core.processing.transformation;

/**
 * This {@link AbstractDataTransformation} selects a set of columns from the input table.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationSelect extends AbstractDataTransformation {
	
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
		return DataTransformationServiceSelectImpl.TRANSFORMATION_NAME;
	}

}
