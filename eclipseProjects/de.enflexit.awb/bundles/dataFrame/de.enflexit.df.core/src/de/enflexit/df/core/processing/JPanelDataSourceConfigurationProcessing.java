package de.enflexit.df.core.processing;

import de.enflexit.df.core.dataSources.integration.AbstractJPanelDataSourceConfiguration;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JTextArea;

/**
 * Configuration panel for {@link ProcessingDataSource}s.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JPanelDataSourceConfigurationProcessing extends AbstractJPanelDataSourceConfiguration<ProcessingDataSource, ProcessingDataSourceIntegration> implements ActionListener, DocumentListener{

	private static final long serialVersionUID = -4901618027081570929L;
	
	private JLabel jLabelProcessingConfiguration;
	private JLabel jLabelName;
	private JTextField jTextFieldName;
	private JLabel jLabelDescription;
	private JTextArea jTextAreaDescription;

	/**
	 * Instantiates a new j panel data source configuration processing.
	 *
	 * @param dsIntegration the ds integration
	 */
	public JPanelDataSourceConfigurationProcessing(ProcessingDataSourceIntegration dsIntegration) {
		super(dsIntegration);
		this.initialize();
		this.setDataSourceToUI();
	}
	
	/**
	 * Initializes the UI components.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelProcessingConfiguration = new GridBagConstraints();
		gbc_jLabelProcessingConfiguration.anchor = GridBagConstraints.WEST;
		gbc_jLabelProcessingConfiguration.gridwidth = 2;
		gbc_jLabelProcessingConfiguration.insets = new Insets(5, 5, 5, 0);
		gbc_jLabelProcessingConfiguration.gridx = 0;
		gbc_jLabelProcessingConfiguration.gridy = 0;
		add(getJLabelProcessingConfiguration(), gbc_jLabelProcessingConfiguration);
		GridBagConstraints gbc_jLabelName = new GridBagConstraints();
		gbc_jLabelName.anchor = GridBagConstraints.WEST;
		gbc_jLabelName.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelName.gridx = 0;
		gbc_jLabelName.gridy = 1;
		add(getJLabelName(), gbc_jLabelName);
		GridBagConstraints gbc_jTextFieldName = new GridBagConstraints();
		gbc_jTextFieldName.insets = new Insets(5, 5, 5, 0);
		gbc_jTextFieldName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldName.gridx = 1;
		gbc_jTextFieldName.gridy = 1;
		add(getJTextFieldName(), gbc_jTextFieldName);
		GridBagConstraints gbc_jLabelDescription = new GridBagConstraints();
		gbc_jLabelDescription.anchor = GridBagConstraints.NORTH;
		gbc_jLabelDescription.insets = new Insets(5, 5, 0, 5);
		gbc_jLabelDescription.gridx = 0;
		gbc_jLabelDescription.gridy = 2;
		add(getJLabelDescription(), gbc_jLabelDescription);
		GridBagConstraints gbc_jTextAreaDescription = new GridBagConstraints();
		gbc_jTextAreaDescription.insets = new Insets(5, 5, 5, 5);
		gbc_jTextAreaDescription.fill = GridBagConstraints.BOTH;
		gbc_jTextAreaDescription.gridx = 1;
		gbc_jTextAreaDescription.gridy = 2;
		add(getJTextAreaDescription(), gbc_jTextAreaDescription);
	}

	private JLabel getJLabelProcessingConfiguration() {
		if (jLabelProcessingConfiguration == null) {
			jLabelProcessingConfiguration = new JLabel("Data Processing Configuration");
			jLabelProcessingConfiguration.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelProcessingConfiguration;
	}
	private JLabel getJLabelName() {
		if (jLabelName == null) {
			jLabelName = new JLabel("Name.");
			jLabelName.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelName;
	}
	private JTextField getJTextFieldName() {
		if (jTextFieldName == null) {
			jTextFieldName = new JTextField();
			jTextFieldName.setColumns(10);
			jTextFieldName.addActionListener(this);
			jTextFieldName.getDocument().addDocumentListener(this);
		}
		return jTextFieldName;
	}
	private JLabel getJLabelDescription() {
		if (jLabelDescription == null) {
			jLabelDescription = new JLabel("Description:");
			jLabelDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelDescription;
	}
	private JTextArea getJTextAreaDescription() {
		if (jTextAreaDescription == null) {
			jTextAreaDescription = new JTextArea();
			jTextAreaDescription.getDocument().addDocumentListener(this);
		}
		return jTextAreaDescription;
	}
	
	private void setDataSourceToUI( ) {
		ProcessingDataSource pds = this.getDataSource();
		
		if (pds!=null) {
			this.getJTextFieldName().setText(pds.getName());
			this.getJTextAreaDescription().setText(pds.getDescription());
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJTextFieldName()) {
			this.getDataSource().setName(this.getJTextFieldName().getText());
			this.getJTextAreaDescription().requestFocus();
			this.informDataSourceSettingChanged(ProcessingDataSource.CHANGED_NAME);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent de) {
		this.onDocumentEvent(de);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent de) {
		this.onDocumentEvent(de);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent de) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Handles document events.
	 * @param de the document event to handle
	 */
	private void onDocumentEvent(DocumentEvent de) {
		if (de.getDocument() == this.getJTextFieldName().getDocument()) {
			this.getDataSource().setName(this.getJTextFieldName().getText());
			this.informDataSourceSettingChanged(ProcessingDataSource.CHANGED_NAME);
		} else if (de.getDocument() == this.getJTextAreaDescription().getDocument()) {
			this.getDataSource().setDescription(this.getJTextAreaDescription().getText());
			this.informDataSourceSettingChanged(ProcessingDataSource.CHANGED_DESCRIPTION);
		}
	}

}
