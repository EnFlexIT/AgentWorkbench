package de.enflexit.common.swing;

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
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import de.enflexit.common.BundleHelper;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;
import de.enflexit.language.Language;

/**
 * The Class TimeFormatSelectionDialog enables to configure the blueprint for 
 * a date / time format within a dialog.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeFormatSelectionDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 5608379141494444602L;

	private boolean isCanceled;
	
	private TimeFormatSelection jPanelTimeFormatter;
	private JButton jButtonOK;
	private JButton jButtonCancel;
	private JPanel jPanelButtons;
	
	
	/**
	 * Instantiates a new time format selection dialog.
	 * @param owner the owner
	 */
	public TimeFormatSelectionDialog(Window owner) {
		super(owner);
		this.initialize();
	}

	private void initialize() {
		
		this.setTitle(Language.translate("Datum / Zeit - Konfiguration"));
		this.setIconImage(BundleHelper.getImageIcon("awb16.png").getImage());
		
		this.setModal(true);
		this.registerEscapeKeyStroke();
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				isCanceled = true;
				setVisible(false);
			}
		});
		this.setSize(490, 160);
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		this.getContentPane().setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jPanelTimeFormatter = new GridBagConstraints();
		gbc_jPanelTimeFormatter.anchor = GridBagConstraints.NORTH;
		gbc_jPanelTimeFormatter.insets = new Insets(10, 10, 0, 10);
		gbc_jPanelTimeFormatter.fill = GridBagConstraints.HORIZONTAL;
		gbc_jPanelTimeFormatter.gridx = 0;
		gbc_jPanelTimeFormatter.gridy = 0;
		
		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.anchor = GridBagConstraints.NORTH;
		gbc_jPanelButtons.insets = new Insets(15, 10, 15, 10);
		gbc_jPanelButtons.gridx = 0;
		gbc_jPanelButtons.gridy = 1;

		this.getContentPane().add(this.getJPanelTimeFormatter(), gbc_jPanelTimeFormatter);
		this.getContentPane().add(this.getJPanelButtons(), gbc_jPanelButtons);
	}
	
	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
    			setVisible(false);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
	
	/**
	 * Gets the time formatter.
	 * @return the time formatter
	 */
	private TimeFormatSelection getJPanelTimeFormatter() {
		if (jPanelTimeFormatter==null) {
			jPanelTimeFormatter = new TimeFormatSelection(true, 5, new Font("Dialog", Font.BOLD, 12));
		}
		return jPanelTimeFormatter;
	}
	
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_jPanelButtons = new GridBagLayout();
			gbl_jPanelButtons.columnWidths = new int[]{0};
			gbl_jPanelButtons.rowHeights = new int[]{0};
			gbl_jPanelButtons.columnWeights = new double[]{Double.MIN_VALUE};
			gbl_jPanelButtons.rowWeights = new double[]{Double.MIN_VALUE};
			jPanelButtons.setLayout(gbl_jPanelButtons);
			
			GridBagConstraints gbc_jButtonOK = new GridBagConstraints();
			gbc_jButtonOK.insets = new Insets(0, 0, 0, 20);
			gbc_jButtonOK.gridx = 0;
			gbc_jButtonOK.gridy = 0;
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(0, 20, 0, 0);
			gbc_jButtonCancel.gridx = 1;
			gbc_jButtonCancel.gridy = 0;
			
			jPanelButtons.add(this.getJButtonOK(), gbc_jButtonOK);
			jPanelButtons.add(this.getJButtonCancel(), gbc_jButtonCancel);
		}
		return jPanelButtons;
	}
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton("Anwenden");
			jButtonOK.setText(Language.translate(jButtonOK.getText()));
			jButtonOK.setForeground(AwbThemeColor.ButtonTextGreen.getColor());
			jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOK.setPreferredSize(new Dimension(120, 26));
			jButtonOK.addActionListener(this);
		}
		return jButtonOK;
	}
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Abbruch");
			jButtonCancel.setText(Language.translate(jButtonCancel.getText()));
			jButtonCancel.setForeground(AwbThemeColor.ButtonTextRed.getColor());
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setPreferredSize(new Dimension(120, 26));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	/**
	 * Checks if is canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return isCanceled;
	}
	/**
	 * Gets the time format.
	 * @return the time format
	 */
	public String getTimeFormat() {
		return this.getJPanelTimeFormatter().getTimeFormat();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource()==this.getJButtonOK()) {
			this.isCanceled = false;
		} else if (ae.getSource()==this.getJButtonCancel()) {
			this.isCanceled = true;
		}
		this.setVisible(true);
	}
}
