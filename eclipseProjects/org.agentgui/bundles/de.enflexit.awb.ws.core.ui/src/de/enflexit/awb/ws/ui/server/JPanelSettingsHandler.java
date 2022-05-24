package de.enflexit.awb.ws.ui.server;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.enflexit.awb.ws.core.model.ServerTreeNodeHandler;

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
	
	/**
	 * Instantiates a new JPanel for the server settings.
	 */
	public JPanelSettingsHandler() {
		this.initialize();
	}
	
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 200, 0, 200, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelContextPath = new GridBagConstraints();
		gbc_jLabelContextPath.anchor = GridBagConstraints.WEST;
		gbc_jLabelContextPath.insets = new Insets(5, 10, 0, 0);
		gbc_jLabelContextPath.gridx = 0;
		gbc_jLabelContextPath.gridy = 0;
		this.add(getJLabelContextPath(), gbc_jLabelContextPath);
		
		GridBagConstraints gbc_jLabelContextPathValue = new GridBagConstraints();
		gbc_jLabelContextPathValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelContextPathValue.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelContextPathValue.gridx = 1;
		gbc_jLabelContextPathValue.gridy = 0;
		this.add(getJLabelContextPathValue(), gbc_jLabelContextPathValue);
		
		GridBagConstraints gbc_jLabelState = new GridBagConstraints();
		gbc_jLabelState.anchor = GridBagConstraints.WEST;
		gbc_jLabelState.insets = new Insets(5, 10, 0, 0);
		gbc_jLabelState.gridx = 2;
		gbc_jLabelState.gridy = 0;
		this.add(getJLabelState(), gbc_jLabelState);
		
		GridBagConstraints gbc_jLabelStateDescription = new GridBagConstraints();
		gbc_jLabelStateDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelStateDescription.insets = new Insets(5, 5, 0, 10);
		gbc_jLabelStateDescription.gridx = 3;
		gbc_jLabelStateDescription.gridy = 0;
		this.add(getJLabelStateDescription(), gbc_jLabelStateDescription);
		
		GridBagConstraints gbc_jLabelHandlerClass = new GridBagConstraints();
		gbc_jLabelHandlerClass.anchor = GridBagConstraints.WEST;
		gbc_jLabelHandlerClass.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelHandlerClass.gridx = 0;
		gbc_jLabelHandlerClass.gridy = 1;
		this.add(getJLabelHandlerClass(), gbc_jLabelHandlerClass);
		
		GridBagConstraints gbc_jLabelHandlerClassValue = new GridBagConstraints();
		gbc_jLabelHandlerClassValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelHandlerClassValue.insets = new Insets(10, 5, 0, 0);
		gbc_jLabelHandlerClassValue.gridx = 1;
		gbc_jLabelHandlerClassValue.gridy = 1;
		this.add(getJLabelHandlerClassValue(), gbc_jLabelHandlerClassValue);
		
		GridBagConstraints gbc_jLabelSourceBundle = new GridBagConstraints();
		gbc_jLabelSourceBundle.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelSourceBundle.anchor = GridBagConstraints.WEST;
		gbc_jLabelSourceBundle.gridx = 2;
		gbc_jLabelSourceBundle.gridy = 1;
		this.add(getJLabelSourceBundle(), gbc_jLabelSourceBundle);
		
		GridBagConstraints gbc_jLabelSourceBundleDescription = new GridBagConstraints();
		gbc_jLabelSourceBundleDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelSourceBundleDescription.insets = new Insets(10, 5, 0, 10);
		gbc_jLabelSourceBundleDescription.gridx = 3;
		gbc_jLabelSourceBundleDescription.gridy = 1;
		this.add(getJLabelSourceBundleDescription(), gbc_jLabelSourceBundleDescription);
		
		GridBagConstraints gbc_jLabelServiceClass = new GridBagConstraints();
		gbc_jLabelServiceClass.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelServiceClass.anchor = GridBagConstraints.WEST;
		gbc_jLabelServiceClass.gridx = 2;
		gbc_jLabelServiceClass.gridy = 2;
		this.add(getJLabelServiceClass(), gbc_jLabelServiceClass);
		
		GridBagConstraints gbc_jLabelServiceClassDescription = new GridBagConstraints();
		gbc_jLabelServiceClassDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelServiceClassDescription.insets = new Insets(10, 5, 0, 10);
		gbc_jLabelServiceClassDescription.gridx = 3;
		gbc_jLabelServiceClassDescription.gridy = 2;
		this.add(getJLabelServiceClassDescription(), gbc_jLabelServiceClassDescription);
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
			color = new Color(0, 153, 0);
		} else {
			color = new Color(153, 0, 0);
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
				
	}
}
