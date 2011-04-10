package agentgui.graphEnvironment.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractListModel;

public class HashMapListModel extends AbstractListModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4257320097623950515L;
	
	private HashMap<String, Object> elements;

	@Override
	public Object getElementAt(int arg0) {
		ArrayList<String> keys = new ArrayList<String>(elements.keySet());
		return keys.get(arg0);
	}

	@Override
	public int getSize() {
		return elements.size();
	}

	public void addElement(String key, Object element){
		elements.put(key, element);
	}
	
	public Object getElement(String key){
		return elements.get(key);
	}
	
	public HashMap<String, Object> getElementsHash(){
		return elements;
	}
}
