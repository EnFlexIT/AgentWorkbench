package de.enflexit.common.classSelection;

/**
 * The listener interface for receiving JListClassSearcher events.
 * The class that is interested in processing a JListClassSearcher
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addJListClassSearcherListener<code> method. When
 * the JListClassSearcher event occurs, that object's appropriate
 * method is invoked.
 *
 * @see JListClassSearcherEvent
 */
public interface JListClassSearcherListener {

	/**
	 * Will be invoked, if a class was found in a bundle and added to the result list.
	 * @param ce2d the ClassElement2Display
	 */
	public void addClassFound(ClassElement2Display ce2d);
	
	/**
	 * Will be invoked if a class was removed from the classes found.
	 * @param ce2d the ClassElement2Display
	 */
	public void removeClassFound(ClassElement2Display ce2d);
	
}
