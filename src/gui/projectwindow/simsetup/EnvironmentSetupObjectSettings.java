package gui.projectwindow.simsetup;

import jade.core.Agent;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.tree.DefaultMutableTreeNode;

import org.w3c.dom.Element;


import application.Language;

import mas.environment.ontology.ActiveObject;
import mas.environment.ontology.Physical2DObject;
import mas.environment.ontology.Scale;
import mas.environment.ontology.StaticObject;
import mas.environment.ontology.PassiveObject;
import mas.environment.ontology.PlaygroundObject;
import mas.environment.ontology.Position;
import mas.environment.ontology.Size;
import mas.environment.utils.SVGHelper;

public class EnvironmentSetupObjectSettings extends JPanel{

	private static final long serialVersionUID = 1L;
	private JLabel lblId = null;
	private JLabel lblClass = null;
	private JLabel lblType = null;
	private JLabel lblPosition = null;
	private JLabel lblSize = null;
	private JButton btnApply = null;
	private JButton btnRemove = null;
	private JTextField tfWidth = null;
	private JTextField tfHeight = null;
	private JLabel lblSizeSeparator = null;
	private JTextField tfXPos = null;
	private JTextField tfYPos = null;
	private JLabel lblPosSeparator = null;
	private JTextField tfId = null;
	private JComboBox cbType = null;
	private JComboBox cbClass = null;
	private JLabel lblUnit1 = null;
	private JLabel lblUnit2 = null;
	
	public static final String SETTINGS_KEY_ID = "id";
	public static final String SETTINGS_KEY_ONTO_CLASS = "ontologyClass";
	public static final String SETTINGS_KEY_AGENT_CLASSNAME = "agentClassName";
	public static final String SETTINGS_KEY_POSITION = "position";
	public static final String SETTINGS_KEY_SIZE = "size";
	
	private static final String ACTIVE_OBJECT_TYPE_STRING = "Agent";
	private static final String PASSIVE_OBJECT_TYPE_STRING = "Nutzlast";
	private static final String STATIC_OBJECT_TYPE_STRING = "Hindernis";
	private static final String PLAYGROUND_OBJECT_TYPE_STRING = "Spielfeld";
	private static final String NO_OBJECT_TYPE_STRING = "Keiner";
	
