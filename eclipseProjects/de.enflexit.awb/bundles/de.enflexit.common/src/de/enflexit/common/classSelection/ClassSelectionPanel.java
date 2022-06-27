package de.enflexit.common.classSelection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import de.enflexit.common.classLoadService.BaseClassLoadServiceUtility;
import de.enflexit.common.images.ImageProvider;
import de.enflexit.common.images.ImageProvider.ImageFile;


/**
 * This extended JPanel can be used in order to select a class that extends a given super class.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ClassSelectionPanel extends JPanel {

	private static final long serialVersionUID = 2L;
	
	private Vector<ClassSelectionListener> classSelectionListener;
	
	private ImageIcon imageGreen = ImageProvider.getImageIcon(ImageFile.MB_CheckGreen_PNG);
	private ImageIcon imageRed   = ImageProvider.getImageIcon(ImageFile.MB_CheckRed_PNG);
	
	private JLabel jLabelCustomize;
	private JTextField jTextFieldCustomizeClass;

	private JButton jButtonCheckClass;
	private JButton jButtonDefaultClass;
	private JButton jButtonSetNull;
	
	private JPanel jPanelProceed;
	private JButton jButtonOK;
	private JButton jButtonCancel;
	private JLabel jLabelSearchCaption;
	private JTextField jTextFieldSearch;
	private JButton jButtonTakeSelected;
	private JListClassSearcher jListClassesFound;
	
	private Class<?> class2Search4 = Object.class;
	private String class2Search4CurrentValue;
	private String class2Search4DefaultValue;
	private String class2Search4Description = "Current Environment Classes:"; 
	private boolean allowNull = false;
	
	private boolean canceled = false;
	private boolean validClass = false;
	private String classSelected; 


	/**
	 * Default constructor.
	 */
	public ClassSelectionPanel() {
		this.initialize();
	}

	/**
	 * Constructor to configure the type of class, we are looking for.
	 *
	 * @param clazz2Search4 the clazz2 search4
	 * @param clazz2Search4CurrentValue the clazz2 search4 current value
	 * @param clazz2Search4DefaultValue the clazz2 search4 default value
	 * @param clazz2Search4Description the clazz2 search4 description
	 * @param allowNull the allow null
	 */
	public ClassSelectionPanel(Class<?> clazz2Search4, String clazz2Search4CurrentValue, String clazz2Search4DefaultValue, String clazz2Search4Description, boolean allowNull) {
		this.class2Search4 = clazz2Search4;
		this.class2Search4CurrentValue = clazz2Search4CurrentValue;
		this.class2Search4DefaultValue = clazz2Search4DefaultValue;
		this.class2Search4Description = clazz2Search4Description;
		this.setAllowNull(allowNull);
		this.initialize();
		
	}

	/**
	 * Constructor to configure the type of class, we are looking for.
	 *
	 * @param jListClassSearcher an actual instance of a {@link JListClassSearcher}
	 * @param clazz2Search4CurrentValue the clazz2 search4 current value
	 * @param clazz2Search4DefaultValue the clazz2 search4 default value
	 * @param clazz2Search4Description the clazz2 search4 description
	 * @param allowNull the allow null
	 */
	public ClassSelectionPanel(JListClassSearcher jListClassSearcher, String clazz2Search4CurrentValue, String clazz2Search4DefaultValue, String clazz2Search4Description, boolean allowNull) {
		this.jListClassesFound = jListClassSearcher;
		this.addDoubleClickEvent2CurrentJListClassSearcher();
		
		this.class2Search4 = jListClassSearcher.getClass2SearchFor();
		this.class2Search4CurrentValue = clazz2Search4CurrentValue;
		this.class2Search4DefaultValue = clazz2Search4DefaultValue;
		this.class2Search4Description = clazz2Search4Description;
		this.setAllowNull(allowNull);
		this.initialize();
	}
	
	/**
	 * Gets the class2 search4.
	 * @return the class2Search4
	 */
	public Class<?> getClass2Search4() {
		return class2Search4;
	}
	/**
	 * Gets the class2 search4 current value.
	 * @return the class2Search4CurrentValue
	 */
	public String getClass2Search4CurrentValue() {
		return class2Search4CurrentValue;
	}
	
	/**
	 * Sets the class2 search4 current value.
	 * @param newClassName the new class2 search4 current value
	 */
	public void setClass2Search4CurrentValue(String newClassName) {
		class2Search4CurrentValue = newClassName;
		this.getJTextFieldCustomizeClass().setText(newClassName);
	}
	/**
	 * Gets the class2 search4 default value.
	 * @return the class2Search4DefaultValue
	 */
	public String getClass2Search4DefaultValue() {
		return class2Search4DefaultValue;
	}
	/**
	 * Gets the class2 search4 description.
	 * @return the class2Search4Description
	 */
	public String getClass2Search4Description() {
		return class2Search4Description;
	}
	
	/**
	 * Checks if null or empty values are allowed.
	 * @return the allowNull
	 */
	public boolean isAllowNull() {
		return allowNull;
	}
	/**
	 * Sets to allow null as a result or not.
	 * @param allowNull the allowNull to set
	 */
	public void setAllowNull(boolean allowNull) {
		this.allowNull = allowNull;
		// --- Update view ------------------
		this.getJButtonSetNull().setEnabled(this.allowNull);
		this.getJButtonSetNull().validate();
		this.getJButtonSetNull().repaint();
	}

	/**
	 * Sets the canceled.
	 * @param canceled the canceled to set
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	/**
	 * Checks if is canceled.
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * Sets the class selected.
	 * @param classSelected the classSelected to set
	 */
	public void setClassSelected(String classSelected) {
		this.classSelected = classSelected;
		this.jTextFieldCustomizeClass.setText(this.classSelected);
	}
	/**
	 * Gets the class selected.
	 * @return the classSelected
	 */
	public String getClassSelected() {
		return classSelected;
	}

	
	/**
	 * This method initializes this dialog.
	 */
	private void initialize() {
	
		this.setSize(730, 600);
		
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.gridx = 3;
		gridBagConstraints13.insets = new Insets(5, 5, 0, 20);
		gridBagConstraints13.gridy = 1;
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 3;
		gridBagConstraints12.insets = new Insets(5, 5, 0, 20);
		gridBagConstraints12.gridy = 5;
		GridBagConstraints gbcJSeparator = new GridBagConstraints();
		gbcJSeparator.gridx = 0;
		gbcJSeparator.fill = GridBagConstraints.HORIZONTAL;
		gbcJSeparator.insets = new Insets(10, 20, 10, 20);
		gbcJSeparator.gridwidth = 4;
		gbcJSeparator.gridy = 3;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.gridy = 5;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.insets = new Insets(5, 20, 0, 0);
		gridBagConstraints3.gridwidth = 3;
		gridBagConstraints3.gridx = 0;
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.anchor = GridBagConstraints.WEST;
		gridBagConstraints21.insets = new Insets(0, 20, 0, 20);
		gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints21.gridwidth = 4;
		gridBagConstraints21.gridy = 4;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.gridy = 6;
		gridBagConstraints11.weightx = 1.0;
		gridBagConstraints11.weighty = 1.0;
		gridBagConstraints11.insets = new Insets(10, 20, 20, 20);
		gridBagConstraints11.gridwidth = 4;
		gridBagConstraints11.gridx = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridwidth = 4;
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
		gridBagConstraints52.insets = new Insets(5, 5, 0, 0);
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

		jLabelSearchCaption = new JLabel();
		jLabelSearchCaption.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelSearchCaption.setText("Search & Select Class extends " + class2Search4.getName() );
		
		jLabelCustomize = new JLabel();
		jLabelCustomize.setText("Klasse mit gewünschter Oberklasse:");
		jLabelCustomize.setText(class2Search4Description + ":");
		jLabelCustomize.setFont(new Font("Dialog", Font.BOLD, 12));
		
		this.setLayout(new GridBagLayout());
		this.add(jLabelCustomize, gridBagConstraints42);
		this.add(this.getJTextFieldCustomizeClass(), gridBagConstraints31);
		this.add(this.getJButtonDefaultClass(), gridBagConstraints52);
		this.add(this.getJButtonCheckClass(), gridBagConstraints28);
		this.add(this.getJPanelProceed(), gridBagConstraints);
		this.add(this.getJListClassesFound(), gridBagConstraints11);
		this.add(jLabelSearchCaption, gridBagConstraints21);
		this.add(this.getJTextFieldSearch(), gridBagConstraints3);
		this.add(new JSeparator(), gbcJSeparator);
		this.add(this.getJButtonTakeSelected(), gridBagConstraints12);
		this.add(this.getJButtonSetNull(), gridBagConstraints13);
		
	}

	/**
	 * This method initializes jTextFieldCustomizeClass.
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
					isValidClass(jTextFieldCustomizeClass);
				}
			});
		}
		return jTextFieldCustomizeClass;
	}
	
	/**
	 * This method initializes jButtonDefaultClass.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonDefaultClass() {
		if (jButtonDefaultClass == null) {
			jButtonDefaultClass = new JButton();
			jButtonDefaultClass.setToolTipText("Reset");
			jButtonDefaultClass.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonDefaultClass.setPreferredSize(new Dimension(45, 26));
			jButtonDefaultClass.setIcon(ImageProvider.getImageIcon(ImageFile.MB_Reset_PNG));
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
	 * This method initializes jButtonSetNull	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSetNull() {
		if (jButtonSetNull == null) {
			jButtonSetNull = new JButton();
			jButtonSetNull.setEnabled(this.isAllowNull());
			jButtonSetNull.setToolTipText("Delete");
			jButtonSetNull.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonSetNull.setPreferredSize(new Dimension(45, 26));
			jButtonSetNull.setIcon(ImageProvider.getImageIcon(ImageFile.MB_Delete_PNG));
			jButtonSetNull.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					jTextFieldCustomizeClass.setText(null);
				}
			});

		}
		return jButtonSetNull;
	}
	
	/**
	 * This method initializes jButtonDefaultClassCustomize.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCheckClass() {
		if (jButtonCheckClass == null) {
			jButtonCheckClass = new JButton();
			jButtonCheckClass.setToolTipText("Check class specification");
			jButtonCheckClass.setPreferredSize(new Dimension(45, 26));
			jButtonCheckClass.setIcon(this.imageGreen);
			jButtonCheckClass.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					isValidClass(jTextFieldCustomizeClass);
				}
			});
		}
		return jButtonCheckClass;
	}
	
	/**
	 * This method checks if a given class reference is valid.
	 *
	 * @param jTextField the j text field
	 * @param jButton the j button
	 * @return true, if is valid class
	 */
	private boolean isValidClass(JTextField jTextField) {
		
		String className = jTextField.getText().trim();
		if (this.class2Search4DefaultValue==null && className.equals("")) {
			// --- If no default value is configured, an empty text field is allowed -----
			this.getJButtonCheckClass().setIcon(this.imageGreen);
			this.setValidClass(true);
			return true;
		} else if (isAllowNull()==true && className.equals("")) {
			this.getJButtonCheckClass().setIcon(this.imageGreen);
			this.setValidClass(true);
			return true;
			
		} else {
			// --- If a default value is configured, there should be a valid class ------ 
			try {
				@SuppressWarnings("unused")
				Class<?> clazz = BaseClassLoadServiceUtility.forName(className);
				this.getJButtonCheckClass().setIcon(this.imageGreen);
				this.setValidClass(true);
				return true;
				
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
				this.getJButtonCheckClass().setIcon(this.imageRed);
			}
		}
		this.setValidClass(false);
		return false;
	}

	/**
	 * Sets the valid class.
	 * @param validClass the new valid class
	 */
	private void setValidClass(boolean validClass) {
		this.validClass = validClass;
	}
	/**
	 * Checks if a valid class was selected.
	 * @return true, if the class is valid or null
	 */
	public boolean isValidClass() {
		return validClass;
	}

	/**
	 * This method initializes jPanelProceed.
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
			jPanelProceed.add(this.getJButtonOK(), gridBagConstraints1);
			jPanelProceed.add(this.getJButtonCancel(), gridBagConstraints2);
		}
		return jPanelProceed;
	}

	/**
	 * This method initializes jButtonCancel.
	 * @return javax.swing.JButton
	 */
	public JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setPreferredSize(new Dimension(120, 26));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setText("Cancel");
			jButtonCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setCanceled(true);
					notifyClassSelectionListenerAboutCancelAction();
				}
			});
		}
		return jButtonCancel;
	}
	/**
	 * This method initializes jButtonOK.
	 * @return javax.swing.JButton
	 */
	public JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setPreferredSize(new Dimension(120, 26));
			jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOK.setForeground(new Color(0, 153, 0));
			jButtonOK.setText("Apply");
			jButtonOK.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					handleOkClick();
				}
			});
		}
		return jButtonOK;
	}
	/**
	 * This method is called by the jButtonOK's ActionListener.
	 */
	public void handleOkClick(){
		if (this.isValidClass(jTextFieldCustomizeClass)) {
			this.setClassSelected(jTextFieldCustomizeClass.getText().trim());
			this.setCanceled(false);
			this.notifyClassSelectionListenerAboutOkAction();
		} else {
			System.out.println("Class not found: '" + this.getJTextFieldCustomizeClass().getText() + "'");						
		}
	}

	/**
	 * This method initializes jTextFieldSearch.
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
	 * the content of the Input-Parameter.
	 *
	 * @param filter4 the filter4
	 */
	private void filterList(String filter4) {
		jListClassesFound.setModelFiltered(filter4);
	}
	
	/**
	 * This method initializes jButtonTakeSelected.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonTakeSelected() {
		if (jButtonTakeSelected == null) {
			jButtonTakeSelected = new JButton();
			jButtonTakeSelected.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonTakeSelected.setPreferredSize(new Dimension(45, 26));
			jButtonTakeSelected.setIcon(ImageProvider.getImageIcon(ImageFile.ARRAOW_Up_PNG));
			jButtonTakeSelected.setToolTipText("Apply selected class");
			jButtonTakeSelected.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (jListClassesFound.isSelectionEmpty()==false) {
						Object selectedValue = jListClassesFound.getSelectedValue();
						if (selectedValue instanceof ClassElement2Display) {
							ClassElement2Display ce2d = (ClassElement2Display) selectedValue;
							jTextFieldCustomizeClass.setText(ce2d.getClassElement());	
						}
						jButtonCheckClass.doClick();
					}
				}
			});
		}
		return jButtonTakeSelected;
	}
	
	/**
	 * This method initializes jListClassesFound.
	 * @return javax.swing.JList
	 */
	private JListClassSearcher getJListClassesFound() {
		if (jListClassesFound == null) {
			jListClassesFound = new JListClassSearcher(this.class2Search4);
			this.addDoubleClickEvent2CurrentJListClassSearcher();
		}
		return jListClassesFound;
	}
	/**
	 * Adds the double click event to the current JListClassSearcher.
	 */
	private void addDoubleClickEvent2CurrentJListClassSearcher() {
		this.getJListClassesFound().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.getJListClassesFound().jListLoading.setToolTipText("Double-Click to select the class !");
		this.getJListClassesFound().jListLoading.addMouseListener( new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2 ) {
					if (jListClassesFound.isSelectionEmpty()==false) {
						jButtonTakeSelected.doClick();
					}
				}
			}
		});
	}

	
	/**
	 * Returns the current {@link ClassSelectionListener}.
	 * @return the class selection listener
	 */
	private Vector<ClassSelectionListener> getClassSelectionListener() {
		if (classSelectionListener==null) {
			classSelectionListener = new Vector<ClassSelectionListener>();
		}
		return classSelectionListener;
	}
	/**
	 * Adds the specified {@link ClassSelectionListener} to this panel.
	 * @param classSelectionListener the class selection listener
	 */
	public void addClassSelectionListener(ClassSelectionListener classSelectionListener) {
		if (this.getClassSelectionListener().contains(classSelectionListener)==false) {
			this.getClassSelectionListener().add(classSelectionListener);
		}
	}
	/**
	 * Removes the specified {@link ClassSelectionListener} from this panel.
	 * @param classSelectionListener the class selection listener
	 */
	public void removeClassSelectionListener(ClassSelectionListener classSelectionListener) {
		this.getClassSelectionListener().remove(classSelectionListener);
	}

	/** Notifies class selection listener about OK action. */
	private void notifyClassSelectionListenerAboutOkAction() {
		String classSelected = this.getClassSelected();
		Vector<ClassSelectionListener> listener = new Vector<ClassSelectionListener>(this.getClassSelectionListener());
		for (ClassSelectionListener csl : listener) {
			csl.setSelectedClass(classSelected);
		}
	}
	/** Notifies class selection listener about CANCEL action. */
	private void notifyClassSelectionListenerAboutCancelAction() {
		Vector<ClassSelectionListener> listener = new Vector<ClassSelectionListener>(this.getClassSelectionListener());
		for (ClassSelectionListener csl : listener) {
			csl.setSelectionCanceled();
		}
	}
	
} 
