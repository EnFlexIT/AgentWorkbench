package de.enflexit.df.core.expression;

import java.util.List;

import de.enflexit.df.core.model.DataController;
import de.enflexit.expression.Expression;
import de.enflexit.expression.ExpressionContext;
import de.enflexit.expression.ExpressionData;
import de.enflexit.expression.ExpressionData.DataColumn;
import de.enflexit.expression.ExpressionData.DataType;
import de.enflexit.expression.ExpressionResult;
import de.enflexit.expression.ExpressionServiceEvaluator;
import de.enflexit.expression.UnknownExpressionException;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.columns.booleans.BooleanColumnType;
import tech.tablesaw.columns.numbers.DoubleColumnType;
import tech.tablesaw.columns.numbers.FloatColumnType;
import tech.tablesaw.columns.numbers.IntColumnType;
import tech.tablesaw.columns.numbers.LongColumnType;

/**
 * The Class DataFrameExpressionEvaluator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataFrameExpressionEvaluator implements ExpressionServiceEvaluator {

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionServiceEvaluator#evaluate(de.enflexit.expression.Expression, de.enflexit.expression.ExpressionContext)
	 */
	@Override
	public ExpressionResult evaluate(Expression expression, ExpressionContext context) throws UnknownExpressionException {
		
		if (context==null) return null;
		
		Table dataTable = null;
		List<DataController> dataControllerList = context.getContextObject(DataController.class);
		// --- Search to get the result table -----------------------
		for (DataController dc : dataControllerList) {

			String dfExpression = expression.getExpressionString();
			dataTable = dc.getData(dfExpression);
			if (dataTable!=null) {
				break;
			}
		}
		if (dataTable==null) return null;
		
		
		// --- Translate Table into ExpressionResult ----------------
		ExpressionData expData = new ExpressionData();
		
		for (int i = 0; i < dataTable.columnCount(); i++) {
			
			Column<?> col = dataTable.column(i);
			ColumnType colType = col.type();
		
			DataType dType = null;
			if (colType instanceof BooleanColumnType) {
				dType = DataType.Boolean;
			
			} else if (colType instanceof IntColumnType) {
				dType = DataType.Integer;
			} else if (colType instanceof LongColumnType) {
				dType = DataType.Long;
				
			} else if (colType instanceof FloatColumnType) {
				dType = DataType.Double;
			} else if (colType instanceof DoubleColumnType) {
				dType = DataType.Double;
			}
			
			if (dType!=null) {
				DataColumn dataColumn = expData.new DataColumn(col.name(), dType, null);
				// TODO
				expData.addDataColumn(dataColumn);
			}
		}
		
		// --- Return the result ------------------------------------
		ExpressionResult eResult = new ExpressionResult();
		eResult.setExpressionData(expData);
		return eResult;
	}

}
