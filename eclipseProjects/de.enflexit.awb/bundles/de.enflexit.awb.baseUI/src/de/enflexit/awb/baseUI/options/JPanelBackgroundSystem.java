package de.enflexit.awb.baseUI.options;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import de.enflexit.awb.core.Application;
import de.enflexit.language.Language;

/**
 * The Class JPanelBackgroundSystem is used to set the Background System settings.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JPanelBackgroundSystem extends AbstractJPanelForOptions {

	private static final long serialVersionUID = 1859223400031520868L;

	private JLabel jLabelServerHeader;
	private JCheckBox jCheckBoxAutoStart;
	
	/**
	 * This is the Constructor.
	 *
	 * @param optionDialog the option dialog
	 * @param startOptions the start options
	 */
	public JPanelBackgroundSystem(OptionDialog optionDialog, StartOptions startOptions) {
		super(optionDialog, startOptions);
		this.initialize();
		// --- Translate ----------------------------------
		jLabelServerHeader.setText(Application.getGlobalInfo().getApplicationTitle() + " " + Language.translate("Hintergrundsystem - Konfiguration"));
		jCheckBoxAutoStart.setText(" " + Language.translate("Hintergrundsystem beim Programmstart automatisch initialisieren"));

	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		jLabelServerHeader = new JLabel();
		jLabelServerHeader.setText("Agent.GUI Hintergrundsystem - Konfiguration");
		jLabelServerHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		GridBagConstraints gbc_jLabelServerHeader = new GridBagConstraints();
		gbc_jLabelServerHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelServerHeader.insets = new Insets(0, 0, 5, 0);
		gbc_jLabelServerHeader.gridx = 0;
		gbc_jLabelServerHeader.gridy = 0;
		this.add(jLabelServerHeader, gbc_jLabelServerHeader);
		GridBagConstraints gbc_jCheckBoxAutoStart = new GridBagConstraints();
		gbc_jCheckBoxAutoStart.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxAutoStart.gridx = 0;
		gbc_jCheckBoxAutoStart.gridy = 1;
		this.add(getJCheckBoxAutoStart(), gbc_jCheckBoxAutoStart);
		
		
	}
	
	/**
	 * This method initializes jCheckBoxAutoStart	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxAutoStart() {
		if (jCheckBoxAutoStart == null) {
			jCheckBoxAutoStart = new JCheckBox();
			jCheckBoxAutoStart.setText("Hintergrundsystem beim Programmstart automatisch initialisieren");
		}
		return jCheckBoxAutoStart;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#refreshView()
	 */
	@Override
	public void refreshView() {
		
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#setGlobalData2Form()
	 */
	@Override
	public void setGlobalData2Form(){
		if (this.getGlobalInfo().isServerAutoRun()== true) {
			this.getJCheckBoxAutoStart().setSelected(true);	
		} else {
			this.getJCheckBoxAutoStart().setSelected(false);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#setFromData2Global()
	 */
	@Override
	public void setFormData2Global() {
		this.getGlobalInfo().setServerAutoRun(this.jCheckBoxAutoStart.isSelected());
		
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#errorFound()
	 */
	@Override
	public boolean errorFound() {
		return false;
	}

}
