package de.enflexit.awb.baseUI.options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

/**
 * The Class AbstractOptionTab.
 */
public abstract class AbstractOptionTab extends JPanel implements ActionListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 210514550187553667L;

	protected OptionDialog optionDialog;
	
	/**
	 * Instantiates a new abstract option tab.
	 */
	public AbstractOptionTab() {
		super();
	}
	/**
	 * Instantiates a new abstract option tab.
	 * @param optionDialog the option dialog
	 */
	public AbstractOptionTab(OptionDialog optionDialog) {
		super();
		this.optionDialog=optionDialog;

	}
	
	/**
	 * Returns the title addition for the {@link OptionDialog}.
	 * @return the title addition
	 */
	public abstract String getTitle();
	
	/**
	 * Returns the tool tip text.
	 * @return the tool tip text
	 */
	public abstract String getTabToolTipText();
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
