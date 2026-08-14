package de.enflexit.df.core.extension.test;

import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import org.hibernate.cfg.Configuration;

import de.enflexit.awb.core.ui.AwbMessageDialog;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.extension.ColumnDescription;
import de.enflexit.df.core.extension.ColumnDescriptionRenderer;
import de.enflexit.df.core.extension.DataWorkbookExtension;
import de.enflexit.df.core.ui.JToolBarData;
import de.enflexit.df.core.workbook.ExtensionCache;
import de.enflexit.df.core.workbook.db.SessionFactoryCreator;

/**
 * The Class ExtensionTestMenu.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExtensionTestColumnDescriptions implements DataWorkbookExtension {

	@SuppressWarnings("unused")
	private ExtensionCache extensionCache;
	private ColumnDescriptionRenderer cdRenderer;
	
	private JButton jButtonHello;
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#getExtensionName()
	 */
	@Override
	public String getExtensionName() {
		return "TEST.4.COLUMN_DESCRIPTION";
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#getExtensionDescription()
	 */
	@Override
	public String getExtensionDescription() {
		return "This class provides an extension test case for table column descriptions";
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#newInstance()
	 */
	@Override
	public DataWorkbookExtension newInstance() {
		return new ExtensionTestColumnDescriptions();
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
					AwbMessageDialog.showMessageDialog(owner, getExtensionDescription(), "Hello from extension '" + getExtensionName() + "'!", AwbMessageDialog.INFORMATION_MESSAGE);
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
		
		String letter = "A";
		for (ColumnDescription colDescription : columnDescriptionList) {
			colDescription.getProperties().setStringValue("Alphanumeric Counter", letter);
			colDescription.setColumnDescriptionRenderer(this.getColumnDescriptionRenderer());
			letter = this.next(letter);
		}
	}
	/**
	 * Returns the column description renderer.
	 * @return the column description renderer
	 */
	private ColumnDescriptionRenderer getColumnDescriptionRenderer() {
		if (cdRenderer==null) {
			cdRenderer = new ColumnDescriptionRenderer() {
				@Override
				public String getDescription(ColumnDescription columnDescription) {
					String colDesc = columnDescription.getDefaultDescription();
					colDesc = colDesc.replaceAll("\\n", "<br>");
					colDesc = "<html>" + colDesc + "</html>";
					return colDesc;
				}
				@Override
				public String getToolTip(ColumnDescription columnDescription) {
					String colDesc = columnDescription.getDefaultDescription();
					colDesc = colDesc.replaceAll("\\n", "<br><b>");
					colDesc = colDesc.replaceAll(":", ":</b>");
					colDesc = "<html><b>" + colDesc + "</html>";
					return colDesc;
				}
			};
		}
		return cdRenderer;
	}
	/**
	 * Next letter.
	 *
	 * @param value the value
	 * @return the string
	 */
	private String next(String value) {

		char[] chars = value.toUpperCase().toCharArray();
		int i = chars.length - 1;
		while (i >= 0 && chars[i] == 'Z') {
			chars[i] = 'A';
			i--;
		}

		if (i < 0) {
			return "A" + new String(chars);
		}
		chars[i]++;
		return new String(chars);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.DataWorkbookExtension#addAnnotatedClassesToDataWorkbook4DB(de.enflexit.df.core.workbook.db.SessionFactoryCreator, org.hibernate.cfg.Configuration)
	 */
	@Override
	public void addAnnotatedClassesToDataWorkbook4DB(SessionFactoryCreator sessionFactoryCreator, Configuration conf) {
		
	}
	
}
