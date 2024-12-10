package de.enflexit.awb.simulation.load.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import de.enflexit.awb.simulation.BundleHelper;
import de.enflexit.awb.simulation.agents.LoadMeasureAgent;
import de.enflexit.language.Language;

/**
 * This is the dialog window for displaying the current system load on
 * the platform and is used by the {@link LoadMeasureAgent}.
 * 
 * @see LoadMeasureAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SystemLoadDialog extends JFrame {

	private static final long serialVersionUID = 3170514914879967107L;

	private LoadMeasureAgent loadMeasureAgent;
	private SystemLoadPanel systemLoadPanel;

	
	/**
	 * Instantiates a new system load dialog.
	 * @param loadMeasureAgent the load measure agent
	 */
	public SystemLoadDialog(LoadMeasureAgent loadMeasureAgent) {
		super();
		this.loadMeasureAgent = loadMeasureAgent;
		this.initialize();
	}

	/**
	 * This method initialises this.
	 */
	private void initialize() {
		
		this.setSize(620, 120);
		this.setIconImage(BundleHelper.getImageIcon("awb16.png").getImage());
	    this.setTitle("Agent.Workbench: " + Language.translate("Load Monitor"));
		this.setContentPane(this.getSystemLoadPanel());		
		
		// --- Add a WindowsListener --------------------------------
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
	}

	/**
	 * Returns the system load panel.
	 * @return the system load panel
	 */
	public SystemLoadPanel getSystemLoadPanel() {
		if (systemLoadPanel==null) {
			systemLoadPanel = new SystemLoadPanel(this.loadMeasureAgent);
		}
		return systemLoadPanel;
	}
	
}  
