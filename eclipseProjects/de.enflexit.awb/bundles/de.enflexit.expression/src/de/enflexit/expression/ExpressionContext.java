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

	// --------------------------------------------------------------
	// --- From here, methods to add context instances --------------  
	// --------------------------------------------------------------
	/**
	 * Adds the specified context object to the local context store.
	 * @param contextObject the context object
	 */
	public void addContextObject(Object contextObject) {
		if (contextObject!=null) {
			this.getContextObjectList().add(contextObject);
		}
	}
	/**
	 * Sets the context objects.
	 * @param contextObjects the new context objects
	 */
	public void addContextObjects(Object[] contextObjects) {
		if (contextObjects!=null) {
			for (int i = 0; i < contextObjects.length; i++) {
				this.addContextObject(contextObjects[i]);
			}
		}
	}
	
	// --------------------------------------------------------------
	// --- From here, access methods to the context information -----  
	// --------------------------------------------------------------
	/**
	 * Returns the context object list.
	 * @return the context object list
	 */
	public List<Object> getContextObjectList() {
		if (contextObjectList==null) {
			contextObjectList = new ArrayList<>();
			// --- TODO Add global context object here !!! ----------
		}
		return contextObjectList;
	}
	
	
	
	
	
	
}
