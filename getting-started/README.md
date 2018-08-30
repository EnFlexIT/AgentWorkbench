# Getting Started

This section describes the required steps, to get started with the Agent.Workbench development environment. For this the following installation and configuration tasks have to be done:

* [Install Agent.Workbench](./#install-agent-workbench): Go to our [GitHub-Site](https://github.com/EnFlexIT/AgentWorkbench/releases) and download the latetst release of Agent.Workbench \(for Windows, Linux or MacOS\).Just extract the provided archive and place it at a desired location.
* [Create an Agent Project](./#create-an-agent-project): After installation, start Agent.Workbench. The first start will execute a benchmark that helps us to classify your machine in case of [distributed agent executions](../distributed-application.md). Go to the menu or toolbar and create a ![](../.gitbook/assets/mbnew%20%281%29.png) _**New Project**_. That's it for the moment - simply close Agent.Workbench now.
* [Install the Eclipse IDE](./#install-eclipse-ide): We recommend to use the [Eclipse IDE for Java EE Developer](https://www.eclipse.org/downloads/) for your developments.
* [Setup your Workspace](getting-started.md#setup-the-eclipse-workspace): Create a new workspace. This is required, since you need to develop against the bundles of the Agent.Workbench installation.
* [Define the Target Platform](./#define-the-target-platform-for-agent-developments): Create a Target Platform definition that points to the Agent.Workbench installation.

## Install Agent.Workbench {#install-agent-workbench}

To install Agent.Workbench, navigate to the release section of our repository at github under [https://github.com/EnFlexIT/AgentWorkbench/releases](https://github.com/EnFlexIT/AgentWorkbench/releases). Depending on your operating system, select the installation package. Agent.Workbench is available for Windows, Linux and MacOS.![](../.gitbook/assets/01_workbench-releases.png)Extract Agent.Workbench at a desired location on your file system. Afterwards start the executable of the program \(e.g. AgentGui.exe, agentgui.app or AgentGui\). After the splash, the main application window appears and and the SciMark 2.0 benchmark will be executed.

![](../.gitbook/assets/02_benchmarkwindow.png)

## Create an Agent Project {#create-an-agent-project}

After the benchmark, click _**New Project**_ in the tool bar or by using the menu _**Project**_ =&gt; ![](../.gitbook/assets/mbnew.png) _**New Project.**_ Define a project title and the folder for your project and click _**OK**_.![](../.gitbook/assets/03_createnewproject.png)

The agent project will be located in the sub-directory **./projects/** beside the installation of Agent.Workbench, while the structure of the installation looks as shown in the image below. Beside this default location, you can also locate your agent projects at a different location that can be configured via menu _**Extras**_ =&gt; _**Options**_ \(opens the Option Dialog\) =&gt; _**\[Tab: Directories\]**_ =&gt; _**Projects Root Directory**_ and by selecting ![](../.gitbook/assets/mbopen.png) the desired directory location.

![](../.gitbook/assets/04_installationstructure.png)

For the time being, save the project and close Agent.Workbench. [A description of the project window structure will be provided here](../the-project-window.md).

## Install Eclipse IDE for Java EE Developers {#install-eclipse-ide}

Creating new OSGI bundles, requires at least to use the Eclipse Plug-in Development Environment \(PDE\). For this, we recommend to install the Eclipse IDE for Java EE Developer that provides this environment, but also a little more. Navigate to [https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/) and download the Eclipse Installer.

Since the installer wants to create directories and copy files to it, execute the program as someone who has the right permissions. For example: if you want to install Eclipse under _C:\Program Files\jee-oxygen,_ you should have executed the installer as the administrator of the system. After the installer has started, select the right version of Eclipse and follow the further instructions.

![](../.gitbook/assets/05_eclipseinstaller.png)

## Setup the Eclipse-Workspace {#setup-the-eclipse-workspace}

### For Beginners

If you have never worked with Eclipse, you should first take the time to read one of the available beginner tutorials in the web. Some of them are:

* the HTMLHelp Center of Eclipse: 

  [http://help.eclipse.org/oxygen/index.jsp](http://help.eclipse.org/oxygen/index.jsp)

  You will recognize the big tree on the left hand site that gives you an impression of the multitude of tools available under Eclipse. The introductory part can be found navigating along _**Workbench User Guide**_ =&gt; _**Getting started**_ =&gt; _**Basic tutorial**_

* the tutorial site of Lars Vogel \(vogella\) that offers a lot of tutorial in the context of Java, Eclipse, Plugin development and other. The introduction to the Eclipse IDE \(Integrated Development Environment\) can be found here:

  [http://www.vogella.com/tutorials/Eclipse/article.html](http://www.vogella.com/tutorials/Eclipse/article.html), 

If you prefer a video tutorial, you might like one of these:

* [https://www.youtube.com/watch?v=23tAK5zdQ9c](https://www.youtube.com/watch?v=23tAK5zdQ9c)
* [https://www.youtube.com/watch?v=xO5DpU2j-WE](https://www.youtube.com/watch?v=xO5DpU2j-WE)

... or simply do what most programmers do, if they have a specific question: [ask Google](http://lmgtfy.com/?q=Eclipse+beginner+tutorial)!

Starting your Eclipse IDE the first time, you will be prompted to define a workspace directory. _Background_: Eclipse organizes Java projects in different, so-called workspaces. For example: while you develop your website code in one workspace, you can develop your agent system in another. Thus it can be avoided to mix-up things that do not belong to each other.

### For Advanced

If you are already using Eclipse for a while, you will be probably aware about switching a workspace. For sake of completeness: Go to menu _**File**_ =&gt; _**Switch Workspace**_ =&gt; _**Other**_ ****and Browse for the desred workspace location.

![](../.gitbook/assets/06_eclipse_switchworkspace.png)

Having defined your workspace, Eclipse will be opened with a welcome page that points to further information or tutorials - simply close this window.

As next, we have to make sure that we use the right [Perspective](https://www.tutorialspoint.com/eclipse/eclipse_perspectives.htm). Select menu _**Window**_ =&gt; _**Perspective**_ =&gt; _**Open Perspective**_ =&gt; _**Other**_. and select the perspective for Plug-in Development.

![](../.gitbook/assets/07_selectplug-inperspective.png)

## Define the Target Platform for Agent Developments {#define-the-target-platform-for-agent-developments}

With a target platform you define the system or library structure against which you develop your own code. By default Eclipse would use the current Eclipse installation by itself, but - of course - our goal is to extend Agent.Workbench with your own code. Consequently, we have to define a target platform that points to the previously installed Agent.Workbench.

To do so, open the Eclipse preferences \(menu _**Window**_ =&gt; _**Preferences**_\) and type the word 'target' into the search text field \(see image\).![](../.gitbook/assets/08_targetplatform_preferences.png)

Further, _**Add**_ a new Target Platform definition, choose **Nothing: Start with an empty target definition** on the first dialog page and press the _**Next**_ button underneath. In the subsequent dialog:

![](../.gitbook/assets/09_targetplatform_addcontent.png)

1. Define the name of the Target Platform definition \(e.g. Agent.Workbench\)
2. Choose _**Add**_ to select the content \(bundles and / or features\) that define your target platform.
3. Select _**Installation**_, press _**Next**_ and _**Browse**_ to the installation of Agent.Workbench in your file system.
4. Again, click _**Next**_ to get a preview of the bundles / plugins to be used for your Target Platform or directly click _**Finish**_.

As a result, the field in the _**Locations**_ tab should point to your Agent.Workbench installation, saying that approx. 131 plugins were found. Click on _**Finish**_**.** The new target platform definition should now also be displayed in the list in the preference dialog. As final step mark this new definition as active \(_**tick the corresponding box**_\) and press _**Apply and Close**_.

Congratulations you are prepared to develop your first agent project with Agent.Workbench.

