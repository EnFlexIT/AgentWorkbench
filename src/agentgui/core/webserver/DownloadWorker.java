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
package agentgui.core.webserver;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class will be used within the {@link DownloadServer} as handler for
 * HTTP-requests.
 *  
 * @see DownloadServer
 */
public class DownloadWorker extends DownloadServer implements HttpConstants, Runnable {

	final static int BUF_SIZE = 2048;
    static final byte[] EOL = {(byte)'\r', (byte)'\n' };
    
    byte[] buf;					/* buffer to use for requests */
    private Socket s;			/* Socket to client we're handling */
    
    private DownloadServer server = null;
    private boolean stopWorker = false;
    
    /**
     * Instantiates a new download worker.
     *
     * @param webServer the web server
     */
    public DownloadWorker(DownloadServer webServer) {
    	this.server = webServer;
        this.buf = new byte[BUF_SIZE];
        this.s = null;
    }

    /**
     * Sets the socket.
     *
     * @param s the new socket
     */
    synchronized void setSocket(Socket s) {
        this.s = s;
        notify();
    }

	/**
	 * Stop execution.
	 */
	public void stopExecution() {
		this.stopWorker = true;
	}

    /* (non-Javadoc)
     * @see agentgui.core.webserver.DownloadServer#run()
     */
    public synchronized void run() {
        while(true) {
            if (s == null) {
                /* nothing to do */
                try {
                    wait();
                } catch (InterruptedException e) {
                    /* should not happen */
                    continue;
                }
            }
            if (stopWorker==true) {
            	break;
            }
            try {
                handleClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
            /* go back in wait queue if there's fewer
             * than numHandler connections.
             */
            s = null;
            Vector<DownloadWorker> pool = server.getThreads();
            synchronized (pool) {
                if (pool.size() >= server.getWorkers()) {
                    /* too many threads, exit this one */
                    return;
                } else {
                    pool.addElement(this);
                }
            }
        }
    }

    /**
     * Handle client.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void handleClient() throws IOException {
        
    	InputStream is = new BufferedInputStream(s.getInputStream());
        PrintStream ps = new PrintStream(s.getOutputStream());
        /* we will only block in read for this many milliseconds
         * before we fail with java.io.InterruptedIOException,
         * at which point we will abandon the connection.
         */
        s.setSoTimeout(server.getTimeout());
        s.setTcpNoDelay(true);
        /* zero out the buffer from last time */
        for (int i = 0; i < BUF_SIZE; i++) {
            buf[i] = 0;
        }
        try {
            /* We only support HTTP GET/HEAD, and don't
             * support any fancy HTTP options,
             * so we're only interested really in
             * the first line.
             */
            int nread = 0, r = 0;

outerloop:
            while (nread < BUF_SIZE) {
                r = is.read(buf, nread, BUF_SIZE - nread);
                if (r == -1) {
                    /* EOF */
                    return;
                }
                int i = nread;
                nread += r;
                for (; i < nread; i++) {
                    if (buf[i] == (byte)'\n' || buf[i] == (byte)'\r') {
                        /* read one line */
                        break outerloop;
                    }
                }
            }

            /* are we doing a GET or just a HEAD */
            boolean doingGet;
            /* beginning of file name */
            int index;
            if (buf[0] == (byte)'G' &&
                buf[1] == (byte)'E' &&
                buf[2] == (byte)'T' &&
                buf[3] == (byte)' ') {
                doingGet = true;
                index = 4;
            } else if (buf[0] == (byte)'H' &&
                       buf[1] == (byte)'E' &&
                       buf[2] == (byte)'A' &&
                       buf[3] == (byte)'D' &&
                       buf[4] == (byte)' ') {
                doingGet = false;
                index = 5;
            } else {
                /* we don't support this method */
                ps.print("HTTP/1.0 " + HTTP_BAD_METHOD + " unsupported method type: ");
                ps.write(buf, 0, 5);
                ps.write(EOL);
                ps.flush();
                s.close();
                return;
            }

            int i = 0;
            /* find the file name, from:
             * GET /foo/bar.html HTTP/1.0
             * extract "/foo/bar.html"
             */
            for (i = index; i < nread; i++) {
                if (buf[i] == (byte)' ') {
                    break;
                }
            }      

            //String fname = (new String(buf, 0, index, i-index)).replace('/', File.separatorChar);
            String fname = new String(buf);
            fname = fname.substring(index, i).trim().replace('/', File.separatorChar);
            if (fname.startsWith(File.separator)) {
                fname = fname.substring(1);
            }
            File targ = new File(server.getRoot(), fname);
            if (targ.isDirectory()) {
                File ind = new File(targ, "index.html");
                if (ind.exists()) {
                    targ = ind;
                }
            }
            boolean OK = printHeaders(targ, ps);
            if (doingGet) {
                if (OK) {
                    sendFile(targ, ps);
                } else {
                    send404(targ, ps);
                }
            }
        } finally {
            s.close();
        }
    }

