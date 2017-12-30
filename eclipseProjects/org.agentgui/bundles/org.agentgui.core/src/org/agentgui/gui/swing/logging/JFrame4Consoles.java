package org.agentgui.gui.swing.logging;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.agentgui.gui.AwbConsoleDialog;

public class JFrame4Consoles extends JFrame implements AwbConsoleDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JTabbedPane4Consoles consoleFolder = null;

	/**
	 * This is the default constructor
	 */
	public JFrame4Consoles() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setSize(800, 300);
		this.setContentPane(getJContentPane());
		this.setTitle("Agent Remote-Debugging");
		
		// --- Listener for closing the application ----
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				setVisible(false);
			}
		});
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screenSize.height / 5; 
	    int width = screenSize.width;
		int top = (screenSize.height - height); 
	    int left = 0;
	    
	    this.setSize(width, height);
	    this.setLocation(left, top);	
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getConsoleFolder(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes consoleFolder	
	 * @return javax.swing.JTabbedPane	
	 */
	public JTabbedPane4Consoles getConsoleFolder() {
		if (consoleFolder == null) {
			consoleFolder = new JTabbedPane4Consoles();
		}
		return consoleFolder;
	}

} 
