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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
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
	private JLabel jLabelUtmZone;
	private JLabel jLabelUtmEasting;
	private JLabel jLabelUtmNorthing;
	private JTextField jTextFieldUtmZone;
	private JTextField jTextFieldUtmEasting;
	private JTextField jTextFieldUtmNorthing;
	
	/**
	 * Instantiates a new JPanel to configure a geographical coordinate.
	 */
	public JPanelGeoCoordinate() {
		initialize();
	}
	/** Initialize the panel. */
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
			gbc_jLabelWGSLatitude.insets = new Insets(10, 10, 0, 0);
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
			gbc_jLabelWGSLongitude.insets = new Insets(10, 10, 0, 0);
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
	private JLabel getJLabelWGSLatitude() {
		if (jLabelWGSLatitude == null) {
			jLabelWGSLatitude = new JLabel("Latitude (North - South):");
			jLabelWGSLatitude.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelWGSLatitude;
	}
	private JLabel getJLabelWGSLongitude() {
		if (jLabelWGSLongitude == null) {
			jLabelWGSLongitude = new JLabel("Longitude (East - West):");
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
	
	
	private JPanel getJPanelUTM() {
		if (jPanelUTM == null) {
			jPanelUTM = new JPanel();
			jPanelUTM.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			GridBagLayout gbl_jPanelUTM = new GridBagLayout();
			gbl_jPanelUTM.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelUTM.rowHeights = new int[]{0, 0, 0, 0};
			gbl_jPanelUTM.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelUTM.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			jPanelUTM.setLayout(gbl_jPanelUTM);
			GridBagConstraints gbc_jLabelUtmZone = new GridBagConstraints();
			gbc_jLabelUtmZone.anchor = GridBagConstraints.EAST;
			gbc_jLabelUtmZone.insets = new Insets(10, 10, 0, 0);
			gbc_jLabelUtmZone.gridx = 0;
			gbc_jLabelUtmZone.gridy = 0;
			jPanelUTM.add(getJLabelUtmZone(), gbc_jLabelUtmZone);
			GridBagConstraints gbc_jTextFieldUtmZone = new GridBagConstraints();
			gbc_jTextFieldUtmZone.insets = new Insets(10, 5, 0, 0);
			gbc_jTextFieldUtmZone.gridx = 1;
			gbc_jTextFieldUtmZone.gridy = 0;
			jPanelUTM.add(getJTextFieldUtmZone(), gbc_jTextFieldUtmZone);
			GridBagConstraints gbc_jLabelUtmEasting = new GridBagConstraints();
			gbc_jLabelUtmEasting.anchor = GridBagConstraints.EAST;
			gbc_jLabelUtmEasting.insets = new Insets(10, 10, 0, 0);
			gbc_jLabelUtmEasting.gridx = 0;
			gbc_jLabelUtmEasting.gridy = 1;
			jPanelUTM.add(getJLabelUtmEasting(), gbc_jLabelUtmEasting);
			GridBagConstraints gbc_jTextFieldUtmEasting = new GridBagConstraints();
			gbc_jTextFieldUtmEasting.insets = new Insets(10, 5, 0, 0);
			gbc_jTextFieldUtmEasting.gridx = 1;
			gbc_jTextFieldUtmEasting.gridy = 1;
			jPanelUTM.add(getJTextFieldUtmEasting(), gbc_jTextFieldUtmEasting);
			GridBagConstraints gbc_jLabelUtmNorthing = new GridBagConstraints();
			gbc_jLabelUtmNorthing.anchor = GridBagConstraints.EAST;
			gbc_jLabelUtmNorthing.insets = new Insets(10, 10, 0, 0);
			gbc_jLabelUtmNorthing.gridx = 0;
			gbc_jLabelUtmNorthing.gridy = 2;
			jPanelUTM.add(getJLabelUtmNorthing(), gbc_jLabelUtmNorthing);
			GridBagConstraints gbc_jTextFieldNorthing = new GridBagConstraints();
			gbc_jTextFieldNorthing.insets = new Insets(10, 5, 0, 0);
			gbc_jTextFieldNorthing.gridx = 1;
			gbc_jTextFieldNorthing.gridy = 2;
			jPanelUTM.add(getJTextFieldUtmNorthing(), gbc_jTextFieldNorthing);
		}
		return jPanelUTM;
	}
	private JLabel getJLabelUtmZone() {
		if (jLabelUtmZone == null) {
			jLabelUtmZone = new JLabel("Zone:");
			jLabelUtmZone.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelUtmZone;
	}
	private JLabel getJLabelUtmEasting() {
		if (jLabelUtmEasting == null) {
			jLabelUtmEasting = new JLabel("Easting:");
			jLabelUtmEasting.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelUtmEasting;
	}
	private JLabel getJLabelUtmNorthing() {
		if (jLabelUtmNorthing == null) {
			jLabelUtmNorthing = new JLabel("Northing:");
			jLabelUtmNorthing.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelUtmNorthing;
	}
	private JTextField getJTextFieldUtmZone() {
		if (jTextFieldUtmZone == null) {
			jTextFieldUtmZone = new JTextField();
			jTextFieldUtmZone.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldUtmZone.setColumns(15);
			jTextFieldUtmZone.getDocument().addDocumentListener(this);
			jTextFieldUtmZone.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent kT) {

					char charackter = kT.getKeyChar();
					String singleChar = Character.toString(charackter);
					String singleCharUpperCase = singleChar.toUpperCase();
					if (singleChar.equals(singleCharUpperCase)==false) {
						kT.setKeyChar(singleCharUpperCase.charAt(0));
						singleChar = singleCharUpperCase;
					}
					if (singleChar.matches("[0-9A-Z]")==false) {
						kT.consume();
					}
					// --- Restrict the length ------------
					JTextField displayField = (JTextField) kT.getComponent();
					String currValue = displayField.getText();
					if (currValue.length()>=3) {
						kT.consume();
					}
				}
			});
		}
		return jTextFieldUtmZone;
	}
	private JTextField getJTextFieldUtmEasting() {
		if (jTextFieldUtmEasting == null) {
			jTextFieldUtmEasting = new JTextField();
			jTextFieldUtmEasting.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldUtmEasting.setColumns(15);
			jTextFieldUtmEasting.addKeyListener(this.getNumberKeyListenerDouble());
			jTextFieldUtmEasting.getDocument().addDocumentListener(this);

		}
		return jTextFieldUtmEasting;
	}
	private JTextField getJTextFieldUtmNorthing() {
		if (jTextFieldUtmNorthing == null) {
			jTextFieldUtmNorthing = new JTextField();
			jTextFieldUtmNorthing.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldUtmNorthing.setColumns(15);
			jTextFieldUtmNorthing.addKeyListener(this.getNumberKeyListenerDouble());
			jTextFieldUtmNorthing.getDocument().addDocumentListener(this);
		}
		return jTextFieldUtmNorthing;
	}
	
	
	/**
	 * Gets the number key listener double.
	 * @return the number key listener double
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
		
		AbstractGeoCoordinate geoCoordinateEdited = null; 
		if (de.getDocument() == this.getJTextFieldWGSLatitude().getDocument()) {
			// --- Set the WGS84 latitude value --------------------- 
			if (this.isFullWGS84Coordinate(this.getJTextFieldWGSLatitude().getText())==false) {
				Double number = this.getDoubleValueFromString(this.getJTextFieldWGSLatitude().getText());
				if (number!=null) {
					WGS84LatLngCoordinate wgsCoord = this.getWGS84LatLngCoordinate(true);
					wgsCoord.setLatitude(number);
					geoCoordinateEdited = wgsCoord;
				}
			}
			
		} else if (de.getDocument() == this.getJTextFieldWGSLongitude().getDocument()) {
			// --- Set the WGS84 longitude value --------------------
			if (this.isFullWGS84Coordinate(this.getJTextFieldWGSLongitude().getText())==false) {
				Double number = this.getDoubleValueFromString(this.getJTextFieldWGSLongitude().getText());
				if (number!=null) {
					WGS84LatLngCoordinate wgsCoord = this.getWGS84LatLngCoordinate(true);
					wgsCoord.setLongitude(number);
					geoCoordinateEdited = wgsCoord;
				}
			}
			
		} else if (de.getDocument() == this.getJTextFieldUtmZone().getDocument()) {
			// --- Set the UTM zone value ---------------------------
			if (this.isFullUTMCoordinate(this.getJTextFieldUtmZone().getText())==false) {
				String zoneText = this.getJTextFieldUtmZone().getText();
				if (zoneText!=null && zoneText.isEmpty()==false) {
					UTMCoordinate utmCoord = this.getUTMCoordinate(true);
					String lngZoneText = zoneText.replaceAll("[^0-9]", "");
					Integer lngZone = 0;
					if (lngZoneText!=null && lngZoneText.isEmpty()==false) {
						lngZone = Integer.parseInt(lngZoneText);
					}
					if (lngZone!=null) {
						utmCoord.setLongitudeZone(lngZone);
						geoCoordinateEdited = utmCoord;	
					}
					String latZone = zoneText.replaceAll("[^A-Z]", "");
					if (latZone!=null && latZone.isEmpty()==false) {
						utmCoord.setLatitudeZone(latZone.toUpperCase());
						geoCoordinateEdited = utmCoord;
					}
				}
			}
			
		} else if (de.getDocument() == this.getJTextFieldUtmEasting().getDocument()) {
			// --- Set the UTM easting value ------------------------
			if (this.isFullUTMCoordinate(this.getJTextFieldUtmEasting().getText())==false) {
				Double number = this.getDoubleValueFromString(this.getJTextFieldUtmEasting().getText());
				if (number!=null) {
					UTMCoordinate utmCoord = this.getUTMCoordinate(true); 
					utmCoord.setEasting(number);
					geoCoordinateEdited = utmCoord;
				}
			}
			
		} else if (de.getDocument() == this.getJTextFieldUtmNorthing().getDocument()) {
			// --- Set the UTM northing value -----------------------
			if (this.isFullUTMCoordinate(this.getJTextFieldUtmNorthing().getText())==false) {
				Double number = this.getDoubleValueFromString(this.getJTextFieldUtmNorthing().getText());
				if (number!=null) {
					UTMCoordinate utmCoord = this.getUTMCoordinate(true); 
					utmCoord.setNorthing(number);
					geoCoordinateEdited = utmCoord;
				}
			}
			
		} 
		
		// --- Set new coordinate? ----------------------------------
		if (geoCoordinateEdited!=null && geoCoordinateEdited!=this.geoCoordinate) {
			this.geoCoordinate = geoCoordinateEdited;
		}
		if (this.geoCoordinate instanceof WGS84LatLngCoordinate) {
			this.setUTMCoordinateToDisplay();
		} else if (this.geoCoordinate instanceof UTMCoordinate) {
			this.setWGS84CoordinateToDisplay();
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
		if (this.geoCoordinate instanceof WGS84LatLngCoordinate) {
			this.getJTabbedPane().setSelectedIndex(0);
		} else if (this.geoCoordinate instanceof UTMCoordinate) {
			this.getJTabbedPane().setSelectedIndex(1);
		}
		this.setWGS84CoordinateToDisplay();
		this.setUTMCoordinateToDisplay();
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
					getJTextFieldWGSLatitude().setText("" + wgs84.getLatitude());
					getJTextFieldWGSLongitude().setText("" + wgs84.getLongitude());
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
	 * Returns the UTMCoordinate from the current settings.
	 *
	 * @param createCoordinateIfRequired the create coordinate if required
	 * @return the UTM coordinate
	 */
	private UTMCoordinate getUTMCoordinate(boolean createCoordinateIfRequired) {
		
		UTMCoordinate utm = null;
		if (this.getGeoCoordinate()==null && createCoordinateIfRequired==true) {
			// --- Create WGS84LatLngCoordinate -----------
			utm = new UTMCoordinate(32, "U", 0, 0);
			this.setGeoCoordinate(utm);
		} else {
			if (this.getGeoCoordinate() instanceof UTMCoordinate) {
				// --- Cast to the right type -------------
				utm = (UTMCoordinate) this.getGeoCoordinate();	
			} else if (this.getGeoCoordinate() instanceof WGS84LatLngCoordinate) {
				// --- Convert to the right type ----------
				WGS84LatLngCoordinate wsg84 = (WGS84LatLngCoordinate) this.getGeoCoordinate();
				utm = wsg84.getUTMCoordinate();
			}
		}
		return utm;
	}
	/**
	 * Sets the UTM coordinate to the display.
	 */
	private void setUTMCoordinateToDisplay() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				pauseDocumentListener = true;
				UTMCoordinate utm = getUTMCoordinate(false);
				if (utm!=null) {
					getJTextFieldUtmZone().setText("" + utm.getLongitudeZone() + utm.getLatitudeZone());
					getJTextFieldUtmEasting().setText("" + utm.getEasting());
					getJTextFieldUtmNorthing().setText("" + utm.getNorthing());
				} else {
					getJTextFieldUtmZone().setText(null);
					getJTextFieldUtmEasting().setText(null);
					getJTextFieldUtmNorthing().setText(null);
				}
				pauseDocumentListener = false;
			}
		});
	}
	/**
	 * Checks if the specified string is full UTM coordinate.
	 *
	 * @param text the text
	 * @return true, if is a full UTM coordinate
	 */
	private boolean isFullUTMCoordinate(String text) {
		boolean isFullUTMCoordinate = false;
		if (text.contains(" ") && text.split(" ").length==3) {
			// --- Try to convert to UTM coordinate -----------------
			try {
				String[] textParts = text.split(" ");
				String zoneText = textParts[0].trim();
				String eastingText = textParts[1].trim();
				String nortingText = textParts[2].trim();
				
				Integer lngZone = Integer.parseInt(zoneText.replaceAll("[^0-9]", ""));
				String latZone = zoneText.replaceAll("[^A-Za-z]", "");
				
				Double easting = Double.parseDouble(eastingText);
				Double northing = Double.parseDouble(nortingText);
				
				UTMCoordinate utm = new UTMCoordinate(lngZone, latZone, easting, northing);
				this.setGeoCoordinate(utm);
				isFullUTMCoordinate = true;
				
			} catch (Exception ex) {
				System.err.println("Could not convert '" + text + "' to UTM coordinate!");
				//ex.printStackTrace();
			}
		}
		return isFullUTMCoordinate;
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
