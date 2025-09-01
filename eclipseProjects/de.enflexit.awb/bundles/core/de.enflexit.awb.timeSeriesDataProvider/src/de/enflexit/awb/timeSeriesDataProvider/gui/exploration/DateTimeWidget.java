package de.enflexit.awb.timeSeriesDataProvider.gui.exploration;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.enflexit.common.swing.TimeZoneJButton;
import de.enflexit.common.swing.TimeZoneJButton.TimeZoneDialogPosition;
import de.enflexit.common.swing.JSpinnerDateTime;

/**
 * The Class DateTimeWidget can be used to set a date time and a time zone.
 * To react on configuration changes, add a {@link ChangeListener} to the widget instance.
 * <p>
 * The visual representation can be configured by using the methods:<br>
 * - {@link #setEnabledTimeZoneConfiguration(boolean)}<br>
 * - {@link #setFont(Font)}<br>
 * - {@link #setFontOfJLabelBold(boolean)}<br>
 * </p>
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DateTimeWidget extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 7999627310803332872L;
	
	private JLabel jLabelStartDate = null;
	private JSpinnerDateTime jSpinnerDateTime = null;
	private JLabel jLabelStartMillis = null;
	private JSpinner jSpinnerMillis = null;
	private TimeZoneJButton jButtonTimeZone;

	private Vector<ChangeListener> listener;
	private boolean pauseChangeListener=false;
	
	
	/**
	 * Instantiates a new DateTimeWidget.
	 */
	public DateTimeWidget() {
		this(null, null);
	}
	/**
	 * Instantiates a new DateTimeWidget.
	 * @param currentDate the current date
	 */
	public DateTimeWidget(Date currentDate) {
		this(currentDate, null);
	}
	/**
	 * Instantiates a new DateTimeWidget.
	 * @param timestamp the timestamp
	 */
	public DateTimeWidget(long timestamp) {
		this(timestamp, null);
	}
	
	/**
	 * Instantiates a new date time widget.
	 *
	 * @param currentDate the current date
	 * @param zoneId the zone id
	 */
	public DateTimeWidget(long timestamp, ZoneId zoneId) {
		this.initialize();
		this.setDate(timestamp);
		this.setZoneID(zoneId);
	}
	/**
	 * Instantiates a new date time widget.
	 *
	 * @param currentDate the current date
	 * @param zoneId the zone id
	 */
	public DateTimeWidget(Date currentDate, ZoneId zoneId) {
		this.initialize();
		this.setDate(currentDate);
		this.setZoneID(zoneId);
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {

        this.setPreferredSize(new Dimension(370, 28));
        this.setMinimumSize(new Dimension(370, 28));
        
        GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
        
        GridBagConstraints gbc_jLabelStartDate = new GridBagConstraints();
        gbc_jLabelStartDate.anchor = GridBagConstraints.WEST;
        gbc_jLabelStartDate.insets = new Insets(0, 0, 0, 5);
        gbc_jLabelStartDate.gridx = 0;
        gbc_jLabelStartDate.gridy = 0;

        GridBagConstraints gbc_jSpinnerDateStart = new GridBagConstraints();
        gbc_jSpinnerDateStart.fill = GridBagConstraints.HORIZONTAL;
        gbc_jSpinnerDateStart.insets = new Insets(0, 0, 0, 5);
        gbc_jSpinnerDateStart.gridx = 1;
        gbc_jSpinnerDateStart.gridy = 0;

        GridBagConstraints gbc_jLabelStartMillis = new GridBagConstraints();
        gbc_jLabelStartMillis.anchor = GridBagConstraints.WEST;
        gbc_jLabelStartMillis.insets = new Insets(0, 0, 0, 5);
        gbc_jLabelStartMillis.gridx = 2;
        gbc_jLabelStartMillis.gridy = 0;
       
        GridBagConstraints gbc_jSpinnerMillisStart = new GridBagConstraints();
        gbc_jSpinnerMillisStart.insets = new Insets(0, 0, 0, 5);
        gbc_jSpinnerMillisStart.anchor = GridBagConstraints.WEST;
        gbc_jSpinnerMillisStart.gridx = 3;
        gbc_jSpinnerMillisStart.gridy = 0;

        GridBagConstraints gbc_jButtonTimeZone = new GridBagConstraints();
		gbc_jButtonTimeZone.gridx = 4;
		gbc_jButtonTimeZone.gridy = 0;
		
        this.add(this.getJLabelDate(), gbc_jLabelStartDate);
        this.add(this.getJSpinnerDateTime(), gbc_jSpinnerDateStart);
        this.add(this.getJLabelMillis(), gbc_jLabelStartMillis);
        this.add(this.getJSpinnerMillis(), gbc_jSpinnerMillisStart);
		this.add(this.getJButtonTimeZone(), gbc_jButtonTimeZone);
	}

	private JLabel getJLabelDate() {
		if (jLabelStartDate==null) {
			jLabelStartDate = new JLabel();
	        jLabelStartDate.setText("Date - Time:");
		}
		return jLabelStartDate;
	}
	public void setTextForDate(String newText) {
		this.getJLabelDate().setText(newText);
	}
	public void setTextWidthForDate(int textWidth) {
		this.getJLabelDate().setPreferredSize(new Dimension(textWidth, 26));
		this.getJLabelDate().setMinimumSize(new Dimension(textWidth, 26));
		this.getJLabelDate().setMaximumSize(new Dimension(textWidth, 26));
		this.validate();
		this.repaint();
	}
	
	private JLabel getJLabelMillis() {
		if (jLabelStartMillis==null) {
			jLabelStartMillis = new JLabel();
			jLabelStartMillis.setText("Millis:");
		}
		return jLabelStartMillis;
	}
	public void setTextForMillis(String newText) {
		this.getJLabelMillis().setText(newText);
	}
	public void setTextWidthForMillis(int textWidth) {
		this.getJLabelMillis().setPreferredSize(new Dimension(textWidth, 26));
		this.getJLabelMillis().setMinimumSize(new Dimension(textWidth, 26));
		this.getJLabelMillis().setMaximumSize(new Dimension(textWidth, 26));
		this.validate();
		this.repaint();
	}

	
	private JSpinnerDateTime getJSpinnerDateTime() {
		if (jSpinnerDateTime==null) {
			jSpinnerDateTime = new JSpinnerDateTime("dd.MM.yyyy - HH:mm:ss");
			jSpinnerDateTime.setPreferredSize(new Dimension(160, 28));
			jSpinnerDateTime.addChangeListener(this);
		}
		return jSpinnerDateTime;
	}
	private JSpinner getJSpinnerMillis() {
		if (jSpinnerMillis==null) {
			jSpinnerMillis = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
			jSpinnerMillis.setEditor(new JSpinner.NumberEditor(jSpinnerMillis, "000"));
			jSpinnerMillis.setPreferredSize(new Dimension(60, 28));
			jSpinnerMillis.addChangeListener(this);
		}
		return jSpinnerMillis;
	}
	private void updateJSpinnerZoneId() {
		try {
			this.getJSpinnerDateTime().setTimeZone(this.getJButtonTimeZone().getTimeZone());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private TimeZoneJButton getJButtonTimeZone() {
		if (jButtonTimeZone == null) {
			jButtonTimeZone = new TimeZoneJButton(null, TimeZoneDialogPosition.BelowRightJustified);
			jButtonTimeZone.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DateTimeWidget.this.updateJSpinnerZoneId();
					DateTimeWidget.this.stateChanged(new ChangeEvent(this));
				}
			});
		}
		return jButtonTimeZone;
	}
	
	/**
	 * Sets the time zone configuration enabled or disabled.
	 * @param b the new enabled time zone configuration
	 */
	public void setEnabledTimeZoneConfiguration(boolean setEnabled) {
		if (this.getJButtonTimeZone().isVisible()!=setEnabled) {
			this.getJButtonTimeZone().setVisible(setEnabled);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.getJLabelDate().setEnabled(enabled);
		this.getJSpinnerDateTime().setEnabled(enabled);
		this.getJLabelMillis().setEnabled(enabled);
		this.getJSpinnerMillis().setEnabled(enabled);
		this.getJButtonTimeZone().setEnabled(enabled);
		super.setEnabled(enabled);
	}

	/**
	 * Sets this widget editable or not.
	 * @param editable the new editable
	 */
	public void setEditable(boolean editable) {
		
		this.getJSpinnerDateTime().setEnabled(editable);
		((DefaultEditor) this.getJSpinnerDateTime().getEditor()).getTextField().setEnabled(true);
		((DefaultEditor) this.getJSpinnerDateTime().getEditor()).getTextField().setEditable(editable);

		this.getJSpinnerMillis().setEnabled(editable);
		((DefaultEditor) this.getJSpinnerMillis().getEditor()).getTextField().setEnabled(true);
		((DefaultEditor) this.getJSpinnerMillis().getEditor()).getTextField().setEditable(editable);
		
		this.getJButtonTimeZone().setEnabled(editable);
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setFont(java.awt.Font)
	 */
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		this.getJLabelDate().setFont(font);
		this.getJSpinnerDateTime().setFont(font);
		this.getJLabelMillis().setFont(font);
		this.getJSpinnerMillis().setFont(font);
	}
	/**
	 * Sets the font style of all JLabel to bold or plain.
	 * @param isSetJLabelBold the indicator to set the font of JLabel to bold or plain
	 */
	public void setFontOfJLabelBold(boolean isSetJLabelBold) {
	
		Font currFont = this.getJLabelDate().getFont();
		Font font = null; 
		if (isSetJLabelBold==true) {
			font = new Font(currFont.getFamily(), Font.BOLD, currFont.getSize());
		} else {
			font = new Font(currFont.getFamily(), Font.PLAIN, currFont.getSize());
		}
		this.getJLabelDate().setFont(font);
		this.getJLabelMillis().setFont(font);
	}
	
	
	/**
	 * Sets the date.
	 * @param timeStamp the new date
	 */
	public void setDate(long timeStamp) {
		this.setDate(new Date(timeStamp));
	}
	/**
	 * Sets the date.
	 * @param newDate the new date
	 */
	public void setDate(Date newDate) {

		if (newDate==null) return;
		
		Calendar calendarWork = Calendar.getInstance();
		calendarWork.setTime(newDate);
		
		this.pauseChangeListener=true;
		this.getJSpinnerDateTime().setValue(new Date(newDate.getTime()));
		this.getJSpinnerMillis().setValue(calendarWork.get(Calendar.MILLISECOND));
		this.pauseChangeListener=false;
	}
	/**
	 * Gets the current configured date.
	 * @return the date
	 */
	public Date getDate() {
		
		// --- Get date time as Long ----------------------
		Date startDate = (Date) this.getJSpinnerDateTime().getValue();
		int startMillis = (Integer) this.getJSpinnerMillis().getValue();
		
		Calendar calendarWork = Calendar.getInstance();
		calendarWork.setTime(startDate);
		calendarWork.set(Calendar.MILLISECOND, startMillis);
		
		return calendarWork.getTime();
	}
	
	
	/**
	 * Sets the ZoneId currently to use.
	 * @param zoneId the new zone ID
	 */
	public void setZoneID(ZoneId zoneId) {
		this.getJButtonTimeZone().setZoneId(zoneId);
		this.updateJSpinnerZoneId();
	}
	/**
	 * Return the currently configured ZoneId.
	 * @return the zone id
	 */
	public ZoneId getZoneId() {
		return this.getJButtonTimeZone().getZoneId();
	}
	
	
	/**
	 * Sets a lower bound for the time range that can be selected by the widget.
	 * @param timestamp The timestamp for the lower bound
	 */
	public void setMinimumDate(long timestamp){
		this.setMinimumDate(new Date(timestamp));
	}
	/**
	 * Sets a lower bound for the time range that can be selected by the widget.
	 * @param minimumDate The lower bound
	 */
	public void setMinimumDate(Date minimumDate){
		SpinnerDateModel spinnerModelDate = (SpinnerDateModel) this.getJSpinnerDateTime().getModel();
		spinnerModelDate.setStart(minimumDate);
	}

	/**
	 * Sets a upper bound for the time range that can be selected by the widget.
	 * @param timestamp The timestamp for the upper bound
	 */
	public void setMaximumDate(long timestamp){
		this.setMaximumDate(new Date(timestamp));
	}
	/**
	 * Sets an upper bound for the time range that can be selected by the widget.
	 * @param maximumDate The upper bound
	 */
	public void setMaximumDate(Date maximumDate){
		SpinnerDateModel spinnerModelDate = (SpinnerDateModel) this.getJSpinnerDateTime().getModel();
		spinnerModelDate.setEnd(maximumDate);
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent ce) {
		// --- Capture ChangeEvent of local JSPinner ------
		if (pauseChangeListener==false && this.getChangeListener().size()>0) {
			// --- Create and fire own ChangeEvent -------- 
			ChangeEvent newCE = new ChangeEvent(this);
			for (ChangeListener cl : this.getChangeListener()) {
				cl.stateChanged(newCE);
			}	
		}
	}
	/**
	 * Returns the currently registered ChangeListener.
	 * @return the change listener
	 */
	private Vector<ChangeListener> getChangeListener() {
		if (listener==null) {
			listener = new Vector<ChangeListener>();
		}
		return listener;
	}
	/**
	 * Adds a change listener to this widget.
	 * @param cl the new {@link ChangeListener}
	 */
	public void addChangeListener(ChangeListener cl) {
		if (this.getChangeListener().contains(cl)==false) {
			this.getChangeListener().add(cl);
		}
	}
	/**
	 * Removes the specified {@link ChangeListener}.
	 * @param cl the {@link ChangeListener}
	 */
	public void removeChangeListener(ChangeListener cl) {
		this.getChangeListener().remove(cl);
	}
	
}  
