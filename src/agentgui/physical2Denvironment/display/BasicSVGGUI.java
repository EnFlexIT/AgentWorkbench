package agentgui.physical2Denvironment.display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.geom.Dimension2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.w3c.dom.Document;

import agentgui.core.application.Application;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;

import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.KeyEvent;

/**
 * Basic GUI for displaying SVG graphics
 * JSVGCanvas on Scrollpane + Zoom buttons   
 * @author Nils
 *
 */
public class BasicSVGGUI extends JPanel {

	private static final long serialVersionUID = 1L;
	private final String PathImage = Application.RunInfo.PathImageIntern();  //  @jve:decl-index=0:
	private JPanel pnlZoom = null;
	private JButton btnZoomIn = null;
	private JButton btnZoomOut = null;
	private JButton btnZoomReset = null;
	private JSVGCanvas canvas = null;
	private JSVGScrollPane scrollpane;
	private JPanel jPanelDummy = null;
	private JLabel jLabelSpeed = null;
	protected JSlider jSliderVisualation = null;
	private JLabel jLabelTime = null;
	protected JSlider jSliderTime = null;
	protected JPanel jPanelSimuInfos = null;
	protected JLabel jLabelTimeDisplay = null;
	protected JLabel jLabelSpeedFactor = null;
	protected JButton jButtonPlay = null;
	/**
	 * This is the default constructor
	 */
	public BasicSVGGUI() {
		super();
		this.canvas = new JSVGCanvas();
		initialize();
		jPanelSimuInfos.setVisible(false);
		
		
	}
	
	public void setMaximum(int max)
	{
		this.jSliderTime.setMaximum(max);
	}
	
	public void setCurrentTimePos(int pos)
	{
	
		this.jSliderTime.setValue(pos);
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(0);
		borderLayout.setVgap(0);
		this.setLayout(borderLayout);
		this.setSize(new Dimension(415, 218));
		this.add(getPnlZoom(), BorderLayout.WEST);
		this.add(getScrollpane(), BorderLayout.CENTER);
		this.add(getJPanelSimuInfos(), BorderLayout.SOUTH);
	}

