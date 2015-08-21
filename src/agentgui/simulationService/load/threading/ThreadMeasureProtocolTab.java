package agentgui.simulationService.load.threading;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class ThreadMeasureProtocolTab extends JPanel {

	private static final long serialVersionUID = -7315494195421538651L;

	private ThreadProtocolVector threadProtocolVector;
	
	private JLabel JLabelHeader;

	// --- Hier geht's weiter für den Hanno  ----!!!
	
	public ThreadMeasureProtocolTab(ThreadProtocolVector threadProtocolVector) {
		this.threadProtocolVector = threadProtocolVector;
		initialize();

	}
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{33, 335, 25, 15, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_JLabelHeader = new GridBagConstraints();
		gbc_JLabelHeader.anchor = GridBagConstraints.NORTHWEST;
		gbc_JLabelHeader.insets = new Insets(0, 0, 0, 5);
		gbc_JLabelHeader.gridx = 0;
		gbc_JLabelHeader.gridy = 0;
		add(getJLabelHeader(), gbc_JLabelHeader);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);

		JTable table = new JTable(threadProtocolVector.getTableModel());
		table.setPreferredScrollableViewportSize(new Dimension(600, 500));
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);
		
		//set column min-width
		table.getColumnModel().getColumn(0).setMinWidth(50);
		table.getColumnModel().getColumn(1).setMinWidth(200);
		
		// Sort Threads - top most user time
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 3; //User Time
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.DESCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();
		
		scrollPane.setViewportView(table);
		
	}
	
	private JLabel getJLabelHeader() {
		if (JLabelHeader == null) {
			JLabelHeader = new JLabel("Thread Times");
		}
		return JLabelHeader;
	}
}
