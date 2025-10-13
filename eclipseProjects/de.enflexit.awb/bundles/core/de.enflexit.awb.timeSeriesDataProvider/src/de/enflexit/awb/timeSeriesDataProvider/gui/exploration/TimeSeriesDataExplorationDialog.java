package de.enflexit.awb.timeSeriesDataProvider.gui.exploration;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * A simple dialog for exploring time series data.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesDataExplorationDialog extends JDialog {
	
	private static final long serialVersionUID = 2163595342898387397L;
	
	private TimeSeriesDataExplorationPanel explorationPanel;
	
	/**
	 * Instantiates a new time series data exploration dialog.
	 * @param owner the owner window
	 */
	public TimeSeriesDataExplorationDialog(Window owner) {
		super(owner);
		initialize();
	}
	
	/**
	 * Initializes the GUI components.
	 */
	private void initialize() {
		this.setTitle("Explore Time Series Data");
		this.setContentPane(this.getExplorationPanel());
		this.setSize(600, 400);
		this.registerEscapeKeyStroke();
	}

	/**
	 * Gets the exploration panel.
	 * @return the exploration panel
	 */
	private JPanel getExplorationPanel() {
		if (explorationPanel == null) {
			explorationPanel = new TimeSeriesDataExplorationPanel();
		}
		return explorationPanel;
	}
	
	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
            	TimeSeriesDataExplorationDialog.this.setVisible(false);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
}
