package de.enflexit.df.core.processing.transformation.graph.ui;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JWindow;

import de.enflexit.df.core.model.DataController;

public class DataSourceSelectionWindow extends JWindow {

private static final long serialVersionUID = -653322247106918023L;
	
	private DataSourceSelectionPanel jPanelSelectionList;
	
	private TransformationGraphEditorPanel parentEditorPanel;
	
	private DataController dataControler;
	
	/**
	 * Instantiates a new j dialog select data sources.
	 * @param owner the owner
	 * @param graphController the graph controller
	 */
	public DataSourceSelectionWindow(Window owner, TransformationGraphEditorPanel parentEditor, DataController dataControler) {
		super(owner);
		this.parentEditorPanel = parentEditor;
		this.dataControler = dataControler;
		initialize();
	}
	
	/**
	 * Initializes the UI elements.
	 */
	private void initialize() {
		getContentPane().add(getJPanelSelectionList(), BorderLayout.CENTER);
		this.setSize(200, 250);
		
		this.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				setVisible(false);
			}
			@Override
			public void windowGainedFocus(WindowEvent e) {
				// --- Nothing to do here ---
			}
		});
	}

	private DataSourceSelectionPanel getJPanelSelectionList() {
		if (jPanelSelectionList == null) {
			jPanelSelectionList = new DataSourceSelectionPanel(dataControler, parentEditorPanel);
		}
		return jPanelSelectionList;
	}

}
