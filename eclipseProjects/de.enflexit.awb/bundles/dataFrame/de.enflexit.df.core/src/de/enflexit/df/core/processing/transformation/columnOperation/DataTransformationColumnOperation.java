package de.enflexit.df.core.processing.transformation.columnOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformation.columnOperation.ColumnOperationDefinition.ResultHandling;
import de.enflexit.df.core.processing.transformation.graph.DataTableNode;
import de.enflexit.df.core.processing.transformation.graph.TransformationGraphController;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

/**
 * This data transformation allows to manipulate table data.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationColumnOperation extends AbstractDataTransformation {
	
	private List<ColumnOperationDefinition> operationsList;
	
	private ArrayList<ResultWrapper> resultsList;

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#getTransformationName()
	 */
	@Override
	public String getTransformationName() {
		return DataTransformationServiceColumnOperation.TRANSFORMATION_NAME;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#getLabelText()
	 */
	@Override
	public String getLabelText() {
		return "Calculation";
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#verifyParameters()
	 */
	@Override
	public boolean verifyParameters() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#performTransformation(de.enflexit.df.core.processing.transformation.graph.TransformationGraphController)
	 */
	@Override
	public Table performTransformation(TransformationGraphController graphController) {
		List<ColumnOperationDefinition> operationsList = this.getOperationsFromParameters();
		for (ColumnOperationDefinition rule : operationsList) {
			DataOperation operation = ColumnOperationRegistry.getInstance().getOperationByID(rule.getOperationID());
			Column<?> originalColumn = this.getInputTable().column(rule.getColumnName());
			if (operation.supports(originalColumn)) {
				Object parameter = operation.parseParameter(rule.getParameter());
				Column<?> modifiedColumn = operation.apply(originalColumn, parameter);
				this.getResultsList().add(new ResultWrapper(originalColumn, modifiedColumn, rule.getResultHandling()));
			}
			
		}
		
		// --- Create a copy of the table ---------------------------
		Table resultTable = this.getInputTable().copy();
		for (ResultWrapper result : this.getResultsList()) {
			if (result.resultHandling==ResultHandling.REPLACE) {
				// --- Replace the original column with the result --
				result.modifiedColumn.setName(result.originalColumn.name());
				resultTable.replaceColumn(result.modifiedColumn);
			} else {
				// --- Append the result, keep the original column --
//				result.modifiedColumn.setName(result.originalColumn.name() + "_mod");
				resultTable.addColumns(result.modifiedColumn);
			}
		}
		
		return resultTable;
	}

	/**
	 * Gets the operations list.
	 * @return the operations list
	 */
	public List<ColumnOperationDefinition> getOperationsList() {
		return operationsList;
	}

	/**
	 * Gets the configured operations from the parameters.
	 * @return the operations
	 */
	public List<ColumnOperationDefinition> getOperationsFromParameters() {
		String operationsListString = this.getTransformationParameters().getStringValue(DataTransformationServiceColumnOperation.PARAM_NAME_TRANSFORMATIONS_LIST);
		List<String> operationStrings = Arrays.asList(operationsListString.split(","));
		ArrayList<ColumnOperationDefinition> operationsList = new ArrayList<ColumnOperationDefinition>();
		for (String operationString : operationStrings) {
			operationsList.add(ColumnOperationDefinition.fromString(operationString));
		}
		return operationsList;
	}
	
	private Table getInputTable() {
		DataTableNode inputNode = this.getInputNodes().get(0);
		return inputNode!=null ? inputNode.getDataTable() : null;
	}
	
	private ArrayList<ResultWrapper> getResultsList() {
		if (resultsList==null) {
			resultsList = new ArrayList<DataTransformationColumnOperation.ResultWrapper>();
		}
		return resultsList;
	}

	/**
	 * Helper class to remember the results of the defined column operations.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class ResultWrapper {
		private Column<?> originalColumn;
		private Column<?> modifiedColumn;
		private ResultHandling resultHandling;
		
		public ResultWrapper(Column<?> originalColumn, Column<?> modifiedColumn, ResultHandling resultHandling) {
			super();
			this.originalColumn = originalColumn;
			this.modifiedColumn = modifiedColumn;
			this.resultHandling = resultHandling;
		}
		
		
	}

}
