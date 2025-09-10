package de.enflexit.ml.pmml.evaluator.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import de.enflexit.awb.baseUI.mainWindow.MainWindowExtension;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.common.swing.AwbThemeImageIcon;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;

public class PMMLEvaluatorUIIntegration extends MainWindowExtension implements ActionListener {
	
	private static final String ICON_PATH_LIGHT_MODE = "/icons/KI_LightMode.png";
	private static final String ICON_PATH_DARK_MODE = "/icons/KI_DarkMode.png";
	
	private JButton toolbarButton;
	private AwbThemeImageIcon kiIcon;
	private JDialog pmmlEvaluatorDialog;

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getToolbarButton()) {
			this.showPmmlEvaluatorDialog();
		}
	}

	@Override
	public void initialize() {
		if (Application.getGlobalInfo().getExecutionMode()==ExecutionMode.APPLICATION) {
			this.addToolbarComponent(this.getToolbarButton(), null, null);
		}
	}
	
	private JButton getToolbarButton() {
		if (toolbarButton==null) {
			toolbarButton = new JButton();
			toolbarButton.setIcon(this.getKiIcon());
			toolbarButton.setToolTipText("Start PMML Evaluator Test");
			toolbarButton.addActionListener(this);
		}
		return toolbarButton;
	}
	private AwbThemeImageIcon getKiIcon() {
		if (kiIcon==null) {
			ImageIcon lightModeIcon = new ImageIcon(this.getClass().getResource(ICON_PATH_LIGHT_MODE));
			ImageIcon darkModeIcon = new ImageIcon(this.getClass().getResource(ICON_PATH_DARK_MODE));
			kiIcon = new AwbThemeImageIcon(lightModeIcon, darkModeIcon);
		}
		return kiIcon;
	}
	
	private void showPmmlEvaluatorDialog() {
		this.getPmmlEvaluatorDialog().setVisible(true);
	}
	
	public JDialog getPmmlEvaluatorDialog() {
		if (pmmlEvaluatorDialog==null) {
			
			Window owner = null;
			if (Application.getMainWindow()!=null) {
				owner = (Window) Application.getMainWindow();
			}
			pmmlEvaluatorDialog = new JDialog(owner, "PMML Model Evaluator");
			pmmlEvaluatorDialog.setContentPane(new PMMLEvaluatorPanel());
			pmmlEvaluatorDialog.setSize(600, 600);
			WindowSizeAndPostionController.setJDialogPositionOnScreen(pmmlEvaluatorDialog, JDialogPosition.ParentCenter);
		}
		return pmmlEvaluatorDialog;
	}
	

}
