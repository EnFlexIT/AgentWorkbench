package agentgui.core.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.components.ClassElement2Display;
import agentgui.core.gui.components.JListClassSearcher;
import agentgui.core.gui.components.JListWithProgressBar;
import agentgui.core.jade.ClassSearcherSingle;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

public class ClassSelector extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private final String PathImage = Application.RunInfo.PathImageIntern();  //  @jve:decl-index=0:
	private ImageIcon imageIcon = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUI.png") );
	private Image image = imageIcon.getImage();  //  @jve:decl-index=0:
	
	private JPanel jContentPane = null;
	private JLabel jLabelCustomize = null;
	private JTextField jTextFieldCustomizeClass = null;
	private JButton jButtonDefaultClass = null;
	private JButton jButtonCheckClass = null;

	private JPanel jPanelProceed = null;
	private JButton jButtonOK = null;
	private JButton jButtonCancel = null;
	
	private JLabel jLabelSeperator = null;

	private JLabel jLabelSearchCaption = null;
	private JTextField jTextFieldSearch = null;
	private JButton jButtonTakeSelected = null;
	private JListClassSearcher jListClassesFound = null;
	
	private Class<?> class2Search4 = Object.class;  //  @jve:decl-index=0:
	private String class2Search4CurrentValue = null;  //  @jve:decl-index=0:
	private String class2Search4DefaultValue = null;  //  @jve:decl-index=0:
	private String class2Search4Description = "Klassen der aktuellen Laufzeitumgebung.";  //  @jve:decl-index=0:

	private ClassSearcherSingle css = null;
	
	private boolean canceled = false;
	private String classSelected = null;  //  @jve:decl-index=0:


	/**
	 * Default constructor 
	 * @param owner
	 */
	public ClassSelector(Frame owner) {
		super(owner);
		this.initialize();
	}

	/**
	 * Constructor to configure the type of class, we are looking for 
	 * @param owner
	 * @param clazz2Search4
	 * @param clazz2Search4CurrentValue
	 * @param clazz2Search4DefaultValue
	 * @param clazz2Search4Description
	 */
	public ClassSelector(Frame owner, Class<?> clazz2Search4, String clazz2Search4CurrentValue, String clazz2Search4DefaultValue, String clazz2Search4Description) {
		super(owner);
		
		this.class2Search4 = clazz2Search4;
		this.class2Search4CurrentValue = clazz2Search4CurrentValue;
		this.class2Search4DefaultValue = clazz2Search4DefaultValue;
		this.class2Search4Description = clazz2Search4Description;
		
		this.initialize();
	}

	/**
	 * @return the class2Search4
	 */
	public Class<?> getClass2Search4() {
		return class2Search4;
	}
	/**
	 * @return the class2Search4CurrentValue
	 */
	public String getClass2Search4CurrentValue() {
		return class2Search4CurrentValue;
	}

	/**
	 * @return the class2Search4DefaultValue
	 */
	public String getClass2Search4DefaultValue() {
		return class2Search4DefaultValue;
	}
	/**
	 * @return the class2Search4Description
	 */
	public String getClass2Search4Description() {
		return class2Search4Description;
	}
	/**
	 * @param canceled the canceled to set
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	/**
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * @param classSelected the classSelected to set
	 */
	public void setClassSelected(String classSelected) {
		this.classSelected = classSelected;
	}
	/**
	 * @return the classSelected
	 */
	public String getClassSelected() {
		return classSelected;
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible==false) {
			// --- Stop the class search ------------------
			this.css.stopSearch();	
		}
		super.setVisible(visible);
	}
	
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
	
		// --- Die Suche nach den möglichen Klassen starten ---------
		this.css = new ClassSearcherSingle(class2Search4);
		this.css.startSearch();
		
		this.setSize(622, 580);
		this.setContentPane(getJContentPane());
		this.setTitle("Agent.GUI: Class-Selector");
		this.setIconImage(image);
		this.setModal(true);		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				setCanceled(true);
				setVisible(false);
			}
		});
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);
		
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 2;
			gridBagConstraints12.insets = new Insets(5, 5, 0, 20);
			gridBagConstraints12.gridy = 5;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new Insets(10, 20, 10, 20);
			gridBagConstraints5.gridwidth = 3;
			gridBagConstraints5.gridy = 3;
			jLabelSeperator = new JLabel();
			jLabelSeperator.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jLabelSeperator.setText(" ");
			jLabelSeperator.setPreferredSize(new Dimension(200, 2));
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 5;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new Insets(5, 20, 0, 0);
			gridBagConstraints3.gridwidth = 2;
			gridBagConstraints3.gridx = 0;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.anchor = GridBagConstraints.WEST;
			gridBagConstraints21.insets = new Insets(0, 20, 0, 20);
			gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.gridwidth = 3;
			gridBagConstraints21.gridy = 4;
			jLabelSearchCaption = new JLabel();
			jLabelSearchCaption.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelSearchCaption.setText("Suche");
			jLabelSearchCaption.setText(Language.translate(jLabelSearchCaption.getText()) + ": Class extends " + class2Search4.getName() );
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 6;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.insets = new Insets(10, 20, 20, 20);
			gridBagConstraints11.gridwidth = 3;
			gridBagConstraints11.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridwidth = 3;
			gridBagConstraints.insets = new Insets(10, 20, 0, 20);
			gridBagConstraints.gridy = 2;
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.gridx = 1;
			gridBagConstraints28.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints28.gridy = 1;
			GridBagConstraints gridBagConstraints52 = new GridBagConstraints();
			gridBagConstraints52.fill = GridBagConstraints.NONE;
			gridBagConstraints52.gridx = 2;
			gridBagConstraints52.gridy = 1;
			gridBagConstraints52.insets = new Insets(5, 5, 0, 20);
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.anchor = GridBagConstraints.WEST;
			gridBagConstraints31.insets = new Insets(5, 20, 0, 0);
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.gridy = 1;
			gridBagConstraints31.weightx = 1.0;
			gridBagConstraints31.fill = GridBagConstraints.BOTH;
			GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
			gridBagConstraints42.anchor = GridBagConstraints.WEST;
			gridBagConstraints42.gridy = 0;
			gridBagConstraints42.gridwidth = 3;
			gridBagConstraints42.insets = new Insets(20, 20, 0, 0);
			gridBagConstraints42.gridx = 0;
			jLabelCustomize = new JLabel();
			jLabelCustomize.setText("Klasse mit gewünschter Oberklasse:");
			jLabelCustomize.setText(class2Search4Description + ":");
			jLabelCustomize.setFont(new Font("Dialog", Font.BOLD, 12));
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(jLabelCustomize, gridBagConstraints42);
			jContentPane.add(getJTextFieldCustomizeClass(), gridBagConstraints31);
			jContentPane.add(getJButtonDefaultClass(), gridBagConstraints52);
			jContentPane.add(getJButtonDefaultClassCustomize(), gridBagConstraints28);
			jContentPane.add(getJPanelProceed(), gridBagConstraints);
			jContentPane.add(getJListClassesFound(), gridBagConstraints11);
			jContentPane.add(jLabelSearchCaption, gridBagConstraints21);
			jContentPane.add(getJTextFieldSearch(), gridBagConstraints3);
			jContentPane.add(jLabelSeperator, gridBagConstraints5);
			jContentPane.add(getJButtonTakeSelected(), gridBagConstraints12);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTextFieldCustomizeClass	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldCustomizeClass() {
		if (jTextFieldCustomizeClass == null) {
			jTextFieldCustomizeClass = new JTextField();
			jTextFieldCustomizeClass.setText(class2Search4CurrentValue);
			jTextFieldCustomizeClass.setPreferredSize(new Dimension(400, 26));
			jTextFieldCustomizeClass.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent kR) {
					super.keyReleased(kR);
					isValidClass(jTextFieldCustomizeClass, jButtonCheckClass);
				}
			});
		}
		return jTextFieldCustomizeClass;
	}
	/**
	 * This method initializes jButtonDefaultClass	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDefaultClass() {
		if (jButtonDefaultClass == null) {
			jButtonDefaultClass = new JButton();
			jButtonDefaultClass.setToolTipText(Language.translate("Agent.GUI - Standard verwenden"));			
			jButtonDefaultClass.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonDefaultClass.setPreferredSize(new Dimension(45, 26));
			jButtonDefaultClass.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBreset.png")));
			jButtonDefaultClass.setToolTipText(Language.translate("Zurücksetzen"));
			jButtonDefaultClass.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					jTextFieldCustomizeClass.setText(class2Search4DefaultValue);
					jButtonCheckClass.doClick();
				}
			});
		}
		return jButtonDefaultClass;
	}
	
	/**
	 * This method initializes jButtonDefaultClassCustomize	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDefaultClassCustomize() {
		if (jButtonCheckClass == null) {
			jButtonCheckClass = new JButton();
			jButtonCheckClass.setToolTipText(Language.translate("Klassenangabe überprüfen"));
			jButtonCheckClass.setPreferredSize(new Dimension(45, 26));
			jButtonCheckClass.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBcheckGreen.png")));
			jButtonCheckClass.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					isValidClass(jTextFieldCustomizeClass, jButtonCheckClass);
				}
			});
		}
		return jButtonCheckClass;
	}
	
	/**
	 * This method checks if a given classs reference is valid
	 * @param className: reference to the class  
	 * @param classType: static(0) or dynamic(1)
	 */
	private boolean isValidClass(JTextField jTextField, JButton jButton) {
		
		String className = jTextField.getText().trim();
		if (this.class2Search4DefaultValue==null && className.equals("")) {
			// --- If no default value is configured, an empty textfield is allowed -----
			jButton.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBcheckGreen.png")));
			return true;
		
		} else {
			// --- If a default value is configured, there should be a valid class ------ 
			try {
				@SuppressWarnings("unused")
				Class<?> clazz = Class.forName(className);
				jButton.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBcheckGreen.png")));
				return true;
				
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
				jButton.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBcheckRed.png")));
			}
		}
		return false;
	}

	/**
	 * This method initializes jPanelProceed	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelProceed() {
		if (jPanelProceed == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(0, 30, 0, 0);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(0, 0, 0, 30);
			jPanelProceed = new JPanel();
			jPanelProceed.setLayout(new GridBagLayout());
			jPanelProceed.add(getJButtonOK(), gridBagConstraints1);
			jPanelProceed.add(getJButtonCancel(), gridBagConstraints2);
		}
		return jPanelProceed;
	}

	/**
	 * This method initializes jButtonOK	
	 * @return javax.swing.JButton	
	 */
	JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setPreferredSize(new Dimension(120, 26));
			jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOK.setForeground(new Color(0, 153, 0));
			jButtonOK.setText("Übernehmen");
			jButtonOK.setText(Language.translate(jButtonOK.getText()));
			jButtonOK.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
