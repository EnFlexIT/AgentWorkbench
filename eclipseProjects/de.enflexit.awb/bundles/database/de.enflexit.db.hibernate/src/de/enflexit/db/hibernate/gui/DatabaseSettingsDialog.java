package de.enflexit.db.hibernate.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;

/**
 * The Class DatabaseSettingsDialog can be used to configure the 
 * connection settings for hibernate.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DatabaseSettingsDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -5778880894988052682L;
	
	private JPanel jPanelButtons;
	private JButton jButtonSave;
	private JButton jButtonTestConnection;
	private JButton jButtonCancel;

	private boolean canceled;
	private Vector<String> userMessages;
	private DatabaseSettingsPanel jPanelDbSettings;
	
	/**
	 * Instantiates a new database settings dialog.
	 * @param owner the owner
	 * @param databaseSettings the actual database settings to configure
	 */
	public DatabaseSettingsDialog(Window owner, DatabaseSettings databaseSettings) {
		super(owner);
		this.initialize();
		this.setDatabaseSettings(databaseSettings);
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setTitle("Database Settings");
		this.setSize(new Dimension(600, 500));
		this.setModal(true);
		
		this.registerEscapeKeyStroke();
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				canceled = true;
				setVisible(false);
			}
		});
		
		// --- Set Dialog position ----------------------------------
	    WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
	    
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_jPanelDbSettings = new GridBagConstraints();
		gbc_jPanelDbSettings.insets = new Insets(0, 0, 5, 0);
		gbc_jPanelDbSettings.fill = GridBagConstraints.BOTH;
		gbc_jPanelDbSettings.gridx = 0;
		gbc_jPanelDbSettings.gridy = 0;
		getContentPane().add(getJPanelDbSettings(), gbc_jPanelDbSettings);
		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.fill = GridBagConstraints.VERTICAL;
		gbc_jPanelButtons.insets = new Insets(10, 10, 20, 10);
		gbc_jPanelButtons.gridx = 0;
		gbc_jPanelButtons.gridy = 1;
		getContentPane().add(getJPanelButtons(), gbc_jPanelButtons);
		
	}
	
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
    			setCanceled(true);
            	setVisible(false);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
	
    private DatabaseSettingsPanel getJPanelDbSettings() {
		if (jPanelDbSettings == null) {
			jPanelDbSettings = new DatabaseSettingsPanel(null);
		}
		return jPanelDbSettings;
	}
    
    /**
     * Sets the header text for the Dialog.
     * @param headerText the new header
     */
    public void setHeaderText(String headerText) {
    	if (headerText!=null && headerText.isEmpty()==false) {
    		this.getJPanelDbSettings().setHeaderText(headerText);
    	}
	}
	
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_jPanelButtons = new GridBagLayout();
			gbl_jPanelButtons.columnWidths = new int[]{0, 0, 0, 0};
			gbl_jPanelButtons.rowHeights = new int[]{0, 0};
			gbl_jPanelButtons.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelButtons.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelButtons.setLayout(gbl_jPanelButtons);
			GridBagConstraints gbc_jButtonSave = new GridBagConstraints();
			gbc_jButtonSave.insets = new Insets(0, 0, 0, 60);
			gbc_jButtonSave.gridx = 0;
			gbc_jButtonSave.gridy = 0;
			jPanelButtons.add(getJButtonSave(), gbc_jButtonSave);
			GridBagConstraints gbc_jButtonTestConnection = new GridBagConstraints();
			gbc_jButtonTestConnection.insets = new Insets(0, 0, 0, 5);
			gbc_jButtonTestConnection.gridx = 1;
			gbc_jButtonTestConnection.gridy = 0;
			jPanelButtons.add(getJButtonTestConnection(), gbc_jButtonTestConnection);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(0, 60, 0, 0);
			gbc_jButtonCancel.gridx = 2;
			gbc_jButtonCancel.gridy = 0;
			jPanelButtons.add(getJButtonCancel(), gbc_jButtonCancel);
		}
		return jPanelButtons;
	}
	private JButton getJButtonSave() {
		if (jButtonSave == null) {
			jButtonSave = new JButton("Save");
			jButtonSave.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonSave.setForeground(new Color(0, 153, 0));
			jButtonSave.setPreferredSize(new Dimension(80, 26));
			jButtonSave.addActionListener(this);
		}
		return jButtonSave;
	}
	private JButton getJButtonTestConnection() {
		if (jButtonTestConnection == null) {
			jButtonTestConnection = new JButton("Test Connection");
			jButtonTestConnection.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonTestConnection.addActionListener(this);
		}
		return jButtonTestConnection;
	}
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setPreferredSize(new Dimension(80, 26));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	
	/**
	 * Checks if the dialog action was canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	/**
	 * Sets the canceled.
	 * @param canceled the new canceled
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	
	/**
	 * Returns the database settings.
	 * @return the database settings
	 */
	public DatabaseSettings getDatabaseSettings() {
		return this.getJPanelDbSettings().getDatabaseSettings();
	}
	/**
	 * Sets the database settings.
	 * @param databaseSettings the new database settings
	 */
	public void setDatabaseSettings(DatabaseSettings databaseSettings) {
		this.getJPanelDbSettings().setDatabaseSettings(databaseSettings);
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonCancel()) {
			// --- Cancel editing -------------------------
			this.setCanceled(true);
			this.setVisible(false);
			
		} else if (ae.getSource()==this.getJButtonTestConnection()) {
			// --- Test connection ------------------------
			DatabaseSettings dbSettings = this.getJPanelDbSettings().getDatabaseSettings();
			
			HibernateDatabaseService hds = HibernateUtilities.getDatabaseService(dbSettings.getDatabaseSystemName());
			if (hds!=null) {
				Properties props = dbSettings.getHibernateDatabaseSettings();
				this.clearUserMessages();
				if (hds.isDatabaseAccessible(props, this.getUserMessages(), true)==true) {
					JOptionPane.showMessageDialog(this, "Connection test successful!", "Connection Test", JOptionPane.INFORMATION_MESSAGE);
				} else {
					String message = "Connection test failed!";
					if (this.getUserMessages().isEmpty()==false) {
						message += "\n\n";
						for (String singelMessage : this.getUserMessages()) {
							message += singelMessage + "\n";
						}
					}
					JOptionPane.showMessageDialog(this, message, "Connection Test", JOptionPane.ERROR_MESSAGE);
				}
			}
			
		} else if (ae.getSource()==this.getJButtonSave()) {
			// --- Save the current settings --------------
			this.setCanceled(false);
			this.setVisible(false);
			
		}
	}
	
	/**
	 * Returns a message vector that can be used to inform the user about 
	 * configuration states (e.g. errors). This vector is especially used 
	 * in case of the JDBC connection test, to allow to provide more detailed
	 * information about the result of the connection test.
	 * 
	 * @return the user messages
	 */
	public Vector<String> getUserMessages() {
		if (userMessages==null) {
			userMessages = new Vector<>();
		}
		return userMessages;
	}
	/**
	 * Adds or prints the specified user message.
	 *
	 * @param userMessage the user message
	 * @param isError the is error
	 * @param printToConsole the print to console
	 */
	protected void addUserMessage(String userMessage, boolean printToConsole, boolean isError) {
		
		if (userMessage==null || userMessage.isEmpty()==true) return;
		
		this.getUserMessages().addElement(userMessage);
		if (printToConsole==true) {
			if (isError==true) {
				System.err.println(userMessage);
			} else {
				System.out.println(userMessage);
			}
		}
	}
	/**
	 * Clears all user messages.
	 */
	protected void clearUserMessages() {
		this.getUserMessages().clear();
	}
	
}