    /**
     * Prints the headers.
     *
     * @param targ the targ
     * @param ps the ps
     * @return true, if successful
     * @throws IOException Signals that an I/O exception has occurred.
     */
    boolean printHeaders(File targ, PrintStream ps) throws IOException {
        
    	boolean ret = false;
        @SuppressWarnings("unused")
		int rCode = 0;
        
        if (!targ.exists()) {
            rCode = HTTP_NOT_FOUND;
            ps.print("HTTP/1.0 " + HTTP_NOT_FOUND + " not found");
            ps.write(EOL);
            ret = false;
        }  else {
            rCode = HTTP_OK;
            ps.print("HTTP/1.0 " + HTTP_OK+" OK");
            ps.write(EOL);
            ret = true;
        }
        //System.out.println("From " +s.getInetAddress().getHostAddress()+": GET " + targ.getAbsolutePath()+"-->"+rCode);
        
        ps.print("Server: Simple java");
        ps.write(EOL);
        ps.print("Date: " + (new Date()));
        ps.write(EOL);
        if (ret) {
            if (!targ.isDirectory()) {
                ps.print("Content-length: "+targ.length());
                ps.write(EOL);
                ps.print("Last Modified: " + (new Date(targ.lastModified())));
                ps.write(EOL);
                String name = targ.getName();
                int ind = name.lastIndexOf('.');
                String ct = null;
                if (ind > 0) {
                    ct = (String) map.get(name.substring(ind));
                }
                if (ct == null) {
                    ct = "unknown/unknown";
                }
                ps.print("Content-type: " + ct);
                ps.write(EOL);
            } else {
                ps.print("Content-type: text/html");
                ps.write(EOL);
            }
        }
        return ret;
    }

    /**
     * Send404.
     *
     * @param targ the targ
     * @param ps the ps
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void send404(File targ, PrintStream ps) throws IOException {
        ps.write(EOL);
        ps.write(EOL);
        ps.println("Not Found\n\n" + "The requested resource was not found.\n");
    }

    /**
     * Send file.
     *
     * @param targ the targ
     * @param ps the ps
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void sendFile(File targ, PrintStream ps) throws IOException {
        InputStream is = null;
        ps.write(EOL);
        if (targ.isDirectory()) {
            listDirectory(targ, ps);
            return;
        } else {
            is = new FileInputStream(targ.getAbsolutePath());
        }

        try {
            int n;
            while ((n = is.read(buf)) > 0) {
                ps.write(buf, 0, n);
            }
        } finally {
            is.close();
        }
    }

    /* mapping of file extensions to content-types */
    /** The map. */
    static java.util.Hashtable<String, String> map = new java.util.Hashtable<String, String>();

    static {
        fillMap();
    }
    
    /**
     * Sets the suffix.
     *
     * @param k the k
     * @param v the v
     */
    static void setSuffix(String k, String v) {
        map.put(k, v);
    }

    /**
     * Fill map.
     */
    static void fillMap() {
        setSuffix("", "content/unknown");
        setSuffix(".uu", "application/octet-stream");
        setSuffix(".exe", "application/octet-stream");
        setSuffix(".ps", "application/postscript");
        setSuffix(".zip", "application/zip");
        setSuffix(".sh", "application/x-shar");
        setSuffix(".tar", "application/x-tar");
        setSuffix(".snd", "audio/basic");
        setSuffix(".au", "audio/basic");
        setSuffix(".wav", "audio/x-wav");
        setSuffix(".gif", "image/gif");
        setSuffix(".jpg", "image/jpeg");
        setSuffix(".jpeg", "image/jpeg");
        setSuffix(".htm", "text/html");
        setSuffix(".html", "text/html");
        setSuffix(".text", "text/plain");
        setSuffix(".c", "text/plain");
        setSuffix(".cc", "text/plain");
        setSuffix(".c++", "text/plain");
        setSuffix(".h", "text/plain");
        setSuffix(".pl", "text/plain");
        setSuffix(".txt", "text/plain");
        setSuffix(".java", "text/plain");
    }

    /**
     * List directory.
     *
     * @param dir the dir
     * @param ps the ps
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void listDirectory(File dir, PrintStream ps) throws IOException {
        ps.println("<TITLE>Directory listing</TITLE><P>\n");
        ps.println("<A HREF=\"..\">Parent Directory</A><BR>\n");
        String[] list = dir.list();
        for (int i = 0; list != null && i < list.length; i++) {
            File f = new File(dir, list[i]);
            if (f.isDirectory()) {
                ps.println("<A HREF=\""+list[i]+"/\">"+list[i]+"/</A><BR>");
            } else {
                ps.println("<A HREF=\""+list[i]+"\">"+list[i]+"</A><BR>");
            }
        }
        ps.println("<P><HR><BR><I>" + (new Date()) + "</I>");
    }

}
