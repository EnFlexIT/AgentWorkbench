package agentgui.core.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import agentgui.core.application.Application;
import agentgui.core.application.Language;

public class Translation extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final String appName = Application.RunInfo.AppTitel();  //  @jve:decl-index=0:
	private final String PathImage = Application.RunInfo.PathImageIntern();  //  @jve:decl-index=0:
	private ImageIcon imageIcon = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUI.png") );
	private Image image = imageIcon.getImage();
	
	private JPanel jContentPane = null;
	private JTabbedPane jTabbedPane = null;
		private JPanel jPanelTranslation = null;
		private JScrollPane jScrollPaneDictionary = null;
			private JTable jTableDictionary = null;
			private int lastDictionarySelection = -1;
			
	private JPanel jPanelSouth = null;
	private JPanel jPaneFooter = null;
	private JButton jButtonClose = null;
	private JLabel jLabelWest = null;
	private JLabel jLabelEast = null;
	private JLabel jLabelNorth = null;
	
	private JLabel jLabelSource = null;
	private JLabel jLabelDestination = null;
	private JScrollPane jScrollPaneTextSource = null;
	private JScrollPane jScrollPaneTextDestination = null;
	private JTextPane jTextFieldSource = null;
	private JTextPane jTextFieldDestination = null;
	private JComboBox jComboBoxSourceLang = null;
	private JComboBox jComboBoxDestinationLang = null;
	
	private JLabel jLabelSelectSourceLang = null;
	private JLabel jLabelSelectDestinationLang = null;
	
	private DefaultTableModel dictData = new DefaultTableModel();
	private DefaultComboBoxModel langSelectionModel = new DefaultComboBoxModel();
	
	private JPopupMenu jPopupMenuDictionary = null;
	private JMenuItem jMenuItemDelete = null;
	private JMenuItem jMenuItemEdit = null;

	private JPanel jPanelGoogle = null;
	private JButton jButtonAskGoogle = null;


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
		this.setLanguageComboModel();
		
		// --- Configure PopUp-menue -------------------------------- 
		this.getJPopupMenuDictionary();
		
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
		jMenuItemEdit.setText(Language.translate("Bearbeiten"));
		
	}

	/**
	 * This method will set the data model for the Language selection
	 */
	private void setLanguageComboModel() {
		String[] languages = Language.getLanguages();
		for (int i = 0; i < languages.length; i++) {
			String lang = Language.getLanguageName(languages[i]) ;
			this.langSelectionModel.addElement(lang);
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
			this.dictData.addColumn(Language.getLanguageName(lang));	
		}
		
		// --- Rows of the dictionary -------------------------------
		int rowNo = 0;
		List<String> dictContent = Language.getDictLineList();
		for (String dictLine : dictContent) {
			
			Vector<Object> rowData = new Vector<Object>(); 
			rowData.addAll(Arrays.asList(dictLine.split(Language.Seperator, -1)));
			if (rowData.get(0).equals("LANG_DE")==false) {
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
				
				// --- maybe correct value --------------------------
				Object cellValue = dictData.getValueAt(rowChanged, colChanged);
				String checkValue = new String(cellValue.toString());
				if (checkValue.trim().equals(cellValue.toString())==false) {
					dictData.setValueAt(checkValue.trim(), rowChanged, colChanged);
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
						dictRow += Language.Seperator + rowData.get(i);
					}
				}
				
				// --- update the dictionary ------------------------
				Language.update(deExp, dictRow);
				
			}
		});
		
		// ----------------------------------------------------------
		// --- Set layout of the table ------------------------------
		TableColumn tbCol = this.jTableDictionary.getColumnModel().getColumn(0);
		tbCol.setPreferredWidth(35);
		tbCol.setMinWidth(35);
		tbCol.setMaxWidth(45);
		
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
					if (currSelection!=lastDictionarySelection) {
						// --- remind selection -----------
						lastDictionarySelection = currSelection;
						// --- provide dataset  -----------
						System.out.println("Aktueller DS: " + jTableDictionary.getSelectedRow());
						
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
			jMenuItemEdit.setText("Bearbeiten");
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
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.fill = GridBagConstraints.BOTH;
			gridBagConstraints9.weighty = 1.0;
			gridBagConstraints9.insets = new Insets(20, 10, 0, 0);
			gridBagConstraints9.gridwidth = 3;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.gridy = 4;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.insets = new Insets(20, 20, 5, 0);
			gridBagConstraints8.gridy = 2;
			jLabelSelectDestinationLang = new JLabel();
			jLabelSelectDestinationLang.setText("Sprache:");
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.insets = new Insets(10, 20, 5, 0);
			gridBagConstraints7.gridy = 0;
			jLabelSelectSourceLang = new JLabel();
			jLabelSelectSourceLang.setText("Sprache:");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.NONE;
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.insets = new Insets(20, 5, 5, 0);
			gridBagConstraints6.gridx = 2;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.NONE;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.insets = new Insets(10, 5, 5, 0);
			gridBagConstraints5.gridx = 2;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.gridy = 3;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.insets = new Insets(0, 10, 0, 10);
			gridBagConstraints4.gridwidth = 3;
			gridBagConstraints4.gridx = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(20, 10, 5, 0);
			gridBagConstraints3.gridy = 2;
			jLabelDestination = new JLabel();
			jLabelDestination.setText("Übersetzter Text ");
			jLabelDestination.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(10, 10, 5, 0);
			gridBagConstraints2.gridy = 0;
			jLabelSource = new JLabel();
			jLabelSource.setText("Text zur Übersetzung");
			jLabelSource.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new Insets(0, 10, 0, 10);
			gridBagConstraints1.gridwidth = 3;
			gridBagConstraints1.gridx = 0;
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
			jPanelTranslation.add(getJPanelGoogle(), gridBagConstraints9);
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
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(10, 10, 15, 10);
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			jPanelSouth = new JPanel();
			jPanelSouth.setLayout(new GridBagLayout());
			jPanelSouth.add(getJPaneFooter(), gridBagConstraints);
		}
		return jPanelSouth;
	}

	/**
	 * This method initializes jPaneFooter	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPaneFooter() {
		if (jPaneFooter == null) {
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
	 * This method initializes jScrollPaneDictionary	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneTextSource() {
		if (jScrollPaneTextSource == null) {
			jScrollPaneTextSource = new JScrollPane();
			jScrollPaneTextSource.setPreferredSize(new Dimension(40, 52));
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
			jScrollPaneTextDestination.setPreferredSize(new Dimension(40, 52));
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
			jComboBoxSourceLang.setModel(langSelectionModel);
			jComboBoxSourceLang.setPreferredSize(new Dimension(200, 26));
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
			jComboBoxDestinationLang.setModel(langSelectionModel);
			jComboBoxDestinationLang.setPreferredSize(new Dimension(200, 26));
		}
		return jComboBoxDestinationLang;
	}

	/**
	 * This method initializes jPanelGoogle	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelGoogle() {
		if (jPanelGoogle == null) {
			jPanelGoogle = new JPanel();
			jPanelGoogle.setLayout(new GridBagLayout());
			jPanelGoogle.add(getJButtonAskGoogle(), new GridBagConstraints());
		}
		return jPanelGoogle;
	}

	/**
	 * This method initializes jButtonAskGoogle	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAskGoogle() {
		if (jButtonAskGoogle == null) {
			jButtonAskGoogle = new JButton();
		}
		return jButtonAskGoogle;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		Object trigger = ae.getSource();
		if (trigger == jButtonClose) {
			this.setVisible(false);
		} else if (trigger == jMenuItemEdit) {
			jTabbedPane.setSelectedComponent(jPanelTranslation);
			
		} else if (trigger == jMenuItemDelete) {
			
		} else {
			System.out.println("Unknown Action-Event" + ae.getActionCommand() );
		}
		
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
