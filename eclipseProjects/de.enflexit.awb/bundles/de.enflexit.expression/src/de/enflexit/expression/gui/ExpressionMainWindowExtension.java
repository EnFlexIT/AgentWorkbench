package de.enflexit.expression.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.agentgui.gui.swing.MainWindowExtension;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo;
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
			ExpressionEditorDialog eed = new ExpressionEditorDialog(Application.getMainWindow(), null, null, true);
			eed.setVisible(true);
		}
	}
	
}
