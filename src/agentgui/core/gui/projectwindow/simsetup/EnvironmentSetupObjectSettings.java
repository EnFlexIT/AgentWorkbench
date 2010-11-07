package agentgui.core.gui.projectwindow.simsetup;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.w3c.dom.Element;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.physical2Denvironment.ontology.ActiveObject;
import agentgui.physical2Denvironment.ontology.PassiveObject;
import agentgui.physical2Denvironment.ontology.Physical2DObject;
import agentgui.physical2Denvironment.ontology.PlaygroundObject;
import agentgui.physical2Denvironment.ontology.Position;
import agentgui.physical2Denvironment.ontology.Scale;
import agentgui.physical2Denvironment.ontology.Size;
import agentgui.physical2Denvironment.ontology.StaticObject;
import agentgui.physical2Denvironment.utils.SVGHelper;

public class EnvironmentSetupObjectSettings extends JPanel{

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
	private JComboBox cbAgentClass = null;
	private JLabel lblUnit1 = null;
	private JLabel lblUnit2 = null;
	private JLabel lblSpeedUnit = null;
	/**
	 * This project's agent classes. Key = class name, value = full name with package 
	 */
	private HashMap<String, String> agentClasses = null;
	/**
	 * Assigns ontology classes to object types. 
	 */
	private HashMap<String, Class<?>> typeClass = null;
	/**
	 * The EnvironmentSetup instance containing this EnvironmentSetupObjectSettings instance
	 */
	private EnvironmentSetup parent = null;
	/**
	 * This is the default constructor
	 */
	public EnvironmentSetupObjectSettings(EnvironmentSetup parent) {
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
		lblUnit2.setLocation(new Point(145, 230));
		lblUnit2.setSize(lblUnit2.getPreferredSize());
		lblUnit1 = new JLabel();
		lblUnit1.setText("m");
		lblUnit1.setLocation(new Point(145, 190));
		lblUnit1.setSize(lblUnit1.getPreferredSize());
		lblSpeedUnit = new JLabel();
		lblSpeedUnit.setText("m/s");
		lblSpeedUnit.setLocation(new Point(135,135));
		lblSpeedUnit.setSize(lblSpeedUnit.getPreferredSize());
		lblPosSeparator = new JLabel();
		lblPosSeparator.setText(":");
		lblPosSeparator.setLocation(new Point(70, 188));
		lblPosSeparator.setSize(lblPosSeparator.getPreferredSize());
		lblSizeSeparator = new JLabel();
		lblSizeSeparator.setText("x");
		lblSizeSeparator.setLocation(new Point(70, 228));
		lblSizeSeparator.setSize(lblSizeSeparator.getPreferredSize());
		lblSize = new JLabel();
		lblSize.setText(Language.translate("Größe"));
		lblSize.setLocation(new Point(15, 211));
		lblSize.setSize(lblSize.getPreferredSize());		
		lblPosition = new JLabel();
		lblPosition.setText(Language.translate("Position"));
		lblPosition.setLocation(new Point(16, 171));
		lblPosition.setSize(lblPosition.getPreferredSize());
		lblType = new JLabel();
		lblType.setText(Language.translate("Typ"));
		lblType.setLocation(new Point(10, 50));
		lblType.setSize(lblType.getPreferredSize());
		lblClass = new JLabel();
		lblClass.setText(Language.translate("Klasse"));
		lblClass.setLocation(new Point(10, 80));
		lblClass.setSize(lblClass.getPreferredSize());
		lblMaxSpeed = new JLabel();
		lblMaxSpeed.setText(Language.translate("Maximale Geschwindigkeit"));
		lblMaxSpeed.setLocation(new Point(10, 111));
		lblMaxSpeed.setSize(lblMaxSpeed.getPreferredSize());
		lblId = new JLabel();
		lblId.setText("ID");
		lblId.setLocation(new Point(10, 15));
		lblId.setSize(lblId.getPreferredSize());
		this.setLayout(null);
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
		this.add(getCbAgentClass());
		this.add(getTfMaxSpeed(), null);
		this.add(lblUnit1, null);
		this.add(lblUnit2, null);
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
			btnApply.setLocation(new Point(25, 265));
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
			btnRemove.setLocation(new Point(25, 295));
			btnRemove.setEnabled(false);
			btnRemove.setSize(new Dimension(150, 26));
			btnRemove.addActionListener(parent);		
		}
		return btnRemove;
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
			tfWidth.setLocation(new Point(16, 226));
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
			tfHeight.setLocation(new Point(80, 226));
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
			tfXPos.setLocation(new Point(16, 186));
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
			tfYPos.setLocation(new Point(80, 186));
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
			typeClass.put(Language.translate(EnvironmentSetup.TYPE_STRING_NO_TYPE), null);
			typeClass.put(Language.translate(EnvironmentSetup.TYPE_STRING_ACTIVE_OBJECT), ActiveObject.class);
			typeClass.put(Language.translate(EnvironmentSetup.TYPE_STRING_STATIC_OBJECT), StaticObject.class);
			typeClass.put(Language.translate(EnvironmentSetup.TYPE_STRING_PASSIVE_OBJECT), PassiveObject.class);
			typeClass.put(Language.translate(EnvironmentSetup.TYPE_STRING_PLAYGROUND_OBJECT), PlaygroundObject.class);
			
