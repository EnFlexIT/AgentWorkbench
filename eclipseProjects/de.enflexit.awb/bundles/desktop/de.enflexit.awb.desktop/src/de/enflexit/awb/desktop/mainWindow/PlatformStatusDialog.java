package de.enflexit.awb.desktop.mainWindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.Timer;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.jade.PlatformStateInformation;
import de.enflexit.awb.core.jade.PlatformStateInformation.PlatformState;

/**
 * The Class PlatformStatusDialog.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PlatformStatusDialog extends JDialog implements PropertyChangeListener, ActionListener {

	private static final long serialVersionUID = -3973068772318569985L;
	
	private JLabel jLabelPlatformStatePrev;
	private JLabel jLabelPlatformState;
	private JLabel jLabelPlatformStateNext;
	
	private Timer timerPropertyChange;
	private JButton jButtonClose;
	
	
	/**
	 * Instantiates a new platform status dialog.
	 * @param mainWindow the owner window instance
	 */
	public PlatformStatusDialog(MainWindow mainWindow) {
		super(mainWindow);
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setTitle("Platform State");
		this.setSize(500, 85);
		this.setUndecorated(true);
		
		this.getOwner().addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent ce) {
		    	if (PlatformStatusDialog.this.isVisible()==true) {
		    		PlatformStatusDialog.this.setDialogPosition();
		    	}
		    }
		    @Override
		    public void componentMoved(ComponentEvent ce) {
		    	if (PlatformStatusDialog.this.isVisible()==true) {
		    		PlatformStatusDialog.this.setDialogPosition();
		    	}
		    }
		});
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.getContentPane().setLayout(gridBagLayout);
		this.getContentPane().setBackground(new Color(255, 255, 224));
		
		GridBagConstraints gbc_jLabelPlatformStatePrev = new GridBagConstraints();
		gbc_jLabelPlatformStatePrev.insets = new Insets(10, 10, 0, 10);
		gbc_jLabelPlatformStatePrev.fill = GridBagConstraints.BOTH;
		gbc_jLabelPlatformStatePrev.gridx = 0;
		gbc_jLabelPlatformStatePrev.gridy = 0;
		this.getContentPane().add(this.getJLabelPlatformStatePrev(), gbc_jLabelPlatformStatePrev);
		
		GridBagConstraints gbc_jMenuCloseButton = new GridBagConstraints();
		gbc_jMenuCloseButton.gridheight = 2;
		gbc_jMenuCloseButton.insets = new Insets(5, 5, 0, 5);
		gbc_jMenuCloseButton.anchor = GridBagConstraints.NORTHEAST;
		gbc_jMenuCloseButton.gridx = 1;
		gbc_jMenuCloseButton.gridy = 0;
		this.getContentPane().add(this.getJButtonClose(), gbc_jMenuCloseButton);
		
		GridBagConstraints gbc_jLabelPlatformState = new GridBagConstraints();
		gbc_jLabelPlatformState.insets = new Insets(7, 10, 0, 10);
		gbc_jLabelPlatformState.anchor = GridBagConstraints.WEST;
		gbc_jLabelPlatformState.gridx = 0;
		gbc_jLabelPlatformState.gridy = 1;
		this.getContentPane().add(this.getJLabelPlatformState(), gbc_jLabelPlatformState);
		
		GridBagConstraints gbc_jLabelPlatformStateNext = new GridBagConstraints();
		gbc_jLabelPlatformStateNext.insets = new Insets(7, 10, 10, 10);
		gbc_jLabelPlatformStateNext.anchor = GridBagConstraints.WEST;
		gbc_jLabelPlatformStateNext.gridx = 0;
		gbc_jLabelPlatformStateNext.gridy = 2;
		this.getContentPane().add(this.getJLabelPlatformStateNext(), gbc_jLabelPlatformStateNext);
		
		Application.getJadePlatform().addPropertyChangeListener(this);
		
	}

	private JButton getJButtonClose() {
		if (jButtonClose == null) {
			jButtonClose = new JButton();
			jButtonClose.setBackground(new Color(255, 255, 224));
			jButtonClose.setText("");
			jButtonClose.setToolTipText("Close platform state message");
			jButtonClose.setIcon(GlobalInfo.getInternalImageIcon("MBclose.png"));
			jButtonClose.setBorder(BorderFactory.createEmptyBorder());
			jButtonClose.setBorderPainted(false);
			jButtonClose.setFocusPainted(false);
			jButtonClose.setMargin(new Insets(0, 0, 0, 0));
			jButtonClose.setPreferredSize(new Dimension(20, 20));
			jButtonClose.addActionListener(this);
		}
		return jButtonClose;
	}
	
	private JLabel getJLabelPlatformStatePrev() {
		if (jLabelPlatformStatePrev == null) {
			jLabelPlatformStatePrev = new JLabel("Platform State Information");
			jLabelPlatformStatePrev.setBackground(new Color(255, 255, 224));
			jLabelPlatformStatePrev.setForeground(new Color(0, 125, 0));
			jLabelPlatformStatePrev.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelPlatformStatePrev;
	}
	
	private JLabel getJLabelPlatformState() {
		if (jLabelPlatformState == null) {
			jLabelPlatformState = new JLabel("Platform State Information");
			jLabelPlatformState.setBackground(new Color(255, 255, 224));
			jLabelPlatformState.setForeground(new Color(0, 125, 0));
			jLabelPlatformState.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelPlatformState;
	}
	private JLabel getJLabelPlatformStateNext() {
		if (jLabelPlatformStateNext == null) {
			jLabelPlatformStateNext = new JLabel("Platform State Information");
			jLabelPlatformStateNext.setBackground(new Color(255, 255, 224));
			jLabelPlatformStateNext.setForeground(Color.GRAY);
			jLabelPlatformStateNext.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelPlatformStateNext;
	}
	
	public Timer getPropertyChangeTimer() {
		if (timerPropertyChange==null) {
			timerPropertyChange = new Timer(1000 * 10, this);
			timerPropertyChange.setRepeats(false);
		}
		return timerPropertyChange;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getPropertyChangeTimer()) {
			this.setVisible(false);
		
		} else if (ae.getSource()==this.getJButtonClose()) {
			this.getPropertyChangeTimer().stop();
			this.setVisible(false);
		}
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pcEv) {

		if (pcEv.getPropertyName().equals(PlatformStateInformation.PLATFORM_STATE)) {
			
			PlatformState oldPlatformState = (PlatformState) pcEv.getOldValue();
			PlatformState newPlatformState = (PlatformState) pcEv.getNewValue();
			this.setPlatformState(oldPlatformState, newPlatformState);
		}
	}
	/**
	 * Sets the platform state visible to the user.
	 *
	 * @param oldPlatformState the old platform state
	 * @param newPlatformState the new platform state
	 */
	private void setPlatformState(PlatformState oldPlatformState, PlatformState newPlatformState) {
		
		this.getJLabelPlatformStatePrev().setText(oldPlatformState.getDescription());
		this.getJLabelPlatformState().setText("=> " + newPlatformState.getDescription());
		
		List<PlatformState> stateList = PlatformStateInformation.getPlatformStateList(newPlatformState);
		String nextState = "";
		if (stateList.size()>1) {
			nextState = stateList.get(1).getDescription();
		}
		this.getJLabelPlatformStateNext().setText(nextState);
		
		this.setDialogPosition();
		this.setVisible(true);
		this.getPropertyChangeTimer().restart();
		
	}
	/**
	 * Sets the dialog position to the bottom right position of the {@link MainWindow}.
	 */
	private void setDialogPosition() {
		
		MainWindow mainWindow = (MainWindow) this.getOwner();
		
		int newX = this.getOwner().getX() + this.getOwner().getWidth()  - this.getWidth()  - 10;
		int newY = this.getOwner().getY() + this.getOwner().getHeight() - this.getHeight() - 10 - mainWindow.getStatusBar().getHeight();
		this.setLocation(newX, newY);
	}
	
}
