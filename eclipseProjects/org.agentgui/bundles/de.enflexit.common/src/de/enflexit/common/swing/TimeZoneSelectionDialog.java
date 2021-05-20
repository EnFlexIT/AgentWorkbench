/*
 * 
 */
package de.enflexit.common.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * The Class TimeZoneSelectionDialog can be used to select a {@link ZoneId} of a time zone.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TimeZoneSelectionDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 5551312935313702253L;
	
	private boolean canceled;
	
	private JLabel jLabelHeader;

	private JScrollPane jScrollPaneZoneIds;
	private JList<TimeZoneIdAndOffsets> jListZoneIds;
	private DefaultListModel<TimeZoneIdAndOffsets> listModel;
	private DefaultListModel<TimeZoneIdAndOffsets> listModelFiltered;
	
	private JLabel jListSearch;
	private JTextField jTextFieldSearch;
	private JCheckBox jCheckboxShowDiffToLocalTime;
	private JCheckBox jCheckboxShowDiffToUTC;
	
	private JPanel jPanelSorting;
	private JLabel jLabelSort;
	private JRadioButton jRadioButtonSortById;
	private JRadioButton jRadioButtonSortByOffsetToLocalTime;
	private JRadioButton jRadioButtonSortByOffsetToUTC;
	
	private JPanel jPanelButtons;
	private JButton jButtonOk;
	private JButton jButtonCancel;
	
	
	/**
	 * Instantiates a new time zone selection dialog.
	 */
	public TimeZoneSelectionDialog() {
		this(null);
	}
	/**
	 * Instantiates a new time zone selection dialog.
	 * @param owner the owner window
	 */
	public TimeZoneSelectionDialog(Window owner) {
		super(owner);
		this.initialize();
	}
	private void initialize() {
		
		// --- Some general settings ------------------------------------------
		this.setTitle("Time Zone Selection");
		this.setModal(true);
		this.setSize(new Dimension(500, 500));
		this.setLocationRelativeTo(null);
		
		this.registerEscapeKeyStroke();
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
            	TimeZoneSelectionDialog.this.setCanceled(true);
            	TimeZoneSelectionDialog.this.setVisible(false);
			}
		});
		
		// --- Set initial configuration --------------------------------------
		this.getJRadioButtonSortById().setSelected(true);
		this.getJRadioButtonSortByOffsetToLocalTime().setSelected(false);
		this.getJRadioButtonSortByOffsetToUTC().setSelected(false);
		
		this.getJCheckboxShowDiffToLocalTime().setSelected(false);
		this.getJCheckboxShowDiffToUTC().setSelected(false);
		
		// --- Build-up visualization -----------------------------------------
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.insets = new Insets(10, 10, 0, 10);
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.gridwidth = 2;
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		getContentPane().add(getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jScrollPaneZoneIds = new GridBagConstraints();
		gbc_jScrollPaneZoneIds.insets = new Insets(5, 10, 0, 10);
		gbc_jScrollPaneZoneIds.gridwidth = 2;
		gbc_jScrollPaneZoneIds.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneZoneIds.gridx = 0;
		gbc_jScrollPaneZoneIds.gridy = 1;
		getContentPane().add(getJScrollPaneZoneIds(), gbc_jScrollPaneZoneIds);
		GridBagConstraints gbc_jListSearch = new GridBagConstraints();
		gbc_jListSearch.insets = new Insets(5, 10, 0, 0);
		gbc_jListSearch.anchor = GridBagConstraints.WEST;
		gbc_jListSearch.gridx = 0;
		gbc_jListSearch.gridy = 2;
		getContentPane().add(getJListSearch(), gbc_jListSearch);
		GridBagConstraints gbc_jTextFieldSearch = new GridBagConstraints();
		gbc_jTextFieldSearch.insets = new Insets(5, 5, 0, 10);
		gbc_jTextFieldSearch.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldSearch.gridx = 1;
		gbc_jTextFieldSearch.gridy = 2;
		getContentPane().add(getJTextFieldSearch(), gbc_jTextFieldSearch);
		GridBagConstraints gbc_jPanelSorting = new GridBagConstraints();
		gbc_jPanelSorting.insets = new Insets(5, 10, 0, 10);
		gbc_jPanelSorting.gridwidth = 2;
		gbc_jPanelSorting.fill = GridBagConstraints.BOTH;
		gbc_jPanelSorting.gridx = 0;
		gbc_jPanelSorting.gridy = 3;
		getContentPane().add(getJPanelSorting(), gbc_jPanelSorting);
		GridBagConstraints gbc_jCheckboxShowDiffToLocalTime = new GridBagConstraints();
		gbc_jCheckboxShowDiffToLocalTime.insets = new Insets(5, 10, 0, 10);
		gbc_jCheckboxShowDiffToLocalTime.anchor = GridBagConstraints.WEST;
		gbc_jCheckboxShowDiffToLocalTime.gridwidth = 2;
		gbc_jCheckboxShowDiffToLocalTime.gridx = 0;
		gbc_jCheckboxShowDiffToLocalTime.gridy = 4;
		getContentPane().add(getJCheckboxShowDiffToLocalTime(), gbc_jCheckboxShowDiffToLocalTime);
		GridBagConstraints gbc_jCheckboxShowDiffToUTC = new GridBagConstraints();
		gbc_jCheckboxShowDiffToUTC.insets = new Insets(0, 10, 0, 10);
		gbc_jCheckboxShowDiffToUTC.anchor = GridBagConstraints.WEST;
		gbc_jCheckboxShowDiffToUTC.gridwidth = 2;
		gbc_jCheckboxShowDiffToUTC.gridx = 0;
		gbc_jCheckboxShowDiffToUTC.gridy = 5;
		getContentPane().add(getJCheckboxShowDiffToUTC(), gbc_jCheckboxShowDiffToUTC);
		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.insets = new Insets(15, 10, 15, 10);
		gbc_jPanelButtons.gridwidth = 2;
		gbc_jPanelButtons.fill = GridBagConstraints.VERTICAL;
		gbc_jPanelButtons.gridx = 0;
		gbc_jPanelButtons.gridy = 6;
		getContentPane().add(getJPanelButtons(), gbc_jPanelButtons);
	}

	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
            	TimeZoneSelectionDialog.this.setCanceled(true);
            	TimeZoneSelectionDialog.this.setVisible(false);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
	
	
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Please, select a time zone from the list below");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	private JScrollPane getJScrollPaneZoneIds() {
		if (jScrollPaneZoneIds == null) {
			jScrollPaneZoneIds = new JScrollPane();
			jScrollPaneZoneIds.setViewportView(this.getJListZoneIds());
		}
		return jScrollPaneZoneIds;
	}
	private JList<TimeZoneIdAndOffsets> getJListZoneIds() {
		if (jListZoneIds == null) {
			jListZoneIds = new JList<>(this.getTimeZoneListModel());
			jListZoneIds.setFont(new Font("Dialog", Font.PLAIN, 12));
			jListZoneIds.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			// ----------------------------------------------------------------
			// --- Define list cell renderer ----------------------------------
			// ----------------------------------------------------------------
			jListZoneIds.setCellRenderer(new DefaultListCellRenderer() {
				
				private static final long serialVersionUID = 8590153419197911186L;
				
				private final Color colBackground = Color.WHITE;
				private final Color colBackgroundSelected = new Color(57, 105, 138);
				private final Color colTextForeground = new Color(35, 35, 36);
				private final Color colTextForegroundSelected = Color.WHITE;
				
				/* (non-Javadoc)
				 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
				 */
				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

					Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

					JLabel displayElement = (JLabel) comp;
					boolean includingOffsetToLocal = TimeZoneSelectionDialog.this.getJCheckboxShowDiffToLocalTime().isSelected();
					boolean includingOffsetToUTC = TimeZoneSelectionDialog.this.getJCheckboxShowDiffToUTC().isSelected();
					boolean withLeadingOffset = true;
					
					TimeZoneIdAndOffsets tzoInstance = (TimeZoneIdAndOffsets) value;
					displayElement.setText(tzoInstance.getZoneDescription(includingOffsetToLocal, includingOffsetToUTC, withLeadingOffset));
					
					// --- Visual settings -----------------
					displayElement.setOpaque(true);
					if (isSelected == true) {
						comp.setForeground(colTextForegroundSelected);
						comp.setBackground(colBackgroundSelected);
					} else {
						comp.setForeground(colTextForeground);
						comp.setBackground(colBackground);
					}
					return comp;
				}
			});
			
			// ----------------------------------------------------------------
			// --- React on double click --------------------------------------
			// ----------------------------------------------------------------
			jListZoneIds.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {
					if (me.getClickCount()==2) {
						// --- Double Click -----
						TimeZoneSelectionDialog.this.getJButtonOk().doClick();
					}
				}
			});
		}
		return jListZoneIds;
	}
	private DefaultListModel<TimeZoneIdAndOffsets> getTimeZoneListModel() {
		if (listModel==null) {
			listModel = new DefaultListModel<>();
			this.refillListModel();
		}
		return listModel;
	}
	private DefaultListModel<TimeZoneIdAndOffsets> getTimeZoneListModelFiltered() {
		if (listModelFiltered==null) {
			listModelFiltered = new DefaultListModel<>();
		}
		return listModelFiltered;
	}
	
	private JLabel getJListSearch() {
		if (jListSearch == null) {
			jListSearch = new JLabel("Search");
			jListSearch.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jListSearch;
	}
	private JTextField getJTextFieldSearch() {
		if (jTextFieldSearch == null) {
			jTextFieldSearch = new JTextField();
			jTextFieldSearch.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					TimeZoneSelectionDialog.this.setJListModel(true);
				}
				@Override
				public void insertUpdate(DocumentEvent e) {
					TimeZoneSelectionDialog.this.setJListModel(true);
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
					TimeZoneSelectionDialog.this.setJListModel(true);
				}
			});
		}
		return jTextFieldSearch;
	}
	private JCheckBox getJCheckboxShowDiffToLocalTime() {
		if (jCheckboxShowDiffToLocalTime == null) {
			jCheckboxShowDiffToLocalTime = new JCheckBox("Show current offset to local time");
			jCheckboxShowDiffToLocalTime.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckboxShowDiffToLocalTime.addActionListener(this);
		}
		return jCheckboxShowDiffToLocalTime;
	}
	private JCheckBox getJCheckboxShowDiffToUTC() {
		if (jCheckboxShowDiffToUTC == null) {
			jCheckboxShowDiffToUTC = new JCheckBox("Show current offset to UTC");
			jCheckboxShowDiffToUTC.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckboxShowDiffToUTC.addActionListener(this);
		}
		return jCheckboxShowDiffToUTC;
	}
	private JPanel getJPanelSorting() {
		if (jPanelSorting == null) {
			jPanelSorting = new JPanel();
			GridBagLayout gbl_jPanelSorting = new GridBagLayout();
			gbl_jPanelSorting.columnWidths = new int[]{0, 0, 0, 0, 0};
			gbl_jPanelSorting.rowHeights = new int[]{0, 0};
			gbl_jPanelSorting.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelSorting.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelSorting.setLayout(gbl_jPanelSorting);
			GridBagConstraints gbc_jLabelSort = new GridBagConstraints();
			gbc_jLabelSort.gridx = 0;
			gbc_jLabelSort.gridy = 0;
			jPanelSorting.add(getJLabelSort(), gbc_jLabelSort);
			GridBagConstraints gbc_jRadioButtonSortById = new GridBagConstraints();
			gbc_jRadioButtonSortById.insets = new Insets(0, 5, 0, 0);
			gbc_jRadioButtonSortById.gridx = 1;
			gbc_jRadioButtonSortById.gridy = 0;
			jPanelSorting.add(getJRadioButtonSortById(), gbc_jRadioButtonSortById);
			GridBagConstraints gbc_jRadioButtonSortByOffsetToLocalTime = new GridBagConstraints();
			gbc_jRadioButtonSortByOffsetToLocalTime.insets = new Insets(0, 5, 0, 0);
			gbc_jRadioButtonSortByOffsetToLocalTime.gridx = 2;
			gbc_jRadioButtonSortByOffsetToLocalTime.gridy = 0;
			jPanelSorting.add(getJRadioButtonSortByOffsetToLocalTime(), gbc_jRadioButtonSortByOffsetToLocalTime);
			GridBagConstraints gbc_jRadioButtonSortByOffsetToUTC = new GridBagConstraints();
			gbc_jRadioButtonSortByOffsetToUTC.insets = new Insets(0, 5, 0, 0);
			gbc_jRadioButtonSortByOffsetToUTC.gridx = 3;
			gbc_jRadioButtonSortByOffsetToUTC.gridy = 0;
			jPanelSorting.add(getJRadioButtonSortByOffsetToUTC(), gbc_jRadioButtonSortByOffsetToUTC);
			
			ButtonGroup bg = new ButtonGroup();
			bg.add(this.getJRadioButtonSortById());
			bg.add(this.getJRadioButtonSortByOffsetToLocalTime());
			bg.add(this.getJRadioButtonSortByOffsetToUTC());
			
		}
		return jPanelSorting;
	}
	private JLabel getJLabelSort() {
		if (jLabelSort == null) {
			jLabelSort = new JLabel("Sort by:");
			jLabelSort.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSort;
	}
	private JRadioButton getJRadioButtonSortById() {
		if (jRadioButtonSortById == null) {
			jRadioButtonSortById = new JRadioButton("Zone ID");
			jRadioButtonSortById.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonSortById.addActionListener(this);
		}
		return jRadioButtonSortById;
	}
	private JRadioButton getJRadioButtonSortByOffsetToLocalTime() {
		if (jRadioButtonSortByOffsetToLocalTime == null) {
			jRadioButtonSortByOffsetToLocalTime = new JRadioButton("Offset to local time (" + ZoneId.systemDefault().getId() + ")");
			jRadioButtonSortByOffsetToLocalTime.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonSortByOffsetToLocalTime.addActionListener(this);
		}
		return jRadioButtonSortByOffsetToLocalTime;
	}
	private JRadioButton getJRadioButtonSortByOffsetToUTC() {
		if (jRadioButtonSortByOffsetToUTC == null) {
			jRadioButtonSortByOffsetToUTC = new JRadioButton("Offset to UTC");
			jRadioButtonSortByOffsetToUTC.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonSortByOffsetToUTC.addActionListener(this);
		}
		return jRadioButtonSortByOffsetToUTC;
	}
	
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_jPanelButtons = new GridBagLayout();
			gbl_jPanelButtons.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelButtons.rowHeights = new int[]{0, 0};
			gbl_jPanelButtons.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelButtons.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelButtons.setLayout(gbl_jPanelButtons);
			GridBagConstraints gbc_jButtonOk = new GridBagConstraints();
			gbc_jButtonOk.insets = new Insets(0, 0, 0, 20);
			gbc_jButtonOk.gridx = 0;
			gbc_jButtonOk.gridy = 0;
			jPanelButtons.add(getJButtonOk(), gbc_jButtonOk);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(0, 20, 0, 0);
			gbc_jButtonCancel.gridx = 1;
			gbc_jButtonCancel.gridy = 0;
			jPanelButtons.add(getJButtonCancel(), gbc_jButtonCancel);
		}
		return jPanelButtons;
	}
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton("Ok");
			jButtonOk.setPreferredSize(new Dimension(80, 26));
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.setForeground(new Color(0, 153, 0));
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setPreferredSize(new Dimension(80, 26));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	
	
	/**
	 * Fills the list model.
	 */
	private void refillListModel() {
		
		// --- Remind currently selected element --------------------
		TimeZoneIdAndOffsets tzSelected = null;
		if (this.getJListZoneIds()!=null) {
			tzSelected = this.getJListZoneIds().getSelectedValue();
		}
		
		// --- Get the ZoneId's as list -----------------------------
		List<TimeZoneIdAndOffsets> zoneIDList = null;
		if (this.getTimeZoneListModel().size()==0) {
			// --- Get fresh / new instances ------------------------
			zoneIDList = TimeZoneIdAndOffsets.getAvailableZoneIdsWithOffsets();
		} else {
			// -- Reuse the available instances ---------------------
			zoneIDList = new ArrayList<>();
			for (int i = 0; i < this.getTimeZoneListModel().size(); i++) {
				zoneIDList.add(this.getTimeZoneListModel().get(i)); 
			}
		}
		
		// --- Sort the list according to the settings --------------
		if (this.getJRadioButtonSortById().isSelected()) {
			Collections.sort(zoneIDList, TimeZoneIdAndOffsets.getComparatorForZoneId());
		} else if (this.getJRadioButtonSortByOffsetToLocalTime().isSelected()) {
			Collections.sort(zoneIDList, TimeZoneIdAndOffsets.getComparatorForOffsetToLocalTime());
		} else if (this.getJRadioButtonSortByOffsetToUTC().isSelected()) {
			Collections.sort(zoneIDList, TimeZoneIdAndOffsets.getComparatorForOffsetToUTC());
		}
		
		// --- Clear and fill the list model ------------------------
		this.getTimeZoneListModel().clear();
		for (int i = 0; i < zoneIDList.size(); i++) {
			this.getTimeZoneListModel().addElement(zoneIDList.get(i));
		}
		
		// --- Set the actual list model to the JList ---------------
		this.setJListModel(false);
		
		// --- Reselect ---------------------------------------------
		if (tzSelected!=null) {
			this.getJListZoneIds().setSelectedValue(tzSelected, true);
		}
	}
	
	/**
	 * Sets the right model to the JList (complete or filtered).
	 * @param reselctSelectedElement the indicator to reselect the current selection 
	 */
	private void setJListModel(boolean reselctSelectedElement) {
		
		// --- Remind selected element? -----------------------------
		TimeZoneIdAndOffsets tzSelected = null;
		if (reselctSelectedElement==true && this.getJListZoneIds()!=null) {
			tzSelected = this.getJListZoneIds().getSelectedValue();
		}
		
		// --- Set the list model to the JList ----------------------
		if (this.getJTextFieldSearch().getText().isEmpty()==false) {
			this.updateTimeZoneListModelFiltered();
			this.getJListZoneIds().setModel(this.getTimeZoneListModelFiltered());
		} else {
			this.getJListZoneIds().setModel(this.getTimeZoneListModel());
		}
		
		// --- Reselect ---------------------------------------------
		if (reselctSelectedElement==true && tzSelected!=null) {
			this.getJListZoneIds().setSelectedValue(tzSelected, true);
		}
	}

	/**
	 * Update time zone list model filtered.
	 */
	private void updateTimeZoneListModelFiltered() {
		
		String searchPhrase = this.getJTextFieldSearch().getText().trim();
		if (searchPhrase.isEmpty()==false) {
			
			// --- Reset filtered list model --------------------
			this.getTimeZoneListModelFiltered().clear();
			
			// --- Split search phrase by blanks ---------------- 
			String[] searchArray = searchPhrase.toLowerCase().split(" ");

			// --- Filter for searchArray elements -------------- 
			for (int i = 0; i < this.getTimeZoneListModel().size(); i++) {
				
				TimeZoneIdAndOffsets tzo = this.getTimeZoneListModel().get(i);
				String tzoID = tzo.getZoneID().getId().toLowerCase();
				boolean addToFilteredList = true;
				
				// --- Search for all expressions ---------------
				for (int j = 0; j < searchArray.length; j++) {
					String searchExpresion = searchArray[j];
					if (tzoID.indexOf(searchExpresion)==-1) {
						addToFilteredList = false;
						break;
					}
				}
				// --- Add to filtered list? -------------------- 
				if (addToFilteredList==true) {
					this.listModelFiltered.addElement(tzo);
				}
			}
		
		} else {
			this.listModelFiltered = this.getTimeZoneListModel();
		}
	}
	
	/**
	 * Return the TimeZoneIdAndOffsets instance out of the list model and the specified {@link ZoneId}.
	 *
	 * @param zoneId the zone id to search for
	 * @return the time zone id and offsets
	 */
	private TimeZoneIdAndOffsets getTimeZoneIdAndOffsets(ZoneId zoneId) {
		
		if (zoneId==null) return null;
		
		TimeZoneIdAndOffsets tzoForFound = null; 
		TimeZoneIdAndOffsets tzoForCheck = new TimeZoneIdAndOffsets(zoneId);
		int modelIndex = this.getTimeZoneListModel().indexOf(tzoForCheck);
		if (modelIndex!=-1) {
			tzoForFound = this.getTimeZoneListModel().get(modelIndex);
		}
		return tzoForFound;
	}
	
	/**
	 * Checks if is error.
	 * @return true, if is error
	 */
	private boolean isError() {
		
		boolean isError = false;
		
		TimeZoneIdAndOffsets zoneIdSelected = this.getJListZoneIds().getSelectedValue();
		if (zoneIdSelected==null) {
			JOptionPane.showMessageDialog(this, "No Time Zone was selected!", this.getTitle(), JOptionPane.ERROR_MESSAGE, null);
			isError = true;
		}
		
		return isError;
	}
	
	/**
	 * Returns the currently selected {@link ZoneId}.
	 * @return the zone id
	 */
	public ZoneId getZoneId() {
		TimeZoneIdAndOffsets zoneIdSelected = this.getJListZoneIds().getSelectedValue();
		if (zoneIdSelected!=null) {
			return zoneIdSelected.getZoneID();
		}
		return null;
	}
	/**
	 * Sets the zone id.
	 * @param zoneId the new zone id
	 */
	public void setZoneId(ZoneId zoneId) {
		TimeZoneIdAndOffsets tzoFound = this.getTimeZoneIdAndOffsets(zoneId);
		if (tzoFound!=null) {
			this.getJListZoneIds().setSelectedValue(tzoFound, true);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Dialog#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean setVisible) {
		// --- Do own stuff first ---------------
		if (setVisible==true) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					TimeZoneSelectionDialog.this.getJTextFieldSearch().requestFocus();
				}
			});
		}
		// --- Finally, do super class stuff ---- 
		super.setVisible(setVisible);
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
	 * @param cannceld the new canceled
	 */
	private void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJRadioButtonSortById()) {
			if (this.getJCheckboxShowDiffToLocalTime().isSelected()==false || this.getJCheckboxShowDiffToUTC().isSelected()==false) {
				this.getJCheckboxShowDiffToLocalTime().setSelected(false);
				this.getJCheckboxShowDiffToUTC().setSelected(false);
			}
			this.refillListModel();
			
		} else if (ae.getSource()==this.getJRadioButtonSortByOffsetToLocalTime()) {
			if (this.getJCheckboxShowDiffToLocalTime().isSelected()==false || this.getJCheckboxShowDiffToUTC().isSelected()==false) {
				this.getJCheckboxShowDiffToLocalTime().setSelected(true);
				this.getJCheckboxShowDiffToUTC().setSelected(false);
			}
			this.refillListModel();
			
		} else if (ae.getSource()==this.getJRadioButtonSortByOffsetToUTC()) {
			if (this.getJCheckboxShowDiffToLocalTime().isSelected()==false || this.getJCheckboxShowDiffToUTC().isSelected()==false) {
				this.getJCheckboxShowDiffToLocalTime().setSelected(false);
				this.getJCheckboxShowDiffToUTC().setSelected(true);
			}
			this.refillListModel();

			
		} else if (ae.getSource()==this.getJCheckboxShowDiffToLocalTime()) {
			this.getJListZoneIds().repaint();
		} else if (ae.getSource()==this.getJCheckboxShowDiffToUTC()) {
			this.getJListZoneIds().repaint();
			
		} else if (ae.getSource()==this.getJButtonCancel()) {
			this.setCanceled(true);
			this.setVisible(false);
			
		} else if (ae.getSource()==this.getJButtonOk()) {
			if (this.isError()==false) {
				this.setCanceled(false);
				this.setVisible(false);
			}
		}
		
	}
	
}
