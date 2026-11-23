package de.enflexit.df.core.ui.selection;

import javax.swing.JPanel;
import java.awt.GridBagLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JScrollPane;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.enflexit.common.swing.CheckBoxList;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.extension.ColumnDescription;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JButton;

/**
 * The Class JPanelColumnSelection.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelColumnSelection extends JPanel {

	private static final long serialVersionUID = 903249138584365791L;
	
	private JLabel jLableColumnSelection;
	
	private JButton jButtonSelectAll;
	private JButton jButtonClearSelection;

	private JScrollPane jScrollPaneColumnSelection;
	private DefaultListModel<ColumnDescription> listModel; 
	private CheckBoxList<ColumnDescription> jCheckBoxList;
	private JLabel jLabelSearch;
	private JTextField JTextFieldSearch;
	private JButton jButtonSearchReset;
	
	
	/**
	 * Instantiates a new j panel column selection.
	 */
	public JPanelColumnSelection() {
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLableColumnSelection = new GridBagConstraints();
		gbc_jLableColumnSelection.anchor = GridBagConstraints.SOUTHWEST;
		gbc_jLableColumnSelection.gridx = 0;
		gbc_jLableColumnSelection.gridy = 0;
		add(getJLableColumnSelection(), gbc_jLableColumnSelection);
		GridBagConstraints gbc_jButtonSelectAll = new GridBagConstraints();
		gbc_jButtonSelectAll.anchor = GridBagConstraints.EAST;
		gbc_jButtonSelectAll.insets = new Insets(5, 0, 0, 5);
		gbc_jButtonSelectAll.gridx = 1;
		gbc_jButtonSelectAll.gridy = 0;
		add(getJButtonSelectAll(), gbc_jButtonSelectAll);
		GridBagConstraints gbc_jButtonClearSelection = new GridBagConstraints();
		gbc_jButtonClearSelection.insets = new Insets(5, 0, 0, 0);
		gbc_jButtonClearSelection.gridx = 2;
		gbc_jButtonClearSelection.gridy = 0;
		add(getJButtonClearSelection(), gbc_jButtonClearSelection);
		GridBagConstraints gbc_jScrollPaneColumnSelection = new GridBagConstraints();
		gbc_jScrollPaneColumnSelection.gridwidth = 3;
		gbc_jScrollPaneColumnSelection.insets = new Insets(5, 0, 0, 0);
		gbc_jScrollPaneColumnSelection.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneColumnSelection.gridx = 0;
		gbc_jScrollPaneColumnSelection.gridy = 1;
		add(getJScrollPaneColumnSelection(), gbc_jScrollPaneColumnSelection);
		GridBagConstraints gbc_jLabelSearch = new GridBagConstraints();
		gbc_jLabelSearch.anchor = GridBagConstraints.WEST;
		gbc_jLabelSearch.insets = new Insets(5, 0, 0, 0);
		gbc_jLabelSearch.gridx = 0;
		gbc_jLabelSearch.gridy = 2;
		add(getJLabelSearch(), gbc_jLabelSearch);
		GridBagConstraints gbc_JTextFieldSearch = new GridBagConstraints();
		gbc_JTextFieldSearch.fill = GridBagConstraints.HORIZONTAL;
		gbc_JTextFieldSearch.insets = new Insets(5, 5, 0, 0);
		gbc_JTextFieldSearch.gridx = 1;
		gbc_JTextFieldSearch.gridy = 2;
		add(getJTextFieldSearch(), gbc_JTextFieldSearch);
		GridBagConstraints gbc_jButtonSearchReset = new GridBagConstraints();
		gbc_jButtonSearchReset.insets = new Insets(5, 5, 0, 0);
		gbc_jButtonSearchReset.gridx = 2;
		gbc_jButtonSearchReset.gridy = 2;
		add(getJButtonSearchReset(), gbc_jButtonSearchReset);
	}
	
	private JLabel getJLableColumnSelection() {
		if (jLableColumnSelection == null) {
			jLableColumnSelection = new JLabel("Column");
			jLableColumnSelection.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableColumnSelection;
	}
	
	private JButton getJButtonSelectAll() {
		if (jButtonSelectAll == null) {
			jButtonSelectAll = new JButton();
			jButtonSelectAll.setToolTipText("Select all");
			jButtonSelectAll.setIcon(BundleHelper.getThemedIcon("MBselectAll.png"));
			jButtonSelectAll.setPreferredSize(new Dimension(26, 26));
			jButtonSelectAll.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonSelectAll.addActionListener(null);
		}
		return jButtonSelectAll;
	}
	private JButton getJButtonClearSelection() {
		if (jButtonClearSelection == null) {
			jButtonClearSelection = new JButton();
			jButtonClearSelection.setToolTipText("Clear selection");
			jButtonClearSelection.setIcon(BundleHelper.getThemedIcon("MBselectNone.png"));
			jButtonClearSelection.setPreferredSize(new Dimension(26, 26));
			jButtonClearSelection.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonClearSelection.addActionListener(null);
		}
		return jButtonClearSelection;
	}
	
	private JScrollPane getJScrollPaneColumnSelection() {
		if (jScrollPaneColumnSelection == null) {
			jScrollPaneColumnSelection = new JScrollPane();
			jScrollPaneColumnSelection.setViewportView(this.getJCheckBoxList());
		}
		return jScrollPaneColumnSelection;
	}
	
	/**
	 * Sets the column description list.
	 * @param cdList the new column description list
	 */
	public void setColumnDescriptionList(List<ColumnDescription> cdList) {
		this.getListModel().clear();
		if (cdList!=null) {
			cdList.forEach(cd -> this.getListModel().addElement(cd));
		}
	}
	/**
	 * Returns the list model.
	 * @return the list model
	 */
	private DefaultListModel<ColumnDescription> getListModel() {
		if (listModel==null) {
			listModel = new DefaultListModel<>();
		}
		return listModel; 
	}
	/**
	 * Returns the filtered list model.
	 *
	 * @param searchPhrase the search phrase
	 * @return the list model filtered
	 */
	private DefaultListModel<ColumnDescription> getListModelFiltered(String searchPhrase) {
		
		 DefaultListModel<ColumnDescription> listModelFiltered = new DefaultListModel<>();
		 for (int i = 0; i < this.getListModel().size(); i++) {
			 ColumnDescription cd = this.getListModel().get(i);
			 if (cd.getDefaultDescription().toLowerCase().contains(searchPhrase)) {
				 listModelFiltered.addElement(cd);
			 }
		 }
		 return listModelFiltered;
	}
	/**
	 * Filter.
	 */
	private void filter() {
		String searchPrahse = this.getJTextFieldSearch().getText().trim().toLowerCase();
		if (searchPrahse==null || searchPrahse.isBlank()==true) {
			this.getJCheckBoxList().setModel(this.getListModel());
		} else {
			this.getJCheckBoxList().setModel(this.getListModelFiltered(searchPrahse));
		}
	}
	
	/**
	 *
	 * @return the CheckBoxList for the Column description 
	 */
	private CheckBoxList<ColumnDescription> getJCheckBoxList() {
		if (jCheckBoxList == null) {
			jCheckBoxList = new CheckBoxList<>(this.getListModel(), new Function<ColumnDescription, String>() {
				@Override
				public String apply(ColumnDescription cd) {
					return cd.getDescriptionForColumnSelection();
				}
			});
			jCheckBoxList.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jCheckBoxList;
	}
	
	// ------------------------------------------------------------------------
	// --- Selection area -----------------------------------------------------
	// ------------------------------------------------------------------------
	/**
	 * Sets the selected columns.
	 * @param selColumnNames the new selected columns
	 */
	public void setSelectedColumns(List<String> selColumnNames) {
		// TODO
	}
	
	/**
	 * Returns the selected columns.
	 * @return the selected columns
	 */
	public List<String> getSelectedColumns() {
		
		List<String> selColumns = new ArrayList<>();
		// TODO		
		return selColumns;
	}
	
	/**
	 * Sets the selected ColumnDescription.
	 * @param selColumnDescriptions the new selected column descriptions
	 */
	public void setSelectedColumnDescriptions(List<ColumnDescription> selColumnDescriptions) {

		// TODO
	}
	/**
	 * Returns the selected ColumnDescription.
	 * @return the selected ColumnDescription
	 */
	public List<ColumnDescription> getSelectedColumnDescriptions() {
		
		List<ColumnDescription> selColumns = new ArrayList<>();
		// TODO		
		return selColumns;
	}
	
	// ------------------------------------------------------------------------
	// --- Search area --------------------------------------------------------
	// ------------------------------------------------------------------------
	private JLabel getJLabelSearch() {
		if (jLabelSearch == null) {
			jLabelSearch = new JLabel("Search:");
			jLabelSearch.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSearch;
	}
	private JTextField getJTextFieldSearch() {
		if (JTextFieldSearch == null) {
			JTextFieldSearch = new JTextField();
			JTextFieldSearch.setFont(new Font("Dialog", Font.PLAIN, 12));
			JTextFieldSearch.setPreferredSize(new Dimension(100, 26));
			JTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent de) {
					JPanelColumnSelection.this.filter();
				}
				@Override
				public void insertUpdate(DocumentEvent e) {
					JPanelColumnSelection.this.filter();
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
					JPanelColumnSelection.this.filter();
				}
			});
		}
		return JTextFieldSearch;
	}
	private JButton getJButtonSearchReset() {
		if (jButtonSearchReset == null) {
			jButtonSearchReset = new JButton();
			jButtonSearchReset.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonSearchReset.setIcon(BundleHelper.getThemedIcon("revert_black.png", "revert_grey.png"));
			jButtonSearchReset.setPreferredSize(new Dimension(26, 26));
			jButtonSearchReset.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					JPanelColumnSelection.this.getJTextFieldSearch().setText("");
				}
			});
		}
		return jButtonSearchReset;
	}
	
}
