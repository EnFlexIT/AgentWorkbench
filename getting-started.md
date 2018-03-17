# Getting Started

This section shortly describes how you can define, develop and start your first agent-based "application". Even this is a very simple example, it will demonstrate the principle on how you can extend Agent.Workbench with your own agents and user functions. Since the workbench is based on OSGI / Eclipse _bundles_, you have to develope OSGI _plugins_ for your agent projects too. In the course of the text we are mixing-up these terms, but their meaning is the same \(plugin = bundle\). For further reading and a practical introduction into the modularity concept of OSGI, we recommend to read this tutorial: [http://www.vogella.com/tutorials/OSGi/article.html.](http://www.vogella.com/tutorials/OSGi/article.html)

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

After the benchmark, click _**New Project**_ in the tool bar or by using the menu _**Project **_=&gt; ![](/eclipseProjects/org.agentgui/bundles/org.agentgui.core/icons/core/MBnew.png) _**New Project.**_ Define a project title and the folder for your project and click _**OK**_.![](/00_images/01_GettingStarted/03_CreateNewProject.png)

The agent project will be located in the sub-directory **./projects/** beside the installation of Agent.Workbench, while the structure of the installation looks as shown in the image below. Beside this default location, you can also locate your agent projects at a different location that can be configured via menu _**Extras** _=&gt; _**Options**_ \(opens the Option Dialog\) =&gt; _**\[Tab: Directories\]**_ =&gt; _**Projects Root Directory**_ and by selecting ![](/eclipseProjects/org.agentgui/bundles/org.agentgui.core/icons/core/MBopen.png) the desired directory location.

![](/00_images/01_GettingStarted/04_InstallationStructure.png)

For the time being, save the project and close Agent.Workbench. [A description of the project window structure will be provided here](/the-project-window.md).

## Install Eclipse IDE for Java EE Developers {#install-eclipse-ide}

Creating new OSGI bundles, requires at least to use the Eclipse Plug-in Development Environment. For this, we recommend to install the Eclipse IDE for Java EE Developer that provides this environment, but also a little more. Navigate to [https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/) and download the Eclipse Installer.

Since the installer wants to create directories and copy files to it, execute the program as someone who has the right permissions. For example: if you want to install Eclipse under _C:\Program Files\jee-oxygen,_ you should have executed the installer as the administrator of the system. After the installer has started, select the right version of Eclipse and follow the further instructions.

![](/00_images/01_GettingStarted/05_EclipseInstaller.png)

## Setup the Eclipse-Workspace {#setup-the-eclipse-workspace}

### For Beginners

If you have never worked with Eclipse, you should first take the time to read one of the available beginner tutorials in the web. Some of them are:

* The HTMLHelp Center of Eclipse: 
  [http://help.eclipse.org/oxygen/index.jsp](http://help.eclipse.org/oxygen/index.jsp)
  You will recognize the big tree on the left hand site that gives you an impression of the multitude of tools available under Eclipse. The introductory part can be found navigating along _**Workbench User Guide**_ =&gt; _**Getting started**_ =&gt; _**Basic tutorial**_
* The tutorial site of Lars Vogel \(vogella\) that offers a lot of tutorial in the context of Java, Eclipse, Plugin development and other. The introduction to the Eclipse IDE \(Integrated Development Environment\) can be found here:
  [http://www.vogella.com/tutorials/Eclipse/article.html](http://www.vogella.com/tutorials/Eclipse/article.html), 

If you prefer a video tutorial, you might like one of these:

* [https://www.youtube.com/watch?v=23tAK5zdQ9c](https://www.youtube.com/watch?v=23tAK5zdQ9c)

* [https://www.youtube.com/watch?v=xO5DpU2j-WE](https://www.youtube.com/watch?v=xO5DpU2j-WE)

... or simply do what most programmers do, if they have a specific question: [ask Google](http://lmgtfy.com/?q=Eclipse+beginner+tutorial)!

Starting your Eclipse IDE the first time, you will be prompted to define a workspace directory. _Background_: Eclipse organizes Java projects in different, so-called workspaces. For example: while you develop your website code in one workspace, you can develop your agent system in another. Thus it can be avoided to mix-up things that do not belong to each other.

### For Advanced

If you are already using Eclipse for a while, you will be probably aware about switching a workspace. For sake of completeness: Go to menu _**File**_ =&gt; _**Switch Workspace**_ =&gt; \_**Other **\_and Browse for the desred workspace location.

![](/00_images/01_GettingStarted/06_Eclipse_SwitchWorkspace.png)

Having defined your workspace, Eclipse will be opend with a welcome page that points to further information and tutorials.

As next we have to make sure that we use the right [Perspective](https://www.tutorialspoint.com/eclipse/eclipse_perspectives.htm). Select menu _**Window **_=&gt; _**Perspective**_ =&gt; _**Open Perspective**_ =&gt; _**Other**_. and select the perspective for Plug-in Development.

![](/00_images/01_GettingStarted/07_SelectPlug-inPerspective.png)

## Define the Target Platform for agent developments {#define-the-target-platform-for-agent-developments}

With a target platform you define the system or library structure against which you develop your own code. By default Eclipse would use the current Eclipse installation by itself, but - of course - our goal is to extend Agent.Workbench with our own code. Consequently, we have to define a target platform that points to the previously installed Agent.Workbench.

To do so, 