//					System.out.println("ClassSelector Listener");
//					if (isValidClass(jTextFieldCustomizeClass, jButtonCheckClass)) {
//						setClassSelected(jTextFieldCustomizeClass.getText().trim());
//						setCanceled(false);
//						setVisible(false);
//					} else {
//						System.out.println(Language.translate("Class not found:") + " '" + jTextFieldCustomizeClass.getText() + "'");						
//					}
					handleOkClick();
				}
			});
		}
		return jButtonOK;
	}
	
	/**
	 * This method is called by the jButtonOK's ActionListener
	 */
	void handleOkClick(){
		if (isValidClass(jTextFieldCustomizeClass, jButtonCheckClass)) {
			setClassSelected(jTextFieldCustomizeClass.getText().trim());
			setCanceled(false);
			setVisible(false);
		} else {
			System.out.println(Language.translate("Class not found:") + " '" + jTextFieldCustomizeClass.getText() + "'");						
		}
	}

	/**
	 * This method initializes jButtonCancel	
	 * @return javax.swing.JButton	
	 */
	JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setPreferredSize(new Dimension(120, 26));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setText("Abbruch");
			jButtonCancel.setText(Language.translate(jButtonCancel.getText()));
			jButtonCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setCanceled(true);
					setVisible(false);
				}
			});
		}
		return jButtonCancel;
	}

	/**
	 * This method initializes jTextFieldSearch	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldSearch() {
		if (jTextFieldSearch == null) {
			jTextFieldSearch = new JTextField();
			jTextFieldSearch.setPreferredSize(new Dimension(250, 26));
			jTextFieldSearch.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(java.awt.event.KeyEvent e) {
					filterList( jTextFieldSearch.getText());
				}
			});
		}
		return jTextFieldSearch;
	}
	/**
	 * This will filter the list of classes depending on 
	 * the content of the Input-Parameter 
	 * @param filter4
	 */
	private void filterList(String filter4) {
		jListClassesFound.setModelFiltered(filter4);
	}
	
	/**
	 * This method initializes jButtonTakeSelected	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonTakeSelected() {
		if (jButtonTakeSelected == null) {
			jButtonTakeSelected = new JButton();
			jButtonTakeSelected.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonTakeSelected.setPreferredSize(new Dimension(45, 26));
			jButtonTakeSelected.setIcon(new ImageIcon(getClass().getResource(PathImage + "ArrowUp.png")));
			jButtonTakeSelected.setToolTipText(Language.translate("Ausgewählte Klasse übernehmen!"));
			jButtonTakeSelected.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (jListClassesFound.isSelectionEmpty()==false) {
						ClassElement2Display ce2d = (ClassElement2Display) jListClassesFound.getSelectedValue();
						jTextFieldCustomizeClass.setText(ce2d.toString());
						jButtonCheckClass.doClick();
					}
				}
			});
		}
		return jButtonTakeSelected;
	}
	
	/**
	 * This method initializes jListClassesFound	
	 * @return javax.swing.JList	
	 */
	private JListWithProgressBar getJListClassesFound() {
		if (jListClassesFound == null) {
			jListClassesFound = new JListClassSearcher(this.class2Search4, this.css);
			jListClassesFound.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListClassesFound.jListLoading.setToolTipText(Language.translate("Doppelklick um eine Klasse auszuwählen !"));
			jListClassesFound.jListLoading.addMouseListener( new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if (me.getClickCount() == 2 ) {
						if (jListClassesFound.isSelectionEmpty()==false) {
							jButtonTakeSelected.doClick();
						}
					}
				}
			});
		}
		return jListClassesFound;
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
