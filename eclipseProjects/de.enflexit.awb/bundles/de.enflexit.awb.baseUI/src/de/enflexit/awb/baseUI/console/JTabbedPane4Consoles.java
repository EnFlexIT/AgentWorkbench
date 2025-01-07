package de.enflexit.awb.baseUI.console;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTabbedPane;

import de.enflexit.awb.core.ui.AwbConsole;
import de.enflexit.awb.core.ui.AwbConsoleFolder;

/**
 * The Class JTabbedPane4Consoles.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JTabbedPane4Consoles extends JTabbedPane implements AwbConsoleFolder {

	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public JTabbedPane4Consoles() {
		super();
		this.initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setFont(new Font("Dialog", Font.BOLD, 12));
	}

	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbConsoleFolder#addTab(java.lang.String, org.agentgui.gui.AwbConsole)
	 */
	@Override
	public void addTab(String title, AwbConsole console) {
		this.addTab(title, (Component)console);
		
	}
	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbConsoleFolder#remove(org.agentgui.gui.AwbConsole)
	 */
	@Override
	public void remove(AwbConsole console) {
		this.remove((Component)console);
	}

	/* (non-Javadoc)
	 * @see org.agentgui.gui.AwbConsoleFolder#setSelectedComponent(org.agentgui.gui.AwbConsole)
	 */
	@Override
	public void setSelectedComponent(AwbConsole console) {
		this.setSelectedComponent((Component) console);
	}
	
}
