package de.enflexit.awb.ws.ui.server;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * The Class JPanelServerConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelServerConfiguration extends JPanel {

	private static final long serialVersionUID = -1935493940628529036L;
	
	private JSplitPane splitPane;
	private JScrollPane jScrollPaneLeft;
	private JScrollPane jScrollPaneRight;
	
	private ServerTree jTreeServer;
	
	
	/**
	 * Instantiates a new j panel server configuration.
	 */
	public JPanelServerConfiguration() {
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
			splitPane.setDividerLocation(320);
			splitPane.setResizeWeight(0.5);
			splitPane.setBorder(BorderFactory.createEmptyBorder());
			splitPane.setLeftComponent(this.getJScrollPaneLeft());
			splitPane.setRightComponent(this.getJScrollPaneRight());
		}
		return splitPane;
	}
	private JScrollPane getJScrollPaneLeft() {
		if (jScrollPaneLeft == null) {
			jScrollPaneLeft = new JScrollPane();
			jScrollPaneLeft.setViewportView(this.getJTreeServer());
		}
		return jScrollPaneLeft;
	}
	private JScrollPane getJScrollPaneRight() {
		if (jScrollPaneRight == null) {
			jScrollPaneRight = new JScrollPane();
		}
		return jScrollPaneRight;
	}
	private ServerTree getJTreeServer() {
		if (jTreeServer == null) {
			jTreeServer = new ServerTree();
		}
		return jTreeServer;
	}
	
	
}
