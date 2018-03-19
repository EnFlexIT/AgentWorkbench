# Create a Plugin for an Agent.Workbench Project

This section shortly describes how you can define, develop and start your first agent-based "application". Even this is a very simple example, it will demonstrate the principle on how you can extend Agent.Workbench with your own agents and user functions. Since the workbench is based on OSGI / Eclipse _bundles_, you have to develope OSGI _plugins_ for your agent projects too. In the course of the text we are mixing-up these terms, but their meaning is the same \(plugin = bundle\). For further reading and a practical introduction into the modularity concept of OSGI, we recommend to read this tutorial: [http://www.vogella.com/tutorials/OSGi/article.html.](http://www.vogella.com/tutorials/OSGi/article.html)

As a prerequisite, we assume that you have an installed Agent.Workbench and an Eclipse IDE \(**I**ntegrated **D**evelopment **E**nvironment\) that enables you to develop Plugins \(an IDE that enables Java developments only, wil not be sufficient\). For further details see the [Getting Started](//getting-started.md#getting-started) tutorial.   





Create your first agent Plugin-Project:Create an Eclipse Plugin-Project, your desired package structure and create your first agent class.

* Export your agent bundle / plugin into the workbench's project directory and start Agent.Workbench again. Open the above defined workbench project and have a look at the tabs \[Resources\] and \[Agents\], where you will find your exported bundle and your first agent.

* Define a setup and start your agent application.



