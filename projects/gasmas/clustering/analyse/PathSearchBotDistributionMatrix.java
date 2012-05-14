/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package gasmas.clustering.analyse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

/**
 * The Class AntDistributionMatrix.
 */
public class PathSearchBotDistributionMatrix {

	private static final int SKIP_STEPS = 2;

	/** The component i ds. */
	private HashSet<String> componentIDs = new HashSet<String>();

	/** The dynamic matrix. */
	private ArrayList<HashMap<String, Integer>> dynamicMatrix = new ArrayList<HashMap<String, Integer>>();

	/**
	 * Adds the ant to dynamic matrix.
	 *
	 * @param bot the ant
	 */
	public void addAntToDynamicMatrix(PathSearchBot bot) {
		ArrayList<String> path = bot.getPath();
		for (int step = SKIP_STEPS; step < path.size(); step++) {
			// System.out.print(path.get(step) + ";");
			if (dynamicMatrix.size() <= step) {
				dynamicMatrix.add(new HashMap<String, Integer>());
			}
			HashMap<String, Integer> map = dynamicMatrix.get(step - SKIP_STEPS);
			String componentID = path.get(step);
			if (map.containsKey(componentID)) {
				map.put(componentID, map.get(componentID).intValue() + 1);
			} else {
				componentIDs.add(componentID);
				map.put(componentID, 1);
			}
		}
	}

	/**
	 * Removes the same start.
	 *
	 * @return the array list
	 */
	private ArrayList<HashMap<String, Integer>> removeSameStart() {
		ArrayList<HashMap<String, Integer>> listofMaps = new ArrayList<HashMap<String, Integer>>(dynamicMatrix);
		for (HashMap<String, Integer> map : dynamicMatrix) {
			if (map.size() == 1) {
				listofMaps.remove(map);
			}
		}
		return listofMaps;
	}

	/**
	 * Gets the distribution component.
	 *
	 * @param componentID the component id
	 * @return the distribution component
	 */
	public ArrayList<Integer> getDistributionComponent(String componentID) {
		ArrayList<Integer> distribution = new ArrayList<Integer>();
		for (HashMap<String, Integer> hashMap : dynamicMatrix) {
			if (hashMap.containsKey(componentID)) {
				distribution.add(hashMap.get(componentID));
			} else {
				distribution.add(0);
			}
		}
		return distribution;
	}

	/**
	 * Accumulate.
	 *
	 * @param dynamicMatrix the dynamic matrix
	 * @return the hash map
	 */
	private HashMap<String, Integer> accumulate(ArrayList<HashMap<String, Integer>> dynamicMatrix) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (HashMap<String, Integer> stepMap : dynamicMatrix) {
			for (Entry<String, Integer> entry : stepMap.entrySet()) {
				if (map.containsKey(entry.getKey())) {
					map.put(entry.getKey(), map.get(entry.getKey()) + entry.getValue());
				} else {
					map.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return map;
	}

	/**
	 * Find frequent path component.
	 *
	 * @return the string
	 */
	public String findFrequentPathComponent() {
		HashMap<String, Integer> accumulatedMap = accumulate(dynamicMatrix);
		Entry<String, Integer> maxEntry = null;
		ArrayList<Entry<String, Integer>> maxEntryList = new ArrayList<Entry<String, Integer>>();
		for (Entry<String, Integer> entry : accumulatedMap.entrySet()) {
			if (maxEntry == null) {
				maxEntry = entry;
				maxEntryList.add(maxEntry);
			} else if (maxEntry.getValue().intValue() < entry.getValue().intValue()) {
				maxEntry = entry;
				maxEntryList = new ArrayList<Entry<String, Integer>>();
				maxEntryList.add(maxEntry);
			} else if (maxEntry.getValue().intValue() == entry.getValue().intValue()) {
				maxEntryList.add(entry);
			}
		}
		if (maxEntryList.size() > 1) {
			return getMaxEntry(maxEntryList);
		}
		if (maxEntry == null) {
			return null;
		}
		return maxEntry.getKey();
	}

	/**
	 * finds the first Entry from the maxEntryList.
	 *
	 * @param maxEntryList the max entry list
	 * @return the max entry
	 */
	private String getMaxEntry(ArrayList<Entry<String, Integer>> maxEntryList) {
		for (HashMap<String, Integer> map : dynamicMatrix) {
			for (Entry<String, Integer> maxEntry : maxEntryList) {
				if (map.containsKey(maxEntry.getKey())) {
					return maxEntry.getKey();
				}
			}
		}
		return null;
	}

	/**
	 * Prints the path matrix.
	 *
	 * @param dynamicMatrix the dynamic matrix
	 */
	public void printPathMatrix(ArrayList<HashMap<String, Integer>> dynamicMatrix) {
		System.out.print("   :");
		for (String componentID : componentIDs) {
			System.out.printf("%-5s", componentID);
		}
		System.out.println();
		for (int i = 0; i < dynamicMatrix.size(); i++) {
			System.out.printf("%2d :", i);
			for (String componentID : componentIDs) {
				if (dynamicMatrix.get(i).containsKey(componentID)) {
					System.out.printf("%-5s", dynamicMatrix.get(i).get(componentID));
				} else {
					System.out.printf("%-5s", "-");
				}
			}
			System.out.println();
		}
	}

	/**
	 * Gets the distribution.
	 *
	 * @return the distribution
	 */
	public HashMap<String, ArrayList<Integer>> getDistribution() {
		HashMap<String, ArrayList<Integer>> distribution = new HashMap<String, ArrayList<Integer>>();
		for (String componentID : componentIDs) {
			distribution.put(componentID, getDistributionComponent(componentID));
		}
		return distribution;
	}
}
