package de.enflexit.awb.ws.ui.server;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServer;

/**
 * The Class JToolBarServer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JToolBarServer extends JToolBar implements ActionListener {

	private static final long serialVersionUID = 935898280006480662L;

	private static final Dimension buttonSize = new Dimension(26, 26);
	
	private JLabel jLabelTitle;
	
	private JButton jButtonSave;
	private JButton jButtonResetToSavedSettings;
	private JButton jButtonResetToServiceSettings;
	
	private JButton jButtonStopServer;
	private JButton jButtonStartServer;

	
	public JToolBarServer() {
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setFloatable(false);
		this.setRollover(true);
		
		this.add(this.getJLabelTitle());
		
		this.add(this.getJButtonSave());
		this.addSeparator();
		
		this.add(this.getJButtonResetToSavedSettings());
		this.add(this.getJButtonResetToServiceSettings());
		this.addSeparator();
		
		this.add(this.getJButtonStartServer());
		this.add(this.getJButtonStopServer());
		this.addSeparator();
	}
	
	public JLabel getJLabelTitle() {
		if (jLabelTitle==null) {
			jLabelTitle = new JLabel("Server Name: ");
			jLabelTitle.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelTitle;
	}

	public JButton getJButtonSave() {
		if (jButtonSave==null) {
			jButtonSave = new JButton();
			jButtonSave.setPreferredSize(buttonSize);
			jButtonSave.setToolTipText("Save current settings");
			jButtonSave.setIcon(BundleHelper.getImageIcon("MBsave.png"));
			jButtonSave.addActionListener(this);
		}
		return jButtonSave;
	}
	public JButton getJButtonResetToSavedSettings() {
		if (jButtonResetToSavedSettings==null) {
			jButtonResetToSavedSettings = new JButton();
			jButtonResetToSavedSettings.setPreferredSize(buttonSize);
			jButtonResetToSavedSettings.setToolTipText("Reset to previous settings");
			jButtonResetToSavedSettings.setIcon(BundleHelper.getImageIcon("MBreset.png"));
			jButtonResetToSavedSettings.addActionListener(this);
		}
		return jButtonResetToSavedSettings;
	}
	public JButton getJButtonResetToServiceSettings() {
		if (jButtonResetToServiceSettings==null) {
			jButtonResetToServiceSettings = new JButton();
			jButtonResetToServiceSettings.setPreferredSize(buttonSize);
			jButtonResetToServiceSettings.setToolTipText("Reset to previous settings");
			jButtonResetToServiceSettings.setIcon(BundleHelper.getImageIcon("MBreset.png"));
			jButtonResetToServiceSettings.addActionListener(this);
		}
		return jButtonResetToServiceSettings;
	}
	
	
	public JButton getJButtonStartServer() {
		if (jButtonStartServer==null) {
			jButtonStartServer = new JButton();
			jButtonStartServer.setToolTipText("Start server");
			jButtonStartServer.setPreferredSize(buttonSize);
			jButtonStartServer.setIcon(BundleHelper.getImageIcon("MBstart.png"));
			jButtonStartServer.addActionListener(this);
		}
		return jButtonStartServer;
	}
	public JButton getJButtonStopServer() {
		if (jButtonStopServer==null) {
			jButtonStopServer = new JButton();
			jButtonStopServer.setToolTipText("Stop server");
			jButtonStopServer.setPreferredSize(buttonSize);
			jButtonStopServer.setIcon(BundleHelper.getImageIcon("MBstop.png"));
			jButtonStopServer.addActionListener(this);
		}
		return jButtonStopServer;
	}
	
	/**
	 * Sets the current server tree node.
	 * @param serverTreeNode the new server tree node
	 */
	public void setServerTreeNode(ServerTreeNodeServer serverTreeNode) {
		// TODO Auto-generated method stub
		
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonSave()) {
			
		} else if (ae.getSource()==this.getJButtonResetToSavedSettings()) {
		
		} else if (ae.getSource()==this.getJButtonResetToServiceSettings()) {
			
		
		} else if (ae.getSource()==this.getJButtonStartServer()) {
		
		} else if (ae.getSource()==this.getJButtonStopServer()) {
		
		}
	}
	
}
