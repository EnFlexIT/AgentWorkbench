package de.enflexit.awb.baseUI.options;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.EtchedBorder;

import de.enflexit.awb.bgSystem.db.BgSystemDatabaseConnectionService;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.awb.core.config.GlobalInfo.MtpProtocol;
import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.SessionFactoryMonitor;
import de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState;
import de.enflexit.db.hibernate.gui.HibernateStateVisualizationService;
import de.enflexit.db.hibernate.gui.HibernateStateVisualizer;
import de.enflexit.language.Language;

/**
 * On this JPanel the starting options of AgentGUI can be set.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class StartOptions extends AbstractOptionTab implements ActionListener, HibernateStateVisualizationService {

	private static final long serialVersionUID = -5837814050254569584L;
	
	private OptionDialog optionDialog;

	private JPanel jPanelTop;
	private JRadioButton jRadioButtonRunAsApplication;
	private JRadioButton jRadioButtonRunAsServer;
	private JRadioButton jRadioButtonRunAsDeviceService;
	
	private JLabel jLabelRunsAs;
	private JButton jButtonApply;
	private JButton jButtonUseDefaults;
	
	private ExecutionMode executionModeOld = Application.getGlobalInfo().getExecutionMode();
	private ExecutionMode executionModeNew = Application.getGlobalInfo().getExecutionMode();

	
	private JPanel jPanel4ScrollPane;
	private JScrollPane jScrollPaneConfig;
	private JPanel jPanelConfig;

	private JPanelMasterConfiguration jPanelMasterConfiguration;
	private JPanelOwnMTP jPanelOwnMTP;
	private JPanelBackgroundSystem jPanelBackgroundService;
	private HibernatePanel jPanelHibernatePanel;
	private JButton jButtonDatabaseSettings;
	private JLabel jLabelHibernateHeader;
	private JLabel jLabelHibernateState;
	private JPanelEmbeddedSystemAgent jPanelEmbeddedSystemAgent;
	private JPanelMTPConfig jPanelMTPConfig;
	
	private List<AbstractJPanelForOptions> optionPanels;
	

	/**
	 * This is the Constructor.
	 * @param optionDialog the option dialog
	 */
	public StartOptions(OptionDialog optionDialog) {
		super(optionDialog);
		
		this.optionDialog = optionDialog;
		this.initialize();
		this.setGlobalData2Form();
		
		// --- Translate ----------------------------------
		jLabelRunsAs.setText(Application.getGlobalInfo().getApplicationTitle() + " - " + Language.translate("Starte als:"));
		
		jRadioButtonRunAsApplication.setText(Language.translate("Anwendung"));
		jRadioButtonRunAsServer.setText(Language.translate("Hintergrundsystem (Master / Slave)"));
		jRadioButtonRunAsDeviceService.setText(Language.translate("Dienst / Embedded System Agent"));
		jButtonApply.setText(Language.translate("Anwenden"));
		
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTitleAddition()
	 */
	@Override
	public String getTitle() {
		return Language.translate("Programmstart");
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText() {
		return Language.translate("Programmstart");
	}

	/**
	 * Gets the option panels.
	 * @return the option panels
	 */
	public List<AbstractJPanelForOptions> getOptionPanels() {
		if (optionPanels==null) {
			optionPanels = new ArrayList<AbstractJPanelForOptions>();
		}
		return optionPanels;
	}
	/**
	 * Register option panel.
	 * @param optionPanel the option panel
	 */
	private void registerOptionPanel(AbstractJPanelForOptions optionPanel) {
		this.getOptionPanels().add(optionPanel);
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		// --- Initiate all sub panels --------------------
		this.registerOptionPanel(this.getJPanelMasterConfiguration());
		this.registerOptionPanel(this.getJPanelOwnMTP());
		this.registerOptionPanel(this.getJPanelMTPConfig());
		this.registerOptionPanel(this.getJPanelBackgroundSystem());
		this.registerOptionPanel(this.getJPanelHibernateState());
		this.registerOptionPanel(this.getJPanelEmbeddedSystemAgent());
		
		this.setSize(770, 440);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		GridBagConstraints gbcJPanelTop = new GridBagConstraints();
		gbcJPanelTop.gridx = 0;
		gbcJPanelTop.fill = GridBagConstraints.HORIZONTAL;
		gbcJPanelTop.insets = new Insets(20, 20, 0, 20);
		gbcJPanelTop.anchor = GridBagConstraints.WEST;
		gbcJPanelTop.weightx = 1.0;
		gbcJPanelTop.weighty = 0.0;
		gbcJPanelTop.gridy = 0;
		this.add(this.getJPanelTop(), gbcJPanelTop);

		GridBagConstraints gbcJPanel4ScrollPane = new GridBagConstraints();
		gbcJPanel4ScrollPane.gridx = 0;
		gbcJPanel4ScrollPane.fill = GridBagConstraints.BOTH;
		gbcJPanel4ScrollPane.weightx = 1.0;
		gbcJPanel4ScrollPane.weighty = 1.0;
		gbcJPanel4ScrollPane.insets = new Insets(10, 10, 10, 10);
		gbcJPanel4ScrollPane.gridy = 1;
		this.add(this.getJPanel4ScrollPane(), gbcJPanel4ScrollPane);
	
		this.registerWindowListener();
		
	}
	
	/**
	 * Register window listener.
	 */
	private void registerWindowListener() {
		
		// --- Check for a Window that owns this panel --------------
		if (this.optionDialog!=null) {
			this.optionDialog.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentHidden(ComponentEvent e) {
					StartOptions.this.unregisterHibernateStateVisualizationService();
				}
			});
		}
		// --- register as HibernateStateVisualizationService -------
		this.registerHibernateStateVisualizationService();
		// --- Check the hibernate SessionFactory state -------------
		SessionFactoryMonitor monitor = HibernateUtilities.getSessionFactoryMonitor(BgSystemDatabaseConnectionService.SESSION_FACTORY_ID);
		setSessionFactoryState(monitor.getFactoryID(), monitor.getSessionFactoryState());
	}
	
	/**
	 * This method initializes jPanel4ScrollPane	.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel4ScrollPane() {
		if (jPanel4ScrollPane == null) {
			jPanel4ScrollPane = new JPanel();
			jPanel4ScrollPane.setLayout(new BorderLayout());
			jPanel4ScrollPane.add(this.getJScrollPaneConfig(), BorderLayout.CENTER);
		}
		return jPanel4ScrollPane;
	}
	
	/**
	 * This method initializes jRadioButtonRunAsApplication	.
	 * @return the j radio button run as application
	 */
	private JRadioButton getJRadioButtonRunAsApplication() {
		if (jRadioButtonRunAsApplication == null) {
			jRadioButtonRunAsApplication = new JRadioButton();
			jRadioButtonRunAsApplication.setText("Anwendung");
			jRadioButtonRunAsApplication.setSelected(true);
			jRadioButtonRunAsApplication.setPreferredSize(new Dimension(300, 24));
			jRadioButtonRunAsApplication.setActionCommand("runAsApplication");
			jRadioButtonRunAsApplication.addActionListener(this);
		}
		return jRadioButtonRunAsApplication;
	}
	/**
	 * This method initializes jRadioButtonRunAsServer	.
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRadioButtonRunAsServer() {
		if (jRadioButtonRunAsServer == null) {
			jRadioButtonRunAsServer = new JRadioButton();
			jRadioButtonRunAsServer.setText("Hintergrundsystem (Master / Slave)");
			jRadioButtonRunAsServer.setPreferredSize(new Dimension(300, 24));
			jRadioButtonRunAsServer.setActionCommand("runAsServer");
			jRadioButtonRunAsServer.addActionListener(this);
		}
		return jRadioButtonRunAsServer;
	}
	/**
	 * This method initializes jRadioButtonRunAsDevice	.
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRadioButtonRunAsDeviceService() {
		if (jRadioButtonRunAsDeviceService == null) {
			jRadioButtonRunAsDeviceService = new JRadioButton();
			jRadioButtonRunAsDeviceService.setText("Dienst / Embedded System Agent");
			jRadioButtonRunAsDeviceService.setPreferredSize(new Dimension(300, 24));
			jRadioButtonRunAsDeviceService.setActionCommand("runAsEmbeddedSystemAgent");
			jRadioButtonRunAsDeviceService.addActionListener(this);
		}
		return jRadioButtonRunAsDeviceService;
	}
	/**
	 * This method initializes jPanelTop	.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelTop() {
		if (jPanelTop == null) {
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.gridx = 1;
			gridBagConstraints28.anchor = GridBagConstraints.WEST;
			gridBagConstraints28.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints28.gridwidth = 1;
			gridBagConstraints28.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints28.gridy = 2;
			
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.insets = new Insets(0, 2, 0, 0);
			gridBagConstraints26.gridy = 0;
			gridBagConstraints26.weightx = 0.0;
			gridBagConstraints26.gridx = 4;
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.anchor = GridBagConstraints.EAST;
			gridBagConstraints25.gridx = 3;
			gridBagConstraints25.gridy = 0;
			gridBagConstraints25.weightx = 0.0;
			gridBagConstraints25.insets = new Insets(0, 20, 0, 0);
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.gridx = 1;
			gridBagConstraints18.gridy = 0;
			gridBagConstraints18.insets = new Insets(10, 20, 0, 10);
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints24.gridy = 1;
			gridBagConstraints24.ipadx = 0;
			gridBagConstraints24.anchor = GridBagConstraints.WEST;
			gridBagConstraints24.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints24.gridx = 1;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints23.gridy = 0;
			gridBagConstraints23.ipadx = 0;
			gridBagConstraints23.anchor = GridBagConstraints.SOUTH;
			gridBagConstraints23.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints23.weightx = 1.0;
			gridBagConstraints23.gridx = 1;
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.insets = new Insets(2, 0, 0, 0);
			gridBagConstraints20.gridy = 0;
			gridBagConstraints20.ipadx = 0;
			gridBagConstraints20.anchor = GridBagConstraints.WEST;
			gridBagConstraints20.weightx = 0.0;
			gridBagConstraints20.gridx = 0;
			
			jLabelRunsAs = new JLabel();
			jLabelRunsAs.setText("starte als:");
			jLabelRunsAs.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelTop = new JPanel();
			jPanelTop.setLayout(new GridBagLayout());
			jPanelTop.add(jLabelRunsAs, gridBagConstraints20);
			jPanelTop.add(getJRadioButtonRunAsApplication(), gridBagConstraints23);
			jPanelTop.add(getJRadioButtonRunAsServer(), gridBagConstraints24);
			jPanelTop.add(getJButtonApply(), gridBagConstraints25);
			jPanelTop.add(getJButtonUseDefaults(), gridBagConstraints26);
			jPanelTop.add(getJRadioButtonRunAsDeviceService(), gridBagConstraints28);
			
			ButtonGroup runAsGroup = new ButtonGroup();
			runAsGroup.add(jRadioButtonRunAsApplication);
			runAsGroup.add(jRadioButtonRunAsServer);
			runAsGroup.add(jRadioButtonRunAsDeviceService);
			
		}
		return jPanelTop;
	}

	/**
	 * Gets the JPanelMasterConfiguration.
	 * @return the JPanelMasterConfiguration
	 */
	private JPanelMasterConfiguration getJPanelMasterConfiguration() {
		if (jPanelMasterConfiguration == null) {
			jPanelMasterConfiguration = new JPanelMasterConfiguration(this.optionDialog, this);
		}
		return jPanelMasterConfiguration;
	}
	/**
	 * Gets the JPanelOwnMTP.
	 * @return the JPanelOwnMTP
	 */
	private JPanelOwnMTP getJPanelOwnMTP() {
		if (jPanelOwnMTP == null) {
			jPanelOwnMTP = new JPanelOwnMTP(this.optionDialog, this);
		}
		return jPanelOwnMTP;
	}
	/**
	 * Gets the JPanelBackgroundSystem.
	 * @return the JPanelBackgroundSystem
	 */
	private JPanelBackgroundSystem getJPanelBackgroundSystem() {
		if (jPanelBackgroundService == null) {
			jPanelBackgroundService = new JPanelBackgroundSystem(this.optionDialog, this);
		}
		return jPanelBackgroundService;
	}
	
	/**
	 * Returns the JPanel for the hibernate state.
	 * @return the j panel hibernate state
	 */
	private HibernatePanel getJPanelHibernateState() {
		if (jPanelHibernatePanel==null) {
			jPanelHibernatePanel = new HibernatePanel(this.optionDialog, this);
		}
		return jPanelHibernatePanel;
	}
	/**
	 * The wrapper Class HibernatePanel.
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	private class HibernatePanel extends AbstractJPanelForOptions {
		
		private static final long serialVersionUID = -3718990899506527691L;
		
		/**
		 * Instantiates a new hibernate panel.
		 *
		 * @param optionDialog the option dialog
		 * @param startOptions the start options
		 */
		public HibernatePanel(OptionDialog optionDialog, StartOptions startOptions) {
			super(optionDialog, startOptions);
			this.initialize();
		}
		private void initialize() {
			
			this.setLayout(new GridBagLayout());
			
			GridBagConstraints gbc_Header = new GridBagConstraints();
			gbc_Header.gridx = 0;
			gbc_Header.gridy = 0;
			gbc_Header.anchor = GridBagConstraints.WEST;
			gbc_Header.fill = GridBagConstraints.BOTH;
			gbc_Header.weightx = 0.0;
			gbc_Header.weighty = 0.0;
			gbc_Header.insets = new Insets(0, 0, 0, 0);
			this.add(StartOptions.this.getJLabelHibernateHeader(), gbc_Header);
			
			GridBagConstraints gbc_Button = new GridBagConstraints();
			gbc_Button.gridx = 1;
			gbc_Button.gridy = 0;
			gbc_Button.anchor = GridBagConstraints.EAST;
			gbc_Button.weightx = 0.0;
			gbc_Button.weighty = 0.0;
			gbc_Button.insets = new Insets(0, 0, 0, 0);
			this.add(StartOptions.this.getJButtonDatabaseSettings(), gbc_Button);
			
			GridBagConstraints gbc_Hibernate = new GridBagConstraints();
			gbc_Hibernate.gridx = 0;
			gbc_Hibernate.gridy = 1;
			gbc_Hibernate.fill = GridBagConstraints.HORIZONTAL;
			gbc_Hibernate.insets = new Insets(5, 0, 5, 0);
			gbc_Hibernate.anchor = GridBagConstraints.WEST;
			gbc_Hibernate.weightx = 0.0;
			gbc_Hibernate.weighty = 0.0;
			gbc_Hibernate.gridwidth = 2;
			this.add(StartOptions.this.getJLabelHibernateState(), gbc_Hibernate);
		}
		/* (non-Javadoc)
		 * @see de.enflexit.awb.baseUI.options.AbstractJPanelForOptions#setGlobalData2Form()
		 */
		@Override
		public void setGlobalData2Form() { }
		/* (non-Javadoc)
		 * @see de.enflexit.awb.baseUI.options.AbstractJPanelForOptions#setFormData2Global()
		 */
		@Override
		public void setFormData2Global() { }
		/* (non-Javadoc)
		 * @see de.enflexit.awb.baseUI.options.AbstractJPanelForOptions#errorFound()
		 */
		@Override
		public boolean errorFound() {
			return false;
		}
		/* (non-Javadoc)
		 * @see de.enflexit.awb.baseUI.options.AbstractJPanelForOptions#refreshView()
		 */
		@Override
		public void refreshView() {
		}
	}
	
	/**
	 * Gets the JLabel for the hibernate state.
	 * @return the j label hibernate state
	 */
	private JLabel getJLabelHibernateHeader() {
		if (jLabelHibernateHeader == null) {
			jLabelHibernateHeader = new JLabel("[Optional] " + Language.translate("Datenbank für den 'server.master'"));
			jLabelHibernateHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHibernateHeader;
	}
	private JButton getJButtonDatabaseSettings() {
		if (jButtonDatabaseSettings == null) {
			jButtonDatabaseSettings = new JButton();
			jButtonDatabaseSettings.setToolTipText("Edit database settings");
			jButtonDatabaseSettings.setIcon(GlobalInfo.getInternalImageIcon("edit.png"));
			jButtonDatabaseSettings.setSize(new Dimension(26, 26));
			jButtonDatabaseSettings.setMaximumSize(new Dimension(26, 26));
			jButtonDatabaseSettings.addActionListener(this);
		}
		return jButtonDatabaseSettings;
	}
	/**
	 * Gets the JLabel for the hibernate state.
	 * @return the j label hibernate state
	 */
	private JLabel getJLabelHibernateState() {
		if (jLabelHibernateState == null) {
			jLabelHibernateState = new JLabel("");
			jLabelHibernateState.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHibernateState;
	}
	/**
	 * Registers hibernate state visualization service.
	 */
	private void registerHibernateStateVisualizationService() {
		HibernateStateVisualizer.registerStateVisualizationService(this);
	}
	/**
	 * Unregister hibernate state visualization service.
	 */
	private void unregisterHibernateStateVisualizationService() {
		HibernateStateVisualizer.unregisterStateVisualizationService(this);
	}
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.gui.HibernateStateVisualizationService#setSessionFactoryState(java.lang.String, de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState)
	 */
	@Override
	public void setSessionFactoryState(String factoryID, SessionFactoryState sessionFactoryState) {

		if (factoryID.equals(BgSystemDatabaseConnectionService.SESSION_FACTORY_ID)==false) return;
		this.getJLabelHibernateState().setIcon(sessionFactoryState.getIconImage());
		this.getJLabelHibernateState().setText("SessionFactory '" + factoryID + "': " + sessionFactoryState.getDescription());
	}

	/**
	 * Gets the JPanelEmbeddedSystemAgent.
	 * @return the JPanelEmbeddedSystemAgent
	 */
	private JPanelEmbeddedSystemAgent getJPanelEmbeddedSystemAgent() {
		if (jPanelEmbeddedSystemAgent == null) {
			jPanelEmbeddedSystemAgent = new JPanelEmbeddedSystemAgent(this.optionDialog, this);
		}
		return jPanelEmbeddedSystemAgent;
	}

	/**
	 * Gets the JPanelMTPConfig.
	 * @return the JPanelMTPConfig
	 */
	private JPanelMTPConfig getJPanelMTPConfig() {
		if (jPanelMTPConfig == null) {
			jPanelMTPConfig = new JPanelMTPConfig(this.optionDialog, this);
		}
		return jPanelMTPConfig;
	}
	
	
	
	/**
	 * This method initializes jButtonUpdateSiteDefault	.
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonUseDefaults() {
		if (jButtonUseDefaults == null) {
			jButtonUseDefaults = new JButton();
			jButtonUseDefaults.setPreferredSize(new Dimension(45, 26));
			jButtonUseDefaults.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonUseDefaults.setToolTipText(Language.translate("Standard verwenden"));
			jButtonUseDefaults.setActionCommand("resetSettings");
			jButtonUseDefaults.addActionListener(this);
		}
		return jButtonUseDefaults;
	}
	
	/**
	 * This method initializes jButtonApply	.
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonApply() {
		if (jButtonApply == null) {
			jButtonApply = new JButton();
			jButtonApply.setText("Anwenden");
			jButtonApply.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApply.setForeground(AwbThemeColor.ButtonTextGreen.getColor());
			jButtonApply.setPreferredSize(new Dimension(100, 26));
			jButtonApply.setActionCommand("applySettings");
			jButtonApply.addActionListener(this);
		}
		return jButtonApply;
	}
	
	/**
	 * This method initializes jScrollPaneConfig	.
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneConfig() {
		if (jScrollPaneConfig == null) {
			jScrollPaneConfig = new JScrollPane();
			jScrollPaneConfig.setPreferredSize(new Dimension(100, 100));
			jScrollPaneConfig.getVerticalScrollBar().setUnitIncrement(10);
			jScrollPaneConfig.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		}
		return jScrollPaneConfig;
	}
	
	/**
	 * This method initializes jPanelConfig	.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelConfig() {
		if (jPanelConfig == null) {
			jPanelConfig = new JPanel();
			jPanelConfig.setLayout(new GridBagLayout());
			jPanelConfig.setSize(new Dimension(600, 20));
			jPanelConfig.setPreferredSize(new Dimension(600, 20));
		}
		return jPanelConfig;
	}
	
	/**
	 * Resets the jPanelConfig.
	 */
	private void resetJPanelConfigReset() {
		this.getJPanelConfig().removeAll();
		this.getJPanelConfig().setSize(new Dimension(600, 20));
		this.getJPanelConfig().setPreferredSize(new Dimension(600, 20));
	}
	/**
	 * Adds a new component to the jPanelConfig panel in vertical direction.
	 * @param newComponent the new component
	 */
	private void addToConfigPanel(Component newComponent) {
		
		int compsThere = this.getJPanelConfig().getComponentCount();
		
		GridBagConstraints gridBagC = new GridBagConstraints();
		gridBagC.gridx = 0;
		gridBagC.gridy = compsThere;
		gridBagC.fill = GridBagConstraints.NONE;
		gridBagC.insets = new Insets(10, 10, 5, 10);
		gridBagC.weightx = 1.0;
		gridBagC.anchor = GridBagConstraints.WEST;
		
		// --- Get old panel height -------------
		int oldPanelHeight = this.getJPanelConfig().getHeight();
		int oldPanelWidth = this.getJPanelConfig().getWidth();
		
		// --- new Component height -------------
		int newCompHeight = newComponent.getHeight();
		int newCompPrefHeight = (int) newComponent.getPreferredSize().getHeight();
		if (newCompPrefHeight>newCompHeight) newCompHeight = newCompPrefHeight;
		int newPanelHeight = oldPanelHeight + newCompHeight + gridBagC.insets.top + gridBagC.insets.bottom + 10; 
		
		// --- Set size and add component -------
		this.getJPanelConfig().setSize(new Dimension(oldPanelWidth, newPanelHeight));
		this.getJPanelConfig().setPreferredSize(new Dimension(oldPanelWidth, newPanelHeight));
		this.getJPanelConfig().add(newComponent, gridBagC);
		this.addHorizontalSeparator();
		
		this.getJPanelConfig().revalidate();
		this.getJPanelConfig().repaint();
	}
	/**
	 * Adds a horizontal separator to the local config panel.
	 */
	private void addHorizontalSeparator() {
		
		int compsThere = this.getJPanelConfig().getComponentCount();
		
		GridBagConstraints gridBagC = new GridBagConstraints();
		gridBagC.fill = GridBagConstraints.HORIZONTAL;
		gridBagC.gridx = 0;
		gridBagC.gridy = compsThere;
		gridBagC.fill = GridBagConstraints.HORIZONTAL;
		gridBagC.insets = new Insets(0, 10, 0, 10);
		this.getJPanelConfig().add(new JSeparator(), gridBagC);
	}
	/**
	 * Adds the to config panel completion.
	 */
	private void addToConfigPanelCompletion() {
		
		int compsThere = this.getJPanelConfig().getComponentCount();
		
		GridBagConstraints gridBagC = new GridBagConstraints();
		gridBagC.gridx = 0;
		gridBagC.gridy = compsThere;
		gridBagC.fill = GridBagConstraints.BOTH;
		gridBagC.insets = new Insets(10, 10, 5, 10);
		gridBagC.weightx = 1.0;
		gridBagC.weighty = 1.0;

		JLabel jLabelDummy = new JLabel("");

		this.getJPanelConfig().add(jLabelDummy, gridBagC);
		this.getJPanelConfig().revalidate();
		this.getJPanelConfig().repaint();
	}
	
	/**
	 * Returns the selected execution mode.
	 * @return the selected execution mode
	 */
	public ExecutionMode getSelectedExecutionMode() {
		return executionModeNew;
	}
	
	/**
	 * This method handles and refreshes the view.
	 */
	private void refreshView() {
		
		// --- Reset the configuration panel first --------
		this.resetJPanelConfigReset();
		
		// --- Add the components as needed ---------------
		switch (this.getSelectedExecutionMode()) {
		case APPLICATION:
			this.addToConfigPanel(this.getJPanelMasterConfiguration());
			this.addToConfigPanel(this.getJPanelOwnMTP());
			this.addToConfigPanel(this.getJPanelMTPConfig());
			break;

		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			this.addToConfigPanel(this.getJPanelMasterConfiguration());
			this.addToConfigPanel(this.getJPanelOwnMTP());
			this.addToConfigPanel(this.getJPanelMTPConfig());
			this.addToConfigPanel(this.getJPanelBackgroundSystem());
			this.addToConfigPanel(this.getJPanelHibernateState());
			break;
			
		case DEVICE_SYSTEM:
			this.addToConfigPanel(this.getJPanelMasterConfiguration());
			this.addToConfigPanel(this.getJPanelOwnMTP());
			this.addToConfigPanel(this.getJPanelMTPConfig());
			this.addToConfigPanel(this.getJPanelEmbeddedSystemAgent());
			break;

		}

		// --- Refresh view of registered option panels ---
		for (AbstractJPanelForOptions optionPanel : this.getOptionPanels()) {
			optionPanel.refreshView();
		}
		
		// --- Add completion dummy JLabel ----------------
		this.addToConfigPanelCompletion();
		
		// --- Refresh the view in the scroll pane --------
		this.getJScrollPaneConfig().setViewportView(this.getJPanelConfig());
		this.getJScrollPaneConfig().revalidate();
		this.getJScrollPaneConfig().repaint();
	}
	
	/**
	 * This method sets the Data from the global Area to the Form.
	 */
	private void setGlobalData2Form(){
		
		switch (Application.getGlobalInfo().getExecutionMode()) {
		case APPLICATION:
			this.getJRadioButtonRunAsApplication().setSelected(true);
			this.getJRadioButtonRunAsServer().setSelected(false);
			this.getJRadioButtonRunAsDeviceService().setSelected(false);
			break;
			
		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			this.getJRadioButtonRunAsApplication().setSelected(false);
			this.getJRadioButtonRunAsServer().setSelected(true);
			this.getJRadioButtonRunAsDeviceService().setSelected(false);
			break;

		case DEVICE_SYSTEM:
			this.getJRadioButtonRunAsApplication().setSelected(false);
			this.getJRadioButtonRunAsServer().setSelected(false);
			this.getJRadioButtonRunAsDeviceService().setSelected(true);
			break;
		}
		
		// --- Do the same action in the option panels ----
		for (AbstractJPanelForOptions optionPanel : this.getOptionPanels()) {
			optionPanel.setGlobalData2Form();
		}
		this.refreshView();
	}
	
	/**
	 * This method writes the data back from the form to the global area.
	 */
	private void setFormData2Global() {
		
		ExecutionMode newMode = null;
		if (this.jRadioButtonRunAsApplication.isSelected()) {
			newMode = ExecutionMode.APPLICATION;
		} else if (this.jRadioButtonRunAsServer.isSelected()) {
			newMode = ExecutionMode.SERVER;
		} else if (this.jRadioButtonRunAsDeviceService.isSelected()) {
			newMode = ExecutionMode.DEVICE_SYSTEM;
		}
		if (newMode!=null) {
			Application.getGlobalInfo().setExecutionMode(newMode);
		}
		
		// --- Do the same action in the option panels ----
		for (AbstractJPanelForOptions optionPanel : this.getOptionPanels()) {
			optionPanel.setFormData2Global();
		}
	}
	
	/**
	 * This method doe's the Error-Handling for this Dialog.
	 * @return true or false
	 */
	private boolean errorFound() {
		// --- Do the same action in the option panels ----
		for (AbstractJPanelForOptions optionPanel : this.getOptionPanels()) {
			if (optionPanel.errorFound()) return true;
		}
		return false;
	}
	
	/**
	 * Doe's the actions when using the OK-Button.
	 */
	private void doOkAction() {
		
		String newLine = Application.getGlobalInfo().getNewLineSeparator();
		
		// --- Error-Handling -------------------------------------------------
		if (errorFound()==true) {
			return;
		}
		// --- If a change from 'Application' to 'Server' occurs --------------
		if (this.executionModeNew!=this.executionModeOld) {
			// ----------------------------------------------------------------
			// --- Restart application because it was switched ----------------
			// --- between ExecutionModes, but ask the user before ------------
			// ----------------------------------------------------------------
			String executionModeTextNew = Application.getGlobalInfo().getExecutionModeDescription(this.executionModeNew);
			String msgHead = "";
			String msgText = "";
			
			msgHead += Application.getGlobalInfo().getApplicationTitle() + ": " + Language.translate("Anwendung umschalten ?");
			msgText += Language.translate("Progamm umschalten auf") + " '" + executionModeTextNew + "':" + newLine; 	
			msgText += Language.translate("Möchten Sie die Anwendung nun umschalten ?");

			int msgAnswer = JOptionPane.showConfirmDialog(this.optionDialog, msgText, msgHead, JOptionPane.YES_NO_OPTION);
			if (msgAnswer==JOptionPane.NO_OPTION) {
				return;
			}
			// ----------------------------------------------------------------
		}
		this.setFormData2Global();
		Application.getGlobalInfo().doSaveConfiguration();
		// --------------------------------------------------------------------
		this.applySettings();
		// --------------------------------------------------------------------
		this.optionDialog.setVisible(false);
	}

	/**
	 * Apply settings.
	 */
	private void applySettings(){
		
		if (this.executionModeNew==this.executionModeOld) {
			// ------------------------------------------------------
			// --- Same ExecutionMode -------------------------------
			// ------------------------------------------------------
			switch (this.executionModeOld) {
			case APPLICATION:
				// --- Do nothing in this case ----------------------
				break;
			
			case SERVER:
			case SERVER_MASTER:
			case SERVER_SLAVE:
				// --- Background System Modus ----------------------
				System.out.println("\n" + Language.translate("Neustart des Server-Dienstes") + " ...");
				Application.getJadePlatform().stop(false);
				Application.removeTrayIcon();
				Application.startAgentWorkbench();
				break;
				
			case DEVICE_SYSTEM:
				// --- Device / Embedded System Agent ---------------
				System.out.println("\n" + Language.translate("Neustart") + " " + Application.getGlobalInfo().getExecutionModeDescription(this.executionModeNew) + " ...");
				Application.getJadePlatform().stop(false);
				if (Application.getProjectsLoaded()!= null) {
					if (Application.getProjectsLoaded().closeAll(this.optionDialog, true)==false) return;	
				}		
				Application.setMainWindow(null);
				Application.removeTrayIcon();	
				Application.startAgentWorkbench();
				break;
			}
			
		} else {
			// ------------------------------------------------------
			// --- New ExecutionMode --------------------------------
			// ------------------------------------------------------
			String textPrefix = Language.translate("Umschalten von");
			String executionModeTextOld = Application.getGlobalInfo().getExecutionModeDescription(this.executionModeOld);
			String textMiddle = Language.translate("auf");
			String executionModeTextNew = Application.getGlobalInfo().getExecutionModeDescription(this.executionModeNew);
			System.out.println(textPrefix + " '" + executionModeTextOld + "' " + textMiddle + " '" + executionModeTextNew + "'");
			
			// ------------------------------------------------------
			// --- Controlled shutdown of the current execution -----
			Application.getJadePlatform().stop(false);
			
			// --- Case separation for current ExecutionMode --------
			switch (this.executionModeOld) {
			case APPLICATION:
				// --- Application Modus ----------------------------
				if (Application.getProjectsLoaded()!= null) {
					if (Application.getProjectsLoaded().closeAll(this.optionDialog) == false ) return;	
				}		
				// --- Close main window and TrayIcon ---------------
				Application.setMainWindow(null);
				Application.removeTrayIcon();
				break;

			case SERVER:
			case SERVER_MASTER:
			case SERVER_SLAVE:
				// --- Background System Modus ----------------------
				Application.removeTrayIcon();
				break;
				
			case DEVICE_SYSTEM:
				// --- Device / Embedded System Agent ---------------
				if (Application.getProjectsLoaded()!= null) {
					if (Application.getProjectsLoaded().closeAll(this.optionDialog, true)==false) return;	
				}		
				Application.setMainWindow(null);
				Application.removeTrayIcon();	
				//Application.stopLoggingWriter(); TODO Check if to be used 
				break;
			}
			// --- Restart ------------------------------------------
			Application.startAgentWorkbench();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String actCMD = ae.getActionCommand();
		if (actCMD.equalsIgnoreCase("runAsApplication") || actCMD.equalsIgnoreCase("runAsServer") || actCMD.equalsIgnoreCase("runAsEmbeddedSystemAgent")) {
			if (getJRadioButtonRunAsApplication().isSelected()) {
				this.executionModeNew = ExecutionMode.APPLICATION;
			} else if (getJRadioButtonRunAsServer().isSelected()) {
				this.executionModeNew = ExecutionMode.SERVER;
			} else if (getJRadioButtonRunAsDeviceService().isSelected()) {
				this.executionModeNew = ExecutionMode.DEVICE_SYSTEM;
			}
			this.refreshView();
			
		} else if (actCMD.equalsIgnoreCase("resetSettings")) {
			this.executionModeNew = this.executionModeOld;
			this.setGlobalData2Form();
			this.getJPanelMTPConfig().getJComboBoxMtpProtocol().setSelectedProtocol(MtpProtocol.HTTP);
			this.getJPanelMTPConfig().hideCertificateSettings();
			
		} else if (actCMD.equalsIgnoreCase("applySettings")) {
			if (getJPanelMTPConfig().getJComboBoxMtpProtocol().getSelectedProtocol()!= getJPanelMasterConfiguration().getJcomboboxMtpProtocol().getSelectedProtocol()) {
				String title = Language.translate("Different MTP-protocols configured!", Language.EN); 
				String msg = Language.translate("Please, choose the same Protocol for the server.master and the MTP-protocol!", Language.EN);
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
			} else {
				this.doOkAction();
			}
			
		} else if (ae.getSource()==this.getJButtonDatabaseSettings()) {
			Application.showDatabaseDialog(BgSystemDatabaseConnectionService.SESSION_FACTORY_ID);
			
		} else {
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + actCMD);
		}
	}


}  
