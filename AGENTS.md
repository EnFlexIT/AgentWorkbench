# AGENTS.md — Agent.Workbench (AWB)

Java 21 / OSGi (Equinox) / Eclipse Tycho multi-bundle product. Two products are built from one reactor:
AWB **Desktop** (Swing) and AWB **Web Server** (headless Jetty + REST).

## Orientation

- **The Maven reactor root is NOT the repo root.** It is `eclipseProjects/de.enflexit.awb/`.
  Always pass `-f eclipseProjects/de.enflexit.awb`.
- Reactor modules: `bundles/` (code), `features/` (p2 features), `releng/` (target platform, products, update site, parent pom).
- Parent/config POM with all versions and the target-platform binding: `eclipseProjects/de.enflexit.awb/releng/de.enflexit.awb.configuration/pom.xml`.
- `eclipseProjects/de.enflexit.awb/examples/` has **no aggregator pom and is not in the reactor** — Maven never compiles it. Only the Eclipse workspace does.
- `awbProjects/` and `result/` hold runtime AWB projects/sample data, not source.
- `plans/` is a scratch directory for agent plan/design files.

## Verification

- **There are no tests.** No JUnit anywhere; `tycho-surefire-plugin` is commented out in the parent pom (`releng/de.enflexit.awb.configuration/pom.xml:100-116`). Never claim tests pass, and don't add JUnit/test infra without asking.
- **Default verification is compiling in the Eclipse IDE (PDE workspace)**, not Maven. Do not run a full Tycho build as a routine check — it resolves the whole target platform (Eclipse `2026-03` p2 site plus many Maven locations) and builds native product archives for 4 platforms.
- If a Maven build is explicitly requested:
  ```
  JAVA_HOME=$(/usr/libexec/java_home -v 21) mvn clean install -f eclipseProjects/de.enflexit.awb
  ```
  `JAVA_HOME` matters: on this machine `mvn` defaults to Homebrew JDK 26 while the project targets `JavaSE-21`.
- **Ignore Java LSP/jdtls diagnostics.** Outside the PDE workspace there is no classpath: dependencies resolve through the Tycho target platform and OSGi `Import-Package`, so a plain language server reports mass "import cannot be resolved" / "cannot be resolved to a type" errors in untouched files (`org.eclipse.*`, `de.enflexit.*`, `edu.uci.ics.jung.*`, `com.nimbusds.*`, …). These are artifacts of the tooling, not real breakage — do not "fix" them.
- The `p2Deploy` / `p2DeployClean` / `uploadProducts` profiles scp to `p2.enflex.it` and need credentials in `~/.m2/settings.xml`. CI-only — never run them.
- CI is Jenkins. `Jenkinsfile` (Java 21) is the live one. `Jenkinsfile.J11` and `Jenkinsfile.J8` are dead (J8 still points at the removed `eclipseProjects/org.agentgui`).

## Tycho pomless — where build metadata actually lives

Bundles and features have **no `pom.xml`**. `tycho-build` (`.mvn/extensions.xml`) synthesizes one into `.polyglot.META-INF` (gitignored, regenerated).

- To change a bundle's dependencies/version/exports, edit `META-INF/MANIFEST.MF` and `build.properties` — never `.polyglot.META-INF`.
- Adding a bundle requires **two** edits: a `<module>` entry in the parent aggregator pom (e.g. `bundles/core/pom.xml`, `bundles/web/pom.xml`) **and** inclusion in the right `features/*/feature.xml`. Missing the feature means it silently never ships.
- Real `pom.xml` files exist only for aggregators, `releng/*`, `bundles/de.enflexit.awb.help`, and the side projects `xCodgen/` and `xPullMavenDeps/`.
- Every MANIFEST uses `Bundle-RequiredExecutionEnvironment: JavaSE-21`; `Require-Capability`/`osgi.ee` is not used. Keep that consistent.

## REST API: committed generated code (read before touching `bundles/web`)

`de.enflexit.awb.ws.restapi` and `de.enflexit.awb.ws.dynSiteApi` compile OpenAPI-generated sources from
`xCodgen/target/generated-sources/openapi/src/{gen,main}/java` — wired in via `build.properties` (`source..`) and `.classpath`.

- **Those generated sources are committed to git.** `xCodgen/.gitignore` ignores only `target/generated-sources/openapi/pom.xml`, not the sources. **Never delete or clean `xCodgen/target`** — you would delete tracked source. A reactor `mvn clean` does not touch it (separate Maven project).
- Codegen is a standalone pom outside the reactor: `mvn -f bundles/web/de.enflexit.awb.ws.restapi/xCodgen/pom.xml generate-sources` (openapi-generator 7.22.0, `jaxrs-jersey`/`jersey3`).
- Its input spec lives in the **`xAPI` git submodule** (`bundles/web/xAPI` → `EnFlexIT/RestAPIs`), which is currently **not checked out** (empty dir). `.gitmodules` also has an absolute `path = /eclipseProjects/...`, which breaks `git submodule update --init`. So codegen cannot be re-run as-is — this is why the generated output is committed.
- To change endpoint **behaviour**, edit `src/de/enflexit/awb/ws/restapi/impl/*Impl.java`. The generated `gen/impl/*ApiServiceImpl.java` stubs are dead code.
- To add an endpoint you must also: register the generated `*Api` class in `RestApiConfiguration.java`, and map it in `ServletInitParameter.java` (`"<X>Api.implementation"` → your impl class). The generated `*Api` constructors resolve impls by `Class.forName` on that servlet init param.
- `bundles/de.enflexit.awb.help` writes javadoc **into its own source tree** (`help/api-docs/javadoc/`, `cleanFirst=true`) via `tycho-document-bundle-plugin` — gitignored, but expect churn there after builds.

