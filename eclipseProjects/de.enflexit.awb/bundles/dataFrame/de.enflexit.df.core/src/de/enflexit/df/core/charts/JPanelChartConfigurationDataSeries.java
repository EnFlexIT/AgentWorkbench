package de.enflexit.df.core.charts;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.awb.core.ui.AwbMessageDialog;
import de.enflexit.charts.model.AbstractChart;
import de.enflexit.charts.model.DataSeries;
import de.enflexit.charts.model.DataSeriesWithReference;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.selection.JTextFieldNodeSelection;
import de.enflexit.df.core.ui.selection.JPanelColumnSelection;

/**
 * The Class JPanelChartConfigurationDataSeries.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelChartConfigurationDataSeries extends JPanel implements ActionListener, DocumentListener {

	private static final long serialVersionUID = -9203558305257293840L;

	private DataController dataController;
	private DefaultMutableTreeNode dataTreeNode;
	
	private String referenceTable;
	private List<String> referenceColumns;
	
	private JPanelChartConfiguration jPanelChartConfiguration;
	private AbstractChart chart;
	private DataSeries dataSeries;
	private boolean isPauseDocumentListener;
	
	private JLabel jLabelHeaderDataSerieses;
	private JScrollPane jScrollPaneDataSeries;
	private DefaultListModel<DataSeries> dataSeriesListModel;
	private JList<DataSeries> jListDataSerieses;

	private JLabel jLabelDataSeriesLabel;
	private JTextField jTextFieldLabel;
	private JCheckBox jCheckBoxSecondaryYAxis;
	
	private JButton jButtonMoveUp;
	private JButton jButtonMoveDown;
	private JButton jButtonDelete;
	private JButton jButtonAddSeries;
	private JLabel jLabelTable;
	private JTextFieldNodeSelection jTextFieldNodeSelection;
	private JPanelColumnSelection jPanelColumnSelection;
	
	/**
	 * Instantiates a new j panel chart configuration base.
	 * @param jPanelChartConfiguration the parent JPanelChartConfiguration
	 */
	public JPanelChartConfigurationDataSeries(JPanelChartConfiguration jPanelChartConfiguration) {
		this.jPanelChartConfiguration = jPanelChartConfiguration;
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelHeaderDataSerieses = new GridBagConstraints();
		gbc_jLabelHeaderDataSerieses.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelHeaderDataSerieses.gridwidth = 2;
		gbc_jLabelHeaderDataSerieses.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeaderDataSerieses.gridx = 0;
		gbc_jLabelHeaderDataSerieses.gridy = 0;
		add(getJLabelHeaderDataSerieses(), gbc_jLabelHeaderDataSerieses);
		GridBagConstraints gbc_jScrollPaneDataSeries = new GridBagConstraints();
		gbc_jScrollPaneDataSeries.insets = new Insets(5, 5, 0, 0);
		gbc_jScrollPaneDataSeries.gridheight = 4;
		gbc_jScrollPaneDataSeries.gridwidth = 2;
		gbc_jScrollPaneDataSeries.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneDataSeries.gridx = 0;
		gbc_jScrollPaneDataSeries.gridy = 1;
		add(getJScrollPaneDataSeries(), gbc_jScrollPaneDataSeries);
		GridBagConstraints gbc_jButtonAddSeries = new GridBagConstraints();
		gbc_jButtonAddSeries.insets = new Insets(5, 5, 0, 5);
		gbc_jButtonAddSeries.gridx = 2;
		gbc_jButtonAddSeries.gridy = 1;
		add(getJButtonAddSeries(), gbc_jButtonAddSeries);
		
		GridBagConstraints gbc_jButtonMoveUp = new GridBagConstraints();
		gbc_jButtonMoveUp.insets = new Insets(15, 5, 0, 5);
		gbc_jButtonMoveUp.gridx = 2;
		gbc_jButtonMoveUp.gridy = 2;
		this.add(this.getJButtonMoveUp(), gbc_jButtonMoveUp);
		GridBagConstraints gbc_jButtonMoveDown = new GridBagConstraints();
		gbc_jButtonMoveDown.insets = new Insets(5, 5, 0, 5);
		gbc_jButtonMoveDown.gridx = 2;
		gbc_jButtonMoveDown.gridy = 3;
		this.add(this.getJButtonMoveDown(), gbc_jButtonMoveDown);
		
		GridBagConstraints gbc_jButtonDelete = new GridBagConstraints();
		gbc_jButtonDelete.insets = new Insets(15, 5, 0, 5);
		gbc_jButtonDelete.anchor = GridBagConstraints.NORTH;
		gbc_jButtonDelete.gridx = 2;
		gbc_jButtonDelete.gridy = 4;
		this.add(this.getJButtonDelete(), gbc_jButtonDelete);
		GridBagConstraints gbc_jLabelDataSeriesLabel = new GridBagConstraints();
		gbc_jLabelDataSeriesLabel.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelDataSeriesLabel.anchor = GridBagConstraints.WEST;
		gbc_jLabelDataSeriesLabel.gridx = 0;
		gbc_jLabelDataSeriesLabel.gridy = 5;
		add(getJLabelDataSeriesLabel(), gbc_jLabelDataSeriesLabel);
		GridBagConstraints gbc_jTextFieldLabel = new GridBagConstraints();
		gbc_jTextFieldLabel.insets = new Insets(5, 5, 0, 0);
		gbc_jTextFieldLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldLabel.gridx = 1;
		gbc_jTextFieldLabel.gridy = 5;
		add(getJTextFieldLabel(), gbc_jTextFieldLabel);
		
		GridBagConstraints gbc_jCheckBoxSecondaryYAxis = new GridBagConstraints();
		gbc_jCheckBoxSecondaryYAxis.insets = new Insets(5, 5, 0, 0);
		gbc_jCheckBoxSecondaryYAxis.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxSecondaryYAxis.gridx = 1;
		gbc_jCheckBoxSecondaryYAxis.gridy = 6;
		add(getJCheckBoxSecondaryYAxis(), gbc_jCheckBoxSecondaryYAxis);
		GridBagConstraints gbc_jLabelTable = new GridBagConstraints();
		gbc_jLabelTable.anchor = GridBagConstraints.WEST;
		gbc_jLabelTable.insets = new Insets(10, 5, 0, 0);
		gbc_jLabelTable.gridx = 0;
		gbc_jLabelTable.gridy = 7;
		add(getJLabelTable(), gbc_jLabelTable);
		GridBagConstraints gbc_textFieldNodeSelection = new GridBagConstraints();
		gbc_textFieldNodeSelection.insets = new Insets(10, 5, 0, 0);
		gbc_textFieldNodeSelection.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldNodeSelection.gridx = 1;
		gbc_textFieldNodeSelection.gridy = 7;
		add(getJTextFieldNodeSelection(), gbc_textFieldNodeSelection);
		GridBagConstraints gbc_jPanelColumnSelection = new GridBagConstraints();
		gbc_jPanelColumnSelection.insets = new Insets(5, 5, 5, 5);
		gbc_jPanelColumnSelection.gridwidth = 3;
		gbc_jPanelColumnSelection.fill = GridBagConstraints.BOTH;
		gbc_jPanelColumnSelection.gridx = 0;
		gbc_jPanelColumnSelection.gridy = 8;
		add(getJPanelColumnSelection(), gbc_jPanelColumnSelection);
	}
	
	private JLabel getJLabelHeaderDataSerieses() {
		if (jLabelHeaderDataSerieses == null) {
			jLabelHeaderDataSerieses = new JLabel("Data Series's");
			jLabelHeaderDataSerieses.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeaderDataSerieses;
	}
	private JScrollPane getJScrollPaneDataSeries() {
		if (jScrollPaneDataSeries == null) {
			jScrollPaneDataSeries = new JScrollPane();
			jScrollPaneDataSeries.setViewportView(getJListDataSerieses());
		}
		return jScrollPaneDataSeries;
	}
	
	private DefaultListModel<DataSeries> getDataSeriesListModel() {
		if (dataSeriesListModel==null) {
			dataSeriesListModel = new DefaultListModel<>();
		}
		return dataSeriesListModel;
	}
	/**
	 * Fill data series data model.
	 * @param dsList the list of DataSeries
	 */
	private void fillDataSeriesDataModel(List<DataSeries> dsList) {
		this.getDataSeriesListModel().clear();
		if (dsList==null) return;
		for (DataSeries ds : dsList) {
			this.getDataSeriesListModel().addElement(ds);
		}
	}
	
	private JList<DataSeries> getJListDataSerieses() {
		if (jListDataSerieses == null) {
			jListDataSerieses = new JList<>(this.getDataSeriesListModel());
			jListDataSerieses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListDataSerieses.setFont(new Font("Dialog", Font.PLAIN, 12));
			jListDataSerieses.setCellRenderer(new DefaultListCellRenderer() {
				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					DataSeries dataSeries = (DataSeries) value;
					label.setText(dataSeries.getLabel() + (dataSeries.getSecondaryYAxis()==false ? "" : "(2nd)"));
					return label;
				}
			});
			jListDataSerieses.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent lse) {
					if (lse.getValueIsAdjusting()==true) return;
					JPanelChartConfigurationDataSeries.this.setDataSeries(JPanelChartConfigurationDataSeries.this.getJListDataSerieses().getSelectedValue());
				}
			});
		}
		return jListDataSerieses;
	}
	
	private JLabel getJLabelDataSeriesLabel() {
		if (jLabelDataSeriesLabel == null) {
			jLabelDataSeriesLabel = new JLabel("Label:");
			jLabelDataSeriesLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelDataSeriesLabel;
	}
	private JTextField getJTextFieldLabel() {
		if (jTextFieldLabel == null) {
			jTextFieldLabel = new JTextField();
			jTextFieldLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldLabel.getDocument().addDocumentListener(this);
		}
		return jTextFieldLabel;
	}
	private JCheckBox getJCheckBoxSecondaryYAxis() {
		if (jCheckBoxSecondaryYAxis == null) {
			jCheckBoxSecondaryYAxis = new JCheckBox("use secondary y-axis");
			jCheckBoxSecondaryYAxis.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jCheckBoxSecondaryYAxis;
	}
	
	private JButton getJButtonAddSeries() {
		if (jButtonAddSeries == null) {
			jButtonAddSeries = new JButton();
			jButtonAddSeries.setIcon(BundleHelper.getImageIcon("ListPlus.png"));
			jButtonAddSeries.setPreferredSize(new Dimension(26, 26));
			jButtonAddSeries.setToolTipText("Add Data Series");
			jButtonAddSeries.addActionListener(this);
		}
		return jButtonAddSeries;
	}
	private JButton getJButtonMoveUp() {
		if (jButtonMoveUp == null) {
			jButtonMoveUp = new JButton();
			jButtonMoveUp.setIcon(BundleHelper.getImageIcon("ArrowUp.png"));
			jButtonMoveUp.setPreferredSize(new Dimension(26, 26));
			jButtonMoveUp.setToolTipText("Move Up");
			jButtonMoveUp.addActionListener(this);
		}
		return jButtonMoveUp;
	}
	private JButton getJButtonMoveDown() {
		if (jButtonMoveDown == null) {
			jButtonMoveDown = new JButton();
			jButtonMoveDown.setIcon(BundleHelper.getImageIcon("ArrowDown.png"));
			jButtonMoveDown.setPreferredSize(new Dimension(26, 26));
			jButtonMoveDown.setToolTipText("Move Down");
			jButtonMoveDown.addActionListener(this);
		}
		return jButtonMoveDown;
	}
	private JButton getJButtonDelete() {
		if (jButtonDelete == null) {
			jButtonDelete = new JButton();
			jButtonDelete.setIcon(BundleHelper.getImageIcon("Delete.png"));
			jButtonDelete.setPreferredSize(new Dimension(26, 26));
			jButtonDelete.setToolTipText("Delete Chart");
			jButtonDelete.addActionListener(this);
		}
		return jButtonDelete;
	}
	
	private JLabel getJLabelTable() {
		if (jLabelTable == null) {
			jLabelTable = new JLabel("Table:");
			jLabelTable.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelTable;
	}
	private JTextFieldNodeSelection getJTextFieldNodeSelection() {
		if (jTextFieldNodeSelection == null) {
			jTextFieldNodeSelection = new JTextFieldNodeSelection(this.jPanelChartConfiguration.getDataController(), false);
			jTextFieldNodeSelection.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldNodeSelection.setText("");
			jTextFieldNodeSelection.addActionListener(this);
		}
		return jTextFieldNodeSelection;
	}
	
	private JPanelColumnSelection getJPanelColumnSelection() {
		if (jPanelColumnSelection == null) {
			jPanelColumnSelection = new JPanelColumnSelection();
		}
		return jPanelColumnSelection;
	}
	
	
	/**
	 * Sets the current tree node selection.
	 *
	 * @param dataController the data controller
	 * @param dataTreeNode the data tree node
	 */
	public void setTreeNodeSelection(DataController dataController, DefaultMutableTreeNode dataTreeNode) {
		this.dataController = dataController;
		this.dataTreeNode = dataTreeNode;
		this.getJTextFieldNodeSelection().setDataController(this.dataController);
	}
	
	/**
	 * Sets the chart to the UI.
	 * @param chart the new chart
	 */
	public void setChart(AbstractChart chart) {
		
		this.chart = chart;
		if (this.chart==null) {
			this.fillDataSeriesDataModel(null);
		} else {
			this.fillDataSeriesDataModel(this.chart.getDataSeries());
		}
	}
	/**
	 * Sets the current DataSeries.
	 * @param dataSeries the new data series
	 */
	private void setDataSeries(DataSeries dataSeries) {
		
		this.dataSeries = dataSeries;
		
		this.isPauseDocumentListener = true;
		
		if (this.dataSeries==null) {
			this.getJTextFieldLabel().setText("");
			this.getJCheckBoxSecondaryYAxis().setSelected(false);
			
		} else {
			this.getJTextFieldLabel().setText(this.dataSeries.getLabel());
			this.getJCheckBoxSecondaryYAxis().setSelected(this.dataSeries.getSecondaryYAxis());
			if (this.dataSeries instanceof DataSeriesWithReference dsRef) {
				String reference = dsRef.getReference();
				// TODO: Check if reference is enough to 
				DefaultMutableTreeNode treeNodeToSelect = this.dataController.getDataTreeModel().getTreeNodeOfExpression(reference);
				this.getJTextFieldNodeSelection().setSelectedTreeNode(treeNodeToSelect);
			}
		}
		this.isPauseDocumentListener = false;
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (this.chart==null) return;
		
		if (ae.getSource()==this.getJButtonAddSeries()) {
			// --- Add new DataSeries -------------------------------
			DataSeries dsNew = new DataSeriesWithReference();
			dsNew.setLabel("New Series " + (this.chart.getDataSeries().size() + 1));
			this.chart.addDataSeriesItem(dsNew);
			this.getDataSeriesListModel().addElement(dsNew);
			this.getJListDataSerieses().setSelectedValue(dsNew, true);
			this.setDataSeries(dsNew);
			
		} else if (ae.getSource()==this.getJButtonMoveUp()) {
			
			
		} else if (ae.getSource()==this.getJButtonMoveDown()) {
			
			
		} else if (ae.getSource()==this.getJButtonDelete()) {
			// --- Delete selected dDataSeries ----------------------
			DataSeries dsDelete = this.getJListDataSerieses().getSelectedValue();
			if (dsDelete==null) return;
			
			Window owner = OwnerDetection.getOwnerWindowForComponent(this); 
			String question = "Are you sure that you want to delete data series '" + dsDelete.getLabel() + "'?";
			int answer = AwbMessageDialog.showConfirmDialog(owner, question, "Delete Chart?", AwbMessageDialog.YES_NO_OPTION, AwbMessageDialog.QUESTION_MESSAGE);
			if (answer==AwbMessageDialog.YES_OPTION) {
				this.getDataSeriesListModel().removeElement(dsDelete);
				this.chart.getDataSeries().remove(dsDelete);
				this.setDataSeries(null);
			}
			
		} else if (ae.getSource()==this.getJTextFieldNodeSelection()) {
			// --- Table was chosen - update column list ------------
			DefaultMutableTreeNode treeNodeSelected = this.getJTextFieldNodeSelection().getSelectedTreeNode();
			if (treeNodeSelected.getUserObject() instanceof AbstractDataSourceDTNO<?> dsDTNO) {
				
				this.getJPanelColumnSelection().setColumnDescriptionList(dsDTNO.getColumnDescriptionList());
				// TODO
			}
			
			
			this.referenceTable = this.dataController.getDataTreeModel().getExpressionPathByUUID(treeNodeSelected); 
			
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
		this.onDocumentEvent(de);
	}
	/**
	 * On document event.
	 * @param de the DocumentEvent
	 */
	private void onDocumentEvent(DocumentEvent de) {
		
		if (this.isPauseDocumentListener==true) return;
		
//		if (de.getDocument()==this.getJTextFieldChartTitle().getDocument()) {
//			this.chart.setTitle(this.getJTextFieldChartTitle().getText());
//			this.informBaseConfigurationChanged();
//			this.informBaseConfigurationChanged();
//		}
	}
	
	/**
	 * Inform base configuration changed.
	 */
	private void informBaseConfigurationChanged() {
		this.jPanelChartConfiguration.informAboutChartEdit(DataController.DC_CHART_EDITED_TITLE_OR_LABEL, null, this.chart);
	}
	
}
