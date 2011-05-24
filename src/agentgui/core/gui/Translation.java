package agentgui.core.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
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
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import agentgui.core.application.Application;
import agentgui.core.application.Language;

import com.google.api.GoogleAPI;
import com.google.api.translate.Translate;
import java.awt.Color;
import javax.swing.SwingConstants;

public class Translation extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final String appName = Application.RunInfo.getApplicationTitle();  //  @jve:decl-index=0:
	private final String PathImage = Application.RunInfo.PathImageIntern();  //  @jve:decl-index=0:
	private ImageIcon imageIcon = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUI.png") );
	private Image image = imageIcon.getImage();
	
	private Vector<Object> currDataSet = null;
	private DefaultTableModel dictData = new DefaultTableModel();
	private DefaultComboBoxModel langSelectionModelSource = new DefaultComboBoxModel();
	private DefaultComboBoxModel langSelectionModelDestin = new DefaultComboBoxModel();
	
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
			
			private JComboBox jComboBoxSourceLang = null;
			private JComboBox jComboBoxDestinationLang = null;

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
	
	/**
	 * @param owner
	 */
	public Translation(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(1000, 550);
		
		this.setTitle(appName + ": " + "Wörterbuch");
		this.setIconImage( image );
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
		this.setDictionaryTable();
		this.setLanguageComboModels();
		
		// --- Configure PopUp-menue -------------------------------- 
		this.getJPopupMenuDictionary();
		
		// --- Set Selection in combos ------------------------------
		String currLang = Application.RunInfo.getLanguage();
		int currLangIndex = Language.getIndexOfLanguage(currLang)-1;
		if (currLang.equalsIgnoreCase("de") || currLang.equalsIgnoreCase("en") ) {
			jComboBoxSourceLang.setSelectedIndex(Language.getIndexOfLanguage("de")-1);
			jComboBoxDestinationLang.setSelectedIndex(Language.getIndexOfLanguage("en")-1);
		} else {
			jComboBoxSourceLang.setSelectedIndex(Language.getIndexOfLanguage("en")-1);
			jComboBoxDestinationLang.setSelectedIndex(currLangIndex);
		}
		
		// --- Set Google-HttpReferrer ------------------------------
		GoogleAPI.setHttpReferrer("http://code.google.com/p/google-api-translate-java/");
		
		// --- Listen to keyboard events ----------------------------
		this.setKeyListenEvents();
		
		// --- Position festlegen -----------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);	
	 
	    // --- Übersetzungen einstellen -----------------------------
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
	 * Here the key actions will be handled 
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
	 * This method will set the data model for the Language selection
	 */
	private void setLanguageComboModels() {
		String[] languages = Language.getLanguages(true);
		for (int i = 0; i < languages.length; i++) {
			LanguageListElement lang2List = new LanguageListElement(languages[i], Language.getLanguageName(languages[i]));
			this.langSelectionModelSource.addElement(lang2List);
			this.langSelectionModelDestin.addElement(lang2List);
		}
	}
	public class LanguageListElement {
		private String langShort;
		private String langLong;
		public LanguageListElement(String langShortText, String langLongText) {
			this.langShort = langShortText; 
			this.langLong = langLongText;
		}
		public String toString() {
			return this.langLong;
		}
		public String getLangShort() {
			return langShort;
		}
		public String getLangLong() {
			return langLong;
		}
	}
	
	/**
	 * dictionary data will be loaded to the TableModel
	 */
	private void setDictionaryTable() {
		
		// --- Header of the table ----------------------------------
		this.dictData.addColumn("Nr.");
		String[] languages = Language.getLanguages();
		for (int i = 0; i < languages.length; i++) {
			String lang = languages[i];
			if (lang.equalsIgnoreCase(Language.SOURCE_LANG)) {
				this.dictData.addColumn(Language.translate("Sprache"));
			} else {
				this.dictData.addColumn(Language.getLanguageName(lang));	
			}
				
		}
		
		// --- Rows of the dictionary -------------------------------
		int rowNo = 0;
		List<String> dictContent = Language.getDictLineList();
		for (String dictLine : dictContent) {
			
			Vector<Object> rowData = new Vector<Object>(); 
			rowData.addAll(Arrays.asList(dictLine.split(Language.seperator, -1)));
			if (rowData.get(0).equals(Language.SOURCE_LANG)==false) {
				// --- row counter ------------------------
				rowNo++;
				rowData.add(0, rowNo);
				this.dictData.addRow(rowData);	
			}
		}
		
		// --- add TableModelListener -------------------------------
		this.dictData.addTableModelListener(new TableModelListener() {
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
	
	/**
	 * This method initializes jTableDictionary	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTableDictionary() {
		if (jTableDictionary == null) {
			jTableDictionary = new JTable();
			jTableDictionary.setModel(dictData);
			jTableDictionary.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
			jTableDictionary.setColumnSelectionAllowed(false);
			jTableDictionary.setRowSelectionAllowed(true);
			jTableDictionary.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableDictionary.setAutoCreateRowSorter(true);
			// --- MouseListener hinzufügen ---------------
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
						if (jPopupMenuDictionary== null) {
							getJPopupMenuDictionary();
						}
						jPopupMenuDictionary.show(evt.getComponent(), evt.getX(), evt.getY());
					}
				}
			});
			// --- SelectionListener hinzufügen -----------
			jTableDictionary.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent eve) {
					int currSelection = jTableDictionary.getSelectedRow();
					if (currSelection>-1) {
						// --- provide dataset  -----------
						setCurrentDataSet(currSelection);
					}
				}
			});
			
		}
		return jTableDictionary;
	}
	
	/**
	 * This method initializes jPopupMenuDictionary	
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
	 * This method initializes jMenuItemDelete	
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
	 * This method initializes jMenuItemEdit	
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
	 * This method initializes jContentPane
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
	 * This method initializes jTabbedPane	
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
	 * This method initializes jPanelTranslation	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelTranslation() {
		if (jPanelTranslation == null) {
			
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
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.gridwidth = 7;
			gridBagConstraints11.insets = new Insets(0, 10, 10, 10);
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
			gridBagConstraints1.weighty = 1.0;
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
			gridBagConstraints4.weighty = 1.0;
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
		}
		return jPanelTranslation;
	}

	/**
	 * This method initializes jScrollPaneDictionary	
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
	 * This method initializes jPanelSouth	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSouth() {
		if (jPanelSouth == null) {
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.insets = new Insets(10, 10, 15, 10);
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.insets = new Insets(10, 10, 15, 10);
			gridBagConstraints31.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(10, 10, 15, 10);
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.gridx = 1;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			jPanelSouth = new JPanel();
			jPanelSouth.setLayout(new GridBagLayout());
			jPanelSouth.add(getJPanelSouthWest(), gridBagConstraints31);
			jPanelSouth.add(getJPaneFooter(), gridBagConstraints);
			jPanelSouth.add(getJPanelEast(), gridBagConstraints41);
		}
		return jPanelSouth;
	}

	/**
	 * This method initializes jPaneFooter	
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
	 * This method initializes jButtonClose	
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
	 * This method initializes jButtonImportCSV	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonImportCSV() {
		if (jButtonImportCSV == null) {
			jButtonImportCSV = new JButton();
			jButtonImportCSV.setToolTipText(Language.translate("'.csv'-Version des Wörterbuchs übernehmen ..."));
			jButtonImportCSV .setIcon(new ImageIcon(getClass().getResource(PathImage + "MBtransImport.png")));
			jButtonImportCSV.setPreferredSize(new Dimension(26, 26));
			jButtonImportCSV.addActionListener(this);
		}
		return jButtonImportCSV;
	}

	/**
	 * This method initializes jPanelSouthWest	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSouthWest() {
		if (jPanelSouthWest == null) {
			jPanelSouthWest = new JPanel();
			jPanelSouthWest.setLayout(new GridBagLayout());
			jPanelSouthWest.add(getJButtonImportCSV(), new GridBagConstraints());
			
		}
		return jPanelSouthWest;
	}
	/**
	 * This method initializes jPanelEast	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelEast() {
		if (jPanelEast == null) {
			jPanelEast = new JPanel();
			jPanelEast.setLayout(new GridBagLayout());
			jPanelEast.setPreferredSize(new Dimension(26, 26));
		}
		return jPanelEast;
	}
	
	/**
	 * This method initializes jScrollPaneDictionary	
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
	 * This method initializes jTextFieldSource	
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
	 * This method initializes jScrollPaneDictionary	
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
	 * This method initializes jTextFieldDestination	
	 * @return javax.swing.JTextField	
	 */
	private JTextPane getJTextFieldDestination() {
		if (jTextFieldDestination == null) {
			jTextFieldDestination = new JTextPane();
		}
		return jTextFieldDestination;
	}

	/**
	 * This method initializes jComboBoxSourceLang	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxSourceLang() {
		if (jComboBoxSourceLang == null) {
			jComboBoxSourceLang = new JComboBox();
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
	 * This method initializes jComboBoxDestinationLang	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxDestinationLang() {
		if (jComboBoxDestinationLang == null) {
			jComboBoxDestinationLang = new JComboBox();
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
	 * This method initializes jScrollPanelGoogle	
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
	 * This method initializes jTextAreaGoogle	
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
	 * This method initializes jButtonNextDS	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonNextDS() {
		if (jButtonNextDS == null) {
			jButtonNextDS = new JButton();
			jButtonNextDS.setPreferredSize(new Dimension(26, 26));
			jButtonNextDS.setToolTipText("Nächster Datensatz (STRG+DOWN)");
			jButtonNextDS.setIcon(new ImageIcon(getClass().getResource(PathImage + "ArrowDown.png")));
			jButtonNextDS.addActionListener(this);
		}
		return jButtonNextDS;
	}

	/**
	 * This method initializes jButtonPreviousDS	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonPreviousDS() {
		if (jButtonPreviousDS == null) {
			jButtonPreviousDS = new JButton();
			jButtonPreviousDS.setPreferredSize(new Dimension(26, 26));
			jButtonPreviousDS.setToolTipText("Voriger Datensatz (STRG+UP)");
			jButtonPreviousDS.setIcon(new ImageIcon(getClass().getResource(PathImage + "ArrowUp.png")));
			jButtonPreviousDS.addActionListener(this);
		}
		return jButtonPreviousDS;
	}
	
	/**
	 * This method initializes jButtonDelete	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDelete() {
		if (jButtonDelete == null) {
			jButtonDelete = new JButton();
			jButtonDelete.setPreferredSize(new Dimension(26, 26));
			jButtonDelete.setToolTipText("Datensatz löschen (STRG+DELETE)");
			jButtonDelete.setIcon(new ImageIcon(getClass().getResource(PathImage + "Delete.png")));
			jButtonDelete.addActionListener(this);
		}
		return jButtonDelete;
	}
	/**
	 * This method initializes jButtonSave	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSave() {
		if (jButtonSave == null) {
			jButtonSave = new JButton();
			jButtonSave.setPreferredSize(new Dimension(26, 26));
			jButtonSave.setToolTipText("Übersetzung speichern (STRG+S)");
			jButtonSave.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBsave.png")));
			jButtonSave.addActionListener(this);
		}
		return jButtonSave;
	}

	/**
	 * This method initializes jButtonFindGap	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonFindGap() {
		if (jButtonFindGap == null) {
			jButtonFindGap = new JButton();
			jButtonFindGap.setPreferredSize(new Dimension(26, 26));
			jButtonFindGap.setToolTipText("Suche nach der nächsten fehlenden Übersetzung (STRG+F)");
			jButtonFindGap.setIcon(new ImageIcon(getClass().getResource(PathImage + "Search.png")));
			jButtonFindGap.addActionListener(this);
		}
		return jButtonFindGap;
	}

	/**
	 * This method initializes jButtonGoogleTake	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonGoogleTake() {
		if (jButtonGoogleTake == null) {
			jButtonGoogleTake = new JButton();
			jButtonGoogleTake.setPreferredSize(new Dimension(26, 26));
			jButtonGoogleTake.setToolTipText("Google-Übersetzung übernehmen (STRG+G)");
			jButtonGoogleTake.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBgoogle.png")));
			jButtonGoogleTake.addActionListener(this);
		}
		return jButtonGoogleTake;
	}

	/**
	 * Sets the current dataset to the specfied row 
	 * @param rowNumber
	 */
	private void setCurrentDataSet(int rowNumber) {
		
		jTableDictionary.setRowSelectionInterval(rowNumber, rowNumber);
		
		// --- provide dataset  ---------------------------
		Vector<?> dataVectorRow = (Vector<?>) dictData.getDataVector().elementAt(rowNumber);
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
	 * Set the focus to the directed dataset
	 * @param direction
	 */
	private void move2Dataset(int direction) {
				
		int currSelection = 0;
		if (currDataSet!=null) {
			currSelection = jTableDictionary.getSelectedRow() + direction;
		}
		if (currSelection < 0 || currSelection >= dictData.getRowCount() ) {
			return;
		}
		this.setCurrentDataSet(currSelection);
	}
	
	/**
	 * Shows and displays the next translation gap
	 */
	private void findNextTranslationGap() {
		
		int searchColumn = jComboBoxDestinationLang.getSelectedIndex() + 1;
		
		for (int searchRow=0;searchRow<dictData.getRowCount();searchRow++) {
			
			Vector<?> lineVector = (Vector<?>) dictData.getDataVector().elementAt(searchRow);
			String element = (String) lineVector.get(searchColumn);
			
			if (element==null || element.equals("")) {
				this.setCurrentDataSet(searchRow);
				break;
			}
		}
	}
	
	/**
	 * Delete the current dataset
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
			dictData.removeRow(row);
			jTableDictionary.setRowSelectionInterval(row-1, row-1);
			
		}
	}
	
	/**
	 * Save the current dataset
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
		dictData.setValueAt(textEdited, rowEdited, colEdited+1);
		
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
	 * Translate the given text by using the Google-Translate-API
	 */
	private void setGoogleTranslation() {
		
		String langSource = ((LanguageListElement) jComboBoxSourceLang.getSelectedItem()).getLangShort();
		String langDestin = ((LanguageListElement) jComboBoxDestinationLang.getSelectedItem()).getLangShort();
		String text2Translate = jTextFieldSource.getText();
		
		langSource = langSource.replace("LANG_", "").toLowerCase();
		langDestin = langDestin.replace("LANG_", "").toLowerCase();
		
		com.google.api.translate.Language langGoogleSource = com.google.api.translate.Language.fromString(langSource);
		com.google.api.translate.Language langGoogleDestin = com.google.api.translate.Language.fromString(langDestin);
		
		String translation = null; 
		try {
			translation = Translate.execute(text2Translate, langGoogleSource, langGoogleDestin);
		} catch (Exception e) {
			translation = "ERROR: " + e.getLocalizedMessage();
			//e.printStackTrace();
		}
		jTextAreaGoogle.setText(translation);
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent ae) {

		Object trigger = ae.getSource();
		if (trigger == jButtonClose) {
			this.setVisible(false);
			
		} else if (trigger == jButtonImportCSV) {
			Language.useCSVDictionaryFile();
			
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
			
		} else {
			System.out.println("Unknown Action-Event" + ae.getActionCommand() );
		}
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
