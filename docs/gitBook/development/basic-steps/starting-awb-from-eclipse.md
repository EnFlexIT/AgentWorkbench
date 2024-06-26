---
description: Setup a Debug or Run Configuration to start Agent.Workbench from Eclipse
---

# Starting AWB from Eclipse

### Create a Configuration and select the plug-ins

As a next step we are going to configure the Run configuration. Click the arrow next to the debug icon and click _Debug Configurations..._ .

![](../../.gitbook/assets/debugconfig.jpg)

In the explorer on the left, right-click _Eclipse Application_ and choose _New Configuration_.

![](../../.gitbook/assets/newlaunchconfig2.jpg)

Name the configuration (e.g "Agent.Workbench.local"). In the Main tab, check if _Run a product_ equals _org.agentgui.core.product_ and make sure the option _Clear:_ is unselected.

![](../../.gitbook/assets/newlaunchconfigmain.jpg)

Then open the _Plug-ins tab_ (1). Make sure you select _Launch with: plug-ins selected below only_ (2).

![2](../../.gitbook/assets/newlaunchconfig3.jpg)

To create our own custom configuration, we need to deselect all selected plug-ins with the _Deselect All_ button (3). To avoid any problems, it is important to execute the following steps in the exact order. To avoid any problems, execute the following steps in the exact order.

First we must select the following plug-ins:

* org.eclipse.core.runtime (Start Level 1, Auto-Start: true)
* org.apache.felix.scr (Start Level: 2, Auto-Start: true)
* org.eclipse.equinox.event (Start Level: 2, Auto-Start: true)
* org.agentgui.core

Then click _Add Required Plug-ins_ (4) and _Validate Plug-ins_ (5). A window should show up, saying that no problems were detected.

![](../../.gitbook/assets/confignoproblems.jpg)

As a last step, head to the Configuration tab and make sure the checkbox _Clear the configuration area before launching_ is unselected as well.

![](../../.gitbook/assets/configtabdebugconfiguration.jpg)

Now you can launch Agent.Workbench with the launch configuration from the IDE.

### Adding own plug-ins to the run configuration

In case you already developed own components that you want to launch with Agent.Workbench, adding them to your run configuration is simple. The process is demonstrated by adding a plug-in called _de.agent.test_.

Open your run configuration and go to the _Plug-ins_ tab (1), as shown in the tutorial above. Your own components should appear under _Workspace_. Just select them and apply your changes, or start Agent.Workbench in debug-mode. Your own components are now accessible within the application.

![](<../../.gitbook/assets/addplugintotunconfig (1).jpg>)

### Problems after validating plug-ins

In some cases after validating the plug-ins, a dialogue shows up saying that problems were detected.

![](../../.gitbook/assets/xmlgraphicsproblem.jpg)

To solve this problem, we need to add the required bundle:

* org.apache.commons.logging

After you selected the plug-in, click _Add Required Plug-ins_ (4), then _Validate Plug-ins_ (5). No more problems should be detected.
