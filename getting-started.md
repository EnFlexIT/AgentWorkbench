# Getting Started

This section shortly describes how you can define, develop and start your first agent-based "application". Even this is a very simple example, it will demonstrate the principle on how you can extend Agent.Workbench with your own agents and user functions. Since the workbench is based on OSGI / Eclipse _bundles_, you have to develope OSGI _plugins_ for your agent projects too. In the course of the text we are mixing-up these terms, but their meaning is the same \(plugin = bundle\).

To create an agent application, the following installation, configuration and development tasks have to be done:

* [Install Agent.Workbench](#install-agent-workbench): Go to our [GitHub-Site](https://github.com/EnFlexIT/AgentWorkbench/releases) and download the latetst release of Agent.Workbench \(for Windows, Linux or MacOS\).Just extract the provided archive and place it at a desired location.

* [Create an Agent Project](#create-an-agent-project): After installing, start Agent.Workbench. The first start will execute a benchmark that helps us to classify your machine in case of [distributed agent executions](/distributed-application.md). Go to the menu or toolbar and create a ![](/eclipseProjects/org.agentgui/bundles/org.agentgui.core/icons/core/MBnew.png) _**New Project**_. That's it for the moment - simply close Agent.Workbench now.

* [Install the Eclipse IDE](#install-eclipse-ide): We recommend to use the [Eclipse IDE for Java EE Developer](https://www.eclipse.org/downloads/) for your developments.

* [Setup your Workspace](/01_getting-started/define-your-eclipse-target-platform.md): Create a new workspace. This is required, since you need to develop against the bundles of the Agent.Workbench installation.

* [Define a Target Platform](/01_getting-started/define-a-target-platform.md): Create a Target Platform definition that points to the previously installed Agent.Workbench.

* [Create your first agent Plugin-Project](/01_getting-started/create-new-agent-project.md):Create an Eclipse Plugin-Project, your desired package structure and create your first agent class.

* [Export your agent bundle / Plugin](/01_getting-started/export-an-agent-bundle.md) into the workbench's project directory and start Agent.Workbench again. Open the above defined workbench project and have a look at the tabs \[Resources\] and \[Agents\], where you will find your exported bundle and your first agent.

* Define a setup and [start your agent application](/01_getting-started/start-your-first-setup.md).

---

## Install Agent.Workbench {#install-agent-workbench}

To install Agent.Workbench, navigate to the release section of our repository at github under [https://github.com/EnFlexIT/AgentWorkbench/releases](https://github.com/EnFlexIT/AgentWorkbench/releases). Depending on your operating system, select the installation package. Agent.Workbench is available for Windows, Linux and MacOS.![](/00_images/01_GettingStarted/01_Workbench-Releases.png)Extract Agent.Workbench at a desired location on your file system. Afterwards start the executable of the program \(e.g. AgentGui.exe, agentgui.app or AgentGui\). After the splash, the main application window appears and and the SciMark 2.0 benchmark will be executed.

![](/00_images/01_GettingStarted/02_BenchmarkWindow.png)

## Create an Agent Project {#create-an-agent-project}

After the benchmark, click _**New Project**_ in the tool bar or by using the menu _**Project **_=&gt; ![](/eclipseProjects/org.agentgui/bundles/org.agentgui.core/icons/core/MBnew.png) _**New Project**_.![](/00_images/01_GettingStarted/03_CreateNewProject.png)



## Install Eclipse IDE for Java EE Developers {#install-eclipse-ide}

Using and creating new OSGI bundles, requires at least to use the Eclipse Plug-in Development Environment. For this, we recommend to install the Eclipse IDE for Java EE Developer. Simply navigate to [https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/), download the Eclipse Installer and follow its installation instructions.

