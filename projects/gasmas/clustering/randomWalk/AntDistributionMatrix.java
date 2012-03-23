package gasmas.clustering.randomWalk;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class AntDistributionMatrix {

	private ArrayList<Ant> ants;
	private HashSet<String> componentIDs = new HashSet<String>();
	private ArrayList<HashMap<String, Integer>> dynamicMatrix = new ArrayList<HashMap<String, Integer>>();

	public AntDistributionMatrix(ArrayList<Ant> ants) {
		this.ants = ants;
	}

	private void createDynamicMatrix(boolean onlyActive, ArrayList<Ant> ants) {
		for (Ant ant : ants) {
			if (!onlyActive || (onlyActive && ant.getActive())) {
				ArrayList<String> path = ant.getPath();
				for (int step = 0; step < path.size(); step++) {
					// System.out.print(path.get(step) + ";");
					if (dynamicMatrix.size() <= step) {
						dynamicMatrix.add(new HashMap<String, Integer>());
					}
					HashMap<String, Integer> map = dynamicMatrix.get(step);
					String componentID = path.get(step);
					if (map.containsKey(componentID)) {
						map.put(componentID, map.get(componentID).intValue() + 1);
					} else {
						componentIDs.add(componentID);
						map.put(componentID, 1);
					}
				}
			}
			// System.out.println();
		}
	}

	private ArrayList<HashMap<String, Integer>> removeSameStart() {
		ArrayList<HashMap<String, Integer>> listofMaps = new ArrayList<HashMap<String, Integer>>(dynamicMatrix);
		for (HashMap<String, Integer> map : dynamicMatrix) {
			if (map.size() == 1) {
				listofMaps.remove(map);
			}
		}
		return listofMaps;
	}

	private ArrayList<Ant> removeCriclesAndCrossings() {
		ArrayList<Ant> listAnts = new ArrayList<Ant>(ants);
		{
			for (Ant ant : ants) {
				if (new HashSet<String>(ant.getPath()).size() != ant.getPath().size()) {
					listAnts.remove(ant);
				}
			}
		}
		return listAnts;
	}

	public void put(HashMap<String, Integer> hashMap) {
		dynamicMatrix.add(hashMap);
	}

	public void set(int step, HashMap<String, Integer> hashMap) {
		if (step < dynamicMatrix.size()) {
			dynamicMatrix.set(step, hashMap);
		}
	}

	public HashMap<String, Integer> get(int step) {
		if (dynamicMatrix.size() > step) {
			return dynamicMatrix.get(step);
		}
		return null;
	}

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

	public void printDistrubutionAfterStep() {
		printMatrix(getDistribution());
	}

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

	public String findFrequentPathComponent() {
		createDynamicMatrix(false, ants);
		ArrayList<HashMap<String, Integer>> dynamicMatrix = removeSameStart();
		HashMap<String, Integer> accumulatedMap = accumulate(dynamicMatrix);
		ArrayList<HashMap<String, Integer>> mapLits = new ArrayList<HashMap<String, Integer>>();
		mapLits.add(accumulatedMap);
		printPathMatrix(mapLits);
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
		return maxEntry.getKey();
	}

	/**
	 * finds the first Entry from the maxEntryList
	 * 
	 * @param maxEntryList
	 * @return
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

	public void printPathMatrix(ArrayList<HashMap<String, Integer>> dynamicMatrix) {
		System.out.println("Ants:" + ants.size());

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

	public HashMap<String, ArrayList<Integer>> getDistribution() {
		createDynamicMatrix(false, ants);
		HashMap<String, ArrayList<Integer>> distribution = new HashMap<String, ArrayList<Integer>>();
		for (String componentID : componentIDs) {
			distribution.put(componentID, getDistributionComponent(componentID));
		}
		return distribution;
	}

	public int size() {
		return dynamicMatrix.size();
	}

	private void printMatrix(HashMap<String, ArrayList<Integer>> distribution) {
		for (Entry<String, ArrayList<Integer>> entry : distribution.entrySet()) {
			System.out.printf("%-4s :", entry.getKey());
			for (Integer integer : entry.getValue()) {
				System.out.printf("%6d", integer);
			}
			System.out.println();
		}
	}
}
