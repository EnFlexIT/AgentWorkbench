# Problem Description
I have a problem running the product "de.enflexit.awb.core.product" in this Eclipse project locally on macOS Tahoe 26.5.2.

# Console output
Unfortunately, the program is running, but nothing seems to happen (stuck) and there is no Exception at all in the application's log, just:
```
WARNING: Using incubator modules: jdk.incubator.vector
Agent.Workbench-Desktop 3.0.0 (qualifier) on Java 21.0.11 of Eclipse Adoptium
```

# Assumption
Additionally, I see a Splash-Screen is popped up, but just in the background, when taking a look to macOS' "Mission Control" (shows all open windows). 
So, it seems to be a (known?) issue with Java's Swing UI on macOS?


# Run Config 
The following run config were used, and it's not part of the codebase: 
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<launchConfiguration type="org.eclipse.pde.ui.RuntimeWorkbench">
    <booleanAttribute key="append.args" value="true"/>
    <stringAttribute key="application" value="de.enflexit.awb.desktop.swt.application"/>
    <booleanAttribute key="askclear" value="true"/>
    <booleanAttribute key="automaticAdd" value="true"/>
    <booleanAttribute key="automaticValidate" value="true"/>
    <stringAttribute key="bootstrap" value=""/>
    <stringAttribute key="checked" value="[NONE]"/>
    <booleanAttribute key="clearConfig" value="true"/>
    <booleanAttribute key="clearws" value="false"/>
    <booleanAttribute key="clearwslog" value="false"/>
    <stringAttribute key="configLocation" value="${workspace_loc}/.metadata/.plugins/org.eclipse.pde.core/AWB-Base (158 plugins)"/>
    <booleanAttribute key="default" value="false"/>
    <setAttribute key="deselected_workspace_bundles">
        <setEntry value="de.enflexit.awb.desktop.swt"/>
        <setEntry value="de.enflexit.awb.help"/>
        <setEntry value="de.enflexit.awb.remoteControl"/>
        <setEntry value="de.enflexit.awb.samples.Examples"/>
        <setEntry value="de.enflexit.awb.samples.GameOfLife"/>
        <setEntry value="de.enflexit.awb.samples.PlugIn"/>
        <setEntry value="de.enflexit.awb.samples.SimService"/>
        <setEntry value="de.enflexit.awb.samples.db"/>
        <setEntry value="de.enflexit.awb.samples.perspective"/>
        <setEntry value="de.enflexit.awb.samples.ws.api"/>
        <setEntry value="de.enflexit.awb.samples.ws.restapi.client"/>
        <setEntry value="de.enflexit.awb.samples.ws.restapi.server"/>
        <setEntry value="de.enflexit.awb.timeSeriesDataProvider"/>
        <setEntry value="de.enflexit.awb.ws.client"/>
        <setEntry value="de.enflexit.awb.ws.core"/>
        <setEntry value="de.enflexit.awb.ws.core.db"/>
        <setEntry value="de.enflexit.awb.ws.core.ui"/>
        <setEntry value="de.enflexit.awb.ws.dynSiteApi"/>
        <setEntry value="de.enflexit.awb.ws.restapi"/>
        <setEntry value="de.enflexit.awb.ws.swagger2x"/>
        <setEntry value="org.awb.env.maps"/>
        <setEntry value="org.awb.env.networkModel"/>
    </setAttribute>
    <booleanAttribute key="includeOptional" value="true"/>
    <stringAttribute key="location" value="${workspace_loc}/runtime-AWB-Base"/>
    <booleanAttribute key="org.eclipse.debug.core.ATTR_FORCE_SYSTEM_CONSOLE_ENCODING" value="false"/>
    <booleanAttribute key="org.eclipse.jdt.launching.ATTR_ATTR_USE_ARGFILE" value="false"/>
    <booleanAttribute key="org.eclipse.jdt.launching.ATTR_SHOW_CODEDETAILS_IN_EXCEPTION_MESSAGES" value="true"/>
    <booleanAttribute key="org.eclipse.jdt.launching.ATTR_USE_START_ON_FIRST_THREAD" value="true"/>
    <stringAttribute key="org.eclipse.jdt.launching.JRE_CONTAINER" value="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-21"/>
    <stringAttribute key="org.eclipse.jdt.launching.PROGRAM_ARGUMENTS" value="-os ${target.os} -ws ${target.ws} -arch ${target.arch} -nl ${target.nl} -consoleLog"/>
    <stringAttribute key="org.eclipse.jdt.launching.SOURCE_PATH_PROVIDER" value="org.eclipse.pde.ui.workbenchClasspathProvider"/>
    <stringAttribute key="org.eclipse.jdt.launching.VM_ARGUMENTS" value="-Dorg.eclipse.swt.graphics.Resource.reportNonDisposed=true -Dsun.awt.disableim=true -Dapple.awt.inputmethod=none -Dapple.awt.swingMenuBar=false --add-opens java.desktop/java.awt=ALL-UNNAMED --add-opens java.desktop/sun.lwawt.macosx=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED -Xms512M -Xmx4G -XstartOnFirstThread"/>
    <stringAttribute key="pde.version" value="3.3"/>
    <stringAttribute key="product" value="de.enflexit.awb.core.product"/>
    <setAttribute key="selected_target_bundles">
        <setEntry value="bcpg@default:default"/>
        <setEntry value="bcpkix@default:default"/>
        <setEntry value="bcprov@default:default"/>
        <setEntry value="bcutil@default:default"/>
        <setEntry value="com.github.weisj.jsvg@default:default"/>
        <setEntry value="com.ibm.icu@default:default"/>
        <setEntry value="com.sun.jna.platform@default:default"/>
        <setEntry value="com.sun.jna@default:default"/>
        <setEntry value="com.sun.xml.bind.jaxb-core@default:default"/>
        <setEntry value="com.sun.xml.bind.jaxb-impl@default:default"/>
        <setEntry value="de.enflexit.jade@default:default"/>
        <setEntry value="de.enflexit.jfreechart@default:default"/>
        <setEntry value="jakarta.activation-api*2.1.4@default:default"/>
        <setEntry value="jakarta.annotation-api*1.3.5@default:default"/>
        <setEntry value="jakarta.annotation-api*2.1.1@default:default"/>
        <setEntry value="jakarta.el-api*5.0.1@default:default"/>
        <setEntry value="jakarta.enterprise.cdi-api@default:default"/>
        <setEntry value="jakarta.enterprise.lang-model@default:default"/>
        <setEntry value="jakarta.inject.jakarta.inject-api@default:default"/>
        <setEntry value="jakarta.interceptor-api@default:default"/>
        <setEntry value="jakarta.persistence-api@default:default"/>
        <setEntry value="jakarta.transaction-api*2.0.1@default:default"/>
        <setEntry value="jakarta.xml.bind-api*4.0.5@default:default"/>
        <setEntry value="org.apache.aries.spifly.dynamic.bundle@default:default"/>
        <setEntry value="org.apache.batik.constants@default:default"/>
        <setEntry value="org.apache.batik.css@default:default"/>
        <setEntry value="org.apache.batik.i18n@default:default"/>
        <setEntry value="org.apache.batik.util@default:default"/>
        <setEntry value="org.apache.commons.commons-codec*1.19.0@default:default"/>
        <setEntry value="org.apache.commons.commons-compress@default:default"/>
        <setEntry value="org.apache.commons.commons-io*2.21.0@default:default"/>
        <setEntry value="org.apache.commons.commons-logging@default:default"/>
        <setEntry value="org.apache.commons.lang3*3.20.0@default:default"/>
        <setEntry value="org.apache.felix.gogo.command@default:default"/>
        <setEntry value="org.apache.felix.gogo.runtime@default:default"/>
        <setEntry value="org.apache.felix.gogo.shell@default:default"/>
        <setEntry value="org.apache.felix.scr@2:true"/>
        <setEntry value="org.apache.xmlgraphics@default:default"/>
        <setEntry value="org.commonmark@default:default"/>
        <setEntry value="org.eclipse.ant.core@default:default"/>
        <setEntry value="org.eclipse.core.commands@default:default"/>
        <setEntry value="org.eclipse.core.contenttype@default:default"/>
        <setEntry value="org.eclipse.core.databinding.observable@default:default"/>
        <setEntry value="org.eclipse.core.databinding.property@default:default"/>
        <setEntry value="org.eclipse.core.databinding@default:default"/>
        <setEntry value="org.eclipse.core.expressions@default:default"/>
        <setEntry value="org.eclipse.core.filesystem.macosx@default:false"/>
        <setEntry value="org.eclipse.core.filesystem@default:default"/>
        <setEntry value="org.eclipse.core.jobs@default:default"/>
        <setEntry value="org.eclipse.core.net@default:default"/>
        <setEntry value="org.eclipse.core.resources@default:default"/>
        <setEntry value="org.eclipse.core.runtime@1:true"/>
        <setEntry value="org.eclipse.core.variables@default:default"/>
        <setEntry value="org.eclipse.e4.core.commands@default:default"/>
        <setEntry value="org.eclipse.e4.core.contexts@default:default"/>
        <setEntry value="org.eclipse.e4.core.di.annotations@default:default"/>
        <setEntry value="org.eclipse.e4.core.di.extensions.supplier@default:default"/>
        <setEntry value="org.eclipse.e4.core.di.extensions@default:default"/>
        <setEntry value="org.eclipse.e4.core.di@default:default"/>
        <setEntry value="org.eclipse.e4.core.services@default:default"/>
        <setEntry value="org.eclipse.e4.emf.xpath@default:default"/>
        <setEntry value="org.eclipse.e4.ui.bindings@default:default"/>
        <setEntry value="org.eclipse.e4.ui.css.core@default:default"/>
        <setEntry value="org.eclipse.e4.ui.css.swt.theme@default:default"/>
        <setEntry value="org.eclipse.e4.ui.css.swt@default:default"/>
        <setEntry value="org.eclipse.e4.ui.di@default:default"/>
        <setEntry value="org.eclipse.e4.ui.dialogs@default:default"/>
        <setEntry value="org.eclipse.e4.ui.ide@default:default"/>
        <setEntry value="org.eclipse.e4.ui.model.workbench@default:default"/>
        <setEntry value="org.eclipse.e4.ui.services@default:default"/>
        <setEntry value="org.eclipse.e4.ui.widgets@default:default"/>
        <setEntry value="org.eclipse.e4.ui.workbench.addons.swt@default:default"/>
        <setEntry value="org.eclipse.e4.ui.workbench.renderers.swt.cocoa@default:false"/>
        <setEntry value="org.eclipse.e4.ui.workbench.renderers.swt@default:default"/>
        <setEntry value="org.eclipse.e4.ui.workbench.swt@default:default"/>
        <setEntry value="org.eclipse.e4.ui.workbench3@default:default"/>
        <setEntry value="org.eclipse.e4.ui.workbench@default:default"/>
        <setEntry value="org.eclipse.ecf.filetransfer@default:default"/>
        <setEntry value="org.eclipse.ecf.identity@default:default"/>
        <setEntry value="org.eclipse.ecf.provider.filetransfer@default:default"/>
        <setEntry value="org.eclipse.ecf@default:default"/>
        <setEntry value="org.eclipse.emf.common@default:default"/>
        <setEntry value="org.eclipse.emf.ecore.change@default:default"/>
        <setEntry value="org.eclipse.emf.ecore.xmi@default:default"/>
        <setEntry value="org.eclipse.emf.ecore@default:default"/>
        <setEntry value="org.eclipse.equinox.app@default:default"/>
        <setEntry value="org.eclipse.equinox.bidi@default:default"/>
        <setEntry value="org.eclipse.equinox.common@2:true"/>
        <setEntry value="org.eclipse.equinox.concurrent@default:default"/>
        <setEntry value="org.eclipse.equinox.event@2:true"/>
        <setEntry value="org.eclipse.equinox.p2.artifact.repository@default:default"/>
        <setEntry value="org.eclipse.equinox.p2.core@default:default"/>
        <setEntry value="org.eclipse.equinox.p2.director@default:default"/>
        <setEntry value="org.eclipse.equinox.p2.engine@default:default"/>
        <setEntry value="org.eclipse.equinox.p2.jarprocessor@default:default"/>
        <setEntry value="org.eclipse.equinox.p2.metadata.repository@default:default"/>
        <setEntry value="org.eclipse.equinox.p2.metadata@default:default"/>
        <setEntry value="org.eclipse.equinox.p2.operations@default:default"/>
        <setEntry value="org.eclipse.equinox.p2.repository@default:default"/>
        <setEntry value="org.eclipse.equinox.p2.transport.ecf@default:default"/>
        <setEntry value="org.eclipse.equinox.preferences@default:default"/>
        <setEntry value="org.eclipse.equinox.region@default:false"/>
        <setEntry value="org.eclipse.equinox.registry@default:default"/>
        <setEntry value="org.eclipse.equinox.security.macosx@default:false"/>
        <setEntry value="org.eclipse.equinox.security@default:default"/>
        <setEntry value="org.eclipse.equinox.simpleconfigurator@1:true"/>
        <setEntry value="org.eclipse.equinox.transforms.hook@default:false"/>
        <setEntry value="org.eclipse.equinox.weaving.hook@default:false"/>
        <setEntry value="org.eclipse.help@default:default"/>
        <setEntry value="org.eclipse.jetty.ee10.osgi.alpn.fragment@default:false"/>
        <setEntry value="org.eclipse.jetty.servlet-api*4.0.9@default:default"/>
        <setEntry value="org.eclipse.jface.databinding@default:default"/>
        <setEntry value="org.eclipse.jface.text@default:default"/>
        <setEntry value="org.eclipse.jface@default:default"/>
        <setEntry value="org.eclipse.orbit.xml-apis-ext@default:default"/>
        <setEntry value="org.eclipse.osgi*3.24.100.v20251215-1416@1:true"/>
        <setEntry value="org.eclipse.osgi.compatibility.state@default:false"/>
        <setEntry value="org.eclipse.swt.cocoa.macosx.aarch64@default:false"/>
        <setEntry value="org.eclipse.svg.svg@default:false"/>
        <setEntry value="org.eclipse.svg@default:false"/>
        <setEntry value="org.eclipse.text@default:default"/>
        <setEntry value="org.eclipse.ui.cocoa@default:false"/>
        <setEntry value="org.eclipse.ui.forms@default:default"/>
        <setEntry value="org.eclipse.ui.ide@default:default"/>
        <setEntry value="org.eclipse.ui.navigator@default:default"/>
        <setEntry value="org.eclipse.ui.views@default:default"/>
        <setEntry value="org.eclipse.ui.workbench@default:default"/>
        <setEntry value="org.eclipse.ui@default:default"/>
        <setEntry value="org.eclipse.urischeme@default:default"/>
        <setEntry value="org.glassfish.corba.glassfish-corba-omgapi@default:false"/>
        <setEntry value="org.objectweb.asm*9.9.1@default:default"/>
        <setEntry value="org.objectweb.asm.commons*9.9.1@default:default"/>
        <setEntry value="org.objectweb.asm.tree*9.9.1@default:default"/>
        <setEntry value="org.objectweb.asm.tree.analysis*9.9.1@default:default"/>
        <setEntry value="org.objectweb.asm.util*9.9.1@default:default"/>
        <setEntry value="org.osgi.service.cm@default:default"/>
        <setEntry value="org.osgi.service.component@default:default"/>
        <setEntry value="org.osgi.service.event@default:default"/>
        <setEntry value="org.osgi.service.metatype@default:default"/>
        <setEntry value="org.osgi.service.prefs@default:default"/>
        <setEntry value="org.osgi.util.function*1.2.0.202109301733@default:default"/>
        <setEntry value="org.osgi.util.promise*1.3.0.202212101352@default:default"/>
        <setEntry value="org.sat4j.core@default:default"/>
        <setEntry value="org.sat4j.pb@default:default"/>
        <setEntry value="org.tukaani.xz@default:default"/>
        <setEntry value="wrapped.es.urjc.etsii.grafo.scimark@default:default"/>
    </setAttribute>
    <setAttribute key="selected_workspace_bundles">
        <setEntry value="de.enflexit.awb.baseOntology@default:default"/>
        <setEntry value="de.enflexit.awb.baseUI@default:default"/>
        <setEntry value="de.enflexit.awb.bgSystem@default:default"/>
        <setEntry value="de.enflexit.awb.core@default:default"/>
        <setEntry value="de.enflexit.awb.desktop@default:default"/>
        <setEntry value="de.enflexit.common.flatlaf@default:default"/>
        <setEntry value="de.enflexit.common@default:default"/>
        <setEntry value="de.enflexit.db.hibernate@default:default"/>
        <setEntry value="de.enflexit.jaxb.impl.binding@default:false"/>
        <setEntry value="de.enflexit.language@default:default"/>
        <setEntry value="de.enflexit.logging@2:true"/>
        <setEntry value="de.enflexit.oshi@default:default"/>
    </setAttribute>
    <booleanAttribute key="show_selected_only" value="false"/>
    <stringAttribute key="templateConfig" value="${target_home}/configuration/config.ini"/>
    <booleanAttribute key="tracing" value="false"/>
    <booleanAttribute key="useCustomFeatures" value="false"/>
    <booleanAttribute key="useDefaultConfig" value="true"/>
    <booleanAttribute key="useDefaultConfigArea" value="true"/>
    <booleanAttribute key="useProduct" value="true"/>