			cbOntologyClass.setModel(new DefaultComboBoxModel(typeClass.keySet().toArray()));
			cbOntologyClass.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(cbOntologyClass.getSelectedItem().equals(Language.translate(EnvironmentSetup.TYPE_STRING_ACTIVE_OBJECT))){
						getTfMaxSpeed().setEnabled(true);
						getCbAgentClass().setEnabled(true);
					}else{
						getTfMaxSpeed().setEnabled(false);
						getCbAgentClass().setEnabled(false);
					}
					if(cbOntologyClass.getSelectedItem().equals(Language.translate(EnvironmentSetup.TYPE_STRING_NO_TYPE))){
						getBtnApply().setEnabled(false);
					}else{
						getBtnApply().setEnabled(true);
					}
					
				}
				
			});			
		}
		return cbOntologyClass;
	}
	
	/**
	 * This method initializes cbClass	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	JComboBox getCbAgentClass() {
		if (cbAgentClass == null) {
			cbAgentClass = new JComboBox();
			cbAgentClass.setLocation(new Point(70, 78));
			cbAgentClass.setSize(new Dimension(100, 25));
			cbAgentClass.setEnabled(false);
			Vector<Class<?>> classes = Application.classDetector.getAgentClasse(true);
			Vector<String> names = new Vector<String>();
			
			agentClasses = new HashMap<String, String>();
			
			for(int i=0; i<classes.size(); i++){
				names.add(classes.get(i).getSimpleName());
				agentClasses.put(classes.get(i).getSimpleName(), classes.get(i).getName());
				
			}
			cbAgentClass.setModel(new DefaultComboBoxModel(names));
		}
		return cbAgentClass;
	}
	
	JTextField getTfMaxSpeed(){
		if(tfMaxSpeed == null){
			tfMaxSpeed = new JTextField();
			tfMaxSpeed.setText("10.0");
			tfMaxSpeed.setLocation(new Point(16, 134));
			tfMaxSpeed.setSize(new Dimension(100, 25));
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
			
			Scale scale = parent.controller.getEnvironment().getScale();
			
			
			getTfId().setText(elem.getAttributeNS(null, "id"));
			
			Size size = SVGHelper.getSizeFromElement(elem, scale);
			getTfWidth().setText(""+size.getWidth());
			getTfHeight().setText(""+size.getHeight());
			
			Position pos = SVGHelper.getPosFromElement(elem, scale);
			getTfXPos().setText(""+pos.getXPos());
			getTfYPos().setText(""+pos.getYPos());
			
			Physical2DObject object = parent.controller.getEnvWrap().getObjectById(elem.getAttributeNS(null, "id"));
			setObjectType(object);
			
			if(object instanceof ActiveObject){
				String agentClass = ((ActiveObject)object).getAgentClassName();
				getCbAgentClass().setSelectedItem(agentClass.substring(agentClass.lastIndexOf('.')+1));
			}
			
		}else{
			getTfId().setText("");
			getTfXPos().setText("");
			getTfYPos().setText("");
			getTfWidth().setText("");
			getTfHeight().setText("");
			getOntologyClass().setSelectedItem(Language.translate(EnvironmentSetup.TYPE_STRING_NO_TYPE));
			getTfMaxSpeed().setText("");
		}
	}
	
	private void setObjectType(Physical2DObject object){
		if(object == null){
			getOntologyClass().setSelectedItem(Language.translate(EnvironmentSetup.TYPE_STRING_NO_TYPE));
		}
		else if(object instanceof ActiveObject){
			getOntologyClass().setSelectedItem(Language.translate(EnvironmentSetup.TYPE_STRING_ACTIVE_OBJECT));
			getTfMaxSpeed().setText(""+((ActiveObject)object).getMaxSpeed());
		}else if(object instanceof PassiveObject){
			getOntologyClass().setSelectedItem(Language.translate(EnvironmentSetup.TYPE_STRING_PASSIVE_OBJECT));
		}else if(object instanceof StaticObject){
			getOntologyClass().setSelectedItem(Language.translate(EnvironmentSetup.TYPE_STRING_STATIC_OBJECT));
		}else if(object instanceof PlaygroundObject){
			getOntologyClass().setSelectedItem(Language.translate(EnvironmentSetup.TYPE_STRING_PLAYGROUND_OBJECT));
		}
	}
	
	/**
	 * This method returns a HashMap containing object settings defined by the input's current values
	 * @return HashMap containing object settings
	 */
	HashMap<String, Object> getObjectProperties(){
		HashMap<String, Object> settings = new HashMap<String, Object>();
		
		settings.put(EnvironmentSetup.SETTINGS_KEY_ID, tfId.getText());
		settings.put(EnvironmentSetup.SETTINGS_KEY_ONTO_CLASS, typeClass.get(cbOntologyClass.getSelectedItem()));
		if(cbOntologyClass.getSelectedItem().equals(Language.translate(EnvironmentSetup.TYPE_STRING_ACTIVE_OBJECT))){
			settings.put(EnvironmentSetup.SETTINGS_KEY_AGENT_MAX_SPEED, tfMaxSpeed.getText());
			settings.put(EnvironmentSetup.SETTINGS_KEY_AGENT_CLASSNAME, agentClasses.get(getCbAgentClass().getSelectedItem().toString()));
		}

		Position pos = new Position();
		pos.setXPos(Float.parseFloat(getTfXPos().getText()));
		pos.setYPos(Float.parseFloat(getTfYPos().getText()));
		settings.put(EnvironmentSetup.SETTINGS_KEY_POSITION, pos);
		
		Size size = new Size();
		size.setWidth(Float.parseFloat(getTfWidth().getText()));
		size.setHeight(Float.parseFloat(getTfHeight().getText()));
		settings.put(EnvironmentSetup.SETTINGS_KEY_SIZE, size);
		
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
