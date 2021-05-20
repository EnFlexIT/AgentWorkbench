package de.enflexit.common.swing;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.enflexit.common.BundleHelper;

/**
 * The Class TimeZoneWidget can be used to select a TimeZone.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TimeZoneWidget extends JPanel implements ActionListener {

	private static final long serialVersionUID = 3723882095849761105L;

	private Window ownerWindow;
	
	private JLabel jLabelTimeZone;
	private JTextField jTextFieldTimeZone;
	private JButton jButtonSearch;
	
	private ZoneId zoneId;
	
	/**
	 * Instantiates a new time zone widget.
	 */
	public TimeZoneWidget() {
		this(null);
	}
	/**
	 * Instantiates a new time zone widget.
	 * @param owner the owner window
	 */
	public TimeZoneWidget(Window owner) {
		this.ownerWindow = owner;
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
	
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelTimeZone = new GridBagConstraints();
		gbc_jLabelTimeZone.anchor = GridBagConstraints.WEST;
		gbc_jLabelTimeZone.gridx = 0;
		gbc_jLabelTimeZone.gridy = 0;
		this.add(getJLabelTimeZone(), gbc_jLabelTimeZone);
		
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 2, 0, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		this.add(getJTextFieldTimeZone(), gbc_comboBox);
		
		GridBagConstraints gbc_jButtonSort = new GridBagConstraints();
		gbc_jButtonSort.fill = GridBagConstraints.VERTICAL;
		gbc_jButtonSort.insets = new Insets(0, 2, 0, 0);
		gbc_jButtonSort.gridx = 2;
		gbc_jButtonSort.gridy = 0;
		this.add(getJButtonSearch(), gbc_jButtonSort);
		
	}
	
	public JLabel getJLabelTimeZone() {
		if (jLabelTimeZone == null) {
			jLabelTimeZone = new JLabel("Time Zone");
			jLabelTimeZone.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelTimeZone;
	}
	public JTextField getJTextFieldTimeZone() {
		if (jTextFieldTimeZone==null) {
			jTextFieldTimeZone = new JTextField(this.getZoneId().getId());
			jTextFieldTimeZone.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldTimeZone.addActionListener(this);
			jTextFieldTimeZone.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent fe) {
					String tzString = TimeZoneWidget.this.getJTextFieldTimeZone().getText().trim();
					if (tzString.isEmpty()) {
						// --- Set to local variable ----------------
						TimeZoneWidget.this.getJTextFieldTimeZone().setText(getZoneId().getId());
					} else {
						ZoneId zoneIDFound = TimeZoneWidget.this.getZoneIdFromSearchPhrase(tzString);
						if (zoneIDFound==null) {
							// --- Set to local variable ------------
							TimeZoneWidget.this.getJTextFieldTimeZone().setText(getZoneId().getId());
						} else if (tzString.equals(zoneIDFound.getId())==false) {
							// --- Compare strings ------------------
							TimeZoneWidget.this.getJTextFieldTimeZone().setText(zoneIDFound.getId());
						}
					}
				}
			});
		}
		return jTextFieldTimeZone;
	}
	public JButton getJButtonSearch() {
		if (jButtonSearch == null) {
			jButtonSearch = new JButton();
			jButtonSearch.setIcon(BundleHelper.getImageIcon("Search.png"));
			jButtonSearch.setToolTipText("Search ...");
			jButtonSearch.setPreferredSize(new Dimension(26, 26));
			jButtonSearch.setSize(new Dimension(26, 26));
			jButtonSearch.addActionListener(this);
		}
		return jButtonSearch;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJTextFieldTimeZone()) {
			// --- Enter was pressed in search ----------------------
			String searchPhrase = this.getJTextFieldTimeZone().getText().trim();
			if (searchPhrase.isEmpty()==true) {
				// --- Reset to initial state -----------------------
				this.getJTextFieldTimeZone().setText(this.getZoneId().getId());
				
			} else {
				// --- Check if a zone ID can be found -------------- 
				ZoneId zoneID = this.getZoneIdFromSearchPhrase(searchPhrase);
				if (zoneID!=null) {
					// --- Set the ZoneId found ---------------------
					this.setZoneId(zoneID);
				} else {
					// --- Reset to initial state -------------------
					this.getJTextFieldTimeZone().setText(this.getZoneId().getId());
				}
			}
			
		} else if (ae.getSource()==this.getJButtonSearch()) {
			// --- Open search dialog for ZoneIds -------------------
			ZoneId zoneIdSelected = this.getZoneIdFormUserDialog(null);
			if (zoneIdSelected!=null) {
				this.setZoneId(zoneIdSelected);
			}
		}
	}

	/**
	 * Return the zone id from search phrase.
	 *
	 * @param searchPhrase the search phrase
	 * @return the zone id from search phrase
	 */
	private ZoneId getZoneIdFromSearchPhrase(String searchPhrase) {
		
		ZoneId zoneID = null;
		
		// --- Check 1: Try to get ZoneId directly ------------------
		try {
			zoneID = ZoneId.of(searchPhrase);
		} catch (DateTimeException zrEx) { }
		
		// --- Check 2: Try to get ZoneId from own search -----------
		if (zoneID==null) {
			List<TimeZoneIdAndOffsets> resultList = TimeZoneIdAndOffsets.getZoneIdsWithOffsets(searchPhrase);
			if (resultList.size()==0) {
				// --- Open search dialog without search phrase -----
				zoneID = this.getZoneIdFormUserDialog(null);
			} else if (resultList.size()==1) {
				// --- Get ZoneId found -----------------------------
				zoneID = resultList.get(0).getZoneID();
			} else {
				// --- Open search dialog with search phrase --------
				zoneID = this.getZoneIdFormUserDialog(searchPhrase);
			}
		}
		return zoneID;
	}
	/**
	 * Returns the ZoneId that was selected by user in the corresponding dialog.
	 * @return the zone id form user dialog
	 */
	private ZoneId getZoneIdFormUserDialog(String searchPhrase) {
		
		// --- Open search dialog for ZoneIds -------------
		ZoneId zoneId = null;
		TimeZoneSelectionDialog tzsDialog = new TimeZoneSelectionDialog(this.ownerWindow);
		tzsDialog.setZoneId(this.getZoneId());
		if (searchPhrase!=null && searchPhrase.isEmpty()==false) {
			tzsDialog.setSearchPhrase(searchPhrase);
		}
		tzsDialog.setVisible(true);

		// --- Wait for user ------------------------------
		if (tzsDialog.isCanceled()==false) {
			zoneId = tzsDialog.getZoneId();
			tzsDialog.dispose();
		}
		return zoneId;
	}
	
	/**
	 * Returns the currently selected ZoneId.
	 * @return the zone id
	 */
	public ZoneId getZoneId() {
		if (zoneId==null) {
			zoneId = ZoneId.systemDefault();
		}
		return zoneId;
	}
	/**
	 * Sets the Zone id.
	 * @param zoneId the new zone id
	 */
	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
		this.getJTextFieldTimeZone().setText(zoneId.getId());
	}
	
}
