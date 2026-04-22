package de.enflexit.df.core.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import de.enflexit.df.core.model.DataController;

/**
 * The Class JPanelDataViewer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelDataViewer extends JPanel {
	
	private static final long serialVersionUID = 112966832470901946L;

	private DataController dataController;
	
	private JToolBarData jToolBarData;
	
	private JSplitPane jSplitPaneData;
	private JPanelNavigation jPanelNavigation;
	private JPanelDataDetailView jPanelDataDetailView;
	
	
	/**
	 * Instantiates a new JPanelDataViewer.
	 */
	public JPanelDataViewer() {
		this(null);
	}
	/**
	 * Instantiates a new JPanelDataViewer.
	 */
	public JPanelDataViewer(DataController dataController) {
		this.setDataController(dataController);
		this.initialize();
	}
	
	/**
	 * Sets the DataController.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
	}
	/**
	 * Returns the data controller.
	 * @return the data controller
	 */
	public DataController getDataController() {
		if (dataController==null) {
			dataController = new DataController();
		}
		return dataController;
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		this.setLayout(new BorderLayout(0, 0));
		this.add(this.getJToolBarData(), BorderLayout.NORTH);
		this.add(this.getJSplitPaneData(), BorderLayout.CENTER);
	}
	private JToolBar getJToolBarData() {
		if (jToolBarData == null) {
			jToolBarData = new JToolBarData(this.getDataController());
		}
		return jToolBarData;
	}
	private JSplitPane getJSplitPaneData() {
		if (jSplitPaneData==null) {
			jSplitPaneData = new JSplitPane();
			jSplitPaneData.setOneTouchExpandable(true);
			jSplitPaneData.setDividerSize(5);
			jSplitPaneData.setResizeWeight(0.25);
			
			jSplitPaneData.setLeftComponent(this.getJPanelNavigation());
			jSplitPaneData.setRightComponent(this.getJPanelDataDetailView());
		}
		return jSplitPaneData;
	}
	private JPanelNavigation getJPanelNavigation() {
		if (jPanelNavigation == null) {
			jPanelNavigation = new JPanelNavigation(this.getDataController());
		}
		return jPanelNavigation;
	}
	private JPanelDataDetailView getJPanelDataDetailView() {
		if (jPanelDataDetailView==null) {
			jPanelDataDetailView = new JPanelDataDetailView(this.getDataController());
		}
		return jPanelDataDetailView;
	}
	
}
