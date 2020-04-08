# Define a Target Platform

### Define the Target Platform for Agent Developments...

With a target platform you define the system or library structure against which you develop. By default, Eclipse uses the current Eclipse installation as the target platform. But since our goal is to extend Agent.Workbench with your own code, you need to develop against the system and library structure of Agent.Workbench. Consequently we need to add Agent.Workbench to our target platform. There are three possible ways to achieve this.

### ...based on an AWB Installation

Defining a target platform that points to an installation of Agent.Workbench is one way to do it. If you followed along our [Installation page](../installations.md), you already have an instance of Agent.Workbench installed. Eclipse then uses the installation as the library that you develop against. Defining a target platform that points to an Agent.Workbench installation is sufficient for most use cases and is recommended for beginners. As a developer you can also access the Agent.Workbench source code by downloading the "Development resources" in the installation. The tutorial on how to define a target platform, based on an AWB installation, can be found [here](target-platform-based-on-awb-installation.md).

### ...based on the AWB Software Repository

Another way to define the target platform is by utilizing the Agent.Workbench software repository, that is located on a remote server. The Target Platform then points to the repository and uses its bundles as the library, that you develop your own code against. The tutorial on how to define a target platform, based on the AWB software repository, can be found [here](getting-started-as-an-awb-developer.md).

### ...based on the AWB source code

A more cumbersome but equally effective way is to download the Agent.Workbench source code from GitHub directly into your Eclipse workspace. That way you don't have to explicitly change the target platform, but having all the bundles that you develop against in your IDE has the same effect. The tutorial on how to define a target platform, based on AWB sources, can be found [here](getting-started-as-an-awb-developer.md).

Defining a target platform is mandatory if you want to start Agent.Workbench from your Eclipse IDE. Instructions on how to start Agent.Workbench from Eclipse can be found on [this page](../starting-awb-from-eclipse.md).

