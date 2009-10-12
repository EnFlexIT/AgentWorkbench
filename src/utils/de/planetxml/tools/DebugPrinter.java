package de.planetxml.tools;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.beans.Introspector;

/**
 * Diese Klasse erzeugt eine Debugausgabe eines Objektes, Arrays, Liste oder
 * Map ähnlich der print_r oder var_dump Funktion in PHP. Es wird nicht
 * überprüft ob die Struktur rekursiv ist, stattdessen muss die maximale Tiefe
 * angegeben werden bis zu der verzweigt wird.
 *
 * Das Ausgabeformat orientiert sich an der
 * <a href="http://www.json.org/">JavaScript Object Notation</a>.
 *
 * @version $Revision$
 * @author Jörn Horstmann <joern_h@gmx.net>, http://blog.planetxml.de/
 */
public class DebugPrinter {
    private Object  object;
    private int     maxLevel;
    private HashSet visited;

    public DebugPrinter(Object o, int maxLevel) {
        this.object   = o;
        this.maxLevel = maxLevel;
        this.visited  = new HashSet();
    }
    
    public DebugPrinter(Object o) {
        this(o, 3);
    }

    private static String escapeString(String str) {
        return escapeString(str, '"');
    }

    private static String escapeString(String str, char quote) {
        // create a StringBuffer that is a bit bigger than str
        StringBuffer sb = new StringBuffer(str.length() + str.length() / 8 + 2);
        int len = str.length();
        sb.append(quote);
        for (int i=0; i<len; i++) {
            char ch = str.charAt(i);
            if (ch == '\\') {
                sb.append("\\\\");
            }
            else if (ch == '\n') {
                sb.append("\\n");
            }
            else if (ch == '\r') {
                sb.append("\\r");
            }
            else if (ch == '\t') {
                sb.append("\\t");
            }
            else if (ch == '\b') {
                sb.append("\\b");
            }
            else if (ch == '\f') {
                sb.append("\\f");
            }
            else if (ch == quote) {
                sb.append('\\');
                sb.append(quote);
            }
            else if (ch < 16) {
                sb.append("\\u000");
                sb.append(Integer.toHexString(ch));
            }
            else if (ch < 32) {
                sb.append("\\u00");
                sb.append(Integer.toHexString(ch));
            }
            else if (ch < 127) { // 127 is a control character
                sb.append(ch);
            }
            else if (ch < 256) {
                sb.append("\\u00");
                sb.append(Integer.toHexString(ch));
            }
            else if (ch < 4096) {
                sb.append("\\u0");
                sb.append(Integer.toHexString(ch));
            }
            else {
                sb.append("\\u");
                sb.append(Integer.toHexString(ch));
            }
        }
        sb.append(quote);
        return sb.toString();
    }
    
    public void print(PrintWriter out) {
        printProperties(object, out, 0);
        out.flush();
    }
    
    public String dump() {
        StringWriter w = new StringWriter(512);
        print(new PrintWriter(w));
        return w.toString();
    }
    
    private static void indent(PrintWriter pw, int level) {
        for (int i=0; i<level; i++) {
            pw.print("    ");
        }
    }

    private static String getPropertyName(Method m) {
        Class  type     = m.getReturnType();
        String name     = m.getName();
        String property = null;
        if (type != Void.TYPE && type != Class.class && m.getParameterTypes().length == 0 && Modifier.isPublic(m.getModifiers())) {
            if (type == Boolean.TYPE && name.length() > 2 && name.startsWith("is")) {
                property = name.substring(2);
            }
            else if (name.length() > 3 && name.startsWith("get")) {
                property = name.substring(3);
            }

            if (property != null) {
                property = Introspector.decapitalize(property);
            }
        }
        return property;
    }

    private void printProperties(Object o, PrintWriter pw, int level) {
        if (o == null) {
            pw.print("null");
        }
        else if (o instanceof Boolean || o instanceof Byte || o instanceof Short || o instanceof Character || o instanceof Integer || o instanceof Long || o instanceof Float || o instanceof Double) {
            pw.print(o);
        }
        else if (o instanceof String) {
            pw.print(escapeString((String)o));
        }
        else {
            if (level > maxLevel) {
                pw.print("...");
                return;
            }
            else if (o.getClass().isArray()) {
                pw.println('[');
                int len = Array.getLength(o);
                for (int i=0; i<len; i++) {
                    indent(pw, level+1);
                    pw.print(i > 0 ? ", " : "  ");
                    printProperties(Array.get(o, i), pw, level+1);
                    pw.println();
                }
                indent(pw, level);
                pw.print(']');
            }
            else if (o instanceof Collection) {
                pw.println('[');
                Collection col = (Collection)o;
                int c = 0;
                for (Iterator it=col.iterator(); it.hasNext(); ) {
                    indent(pw, level+1);
                    pw.print(c > 0 ? ", " : "  ");
                    printProperties(it.next(), pw, level+1);
                    pw.println();
                    c++;
                }
                indent(pw, level);
                pw.print(']');
            }
            else if (o instanceof Iterator) {
                pw.println('[');
                int c = 0;
                for (Iterator it=(Iterator)o; it.hasNext(); ) {
                    indent(pw, level+1);
                    pw.print(c > 0 ? ", " : "  ");
                    printProperties(it.next(), pw, level+1);
                    pw.println();
                    c++;
                }
                indent(pw, level);
                pw.print(']');
            }
            else if (o instanceof Enumeration) {
                pw.println('[');
                int c = 0;
                for (Enumeration e=(Enumeration)o; e.hasMoreElements(); ) {
                    indent(pw, level+1);
                    pw.print(c > 0 ? ", " : "  ");
                    printProperties(e.nextElement(), pw, level+1);
                    pw.println();
                    c++;
                }
                indent(pw, level);
                pw.print(']');
            }
            else if (o instanceof Map) {
                pw.println('{');
                Map map = (Map)o;
                int c   = 0;
                for (Iterator it=map.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry me = (Map.Entry)it.next();
                    indent(pw, level+1);
                    pw.print(c > 0 ? ", " : "  ");
                    pw.print(escapeString(me.getKey().toString()));
                    pw.print(": ");
                    printProperties(me.getValue(), pw, level+1);
                    pw.println();
                    c++;
                }
                indent(pw, level);
                pw.print('}');
            }
            else {
                Method[] m = o.getClass().getMethods();
                int      c = 0;
                pw.println('{');
                for (int i=0; i<m.length; i++) {
                    String property = getPropertyName(m[i]);
                    if (property != null) {
                        indent(pw, level+1);
                        pw.print(c > 0 ? ", " : "  ");
                        pw.print(escapeString(property));
                        pw.print(": ");
                        try {
                            printProperties(m[i].invoke(o, null), pw, level+1);
                            c++;
                        }
                        catch (Exception e) {
                        }
                        pw.println();
                    }
                }
                indent(pw, level);
                pw.print('}');
            }
        }
    }
}
