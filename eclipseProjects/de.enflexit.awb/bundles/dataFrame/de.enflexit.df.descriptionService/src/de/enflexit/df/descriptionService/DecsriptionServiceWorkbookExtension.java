package de.enflexit.df.descriptionService;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.hibernate.cfg.Configuration;

import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.extension.ColumnDescription;
import de.enflexit.df.core.extension.ColumnDescriptionPanel;
import de.enflexit.df.core.extension.DataWorkbookExtension;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.JToolBarData;
import de.enflexit.df.core.workbook.ExtensionCache;
import de.enflexit.df.core.workbook.db.SessionFactoryCreator;
import de.enflexit.df.descriptionService.db.DataColumnDescription;
import de.enflexit.df.descriptionService.db.DataColumnAlternativeID;
import de.enflexit.df.descriptionService.ui.ColumnDescriptionEditorDialog;
import de.enflexit.df.descriptionService.ui.DescriptionServiceColumnDescriptionPanel;

/**
 * This {@link DataWorkbookExtension} allows to attach describing information to columns of a data workbook.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DecsriptionServiceWorkbookExtension implements DataWorkbookExtension, ActionListener {
	
	private static final String EXTENSION_NAME = "Column Description Service";
	private static final String EXTENSION_DESCRIPTION = "This extension allows to attach describing information to columns of the extended data workbook";
	
	private SessionFactoryCreator sessionFactoryCreator;
	
	private JButton jButtonDescriptionEditor;
	private ImageIcon descriptionEditorIcon;
	private ColumnDescriptionEditorDialog descriptionEditorDialog;
	
	private DataController dataController;
	
	private DescriptionsController descriptionsController;
	
	private DescriptionServiceColumnDescriptionPanel columnDescriptionPanel;
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#getExtensionName()
	 */
	@Override
	public String getExtensionName() {
		return EXTENSION_NAME;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#getExtensionDescription()
	 */
	@Override
	public String getExtensionDescription() {
		return EXTENSION_DESCRIPTION;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#newInstance()
	 */
	@Override
	public DataWorkbookExtension newInstance() {
		return new DecsriptionServiceWorkbookExtension();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#initialize(de.enflexit.df.core.workbook.ExtensionCache)
	 */
	@Override
	public void initialize(ExtensionCache extensionCache) {
		// Not required
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#dispose()
	 */
	@Override
	public void dispose() {
		// Not required
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#requiresWorkbookReload()
	 */
	@Override
	public boolean requiresWorkbookReload() {
		return true;
	}

	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#addMainToolbarComponents(de.enflexit.df.core.ui.JToolBarData)
	 */
	@Override
	public void addMainToolbarComponents(JToolBarData jToolBarData) {
		this.dataController = jToolBarData.getDataController();
		jToolBarData.add(this.getjButtonDescriptionEditor());
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#updateColumnDescriptionList(java.util.List)
	 */
	@Override
	public void updateColumnDescriptionList(List<ColumnDescription> columnDescriptionList) {
		for (ColumnDescription colDesc : columnDescriptionList) {
			colDesc.setColumnDescriptionRenderer(new DescriptorServiceColumnDescriptionRenderer(this.getDescriptionsController()));
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#getColumnDescriptionPanel()
	 */
	@Override
	public ColumnDescriptionPanel getColumnDescriptionPanel() {
		if (columnDescriptionPanel==null) {
			columnDescriptionPanel = new DescriptionServiceColumnDescriptionPanel(this.getDescriptionsController());
		}
		return columnDescriptionPanel;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#addAnnotatedClassesToDataWorkbook4DB(de.enflexit.df.core.workbook.db.SessionFactoryCreator, org.hibernate.cfg.Configuration)
	 */
	@Override
	public void addAnnotatedClassesToDataWorkbook4DB(SessionFactoryCreator sessionFactoryCreator, Configuration conf) {
		this.sessionFactoryCreator = sessionFactoryCreator;
		conf.addAnnotatedClass(DataColumnDescription.class);
		conf.addAnnotatedClass(DataColumnAlternativeID.class);
	}
	
	/**
	 * Gets the j button description editor.
	 * @return the j button description editor
	 */
	private JButton getjButtonDescriptionEditor() {
		if (jButtonDescriptionEditor==null) {
			jButtonDescriptionEditor = new JButton();
			jButtonDescriptionEditor.setIcon(this.getDescriptionEditorIcon());
			jButtonDescriptionEditor.setToolTipText("Open the data column description editor");
			jButtonDescriptionEditor.addActionListener(this);
		}
		return jButtonDescriptionEditor;
	}

	/**
	 * Gets the description editor icon.
	 * @return the description editor icon
	 */
	private ImageIcon getDescriptionEditorIcon() {
		if (descriptionEditorIcon==null) {
			descriptionEditorIcon = BundleHelper.getImageIcon("Edit.png");
		}
		return descriptionEditorIcon;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getjButtonDescriptionEditor()) {
			this.getDescriptionEditorDialog().setVisible(true);
		}
	}
	
	/**
	 * Gets the description editor dialog.
	 * @return the description editor dialog
	 */
	private ColumnDescriptionEditorDialog getDescriptionEditorDialog() {
		if (descriptionEditorDialog==null) {
			Window owner = OwnerDetection.getOwnerWindowForComponent(this.getjButtonDescriptionEditor());
			descriptionEditorDialog = new ColumnDescriptionEditorDialog(owner, this.getDescriptionsController());
		}
		return descriptionEditorDialog;
	}
	
	/**
	 * Gets the descriptions controller.
	 * @return the descriptions controller
	 */
	private DescriptionsController getDescriptionsController() {
		if (descriptionsController==null) {
			descriptionsController = new DescriptionsController(this.dataController, this.sessionFactoryCreator);
		}
		return descriptionsController;
	}

}
