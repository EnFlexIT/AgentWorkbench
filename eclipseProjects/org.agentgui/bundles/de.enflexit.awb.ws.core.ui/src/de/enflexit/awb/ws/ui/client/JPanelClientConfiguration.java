package de.enflexit.awb.ws.ui.client;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import de.enflexit.awb.ws.ui.WsConfigurationInterface;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * The Class JPanelClientConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelClientConfiguration extends JPanel implements WsConfigurationInterface {
	
	private static final long serialVersionUID = 7987858783733542296L;

	private JSplitPane splitPane;
	private JScrollPane jScrollPaneLeft;
	private JScrollPane jScrollPaneRight;

	private JList jListClients;
	
	/**
	 * Instantiates a new j panel client configuration.
	 */
	public JPanelClientConfiguration() {
		this.initialize();
	}
	private void initialize() {
		
		this.setBorder(BorderFactory.createEmptyBorder());
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.insets = new Insets(10, 10, 10, 10);
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
		add(getSplitPane(), gbc_splitPane);
	}
	
	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane();
			splitPane.setDividerLocation(280);
			splitPane.setResizeWeight(0.5);
			splitPane.setBorder(BorderFactory.createEmptyBorder());
			splitPane.setLeftComponent(getJScrollPaneLeft());
			splitPane.setRightComponent(getJScrollPaneRight());
		}
		return splitPane;
	}
	private JScrollPane getJScrollPaneLeft() {
		if (jScrollPaneLeft == null) {
			jScrollPaneLeft = new JScrollPane();
			jScrollPaneLeft.setViewportView(this.getJListClients());
		}
		return jScrollPaneLeft;
	}
	private JScrollPane getJScrollPaneRight() {
		if (jScrollPaneRight == null) {
			jScrollPaneRight = new JScrollPane();
		}
		return jScrollPaneRight;
	}
	private JList getJListClients() {
		if (jListClients == null) {
			jListClients = new JList();
		}
		return jListClients;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	 */
	@Override
	public boolean hasUnsavedChanges() {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#userConfirmedToChangeView()
	 */
	@Override
	public boolean userConfirmedToChangeView() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
