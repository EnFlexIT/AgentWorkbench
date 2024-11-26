package org.agentgui.lib.jade;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.junit.jupiter.api.Test;

public class MyFirstTestClass {

	
	@Test
	public void executeTest() throws InterruptedException {
		
		Bundle bundle = FrameworkUtil.getBundle(Test.class);
		assertNotNull(bundle, "Not running inside an OSGi framework!");

		BundleContext bundleContext = bundle.getBundleContext();
		assertNotNull(bundleContext, "The test bundle is not started!");
		
	}
	
}
