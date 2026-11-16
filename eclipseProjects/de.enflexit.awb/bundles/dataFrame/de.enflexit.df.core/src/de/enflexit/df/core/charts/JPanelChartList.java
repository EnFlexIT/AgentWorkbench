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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.awb.core.ui.AwbMessageDialog;
import de.enflexit.charts.model.AbstractChart;
import de.enflexit.charts.model.BarChart;
import de.enflexit.charts.model.LineChart;
import de.enflexit.charts.model.PieChart;
import de.enflexit.charts.model.ScatterPlot;
import de.enflexit.charts.model.TimeSeriesChart;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.model.AffectedDataObjects;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.treeNode.DTNO_DataWorkbook;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class JPanelChartList.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelChartList extends JPanel implements PropertyChangeListener, ActionListener {

	private static final long serialVersionUID = 2127119968826382382L;
	
	private DataController dataController;
	private DefaultMutableTreeNode dataTreeNode;

	private JLabel jLabelHeader;
	private JScrollPane jScrollPaneChartList;
	private JButton jButtonMoveUp;
	private JButton jButtonMoveDown;
	private JButton jButtonDelete;
	
	private DefaultListModel<AbstractChart> listModelCharts;
	private JList<AbstractChart> jListCharts;

	
	/**
	 * Instantiates a new j panel chart list.
	 */
	public JPanelChartList() {
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		this.add(this.getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jScrollPaneChartList = new GridBagConstraints();
		gbc_jScrollPaneChartList.gridheight = 3;
		gbc_jScrollPaneChartList.insets = new Insets(5, 5, 5, 0);
		gbc_jScrollPaneChartList.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneChartList.gridx = 0;
		gbc_jScrollPaneChartList.gridy = 1;
		this.add(this.getJScrollPaneChartList(), gbc_jScrollPaneChartList);
		
		GridBagConstraints gbc_jButtonMoveUp = new GridBagConstraints();
		gbc_jButtonMoveUp.insets = new Insets(5, 5, 0, 5);
		gbc_jButtonMoveUp.gridx = 1;
		gbc_jButtonMoveUp.gridy = 1;
		this.add(this.getJButtonMoveUp(), gbc_jButtonMoveUp);
		GridBagConstraints gbc_jButtonMoveDown = new GridBagConstraints();
		gbc_jButtonMoveDown.insets = new Insets(5, 5, 0, 5);
		gbc_jButtonMoveDown.gridx = 1;
		gbc_jButtonMoveDown.gridy = 2;
		this.add(this.getJButtonMoveDown(), gbc_jButtonMoveDown);
		
		GridBagConstraints gbc_jButtonDelete = new GridBagConstraints();
		gbc_jButtonDelete.insets = new Insets(15, 5, 0, 5);
		gbc_jButtonDelete.anchor = GridBagConstraints.NORTH;
		gbc_jButtonDelete.gridx = 1;
		gbc_jButtonDelete.gridy = 3;
		this.add(this.getJButtonDelete(), gbc_jButtonDelete);
	}

	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Available Charts");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	private JScrollPane getJScrollPaneChartList() {
		if (jScrollPaneChartList == null) {
			jScrollPaneChartList = new JScrollPane();
			jScrollPaneChartList.setViewportView(getJListCharts());
		}
		return jScrollPaneChartList;
	}
	
	private DefaultListModel<AbstractChart> getListModelCharts() {
		if (listModelCharts==null) {
			listModelCharts = new DefaultListModel<>();
		}
		return listModelCharts;
	}
	private void updateListModelCharts() {
		
		AbstractChart chartSelected = this.getJListCharts().getSelectedValue();
		
		// --- Clear list -------------
		this.getListModelCharts().clear();
		
		DataWorkbook dw = this.getDataWorkbook();
		if (dw==null) return;

		for (AbstractChart chart : dw.getChartList()) {
			this.addToListModelCharts(chart);
		}
		
		if (chartSelected!=null) {
			this.getJListCharts().setSelectedValue(chartSelected, true);
		}
	}
	private void addToListModelCharts(AbstractChart chart) {
		this.getListModelCharts().addElement(chart);
	}
	
	private JList<AbstractChart> getJListCharts() {
		if (jListCharts == null) {
			jListCharts = new JList<>(this.getListModelCharts());
			jListCharts.setFont(new Font("Dialog", Font.PLAIN, 12));
			jListCharts.setCellRenderer(new DefaultListCellRenderer() {
				private final Icon barChartIcon = ChartIconFactory.createBarChartIcon();
				private final Icon pieChartIcon = ChartIconFactory.createPieChartIcon();
				private final Icon scatterPlotIcon = ChartIconFactory.createScatterPlotIcon();
				private final Icon xyChartIcon = ChartIconFactory.createXYChartIcon();
				private final Icon timeSeriesChartIcon = ChartIconFactory.createTimeSeriesChartIcon();
				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					AbstractChart chart = (AbstractChart) value;
					label.setText(chart.getTitle());
					if (chart instanceof BarChart) {
						label.setIcon(this.barChartIcon);
					} else if (chart instanceof PieChart) {
						label.setIcon(this.pieChartIcon);
					} else if (chart instanceof ScatterPlot) {
						label.setIcon(this.scatterPlotIcon);
					} else if (chart instanceof TimeSeriesChart) {
						label.setIcon(this.timeSeriesChartIcon);
					} else if (chart instanceof LineChart) {
						label.setIcon(this.xyChartIcon);
					}
					return label;
				}
			});
			jListCharts.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent lse) {
					if (lse.getValueIsAdjusting()==true) return;
					if (JPanelChartList.this.dataController==null) return;
					JPanelChartList.this.dataController.getSelectionModel().setSelectedCharts(JPanelChartList.this.getJListCharts().getSelectedValuesList());
				}
			});
		}
		return jListCharts;
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
	
	/**
	 * Returns the current data workbook.
	 * @return the data workbook
	 */
	private DataWorkbook getDataWorkbook() {
		DefaultMutableTreeNode searchNode = this.dataTreeNode;
		if (searchNode!=null) {
			while (searchNode.getParent()!=null) {
				if (searchNode.getUserObject() instanceof DTNO_DataWorkbook dwDTNO) {
					return dwDTNO.getDataWorkbook();
				}
				searchNode = (DefaultMutableTreeNode) searchNode.getParent();
			}
		}
		return null;
	}
	/**
	 * Sets the current tree node selection.
	 *
	 * @param dataController the data controller
	 * @param dataTreeNode the data tree node
	 */
	public void setTreeNodeSelection(DataController dataController, DefaultMutableTreeNode dataTreeNode) {
		
		if (dataController==null) {
			if (this.dataController!=null) {
				this.dataController.removePropertyChangeListener(this);
			}
			this.dataController = dataController; // = null !
			
		} else {
			if (this.dataController!=dataController) {
				if (this.dataController!=null) {
					this.dataController.removePropertyChangeListener(this);
				}
				this.dataController = dataController;
				this.dataController.addPropertyChangeListener(this);
			}
		}
		
		this.dataTreeNode = dataTreeNode;
		this.updateListModelCharts();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
	
		// --- Which chart is selected ? ------------------
		List<AbstractChart> chartSelected = this.getJListCharts().getSelectedValuesList();
		if (chartSelected==null || chartSelected.size()==0) return;
		
		// --- Do the desired action ----------------------
		if (ae.getSource()==this.getJButtonMoveUp()) {
			this.moveChart(chartSelected.get(0), -1);
		} else if (ae.getSource()==this.getJButtonMoveDown()) {
			this.moveChart(chartSelected.get(0), 1);
			
		} else if (ae.getSource()==this.getJButtonDelete()) {
			String question = null;
			if (chartSelected.size()>1) {
				question = "Are you sure that you want to delete the " + chartSelected.size() + " selected charts?";
			} else {
				question = "Are you sure that you want to delete chart '" + chartSelected.get(0).getTitle() + "'?";
			}
			Window owner = OwnerDetection.getOwnerWindowForComponent(this); 
			int answer = AwbMessageDialog.showConfirmDialog(owner, question, "Delete Chart?", AwbMessageDialog.YES_NO_OPTION, AwbMessageDialog.QUESTION_MESSAGE);
			if (answer==AwbMessageDialog.YES_OPTION) {
				for (AbstractChart chart : chartSelected) {
					this.dataController.removeChart(this.getDataWorkbook(), chart);
					this.getListModelCharts().removeElement(chart);
				}
				this.dataController.getSelectionModel().setSelectedCharts(null);
			}
			
		}
	}
	/**
	 * Moves the chart in the specified direction.
	 *
	 * @param chartToMove the chart to move
	 * @param movement the movement
	 */
	private void moveChart(AbstractChart chartToMove, int movement) {
		
		// --- Get the old index position -----------------
		int idxOld = this.getDataWorkbook().getChartList().indexOf(chartToMove);
		if (idxOld==-1) return;
		
		// --- Check if the movement is allowed -----------
		int idxNew = idxOld + movement;
		if (idxNew<0 || idxNew>=this.getDataWorkbook().getChartList().size()) {
			return;
		}
		
		// --- Replace the chart element ------------------
		this.getDataWorkbook().getChartList().remove(idxOld);
		this.getDataWorkbook().getChartList().add(idxNew, chartToMove);
		
		this.getListModelCharts().remove(idxOld);
		this.getListModelCharts().add(idxNew, chartToMove);
		
		this.getJListCharts().setSelectedValue(chartToMove, true);
	}
	
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		switch (evt.getPropertyName()) {
		case DataController.DC_CHART_ADDED:
			AffectedDataObjects ado = (AffectedDataObjects) evt.getNewValue();
			AbstractChart chart = ado.getChart();
			this.updateListModelCharts();
			this.getJListCharts().setSelectedValue(chart, true);
			break;
		case DataController.DC_CHART_EDITED_TITLE_OR_LABEL:
			this.getJListCharts().repaint();
			break;
		}
		
	}
	
}
