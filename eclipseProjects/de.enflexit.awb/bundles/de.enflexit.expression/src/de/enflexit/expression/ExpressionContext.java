package de.enflexit.expression;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class ExpressionContext is used to provide the required specific instances to the corresponding
 * {@link ExpressionService}s. Therefore, the class also provides access to global context objects. 
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExpressionContext {

	private List<Object> contextObjectList;
	
	
	/**
	 * Instantiates a new expression context that contains the global 
	 * context only (e.g. AWB database connections and similar).
	 */
	public ExpressionContext() { }
	/**
	 * Instantiates a new ExpressionContext.
	 * @param contextObjects the context object
	 */
	public ExpressionContext(Object contextObject) {
		this.addContextObject(contextObject);
	}
	/**
	 * Instantiates a new ExpressionContext.
	 * @param contextObjects the context objects
	 */
	public ExpressionContext(Object ... contextObjects) {
		this.addContextObjects(contextObjects);
	}

	/**
	 * Returns the context object list.
	 * @return the context object list
	 */
	public List<Object> getContextObjectList() {
		if (contextObjectList==null) {
			contextObjectList = new ArrayList<>();
			// --- TODO Add global context objects here !!! ----------
		}
		return contextObjectList;
	}

	/**
	 * Adds the specified context object to the local context store.
	 * @param contextObject the context object
	 */
	public boolean addContextObject(Object contextObject) {
		if (contextObject!=null && this.getContextObjectList().contains(contextObject)==false) {
			return this.getContextObjectList().add(contextObject);
		}
		return false;
	}
	/**
	 * Sets the context objects.
	 * @param contextObjects the new context objects
	 */
	public void addContextObjects(Object[] contextObjects) {
		if (contextObjects!=null && contextObjects.length>0) {
			for (int i = 0; i < contextObjects.length; i++) {
				this.addContextObject(contextObjects[i]);
			}
		}
	}
	
	/**
	 * Returns all instances of the specified context class as list.
	 *
	 * @param <T> the generic type, specifying the context class 
	 * @param clazz the actual context class
	 * @return the list of context instances that are of the type specified
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getContextObject(Class<T> clazz) {
		
		List<T> objectsFound = new ArrayList<>();
		for (Object contextObject : this.getContextObjectList()) {
			if (contextObject.getClass().equals(clazz)==true) {
				objectsFound.add((T) contextObject);
			}
		}
		return objectsFound;
	}
	
}
