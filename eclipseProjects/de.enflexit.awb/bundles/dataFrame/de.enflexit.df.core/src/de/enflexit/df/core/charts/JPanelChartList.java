package de.enflexit.df.core.charts;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JScrollPane;

import de.enflexit.charts.model.AbstractChart;
import de.enflexit.df.core.BundleHelper;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;

/**
 * The Class JPanelChartList.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelChartList extends JPanel {

	private static final long serialVersionUID = 2127119968826382382L;
	private JLabel jLabelHeader;
	private JScrollPane jScrollPaneChartList;
	private JButton jButtonDelete;
	private JButton jButtonMoveUp;
	private JButton jButtonMoveDown;
	
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
		GridBagConstraints gbc_jButtonDelete = new GridBagConstraints();
		gbc_jButtonDelete.insets = new Insets(5, 5, 0, 5);
		gbc_jButtonDelete.gridx = 1;
		gbc_jButtonDelete.gridy = 1;
		this.add(this.getJButtonDelete(), gbc_jButtonDelete);
		GridBagConstraints gbc_jButtonMoveUp = new GridBagConstraints();
		gbc_jButtonMoveUp.insets = new Insets(15, 5, 0, 5);
		gbc_jButtonMoveUp.gridx = 1;
		gbc_jButtonMoveUp.gridy = 2;
		this.add(this.getJButtonMoveUp(), gbc_jButtonMoveUp);
		GridBagConstraints gbc_jButtonMoveDown = new GridBagConstraints();
		gbc_jButtonMoveDown.insets = new Insets(5, 5, 0, 5);
		gbc_jButtonMoveDown.anchor = GridBagConstraints.NORTH;
		gbc_jButtonMoveDown.gridx = 1;
		gbc_jButtonMoveDown.gridy = 3;
		this.add(this.getJButtonMoveDown(), gbc_jButtonMoveDown);
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
	private JList<AbstractChart> getJListCharts() {
		if (jListCharts == null) {
			jListCharts = new JList<>(this.getListModelCharts());
			jListCharts.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jListCharts;
	}
	
	private JButton getJButtonDelete() {
		if (jButtonDelete == null) {
			jButtonDelete = new JButton();
			jButtonDelete.setIcon(BundleHelper.getImageIcon("Delete.png"));
			jButtonDelete.setPreferredSize(new Dimension(26, 26));
			jButtonDelete.setToolTipText("Delete Chart");
		}
		return jButtonDelete;
	}
	
	private JButton getJButtonMoveUp() {
		if (jButtonMoveUp == null) {
			jButtonMoveUp = new JButton();
			jButtonMoveUp.setIcon(BundleHelper.getImageIcon("ArrowUp.png"));
			jButtonMoveUp.setPreferredSize(new Dimension(26, 26));
			jButtonMoveUp.setToolTipText("Move Up");
		}
		return jButtonMoveUp;
	}
	private JButton getJButtonMoveDown() {
		if (jButtonMoveDown == null) {
			jButtonMoveDown = new JButton();
			jButtonMoveDown.setIcon(BundleHelper.getImageIcon("ArrowDown.png"));
			jButtonMoveDown.setPreferredSize(new Dimension(26, 26));
			jButtonMoveDown.setToolTipText("Move Down");
		}
		return jButtonMoveDown;
	}
	
}
