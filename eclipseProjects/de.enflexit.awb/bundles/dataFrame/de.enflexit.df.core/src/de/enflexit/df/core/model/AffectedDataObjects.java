package de.enflexit.df.core.model;

import de.enflexit.common.dataSources.AbstractDataSource;
import de.enflexit.df.core.model.treeNode.AbstractDataTreeNodeDataSource;
import de.enflexit.df.core.model.treeNode.DataTreeNodeObjectBase;
import de.enflexit.df.core.workbook.DataWorkbook;
import tech.tablesaw.api.Table;

/**
 * The class AffectedDataObjects represents a container that can hold instances 
 * of different types.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AffectedDataObjects {
	
	private Object[] arguments;
	
	/**
	 * Instantiates a new instance of AffectedDataObjects.
	 * @param args the argument Objects
	 */
	public AffectedDataObjects(Object... args) {
		this.setArguments(args);
	}
	
	/**
	 * Returns the arguments.
	 * @return the arguments
	 */
	public Object[] getArguments() {
		return arguments;
	}
	/**
	 * Sets the arguments.
	 * @param arguments the new arguments
	 */
	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}
	
	/**
	 * Returns the first instance of the specified class.
	 *
	 * @param <Type> the generic type
	 * @param typeClass the type class
	 * @return the instance found
	 */
	@SuppressWarnings("unchecked")
	public <Type> Type getInstance(Class<Type> typeClass) {
		for (Object arg : this.getArguments()) {
			if (typeClass.isAssignableFrom(arg.getClass())==true) {
				return (Type) arg;
			}
		}
		return null;

	}
	/**
	 * Returns the affected DataWorkbook, if available.
	 * @return the DataWorkbook
	 */
	public DataWorkbook getDataWorkbook() {
		return this.getInstance(DataWorkbook.class);
	}
	/**
	 * Returns the affected AbstractDataSource, if available.
	 * @return the data source
	 */
	public AbstractDataSource getDataSource() {
		return this.getInstance(AbstractDataSource.class);
	}
	
	/**
	 * Returns the first DataTreeNodeObjectBase, if available.
	 * @return the data tree node object
	 */
	public DataTreeNodeObjectBase getDataTreeNodeObject() {
		return this.getInstance(DataTreeNodeObjectBase.class);
	}
	
	
	/**
	 * Returns the first data source tree node object, if available.
	 * @return the data tree node object
	 */
	public AbstractDataTreeNodeDataSource<?> getDataTreeNodeObjectDataSource() {
		return this.getInstance(AbstractDataTreeNodeDataSource.class);
	}
	/**
	 * Returns the tablesaw table, if available.
	 * @return the table instance
	 */
	public Table getTable() {
		return this.getInstance(Table.class);
	}
	
	
	/**
	 * Creates an instance of the affected data objects.
	 *
	 * @param args the argument to be used reminded
	 * @return the affected data objects
	 */
	public static AffectedDataObjects create(Object... args) {
		return new AffectedDataObjects(args);
	}
	
}
