package mas.environment.guiComponents;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import org.apache.batik.bridge.UpdateManager;

import sma.ontology.AbstractObject;
import sma.ontology.AgentObject;
import sma.ontology.Position;
import sma.ontology.Size;

import application.Language;

import mas.display.SvgTypes;
import mas.environment.EnvironmentControllerGUI;
import mas.environment.ObjectTypes;
import mas.environment.OntoUtilities;

public class PnlObject extends JPanel {

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
	public PnlObject(EnvironmentControllerGUI parent) {
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
		lblUnit3.setLocation(new Point(150,230));
		lblUnit3.setSize(lblUnit3.getPreferredSize());
		lblUnit2 = new JLabel();
		lblUnit2.setText("m");
		lblUnit2.setLocation(new Point(150,195));
		lblUnit2.setSize(lblUnit2.getPreferredSize());
		lblUnit1 = new JLabel();
		lblUnit1.setBounds(new Rectangle(146, 141, 38, 16));
		lblUnit1.setText("m");
		lblUnit1.setLocation(new Point(145,140));
		lblUnit1.setSize(lblUnit1.getPreferredSize());
		lblPosSeparator = new JLabel();
		lblPosSeparator.setText(":");
		lblPosSeparator.setLocation(new Point(70, 142));
		lblPosSeparator.setSize(lblPosSeparator.getPreferredSize());
		lblSizeSeparator = new JLabel();
		lblSizeSeparator.setText("x");
		lblSizeSeparator.setLocation(new Point(70, 192));
		lblSizeSeparator.setSize(lblSizeSeparator.getPreferredSize());
		lblSpeed = new JLabel();
		lblSpeed.setText("Geschwindigkeit");
		lblSpeed.setLocation(new Point(10, 230));
		lblSpeed.setSize(lblSpeed.getPreferredSize());
		lblSize = new JLabel();
		lblSize.setText("Größe");
		lblSize.setLocation(new Point(15,165));
		lblSize.setSize(lblSize.getPreferredSize());		
		lblPosition = new JLabel();
		lblPosition.setText("Position");
		lblPosition.setLocation(new Point(16, 119));
		lblPosition.setSize(lblPosition.getPreferredSize());
		lblType = new JLabel();
		lblType.setText("Typ");
		lblType.setLocation(new Point(10, 45));
		lblType.setSize(lblType.getPreferredSize());
		lblClass = new JLabel();
		lblClass.setText("Klasse");
		lblClass.setLocation(new Point(10, 85));
		lblClass.setSize(lblClass.getPreferredSize());
		lblId = new JLabel();
		lblId.setText("ID");
		lblId.setLocation(new Point(10, 15));
		lblId.setSize(lblId.getPreferredSize());
//		this.setSize(200, 350);
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
	private JButton getBtnApply() {
		if (btnApply == null) {
			btnApply = new JButton();
			btnApply.setText("Anwenden");
			btnApply.setSize(new Dimension(150, 26));
			btnApply.setLocation(new Point(25, 270));
			btnApply.addActionListener(new ActionListener(){
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// Create an ontology object 
					parent.getController().createObject(parent.getSelectedElement(), getObjectSettings());
					
					// Change position and size
					Position pos = new Position();
					pos.setX(Float.parseFloat(tfPosX.getText()));
					pos.setY(Float.parseFloat(tfPosY.getText()));
					
					Size size = new Size();
					size.setWidth(Float.parseFloat(tfWidth.getText()));
					size.setHeight(Float.parseFloat(tfHeight.getText()));
					UpdateManager um = parent.getSvgGUI().getCanvas().getUpdateManager();
					um.getUpdateRunnableQueue().invokeLater(parent.new ElementChanger(pos, size));					
					
					// Remove selection
					parent.setSelectedElement(null);					
					
				}
				
			});
		}
		return btnApply;
	}

