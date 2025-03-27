package de.enflexit.db.hibernate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.ColumnOrderingStrategyStandard;
import org.hibernate.dialect.Dialect;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;

/**
 * The Class ColumnOrderingStrategyAsDefinedInClass.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ColumnOrderingStrategyAsDefinedInClass extends ColumnOrderingStrategyStandard {

	
	/* (non-Javadoc)
	 * @see org.hibernate.boot.model.relational.ColumnOrderingStrategyStandard#orderTableColumns(org.hibernate.mapping.Table, org.hibernate.boot.Metadata)
	 */
	@Override
	public List<Column> orderTableColumns(Table table, Metadata metadata) {
		
		System.err.println("[" + this.getClass().getSimpleName() + "] was called for: " + table.getName() + " => " + table.getNameIdentifier());
		
		List<String> entityAttribteList = this.getEntityAttributeList(table, metadata);

		
		
		
		
		final ArrayList<Column> orderedColumns = new ArrayList<>( table.getColumns() );
		orderedColumns.sort(new ColumnComparator(metadata));
		return orderedColumns;
	}
	
	/**
	 * Returns the entity attribute list in the natural order.
	 *
	 * @param table the table
	 * @param metadata the metadata
	 * @return the entity attribute list
	 */
	private List<String> getEntityAttributeList(Table table, Metadata metadata) {
		
		// --- Get PersistentClass of the table -----------
		PersistentClass pc = this.getPersistentClassFromTable(table, metadata);
		if (pc==null) return null;
		
		// --- Evaluate class -----------------------------
		Class<?> clazz = pc.getMappedClass();
		if (clazz==null) return null;
		
		// --- Evaluate field of class --------------------
		List<String> attributeList = new ArrayList<>();
		Field[] attributes = clazz.getDeclaredFields();
		for (Field field : attributes) {
			attributeList.add(field.getName());

		}
		return attributeList;
	}
	
	/**
	 * Return the Hibernate persistent class from table.
	 *
	 * @param table the table
	 * @param metadata the metadata
	 * @return the persistent class from table
	 */
	private PersistentClass getPersistentClassFromTable(Table table, Metadata metadata) {
		
		PersistentClass pcFound = null;
		Collection<PersistentClass> pcList = metadata.getEntityBindings();
		for (PersistentClass pc : pcList) {
			
			Table tbCheck = pc.getTable();
			if (tbCheck==table) {
				pcFound=pc;
			}
		}
		return pcFound;
	}
	
	
	/**
	 * The Class ColumnComparator.
	 *
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	protected static class ColumnComparator implements Comparator<Column> {
		
		private final Metadata metadata;

		protected ColumnComparator(Metadata metadata) {
			this.metadata = metadata;
		}

		@Override
		public int compare(Column col1, Column col2) {
			
			final Dialect dialect = metadata.getDatabase().getDialect();
			
			final int physicalSizeInBytes1 = physicalSizeInBytes(col1.getSqlTypeCode(metadata), col1.getColumnSize(dialect, metadata ), metadata);
			final int physicalSizeInBytes2 = physicalSizeInBytes(col2.getSqlTypeCode(metadata), col2.getColumnSize(dialect, metadata ), metadata);

			int cmp = Integer.compare( Integer.max( physicalSizeInBytes1, 4 ), Integer.max( physicalSizeInBytes2, 4 ) );
			if ( cmp != 0 ) {
				return cmp;
			}
			cmp = Boolean.compare( physicalSizeInBytes1 > 2048, physicalSizeInBytes2 > 2048 );
			if ( cmp != 0 ) {
				return cmp;
			}
			return col1.getName().compareTo( col2.getName() );
		}
	}
	
}
