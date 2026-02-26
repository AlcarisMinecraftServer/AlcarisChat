# Alcaris Network Plugin Template

A Gradle-based template for building hybrid Minecraft plugins supporting both Paper and Velocity platforms.
Target: Minecraft 1.21.8 / Java 21.

## Project Structure

```
AlcarisTemplate/
├─ .gitignore
├─ build.gradle
├─ gradle.properties
├─ gradlew
├─ gradlew.bat
├─ settings.gradle
│
├─ common/
│  └─ src/main/java/net/alcaris/plugin/template/SharedUtil.java
│
├─ paper/
│  ├─ src/main/java/net/alcaris/plugin/template/PaperMain.java
│  └─ src/main/resources/plugin.yml
│
└─ velocity/
   ├─ src/main/java/net/alcaris/plugin/template/VelocityMain.java
   └─ src/main/resources/velocity-plugin.json
```

## Modules

| Module   | Description                                       |
| -------- | ------------------------------------------------- |
| common   | Shared logic and utilities used by both platforms |
| paper    | Paper-specific implementation                     |
| velocity | Velocity-specific implementation                  |

## Requirements

* Java 21 (recommended for Paper 1.21)
* Gradle 8.10 or newer

## Build

Execute the following command in the project root:

```bash
./gradlew clean build
```

Output JAR files will be located in the `target/` directory:

```
target/
├─ AlcarisTemplate-paper.jar
└─ AlcarisTemplate-velocity.jar
```

## Run

### Paper

Place `AlcarisTemplate-paper.jar` in your server’s `plugins/` folder.

Example output:

```
[PaperMain] Paper plugin enabled!
[Common] Hello from SharedUtil! Running on Paper
```

### Velocity

Place `AlcarisTemplate-velocity.jar` in your proxy’s `plugins/` folder.

Example output:

```
[Velocity] Velocity plugin enabled!
[Common] Hello from SharedUtil! Running on Velocity
```

## License
This project is licensed under the MIT License . see [LICENSE](LICENSE).