package gui.projectwindow;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import mas.onto.OntologyClassTreeObject;
import java.awt.Font;

public class OntologyTabClassView extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel ClassNameCaption = null;
	private JTextField ClassName = null;
	private JTable ClassSlots = null;
	private JScrollPane ClassSlotsSrollPane = null;
	private JLabel ClassDescCaption = null;
	private JTextField ClassDescription1 = null;
	private JLabel Dummy = null;
	
	private DefaultMutableTreeNode CurrOntoNode;
	private OntologyClassTreeObject CurrOntoObject;
	/**
	 * This is the default constructor
	 */
	public OntologyTabClassView( DefaultMutableTreeNode Node ) {
		super();
		CurrOntoNode = Node;
		CurrOntoObject = (OntologyClassTreeObject) CurrOntoNode.getUserObject();
		initialize();
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 2;
		gridBagConstraints2.gridy = 0;
		Dummy = new JLabel();
		Dummy.setText(" ");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.insets = new Insets(10, 0, 10, 10);
		gridBagConstraints1.weightx = 1.0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.insets = new Insets(10, 10, 10, 5);
		gridBagConstraints.gridy = 0;
		ClassDescCaption = new JLabel();
		ClassDescCaption.setText("Text");
		ClassDescCaption.setVisible(false);
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.fill = GridBagConstraints.BOTH;
		gridBagConstraints6.gridwidth = 5;
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.gridy = 1;
		gridBagConstraints6.weightx = 1.0;
		gridBagConstraints6.weighty = 1.0;
		gridBagConstraints6.insets = new Insets(0, 10, 0, 10);
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.fill = GridBagConstraints.BOTH;
		gridBagConstraints5.gridx = 1;
		gridBagConstraints5.gridy = 0;
		gridBagConstraints5.weightx = 1.0;
		gridBagConstraints5.anchor = GridBagConstraints.CENTER;
		gridBagConstraints5.weighty = 0.0;
		gridBagConstraints5.insets = new Insets(10, 0, 10, 10);
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.insets = new Insets(10, 10, 10, 5);
		gridBagConstraints4.gridy = 0;
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.gridx = 0;
		ClassNameCaption = new JLabel();
		ClassNameCaption.setText("Name:");
		ClassNameCaption.setFont(new Font("Dialog", Font.BOLD, 12));
		this.setSize(392, 238);
		this.setLayout(new GridBagLayout());
		this.add(ClassNameCaption, gridBagConstraints4);
		this.add(getClassName(), gridBagConstraints5);
		this.add(getClassSlotsScollPane(), gridBagConstraints6);
		this.add(ClassDescCaption, gridBagConstraints);
		this.add(getClassDescription1(), gridBagConstraints1);
		this.add(Dummy, gridBagConstraints2);
	}

	/**
	 * This method initializes ClassName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getClassName() {
		if (ClassName == null) {
			ClassName = new JTextField();
			ClassName.setPreferredSize(new Dimension(120, 26));
			ClassName.setEditable(false);
			ClassName.setFont(new Font("Dialog", Font.BOLD, 12));
			ClassName.setText( CurrOntoObject.getClassReference() );
		}
		return ClassName;
	}

	/**
	 * This method initializes ClassSlots	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JScrollPane getClassSlotsScollPane() {
		if (ClassSlotsSrollPane == null) {
			ClassSlotsSrollPane = new JScrollPane();
			ClassSlotsSrollPane.setViewportView( getClassSlots() );
		}
		return ClassSlotsSrollPane;
	}

	/**
	 * This method initializes ClassSlots	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getClassSlots() {
		if (ClassSlots == null) {
			DefaultTableModel DTM = CurrOntoObject.getTableModel4Slot();
			ClassSlots = new JTable();
			if ( DTM != null) {
				ClassSlots.setModel( DTM );	
			}			
			ClassSlots.setEnabled(true);
			ClassSlots.setShowGrid(true);
		}
		return ClassSlots;
	}

	/**
	 * This method initializes ClassDescription1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getClassDescription1() {
		if (ClassDescription1 == null) {
			ClassDescription1 = new JTextField();
			ClassDescription1.setPreferredSize(new Dimension(120, 26));
			ClassDescription1.setVisible(false);
		}
		return ClassDescription1;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
