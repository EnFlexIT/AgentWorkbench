/**
 * @author Hanno - Felix Wagner, 13.04.2010
 * Copyright 2010 Hanno - Felix Wagner
 * 
 * This file is part of ContMAS.
 *
 * ContMAS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ContMAS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ContMAS.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package contmas.main;

import jade.content.Concept;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.*;

import contmas.ontology.BayMap;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class LoadBayMapFrame extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID= -4640512083329778024L;
	private JPanel jPanel=null;
	private JButton buttonLookupFile=null;
	private JTextField textFieldFileName=null;
	private JLabel labelFile=null;
	private JButton buttonChooseBayMap=null;
	private JScrollPane scrollPaneBayMaps=null;
	private JList listBayMaps=null;
	protected String workingDir="";

	/**
	 * This method initializes 
	 * 
	 */
	public LoadBayMapFrame(){
		super();
		this.initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize(){
		this.setSize(new Dimension(268,168));
		this.setModal(true);
		this.setTitle("Load BayMap");
		this.setContentPane(this.getJPanel());

	}
	
	public void setWorkingDir(String workingDir){
		this.workingDir=workingDir;
	}

	private OWLImportMapper getMapper(String fileName){
		String ontologyJavaPackage=this.getClass().getPackage().getName();
		ontologyJavaPackage=ontologyJavaPackage.substring(0,ontologyJavaPackage.lastIndexOf(".")) + ".ontology";

		return new OWLImportMapper(fileName,ontologyJavaPackage);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0){
		// TODO Auto-generated method stub

	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel(){
		if(this.jPanel == null){
			this.labelFile=new JLabel();
			this.labelFile.setBounds(new Rectangle(5,9,38,16));
			this.labelFile.setText("File:");
			this.jPanel=new JPanel();
			this.jPanel.setLayout(null);
			this.jPanel.add(this.getButtonLookupFile(),null);
			this.jPanel.add(this.getTextFieldFileName(),null);
			this.jPanel.add(this.labelFile,null);
			this.jPanel.add(this.getButtonChooseBayMap(),null);
			this.jPanel.add(this.getScrollPaneBayMaps(),null);
		}
		return this.jPanel;
	}

	/**
	 * This method initializes buttonLookupFile	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButtonLookupFile(){
		if(this.buttonLookupFile == null){
			this.buttonLookupFile=new JButton();
			this.buttonLookupFile.setBounds(new Rectangle(219,7,34,23));
			this.buttonLookupFile.setText("...");
			this.buttonLookupFile.addActionListener(new java.awt.event.ActionListener(){

				public void actionPerformed(java.awt.event.ActionEvent e){
					final JFileChooser fc=new JFileChooser();
					final String structureFileName="resources\\contmas_ontology.owl";
					final String ontologyGeneratorFileName="resources\\OWLSimpleJADEAbstractOntology.owl";

					Integer returnVal=fc.showOpenDialog(LoadBayMapFrame.this.jPanel);

					if(returnVal == JFileChooser.APPROVE_OPTION){
						File file=fc.getSelectedFile();
						LoadBayMapFrame.this.getTextFieldFileName().setText(file.getAbsolutePath());
						OWLImportMapper mapper=LoadBayMapFrame.this.getMapper(file.getAbsolutePath());
						mapper.setStructureFile(workingDir + structureFileName);
						mapper.setBeanGeneratorFile(workingDir + ontologyGeneratorFileName);
						HashMap<String, Object> allBayMaps=mapper.getNamedMappedIndividualsOf("BayMap");
						java.util.Iterator<String> iter=allBayMaps.keySet().iterator();

						DefaultListModel bayMapList=new DefaultListModel();

						while(iter.hasNext()){
							String curName=iter.next();
							BayMap curBayMap=(BayMap) allBayMaps.get(curName);
							bayMapList.addElement(new OntologyElement(curBayMap," " + curName));
						}
						LoadBayMapFrame.this.getListBayMaps().setModel(bayMapList);
					}
				}
			});
		}
		return this.buttonLookupFile;
	}

	/**
	 * This method initializes textFieldFileName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTextFieldFileName(){
		if(this.textFieldFileName == null){
			this.textFieldFileName=new JTextField();
			this.textFieldFileName.setBounds(new Rectangle(50,7,160,20));
		}
		return this.textFieldFileName;
	}

	/**
	 * This method initializes buttonChooseBayMap	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButtonChooseBayMap(){
		if(this.buttonChooseBayMap == null){
			this.buttonChooseBayMap=new JButton();
			this.buttonChooseBayMap.setBounds(new Rectangle(11,115,244,20));
			this.buttonChooseBayMap.setText("Choose BayMap");
			this.buttonChooseBayMap.addActionListener(new java.awt.event.ActionListener(){
				public void actionPerformed(java.awt.event.ActionEvent e){
					LoadBayMapFrame.this.setVisible(false);
				}
			});
		}
		return this.buttonChooseBayMap;
	}

	/**
	 * This method initializes scrollPaneBayMaps	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollPaneBayMaps(){
		if(this.scrollPaneBayMaps == null){
			this.scrollPaneBayMaps=new JScrollPane();
			this.scrollPaneBayMaps.setBounds(new Rectangle(9,33,244,80));
			this.scrollPaneBayMaps.setViewportView(this.getListBayMaps());
		}
		return this.scrollPaneBayMaps;
	}

	/**
	 * This method initializes listBayMaps	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getListBayMaps(){
		if(this.listBayMaps == null){
			this.listBayMaps=new JList();
			this.listBayMaps.setModel(new DefaultListModel());
		}
		return this.listBayMaps;
	}

	public BayMap getChosenBayMap(){
		if( !this.getListBayMaps().isSelectionEmpty()){
			Concept chosenBayMap=((OntologyElement) this.getListBayMaps().getSelectedValue()).getOntologyConcept();
			if((chosenBayMap != null) & (chosenBayMap instanceof BayMap)){
				return (BayMap) chosenBayMap;
			}
		}
		return null;
	}

} //  @jve:decl-index=0:visual-constraint="10,10"
