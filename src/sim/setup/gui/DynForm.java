package sim.setup.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
	public int actualX = 0;
	public int actualY = 0;
	public int einrueckungObersteEbene = 10;
	public int einrueckungProUntereEbene = 10;
	List<String> innerObjects = new ArrayList<String>();

	
	public DynForm(Project project, String agentReference) {
		
		currProject = project;
		currAgentReference = agentReference;
		
		
		JFrame testFrame = new JFrame();
		testFrame.setSize(this.frameWidht,this.frameHeight);
		testFrame.setVisible(true);
		testFrame.add(this);
		
		
		
		testFrame.add(mainPanel);
		//mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		//mainPanel.setAlignmentX(LEFT_ALIGNMENT);
		mainPanel.setLayout(null);
		mainPanel.setBounds(new Rectangle(0,0, this.frameWidht, this.frameHeight));
		
		
		//this.createGui(agentReference, mainPanel);
		//this.createGUI2(agentReference);
	
		//this.testBla(agentReference);
		this.start(agentReference);
		//this.setRightHeight(mainPanel,0);
	}
	
	
	public void start(String agentReference){
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
			//System.out.println( startPosition + ": " + startObjectClassName + " Package: " + startObjectPackage);
			
			//System.out.println("StartObjectClass " + startObjectClass);
			
			// --- Get the Infos about the slots -------------------
			DefaultTableModel tm = currProject.ontologies4Project.getSlots4Class(startObjectClass);
			//System.out.println(tm.toString());
			// --- Now, the GUI can be build ------------------------
			
			this.createGUI(tm, startObjectClassName, mainPanel, 0);

		}					
	}
	
	public void createGUI(DefaultTableModel tm , String startObjectClassName, JPanel pan, int tiefe){
		if(tm != null){
			if(tiefe == 0){
				JLabel objectLabelName = new JLabel();
				objectLabelName.setBounds(new Rectangle(einrueckungObersteEbene, actualY, 150, 16));
				objectLabelName.setText(startObjectClassName);
				Font boldFont=new Font(objectLabelName.getFont().getName(),Font.BOLD,objectLabelName.getFont().getSize());
				objectLabelName.setFont(boldFont);
				mainPanel.add(objectLabelName);
				actualY += 15;
				innerObjects.clear();
				innerObjects.add(this.startObjectPackage+"."+startObjectClassName);
				System.out.println("Level 0: "+startObjectClassName);
			}
			
			
		
			for(int i=0; i<tm.getRowCount() && tiefe <= 2; i++){
				if(tm.getValueAt(i, 2) != null)
				{
					//System.out.println("Name " + tm.getValueAt(i, 0) + "Values : "+tm.getValueAt(i, 2));
					String dataItemName = tm.getValueAt(i, 0).toString();
					String dataItemValue = tm.getValueAt(i, 2).toString();
					
					if(isSpecialType(dataItemValue)){
						if(dataItemValue.matches("Instance of (.)*")){
							String clazz = dataItemValue.substring(12);
							//System.out.println("The class: "+clazz + " Package: " + this.startObjectPackage);
							//System.out.println("+++++++++++++++ Start deep search +++++++++++++++++++++");
							System.out.println("Here we have an instance of: "+clazz);
							if(objectAlreadyDisplayed(this.startObjectPackage+"."+clazz) == false)
							{
								innerObjects.add(this.startObjectPackage+"."+clazz);
								this.createInnerElements(dataItemName, this.startObjectPackage+"."+clazz, tiefe+1, pan);
							}
							else
								System.out.println("Class " + this.startObjectPackage+"."+clazz + " already displayed!");
						}
					}
					else{
						this.createOuterElements(dataItemName, pan, tiefe);
					}
				}
			}
		}
	}	

	public boolean objectAlreadyDisplayed(String objectClass){
		String objects = "";
		for (String elem : innerObjects) {
			objects +=  " | " + elem;
			if(elem.equals(objectClass))
				return true;
		}
		System.out.println(objects);
		return false;
	}
	
