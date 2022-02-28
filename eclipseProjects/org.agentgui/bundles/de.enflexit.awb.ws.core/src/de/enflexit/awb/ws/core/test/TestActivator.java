package de.enflexit.awb.ws.core.test;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class TestActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
//		// TODO Auto-generated method stub
//        Server server = new Server(9022);
//        ServletHandler handler = new ServletHandler();
//        server.setHandler(handler);
//
//        handler.addServletWithMapping(ExampleServlet.class, "/topic/*");
//        try {
//        System.out.println("Server startet...");
//        server.start();
//        System.out.println("Server gestartet!");
//        }catch (Exception e) {
//			// TODO: handle exception
//        	System.out.println("Server konnte nicht starten!");
//        	e.printStackTrace();
//		}
//        
//        try {
//        	server.join();
//        	System.out.println("Server joined!");
//            }catch (Exception e) {
//    			// TODO: handle exception
//            	System.out.println("Server konnte nicht Join-Funktion ausführen!");
//            	e.printStackTrace();
//    		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

}
