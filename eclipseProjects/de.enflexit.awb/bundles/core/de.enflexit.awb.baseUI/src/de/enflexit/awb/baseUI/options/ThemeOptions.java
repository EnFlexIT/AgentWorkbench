package de.enflexit.awb.baseUI.options;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import de.enflexit.awb.core.Application;
import de.enflexit.common.swing.AwbLookAndFeelAdjustments;
import de.enflexit.common.swing.AwbLookAndFeelInfo;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.language.Language;

/**
 * The Class ThemeOptions extends an {@link AbstractOptionTab} and is
 * used in to visually configure the Agent.Workbench Theme.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ThemeOptions extends AbstractOptionTab {

	private static final long serialVersionUID = 1L;
	
	private JLabel jLabelThemeSelection;	
	private DefaultComboBoxModel<AwbLookAndFeelInfo> comboBoxModel;
	private JComboBox<AwbLookAndFeelInfo> jComboBoxThemeSelection;
	private JButton jButtonApply;
	
	
	/**
	 * This is the default constructor
	 */
	public ThemeOptions() {
		super();
		this.initialize();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTitleAddition()
	 */
	@Override
	public String getTitle() {
		return Application.getApplicationTitle() + " - Theme";
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText() {
		return Application.getApplicationTitle() + " - Theme";
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 200, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);

		GridBagConstraints gbc_jLabelLoggingDirectory = new GridBagConstraints();
		gbc_jLabelLoggingDirectory.insets = new Insets(20, 20, 0, 5);
		gbc_jLabelLoggingDirectory.gridx = 0;
		gbc_jLabelLoggingDirectory.gridy = 0;
		this.add(this.getJLabelThemSelection(), gbc_jLabelLoggingDirectory);
		
		GridBagConstraints gbc_jTextFieldLogingDirectory = new GridBagConstraints();
		gbc_jTextFieldLogingDirectory.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldLogingDirectory.gridx = 1;
		gbc_jTextFieldLogingDirectory.gridy = 0;
		gbc_jTextFieldLogingDirectory.insets = new Insets(20, 5, 0, 0);
		this.add(this.getJComboBoxTheme(), gbc_jTextFieldLogingDirectory);
		
		GridBagConstraints gbc_jButtonApply = new GridBagConstraints();
		gbc_jButtonApply.gridx = 2;
		gbc_jButtonApply.gridy = 0;
		gbc_jButtonApply.insets = new Insets(20, 5, 0, 20);
		this.add(getJButtonApply(), gbc_jButtonApply);
	}
	
	private JLabel getJLabelThemSelection() {
		if (jLabelThemeSelection==null) {
			jLabelThemeSelection = new JLabel();
			jLabelThemeSelection.setText(Application.getApplicationTitle() + " - Theme:");
			jLabelThemeSelection.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelThemeSelection;
	}
	
	private DefaultComboBoxModel<AwbLookAndFeelInfo> getComboBoxModel() {
		if (comboBoxModel==null) {
			String lafClassNameActive = Application.getGlobalInfo().getAppLookAndFeelClassName();
			List<AwbLookAndFeelInfo> lafList = AwbLookAndFeelAdjustments.getAvailableLookAndFeels();
			comboBoxModel = new DefaultComboBoxModel<>();
			for (AwbLookAndFeelInfo lafInfo : lafList) {
				comboBoxModel.addElement(lafInfo);
				if (lafInfo.getClassName().equals(lafClassNameActive)==true) {
					comboBoxModel.setSelectedItem(lafInfo);
				}
			}
		}
		return comboBoxModel;
	}
	private JComboBox<AwbLookAndFeelInfo> getJComboBoxTheme() {
		if (jComboBoxThemeSelection == null) {
			jComboBoxThemeSelection = new JComboBox<AwbLookAndFeelInfo>(this.getComboBoxModel());
			jComboBoxThemeSelection.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jComboBoxThemeSelection;
	}
	
	private JButton getJButtonApply() {
		if (jButtonApply == null) {
			jButtonApply = new JButton();
			jButtonApply.setText(Language.translate("Anwenden"));
			jButtonApply.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApply.setForeground(new Color(0, 153, 0));
			jButtonApply.setPreferredSize(new Dimension(100, 26));
			jButtonApply.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ThemeOptions. this.applyNewLookAndFeel();
				}
			});
		}
		return jButtonApply;
	}
	
	/**
	 * Writes the form data to the properties file.
	 */
	private void applyNewLookAndFeel() {
		
		// ----------------------------------------------------------
		// --- Check if we have a new LookAndFeel selection ---------
		AwbLookAndFeelInfo awbLafNew = (AwbLookAndFeelInfo) this.getComboBoxModel().getSelectedItem();
		String lafClassNameNew    = awbLafNew.getClassName(); 
		String lafClassNameActive = Application.getGlobalInfo().getAppLookAndFeelClassName();
		// --- Exit here? ------------------------------------------- 
		if (lafClassNameNew.equals(lafClassNameActive)==true) return;
		
		
		// ----------------------------------------------------------
		// --- Remind new LookAndFeel -------------------------------
		Application.getGlobalInfo().setAppLookAndFeelClassName(lafClassNameNew);
		
		// --- Apply the new LookAndFeel to the current dialog ------
		Window owner = OwnerDetection.getOwnerWindowForComponent(this);
		AwbLookAndFeelAdjustments.setLookAndFeel(lafClassNameNew, owner);
		
		// --- Apply somewhere else? --------------------------------
		if (Application.isMainWindowInitiated()==true) {
			Application.getMainWindow().applyNewThemeSettings();
		}
	}
	
}  
