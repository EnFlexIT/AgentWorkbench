package de.enflexit.df.core.extension;

import java.util.List;

import javax.swing.JComponent;

/**
 * The Interface DataFrameExtensionService.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface DataFrameExtensionService {

	/**
	 * Has to return the extension name.
	 * @return the extension name
	 */
	public String getExtensionName(); 
	
	/**
	 * Has to return the extensions description.
	 * @return the extension description
	 */
	public String getExtensionDescription();
	
	
	/**
	 * Has to return the individual toolbar components.
	 * @return the toolbar components
	 */
	public List<JComponent> getToolbarComponents();
	
	
	
	
}
