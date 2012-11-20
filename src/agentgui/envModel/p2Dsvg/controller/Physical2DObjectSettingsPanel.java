/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.envModel.p2Dsvg.controller;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.w3c.dom.Element;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.envModel.p2Dsvg.ontology.ActiveObject;
import agentgui.envModel.p2Dsvg.ontology.PassiveObject;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.ontology.PlaygroundObject;
import agentgui.envModel.p2Dsvg.ontology.Position;
import agentgui.envModel.p2Dsvg.ontology.Scale;
import agentgui.envModel.p2Dsvg.ontology.Size;
import agentgui.envModel.p2Dsvg.ontology.StaticObject;
import agentgui.envModel.p2Dsvg.utils.EnvironmentHelper;
/** This class is a panel, which provides an input mask for the properties of an object which is used in the environment.
* @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
*/  
public class Physical2DObjectSettingsPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private JLabel lblId = null;
	private JLabel lblMaxSpeed = null;
	private JLabel lblType = null;
	private JLabel lblClass = null;
	private JLabel lblPosition = null;
	private JLabel lblSize = null;
	private JButton btnApply = null;
	private JButton btnRemove = null;
	private JTextField tfWidth = null;
	private JTextField tfHeight = null;
	private JLabel lblSizeSeparator = null;
	private JTextField tfXPos = null;
	private JTextField tfYPos = null;
	private JTextField tfMaxSpeed = null;
	private JLabel lblPosSeparator = null;
	private JTextField tfId = null;
	private JComboBox cbOntologyClass = null;
	private JLabel lblUnit1 = null;
	private JLabel lblUnit2 = null;
	private JLabel lblSpeedUnit = null;
	/**
	 * Assigns ontology classes to object types. 
	 */
	private HashMap<String, Class<?>> typeClass = null;
	
	/**
	 * The EnvironmentSetup instance containing this EnvironmentSetupObjectSettings instance
	 */
	private Physical2DEnvironmentControllerGUI parent = null;
	
	private JTextField tfAgentClass = null;
	private JButton btnSetAgentClass = null;
	/**
	 * This is the default constructor
	 */
	public Physical2DObjectSettingsPanel(Physical2DEnvironmentControllerGUI parent) {
		super();
		this.parent = parent;
		
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		lblUnit2 = new JLabel();
		lblUnit2.setText("m");
		lblUnit2.setLocation(new Point(131, 243));
		lblUnit2.setSize(lblUnit2.getPreferredSize());
		lblUnit1 = new JLabel();
		lblUnit1.setText("m");
		lblUnit1.setLocation(new Point(131, 203));
		lblUnit1.setSize(lblUnit1.getPreferredSize());
		lblSpeedUnit = new JLabel();
		lblSpeedUnit.setText("m/s");
		lblSpeedUnit.setLocation(new Point(75, 159));
		lblSpeedUnit.setSize(lblSpeedUnit.getPreferredSize());
		lblPosSeparator = new JLabel();
		lblPosSeparator.setText(":");
		lblPosSeparator.setLocation(new Point(70, 203));
		lblPosSeparator.setSize(lblPosSeparator.getPreferredSize());
		lblSizeSeparator = new JLabel();
		lblSizeSeparator.setText("x");
		lblSizeSeparator.setLocation(new Point(70, 243));
		lblSizeSeparator.setSize(lblSizeSeparator.getPreferredSize());
		lblSize = new JLabel();
		lblSize.setText(Language.translate("Größe"));
		lblSize.setLocation(new Point(15, 226));
		lblSize.setSize(lblSize.getPreferredSize());		
		lblPosition = new JLabel();
		lblPosition.setText(Language.translate("Position"));
		lblPosition.setLocation(new Point(16, 186));
		lblPosition.setSize(lblPosition.getPreferredSize());
		lblType = new JLabel();
		lblType.setText(Language.translate("Typ"));
		lblType.setLocation(new Point(10, 50));
		lblType.setSize(lblType.getPreferredSize());
		lblClass = new JLabel();
		lblClass.setText(Language.translate("Agenten-Klasse"));
		lblClass.setLocation(new Point(10, 80));
		lblClass.setSize(lblClass.getPreferredSize());
		lblMaxSpeed = new JLabel();
		lblMaxSpeed.setText(Language.translate("Maximale Geschwindigkeit"));
		lblMaxSpeed.setLocation(new Point(10, 134));
		lblMaxSpeed.setSize(lblMaxSpeed.getPreferredSize());
		lblId = new JLabel();
		lblId.setText("ID");
		lblId.setLocation(new Point(10, 15));
		lblId.setSize(lblId.getPreferredSize());
		this.setLayout(null);
		this.setSize(new Dimension(200, 450));
		this.add(lblId, null);
		this.add(lblSpeedUnit, null);
		this.add(lblClass, null);
		this.add(lblType, null);
		this.add(lblPosition, null);
		this.add(lblSize, null);
		this.add(lblMaxSpeed, null);
		this.add(getBtnApply(), null);
		this.add(getBtnRemove(), null);
		this.add(getTfWidth(), null);
		this.add(getTfHeight(), null);
		this.add(lblSizeSeparator, null);
		this.add(getTfXPos(), null);
		this.add(getTfYPos(), null);
		this.add(lblPosSeparator, null);
		this.add(getTfId(), null);
		this.add(getOntologyClass(), null);
		this.add(getTfMaxSpeed(), null);
		this.add(lblUnit1, null);
		this.add(lblUnit2, null);
		this.add(getTfAgentClass(), null);
		this.add(getBtnSetAgentClass(), null);
	}

	/**
	 * This method initializes btnSetAgentClass	
	 * 	
	 * @return javax.swing.JButton	
	 */
	JButton getBtnSetAgentClass() {
		if (btnSetAgentClass == null) {
			btnSetAgentClass = new JButton();
			btnSetAgentClass.setIcon(new ImageIcon(getClass().getResource(Application.getGlobalInfo().PathImageIntern() + "Search.png")));
			btnSetAgentClass.setSize(new Dimension(45, 26));
			btnSetAgentClass.setLocation(new Point(140, 100));
			btnSetAgentClass.addActionListener(parent);
			btnSetAgentClass.setEnabled(false);
		}
		return btnSetAgentClass;
	}

	/**
	 * This method initializes btnApply	
	 * 	
	 * @return javax.swing.JButton	
	 */
	JButton getBtnApply() {
		if (btnApply == null) {
			btnApply = new JButton();
			btnApply.setText(Language.translate("Anwenden"));
			btnApply.setSize(new Dimension(150, 26));
			btnApply.setLocation(new Point(16, 270));
			btnApply.addActionListener(parent);
			btnApply.setEnabled(false);
		}
		return btnApply;
	}

	/**
	 * This method initializes btnRemove	
	 * 	
	 * @return javax.swing.JButton	
	 */
	JButton getBtnRemove() {
		if (btnRemove == null) {
			btnRemove = new JButton();
			btnRemove.setText(Language.translate("Objekt Entfernen"));
			btnRemove.setSize(new Dimension(150, 26));
			btnRemove.setLocation(new Point(16, 300));
			btnRemove.setEnabled(false);
			btnRemove.setSize(new Dimension(150, 26));
			btnRemove.addActionListener(parent);		
		}
		return btnRemove;
	}

	/**
	 * This method initializes tfAgentClass	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	JTextField getTfAgentClass() {
		if (tfAgentClass == null) {
			tfAgentClass = new JTextField();
			tfAgentClass.setLocation(new Point(16, 100));
			tfAgentClass.setText(Language.translate("Keine"));
			tfAgentClass.setSize(new Dimension(120, 25));
//			tfAgentClass.setEditable(false);
			tfAgentClass.setEnabled(false);
		}
		return tfAgentClass;
	}

	/**
	 * This method initializes tfWidth	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	JTextField getTfWidth() {
		if (tfWidth == null) {
			tfWidth = new JTextField();
			tfWidth.setSize(new Dimension(50, 25));
			tfWidth.setLocation(new Point(15, 240));
			tfWidth.setEnabled(false);
		}
		return tfWidth;
	}

	/**
	 * This method initializes tfHeight	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	JTextField getTfHeight() {
		if (tfHeight == null) {
			tfHeight = new JTextField();
			tfHeight.setSize(new Dimension(50, 25));
			tfHeight.setLocation(new Point(80, 240));
			tfHeight.setEnabled(false);
		}
		return tfHeight;
	}

	/**
	 * This method initializes tfXPos	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	JTextField getTfXPos() {
		if (tfXPos == null) {
			tfXPos = new JTextField();
			tfXPos.setLocation(new Point(15, 200));
			tfXPos.setSize(new Dimension(50, 25));
			tfXPos.setEnabled(false);
		}
		return tfXPos;
	}

	/**
	 * This method initializes tfYPos	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	JTextField getTfYPos() {
		if (tfYPos == null) {
			tfYPos = new JTextField();
			tfYPos.setLocation(new Point(80, 200));
			tfYPos.setSize(new Dimension(50, 25));
			tfYPos.setEnabled(false);
		}
		return tfYPos;
	}

	/**
	 * This method initializes tfId	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	JTextField getTfId() {
		if (tfId == null) {
			tfId = new JTextField();
			tfId.setLocation(new Point(48, 14));
			tfId.setSize(new Dimension(120, 25));
			tfId.setEnabled(false);
		}
		return tfId;
	}

	/**
	 * This method initializes cbType	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	JComboBox getOntologyClass() {
		if (cbOntologyClass == null) {
			
			cbOntologyClass = new JComboBox();
			cbOntologyClass.setLocation(new Point(50, 47));
			cbOntologyClass.setSize(new Dimension(120, 25));
			cbOntologyClass.setEnabled(false);
			
			typeClass = new HashMap<String, Class<?>>();
			typeClass.put(Language.translate(Physical2DEnvironmentControllerGUI.TYPE_STRING_NO_TYPE), null);
			typeClass.put(Language.translate(Physical2DEnvironmentControllerGUI.TYPE_STRING_ACTIVE_OBJECT), ActiveObject.class);
			typeClass.put(Language.translate(Physical2DEnvironmentControllerGUI.TYPE_STRING_STATIC_OBJECT), StaticObject.class);
			typeClass.put(Language.translate(Physical2DEnvironmentControllerGUI.TYPE_STRING_PASSIVE_OBJECT), PassiveObject.class);
			typeClass.put(Language.translate(Physical2DEnvironmentControllerGUI.TYPE_STRING_PLAYGROUND_OBJECT), PlaygroundObject.class);
			
			cbOntologyClass.setModel(new DefaultComboBoxModel(typeClass.keySet().toArray()));
			cbOntologyClass.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(cbOntologyClass.getSelectedItem().equals(Language.translate(Physical2DEnvironmentControllerGUI.TYPE_STRING_ACTIVE_OBJECT))){
						getTfMaxSpeed().setEnabled(true);
						getTfAgentClass().setEnabled(true);
						getBtnSetAgentClass().setEnabled(true);
					}else{
						getTfMaxSpeed().setEnabled(false);
						getTfAgentClass().setEnabled(false);
						getBtnSetAgentClass().setEnabled(false);
					}
					if(cbOntologyClass.getSelectedItem().equals(Language.translate(Physical2DEnvironmentControllerGUI.TYPE_STRING_NO_TYPE))){
						getBtnApply().setEnabled(false);
					}else{
						getBtnApply().setEnabled(true);
					}
					
				}
				
			});			
		}
		return cbOntologyClass;
	}
	
	JTextField getTfMaxSpeed(){
		if(tfMaxSpeed == null){
			tfMaxSpeed = new JTextField();
			tfMaxSpeed.setText("10.0");
			tfMaxSpeed.setLocation(new Point(16, 155));
			tfMaxSpeed.setSize(new Dimension(51, 25));
			tfMaxSpeed.setEnabled(false);
		}
		return tfMaxSpeed;
	}

	/**
	 * Sets the values of the input components according to the given SVG element
	 * @param elem
	 */
	void setInputValues(Element elem){
		if(elem != null){
			
			Scale scale = parent.getP2DController().getEnvironmentDataObject().getScale();
			
			getTfId().setText(elem.getAttributeNS(null, "id"));
			
			Size size = EnvironmentHelper.getSizeFromElement(elem, scale);
			getTfWidth().setText(""+size.getWidth());
			getTfHeight().setText(""+size.getHeight());
			
			Position pos = EnvironmentHelper.getPosFromElement(elem, scale);
			getTfXPos().setText(""+pos.getXPos());
			getTfYPos().setText(""+pos.getYPos());
			
			Physical2DObject object = parent.getP2DController().getEnvWrap().getObjectById(elem.getAttributeNS(null, "id"));
			setObjectType(object);
			
			if(object instanceof ActiveObject){
				String agentClass = ((ActiveObject)object).getAgentClassName();
				getTfAgentClass().setText(agentClass);
			}else{
				getTfAgentClass().setText(Language.translate("Keine"));
			}
			
		}else{
			getTfId().setText("");
			getTfAgentClass().setText(Language.translate("Keine"));
			getTfXPos().setText("");
			getTfYPos().setText("");
			getTfWidth().setText("");
			getTfHeight().setText("");
			getOntologyClass().setSelectedItem(Language.translate(Physical2DEnvironmentControllerGUI.TYPE_STRING_NO_TYPE));
			getTfMaxSpeed().setText("");
		}
	}
	
	private void setObjectType(Physical2DObject object){
		if(object == null){
			getOntologyClass().setSelectedItem(Language.translate(Physical2DEnvironmentControllerGUI.TYPE_STRING_NO_TYPE));
		}
		else if(object instanceof ActiveObject){
			getOntologyClass().setSelectedItem(Language.translate(Physical2DEnvironmentControllerGUI.TYPE_STRING_ACTIVE_OBJECT));
			getTfMaxSpeed().setText(""+((ActiveObject)object).getMaxSpeed());
		}else if(object instanceof PassiveObject){
			getOntologyClass().setSelectedItem(Language.translate(Physical2DEnvironmentControllerGUI.TYPE_STRING_PASSIVE_OBJECT));
		}else if(object instanceof StaticObject){
			getOntologyClass().setSelectedItem(Language.translate(Physical2DEnvironmentControllerGUI.TYPE_STRING_STATIC_OBJECT));
		}else if(object instanceof PlaygroundObject){
			getOntologyClass().setSelectedItem(Language.translate(Physical2DEnvironmentControllerGUI.TYPE_STRING_PLAYGROUND_OBJECT));
		}
	}
	
	/**
	 * This method returns a HashMap containing object settings defined by the input's current values
	 * @return HashMap containing object settings
	 */
	HashMap<String, Object> getObjectProperties(){
		HashMap<String, Object> settings = new HashMap<String, Object>();
		
		settings.put(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_ID, tfId.getText());
		settings.put(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_ONTO_CLASS, typeClass.get(cbOntologyClass.getSelectedItem()));
		if(cbOntologyClass.getSelectedItem().equals(Language.translate(Physical2DEnvironmentControllerGUI.TYPE_STRING_ACTIVE_OBJECT))){
			settings.put(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_AGENT_MAX_SPEED, tfMaxSpeed.getText());
			settings.put(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_AGENT_CLASSNAME, getTfAgentClass().getText());
		}

		Position pos = new Position();
		pos.setXPos(Float.parseFloat(getTfXPos().getText()));
		pos.setYPos(Float.parseFloat(getTfYPos().getText()));
		settings.put(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_POSITION, pos);
		
		Size size = new Size();
		size.setWidth(Float.parseFloat(getTfWidth().getText()));
		size.setHeight(Float.parseFloat(getTfHeight().getText()));
		settings.put(Physical2DEnvironmentControllerGUI.SETTINGS_KEY_SIZE, size);
		
		return settings;
	}
	/**
	 * Enabling or disabling the major inputs
	 * @param enabled Enable or disable
	 */
	void enableControlls(boolean enabled){
		getTfId().setEnabled(enabled);
		getTfXPos().setEnabled(enabled);
		getTfYPos().setEnabled(enabled);
		getTfWidth().setEnabled(enabled);
		// For circles, the width is used to specify the radius 
		if(enabled && parent.selectedElement.getTagName().equals("circle")){
			getTfHeight().setEnabled(false);
		}else{
			getTfHeight().setEnabled(enabled);
		}
		getOntologyClass().setEnabled(enabled);
	}
	/**
	 * Sets the unit in 
	 * @param unit
	 */
	protected void setUnit(String unit){
		lblUnit1.setText(unit);
		lblUnit1.setSize(lblUnit1.getPreferredSize());
		lblUnit2.setText(unit);
		lblUnit2.setSize(lblUnit2.getPreferredSize());
		lblSpeedUnit.setText(unit+"/s");
		lblSpeedUnit.setSize(lblSpeedUnit.getPreferredSize());
	}
	
}
