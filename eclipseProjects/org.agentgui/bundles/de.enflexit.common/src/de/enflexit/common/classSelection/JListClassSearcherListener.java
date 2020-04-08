package de.enflexit.common.classSelection;

/**
 * The listener interface for receiving results from a {@link JListClassSearcher}.
 * 
 * @see JListClassSearcher
 * @see JListClassSearcher#addClassSearcherListListener(JListClassSearcherListener)
 * @see JListClassSearcher#removeClassSearcherListListener(JListClassSearcherListener)
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
