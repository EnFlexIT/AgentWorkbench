package de.enflexit.common.properties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import de.enflexit.common.BundleHelper;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;

/**
 * The Class PropertiesDialog enables to edit the specified Properties.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertiesDialog extends JDialog {

	private static final long serialVersionUID = -6707142186836769523L;

	private Properties properties;
	private String title;
	private PropertiesPanel propertiesPanel;
	
	/**
	 * Instantiates a new properties dialog.
	 *
	 * @param owner the owner window
	 * @param properties the properties
	 * @param title the title
	 */
	public PropertiesDialog(Window owner, Properties properties, String title) {
		super(owner);
		if (properties==null) {
			throw new NullPointerException("The Properties specified is not allowed to null!");
		}
		this.properties = properties;
		this.title = title;
		this.initialize();
	}
	/**
	 * Initializes this dialog.
	 */
	private void initialize() {
		
		this.setTitle(this.title);
		this.setIconImage(BundleHelper.getImageIcon("MBsettings.png").getImage());
		
		this.setModal(true);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		
		this.setSize(1000, 650);
		this.registerEscapeKeyStroke();
		
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);

		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{434, 0};
		gridBagLayout.rowHeights = new int[]{261, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		this.getContentPane().setLayout(gridBagLayout);
		
		GridBagConstraints gbc_learningConfigurationPanel = new GridBagConstraints();
		gbc_learningConfigurationPanel.insets = new Insets(10, 10, 10, 10);
		gbc_learningConfigurationPanel.fill = GridBagConstraints.BOTH;
		gbc_learningConfigurationPanel.gridx = 0;
		gbc_learningConfigurationPanel.gridy = 0;
		this.getContentPane().add(this.getPropertiesPanel(), gbc_learningConfigurationPanel);
	}
	
	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
            	PropertiesDialog.this.setVisible(false);
            	PropertiesDialog.this.dispose();
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
	
	/**
	 * Gets the learning configuration panel.
	 * @return the learning configuration panel
	 */
	private PropertiesPanel getPropertiesPanel() {
		if (propertiesPanel == null) {
			propertiesPanel = new PropertiesPanel(this.properties, this.title);
		}
		return propertiesPanel;
	}
}
