package de.enflexit.expression.gui;

import java.util.ArrayList;

import de.enflexit.expression.ExpressionService;

/**
 * The Class ExpressionLibraryFilter can be used to filter the list of {@link ExpressionService}s in
 * the library of the {@link ExpressionEditorDialog}. Filtering can be done based on the names of the
 * services, either by in include or exclude mode.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionServiceFilter {
	public enum FilterMode{
		INCLUDE, EXCLUDE
	}
	
	private FilterMode filterMode;
	
	private ArrayList<Class<? extends ExpressionService>> filterList;

	/**
	 * Gets the filter mode.
	 * @return the filter mode
	 */
	public FilterMode getFilterMode() {
		return filterMode;
	}

	/**
	 * Sets the filter mode (include or exclude).
	 * @param filterMode the new filter mode
	 */
	public void setFilterMode(FilterMode filterMode) {
		this.filterMode = filterMode;
	}

	private ArrayList<Class<? extends ExpressionService>> getFilterList() {
		if (filterList==null) {
			filterList = new ArrayList<>();
		}
		return filterList;
	}
	
	/**
	 * Adds the specified expression service to the filter.
	 * @param serviceClass the expression type
	 */
	public void addServiceToFilter(Class<? extends ExpressionService> serviceClass) {
		if (this.getFilterList().contains(serviceClass)==false) {
			this.getFilterList().add(serviceClass);
		}
	}
	
	/**
	 * Removes the specified expression service from the filter.
	 * @param serviceClass the expression type
	 */
	public void removeServiceFromFilter(Class<? extends ExpressionService> serviceClass) {
		if (this.getFilterList().contains(serviceClass)==true) {
			this.getFilterList().remove(serviceClass);
		}
	}
	
	/**
	 * Checks if the specified expression service matches the filter.
	 * @param service the service
	 * @return true, if successful
	 */
	public boolean matches(ExpressionService service) {
		if (this.getFilterMode()==FilterMode.INCLUDE && this.getFilterList().contains(service.getClass())==true) {
			return true;
		} else if (this.getFilterMode()==FilterMode.EXCLUDE && this.getFilterList().contains(service.getClass())==false) {
			return true;
		}
		return false;
	}
	
}
