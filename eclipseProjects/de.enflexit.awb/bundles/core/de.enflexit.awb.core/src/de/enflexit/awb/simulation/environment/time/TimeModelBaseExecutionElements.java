package de.enflexit.awb.simulation.environment.time;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.ZoneId;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.common.swing.TimeZoneDateFormat;
import de.enflexit.language.Language;

/**
 * The Class TimeModelBaseExecutionElements has to be extended in order to
 * provide a specific display for a TimeModel during the execution of an agency.
 * 
 * @see TimeModel
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public abstract class TimeModelBaseExecutionElements implements ActionListener {

	protected final static int ViewTIMER = 0;
	protected final static int ViewCOUNTDOWN = 1;
	protected int view = 0;
	
	/** This is the JToolBar to which your elements can be added !*/
	protected JToolBar jToolBar4Elements = null;
	private Vector<JToolBar.Separator> separatorVector=null;
	
	private JLabel jLabelIntro = null;
	private JLabel jLabelTimeDisplay = null;
	
	private JButton jButtonTimeConfig=null;
	private JPopupMenu jPopupMenuViewConfiguration=null;
	private JMenuItem jMenuItemViewTimer=null;	
	private JMenuItem jMenuItemViewCountdown=null;
	
	
	/**
	 * Instantiates a new time model base execution elements.
	 */
	public TimeModelBaseExecutionElements() {
	}
	
	/**
	 * Sets the TimeModel.
	 * @param timeModel the new TimeModel
	 */
	public abstract void setTimeModel(TimeModel timeModel);
	/**
	 * Returns the TimeModel.
	 * @return the TimeModell
	 */
	public abstract TimeModel getTimeModel();
	
	/**
	 * Returns the tool bar title as for example 'Time' or 'Counter'.
	 * Place here a name of your choice.
	 * @return the tool bar title
	 */
	public abstract String getToolBarTitle();
	
	/**
	 * Sets the introduction header for the tools.
	 * @param newIntroHeader the new introduction header for the GUI representation
	 */
	protected void setIntroductionHeader(String newIntroHeader) {
		this.getJLabelIntro().setText(" " + newIntroHeader + ": ");
	}
	/**
	 * Returns the introduction header of the tools.
	 * @return the introduction header
	 */
	protected String getIntroductionHeader() {
		return this.getJLabelIntro().getText();
	}
	
	/**
	 * Adds the custom toolbar elements.
	 * @param jToolBar2AddElements the JToolBar to which elements should be added 
	 */
	public void addToolbarElements(JToolBar jToolBar2AddElements) {
		this.jToolBar4Elements = jToolBar2AddElements;
		this.addToolbarElements();
	}
	/**
	 * Adds the custom toolbar elements.
	 */
	public void addToolbarElements(){
		this.jToolBar4Elements.add(this.getJLabelIntro());
		this.jToolBar4Elements.add(this.getJLabelTimeDisplay());
		this.jToolBar4Elements.add(this.getSeparator(1));
		this.jToolBar4Elements.add(this.getJButtonTimeConfig());	
	}
	
	/**
	 * Removes the custom toolbar elements.
	 */
	public void removeToolbarElements(){
		this.jToolBar4Elements.remove(this.getJLabelIntro());
		this.jToolBar4Elements.remove(this.getJLabelTimeDisplay());
		this.jToolBar4Elements.remove(this.getJButtonTimeConfig());
		this.removeAllSeparator();
	}

	/**
	 * Returns a reminded separator, so that separators can be removed later on.
	 * @param numberOfSeperator the number of separator
	 * @return the separator
	 */
	protected JToolBar.Separator getSeparator(int numberOfSeperator) {
		if (separatorVector==null) {
			separatorVector = new Vector<JToolBar.Separator>();
		}
		if (separatorVector.size()<numberOfSeperator) {
			for (int i = 0; i < numberOfSeperator; i++) {
				separatorVector.add(new JToolBar.Separator());
			}
		}
		return this.separatorVector.get(numberOfSeperator-1);
	}
	/**
	 * Removes all separator.
	 */
	protected  void removeAllSeparator() {
		if (this.separatorVector!=null) {
			for (int i = 0; i < this.separatorVector.size(); i++) {
				this.jToolBar4Elements.remove(this.separatorVector.get(i));
			}
			this.separatorVector=null;
		}
	}
	
	/**
	 * Returns a time formatted.
	 *
	 * @param time the time
	 * @param zoneId the ZoneId
	 * @param timeFormat the time format
	 * @return the time formatted
	 */
	protected String getTimeFormatted(long time, ZoneId zoneId, String timeFormat) {
		
		if (zoneId==null) zoneId = ZoneId.systemDefault();
		if (timeFormat==null || timeFormat.isBlank()) timeFormat = TimeModelDateBased.DEFAULT_TIME_FORMAT;
		
		String timeString = null;
		try {
			timeString = new TimeZoneDateFormat(timeFormat, zoneId).format(time);	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return timeString;
	}
	
	/**
	 * Returns a time duration formatted.
	 *
	 * @param duration the time difference
	 * @param timeFormat the time format
	 * @return the time formated
	 */
	protected String getTimeDurationFormatted(long duration, String timeFormat) {
		String timeString = null;
		try {
			timeString = new TimeDurationFormat(timeFormat).format(duration);	
		} catch (Exception ex) {
			// Retry below
		}
		if (timeString==null) {
			timeString = new TimeDurationFormat(TimeModelDateBased.DEFAULT_TIME_FORMAT).format(duration);
		}
		return timeString;
	}
	
	/**
	 * Returns the JLabel for the introduction of the TimeModel's display.
	 * @return the JLabel for the introduction 
	 */
	protected JLabel getJLabelIntro() {
		if (this.jLabelIntro==null) {
			jLabelIntro = new JLabel("");
			jLabelIntro.setFont(new Font("Dialog", Font.BOLD, 12));
			this.setIntroductionHeader(this.getToolBarTitle());
		}
		return this.jLabelIntro;
	}
	/**
	 * Returns the time display.
	 * @return the time display
	 */
	protected JLabel getJLabelTimeDisplay() {
		if (this.jLabelTimeDisplay==null) {
			jLabelTimeDisplay = new JLabel();
			jLabelTimeDisplay.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelTimeDisplay.setForeground(AwbThemeColor.ButtonTextGreen.getColor());
		}
		return this.jLabelTimeDisplay;
	}
	/**
	 * Gets the button for the time configuration.
	 * @return the button for the time configuration
	 */
	protected JButton getJButtonTimeConfig() {
		if (jButtonTimeConfig==null) {
			jButtonTimeConfig = new JButton();
			jButtonTimeConfig.setText(Language.translate("Ansicht"));
			jButtonTimeConfig.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonTimeConfig.setSize(36, 36);
			jButtonTimeConfig.addActionListener(this);	
		}
		return jButtonTimeConfig;
	}
	/**
	 * Gets the j popup menu view configuration.
	 * @return the j popup menu view configuration
	 */
	protected JPopupMenu getJPopupMenuViewConfiguration(){
		if (jPopupMenuViewConfiguration==null) {
			jPopupMenuViewConfiguration = new JPopupMenu("Time-Configuration");
			jPopupMenuViewConfiguration.setBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED) );
			jPopupMenuViewConfiguration.add(this.getJMenuItemViewTimer());
			jPopupMenuViewConfiguration.add(this.getJMenuItemViewCountdown());
		}
		return jPopupMenuViewConfiguration;
	}
	/**
	 * Gets the j menu item view timer.
	 * @return the j menu item view timer
	 */
	protected JMenuItem getJMenuItemViewTimer() {
		if (jMenuItemViewTimer==null) {
			jMenuItemViewTimer=new JMenuItem();
			jMenuItemViewTimer.setText(Language.translate("Timer-Ansicht"));
			jMenuItemViewTimer.addActionListener(this);
		}
		return jMenuItemViewTimer;
	}
	/**
	 * Gets the j menu item view Countdown.
	 * @return the j menu item view Countdown
	 */
	protected JMenuItem getJMenuItemViewCountdown() {
		if (jMenuItemViewCountdown==null) {
			jMenuItemViewCountdown=new JMenuItem();
			jMenuItemViewCountdown.setText(Language.translate("Countdown-Ansicht"));
			jMenuItemViewCountdown.addActionListener(this);
		}
		return jMenuItemViewCountdown;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		Object trigger = ae.getSource();
		if (trigger==this.getJButtonTimeConfig()) {
			this.getJPopupMenuViewConfiguration().show(this.getJButtonTimeConfig(), 0, this.getJButtonTimeConfig().getHeight());
			
		} else if (trigger==this.getJMenuItemViewTimer()) {
			this.view = ViewTIMER;
			this.setTimeModel(this.getTimeModel());
			
		} else if (trigger==this.getJMenuItemViewCountdown()) {
			this.view = ViewCOUNTDOWN;
			this.setTimeModel(this.getTimeModel());
		}
		
	}
	
}
