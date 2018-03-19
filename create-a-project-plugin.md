# Create a Plug-in for an Agent.Workbench Project

This section shortly describes how you can define, develop and start your first agent-based "application". Even this is a very simple example, it will demonstrate the principle on how you can extend Agent.Workbench with your own agents and further user functions. Since the workbench is based on OSGI / Eclipse _bundles_, you have to develope OSGI _plug-ins_ for your agent projects too. In the course of the text we are mixing-up these terms from time to time, but their meaning is the same \(plug-in = bundle\). For further reading and a practical introduction into the modularity concept of OSGI, we recommend to read this tutorial: [http://www.vogella.com/tutorials/OSGi/article.html.](http://www.vogella.com/tutorials/OSGi/article.html)

**Eclipse Projects & Agent.Workbench Projects**: We would also like to mention here that, in the context of Agent.Workbench and Eclipse, the word "project" is used with two meanings. Since Agent.Workbench handles agents or Multi-Agent Systems \(MAS\) as self-containing projects on one hand site, also Eclipse uses the term "project" for Java or plug-in developments. This, in turn, makes it sometimes misleading, if we speak about an "agent project" in general. More precisely, we have to distinguish these project types to avoid a lingusitic misunderstanding.

As a prerequisite, we assume that you have an installed Agent.Workbench and an Eclipse IDE \(**I**ntegrated **D**evelopment **E**nvironment\) that enables you to develop plug-ins \(an IDE that enables Java developments only, wil not be sufficient\). For further details see the [Getting Started](//getting-started.md#getting-started) tutorial.

In short words: Our goal here is to create an Eclipse-Plug-in and a first agent, start and debug this agent from the Eclipse IDE and finally, to export the developed agent into a Agent.Workbench project so that the agent application can work on its own \(without the IDE\). For this the following steps are to be done:

* Create a new Plug-in-Project in the Eclipse IDE.
* Create an Agent: Define your desired package structure and create your first agent class.
* Define a Debug-Configuration
* Debug an Agent:
* Export your agent bundle / plugin into the workbench's project directory and Agent.Workbench standalone. Open the above defined workbench project and have a look at the tabs \[Resources\] and \[Agents\], where you will find your exported bundle and your first agent.

* Define a setup and start your agent application.



