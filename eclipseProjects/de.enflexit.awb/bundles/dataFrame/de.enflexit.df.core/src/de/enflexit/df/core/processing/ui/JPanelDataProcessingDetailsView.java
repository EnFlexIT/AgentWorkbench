package de.enflexit.df.core.processing.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.processing.ProcessingDataSource;
import de.enflexit.df.core.processing.transformationGraph.TransformationGraphController;

import javax.swing.JSplitPane;

/**
 * Details view panel for {@link ProcessingDataSource}s. Will contain the Graph UI for editing data processing flows.   
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JPanelDataProcessingDetailsView extends JPanel {

	private static final long serialVersionUID = -1206393060851933798L;
	
	private JSplitPane jSplitPaneDataTransformationEditor;
	private JPanelSourceTableSelection jPanelSourceTableSelection;
	private JPanelTransformationGraphEditor jPanelGraphEditor;
	
	private DataController dataController;
	private TransformationGraphController graphController;
	
	/**
	 * Instantiates a new j panel data processing details view.
	 */
	public JPanelDataProcessingDetailsView(DataController dataController) {
		this.dataController = dataController;
		initialize();
	}
	
	/**
	 * Initializes the UI components.
	 */
	private void initialize() {
		this.setLayout(new BorderLayout(0, 0));
		this.add(getJSplitPaneDataTransformationEditor(), BorderLayout.CENTER);
	}
	
	/**
	 * Gets the main split pane containing all other UI parts. 
	 * @return the j split pane data transformation editor
	 */
	private JSplitPane getJSplitPaneDataTransformationEditor() {
		if (jSplitPaneDataTransformationEditor == null) {
			jSplitPaneDataTransformationEditor = new JSplitPane();
			jSplitPaneDataTransformationEditor.setLeftComponent(getJPanelSourceTableSelection());
			jSplitPaneDataTransformationEditor.setRightComponent(this.getjPanelGraphEditor());
			jSplitPaneDataTransformationEditor.setDividerLocation(0.25);
		}
		return jSplitPaneDataTransformationEditor;
	}
	
	/**
	 * Gets the left panel for selecting the source tables.
	 * @return the j panel source table selection
	 */
	private JPanelSourceTableSelection getJPanelSourceTableSelection() {
		if (jPanelSourceTableSelection == null) {
			jPanelSourceTableSelection = new JPanelSourceTableSelection(this.dataController, this.getGraphController());
		}
		return jPanelSourceTableSelection;
	}
	
	/**
	 * Gets the right panel for editing the transformation graph.
	 * @return the j panel graph editor
	 */
	private JPanelTransformationGraphEditor getjPanelGraphEditor() {
		if (jPanelGraphEditor==null) {
			jPanelGraphEditor = new JPanelTransformationGraphEditor(this.getGraphController());
		}
		return jPanelGraphEditor;
	}

	/**
	 * Gets the graph controller, initializes it if null.
	 * @return the graph controller
	 */
	private TransformationGraphController getGraphController() {
		if (graphController==null) {
			graphController = new TransformationGraphController();
		}
		return graphController;
	}

}
