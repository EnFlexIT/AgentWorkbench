package mas.environment.guiComponents;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Point;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;


import application.Language;

import mas.display.SvgTypes;
import mas.display.ontology.AbstractObject;
import mas.display.ontology.AgentObject;
import mas.environment.ObjectTypes;

public class PnlObjectSettings extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblId = null;
	private JLabel lblClass = null;
	private JLabel lblType = null;
	private JLabel lblPosition = null;
	private JLabel lblSize = null;
	private JLabel lblSpeed = null;
	private JButton btnApply = null;
	private JButton btnRemove = null;
	private JTextField tfSpeed = null;
	private JTextField tfWidth = null;
	private JTextField tfHeight = null;
	private JLabel lblSizeSeparator = null;
	private JTextField tfPosX = null;
	private JTextField tfPosY = null;
	private JLabel lblPosSeparator = null;
	private JTextField tfId = null;
	private JComboBox cbType = null;
	private JComboBox cbClass = null;
	private JLabel lblUnit1 = null;
	private JLabel lblUnit2 = null;
	private JLabel lblUnit3 = null;
	
	private EnvironmentControllerGUI parent = null;
	/**
	 * This is the default constructor
	 */
	public PnlObjectSettings(EnvironmentControllerGUI parent) {
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
		lblUnit3 = new JLabel();
		lblUnit3.setText("m/s");
		lblUnit3.setLocation(new Point(155, 202));
		lblUnit3.setSize(lblUnit3.getPreferredSize());
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
		lblSpeed = new JLabel();
		lblSpeed.setText("Geschwindigkeit");
		lblSpeed.setLocation(new Point(10, 202));
		lblSpeed.setSize(lblSpeed.getPreferredSize());
		lblSize = new JLabel();
		lblSize.setText("Größe");
		lblSize.setLocation(new Point(15, 145));
		lblSize.setSize(lblSize.getPreferredSize());		
		lblPosition = new JLabel();
		lblPosition.setText("Position");
		lblPosition.setLocation(new Point(16, 105));
		lblPosition.setSize(lblPosition.getPreferredSize());
		lblType = new JLabel();
		lblType.setText("Typ");
		lblType.setLocation(new Point(10, 50));
		lblType.setSize(lblType.getPreferredSize());
		lblClass = new JLabel();
		lblClass.setText("Klasse");
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
		this.add(lblSpeed, null);
		this.add(getBtnApply(), null);
		this.add(getBtnRemove(), null);
		this.add(getTfSpeed(), null);
		this.add(getTfWidth(), null);
		this.add(getTfHeight(), null);
		this.add(lblSizeSeparator, null);
		this.add(getTfPosX(), null);
		this.add(getTfPosY(), null);
		this.add(lblPosSeparator, null);
		this.add(getTfId(), null);
		this.add(getCbType(), null);
		this.add(getCbClass(), null);
		this.add(lblUnit1, null);
		this.add(lblUnit2, null);
		this.add(lblUnit3, null);
	}

	/**
	 * This method initializes btnApply	
	 * 	
	 * @return javax.swing.JButton	
	 */
	JButton getBtnApply() {
		if (btnApply == null) {
			btnApply = new JButton();
			btnApply.setText("Anwenden");
			btnApply.setSize(new Dimension(150, 26));
			btnApply.setLocation(new Point(25, 240));
			btnApply.addActionListener(parent);
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
			btnRemove.setText("Objekt Entfernen");
			btnRemove.setSize(new Dimension(150, 26));
			btnRemove.setLocation(new Point(25, 270));
			btnRemove.setEnabled(false);
			btnRemove.setSize(new Dimension(150, 26));
			btnRemove.addActionListener(parent);		
		}
		return btnRemove;
	}

	/**
	 * This method initializes tfSpeed	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	JTextField getTfSpeed() {
		if (tfSpeed == null) {
			tfSpeed = new JTextField();
			tfSpeed.setLocation(new Point(110, 200));
			tfSpeed.setSize(new Dimension(40, 25));
		}
		return tfSpeed;
	}

	/**
	 * This method initializes tfwidth	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	JTextField getTfWidth() {
		if (tfWidth == null) {
			tfWidth = new JTextField();
			tfWidth.setSize(new Dimension(50, 25));
			tfWidth.setLocation(new Point(16, 160));
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
		}
		return tfHeight;
	}

	/**
	 * This method initializes tfPosX	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	JTextField getTfPosX() {
		if (tfPosX == null) {
			tfPosX = new JTextField();
			tfPosX.setLocation(new Point(16, 120));
			tfPosX.setSize(new Dimension(50, 25));
		}
		return tfPosX;
	}

	/**
	 * This method initializes tfPosY	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	JTextField getTfPosY() {
		if (tfPosY == null) {
			tfPosY = new JTextField();
			tfPosY.setLocation(new Point(80, 120));
			tfPosY.setSize(new Dimension(50, 25));
		}
		return tfPosY;
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
			Vector<String> types = new Vector<String>();
			types.add(Language.translate("Keiner"));
			for(ObjectTypes type : ObjectTypes.values()){
				types.add(type.toString());
			}
			cbType.setModel(new DefaultComboBoxModel(types));
			cbType.addActionListener(parent);
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
			Vector<Class<?>> classes = parent.getController().getCurrentProject().getProjectAgents();
			Vector<String> names = new Vector<String>();
			
			parent.setAgentClasses(new HashMap<String, String>());
			
			for(int i=0; i<classes.size(); i++){
				names.add(classes.get(i).getSimpleName());
				parent.getAgentClasses().put(classes.get(i).getSimpleName(), classes.get(i).getName());
				
			}
			cbClass.setModel(new DefaultComboBoxModel(names));
		}
		return cbClass;
	}
	
	/**
	 * Collecting all important input values for creating a new environment object 
	 * @return HashMap<String, String>
	 */
	HashMap<String, String> getObjectSettings(){
		HashMap<String, String> settings = new HashMap<String, String>();
		settings.put("id", tfId.getText());
		settings.put("type", cbType.getSelectedItem().toString());
		if(cbClass.isEnabled()){
			settings.put("class", parent.getAgentClasses().get(cbClass.getSelectedItem().toString()));			
		}
		settings.put("speed", tfSpeed.getText());
		
		return settings;
	}
	
	/**
	 * Setting the values of all controls when an object/element is selected 
	 */
	void setObjectSettings(){
		String id = null;
		AbstractObject object = null;
		if(parent.getSelectedElement() != null){
			id = parent.getSelectedElement().getAttributeNS(null, "id");
			object = parent.getController().getObjectHash().get(id);
			cbType.setEnabled(true);
			float xPos=0, yPos=0, width=0, height=0;
			switch(SvgTypes.getType(parent.getSelectedElement())){
				case RECT:
				case IMAGE:
					xPos = Float.parseFloat(parent.getSelectedElement().getAttributeNS(null, "x"));
					yPos = Float.parseFloat(parent.getSelectedElement().getAttributeNS(null, "y"));
					width = Float.parseFloat(parent.getSelectedElement().getAttributeNS(null, "width"));
					height = Float.parseFloat(parent.getSelectedElement().getAttributeNS(null, "height"));
				break;
				case CIRCLE:
					xPos = Float.parseFloat(parent.getSelectedElement().getAttributeNS(null, "cx"));
					yPos = Float.parseFloat(parent.getSelectedElement().getAttributeNS(null, "cy"));
					width = height = Float.parseFloat(parent.getSelectedElement().getAttributeNS(null, "r"))*2;
				break;
				case ELLIPSE:
					xPos = Float.parseFloat(parent.getSelectedElement().getAttributeNS(null, "cx"));
					yPos = Float.parseFloat(parent.getSelectedElement().getAttributeNS(null, "cy"));
					width = Float.parseFloat(parent.getSelectedElement().getAttributeNS(null, "r1"))*2;
					height = Float.parseFloat(parent.getSelectedElement().getAttributeNS(null, "r2"))*2;
				break;					
			}
//			xPos = OntoUtilities.calcRWU(xPos, parent.getController().getEnvironment().getScale());
//			yPos = OntoUtilities.calcRWU(yPos, parent.getController().getEnvironment().getScale());
//			width = OntoUtilities.calcRWU(width, parent.getController().getEnvironment().getScale());
//			height = OntoUtilities.calcRWU(height, parent.getController().getEnvironment().getScale());
			
			xPos = parent.getController().getEnvironment().getScale().calcRwu(xPos);
			yPos = parent.getController().getEnvironment().getScale().calcRwu(yPos);
			width = parent.getController().getEnvironment().getScale().calcRwu(width);
			height = parent.getController().getEnvironment().getScale().calcRwu(height);
			
			getTfPosX().setText(""+xPos);
			getTfPosY().setText(""+yPos);
			getTfWidth().setText(""+width);
			getTfHeight().setText(""+height);
			
		}else{
			cbType.setEnabled(false);
			getTfPosX().setText("");
			getTfPosY().setText("");
			getTfWidth().setText("");
			getTfHeight().setText("");
			getTfSpeed().setText("");
			
		}
		tfId.setText(id);
		if(object != null){
			ObjectTypes type = ObjectTypes.getType(object);
			cbType.setSelectedItem(type.toString());
			if(type == ObjectTypes.AGENT){
				String className = ((AgentObject)object).getAgentClass();
				className = className.substring(className.lastIndexOf('.')+1);
				cbClass.setSelectedItem(className);
				if(((AgentObject)object).getCurrentSpeed() != null)
					tfSpeed.setText(""+((AgentObject)object).getCurrentSpeed().getSpeed());
			}
			btnRemove.setEnabled(true);
		}else{
			cbType.setSelectedItem(Language.translate("Keiner"));
			btnRemove.setEnabled(false);
		}
	}

}
