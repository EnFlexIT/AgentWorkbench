package com.missing.inspect;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;

import com.missing.Str;
/**
 * Inspector serves as a means to quickly inspect Java Objects.
 * Inspection is done recursively. There is a protection against loops.
 * @author Claus-Christoph Küthe
 */
public class Inspector {
	private Vector<Object> traversed = new Vector<Object>();
	private Vector<InspectorRenderer> renderer = new Vector<InspectorRenderer>();
	public Inspector() {
	}
	/**
	 * Adds a renderer for non-primitive classes, to shorten output
	 * @param renderer
	 */
	public void addRenderer(InspectorRenderer renderer) {
		this.renderer.add(renderer);
	}
	
	private String indent(int amount)  {
	return Str.repeat(" ", amount);
	}
	
	/**
	 * Inspects object, direct printout
	 * @param object
	 */
	public static void inspect(Object object) {
		Inspector ins = new Inspector();
	System.out.println(ins.getInspect(object, 0));
	}
	
	/**
	 * Inspects object, but returns output as string
	 * @param object
	 * @return String
	 */
	public static String getInspect(Object object) {
		Inspector ins = new Inspector();
	return ins.getInspect(object, 0);
	}
	
	public void inspect(Object object, int ind) {
		System.out.println(this.getInspect(object, ind));
	}
	
	private String getRendered(Object object, int ind) {
		for(InspectorRenderer renderer : this.renderer) {
			if(renderer.fitRenderer(object)) {
				return renderer.render(object);
			}
		}
	return null;
	}
	
	private boolean hasRenderer(Object object) {
		for(InspectorRenderer renderer : this.renderer) {
			if(renderer.fitRenderer(object)) {
				return true;
			}
		}
	return false;
	}
	
	
	public String getInspect(Object object, int ind) {
		if(object==null) {
			return "(null)";
		}
		if(object.getClass().isPrimitive() || this.isPrimitive(object)) {
			return this.inspectPrimitive(object, 0);
		}
		if(this.hasRenderer(object)) {
			return this.getRendered(object, ind);
		}
		
		if(this.knowsObject(object)) {
			return "*RECURSION*";
		} else {
			this.traversed.add(object);
		}
		
		if(object instanceof Map<?,?>) {
			return this.inspectMap((Map<?, ?>) object, ind);
		}
		
		if(object instanceof Collection<?>) {
			return this.inspectCollection((Collection<?>)object, ind);
		}
		
		if(PrimitiveArrayRenderer.isPrimitiveArray(object)) {
			return PrimitiveArrayRenderer.render(object, ind);
		}
	
		if(object.getClass().isArray()) {
			return this.inspectArray((Object[]) object, ind);
		}
		
		
		
		
		//result += "<can't handle "+object.getClass().getCanonicalName()+">";
	return this.inspectObject(object, ind);
	}
	
	private boolean knowsObject(Object object) {
		for(Object value : this.traversed) {
			if(value==object) {
				return true;
			}
		}
		return false;
	}
	
	private String inspectArray(Object[] object, int ind) {
		String result = "";
		String header;
		String assign;
		if(this.isPrimitiveWrapperArray(object)) {
			header = object.getClass().getSimpleName()+" {";
		} else {
			header = object.getClass().getCanonicalName()+" {";	
		}
		result += header;
		result += "\n";
		for(int i = 0; i<object.length;i++) {
			assign = "["+i+"] => ";
			result += this.indent(header.length()-4+ind);
			result += assign;
			result += this.getInspect(object[i], header.length()-4+assign.length()+ind);
			result += "\n";
			
		}
		result += this.indent(ind)+"}";
	return result;
	}



	
	
	
	private String inspectMap(Map<?,?> map, int ind) {
		String result = "";
		String header;
		String assign;
		header = map.getClass().getCanonicalName()+" {";
		result += header;
		result += "\n";
		Object[] keyset = map.keySet().toArray();
		String keydet;
		for(int i = 0; i<map.size();i++) {
			if(this.hasRenderer(keyset[i])) {
				keydet = this.getRendered(keyset[i], ind);
			} else {
				keydet = (String)keyset[i];
			}
			assign = "["+keydet+"] => ";
			result += this.indent(header.length()-4+ind);
			result += assign;
			result += this.getInspect(map.get(keyset[i]), header.length()-4+assign.length()+ind);
			result += "\n";
			
		}
		result += this.indent(ind)+"}";
	return result;
	}

	private String inspectCollection(Collection<?> collection, int ind) {
		String result = "";
		String header;
		String assign;
		header = collection.getClass().getCanonicalName()+" {";
		result += header;
		result += "\n";
		int i=0;
		for(Object object : collection) {
			assign = "["+i+"] => ";
			result += this.indent(header.length()-4+ind);
			result += assign;
			result += this.getInspect(object, header.length()-4+assign.length()+ind);
			result += "\n";
			i++;
		}
		result += this.indent(ind)+"}";
	return result;
	}

	
	private String inspectObject(Object object, int ind) {
		String result = "";
		String header;
		String assign;
		header = object.getClass().getCanonicalName()+" {";	
		result += header;
		result += "\n";
		Field[] fields = object.getClass().getDeclaredFields();
		Object content;
		for(int i = 0; i<fields.length;i++) {
			try {
				if(fields[i].isAccessible()) {
					content = fields[i].get(object);
				} else {
					fields[i].setAccessible(true);
					content = fields[i].get(object);
					fields[i].setAccessible(false);
				}
			} catch (Exception e) {
				content = "<inaccessible>";
			}

			assign = "["+fields[i].getName()+"] => ";
			result += this.indent(header.length()+ind);
			result += assign;
			result += this.getInspect(content, header.length()+assign.length()+ind);
			result += "\n";
		}
		result += this.indent(ind)+"}";
	return result;

	}
	
	private boolean isPrimitive(Object object) {
		if(object instanceof Number) {
			return true;
		}
		if(object instanceof Boolean) {
			return true;
		}
		if(object instanceof Character) {
			return true;
		}
		if(object instanceof String) {
			return true;
		}

	return false;
	}
	
	private boolean isPrimitiveWrapperArray(Object[] object) {
		if(object instanceof Number[]) {
			return true;
		}
		if(object instanceof Boolean[]) {
			return true;
		}
		if(object instanceof Character[]) {
			return true;
		}
		if(object instanceof String[]) {
			return true;
		}

	return false;
	}
	

	
	private String inspectPrimitive(Object object, int ind) {
		String result = "";
		result += "("+object.getClass().getSimpleName()+")"+object.toString();
	return result; 
	}
}
