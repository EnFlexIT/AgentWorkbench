package de.enflexit.df.core.workbook.ui;

import javax.swing.JPanel;

import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;
import de.enflexit.df.core.workbook.DataWorkbook4JSON;
import de.enflexit.df.core.workbook.DataWorkbook4XML;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * The Class JPanelDataWorkbookInFile.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelDataWorkbookInFile extends JPanel implements DocumentListener {

	private static final long serialVersionUID = 621465097935840406L;
	
	private DataController dataController;
	private DataWorkbook dataWorkbook;
	
	private JLabel jLabelCaptionFile;
	private JLabel jLabelFilePath;
	private JLabel jLableCaptionName;
	private JLabel jLabelDescription;
	private JScrollPane jScrollPaneDescription;
	private JTextArea jTextAreaDescription;
	private JLabel jLabelCaptionID;
	private JTextField jTextFieldName;
	private JLabel jLabelID;
	
	/**
	 * Instantiates a new j panel data workbook as file.
	 */
	public JPanelDataWorkbookInFile() {
		this(null, null);
	}
	
	/**
	 * Instantiates a  JPanelDataWorkbookInFile.
	 *
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 */
	public JPanelDataWorkbookInFile(DataController dataController, DataWorkbook dataWorkbook) {
		this.initialize();
		this.setDataWorkbook(dataWorkbook);
		this.setDataController(dataController);
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelCaptionID = new GridBagConstraints();
		gbc_jLabelCaptionID.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelCaptionID.anchor = GridBagConstraints.WEST;
		gbc_jLabelCaptionID.gridx = 0;
		gbc_jLabelCaptionID.gridy = 0;
		add(getJLabelCaptionID(), gbc_jLabelCaptionID);
		GridBagConstraints gbc_jLabelID = new GridBagConstraints();
		gbc_jLabelID.insets = new Insets(5, 5, 0, 5);
		gbc_jLabelID.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelID.gridx = 1;
		gbc_jLabelID.gridy = 0;
		add(getJLabelID(), gbc_jLabelID);
		
		GridBagConstraints gbc_jLableCaptionName = new GridBagConstraints();
		gbc_jLableCaptionName.anchor = GridBagConstraints.WEST;
		gbc_jLableCaptionName.insets = new Insets(5, 5, 0, 0);
		gbc_jLableCaptionName.gridx = 0;
		gbc_jLableCaptionName.gridy = 1;
		this.add(this.getJLableCaptionName(), gbc_jLableCaptionName);
		GridBagConstraints gbc_jTextFieldName = new GridBagConstraints();
		gbc_jTextFieldName.insets = new Insets(5, 5, 0, 5);
		gbc_jTextFieldName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldName.gridx = 1;
		gbc_jTextFieldName.gridy = 1;
		this.add(this.getJTextFieldName(), gbc_jTextFieldName);
		
		GridBagConstraints gbc_jLabelDescription = new GridBagConstraints();
		gbc_jLabelDescription.anchor = GridBagConstraints.NORTH;
		gbc_jLabelDescription.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelDescription.gridx = 0;
		gbc_jLabelDescription.gridy = 2;
		this.add(this.getJLabelDescription(), gbc_jLabelDescription);
		GridBagConstraints gbc_jScrollPaneDescription = new GridBagConstraints();
		gbc_jScrollPaneDescription.insets = new Insets(5, 5, 0, 5);
		gbc_jScrollPaneDescription.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneDescription.gridx = 1;
		gbc_jScrollPaneDescription.gridy = 2;
		this.add(this.getJScrollPaneDescription(), gbc_jScrollPaneDescription);
		
		GridBagConstraints gbc_jLabelCaptionFile = new GridBagConstraints();
		gbc_jLabelCaptionFile.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelCaptionFile.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelCaptionFile.gridx = 0;
		gbc_jLabelCaptionFile.gridy = 3;
		this.add(this.getJLabelCaptionFile(), gbc_jLabelCaptionFile);
		GridBagConstraints gbc_jLabelFilePath = new GridBagConstraints();
		gbc_jLabelFilePath.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelFilePath.anchor = GridBagConstraints.NORTH;
		gbc_jLabelFilePath.insets = new Insets(5, 5, 0, 5);
		gbc_jLabelFilePath.gridx = 1;
		gbc_jLabelFilePath.gridy = 3;
		this.add(this.getJLabelFilePath(), gbc_jLabelFilePath);
		
	}
	
	/**
	 * Returns the controller.
	 * @return the controller
	 */
	private DataController getDataController() {
		return this.dataController;
	}
	/**
	 * Sets the data controller.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
	}
	/**
	 * Informs the data controller about settings changes.
	 */
	protected void informDataWorkbookSettingChanged() {
		if (this.getDataController()!=null) {
			this.getDataController().firePropertyChange(DataController.DC_DATA_WORKBOOK_CONFIGURATION_CHANGED, null, null);	
		}
	}
	
	/**
	 * Returns the data workbook.
	 * @return the data workbook
	 */
	public DataWorkbook getDataWorkbook() {
		return dataWorkbook;
	}
	/**
	 * Sets the data workbook.
	 * @param dataWorkbook the new data workbook
	 */
	public void setDataWorkbook(DataWorkbook dataWorkbook) {
		this.dataWorkbook = dataWorkbook;

		String filePath = "Not defined yet!";
		if (this.dataWorkbook==null) {
			this.getJLabelID().setText("");
			this.getJTextFieldName().setText("");
			this.getJTextAreaDescription().setText("");
		} else {
			this.getJLabelID().setText(this.dataWorkbook.getID() + "");
			this.getJTextFieldName().setText(this.dataWorkbook.getName());
			this.getJTextAreaDescription().setText(this.dataWorkbook.getDescription());
			
			if (this.getDataWorkbook() instanceof DataWorkbook4XML) {
				filePath = ((DataWorkbook4XML)this.getDataWorkbook()).getDataWorkbookFile().getAbsolutePath();
			} else if (this.getDataWorkbook() instanceof DataWorkbook4JSON) {
				filePath = ((DataWorkbook4JSON)this.getDataWorkbook()).getDataWorkbookFile().getAbsolutePath();
			}
		}
		this.getJLabelFilePath().setText("<html>" + filePath + "</html>");
	}
	
	
	private JLabel getJLabelCaptionID() {
		if (jLabelCaptionID == null) {
			jLabelCaptionID = new JLabel("ID:");
			jLabelCaptionID.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelCaptionID.setForeground(Color.GRAY);
		}
		return jLabelCaptionID;
	}
	private JLabel getJLabelID() {
		if (jLabelID == null) {
			jLabelID = new JLabel();
			jLabelID.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelID.setForeground(Color.GRAY);
			jLabelID.setPreferredSize(new Dimension(80, 26));
		}
		return jLabelID;
	}
	
	private JLabel getJLableCaptionName() {
		if (jLableCaptionName == null) {
			jLableCaptionName = new JLabel("Name:");
			jLableCaptionName.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableCaptionName;
	}
	private JTextField getJTextFieldName() {
		if (jTextFieldName == null) {
			jTextFieldName = new JTextField();
			jTextFieldName.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldName.setPreferredSize(new Dimension(80, 26));
			jTextFieldName.getDocument().addDocumentListener(this);
		}
		return jTextFieldName;
	}
	
	private JLabel getJLabelDescription() {
		if (jLabelDescription == null) {
			jLabelDescription = new JLabel("Description:");
			jLabelDescription.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelDescription;
	}
	private JScrollPane getJScrollPaneDescription() {
		if (jScrollPaneDescription == null) {
			jScrollPaneDescription = new JScrollPane((Component) null);
			jScrollPaneDescription.setPreferredSize(new Dimension(360, 52));
			jScrollPaneDescription.setMinimumSize(new Dimension(360, 52));
			jScrollPaneDescription.setViewportView(getJTextAreaDescription());
		}
		return jScrollPaneDescription;
	}
	private JTextArea getJTextAreaDescription() {
		if (jTextAreaDescription == null) {
			jTextAreaDescription = new JTextArea();
			jTextAreaDescription.setText((String) null);
			jTextAreaDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextAreaDescription.getDocument().addDocumentListener(this);
		}
		return jTextAreaDescription;
	}
	
	private JLabel getJLabelCaptionFile() {
		if (jLabelCaptionFile == null) {
			jLabelCaptionFile = new JLabel("File:");
			jLabelCaptionFile.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelCaptionFile.setVerticalAlignment(JLabel.TOP);
		}
		return jLabelCaptionFile;
	}
	private JLabel getJLabelFilePath() {
		if (jLabelFilePath == null) {
			jLabelFilePath = new JLabel();
			jLabelFilePath.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelFilePath.setVerticalAlignment(JLabel.TOP);
			jLabelFilePath.setPreferredSize(new Dimension(60, 50));
		}
		return jLabelFilePath;
	}
	
	
	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent de) {
		this.onChange(de);
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent de) {
		this.onChange(de);
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent de) {
		this.onChange(de);
	}
	/**
	 * Reacts on document events.
	 * @param de the DocumentEvent
	 */
	private void onChange(DocumentEvent de) {
		
		if (de.getDocument()==this.getJTextFieldName().getDocument()) {
			this.getDataWorkbook().setName(this.getJTextFieldName().getText());
			this.informDataWorkbookSettingChanged();
		} else if (de.getDocument()==this.getJTextAreaDescription().getDocument()) {
			this.getDataWorkbook().setDescription(this.getJTextAreaDescription().getText());
			this.informDataWorkbookSettingChanged();
		}
	}
	
}
