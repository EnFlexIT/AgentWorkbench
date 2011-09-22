package agentgui.core.gui.debugging;

import java.awt.Font;

import javax.swing.JTabbedPane;

public class CoreWindowConsoleTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public CoreWindowConsoleTabbedPane() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setFont(new Font("Dialog", Font.BOLD, 12));
	}

	
	
}
