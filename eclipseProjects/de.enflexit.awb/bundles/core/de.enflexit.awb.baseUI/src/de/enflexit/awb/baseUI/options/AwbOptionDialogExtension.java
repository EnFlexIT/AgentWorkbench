package de.enflexit.awb.baseUI.options;

import java.util.List;

/**
 * Implement this interface to extend the AWB options dialog with additional tabs. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public interface AwbOptionDialogExtension {
	
	public static final String OPTION_DIALOG_EXTENSION_ID = "de.enflexit.awb.baseUI.options.optionDialogExtension";
	
	/**
	 * Initializes the extension.
	 */
	public void initialize();

	/**
	 * Gets the list of options tabs to be added.
	 * @return the options tabs
	 */
	public List<AbstractOptionTab> getOptionTabs();

	/**
	 * Adds an options tab to the list.
	 * @param optionsTab the options tab
	 */
	public void addOptionsTab(AbstractOptionTab optionsTab);
}
