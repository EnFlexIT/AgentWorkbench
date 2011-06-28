package agentgui.core.ontologies.gui;

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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

public class OntologyInstanceDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Project currProject = null;
	private String currAgentReference = null;
	private String[] ontologyClassReference = null;
	
	private boolean cancelled = false;
	private Object [] objectConfiguration = null;
	
	private OntologyInstanceViewer oiv = null;
	private JPanel jContentPane = null;
	private JButton jButtonOK = null;
	private JButton jButtonCancel = null;
	private JPanel jPanelBottom = null;
	
	/**
	 * @param owner
	 */
	public OntologyInstanceDialog(Frame owner, Project project) {
		super(owner);
		this.currProject = project;
		initialize();
	}
	public OntologyInstanceDialog(Frame owner, Project project, String agentReference) {
		super(owner);
		this.currProject = project;
		this.currAgentReference = agentReference;
		initialize();
	}
	public OntologyInstanceDialog(Frame owner, Project project, String[] ontologyClassReference) {
		super(owner);
		this.currProject = project;
		this.ontologyClassReference = ontologyClassReference;
		initialize();
	}
	
	/**
	 * This method initialises this
	 */
	private void initialize() {
		
		this.setSize(600, 500);
		this.setModal(true);
		this.setTitle(Language.translate("Ontologie-Klassen initialisieren"));
		this.setContentPane(getJContentPane());
		
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				setCancelled(true);
				setVisible(false);
			}
		});
		// --- Set the IconImage ----------------------------------
		this.setIconImage( Application.MainWindow.getIconImage() );
		
		// --- Dialog zentrieren ------------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);	
	    
	}

	/**
	 * @return
	 */
	private OntologyInstanceViewer getOIV() {
		if(oiv==null) {
			if (currAgentReference!=null && oiv==null) {
				oiv = new OntologyInstanceViewer(currProject, currAgentReference);
				oiv.setAllowViewEnlargement(false);
			}
			if (ontologyClassReference!=null && oiv==null) {
				oiv = new OntologyInstanceViewer(currProject, ontologyClassReference);
				oiv.setAllowViewEnlargement(false);
			}	
		}
		return oiv;
	}
	
	/**
	 * @param canceld the cancelled to set
	 */
	public void setCancelled(boolean canceled) {
		this.cancelled = canceled;
	}
	/**
	 * @return the cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}
	
	/**
	 * @param objectConfiguration the objectConfiguration to set
	 */
	public void setObjectConfiguration(Object [] objectConfiguration) {
		this.objectConfiguration = objectConfiguration;
	}
	/**
	 * @return the objectConfiguration
	 */
	public Object [] getObjectConfiguration() {
		return objectConfiguration;
	}
	/**
	 * This method initialises jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new Insets(10, 10, 20, 10);
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.insets = new Insets(10, 10, 0, 10);
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getOIV(), gridBagConstraints);
			jContentPane.add(getJPanelBottom(), gridBagConstraints3);
		}
		return jContentPane;
	}
	
	/**
	 * This method initialises jButtonOK	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setPreferredSize(new Dimension(120, 26));
			jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOK.setForeground(new Color(0, 153, 0));
			jButtonOK.setText("OK");
			jButtonOK.setText(Language.translate(jButtonOK.getText()));
			jButtonOK.addActionListener(this);
		}
		return jButtonOK;
	}
	/**
	 * This method initialises jButtonCancel	
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
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	/**
	 * This method initialises jPanelBottom	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.insets = new Insets(10, 50, 10, 0);
			gridBagConstraints2.anchor = GridBagConstraints.CENTER;
			gridBagConstraints2.gridy = -1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = -1;
			gridBagConstraints1.insets = new Insets(10, 0, 10, 50);
			gridBagConstraints1.gridy = -1;
			jPanelBottom = new JPanel();
			jPanelBottom.setLayout(new GridBagLayout());
			jPanelBottom.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanelBottom.add(getJButtonOK(), gridBagConstraints1);
			jPanelBottom.add(getJButtonCancel(), gridBagConstraints2);
		}
		return jPanelBottom;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		Object trigger = ae.getSource();
		if (trigger==jButtonOK) {
			oiv.save();
			this.setObjectConfiguration(oiv.getConfigurationInstances());
			this.setCancelled(false);
			this.setVisible(false);
			
		} else if (trigger==jButtonCancel) {
			this.setCancelled(true);
			this.setVisible(false);
			
		}
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
