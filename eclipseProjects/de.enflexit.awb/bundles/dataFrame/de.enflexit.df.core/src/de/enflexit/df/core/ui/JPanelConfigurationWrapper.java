package de.enflexit.df.core.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import de.enflexit.df.core.BundleHelper;


/**
 * The Class JPanelConfigurationWrapper.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelConfigurationWrapper extends JPanel {

	private static final long serialVersionUID = 4889473189361960869L;
	
	private ConfigurationPanel configPanel;
	private List<JComponent> configToolBarElements;
	private JComponent configComponent;
	
	private JPanel jPanelNorth;
	
	private JToolBar jToolBarConfiguration;
	private JToolBar jToolBarCloseConfiguration;
	private JButton jButtonClose;
	private ActionListener closeActionListener;
	
	
	/**
	 * Instantiates a new j panel configuration wrapper.
	 */
	public JPanelConfigurationWrapper() {
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		this.setLayout(new BorderLayout(0, 0));
		this.add(this.getJPanelNorth(), BorderLayout.NORTH);
	}
	
	private JPanel getJPanelNorth() {
		if (jPanelNorth == null) {
			jPanelNorth = new JPanel();
			jPanelNorth.setLayout(new BorderLayout(0, 0));
			jPanelNorth.add(this.getJToolBarConfiguration(), BorderLayout.WEST);
			jPanelNorth.add(this.getJToolBarCloseConfiguration(), BorderLayout.EAST);
		}
		return jPanelNorth;
	}
	private JToolBar getJToolBarConfiguration() {
		if (jToolBarConfiguration == null) {
			jToolBarConfiguration = new JToolBar();
			jToolBarConfiguration.setFloatable(false);
			jToolBarConfiguration.setBorder(BorderFactory.createEmptyBorder());
		}
		return jToolBarConfiguration;
	}
	
	private JToolBar getJToolBarCloseConfiguration() {
		if (jToolBarCloseConfiguration == null) {
			jToolBarCloseConfiguration = new JToolBar();
			jToolBarCloseConfiguration.setFloatable(false);
			jToolBarCloseConfiguration.setBorder(BorderFactory.createEmptyBorder());
			jToolBarCloseConfiguration.add(this.getJButtonClose());
		}
		return jToolBarCloseConfiguration;
	}
	private JButton getJButtonClose() {
		if (jButtonClose == null) {
			jButtonClose = new JButton("");
			jButtonClose.setToolTipText("Close the configuration panel!");
			jButtonClose.setIcon(BundleHelper.getImageIcon("MBclose.png"));
			jButtonClose.setPreferredSize(new Dimension(26, 26));
			jButtonClose.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					if (closeActionListener!=null) {
						// --- Modify the source and forward to listener ------
						ae.setSource(JPanelConfigurationWrapper.this);
						closeActionListener.actionPerformed(ae);
					}
				}
			});
		}
		return jButtonClose;
	}
	
	/**
	 * Will set the specified configuration panel to this .
	 * @param newConfigPanel the new configuration panel
	 */
	public void setConfigurationPanel(ConfigurationPanel newConfigPanel) {

		// --- Already visualized? ----------------------------------
		if (newConfigPanel!=null && this.configPanel!=null && newConfigPanel==this.configPanel) return;
		
		// ----------------------------------------------------------
		// --- Remove elder components first ------------------------
		if (this.configToolBarElements!=null) {
			this.configToolBarElements.forEach(tbComp -> this.getJToolBarConfiguration().remove(tbComp));
			this.configToolBarElements = null;
		}
		if (this.configComponent!=null) {
			this.remove(this.configComponent);
			this.configComponent = null;
		}
		
		this.configPanel = newConfigPanel;
		
		// ----------------------------------------------------------
		// --- Add new components ? ---------------------------------
		if (this.configPanel==null) {
			// --- Just as a placeholder ----------------------------
			this.configComponent = new JPanel();
		
		} else {
			// --- Add the toolbar components -----------------------
			this.configToolBarElements = this.configPanel.getConfigurationToolbarComponents();
			if (this.configToolBarElements!=null) {
				this.configToolBarElements.forEach(tbComp -> this.getJToolBarConfiguration().add(tbComp));
			}
			
			this.configComponent = this.configPanel.getConfigurationPanel();
			if (this.configComponent==null) {
				this.configComponent = new JPanel();
			}
		}
		this.add(this.configComponent, BorderLayout.CENTER);
		this.validate();
		this.repaint();
	}
	
	/**
	 * Adds the close action listener.
	 * @param listener the listener
	 */
	public void addCloseActionListener(ActionListener listener) {
		this.closeActionListener = listener;
	}
	
}
