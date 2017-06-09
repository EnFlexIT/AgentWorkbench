package agentgui.logging.components;

import java.awt.Font;

import javax.swing.JTabbedPane;

public class JTabbedPane4Consoles extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public JTabbedPane4Consoles() {
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
