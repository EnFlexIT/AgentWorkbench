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
import java.util.TimeZone;
import java.util.Vector;

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
	private boolean isShowLabel;
	
	private JLabel jLabelTimeZone;
	private JTextField jTextFieldTimeZone;
	private JButton jButtonSearch;
	private JButton jButtonReset;

	private TimeZoneSelectionDialog tzsDialog;
	
	private ZoneId zoneId;
	
	private Vector<ActionListener> actionListeners;
	
	/**
	 * Instantiates a new time zone widget.
	 */
	public TimeZoneWidget() {
		this(null, true);
	}
	/**
	 * Instantiates a new time zone widget.
	 * @param owner the owner window
	 */
	public TimeZoneWidget(Window owner) {
		this(owner, true);
	}
	/**
	 * Instantiates a new time zone widget.
	 *
	 * @param owner the owner
	 * @param isShowLabel the indicator to show/hide the initial label
	 */
	public TimeZoneWidget(Window owner, Boolean isShowLabel) {
		this.ownerWindow = owner;
		this.isShowLabel = isShowLabel==null ? true : isShowLabel;
		this.initialize();	
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
	
		if (this.isShowLabel==false) {
			// --- Set layout without initial label ---------------------------
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
			gridBagLayout.rowHeights = new int[]{0, 0};
			gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
			gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			this.setLayout(gridBagLayout);
			
			GridBagConstraints gbc_comboBox = new GridBagConstraints();
			gbc_comboBox.fill = GridBagConstraints.BOTH;
			gbc_comboBox.gridx = 0;
			gbc_comboBox.gridy = 0;
			this.add(this.getJTextFieldTimeZone(), gbc_comboBox);
			
			GridBagConstraints gbc_jButtonSearch = new GridBagConstraints();
			gbc_jButtonSearch.fill = GridBagConstraints.VERTICAL;
			gbc_jButtonSearch.insets = new Insets(0, 2, 0, 0);
			gbc_jButtonSearch.gridx = 1;
			gbc_jButtonSearch.gridy = 0;
			this.add(this.getJButtonSearch(), gbc_jButtonSearch);
			
			GridBagConstraints gbc_jButtonReset = new GridBagConstraints();
			gbc_jButtonReset.fill = GridBagConstraints.VERTICAL;
			gbc_jButtonReset.insets = new Insets(0, 2, 0, 0);
			gbc_jButtonReset.gridx = 2;
			gbc_jButtonReset.gridy = 0;
			this.add(this.getJButtonReset(), gbc_jButtonReset);
			return;
		} 
		
		// --- Set layout in case that the initial label is to be shown ---
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelTimeZone = new GridBagConstraints();
		gbc_jLabelTimeZone.anchor = GridBagConstraints.WEST;
		gbc_jLabelTimeZone.gridx = 0;
		gbc_jLabelTimeZone.gridy = 0;
		this.add(this.getJLabelTimeZone(), gbc_jLabelTimeZone);
		
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 5, 0, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		this.add(this.getJTextFieldTimeZone(), gbc_comboBox);
		
		GridBagConstraints gbc_jButtonSearch = new GridBagConstraints();
		gbc_jButtonSearch.insets = new Insets(0, 2, 0, 0);
		gbc_jButtonSearch.gridx = 2;
		gbc_jButtonSearch.gridy = 0;
		this.add(this.getJButtonSearch(), gbc_jButtonSearch);
		
		GridBagConstraints gbc_jButtonReset = new GridBagConstraints();
		gbc_jButtonReset.insets = new Insets(0, 2, 0, 0);
		gbc_jButtonReset.gridx = 3;
		gbc_jButtonReset.gridy = 0;
		this.add(this.getJButtonReset(), gbc_jButtonReset);
		
	}
	
	private JLabel getJLabelTimeZone() {
		if (jLabelTimeZone == null) {
			jLabelTimeZone = new JLabel("Time Zone:");
			jLabelTimeZone.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelTimeZone;
	}
	private JTextField getJTextFieldTimeZone() {
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
	private JButton getJButtonSearch() {
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
	
	private JButton getJButtonReset() {
		if (jButtonReset == null) {
			jButtonReset = new JButton();
			jButtonReset.setIcon(BundleHelper.getImageIcon("MBreset.png"));
			jButtonReset.setToolTipText("Reset to system default");
			jButtonReset.setSize(new Dimension(26, 26));
			jButtonReset.setPreferredSize(new Dimension(26, 26));
			jButtonReset.addActionListener(this);
		}
		return jButtonReset;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setFont(java.awt.Font)
	 */
	@Override
	public void setFont(Font newFont) {
		if (newFont!=null) {
			this.getJLabelTimeZone().setFont(newFont.deriveFont(Font.BOLD));
			this.getJTextFieldTimeZone().setFont(newFont.deriveFont(Font.PLAIN));
		}
		super.setFont(newFont);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setPreferredSize(java.awt.Dimension)
	 */
	@Override
	public void setPreferredSize(Dimension newPreferredSize) {
		if (newPreferredSize!=null) {
			int newHeight = (int) newPreferredSize.getHeight();
			this.getJTextFieldTimeZone().setPreferredSize(new Dimension((int)this.getJTextFieldTimeZone().getPreferredSize().getWidth(), newHeight));
			this.getJButtonSearch().setPreferredSize(new Dimension(newHeight, newHeight));
			this.getJButtonReset().setPreferredSize(new Dimension(newHeight, newHeight));
		}
		super.setPreferredSize(newPreferredSize);
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
		} else if (ae.getSource()==this.getJButtonReset()) {
			// --- Reset to system default time zone ----------------
			this.setZoneId(ZoneId.systemDefault());
			
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
				zoneID = resultList.get(0).getZoneId();
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
		
		if (this.tzsDialog!=null) return null;
		
		// --- Open search dialog for ZoneIds -------------
		ZoneId zoneId = null;
		this.tzsDialog = new TimeZoneSelectionDialog(this.ownerWindow);
		this.tzsDialog.setZoneId(this.getZoneId());
		if (searchPhrase!=null && searchPhrase.isEmpty()==false) {
			this.tzsDialog.setSearchPhrase(searchPhrase);
		}
		this.tzsDialog.setVisible(true);

		// --- Wait for user ------------------------------
		if (this.tzsDialog.isCanceled()==false) {
			zoneId = this.tzsDialog.getZoneId();
		}
		this.tzsDialog.dispose();
		this.tzsDialog = null;
		return zoneId;
	}
	
	
	// ------------------------------------------------------------------------
	// --- From here, public data getter and setter methods -------------------
	// ------------------------------------------------------------------------	
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
		this.notifyListeners();
	}
	
	/**
	 * Returns the current {@link ZoneId} as {@link TimeZone}.
	 * @return the time zone
	 */
	public TimeZone getTimeZone() {
		return TimeZone.getTimeZone(this.getZoneId());
	}
	/**
	 * Sets the {@link ZoneId} based on the specified {@link TimeZone}.
	 * @param newTimeZone the new time zone
	 */
	public void setTimeZone(TimeZone newTimeZone) {
		if (newTimeZone!=null) {
			if (newTimeZone.equals(this.getTimeZone())==false) {
				this.setZoneId(newTimeZone.toZoneId()); 
			}
		}
	}
	// ------------------------------------------------------------------------
	// --- Till here, public data getter and setter methods -------------------
	// ------------------------------------------------------------------------	
	
	
	/**
	 * Adds an {@link ActionListener} to the widget.
	 * @param actionListener the action listener
	 */
	public void addActionListener(ActionListener actionListener) {
		this.getActionListeners().add(actionListener);
	}
	
	/**
	 * Removes an {@link ActionListener} from the widget.
	 * @param actionListener the action listener
	 */
	public void removeActionListeners(ActionListener actionListener) {
		this.getActionListeners().remove(actionListener);
	}
	
	/**
	 * Gets the registered action listeners.
	 * @return the action listeners
	 */
	private Vector<ActionListener> getActionListeners() {
		if (actionListeners==null) {
			actionListeners = new Vector<ActionListener>();
		}
		return actionListeners;
	}
	
	/**
	 * Notifies all registered listeners about a change of the selected time zone. 
	 */
	private void notifyListeners() {
		ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null);
		for (int i=0; i<this.getActionListeners().size(); i++) {
			this.getActionListeners().get(i).actionPerformed(ae);
		}
	}
	
}
