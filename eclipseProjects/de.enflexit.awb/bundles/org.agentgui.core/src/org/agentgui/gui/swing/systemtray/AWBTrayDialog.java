package org.agentgui.gui.swing.systemtray;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import agentgui.core.application.Application;
import de.enflexit.language.Language;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;

/**
 * This JDialog is a reserve dialog in case that tray icons are not supported
 * by the OS. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class AWBTrayDialog extends JDialog implements MouseListener {

	private static final long serialVersionUID = -663073173834257611L;
	
	private PopupMenu trayIconPopUp = null;  //  @jve:decl-index=0:
	private AWBTrayIcon appTrayIconInstance = null;
	
	private JPanel jContentPane = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private JLabel jLabelIcon = null;
	private JLabel jLabelInfo = null;
	
	/**
	 * Instantiates a new Agent.GUI tray dialog.
	 *
	 * @param owner the owner
	 * @param trayIconInstance the tray icon instance
	 */
	public AWBTrayDialog(Frame owner, AWBTrayIcon trayIconInstance) {
		super(owner);
		appTrayIconInstance = trayIconInstance;
		trayIconPopUp = trayIconInstance.getTrayPopUp();
		initialize();
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {
		
		this.setSize(280, 70);
		this.add(getJContentPane());
		this.setUndecorated(true);
		this.addMouseListener(this);
		this.add(trayIconPopUp);
		this.setAlwaysOnTop(true);
		
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentBottomRight);
		
		String viewTitle = Application.getGlobalInfo().getApplicationTitle();
		jContentPane.setToolTipText(viewTitle);
		jLabelInfo.setToolTipText(viewTitle);
		
		String viewText = Language.translate("Rechts-Klick für weitere Optionen");
		jLabelInfo.setText(viewText);
		this.getjLabel4Icon().setToolTipText(viewText);
	}

	/**
	 * This method initializes jContentPane.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			
			jLabelInfo = new JLabel();
			jLabelInfo.setBounds(new Rectangle(60, 25, 210, 16));
			jLabelInfo.setText("Rechts-Klick für weitere Optionen");
			jLabelInfo.addMouseListener(this);
			
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.setSize(new Dimension(329, 68));
			jContentPane.addMouseListener(this);
			jContentPane.add(this.getjLabel4Icon(), null);
			jContentPane.add(jLabelInfo, null);
		}
		return jContentPane;
	}

	/**
	 * Returns the JLable for the Icon.
	 * @return the JLable for the Icon
	 */
	public JLabel getjLabel4Icon() {
		if (jLabelIcon==null) {
			jLabelIcon = new JLabel();
			jLabelIcon.setBounds(new Rectangle(5, 5, 51, 57));
			jLabelIcon.setIcon(appTrayIconInstance.getImageIconRed());
			jLabelIcon.setText("");
			jLabelIcon.addMouseListener(this);
		}
		return jLabelIcon;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println( "MouseReleased" );
		if (e.isPopupTrigger()) {
			if (trayIconPopUp == null) {
				System.out.println("Could not find context menu.");
			} else {
				trayIconPopUp.show( e.getComponent(), e.getX(), e.getY());	
			}			
		}		
	}

}  //  @jve:decl-index=0:visual-constraint="24,14"
