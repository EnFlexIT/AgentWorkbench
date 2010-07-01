package sim.setup.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import mas.onto.OntologySingleClassDescription;
import mas.onto.OntologySingleClassSlotDescription;
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
	JPanel superPanel = new JPanel();
	JFrame testFrame = new JFrame();
	
	public DynForm(Project project, String agentReference) {
		
		currProject = project;
		currAgentReference = agentReference;
		
		//mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		//mainPanel.setAlignmentX(LEFT_ALIGNMENT);
		//superPanel.setBounds(0, 0, this.frameWidht, this.frameHeight);
		superPanel.setPreferredSize(new Dimension(this.frameWidht, this.frameHeight));
		superPanel.setLayout(new BorderLayout());
		mainPanel.setLayout(null);
		mainPanel.setPreferredSize(new Dimension(this.frameWidht, this.frameHeight));
		//mainPanel.setBounds(0,0,this.frameWidht, this.frameHeight);
		//mainPanel.setBounds(new Rectangle(0,0, this.frameWidht, this.frameHeight));
		
		
		
		
		testFrame.setSize(this.frameWidht,this.frameHeight);
		testFrame.setVisible(true);
		//testFrame.add(this);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(superPanel);
		
		superPanel.add(mainPanel);
		
		testFrame.add(scrollPane);
		
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
			
			// --------------------------------------------------------------------------
			// --- Get the Infos about the slots -------------------
			DefaultTableModel tm = currProject.ontologies4Project.getSlots4Class(startObjectClass);

			// --------------------------------------------------------------------------
			// --- Hier das neu Zeug als Objektstruktur und nicht als TableModel -------- 
			// --------------------------------------------------------------------------
			OntologySingleClassDescription osc = currProject.ontologies4Project.getSlots4ClassAsObject(startObjectClass);
			System.out.println( "---------------------------------------------------");
			System.out.println( "---------------------------------------------------");
			System.out.println( "Class<?>-Object: " + osc.getClazz() );
			System.out.println( "Klassenreferenz: " + osc.getClassReference() );
			System.out.println( "Package-Name   : " + osc.getPackageName() );
			System.out.println( "Klassen-Name   : " + osc.getClassName() );
			
			Iterator<OntologySingleClassSlotDescription> iterator = osc.osdArr.iterator();
			while (iterator.hasNext()) {
				OntologySingleClassSlotDescription osd = iterator.next();
				System.out.println( "---------------------------------------------------");
				System.out.println( "Slot-Name   : " + osd.getSlotName() );
				System.out.println( "Kardinalität: " + osd.getSlotCardinality() + " => CardinalityIsMultiple [bool] " + osd.isSlotCardinalityIsMultiple() );
				System.out.println( "VarTyp      : " + osd.getSlotVarType() );
				System.out.println( "Other Facts : " + osd.getSlotOtherFacts() );
				
				System.out.println( "Methoden    : " + osd.getSlotOtherFacts() );
				Hashtable<String, Method> meth = osd.getSlotMethodList();
				Iterator<String> methIT = meth.keySet().iterator();
				while (methIT.hasNext()) {
					String methodeName = methIT.next();
					Method methodeHimSelf = meth.get(methodeName);
					System.out.println( " - " + methodeName + " <=> " + methodeHimSelf);
				}
				
			}
			// --------------------------------------------------------------------------
			// --------------------------------------------------------------------------
			// --------------------------------------------------------------------------
			
			//System.out.println(tm.toString());
			// --- Now, the GUI can be build ------------------------
			
			this.createGUI(tm, startObjectClassName, mainPanel, 0);

		}
		this.addSubmitButton();
	}
	
	private void addSubmitButton() {
		JButton submitButton = new JButton();
		submitButton.setBounds(new Rectangle(0, actualY + 30, 70, 25));
		submitButton.setText("Submit");
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generateInstances(mainPanel, 0);
			}
		});
		mainPanel.add(submitButton);
	}

	private void generateInstances(JPanel pan, int tiefe){
		Component[] components = pan.getComponents();
		for (int i = 0; i < components.length; i++) {
			if(tiefe == 0){
				if(components[i] instanceof JPanel){
					tiefe++;
					generateInstances((JPanel)components[i], tiefe);
				}
				else if(components[i] instanceof JLabel){
					String className = ((JLabel)components[i]).getText();
					System.out.println("Here we have class: "+className);
					try {
						Class cl = Class.forName(this.startObjectPackage+"."+className);
						for (Method m : cl.getMethods()) {
							System.out.println(m.getReturnType().toString() + " - " + m.getName().toString());
							for (Class paramClass: m.getParameterTypes()) {
								System.out.print(" "+paramClass.getName());
							}
						}
						
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else{
				if(components[i] instanceof JPanel)
				{
					tiefe++;
					generateInstances((JPanel)components[i], tiefe);
				}
				else if(components[i] instanceof JLabel && components[i+1] !=null && 
						components[i+1] instanceof JLabel)
					System.out.println("Here we have (innerclass) "+((JLabel)components[i+1]).getText());
				else{
					if (components[i] instanceof JLabel && components[i+1] != null && 
							components[i+1] instanceof JTextField) {
						String variableName = ((JLabel)components[i]).getText();
						String variableValue = ((JTextField)components[i+1]).getText();
						System.out.println("Variable: "+ variableName + " Value: " + variableValue);
					}
				}
			}
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
			
			
		
			for(int i=0; i<tm.getRowCount() ; i++){
				if(tm.getValueAt(i, 2) != null)
				{
					String dataItemCardinality = tm.getValueAt(i,1).toString();
					//System.out.println("Name " + tm.getValueAt(i, 0) + "Values : "+tm.getValueAt(i, 2));
					String dataItemName = tm.getValueAt(i, 0).toString();
					String dataItemValue = tm.getValueAt(i, 2).toString();
					
					if(isSpecialType(dataItemValue)){
						if(dataItemValue.matches("Instance of (.)*")){
							String clazz = dataItemValue.substring(12);
							//System.out.println("The class: "+clazz + " Package: " + this.startObjectPackage);
							//System.out.println("+++++++++++++++ Start deep search +++++++++++++++++++++");
							//System.out.println("Here we have an instance of: "+clazz);
							if(objectAlreadyDisplayed(this.startObjectPackage+"."+clazz) == false)
							{
								innerObjects.add(this.startObjectPackage+"."+clazz);
								this.createInnerElements(dataItemName, dataItemCardinality,  this.startObjectPackage+"."+clazz, tiefe+1, pan);
							}
							else
								System.out.println("Class " + this.startObjectPackage+"."+clazz + " already displayed!");
						}
					}
					else{
						this.createOuterElements(dataItemName, dataItemCardinality, pan, tiefe);
					}
				}
			}
		}
		else{
			System.out.println("Could not create DefaultTableModel ("+startObjectClassName+")");
		}
	}	

	public boolean objectAlreadyDisplayed(String objectClass){
		String objects = "";
		for (String elem : innerObjects) {
			objects +=  " | " + elem;
			if(elem.equals(objectClass))
				return true;
		}
		//System.out.println(objects);
		return false;
	}
	
public void createInnerElements(String dataItemName, String dataItemCardinality, String startObjectClassName, int tiefe, final JPanel pan){
		
		int lastDot = startObjectClassName.lastIndexOf(".");
		String plainStartObjectClassName = startObjectClassName.substring(lastDot + 1);
		
		final JPanel dataPanel = new JPanel();
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
		
		JButton multipleButton = new JButton("+");
		multipleButton.setBounds(new Rectangle(250, 0, 35, 25));
		multipleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//showPanelComponents(dataPanel);
				addMultiple(dataPanel);
			}
		});
		
		System.out.println("Adding: "+dataItemName);
		
		dataPanel.add(valueFieldText, null);
		dataPanel.add(innerClassName, null);
		if(dataItemCardinality.equals("multiple"))
			dataPanel.add(multipleButton, null);
		
		
		
		DefaultTableModel tm = currProject.ontologies4Project.getSlots4Class(startObjectClassName);
		this.createGUI(tm, startObjectClassName, dataPanel, tiefe);
	
		
		Rectangle r = pan.getBounds();
		if(dataPanel.getWidth() > r.width)
			pan.setBounds(r.x, r.y, dataPanel.getWidth(), r.height + 25);
		else
			pan.setBounds(r.x, r.y, r.width, r.height + 25);
		
		
		pan.add(dataPanel);
		
		actualY += 20;
		
		
	}
	
	public void addMultiple(JPanel dataPanel){
		
		JPanel pan = (JPanel) dataPanel.getParent();
		int dataPanelHeight = dataPanel.getHeight();
		pan.setBounds(pan.getX(), pan.getY(), pan.getWidth(), pan.getHeight() + dataPanelHeight);
		this.moveOtherPanels4Multiple(dataPanel, true);
		if(mainPanel.getHeight() > superPanel.getSize().height)
			superPanel.setPreferredSize(new Dimension(superPanel.getSize().width, mainPanel.getHeight()));
		JPanel dataPanelCopy = new JPanel();
		dataPanelCopy.setLayout(null);
		dataPanelCopy.setBorder(dataPanel.getBorder());
		this.createPanelCopy(dataPanelCopy,dataPanel);
		dataPanelCopy.setBounds(dataPanel.getX(), dataPanel.getY() + dataPanelHeight, dataPanel.getWidth(), dataPanel.getHeight());
		pan.add(dataPanelCopy);
		testFrame.validate();
	}
	
	private void createPanelCopy(final JPanel newPanel, JPanel originalPanel) {
		Component[] c = originalPanel.getComponents();
		for (Component component : c) {
			if(component instanceof JPanel)
			{
				JPanel pan = new JPanel();
				pan.setLayout(null);
				JPanel panOrig = (JPanel) component;
				pan.setBounds(panOrig.getX(), panOrig.getY(), panOrig.getWidth(), panOrig.getHeight());
				pan.setBorder(panOrig.getBorder());
				newPanel.add(pan);
				this.createPanelCopy(pan, (JPanel) component);
			}
			else if(component instanceof JLabel){
				JLabel label = new JLabel();
				JLabel origLabel = (JLabel) component;
				label.setBounds(origLabel.getX(), origLabel.getY(), origLabel.getWidth(), origLabel.getHeight());
				label.setText(origLabel.getText());
				label.setFont(origLabel.getFont());
				newPanel.add(label);
			}
			else if (component instanceof JTextField) {
				JTextField text = new JTextField();
				JTextField origText = (JTextField) component;
				text.setBounds(origText.getX(), origText.getY(), origText.getWidth(), origText.getHeight());
				newPanel.add(text);
			}
			else if (component instanceof JButton) {
				JButton button = new JButton();
				JButton buttonOrig = (JButton) component;
				button.setBounds(buttonOrig.getX(), buttonOrig.getY(), buttonOrig.getWidth(), buttonOrig.getHeight());
				button.setText(buttonOrig.getText());
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						addMultiple(newPanel);
					}
				});
				
				newPanel.add(button);
				
				JButton removeComponent = new JButton();
				removeComponent.setBounds(button.getX() + button.getWidth(), button.getY(), button.getWidth(), button.getHeight());
				removeComponent.setText("-");
				removeComponent.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JPanel parent = (JPanel) newPanel.getParent();
						newPanel.setVisible(false);
						//parent.setBounds(parent.getX(), parent.getY() - newPanel.getHeight(), parent.getWidth(), parent.getHeight());
						moveOtherPanels4Multiple(newPanel, false);
						parent.remove(newPanel);
						parent.setBounds(parent.getX(), parent.getY(), parent.getWidth(), parent.getHeight() - newPanel.getHeight());
						parent.validate();
					}
				});
				newPanel.add(removeComponent);
			}
		}
	}


	public void moveOtherPanels4Multiple(JPanel dataPanel, boolean add){
		Component[] panChilds = ((JPanel) dataPanel.getParent()).getComponents();
		boolean flag = false;
		for (Component component : panChilds) {
			if(component == dataPanel)
				flag = true;
			if(add){
				if(flag && component != dataPanel){
					component.setBounds(component.getX(), component.getY() + dataPanel.getHeight(), component.getWidth(), component.getHeight());
				}
			}
			else
			{
				if(component.getY() > dataPanel.getY())
					component.setBounds(component.getX(), component.getY() - dataPanel.getHeight(), component.getWidth(), component.getHeight());
				}
			}
	}
	
	


	public void showPanelComponents(JPanel pan){
		Component [] c = pan.getComponents();
		for (Component component : c) {
			if(component instanceof JPanel)
				showPanelComponents((JPanel)component);
			else if(component instanceof JLabel)
				System.out.println("JLabel: " + ((JLabel) component).getText());
		}
	}


	public int getCorrectOuterHeight(JPanel pan){
		int height = 0;
		Component[] c = pan.getComponents();
		for(int i = 0; i<c.length; i++){
			height += c[i].getHeight();
		}
		return height;
	}

	
	public void createOuterElements(String dataItemName, String dataItemCardinality, JPanel pan, int tiefe){
		
		Rectangle r2 = pan.getBounds();
		//System.out.println("Panel x,y + width height" + r2.getX() + " " + r2.y + " " + r2.width+ " " + r2.height );
		
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		int innerX = 0;
		if(tiefe == 0)
		{
			System.out.println("Flat object: " + dataItemName + " tiefe: " + tiefe + " actualY " + actualY);
			dataPanel.setBounds(new Rectangle(10, actualY , 250, 30));
		}
		else
		{
			innerX = actualX + (einrueckungProUntereEbene*tiefe);
			//dataPanel.setBounds(new Rectangle(innerX, r2.height + 10 , 250, 30));
			dataPanel.setBounds(new Rectangle(innerX, this.getCorrectOuterHeight(pan) - 10, 250, 30));
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
		pan.setBounds(r.x, r.y, r.width + innerX, this.getCorrectOuterHeight(pan) + 30);
		pan.add(dataPanel);
		
		if(actualY > superPanel.getSize().height)
			superPanel.setPreferredSize(new Dimension(superPanel.getSize().width, actualY));
		
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
