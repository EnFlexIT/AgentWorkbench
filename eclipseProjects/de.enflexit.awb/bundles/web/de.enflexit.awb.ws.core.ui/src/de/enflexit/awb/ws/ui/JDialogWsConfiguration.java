package de.enflexit.awb.ws.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.ui.client.JPanelClientConfiguration;
import de.enflexit.awb.ws.ui.server.JPanelServerConfiguration;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;

/**
 * The Class JDialogWsConfiguration enables to configure WS server and clients.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JDialogWsConfiguration extends JDialog implements ActionListener {

	private static final long serialVersionUID = 5524799981049258706L;
	
	private JTabbedPane jTabbedPane;
	private JPanelServerConfiguration jPanelServerConfiguration;
	private JPanelClientConfiguration jPanelClientConfiguration;
	private JButton jButtonClose;

	/**
	 * Instantiates a new dialog for the WS-configuration.
	 */
	public JDialogWsConfiguration() {
		this(null);
	}
	/**
	 * Instantiates a new dialog for the WS-configuration.
	 * @param ownerWindow the owner window
	 */
	public JDialogWsConfiguration(Window ownerWindow) {
		super(ownerWindow);
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setModal(true);
		this.setAlwaysOnTop(false);

		this.setTitle("WS - Configuration");
		this.setIconImage(BundleHelper.getImageIcon("awbWeb16.png").getImage());
		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		this.registerEscapeKeyStroke();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				JDialogWsConfiguration.this.doCloseAskUser();
			}
		});
		
		// --- Size and center dialog -------------------------------
		Rectangle screenSize = this.getGraphicsConfiguration().getBounds();
		int height = (int)(screenSize.getHeight() * 0.6);
		int width  = (int) (screenSize.getWidth() * 0.6);
		this.setSize(width, height);
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
	    

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jTabbedPane = new GridBagConstraints();
		gbc_jTabbedPane.insets = new Insets(10, 10, 10, 10);
		gbc_jTabbedPane.fill = GridBagConstraints.BOTH;
		gbc_jTabbedPane.gridx = 0;
		gbc_jTabbedPane.gridy = 0;
		this.getContentPane().add(this.getJTabbedPane(), gbc_jTabbedPane);
		GridBagConstraints gbc_jButtonClose = new GridBagConstraints();
		gbc_jButtonClose.insets = new Insets(0, 10, 15, 10);
		gbc_jButtonClose.gridx = 0;
		gbc_jButtonClose.gridy = 1;
		getContentPane().add(getJButtonClose(), gbc_jButtonClose);
		
	}
	 /**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
            	JDialogWsConfiguration.this.doCloseAskUser();
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
			jTabbedPane.setFont(new Font("Dialog", Font.BOLD, 13));
			jTabbedPane.addTab(" WS - Server ",  null, this.getJPanelServerConfiguration(), null);
			jTabbedPane.addTab(" WS - Clients ", null, this.getJPanelClientConfiguration(), null);
		}
		return jTabbedPane;
	}
	private JPanelServerConfiguration getJPanelServerConfiguration() {
		if (jPanelServerConfiguration == null) {
			jPanelServerConfiguration = new JPanelServerConfiguration();
		}
		return jPanelServerConfiguration;
	}
	
	private JPanelClientConfiguration getJPanelClientConfiguration() {
		if (this.jPanelClientConfiguration == null) {
			this.jPanelClientConfiguration = new JPanelClientConfiguration();
		}
		return this.jPanelClientConfiguration;
	}
	private JButton getJButtonClose() {
		if (jButtonClose == null) {
			jButtonClose = new JButton("Close");
			jButtonClose.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonClose.setForeground(new Color(0, 0, 153));
			jButtonClose.setPreferredSize(new Dimension(100, 26));
			jButtonClose.addActionListener(this);
		}
		return jButtonClose;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonClose()) {
			this.doCloseAskUser();
		}
	}

	/**
	 * Will close the dialog if no unsaved changes occur in the settings.
	 */
	private void doCloseAskUser() {
		
		// --- Check server configuration ---------------------------
		if (this.getJPanelServerConfiguration().hasUnsavedChanges()==true) {
			this.getJTabbedPane().setSelectedIndex(0);
			if (this.getJPanelServerConfiguration().userConfirmedToChangeView()==false) return;
		}
		// --- Check client configuration ---------------------------
		if (this.getJPanelClientConfiguration().hasUnsavedChanges()==true) {
			this.getJTabbedPane().setSelectedIndex(1);
			if (this.getJPanelClientConfiguration().userConfirmedToChangeView()==false) return;
		}
		// --- Done - close dialog ----------------------------------		
		this.setVisible(false);
	}
	
}