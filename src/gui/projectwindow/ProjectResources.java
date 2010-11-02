package gui.projectwindow;

import common.ClassLoaderUtil;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import application.Application;
import application.Project;

public class ProjectResources extends JPanel implements ActionListener, Observer {

	private static final long serialVersionUID = 1L;

	private Project currProjet = null;

	private JList jListResources = null;

	private JButton jButtonAdd = null;

	private JButton jButtonRemove = null;
	
	 DefaultListModel myModel=null;

	private JButton jButton = null;
	
	/**
	 * This is the default constructor
	 */
	public ProjectResources(Project cp) {
		super();
		currProjet = cp;
		initialize();
		myModel=new DefaultListModel();
		jListResources.setModel(myModel);
	;
	}
	private String adjustString(String path)
	{
		final String projectFolder=currProjet.getProjectFolder();
		if(path.contains(projectFolder))
		{
			int find=path.indexOf(projectFolder);
			return path.substring(find-1);
		
		}	
		return path;
		
	
	}
	
	private boolean alreay_there(String path)
	{
		
		return currProjet.projectResources.contains(path);
	}
	
	
	private Vector<String> adjustPaths(File[] files)
	{
	
		Vector<String> result=new Vector<String>();
	
		
		if(files!=null)
		{
			
			for(File file : files)
			{
				String path=file.getAbsolutePath();
				if(!path.contains(".jar"))
				{
					
					Vector<String> directoryFiles=handleDirectories(file);
					 for(String foreignJar : directoryFiles)
					 {
						if(!alreay_there(foreignJar))
						{
						 result.add(this.adjustString(foreignJar)); // Use relative paths within projects
						}
					
						}
					 
				}
				else
				
				{
				if(!alreay_there(path))
				{
				result.add(this.adjustString(path)); // Use absolut within projects
				}
			
				}
			
								
			
			 }
		}
		
		 return result;
		}
		
	
	

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 3;
		gridBagConstraints11.gridy = 0;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 2;
		gridBagConstraints2.gridy = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(20, 20, 20, 20);
		gridBagConstraints.gridx = 0;
		this.setSize(581, 273);
		this.setLayout(new GridBagLayout());
		this.add(getJListResources(), gridBagConstraints);
		this.add(getJButtonAdd(), gridBagConstraints1);
		this.add(getJButtonRemove(), gridBagConstraints2);
		this.add(getJButton(), gridBagConstraints11);
	}

	private Vector<String> handleDirectories(File dir)
	{
		Vector<String> result=new Vector<String>();
		try 
		{
			result.add(dir.getAbsolutePath());
		
			File[] possibleJars = dir.listFiles(); 	// Find all Files
	
			    for(File possibleJar : possibleJars)
			    {
			    	
			    	
			    	if(possibleJar.getAbsolutePath().contains(".jar"))
			    	{
			    		
			    		//result.add(possibleJar.getAbsolutePath());
			    		//ClassLoaderUtil.addFile(possibleJar);
			    		
			    		
			    	}
			    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
			return result;
		}
		
	
	
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

	/**
	 * This method initializes jListResources	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getJListResources() {
		if (jListResources == null) {
			jListResources = new JList();
		}
		return jListResources;
	}

	/**
	 * This method initializes jButtonAdd	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAdd() {
		if (jButtonAdd == null) {
			jButtonAdd = new JButton();
			jButtonAdd.setText("Add");
			jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				
					JFileChooser chooser=new JFileChooser();
					
					chooser.setCurrentDirectory(new File(currProjet.getProjectFolderFullPath()));
					FileNameExtensionFilter filter = new FileNameExtensionFilter("jar","JAR");
				    chooser.setFileFilter(filter);
					chooser.setMultiSelectionEnabled(true); 
					chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					chooser.setAcceptAllFileFilterUsed(false);
					chooser.showDialog(jButtonAdd, "Load Files");
					Vector<String > names=adjustPaths(chooser.getSelectedFiles());
					currProjet.projectResources.addAll(names);
					for(String name: names)
					{
					myModel.addElement(name);
					}
					// myModel.addElement(names);
					
					 jListResources.updateUI();
					 currProjet.setChangedAndNotify("projectResources");
					
					
				}
			});
		}
		return jButtonAdd;
	}

	/**
	 * This method initializes jButtonRemove	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRemove() {
		if (jButtonRemove == null) {
			jButtonRemove = new JButton();
			jButtonRemove.setText("Remove");
			jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				System.out.println( jListResources.getSelectedValue());
				Object [] values= jListResources.getSelectedValues();
				//Remove from the classpath
				if(values!=null)
				{
					for(Object file: values)
					{
						myModel.removeElement(file);
						currProjet.projectResources.remove(file);
						String jarFile=(String) file;
						
						jarFile=ClassLoaderUtil.adjustPathForLoadin(jarFile, currProjet.getProjectFolder(), currProjet.getProjectFolderFullPath());
						
						
						try {
							ClassLoaderUtil.removeFile(jarFile);
						} catch (RuntimeException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (NoSuchFieldException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						
					}
					 currProjet.setChangedAndNotify("projectResources");
				}
				
				
				}
			});
		}
		return jButtonRemove;
	}
	
	
		
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Load");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String [] res=new String[jListResources.getSelectedValues().length];
					
					for(Object obj:jListResources.getSelectedValues())
					{
						int index=0;
						String test=obj.toString();
						res[index]=test;
						index++;
					}
					
				
				
					

				
					
					try {
						//D:\\JadeAgent\\AgentGUI\\projects\\bla\\
						
					
						
					
						Vector<String> allClasses=new Vector<String>();
					
					    
						ClassLoaderUtil.addFile(Application.RunInfo.PathJade(true));
						for(String selectedJar: res)
						{
						
						File file = null;
					
						selectedJar=ClassLoaderUtil.adjustPathForLoadin(selectedJar, currProjet.getProjectFolder(), currProjet.getProjectFolderFullPath());
						file=new File(selectedJar);	
						URL url=file.toURI().toURL();
						if(url.getPath().contains(".jar"))
						{
						ClassLoaderUtil.addFile(file.getAbsoluteFile());
						
						Vector<String> classNames=ClassLoaderUtil.getClassNamesFromJar(url);
						allClasses.addAll(classNames);
						}
						}
						
						Application.JadePlatform.jadeStart();
					
						
						
						
					   AgentContainer container=Application.JadePlatform.MASmc;
				
					   //Application.JadePlatform.jadeAgentStart(className, className);
						//Application.JadePlatform.jadeAgentStart(className, c, null, container.getContainerName());
					
					   Vector<AgentController> controller=ClassLoaderUtil.loadAgentsIntoContainer(allClasses, container);
				     
					  
					
					
					  // System.out.println("ClassPath:"+Application.RunInfo.getClassPathEntries());
					   //Application.RunInfo.getJadeDefaultPlatformConfig().
					   
					   for(AgentController control: controller)
					   {
						  
						   if(control.getName().contains("StartAgent"))
						   { 
							 
							  // control.start();
						   }
					   }
							
						
					
				
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						  System.out.println("Fehler:"+e1.getMessage());
						  e1.printStackTrace();
					} catch (Throwable e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}	
					
				
					
				}
			});
		}
		return jButton;
	}
	

}  //  @jve:decl-index=0:visual-constraint="-105,-76"
