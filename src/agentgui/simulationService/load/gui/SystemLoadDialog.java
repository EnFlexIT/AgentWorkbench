package agentgui.simulationService.load.gui;


import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.CoreWindow;

public class SystemLoadDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	
	final static String PathImage = Application.RunInfo.PathImageIntern();
	private final ImageIcon iconAgentGUI = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUI.png") );
	private final Image imageAgentGUI = iconAgentGUI.getImage();

	/**
	 * @param owner
	 */
	public SystemLoadDialog() {
		super();
		initialize();
	}

	/**
	 * This method initialises this
	 * @return void
	 */
	private void initialize() {
		this.setSize(620, 120);
		this.setIconImage(imageAgentGUI);
	    this.setTitle( Application.RunInfo.getApplicationTitle() + ": " + Language.translate("Load Monitor") );
		this.setDefaultCloseOperation(CoreWindow.DO_NOTHING_ON_CLOSE);

		// --- Set the Look and Feel of the Dialog ------------------
//		this.setLookAndFeel();
		
		// --- Add a WindowsListener --------------------------------
	    this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});

	}

	
	/**
	 * This method set the Look and Feel of this Dialog
	 * @param NewLnF
	 */
	@SuppressWarnings("unused")
	private void setLookAndFeel() {
 
		String lnfClassname = Application.RunInfo.getAppLnF();
		try {
			if (lnfClassname == null) {
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			}
			UIManager.setLookAndFeel(lnfClassname);
			SwingUtilities.updateComponentTreeUI(this);				
		} 
		catch (Exception e) {
			System.err.println("Cannot install " + lnfClassname + " on this platform:" + e.getMessage());
		}
	}	
	
}  //  @jve:decl-index=0:visual-constraint="11,3"
