package com.missing;

import java.util.List;
import java.util.Locale;
import java.util.Vector;
/**
 * Some string utilities 
 * @author Claus-Christoph Küthe
 */
public class Str {
	public static final int PADLEFT = 1;
	public static final int PADRIGHT = 2;
	/**
	 * split roughly is the same as Java's String.split, without resorting to regex.
	 * I'll sometimes find it cumbersome to take regex syntax into account, especially
	 * in simpler cases where you don't need it.
	 * @param delim Delimiter to split by
	 * @param string subject to be splitted
	 * @return
	 */
	public static List<String> split(String delim, String string) {
		Vector<String> vec = new Vector<String>();
		if(string==null) {
			return vec;
		}

		if(delim==null || delim.equals("")) {
			vec.add(string);
		return vec;
		}
		String substr;
		String remainstr = "";
		int index = string.indexOf(delim);
		while(index!=-1) {
			substr = string.substring(0, index);
			remainstr = string.substring(index+delim.length(), string.length());
			vec.add(substr);
			string = remainstr;
			index = string.indexOf(delim);
			System.out.println(index);
			System.out.println(remainstr);
		}
		if(!remainstr.equals("")) {
			vec.add(remainstr);
		}
	return vec;
	}
	/**
	 * repeats a string n times
	 * @param string String to repeat
	 * @param amount amount to repeat
	 * @return
	 */
	public static String repeat(String string, int amount) {
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i<amount;i++) {
			buffer.append(string);
		}
	return buffer.toString();
	}
	/**
	 * 
	 * @param string String to pad
	 * @param pad String which is used to pad
	 * @param amount length to be padded to
	 * @param type Str.LEFTPAD or Str.RIGHTPAD
	 * @return
	 */
	public static String pad(String string, String pad, int amount, int type) {
		StringBuffer buffer = new StringBuffer();
		int havetopad = amount-string.length();
		if(havetopad<0) {
			return string;
		}
		for(int i = 0; i < havetopad; ) {
			for(int k = 0; k<pad.length();k++) {
				buffer.append(pad.charAt(k));
				i++;
				if(i==havetopad) {
					break;
				}
			}
		}
		if(type==Str.PADLEFT) {
			return buffer+string;
		} else {
			return string+buffer;
		}
	}
	/**
	 * String replace which is far more convenient than String.replace(CharSequence...)
	 * @param string String subject
	 * @param search String to be searched
	 * @param replace String to be replaced
	 * @return
	 */
	public static String replace(String string, String search, String replace) {
		return string.replace(search.subSequence(0, search.length()), replace.subSequence(0, replace.length()));
	}
	/**
	 * Uppercase the first character of a string
	 * @param string String to be ucfirsted
	 * @return
	 */
	public static String ucfirst(String string) {
		String first = string.substring(0, 1).toUpperCase();
		string = first+string.substring(1, string.length());
	return string;
	}
	/**
	 * Uppercase the first character of a string, taking Locale into account
	 * @see String.toUppercase
	 * @param string String to be ucfirsted
	 * @param locale Locale for uppercase transformation
	 * @return
	 */
	public static String ucfirst(String string, Locale locale) {
		String first = string.substring(0, 1).toUpperCase(locale);
		string = first+string.substring(1, string.length());
	return string;
	}

}