</launchConfiguration>
```

# Additional Infos
This project runs fine on Windows 11 machines, with the same codebase, dependencies and run config.

## Debug
When debugging the application and waiting till it is stuck, and because of my assumption, I suspend the Thread "AWT-EventQueue", it seems to hang at LockSupport.park().
### Suspended thread's stacktrace
```
Thread [AWT-EventQueue-0] (Suspended)	
	Unsafe.park(boolean, long) line: not available [native method]	
	12 collapsed frames
		LockSupport.park() line: 371	
		AbstractQueuedSynchronizer$ConditionNode.block() line: 519	
		ForkJoinPool.unmanagedBlock(ForkJoinPool$ManagedBlocker) line: 3780	
		ForkJoinPool.managedBlock(ForkJoinPool$ManagedBlocker) line: 3725	
		AbstractQueuedSynchronizer$ConditionObject.await() line: 1746	
		EventQueue.getNextEvent() line: 565	
		EventDispatchThread.pumpOneEventForFilters(int) line: 190	
		EventDispatchThread.pumpEventsForFilter(int, Conditional, EventFilter) line: 124	
		EventDispatchThread.pumpEventsForHierarchy(int, Conditional, Component) line: 113	
		EventDispatchThread.pumpEvents(int, Conditional) line: 109	
		EventDispatchThread.pumpEvents(Conditional) line: 101	
		EventDispatchThread.run() line: 90	
```
### Suspended thread's code
java.util.concurrent.locks.LockSupport
```java
    public static void park() {
        if (Thread.currentThread().isVirtual()) {
            VirtualThreads.park();
        } else {
            U.park(false, 0L); // line 371, suspended here
        }
    }
```

# Instructions
Plan out on how to find the root cause and propose potential fixes with the goal to get the app running fine on macOS.
Use a websearch if necessary to gain deeper knowledge about the issue.
