package agentgui.core.gui.projectwindow;



import java.awt.Cursor;
import java.awt.Dimension;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.common.ClassLoaderUtil;

public class ProjectResources extends JPanel implements ActionListener, Observer {

	private static final long serialVersionUID = 1L;

	private Project currProjet = null;

	private JList jListResources = null;

	private JButton jButtonAdd = null;

	private JButton jButtonRemove = null;
	
	 DefaultListModel myModel=null;

	private JButton jButtonRefresh = null;

	private JPanel jPanelRight = null;
	
	final static String PathImage = Application.RunInfo.PathImageIntern();

	private JScrollPane jScrollPane = null;

	/**
	 * This is the default constructor
	 */
	public ProjectResources(Project cp) {
		super();
		currProjet = cp;
		initialize();
		myModel=new DefaultListModel();
		jListResources.setModel(myModel);
		
	
		for(String file : currProjet.projectResources )
		{
			myModel.addElement(file);
		}
	
		
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
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.weighty = 1.0;
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 0;
		gridBagConstraints11.insets = new Insets(10, 10, 2, 5);
		gridBagConstraints11.weightx = 1.0;
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 28;
		gridBagConstraints12.fill = GridBagConstraints.NONE;
		gridBagConstraints12.insets = new Insets(10, 5, 5, 10);
		gridBagConstraints12.anchor = GridBagConstraints.NORTH;
		gridBagConstraints12.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(10, 10, 5, 10);
		gridBagConstraints.gridx = 0;
		this.setSize(581, 273);
		this.setLayout(new GridBagLayout());
		this.add(getJPanelRight(), gridBagConstraints12);
		this.add(getJScrollPane(), gridBagConstraints11);
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
			jButtonAdd.setPreferredSize(new Dimension(45, 26));
			jButtonAdd.setIcon(new ImageIcon(getClass().getResource( PathImage +"ListPlus.png")));
			jButtonAdd.setToolTipText("Add");
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
					
					//name=ClassLoaderUtil.adjustPathForLoadin(name, currProjet.getProjectFolder(), currProjet.getProjectFolderFullPath());
					
				
					
					
					try
					{
			
					
						remove();
					
						load();
					
						
					
						
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					
					}
					}
				
					
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
			
			jButtonRemove.setIcon(new ImageIcon(getClass().getResource( PathImage+"ListMinus.png")));
			jButtonRemove.setPreferredSize(new Dimension(45, 26));
			jButtonRemove.setToolTipText("Remove");

			jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
			
				
					checkJadeState();
				//Remove from the classpath
				Object [] values= jListResources.getSelectedValues();
				for(Object file : values)
				{
					
					

						myModel.removeElement(file);
						currProjet.projectResources.remove(file);
				}
				
				try 
				{
					remove();
					load();
				}
				catch(Exception e1)
				{
							e1.printStackTrace();
				}
				
		}
				
				
				
				
			});
		}
		return jButtonRemove;
	}
	
	boolean checkJadeState()
	{
		if(Application.JadePlatform.jadeMainContainerIsRunning())
		{
			String MsgHead = Language.translate("JADE wird zur Zeit ausgeführt!");
			String MsgText = Language.translate("Möchten Sie JADE nun beenden und fortfahren?");
			Integer MsgAnswer =  JOptionPane.showInternalConfirmDialog( Application.MainWindow.getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
			if ( MsgAnswer == 1 ) return false; // --- NO,just exit 
			// --- Stop the JADE-Platform -------------------
			Application.JadePlatform.jadeStop();
			return true;
		}
		return false;
	}
	
	
	public void remove()
	{
		this.checkJadeState();
		for(int i=0;i<myModel.size();i++)
		{
			
			Object file=myModel.get(i);

				
				String jarFile=(String) file;
				
				jarFile=ClassLoaderUtil.adjustPathForLoadin(jarFile, currProjet.getProjectFolder(), currProjet.getProjectFolderFullPath());
				
				
				try {
					ClassLoaderUtil.removeFile(jarFile);
					ClassLoaderUtil.removeJarFromClassPath(jarFile);
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
		
		
		
	
	
	
	
	public void load()
	{
		
		try {
			
			
		    int size=myModel.size();
		
			String [] res=new String[size];
			
			for(int index=0;index<size;index++)
			{
				res[index]= myModel.getElementAt(index).toString();
				
				
			}
			
		
	
		
		    
		
			for(String selectedJar: res)
			{
			
			File file = null;
		
			selectedJar=ClassLoaderUtil.adjustPathForLoadin(selectedJar, currProjet.getProjectFolder(), currProjet.getProjectFolderFullPath());
			file=new File(selectedJar);	
			//URL url=file.toURI().toURL();
			//if(url.getPath().contains(".jar"))
			//{
				
				ClassLoaderUtil.addFile(file.getAbsoluteFile());
				ClassLoaderUtil.addJarToClassPath(selectedJar);
			
			
			//}
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
	
		
	/**
	 * This method initializes jButtonRefresh	
	 * 	
	 * @return javax.swing.JButton	
	 */
	
	private JButton getJButtonRefresh() {
		if (jButtonRefresh == null) {
			jButtonRefresh = new JButton();
			
			jButtonRefresh.setIcon(new ImageIcon(getClass().getResource( PathImage+"Refresh.png")));
			jButtonRefresh.setPreferredSize(new Dimension(45, 26));
			jButtonRefresh.setToolTipText("Refresh");
			
			
			jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Application.MainWindow.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
					remove();				
				   load();					
					Application.MainWindow.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				
				
					
				
					
				}
			});
		}
		return jButtonRefresh;
	}
	/**
	 * This method initializes jPanelRight	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelRight() {
		if (jPanelRight == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new Insets(20, 0, 0, 0);
			gridBagConstraints3.gridy = 2;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = -1;
			gridBagConstraints1.insets = new Insets(0, 0, 5, 0);
			gridBagConstraints1.gridy = -1;
			jPanelRight = new JPanel();
			jPanelRight.setLayout(new GridBagLayout());
			jPanelRight.add(getJButtonAdd(), gridBagConstraints1);
			jPanelRight.add(getJButtonRemove(), gridBagConstraints2);
			jPanelRight.add(getJButtonRefresh(), gridBagConstraints3);
		}
		return jPanelRight;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJListResources());
		}
		return jScrollPane;
	}
	

}  //  @jve:decl-index=0:visual-constraint="-105,-76"
