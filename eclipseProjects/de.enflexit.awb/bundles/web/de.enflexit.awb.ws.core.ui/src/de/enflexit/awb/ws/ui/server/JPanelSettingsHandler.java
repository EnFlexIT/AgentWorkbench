package de.enflexit.awb.ws.ui.server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import de.enflexit.awb.ws.core.model.ServerTreeNodeHandler;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServer;
import de.enflexit.common.swing.AwbThemeColor;

/**
 * The Class JPanelSettingsHandler.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelSettingsHandler extends JPanel implements JettyConfigurationInterface<ServerTreeNodeHandler> {

	private static final long serialVersionUID = -4985161964727450005L;

	private ServerTreeNodeHandler serverTreeNodeHandler;
	
	private JLabel jLabelContextPath;
	private JLabel jLabelContextPathValue;
	private JLabel jLabelState;
	private JLabel jLabelStateDescription;
	private JLabel jLabelSourceBundle;
	private JLabel jLabelSourceBundleDescription;
	private JLabel jLabelServiceClass;
	private JLabel jLabelServiceClassDescription;
	private JLabel jLabelHandlerClass;
	private JLabel jLabelHandlerClassValue;
	private JSeparator separator;
	private JPanelSettingsSecurity jPanelSettingsSecurity;
	private JPanel jPanelLeft;
	private JPanel jPanelRight;
	
	/**
	 * Instantiates a new JPanel for the server settings.
	 */
	public JPanelSettingsHandler() {
		this.initialize();
	}
	
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jPanelLeft = new GridBagConstraints();
		gbc_jPanelLeft.insets = new Insets(5, 10, 0, 5);
		gbc_jPanelLeft.fill = GridBagConstraints.BOTH;
		gbc_jPanelLeft.gridx = 0;
		gbc_jPanelLeft.gridy = 0;
		this.add(this.getJPanelLeft(), gbc_jPanelLeft);
		
		GridBagConstraints gbc_jPanelRight = new GridBagConstraints();
		gbc_jPanelRight.insets = new Insets(5, 5, 0, 10);
		gbc_jPanelRight.fill = GridBagConstraints.BOTH;
		gbc_jPanelRight.gridx = 1;
		gbc_jPanelRight.gridy = 0;
		this.add(this.getJPanelRight(), gbc_jPanelRight);
		
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(10, 10, 0, 10);
		gbc_separator.gridwidth = 2;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 4;
		this.add(this.getSeparator(), gbc_separator);
		
		GridBagConstraints gbc_jPanelSettingsSecurity = new GridBagConstraints();
		gbc_jPanelSettingsSecurity.insets = new Insets(5, 0, 0, 0);
		gbc_jPanelSettingsSecurity.gridwidth = 2;
		gbc_jPanelSettingsSecurity.fill = GridBagConstraints.BOTH;
		gbc_jPanelSettingsSecurity.gridx = 0;
		gbc_jPanelSettingsSecurity.gridy = 5;
		add(getJPanelSettingsSecurity(), gbc_jPanelSettingsSecurity);
	}
	
	private JPanel getJPanelLeft() {
		if (jPanelLeft == null) {
			jPanelLeft = new JPanel();
			jPanelLeft.setPreferredSize(new Dimension(200, 80));
			
			GridBagLayout gbl_jPanelLeft = new GridBagLayout();
			gbl_jPanelLeft.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelLeft.rowHeights = new int[]{0, 0, 0};
			gbl_jPanelLeft.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelLeft.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			jPanelLeft.setLayout(gbl_jPanelLeft);
			GridBagConstraints gbc_jLabelContextPath = new GridBagConstraints();
			gbc_jLabelContextPath.anchor = GridBagConstraints.WEST;
			gbc_jLabelContextPath.gridx = 0;
			gbc_jLabelContextPath.gridy = 0;
			jPanelLeft.add(getJLabelContextPath(), gbc_jLabelContextPath);
			GridBagConstraints gbc_jLabelContextPathValue = new GridBagConstraints();
			gbc_jLabelContextPathValue.insets = new Insets(0, 5, 0, 0);
			gbc_jLabelContextPathValue.anchor = GridBagConstraints.WEST;
			gbc_jLabelContextPathValue.gridx = 1;
			gbc_jLabelContextPathValue.gridy = 0;
			jPanelLeft.add(getJLabelContextPathValue(), gbc_jLabelContextPathValue);
			GridBagConstraints gbc_jLabelHandlerClass = new GridBagConstraints();
			gbc_jLabelHandlerClass.insets = new Insets(10, 0, 0, 0);
			gbc_jLabelHandlerClass.anchor = GridBagConstraints.WEST;
			gbc_jLabelHandlerClass.gridx = 0;
			gbc_jLabelHandlerClass.gridy = 1;
			jPanelLeft.add(getJLabelHandlerClass(), gbc_jLabelHandlerClass);
			GridBagConstraints gbc_jLabelHandlerClassValue = new GridBagConstraints();
			gbc_jLabelHandlerClassValue.insets = new Insets(10, 5, 0, 0);
			gbc_jLabelHandlerClassValue.anchor = GridBagConstraints.WEST;
			gbc_jLabelHandlerClassValue.gridx = 1;
			gbc_jLabelHandlerClassValue.gridy = 1;
			jPanelLeft.add(getJLabelHandlerClassValue(), gbc_jLabelHandlerClassValue);
		}
		return jPanelLeft;
	}
	private JLabel getJLabelContextPath() {
		if (jLabelContextPath == null) {
			jLabelContextPath = new JLabel("Context Path:");
			jLabelContextPath.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelContextPath;
	}
	private JLabel getJLabelContextPathValue() {
		if (jLabelContextPathValue == null) {
			jLabelContextPathValue = new JLabel("*");
			jLabelContextPathValue.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelContextPathValue;
	}
	
	private JPanel getJPanelRight() {
		if (jPanelRight == null) {
			jPanelRight = new JPanel();
			jPanelRight.setPreferredSize(new Dimension(200, 80));
			
			GridBagLayout gbl_jPanelRight = new GridBagLayout();
			gbl_jPanelRight.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelRight.rowHeights = new int[]{0, 0, 0, 0};
			gbl_jPanelRight.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelRight.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			jPanelRight.setLayout(gbl_jPanelRight);
			GridBagConstraints gbc_jLabelState = new GridBagConstraints();
			gbc_jLabelState.anchor = GridBagConstraints.WEST;
			gbc_jLabelState.gridx = 0;
			gbc_jLabelState.gridy = 0;
			jPanelRight.add(getJLabelState(), gbc_jLabelState);
			GridBagConstraints gbc_jLabelStateDescription = new GridBagConstraints();
			gbc_jLabelStateDescription.insets = new Insets(0, 5, 0, 0);
			gbc_jLabelStateDescription.anchor = GridBagConstraints.WEST;
			gbc_jLabelStateDescription.gridx = 1;
			gbc_jLabelStateDescription.gridy = 0;
			jPanelRight.add(getJLabelStateDescription(), gbc_jLabelStateDescription);
			GridBagConstraints gbc_jLabelSourceBundle = new GridBagConstraints();
			gbc_jLabelSourceBundle.insets = new Insets(10, 0, 0, 0);
			gbc_jLabelSourceBundle.anchor = GridBagConstraints.WEST;
			gbc_jLabelSourceBundle.gridx = 0;
			gbc_jLabelSourceBundle.gridy = 1;
			jPanelRight.add(getJLabelSourceBundle(), gbc_jLabelSourceBundle);
			GridBagConstraints gbc_jLabelSourceBundleDescription = new GridBagConstraints();
			gbc_jLabelSourceBundleDescription.insets = new Insets(10, 5, 0, 0);
			gbc_jLabelSourceBundleDescription.anchor = GridBagConstraints.WEST;
			gbc_jLabelSourceBundleDescription.gridx = 1;
			gbc_jLabelSourceBundleDescription.gridy = 1;
			jPanelRight.add(getJLabelSourceBundleDescription(), gbc_jLabelSourceBundleDescription);
			GridBagConstraints gbc_jLabelServiceClass = new GridBagConstraints();
			gbc_jLabelServiceClass.insets = new Insets(10, 0, 0, 0);
			gbc_jLabelServiceClass.anchor = GridBagConstraints.WEST;
			gbc_jLabelServiceClass.gridx = 0;
			gbc_jLabelServiceClass.gridy = 2;
			jPanelRight.add(getJLabelServiceClass(), gbc_jLabelServiceClass);
			GridBagConstraints gbc_jLabelServiceClassDescription = new GridBagConstraints();
			gbc_jLabelServiceClassDescription.insets = new Insets(10, 5, 0, 0);
			gbc_jLabelServiceClassDescription.anchor = GridBagConstraints.WEST;
			gbc_jLabelServiceClassDescription.gridx = 1;
			gbc_jLabelServiceClassDescription.gridy = 2;
			jPanelRight.add(getJLabelServiceClassDescription(), gbc_jLabelServiceClassDescription);
		}
		return jPanelRight;
	}
	private JLabel getJLabelState() {
		if (jLabelState == null) {
			jLabelState = new JLabel("State:");
			jLabelState.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelState;
	}
	private JLabel getJLabelStateDescription() {
		if (jLabelStateDescription == null) {
			jLabelStateDescription = new JLabel("Stopped");
			jLabelStateDescription.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelStateDescription;
	}
	private void setStateDescription(ServerTreeNodeHandler serverNodeHandler) {
		String display = serverNodeHandler.getRunningHandlerDescription();
		Color color = null; 
		if (serverNodeHandler.isRunningHandler()==true) {
			color = AwbThemeColor.ButtonTextGreen.getColor();
		} else {
			color = AwbThemeColor.ButtonTextRed.getColor();
		}
		this.getJLabelStateDescription().setText(display);
		this.getJLabelStateDescription().setForeground(color);
	}
	
	private JLabel getJLabelSourceBundle() {
		if (jLabelSourceBundle == null) {
			jLabelSourceBundle = new JLabel("Bundle:");
			jLabelSourceBundle.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSourceBundle;
	}
	private JLabel getJLabelSourceBundleDescription() {
		if (jLabelSourceBundleDescription == null) {
			jLabelSourceBundleDescription = new JLabel("my.bundle.name");
			jLabelSourceBundleDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelSourceBundleDescription;
	}
	private JLabel getJLabelServiceClass() {
		if (jLabelServiceClass == null) {
			jLabelServiceClass = new JLabel("Service:");
			jLabelServiceClass.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelServiceClass;
	}
	private JLabel getJLabelServiceClassDescription() {
		if (jLabelServiceClassDescription == null) {
			jLabelServiceClassDescription = new JLabel("Class.Name");
			jLabelServiceClassDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelServiceClassDescription;
	}
	private JLabel getJLabelHandlerClass() {
		if (jLabelHandlerClass == null) {
			jLabelHandlerClass = new JLabel("Handler Class:");
			jLabelHandlerClass.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHandlerClass;
	}
	private JLabel getJLabelHandlerClassValue() {
		if (jLabelHandlerClassValue == null) {
			jLabelHandlerClassValue = new JLabel("-");
			jLabelHandlerClassValue.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelHandlerClassValue;
	}
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
		}
		return separator;
	}
	private JPanelSettingsSecurity getJPanelSettingsSecurity() {
		if (jPanelSettingsSecurity == null) {
			jPanelSettingsSecurity = new JPanelSettingsSecurity();
		}
		return jPanelSettingsSecurity;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.JettyConfigurationInterface#setServerTreeNodeServer(de.enflexit.awb.ws.core.model.ServerTreeNodeServer)
	 */
	@Override
	public void setServerTreeNodeServer(ServerTreeNodeServer serverTreeNodeServer) {
		this.getJPanelSettingsSecurity().setServerTreeNodeServer(serverTreeNodeServer);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.AbstractJPanelSettings#setDataModel(de.enflexit.awb.ws.core.model.AbstractServerTreeNodeObject)
	 */
	@Override
	public void setDataModel(ServerTreeNodeHandler dataModel) {
		
		this.serverTreeNodeHandler = dataModel;
		
		this.getJLabelContextPathValue().setText(this.serverTreeNodeHandler.getContextPath().isBlank() ? "-" : this.serverTreeNodeHandler.getContextPath());
		this.getJLabelHandlerClassValue().setText(this.serverTreeNodeHandler.getHandlerClassName());
		
		this.setStateDescription(this.serverTreeNodeHandler);
		this.getJLabelSourceBundleDescription().setText(this.serverTreeNodeHandler.getSourceBundle().getSymbolicName());
		this.getJLabelServiceClassDescription().setText(this.serverTreeNodeHandler.getServiceClassName()==null ? "-" : "Class: " + this.serverTreeNodeHandler.getServiceClassName());

		this.getJPanelSettingsSecurity().setServerTreeNodeHandler(dataModel);
				
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.JettyConfigurationInterface#stopEditing()
	 */
	@Override
	public void stopEditing() {
		this.getJPanelSettingsSecurity().stopEditing();
	}
}
