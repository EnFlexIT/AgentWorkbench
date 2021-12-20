package de.enflexit.common.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.time.ZoneId;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * The Class TimeZoneWidgetDialog represents a modal dialog that can be used 
 * to configure a time zone to use. It contains the {@link TimeZoneWidget}
 * as the central configuration element.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TimeZoneWidgetDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 3727546083615787299L;

	private TimeZoneWidget timeZoneWidget;
	private JButton jButtonApply;
	private JButton jButtonCancel;

	private boolean canceled;
	
	/**
	 * Instantiates a new time zone pop up.
	 * @param ownerFrame the owner frame
	 * @wbp.parser.constructor
	 */
	public TimeZoneWidgetDialog(Frame ownerFrame) {
		this(ownerFrame, null);
	}
	/**
	 * Instantiates a new time zone pop up.
	 *
	 * @param ownerFrame the owner frame
	 * @param zoneId the zone id
	 */
	public TimeZoneWidgetDialog(Frame ownerFrame, ZoneId zoneId) {
		super(ownerFrame);
		this.initialize();
		this.setZoneId(zoneId);
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		// --- Some general settings ---------------------- 
		this.setSize(350, 90);
		this.setUndecorated(true);
		((JPanel) this.getContentPane()).setBorder(BorderFactory.createLineBorder(Color.black));
		
		this.registerEscapeKeyStroke();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		this.getContentPane().setLayout(gridBagLayout);
		
		GridBagConstraints gbc_timeZoneWidget = new GridBagConstraints();
		gbc_timeZoneWidget.gridwidth = 2;
		gbc_timeZoneWidget.insets = new Insets(10, 10, 0, 10);
		gbc_timeZoneWidget.fill = GridBagConstraints.BOTH;
		gbc_timeZoneWidget.gridx = 0;
		gbc_timeZoneWidget.gridy = 0;
		this.getContentPane().add(getTimeZoneWidget(), gbc_timeZoneWidget);
		
		GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
		gbc_jButtonCancel.insets = new Insets(10, 25, 10, 0);
		gbc_jButtonCancel.gridx = 0;
		gbc_jButtonCancel.gridy = 1;
		this.getContentPane().add(getJButtonCancel(), gbc_jButtonCancel);
		
		GridBagConstraints gbc_jButtonApplyTimeFormat = new GridBagConstraints();
		gbc_jButtonApplyTimeFormat.insets = new Insets(10, 0, 10, 25);
		gbc_jButtonApplyTimeFormat.gridx = 1;
		gbc_jButtonApplyTimeFormat.gridy = 1;
		this.getContentPane().add(getJButtonApply(), gbc_jButtonApplyTimeFormat);
		
	}
	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
    			TimeZoneWidgetDialog.this.canceled = true;
    			TimeZoneWidgetDialog.this.setVisible(false);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    
	/**
	 * Returns the time zone widget.
	 * @return the time zone widget
	 */
	private TimeZoneWidget getTimeZoneWidget() {
		if (timeZoneWidget == null) {
			timeZoneWidget = new TimeZoneWidget();
			timeZoneWidget.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return timeZoneWidget;
	}
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setPreferredSize(new Dimension(100, 26));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	private JButton getJButtonApply() {
		if (jButtonApply==null) {
			jButtonApply = new JButton("Apply");
			jButtonApply.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApply.setPreferredSize(new Dimension(100, 26));
			jButtonApply.setForeground(new Color(0, 153, 0));
			jButtonApply.addActionListener(this);
		}
		return jButtonApply;
	}
	
	/**
	 * Sets the zone id.
	 * @param zoneId the new zone id
	 */
	public void setZoneId(ZoneId zoneId) {
		this.getTimeZoneWidget().setZoneId(zoneId);
	}
	/**
	 * Returns the zone id.
	 * @return the zone id
	 */
	public ZoneId getZoneId() {
		return this.getTimeZoneWidget().getZoneId();
	}
	
	/**
	 * Checks if the dialog action was canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonApply()) {
			this.setVisible(false);
		} else if (ae.getSource()==this.getJButtonCancel()) {
			this.canceled = true;
			this.setVisible(false);
		}
	}

}
