package sim.setup.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import application.Project;

public class DynForm extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7942028680794127910L;

	private Project currProject = null;
	private String currAgentReference = null;
	JPanel mainPanel = new JPanel();
	int frameHeight = 600;
	int frameWidht = 600;
	String startObjectPackage = "";
	
	public DynForm(Project project, String agentReference) {
		
		currProject = project;
		currAgentReference = agentReference;
		
		
		JFrame testFrame = new JFrame();
		testFrame.setSize(this.frameWidht,this.frameHeight);
		testFrame.setVisible(true);
		testFrame.add(this);
		testFrame.add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		this.createGui(agentReference, mainPanel);
		
	
	}
	
	private void createGui(String agentReference, JPanel pan){
		
		// --- Find Agent in AgentConfig ----------------------------
		if ( currProject.AgentConfig.containsKey(currAgentReference)== false) {
			// TODO: Hier ein leeres JPanle zurückgeben ---!!!
			return;
		}
		
		System.out.println("Building TreeMap");
		
		// --- Which Start-Objects are configured for the Agent? ---- 
		TreeMap<Integer,String> startObjectList = currProject.AgentConfig.getReferencesAsTreeMap(currAgentReference);
		Vector<Integer> v = new Vector<Integer>(startObjectList.keySet()); 
		Collections.sort(v);
		

		System.out.println("CurAgentRef: "+agentReference);
		
		Iterator<Integer> it = v.iterator();
		while (it.hasNext()) {
			Integer startPosition = it.next();
			String startObjectClass = startObjectList.get(startPosition);
			startObjectPackage = startObjectClass.substring(0, startObjectClass.lastIndexOf("."));
			String startObjectClassName = startObjectClass.substring(startObjectClass.lastIndexOf(".") + 1, startObjectClass.length());
			System.out.println( startPosition + ": " + startObjectClassName + " Package: " + startObjectPackage);
			
			System.out.println("StartObjectClass " + startObjectClass);
			
			// --- Get the Infos about the slots -------------------
			DefaultTableModel tm = currProject.ontologies4Project.getSlots4Class(startObjectClass);
			//System.out.println(tm.toString());
			// --- Now, the GUI can be build ------------------------
			
			System.out.println("Create Ontology Elements");
			this.createElements(tm, startObjectClassName, pan);

		}	
	}
	
	private void createInnerElements(String innerClass, JPanel pan){
		String startObjectClassName = innerClass.substring(innerClass.lastIndexOf(".") + 1, innerClass.length());
		DefaultTableModel tm = currProject.ontologies4Project.getSlots4Class(innerClass);
		System.out.println("InnerElements: "+innerClass + " - " +startObjectClassName);
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setAlignmentX(LEFT_ALIGNMENT);
		//p.setAlignmentY(TOP_ALIGNMENT);
		p.setBorder(BorderFactory.createLineBorder (Color.black, 1));
		pan.add(p);
		this.createElements(tm, startObjectClassName, p);
		
		mainPanel.validate();
		mainPanel.updateUI();
		
	}
	
	
	private void setObjectName(String startObjectClassName, JPanel pan){
		JLabel objectName = new JLabel(); 
		Font boldFont=new Font(objectName.getFont().getName(),Font.BOLD,objectName.getFont().getSize());
		objectName.setText(startObjectClassName);
		objectName.setFont(boldFont);
		objectName.setAlignmentX(LEFT_ALIGNMENT);
		pan.add(objectName);
	}
	
	private JPanel setDataItemPanel(){
		JPanel dataItemPanel = new JPanel();
		dataItemPanel.setLayout(new BoxLayout(dataItemPanel, BoxLayout.X_AXIS));
		dataItemPanel.setAlignmentX(RIGHT_ALIGNMENT);
		return dataItemPanel;
	}
	
	private void setDataItemName(String name, JPanel dataItemPanel){
		JLabel dataItemName = new JLabel();
		dataItemName.setText(name);
		dataItemName.setMinimumSize(new Dimension(100, 30));
		dataItemName.setAlignmentX(RIGHT_ALIGNMENT);
		dataItemPanel.add(dataItemName);
	}
	

	
	public void createElements(DefaultTableModel tm , String startObjectClassName, JPanel pan){
		JPanel dataPanel = new JPanel();
		//dataPanel.setBorder(BorderFactory.createLineBorder (Color.black, 1));
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
		dataPanel.setAlignmentX(LEFT_ALIGNMENT);

		this.setObjectName(startObjectClassName, pan);
		
		for(int i=0; i<tm.getRowCount(); i++){
			JPanel dataItemPanel = this.setDataItemPanel();

			if(tm.getValueAt(i, 2) != null)
			{
				System.out.println("Name " + tm.getValueAt(i, 0) + "Values : "+tm.getValueAt(i, 2));
				this.setDataItemName(tm.getValueAt(i, 0).toString(), dataItemPanel);
				dataItemPanel.add(Box.createRigidArea(new Dimension(10,0)));
				this.setDataValueField(tm.getValueAt(i, 2).toString(), dataItemPanel);
								
				
			}
			dataPanel.add(dataItemPanel);
		}
		
		pan.add(dataPanel); 
		
	}
	
	
	private void setDataValueField(String valueType, JPanel dataItemPanel) {
		if(valueType.equals("String")){
			JTextField dataValueField = new JTextField();
			dataValueField.setMaximumSize(new Dimension(100, 30));
			dataValueField.setAlignmentX(RIGHT_ALIGNMENT);
			dataItemPanel.add(dataValueField);
		}
		else if(valueType.equals("float")){
			JTextField dataValueField = new JTextField();
			dataValueField.setMaximumSize(new Dimension(100, 30));
			dataValueField.setAlignmentX(RIGHT_ALIGNMENT);
			dataItemPanel.add(dataValueField);
		}
		else if(valueType.equals("int")){
			JTextField dataValueField = new JTextField();
			dataValueField.setMaximumSize(new Dimension(100, 30));
			dataValueField.setAlignmentX(RIGHT_ALIGNMENT);
			dataItemPanel.add(dataValueField);
		}
		else{
			JLabel dataValueField = new JLabel();
			dataValueField.setText("Unknown Type");
			dataValueField.setMaximumSize(new Dimension(100, 30));
			dataValueField.setAlignmentX(RIGHT_ALIGNMENT);
			System.out.println("Unknown: "+valueType);
			if(valueType.matches("Instance of (.)*")){
				
				String clazz = valueType.substring(12);
				System.out.println("The class: "+clazz + " Package: " + this.startObjectPackage);
				System.out.println("+++++++++++++++ Start deep search +++++++++++++++++++++");
				this.createInnerElements(this.startObjectPackage+"."+clazz, dataItemPanel);
			}
			else
			{
				dataItemPanel.add(dataValueField);
			}
		}
		

		System.out.println("Type: "+valueType);
		
	}

	public static void main(String[] args) {
		
		JDialog jd = new JDialog();
		//jd.add(new DynForm(args));
		
	}
	
	
	
}
