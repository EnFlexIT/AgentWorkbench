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
package org.agentgui.bundle.classSelection;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import agentgui.core.agents.AgentClassElement;
import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.core.config.GlobalInfo;
import agentgui.core.gui.components.ClassElement2Display;


/**
 * This class represents the dialog to select a specific 
 * class that extends a given super class
 *  * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ClassSelector extends JDialog {

	private static final long serialVersionUID = 1674830519185536173L;

	private JPanel jContentPane;
	private JLabel jLabelCustomize;
	private JTextField jTextFieldCustomizeClass;

	private JButton jButtonCheckClass;
	private JButton jButtonDefaultClass;
	private JButton jButtonSetNull;
	
	private JPanel jPanelProceed;
	private JButton jButtonOK;
	private JButton jButtonCancel;
	private JLabel jLabelSeperator;
	private JLabel jLabelSearchCaption;
	private JTextField jTextFieldSearch;
	private JButton jButtonTakeSelected;
	private JListClassSearcher jListClassesFound;
	
	private Class<?> superClass2Search4 = Object.class;
	private String class2Search4CurrentValue;
	private String class2Search4DefaultValue;
	private String class2Search4Description = "Klassen der aktuellen Laufzeitumgebung.";
	private boolean allowNull = false;
	
	private boolean canceled = false;
	private boolean validClass = false;
	private String classSelected;


	/**
	 * Default constructor.
	 * @param owner the owner
	 */
	public ClassSelector(Frame owner) {
		super(owner);
		this.initialize();
	}

	/**
	 * Constructor to configure the type of class, we are looking for.
	 *
	 * @param owner the owner
	 * @param clazz2Search4 the clazz2 search4
	 * @param clazz2Search4CurrentValue the clazz2 search4 current value
	 * @param clazz2Search4DefaultValue the clazz2 search4 default value
	 * @param clazz2Search4Description the clazz2 search4 description
	 */
	public ClassSelector(Frame owner, Class<?> clazz2Search4, String clazz2Search4CurrentValue, String clazz2Search4DefaultValue, String clazz2Search4Description, boolean allowNull) {
		super(owner);
		this.superClass2Search4 = clazz2Search4;
		this.class2Search4CurrentValue = clazz2Search4CurrentValue;
		this.class2Search4DefaultValue = clazz2Search4DefaultValue;
		this.class2Search4Description = clazz2Search4Description;
		this.setAllowNull(allowNull);
		this.initialize();
		
	}
	/**
	 * Constructor to configure the type of class, we are looking for.
	 *
	 * @param ownerFrame the owner
	 * @param jListClassSearcher an actual instance of a {@link JListClassSearcher}
	 * @param clazz2Search4CurrentValue the clazz2 search4 current value
	 * @param clazz2Search4DefaultValue the clazz2 search4 default value
	 * @param clazz2Search4Description the clazz2 search4 description
	 * @param allowNull the allow null
	 */
	public ClassSelector(Frame ownerFrame, JListClassSearcher jListClassSearcher, String clazz2Search4CurrentValue, String clazz2Search4DefaultValue, String clazz2Search4Description, boolean allowNull) {
		super(ownerFrame);
		this.jListClassesFound = jListClassSearcher;
		this.addDoubleClickEvent2CurrentJListClassSearcher();
		
		this.superClass2Search4 = jListClassSearcher.getClass2SearchFor();
		this.class2Search4CurrentValue = clazz2Search4CurrentValue;
		this.class2Search4DefaultValue = clazz2Search4DefaultValue;
		this.class2Search4Description = clazz2Search4Description;
		this.setAllowNull(allowNull);
		this.initialize();
	}
	/**
	 * Constructor to configure the type of class, we are looking for.
	 * 
	 * @param ownerDialog the parent Dialog
	 * @param jListClassSearcher an actual instance of a {@link JListClassSearcher}
	 * @param clazz2Search4CurrentValue the clazz2 search4 current value
	 * @param clazz2Search4DefaultValue the clazz2 search4 default value
	 * @param clazz2Search4Description the clazz2 search4 description
	 * @param allowNull the allow null
	 */
	public ClassSelector(Dialog ownerDialog, JListClassSearcher jListClassSearcher, String clazz2Search4CurrentValue, String clazz2Search4DefaultValue, String clazz2Search4Description, boolean allowNull) {
		super(ownerDialog);
		this.jListClassesFound = jListClassSearcher;
		this.addDoubleClickEvent2CurrentJListClassSearcher();
		
		this.superClass2Search4 = jListClassSearcher.getClass2SearchFor();
		this.class2Search4CurrentValue = clazz2Search4CurrentValue;
		this.class2Search4DefaultValue = clazz2Search4DefaultValue;
		this.class2Search4Description = clazz2Search4Description;
		this.setAllowNull(allowNull);
		this.initialize();
	}
	
	
	/**
	 * Gets the class2 search4.
	 * @return the superClass2Search4
	 */
	public Class<?> getClass2Search4() {
		return superClass2Search4;
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

	/* (non-Javadoc)
	 * @see java.awt.Dialog#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible==false) {
			// --- Stop the class search ------------------
//			if (!superClass2Search4.equals(Agent.class) && !superClass2Search4.equals(Ontology.class) && !superClass2Search4.equals(BaseService.class)) {
//				this.css.stopSearch();
//			}
		}
		super.setVisible(visible);
	}
	
	
	/**
	 * This method initializes this.
	 *
	 * @return void
	 */
	private void initialize() {
	
		this.setSize(730, 606);
		this.setContentPane(getJContentPane());
		this.setTitle(Application.getGlobalInfo().getApplicationTitle() + ": Class-Selector");
		this.setIconImage(GlobalInfo.getInternalImage("AgentGUI.png"));
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
	 * This method initializes jContentPane.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 3;
			gridBagConstraints13.insets = new Insets(5, 5, 0, 20);
			gridBagConstraints13.gridy = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 3;
			gridBagConstraints12.insets = new Insets(5, 5, 0, 20);
			gridBagConstraints12.gridy = 5;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new Insets(10, 20, 10, 20);
			gridBagConstraints5.gridwidth = 4;
			gridBagConstraints5.gridy = 3;
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

			jLabelSeperator = new JLabel();
			jLabelSeperator.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jLabelSeperator.setText(" ");
			jLabelSeperator.setPreferredSize(new Dimension(200, 2));
			
			jLabelSearchCaption = new JLabel();
			jLabelSearchCaption.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelSearchCaption.setText("Suche & Auswahl");
			jLabelSearchCaption.setText(Language.translate(jLabelSearchCaption.getText()) + ": Class extends " + superClass2Search4.getName() );
			
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
			jContentPane.add(getJButtonSetNull(), gridBagConstraints13);
		}
		return jContentPane;
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
					isValidClass(jTextFieldCustomizeClass, jButtonCheckClass);
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
			jButtonDefaultClass.setToolTipText(Language.translate("Zurücksetzen"));
			jButtonDefaultClass.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonDefaultClass.setPreferredSize(new Dimension(45, 26));
			jButtonDefaultClass.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
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
			jButtonSetNull.setToolTipText(Language.translate("Löschen"));
			jButtonSetNull.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonSetNull.setPreferredSize(new Dimension(45, 26));
			jButtonSetNull.setIcon(GlobalInfo.getInternalImageIcon("Delete.png"));
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
	private JButton getJButtonDefaultClassCustomize() {
		if (jButtonCheckClass == null) {
			jButtonCheckClass = new JButton();
			jButtonCheckClass.setToolTipText(Language.translate("Klassenangabe überprüfen"));
			jButtonCheckClass.setPreferredSize(new Dimension(45, 26));
			jButtonCheckClass.setIcon(GlobalInfo.getInternalImageIcon("MBcheckGreen.png"));
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
	 * This method checks if a given class reference is valid.
	 *
	 * @param jTextField the j text field
	 * @param jButton the j button
	 * @return true, if is valid class
	 */
	private boolean isValidClass(JTextField jTextField, JButton jButton) {
		
		String className = jTextField.getText().trim();
		if (this.class2Search4DefaultValue==null && className.equals("")) {
			// --- If no default value is configured, an empty text field is allowed -----
			jButton.setIcon(GlobalInfo.getInternalImageIcon("MBcheckGreen.png"));
			this.setValidClass(true);
			return true;
		} else if (isAllowNull()==true && className.equals("")) {
			jButton.setIcon(GlobalInfo.getInternalImageIcon("MBcheckGreen.png"));
			this.setValidClass(true);
			return true;
			
		} else {
			// --- If a default value is configured, there should be a valid class ------ 
			try {
				ClassLoadServiceUtility.forName(className);
				jButton.setIcon(GlobalInfo.getInternalImageIcon("MBcheckGreen.png"));
				this.setValidClass(true);
				return true;
				
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
				jButton.setIcon(GlobalInfo.getInternalImageIcon("MBcheckRed.png"));
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
			jPanelProceed.add(getJButtonOK(), gridBagConstraints1);
			jPanelProceed.add(getJButtonCancel(), gridBagConstraints2);
		}
		return jPanelProceed;
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
			jButtonOK.setText("Übernehmen");
			jButtonOK.setText(Language.translate(jButtonOK.getText()));
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
		if (isValidClass(jTextFieldCustomizeClass, jButtonCheckClass)) {
			setClassSelected(jTextFieldCustomizeClass.getText().trim());
			setCanceled(false);
			setVisible(false);
		} else {
			System.out.println(Language.translate("Class not found:") + " '" + jTextFieldCustomizeClass.getText() + "'");						
		}
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
			jButtonTakeSelected.setIcon(GlobalInfo.getInternalImageIcon("ArrowUp.png"));
			jButtonTakeSelected.setToolTipText(Language.translate("Ausgewählte Klasse übernehmen!"));
			jButtonTakeSelected.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (jListClassesFound.isSelectionEmpty()==false) {
						Object selectedValue = jListClassesFound.getSelectedValue();
						if (selectedValue instanceof ClassElement2Display) {
							ClassElement2Display ce2d = (ClassElement2Display) selectedValue;
							jTextFieldCustomizeClass.setText(ce2d.toString());	
						} else if (selectedValue instanceof AgentClassElement) {
							AgentClassElement ace = (AgentClassElement) selectedValue;
							jTextFieldCustomizeClass.setText(ace.toString());
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
			jListClassesFound = new JListClassSearcher(this.superClass2Search4);
			this.addDoubleClickEvent2CurrentJListClassSearcher();
		}
		return jListClassesFound;
	}
	/**
	 * Adds the double click event to the current JListClassSearcher.
	 */
	private void addDoubleClickEvent2CurrentJListClassSearcher() {
		this.getJListClassesFound().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.getJListClassesFound().jListLoading.setToolTipText(Language.translate("Doppelklick um eine Klasse auszuwählen !"));
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
	
	
}
