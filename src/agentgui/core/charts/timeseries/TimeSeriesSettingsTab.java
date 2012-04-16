package agentgui.core.charts.timeseries;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import agentgui.core.application.Language;
import agentgui.core.charts.SettingsTab;
import agentgui.envModel.graph.components.TableCellEditor4Color;
import agentgui.envModel.graph.components.TableCellRenderer4Color;
import agentgui.ontology.TimeSeries;

public class TimeSeriesSettingsTab extends SettingsTab implements ActionListener, Observer{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3617880987434804785L;
	private JTextField txtTitle;
	private JTextField txtXaxislabel;
	private JTextField txtYaxislabel;
	private JTable tblSeries;
	private JComboBox cbRenderer;
	
	private JButton btnConfirm;
	
	private TimeSeriesDataModel model = null;
	
	/**
	 * Create the panel.
	 */
	public TimeSeriesSettingsTab(TimeSeriesDataModel model, TimeSeriesChartTab chartTab) {
		this.model = model;
		this.model.addObserver(this);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{71, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblTitle = new JLabel("Title");
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.anchor = GridBagConstraints.EAST;
		gbc_lblTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 1;
		add(lblTitle, gbc_lblTitle);
		
		txtTitle = new JTextField();
		txtTitle.setText(model.getTitle());
		GridBagConstraints gbc_txtTitle = new GridBagConstraints();
		gbc_txtTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTitle.weightx = 10.0;
		gbc_txtTitle.insets = new Insets(0, 0, 5, 0);
		gbc_txtTitle.gridx = 1;
		gbc_txtTitle.gridy = 1;
		add(txtTitle, gbc_txtTitle);
		txtTitle.setColumns(10);
		
		JLabel lblXaxis = new JLabel("X axis label");
		GridBagConstraints gbc_lblXaxis = new GridBagConstraints();
		gbc_lblXaxis.insets = new Insets(0, 0, 5, 5);
		gbc_lblXaxis.anchor = GridBagConstraints.EAST;
		gbc_lblXaxis.gridx = 0;
		gbc_lblXaxis.gridy = 2;
		add(lblXaxis, gbc_lblXaxis);
		
		txtXaxislabel = new JTextField();
		txtXaxislabel.setText(model.getxAxisLabel());
		GridBagConstraints gbc_txtXaxislabel = new GridBagConstraints();
		gbc_txtXaxislabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtXaxislabel.insets = new Insets(0, 0, 5, 0);
		gbc_txtXaxislabel.gridx = 1;
		gbc_txtXaxislabel.gridy = 2;
		add(txtXaxislabel, gbc_txtXaxislabel);
		txtXaxislabel.setColumns(10);
		
		JLabel lblYaxis = new JLabel("Y axis label");
		GridBagConstraints gbc_lblYaxis = new GridBagConstraints();
		gbc_lblYaxis.insets = new Insets(0, 0, 5, 5);
		gbc_lblYaxis.anchor = GridBagConstraints.EAST;
		gbc_lblYaxis.gridx = 0;
		gbc_lblYaxis.gridy = 3;
		add(lblYaxis, gbc_lblYaxis);
		
		txtYaxislabel = new JTextField();
		txtYaxislabel.setText(model.getyAxisLabel());
		GridBagConstraints gbc_txtYaxislabel = new GridBagConstraints();
		gbc_txtYaxislabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtYaxislabel.insets = new Insets(0, 0, 5, 0);
		gbc_txtYaxislabel.gridx = 1;
		gbc_txtYaxislabel.gridy = 3;
		add(txtYaxislabel, gbc_txtYaxislabel);
		txtYaxislabel.setColumns(10);
		
		JLabel lblRenderer = new JLabel("Renderer");
		GridBagConstraints gbc_lblRenderer = new GridBagConstraints();
		gbc_lblRenderer.insets = new Insets(0, 0, 5, 5);
		gbc_lblRenderer.anchor = GridBagConstraints.EAST;
		gbc_lblRenderer.gridx = 0;
		gbc_lblRenderer.gridy = 4;
		add(lblRenderer, gbc_lblRenderer);
		
		cbRenderer = new JComboBox();
		cbRenderer.setModel(new DefaultComboBoxModel(chartTab.getAllowedRendererClasses()));
		cbRenderer.setSelectedItem(chartTab.getRendererClassName());
		GridBagConstraints gbc_cbRenderer = new GridBagConstraints();
		gbc_cbRenderer.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbRenderer.insets = new Insets(0, 0, 5, 0);
		gbc_cbRenderer.gridx = 1;
		gbc_cbRenderer.gridy = 4;
		add(cbRenderer, gbc_cbRenderer);
		
		JScrollPane scpSeriestable = new JScrollPane();
		GridBagConstraints gbc_scpSeriestable = new GridBagConstraints();
		gbc_scpSeriestable.weightx = 1.0;
		gbc_scpSeriestable.insets = new Insets(0, 0, 5, 0);
		gbc_scpSeriestable.gridwidth = 2;
		gbc_scpSeriestable.fill = GridBagConstraints.BOTH;
		gbc_scpSeriestable.gridx = 0;
		gbc_scpSeriestable.gridy = 5;
		add(scpSeriestable, gbc_scpSeriestable);
		
		tblSeries = new JTable();
		rebuildSeriesTable();
		scpSeriestable.setViewportView(tblSeries);
		
		btnConfirm = new JButton("Übernehmen");
		btnConfirm.addActionListener(this);
		GridBagConstraints gbc_btnConfirm = new GridBagConstraints();
		gbc_btnConfirm.anchor = GridBagConstraints.EAST;
		gbc_btnConfirm.gridx = 1;
		gbc_btnConfirm.gridy = 6;
		add(btnConfirm, gbc_btnConfirm);

	}
	
	private void rebuildSeriesTable(){
		
		DefaultTableModel tm = new DefaultTableModel();
		
		tm.addColumn("Label");
		tm.addColumn(Language.translate("Farbe"));
		tm.addColumn(Language.translate("Dicke"));
		
		// Get the series settings from the ontology model and add them to the table model 
		for(int i=0; i<model.getNumberOfSeries(); i++){
			String label = (String) model.getOntologyModel().getValueAxisDescriptions().get(i);
			Color color = new Color(Integer.parseInt((String) model.getOntologyModel().getValueAxisColors().get(i)));
			Float lineWitdh = (Float) model.getOntologyModel().getValueAxisLineWidth().get(i);
			Object[] rowData = {label, color, lineWitdh};
			tm.addRow(rowData);
		}
		
		tblSeries.setModel(tm);
		TableColumn tcColor = tblSeries.getColumnModel().getColumn(1);
		tcColor.setCellRenderer(new TableCellRenderer4Color(true));
		tcColor.setCellEditor(new TableCellEditor4Color());
		tblSeries.getColumnModel().getColumn(2).setCellEditor(new TableCellSpinnerEditor4FloatObject());
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnConfirm)){
			
			TableCellEditor tce = tblSeries.getCellEditor();
			if(tce != null){
				tce.stopCellEditing();
			}
			
			model.setTitle(txtTitle.getText());
			model.setxAxisLabel(txtXaxislabel.getText());
			model.setyAxisLabel(txtYaxislabel.getText());
			model.setRendererType(cbRenderer.getSelectedItem().toString());
			
			TimeSeries ontoModel = model.getOntologyModel();
			ontoModel.clearAllValueAxisDescriptions();
			ontoModel.clearAllValueAxisColors();
			ontoModel.clearAllValueAxisLineWidth();
			
			for(int i=0; i<tblSeries.getRowCount(); i++){
				// Update ontology model
				ontoModel.getValueAxisDescriptions().add(tblSeries.getValueAt(i, 0));
				Color color = (Color) tblSeries.getValueAt(i, 1);
				ontoModel.addValueAxisColors(""+color.getRGB());
				ontoModel.getValueAxisLineWidth().add(tblSeries.getValueAt(i, 2));
			}
			
			model.triggerNotifySettingsChanged();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o == model && (Integer)arg == TimeSeriesDataModel.DATA_ADDED){
			rebuildSeriesTable();
		}
		
	}

}