	/**
	 * This method initializes btnRemove	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnRemove() {
		if (btnRemove == null) {
			btnRemove = new JButton();
			btnRemove.setText("Objekt Entfernen");
			btnRemove.setSize(new Dimension(150, 26));
			btnRemove.setLocation(new Point(25, 310));
			btnRemove.setEnabled(false);
			btnRemove.setSize(new Dimension(150, 26));
			btnRemove.addActionListener(new ActionListener(){
	
				@Override
				public void actionPerformed(ActionEvent arg0) {
					parent.getController().deleteObject(parent.getSelectedElement().getAttributeNS(null, "id"), false);
					parent.setSelectedElement(null);
				}
				
			});
		
		}
		return btnRemove;
	}

	/**
	 * This method initializes tfSpeed	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfSpeed() {
		if (tfSpeed == null) {
			tfSpeed = new JTextField();
			tfSpeed.setLocation(new Point(103, 226));
			tfSpeed.setSize(new Dimension(32, 25));
		}
		return tfSpeed;
	}

	/**
	 * This method initializes tfwidth	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfWidth() {
		if (tfWidth == null) {
			tfWidth = new JTextField();
			tfWidth.setSize(new Dimension(50, 25));
			tfWidth.setLocation(new Point(15, 188));
		}
		return tfWidth;
	}

	/**
	 * This method initializes tfHeight	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfHeight() {
		if (tfHeight == null) {
			tfHeight = new JTextField();
			tfHeight.setSize(new Dimension(50, 25));
			tfHeight.setLocation(new Point(86, 190));
		}
		return tfHeight;
	}

	/**
	 * This method initializes tfPosX	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfPosX() {
		if (tfPosX == null) {
			tfPosX = new JTextField();
			tfPosX.setLocation(new Point(16, 141));
			tfPosX.setSize(new Dimension(50, 25));
		}
		return tfPosX;
	}

	/**
	 * This method initializes tfPosY	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfPosY() {
		if (tfPosY == null) {
			tfPosY = new JTextField();
			tfPosY.setLocation(new Point(84, 140));
			tfPosY.setSize(new Dimension(50, 25));
		}
		return tfPosY;
	}

	/**
	 * This method initializes tfId	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfId() {
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
	private JComboBox getCbType() {
		if (cbType == null) {
			cbType = new JComboBox();
			cbType.setLocation(new Point(80, 47));
			cbType.setSize(new Dimension(100, 25));
//			cbType.setEnabled(false);
			Vector<String> types = new Vector<String>();
			types.add(Language.translate("Keiner"));
			for(ObjectTypes type : ObjectTypes.values()){
				types.add(type.toString());
			}
			cbType.setModel(new DefaultComboBoxModel(types));
			cbType.addActionListener(new ActionListener(){
	
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(ObjectTypes.getType(cbType.getSelectedItem().toString()) != null){
						btnApply.setEnabled(true);						
					}else{
						btnApply.setEnabled(false);						
					}
					
					if(cbType.getSelectedItem().equals("AGENT")){
						cbClass.setEnabled(true);
						tfSpeed.setEditable(true);
					}else{
						cbClass.setEnabled(false);
						tfSpeed.setEditable(false);
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
	private JComboBox getCbClass() {
		if (cbClass == null) {
			cbClass = new JComboBox();
			cbClass.setLocation(new Point(78, 82));
			cbClass.setSize(new Dimension(100, 25));
//			cbClass.setEnabled(false);
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
	public HashMap<String, String> getObjectSettings(){
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
	public void setObjectSettings(){
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
			xPos = OntoUtilities.calcRWU(xPos, parent.getController().getEnvironment().getScale());
			yPos = OntoUtilities.calcRWU(yPos, parent.getController().getEnvironment().getScale());
			width = OntoUtilities.calcRWU(width, parent.getController().getEnvironment().getScale());
			height = OntoUtilities.calcRWU(height, parent.getController().getEnvironment().getScale());
			
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
		}
		tfId.setText(id);
		if(object != null){
			ObjectTypes type = ObjectTypes.getType(object);
			cbType.setSelectedItem(type.toString());
			if(type == ObjectTypes.AGENT){
				cbClass.setSelectedItem(((AgentObject)object).getAgentClass());
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
