package com.missing;

import java.util.List;
import java.util.Map;

public class Join {
	/**
	 * @param separator
	 * @param array
	 * @return
	 */
	public static String implode(String separator, String[] array) {
		String string = "";
		int i = 0;
		for(i=0;i<array.length;i++) {
			if(array[i]!=null) {
				string += array[i];	
			}
			if(i<array.length-1) {
				string += separator;	
			}
		}
	return string;
	}
	
	public static String implode(String separator, List<String> list) {
		String string = "";
		int i = 0;
		for(String value : list) {
			if(list.get(i)!=null) {
				string += list.get(i);	
			}
			if(i<list.size()-1) {
				string += separator;	
			}
		i++;
		}
	
	return string;
	}
	
	public static String implode(String inner, String outer, Map<String, String> map) {
		String string = "";
		int i = 0;
		for(String key : map.keySet()) {
			if(map.get(key)==null) {
				continue;
			}
			if(i!=0) {
				string += outer;
			}
			string += key+inner+map.get(key);
		i++;
		}
	return string;
	}

	public static String implodeEnclosing(String inner, String outer, Map<String, String> map) {
		return Join.implode(inner, outer, map)+outer;
	}

}
