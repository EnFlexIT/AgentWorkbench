package agentgui.core.charts.timeseriesChart.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.components.JComboBoxWide;
import agentgui.core.gui.components.JPanelForActions;
import agentgui.simulationService.time.TimeFormat;
import agentgui.simulationService.time.TimeFormatVector;
import agentgui.simulationService.time.TimeModelDateBased;

public class TimeFormatSelector extends JPanelForActions {

	private static final long serialVersionUID = 1L;
	
	final static String PathImage = Application.getGlobalInfo().PathImageIntern();
	
	private JLabel jLabelFormat = null;
	private JTextField jTextFieldTimeFormat = null;
	private JButton jButtonTimeFormatDefault = null;
	private JComboBoxWide jComboBoxTimeFormat = null;

	private JPanel jPanelDummy = null;

	/** The parent GUI component */
	private TimeSeriesChartSettingsTab parent = null;

	
	/**
	 * This is the default constructor.
	 */
	public TimeFormatSelector(TimeSeriesChartSettingsTab parent) {
		super();
		this.parent = parent;
		initialize();
	}

	/**
	 * This method initializes this.
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
		gridBagConstraints41.gridx = 0;
		gridBagConstraints41.fill = GridBagConstraints.BOTH;
		gridBagConstraints41.weightx = 1.0;
		gridBagConstraints41.weighty = 1.0;
		gridBagConstraints41.gridwidth = 3;
		gridBagConstraints41.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints41.gridy = 3;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.gridy = 2;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.insets = new Insets(7, 0, 0, 0);
		gridBagConstraints3.gridx = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 2;
		gridBagConstraints2.insets = new Insets(0, 5, 0, 0);
		gridBagConstraints2.gridy = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(0, 5, 0, 0);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridy = 1;
		
//		jLabelFormat = new JLabel();
//		jLabelFormat.setText("Format");
//		jLabelFormat.setPreferredSize(new Dimension(40, 16));
//		jLabelFormat.setText(Language.translate(jLabelFormat.getText()) + ":");
		
		this.setSize(427, 65);
		this.setLayout(new GridBagLayout());
		this.add(getJTextFieldTimeFormat(), gridBagConstraints1);
		this.add(getJButtonTimeFormatDefault(), gridBagConstraints2);
		if (jLabelFormat!=null) {
			this.add(jLabelFormat, gridBagConstraints);	
		}
		this.add(getJComboBoxTimeFormat(), gridBagConstraints3);
		this.add(getJPanelDummy(), gridBagConstraints41);
	}

	/**
	 * This method initializes jTextFieldTimeFormat.
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldTimeFormat() {
		if (jTextFieldTimeFormat == null) {
			jTextFieldTimeFormat = new JTextField();
			jTextFieldTimeFormat.setPreferredSize(new Dimension(30, 26));
			jTextFieldTimeFormat.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					fireActionEvent();
				}
				@Override
				public void insertUpdate(DocumentEvent e) {
					fireActionEvent();
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
					fireActionEvent();
				}
			});
		}
		return jTextFieldTimeFormat;
	}
	/**
	 * This method initializes jButtonTimeFormatDefault.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonTimeFormatDefault() {
		if (jButtonTimeFormatDefault == null) {
			jButtonTimeFormatDefault = new JButton();
			jButtonTimeFormatDefault.setPreferredSize(new Dimension(45, 26));
			jButtonTimeFormatDefault.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonTimeFormatDefault.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBreset.png")));
			jButtonTimeFormatDefault.setToolTipText("Agent.GUI - Standard verwenden");
			jButtonTimeFormatDefault.setToolTipText(Language.translate(jButtonTimeFormatDefault.getToolTipText()));
			jButtonTimeFormatDefault.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getJTextFieldTimeFormat().setText(parent.getDefaultTimeFormat());
					fireActionEvent();
				}
			});
		}
		return jButtonTimeFormatDefault;
	}
	/**
	 * This method initializes jComboBoxTimeFormat.
	 * @return javax.swing.JComboBox
	 */
	private JComboBoxWide getJComboBoxTimeFormat() {
		if (jComboBoxTimeFormat == null) {
			final DefaultComboBoxModel cbm = new DefaultComboBoxModel(new TimeFormatVector());
			jComboBoxTimeFormat = new JComboBoxWide(cbm);
			jComboBoxTimeFormat.setToolTipText("Vorlagen");
			jComboBoxTimeFormat.setToolTipText(Language.translate(jComboBoxTimeFormat.getToolTipText()));
			jComboBoxTimeFormat.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					TimeFormat tf = (TimeFormat) cbm.getSelectedItem();
					getJTextFieldTimeFormat().setText(tf.getFormat());
					fireActionEvent();
				}
			});
		}
		return jComboBoxTimeFormat;
	}
	/**
	 * This method initializes jPanelDummy	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelDummy() {
		if (jPanelDummy == null) {
			jPanelDummy = new JPanel();
			jPanelDummy.setLayout(new GridBagLayout());
		}
		return jPanelDummy;
	}

	/**
	 * Sets the time format.
	 * @param newTimeFormat the new time format
	 */
	public void setTimeFormat(String newTimeFormat) {
		if (newTimeFormat==null) {
			this.getJTextFieldTimeFormat().setText(this.getTimeFormatDefault());
			if(! this.getJComboBoxTimeFormat().getSelectedItem().equals(this.getTimeFormatDefault())){
				this.getJComboBoxTimeFormat().setSelectedItem(this.getTimeFormatDefault());
			}
		} else {
			this.getJTextFieldTimeFormat().setText(newTimeFormat);
			if(! this.getJComboBoxTimeFormat().getSelectedItem().equals(newTimeFormat)){
				this.getJComboBoxTimeFormat().setSelectedItem(newTimeFormat);
			}
		}
	}
	/**
	 * Gets the time format.
	 * @return the time format
	 */
	public String getTimeFormat() {
		if (this.getJTextFieldTimeFormat().getText()==null) {
			this.setTimeFormat(this.getTimeFormatDefault());
		}
		return this.getJTextFieldTimeFormat().getText();
	}
	
	/**
	 * Returns the default time format.
	 * @return the default time format
	 */
	public String getTimeFormatDefault() {
		return TimeModelDateBased.DEFAULT_TIME_FORMAT;
	}
	
	/**
	 * Fires a action event for this panel.
	 */
	private void fireActionEvent() {
		ActionEvent ae = new ActionEvent(this, 0, "DateFormatChnaged");
		this.fireUpdate(ae);
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
