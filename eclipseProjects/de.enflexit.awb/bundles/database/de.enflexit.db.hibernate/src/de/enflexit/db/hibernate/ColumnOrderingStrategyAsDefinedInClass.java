package de.enflexit.db.hibernate;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.ColumnOrderingStrategyStandard;
import org.hibernate.mapping.BasicValue;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.Value;

/**
 * The Class ColumnOrderingStrategyAsDefinedInClass.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ColumnOrderingStrategyAsDefinedInClass extends ColumnOrderingStrategyStandard {

	private boolean isDebug = false;
	
	/* (non-Javadoc)
	 * @see org.hibernate.boot.model.relational.ColumnOrderingStrategyStandard#orderTableColumns(org.hibernate.mapping.Table, org.hibernate.boot.Metadata)
	 */
	@Override
	public List<Column> orderTableColumns(Table table, Metadata metadata) {
		
		// --- Get attribute positions ------------------------------
		List<String> entityAttribteList = this.getEntityAttributeList(table, metadata);
		
		if (this.isDebug==true) {
			System.out.println("[" + this.getClass().getSimpleName() + "] Adjusting column order for table '" + table.getName() + "'");
			System.out.println("[" + this.getClass().getSimpleName() + "] Attribute order in class:");
			System.out.println("[" + this.getClass().getSimpleName() + "] " + String.join(", ", entityAttribteList));
		}
		
		if (entityAttribteList!=null) {
			// --- Sort with the help of the local comparator -------
			final ArrayList<Column> orderedColumns = new ArrayList<>(table.getColumns());
			orderedColumns.sort(new ColumnComparator(entityAttribteList));
			if (this.isDebug==true) {
				System.out.println("[" + this.getClass().getSimpleName() + "] Column order after sorting:");
				System.out.println("[" + this.getClass().getSimpleName() + "] " + String.join(", ", entityAttribteList));
			}
			return orderedColumns;
		}
		return super.orderTableColumns(table, metadata);
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
		
		private final List<String> entityAttribteList;
		private HashMap<String, Integer> entityAttributePositionHashMap;

		protected ColumnComparator(List<String> entityAttribteList) {
			this.entityAttribteList = entityAttribteList ;
		}

		/**
		 * Gets the entity attribute position hash map.
		 * @return the entity attribute position hash map
		 */
		public HashMap<String, Integer> getEntityAttribtePositionHashMap() {
			if (entityAttributePositionHashMap==null) {
				entityAttributePositionHashMap = new HashMap<>();
				for (int i = 0; i < entityAttribteList.size(); i++) {
					String attribute = entityAttribteList.get(i);
					entityAttributePositionHashMap.put(attribute, i+1);
				}
			}
			return entityAttributePositionHashMap;
		}
		
		@Override
		public int compare(Column col1, Column col2) {
			
			String collumnAttribute1 = this.getAttributeName(col1);
			String collumnAttribute2 = this.getAttributeName(col2);
			
			Integer listPosition1 = this.getEntityAttribtePositionHashMap().get(collumnAttribute1);
			Integer listPosition2 = this.getEntityAttribtePositionHashMap().get(collumnAttribute2);
			
			if (listPosition1!=null && listPosition2!=null) {
				int cmp = Integer.compare(listPosition1, listPosition2 );
				if (cmp!=0) return cmp;
			}
			return col1.getName().compareTo( col2.getName() );
		}
		
		/**
		 * Returns the classes attribute name that corresponds to the specified column.
		 *
		 * @param column the column
		 * @return the attribute name
		 */
		private String getAttributeName(Column column) {
			
			// --- Quick solution ? ---------------------------------
			String attributeName = column.getName(); // as default
			if (this.getEntityAttribtePositionHashMap().containsKey(attributeName)==true) return attributeName;

			// -- Check the value of the column ---------------------
			Type javaType = null;
			Value value = column.getValue();
			if (value instanceof SimpleValue) {
				SimpleValue simpleValue = (SimpleValue)value;
				Properties typeParameters = simpleValue.getTypeParameters();
				if (typeParameters!=null) {
					Object propValue = typeParameters.get("org.hibernate.type.ParameterType.propertyName");
					if (propValue!=null) {
						attributeName = (String)propValue;
						if (this.getEntityAttribtePositionHashMap().containsKey(attributeName)==true) return attributeName;
					}
				}
				
				// --- Try getting the Java Type --------------------
				if (simpleValue instanceof BasicValue) {
					javaType = ((BasicValue) simpleValue).getResolvedJavaType();
				}
			}
			
			// --- Now, guess :-( -----------------------------------
			String columnName = column.getName();
			String columnNameToLower = columnName.toLowerCase();
			String columnNameWithoutUnderscore = columnNameToLower.replace("_", "");
					
			boolean isBoolean = javaType!=null ? javaType.equals(Boolean.TYPE) : false;
			
			for (String toCheck : this.entityAttribteList) {
				String toCheckToLower = toCheck.toLowerCase();
				String toCheckWithoutUnderscore = toCheckToLower.replace("_", "");
				
				if (toCheck.equals(columnName)==true) return toCheck;
				if (toCheckToLower.equals(columnNameToLower)==true) return toCheck;
				if (toCheckWithoutUnderscore.equals(columnNameWithoutUnderscore)==true) return toCheck;
				
				if (isBoolean==true) {
					String toCheckPrefixIs  = "is" + toCheckWithoutUnderscore;
					if (toCheckPrefixIs.equals(columnNameWithoutUnderscore)==true) return toCheck;
					
					String toCheckPrefixHas = "has" + toCheckWithoutUnderscore;
					if (toCheckPrefixHas.equals(columnNameWithoutUnderscore)==true) return toCheck;
				}
			}
			return null;
		}
		
	}
	
}
