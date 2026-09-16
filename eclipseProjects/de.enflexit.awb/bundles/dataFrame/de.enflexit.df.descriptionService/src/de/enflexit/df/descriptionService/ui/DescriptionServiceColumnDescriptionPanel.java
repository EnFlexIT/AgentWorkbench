package de.enflexit.df.descriptionService.ui;

import javax.swing.JPanel;

import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.extension.ColumnDescription;
import de.enflexit.df.core.extension.ColumnDescriptionPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextPane;

/**
 * Custom column details panel for the Description Service. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DescriptionServiceColumnDescriptionPanel extends JPanel implements ColumnDescriptionPanel, ActionListener {
	
	private static final long serialVersionUID = 4996550705735411624L;
	
	private static final String NO_COLUMN_SELECTED_TEXT = "<html><p>No data column selected.<br/>Click on a column header to the show column details here</p></html>";
	
	private JLabel jLabelHeader;
	private JButton jButtonDetailsEditor;
	private JTextPane jTextPaneColumnDetails;
	
	private ColumnDescription columnDescription;
	
	/**
	 * Instantiates a new description service column details panel.
	 */
	public DescriptionServiceColumnDescriptionPanel() {
		initialize();
	}
	
	/**
	 * Initializes the UI components.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.insets = new Insets(10, 10, 5, 5);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		add(getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jButtonDetailsEditor = new GridBagConstraints();
		gbc_jButtonDetailsEditor.anchor = GridBagConstraints.EAST;
		gbc_jButtonDetailsEditor.insets = new Insets(10, 10, 5, 10);
		gbc_jButtonDetailsEditor.gridx = 1;
		gbc_jButtonDetailsEditor.gridy = 0;
		add(getJButtonDetailsEditor(), gbc_jButtonDetailsEditor);
		GridBagConstraints gbc_jTextPaneColumnDetails = new GridBagConstraints();
		gbc_jTextPaneColumnDetails.gridwidth = 2;
		gbc_jTextPaneColumnDetails.insets = new Insets(5, 10, 10, 10);
		gbc_jTextPaneColumnDetails.fill = GridBagConstraints.BOTH;
		gbc_jTextPaneColumnDetails.gridx = 0;
		gbc_jTextPaneColumnDetails.gridy = 1;
		add(getJTextPaneColumnDetails(), gbc_jTextPaneColumnDetails);
	}

	/**
	 * Gets the j label header.
	 * @return the j label header
	 */
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Column Details");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	
	/**
	 * Gets the j button details editor.
	 * @return the j button details editor
	 */
	private JButton getJButtonDetailsEditor() {
		if (jButtonDetailsEditor == null) {
			jButtonDetailsEditor = new JButton("Edit");
			jButtonDetailsEditor.setIcon(BundleHelper.getImageIcon("Edit.png"));
			jButtonDetailsEditor.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonDetailsEditor.setEnabled(false);
			jButtonDetailsEditor.addActionListener(this);
		}
		return jButtonDetailsEditor;
	}
	
	/**
	 * Gets the j text pane column details.
	 * @return the j text pane column details
	 */
	private JTextPane getJTextPaneColumnDetails() {
		if (jTextPaneColumnDetails == null) {
			jTextPaneColumnDetails = new JTextPane();
			jTextPaneColumnDetails.setContentType("text/html");
			jTextPaneColumnDetails.setEditable(false);
			jTextPaneColumnDetails.setOpaque(false);
		}
		return jTextPaneColumnDetails;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.ColumnDescriptionPanel#getActualColumnDescriptionPanel()
	 */
	@Override
	public JPanel getActualColumnDescriptionPanel() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.ColumnDescriptionPanel#setColumnDescription(de.enflexit.df.core.extension.ColumnDescription)
	 */
	@Override
	public void setColumnDescription(ColumnDescription columnDescription) {
		this.columnDescription = columnDescription;
		this.getJButtonDetailsEditor().setEnabled(columnDescription!=null);
		if (columnDescription!=null) {
			this.getJTextPaneColumnDetails().setText(columnDescription.getDescription());
		} else {
			this.getJTextPaneColumnDetails().setText(NO_COLUMN_SELECTED_TEXT);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonDetailsEditor()) {
			//TODO open column description editor with the current column selected
			JOptionPane.showMessageDialog(this, "Under construction");
		}
	}
	
}
