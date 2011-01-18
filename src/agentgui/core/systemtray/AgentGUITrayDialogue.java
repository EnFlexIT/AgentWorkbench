package agentgui.core.systemtray;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import agentgui.core.application.Application;
import agentgui.core.application.Language;

public class AgentGUITrayDialogue extends JDialog implements MouseListener {

	private static final long serialVersionUID = -663073173834257611L;
	
	private PopupMenu popUp = null;  //  @jve:decl-index=0:
	private AgentGUITrayIcon appTrayIconInstance = null;
	
	private JPanel jContentPane = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	public JLabel jLabelIcon = null;
	private JLabel jLabelInfo = null;
	/**
	 * @param owner
	 */
	public AgentGUITrayDialogue(Frame owner, AgentGUITrayIcon trayIconInstance) {
		super(owner);
		appTrayIconInstance = trayIconInstance;
		popUp = trayIconInstance.popUp;
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setSize(279, 66);
		this.add(getJContentPane());
		this.setUndecorated(true);
		this.addMouseListener(this);
		this.add(popUp);
		this.setAlwaysOnTop(true);
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int newX = (int) (screen.getWidth() - this.getWidth());
		int newY = (int) (screen.getHeight()- 1.5*this.getHeight());
		this.setLocation( newX, newY );
		
		String viewTitle = Application.RunInfo.getApplicationTitle();
		jContentPane.setToolTipText(viewTitle);
		jLabelInfo.setToolTipText(viewTitle);
		
		String viewText = Language.translate("Rechts-Klick für weitere Optionen");
		jLabelInfo.setText(viewText);
		jLabelIcon.setToolTipText(viewText);
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			
			jLabelInfo = new JLabel();
			jLabelInfo.setBounds(new Rectangle(60, 25, 210, 16));
			jLabelInfo.setText("Rechts-Klick für weitere Optionen");
			jLabelInfo.addMouseListener(this);
			
			jLabelIcon = new JLabel();
			jLabelIcon.setBounds(new Rectangle(5, 5, 51, 57));
			jLabelIcon.setIcon(appTrayIconInstance.imageIcon);
			jLabelIcon.setText("");
			jLabelIcon.addMouseListener(this);
			
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.setSize(new Dimension(329, 68));
			jContentPane.addMouseListener(this);
			jContentPane.add(jLabelIcon, null);
			jContentPane.add(jLabelInfo, null);
		}
		return jContentPane;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println( "MouseClicked" );
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		//System.out.println( "MouseEntered" );
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// System.out.println( "MouseExited" );
	}
	@Override
	public void mousePressed(MouseEvent e) {
		//System.out.println( "MousePressed" );
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println( "MouseReleased" );
		if (e.isPopupTrigger()) {
			if (popUp == null) {
				System.out.println("Could not find context menu.");
			} else {
				popUp.show( e.getComponent(), e.getX(), e.getY());	
			}			
		}		
	}

}  //  @jve:decl-index=0:visual-constraint="24,14"
