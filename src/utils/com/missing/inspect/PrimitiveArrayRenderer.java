package com.missing.inspect;

import java.util.Vector;

public class PrimitiveArrayRenderer {
	public static String render(Object object, int ind) {
		if(object.getClass().getCanonicalName().equals("byte[]")) {
			return PrimitiveArrayRenderer.renderFinal((byte[])object, ind);
		}
		if(object.getClass().getCanonicalName().equals("char[]")) {
			return PrimitiveArrayRenderer.renderFinal((char[])object, ind);
		}
		if(object.getClass().getCanonicalName().equals("short[]")) {
			return PrimitiveArrayRenderer.renderFinal((short[])object, ind);
		}
		if(object.getClass().getCanonicalName().equals("int[]")) {
			return PrimitiveArrayRenderer.renderFinal((int[])object, ind);
		}
		if(object.getClass().getCanonicalName().equals("long[]")) {
			return PrimitiveArrayRenderer.renderFinal((long[])object, ind);
		}
		if(object.getClass().getCanonicalName().equals("boolean[]")) {
			return PrimitiveArrayRenderer.renderFinal((boolean[])object, ind);
		}
		if(object.getClass().getCanonicalName().equals("float[]")) {
			return PrimitiveArrayRenderer.renderFinal((float[])object, ind);
		}
		if(object.getClass().getCanonicalName().equals("double[]")) {
			return PrimitiveArrayRenderer.renderFinal((double[])object, ind);
		}
	return null;
	}
	
	public static boolean isPrimitiveArray(Object object) {
		if(!object.getClass().isArray()) {
			return false;
		}
		Vector<String> primitives = new Vector<String>();
		primitives.add("byte[]");
		primitives.add("char[]");
		primitives.add("int[]");
		primitives.add("long[]");
		primitives.add("short[]");
		primitives.add("float[]");
		primitives.add("double[]");
		primitives.add("boolean[]");
		if(primitives.contains(object.getClass().getCanonicalName())) {
			return true;
		}
	return false;
	}
	
	private static String renderFinal(byte[] object, int ind) {
		String result = "";
		String header;
		String assign;
		header = object.getClass().getCanonicalName()+" {";	
		result += header;
		result += "\n";
		for(int i = 0; i<object.length;i++) {
			assign = "["+i+"] => ";
			result += PrimitiveArrayRenderer.indent(header.length()-4+ind);
			result += assign;
			result += object[i];
			result += "\n";
		}
		result += PrimitiveArrayRenderer.indent(ind)+"}";
	return result;
	}

	private static String renderFinal(char[] object, int ind) {
		String result = "";
		String header;
		String assign;
		header = object.getClass().getCanonicalName()+" {";	
		result += header;
		result += "\n";
		for(int i = 0; i<object.length;i++) {
			assign = "["+i+"] => ";
			result += PrimitiveArrayRenderer.indent(header.length()-4+ind);
			result += assign;
			result += object[i];
			result += "\n";
		}
		result += PrimitiveArrayRenderer.indent(ind)+"}";
	return result;
	}

	private static String renderFinal(short[] object, int ind) {
		String result = "";
		String header;
		String assign;
		header = object.getClass().getCanonicalName()+" {";	
		result += header;
		result += "\n";
		for(int i = 0; i<object.length;i++) {
			assign = "["+i+"] => ";
			result += PrimitiveArrayRenderer.indent(header.length()-4+ind);
			result += assign;
			result += object[i];
			result += "\n";
		}
		result += PrimitiveArrayRenderer.indent(ind)+"}";
	return result;
	}
	
	private static String renderFinal(int[] object, int ind) {
		String result = "";
		String header;
		String assign;
		header = object.getClass().getCanonicalName()+" {";	
		result += header;
		result += "\n";
		for(int i = 0; i<object.length;i++) {
			assign = "["+i+"] => ";
			result += PrimitiveArrayRenderer.indent(header.length()-4+ind);
			result += assign;
			result += object[i];
			result += "\n";
		}
		result += PrimitiveArrayRenderer.indent(ind)+"}";
	return result;
	}
	
	private static String renderFinal(long[] object, int ind) {
		String result = "";
		String header;
		String assign;
		header = object.getClass().getCanonicalName()+" {";	
		result += header;
		result += "\n";
		for(int i = 0; i<object.length;i++) {
			assign = "["+i+"] => ";
			result += PrimitiveArrayRenderer.indent(header.length()-4+ind);
			result += assign;
			result += object[i];
			result += "\n";
		}
		result += PrimitiveArrayRenderer.indent(ind)+"}";
	return result;
	}

	private static String renderFinal(float[] object, int ind) {
		String result = "";
		String header;
		String assign;
		header = object.getClass().getCanonicalName()+" {";	
		result += header;
		result += "\n";
		for(int i = 0; i<object.length;i++) {
			assign = "["+i+"] => ";
			result += PrimitiveArrayRenderer.indent(header.length()-4+ind);
			result += assign;
			result += object[i];
			result += "\n";
		}
		result += PrimitiveArrayRenderer.indent(ind)+"}";
	return result;
	}

	private static String renderFinal(double[] object, int ind) {
		String result = "";
		String header;
		String assign;
		header = object.getClass().getCanonicalName()+" {";	
		result += header;
		result += "\n";
		for(int i = 0; i<object.length;i++) {
			assign = "["+i+"] => ";
			result += PrimitiveArrayRenderer.indent(header.length()-4+ind);
			result += assign;
			result += object[i];
			result += "\n";
		}
		result += PrimitiveArrayRenderer.indent(ind)+"}";
	return result;
	}

	private static String renderFinal(boolean[] object, int ind) {
		String result = "";
		String header;
		String assign;
		header = object.getClass().getCanonicalName()+" {";	
		result += header;
		result += "\n";
		for(int i = 0; i<object.length;i++) {
			assign = "["+i+"] => ";
			result += PrimitiveArrayRenderer.indent(header.length()-4+ind);
			result += assign;
			result += object[i];
			result += "\n";
		}
		result += PrimitiveArrayRenderer.indent(ind)+"}";
	return result;
	}


	private static String indent(int amount)  {
		String result = "";
		for(int i=0;i<amount;i++) {
			result += " ";
		}
	return result;
	}
}
