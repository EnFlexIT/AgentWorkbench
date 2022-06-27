package de.enflexit.common.swing;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.Vector;

import javax.swing.JButton;

import de.enflexit.common.BundleHelper;

/**
 * The class TimeZoneJButton provides a 28 x 28 sized JButton that will open 
 * a pop-up dialog to select a {@link ZoneId} to use.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TimeZoneJButton extends JButton {

	private static final long serialVersionUID = 1754429867095026305L;

	public enum TimeZoneDialogPosition {
		AboveRightJustified,
		AboveLeftJustified,
		BelowRightJustified,
		BelowLeftJustified,
	}
	
	private ZoneId zoneId;
	private TimeZoneDialogPosition timeZoneDialogPosition;
	
	private Vector<ActionListener> actionListeners;
	
	/**
	 * Instantiates a new time zone button.
	 */
	public TimeZoneJButton() {
		this(null, null);
	}
	/**
	 * Instantiates a new time zone button.
	 * @param zoneId the current ZoneId
	 */
	public TimeZoneJButton(ZoneId zoneId) {
		this(zoneId, null);
	}
	/**
	 * Instantiates a new time zone button.
	 * @param zoneId the current ZoneId
	 * @param dialogPosition the dialog position
	 */
	public TimeZoneJButton(ZoneId zoneId, TimeZoneDialogPosition dialogPosition) {
		this.initialize();
		this.setZoneId(zoneId);
		this.setTimeZoneDialogPosition(dialogPosition);
		this.updateZoneIdToolTip();
	}
	/**
	 * Updates ZoneId tool tip.
	 */
	private void updateZoneIdToolTip() {
		String id = this.getZoneId().getId();
		String tipText = "Change current Time Zone '" + id + "' to ...";
		this.setToolTipText(tipText);
	}
	/**
	 * Initializes the button.
	 */
	private void initialize() {
		this.setIcon(BundleHelper.getImageIcon("TimeZone.png"));
		this.setPreferredSize(new Dimension(28, 28));
		super.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				TimeZoneJButton.this.openTimeZonePopUp();
			}
		});
	}
	
	/**
	 * Returns the owner frame for the specified Container.
	 * 
	 * @param container the container
	 * @return the owner frame for component or null, if no container was specified
	 */
	private Frame getOwnerFrameForContainer(Container container) {
		if (container==null) return null;
		Frame frameFound = null;
		Container currComp = container;
		while (currComp!=null) {
			if (currComp instanceof Frame) {
				frameFound = (Frame) currComp;
				break;
			}
			currComp = currComp.getParent();
		}
		return frameFound;
	}
	/**
	 * Opens the {@link TimeZoneWidgetDialog} to edit the time zone.
	 */
	private void openTimeZonePopUp() {

		// --- Initiate TimeZoneDialog ------------------------------
		Frame owner = this.getOwnerFrameForContainer(this.getParent());
		TimeZoneWidgetDialog timeZoneDialog = new TimeZoneWidgetDialog(owner, this.getZoneId());
		
		timeZoneDialog.setSize((int)this.getParent().getSize().getWidth(), timeZoneDialog.getHeight());
		
		
		// --- Set position -----------------------------------------
		int posX = 0;
		int posY = 0; 

		switch (this.getTimeZoneDialogPosition()) {
		case AboveLeftJustified:
			posX = (int) this.getLocationOnScreen().getX();
			posY = (int) this.getLocationOnScreen().getY() - timeZoneDialog.getHeight();
			break;
		
		case AboveRightJustified:
			posX = (int) this.getLocationOnScreen().getX() - timeZoneDialog.getWidth() + this.getWidth();
			posY = (int) this.getLocationOnScreen().getY() - timeZoneDialog.getHeight();
			break;

		case BelowLeftJustified:
			posX = (int) this.getLocationOnScreen().getX();
			posY = (int) this.getLocationOnScreen().getY() + this.getHeight(); 
			break;
		
		case BelowRightJustified:
			posX = (int) this.getLocationOnScreen().getX() - timeZoneDialog.getWidth() + this.getWidth();
			posY = (int) this.getLocationOnScreen().getY() + this.getHeight();
			break;
		}
		timeZoneDialog.setLocation(posX, posY);	

		timeZoneDialog.setModal(true);
		timeZoneDialog.setVisible(true);
		// ----------------------------------------------------------
		// --- Wait for user selection or cancel action -------------
		// ----------------------------------------------------------
		if (timeZoneDialog.isCanceled()==false) {
			ZoneId newZoneID = timeZoneDialog.getZoneId();
			if (newZoneID.equals(this.getZoneId())==false) {
				// --- Set new instance locally ----------------------
				this.setZoneId(newZoneID);
				// --- Fire action event for external listener ------
				TimeZoneJButton.this.notifyListeners();
			}
		}
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
		this.updateZoneIdToolTip();
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
	
	/**
	 * Returns the time zone dialog position.
	 * @return the time zone dialog position
	 */
	private TimeZoneDialogPosition getTimeZoneDialogPosition() {
		if (timeZoneDialogPosition==null) {
			timeZoneDialogPosition = TimeZoneDialogPosition.BelowRightJustified;
		}
		return timeZoneDialogPosition;
	}
	/**
	 * Sets the time zone dialog position.
	 * @param timeZoneDialogPosition the new time zone dialog position
	 */
	public void setTimeZoneDialogPosition(TimeZoneDialogPosition timeZoneDialogPosition) {
		this.timeZoneDialogPosition = timeZoneDialogPosition;
	}
	
	// ------------------------------------------------------------------------
	// --- Till here, public data getter and setter methods -------------------
	// ------------------------------------------------------------------------	

	/**
	 * Adds an {@link ActionListener} to the widget.
	 * @param actionListener the action listener
	 */
	public void addActionListener(ActionListener actionListener) {
		this.getInternalActionListeners().add(actionListener);
	}
	
	/**
	 * Removes an {@link ActionListener} from the widget.
	 * @param actionListener the action listener
	 */
	public void removeActionListeners(ActionListener actionListener) {
		this.getInternalActionListeners().remove(actionListener);
	}
	/**
	 * Gets the registered action listeners.
	 * @return the action listeners
	 */
	private Vector<ActionListener> getInternalActionListeners() {
		if (actionListeners==null) {
			actionListeners = new Vector<ActionListener>();
		}
		return actionListeners;
	}
	/**
	 * Notifies all registered listeners about a change of the selected time zone. 
	 */
	private void notifyListeners() {
		ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, this.getActionCommand());
		for (int i=0; i<this.getInternalActionListeners().size(); i++) {
			this.getInternalActionListeners().get(i).actionPerformed(ae);
		}
	}	
	
}