	/**
	 * This project's agent classes
	 */
	private HashMap<String, String> agentClassNames = null;
	/**
	 * Defines which object type is linked to which environment ontology class
	 */
	private HashMap<String, Class<?>> typeClass = null;
	
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
		lblUnit2.setLocation(new Point(150, 162));
		lblUnit2.setSize(lblUnit2.getPreferredSize());
		lblUnit1 = new JLabel();
		lblUnit1.setText("m");
		lblUnit1.setLocation(new Point(145, 122));
		lblUnit1.setSize(lblUnit1.getPreferredSize());
		lblPosSeparator = new JLabel();
		lblPosSeparator.setText(":");
		lblPosSeparator.setLocation(new Point(70, 122));
		lblPosSeparator.setSize(lblPosSeparator.getPreferredSize());
		lblSizeSeparator = new JLabel();
		lblSizeSeparator.setText("x");
		lblSizeSeparator.setLocation(new Point(70, 162));
		lblSizeSeparator.setSize(lblSizeSeparator.getPreferredSize());
		lblSize = new JLabel();
		lblSize.setText(Language.translate("Größe"));
		lblSize.setLocation(new Point(15, 145));
		lblSize.setSize(lblSize.getPreferredSize());		
		lblPosition = new JLabel();
		lblPosition.setText(Language.translate("Position"));
		lblPosition.setLocation(new Point(16, 105));
		lblPosition.setSize(lblPosition.getPreferredSize());
		lblType = new JLabel();
		lblType.setText(Language.translate("Typ"));
		lblType.setLocation(new Point(10, 50));
		lblType.setSize(lblType.getPreferredSize());
		lblClass = new JLabel();
		lblClass.setText(Language.translate("Klasse"));
		lblClass.setLocation(new Point(10, 80));
		lblClass.setSize(lblClass.getPreferredSize());
		lblId = new JLabel();
		lblId.setText("ID");
		lblId.setLocation(new Point(10, 15));
		lblId.setSize(lblId.getPreferredSize());
		this.setLayout(null);
		this.add(lblId, null);
		this.add(lblClass, null);
		this.add(lblType, null);
		this.add(lblPosition, null);
		this.add(lblSize, null);
		this.add(getBtnApply(), null);
		this.add(getBtnRemove(), null);
		this.add(getTfWidth(), null);
		this.add(getTfHeight(), null);
		this.add(lblSizeSeparator, null);
		this.add(getTfXPos(), null);
		this.add(getTfYPos(), null);
		this.add(lblPosSeparator, null);
		this.add(getTfId(), null);
		this.add(getCbType(), null);
		this.add(getCbClass(), null);
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
			btnApply.setLocation(new Point(25, 210));
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
			btnRemove.setLocation(new Point(25, 240));
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
			tfWidth.setLocation(new Point(16, 160));
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
			tfHeight.setLocation(new Point(80, 160));
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
			tfXPos.setLocation(new Point(16, 120));
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
			tfYPos.setLocation(new Point(80, 120));
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
			tfId.setLocation(new Point(78, 14));
			tfId.setSize(new Dimension(100, 25));
			tfId.setEnabled(false);
		}
		return tfId;
	}

	/**
	 * This method initializes cbType	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	JComboBox getCbType() {
		if (cbType == null) {
			
			cbType = new JComboBox();
			cbType.setLocation(new Point(80, 47));
			cbType.setSize(new Dimension(100, 25));
			cbType.setEnabled(false);
			
			typeClass = new HashMap<String, Class<?>>();
			typeClass.put(Language.translate(NO_OBJECT_TYPE_STRING), null);
			typeClass.put(Language.translate(ACTIVE_OBJECT_TYPE_STRING), ActiveObject.class);
			typeClass.put(Language.translate(STATIC_OBJECT_TYPE_STRING), StaticObject.class);
			typeClass.put(Language.translate(PASSIVE_OBJECT_TYPE_STRING), PassiveObject.class);
			typeClass.put(Language.translate(PLAYGROUND_OBJECT_TYPE_STRING), PlaygroundObject.class);
			
			cbType.setModel(new DefaultComboBoxModel(typeClass.keySet().toArray()));
			cbType.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(cbType.getSelectedItem().equals(Language.translate(ACTIVE_OBJECT_TYPE_STRING))){
						getCbClass().setEnabled(true);
					}else{
						getCbClass().setEnabled(false);
					}
					if(cbType.getSelectedItem().equals(Language.translate(NO_OBJECT_TYPE_STRING))){
						getBtnApply().setEnabled(false);
					}else{
						getBtnApply().setEnabled(true);
					}
					
				}
				
			});			
		}
		return cbType;
	}

	/**
	 * This method initializes cbClass	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	JComboBox getCbClass() {
		if (cbClass == null) {
			cbClass = new JComboBox();
			cbClass.setLocation(new Point(80, 78));
			cbClass.setSize(new Dimension(100, 25));
			cbClass.setEnabled(false);
			Vector<Class<? extends Agent>> classes = parent.project.getProjectAgents();
			Vector<String> names = new Vector<String>();
			
			agentClassNames = new HashMap<String, String>();
			
			for(int i=0; i<classes.size(); i++){
				names.add(classes.get(i).getSimpleName());
				agentClassNames.put(classes.get(i).getSimpleName(), classes.get(i).getName());
				
			}
			cbClass.setModel(new DefaultComboBoxModel(names));
		}
		return cbClass;
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
			
			setObjectType(parent.controller.getEnvWrap().getObjectById(elem.getAttributeNS(null, "id")));
			
		}else{
			getTfId().setText("");
			getTfXPos().setText("");
			getTfYPos().setText("");
			getTfWidth().setText("");
			getTfHeight().setText("");
			getCbType().setSelectedItem(Language.translate(NO_OBJECT_TYPE_STRING));
		}
	}
	
	private void setObjectType(Physical2DObject object){
		if(object == null){
			getCbType().setSelectedItem(Language.translate(NO_OBJECT_TYPE_STRING));
		}
		else if(object instanceof ActiveObject){
			getCbType().setSelectedItem(Language.translate(ACTIVE_OBJECT_TYPE_STRING));
		}else if(object instanceof PassiveObject){
			getCbType().setSelectedItem(Language.translate(PASSIVE_OBJECT_TYPE_STRING));
		}else if(object instanceof StaticObject){
			getCbType().setSelectedItem(Language.translate(STATIC_OBJECT_TYPE_STRING));
		}else if(object instanceof PlaygroundObject){
			getCbType().setSelectedItem(Language.translate(PLAYGROUND_OBJECT_TYPE_STRING));
		}
	}
	
	/**
	 * This method returns a HashMap containing object settings defined by the input's current values
	 * @return HashMap containing object settings
	 */
	HashMap<String, Object> getObjectProperties(){
		HashMap<String, Object> settings = new HashMap<String, Object>();
		
		settings.put(SETTINGS_KEY_ID, tfId.getText());
		settings.put(SETTINGS_KEY_ONTO_CLASS, typeClass.get(cbType.getSelectedItem()));
		if(cbType.getSelectedItem().equals(Language.translate(ACTIVE_OBJECT_TYPE_STRING))){
			settings.put(SETTINGS_KEY_AGENT_CLASSNAME, cbClass.getSelectedItem());
		}

		Position pos = new Position();
		pos.setXPos(Float.parseFloat(getTfXPos().getText()));
		pos.setYPos(Float.parseFloat(getTfYPos().getText()));
		settings.put(SETTINGS_KEY_POSITION, pos);
		
		Size size = new Size();
		size.setWidth(Float.parseFloat(getTfWidth().getText()));
		size.setHeight(Float.parseFloat(getTfHeight().getText()));
		settings.put(SETTINGS_KEY_SIZE, size);
		
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
		getTfHeight().setEnabled(enabled);
		getCbType().setEnabled(enabled);
	}
	
}
