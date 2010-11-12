package agentgui.physical2Denvironment.display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.w3c.dom.Document;

import agentgui.core.application.Application;

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
	
	/**
	 * This is the default constructor
	 */
	public BasicSVGGUI() {
		super();
		this.canvas = new JSVGCanvas();
		initialize();
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getPnlZoom(), BorderLayout.WEST);
		this.add(getScrollpane(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes pnlZoom	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlZoom() {
		if (pnlZoom == null) {
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
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(5, 5, 0, 5);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridx = 0;
			pnlZoom = new JPanel();
			pnlZoom.setLayout(new GridBagLayout());
			pnlZoom.add(getBtnZoomIn(), gridBagConstraints);
			pnlZoom.add(getBtnZoomOut(), gridBagConstraints1);
			pnlZoom.add(getBtnZoomReset(), gridBagConstraints2);
			pnlZoom.add(getJPanelDummy(), gridBagConstraints3);
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
			this.setPreferredSize(new Dimension((int)svgWidth, (int) (svgHeight+getPnlZoom().getPreferredSize().getHeight())));
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
			jPanelDummy = new JPanel();
			jPanelDummy.setLayout(new GridBagLayout());
			jPanelDummy.setPreferredSize(new Dimension(45, 20));
		}
		return jPanelDummy;
	}
}
