package de.enflexit.awb.baseUI.options;

import java.util.ArrayList;
import java.util.List;

/**
 * Superclass for option dialog extensions.  
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class OptionDialogExtension implements AwbOptionDialogExtension {
	
	private ArrayList<AbstractOptionTab> optionTabs;

	/* (non-Javadoc)
	 * @see de.enflexit.awb.baseUI.options.AwbOptionDialogExtension#getOptionTabs()
	 */
	@Override
	public List<AbstractOptionTab> getOptionTabs() {
		if (optionTabs==null) {
			optionTabs = new ArrayList<AbstractOptionTab>();
		}
		return optionTabs;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.baseUI.options.AwbOptionDialogExtension#addOptionsTab(de.enflexit.awb.baseUI.options.AbstractOptionTab)
	 */
	@Override
	public void addOptionsTab(AbstractOptionTab optionsTab) {
		this.getOptionTabs().add(optionsTab);
	}

}
