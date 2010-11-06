package agentgui.core.gui;

import jade.content.onto.Ontology;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.jade.ClassSearcherSingle;
import agentgui.core.jade.ClassSearcherSingle.ClassSearcherUpdate;

public class OntologieSelector extends JDialog implements Observer, ActionListener{

	private static final long serialVersionUID = 1L;
	private Boolean canceled = false;  //  @jve:decl-index=0:
	private String ontologySelected = null;
	
	private Project currProject;
	private JPanel jContentPane = null;
	private JList jListOntologies = null;
	private JButton jButtonSelect = null;
	private JButton jButtonCancel = null;
	private JScrollPane jScrollPaneOntologie = null;
	private JLabel jLabelOntoList = null;
	private JPanel jPanelShowOptions = null;
	private ButtonGroup buttonGroup = null; 
	private JRadioButton jRadioButtonShowAll = null;	
	private JRadioButton jRadioButtonShowNoneJade = null;
	
	private JLabel jLabelShow = null;
	/**
	 * @param owner
	 */
	public OntologieSelector(Frame owner, String titel, boolean modal, Project project) {
		super(owner, titel, modal);
		currProject = project;
		currProject.addObserver(this);
		
		// --- Dialog aufbauen ---------------------------- 
		initialize();
		// --- Fill List with the found Ontologies --------
		this.showOntologyListModel();
		// --- Anzeigesprache festlegen -------------------
		jLabelOntoList.setText(Language.translate("Auswahl Ontologien"));
		jLabelShow.setText(Language.translate("Ansicht:"));
		jRadioButtonShowAll.setText(Language.translate("Alle Ontologien"));
		jRadioButtonShowNoneJade.setText(Language.translate("Nur Nicht-JADE-Ontologien"));
		jButtonSelect.setText(Language.translate("Hinzufügen"));
		jButtonCancel.setText(Language.translate("Abbrechen"));
		// --- Dialog zentrieren --------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);	
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(561, 389);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				canceled = true;
				setVisible(false);
			}
		});
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.anchor = GridBagConstraints.CENTER;
			gridBagConstraints4.insets = new Insets(5, 20, 7, 20);
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 2;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(20, 20, 5, 0);
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.gridy = 0;
			jLabelOntoList = new JLabel();
			jLabelOntoList.setText("Auswahl Ontologien");
			jLabelOntoList.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.gridwidth = 2;
			gridBagConstraints11.insets = new Insets(0, 20, 5, 20);
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.weightx = 0.5;
			gridBagConstraints2.insets = new Insets(10, 0, 20, 0);
			gridBagConstraints2.gridy = 4;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.weightx = 0.5;
			gridBagConstraints1.insets = new Insets(10, 0, 20, 0);
			gridBagConstraints1.gridy = 4;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJScrollPaneOntologie(), gridBagConstraints11);
			jContentPane.add(getJButtonSelect(), gridBagConstraints1);
			jContentPane.add(getJButtonCancel(), gridBagConstraints2);
			jContentPane.add(jLabelOntoList, gridBagConstraints);
			jContentPane.add(getJPanelShowOptions(), gridBagConstraints4);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jListOntologies	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getJListOntologies() {
		if (jListOntologies == null) {
			jListOntologies = new JList();
			jListOntologies.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListOntologies.addMouseListener( new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if (me.getClickCount() == 2 ) {
						jButtonSelect.doClick();	
					}
				}
			});
		}
		return jListOntologies;
	}

	/**
	 * This method initializes jButtonSelect	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSelect() {
		if (jButtonSelect == null) {
			jButtonSelect = new JButton();
			jButtonSelect.setText("Hinzufügen");
			jButtonSelect.setForeground(new Color(0, 153, 0));
			jButtonSelect.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonSelect.setActionCommand("AddOntology");
			jButtonSelect.setPreferredSize(new Dimension(100, 26));
			jButtonSelect.addActionListener(this);
		}
		return jButtonSelect;
	}

	/**
	 * This method initializes jButtonCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText("Abbrechen");
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setActionCommand("Cancel");
			jButtonCancel.setPreferredSize(new Dimension(100, 26));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	public boolean isCanceled(){
		return canceled;
	}
	/**
	 * @return the ontologySelected
	 */
	public String getNewOntologySelected() {
		return ontologySelected;
	}

	/**
	 * This method initializes jScrollPaneOntologie	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneOntologie() {
		if (jScrollPaneOntologie == null) {
			jScrollPaneOntologie = new JScrollPane();
			jScrollPaneOntologie.setViewportView(getJListOntologies());
		}
		return jScrollPaneOntologie;
	}

	/**
	 * This method initializes jPanelShowOptions	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelShowOptions() {
		if (jPanelShowOptions == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.insets = new Insets(0, 0, 0, 10);
			gridBagConstraints6.gridy = 0;
			jLabelShow = new JLabel();
			jLabelShow.setText("Ansicht:");
			jLabelShow.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.insets = new Insets(0, 0, 0, 10);
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new Insets(0, 0, 0, 10);
			gridBagConstraints3.gridy = -1;
			jPanelShowOptions = new JPanel();
			jPanelShowOptions.setLayout(new GridBagLayout());
			jPanelShowOptions.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanelShowOptions.add(getJRadioButtonShowAll(), gridBagConstraints3);
			jPanelShowOptions.add(getJRadioButtonShowNoneJade(), gridBagConstraints5);
			jPanelShowOptions.add(jLabelShow, gridBagConstraints6);
			// --- Button-Group definieren --------------------------
			buttonGroup = new ButtonGroup();
			buttonGroup.add(jRadioButtonShowAll);
			buttonGroup.add(jRadioButtonShowNoneJade);
		}
		return jPanelShowOptions;
	}

	/**
	 * This method initializes jRadioButtonShowAll	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonShowAll() {
		if (jRadioButtonShowAll == null) {
			jRadioButtonShowAll = new JRadioButton();
			jRadioButtonShowAll.setText("Alle Ontologien");
			jRadioButtonShowAll.setActionCommand("AllOntologies");
			jRadioButtonShowAll.setMnemonic(KeyEvent.VK_UNDEFINED);
			jRadioButtonShowAll.addActionListener(this);
		}
		return jRadioButtonShowAll;
	}
	/**
	 * This method initializes jRadioButtonShowNoneJade	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonShowNoneJade() {
		if (jRadioButtonShowNoneJade == null) {
			jRadioButtonShowNoneJade = new JRadioButton();
			jRadioButtonShowNoneJade.setText("Nur Nicht-JADE-Ontologien");
			jRadioButtonShowNoneJade.setActionCommand("NoneJadeOntologiesOnly");
			jRadioButtonShowNoneJade.setSelected(true);
			jRadioButtonShowNoneJade.addActionListener(this);
		}
		return jRadioButtonShowNoneJade;
	}

	private void showOntologyListModel() {
		
		DefaultListModel jOntoListModel = null;
		String ActCMD = buttonGroup.getSelection().getActionCommand();
		Vector<String> projectOntoList = currProject.ontologies4Project.getAllNoneUsedOntologies();
		
		jOntoListModel = new DefaultListModel();
		for (int i =0; i<projectOntoList.size(); i++) {
			
			String className = projectOntoList.get(i);
			if ( ActCMD == "AllOntologies" ) {
				jOntoListModel.addElement( className );
			} else if ( ActCMD == "NoneJadeOntologiesOnly" ) {
				if ( className.startsWith("jade.")==false ) {
					jOntoListModel.addElement( className );	
				}
			}			
		}
		// --- DefaultListModel auf die Listen-Darstellung anwenden ------
		jListOntologies.setModel(jOntoListModel);
		// --- Jade-Klasse sollen nicht dem Projekt hinzugefügt werden ---
		if ( ActCMD == "AllOntologies" ) {
			jButtonSelect.setEnabled(false);
		} else if ( ActCMD == "NoneJadeOntologiesOnly" ) {
			jButtonSelect.setEnabled(true);
		}	
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String ActCMD = ae.getActionCommand();
		String MsgHead, MsgText;
		
		if ( ActCMD == "AddOntology" ) {
			if ( jListOntologies.getSelectedValue()!=null ) {
				ontologySelected = (String) jListOntologies.getSelectedValue();
				this.setVisible(false);
			} else {
				MsgHead = Language.translate("Fehlende Ontologie-Auswahl !");
				MsgText = Language.translate("Bitte wählen Sie die Ontologie aus, die Ihrem Projekt hinzugefügt werden soll !");			
				JOptionPane.showInternalMessageDialog( this.getContentPane(), MsgText, MsgHead, JOptionPane.WARNING_MESSAGE);
			}
		} else if ( ActCMD == "Cancel" ) {
			this.canceled = true;
			this.setVisible(false);
		} else if ( ActCMD == "AllOntologies" ) {
			this.showOntologyListModel();
		} else if ( ActCMD == "NoneJadeOntologiesOnly" ) {
			this.showOntologyListModel();
		} else {
			System.out.println( "Unknown ActionCommand: " + ActCMD );
		};
	}

	@Override
	public void update(Observable o, Object notifyObject) {
		
		String ObjectName = notifyObject.toString();
		if ( ObjectName.equals(ClassSearcherSingle.classSearcherNotify) ) {
			ClassSearcherUpdate csu = (ClassSearcherUpdate) notifyObject;
			if (csu.getClass2SearchFor().equals(Ontology.class)) {
				// --- Liste der Ontologien aktualisieren -----
				this.showOntologyListModel();				
			}
		}
		
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
