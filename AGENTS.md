# AGENTS.md

Repo-specific guidance for AI agents working in **Agent.Workbench (AWB)** — an OSGi/Eclipse-Tycho multi-agent platform on JADE/Java 21. Read this before editing or building.

## Build

The Maven/Tycho reactor root is **not** the repo root. It is `eclipseProjects/de.enflexit.awb`.

```bash
cd eclipseProjects/de.enflexit.awb
mvn clean install
```

Prerequisites (non-negotiable — bundles declare `JavaSE-21`):
- **JDK 21** and **Maven 3.9+**
- **Git submodules must be initialized** before building the `web` bundles:
  ```bash
  git submodule update --init
  ```
  `eclipseProjects/de.enflexit.awb/bundles/web/xAPI` is a submodule (the `RestAPIs` repo) holding the OpenAPI YAML specs consumed by codegen (see below). On a fresh clone it is empty and the `web`/REST build will fail.

Known repo wart: there is a second, orphaned gitlink at `bundles/web/de.enflexit.awb.ws.restapi/xAPI` with **no `.gitmodules` mapping**. `git submodule status` errors on it. The *valid* submodule the build needs is `bundles/web/xAPI` (referenced by codegen as `../../xAPI/*.yaml`). Ignore the orphaned one.

## CI / deploy profiles

Current CI is `Jenkinsfile` (Java 21). `Jenkinsfile.J8` and `Jenkinsfile.J11` are **legacy/stale** (J8 even points at the old `eclipseProjects/org.agentgui` path) — do not use them as a reference for current paths.

Deploy profiles are CI-only and need credentials in `~/.m2/settings.xml`:
- `p2Deploy`, `p2DeployClean` — publish p2 update site to `https://p2.enflex.it`
- `uploadProducts` — upload product archives to the distribution server

CI invocation:
```bash
mvn clean install -P p2Deploy,uploadProducts -f eclipseProjects/de.enflexit.awb \
  -Dtycho.localArtifacts=ignore -Dtycho.p2.transport.min-cache-minutes=0
```

## Repository layout

Non-obvious from filenames alone:

- `eclipseProjects/de.enflexit.awb/` — the actual project: `bundles/`, `features/`, `releng/`, `examples/`. Reactor modules in the root `pom.xml` are **only** `bundles`, `features`, `releng`.
- `bundles/core/` — framework core, JADE integration, common libs, ontology, base UI, OIDC, logging, geography, expression, JAXB binding.
- `bundles/desktop/` — Swing and SWT desktop UI variants.
- `bundles/web/` — Jetty 12 REST API (`ws.core`, `ws.restapi`, `ws.dynSiteApi`, `ws.swagger2x`, `ws.client`).
- `bundles/database/` — Hibernate + Derby/MariaDB/MySQL/Postgres drivers + user management.
- `bundles/docker/`, `bundles/dataFrame/` — Docker client; tablesaw data frames.
- `features/` — Tycho feature definitions that assemble the two products.
- `releng/` — target platform (`de.enflexit.awb.target`), both product definitions, p2 update site, and the **parent/configuration pom** (`de.enflexit.awb.configuration`) that defines Tycho 5.0.0, the target platform, and build environments.
- `examples/` — sample OSGi plugin bundles. **Not part of the Maven build** (no parent pom, not in reactor modules). Develop/run these from Eclipse PDE against the AWB target platform only.
- `awbProjects/` — committed runtime AWB *project* data (XML configs, setups, environments). End-user project files, **not build source**. Do not wire these into Maven.
- `docs/gitBook/` — GitBook source (`book.json` roots at `./docs/gitBook`).
- `plans/`, `result/` — scratch/working dirs; generally empty.

## Products & build output

Two products are assembled from the same bundles via Tycho features:

| Product | Feature | Output artifacts |
|---|---|---|
| AWB Desktop (Swing) | `de.enflexit.awb.desktop.feature` | `releng/de.enflexit.awb.product/target/products/de.enflexit.awb-*` |
| AWB Web Server (headless Jetty REST) | `de.enflexit.awb.ws.feature` | `releng/de.enflexit.awb.ws.product/target/products/de.enflexit.awb.ws-*` |

Javadoc zip is also produced at `bundles/de.enflexit.awb.help/target/de.enflexit.awb.help-*javadoc.zip`.

## OpenAPI codegen (do not hand-edit generated sources)

`bundles/web/de.enflexit.awb.ws.restapi/xCodgen` and `bundles/web/de.enflexit.awb.ws.dynSiteApi/xCodgen` run `openapi-generator-maven-plugin` (`jaxrs-jersey` / `jersey3`, Jakarta EE) **during the build**, reading YAML specs from the `xAPI` submodule:
- `AWB-RestAPI.yaml` → `de.enflexit.awb.ws.restapi`
- `Dynamic-Content-Api.yaml` → `de.enflexit.awb.ws.dynSiteApi`

Generated Java lands in `xCodgen/target/generated-sources/openapi/{src/main,src/gen}/java/` and is wired into the bundle classpath via `build.properties` / `build-helper-maven-plugin`.

- **Never edit files under `xCodgen/target/`** — they are regenerated.
- To change the API contract, edit the YAML in the **`xAPI` (RestAPIs) submodule**, then rebuild.
- Hand-written REST implementation lives in each bundle's `src/`.

## Testing

There is **no automated test suite in the build**. Tycho surefire is commented out in `releng/de.enflexit.awb.configuration/pom.xml`. The only `*Test.java` files are ad-hoc mains inside `src/` (not run by Maven). Do not assume `mvn test` validates anything — verify changes by building and, where relevant, launching the product.

## OSGi / Tycho conventions

- `META-INF/MANIFEST.MF` is the source of truth for bundle metadata (deps, `Export-Package`, `Service-Component`, execution env). `.polyglot.META-INF` and `.polyglot.build.properties` are Tycho-derived and gitignored — **do not edit them**.
- Bundle versions are OSGi `x.y.z.qualifier`; Tycho substitutes the qualifier from the jgit timestamp (`tycho-buildtimestamp-jgit`). Working-tree dirtiness is a warning, not an error.
- Target platform is pinned in `releng/de.enflexit.awb.target/de.enflexit.awb.target.target` (Eclipse release + Maven-p2 dependencies). Bumping a dependency means editing the `.target` file, not a `<dependencies>` block.
- The Tycho build extension is declared in `.mvn/extensions.xml` (`tycho-build` 4.0.12).
- Root `.gitignore` excludes `**/.settings` and `**/.project` — Eclipse PDE metadata is regenerated on import; do not commit it.

## Workflow notes

- Contributing: open an issue to discuss before opening a PR (per `README.md`).
- Default branch is `master`. A `Java17-Jakarta` remote branch exists for the older Java 17/Jakarta line — current `master` is Java 21.
