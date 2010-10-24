package gui.projectwindow;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import application.Project;

public class ProjectResources extends JPanel implements ActionListener, Observer {

	private static final long serialVersionUID = 1L;

	private Project currProjet = null;

	private JList jListResources = null;

	private JButton jButtonAdd = null;

	private JButton jButtonRemove = null;
	
	 DefaultListModel myModel=null;
	
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
	}

	private Vector<String> handleDirectories(File dir)
	{
		Vector<String> result=new Vector<String>();
		
			File[] possibleJars = dir.listFiles(); 	// Find all Files
	
			    for(File possibleJar : possibleJars)
			    {
			    	
			    	
			    	if(possibleJar.getAbsolutePath().contains(".jar"))
			    	{
			    		
			    		result.add(possibleJar.getAbsolutePath());
			    		
			    		
			    	}
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
						
						
					}
					 currProjet.setChangedAndNotify("projectResources");
				}
				
				
				}
			});
		}
		return jButtonRemove;
	}
	

}  //  @jve:decl-index=0:visual-constraint="-105,-76"
