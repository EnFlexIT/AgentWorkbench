package gasmas.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AntDistributionMatrix {

	private HashSet<String> componentIDs = new HashSet<String>();
	private ArrayList<HashMap<String, Integer>> dynamicMatrix = new ArrayList<HashMap<String, Integer>>();

	public AntDistributionMatrix(ArrayList<Ant> ants) {
		for (Ant ant : ants) {
			ArrayList<String> path = ant.getPath();
			for (int step = 0; step < path.size(); step++) {
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

	public HashMap<String, ArrayList<Integer>> getDistribution() {
		HashMap<String, ArrayList<Integer>> distribution = new HashMap<String, ArrayList<Integer>>();
		for (String componentID : componentIDs) {
			distribution.put(componentID, getDistributionComponent(componentID));
		}
		return distribution;
	}

	public int size() {
		return dynamicMatrix.size();
	}
}