	/**
	 * This method initializes pnlZoom	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlZoom() {
		if (pnlZoom == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(5, 5, 0, 5);
			gridBagConstraints.gridy = -1;
			gridBagConstraints.gridx = -1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.gridy = 3;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(10, 5, 0, 5);
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.gridx = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.gridx = 0;
			pnlZoom = new JPanel();
			pnlZoom.setLayout(new GridBagLayout());
			pnlZoom.add(getBtnZoomOut(), gridBagConstraints1);
			pnlZoom.add(getBtnZoomReset(), gridBagConstraints2);
			pnlZoom.add(getJPanelDummy(), gridBagConstraints3);
			pnlZoom.add(getBtnZoomIn(), gridBagConstraints);
		}
		return pnlZoom;
	}

	/**
	 * This method initializes btnZoomIn	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnZoomIn() {
		if (btnZoomIn == null) {
			btnZoomIn = new JButton();
			btnZoomIn.setPreferredSize(new Dimension(45, 26));
			btnZoomIn.setIcon(new ImageIcon(getClass().getResource(PathImage + "ListPlus.png")));
			btnZoomIn.setToolTipText("Zoom in");
			btnZoomIn.addActionListener(canvas.new ZoomAction(1.2));
		}
		return btnZoomIn;
	}

	/**
	 * This method initializes btnZoomOut	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnZoomOut() {
		if (btnZoomOut == null) {
			btnZoomOut = new JButton();
			btnZoomOut.setPreferredSize(new Dimension(45, 26));
			btnZoomOut.setIcon(new ImageIcon(getClass().getResource(PathImage + "ListMinus.png")));
			btnZoomOut.setToolTipText("Zoom out");
			btnZoomOut.addActionListener(canvas.new ZoomAction(0.8333333333));
		}
		return btnZoomOut;
	}

	/**
	 * This method initializes btnZoomReset	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnZoomReset() {
		if (btnZoomReset == null) {
			btnZoomReset = new JButton();
			btnZoomReset.setPreferredSize(new Dimension(45, 26));
			btnZoomReset.setIcon(new ImageIcon(getClass().getResource(PathImage + "Refresh.png")));
			btnZoomReset.setToolTipText("Reset to 100%");
			btnZoomReset.addActionListener(canvas.new ResetTransformAction());
		}
		return btnZoomReset;
	}
	
	private JSVGScrollPane getScrollpane(){
		if(scrollpane == null){
			scrollpane = new JSVGScrollPane(canvas);
		}
		return scrollpane;
	}
	
	public JSVGCanvas getCanvas(){
		return canvas;
	}
	
	public void setSVGDoc(Document doc){
		this.canvas.setDocument(doc);
		if(doc != null){
			float svgWidth = Float.parseFloat(doc.getDocumentElement().getAttributeNS(null, "width"));
			float svgHeight = Float.parseFloat(doc.getDocumentElement().getAttributeNS(null, "height"));
			Dimension prefSize = new Dimension();
			prefSize.setSize(svgWidth+getPnlZoom().getPreferredSize().getWidth(), svgHeight);
			this.setPreferredSize(prefSize);
		}		
	}
	
	public Document getSVGDoc(){
		return canvas.getSVGDocument();
	}

	/**
	 * This method initializes jPanelDummy	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelDummy() {
		if (jPanelDummy == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridx = 0;
			jPanelDummy = new JPanel();
			jPanelDummy.setLayout(new GridBagLayout());
			jPanelDummy.setPreferredSize(new Dimension(45, 20));
		}
		return jPanelDummy;
	}

	/**
	 * This method initializes jLabelSpeed	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getJLabelSpeed() {
		if (jLabelSpeed == null) {
			jLabelSpeed = new JLabel();
			jLabelSpeed.setText("Vis.-Geschwindigkeit");
		}
		return jLabelSpeed;
	}

	/**
	 * This method initializes jSliderVisualation	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getJSliderVisualation() {
		if (jSliderVisualation == null) {
			jSliderVisualation = new JSlider();
			jSliderVisualation.setMinimum(1);
			jSliderVisualation.setValue(1);
			jSliderVisualation.setMaximum(10);
//			jSliderVisualation.setMajorTickSpacing(10);
			jSliderVisualation.setMinorTickSpacing(1);
			jSliderVisualation.setPaintTicks(true);
//			jSliderVisualation.createStandardLabels(250);
			jSliderVisualation.setPaintLabels(false);
			jSliderVisualation.setSnapToTicks(true);
		}
		return jSliderVisualation;
	}

	/**
	 * This method initializes jLabelTime	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getJLabelTime() {
		if (jLabelTime == null) {
			jLabelTime = new JLabel();
			jLabelTime.setText("Simulationszeit");
			jLabelTime.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
		}
		return jLabelTime;
	}

	/**
	 * This method initializes jSliderTime	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getJSliderTime() {
		if (jSliderTime == null) {
			jSliderTime = new JSlider();
			jSliderTime.setMaximum(1);
			jSliderTime.setValue(0);
			jSliderTime.setSnapToTicks(true);
			jSliderTime.setMinorTickSpacing(1);
			jSliderTime.setPaintTicks(true);
			
			
		}
		return jSliderTime;
	}

	/**
	 * This method initializes jPanelSimuInfos	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSimuInfos() {
		if (jPanelSimuInfos == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 4;
			gridBagConstraints13.gridheight = 3;
			gridBagConstraints13.anchor = GridBagConstraints.CENTER;
			gridBagConstraints13.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints13.fill = GridBagConstraints.NONE;
			gridBagConstraints13.gridwidth = 1;
			gridBagConstraints13.gridy = 0;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 3;
			gridBagConstraints12.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.gridy = 2;
			jLabelSpeedFactor = new JLabel();
			jLabelSpeedFactor.setText("");
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 3;
			gridBagConstraints11.insets = new Insets(5, 5, 5, 1);
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.gridy = 0;
			jLabelTimeDisplay = new JLabel();
			jLabelTimeDisplay.setText("0 Sekunden");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.insets = new Insets(5, 5, 0, 5);
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.BOTH;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints9.gridx = 2;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.BOTH;
			gridBagConstraints8.gridy = 2;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(5, 0, 5, 0);
			gridBagConstraints8.gridx = 2;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.insets = new Insets(5, 5, 4, 5);
			gridBagConstraints7.gridy = 2;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 0;
			jPanelSimuInfos = new JPanel();
			jPanelSimuInfos.setLayout(new GridBagLayout());
			jPanelSimuInfos.add(getJLabelSpeed(), gridBagConstraints7);
			jPanelSimuInfos.add(getJLabelTime(), gridBagConstraints10);
			jPanelSimuInfos.add(getJSliderVisualation(), gridBagConstraints8);
			jPanelSimuInfos.add(getJSliderTime(), gridBagConstraints9);
			jPanelSimuInfos.add(jLabelTimeDisplay, gridBagConstraints11);
			jPanelSimuInfos.add(jLabelSpeedFactor, gridBagConstraints12);
			jPanelSimuInfos.add(getJButtonPlay(), gridBagConstraints13);
		}
		return jPanelSimuInfos;
	}

	/**
	 * This method initializes jButtonPlay	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonPlay() {
		if (jButtonPlay == null) {
			jButtonPlay = new JButton();
			jButtonPlay.setPreferredSize(new Dimension(45, 26));
			jButtonPlay.setIcon(new ImageIcon(getClass().getResource("/agentgui/core/gui/img/MBLoadPlay.png")));
			
		}
		return jButtonPlay;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
