package de.enflexit.common.swing;

/**
 * The class TimeSelection can be used for a ComboBoxModel of the sampling interval as user object.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeSelection {

	private long timeInMill = 0;
	
	/**
	 * Instantiates a new time selection.
	 * @param timeInMillis the time in milliseconds
	 */
	public TimeSelection(long timeInMillis) {
		this.timeInMill = timeInMillis;
	}
	
	/**
	 * Gets the time in milliseconds.
	 * @return the time in milliseconds
	 */
	public long getTimeInMill() {
		return timeInMill;
	}
	/**
	 * Sets the time in milliseconds.
	 * @param timeInMill the milliseconds to set
	 */
	public void setTimeInMill(int timeInMill) {
		this.timeInMill = timeInMill;
	}
	
	/**
	 * Converts the milliseconds into seconds.
	 * @return the text to display in seconds
	 */
	public String toString() {
		
		int timeInTenth = Math.round(timeInMill / 100);
		float timeInSecFloat = (float) timeInTenth / 10;  
		int timeInSecInt = (int) timeInSecFloat;

		if ((timeInSecFloat-timeInSecInt)>0) {
			return timeInSecFloat + " s";
		} else {
			return timeInSecInt + " s";
		}			
	}
}
