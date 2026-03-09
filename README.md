# Agent.Workbench (AWB)

![License](https://img.shields.io/badge/license-LGPL--2.1-blue.svg)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Build](https://img.shields.io/badge/build-Tycho%20%2F%20Maven-brightgreen.svg)
![Version](https://img.shields.io/badge/version-3.0.0-informational.svg)

**Agent.Workbench** is an open-source application platform for building, deploying, and operating **multi-agent systems** — from desktop applications to distributed, cloud-ready deployments. It combines the power of the [JADE](http://jade.tilab.com/) agent framework with a Java Swing-based desktop environment and a modern REST API, making agent technology accessible to developers and end users alike.

> Originally known as *Agent.GUI*, the project has been developed by [EnFlex.IT](http://www.enflex.it) and moved to GitHub in 2017. The current release is **v3.0.0**, built on Java 21 and Eclipse 4.x.

---

## What is Agent.Workbench?

Multi-agent systems (MAS) are a powerful paradigm for solving complex, distributed problems — used in energy management, industrial automation, simulation, and beyond. However, building and operating MAS has traditionally required deep framework knowledge and significant engineering effort.

**Agent.Workbench bridges that gap.** It provides:

- A **graphical desktop environment** (Java Swing) for configuring, running, and monitoring agent-based applications — no command-line expertise required. Eclipse RCP and SWT are used for the RCP-specific installation variant and for property dialogs provided by the RCP framework.
- A **REST API** (OpenAPI-based, served via embedded Jetty) for integrating AWB into larger system landscapes or controlling it programmatically.
- A **web-based UI** (in active development) for browser-based access and remote operations.
- An **extensible plugin architecture** (OSGi) that allows developers to build custom agent applications on top of AWB without modifying the core.

AWB is not just a development tool — it is a **runtime platform** for production deployments, including embedded and headless operation on edge hardware.

---

## Key Features

- **OSGi Plugin System** — Modular architecture allows teams to build independent application bundles that plug into AWB seamlessly.
- **Simulation & Time Management** — Built-in support for discrete and continuous simulations with time control.
- **Load Balancing & Distribution** — Distribute agent workloads across multiple machines in a network.
- **JADE Integration** — Full support for the JADE multi-agent platform (IEEE/FIPA-compliant), including agent lifecycle management, ontologies, and inter-agent communication.
- **Desktop UI (Java Swing)** — A rich desktop application for end users to manage agent projects, configure environments, and monitor running systems. SWT is used only within the Eclipse RCP installation variant and for RCP-provided property dialogs.
- **Web Server Base UI (System Tray)** — The Web Server runtime includes a lightweight base UI accessible via the system tray icon, allowing local configuration of the web server and related settings without a full desktop environment.
- **Embedded REST API & Embedded System Mode** — OpenAPI-documented REST endpoints served via Jetty, fully extensible for custom application APIs. AWB can also be deployed headlessly on edge devices or servers, with agents starting automatically on boot.
- **Web UI** — A React Native-based web interface for remote access and monitoring (active development, see [web.template](https://github.com/EnFlexIT/web.template)).
- **Database Support** — Hibernate-based database connectivity with support for MySQL, MariaDB, and PostgreSQL.
- **Protocol Connectors** — Integration of communication protocols (e.g. MQTT) via [AWB-Connectors](https://github.com/EnFlexIT/AWB-Connectors).

---

## Architecture Overview

AWB is structured as a layered OSGi application, built and packaged with [Eclipse Tycho](https://eclipse.dev/tycho/).

```
┌─────────────────────────────────────────────────────────┐
│                  Application Layer                      │
│     (Custom agent applications as OSGi bundles,         │
│      JADE-based agents, ontologies)                     │
├─────────────────────────────────────────────────────────┤
│                   AWB Product Layer                     │
│  ┌──────────────┐  ┌───────────────────────────────┐    │
│  │  Desktop UI  │  │  REST API & Web UI            │    │
│  │  (Swing /    │  │  (Jetty / OpenAPI /           │    │
│  │   SWT/RCP)   │  │   React Native)               │    │
│  └──────────────┘  └───────────────────────────────┘    │
├─────────────────────────────────────────────────────────┤
│                   AWB Core Layer                        │
│  Agent Lifecycle │ Simulation │ Load Balancing │ Config │
├─────────────────────────────────────────────────────────┤
│                    JADE Agent Platform                  │
│          (IEEE/FIPA-compliant, Java-based MAS)          │
├─────────────────────────────────────────────────────────┤
│               OSGi Runtime (Eclipse Equinox)            │
└─────────────────────────────────────────────────────────┘
```

**Two products are built from this repository:**

- **AWB Desktop** — The full Swing-based desktop application for development and operations.
- **AWB Web Server** — A server runtime with embedded Jetty and REST API. Includes a lightweight base UI accessible via the system tray for local configuration.

Both products share the same core bundles and are assembled via Tycho feature definitions.

---

## Getting Started

### Prerequisites

- **Java 21** (JDK required)
- **Eclipse IDE** with Plug-in Development Environment (PDE) for development
- Maven 3.9+ (for command-line builds)

### Download & Run

Pre-built releases are available on the [Releases page](https://github.com/EnFlexIT/AgentWorkbench/releases). AWB is provided as two products — the full **Desktop Application** and a headless **Web Server Application** — for all major platforms.

**AWB Desktop Application** (Swing UI + embedded Jetty):

| Platform | Download |
|---|---|
| Windows (x86_64) | [de.enflexit.awb-win32.win32.x86_64.zip](https://p2.enflex.it/awb/product/3.0.0/de.enflexit.awb-win32.win32.x86_64.zip) |
| Linux (x86_64) | [de.enflexit.awb-linux.gtk.x86_64.tar.gz](https://p2.enflex.it/awb/product/3.0.0/de.enflexit.awb-linux.gtk.x86_64.tar.gz) |
| macOS (x86_64) | [de.enflexit.awb-macosx.cocoa.x86_64.tar.gz](https://p2.enflex.it/awb/product/3.0.0/de.enflexit.awb-macosx.cocoa.x86_64.tar.gz) |

**AWB Web Server Application** (headless REST API + system tray base UI):

| Platform | Download |
|---|---|
| Windows (x86_64) | [de.enflexit.awb.ws-win32.win32.x86_64.zip](https://p2.enflex.it/awb/product/3.0.0/de.enflexit.awb.ws-win32.win32.x86_64.zip) |
| Linux (x86_64) | [de.enflexit.awb.ws-linux.gtk.x86_64.tar.gz](https://p2.enflex.it/awb/product/3.0.0/de.enflexit.awb.ws-linux.gtk.x86_64.tar.gz) |
| macOS (x86_64) | [de.enflexit.awb.ws-macosx.cocoa.x86_64.tar.gz](https://p2.enflex.it/awb/product/3.0.0/de.enflexit.awb.ws-macosx.cocoa.x86_64.tar.gz) |

1. Download the archive for your platform and product variant.
2. Extract and launch the `AgentWorkbench` executable (Desktop) or the start script (Web Server).
3. The application starts with an embedded Jetty server (default port: `8080`, configurable).

The REST API is available at:
```
http://localhost:8080/api/v1/
```

API documentation (Swagger UI) is served at:
```
http://localhost:8080/api/swagger-ui
```


### Build from Source

```bash
git clone https://github.com/EnFlexIT/AgentWorkbench.git
cd AgentWorkbench/eclipseProjects/de.enflexit.awb
mvn clean install
```

The build produces both products in the respective `target/products` directories.

### Maven Build Profiles

Additional Maven profiles are available for publishing build artifacts:

| Profile | Description |
|---|---|
| `p2DeployClean` | Cleans the remote p2 update site before publishing |
| `p2Deploy` | Publishes the p2 update site to the central repository |
| `uploadProducts` | Uploads the built product archives to the distribution server |

Example — full build including p2 deployment and product upload:
```bash
mvn clean install -P [p2DeployClean|p2Deploy],uploadProducts
```

> **Note:** These profiles require server credentials configured in your local `settings.xml`. They are intended for CI/CD use only and are executed automatically via the Jenkins pipeline.

### Eclipse Update Site

AWB and its dependencies can be installed directly into an Eclipse IDE via the central p2 repository:

```
https://p2.enflex.it/awb/latest/
```

In Eclipse: **Help → Install New Software → Add…** and enter the URL above.

---

## Related Repositories

| Repository | Description |
|---|---|
| [JADE](https://github.com/EnFlexIT/JADE) | EnFlex.IT fork of the JADE agent platform |
| [RestAPIs](https://github.com/EnFlexIT/RestAPIs) | OpenAPI specifications for AWB REST endpoints |
| [web.template](https://github.com/EnFlexIT/web.template) | React Native-based web UI template for AWB applications |
| [AWB-Connectors](https://github.com/EnFlexIT/AWB-Connectors) | Protocol connectors (MQTT and others) for AWB |
| [AWB-ProjectAssist](https://github.com/EnFlexIT/AWB-ProjectAssist) | Eclipse wizards for creating AWB-compatible plugins and projects (AWBTools) |
| [EnergyAgents](https://github.com/EnFlexIT/EnergyAgents) | Reference implementation: Energy Agent applications on AWB |

---

## Documentation

- 📖 **GitBook** — [https://enflexit.gitbook.io/agent-workbench](https://enflexit.gitbook.io/agent-workbench)

- 🔧 **Wiki** — [GitHub Wiki](https://github.com/EnFlexIT/AgentWorkbench/wiki)

---

## Contributing

We welcome contributions from the community. Please open an issue to discuss proposed changes before submitting a pull request.

- Bug reports and feature requests: [GitHub Issues](https://github.com/EnFlexIT/AgentWorkbench/issues)
- Discussions: [GitHub Discussions](https://github.com/EnFlexIT/AgentWorkbench/discussions)

---

## License

Agent.Workbench is released under the [GNU Lesser General Public License v2.1](LICENSE).

© [EnFlex.IT](http://www.enflex.it), Essen, Germany.


