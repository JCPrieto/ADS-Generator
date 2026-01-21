# Repository Guidelines

## Project Structure & Module Organization

- `src/main/java` contains production Java code organized by package: `gui` (UI flows), `arbol` (tree/domain logic),
  `data` (project data), and `auxiliar` (helpers).
- `src/main/resources` holds non-code assets (e.g., `ADS2.png`).
- `src/test/java` is the test root; it is currently empty.
- `pom.xml` defines the Maven build and Java 21 toolchain.

## Build, Test, and Development Commands

- `mvn -q clean package` builds the project and produces the JAR in `target/`.
- `mvn -q test` runs the test suite (no tests are present yet).
- `mvn -q exec:java -Dexec.mainClass=gui.Principal` runs the main UI class (requires `exec-maven-plugin` if not already
  configured).
- `mvn -q -Pjpackage-linux clean package` creates the Linux app image in `target/dist/`.
- `mvn -q -Pjpackage-windows clean package` creates the Windows app image in `target/dist/`.
- `mvn -q -Pjpackage-mac clean package` creates the macOS app image in `target/dist/`.
    - Note: packages must be built on their target OS (no cross-builds).

## Coding Style & Naming Conventions

- Use 4-space indentation for Java; keep braces on the same line.
- Package names are lowercase (`gui`, `arbol`, `auxiliar`); class names are PascalCase (e.g., `NuevoProyecto`).
- Prefer descriptive Spanish identifiers when extending existing modules to match current naming.

## Testing Guidelines

- Place unit tests under `src/test/java` mirroring the package structure.
- Use the `*Test` suffix (e.g., `ArbolTest`) for test classes.
- Add tests for new behavior whenever introducing logic in `arbol` or `data`.

## Commit & Pull Request Guidelines

- Keep commit messages short, imperative, and specific (e.g., "Initialize project with Maven structure").
- PRs should include: a concise description, the scope of changes, and any UI screenshots if GUI behavior changes.

## Configuration & Assets

- The project targets Java 21; ensure your local JDK matches `pom.xml`.
- Assets belong in `src/main/resources` and should be referenced from code via the classpath.
