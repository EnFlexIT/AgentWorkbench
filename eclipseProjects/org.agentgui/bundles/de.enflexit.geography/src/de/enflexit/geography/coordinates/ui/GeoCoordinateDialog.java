package de.enflexit.geography.coordinates.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import de.enflexit.common.SerialClone;
import de.enflexit.geography.coordinates.AbstractGeoCoordinate;
import de.enflexit.geography.coordinates.OSGBCoordinate;
import de.enflexit.geography.coordinates.UTMCoordinate;
import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;


/**
 * The Class GeoCoordinateDialog can be used in order to configure an geographical coordinate.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GeoCoordinateDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -7891622905468578296L;
	
	private AbstractGeoCoordinate geoCoordinateOld;
	private boolean isCancelAction;
	
	private JPanelGeoCoordinate jPanelGeoCoordinate;
	private JPanel jPanelOkCancel;
	private JButton jButtonOk;
	private JButton jButtonCancel;


	/**
	 * Instantiates a new GeoCoordinateDialog.
	 * @param owner the owner frame
	 * @wbp.parser.constructor
	 */
	public GeoCoordinateDialog(Frame owner) {
		super(owner);
		this.initialize();
	}
	/**
	 * Instantiates a new dialog to configure a geographical coordinate .
	 * @param owner the owner frame
	 * @param geoCoordinate the geo coordinate
	 */
	public GeoCoordinateDialog(Frame owner, AbstractGeoCoordinate geoCoordinate) {
		super(owner);
		this.setGeoCoordinate(geoCoordinate);
		this.initialize();
	}	

	/**
	 * Returns the current geographical  coordinate.
	 * @return the geographical coordinate
	 */
	public AbstractGeoCoordinate getGeoCoordinate() {
		if (this.isCancelAction==true) {
			return this.geoCoordinateOld;
		}
		return this.getJPanelGeoCoordinate().getGeoCoordinate();
	}
	/**
	 * Sets the current geographical coordinate.
	 * @param geoCoordinate the geographical coordinate to set
	 */
	public void setGeoCoordinate(AbstractGeoCoordinate geoCoordinate) {
		this.geoCoordinateOld = SerialClone.clone(geoCoordinate) ;
		this.getJPanelGeoCoordinate().setGeoCoordinate(geoCoordinate);
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {

		this.setTitle("Geographical Location");
		this.setSize(450, 350);
		this.setModal(true);
		this.getContentPane().add(this.getJPanelGeoCoordinate(), BorderLayout.CENTER);
		this.getContentPane().add(this.getJPanelOkCancel(), BorderLayout.SOUTH);
		this.registerEscapeKeyStroke();
		
		// --- Catch close Dialog -----------------------------------
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				doCancelAction();
			}
		});
		
		// --- Set Dialog position ----------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);		
		
		this.setVisible(true);
	}
	
	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
    			doCancelAction();
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
	/**
	 * Gets the JPanel to configure the geo coordinate.
	 * @return the JPanel to configure the geo coordinate.
	 */
	private JPanelGeoCoordinate getJPanelGeoCoordinate() {
		if (jPanelGeoCoordinate == null) {
			jPanelGeoCoordinate = new JPanelGeoCoordinate();
		}
		return jPanelGeoCoordinate;
	}
	/**
	 * Gets the JPanel for the OK and cancel action.
	 * @return the JPanel for the OK and cancel action
	 */
	private JPanel getJPanelOkCancel() {
		if (jPanelOkCancel == null) {
			jPanelOkCancel = new JPanel();
			GridBagLayout gbl_jPanelOkCancel = new GridBagLayout();
			gbl_jPanelOkCancel.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelOkCancel.rowHeights = new int[]{0, 0};
			gbl_jPanelOkCancel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelOkCancel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelOkCancel.setLayout(gbl_jPanelOkCancel);
			GridBagConstraints gbc_buttonOk = new GridBagConstraints();
			gbc_buttonOk.insets = new Insets(5, 20, 20, 0);
			gbc_buttonOk.gridx = 0;
			gbc_buttonOk.gridy = 0;
			jPanelOkCancel.add(getJButtonOK(), gbc_buttonOk);
			GridBagConstraints gbc_buttonCancel = new GridBagConstraints();
			gbc_buttonCancel.anchor = GridBagConstraints.NORTH;
			gbc_buttonCancel.insets = new Insets(5, 0, 0, 20);
			gbc_buttonCancel.gridx = 1;
			gbc_buttonCancel.gridy = 0;
			jPanelOkCancel.add(getJButtonCancel(), gbc_buttonCancel);
		}
		return jPanelOkCancel;
	}
	/**
	 * Gets the button ok.
	 * @return the button ok
	 */
	private JButton getJButtonOK() {
		if (jButtonOk == null) {
			jButtonOk = new JButton("OK");
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.setForeground(new Color(0, 153, 0));
			jButtonOk.setPreferredSize(new Dimension(100, 26));
			jButtonOk.setMaximumSize(jButtonOk.getPreferredSize());
			jButtonOk.setMinimumSize(jButtonOk.getPreferredSize());
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}
	/**
	 * Gets the button cancel.
	 * @return the button cancel
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setPreferredSize(new Dimension(100, 26));
			jButtonCancel.setMinimumSize(jButtonCancel.getPreferredSize());
			jButtonCancel.setMaximumSize(jButtonCancel.getPreferredSize());
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonOK()) {
			// --- OK-Action ------------------------------
			this.doOkAction();
		} else if (ae.getSource()==this.getJButtonCancel()) {
			// --- Cancel-Action --------------------------
			this.doCancelAction();
		}
	}
	/**
	 * Does the OK action.
	 */
	private void doOkAction() {
		this.isCancelAction = false;
		this.setVisible(false);
	}
	/**
	 * Does the cancel action.
	 */
	private void doCancelAction() {
		this.isCancelAction = true;
		this.setVisible(false);
	}

	/**
	 * Do some coordinate conversion tests.
	 */
	@SuppressWarnings("unused")
	private void doCoordinateTest() {

		// --------------------------------------
		// --- Test 1 ---------------------------
		// --------------------------------------
//		var lld1 = new LatLng(40.718119, -73.995667); // New York
//		document.write("New York Lat/Long: " + lld1.toString() + "<br />");
//		var lld2 = new LatLng(51.499981, -0.125313);  // London
//		document.write("London Lat/Long: " + lld2.toString() + "<br />");
//		var d = lld1.distance(lld2);
//		document.write("Surface Distance between New York and London: " + d + "km");
//
//		produces the following output:
//
//		New York Lat/Long: (40.718119, -73.995667)
//		London Lat/Long: (51.499981, -0.125313)
//		Surface Distance between New York and London: 5596.539266156427km
		
		WGS84LatLngCoordinate llc1 = new WGS84LatLngCoordinate(40.718119, -73.995667);
		System.out.println("New York Lat/Long: " + llc1);
		
		WGS84LatLngCoordinate llc2 = new WGS84LatLngCoordinate(51.499981, -0.125313);
		System.out.println("London Lat/Long: " + llc2);
		
		double distance = llc1.getDistanceInKm(llc2);
		System.out.println("Surface Distance between New York and London: " + distance + " km | London as OSGB coordinaate: " + llc2.getOSGBCoordinate().getSixFigureString());
		System.out.println();
		
		// --------------------------------------
		// --- Test 2 ---------------------------
		// --------------------------------------
//		Convert OS Grid Reference to Latitude/Longitude
//
//		Note that the OSGB-Latitude/Longitude conversions use the OSGB36 datum by default. The majority of applications use the WGS84 datum, for which the appropriate conversions need to be added. See the examples below to see the difference between the two data.
//
//		Using OSGB36 (convert an OSGB grid reference to a latitude and longitude using the OSGB36 datum):
//
//		var os1 = new OSRef(651409.903, 313177.270);
//		document.write("OS Grid Reference: " + os1.toString() + " - " + os1.toSixFigureString() + "<br />");
//		var ll1 = os1.toLatLng();
//		document.write("Converted to Lat/Long: " + ll1.toString());
//
//		produces the following output:
//
//		OS Grid Reference: (651409.903, 313177.27) - TG514131
//		Converted to Lat/Long: (52.657570301933156, 1.717921580645096)

		OSGBCoordinate os1 = new OSGBCoordinate(651409.903, 313177.270);
		System.out.println("OS Grid Reference: " + os1 + " - " + os1.getSixFigureString());

		WGS84LatLngCoordinate ll1 = os1.getWGS84LatLngCoordinate();
		System.out.println("Converted to Lat/Long: " + ll1);
		System.out.println();
		
		// --------------------------------------
		// --- Test 3 ---------------------------
		// --------------------------------------
//		Using WGS84 (convert an OSGB grid reference to a latitude and longitude using the WGS84 datum):
//		
//		var os1w = new OSRef(651409.903, 313177.270);
//		document.write("OS Grid Reference: " + os1w.toString() + " - " + os1w.toSixFigureString() + "<br />");
//		var ll1w = os1w.toLatLng(os1w);
//		ll1w.OSGB36ToWGS84();
//		document.write("Converted to Lat/Long: " + ll1w.toString());
//		
//		produces the following output:
//		
//		OS Grid Reference: (651409.903, 313177.27) - TG514131
//		Converted to Lat/Long: (52.65797559953351, 1.7160665447977752)

		OSGBCoordinate os1w = new OSGBCoordinate(651409.903, 313177.270);
		System.out.println("OS Grid Reference: " + os1w + " - " + os1w.getSixFigureString());
		
		WGS84LatLngCoordinate ll1w = os1w.getWGS84LatLngCoordinate();
		ll1w.convertOSGB36ToWGS84();;
		System.out.println("Converted to Lat/Long: " + ll1w.toString());
		System.out.println();

		// --------------------------------------
		// --- Test 4 ---------------------------
		// --------------------------------------
//		Convert Latitude/Longitude to OS Grid Reference
//
//		Note that the OSGB-Latitude/Longitude conversions use the OSGB36 datum by default. The majority of applications use the WGS84 datum, for which the appropriate conversions need to be added. See the examples below to see the difference between the two data.
//
//		Using OSGB36 (convert a latitude and longitude using the OSGB36 datum to an OSGB grid reference):
//
//		var ll2 = new LatLng(52.657570301933, 1.7179215806451);
//		document.write("Latitude/Longitude: " + ll2.toString() + "<br />");
//		var os2 = ll2.toOSRef();
//		document.write("Converted to OS Grid Ref: " + os2.toString() + " - " + os2.toSixFigureString());
//
//		produces the following output:
//
//		Latitude/Longitude: (52.657570301933, 1.7179215806451)
//		Converted to OS Grid Ref: (651409.902802228, 313177.26991869847) - TG514131
		
		WGS84LatLngCoordinate ll2 = new WGS84LatLngCoordinate(52.657570301933, 1.7179215806451);
		System.out.println("Latitude/Longitude: " + ll2);
		OSGBCoordinate os2 = ll2.getOSGBCoordinate();
		System.out.println("Converted to OS Grid Ref: " + os2.toString() + " - " + os2.getSixFigureString());
		System.out.println();

		// --------------------------------------
		// --- Test 5 ---------------------------
		// --------------------------------------
//		Using WGS84 (convert a latitude and longitude using the WGS84 datum to an OSGB grid reference):
//
//		var ll2w = new LatLng(52.657570301933, 1.7179215806451);
//		document.write("Latitude/Longitude: " + ll2.toString() + "<br />");
//		ll2w.WGS84ToOSGB36();
//		var os2w = ll2w.toOSRef();
//		document.write("Converted to OS Grid Ref: " + os2w.toString() + " - " + os2w.toSixFigureString());
//
//		produces the following output:
//
//		Latitude/Longitude: (52.657570301933, 1.7179215806451)
//		Converted to OS Grid Ref: (651537.6353407338, 313138.68699871836) - TG515131

		WGS84LatLngCoordinate ll2w = new WGS84LatLngCoordinate(52.657570301933, 1.7179215806451);
		System.out.println("Latitude/Longitude: " + ll2w);
		ll2w.convertWGS84ToOSGB36();
		OSGBCoordinate os2w = ll2w.getOSGBCoordinate();
		System.out.println("Converted to OS Grid Ref: " + os2w + " - " + os2w.getSixFigureString());
		System.out.println();
		
		// --------------------------------------
		// --- Test 6 ---------------------------
		// --------------------------------------
//		Convert Six-Figure OS Grid Reference String to an OSRef Object
//
//		To convert a string representing a six-figure OSGB grid reference:
//
//		var os6 = "TG514131";
//		document.write("Six figure string: " + os6 + "<br />");
//		var os6x = getOSRefFromSixFigureReference(os6);
//		document.write("Converted to OS Grid Ref: " + os6x.toString() + " - " + os6x.toSixFigureString());
//
//		produces the following output:
//
//		Six figure string: TG514131
//		Converted to OS Grid Ref: (651400, 313100) - TG514131 

		String os6 = "TG514131";
		System.out.println("Six figure string: " + os6);
		OSGBCoordinate os6x = new OSGBCoordinate(os6);
		System.out.println("Converted to OS Grid Ref: " + os6x + " - " + os6x.getSixFigureString());
		System.out.println();
		
		// --------------------------------------
		// --- Test 7 --------------------------- 
		// --------------------------------------
//		Convert UTM Reference to Latitude/Longitude
//
//		var utm1 = new UTMRef(456463.99, 3335334.05, "E", 12);
//		document.write("UTM Reference: " + utm1.toString() + "<br />");
//		var ll3 = utm1.toLatLng();
//		document.write("Converted to Lat/Long: " + ll3.toString());
//
//		produces the following output:
//
//		UTM Reference: 12E 456463.99 3335334.05
//		Converted to Lat/Long: (-60.1166999854101, -111.78330000822082)
//		Convert Latitude/Longitude to UTM Reference

		UTMCoordinate utm1 = new UTMCoordinate(12, "E", 456463.99, 3335334.05);
		System.out.println("UTM Reference: " + utm1.toString());
		WGS84LatLngCoordinate ll3 = utm1.getWGS84LatLngCoordinate();
		System.out.println("Converted to Lat/Long: " + ll3);
		System.out.println();

		// --------------------------------------
		// --- Test 8 --------------------------- 
		// --------------------------------------
//		var ll4 = new LatLng(-60.1167, -111.7833);
//		document.write("Latitude/Longitude: " + ll4.toString() + "<br />");
//		var utm2 = ll4.toUTMRef();
//		document.write("Converted to UTM Ref: " + utm2.toString());
//
//		produces the following output:
//
//		Latitude/Longitude: (-60.1167, -111.7833)
//		Converted to UTM Ref: 12E 456463.9904761317 3335334.047855188

		WGS84LatLngCoordinate ll4 = new WGS84LatLngCoordinate(-60.1167, -111.7833);
		System.out.println("Latitude/Longitude: " + ll4);
		UTMCoordinate  utm2 = ll4.getUTMCoordinate();
		System.out.println("Converted to UTM Ref: " + utm2);
		System.out.println();
		

		// --------------------------------------
		// --- test 9: Olbrichstr.56  ----------- 
		// --------------------------------------
		
		WGS84LatLngCoordinate llOlbrich = new WGS84LatLngCoordinate(51.443673, 7.028523);
		System.out.println("Latitude/Longitude: " + llOlbrich);
		UTMCoordinate  utmOlbrich = llOlbrich.getUTMCoordinate();
		System.out.println("Converted to UTM Ref: " + utmOlbrich);
		System.out.println();

		
		
	}
	
	
	
}
