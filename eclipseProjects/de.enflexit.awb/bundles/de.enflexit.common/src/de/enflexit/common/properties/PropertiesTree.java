package de.enflexit.common.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * The Class PropertiesTree transfers a {@link Properties} instance into a tree structure.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertiesTree extends DefaultTreeModel {

	private static final long serialVersionUID = 6862973101047809438L;

	private HashMap<String, DefaultMutableTreeNode> treeNodeHash;
	
	
	/**
	 * Instantiates a new properties tree that contains each single properties.
	 * @param properties the properties
	 */
	public PropertiesTree(Properties properties) {
		super(new DefaultMutableTreeNode());
		this.fillTree(properties, null);
	}
	/**
	 * Instantiates a new properties tree, filtered by the specified filter phrase.
	 *
	 * @param properties the properties
	 * @param filterPhrase the filter phrase
	 */
	public PropertiesTree(Properties properties, String filterPhrase) {
		super(new DefaultMutableTreeNode());
		this.fillTree(properties, filterPhrase);
	}
	
	/**
	 * Return the root node. Will invoke {@link #getRoot()} and casts the root to a {@link DefaultMutableTreeNode}.
	 * @return the root node
	 */
	private DefaultMutableTreeNode getRootNode() {
		return (DefaultMutableTreeNode) this.getRoot();
	}
	
	/**
	 * Fills tree.
	 *
	 * @param properties the properties
	 * @param filterPhrase the filter phrase
	 */
	private void fillTree(Properties properties, String filterPhrase) {
		
		List<String> idList = properties.getIdentifierList();
		for (String identifier : idList) {
			
			// --- Get PropertyValue ------------------------------------------
			PropertyValue pValue = properties.getPropertyValue(identifier);

			// --- Check for the filter phrase --------------------------------
			if (this.isFilterMatch(filterPhrase, identifier, pValue)==false) continue;
			
			// --- Split by dot -----------------------------------------------
			String idPath[] = identifier.split("\\.");
			if (idPath.length==1) {
				// --- Simple element -----------------------------------------
				this.addPropertyNode(this.getRootNode(), identifier, identifier, pValue);
				
			} else {
				// --- Tree element -------------------------------------------
				String pathID = "";
				for (int i = 0; i < idPath.length; i++) {

					String parentPathID = pathID;
					DefaultMutableTreeNode parentNode = this.getNode(parentPathID);
					
					pathID += idPath[i] + "."; 
					DefaultMutableTreeNode checkNode = this.getNode(pathID);
					if (checkNode!=this.getRootNode()) continue;
					
					boolean isLeaf = i==idPath.length-1; 
					if (isLeaf==true) {
						this.addPropertyNode(parentNode, pathID, identifier, pValue);
					} else {
						this.addPropertyNode(parentNode, pathID, null, null);
					}
				}
			}
		}
	}
	/**
	 * Checks if identifier or PropertyValue are a filter match.
	 *
	 * @param filterPhrase the filter phrase
	 * @param identitier the identitier
	 * @param propertyValue the property value
	 * @return true, if successful
	 */
	private boolean isFilterMatch(String filterPhrase, String identitier, PropertyValue propertyValue) {
		
		if (filterPhrase==null || filterPhrase.isEmpty() || filterPhrase.isBlank()) return true;
		
		if (this.isFilterMatch(filterPhrase, identitier)==true) return true;
		if (propertyValue!=null) {
			if (this.isFilterMatch(filterPhrase, propertyValue.getValueClass())==true) return true;
			if (this.isFilterMatch(filterPhrase, propertyValue.getValueString())==true) return true;
		}
		return false;
		
	}
	/**
	 * Checks if the specified String is a filter match.
	 *
	 * @param filterPhrase the filter phrase
	 * @param checkString the check string
	 * @return true, if is filter match
	 */
	private boolean isFilterMatch(String filterPhrase, String checkString) {
		if (checkString==null || checkString.isEmpty() || checkString.isBlank()) return false;
		return checkString.toLowerCase().matches("(?i).*(" + filterPhrase.toLowerCase() + ").*");
	}
	
	
	/**
	 * Adds the property node.
	 *
	 * @param parentNode the parent node
	 * @param pathID the path ID
	 * @param identifier the identifier
	 * @param propertyValue the property value
	 * @param displayInstruction the display instruction
	 * @return the default mutable tree node
	 */
	private DefaultMutableTreeNode addPropertyNode(DefaultMutableTreeNode parentNode, String pathID, String identifier, PropertyValue propertyValue) {
		
		PropertyNodeUserObject pnuo = new PropertyNodeUserObject(identifier, propertyValue, pathID);
		DefaultMutableTreeNode nodeAdded = new DefaultMutableTreeNode(pnuo);
		parentNode.add(nodeAdded);
		this.getTreeNodeHash().put(pathID, nodeAdded);
		return nodeAdded;
	}
	
	/**
	 * Returns the tree node hash.
	 * @return the tree node hash
	 */
	private HashMap<String, DefaultMutableTreeNode> getTreeNodeHash() {
		if (treeNodeHash==null) {
			treeNodeHash = new HashMap<>();
		}
		return treeNodeHash;
	}
	/**
	 * Returns the parent node for the specified path.
	 *
	 * @param path the path
	 * @return the parent node
	 */
	private DefaultMutableTreeNode getNode(String path) {
		
		DefaultMutableTreeNode parentNode = this.getTreeNodeHash().get(path); 
		if (parentNode==null) {
			parentNode = this.getRootNode();
		}
		return parentNode;
	}
	
	/**
	 * Returns the list of property identifier based on the specified node.
	 *
	 * @param pNode the property node
	 * @return the identifier from node
	 */
	private List<String> getIdentifierFromNode(DefaultMutableTreeNode pNode) {
		
		List<String> idList = new ArrayList<>();
		
		PropertyNodeUserObject pnuo = (PropertyNodeUserObject) pNode.getUserObject();
		if (pnuo.getIdentifier()!=null) {
			// --- Found property entry ---------------
			idList.add(pnuo.getIdentifier());
			
		} else {
			// --- Found parent node ------------------
			for (int i = 0; i < pNode.getChildCount(); i++) {
				idList.addAll(this.getIdentifierFromNode((DefaultMutableTreeNode) pNode.getChildAt(i)));
			}
		}
		return idList;
	}
	
	/**
	 * Returns the list of affected identifier based on the specified display instruction.
	 *
	 * @param displayInstruction the display instruction
	 * @return the identifier from display instruction
	 */
	public List<String> getIdentifierFromDisplayInstruction(String displayInstruction) {

		if (displayInstruction==null) return null;
		
		List<String> idList = new ArrayList<>();
		DefaultMutableTreeNode pNode = this.getNode(displayInstruction);
		if (pNode!=null) {
			List<String> idListNode = this.getIdentifierFromNode(pNode);
			if (idListNode!=null && idListNode.size()>0) {
				idList.addAll(idListNode);
			}
		}
		return idList;
	}
	
	
	
	/**
	 * Returns the current properties tree as list
	 * @return the list of tree nodes
	 */
	public List<DefaultMutableTreeNode> asList() {
		List<DefaultMutableTreeNode> nodeModelList = new ArrayList<>();
		DefaultMutableTreeNode rooNode = this.getRootNode();
		this.asListRecursion(rooNode, nodeModelList);
		return nodeModelList;
	}
	/**
	 * This is the recursive call for {@link #asList()}.
	 *
	 * @param parentNode the parent node
	 * @param nodeModelList the node model list
	 */
	private void asListRecursion(DefaultMutableTreeNode parentNode, List<DefaultMutableTreeNode> nodeModelList) {
		for (int i = 0; i < parentNode.getChildCount(); i++) {
			DefaultMutableTreeNode currNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
			nodeModelList.add(currNode) ;
			this.asListRecursion(currNode, nodeModelList);
		}
	}
	
	
	
	/**
	 * The Class PropertyNodeUserObject serves as user object for the current {@link PropertiesTree}.
	 *
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	public class PropertyNodeUserObject {
		
		private String identifier;
		private PropertyValue propertyValue;
		private String displayInstruction;
		
		/**
		 * Instantiates a new property node user object.
		 *
		 * @param identifier the identifier
		 * @param propertyValue the property value
		 * @param displayInstruction the display instruction
		 */
		public PropertyNodeUserObject(String identifier, PropertyValue propertyValue, String displayInstruction) {
			super();
			this.setIdentifier(identifier);
			this.setPropertyValue(propertyValue);
			this.setDisplayInstruction(displayInstruction);
		}
		public String getIdentifier() {
			return identifier;
		}
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}
		public PropertyValue getPropertyValue() {
			return propertyValue;
		}
		public void setPropertyValue(PropertyValue propertyValue) {
			this.propertyValue = propertyValue;
		}
		public String getDisplayInstruction() {
			return displayInstruction;
		}
		public void setDisplayInstruction(String displayInstruction) {
			this.displayInstruction = displayInstruction;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			
			String diArray[] = this.getDisplayInstruction().split(";");

			String displayText = diArray[0];
			if (this.getPropertyValue()!=null) {
				displayText += ": " + this.getPropertyValue().getValueClass() + "=" + this.getPropertyValue().getValueString();
				displayText += " [" + this.getIdentifier() + "]";
			}
			return displayText;
		}
	}
	
}
