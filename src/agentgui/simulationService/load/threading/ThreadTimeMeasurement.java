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
package agentgui.simulationService.load.threading;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Vector;

/**
 * The Class ThreadTimeMeasurement.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadTimeMeasurement extends Thread {
	
	/** The Thread interval. */
	private final long interval;

	
    /**
     *  Create a polling thread to track times. 
     * @param interval the interval
     */
    public ThreadTimeMeasurement( final long interval) {
        super("Thread time monitor");
        this.interval = interval;
        setDaemon(true);
    }
 
    /** 
     * Run the thread until interrupted. 
     */
    public void run( ) {
        
    	while ( !isInterrupted() ) {
        	try { 
            	this.generateThreadProtocol();
        		Thread.sleep(this.interval); 
            } catch ( InterruptedException e ) { 
            	break; 
            }
        }
    }
    

    /**
     * Generate thread protocol.
     */
    private void generateThreadProtocol(){
    	
        final ThreadMXBean tmxb = ManagementFactory.getThreadMXBean();
        final long[] ids = tmxb.getAllThreadIds();
        
        Vector<ThreadTime> threadTimes = new Vector<ThreadTime>();
        String threadName;
        long cpuTime = 0L;
        long userTime = 0L;
        
        if (tmxb.isThreadCpuTimeSupported()==true){
        	if(tmxb.isThreadCpuTimeEnabled()==false){
        		tmxb.setThreadCpuTimeEnabled(true);  
//        		System.out.println("ThreadTimeMeasurement now enabled.");
        	}	
        } else{
        	System.err.println("ThreadTimeMeasurement not supported !!");
        	return;
        }
        
        for (long id : ids) {
            
            cpuTime = tmxb.getThreadCpuTime(id);
            userTime = tmxb.getThreadUserTime(id);
            
            if ( cpuTime == -1 || userTime == -1 )
                continue;   // Thread died

        	threadName = tmxb.getThreadInfo(id).getThreadName();
        	threadTimes.add(new ThreadTime(threadName, cpuTime, userTime));
        }
        
		ThreadProtocol threadProtocol = new ThreadProtocol("simulationSetup", "containerName", "machineName", System.currentTimeMillis(), threadTimes);
		
        //TODO: übergeben
    }
}
