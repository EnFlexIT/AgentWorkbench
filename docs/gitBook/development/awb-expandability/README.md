---
description: 'How can AWB be extended, and what can be extended?'
---

# AWB-Expandability

## How to extend?

### Extend superclasses provided by the framework \(e.g. Agents, Environment Models, ...\)

### Create one or more OSGI bundles and include it/them into an AWB project

Create new PlugIn Project in eclipse  
Write code  
Implement ClassLoadService  
Export into the project's plugin-folder

### Create a feature summarizing several bundles and install it into AWB from a p2 repository

What is a feature? -&gt; Eclipse, p2 Repository  
How to create a feature?  
How to add a feature to AWB?

### When to build a project bundle? When to build a feature?

## What to extend?

### Extensibility Overview

The table below shows what can be extended, and is applicable for both types of extensions; project bundles as well as AWB features.

| Topic | Related Questions | Tutorial/Explanation | Example |
| :--- | :--- | :--- | :--- |
| Environment Models | How to specify an environment model for the agents to operate in? |  |  |
| Time Models | How to specify the simulation time? |  |  |
| Project Plug-Ins | How to react to user interactions on projects like save, load, change setup etc.? |  |  |
| Project Tabs | How to add a new tab to the project window? |  |  |
| Menus / Menu items / Toolbar Buttons | How to add components for user interactions?  |  |  |
| Ontologies | How to create ontologies for AWB projects and use them? |  |  |
| Agents | How to implement and configure agents? |  |  |
| JADE Services | How to extend the functionality provided by the JADE platform to the agents? |  |  |
| Load Balancing \(static, dynamic\) | How to atuomatically manage the distribution of agents automatically? |  |  |
| Bundle Properties | How to persist bundle-specific information? |  |  |
| Database Conenctors | How to connect AWB to different  database management systems? |  |  |
| Database Scheme Access | How to get read / write access to customized database structures \(using Hibernate\)? |  |  |

