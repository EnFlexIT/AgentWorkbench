package de.enflexit.awb.ws.ui.client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.client.ApiRegistration;
import de.enflexit.awb.ws.client.AwbApiRegistrationService;
import de.enflexit.awb.ws.client.WsCredentialStore;
import de.enflexit.awb.ws.ui.WsConfigurationInterface;
import de.enflexit.common.ServiceFinder;

/**
 * The Class JPanelClientConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelClientBundle extends JPanel implements WsConfigurationInterface{
	
	private static final long serialVersionUID = 7987858783733542296L;
	private JScrollPane jScrollPaneBundleList;
	private JList<ApiRegistration> jListApiRegistration;
	private DefaultListModel<ApiRegistration> listModelRegisteredApis;
	
	private JPanel jPanelInfo;
		private JLabel jLabelDescription;
		private JScrollPane jScrollPaneDescription;
		private JTextArea jTextAreaDescription;
		private JPanel jPanelHeader;
		private JLabel jLabelBundleList;
		private JButton jButtonCachedCredentialAssignmentsView;
	
	/**
	 * Instantiates a new j panel client configuration.
	 */
	public JPanelClientBundle() {
		this.initialize();
	}
	
	private void initialize() {		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{26, 0, 150, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(getJPanelHeader(), gbc_panel);
		GridBagConstraints gbc_jScrollPaneBundleList = new GridBagConstraints();
		gbc_jScrollPaneBundleList.insets = new Insets(5, 0, 5, 0);
		gbc_jScrollPaneBundleList.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneBundleList.gridx = 0;
		gbc_jScrollPaneBundleList.gridy = 1;
		add(getJScrollPaneBundleList(), gbc_jScrollPaneBundleList);
		GridBagConstraints gbc_jPanelInfo = new GridBagConstraints();
		gbc_jPanelInfo.insets = new Insets(10, 0, 0, 0);
		gbc_jPanelInfo.fill = GridBagConstraints.BOTH;
		gbc_jPanelInfo.gridx = 0;
		gbc_jPanelInfo.gridy = 2;
		add(getJPanelInfo(), gbc_jPanelInfo);
	}
	
	/**
	 * Gets the {@link JScrollPane} of the {@link ApiRegistration}-Bundle list.
	 *
	 * @return the {@link JScrollPane} bundle list
	 */
	private JScrollPane getJScrollPaneBundleList() {
		if (jScrollPaneBundleList == null) {
			jScrollPaneBundleList = new JScrollPane();
			jScrollPaneBundleList.setViewportView(getJListApiRegistration());
		}
		return jScrollPaneBundleList;
	}
	
	/**
	 * Gets the corresponding {@link ListModel} of the JListApiRegistration.
	 *
	 * @return the list model registered apis
	 */
	private DefaultListModel<ApiRegistration> getListModelRegisteredApis() {
		if (listModelRegisteredApis==null) {
			refillListModelRegisteredApis();
		}
		return listModelRegisteredApis;
	}

	/**
	 * Creates and fills a new {@link ListModel} for the JListAPIRegistration.
	 */
	public void refillListModelRegisteredApis() {
		listModelRegisteredApis = new DefaultListModel<ApiRegistration>();
		List<AwbApiRegistrationService> apiRegServiceList=WsCredentialStore.getInstance().getApiRegistrationServiceList();
		List<ApiRegistration> apiRegList=new ArrayList<>();
		for (AwbApiRegistrationService awbApiRegistrationService : apiRegServiceList) {
			apiRegList.add(new ApiRegistration(awbApiRegistrationService));
		}
		listModelRegisteredApis.addAll(apiRegList);
		this.getJListApiRegistration().setModel(listModelRegisteredApis);
		this.repaint();
		this.revalidate();
	}
	
	/**
	 * Gets the JListAPIRegistration.
	 *
	 * @return the j list api registration
	 */
	public JList<ApiRegistration> getJListApiRegistration() {
		if (jListApiRegistration == null) {
			jListApiRegistration = new JList<ApiRegistration>(this.getListModelRegisteredApis());
			jListApiRegistration.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListApiRegistration.setFont(new Font("Dialog", Font.PLAIN, 12));
			jListApiRegistration.addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					ApiRegistration apiReg=jListApiRegistration.getSelectedValue();
					if (apiReg != null) {
						if (apiReg.getDescription() != null && getJTextAreaDescription() != null) {
							getJTextAreaDescription().setText(apiReg.getDescription());
						}
					}
				}
			});
		}
		return jListApiRegistration;
	}
	
	
	/**
	 * Gets the j panel info.
	 *
	 * @return the j panel info
	 */
	private JPanel getJPanelInfo() {
		if (jPanelInfo == null) {
			jPanelInfo = new JPanel();
			
			GridBagLayout gbl_jPanelInfo = new GridBagLayout();
			gbl_jPanelInfo.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelInfo.rowHeights = new int[]{0, 0, 0};
			gbl_jPanelInfo.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelInfo.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			jPanelInfo.setLayout(gbl_jPanelInfo);
			
			GridBagConstraints gbc_jLabelDescription = new GridBagConstraints();
			gbc_jLabelDescription.anchor = GridBagConstraints.WEST;
			gbc_jLabelDescription.gridx = 0;
			gbc_jLabelDescription.gridy = 0;
			jPanelInfo.add(getJLabelDescription(), gbc_jLabelDescription);
			
			GridBagConstraints gbc_jTextAreaDescription = new GridBagConstraints();
			gbc_jTextAreaDescription.insets = new Insets(5, 0, 0, 0);
			gbc_jTextAreaDescription.gridwidth = 2;
			gbc_jTextAreaDescription.fill = GridBagConstraints.BOTH;
			gbc_jTextAreaDescription.gridx = 0;
			gbc_jTextAreaDescription.gridy = 1;
			jPanelInfo.add(getJScrollPaneDescription(), gbc_jTextAreaDescription);
		}
		return jPanelInfo;
	}
	
	/**
	 * Gets the j label description.
	 *
	 * @return the j label description
	 */
	private JLabel getJLabelDescription() {
		if (jLabelDescription == null) {
			jLabelDescription = new JLabel("Description:");
			jLabelDescription.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelDescription;
	}
	
	/**
	 * Gets the j scroll pane description.
	 *
	 * @return the j scroll pane description
	 */
	private JScrollPane getJScrollPaneDescription() {
		if (jScrollPaneDescription==null) {
			jScrollPaneDescription = new JScrollPane();
			jScrollPaneDescription.setViewportView(this.getJTextAreaDescription());
		}
		return jScrollPaneDescription;
	}
	
	/**
	 * Gets the j text area description.
	 *
	 * @return the j text area description
	 */
	private JTextArea getJTextAreaDescription() {
		if (jTextAreaDescription == null) {
			jTextAreaDescription = new JTextArea();
			jTextAreaDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextAreaDescription.setWrapStyleWord(true);
			jTextAreaDescription.setLineWrap(true);
			jTextAreaDescription.setEditable(false);
			jTextAreaDescription.setBackground(this.getBackground());
		}
		return jTextAreaDescription;
	}
	
	public JButton getJButtonCachedCredentialAssignmentsView() {
		if (jButtonCachedCredentialAssignmentsView == null) {
			jButtonCachedCredentialAssignmentsView = new JButton(BundleHelper.getImageIcon("cache.png"));
			jButtonCachedCredentialAssignmentsView.setToolTipText("See cached Credential Assignments");
			jButtonCachedCredentialAssignmentsView.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
		}
		if(WsCredentialStore.getInstance().getCacheCredentialAssignmentList().size()>0) {
		   jButtonCachedCredentialAssignmentsView.setVisible(true);
		}else {
		   jButtonCachedCredentialAssignmentsView.setVisible(false);
		}
		return jButtonCachedCredentialAssignmentsView;
	}
	
	private JPanel getJPanelHeader() {
		if (jPanelHeader == null) {
			jPanelHeader = new JPanel();
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{0, 0};
			gbl_panel.rowHeights = new int[]{0, 0};
			gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelHeader.setLayout(gbl_panel);
			GridBagConstraints gbc_jLabelBundleList = new GridBagConstraints();
			gbc_jLabelBundleList.gridx = 0;
			gbc_jLabelBundleList.gridy = 0;
			jPanelHeader.add(getJLabelBundleList_1(), gbc_jLabelBundleList);
			GridBagConstraints gbc_jButtonDeleteCredentialAssignment = new GridBagConstraints();
			gbc_jButtonDeleteCredentialAssignment.gridx = 2;
			gbc_jButtonDeleteCredentialAssignment.gridy = 0;
			jPanelHeader.add(this.getJButtonCachedCredentialAssignmentsView(), gbc_jButtonDeleteCredentialAssignment);
		}
		return jPanelHeader;
	}
	
	private JLabel getJLabelBundleList_1() {
		if (jLabelBundleList == null) {
			jLabelBundleList = new JLabel("Server - API / Client Bundle");
			jLabelBundleList.setPreferredSize(new Dimension(150, 26));
			jLabelBundleList.setMinimumSize(new Dimension(150, 26));
			jLabelBundleList.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelBundleList;
	}
	
	//-----------------------------------------------------------
	//-------------Overridden methods----------------------------
	//-----------------------------------------------------------
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	 */
	@Override
	public boolean hasUnsavedChanges() {
		if (this.getJListApiRegistration() == null)
			return false;
		
		// Get actual ApiRegistrationList
		List<AwbApiRegistrationService> apiRegList = ServiceFinder.findServices(AwbApiRegistrationService.class);

		// Get old List
		ArrayList<ApiRegistration> credArrayList = new ArrayList<ApiRegistration>();
		ListModel<ApiRegistration> credModel = getJListApiRegistration().getModel();
		for (int i = 0; i < credModel.getSize(); i++) {
			ApiRegistration cred = credModel.getElementAt(i);
			credArrayList.add(cred);
		}
		if (credArrayList.size() != apiRegList.size()) {
			return true;
		} else {
			return !compareTwoLists(credArrayList,apiRegList);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#userConfirmedToChangeView()
	 */
	@Override
	public boolean userConfirmedToChangeView() {
		return hasUnsavedChanges();
	}
	
	//--------------------------------------------------
	//------------Helping Methods to reduce Code------------
	//--------------------------------------------------
	
	/**
	 * 
	 * @param apiRegList
	 * @param credArrayList ArrayList filled with Instance of 
	 * @return false, if the two Lists are not the same
	 */
	private boolean compareTwoLists(List<ApiRegistration> apiRegList, List<AwbApiRegistrationService> credArrayList) {
		boolean sameObject = false;
		for (int i = 0; i < credArrayList.size(); i++) {
			ApiRegistration apiReg = apiRegList.get(i);
			AwbApiRegistrationService apiRegService = credArrayList.get(i);
			if (apiReg.getClientBundleName().equals(apiRegService.getClientBundleName())) {
				if (apiReg.getCredentialType().equals(apiRegService.getCredentialType())) {
					if (apiReg.getDefaultCredentialName().equals(apiRegService.getDefaultCredentialName())) {
						if (apiReg.getDescription().equals(apiRegService.getDescription())) {
							if (apiReg.getDefaultURL().equals(apiRegService.getDefaultServerURL())) {
								sameObject = true;
							}
						}
					}
				}
			}
			if(!sameObject) {
				break;
			}
		}
		return sameObject;
	}
}
