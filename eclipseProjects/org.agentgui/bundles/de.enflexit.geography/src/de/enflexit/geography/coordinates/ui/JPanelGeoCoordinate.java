/*
 * 
 */
package de.enflexit.geography.coordinates.ui;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.common.swing.AwbBasicTabbedPaneUI;
import de.enflexit.common.swing.KeyAdapter4Numbers;
import de.enflexit.geography.BundleHelper;
import de.enflexit.geography.coordinates.AbstractGeoCoordinate;
import de.enflexit.geography.coordinates.UTMCoordinate;
import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;

import javax.swing.JButton;


/**
 * The Class JPanelGeoCoordinate can be used in order to configure an {@link AbstractGeoCoordinate}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JPanelGeoCoordinate extends JPanel implements ActionListener, DocumentListener {
	
	private static final long serialVersionUID = -7757305104129949134L;
	
	private AbstractGeoCoordinate geoCoordinate;
	
	private KeyAdapter4Numbers numberKeyListenerDouble;
	private boolean pauseDocumentListener;
	
	private JLabel jLabelHeader;
	private JButton jButtonSetNull;
	private JButton jButtonMapsGoogle;
	private JButton jButtonOSM;

	private JTabbedPane jTabbedPane;
	private JPanel jPanelWGS84;
	private JLabel jLabelWGSLatitude;
	private JTextField jTextFieldWGSLatitude;
	private JLabel jLabelWGSLongitude;
	private JTextField jTextFieldWGSLongitude;
	
	private JPanel jPanelUTM;
	
	/**
	 * Instantiates a new JPanel to configure a geographical coordinate.
	 */
	public JPanelGeoCoordinate() {
		initialize();
	}
	/**
	 * Initialise the panel.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.insets = new Insets(10, 10, 5, 10);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		add(getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jButtonSetNull = new GridBagConstraints();
		gbc_jButtonSetNull.insets = new Insets(10, 0, 5, 20);
		gbc_jButtonSetNull.gridx = 1;
		gbc_jButtonSetNull.gridy = 0;
		add(getJButtonSetNull(), gbc_jButtonSetNull);
		GridBagConstraints gbc_jButtonMapsGoogle = new GridBagConstraints();
		gbc_jButtonMapsGoogle.anchor = GridBagConstraints.EAST;
		gbc_jButtonMapsGoogle.insets = new Insets(10, 0, 5, 5);
		gbc_jButtonMapsGoogle.gridx = 2;
		gbc_jButtonMapsGoogle.gridy = 0;
		add(getJButtonMapsGoogle(), gbc_jButtonMapsGoogle);
		GridBagConstraints gbc_jButtonOSM = new GridBagConstraints();
		gbc_jButtonOSM.insets = new Insets(10, 0, 5, 10);
		gbc_jButtonOSM.gridx = 3;
		gbc_jButtonOSM.gridy = 0;
		add(getJButtonOSM(), gbc_jButtonOSM);
		GridBagConstraints gbc_jTabbedPane = new GridBagConstraints();
		gbc_jTabbedPane.gridwidth = 4;
		gbc_jTabbedPane.insets = new Insets(0, 10, 10, 10);
		gbc_jTabbedPane.fill = GridBagConstraints.BOTH;
		gbc_jTabbedPane.gridx = 0;
		gbc_jTabbedPane.gridy = 1;
		add(getJTabbedPane(), gbc_jTabbedPane);
	}

	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Coordinate");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	private JButton getJButtonSetNull() {
		if (jButtonSetNull == null) {
			jButtonSetNull = new JButton();
			jButtonSetNull.setToolTipText("Delete Coordinate");
			jButtonSetNull.setIcon(BundleHelper.getImageIcon("Delete.png"));
			jButtonSetNull.setPreferredSize(new Dimension(26, 26));
			jButtonSetNull.addActionListener(this);
		}
		return jButtonSetNull;
	}
	private JButton getJButtonMapsGoogle() {
		if (jButtonMapsGoogle == null) {
			jButtonMapsGoogle = new JButton();
			jButtonMapsGoogle.setToolTipText("Check on Google Maps ...");
			jButtonMapsGoogle.setIcon(BundleHelper.getImageIcon("MapsGoogle.png"));
			jButtonMapsGoogle.setPreferredSize(new Dimension(26, 26));
			jButtonMapsGoogle.addActionListener(this);
		}
		return jButtonMapsGoogle;
	}
	private JButton getJButtonOSM() {
		if (jButtonOSM == null) {
			jButtonOSM = new JButton();
			jButtonOSM.setToolTipText("Check on OpenStreetMap ...");
			jButtonOSM.setPreferredSize(new Dimension(26, 26));
			jButtonOSM.setIcon(BundleHelper.getImageIcon("MapsOSM.png"));
			jButtonOSM.addActionListener(this);
		}
		return jButtonOSM;
	}
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
			jTabbedPane.setBorder(null);
			jTabbedPane.setUI(new AwbBasicTabbedPaneUI());
			jTabbedPane.setFont(new Font("Dialog", Font.BOLD, 13));
			jTabbedPane.addTab("WGS84", null, getJPanelWGS84(), null);
			jTabbedPane.addTab("UTM", null, getJPanelUTM(), null);
		}
		return jTabbedPane;
	}
	private JPanel getJPanelWGS84() {
		if (jPanelWGS84 == null) {
			jPanelWGS84 = new JPanel();
			jPanelWGS84.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			GridBagLayout gbl_jPanelWGS84 = new GridBagLayout();
			gbl_jPanelWGS84.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelWGS84.rowHeights = new int[]{0, 0, 0};
			gbl_jPanelWGS84.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelWGS84.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			jPanelWGS84.setLayout(gbl_jPanelWGS84);
			GridBagConstraints gbc_jLabelWGSLatitude = new GridBagConstraints();
			gbc_jLabelWGSLatitude.anchor = GridBagConstraints.EAST;
			gbc_jLabelWGSLatitude.insets = new Insets(10, 10, 5, 5);
			gbc_jLabelWGSLatitude.gridx = 0;
			gbc_jLabelWGSLatitude.gridy = 0;
			jPanelWGS84.add(getJLabelWGSLatitude(), gbc_jLabelWGSLatitude);
			GridBagConstraints gbc_jTextFieldWGSLatitude = new GridBagConstraints();
			gbc_jTextFieldWGSLatitude.insets = new Insets(10, 5, 0, 0);
			gbc_jTextFieldWGSLatitude.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldWGSLatitude.gridx = 1;
			gbc_jTextFieldWGSLatitude.gridy = 0;
			jPanelWGS84.add(getJTextFieldWGSLatitude(), gbc_jTextFieldWGSLatitude);
			GridBagConstraints gbc_jLabelWGSLongitude = new GridBagConstraints();
			gbc_jLabelWGSLongitude.insets = new Insets(10, 10, 0, 5);
			gbc_jLabelWGSLongitude.anchor = GridBagConstraints.EAST;
			gbc_jLabelWGSLongitude.gridx = 0;
			gbc_jLabelWGSLongitude.gridy = 1;
			jPanelWGS84.add(getJLabelWGSLongitude(), gbc_jLabelWGSLongitude);
			GridBagConstraints gbc_jTextFieldLongitude = new GridBagConstraints();
			gbc_jTextFieldLongitude.insets = new Insets(10, 5, 0, 0);
			gbc_jTextFieldLongitude.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldLongitude.gridx = 1;
			gbc_jTextFieldLongitude.gridy = 1;
			jPanelWGS84.add(getJTextFieldWGSLongitude(), gbc_jTextFieldLongitude);
		}
		return jPanelWGS84;
	}
	private JPanel getJPanelUTM() {
		if (jPanelUTM == null) {
			jPanelUTM = new JPanel();
			jPanelUTM.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		}
		return jPanelUTM;
	}
	private JLabel getJLabelWGSLatitude() {
		if (jLabelWGSLatitude == null) {
			jLabelWGSLatitude = new JLabel("Latitude:");
			jLabelWGSLatitude.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelWGSLatitude;
	}
	private JLabel getJLabelWGSLongitude() {
		if (jLabelWGSLongitude == null) {
			jLabelWGSLongitude = new JLabel("Longitude:");
			jLabelWGSLongitude.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelWGSLongitude;
	}
	private JTextField getJTextFieldWGSLatitude() {
		if (jTextFieldWGSLatitude == null) {
			jTextFieldWGSLatitude = new JTextField();
			jTextFieldWGSLatitude.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldWGSLatitude.setColumns(15);
			jTextFieldWGSLatitude.addKeyListener(this.getNumberKeyListenerDouble());
			jTextFieldWGSLatitude.getDocument().addDocumentListener(this);
		}
		return jTextFieldWGSLatitude;
	}
	private JTextField getJTextFieldWGSLongitude() {
		if (jTextFieldWGSLongitude == null) {
			jTextFieldWGSLongitude = new JTextField();
			jTextFieldWGSLongitude.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldWGSLongitude.setColumns(15);
			jTextFieldWGSLongitude.addKeyListener(this.getNumberKeyListenerDouble());
			jTextFieldWGSLongitude.getDocument().addDocumentListener(this);
		}
		return jTextFieldWGSLongitude;
	}
	
	/**
	 * Gets the number key listern double.
	 * @return the number key listern double
	 */
	private KeyAdapter4Numbers getNumberKeyListenerDouble() {
		if (numberKeyListenerDouble==null) {
			numberKeyListenerDouble = new KeyAdapter4Numbers(true);
		}
		return numberKeyListenerDouble;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonMapsGoogle()) {
			// --- Browse to Google Maps ------------------
			WGS84LatLngCoordinate wgs84Coordinate = this.getWGS84LatLngCoordinate(false);
			if (wgs84Coordinate!=null) {
				String url = "https://maps.google.com/maps?q=loc:" + wgs84Coordinate.getLatitude() + "," + wgs84Coordinate.getLongitude() + "";
				this.browseTo(url);
			}
			
		} else if (ae.getSource()==this.getJButtonOSM()) {
			// --- Browse to OpenStreeMap -----------------
			WGS84LatLngCoordinate wgs84Coordinate = this.getWGS84LatLngCoordinate(false);
			if (wgs84Coordinate!=null) {
				String url = "http://www.openstreetmap.org/?mlat=" + wgs84Coordinate.getLatitude() + "&mlon=" + wgs84Coordinate.getLongitude() + "&zoom=18";
				this.browseTo(url);
			}
		
		} else if (ae.getSource()==this.getJButtonSetNull()) {
			// --- Set coordinate to null -----------------
			this.setGeoCoordinate(null);
		}
	}
	/**
	 * Tries to browse to the specified URL.
	 * @param url the URL to browse to
	 */
	private void browseTo(String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} catch (URISyntaxException uriEx) {
			uriEx.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent de) {
		this.takeTextFieldUpdate(de);
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent de) {
		this.takeTextFieldUpdate(de);
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent de) {
		this.takeTextFieldUpdate(de);
	}
	/**
	 * Takes a text field update.
	 * @param de the DocumentEvent
	 */
	private void takeTextFieldUpdate(DocumentEvent de) {
		
		if (this.pauseDocumentListener==true) return;
		
		if (de.getDocument() == this.getJTextFieldWGSLatitude().getDocument()) {
			// --- Set the WGS84 latitude value --------------------- 
			if (this.isFullWGS84Coordinate(this.getJTextFieldWGSLatitude().getText())==false) {
				Double number = this.getDoubleValueFromString(this.getJTextFieldWGSLatitude().getText());
				if (number!=null) this.getWGS84LatLngCoordinate(true).setLatitude(number);
			}
			
		} else if (de.getDocument() == this.getJTextFieldWGSLongitude().getDocument()) {
			// --- Set the WGS84 longitude value --------------------
			if (this.isFullWGS84Coordinate(this.getJTextFieldWGSLongitude().getText())==false) {
				Double number = this.getDoubleValueFromString(this.getJTextFieldWGSLongitude().getText());
				if (number!=null) this.getWGS84LatLngCoordinate(true).setLongitude(number);
			}
			
		}
		
	}
	
	
	/**
	 * Returns the current geographical coordinate.
	 * @return the geographical coordinate
	 */
	public AbstractGeoCoordinate getGeoCoordinate() {
		return this.geoCoordinate;
	}
	/**
	 * Sets the current geographical coordinate.
	 * @param newGeoCoordinate the new geographical coordinate
	 */
	public void setGeoCoordinate(AbstractGeoCoordinate newGeoCoordinate) {
		this.geoCoordinate = newGeoCoordinate;
		this.setWGS84CoordinateToDisplay();
	}
	
	/**
	 * Returns the WGS84LatLngCoordinate from the current settings.
	 *
	 * @param createCoordinateIfRequired the create coordinate if required
	 * @return the WGS 84 lat lng coordinate
	 */
	private WGS84LatLngCoordinate getWGS84LatLngCoordinate(boolean createCoordinateIfRequired) {
		
		WGS84LatLngCoordinate wgs84 = null;
		if (this.getGeoCoordinate()==null && createCoordinateIfRequired==true) {
			// --- Create WGS84LatLngCoordinate -----------
			wgs84 = new WGS84LatLngCoordinate(0, 0);
			this.setGeoCoordinate(wgs84);
		} else {
			if (this.getGeoCoordinate() instanceof WGS84LatLngCoordinate) {
				// --- Cast to the right type -------------
				wgs84 = (WGS84LatLngCoordinate) this.getGeoCoordinate();	
			} else if (this.getGeoCoordinate() instanceof UTMCoordinate) {
				// --- Convert to the right type ----------
				UTMCoordinate utm = (UTMCoordinate) this.getGeoCoordinate();
				wgs84 = utm.getWGS84LatLngCoordinate();
			}
		}
		return wgs84;
	}
	/**
	 * Sets the WGS 84 coordinate to the display.
	 */
	private void setWGS84CoordinateToDisplay() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				pauseDocumentListener = true;
				WGS84LatLngCoordinate wgs84 = getWGS84LatLngCoordinate(false);
				if (wgs84!=null) {
					getJTextFieldWGSLatitude().setText(((Double)wgs84.getLatitude()).toString());
					getJTextFieldWGSLongitude().setText(((Double)wgs84.getLongitude()).toString());
				} else {
					getJTextFieldWGSLatitude().setText(null);
					getJTextFieldWGSLongitude().setText(null);
				}
				pauseDocumentListener = false;
			}
		});
	}
	
	/**
	 * Checks if the specified string is full WGS 84 coordinate.
	 *
	 * @param text the text
	 * @return true, if is full WGS 84 coordinate
	 */
	private boolean isFullWGS84Coordinate(String text) {
		boolean isFullWGS84Coordinate = false;
		if (text.contains(",") && text.split(",").length==2) {
			// --- Try to convert to latitude - longitude -----------
			try {
				String[] textParts = text.split(",");
				String latitudeText = textParts[0].trim();
				String longitudeText = textParts[1].trim();
				
				Double latitude = Double.parseDouble(latitudeText);
				Double longitude = Double.parseDouble(longitudeText);
				
				WGS84LatLngCoordinate wgs84 = new WGS84LatLngCoordinate(latitude, longitude);
				this.setGeoCoordinate(wgs84);
				isFullWGS84Coordinate = true;
				
			} catch (Exception ex) {
				System.err.println("Could not convert '" + text + "' to WGS84 coordinate!");
				//ex.printStackTrace();
			}
			
		} else {
			// --- Check if this is a double number to parse --------
			if (this.getDoubleValueFromString(text)==null) {
				// --- Create an empty WGS84 coordinate -------------
				WGS84LatLngCoordinate wgs84 = new WGS84LatLngCoordinate(0, 0);
				this.setGeoCoordinate(wgs84);
				isFullWGS84Coordinate = true;
			}
			
		}
		return isFullWGS84Coordinate;
	}

	/**
	 * Gets the double value from the specified string.
	 *
	 * @param numberText the number text
	 * @return the double value from string
	 */
	private Double getDoubleValueFromString(String numberText) {
		Double number = 0.0;
		if (numberText!=null && numberText.equals("")==false) {
			try {
				number = Double.parseDouble(numberText);
			} catch (Exception ex) {
				System.err.println("Could not convert '" + numberText + "' to double!");
				return null;
			}
		}
		return number;
	}
	
}
