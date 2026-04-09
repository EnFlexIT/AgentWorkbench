package de.enflexit.df.core.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import de.enflexit.df.core.model.DataController;

/**
 * The Class JPanelData.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelData extends JPanel implements PropertyChangeListener {
	
	private static final long serialVersionUID = 112966832470901946L;

	private DataController dataController;
	
	private JToolBarData jToolBarData;
	private JSplitPane jSplitPaneData;
	private JPanelTree jPanelTree;
	private JPanelDataTable jPanelDataTable;
	
	/**
	 * Instantiates a new JPanelData.
	 */
	public JPanelData() {
		this(null);
	}
	/**
	 * Instantiates a new JPanelData.
	 */
	public JPanelData(DataController dataController) {
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
			
			jSplitPaneData.setLeftComponent(this.getJPanelTree());
			jSplitPaneData.setRightComponent(this.getJPanelDataTable());
		}
		return jSplitPaneData;
	}
	private JPanelTree getJPanelTree() {
		if (jPanelTree == null) {
			jPanelTree = new JPanelTree(this.getDataController());
		}
		return jPanelTree;
	}
	private JPanelDataTable getJPanelDataTable() {
		if (jPanelDataTable==null) {
			jPanelDataTable = new JPanelDataTable(this.getDataController());
		}
		return jPanelDataTable;
	}
	
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		
		
	}
	
}
