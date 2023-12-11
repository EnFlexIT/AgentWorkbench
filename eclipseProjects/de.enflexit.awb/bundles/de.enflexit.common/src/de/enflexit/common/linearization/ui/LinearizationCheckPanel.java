package de.enflexit.common.linearization.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.enflexit.common.BundleHelper;
import de.enflexit.common.linearization.Linearization;

/**
 * The Class LinearizationCheckPanel.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class LinearizationCheckPanel extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 7721022766627592516L;

	private Linearization linearization;
	
	private JLabel jLabelHeader;
	private JButton jButtonDoCheck;
	private JScrollPane jScrollPaneCheckResult;
	private JTextArea jTextAreaCheckResult;
	
	
	/**
	 * Instantiates a new linearization range panel.
	 * @param linearization the linearization
	 */
	public LinearizationCheckPanel(Linearization linearization) {
		this.setLinearization(linearization);
		this.initialize();
	}
	
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		this.add(this.getJLabelHeader(), gbc_jLabelHeader);
		
		GridBagConstraints gbc_jButtonDoCheck = new GridBagConstraints();
		gbc_jButtonDoCheck.gridx = 1;
		gbc_jButtonDoCheck.gridy = 0;
		this.add(this.getJButtonDoCheck(), gbc_jButtonDoCheck);
		
		GridBagConstraints gbc_jScrollPaneCheckResult = new GridBagConstraints();
		gbc_jScrollPaneCheckResult.gridwidth = 2;
		gbc_jScrollPaneCheckResult.insets = new Insets(5, 0, 0, 0);
		gbc_jScrollPaneCheckResult.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneCheckResult.gridx = 0;
		gbc_jScrollPaneCheckResult.gridy = 1;
		this.add(this.getJScrollPaneCheckResult(), gbc_jScrollPaneCheckResult);
	}
	
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Result of the Validation Check");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	private JButton getJButtonDoCheck() {
		if (jButtonDoCheck == null) {
			jButtonDoCheck = new JButton();
			jButtonDoCheck.setIcon(BundleHelper.getImageIcon("ClearSearch.png"));
			jButtonDoCheck.setPreferredSize(new Dimension(26, 26));
			jButtonDoCheck.setMinimumSize(new Dimension(26, 26));
			jButtonDoCheck.setMaximumSize(new Dimension(26, 26));
			jButtonDoCheck.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					LinearizationCheckPanel.this.linearization.getValidator().doChecksInThread();
				}
			});
		}
		return jButtonDoCheck;
	}
	
	private JScrollPane getJScrollPaneCheckResult() {
		if (jScrollPaneCheckResult == null) {
			jScrollPaneCheckResult = new JScrollPane();
			jScrollPaneCheckResult.setViewportView(getJTextAreaCheckResult());
		}
		return jScrollPaneCheckResult;
	}
	private JTextArea getJTextAreaCheckResult() {
		if (jTextAreaCheckResult == null) {
			jTextAreaCheckResult = new JTextArea();
			jTextAreaCheckResult.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextAreaCheckResult.setWrapStyleWord(false);
			jTextAreaCheckResult.setLineWrap(true);
			jTextAreaCheckResult.setEditable(false);
		}
		return jTextAreaCheckResult;
	}
	/**
	 * Sets the validation messages.
	 * @param validationMessages the new validation messages
	 */
	public void updateValidationMessages() {
		
		this.getJTextAreaCheckResult().setText(null);

		// --- Do layout settings ---------------
		String msgPrefix = "";
		boolean isValid = this.linearization.getValidator().isValidLinearization();
		if (isValid==true) {
			this.getJTextAreaCheckResult().setForeground(new Color(0, 169,0));
		} else {
			msgPrefix = "- ";
			this.getJTextAreaCheckResult().setForeground(new Color(169, 0, 0));
		}
		// --- Fill text area -------------------
		for (String message : this.linearization.getValidator().getValidationMessages()) {
			this.getJTextAreaCheckResult().append(msgPrefix + message + "\n");
		}
	}
	
	/**
	 * Sets the current linearization instance.
	 * @param linearization the new linearization
	 */
	public void setLinearization(Linearization linearization) {
		if (linearization!=this.linearization) {
			if (this.linearization!=null) {
				this.linearization.removePropertyChangeListener(this);
			}
			this.linearization = linearization;
			if (this.linearization!=null) {
				this.linearization.addPropertyChangeListener(this);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		if (evt.getSource()==this.linearization) {
			switch (evt.getPropertyName()) {
			case Linearization.PROPERTY_VALIDATION_DONE:
				// --- Skip a reaction here ---
				break;
			default:
				this.linearization.getValidator().doChecksInThread();
				break;
			}
		}
	}
	
}
