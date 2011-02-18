package agentgui.core.ontologies.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import java.awt.Font;

public class OntologyInstanceViewer extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	final static String PathImage = Application.RunInfo.PathImageIntern();  //  @jve:decl-index=0:
	private ImageIcon imageEnlarge =  new ImageIcon(getClass().getResource(PathImage + "MBFullScreen.png"));  //  @jve:decl-index=0:
	
	private Project project = null;
	private String agentReference = null;
	private String[] ontologyClassReference = null;
	private boolean use4Agents =  false;

	private String [] configurationXML = null;
	private Object [] configurationInstances = null;
	
	private JScrollPane jScrollPaneDynForm = null;
	private JScrollPane jScrollPaneTextVersion = null;
	
	private DynForm dynForm = null;
	private JTextArea jTextArea = null;
	
	private final String newLine = Application.RunInfo.AppNewLineString();
	private final String separatorLine = "------------------------------------------";  //  @jve:decl-index=0:
	
	private JPanel jPanelEnlarege = null;
	private JLabel jLabelTitleEnlarge = null;  //  @jve:decl-index=0:visual-constraint="12,220"

	private JPanel jContentPane = null;
	private JPanel jPanel4TouchDown = null;


	/**
	 * These are the default constructors
	 */
	public OntologyInstanceViewer(Project currProject) {
		this.project = currProject;
		this.agentReference = null;
		this.ontologyClassReference = null;
		this.use4Agents = true;
		initialize();
	}
	public OntologyInstanceViewer(Project currProject, String currAgentReference) {
		super();
		this.project = currProject;
		this.agentReference = currAgentReference;
		this.ontologyClassReference = null;
		this.use4Agents = true;
		initialize();
	}
	public OntologyInstanceViewer(Project currProject, String[] currOntologyClassReference) {
		super();
		this.project = currProject;
		this.agentReference = null;
		this.ontologyClassReference = currOntologyClassReference;
		this.use4Agents = false;
		initialize();
	}

	/**
	 * This method initialises this
	 * @return void
	 */
	private void initialize() {
		
		this.setSize(400, 200);
		this.setPreferredSize(new Dimension(150, 57));
		this.setTabPlacement(JTabbedPane.BOTTOM);
		
		// --- Add the Form Tab ---------------------------		
		this.addTab("Formular", getJScrollPaneDynForm());
		
		// --- Add the XML-Tab to the ---------------------
		this.addTab("XML", getJScrollPaneTextVersion());

		// --- Add Enlarge-Tab ----------------------------
		this.addEnlargeTab();

		// --- Configure Translations ---------------------
		this.setTitleAt(0, "  " + Language.translate("Formular") + "  ");
		this.setTitleAt(1, "    " + Language.translate("XML") + "    ");
		
	}
	
	/**
	 * This method overrides the 'setSelectedIndex' method
	 * in order to catch the selection of the 'Enlarge view - Tab' 
	 */
	@Override
	public void setSelectedIndex(int index) {
	
		if (index==2) {
			this.setEnlargedView();
			
		} else {

			// --------------------------------------------
			// --- Save the current configuration ---------
			// --------------------------------------------
			this.save();
			
			// --------------------------------------------
			// --- Refresh the view to the data -----------
			// --------------------------------------------
			if (this.getSelectedIndex()==0) {
				// -----------------------------------
				// --- Refresh XML-View --------------
				// -----------------------------------
				String newText = "";
				String argumentLine = "";
				int seperatorLength = separatorLine.length();
				
				for (int i = 0; i < configurationXML.length; i++) {
					
					argumentLine = "--- Argument " + (i+1) + " ";
					argumentLine+= separatorLine.substring(0, seperatorLength-argumentLine.length());
					
					String config = ""; 
					config += separatorLine + newLine;
					config += argumentLine;
					config += newLine + separatorLine + newLine;
					config += configurationXML[i] + newLine;					
					newText += config;
				}
				this.jTextArea.setText(newText);
				this.jTextArea.setCaretPosition(0);
				
			} else if (this.getSelectedIndex()==1) {
				// -----------------------------------
				// --- Refresh Form-View -------------
				// -----------------------------------
				
				
			}
			
			// --------------------------------------------
			// --- Now do the focus change ----------------
			// --------------------------------------------
			super.setSelectedIndex(index);
		}		
	}
	
	/**
	 * This method shows the enlarged dialog for the current ontology class instances 
	 * in order to provide an easier access for the end user  
	 */
	private void setEnlargedView() {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		JDialog dialog = new JDialog(Application.MainWindow);
		dialog.setPreferredSize(new Dimension(100, 200));
		dialog.setName("Ontology-Instance-Viewer");
		dialog.setTitle(Application.RunInfo.getApplicationTitle() +  ": Ontology-Instance-Viewer");
		dialog.setModal(true);
		dialog.setResizable(true);
		dialog.setContentPane(getJContentPane());
		
		
		// --- Size and Center the dialog -----------------
		int diaWidth = (int) (screenSize.width*0.8);
		int diaHeight = (int) (screenSize.height * 0.9);

		int left = (screenSize.width - diaWidth) / 2;
		int top = (screenSize.height - diaHeight) / 2; 

		dialog.setSize(new Dimension(diaWidth, diaHeight));
	    dialog.setLocation(left, top);	
		
	    // --- Remind and remove THIS from the parent -----
	    Container parentContainer = this.getParent();
	    parentContainer.remove(this);
	    parentContainer.validate();
	    parentContainer.repaint();
	    
	    // --- Add THIS to the dialog ---------------------
	    this.removeEnlargeTab();
	    jPanel4TouchDown.add(this, BorderLayout.CENTER);
	    dialog.setVisible(true);
		// - - - - - - - - - - - - - - - - - - - - - - - -  
	    // - - Benutzereigabe / User-Interaction - - - - - 
		// - - - - - - - - - - - - - - - - - - - - - - - -
	    this.addEnlargeTab();
		
	    // --- Add THIS again to the parent ---------------
	    parentContainer.add(this);
	    parentContainer.validate();
	    parentContainer.repaint();
	    
	}
	
	/**
	 * This method initialises jContentPane	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(20, 20, 5, 20);
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJPanel4TouchDown(), gridBagConstraints);
		}
		return jContentPane;
	}
	/**
	 * This method initialises jPanel4TouchDown	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel4TouchDown() {
		if (jPanel4TouchDown == null) {
			jPanel4TouchDown = new JPanel();
			jPanel4TouchDown.setPreferredSize(new Dimension(20, 20));
			jPanel4TouchDown.setLayout(new BorderLayout());
		}
		return jPanel4TouchDown;
	}
	
	/**
	 * This method adds the Enlarge-View-Tab to THIS TabbedPane
	 */
	private void addEnlargeTab() {
		this.addTab("Vergrößern", getJPanelEnlarege());
		this.setTabComponentAt(2, this.getJLabelTitleEnlarge());
		this.jLabelTitleEnlarge.setText("  " + Language.translate("Vergrößern ...") + "  ");
	}
	/**
	 * This method removes the Enlarge-View-Tab to THIS TabbedPane
	 */
	private void removeEnlargeTab() {
		this.remove(jPanelEnlarege);
	}
	
	/**
	 * This method initialises dynForm
	 * @return
	 */
	private DynForm getDynForm() {
		if (dynForm==null) {
			if (use4Agents==true) {
				dynForm = new DynForm(project, agentReference);
			} else {
				dynForm = new DynForm(project, ontologyClassReference);
			}
		}
		return dynForm;
	}
	
	/**
	 * This method initialises jScrollPaneDynForm	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneDynForm() {
		if (jScrollPaneDynForm == null) {
			jScrollPaneDynForm = new JScrollPane();
			jScrollPaneDynForm.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jScrollPaneDynForm.setViewportView(this.getDynForm());
		}
		return jScrollPaneDynForm;
	}

	/**
	 * This method initialises jScrollPaneTextVersion	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneTextVersion() {
		if (jScrollPaneTextVersion == null) {
			jScrollPaneTextVersion = new JScrollPane();
			jScrollPaneTextVersion.setViewportView(getJTextArea());
		}
		return jScrollPaneTextVersion;
	}

	/**
	 * This method initialises jTextArea	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		}
		return jTextArea;
	}
	
	/**
	 * This method initialises jPanelEnlarege	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelEnlarege() {
		if (jPanelEnlarege == null) {
			jPanelEnlarege = new JPanel();
			jPanelEnlarege.setLayout(new GridBagLayout());
		}
		return jPanelEnlarege;
	}

	/**
	 * This method initialises jLabelTitleEnlarge	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getJLabelTitleEnlarge() {
		if (jLabelTitleEnlarge == null) {
			jLabelTitleEnlarge = new JLabel();
			jLabelTitleEnlarge.setText(" Vergrößern");
			jLabelTitleEnlarge.setIcon(imageEnlarge);
		}
		return jLabelTitleEnlarge;
	}

	
	/**
	 * This method saves the current configuration 
	 */
	public void save() {
		
		// --- Save configuration depending on the current view -----
		if (this.getSelectedIndex()==0) {
			// --- Form view ------------------------------
			this.dynForm.save(true);
		} else if (this.getSelectedIndex()==1) {
			// --- XML view -------------------------------
			this.dynForm.save(false);
		}
		
		// --- Store the current configuration locally --------------
		this.configurationXML = this.dynForm.getOntoArgsXML();
		this.configurationInstances = this.dynForm.getOntoArgsInstance();
		
	}
	
	/**
	 * @param configurationXML the configurationXML to set
	 */
	public void setConfigurationXML(String [] configurationXML) {
		this.configurationXML = configurationXML;
		this.dynForm.setOntoArgsXML(configurationXML);
	}
	/**
	 * @return the configurationXML
	 */
	public String [] getConfigurationXML() {
		return configurationXML;
	}
	
	/**
	 * @param configurationInstances the configurationInstances to set
	 */
	public void setConfigurationInstances(Object [] configurationInstances) {
		this.configurationInstances = configurationInstances;
		this.dynForm.setOntoArgsInstance(configurationInstances);
	}
	/**
	 * @return the configurationInstances
	 */
	public Object [] getConfigurationInstances() {
		return configurationInstances;
	}

}