## Architecture facts that filenames don't reveal

- OSGi entrypoint: `bundles/core/de.enflexit.awb.core/src/de/enflexit/awb/core/AwbIApplication.java`, registered in that bundle's `plugin.xml`. Product-specific subclasses: `AwbIApplicationSWT` (desktop.swt), `AwbIApplicationWeb` (ws.core). The `AWBProduct` enum lives inside `GlobalInfo`.
- Central all-static hub: `de.enflexit.awb.core.Application` (~1280 lines). Config singleton reached only via `Application.getGlobalInfo()` (`core/config/GlobalInfo.java`).
- **Extensibility is OSGi Declarative Services, not extension points.** Components are `OSGI-INF/*.xml` listed in `Service-Component:`; lookup goes through `de.enflexit.common.ServiceFinder` (`getServiceReferences`), *not* `java.util.ServiceLoader`. Only 4 Eclipse extension points exist — and note `de.enflexit.awb.desktop.mainWindowExtension` is declared in bundle `de.enflexit.awb.baseUI`, not `.desktop`.
- UI is decoupled behind `de.enflexit.awb.core.ui.*` (`AgentWorkbenchUI`, `AwbMainWindow`, `AwbConsole`, …). **Two providers are registered simultaneously**: `baseUI.BaseUiService` (console, dialogs, tray, monitors) and `desktop.DesktopUiService` (main window, project windows/tabs). `AgentWorkbenchUiManager` calls every provider and expects **exactly one non-null** result — see `AgentWorkbenchUiManager.getMainWindow()` (line ~410). So when adding an `AgentWorkbenchUI` method, each implementation must **return `null`** for artifacts it does not own; throwing or returning a placeholder breaks the dispatcher.
- Agents/ontologies/time models are found by classpath scanning: `de.enflexit.common.bundleEvaluation.BundleEvaluator`, driven by `AwbBundleActivator`, with `core/bundleEvaluation/FilterFor*.java`.
- `bundles/desktop/de.enflexit.awb.desktop.swing/` is a stale leftover (only a `target/` dir, not a module). Ignore it.

## Code conventions

- **Tabs** for indentation. No license header block — files start with `package`, then a Javadoc with `@author`.
- `**/.settings` is gitignored at the repo root, so **no formatter config is shared**. Match the surrounding file's style by hand.
- Swing fields use WindowBuilder-style names (`jButtonOK`, `jLabelHeader`) with lazy `getJXxx()` accessors. Follow the local pattern.
- **i18n — source language is German.** `Language.translate("...")` treats the literal as **DE**; use `Language.translate("...", Language.EN)` for English literals. Dictionary: `bundles/core/de.enflexit.language/properties/dictionary.csv` (`;`-separated, header `SOURCE_LANGUAGE;DE;EN;IT;ES;FR`) plus its Base64 twin `dictionary.bin`. Both are tracked.
  - Gotcha: unknown expressions are auto-appended at runtime, and `Application.stop()` calls `Language.saveDictionaryFile()` (`Application.java:828`), rewriting **both** files fully sorted. Running AWB dirties them. Review that diff and don't commit unrelated dictionary churn.
- Logging: new code uses slf4j (`private static Logger LOGGER = LoggerFactory.getLogger(X.class);`). Legacy code uses `System.out.println`/`System.err.println`, which still works because `de.enflexit.logging.console.ConsoleScanner` installs a `PrintStreamListener` over `System.out`/`System.err` and routes them to the AWB console. Prefer slf4j in new code. Backend is logback (`bundles/core/de.enflexit.logging/properties/logback.xml`).
- 16 bundles carry **committed `lib/*.jar`** on `Bundle-ClassPath` (e.g. `de.enflexit.logging`, `de.enflexit.awb.ws.swagger2x`). To bump one: run the bundle's `xPullMavenDeps/pom.xml` (`mvn dependency:copy-dependencies`), then hand-update MANIFEST `Bundle-ClassPath` + `Export-Package` **and** `build.properties` `bin.includes`.

## Don't trust `docs/`

`docs/gitBook/**` is stale: it still names `org.agentgui.core`, `agentgui.core.Application`, `agentgui.core.config.GlobalInfo`, and product id `org.agentgui.core.product`. Current names are `de.enflexit.awb.core.*` / `de.enflexit.awb.core.product`. Trust source and poms. `README.md` is accurate.
Legacy `agentgui.*` packages do genuinely survive in `bundles/core/de.enflexit.awb.baseOntology/src/agentgui` and in some service interface names (`agentgui.core.classLoadService.ClassLoadService`, `agentgui.core.environment.EnvironmentTypeService`).

## Pre-existing defects — do not "fix" as drive-by

8 DS component descriptors have trailing garbage on the `<provide interface=.../>` line (`.../>ice"/>`), e.g. `bundles/web/de.enflexit.awb.ws.restapi/OSGI-INF/awbWebHandlerApiService.xml:5`. Harmless at runtime (parsed as a text node). Mention it; don't bundle the cleanup into unrelated work.

## Workflow

Edit files only. Do not commit, amend, or push — the human handles git. Stay on the current branch.
