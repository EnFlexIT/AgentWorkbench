package agentgui.gasgridEnvironment.controller;

import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComboBox;

import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.physical2Denvironment.display.BasicSVGGUI;

import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * GUI class for EnvironmentController controlling grid simulation projects
 * @author Nils
 *
 */
public class GridEnvironmentControllerGUI extends JSplitPane implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel pnlControlls = null;
	private JButton btnLoadGraph = null;
	private JComboBox cbEnvMode = null;
	private JLabel lblMode = null;
	
	private BasicSVGGUI svgGUI = null;
	private GridEnvironmentController controller = null;

	@Override
	public void update(Observable o, Object arg) {
		if(o.equals(controller) && arg.equals(GridEnvironmentController.SVG_LOADED)){
			this.svgGUI.setSVGDoc(controller.getSvgDoc());
		}
	}

	/**
	 * This is the default constructor
	 */
	public GridEnvironmentControllerGUI(Project project) {
		super();
		initialize(project);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(Project project) {
		this.setSize(300, 200);

		this.setDividerLocation(150);
		this.setLeftComponent(getPnlControlls());
		this.setRightComponent(getSVGGUI());
		
		this.controller = new GridEnvironmentController(project);
		this.controller.addObserver(this);
		this.controller.loadSimSetupFiles();
	}
	
	private BasicSVGGUI getSVGGUI(){
		if(svgGUI == null){
			svgGUI = new BasicSVGGUI();
		}
		return svgGUI;
	}

	/**
	 * This method initializes pnlControlls	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlControlls() {
		if (pnlControlls == null) {
			lblMode = new JLabel();
			lblMode.setText("Umgebungstyp");
			lblMode.setSize(new Dimension(85, 16));
			lblMode.setLocation(new Point(10, 10));
			pnlControlls = new JPanel();
			pnlControlls.setLayout(null);
			pnlControlls.add(getBtnLoadGraph(), null);
			pnlControlls.add(getCbEnvMode(), null);
			pnlControlls.add(lblMode, null);
		}
		return pnlControlls;
	}

	/**
	 * This method initializes btnLoadGraph	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnLoadGraph() {
		if (btnLoadGraph == null) {
			btnLoadGraph = new JButton();
			btnLoadGraph.setText(Language.translate("Graph Laden"));
			btnLoadGraph.setLocation(new Point(10, 60));
			btnLoadGraph.setSize(new Dimension(120, 26));
			btnLoadGraph.addActionListener(this);
		}
		return btnLoadGraph;
	}

	/**
	 * This method initializes cbEnvMode	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbEnvMode() {
		if (cbEnvMode == null) {
			cbEnvMode = new JComboBox();
			cbEnvMode.setSize(new Dimension(120, 25));
			cbEnvMode.setLocation(new Point(10, 30));
			
			String[] modes = {Language.translate("physikalisch"), Language.translate("Netz")};
			cbEnvMode.setModel(new DefaultComboBoxModel(modes));
			cbEnvMode.addActionListener(this);
			cbEnvMode.setSelectedItem(Language.translate("Netz"));

			cbEnvMode.setEnabled(false);
		}
		return cbEnvMode;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(getBtnLoadGraph())){
			JFileChooser graphFC = new JFileChooser();
			graphFC.setFileFilter(new FileNameExtensionFilter(Language.translate("GraphML-Dateien"), "graphml"));
			if(graphFC.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				File graphMLFile = graphFC.getSelectedFile();
				this.controller.loadNewGrap(graphMLFile);
			}
		}
	}

}
