package gui.options;

import java.awt.BorderLayout;
import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import application.Application;

public class OptionConsole extends JPanel {

	private static final long serialVersionUID = 1L;
	
	/**
	 * This is the default constructor
	 */
	public OptionConsole() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1));
		this.add(Application.Console, BorderLayout.CENTER);
	}

}
