package org.awb.env.networkModel.controller.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.geom.Point2D;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.common.swing.KeyAdapter4Numbers;

/**
 * The Class GraphNodePositionPanel can be used to configure a GraphNode position.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphNodePositionPanel extends JPanel implements DocumentListener {
	
	private static final long serialVersionUID = -7757305104129949134L;
	
	private Point2D graphNodePosition;
	
	private KeyAdapter4Numbers numberKeyListenerDouble;
	private boolean pauseDocumentListener;
	
	private JLabel jLabelHeader;

	private JPanel jPanelPositioning;
	private JLabel jLabelXPostition;
	private JTextField jTextFieldXPostition;
	private JLabel jLabelYPostition;
	private JTextField jTextFieldYPostition;
	private JLabel jLablePositionInfo;
	
	/**
	 * Instantiates a new JPanel to configure a geographical coordinate.
	 */
	public GraphNodePositionPanel() {
		initialize();
	}
	/** Initialize the panel. */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.insets = new Insets(15, 15, 5, 15);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		add(getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jPanelPositioning = new GridBagConstraints();
		gbc_jPanelPositioning.insets = new Insets(0, 15, 15, 15);
		gbc_jPanelPositioning.fill = GridBagConstraints.BOTH;
		gbc_jPanelPositioning.gridx = 0;
		gbc_jPanelPositioning.gridy = 1;
		add(this.getJPanelPositioning(), gbc_jPanelPositioning);
	}

	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("GraphNode - Positioning");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	private JPanel getJPanelPositioning() {
		if (jPanelPositioning == null) {
			jPanelPositioning = new JPanel();
			GridBagLayout gbl_jPanelPositioning = new GridBagLayout();
			gbl_jPanelPositioning.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelPositioning.rowHeights = new int[]{0, 0, 0, 0};
			gbl_jPanelPositioning.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelPositioning.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
			jPanelPositioning.setLayout(gbl_jPanelPositioning);
			GridBagConstraints gbc_jLabelXPosition = new GridBagConstraints();
			gbc_jLabelXPosition.anchor = GridBagConstraints.EAST;
			gbc_jLabelXPosition.insets = new Insets(10, 0, 0, 0);
			gbc_jLabelXPosition.gridx = 0;
			gbc_jLabelXPosition.gridy = 0;
			jPanelPositioning.add(getJLabelXPostition(), gbc_jLabelXPosition);
			GridBagConstraints gbc_jTextFieldXPosition = new GridBagConstraints();
			gbc_jTextFieldXPosition.insets = new Insets(10, 5, 0, 0);
			gbc_jTextFieldXPosition.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldXPosition.gridx = 1;
			gbc_jTextFieldXPosition.gridy = 0;
			jPanelPositioning.add(getJTextFieldXPostition(), gbc_jTextFieldXPosition);
			GridBagConstraints gbc_jLabelYPosition = new GridBagConstraints();
			gbc_jLabelYPosition.insets = new Insets(10, 0, 0, 0);
			gbc_jLabelYPosition.anchor = GridBagConstraints.EAST;
			gbc_jLabelYPosition.gridx = 0;
			gbc_jLabelYPosition.gridy = 1;
			jPanelPositioning.add(getJLabelYPostition(), gbc_jLabelYPosition);
			GridBagConstraints gbc_jTextFieldYPosition = new GridBagConstraints();
			gbc_jTextFieldYPosition.insets = new Insets(10, 5, 0, 0);
			gbc_jTextFieldYPosition.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldYPosition.gridx = 1;
			gbc_jTextFieldYPosition.gridy = 1;
			jPanelPositioning.add(getJTextFieldYPostition(), gbc_jTextFieldYPosition);
			GridBagConstraints gbc_jLablePositionInfo = new GridBagConstraints();
			gbc_jLablePositionInfo.fill = GridBagConstraints.BOTH;
			gbc_jLablePositionInfo.insets = new Insets(0, 0, 0, 0);
			gbc_jLablePositionInfo.gridwidth = 2;
			gbc_jLablePositionInfo.gridx = 0;
			gbc_jLablePositionInfo.gridy = 2;
			jPanelPositioning.add(getJLablePositionInfo(), gbc_jLablePositionInfo);
		}
		return jPanelPositioning;
	}
	private JLabel getJLabelXPostition() {
		if (jLabelXPostition == null) {
			jLabelXPostition = new JLabel("x-Postion:");
			jLabelXPostition.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelXPostition;
	}
	private JLabel getJLabelYPostition() {
		if (jLabelYPostition == null) {
			jLabelYPostition = new JLabel("y-Postion:");
			jLabelYPostition.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelYPostition;
	}
	private JTextField getJTextFieldXPostition() {
		if (jTextFieldXPostition == null) {
			jTextFieldXPostition = new JTextField();
			jTextFieldXPostition.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldXPostition.setColumns(15);
			jTextFieldXPostition.addKeyListener(this.getNumberKeyListenerDouble());
			jTextFieldXPostition.getDocument().addDocumentListener(this);
		}
		return jTextFieldXPostition;
	}
	private JTextField getJTextFieldYPostition() {
		if (jTextFieldYPostition == null) {
			jTextFieldYPostition = new JTextField();
			jTextFieldYPostition.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldYPostition.setColumns(15);
			jTextFieldYPostition.addKeyListener(this.getNumberKeyListenerDouble());
			jTextFieldYPostition.getDocument().addDocumentListener(this);
		}
		return jTextFieldYPostition;
	}
	private JLabel getJLablePositionInfo() {
		if (jLablePositionInfo == null) {
			String wsgInfo = "<html><center>Postioning strings may be pasted from the clipboard<br>using STRG+V in the format <b>51.5, 7.25</b>.</center></html>";
			jLablePositionInfo = new JLabel(wsgInfo);
			jLablePositionInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLablePositionInfo.setHorizontalAlignment(JLabel.CENTER);
		}
		return jLablePositionInfo;
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
		
		Point2D graphNodePosition = null; 
		if (de.getDocument() == this.getJTextFieldXPostition().getDocument()) {
			// --- Set the x-position value ------------------------- 
			if (this.isFullGraphNodePosition(this.getJTextFieldXPostition().getText())==false) {
				Double number = this.getDoubleValueFromString(this.getJTextFieldXPostition().getText());
				if (number!=null) {
					Point2D currPosition = this.getGraphNodePosition(true);
					currPosition.setLocation(number, currPosition.getY());
					graphNodePosition = currPosition;
				}
			}
			
		} else if (de.getDocument() == this.getJTextFieldYPostition().getDocument()) {
			// --- Set the y-position value -------------------------
			if (this.isFullGraphNodePosition(this.getJTextFieldYPostition().getText())==false) {
				Double number = this.getDoubleValueFromString(this.getJTextFieldYPostition().getText());
				if (number!=null) {
					Point2D currPosition = this.getGraphNodePosition(true);
					currPosition.setLocation(currPosition.getX(), number);
					graphNodePosition = currPosition;
				}
			}
			
		} 
		
		// --- Set new coordinate? ----------------------------------
		if (graphNodePosition!=null && graphNodePosition.equals(this.graphNodePosition)==false) {
			this.graphNodePosition = graphNodePosition;
			this.setGraphNodePositionToDisplay();
		}
		
	}
	
	/**
	 * Returns the current GraphNode position.
	 * @return the geographical coordinate
	 */
	public Point2D getGraphNodePosition() {
		return this.graphNodePosition;
	}
	/**
	 * Sets the current GraphNode position.
	 * @param newGraphNodePosition the new graph node position
	 */
	public void setGraphNodePosition(Point2D newGraphNodePosition) {
		this.graphNodePosition = newGraphNodePosition;
		this.setGraphNodePositionToDisplay();
	}
	
	/**
	 * Returns the GraphNodePosition from the current settings.
	 *
	 * @param createCoordinateIfRequired the create coordinate if required
	 * @return the graph node position
	 */
	private Point2D getGraphNodePosition(boolean createCoordinateIfRequired) {
		
		Point2D nodePos = this.getGraphNodePosition();
		if (nodePos==null && createCoordinateIfRequired==true) {
			// --- Create default Point2D -------
			nodePos = new Point2D.Double(0, 0);
			this.setGraphNodePosition(nodePos);
		}
		return nodePos;
	}
	
	/**
	 * Sets the graph node position to display.
	 */
	private void setGraphNodePositionToDisplay() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				pauseDocumentListener = true;
				Point2D graphNodePosition = getGraphNodePosition(false);
				if (graphNodePosition!=null) {
					getJTextFieldXPostition().setText("" + graphNodePosition.getX());
					getJTextFieldYPostition().setText("" + graphNodePosition.getY());
				} else {
					getJTextFieldXPostition().setText(null);
					getJTextFieldYPostition().setText(null);
				}
				pauseDocumentListener = false;
			}
		});
	}
	/**
	 * Checks if the specified string is full Point2D description.
	 *
	 * @param text the text
	 * @return true, if the text is a full Point2D description
	 */
	private boolean isFullGraphNodePosition(String text) {
		boolean isFullGraphNodePosition = false;
		if (text.contains(",") && text.split(",").length==2) {
			// --- Try to convert to latitude - longitude -----------
			try {
				String[] textParts = text.split(",");
				String xPosString = textParts[0].trim();
				String yPosString = textParts[1].trim();
				
				Double xPos = Double.parseDouble(xPosString);
				Double yPos = Double.parseDouble(yPosString);
				
				Point2D newPosition = new Point2D.Double(xPos, yPos);
				this.setGraphNodePosition(newPosition);
				isFullGraphNodePosition = true;
				
			} catch (Exception ex) {
				System.err.println("Could not convert '" + text + "' to Point2D!");
				//ex.printStackTrace();
			}
			
		} else {
			// --- Check if this is a double number to parse --------
			if (this.getDoubleValueFromString(text)==null) {
				// --- Create an empty Point2D ----------------------
				this.setGraphNodePosition(new Point2D.Double(0, 0));
				isFullGraphNodePosition = true;
			}
			
		}
		return isFullGraphNodePosition;
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
