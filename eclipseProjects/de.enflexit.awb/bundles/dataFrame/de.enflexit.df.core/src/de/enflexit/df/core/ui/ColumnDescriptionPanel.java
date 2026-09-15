package de.enflexit.df.core.ui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JTextArea;

import de.enflexit.df.core.extension.ColumnDescription;

public class ColumnDescriptionPanel extends JPanel {
	
	private static final long serialVersionUID = -9163040515488574190L;
	
	private static final String NO_COLUMN_SELECTED_TEXT = "No data column selected.\nClick on a column header to the show column details here";
	
	private JLabel jLabelHeader;
	private JTextArea jTextAreaColumnDescription;
	
	public ColumnDescriptionPanel() {
		initialize();
	}
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		add(getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jTextAreaColumnDescription = new GridBagConstraints();
		gbc_jTextAreaColumnDescription.insets = new Insets(5, 5, 5, 5);
		gbc_jTextAreaColumnDescription.fill = GridBagConstraints.BOTH;
		gbc_jTextAreaColumnDescription.gridx = 0;
		gbc_jTextAreaColumnDescription.gridy = 1;
		add(getJTextAreaColumnDescription(), gbc_jTextAreaColumnDescription);
	}

	/**
	 * Sets the column description.
	 * @param columnDescription the new column description
	 */
	public void setColumnDescription(ColumnDescription columnDescription) {
		if (columnDescription!=null) {
			this.getJTextAreaColumnDescription().setText(columnDescription.getDescription());
		} else {
			this.getJTextAreaColumnDescription().setText(NO_COLUMN_SELECTED_TEXT);
		}
	}

	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Column Details");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	private JTextArea getJTextAreaColumnDescription() {
		if (jTextAreaColumnDescription == null) {
			jTextAreaColumnDescription = new JTextArea();
			jTextAreaColumnDescription.setEditable(false);
			jTextAreaColumnDescription.setLineWrap(true);
			jTextAreaColumnDescription.setWrapStyleWord(true);
			jTextAreaColumnDescription.setOpaque(false);
			jTextAreaColumnDescription.setBorder(null);
			jTextAreaColumnDescription.setText(NO_COLUMN_SELECTED_TEXT);
		}
		return jTextAreaColumnDescription;
	}
}
