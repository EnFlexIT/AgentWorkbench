/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.PropertyContentProvider.FileToProvide;
import agentgui.core.gui.components.JHyperLink;


/**
 * The JDialog is used in order to allow the translations between all defined languages
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Translation extends JDialog implements ActionListener {

private static final long serialVersionUID = 1L;
	
	private final String appName = Application.getGlobalInfo().getApplicationTitle();  //  @jve:decl-index=0:

	private Vector<Object> currDataSet = null;
	private DefaultTableModel dictData = null;
	private DefaultComboBoxModel<LanguageListElement> langSelectionModelSource = new DefaultComboBoxModel<LanguageListElement>();
	private DefaultComboBoxModel<LanguageListElement> langSelectionModelDestin = new DefaultComboBoxModel<LanguageListElement>();
	
	private boolean useGoogleTranslation = false;
	private String lastGoogleTranslation = null;
	
	private boolean forceApplicationRestart = false;
	
	private JPanel jContentPane = null;
		private JLabel jLabelNorth = null;
		private JLabel jLabelWest = null;
		private JLabel jLabelEast = null;
		private JPanel jPanelSouth = null;

	private JTabbedPane jTabbedPane = null;

		private JScrollPane jScrollPaneDictionary = null;
			private JTable jTableDictionary = null;
			
		private JPanel jPanelTranslation = null;
			private JLabel jLabelSource = null;
			private JLabel jLabelDestination = null;
			private JLabel jLabelGoogleHeader = null;
			private JLabel jLabelSelectSourceLang = null;
			private JLabel jLabelSelectDestinationLang = null;

			private JScrollPane jScrollPaneTextSource = null;
			private JScrollPane jScrollPaneTextDestination = null;
			private JScrollPane jScrollPanelGoogle = null;
			
			private JTextPane jTextFieldSource = null;
			private JTextPane jTextFieldDestination = null;
			private JTextPane jTextAreaGoogle = null;
			
			private JComboBox<LanguageListElement> jComboBoxSourceLang = null;
			private JComboBox<LanguageListElement> jComboBoxDestinationLang = null;

			private JButton jButtonNextDS = null;
			private JButton jButtonPreviousDS = null;
			private JButton jButtonDelete = null;
			private JButton jButtonSave = null;
			private JButton jButtonFindGap = null;
			private JButton jButtonGoogleTake = null;

		private JPanel jPaneFooter = null;
			private JButton jButtonClose = null;
	
	private JPopupMenu jPopupMenuDictionary = null;
	private JMenuItem jMenuItemDelete = null;
	private JMenuItem jMenuItemEdit = null;

	private JButton jButtonImportCSV = null;
	private JPanel jPanelSouthWest = null;
	private JPanel jPanelEast = null;
	private JLabel jLabelSourceLanguage = null;

	private JCheckBox jCheckBoxUseGoogleTranslation = null;
	private JHyperLink jHyperLinkGoogleInfo = null;
	
	private JPanel jPanelGoogleKey4API = null;
	private JLabel jLabelGoogleKey4API = null;
	private JTextField jTextFieldGoogleKey4API = null;
	private JLabel jLabelGoogleHTTP = null;
	private JTextField jTextFieldGoogleHTTP = null;
	private JButton jButtonGoogleKey4API = null;
	private JButton jButtonResetDictionary;



	/**
	 * Instantiates a new translation.
	 * @param owner the owner
	 */
	public Translation(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * In order to catch the closing of this window, this method overrides the one
	 * from the super class.
	 * In the case that the csv-version of the dictionary was imported, the application
	 * has to restart now in order to show the right translations.
	 *
	 * @param b the new visible
	 */
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (this.forceApplicationRestart==true) {
			System.out.println(Language.translate("Neuinitialsierung des Anwendungsfensters ..."));
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					Application.setLanguage(Application.getGlobalInfo().getLanguage(), false);
				}
			});
		}
	}
	
	/**
	 * This method initializes this.
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(1000, 550);
		
		this.setTitle(appName + ": " + "Wörterbuch");
		this.setIconImage(GlobalInfo.getInternalImageAwbIcon16());
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				setVisible(false);
			}
		});
		
		// --- Inhalt darstellen ------------------------------------
		this.setContentPane(getJContentPane());

		// --- Load Data from dictionary ----------------------------
		this.getTableModel4Dictionary();
		this.setLanguageComboModels();
		
		// --- Configure PopUp-menue -------------------------------- 
		this.getJPopupMenuDictionary();
		
		// --- Set Selection in combos ------------------------------
		String currLang = Application.getGlobalInfo().getLanguage();
		int currLangIndex = Language.getIndexOfLanguage(currLang)-1;
		if (currLang.equalsIgnoreCase("de") || currLang.equalsIgnoreCase("en") ) {
			jComboBoxSourceLang.setSelectedIndex(Language.getIndexOfLanguage("de")-1);
			jComboBoxDestinationLang.setSelectedIndex(Language.getIndexOfLanguage("en")-1);
		} else {
			jComboBoxSourceLang.setSelectedIndex(Language.getIndexOfLanguage("en")-1);
			jComboBoxDestinationLang.setSelectedIndex(currLangIndex);
		}
		
		// --- Set Google-HttpReferrer and API key ------------------
		this.jTextFieldGoogleHTTP.setText(Application.getGlobalInfo().getGoogleHttpRef());
		this.jTextFieldGoogleKey4API.setText(Application.getGlobalInfo().getGoogleKey4API());
		com.google.api.GoogleAPI.setHttpReferrer(Application.getGlobalInfo().getGoogleHttpRef());
		com.google.api.GoogleAPI.setKey(Application.getGlobalInfo().getGoogleKey4API());

		// --- Listen to keyboard events ----------------------------
		this.setKeyListenEvents();
		
		// --- Position festlegen -----------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);	
	 
	    // --- Translate --------------------------------------------
		this.setTitle(appName + ": " + Language.translate("Wörterbuch"));
		jTabbedPane.setTitleAt(0, Language.translate("Wörterbuch"));
		jTabbedPane.setTitleAt(1, Language.translate("Übersetzen ..."));
		jButtonClose.setText(Language.translate("Schließen"));
		
		jMenuItemDelete.setText(Language.translate("Löschen"));
		jMenuItemEdit.setText(Language.translate("Übersetzen"));
		
		jLabelSelectSourceLang.setText(Language.translate("Sprache:"));
		jLabelSelectDestinationLang.setText(Language.translate("Sprache:"));
		jLabelSource.setText(Language.translate("Text zur Übersetzung"));
		jLabelDestination.setText(Language.translate("Übersetzter Text "));
		jLabelGoogleHeader.setText(Language.translate("Google - Übersetzung"));
		
		jButtonPreviousDS.setToolTipText(Language.translate("Voriger Datensatz (STRG+UP)"));
		jButtonNextDS.setToolTipText(Language.translate("Nächster Datensatz (STRG+DOWN)"));
		jButtonSave.setToolTipText(Language.translate("Übersetzung speichern (STRG+S)"));
		jButtonFindGap.setToolTipText(Language.translate("Suche nach der nächsten fehlenden Übersetzung (STRG+F)"));
		jButtonGoogleTake.setToolTipText(Language.translate("Google-Übersetzung übernehmen (STRG+G)"));

	}

	/**
	 * Here the key actions will be handled.
	 */
	private void setKeyListenEvents() {
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent ke) {
				
				// --- Just listen to KEY_PRESSED-Events --
				if (ke.getID()!=KeyEvent.KEY_PRESSED) {
					return false;
				}
				
				// --- Close Windows-Event ----------------
				if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (jTableDictionary.isEditing()==false) {
						jButtonClose.doClick();	
					}
				} 
				
				// --- CTRL-Events abfangen ---------------
				if (ke.getModifiers() == KeyEvent.CTRL_MASK || ke.getModifiers() == KeyEvent.CTRL_DOWN_MASK) {
					if (ke.getKeyCode()==KeyEvent.VK_S) {
						jButtonSave.doClick();
						
					} else if (ke.getKeyCode()==KeyEvent.VK_G) {
						jButtonGoogleTake.doClick();
					
					} else if (ke.getKeyCode()==KeyEvent.VK_F) {
						jButtonFindGap.doClick();
						
					} else if (ke.getKeyCode()==KeyEvent.VK_UP) {
						if (jTextFieldSource.hasFocus()==false 
								& jTextFieldDestination.hasFocus()==false 
								& jTextAreaGoogle.hasFocus()==false
								& jTableDictionary.hasFocus()==false) {
							jButtonPreviousDS.doClick();	
						}
						
					} else if (ke.getKeyCode()==KeyEvent.VK_DOWN) {
						if (jTextFieldSource.hasFocus()==false 
								& jTextFieldDestination.hasFocus()==false 
								& jTextAreaGoogle.hasFocus()==false
								& jTableDictionary.hasFocus()==false) {
							jButtonNextDS.doClick();	
						}
						
					} else if (ke.getKeyCode()==KeyEvent.VK_DELETE) {
						jButtonDelete.doClick();
						
					}
					
				}					
				return false;
			}
		});
	}
	
	/**
	 * This method will set the data model for the Language selection.
	 */
	private void setLanguageComboModels() {
		String[] languages = Language.getLanguages(true);
		for (int i = 0; i < languages.length; i++) {
			LanguageListElement lang2List = new LanguageListElement(languages[i], Language.getLanguageName(languages[i]));
			this.langSelectionModelSource.addElement(lang2List);
			this.langSelectionModelDestin.addElement(lang2List);
		}
	}
	
	/**
	 * The Class LanguageListElement.
	 */
	public class LanguageListElement {
		
		private String langShort;
		private String langLong;
		
		/**
		 * Instantiates a new language list element.
		 *
		 * @param langShortText the lang short text
		 * @param langLongText the lang long text
		 */
		public LanguageListElement(String langShortText, String langLongText) {
			this.langShort = langShortText; 
			this.langLong = langLongText;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return this.langLong;
		}
		/**
		 * Gets the lang short.
		 * @return the lang short
		 */
		public String getLangShort() {
			return langShort;
		}
		/**
		 * Gets the lang long.
		 * @return the lang long
		 */
		public String getLangLong() {
			return langLong;
		}
	}
	
	/**
	 * dictionary data will be loaded to the TableModel.
	 */
	private DefaultTableModel getTableModel4Dictionary() {
		
		if (dictData==null) {
			
			dictData = new DefaultTableModel();

			// --- Header of the table ----------------------------------
			dictData.addColumn("Nr.");
			String[] languages = Language.getLanguages();
			for (int i = 0; i < languages.length; i++) {
				String lang = languages[i];
				if (lang.equalsIgnoreCase(Language.SOURCE_LANG)) {
					dictData.addColumn(Language.translate("Sprache"));
				} else {
					dictData.addColumn(Language.getLanguageName(lang));	
				}
			}
			
			// --- Rows of the dictionary -------------------------------
			int rowNo = 0;
			List<String> dictContent = Language.getDictLineList();
			for (String dictLine : dictContent) {
				
				Vector<Object> rowData = new Vector<Object>(); 
				rowData.addAll(Arrays.asList(dictLine.split(Language.seperator, -1)));
				
				if (rowData.get(0)!=null && rowData.get(0).equals("")==false && rowData.get(0).equals(Language.SOURCE_LANG)==false) {
					// --- add row to the displayable dictionary --------
					rowNo++;
					rowData.add(0, rowNo);
					dictData.addRow(rowData);	
				}
			}
			
			// --- add TableModelListener -------------------------------
			dictData.addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent evt) {
					
					int rowChanged = evt.getFirstRow();
					int colChanged = evt.getColumn();
					
					if (rowChanged==-1 || colChanged==-1) {
						return;
					}
					
					// --- maybe correct value --------------------------
					String cellValue = (String) dictData.getValueAt(rowChanged, colChanged);
					String checkValue = new String(cellValue.toString());
					if (checkValue.equals(cellValue)==false) {
						dictData.setValueAt(checkValue, rowChanged, colChanged);
					}
					
					// --- get row data ---------------------------------
					Vector<?> dataVectorRow = (Vector<?>) dictData.getDataVector().elementAt(rowChanged);
					
					// --- make a copy of the row data ------------------
					Vector<Object> rowData = new Vector<Object>(dataVectorRow); 
					rowData.removeElementAt(0);
					String deExp = (String) rowData.get(0);
					String dictRow = "";
					for (int i = 0; i < rowData.size(); i++) {
						if (dictRow.equals("")) {
							dictRow += rowData.get(i);	
						} else {
							dictRow += Language.seperator + rowData.get(i);
						}
					}
					
					// --- update the dictionary ------------------------
					Language.update(deExp, dictRow);
					
				}
			});
			
		}
		return dictData;
	}
	/**
	 * This method initializes jTableDictionary.
	 * @return javax.swing.JTable
	 */
	private JTable getJTableDictionary() {
		if (jTableDictionary == null) {
			jTableDictionary = new JTable();
			jTableDictionary.setModel(this.getTableModel4Dictionary());
			jTableDictionary.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
			jTableDictionary.setColumnSelectionAllowed(false);
			jTableDictionary.setRowSelectionAllowed(true);
			jTableDictionary.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableDictionary.setAutoCreateRowSorter(true);
			// --- Add MouseListener ----------------------
			jTableDictionary.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent evt) {
					this.maybeShowPopUp(evt);
				}
				@Override
				public void mousePressed(MouseEvent evt) {
					this.maybeShowPopUp(evt);
				}
				@Override
				public void mouseReleased(MouseEvent evt) {
					this.maybeShowPopUp(evt);
				}
				public void maybeShowPopUp(MouseEvent evt) {
					if (evt.isPopupTrigger()) {
						// --- mark row -------------------
						jTableDictionary.changeSelection(jTableDictionary.rowAtPoint(evt.getPoint()), 0, false, false);
						// --- show popUp -----------------
						getJPopupMenuDictionary().show(evt.getComponent(), evt.getX(), evt.getY());
					}
				}
			});
			// --- Add SelectionListener ------------------
			jTableDictionary.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent eve) {
					int currSelection = jTableDictionary.getSelectedRow();
					if (currSelection>-1) {
						// --- provide dataset  -----------
						setCurrentDataSet(jTableDictionary.convertRowIndexToModel(currSelection));
					}
				}
			});
			
			// --------------------------------------------
			// --- Define RowSorter for the first column --
			TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(dictData);
			sorter.setComparator(0, new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			});
			jTableDictionary.setRowSorter(sorter);
			// --- Define the first sort order ------------
			List<SortKey> sortKeys = new ArrayList<SortKey>();
			sortKeys.add(new SortKey(0, SortOrder.ASCENDING));
			jTableDictionary.getRowSorter().setSortKeys(sortKeys);
			
			// ----------------------------------------------------------
			// --- Set layout of the table ------------------------------
			TableColumn tbColNo = this.jTableDictionary.getColumnModel().getColumn(0);
			tbColNo.setPreferredWidth(40);
			tbColNo.setMinWidth(35);
			tbColNo.setMaxWidth(45);
			
			TableColumn tbColSrcLang = this.jTableDictionary.getColumnModel().getColumn(1);
			tbColSrcLang.setPreferredWidth(55);
			tbColSrcLang.setMinWidth(35);
			tbColSrcLang.setMaxWidth(70);
			
		}
		return jTableDictionary;
	}
	/**
	 * This method initializes jPopupMenuDictionary.
	 * @return javax.swing.JPopupMenu
	 */
	private JPopupMenu getJPopupMenuDictionary() {
		if (jPopupMenuDictionary == null) {
			jPopupMenuDictionary = new JPopupMenu();
			jPopupMenuDictionary.add(getJMenuItemEdit());
			jPopupMenuDictionary.add(getJMenuItemDelete());
		}
		return jPopupMenuDictionary;
	}
	/**
	 * This method initializes jMenuItemDelete.
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItemDelete() {
		if (jMenuItemDelete == null) {
			jMenuItemDelete = new JMenuItem();
			jMenuItemDelete.setText("Löschen");
			jMenuItemDelete.addActionListener(this);
		}
		return jMenuItemDelete;
	}
	/**
	 * This method initializes jMenuItemEdit.
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItemEdit() {
		if (jMenuItemEdit == null) {
			jMenuItemEdit = new JMenuItem();
			jMenuItemEdit.setText("Übersetzen");
			jMenuItemEdit.addActionListener(this);
		}
		return jMenuItemEdit;
	}
	/**
	 * This method initializes jContentPane.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabelNorth = new JLabel();
			jLabelNorth.setText("");
			jLabelNorth.setPreferredSize(new Dimension(30, 10));
			jLabelEast = new JLabel();
			jLabelEast.setText("");
			jLabelEast.setPreferredSize(new Dimension(10, 16));
			jLabelWest = new JLabel();
			jLabelWest.setText("");
			jLabelWest.setPreferredSize(new Dimension(10, 16));
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(0);
			borderLayout.setVgap(0);
			jContentPane = new JPanel();
			jContentPane.setLayout(borderLayout);
			jContentPane.add(getJTabbedPane(), BorderLayout.CENTER);
			jContentPane.add(getJPanelSouth(), BorderLayout.SOUTH);
			jContentPane.add(jLabelWest, BorderLayout.WEST);
			jContentPane.add(jLabelEast, BorderLayout.EAST);
			jContentPane.add(jLabelNorth, BorderLayout.NORTH);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jTabbedPane.
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab("Wörterbuch", null, getJScrollPaneDictionary(), null);
			jTabbedPane.addTab("Übersetzen ...", null, getJPanelTranslation(), null);
		}
		return jTabbedPane;
	}
	/**
	 * This method initializes jPanelTranslation.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelTranslation() {
		if (jPanelTranslation == null) {
			
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.gridx = 4;
			gridBagConstraints27.insets = new Insets(24, 5, 6, 0);
			gridBagConstraints27.gridwidth = 3;
			gridBagConstraints27.anchor = GridBagConstraints.WEST;
			gridBagConstraints27.gridy = 4;
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.insets = new Insets(0, 10, 10, 10);
			gridBagConstraints20.gridwidth = 7;
			gridBagConstraints20.gridy = 6;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 1;
			gridBagConstraints19.insets = new Insets(23, 20, 9, 0);
			gridBagConstraints19.anchor = GridBagConstraints.SOUTHWEST;
			gridBagConstraints19.ipadx = 0;
			gridBagConstraints19.gridwidth = 2;
			gridBagConstraints19.gridy = 4;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 6;
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.insets = new Insets(10, 20, 5, 10);
			gridBagConstraints18.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints18.gridy = 0;
			
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 4;
			gridBagConstraints16.insets = new Insets(20, 5, 5, 5);
			gridBagConstraints16.gridy = 2;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 5;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(10, 30, 5, 5);
			gridBagConstraints9.gridy = 0;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 3;
			gridBagConstraints15.insets = new Insets(20, 20, 2, 5);
			gridBagConstraints15.gridy = 4;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 3;
			gridBagConstraints14.insets = new Insets(20, 20, 5, 5);
			gridBagConstraints14.gridy = 2;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 4;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.insets = new Insets(10, 5, 5, 5);
			gridBagConstraints13.gridy = 0;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 3;
			gridBagConstraints12.insets = new Insets(10, 20, 5, 5);
			gridBagConstraints12.gridy = 0;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 5;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.weighty = 0.33;
			gridBagConstraints11.gridwidth = 7;
			gridBagConstraints11.insets = new Insets(0, 10, 5, 10);
			gridBagConstraints11.anchor = GridBagConstraints.NORTH;
			gridBagConstraints11.gridx = 0;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 4;
			gridBagConstraints10.insets = new Insets(30, 14, 10, 0);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(10, 14, 5, 0);
			
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new Insets(0, 10, 0, 10);
			gridBagConstraints1.weighty = 0.33;
			gridBagConstraints1.gridwidth = 7;
			
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(20, 14, 5, 0);

			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 3;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.insets = new Insets(0, 10, 0, 10);
			gridBagConstraints4.weighty = 0.33;
			gridBagConstraints4.gridwidth = 7;
			
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.insets = new Insets(10, 20, 5, 0);
			gridBagConstraints7.gridy = 0;

			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.NONE;
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.weightx = 0.0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.insets = new Insets(10, 5, 5, 0);

			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.gridy = 2;
			gridBagConstraints8.insets = new Insets(20, 20, 5, 0);
			
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.NONE;
			gridBagConstraints6.gridx = 2;
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.weightx = 0.0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.weighty = 0.0;
			gridBagConstraints6.insets = new Insets(20, 5, 5, 0);
			
			jLabelSelectSourceLang = new JLabel();
			jLabelSelectSourceLang.setText("Sprache:");

			jLabelSelectDestinationLang = new JLabel();
			jLabelSelectDestinationLang.setText("Sprache:");

			jLabelSource = new JLabel();
			jLabelSource.setText("Text zur Übersetzung");
			jLabelSource.setFont(new Font("Dialog", Font.BOLD, 12));

			jLabelDestination = new JLabel();
			jLabelDestination.setText("Übersetzter Text ");
			jLabelDestination.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jLabelSourceLanguage = new JLabel();
			jLabelSourceLanguage.setText("");
			jLabelSourceLanguage.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelSourceLanguage.setForeground(new Color(204, 0, 0));
			jLabelSourceLanguage.setHorizontalAlignment(SwingConstants.LEADING);
			jLabelSourceLanguage.setPreferredSize(new Dimension(38, 26));
			
			jLabelGoogleHeader = new JLabel();
			jLabelGoogleHeader.setText("Google - Übersetzung");
			jLabelGoogleHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jHyperLinkGoogleInfo = new JHyperLink();
			jHyperLinkGoogleInfo.setFont(new Font("Dialog", Font.BOLD, 12));
			jHyperLinkGoogleInfo.setText("Infos zum Google-API key und den Webseiten-Einstellungen ...");
			jHyperLinkGoogleInfo.setText(Language.translate(jHyperLinkGoogleInfo.getText()));
			jHyperLinkGoogleInfo.setLink("http://code.google.com/p/google-api-translate-java/");
			jHyperLinkGoogleInfo.setToolTipText(jHyperLinkGoogleInfo.getLink());
			jHyperLinkGoogleInfo.addActionListener(this);
			
			jPanelTranslation = new JPanel();
			jPanelTranslation.setLayout(new GridBagLayout());
			jPanelTranslation.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanelTranslation.add(getJScrollPaneTextSource(), gridBagConstraints1);
			jPanelTranslation.add(jLabelSource, gridBagConstraints2);
			jPanelTranslation.add(jLabelDestination, gridBagConstraints3);
			jPanelTranslation.add(getJScrollPaneTextDestination(), gridBagConstraints4);
			jPanelTranslation.add(getJComboBoxSourceLang(), gridBagConstraints5);
			jPanelTranslation.add(getJComboBoxDestinationLang(), gridBagConstraints6);
			jPanelTranslation.add(jLabelSelectSourceLang, gridBagConstraints7);
			jPanelTranslation.add(jLabelSelectDestinationLang, gridBagConstraints8);
			jPanelTranslation.add(jLabelGoogleHeader, gridBagConstraints10);
			jPanelTranslation.add(getJScrollPanelGoogle(), gridBagConstraints11);
			jPanelTranslation.add(getJButtonPreviousDS(), gridBagConstraints12);
			jPanelTranslation.add(getJButtonNextDS(), gridBagConstraints13);
			jPanelTranslation.add(getJButtonSave(), gridBagConstraints14);
			jPanelTranslation.add(getJButtonGoogleTake(), gridBagConstraints15);
			jPanelTranslation.add(getJButtonDelete(), gridBagConstraints9);
			jPanelTranslation.add(getJButtonFindGap(), gridBagConstraints16);
			jPanelTranslation.add(jLabelSourceLanguage, gridBagConstraints18);
			jPanelTranslation.add(getJCheckBoxDoGoogleTranslation(), gridBagConstraints19);
			jPanelTranslation.add(getJPanelGoogleKey4API(), gridBagConstraints20);
			jPanelTranslation.add(jHyperLinkGoogleInfo, gridBagConstraints27);
		}
		return jPanelTranslation;
	}
	/**
	 * This method initializes jScrollPaneDictionary.
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneDictionary() {
		if (jScrollPaneDictionary == null) {
			jScrollPaneDictionary = new JScrollPane();
			jScrollPaneDictionary.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jScrollPaneDictionary.setViewportView(getJTableDictionary());
		}
		return jScrollPaneDictionary;
	}

	/**
	 * This method initializes jPanelSouth.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelSouth() {
		if (jPanelSouth == null) {
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.gridx = 3;
			gridBagConstraints41.gridy = 0;
			gridBagConstraints41.insets = new Insets(10, 10, 15, 10);
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.insets = new Insets(10, 10, 15, 10);
			gridBagConstraints31.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(10, 10, 15, 10);
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.gridx = 2;
			jPanelSouth = new JPanel();
			jPanelSouth.setLayout(new GridBagLayout());
			jPanelSouth.add(getJPanelSouthWest(), gridBagConstraints31);
			jPanelSouth.add(getJPaneFooter(), gridBagConstraints);
			jPanelSouth.add(getJPanelEast(), gridBagConstraints41);
		}
		return jPanelSouth;
	}
	/**
	 * This method initializes jPaneFooter.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPaneFooter() {
		if (jPaneFooter == null) {
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = -1;
			gridBagConstraints17.gridy = -1;
			jPaneFooter = new JPanel();
			jPaneFooter.setLayout(new GridBagLayout());
			jPaneFooter.add(getJButtonClose(), new GridBagConstraints());
		}
		return jPaneFooter;
	}
	/**
	 * This method initializes jButtonClose.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonClose() {
		if (jButtonClose == null) {
			jButtonClose = new JButton();
			jButtonClose.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonClose.setPreferredSize(new Dimension(100, 26));
			jButtonClose.setText("Schließen");
			jButtonClose.addActionListener(this);			
		}
		return jButtonClose;
	}
	
	/**
	 * This method initializes jPanelSouthWest.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelSouthWest() {
		if (jPanelSouthWest == null) {
			jPanelSouthWest = new JPanel();
			jPanelSouthWest.setLayout(new GridBagLayout());
			GridBagConstraints gbc_jButtonImportCSV = new GridBagConstraints();
			gbc_jButtonImportCSV.insets = new Insets(0, 0, 5, 0);
			gbc_jButtonImportCSV.gridx = 0;
			gbc_jButtonImportCSV.gridy = 0;
			jPanelSouthWest.add(getJButtonImportCSV(), gbc_jButtonImportCSV);
			GridBagConstraints gbc_jButtonResetDictionary = new GridBagConstraints();
			gbc_jButtonResetDictionary.insets = new Insets(0, 10, 5, 0);
			gbc_jButtonResetDictionary.gridx = 1;
			gbc_jButtonResetDictionary.gridy = 0;
			jPanelSouthWest.add(getJButtonResetDictionary(), gbc_jButtonResetDictionary);
			
		}
		return jPanelSouthWest;
	}
	/**
	 * This method initializes jButtonImportCSV.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonImportCSV() {
		if (jButtonImportCSV == null) {
			jButtonImportCSV = new JButton();
			jButtonImportCSV.setToolTipText(Language.translate("'.csv'-Version des Wörterbuchs übernehmen ..."));
			jButtonImportCSV.setIcon(GlobalInfo.getInternalImageIcon("MBtransImport.png"));
			jButtonImportCSV.setPreferredSize(new Dimension(26, 26));
			jButtonImportCSV.addActionListener(this);
		}
		return jButtonImportCSV;
	}
	/**
	 * Returns the jButtonResetDictionary.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonResetDictionary() {
		if (jButtonResetDictionary == null) {
			jButtonResetDictionary = new JButton();
			jButtonResetDictionary.setToolTipText(Language.translate("Wörterbuch zurücksetzen"));
			jButtonResetDictionary.setPreferredSize(new Dimension(26, 26));
			jButtonResetDictionary.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonResetDictionary.addActionListener(this);
		}
		return jButtonResetDictionary;
	}
	
	/**
	 * This method initializes jPanelEast.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelEast() {
		if (jPanelEast == null) {
			jPanelEast = new JPanel();
			jPanelEast.setLayout(new GridBagLayout());
			jPanelEast.setPreferredSize(new Dimension(62, 26));
		}
		return jPanelEast;
	}
	/**
	 * This method initializes jScrollPaneDictionary.
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneTextSource() {
		if (jScrollPaneTextSource == null) {
			jScrollPaneTextSource = new JScrollPane();
			jScrollPaneTextSource.setViewportView(getJTextFieldSource());
		}
		return jScrollPaneTextSource;
	}
	/**
	 * This method initializes jTextFieldSource.
	 * @return javax.swing.JTextField
	 */
	private JTextPane getJTextFieldSource() {
		if (jTextFieldSource == null) {
			jTextFieldSource = new JTextPane();
			jTextFieldSource.setEditable(false);
		}
		return jTextFieldSource;
	}
	/**
	 * This method initializes jScrollPaneDictionary.
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneTextDestination() {
		if (jScrollPaneTextDestination == null) {
			jScrollPaneTextDestination = new JScrollPane();
			jScrollPaneTextDestination.setViewportView(getJTextFieldDestination());
		}
		return jScrollPaneTextDestination;
	}
	/**
	 * This method initializes jTextFieldDestination.
	 * @return javax.swing.JTextField
	 */
	private JTextPane getJTextFieldDestination() {
		if (jTextFieldDestination == null) {
			jTextFieldDestination = new JTextPane();
		}
		return jTextFieldDestination;
	}
	/**
	 * This method initializes jComboBoxSourceLang.
	 * @return javax.swing.JComboBox
	 */
	private JComboBox<LanguageListElement> getJComboBoxSourceLang() {
		if (jComboBoxSourceLang == null) {
			jComboBoxSourceLang = new JComboBox<LanguageListElement>();
			jComboBoxSourceLang.setModel(langSelectionModelSource);
			jComboBoxSourceLang.setPreferredSize(new Dimension(200, 26));
			jComboBoxSourceLang.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (currDataSet!=null) {
						jTextFieldSource.setText((String) currDataSet.get(jComboBoxSourceLang.getSelectedIndex()+1));	
						setGoogleTranslation();
					}
				}
			});
		}
		return jComboBoxSourceLang;
	}
	/**
	 * This method initializes jComboBoxDestinationLang.
	 * @return javax.swing.JComboBox
	 */
	private JComboBox<LanguageListElement> getJComboBoxDestinationLang() {
		if (jComboBoxDestinationLang == null) {
			jComboBoxDestinationLang = new JComboBox<LanguageListElement>();
			jComboBoxDestinationLang.setModel(langSelectionModelDestin);
			jComboBoxDestinationLang.setPreferredSize(new Dimension(200, 26));
			jComboBoxDestinationLang.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (currDataSet!=null) {
						jTextFieldDestination.setText((String) currDataSet.get(jComboBoxDestinationLang.getSelectedIndex()+1));
						setGoogleTranslation();
					}
				}
			});
		}
		return jComboBoxDestinationLang;
	}
	/**
	 * This method initializes jScrollPanelGoogle.
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPanelGoogle() {
		if (jScrollPanelGoogle == null) {
			jScrollPanelGoogle = new JScrollPane();
			jScrollPanelGoogle.setViewportView(getJTextAreaGoogle());
		}
		return jScrollPanelGoogle;
	}
	/**
	 * This method initializes jTextAreaGoogle.
	 * @return javax.swing.JTextArea
	 */
	private JTextPane getJTextAreaGoogle() {
		if (jTextAreaGoogle == null) {
			jTextAreaGoogle = new JTextPane();
			jTextAreaGoogle.setEditable(false);
		}
		return jTextAreaGoogle;
	}
	/**
	 * This method initializes jButtonNextDS.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonNextDS() {
		if (jButtonNextDS == null) {
			jButtonNextDS = new JButton();
			jButtonNextDS.setPreferredSize(new Dimension(26, 26));
			jButtonNextDS.setToolTipText("Nächster Datensatz (STRG+DOWN)");
			jButtonNextDS.setIcon(GlobalInfo.getInternalImageIcon("ArrowDown.png"));
			jButtonNextDS.addActionListener(this);
		}
		return jButtonNextDS;
	}
	/**
	 * This method initializes jButtonPreviousDS.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonPreviousDS() {
		if (jButtonPreviousDS == null) {
			jButtonPreviousDS = new JButton();
			jButtonPreviousDS.setPreferredSize(new Dimension(26, 26));
			jButtonPreviousDS.setToolTipText("Voriger Datensatz (STRG+UP)");
			jButtonPreviousDS.setIcon(GlobalInfo.getInternalImageIcon("ArrowUp.png"));
			jButtonPreviousDS.addActionListener(this);
		}
		return jButtonPreviousDS;
	}
	/**
	 * This method initializes jButtonDelete.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonDelete() {
		if (jButtonDelete == null) {
			jButtonDelete = new JButton();
			jButtonDelete.setPreferredSize(new Dimension(26, 26));
			jButtonDelete.setToolTipText("Datensatz löschen (STRG+DELETE)");
			jButtonDelete.setIcon(GlobalInfo.getInternalImageIcon("Delete.png"));
			jButtonDelete.addActionListener(this);
		}
		return jButtonDelete;
	}
	/**
	 * This method initializes jButtonSave.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonSave() {
		if (jButtonSave == null) {
			jButtonSave = new JButton();
			jButtonSave.setPreferredSize(new Dimension(26, 26));
			jButtonSave.setToolTipText("Übersetzung speichern (STRG+S)");
			jButtonSave.setIcon(GlobalInfo.getInternalImageIcon("MBsave.png"));
			jButtonSave.addActionListener(this);
		}
		return jButtonSave;
	}
	/**
	 * This method initializes jButtonFindGap.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonFindGap() {
		if (jButtonFindGap == null) {
			jButtonFindGap = new JButton();
			jButtonFindGap.setPreferredSize(new Dimension(26, 26));
			jButtonFindGap.setToolTipText("Suche nach der nächsten fehlenden Übersetzung (STRG+F)");
			jButtonFindGap.setIcon(GlobalInfo.getInternalImageIcon("Search.png"));
			jButtonFindGap.addActionListener(this);
		}
		return jButtonFindGap;
	}
	/**
	 * This method initializes jCheckBoxDoGoogleTranslation	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxDoGoogleTranslation() {
		if (jCheckBoxUseGoogleTranslation == null) {
			jCheckBoxUseGoogleTranslation = new JCheckBox();
			jCheckBoxUseGoogleTranslation.setText("Google-Übersetzung verwenden");
			jCheckBoxUseGoogleTranslation.setText(Language.translate(jCheckBoxUseGoogleTranslation.getText()));
			jCheckBoxUseGoogleTranslation.setSelected(this.useGoogleTranslation);
			jCheckBoxUseGoogleTranslation.addActionListener(this);
		}
		return jCheckBoxUseGoogleTranslation;
	}
	/**
	 * This method initializes jButtonGoogleTake.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonGoogleTake() {
		if (jButtonGoogleTake == null) {
			jButtonGoogleTake = new JButton();
			jButtonGoogleTake.setPreferredSize(new Dimension(26, 26));
			jButtonGoogleTake.setToolTipText("Google-Übersetzung übernehmen (STRG+G)");
			jButtonGoogleTake.setIcon(GlobalInfo.getInternalImageIcon("MBgoogle.png"));
			jButtonGoogleTake.addActionListener(this);
		}
		return jButtonGoogleTake;
	}

	/**
	 * This method initializes jPanelGoogleKey4API	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelGoogleKey4API() {
		if (jPanelGoogleKey4API == null) {
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.fill = GridBagConstraints.BOTH;
			gridBagConstraints26.gridy = 0;
			gridBagConstraints26.weightx = 0.5;
			gridBagConstraints26.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints26.gridx = 3;
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.gridx = 2;
			gridBagConstraints25.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints25.gridy = 0;
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.gridx = 0;
			gridBagConstraints24.gridy = 0;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 2;
			gridBagConstraints23.insets = new Insets(0, 0, 0, 5);
			gridBagConstraints23.gridy = 0;
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 4;
			gridBagConstraints22.gridy = 0;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = GridBagConstraints.BOTH;
			gridBagConstraints21.gridx = 1;
			gridBagConstraints21.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints21.weightx = 0.5;

			jLabelGoogleKey4API = new JLabel();
			jLabelGoogleKey4API.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelGoogleKey4API.setText("Google-API key:");
			
			jLabelGoogleHTTP = new JLabel();
			jLabelGoogleHTTP.setText("Ihre Webseite:");
			jLabelGoogleHTTP.setText(Language.translate(jLabelGoogleHTTP.getText()));
			jLabelGoogleHTTP.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelGoogleKey4API = new JPanel();
			jPanelGoogleKey4API.setLayout(new GridBagLayout());
			jPanelGoogleKey4API.add(getJTextFieldGoogleKey4API(), gridBagConstraints21);
			jPanelGoogleKey4API.add(getJButtonGoogleKey4API(), gridBagConstraints22);
			jPanelGoogleKey4API.add(jLabelGoogleKey4API, gridBagConstraints24);
			jPanelGoogleKey4API.add(jLabelGoogleHTTP, gridBagConstraints25);
			jPanelGoogleKey4API.add(getJTextFieldGoogleHTTP(), gridBagConstraints26);
		}
		return jPanelGoogleKey4API;
	}

	/**
	 * This method initializes jTextFieldGoogleKey4API	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldGoogleKey4API() {
		if (jTextFieldGoogleKey4API == null) {
			jTextFieldGoogleKey4API = new JTextField();
			jTextFieldGoogleKey4API.setPreferredSize(new Dimension(100, 26));
		}
		return jTextFieldGoogleKey4API;
	}
	/**
	 * This method initializes jTextFieldGoogleHTTP	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldGoogleHTTP() {
		if (jTextFieldGoogleHTTP == null) {
			jTextFieldGoogleHTTP = new JTextField();
			jTextFieldGoogleHTTP.setPreferredSize(new Dimension(100, 26));
		}
		return jTextFieldGoogleHTTP;
	}
	/**
	 * This method initializes jButtonGoogleKey4API	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonGoogleKey4API() {
		if (jButtonGoogleKey4API == null) {
			jButtonGoogleKey4API = new JButton();
			jButtonGoogleKey4API.setText(Language.translate("Speichern"));
			jButtonGoogleKey4API.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonGoogleKey4API.setForeground(new Color(0,153, 0));
			jButtonGoogleKey4API.addActionListener(this);
		}
		return jButtonGoogleKey4API;
	}
	
	/**
	 * Sets the current dataset to the specfied row.
	 * @param rowNumber the new current data set
	 */
	private void setCurrentDataSet(int rowNumberOfDataModel) {
		
		int rowNumberOfTable = jTableDictionary.convertRowIndexToView(rowNumberOfDataModel);
		jTableDictionary.setRowSelectionInterval(rowNumberOfTable, rowNumberOfTable);
		
		// --- provide dataset  ---------------------------
		Vector<?> dataVectorRow = (Vector<?>) this.getTableModel4Dictionary().getDataVector().elementAt(rowNumberOfDataModel);
		// --- make a copy of the row data ----------------
		Vector<Object> rowData = new Vector<Object>(dataVectorRow); 
		rowData.removeElementAt(0);
		currDataSet = new Vector<Object>(rowData);

		jTextFieldSource.setText((String) currDataSet.get(jComboBoxSourceLang.getSelectedIndex()+1));
		jTextFieldDestination.setText((String) currDataSet.get(jComboBoxDestinationLang.getSelectedIndex()+1));
		
		String sourceLang = (String) currDataSet.get(0);
		String sourceLangDescription = Language.getLanguageName(sourceLang);
		jLabelSourceLanguage.setText(Language.translate("Quell-Sprache") + ": " + sourceLang + " - " + sourceLangDescription);

		this.setGoogleTranslation();
		
	}
	
	/**
	 * Set the focus to the directed dataset.
	 *
	 * @param direction the direction
	 */
	private void move2Dataset(int direction) {
				
		int currModelSelection = 0;
		int currTabelSelection = 0; 
		
		currTabelSelection = jTableDictionary.getSelectedRow();
		if (currTabelSelection==-1) {
			if (direction > 0) {
				// --- If nothing is selected and direction is positive -----------
				jTableDictionary.setRowSelectionInterval(0, 0);
				currTabelSelection = -1;
			} else {
				return;
			}
		}
		
		try {
			currTabelSelection = currTabelSelection + direction;
			currModelSelection = jTableDictionary.convertRowIndexToModel(currTabelSelection) ;
			this.setCurrentDataSet(currModelSelection);
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
		
	}
	
	/**
	 * Shows and displays the next translation gap.
	 */
	private void findNextTranslationGap() {

		int searchColumn = jComboBoxDestinationLang.getSelectedIndex() + 2;
		int searchRow = 0;
		int searchRowStart = 0;
		int selectedRow = jTableDictionary.getSelectedRow();

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		// --- take into account the current selection --------------
		if (selectedRow!=-1) {
			searchRowStart = selectedRow+1;
		}
		// --- start search for the next gap in the dictionary ------ 
		for (searchRow=searchRowStart; searchRow<this.getTableModel4Dictionary().getRowCount(); searchRow++) {
			
			Vector<?> lineVector = (Vector<?>) this.getTableModel4Dictionary().getDataVector().elementAt(searchRow);
			String element = (String) lineVector.get(searchColumn);
			
			if (element==null || element.equals("")) {
				this.setCurrentDataSet(searchRow);
				this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
			}
		}
		// --- end of dictionary reached, start from the top again --
		if (selectedRow!=-1) {
			// --- ... but only if we didn't start from the top -----
			for (searchRow=0; searchRow<=selectedRow; searchRow++) {
				
				Vector<?> lineVector = (Vector<?>) this.getTableModel4Dictionary().getDataVector().elementAt(searchRow);
				String element = (String) lineVector.get(searchColumn);
				
				if (element==null || element.equals("")) {
					this.setCurrentDataSet(searchRow);
					this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					return;
				}
			}
		}
		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
	}
	
	/**
	 * Delete the current dataset.
	 */
	private void deleteDS() {
		
		if (currDataSet!=null) {
			// --- remove the entry from the dictionary --- 
			String langSource = (String) currDataSet.get(0);
			int langSourceIndex = Language.getIndexOfLanguage(langSource);
			String expression = (String) currDataSet.get(langSourceIndex);
			Language.delete(expression);

			// --- remove the entry from the dataDict -----
			int row = jTableDictionary.getSelectedRow();
			int rowModel = jTableDictionary.convertRowIndexToModel(row);
			this.getTableModel4Dictionary().removeRow(rowModel);
			if (row==0) {
				jTableDictionary.setRowSelectionInterval(row, row);
			} else {
				jTableDictionary.setRowSelectionInterval(row-1, row-1);	
			}
		}
	}
	
	/**
	 * Save the current dataset.
	 */
	private void saveDS() {
		
		if (currDataSet==null) {
			return;
		}
		
		// --- Set Focus to save button -------------------
		jButtonSave.requestFocusInWindow();

		// --- work on the Text ---------------------------  
		String sourceLang = (String) currDataSet.get(0);
		int sourceLangIndex = Language.getIndexOfLanguage(sourceLang);
		String sourceLangExpression = (String) currDataSet.get(sourceLangIndex);

		String textEdited = jTextFieldDestination.getText();
		
		int colEdited = jComboBoxDestinationLang.getSelectedIndex()+1;
		int rowEdited = jTableDictionary.getSelectedRow();
		
		// --- update current dataset ----------------------
		currDataSet.setElementAt(textEdited, colEdited);

		// --- update dictData -----------------------------
		this.getTableModel4Dictionary().setValueAt(textEdited, rowEdited, colEdited+1);
		
		// --- update the dictionary -----------------------		
		String dictRow = "";
		for (int i = 0; i < currDataSet.size(); i++) {
			if (dictRow.equals("")) {
				dictRow += currDataSet.get(i);	
			} else {
				dictRow += Language.seperator + currDataSet.get(i);
			}
		}
		Language.update(sourceLangExpression, dictRow);
		
	}
	
	/**
	 * Translate the given text by using the Google-Translate-API.
	 */
	private void setGoogleTranslation() {
		
		if (this.useGoogleTranslation) {
			
			String langSource = ((LanguageListElement) jComboBoxSourceLang.getSelectedItem()).getLangShort();
			String langDestin = ((LanguageListElement) jComboBoxDestinationLang.getSelectedItem()).getLangShort();
			String text2Translate = jTextFieldSource.getText();
			
			if (this.lastGoogleTranslation!=text2Translate) {
				
				langSource = langSource.replace("LANG_", "").toLowerCase();
				langDestin = langDestin.replace("LANG_", "").toLowerCase();
				
				com.google.api.translate.Language langGoogleSource = com.google.api.translate.Language.fromString(langSource);
				com.google.api.translate.Language langGoogleDestin = com.google.api.translate.Language.fromString(langDestin);
				
				String translation = null; 
				try {
					translation = com.google.api.translate.Translate.DEFAULT.execute(text2Translate, langGoogleSource, langGoogleDestin);
					
				} catch (Exception e) {
					//e.printStackTrace();
					translation  = "ERROR: " + e.getLocalizedMessage();
					translation += "\n=> " + Language.translate("Die Google-Übersetzungsfunktion wurde deaktiviert!");
					this.useGoogleTranslation = false;
					this.jCheckBoxUseGoogleTranslation.setSelected(this.useGoogleTranslation);

				}
				jTextAreaGoogle.setText(translation);
				this.lastGoogleTranslation = text2Translate;
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		Object trigger = ae.getSource();
		if (trigger == jButtonClose) {
			this.setVisible(false);
			
		} else if (trigger == this.getJButtonImportCSV()) {
			// --- Import csv version of the dictionary -------------
			String title = Language.translate("CSV-Version des Wörterbuchs übernehmen?");
			String message = Language.translate("Möchten Sie die CSV-Version des Wörterbuches jetzt übernehmen?");
			
			int answer = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION) {
				this.setVisible(false);
				Language.useCSVDictionaryFile();
				Application.showTranslationDialog();
				this.forceApplicationRestart = true;
			}
			
		} else if (trigger == this.getJButtonResetDictionary()) {
			// --- Reset dictionary to the installation version -----
			String title = Language.translate("Wörterbuch zurücksetzen?");
			String message = Language.translate("Möchten Sie das Wörterbuches auf den Stand nach der Erstinstallation zurücksetzen?");
			
			int answer = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION) {
				this.setVisible(false);
				Application.getGlobalInfo().getPropertyContentProvider().checkAndProvidePropertyContent(FileToProvide.DICTIONARY_CSV, true);
				Application.getGlobalInfo().getPropertyContentProvider().checkAndProvidePropertyContent(FileToProvide.DICTIONARY_BIN, true);
				Language.reStartDictionary();
				Application.showTranslationDialog();
				this.forceApplicationRestart = true;
			}
			
			
		} else if (trigger == jMenuItemEdit) {
			jTabbedPane.setSelectedComponent(jPanelTranslation);
			jTextFieldDestination.requestFocusInWindow();
			
		} else if (trigger == jMenuItemDelete || trigger == jButtonDelete) {
			this.deleteDS();
			
		} else if (trigger == jButtonNextDS) {
			this.move2Dataset(1);

		} else if (trigger == jButtonPreviousDS) {
			this.move2Dataset(-1);
			
		} else if (trigger == jButtonFindGap) {
			this.findNextTranslationGap();
			
		} else if (trigger == jButtonSave) {
			this.saveDS();
			
		} else if (trigger == jButtonGoogleTake) {
			if (jTextAreaGoogle.getText().startsWith("ERROR: ")==false) {
				jTextFieldDestination.setText(jTextAreaGoogle.getText());
			}
		} else if (trigger == jCheckBoxUseGoogleTranslation) {
			this.useGoogleTranslation = jCheckBoxUseGoogleTranslation.isSelected();
		
		} else if (trigger == jHyperLinkGoogleInfo) {
			try {
				Desktop.getDesktop().browse(new URI(ae.getActionCommand()));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		
		} else if (trigger == jButtonGoogleKey4API) {
			Application.getGlobalInfo().setGoogleHttpRef(this.getJTextFieldGoogleHTTP().getText());
			Application.getGlobalInfo().setGoogleKey4API(this.getJTextFieldGoogleKey4API().getText());
			com.google.api.GoogleAPI.setHttpReferrer(Application.getGlobalInfo().getGoogleHttpRef());
			com.google.api.GoogleAPI.setKey(this.getJTextFieldGoogleKey4API().getText());
			
		} else {
			System.out.println("Unknown Action-Event" + ae.getActionCommand() );
		}
		
	}

} 
