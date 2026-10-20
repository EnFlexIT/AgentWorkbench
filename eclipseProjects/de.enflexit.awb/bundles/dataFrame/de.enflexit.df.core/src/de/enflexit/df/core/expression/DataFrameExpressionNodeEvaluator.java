package de.enflexit.df.core.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.extension.ColumnDescription;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.DataTreeModel;
import de.enflexit.df.core.model.treeNode.DTNO_Base;
import de.enflexit.expression.ExpressionContext;
import de.enflexit.expression.ExpressionEditorTreeNode;

/**
 * The Class DataFrameExpressionNodeEvaluator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataFrameExpressionNodeEvaluator {

	private ExpressionEditorTreeNode dfRootNode;
	private ExpressionContext context;
	
	/**
	 * Instantiates a new data frame expression evaluator.
	 *
	 * @param dfRootNode the data frame root node
	 * @param context the context
	 */
	public DataFrameExpressionNodeEvaluator(ExpressionEditorTreeNode dfRootNode, ExpressionContext context) {
		this.dfRootNode = dfRootNode;
		this.context = context;
		if (this.dfRootNode!=null && this.context!=null) {
			this.evaluateContext();
		}
	}
	/**
	 * Returns the data frame root node for the expression editor.
	 * @return the data frame root node
	 */
	public ExpressionEditorTreeNode getDataFrameRootNode() {
		return dfRootNode;
	}
	/**
	 * Evaluates the ExpressionContext for DataFrame relevant objects.
	 */
	private void evaluateContext() {
		
		ExpressionEditorTreeNode parentNode = this.dfRootNode;
		
		List<DataController> dcList = this.context.getContextObject(DataController.class);
		for (int i = 0; i < dcList.size(); i++) {
			
			if (dcList.size()>1) {
				ExpressionEditorTreeNode dcParentNode = new ExpressionEditorTreeNode("DataController [" + (i+1) + "]");
				this.dfRootNode.add(dcParentNode);
				parentNode = dcParentNode;
			}
			this.evaluateDataController(dcList.get(i), parentNode);
		}
	}
	
	/**
	 * Evaluate the specified DataController.
	 *
	 * @param dc the {@link DataController} to evaluate
	 * @param parentNode the parent node to which DataController elements are to be added
	 */
	private void evaluateDataController(DataController dc, ExpressionEditorTreeNode parentNode) {
		
		if (dc==null) return;
		
		DataTreeModel dtm = dc.getDataTreeModel();
		DefaultMutableTreeNode dtmRoot = dtm.getRootNode();

		this.collectTreeStructure(dc, dtmRoot, parentNode);
	}
	
	/**
	 * Collect tree structure.
	 *
	 * @param dc the current DataController
	 * @param dfTreeNode the data frame tree node
	 * @param exEditorTreeNode the expression editor tree node
	 */
	private void collectTreeStructure(DataController dc, DefaultMutableTreeNode dfTreeNode, ExpressionEditorTreeNode exEditorTreeNode) {
		
		for (int i = 0; i < dfTreeNode.getChildCount(); i++) {
			
			DefaultMutableTreeNode dtmNode = (DefaultMutableTreeNode) dfTreeNode.getChildAt(i);
			DTNO_Base dtno = (DTNO_Base) dtmNode.getUserObject();
			
			ExpressionEditorTreeNode exEditorNewTreeNode = new ExpressionEditorTreeNode(dtno.getCaption());
			exEditorTreeNode.add(exEditorNewTreeNode);
		
			// --- Check the type of DTNO -------------------------------------
			if (dtno instanceof AbstractDataSourceDTNO<?> dsDTNO) {
				
				// --- If we have a column description, we have data ! --------
				List<ColumnDescription> columnDescriptionList = dsDTNO.getColumnDescriptionList();
				if (columnDescriptionList.size()>0) {
					
					// --- Predefine expression path ---------------------------
					String pahtByName  = dc.getDataTreeModel().getExpressionPathByName(dtmNode);
					String pahtBysUUID = dc.getDataTreeModel().getExpressionPathByUUID(dtmNode);
					
					ArrayList<String> tableAccessList = new ArrayList<>();
					tableAccessList.add(pahtByName);
					tableAccessList.add(pahtBysUUID);
					
					
					// --- Define the expressions to be used ------------------
					TreeMap<String, ArrayList<String>> expressionTemplate = new TreeMap<>();
					expressionTemplate.put("Complete Table", tableAccessList);
					expressionTemplate.put("Columns by name path", this.getColumnList(pahtByName, columnDescriptionList)); 
					expressionTemplate.put("Columns by sUUID of table node", this.getColumnList(pahtBysUUID, columnDescriptionList));
					
					exEditorNewTreeNode.setExpressionTemplates(expressionTemplate);
				}
			}
			
			// --- Get child nodes --------------------------------------------
			if (dtmNode.getChildCount()>0) {
				this.collectTreeStructure(dc, dtmNode, exEditorNewTreeNode);
			}
			
		}
		
	}
	
	/**
	 * Returns the column list.
	 *
	 * @param columnBasePath the column base path
	 * @param cdList the column description list
	 * @return the String list of columns
	 */
	private ArrayList<String> getColumnList(String columnBasePath, List<ColumnDescription> cdList) {
		
		ArrayList<String> columnList = new ArrayList<>();
		for (ColumnDescription cd : cdList) {
			columnList.add(columnBasePath + ".COL('" + cd.getColumnName() + "')");
		}
		return columnList;
	}
	
}
