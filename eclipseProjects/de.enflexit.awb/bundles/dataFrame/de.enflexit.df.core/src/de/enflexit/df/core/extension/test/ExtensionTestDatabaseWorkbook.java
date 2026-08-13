package de.enflexit.df.core.extension.test;

import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import de.enflexit.awb.core.ui.AwbMessageDialog;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.extension.ColumnDescription;
import de.enflexit.df.core.extension.DataWorkbookExtension;
import de.enflexit.df.core.ui.JToolBarData;
import de.enflexit.df.core.workbook.ExtensionCache;

/**
 * The Class ExtensionTestMenu.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExtensionTestDatabaseWorkbook implements DataWorkbookExtension {

	private ExtensionCache extensionCache;
	
	private JButton jButtonHello;
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#getExtensionName()
	 */
	@Override
	public String getExtensionName() {
		return "TEST.4.DATABASE_CONNECTION";
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#getExtensionDescription()
	 */
	@Override
	public String getExtensionDescription() {
		return "This class provides an extension for a database workbook";
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#newInstance()
	 */
	@Override
	public DataWorkbookExtension newInstance() {
		return new ExtensionTestDatabaseWorkbook();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#initialize(de.enflexit.df.core.workbook.ExtensionCache)
	 */
	@Override
	public void initialize(ExtensionCache extensionCache) {
		this.extensionCache = extensionCache;
		System.out.println("=> Initialized " + this.getClass().getSimpleName());
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#dispose()
	 */
	@Override
	public void dispose() {
		this.extensionCache = null;
		System.out.println("=> Disposed " + this.getClass().getSimpleName());
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#addMainToolbarComponents(de.enflexit.df.core.ui.JToolBarData)
	 */
	@Override
	public void addMainToolbarComponents(JToolBarData jToolBarData) {
		
		jToolBarData.add(this.getJButtonHello());
		jToolBarData.addSeparator();
	}

	private JButton getJButtonHello() {
		if (jButtonHello==null) {
			jButtonHello = new JButton(this.getExtensionName());
			jButtonHello.setToolTipText(this.getExtensionDescription());
			jButtonHello.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
			jButtonHello.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					Window owner = OwnerDetection.getOwnerWindowForComponent(getJButtonHello());
					AwbMessageDialog.showMessageDialog(owner, getExtensionDescription(), "Hello from extension '" + getExtensionName() + "'!", AwbMessageDialog.WARNING_MESSAGE);
				}
			});
		}
		return jButtonHello;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#updateColumnDescriptionList(java.util.List)
	 */
	@Override
	public void updateColumnDescriptionList(List<ColumnDescription> columnDescriptionList) {
		
	}
	
}
