package de.enflexit.geography.coordinates;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

/**
 * The Class GeoUtils.
 */
public class GeoUtils {

	protected static double sinSquared(double x) {
	  return Math.sin(x) * Math.sin(x);
	}

	protected static double cosSquared(double x) {
	  return Math.cos(x) * Math.cos(x);
	}

	protected static double tanSquared(double x) {
	  return Math.tan(x) * Math.tan(x);
	}

	protected static double sec(double x) {
	  return 1.0 / Math.cos(x);
	}
	  
	protected static double deg2rad(double x) {
	  return x * (Math.PI / 180);
	}
	  
	protected static double rad2deg(double x) {
	  return x * (180 / Math.PI);
	}
	  
	protected static String chr(Integer x) {
	  String h = Integer.toHexString(x);
	  if (h.length()==1) {
		  h = "0" + h;
	  }
	  h = "%" + h;
	  return unescape(h);
	}
	  
	protected static int ord(String x) {
	  String c = String.valueOf(x.charAt(0));
	  int i;
	  for (i=0; i<256; i++) {
	    String h = Integer.toHexString(i);
	    if (h.length()==1) {
	    	h = "0" + h;
	    }
	    h = "%" + h;
	    h = unescape(h);
	    if (h.equals(c)) break;
	  }
	  return i;
	}
	
	private static String unescape(String stringToUnescape) {
		String unescaped = null;
		try {
			//System.out.println("Unescape " + stringToUnescape);
			unescaped = URLDecoder.decode(stringToUnescape, Charset.defaultCharset().name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return unescaped;
	}
	
}
