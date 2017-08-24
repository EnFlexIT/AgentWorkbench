package de.enflexit.common.classSelection;

/**
 * The Interface ClassSelectionListener can be used in order
 * to listen to selection the actions of the {@link ClassSelectionPanel}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface ClassSelectionListener {
	
	/**
	 * Will be invoked, if the user selected a class and confirmed its usage with the OK button.
	 * @param classSelected the class that was selected
	 */
	public void setSelectedClass(String classSelected);
	
	/**
	 * Will be invoked, if the user canceled the class selection 
	 */
	public void setSelectionCanceled();
	
}
