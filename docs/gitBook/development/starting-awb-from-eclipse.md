# Starting AWB from Eclipse

## Setup the Run Configuration

### Create a Configuration and select the plug-ins

As  a next step we are going to configure the Run configuration. Click the arrow next to the debug icon and click _Debug Configurations..._ .

![](../.gitbook/assets/debugconfig.jpg)

In the explorer, on the left, under Eclipse Application we can already see four default configurations. To create our own custom configuration we need to duplicate one of the existing configurations \(right-click &gt; _Duplicate_\)

![](../.gitbook/assets/debugconfigduplicate2.jpg)

You can name the configuration \(e.g "Agent.Workbench.local"\), then select the Plug-ins tab \(1\).

![](../.gitbook/assets/configuredebugconfig.jpg)

To create our own custom configuration, we need to deselect all selected plug-ins with the _Deselect All_ button \(2\). To avoid any problems, it is important to execute the following steps in the exact order. To avoid any problems, execute the following steps in the exact order.

First we must select the following plug-ins:

* org.agentgui.core 
* org.eclipse.core.runtime 
* org.apache.felix.scr \(Start Level: 2, Auto-Start: true\)
* org.eclipse.equinox.event

Then click _Add Required Plug-ins_ \(3\) and _Validate Plug-ins_ \(4\). A window should show up, saying that no problems were detected.

![](../.gitbook/assets/confignoproblems.jpg)

Now you can launch Agent.Workbench with the launch configuration from the IDE.

### Problems after validating plug-ins

In some cases after validating the plug-ins, a dialogue shows up saying that problems were detected.

![](../.gitbook/assets/xmlgraphicsproblem.jpg)

To solve this problem, we need to add the required bundle:

* org.apache.commons.logging

After you selected the plug-in, click _Add Required Plug-ins_ \(3\), then _Validate Plug-ins_ \(4\). No more problems should be detected.

