package de.enflexit.awb.timeSeriesDataProvider.jdbc.gui;

import javax.swing.JDialog;
import javax.swing.JPanel;

import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.db.hibernate.gui.DatabaseSettings;
import de.enflexit.db.hibernate.gui.DatabaseSettingsPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * A dialog for configuring the database settings.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DBSettingsDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = -7232291070753066878L;
	private DatabaseSettingsPanel dbSettingsPanel;
	private JPanel jPanelButtons;
	private JButton jButtonApply;
	private JButton jButtonCancel;
	
	private DatabaseSettings databaseSettings;
	private boolean canceled;
	
	public DBSettingsDialog() {
		initialize();
	}
	
	/**
	 * Instantiates a new DB settings dialog.
	 * @param owner the owner
	 * @param title the title
	 */
	public DBSettingsDialog(Window owner, String title) {
		this(owner, title, null);
	}
	
	/**
	 * Instantiates a new DB settings dialog.
	 * @param owner the owner
	 * @param title the title
	 * @param databaseSettings the initial database settings
	 */
	public DBSettingsDialog(Window owner, String title, DatabaseSettings databaseSettings) {
		super(owner, title);
		this.databaseSettings = databaseSettings;
		this.initialize();
		this.getDbSettingsPanel().setDatabaseSettings(databaseSettings);
	}

	private void initialize() {
		getContentPane().add(getJPanelButtons(), BorderLayout.SOUTH);
		getContentPane().add(getDbSettingsPanel(), BorderLayout.CENTER);
		this.setSize(600, 425);
		this.setModal(true);
	}		

	private DatabaseSettingsPanel getDbSettingsPanel() {
		if (dbSettingsPanel == null) {
			dbSettingsPanel = new DatabaseSettingsPanel(null);
		}
		return dbSettingsPanel;
	}
	
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_buttonsPanel = new GridBagLayout();
			gbl_buttonsPanel.columnWidths = new int[]{0, 0, 0};
			gbl_buttonsPanel.rowHeights = new int[]{0, 0};
			gbl_buttonsPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			gbl_buttonsPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelButtons.setLayout(gbl_buttonsPanel);
			GridBagConstraints gbc_btnOk = new GridBagConstraints();
			gbc_btnOk.anchor = GridBagConstraints.EAST;
			gbc_btnOk.insets = new Insets(10, 0, 10, 15);
			gbc_btnOk.gridx = 0;
			gbc_btnOk.gridy = 0;
			jPanelButtons.add(getJButtonApply(), gbc_btnOk);
			GridBagConstraints gbc_btnCancel = new GridBagConstraints();
			gbc_btnCancel.anchor = GridBagConstraints.WEST;
			gbc_btnCancel.insets = new Insets(10, 15, 10, 0);
			gbc_btnCancel.gridx = 1;
			gbc_btnCancel.gridy = 0;
			jPanelButtons.add(getJButtonCancel(), gbc_btnCancel);
		}
		return jPanelButtons;
	}
	private JButton getJButtonApply() {
		if (jButtonApply == null) {
			jButtonApply = new JButton("Apply");
			jButtonApply.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApply.setForeground(AwbThemeColor.ButtonTextGreen.getColor());
			jButtonApply.setPreferredSize(new Dimension(85, 26));
			jButtonApply.addActionListener(this);
		}
		return jButtonApply;
	}
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(AwbThemeColor.ButtonTextRed.getColor());
			jButtonCancel.setPreferredSize(new Dimension(85, 26));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonApply()) {
			this.databaseSettings = this.getDbSettingsPanel().getDatabaseSettings();
			this.canceled = false;
			this.setVisible(false);
		} else if (ae.getSource()==this.getJButtonCancel()) {
			this.canceled = true;
			this.setVisible(false);
		}
	}
	
	/**
	 * Gets the database settings.
	 * @return the database settings
	 */
	public DatabaseSettings getDbSettings() {
		return databaseSettings;
	}
	
	/**
	 * Checks if the dialog was closed using the cancel button.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
}
