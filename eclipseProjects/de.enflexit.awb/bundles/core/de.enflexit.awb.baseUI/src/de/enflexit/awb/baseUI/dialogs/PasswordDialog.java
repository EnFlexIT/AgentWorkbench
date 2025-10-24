package de.enflexit.awb.baseUI.dialogs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.KeyStroke;

import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.ui.AwbPasswordDialog;
import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;

/**
 * The Class PasswordDialog.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PasswordDialog extends JDialog implements AwbPasswordDialog, ActionListener {

	private static final long serialVersionUID = 5521314535563046560L;

	private Window myOwner;
	private boolean isConfirmPassword;
	private boolean isCanceled;
	
	private JLabel jLabelHeader;
	private JLabel jLabelRepeat;
	
	private Dimension passwordFieldSize = new Dimension(300, 26);
	private JPasswordField jPasswordField1;
	private JPasswordField jPasswordField2;

	private JPanel jPanelButtons;
	private JButton jButtonCancel;
	private JButton jButtonOk;
	
	/**
	 * Instantiates a new password dialog.
	 *
	 * @param owner the owner window
	 * @param isConfirmPassword the is confirm password
	 * @param windowTitle the window title
	 * @param headerText the header text
	 */
	public PasswordDialog(Window owner, boolean isConfirmPassword, String windowTitle, String headerText) {
		super(owner);
		this.myOwner = owner;
		this.initialize();
		this.setConfirmPassword(isConfirmPassword);
		this.setWindowTitle(windowTitle);
		this.setHeaderText(headerText);
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setIconImage(GlobalInfo.getInternalImageAwbIcon16());
		this.setTitle("Enter Password");

		this.setSize(new Dimension(400, 250));
		if (this.isConfirmPassword==false) {
			this.setSize(new Dimension(400, 209));
		}
		
		this.setModal(true);
		this.setResizable(false);
		this.registerEscapeKeyStroke();
		
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);

		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				PasswordDialog.this.isCanceled = true;
				PasswordDialog.this.setVisible(false);
			}
		});
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.insets = new Insets(20, 20, 0, 20);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		this.getContentPane().add(getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jPasswordField1 = new GridBagConstraints();
		gbc_jPasswordField1.fill = GridBagConstraints.HORIZONTAL;
		gbc_jPasswordField1.insets = new Insets(5, 20, 0, 20);
		gbc_jPasswordField1.gridx = 0;
		gbc_jPasswordField1.gridy = 1;
		this.getContentPane().add(getJPasswordField1(), gbc_jPasswordField1);
		GridBagConstraints gbc_jLabelRepeat = new GridBagConstraints();
		gbc_jLabelRepeat.anchor = GridBagConstraints.WEST;
		gbc_jLabelRepeat.insets = new Insets(10, 20, 0, 20);
		gbc_jLabelRepeat.gridx = 0;
		gbc_jLabelRepeat.gridy = 2;
		this.getContentPane().add(getJLabelRepeat(), gbc_jLabelRepeat);
		GridBagConstraints gbc_jPasswordField2 = new GridBagConstraints();
		gbc_jPasswordField2.insets = new Insets(5, 20, 0, 20);
		gbc_jPasswordField2.fill = GridBagConstraints.HORIZONTAL;
		gbc_jPasswordField2.gridx = 0;
		gbc_jPasswordField2.gridy = 3;
		this.getContentPane().add(getJPasswordField2(), gbc_jPasswordField2);
		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.insets = new Insets(20, 20, 20, 20);
		gbc_jPanelButtons.fill = GridBagConstraints.VERTICAL;
		gbc_jPanelButtons.gridx = 0;
		gbc_jPanelButtons.gridy = 4;
		getContentPane().add(getJPanelButtons(), gbc_jPanelButtons);
	}
	
	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
				PasswordDialog.this.isCanceled = true;
				PasswordDialog.this.setVisible(false);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Please, enter the password");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	private JPasswordField getJPasswordField1() {
		if (jPasswordField1 == null) {
			jPasswordField1 = new JPasswordField();
			jPasswordField1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jPasswordField1.setPreferredSize(this.passwordFieldSize);
		}
		return jPasswordField1;
	}
	private JLabel getJLabelRepeat() {
		if (jLabelRepeat == null) {
			jLabelRepeat = new JLabel("Repeat password");
			jLabelRepeat.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelRepeat;
	}
	private JPasswordField getJPasswordField2() {
		if (jPasswordField2 == null) {
			jPasswordField2 = new JPasswordField();
			jPasswordField2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jPasswordField2.setPreferredSize(this.passwordFieldSize);
		}
		return jPasswordField2;
	}

	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_jPanelButtons = new GridBagLayout();
			gbl_jPanelButtons.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelButtons.rowHeights = new int[]{0, 0};
			gbl_jPanelButtons.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelButtons.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelButtons.setLayout(gbl_jPanelButtons);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(0, 0, 0, 30);
			gbc_jButtonCancel.gridx = 0;
			gbc_jButtonCancel.gridy = 0;
			jPanelButtons.add(getJButtonCancel(), gbc_jButtonCancel);
			GridBagConstraints gbc_jButtonOk = new GridBagConstraints();
			gbc_jButtonOk.insets = new Insets(0, 30, 0, 0);
			gbc_jButtonOk.gridx = 1;
			gbc_jButtonOk.gridy = 0;
			jPanelButtons.add(getJButtonOk(), gbc_jButtonOk);
		}
		return jPanelButtons;
	}
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(AwbThemeColor.ButtonTextRed.getColor());
			jButtonCancel.setPreferredSize(new Dimension(120, 28));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton("Proceed");
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.setForeground(AwbThemeColor.ButtonTextGreen.getColor());
			jButtonOk.setPreferredSize(new Dimension(120, 28));
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbPasswordDialog#setConfirmPassword(boolean)
	 */
	@Override
	public void setConfirmPassword(boolean isConfirmPassword) {
		this.isConfirmPassword = isConfirmPassword;
		if (this.isConfirmPassword==false) {
			this.getJLabelRepeat().setVisible(false);
			this.getJPasswordField2().setVisible(false);
			this.validate();
			this.repaint();
		}
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbPasswordDialog#setWindowTitle(java.lang.String)
	 */
	@Override
	public void setWindowTitle(String windowTitle) {
		if (windowTitle==null || windowTitle.isBlank()==true) return;
		this.setTitle(windowTitle);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbPasswordDialog#setHeaderText(java.lang.String)
	 */
	@Override
	public void setHeaderText(String headerText) {
		if (headerText==null || headerText.isBlank()==true) return;
		this.getJLabelHeader().setText(headerText);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbPasswordDialog#isCanceled()
	 */
	@Override
	public boolean isCanceled() {
		return isCanceled;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbPasswordDialog#getPassword()
	 */
	@Override
	public char[] getPassword() {
		
		char[] pswdArray1 = this.getJPasswordField1().getPassword();
		char[] pswdArray2 = this.getJPasswordField2().getPassword();
		
		if (this.isConfirmPassword==true) {
			String pswd1 = String.valueOf(pswdArray1);
			String pswd2 = String.valueOf(pswdArray2);
			if (pswd1.equals(pswd2)==false) {
				pswdArray1 = null; 
			}
		}
		return pswdArray1;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonOk()) {
			// --- Check for errors -----
			String errMsg = this.isPasswordError();
			if (errMsg!=null) {
				JOptionPane.showMessageDialog(this.myOwner, errMsg, "Wrong or no password!", JOptionPane.ERROR_MESSAGE, null);
				return;
			}
			
			// --- OK action ------------
			this.isCanceled = false;
			this.setVisible(false);
			
		} else if (ae.getSource()==this.getJButtonCancel()) {
			// --- Cancel Action -------
			this.isCanceled = true;
			this.setVisible(false);
		}
	}
	
	/**
	 * Checks if is password error.
	 * @return true, if is password error
	 */
	private String isPasswordError() {
		
		String msg = "";
		
		char[] pswd = this.getPassword();
		if (pswd==null) {
			if (this.isConfirmPassword==true) {
				msg = "The entered password and its repetition are not equal!";
			} else {
				msg = "No password was provided!";
			}
		}
		
		
		if (msg.isBlank()==true && pswd.length<6) {
			msg = "The password entered is to short";
		}
		
		return msg.isBlank()==true ? null : msg;
	}
	
}
