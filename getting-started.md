# Getting Started

This section shortly describes how you can define, develop and start your first agent-based "application". Even this is a very simple example, it will demonstrate the principle on how you can extend Agent.Workbench with your own agents and user functions. Since the workbench is based on OSGI / Eclipse _bundles_, you have to develope OSGI \_plugins \_for your agent projects too. In the course of the text we mixing-up these terms, but their meaning is the same \(plugin = bundle\).

To create an agent application, the following installation, configuration and development tasks have to be done:

* [Install Agent.Workbench](/01_getting-started/install-agentworkbench.md): Go to our [GitHub-Site](https://github.com/EnFlexIT/AgentWorkbench/releases) and download the latetst release of Agent.Workbench \(for Windows, Linux or MacOS\).Just extract the provided archive and place it at a desired location.

* [Create an Agent Project](/01_getting-started/create-new-agent-project.md): After installing, start Agent.Workbench. The first start will execute a benchmark that helps us to classify your machine in case of [distributed agent executions](/distributed-application.md). Go to the menu or toolbar and create a ![](/eclipseProjects/org.agentgui/bundles/org.agentgui.core/icons/core/MBnew.png) _**New Project**_. That's it for the moment - simply close Agent.Workbench now.

* [Install the Eclipse IDE](/01_getting-started/install-eclipse-ide.md): We recommend to use the [Eclipse IDE for Java EE Developer](https://www.eclipse.org/downloads/) for your developments.

* [Setup your Workspace](/01_getting-started/define-your-eclipse-target-platform.md): Create a new workspace. This is required, since you need to develop against the bundles of the Agent.Workbench installation.

* [Define a Target Platform](/01_getting-started/define-a-target-platform.md): Create a Target Platform definition that points to the previously installed Agent.Workbench.

* [Create your first agent Plugin-Project](/01_getting-started/create-new-agent-project.md):Create an Eclipse Plugin-Project, your desired package structure and create your first agent class.

* [Export your agent bundle / Plugin](/01_getting-started/export-an-agent-bundle.md) into the workbench's project directory and start Agent.Workbench again. Open the above defined workbench project and have a look at the tabs \[Resources\] and \[Agents\], where you will find your exported bundle and your first agent.

* Define a setup and [start your agent application](/01_getting-started/start-your-first-setup.md).