public void createInnerElements(String dataItemName, String startObjectClassName, int tiefe, JPanel pan){
		
		int lastDot = startObjectClassName.lastIndexOf(".");
		String plainStartObjectClassName = startObjectClassName.substring(lastDot + 1);
		
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		int innerX  = 0;
		if(tiefe > 1)
			innerX= actualX + (einrueckungProUntereEbene*(tiefe-1));
		else
			innerX= actualX + (einrueckungProUntereEbene*(tiefe));
		dataPanel.setBounds(new Rectangle(innerX  , actualY , 300, 20));
		dataPanel.setBorder(BorderFactory.createLineBorder (Color.yellow, 1));
		dataPanel.setToolTipText(startObjectClassName + " Inner Panel");
		
		
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText(dataItemName);
		valueFieldText.setBounds(new Rectangle(0, 0, 130, 16));
		
		JLabel innerClassName = new JLabel();
		innerClassName.setText(plainStartObjectClassName);
		innerClassName.setBounds(new Rectangle(140, 0, 150, 16));
		Font boldFont=new Font(innerClassName.getFont().getName(),Font.BOLD,innerClassName.getFont().getSize());
		innerClassName.setFont(boldFont);
		
		System.out.println("Adding: "+dataItemName);
		
		dataPanel.add(valueFieldText, null);
		dataPanel.add(innerClassName, null);
		
		
		
		System.out.println("ref: " + startObjectClassName);
		
		DefaultTableModel tm = currProject.ontologies4Project.getSlots4Class(startObjectClassName);
		this.createGUI(tm, startObjectClassName, dataPanel, tiefe);
		
		Rectangle r = pan.getBounds();
		if(dataPanel.getWidth() > r.width)
			pan.setBounds(r.x, r.y, dataPanel.getWidth(), r.height + 20);
		else
			pan.setBounds(r.x, r.y, r.width, r.height + 20);
		
		
		pan.add(dataPanel);
		
		actualY += 20;
		
		
	}


	
	public void createOuterElements(String dataItemName, JPanel pan, int tiefe){
		
		Rectangle r2 = pan.getBounds();
		//System.out.println("Panel x,y + width height" + r2.getX() + " " + r2.y + " " + r2.width+ " " + r2.height );
		
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		int innerX = 0;
		if(tiefe == 0)
		{
			System.out.println("Flat object: " + dataItemName + " tiefe: " + tiefe + " actualY " + actualY);
			dataPanel.setBounds(new Rectangle(10, actualY , 250, 35));
		}
		else
		{
			innerX = actualX + (einrueckungProUntereEbene*tiefe);
			dataPanel.setBounds(new Rectangle(innerX, r2.height + 10 , 250, 35));
		}
		dataPanel.setBorder(BorderFactory.createLineBorder (Color.black, 1));
		
		dataPanel.setToolTipText(dataItemName + "Panel");
		
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText(dataItemName);
		valueFieldText.setBounds(new Rectangle(0, 5, 130, 16));
		
		JTextField valueField = new JTextField();
		valueField.setBounds(new Rectangle(140, 5, 100, 25));
		
		System.out.println("Adding: "+dataItemName);
		
		dataPanel.add(valueFieldText, null);
		dataPanel.add(valueField);
		
		actualY += 35;
		
		Rectangle r = pan.getBounds();
		pan.setBounds(r.x, r.y, r.width + innerX, r.height + 45);
		pan.add(dataPanel);
	}
	

	
	
	
	
	
	
	
	
	private boolean isSpecialType(String valueType){
		boolean flag = true;
		if(valueType.equals("String")){
			flag = false;
		}
		else if(valueType.equals("float")){
			flag = false;
		}
		else if(valueType.equals("int")){
			flag = false;
		}
		return flag;
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
				//this.createInnerElements(this.startObjectPackage+"."+clazz, dataItemPanel);
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
