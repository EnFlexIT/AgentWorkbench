package agentgui.core.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import agentgui.core.application.Application;
import agentgui.core.application.Language;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

public class ClassSelector extends JDialog {

	private static final long serialVersionUID = 1L;
	
	final static String PathImage = Application.RunInfo.PathImageIntern();
	
	private JPanel jContentPane = null;
	private JLabel jLabelCustomize = null;
	private JTextField jTextFieldCustomizeClass = null;
	private JButton jButtonDefaultClass = null;
	private JButton jButtonDefaultClassCustomize = null;

	private JPanel jPanelProceed = null;

	private JButton jButtonOK = null;

	private JButton jButtonCancel = null;
	
	/**
	 * @param owner
	 */
	public ClassSelector(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(760, 220);
		this.setContentPane(getJContentPane());
		this.setTitle("Agent.GUI: Class-Selector");
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridwidth = 3;
			gridBagConstraints.insets = new Insets(20, 20, 20, 20);
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
			jLabelCustomize.setText("Java-Klasse für individuelle Agent.GUI-Erweiterung");
			jLabelCustomize.setFont(new Font("Dialog", Font.BOLD, 12));
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(jLabelCustomize, gridBagConstraints42);
			jContentPane.add(getJTextFieldCustomizeClass(), gridBagConstraints31);
			jContentPane.add(getJButtonDefaultClass(), gridBagConstraints52);
			jContentPane.add(getJButtonDefaultClassCustomize(), gridBagConstraints28);
			jContentPane.add(getJPanelProceed(), gridBagConstraints);
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
			jTextFieldCustomizeClass.setPreferredSize(new Dimension(400, 26));
			jTextFieldCustomizeClass.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent kR) {
					super.keyReleased(kR);
					isValidClass(jTextFieldCustomizeClass, jButtonDefaultClassCustomize);
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
			jButtonDefaultClass.setToolTipText("Agent.GUI - Standard verwenden");			
			jButtonDefaultClass.setBounds(new Rectangle(120, 121, 80, 26));
			jButtonDefaultClass.setPreferredSize(new Dimension(45, 26));
			jButtonDefaultClass.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBreset.png")));
			jButtonDefaultClass.setActionCommand("StatLoadBalancingDefault");
			jButtonDefaultClass.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
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
		if (jButtonDefaultClassCustomize == null) {
			jButtonDefaultClassCustomize = new JButton();
			jButtonDefaultClassCustomize.setToolTipText("Klassenangabe überprüfen");
			jButtonDefaultClassCustomize.setPreferredSize(new Dimension(45, 26));
			jButtonDefaultClassCustomize.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBcheckGreen.png")));
			jButtonDefaultClassCustomize.setActionCommand("StatLoadBalancingCheck");
			jButtonDefaultClassCustomize.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		return jButtonDefaultClassCustomize;
	}
	
	/**
	 * This method checks if a given classs reference is valid
	 * @param className: reference to the class  
	 * @param classType: static(0) or dynamic(1)
	 */
	private void isValidClass(JTextField jTextField, JButton jButton) {
		
		String className = jTextField.getText().trim();
		try {
			@SuppressWarnings("unused")
			Class<?> clazz = Class.forName(className);
			jButton.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBcheckGreen.png")));
			
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			jButton.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBcheckRed.png")));
		}
	}

	/**
	 * This method initializes jPanelProceed	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelProceed() {
		if (jPanelProceed == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(10, 30, 10, 0);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(10, 0, 10, 30);
			jPanelProceed = new JPanel();
			jPanelProceed.setLayout(new GridBagLayout());
			jPanelProceed.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanelProceed.add(getJButtonOK(), gridBagConstraints1);
			jPanelProceed.add(getJButtonCancel(), gridBagConstraints2);
		}
		return jPanelProceed;
	}

	/**
	 * This method initializes jButtonOK	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setPreferredSize(new Dimension(120, 26));
			jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOK.setForeground(new Color(0, 153, 0));
			jButtonOK.setText("Übernehmen");
			jButtonOK.setText(Language.translate(jButtonOK.getText()));
		}
		return jButtonOK;
	}

	/**
	 * This method initializes jButtonCancel	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setPreferredSize(new Dimension(120, 26));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setText("Abbruch");
			jButtonCancel.setText(Language.translate(jButtonCancel.getText()));
		}
		return jButtonCancel;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
