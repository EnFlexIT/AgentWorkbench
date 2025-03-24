package de.enflexit.expression.gui;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import de.enflexit.awb.baseUI.mainWindow.MainWindowExtension;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.expression.DevelopmentSwitch;

/**
 * The Class ExpressionMainWindowExtension.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExpressionMainWindowExtension extends MainWindowExtension implements ActionListener{
	
	private static final String ICON_PATH_CHART = "MBJadeLogger.gif";
	
	private JButton jButtonExpression;
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.MainWindowExtension#initialize()
	 */
	@Override
	public void initialize() {
		if (DevelopmentSwitch.isDebug()==true) {
			this.addToolbarComponent(this.getJButtonExpression(), null, null);
		}
	}
	
	/**
	 * Returns the JButton to open the expression editor.
	 * @return the JButton to open the expression editor
	 */
	private JButton getJButtonExpression() {
		if (jButtonExpression==null) {
			jButtonExpression = new JButton();
			jButtonExpression.setIcon(GlobalInfo.getInternalImageIcon(ICON_PATH_CHART));
			jButtonExpression.setToolTipText("Open Expression Editor");
			jButtonExpression.setPreferredSize(new Dimension(26, 26));
			jButtonExpression.addActionListener(this);
		}
		return jButtonExpression;
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonExpression()) {

			ExpressionEditorDialog eed = null;
			if (Application.isMainWindowInitiated()==true && Application.getMainWindow() instanceof Window) {
				eed = new ExpressionEditorDialog((Window) Application.getMainWindow(), null, null, true);
			} else {
				eed = new ExpressionEditorDialog(null, null, null, true);
			}
			eed.setVisible(true);
		}
	}
	
}
